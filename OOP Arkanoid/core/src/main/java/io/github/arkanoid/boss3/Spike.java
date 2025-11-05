package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.paddle.Paddle;
import static io.github.arkanoid.core.Constants.*;
import static io.github.arkanoid.core.MusicManager.*;

public class Spike extends Actor {

    private Paddle paddle;
    private Boss3 boss3;
    private Rectangle hitBox;
    private TextureRegion currentFrame;

    private float stateTimer = 0f;
    private boolean isLastFrame = false;
    private boolean hasArrived = false;
    private long spikeSoundId;

    private Animation<TextureRegion> spikeAnimation;

    Vector2 center = new Vector2(SCREEN_WIDTH / 2f, BOSS3_INITIAL_Y);

    public Spike(Texture spike, Boss3 boss3, float bossX, float bossY, Paddle paddle) {
        this.paddle = paddle;
        this.boss3 = boss3;

        int maxFrame = spike.getWidth() / BOSS3_SKILL_2A_WIDTH;
        TextureRegion[] frames = new TextureRegion[maxFrame];
        for (int i = 0; i < maxFrame; i++) {
            frames[i] = new TextureRegion(spike, i * BOSS3_SKILL_2A_WIDTH, 0, BOSS3_SKILL_2A_WIDTH, BOSS3_SKILL_2A_HEIGHT);
        }
        spikeAnimation = new Animation<>(FRAME_DURATION * 1.5f, frames);

        float x = bossX + (BOSS3_WIDTH - BOSS3_SKILL_2A_WIDTH) / 2f;
        float y = bossY + (BOSS3_HEIGHT - BOSS3_SKILL_2A_HEIGHT) / 2f;

        setPosition(x, y);
        setSize(BOSS3_SKILL_2A_WIDTH, BOSS3_SKILL_2A_HEIGHT);
        hitBox = new Rectangle(x, y, BOSS3_SKILL_2A_WIDTH, BOSS3_SKILL_2A_HEIGHT);
        setOrigin(BOSS3_SKILL_2A_WIDTH / 2f, BOSS3_SKILL_2A_HEIGHT / 2f);

        currentFrame = spikeAnimation.getKeyFrame(0);
        this.spikeSoundId = playEffect("spikeSound");
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public void act(float delta) {

        if (!isLastFrame) {
            stateTimer += delta;
            currentFrame = spikeAnimation.getKeyFrame(stateTimer, false);
            if (spikeAnimation.isAnimationFinished(stateTimer)) {
                isLastFrame = true;
            }
        }
        else {
            if (!hasArrived) {
                rotateBy(ROTATION_SPEED * 2 * delta);

                Vector2 currentPosition = new Vector2(getX() + BOSS3_SKILL_2A_WIDTH / 2f, getY());
                Vector2 targetPosition = new Vector2(paddle.getX() + PADDLE_WIDTH / 2f, paddle.getY() + PADDLE_HEIGHT);

                if (currentPosition.dst(targetPosition) < boss3.velocity.len() * delta) {
                    setPosition(targetPosition.x, targetPosition.y);
                    if (!paddle.isInvincible()) {
                        paddle.takeDamage();
                        hasArrived = true;
                    }
                } else {
                    Vector2 direction = targetPosition.cpy().sub(currentPosition).nor();
                    moveBy(direction.x * boss3.velocity.x * delta, direction.y * boss3.velocity.y * delta);
                }
            } else {
                if (!((getRotation() + ROTATION_SPEED * 2 * delta) % 360 < ROTATION_SPEED * 2 * delta)) {
                    rotateBy(ROTATION_SPEED * 2 * delta);
                }
                else {

                    Vector2 currentPosition = new Vector2(getX() + BOSS3_SKILL_2A_WIDTH / 2f, getY());
                    if (currentPosition.dst(center) < boss3.velocity.len() * delta) {
                        setPosition(center.x, center.y);
                        hasArrived = false;
                        boss3.getHitBox().setSize(BOSS3_WIDTH, BOSS3_HEIGHT);
                        boss3.setPosition(BOSS3_INITIAL_X, BOSS3_INITIAL_Y);
                        boss3.setUsingSkill2A(false);
                        boss3.setSkill2AFinished(true);
                        remove();
                        return;
                    }
                    else {
                        Vector2 direction = center.cpy().sub(currentPosition).nor();
                        moveBy(direction.x * boss3.velocity.x * delta, direction.y * boss3.velocity.y * delta);
                    }
                }
            }
        }
        hitBox.setPosition(getX(), getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
    @Override
    public boolean remove() {
        stopEffect("spikeSound", spikeSoundId);
        return super.remove();
    }
}
