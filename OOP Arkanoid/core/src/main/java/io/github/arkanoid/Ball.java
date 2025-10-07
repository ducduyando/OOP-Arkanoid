package io.github.arkanoid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.Constants.*;

public class Ball extends Actor {
    TextureRegion textureRegion;
    Vector velocityVector = new Vector(BALL_VELOCITY_X, BALL_VELOCITY_Y);
    Rectangle hitBox;
    private boolean isLaunched = false;

    Ball(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture);
        this.hitBox = new Rectangle(x, y, BALL_WIDTH, BALL_HEIGHT);
        setPosition(x, y);
        setSize(BALL_WIDTH, BALL_HEIGHT);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
    }

    public Rectangle getBound(){
        return hitBox;
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
            moveBy(velocityVector.getX() * delta, velocityVector.getY() * delta);
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
