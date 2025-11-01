package io.github.arkanoid.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.boss2.BeeEnemy;
import io.github.arkanoid.boss2.Boss2;
import io.github.arkanoid.core.GameLogic;
import io.github.arkanoid.core.GameManager;
import io.github.arkanoid.core.InputManager;
import io.github.arkanoid.core.ProjectileSaveManager;
import io.github.arkanoid.core.Save;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.paddle.Paddle;
import io.github.arkanoid.paddle.PaddleSkill1A;
import io.github.arkanoid.paddle.PaddleSkill1B;
import io.github.arkanoid.ui.HealthBar;
import io.github.arkanoid.ui.ParallaxBackground;
import io.github.arkanoid.ui.PauseMenu;
import io.github.arkanoid.ui.SkillIcon;

import static io.github.arkanoid.core.Constants.*;

public class Boss2Stage implements GameStage {
    private Stage stage;
    private GameLogic gameLogic;

    private Paddle paddle;
    private Ball ball;
    private Boss2 boss2;
    private HealthBar bossHealthBar;
    private ParallaxBackground parallaxBackground;

    private Texture paddleImage;
    private Texture ballImage;
    private Texture bossHealthBarImage;
    private Texture[] bgTextures;


    private SkillIcon skillIconJ;

    private PaddleSkill1A paddleSkill1A1;
    private PaddleSkill1A paddleSkill1A2;
    private PaddleSkill1B paddleSkill1B;

    private float paddleSkill1A2Timer  = 0;
    private final double PADDLE_SKILL_1A2_TIMER = 0.5;
    private boolean keepJBefore = false;

    private boolean isSkill1ASelected;

    public void setSkill1ASelected(boolean skill1ASelected) {
        isSkill1ASelected = skill1ASelected;
    }

    // Pause functionality
    private boolean isPaused = false;
    private PauseMenu pauseMenu;
    private boolean pKeyPressed = false;

    // Boss state
    private boolean bossDefeated = false;
    private boolean quitRequested = false;
    private boolean isCompleted = false;
    private boolean gameOver = false;

    // time
    private float stageTime = 0f;


    // Save data for loading
    private Save.SaveData saveData;

    public Boss2Stage() {
        this.saveData = null;
        
        // Update GameManager state
        GameManager gameManager = GameManager.getInstance();
        gameManager.setCurrentStage(2);
        gameManager.setCurrentPlayerName(Save.loadPlayerName());
        System.out.println("Boss2Stage: Initialized with GameManager");
    }

    public Boss2Stage(Save.SaveData saveData) {
        this.saveData = saveData;
    }

    @Override
    public void enter() {
        stage = new Stage(new ScreenViewport());

        this.paddleImage = new Texture("Universal/" + "paddle" + ".png");
        this.ballImage = new Texture("Ball/" + "normal" + ".png");
        this.bossHealthBarImage = new Texture("Universal/" + "health_bar" + ".png");
        this.bgTextures = new Texture[6];
        for (int i = 0; i < 6; i++) {
            bgTextures[i] = new Texture("Background/" + "Stage2/" + "layer" + i + ".png");
        }

        // Create entities with saved positions if available
        if (saveData != null) {
            paddle = new Paddle(paddleImage, saveData.paddleX, saveData.paddleY);
            paddle.setState(saveData.paddleState); // Restore paddle state

            // Restore paddle skills
            paddle.initializeSkills(
                saveData.isSkill1ASelected,
                saveData.skill1ACooldownTimer,
                saveData.skill1BCooldownTimer
            );

            ball = new Ball(ballImage, saveData.ballX, saveData.ballY);
            ball.setVelocity(saveData.ballVelX, saveData.ballVelY);
            ball.setLaunched(saveData.ballLaunched);

            // Create boss with full HP first, then set current HP
            boss2 = new Boss2(2, saveData.bossX, saveData.bossY, 100);
            boss2.setHp(saveData.bossHP); // Set current HP from save data

            // Sync skill selection
            isSkill1ASelected = saveData.isSkill1ASelected;
            if (saveData.isSkill1ASelected) {
                paddleSkill1A1 = new PaddleSkill1A(paddle);
                paddleSkill1A2 = new PaddleSkill1A(paddle);
            } else {
                // Use the skill1B object from paddle
                paddleSkill1B = paddle.getSkill1B();
            }
        } else {
            paddle = new Paddle(paddleImage, PADDLE_INITIAL_X, PADDLE_INITIAL_Y);
            ball = new Ball(ballImage, 0, 0);
            boss2 = new Boss2(2, BOSS2_INITIAL_X, BOSS2_INITIAL_Y, 100);

            // Initialize paddle skills - always initialize both for UI purposes
            paddle.initializeSkills(isSkill1ASelected, 0f, 0f);

            if (isSkill1ASelected) {
                paddleSkill1A1 = new PaddleSkill1A(paddle);
                paddleSkill1A2 = new PaddleSkill1A(paddle);
            } else {
                // Use the skill1B object from paddle
                paddleSkill1B = paddle.getSkill1B();
            }
        }
        bossHealthBar = new HealthBar(bossHealthBarImage, boss2);
        parallaxBackground = new ParallaxBackground(bgTextures, new float[]{0f, 10f, 20f, 30f, 0f, 40f}, true);

        gameLogic = new GameLogic(paddle, boss2);
        pauseMenu = new PauseMenu();

        stage.addActor(parallaxBackground);
        stage.addActor(bossHealthBar);
        stage.addActor(paddle);
        stage.addActor(ball);
        stage.addActor(boss2);

        Texture skillIconTexture = new Texture("SkillButton/" + "j" + ".png");
        skillIconJ = new SkillIcon(paddle, skillIconTexture, "J", 20, 20);
        stage.addActor(skillIconJ);


        if (saveData != null && saveData.beePositions != null) {
            for (Save.BeePosition beePos : saveData.beePositions) {
                io.github.arkanoid.boss2.BeeEnemy bee = new io.github.arkanoid.boss2.BeeEnemy(
                    new Texture("Boss2/" + "skill" + "1" +".png"), beePos.x, beePos.y
                );
                stage.addActor(bee);
            }
        }
    }

