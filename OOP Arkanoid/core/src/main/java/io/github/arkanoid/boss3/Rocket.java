package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.math.Rectangle;
import io.github.arkanoid.core.Constants;

import static io.github.arkanoid.core.Constants.*;
import static io.github.arkanoid.core.MusicManager.*;

public class Rocket extends Actor {

    private TextureRegion currentFrame;
    private float stateTimer = 0;

    private Texture target;
    private float targetX;
    private float targetY;
    private Rectangle hitBox;

    private boolean isInTarget = false;
    private boolean isRocketSkillFinished = false;
    private long rocketSoundId;

    private Animation<TextureRegion> rocketSkillAnimation;

    public Rocket(Texture rocketSkillTexture, Texture targetTexture, float x, float y) {
        target(targetTexture, x, y);
        float xx = x - Constants.BOSS3_SKILL_1A_WIDTH / 2f;
        float yy = SCREEN_HEIGHT - HP_HEIGHT;
        setPosition(xx, yy);
        setSize(Constants.BOSS3_SKILL_1A_WIDTH, BOSS3_SKILL_1A_HEIGHT);
        hitBox = new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT, target.getWidth(), target.getHeight());

        int maxFrame = rocketSkillTexture.getWidth() / Constants.BOSS3_SKILL_1A_WIDTH;
        TextureRegion[] rocketSkillFrames = new TextureRegion[maxFrame];
        for (int i = 0; i < maxFrame; i++) {
            rocketSkillFrames[i] = new TextureRegion(rocketSkillTexture, i * Constants.BOSS3_SKILL_1A_WIDTH, 0,
                Constants.BOSS3_SKILL_1A_WIDTH, BOSS3_SKILL_1A_HEIGHT);
        }

        rocketSkillAnimation = new Animation<>(FRAME_DURATION * 1.5f, rocketSkillFrames);
        currentFrame = rocketSkillAnimation.getKeyFrame(0);
        this.rocketSoundId = playEffect("rocketSound");

    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public boolean isRocketSkillFinished() {
        return isRocketSkillFinished;
    }

    private void target(Texture target, float x, float y) {
        this.target = target;
        targetX = x - target.getWidth() / 2f;
        targetY = y - target.getHeight() / 2f;
    }

    @Override
    public void act(float delta) {
        if (getY() - ROCKET_SPEED * delta > targetY) {
            moveBy(0, - ROCKET_SPEED * delta);
            currentFrame = rocketSkillAnimation.getKeyFrame(0);
            isRocketSkillFinished = false;
            isInTarget = false;
        } else {
            moveBy(0, targetY - getY());
            hitBox.setPosition(getX(), getY());
            isInTarget = true;
            stateTimer += delta;
            currentFrame = rocketSkillAnimation.getKeyFrame(stateTimer, false);
            if (rocketSkillAnimation.isAnimationFinished(stateTimer)) {
                stateTimer = 0f;
                isRocketSkillFinished = true;
                remove();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isInTarget) {
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        } else {
            batch.draw(target, targetX, targetY);
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        }
    }
    @Override
    public boolean remove() {
        stopEffect("rocketSound", rocketSoundId);
        return super.remove();
    }
}
