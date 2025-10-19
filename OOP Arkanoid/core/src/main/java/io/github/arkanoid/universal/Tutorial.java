package io.github.arkanoid.universal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.paddle.Paddle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static io.github.arkanoid.universal.Constants.*;

public class Tutorial extends Stage {
    private final Texture paddleTexture;
    private final Texture ballTexture;

    private final Paddle paddle;
    private final Ball ball;
    private final ArrayList<BrickActor> bricks = new ArrayList<>();
    private final GameLogic gameLogic;
    private final Texture backgroundTexture = new Texture("brick/" + "background" + ".png");
    private boolean finished = false;
    private final Texture[] brickTextures = new Texture[4];

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

    public Tutorial(Paddle paddle, Ball ball) {
        super(new ScreenViewport());
        addActor(new BackgroundActor());

        brickTextures[0] = new Texture("brick/" + "red" + ".png");
        brickTextures[1] = new Texture("brick/" + "blue" + ".png");
        brickTextures[2] = new Texture("brick/" + "green" + ".png");
        brickTextures[3] = new Texture("brick/" + "orange" + ".png");

        paddleTexture = new Texture("universal/" + "paddle" + ".png");
        ballTexture = new Texture("ball/" + "normal" + ".png");

        this.paddle = paddle;
        this.ball = ball;

        paddle.setPosition((SCREEN_WIDTH - PADDLE_WIDTH) / 2f, 150);
        ball.setPosition((SCREEN_WIDTH - BALL_WIDTH) / 2f, PADDLE_HEIGHT);

        addActor(paddle);
        addActor(ball);

        gameLogic = new GameLogic(paddle, null);
        createBricks();
    }

    private void createBricks() {
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                float x = LEFT_BOUNDARY + j * BRICK_WIDTH;
                float y = SCREEN_HEIGHT - BRICK_HEIGHT - i * BRICK_HEIGHT;
                int textureIndex = new Random().nextInt(brickTextures.length);
                Texture brickTexture = brickTextures[textureIndex];
                BrickActor brick = new BrickActor(brickTexture, x, y, textureIndex);

                bricks.add(brick);
                addActor(brick);
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!finished) {
            gameLogic.launch(ball);
            gameLogic.paddleCollision(ball);
            gameLogic.boundaryCollision(ball, delta, SCREEN_HEIGHT);
            checkBrickCollisions();

            if (bricks.isEmpty()) {
                finished = true;
            }
        }
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
                    ball.velocityVector.x = -ball.velocityVector.x;
                } else {
                    if (ballCenterY < brickCenterY) {
                        ball.setY(brickRect.y - ballRect.height);
                    } else {
                        ball.setY(brickRect.y + brickRect.height);
                    }
                    ball.velocityVector.y = -ball.velocityVector.y;
                }

                brick.destroy();
                iterator.remove();
                break;
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public void resetBricksWithPositions(java.util.List<Save.BrickPosition> brickPositions) {
        // Xóa tất cả gạch hiện tại
        for (BrickActor brick : bricks) brick.remove();
        bricks.clear();

        if (brickPositions == null || brickPositions.isEmpty()) {
            finished = true;
            return;
        }

        finished = false;

        // Tạo lại gạch theo vị trí đã lưu
        for (Save.BrickPosition pos : brickPositions) {
            int textureIndex = Math.max(0, Math.min(pos.textureIndex, brickTextures.length - 1));
            Texture tex = brickTextures[textureIndex];
            BrickActor brick = new BrickActor(tex, pos.x, pos.y, textureIndex);
            bricks.add(brick);
            addActor(brick);
        }
    }

    public void resetBricks(int remainingBricks) {
        // Xóa tất cả gạch hiện tại
        for (BrickActor brick : bricks) brick.remove();
        bricks.clear();

        if (remainingBricks <= 0) {
            finished = true;
            return;
        }

        finished = false;

        // Tạo lại số lượng gạch theo remainingBricks
        int totalBricks = BRICK_ROWS * BRICK_COLS;
        int bricksToCreate = Math.min(remainingBricks, totalBricks);

        for (int count = 0; count < bricksToCreate; count++) {
            int i = count / BRICK_COLS;
            int j = count % BRICK_COLS;

            float x = LEFT_BOUNDARY + j * BRICK_WIDTH;
            float y = SCREEN_HEIGHT - BRICK_HEIGHT - i * BRICK_HEIGHT;
            int textureIndex = new Random().nextInt(brickTextures.length);
            Texture tex = brickTextures[textureIndex];
            BrickActor brick = new BrickActor(tex, x, y, textureIndex);
            bricks.add(brick);
            addActor(brick);
        }
    }

    public java.util.List<Save.BrickPosition> getBrickPositions() {
        java.util.List<Save.BrickPosition> positions = new ArrayList<>();
        for (BrickActor brick : bricks) {
            if (!brick.isDestroyed()) {
                positions.add(new Save.BrickPosition(brick.getX(), brick.getY(), brick.getTextureIndex()));
            }
        }
        return positions;
    }


    @Override
    public void dispose() {
        ballTexture.dispose();
        paddleTexture.dispose();
        backgroundTexture.dispose();
        paddle.remove();
        ball.remove();
        for (Texture texture : brickTextures) {
            texture.dispose();
        }
        super.dispose();
    }
}
