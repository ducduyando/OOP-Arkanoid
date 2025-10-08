package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.Constants.*;


public class Boss extends Actor {
    TextureRegion textureRegion;
    Vector velocityVector;
    Rectangle hitBox;
    private int hp;

    Boss(float x, float y) {
        this.hp = 100;
        setPosition(x, y);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
    }

    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public void act(float delta) {
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
