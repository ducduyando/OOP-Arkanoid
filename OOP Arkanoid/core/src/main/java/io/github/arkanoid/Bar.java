package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;

import static io.github.arkanoid.Constants.*;

public class Bar extends Actor {
    TextureRegion textureRegion;
    Vector velocityVector = new Vector(BAR_VELOCITY_X, BAR_VELOCITY_Y);

    Bar(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture, BAR_WIDTH, BAR_HEIGHT);
        setPosition(x, y);
        setSize(texture.getWidth(), texture.getHeight());
        setOrigin(texture.getWidth() / 2f, texture.getHeight() / 2f);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A) && getX() >= 0) {
            moveBy(-velocityVector.getX(), 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveBy(velocityVector.getX(), 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && getY() >= 0) {
            moveBy(0, -velocityVector.getY());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveBy(0, velocityVector.getY());
        }
    }
    public Rectangle getBound(){
        return new Rectangle( getX(), getY(), getWidth(), getHeight());
    }
}
