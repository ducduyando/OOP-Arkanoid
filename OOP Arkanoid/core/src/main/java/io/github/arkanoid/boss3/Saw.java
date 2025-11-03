package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;
import static io.github.arkanoid.core.Constants.*;

public class Saw extends Actor {

    private TextureRegion currentFrame;
    private float stateTimer = 0;
    private TextureRegion target;
    private int direction;

    private float targetX;
    private float targetY;

    private int rotationAngle;
    private Rectangle hitBox;

    private boolean isVertical;
    private boolean isSawSkillFinished = false;

    private Animation<TextureRegion> sawSkillAnimation;

    private final int HORIZONTAL_SAW_SPEED = 889;
    private final int VERTICAL_SAW_SPEED = 500;


    public Saw(Texture sawTexture, Texture targetTexture, float x, float y, boolean isVertical) {
        this.isVertical = isVertical;
        target(targetTexture, x, y);

        if (!isVertical) {
            if (new Random().nextBoolean()) {
                direction = 1;
                setPosition(-BOSS3_SKILL_1B_WIDTH, y - BOSS3_SKILL_1B_HEIGHT / 2f);
            }
            else {
                direction = -1;
                setPosition(SCREEN_WIDTH, y - BOSS3_SKILL_1B_HEIGHT / 2f);
            }
        } else {
            if (new Random().nextBoolean()) {
                direction = 1;
                setPosition(x - BOSS3_SKILL_1B_WIDTH / 2f, -BOSS3_SKILL_1B_HEIGHT);
            }
            else {
                direction = -1;
                setPosition(x - BOSS3_SKILL_1B_WIDTH / 2f, SCREEN_HEIGHT - HP_HEIGHT);
            }
        }
        setSize(BOSS3_SKILL_1B_WIDTH, BOSS3_SKILL_1B_HEIGHT);
        hitBox = new Rectangle(getX(), getY(), getWidth(), getHeight());
        setOrigin(BOSS3_SKILL_1B_WIDTH / 2f, BOSS3_SKILL_1B_HEIGHT / 2f);

        int maxSawFrame = sawTexture.getWidth() / BOSS3_SKILL_1B_WIDTH;
        TextureRegion[] sawSkillFrames = new TextureRegion[maxSawFrame];
        for (int i = 0; i < maxSawFrame; i++) {
            sawSkillFrames[i] = new TextureRegion(sawTexture, i * BOSS3_SKILL_1B_WIDTH, 0,
                BOSS3_SKILL_1B_WIDTH, BOSS3_SKILL_1B_HEIGHT);
        }
        sawSkillAnimation = new Animation<>(FRAME_DURATION, sawSkillFrames);
        currentFrame = sawSkillAnimation.getKeyFrame(0);
    }

    public boolean isSawSkillFinished() {
        return isSawSkillFinished;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    private void target(Texture targetTexture, float x, float y) {
        target = new TextureRegion(targetTexture);
        if (!isVertical) {
            targetX = x;
            targetY = y - targetTexture.getHeight() / 2f;
            rotationAngle = 0;
        } else {
            targetX = x + targetTexture.getHeight() / 2f;
            targetY = y;
            rotationAngle = 90;
        }
    }


    @Override
    public void act(float delta) {
        rotateBy(ROTATION_SPEED * delta);
        if (!isVertical) {
            moveBy(HORIZONTAL_SAW_SPEED * delta * direction, 0);
        } else {
            moveBy(0,VERTICAL_SAW_SPEED * delta * direction);
        }
        if (getX() >= SCREEN_WIDTH || getX() <= -BOSS3_SKILL_1B_WIDTH
            || getY() >= UP_BOUNDARY || getY() <= -BOSS3_SKILL_1B_HEIGHT) {
            this.remove();
            isSawSkillFinished = true;
            return;
        }
        hitBox.setPosition(getX(), getY());
        stateTimer += delta;
        currentFrame = sawSkillAnimation.getKeyFrame(stateTimer, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(target, targetX, targetY, 0, 0, target.getRegionWidth(), target.getRegionHeight(), 1f, 1f, rotationAngle);
        batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
