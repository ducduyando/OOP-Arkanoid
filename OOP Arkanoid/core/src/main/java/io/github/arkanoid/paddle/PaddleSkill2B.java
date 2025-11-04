package io.github.arkanoid.paddle;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import static io.github.arkanoid.core.Constants.*;
import static io.github.arkanoid.core.MusicManager.*;



public class PaddleSkill2B implements PaddleSkill {
    private final Paddle owner;
    private float skill2BCooldownTimer = 0f;
    private boolean isSkill2BReady = true;

    public boolean isSkill2BReady() {
        return isSkill2BReady;
    }

    public void setIsSkill2BReady(boolean ready) {
        this.isSkill2BReady = ready;
    }
    PaddleHoneyShield paddleHoneyShield;
    Texture honeyShield;

    private float paddleShieldTime;
    private boolean isSkill2Start;

    private long honeyShieldSoundId;
    public PaddleSkill2B(Paddle paddle) {
        owner = paddle;
        honeyShield = new Texture("PaddleSkill/" + "2b" + ".png");
    }
    @Override
    public void update(Paddle paddle, float delta) {

        if (paddleHoneyShield != null) {
            paddleHoneyShield.setPosition(paddle.getHitBox().getX(), paddle.getHitBox().getY());

        }
        if (!isSkill2BReady) {
            skill2BCooldownTimer -= delta;
            if (skill2BCooldownTimer <= 0) {
                isSkill2BReady = true;
                skill2BCooldownTimer = PADDLE_SKILL_COOLDOWN; // Reset về max để tính toán frame đúng
            }
        }
        if (isSkill2Start) {
            paddleShieldTime += delta;
            if (paddleShieldTime >= HONEY_SHIELD_DURATION) {
                owner.setShield(false);
                isSkill2Start = false;
                cleanup();
            }
        }
    }

    @Override
    public void enter(Paddle paddle) {
        this.honeyShieldSoundId = playEffect("honeyShieldSound");
    }

    public void activate(Paddle paddle) {
        if (isSkill2BReady) {
            if (paddle.getState() > 0) {
                paddle.setState(paddle.getState() - 1);
            }
            paddleHoneyShield = new PaddleHoneyShield(honeyShield, paddle.getHitBox().getX(), paddle.getHitBox().getY(), paddle.getState());
            owner.getStage().addActor(paddleHoneyShield);
            owner.setShield(true);
            isSkill2Start = true;
            isSkill2BReady = false;
            skill2BCooldownTimer = PADDLE_SKILL_COOLDOWN;
            paddleShieldTime = 0;
        }
    }

    @Override
    public void cleanup() {
        if (paddleHoneyShield != null) {
            paddleHoneyShield.remove();
            paddleHoneyShield = null;
            stopEffect("honeyShieldSound", honeyShieldSoundId);
        }
    }

    public boolean isDone() {
        return !isSkill2Start;
    }

    public float getSkill2BCooldownTimer() {
        return skill2BCooldownTimer;
    }

    public void setSkill2BCooldownTimer(float timer) {
        this.skill2BCooldownTimer = timer;
    }
}
