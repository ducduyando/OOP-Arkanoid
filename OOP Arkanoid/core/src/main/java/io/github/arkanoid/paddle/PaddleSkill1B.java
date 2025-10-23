package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;

import static io.github.arkanoid.core.Constants.*;

public class PaddleSkill1B implements PaddleSkill {
    private enum Phase {
        CHARGING,
        FIRING,
        DONE
    }

    Texture laserEffect;

    private float paddleLaserTime = 0f;
    private final Paddle owner;
    private Phase currentPhase;
    private PaddleLaserEffect paddleLaserEffect;

    private float skill1BCooldownTimer = SKILL_COOLDOWN;
    private boolean isSkill1BReady = true;

    public void startSkill1BCooldown() {
        isSkill1BReady = false;
        skill1BCooldownTimer = SKILL_COOLDOWN;
    }

    public boolean isSkill1BReady() {
        return isSkill1BReady;
    }

    public PaddleSkill1B(Paddle owner) {
        laserEffect = new Texture("boss1/" + "skill2" + ".png");
        this.owner = owner;
        this.currentPhase = Phase.DONE;
    }

    public void enter(Paddle paddle) {
        this.paddleLaserEffect = new PaddleLaserEffect(laserEffect, paddle.getX(), paddle.getY());
        owner.getStage().addActor(this.paddleLaserEffect);
        currentPhase = Phase.CHARGING;
    }

    public void update(Paddle paddle, float delta) {
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
            if (paddleLaserTime >= 3f) {
                cleanup();
                currentPhase = Phase.DONE;
            }
        } else {
            startSkill1BCooldown();
        }

        if (!isSkill1BReady) {
            skill1BCooldownTimer -= delta;
            if (skill1BCooldownTimer <= 0) {
                isSkill1BReady = true;
            }
        }
    }

    public void cleanup() {
        if (paddleLaserEffect != null) {
            paddleLaserEffect.remove();
            paddleLaserEffect = null;
        }
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
