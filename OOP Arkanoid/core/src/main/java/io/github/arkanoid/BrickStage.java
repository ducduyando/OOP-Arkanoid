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
    private final Bar bar = new Bar(new Texture("Bar.png"), (SCREEN_WIDTH - BAR_WIDTH) / 2f, 60);
    private final Ball ball = new Ball(new Texture("ball/" + "normal" + ".png"), SCREEN_WIDTH / 2f, 120);
    private ArrayList<BrickActor> bricks = new ArrayList<>();
    private GameLogic gameLogic;
    private final Texture backgroundTexture = new Texture("brick/" + "background" + ".png");
    private boolean finished = false;
    private final Texture[] brickTextures = new Texture[4];

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
        addActor(new BackgroundActor());

        brickTextures[0] = new Texture("brick/" + "red" + ".png");
        brickTextures[1] = new Texture("brick/" + "blue" + ".png");
        brickTextures[2] = new Texture("brick/" + "green" + ".png");
        brickTextures[3] = new Texture("brick/" + "orange" + ".png");

        addActor(bar);
        addActor(ball);

        gameLogic = new GameLogic(ball, bar, null);
        createBricks();
    }

    private void createBricks() {
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                float x = LEFT_BOUNDARY + j * BRICK_WIDTH;
                float y = UP_BOUNDARY - i * BRICK_HEIGHT;

                Texture brickTexture = brickTextures[new Random().nextInt(brickTextures.length)];
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
        bar.remove();
        ball.remove();
        for (Texture texture : brickTextures) {
            texture.dispose();
        }
        super.dispose();
    }
}
