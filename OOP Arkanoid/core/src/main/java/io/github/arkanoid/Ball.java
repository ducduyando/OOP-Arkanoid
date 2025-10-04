package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;

import static io.github.arkanoid.Constants.*;

public class Ball extends Actor {
    TextureRegion textureRegion;
    Vector velocityVector = new Vector(BALL_VELOCITY_X, BALL_VELOCITY_Y);
    private boolean wasColliding = false;

    Ball(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture, BALL_WIDTH, BALL_HEIGHT);
        setPosition(x, y);
        setSize(texture.getWidth(), texture.getHeight());
        setOrigin(texture.getWidth() / 2f, texture.getHeight() / 2f);
    }

    public boolean getWasColliding() {
        return wasColliding;
    }

    public void setWasColliding(boolean wasColliding) {
        this.wasColliding = wasColliding;
    }

    @Override
    public void act(float delta) {
        moveBy(velocityVector.getX(), velocityVector.getY());

      // va cham tuong trai va phai
        if (getX() <= LEFT_BOUNDARY || getX() >= RIGHT_BOUNDARY) {
            this.velocityVector.mulX(-1);
        }
        if (getY() <= DOWN_BOUNDARY || getY() >= UP_BOUNDARY) {
            this.velocityVector.mulY(-1);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public Rectangle getBound(){
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void reverseX() {
        this.velocityVector.mulX(-1);
    }

    public void reverseY() {
        this.velocityVector.mulY(-1);
    }

}
