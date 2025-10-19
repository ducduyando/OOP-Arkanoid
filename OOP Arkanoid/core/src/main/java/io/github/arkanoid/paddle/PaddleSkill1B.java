package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;

import static io.github.arkanoid.universal.Constants.*;

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

    private float skill2CooldownTimer = SKILL_COOLDOWN;
    private boolean isSkill2Ready = true;

    public void startSkill2Cooldown() {
        isSkill2Ready = false;
        skill2CooldownTimer = SKILL_COOLDOWN;
    }

    public boolean isSkill2Ready() {
        return isSkill2Ready;
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
        paddleLaserEffect.setX(x);
        if (currentPhase == Phase.CHARGING) {
            if (paddleLaserEffect.isAnimationDone()) {
                currentPhase = Phase.FIRING;
                paddleLaserTime = 0f;
            }

        } else {
            paddleLaserTime += delta;
            if (paddleLaserTime >= 4f) {
                paddleLaserEffect.remove();
                paddleLaserEffect = null;
                currentPhase = Phase.DONE;
                startSkill2Cooldown();
            }
        }

        if (!isSkill2Ready) {
            skill2CooldownTimer -= delta;
            if (skill2CooldownTimer <= 0) {
                isSkill2Ready = true;
            }
        }
    }

    public void cleanup() {
        if (paddleLaserEffect != null) {
            paddleLaserEffect.remove();
            paddleLaserEffect = null;
        }
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
