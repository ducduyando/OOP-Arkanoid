package io.github.arkanoid.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.boss3.Boss3;
import io.github.arkanoid.core.*;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.entities.FinalBoss;
import io.github.arkanoid.paddle.*;
import io.github.arkanoid.ui.*;
import io.github.arkanoid.core.GameManager;
import io.github.arkanoid.core.Save;

import static io.github.arkanoid.core.Constants.*;

public class Boss3Stage implements GameStage {

    private Stage stage;
    private GameLogic gameLogic;

    private Paddle paddle;
    private Ball ball;
    private Boss3 boss3;
    private FinalBossHealthBar bossHealthBar;
    private ParallaxBackground parallaxBackground;

    private Texture paddleImage;
    private Texture ballImage;
    private Texture bossHealthBarImage;
    private Texture[] bgTextures;

    private SkillIcon skillIconJ;
    private SkillIcon skillIconK;

    private PaddleSkill1A paddleSkill1A;
    private PaddleSkill1B paddleSkill1B;

    private PaddleSkill2A paddleSkill2A;
    private PaddleSkill2B paddleSkill2B;

    private boolean isSkill2ASelected;

    // Pause functionality
    private boolean isPaused = false;
    private PauseMenu pauseMenu;
    private boolean pKeyPressed = false;

    // Boss state
    private boolean bossDefeated = false;
    private boolean quitRequested = false;
    private boolean isCompleted = false;
    private boolean gameOver = false;


    // Save data for loading
    private Save.SaveData saveData;

    public Boss3Stage() {
        this.saveData = null;
    }

    public Boss3Stage(Save.SaveData saveData) {
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
            bgTextures[i] = new Texture("Background/" + "Stage3/" + "layer" + i + ".png");
        }

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
            boss3 = new Boss3(saveData.bossX, saveData.bossY, 100, paddle);
            boss3.setHp(saveData.bossHP); // Set current HP from save data

            // Get skill2 selection from GameManager (since save doesn't include skill2 yet)
            isSkill2ASelected = GameManager.getInstance().hasSkill2A();

            // Initialize skill2
            paddle.initializeSkill2(isSkill2ASelected, 0f, 0f);

            // Sync skill1 selection
            if (saveData.isSkill1ASelected) {
                paddleSkill1A = paddle.getSkill1A();
                paddleSkill1B = null;

            } else {
                paddleSkill1B = paddle.getSkill1B();
                paddleSkill1A = null;

            }

