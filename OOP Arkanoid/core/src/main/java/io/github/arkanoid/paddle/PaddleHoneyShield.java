package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.core.Constants.*;

import java.awt.*;


public class PaddleHoneyShield extends Actor {
    private Texture texture;
    private Rectangle hitbox;


    public PaddleHoneyShield(Texture texture, float paddleX, float paddleY, int state) {
        this.texture = texture;
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
        TextureRegion honeyShield = new TextureRegion(texture, getX(), getY(), getWidth(), getHeight());
        batch.draw(honeyShield, getX(), getY(), getWidth(), getHeight());
    }
}
