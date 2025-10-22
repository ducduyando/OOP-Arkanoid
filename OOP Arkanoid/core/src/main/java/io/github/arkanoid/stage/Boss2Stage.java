package io.github.arkanoid.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.boss2.Boss2;
import io.github.arkanoid.core.GameLogic;
import io.github.arkanoid.core.Save;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.paddle.Paddle;
import io.github.arkanoid.ui.HealthBar;
import io.github.arkanoid.ui.ParallaxBackground;
import io.github.arkanoid.ui.PauseMenu;

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

    public Boss2Stage() {
        this.saveData = null;
    }

    public Boss2Stage(Save.SaveData saveData) {
        this.saveData = saveData;
    }

    @Override
    public void enter() {
        stage = new Stage(new ScreenViewport());

        this.paddleImage = new Texture("universal/" + "paddle" + ".png");
        this.ballImage = new Texture("ball/" + "normal" + ".png");
        this.bossHealthBarImage = new Texture("universal/" + "health_bar" + ".png");
        this.bgTextures = new Texture[6];
        for (int i = 0; i < 6; i++) {
            bgTextures[i] = new Texture("background/" + "stage2/" + "layer" + i + ".png");
        }

        // Create entities with saved positions if available
        if (saveData != null) {
            paddle = new Paddle(paddleImage, saveData.paddleX, saveData.paddleY);
            paddle.setState(saveData.paddleState); // Restore paddle state

            ball = new Ball(ballImage, saveData.ballX, saveData.ballY);
            ball.setVelocity(saveData.ballVelX, saveData.ballVelY);
            ball.setLaunched(saveData.ballLaunched);

            // Create boss with full HP first, then set current HP
            boss2 = new Boss2(2, saveData.bossX, saveData.bossY, 150);
            boss2.setHp(saveData.bossHP); // Set current HP from save data
        } else {
            paddle = new Paddle(paddleImage, PADDLE_INITIAL_X, PADDLE_INITIAL_Y);
            ball = new Ball(ballImage, 0, 0);
            boss2 = new Boss2(1, BOSS2_INITIAL_X, BOSS2_INITIAL_Y, 100);
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

        if (!isCompleted && !bossDefeated) {
            gameLogic.launch(ball);
            gameLogic.paddleCollision(ball);
            gameLogic.boundaryCollision(ball, delta, UP_BOUNDARY);
            gameLogic.bossCollision(ball);
            gameLogic.skillCollision(stage);

            if (boss2.isReadyToDeath() && !bossDefeated) {
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
        if (boss2 != null) {
            boss2.dispose();
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

    private void saveGame() {
        Save.saveGame(
            1, // Boss1 stage
            boss2.getHp(), // Boss HP
            paddle.getState(), // Paddle state - FIX: save actual paddle state
            0, // No bricks in boss stage
            paddle.getX(), paddle.getY(),
            ball.getX(), ball.getY(),
            ball.getVelocity().x, ball.getVelocity().y,
            ball.isLaunched(),
            boss2.getX(), boss2.getY()
        );
    }
}
