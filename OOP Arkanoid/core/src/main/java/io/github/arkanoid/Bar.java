package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.Constants.*;

public class Bar extends Actor {
    TextureRegion textureRegion;
    Vector2 velocityVector;
    Rectangle hitBox;
    private int state = 0;


    Bar(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture, 0, 0, BAR_WIDTH, BAR_HEIGHT);
        this.velocityVector = new Vector2(BAR_VELOCITY_X, BAR_VELOCITY_Y);
        this.hitBox = new Rectangle(x, y, BAR_WIDTH, BAR_HEIGHT);
        setPosition(x, y);
        setSize(BAR_WIDTH, BAR_HEIGHT);
        setOrigin(getWidth() / 2f, getHeight() / 2f);

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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A) && getX() + 32 * state > 0) {
            moveBy(-velocityVector.x * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && getX() + BAR_WIDTH - 32 * state < SCREEN_WIDTH) {
            moveBy(velocityVector.x * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && getY() > 0) {
            moveBy(0, -velocityVector.y * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && getY() + BAR_HEIGHT + BALL_HEIGHT < SCREEN_HEIGHT) {
            moveBy(0, velocityVector.y * delta);
        }

        float currentXOffset = 32 * state;
        hitBox.setPosition(getX() + currentXOffset, getY());
    }
}
