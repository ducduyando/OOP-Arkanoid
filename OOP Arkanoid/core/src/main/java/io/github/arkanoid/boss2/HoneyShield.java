package io.github.arkanoid.boss2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.paddle.Paddle;

import static io.github.arkanoid.core.Constants.*;
import static io.github.arkanoid.core.Constants.*;


public class HoneyShield extends Actor {
    private boolean hasShield = false;
    private float shieldDuration;
    private int shieldFrameCount;
    private final Animation<TextureRegion> shieldAnimation;
    private TextureRegion currentFrame;

    public HoneyShield(Texture texture, float x, float y) {

        setPosition(x, y);
        setSize(BOSS2_SKILL2_WIDTH, BOSS2_SKILL2_HEIGHT);
        shieldFrameCount = texture.getWidth() / BOSS2_WIDTH;
        TextureRegion[] shieldFrames = new TextureRegion[shieldFrameCount];
        for (int i = 0; i < shieldFrameCount; i++) {
            shieldFrames[i] = new TextureRegion(texture, BOSS2_SKILL2_WIDTH * i, 0, BOSS2_SKILL2_WIDTH, BOSS2_SKILL2_HEIGHT);
        }
        shieldAnimation = new Animation<TextureRegion>(FRAME_DURATION, shieldFrames);

    }

    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public boolean isHasShield() {
        return hasShield;
    }

    public float getShieldDuration() {
        return shieldDuration;
    }

    public void setShieldDuration(float shieldDuration) {
        this.shieldDuration = shieldDuration;
    }

    @Override
    public void act(float delta) {
        currentFrame = shieldAnimation.getKeyFrame(shieldDuration, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (hasShield && currentFrame != null) {
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        }
    }
}
