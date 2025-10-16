package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static io.github.arkanoid.Constants.*;

public class BrickStage extends Stage {
    private Bar bar;
    private Ball ball;
    private ArrayList<BrickActor> bricks = new ArrayList<>();
    private GameLogic gameLogic;
    private Texture backgroundTexture;
    private boolean finished = false;
    private Random random = new Random();
    private Texture[] brickTextures = new Texture[4];

    private class BrickActor extends Actor {
        private TextureRegion textureRegion;
        private Rectangle hitBox;
        private boolean destroyed = false;

        public BrickActor(Texture texture, float x, float y) {
            this.textureRegion = new TextureRegion(texture);
            this.hitBox = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
            setPosition(x, y);
            setSize(BRICK_WIDTH, BRICK_HEIGHT);
        }

        public Rectangle getHitBox() {
            return hitBox;
        }

        public boolean isDestroyed() {
            return destroyed;
        }

        public void destroy() {
            destroyed = true;
            remove();
        }

        @Override
        public void act(float delta) {
            if (!destroyed) {
                hitBox.setPosition(getX(), getY());
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if (!destroyed) {
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

    public BrickStage() {
        super(new ScreenViewport());
        backgroundTexture = new Texture(BRICK_BACKGROUND_PATH);
        addActor(new BackgroundActor());

        brickTextures[0] = new Texture(BRICK_TEXTURE_BLUE_PATH);
        brickTextures[1] = new Texture(BRICK_TEXTURE_GREEN_PATH);
        brickTextures[2] = new Texture(BRICK_TEXTURE_ORANGE_PATH);
        brickTextures[3] = new Texture(BRICK_TEXTURE_RED_PATH);

        Texture barTexture = new Texture(BAR_TEXTURE_PATH);
        Texture ballTexture = new Texture(BALL_NORMAL_TEXTURE_PATH);

        bar = new Bar(barTexture, (SCREEN_WIDTH - BAR_WIDTH) / 2f, 60);
        ball = new Ball(ballTexture, SCREEN_WIDTH / 2f, 120);

        addActor(bar);
        addActor(ball);

        gameLogic = new GameLogic(ball, bar, null);
        createBricks();
    }

    private void createBricks() {
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                float x = START_X + j * BRICK_SPACING_X;
                float y = START_Y - i * BRICK_SPACING_Y;

                Texture brickTexture = brickTextures[random.nextInt(4)];
                BrickActor brick = new BrickActor(brickTexture, x, y);

                bricks.add(brick);
                addActor(brick);
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!finished) {
            gameLogic.launch();
            gameLogic.barCollision();
            gameLogic.boundaryCollision(delta);
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

    public Bar getBar() {
        return bar;
    }

    public Ball getBall() {
        return ball;
    }

    @Override
    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        for (Texture texture : brickTextures) {
            texture.dispose();
        }
        super.dispose();
    }
}