            // Sync skill2 selection
            if (isSkill2ASelected) {
                paddleSkill2A = paddle.getSkill2A();
                paddleSkill2B = null;

            } else {
                paddleSkill2B = paddle.getSkill2B();
                paddleSkill2A = null;

            }
        } else {
            paddle = new Paddle(paddleImage, PADDLE_INITIAL_X, PADDLE_INITIAL_Y);
            ball = new Ball(ballImage, 0, 0);
            boss3 = new Boss3(BOSS3_INITIAL_X, BOSS3_INITIAL_Y, 100, paddle);

            // Get skill1 selection from GameManager (from Boss1 PowerUpMenu)
            boolean isSkill1ASelected = GameManager.getInstance().hasSkill1A();
            paddle.initializeSkills(isSkill1ASelected, 0f, 0f);

            // Get skill2 selection from GameManager (from Boss2 PowerUpMenu)
            isSkill2ASelected = GameManager.getInstance().hasSkill2A();


            // Remove debug defaulting to avoid forcing skill1A/2A unexpectedly

            // Initialize skill2 - always initialize both for UI purposes
            paddle.initializeSkill2(isSkill2ASelected, 0f, 0f);

            // Initialize skill1 references (from Boss1 selection)
            if (isSkill1ASelected) {
                paddleSkill1A = paddle.getSkill1A();
                paddleSkill1B = null;
            } else {
                paddleSkill1B = paddle.getSkill1B();
                paddleSkill1A = null;

            }

            // Initialize skill2 references (from Boss2 selection)
            if (isSkill2ASelected) {
                paddleSkill2A = paddle.getSkill2A();
                paddleSkill2B = null;

            } else {
                paddleSkill2B = paddle.getSkill2B();
                paddleSkill2A = null;

            }
        }
        bossHealthBar = new FinalBossHealthBar(bossHealthBarImage, boss3);
        parallaxBackground = new ParallaxBackground(bgTextures, new float[]{0f, 10f, 20f, 30f, 0f, 40f}, true);

        gameLogic = new GameLogic(paddle, boss3);
        pauseMenu = new PauseMenu();

        stage.addActor(parallaxBackground);
        stage.addActor(bossHealthBar);
        stage.addActor(paddle);
        stage.addActor(ball);
        stage.addActor(boss3);

        Texture skillIconJTexture = new Texture("SkillButton/" + "j" + ".png");
        skillIconJ = new SkillIcon(paddle, skillIconJTexture, "J", 20, 20);
        stage.addActor(skillIconJ);

        Texture skillIconKTexture = new Texture("SkillButton/" + "k" + ".png");
        skillIconK = new SkillIcon(paddle, skillIconKTexture, "K", 740, 20);
        stage.addActor(skillIconK);
    }

    @Override
    public void update(float delta) {
        // Update InputManager để xử lý input states
        InputManager.getInstance().update();

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

        if (paddle.isGameOver() ) {
            gameOver = true;
            isCompleted = true;
            return;
        }

        if (!isCompleted && !bossDefeated) {
            gameLogic.launch(ball);
            gameLogic.paddleCollision(ball);
            gameLogic.boundaryCollision(ball, delta, UP_BOUNDARY);
            gameLogic.finalBossCollision(ball);
            gameLogic.skillCollision(stage, ball);

            if (paddleSkill1A != null) {
                InputManager inputManager = InputManager.getInstance();
                if (inputManager.isActionJustPressed(InputManager.ACTION_SKILL_1)
                    && paddleSkill1A.isSkill1AReady()

                    && paddleSkill1A.getCurrentPhase() == PaddleSkill1A.Phase.DONE) {

                    paddleSkill1A.enter(paddle);
                    paddleSkill1A.setLaunched(true);
                    paddleSkill1A.setVelocity(0, BALL_VELOCITY.y);


                    paddleSkill1A.setCurrentPhase(PaddleSkill1A.Phase.FIRING); // Cần thêm setter này vào PaddleSkill1A
                }
                if (paddleSkill1A.isLaunched()) {

                    gameLogic.paddleCollision(paddleSkill1A);
                    gameLogic.boundaryCollision(paddleSkill1A, delta, UP_BOUNDARY);
                    gameLogic.bossCollision(paddleSkill1A);
                } else {

                    gameLogic.launch(paddleSkill1A);
                }
                paddleSkill1A.update(paddle, delta);
            }

            else if (paddleSkill1B != null) {
                // Always update skill1B for cooldown timer
                paddleSkill1B.update(paddle, delta);

                InputManager inputManager = InputManager.getInstance();
                if (paddleSkill1B.isDone() && inputManager.isActionJustPressed(InputManager.ACTION_SKILL_1)
                    && paddleSkill1B.isSkill1BReady()) {

                    paddleSkill1B.enter(paddle);
                }

                if (paddleSkill1B.isFiring()) {
                    gameLogic.paddleLaserCollision(paddleSkill1B);
                }
            }
            // Handle Skill2A (Bee Bullet) - K key
            if (paddleSkill2A != null) {
                // Always update skill2A for cooldown timer
                paddleSkill2A.update(paddle, delta);

                InputManager inputManager = InputManager.getInstance();
                if (inputManager.isActionJustPressed(InputManager.ACTION_SKILL_2)
                    && paddleSkill2A.isSkill2AReady()) {

                    paddleSkill2A.fire(paddle);
                }
            }
            // Handle Skill2B (Honey Shield) - K key
            else if (paddleSkill2B != null) {
                // Always update skill2B for cooldown timer
                paddleSkill2B.update(paddle, delta);

                InputManager inputManager = InputManager.getInstance();
                if (paddleSkill2B.isDone() && inputManager.isActionJustPressed(InputManager.ACTION_SKILL_2)
                    && paddleSkill2B.isSkill2BReady()) {

                    paddleSkill2B.activate(paddle);
                }
            }

            if (boss3.isReadyToDeath() && !bossDefeated) {
                bossDefeated = true;
                isCompleted = true;
                saveRank(3);
            }
        }

        stage.act(delta);

    }

    private void saveRank(int stageNumber) {
        GameManager gameManager = GameManager.getInstance();
        String playerName = gameManager.getCurrentPlayerName();
        float totalGameTime = Save.getTotalGameTime();

        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
            gameManager.setCurrentPlayerName(playerName);
        }

        if (stageNumber == 3) {
            Save.addRankEntry(playerName, totalGameTime, stageNumber);
        }

        Save.stopGame();
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
        if (boss3 != null) {
            boss3.dispose();
        }
        if (pauseMenu != null) {
            pauseMenu.dispose();
        }
        // SkillIcon texture will be disposed automatically by LibGDX
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
        return gameOver;
    }

    private void saveGame() {
        // Collect all projectiles using ProjectileSaveManager
        ProjectileSaveManager.ProjectileData projectileData =
            ProjectileSaveManager.collectProjectiles(stage);
        Save.saveGameWithProjectiles(
            3, // Boss3 stage
            boss3.getHp(),
            paddle.getState(),
            projectileData,
            paddle.getX(), paddle.getY(),
            ball.getX(), ball.getY(),
            ball.getVelocity().x, ball.getVelocity().y,
            ball.isLaunched(),
            boss3.getX(), boss3.getY(),
            paddle.isSkill1ASelected(),
            paddle.getSkill1ACooldownTimer(),
            paddle.getSkill1BCooldownTimer(),
            paddle.isSkill2ASelected(),
            paddle.getSkill2ACooldownTimer(),
            paddleSkill2A.isSkill2AReady() ,
            paddle.getSkill2BCooldownTimer(),
            paddleSkill2B.isSkill2BReady()
        );
    }
}
