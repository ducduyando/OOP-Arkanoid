package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.core.Constants.*;


public class PaddleBallUpgrade extends Actor {
    private final TextureRegion ballUpgrade;

    public PaddleBallUpgrade(Texture ballUpgrade, float paddleX, float paddleY) {
        this.ballUpgrade = new TextureRegion(ballUpgrade);
        float x = paddleX + (PADDLE_WIDTH + BALL_WIDTH) / 2f;
        float y = paddleY + PADDLE_HEIGHT;
        setPosition(x, y);
        setSize(BALL_WIDTH, BALL_HEIGHT);
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(ballUpgrade, getX(), getY(), getWidth(), getHeight());
    }
}
