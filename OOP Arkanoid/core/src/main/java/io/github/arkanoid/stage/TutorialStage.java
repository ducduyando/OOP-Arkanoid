package io.github.arkanoid.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.core.GameLogic;
import io.github.arkanoid.core.Save;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.paddle.Paddle;
import io.github.arkanoid.ui.PauseMenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class TutorialStage implements GameStage {
    private Stage gdxStage;

    //Game entities
    private Paddle paddle;
    private Ball ball;
    private ArrayList<BrickActor> bricks;

    private GameLogic gameLogic;
    private Texture backgroundTexture;
    private Texture[] brickTextures;
    private Texture paddleImage;
    private Texture ballImage;

    // Pause functionality
    private boolean isPaused = false;
    private PauseMenu pauseMenu;
    private boolean pKeyPressed = false;
    private boolean quitRequested = false;

    // Save data for loading
    private Save.SaveData saveData;

    public TutorialStage() {
        this.saveData = null;
    }

    public TutorialStage(Save.SaveData saveData) {
        this.saveData = saveData;
    }

    private static class BrickActor extends Actor {
        private final TextureRegion textureRegion;
        private final Rectangle hitBox;
        private boolean isDestroyed = false;
        private final int textureIndex;

        public BrickActor(Texture texture, float x, float y, int textureIndex) {
            this.textureRegion = new TextureRegion(texture);
            this.hitBox = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
            this.textureIndex = textureIndex;
            setPosition(x, y);
            setSize(BRICK_WIDTH, BRICK_HEIGHT);
        }

        public Rectangle getHitBox() {
            return hitBox;
        }

        public boolean isDestroyed() {
            return isDestroyed;
        }

        public void destroy() {
            isDestroyed = true;
            remove();
        }

        public int getTextureIndex() {
            return textureIndex;
        }

        @Override
        public void act(float delta) {
            if (!isDestroyed) {
                hitBox.setPosition(getX(), getY());
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if (!isDestroyed) {
                batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            }
        }
    }

    private class BackgroundActor extends Actor {
        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(backgroundTexture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
    }

    @Override
    public void enter() {
        this.gdxStage = new Stage(new ScreenViewport() {});

        this.backgroundTexture = new Texture("brick/" + "background" + ".png");
        this.brickTextures = new Texture[4];
        brickTextures[0] = new Texture("brick/" + "red" + ".png");
        brickTextures[1] = new Texture("brick/" + "blue" + ".png");
        brickTextures[2] = new Texture("brick/" + "green" + ".png");
        brickTextures[3] = new Texture("brick/" + "orange" + ".png");
        paddleImage = new Texture("universal/" + "paddle" + ".png");
        ballImage = new Texture("ball/" + "normal" + ".png");

        if (saveData != null) {
            paddle = new Paddle(paddleImage, saveData.paddleX, saveData.paddleY);
            ball = new Ball(ballImage, saveData.ballX, saveData.ballY);
            ball.setVelocity(saveData.ballVelX, saveData.ballVelY);
            ball.setLaunched(saveData.ballLaunched);
        } else {
            paddle = new Paddle(paddleImage, PADDLE_INITIAL_X, PADDLE_INITIAL_Y);
            ball = new Ball(ballImage, 0, 0);
        }
        bricks = new ArrayList<>();

        this.gameLogic = new GameLogic(paddle, null);
        pauseMenu = new PauseMenu();

        gdxStage.addActor(new BackgroundActor());
        gdxStage.addActor(paddle);
        gdxStage.addActor(ball);

        createBricks();
    }

    private void createBricks() {
        if (saveData != null && saveData.brickPositions != null && !saveData.brickPositions.isEmpty()) {
            // Load bricks from save data
            for (Save.BrickPosition pos : saveData.brickPositions) {
                BrickActor brick = new BrickActor(brickTextures[pos.textureIndex], pos.x, pos.y, pos.textureIndex);
                bricks.add(brick);
                gdxStage.addActor(brick);
            }
        } else {
            // Create default brick layout
            for (int i = 0; i < BRICK_ROWS; i++) {
                for (int j = 0; j < BRICK_COLS; j++) {
                    float x = LEFT_BOUNDARY + j * BRICK_WIDTH;
                    float y = SCREEN_HEIGHT - BRICK_HEIGHT - i * BRICK_HEIGHT;
                    int textureIndex = new Random().nextInt(brickTextures.length);
                    Texture brickTexture = brickTextures[textureIndex];
                    BrickActor brick = new BrickActor(brickTexture, x, y, textureIndex);

                    bricks.add(brick);
                    gdxStage.addActor(brick);
                }
            }
        }
    }

    @Override
    public void update(float delta) {
        // Handle pause input
        handlePauseInput();

        if (isPaused) {
            pauseMenu.act(delta);

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

            return; // Don't update game logic when paused
        }

        gameLogic.launch(ball);
        gameLogic.paddleCollision(ball);
        gameLogic.boundaryCollision(ball, delta, SCREEN_HEIGHT);
        checkBrickCollisions();

        gdxStage.act(delta);
    }

    private void handlePauseInput() {
        boolean pKeyCurrentlyPressed = Gdx.input.isKeyPressed(Input.Keys.P);

        if (pKeyCurrentlyPressed && !pKeyPressed) {
            isPaused = !isPaused;

            if (isPaused) {
                gdxStage.addActor(pauseMenu);
            } else {
                pauseMenu.remove();
            }
        }

        pKeyPressed = pKeyCurrentlyPressed;
    }

    private void checkBrickCollisions() {
        Rectangle ballRect = ball.getHitBox();
        Iterator<BrickActor> iterator = bricks.iterator();

        while (iterator.hasNext()) {
            BrickActor brick = iterator.next();
            if (!brick.isDestroyed() && ballRect.overlaps(brick.getHitBox())) {
                Rectangle brickRect = brick.getHitBox();

                float ballCenterX = ballRect.x + ballRect.width / 2f;
                float ballCenterY = ballRect.y + ballRect.height / 2f;
                float brickCenterX = brickRect.x + brickRect.width / 2f;
                float brickCenterY = brickRect.y + brickRect.height / 2f;

                float overlapX = (ballRect.width / 2f + brickRect.width / 2f) - Math.abs(ballCenterX - brickCenterX);
                float overlapY = (ballRect.height / 2f + brickRect.height / 2f) - Math.abs(ballCenterY - brickCenterY);

                if (overlapX < overlapY) {
                    if (ballCenterX < brickCenterX) {
                        ball.setX(brickRect.x - ballRect.width);
                    } else {
                        ball.setX(brickRect.x + brickRect.width);
                    }
                    ball.setVelocity(-ball.getVelocity().x, ball.getVelocity().y);
                } else {
                    if (ballCenterY < brickCenterY) {
                        ball.setY(brickRect.y - ballRect.height);
                    } else {
                        ball.setY(brickRect.y + brickRect.height);
                    }
                    ball.setVelocity(ball.getVelocity().x, -ball.getVelocity().y);
                }

                brick.destroy();
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public void exit() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        if (paddleImage != null) {
            paddleImage.dispose();
        }
        if (ballImage != null) {
            ballImage.dispose();
        }
        if (brickTextures != null) {
            for (Texture tex : brickTextures) {
                if (tex != null) {
                    tex.dispose();
                }
            }
        }
        if (pauseMenu != null) {
            pauseMenu.dispose();
        }
        if (gdxStage != null) {
            gdxStage.dispose();
        }
    }


    @Override
    public Stage getGdxStage() {
        return this.gdxStage;
    }

    @Override
    public boolean isFinished() {
        return bricks.isEmpty();
    }

    public boolean isQuitRequested() {
        return quitRequested;
    }

    private void saveGame() {
        List<Save.BrickPosition> brickPositions = new ArrayList<>();
        for (BrickActor brick : bricks) {
            if (!brick.isDestroyed()) {
                brickPositions.add(new Save.BrickPosition(
                    brick.getX(), brick.getY(), brick.getTextureIndex()
                ));
            }
        }

     Save.saveGameWithBrickPositions(
            0,
            0,
            0,
            brickPositions,
            paddle.getX(), paddle.getY(),
            ball.getX(), ball.getY(),
            ball.getVelocity().x, ball.getVelocity().y,
            ball.isLaunched(),
            0, 0
        );
    }
}