    @Override
    public void update(float delta) {
        // Update InputManager
        InputManager.getInstance().update();
        
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
        if (paddle.isGameOver() ) {
            gameOver = true;
            isCompleted = true;
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

            if (paddleSkill1A1 != null && paddleSkill1A2 != null) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.J)
                    && paddleSkill1A1.isSkill1AReady()
                    && !paddleSkill1A1.isLaunched()
                    && paddleSkill1A2.isSkill1AReady()
                    && !paddleSkill1A2.isLaunched()) {

                    paddleSkill1A1.enter(paddle);
                    paddleSkill1A1.setLaunched(true);
                    paddleSkill1A1.setVelocity(0, BALL_VELOCITY.y);

                    keepJBefore = true;

                }

                if (keepJBefore) {
                    paddleSkill1A2Timer += delta;
                    if (paddleSkill1A2Timer >= PADDLE_SKILL_1A2_TIMER) {
                        paddleSkill1A2Timer = 0f;
                        paddleSkill1A2.enter(paddle);
                        paddleSkill1A2.setLaunched(true);
                        paddleSkill1A2.setVelocity(0, BALL_VELOCITY.y);
                        keepJBefore = false;
                    }
                }
                if (paddleSkill1A1.isLaunched()) {

                    gameLogic.paddleCollision(paddleSkill1A1);
                    gameLogic.boundaryCollision(paddleSkill1A1, delta, UP_BOUNDARY);
                    gameLogic.bossCollision(paddleSkill1A1);
                } else {

                    gameLogic.launch(paddleSkill1A1);
                }
                if (paddleSkill1A2.isLaunched()) {

                    gameLogic.paddleCollision(paddleSkill1A2);
                    gameLogic.boundaryCollision(paddleSkill1A2, delta, UP_BOUNDARY);
                    gameLogic.bossCollision(paddleSkill1A2);
                } else {

                    gameLogic.launch(paddleSkill1A2);
                }
                paddleSkill1A1.update(paddle, delta);
                paddleSkill1A2.update(paddle, delta);
            }

            else if (paddleSkill1B != null) {
                // Always update skill1B for cooldown timer
                paddleSkill1B.update(paddle, delta);

                if (paddleSkill1B.isDone() && Gdx.input.isKeyJustPressed(Input.Keys.J)
                    && paddleSkill1B.isSkill1BReady()) {

                    paddleSkill1B.enter(paddle);
                }

                if (paddleSkill1B.isFiring()) {
                    gameLogic.paddleLaserCollision(paddleSkill1B);
                }
            }

            if (boss2.isReadyToDeath() && !bossDefeated) {
                bossDefeated = true;
                isCompleted = true;
                saveRank(2);

            }
        }
        for (Actor actor : stage.getActors()) {
            if (actor instanceof BeeEnemy bee) {
                if (ball.getHitBox().overlaps(bee.getHitBox())) {
                    bee.remove();
                }
            }
        }

        stage.act(delta);
    }

    // luu thu muc rank
    private void saveRank(int stageNumber) {
        GameManager gameManager = GameManager.getInstance();
        String playerName = gameManager.getCurrentPlayerName();
        float totalGameTime = Save.getTotalGameTime();

        System.out.println("Boss2Stage: saveRank() called via GameManager");
        System.out.println("Boss2Stage: Player name: '" + playerName + "'");
        System.out.println("Boss2Stage: Stage number: " + stageNumber);
        System.out.println("Boss2Stage: Total game time: " + totalGameTime);

        // Ensure we have a valid name
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
            gameManager.setCurrentPlayerName(playerName);
        }

        // Use total game time instead of stage time
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
        if (boss2 != null) {
            boss2.dispose();
        }
        if (pauseMenu != null) {
            pauseMenu.dispose();
        }
        // SkillIcon texture will be disposed automatically by LibGDX
        if (stage != null) {
            stage.dispose();
        }
    }




    public void drawEffects(SpriteBatch batch) {
        // Draw pause menu if paused
        if (isPaused) {
            pauseMenu.draw(batch, 1f);
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
        return gameOver;
    }

    private void saveGame() {
        // Collect all projectiles using ProjectileSaveManager
        ProjectileSaveManager.ProjectileData projectileData =
            ProjectileSaveManager.collectProjectiles(stage);
        Save.saveGameWithProjectiles(
            2, // Boss2 stage
            boss2.getHp(),
            paddle.getState(),
            projectileData,
            paddle.getX(), paddle.getY(),
            ball.getX(), ball.getY(),
            ball.getVelocity().x, ball.getVelocity().y,
            ball.isLaunched(),
            boss2.getX(), boss2.getY(),
            paddle.isSkill1ASelected(),
            paddle.getSkill1ACooldownTimer(),
            paddle.getSkill1BCooldownTimer()
        );
    }
}
