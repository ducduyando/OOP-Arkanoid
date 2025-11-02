package io.github.arkanoid.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.boss3.Boss3;
import io.github.arkanoid.core.GameLogic;
import io.github.arkanoid.core.InputManager;
import io.github.arkanoid.core.ProjectileSaveManager;
import io.github.arkanoid.core.Save;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.paddle.*;
import io.github.arkanoid.ui.HealthBar;
import io.github.arkanoid.ui.ParallaxBackground;
import io.github.arkanoid.ui.PauseMenu;
import io.github.arkanoid.ui.SkillIcon;

import static io.github.arkanoid.core.Constants.*;

public class Boss3Stage implements GameStage {

    private Stage stage;
    private GameLogic gameLogic;

    private Paddle paddle;
    private Ball ball;
    private Boss3 boss3;
    private HealthBar bossHealthBar;
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
            boss3 = new Boss3(3, saveData.bossX, saveData.bossY, 100);
            boss3.setHp(saveData.bossHP); // Set current HP from save data

            // Sync skill selection

            if (saveData.isSkill1ASelected) {
                paddleSkill1A = paddle.getSkill1A();
            } else {
                // Use the skill1B object from paddle
                paddleSkill1B = paddle.getSkill1B();
            }
        } else {
            paddle = new Paddle(paddleImage, PADDLE_INITIAL_X, PADDLE_INITIAL_Y);
            ball = new Ball(ballImage, 0, 0);
            boss3 = new Boss3(3, BOSS3_INITIAL_X, BOSS3_INITIAL_Y, 100);

            // Initialize paddle skills - always initialize both for UI purposes
            boolean isSkill1ASelected = paddle.isSkill1ASelected();
            paddle.initializeSkills(isSkill1ASelected, 0f, 0f);

            if (isSkill2ASelected) {
                paddleSkill2A = new PaddleSkill2A(paddle);
            } else {
                // Use the skill1B object from paddle
                paddleSkill2B = new PaddleSkill2B(paddle);
            }
        }
        bossHealthBar = new HealthBar(bossHealthBarImage, boss3);
        parallaxBackground = new ParallaxBackground(bgTextures, new float[]{0f, 10f, 20f, 30f, 0f, 40f}, true);

        gameLogic = new GameLogic(paddle, boss3);
        pauseMenu = new PauseMenu();

        stage.addActor(parallaxBackground);
        stage.addActor(bossHealthBar);
        stage.addActor(paddle);
        stage.addActor(ball);
        stage.addActor(boss3);

        //stage.addActor(boss3.getBoss3LeftHand());
        stage.addActor(boss3.getBoss3RightHand());

        Texture skillIconJTexture = new Texture("SkillButton/" + "j" + ".png");
        skillIconJ = new SkillIcon(paddle, skillIconJTexture, "J", 20, 20);
        stage.addActor(skillIconJ);

        Texture skillIconKTexture = new Texture("SkillButton/" + "k" + ".png");
        skillIconK = new SkillIcon(paddle, skillIconKTexture, "K", 740, 20);
        stage.addActor(skillIconK);
    }

    @Override
    public void update(float delta) {

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
            gameLogic.bossCollision(ball);
            gameLogic.skillCollision(stage);

            if (paddleSkill1A != null) {
                InputManager inputManager = InputManager.getInstance();
                if (inputManager.isActionJustPressed(InputManager.ACTION_SKILL_1)
                    && paddleSkill1A.isSkill1AReady()
                    && !paddleSkill1A.isLaunched()) {

                    paddleSkill1A.enter(paddle);
                    paddleSkill1A.setLaunched(true);
                    paddleSkill1A.setVelocity(0, BALL_VELOCITY.y);
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
            if (paddleSkill2A != null) {
                paddleSkill2A.update(paddle, delta);
            }
            else if (paddleSkill2B != null) {
                paddleSkill2B.update(paddle, delta);
            }

            if (boss3.isReadyToDeath() && !bossDefeated) {
                bossDefeated = true;
                isCompleted = true;
            }
        }

        stage.act(delta);

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
            3, // Boss2 stage
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
            paddle.getSkill1BCooldownTimer()
        );
    }
}
