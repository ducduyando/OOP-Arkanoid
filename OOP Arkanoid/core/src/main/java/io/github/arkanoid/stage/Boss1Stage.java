package io.github.arkanoid.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.boss1.Boss1;
import io.github.arkanoid.core.GameLogic;
import io.github.arkanoid.core.GameManager;
import io.github.arkanoid.core.ProjectileSaveManager;
import io.github.arkanoid.core.Save;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.paddle.Paddle;
import io.github.arkanoid.ui.HealthBar;
import io.github.arkanoid.ui.ParallaxBackground;
import io.github.arkanoid.ui.PauseMenu;

import static io.github.arkanoid.core.Constants.*;
import static io.github.arkanoid.core.ProjectileSaveManager.restoreProjectiles;

public class Boss1Stage implements GameStage {
    private Stage stage;
    private GameLogic gameLogic;

    private Paddle paddle;
    private Ball ball;
    private Boss1 boss1;
    private HealthBar bossHealthBar;
    private ParallaxBackground parallaxBackground;

    private Texture paddleImage;
    private Texture ballImage;
    private Texture bossHealthBarImage;
    private Texture[] bgTextures;

    // Pause functionality
    private boolean isPaused = false;
    private PauseMenu pauseMenu;
    private boolean pKeyPressed = false;

    // Boss state
    private boolean bossDefeated = false;
    private boolean quitRequested = false;
    private boolean isCompleted = false;

    // Save data for loading
    private Save.SaveData saveData;

// time
private float stageTime = 0f;

    public Boss1Stage() {
        this.saveData = null;
        // Reset game time when starting new game
        Save.resetGameTime();
        
        // Update GameManager state
        GameManager gameManager = GameManager.getInstance();
        gameManager.setCurrentStage(1);
        gameManager.setCurrentPlayerName(Save.loadPlayerName());
        System.out.println("Boss1Stage: Initialized with GameManager");
    }

    public Boss1Stage(Save.SaveData saveData) {
        this.saveData = saveData;
    }

    @Override
    public void enter() {
        stage = new Stage(new ScreenViewport());

        this.paddleImage = new Texture("Universal/" + "paddle" + ".png");
        this.ballImage = new Texture("Ball/" + "normal" + ".png");
        this.bossHealthBarImage = new Texture("Universal/" + "health_bar" + ".png");
        this.bgTextures = new Texture[5];
        for (int i = 0; i < 5; i++) {
            bgTextures[i] = new Texture("Background/" + "Stage1/" + "layer" + i + ".png");
        }

        // Create entities with saved positions if available
        if (saveData != null) {
            paddle = new Paddle(paddleImage, saveData.paddleX, saveData.paddleY);
            paddle.setState(saveData.paddleState); // Restore paddle state

            paddle.initializeSkills(
                saveData.isSkill1ASelected,
                saveData.skill1ACooldownTimer,
                saveData.skill1BCooldownTimer
            );
            ball = new Ball(ballImage, saveData.ballX, saveData.ballY);
            ball.setVelocity(saveData.ballVelX, saveData.ballVelY);
            ball.setLaunched(saveData.ballLaunched);

            // Create boss with full HP first, then set current HP
            boss1 = new Boss1(1, saveData.bossX, saveData.bossY, 100);
            boss1.setHp(saveData.bossHP); // Set current HP from save data
        } else {
            paddle = new Paddle(paddleImage, PADDLE_INITIAL_X, PADDLE_INITIAL_Y);
            ball = new Ball(ballImage, 0, 0);
            boss1 = new Boss1(1, BOSS1_INITIAL_X, BOSS1_INITIAL_Y, 100);
        }
        bossHealthBar = new HealthBar(bossHealthBarImage, boss1);
        parallaxBackground = new ParallaxBackground(bgTextures, new float[]{0f, 50f, 40f, 30f, 20f}, false);

        gameLogic = new GameLogic(paddle, boss1);
        pauseMenu = new PauseMenu();

        stage.addActor(parallaxBackground);
        stage.addActor(bossHealthBar);
        stage.addActor(paddle);
        stage.addActor(ball);
        stage.addActor(boss1);

        // Restore projectiles if loading from save
        if (saveData != null && saveData.projectileData != null) {
            restoreProjectiles(stage, saveData.projectileData);
        }
    }

