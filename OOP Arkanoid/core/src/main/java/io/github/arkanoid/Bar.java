package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static io.github.arkanoid.Constants.*;

public class Bar extends Actor {
    TextureRegion[] regions = new TextureRegion[7];
    TextureRegion textureRegion;
    Rectangle[] hitBoxes = new Rectangle[7];
    int state;
    Vector velocityVector = new Vector(BAR_VELOCITY_X, BAR_VELOCITY_Y);

    Bar(Texture texture, float x, float y) {
        state = 0;
        for (int i = 0; i < 7; i++) {
            regions[i] = new TextureRegion(texture,  BAR_WIDTH * i, 0, BAR_WIDTH, BAR_HEIGHT);
        }
        for (int i = 0; i < 7; i++) {
            hitBoxes[i] = new Rectangle(getX() + 32 * i, getY(), BAR_WIDTH - 64 * i, BAR_HEIGHT);
        }
        this.textureRegion = regions[state];
        setPosition(x, y);
        setSize(BAR_WIDTH, BAR_HEIGHT);
        setOrigin(getWidth() / 2f, getHeight() / 2f);

    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Rectangle getBound(){
        return hitBoxes[state];
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A) && getX() + 32 * state > 0) {
            moveBy(-velocityVector.getX() * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && getX() + BAR_WIDTH - 32 * state < SCREEN_WIDTH) {
            moveBy(velocityVector.getX() * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && getY() > 0) {
            moveBy(0, -velocityVector.getY() * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && getY() + BAR_HEIGHT < SCREEN_HEIGHT) {
            moveBy(0, velocityVector.getY() * delta);
        }
    }
}
