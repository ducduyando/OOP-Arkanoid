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
    private final int maxFrame;
    private int currentFrame = 0;

    Boss(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture, 0, 0, BOSS1_WIDTH, BOSS1_HEIGHT);
        this.velocityVector = new Vector(BOSS1_VELOCITY_X, BOSS1_VELOCITY_Y);
        this.hitBox = new Rectangle(x, y, BOSS1_WIDTH, BOSS1_HEIGHT);
        maxFrame = texture.getWidth() / BOSS1_WIDTH;
        setPosition(x, y);
        setSize(BOSS1_WIDTH, BOSS1_HEIGHT);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public void act(float delta) {
        if (!isDead()) {
            currentFrame = (currentFrame + 1) / maxFrame;
            this.textureRegion.setRegion(BOSS1_WIDTH * currentFrame, 0, BOSS1_WIDTH, BOSS1_HEIGHT);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
