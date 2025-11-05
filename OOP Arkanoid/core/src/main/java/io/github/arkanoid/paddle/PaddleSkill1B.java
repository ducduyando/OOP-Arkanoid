package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;

import static io.github.arkanoid.core.Constants.*;
import static io.github.arkanoid.core.MusicManager.*;

public class PaddleSkill1B implements PaddleSkill {
    public enum Phase {
        CHARGING,
        FIRING,
        DONE
    }

    Texture laserEffect;

    private float paddleLaserTime = 0f;
    private final Paddle owner;
    private Phase currentPhase;
    private PaddleLaserEffect paddleLaserEffect;

    private float skill1BCooldownTimer = 0f; // Start ready
    private boolean isSkill1BReady = true;
    private long laserBeamSoundId;

    public PaddleSkill1B(Paddle owner) {
        laserEffect = new Texture("PaddleSkill/" + "1b" + ".png");
        this.owner = owner;
        this.currentPhase = Phase.DONE;
    }

    public void startSkill1BCooldown() {
        isSkill1BReady = false;
        skill1BCooldownTimer = PADDLE_SKILL_COOLDOWN;
    }
    public float getSkill1BCooldownTimer() {
        return skill1BCooldownTimer;
    }

    public void setSkill1BCooldownTimer(float skill1BCooldownTimer) {
        this.skill1BCooldownTimer = skill1BCooldownTimer;
    }

    public void setIsSkill1BReady(boolean isSkill1BReady) {
        this.isSkill1BReady = isSkill1BReady;
    }

    public boolean isSkill1BReady() {
        return isSkill1BReady;
    }


    public float getPaddleLaserTime() {
        return paddleLaserTime;
    }


    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void enter(Paddle paddle) {
        this.paddleLaserEffect = new PaddleLaserEffect(laserEffect, paddle.getX(), paddle.getY());
        owner.getStage().addActor(this.paddleLaserEffect);
        currentPhase = Phase.CHARGING;
        this.laserBeamSoundId = playEffect("laserBeamSound");
    }

    public void update(Paddle paddle, float delta) {
        // Always update cooldown timer
        if (!isSkill1BReady) {
            skill1BCooldownTimer -= delta;
            if (skill1BCooldownTimer <= 0) {
                isSkill1BReady = true;
                skill1BCooldownTimer = 0f; // cooldown complete
            }
        }

        // Only update skill effects if laser is active
        if (paddleLaserEffect != null) {
            float x = paddle.getX() + PADDLE_WIDTH / 2f - LASER_WIDTH / 2f;
            float y = paddle.getY() + PADDLE_HEIGHT;
            paddleLaserEffect.setPosition(x, y);

            if (currentPhase == Phase.CHARGING) {
                if (paddleLaserEffect.isAnimationDone()) {
                    currentPhase = Phase.FIRING;
                    paddleLaserTime = 0f;
                }
            } else if (currentPhase == Phase.FIRING) {
                paddleLaserTime += delta;
                if (paddleLaserTime >= LASER_DURATION) {
                    cleanup();
                    currentPhase = Phase.DONE;
                    startSkill1BCooldown(); // Start cooldown when skill finishes
                }
            }
        }
    }

    public void cleanup() {
        if (paddleLaserEffect != null) {
            paddleLaserEffect.remove();
            paddleLaserEffect = null;
        }
        stopEffect("laserBeamSound", laserBeamSoundId);
        currentPhase = Phase.DONE;
    }

    public boolean isFiring() {
        return currentPhase == Phase.FIRING;
    }
    public boolean isDone() {
        return currentPhase == Phase.DONE;
    }

    public PaddleLaserEffect getPaddleLaserEffect() { // Thêm phương thức getter cho va chạm.
        return paddleLaserEffect;
    }
}
