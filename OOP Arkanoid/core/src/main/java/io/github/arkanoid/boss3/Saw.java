package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;
import static io.github.arkanoid.core.Constants.*;

public class Saw extends Actor {

    private TextureRegion currentFrame;
    private float stateTimer = 0;
    private Texture sawSkillTexture;
    private TextureRegion target;
    private int direction;

    private float targetX;
    private float targetY;

    private int rotationAngle;

    private boolean isSweepX;
    private boolean isSawSkillDone = false;


    private Animation<TextureRegion> sawSkillAnimation;
    private final int SAW_SPEED = 400;

    public Saw(Texture sawSkillTexture, Texture targetTexture, float x, float y) {
        this.sawSkillTexture = sawSkillTexture;
        target(targetTexture, x, y);
        if (x == 0) {
            if (new Random().nextBoolean()) {
                direction = 1;
                setPosition(0, y - BOSS3_SKILL_RIGHT_HAND_HEIGHT / 2f);
            }
            else {
                direction = -1;
                setPosition(SCREEN_WIDTH - BOSS3_SKILL_RIGHT_HAND_WIDTH, y - BOSS3_SKILL_RIGHT_HAND_HEIGHT / 2f);
            }
            isSweepX = false;
        } else if (y == 0) {
            if (new Random().nextBoolean()) {
                direction = 1;
                setPosition(x - BOSS3_SKILL_RIGHT_HAND_WIDTH / 2f, 0);
            }
            else {
                direction = -1;
                setPosition(x - BOSS3_SKILL_RIGHT_HAND_WIDTH / 2f, SCREEN_HEIGHT - BOSS3_SKILL_RIGHT_HAND_HEIGHT - HP_HEIGHT);
            }
            isSweepX = true;
        }
        else {
            return;
        }
        setSize(BOSS3_SKILL_RIGHT_HAND_WIDTH, BOSS3_SKILL_RIGHT_HAND_HEIGHT);
        setOrigin(BOSS3_SKILL_RIGHT_HAND_WIDTH / 2f, BOSS3_SKILL_RIGHT_HAND_HEIGHT / 2f);

        int maxSawFrame = sawSkillTexture.getWidth() / BOSS3_SKILL_RIGHT_HAND_WIDTH;
        TextureRegion[] sawSkillFrames = new TextureRegion[maxSawFrame];
        for (int i = 0; i < maxSawFrame; i++) {
            sawSkillFrames[i] = new TextureRegion(sawSkillTexture, i * BOSS3_SKILL_RIGHT_HAND_WIDTH, 0,
                BOSS3_SKILL_RIGHT_HAND_WIDTH, BOSS3_SKILL_RIGHT_HAND_HEIGHT);
        }
        sawSkillAnimation = new Animation<>(FRAME_DURATION, sawSkillFrames);
    }

    private void target(Texture targetTexture, float x, float y) {
        target = new TextureRegion(targetTexture);
        if (x == 0) {
            targetX = x;
            targetY = y - targetTexture.getHeight() / 2f;
            rotationAngle = 0;
        } else if (y == 0) {
            targetX = x + targetTexture.getHeight() / 2f;
            targetY = y;
            rotationAngle = 90;
        }
    }

    public boolean isSawSkillDone() {
        return isSawSkillDone;
    }

    @Override
    public void act(float delta) {
        rotateBy(ROTATION_SPEED * delta);
        if (!isSweepX) {
            moveBy(SAW_SPEED * delta * direction, 0);
        } else {
            moveBy(0, SAW_SPEED * delta * direction);
        }
        if (getX() >= SCREEN_WIDTH || getX() <= - BOSS3_SKILL_RIGHT_HAND_WIDTH
            || getY() >= UP_BOUNDARY || getY() <= - BOSS3_SKILL_RIGHT_HAND_HEIGHT) {
            this.remove();
            isSawSkillDone = true;
            return;
        }

        stateTimer += delta;
        currentFrame = sawSkillAnimation.getKeyFrame(stateTimer, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(target, targetX, targetY, 0, 0, target.getRegionWidth(), target.getRegionHeight(), 1f, 1f, rotationAngle);
        batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
