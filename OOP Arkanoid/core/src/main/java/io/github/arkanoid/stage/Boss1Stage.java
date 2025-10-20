package io.github.arkanoid.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.boss1.Boss1;
import io.github.arkanoid.core.GameLogic;
import io.github.arkanoid.core.Save;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.paddle.Paddle;
import io.github.arkanoid.ui.HealthBar;
import io.github.arkanoid.ui.ParallaxBackground;
import io.github.arkanoid.ui.PauseMenu;

import static io.github.arkanoid.core.Constants.*;

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

    public Boss1Stage() {
        this.saveData = null;
    }

    public Boss1Stage(Save.SaveData saveData) {
        this.saveData = saveData;
    }

    @Override
    public void enter() {
        stage = new Stage(new ScreenViewport());

        this.paddleImage = new Texture("universal/" + "paddle" + ".png");
        this.ballImage = new Texture("ball/" + "normal" + ".png");
        this.bossHealthBarImage = new Texture("universal/" + "health_bar" + ".png");
        this.bgTextures = new Texture[5];
        for (int i = 0; i < 5; i++) {
            bgTextures[i] = new Texture("background/" + "layer" + i + ".png");
        }

        // Create entities with saved positions if available
        if (saveData != null) {
            paddle = new Paddle(paddleImage, saveData.paddleX, saveData.paddleY);
            paddle.setState(saveData.paddleState); // Restore paddle state
            
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
        parallaxBackground = new ParallaxBackground(bgTextures, new float[]{0f, 50f, 40f, 30f, 20f});

        gameLogic = new GameLogic(paddle, boss1);
        pauseMenu = new PauseMenu();

        stage.addActor(parallaxBackground);
        stage.addActor(bossHealthBar);
        stage.addActor(paddle);
        stage.addActor(ball);
        stage.addActor(boss1);
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
                        pauseMenu.resetChoice();
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

            if (boss1.isReadyToDeath() && !bossDefeated) {
                bossDefeated = true;
                isCompleted = true;
            }
        }

        stage.act(delta);
    }

    private void handlePauseInput() {
        boolean pKeyCurrentlyPressed = Gdx.input.isKeyPressed(Input.Keys.P);

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

    private void saveGame() {
        io.github.arkanoid.core.Save.saveGame(
            1, // Boss1 stage
            boss1.getHp(), // Boss HP
            paddle.getState(), // Paddle state - FIX: save actual paddle state
            0, // No bricks in boss stage
            paddle.getX(), paddle.getY(),
            ball.getX(), ball.getY(),
            ball.getVelocity().x, ball.getVelocity().y,
            ball.isLaunched(),
            boss1.getX(), boss1.getY()
        );
    }
}
