package io.github.arkanoid.paddle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.core.Constants.*;

public class Paddle extends Actor {

    TextureRegion textureRegion;
    Rectangle hitBox;
    private float stopTimer = 0f;
    private boolean isInvincible = false;
    private int state = 0;
    private float blinkTimer = 0f;
    private boolean isVisible = true;

    public Paddle(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture, 0, 0, PADDLE_WIDTH, PADDLE_HEIGHT);
        this.hitBox = new Rectangle(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
        setPosition(x, y);
        setSize(PADDLE_WIDTH, PADDLE_HEIGHT);
        setOrigin(getWidth() / 2f, getHeight() / 2f);

    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

    public int getState() {
        return state;
    }

    public void setState(int newState) {
        this.state = newState;
        this.textureRegion.setRegion(PADDLE_WIDTH * newState, 0, PADDLE_WIDTH, PADDLE_HEIGHT);

        float newWidth = PADDLE_WIDTH - (64 * newState);
        float newXOffset = 32 * newState;

        this.hitBox.setWidth(newWidth);
        this.hitBox.setPosition(getX() + newXOffset, getY());

    }

    public void resetState() {
        this.state = 0;
    }

    public Rectangle getHitBox(){
        return hitBox;
    }

    public void takeDamage() {
        if (!isInvincible) {
            int currentState = getState() + 1;
            setState(currentState);
            isInvincible = true;
            stopTimer = 0f;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible) {
            batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A) && getX() + 32 * state > LEFT_BOUNDARY) {
            moveBy(-PADDLE_VELOCITY.x * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && getX() + PADDLE_WIDTH - 32 * state < RIGHT_BOUNDARY) {
            moveBy(PADDLE_VELOCITY.x * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && getY() > DOWN_BOUNDARY) {
            moveBy(0, -PADDLE_VELOCITY.y * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && getY() + PADDLE_HEIGHT + BALL_HEIGHT < UP_BOUNDARY) {
            moveBy(0, PADDLE_VELOCITY.y * delta);
        }

        if (isInvincible) {
            stopTimer += delta;
            blinkTimer += delta;
            if (blinkTimer >= FRAME_DURATION) {
                isVisible = !isVisible;
                blinkTimer = 0f;
            }

            if (stopTimer >= 2f) {
                isInvincible = false;
                stopTimer = 0f;
                blinkTimer = 0f;
                isVisible = true;
            }
        }

        float currentXOffset = 32 * state;
        hitBox.setPosition(getX() + currentXOffset, getY());
    }
}
