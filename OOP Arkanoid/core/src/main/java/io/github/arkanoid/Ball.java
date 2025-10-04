package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.*;

import static io.github.arkanoid.Constants.*;

public class Ball extends Actor {
    TextureRegion textureRegion;
    private float velocityX = 100;
    private float velocityY = 100;
    private boolean wasColliding = false;


    Ball(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture, BALL_WIDTH, BALL_HEIGHT);
        setPosition(x, y);
        setSize(texture.getWidth(), texture.getHeight());
        setOrigin(texture.getWidth() / 2f, texture.getHeight() / 2f);
    }

    @Override
    public void act(float delta) {
        moveBy(velocityX * delta, velocityY * delta);

      // va cham tuong trai va phai
        if (getX() <= 0 || getX() >= 800 - getWidth()) {
            velocityX = -velocityX;
        }
        if (getY() <= 0 || getY() >= 600 - getHeight()) {
            velocityY = -velocityY;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public Rectangle getBound(){
        return new Rectangle((int) getX(),(int) getY(),(int) getWidth(),(int) getHeight());
    }


    public void bounceStronger(){
        velocityY=-velocityY*2;
    }
    public void bounce() {
        velocityY = -velocityY;
    }
    public void reverseX() {
        velocityX = -velocityX;
    } public void reverseY() {
        velocityY = -velocityY;
    }
    public boolean isWasColliding() {
        return wasColliding;
    }

    public void setWasColliding(boolean wasColliding) {
        this.wasColliding = wasColliding;
    }


}
