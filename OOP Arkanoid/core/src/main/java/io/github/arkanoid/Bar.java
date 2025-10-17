package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.Constants.*;

public class Bar extends Actor {

    TextureRegion textureRegion;
    Rectangle hitBox;
    private float stopTimer = 0f;
    private boolean isInvincible = false;
    private int state = 0;
    private float blinkTimer = 0f;
    private boolean isVisible = true;

    Bar(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture, 0, 0, BAR_WIDTH, BAR_HEIGHT);
        this.hitBox = new Rectangle(x, y, BAR_WIDTH, BAR_HEIGHT);
        setPosition(x, y);
        setSize(BAR_WIDTH, BAR_HEIGHT);
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
        this.textureRegion.setRegion(BAR_WIDTH * newState, 0, BAR_WIDTH, BAR_HEIGHT);

        float newWidth = BAR_WIDTH - (64 * newState);
        float newXOffset = 32 * newState;

        this.hitBox.setWidth(newWidth);
        this.hitBox.setPosition(getX() + newXOffset, getY());

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
            moveBy(-BAR_VELOCITY.x * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && getX() + BAR_WIDTH - 32 * state < RIGHT_BOUNDARY) {
            moveBy(BAR_VELOCITY.x * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && getY() > DOWN_BOUNDARY) {
            moveBy(0, -BAR_VELOCITY.y * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && getY() + BAR_HEIGHT + BALL_HEIGHT < UP_BOUNDARY) {
            moveBy(0, BAR_VELOCITY.y * delta);
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
