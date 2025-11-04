package io.github.arkanoid.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.core.Constants.*;

public class Ball extends Actor {
    TextureRegion textureRegion;
    Vector2 velocityVector;
    Rectangle hitBox;
    private boolean isLaunched = false;

    public Ball(float x, float y) {
        this.velocityVector = new Vector2(BALL_VELOCITY);
        this.hitBox = new Rectangle(x, y, BALL_WIDTH, BALL_HEIGHT);
        setPosition(x, y);
        setSize(BALL_WIDTH, BALL_HEIGHT);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
    }

    public Ball(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture);
        this.velocityVector = new Vector2(BALL_VELOCITY);
        this.hitBox = new Rectangle(x, y, BALL_WIDTH, BALL_HEIGHT);
        setPosition(x, y);
        setSize(BALL_WIDTH, BALL_HEIGHT);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
    }

    public Vector2 getVelocity() {
        return velocityVector;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(float x, float y) {
        this.hitBox.setPosition(x, y);
    }

    public void resetLaunch() {
        isLaunched = false;
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    public void setLaunched(boolean launched) {
        isLaunched = launched;
    }

    @Override
    public void act(float delta) {
        if (isLaunched) {
            moveBy(velocityVector.x * delta, velocityVector.y * delta);
            hitBox.setPosition(getX(), getY());
        }
    }
    public void resetVelocity() {
        velocityVector.set(0, 0);// reset lai vi tri qua bong
    }

    public void setVelocity(float x, float y) {
        velocityVector.set(x, y);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
