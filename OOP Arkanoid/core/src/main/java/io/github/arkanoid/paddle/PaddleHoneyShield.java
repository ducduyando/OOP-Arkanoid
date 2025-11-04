package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.core.Constants.*;

import java.awt.*;


public class PaddleHoneyShield extends Actor {
    private TextureRegion shield;
    private Rectangle hitbox;

    public PaddleHoneyShield(Texture texture, float paddleX, float paddleY, int state) {
        shield = new TextureRegion(texture, state * 32, 0, PADDLE_WIDTH - (64 * state), PADDLE_HEIGHT);
        setPosition(paddleX, paddleY);
        setSize(PADDLE_WIDTH - state * 64, PADDLE_HEIGHT);
        hitbox = new Rectangle(paddleX, paddleY, getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        hitbox.setPosition(getX(), getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(shield, getX(), getY(), getWidth(), getHeight());
    }
}