    @Override
    public void update(float delta) {
        // Handle pause input
        handlePauseInput();

        if (isPaused) {
            pauseMenu.act(delta);

            // Handle pause menu actions
            if (pauseMenu.isOptionChosen()) {
                PauseMenu.Option selectedOption = pauseMenu.getOption();
                switch (selectedOption) {
                    case RESUME:
                        isPaused = false;
                        pauseMenu.remove();
                        pauseMenu.reset();
                        break;
                    case SAVE:
                        saveGame();
                        isPaused = false;
                        pauseMenu.remove();
                        pauseMenu.reset();
                        break;
                    case QUIT:
                        // Exit game
                        Gdx.app.exit();
                        break;
                }
            }

            return;
        }

        // Check game over condition
        if (paddle.isGameOver()) {
            isCompleted = true;
            // Don't save rank when dying

            return;
        }

        if (!isCompleted && !bossDefeated) {
            stageTime += delta;
            // Add time to global game time
            Save.addTime(delta);
            gameLogic.launch(ball);
            gameLogic.paddleCollision(ball);
            gameLogic.boundaryCollision(ball, delta, UP_BOUNDARY);
            gameLogic.bossCollision(ball);
            gameLogic.skillCollision(stage);

            if (boss1.isReadyToDeath() && !bossDefeated) {
                bossDefeated = true;
                isCompleted = true;
                // Save rank when winning Boss1
                saveRank(1);

            }
        }

        stage.act(delta);
    }
    // save bang rank
    private void saveRank(int stageNumber) {
        GameManager gameManager = GameManager.getInstance();
        String playerName = gameManager.getCurrentPlayerName();
        float totalGameTime = Save.getTotalGameTime();

        System.out.println("Boss1Stage: saveRank() called via GameManager");
        System.out.println("Boss1Stage: Player name: '" + playerName + "'");
        System.out.println("Boss1Stage: Stage number: " + stageNumber);
        System.out.println("Boss1Stage: Total game time: " + totalGameTime);

        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
            gameManager.setCurrentPlayerName(playerName);
        }

        Save.addRankEntry(playerName, totalGameTime, stageNumber);

        // Stop game time tracking
        Save.stopGame();
        
        // GameManager state updated
    }

    private void handlePauseInput() {
        boolean pKeyCurrentlyPressed = (Gdx.input.isKeyPressed(Input.Keys.P) || Gdx.input.isKeyPressed(Input.Keys.ESCAPE));

        if (pKeyCurrentlyPressed && !pKeyPressed) {
            isPaused = !isPaused;

            if (isPaused) {
                stage.addActor(pauseMenu);
            } else {
                pauseMenu.remove();
            }
        }

        pKeyPressed = pKeyCurrentlyPressed;
    }

    @Override
    public void exit() {
        if (paddleImage != null) {
            paddleImage.dispose();
        }
        if (ballImage != null) {
            ballImage.dispose();
        }
        if (bossHealthBarImage != null) {
            bossHealthBarImage.dispose();
        }
        if (parallaxBackground != null) {
            parallaxBackground.dispose();
        }
        if (boss1 != null) {
            boss1.dispose();
        }
        if (pauseMenu != null) {
            pauseMenu.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }



    @Override
    public Stage getGdxStage() {
        return this.stage;
    }

    @Override
    public boolean isFinished() {
        return isCompleted;
    }

    public boolean isQuitRequested() {
        return quitRequested;
    }

    public boolean isGameOver() {
        return paddle != null && paddle.isGameOver();
    }

    private void saveGame() {
        ProjectileSaveManager.ProjectileData projectileData =
            ProjectileSaveManager.collectProjectiles(stage);

        boolean isSkillASelected = paddle.isSkill1ASelected();
        float skill1ACooldownTimer = paddle.getSkill1ACooldownTimer();
        float skill1BCooldownTimer = paddle.getSkill1BCooldownTimer();
        Save.saveGameWithProjectiles(
            1, // Boss1 stage
            boss1.getHp(),
            paddle.getState(),
            projectileData,
            paddle.getX(), paddle.getY(),
            ball.getX(), ball.getY(),
            ball.getVelocity().x, ball.getVelocity().y,
            ball.isLaunched(),
            boss1.getX(), boss1.getY(),
            isSkillASelected,
            skill1ACooldownTimer,
            skill1BCooldownTimer
        );
    }
}
