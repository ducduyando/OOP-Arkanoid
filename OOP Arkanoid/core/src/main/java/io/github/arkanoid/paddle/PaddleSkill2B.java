package io.github.arkanoid.paddle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

import static io.github.arkanoid.core.Constants.*;


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

    public PaddleSkill2B(Paddle paddle) {
        owner = paddle;
        honeyShield = new Texture("PaddleSkill/" + "2b" + ".png");
    }
    @Override
    public void update(Paddle paddle, float delta) {

        if (paddleHoneyShield != null) {
            paddleHoneyShield.setPosition(paddle.getX(), paddle.getY());

        }
        if (!isSkill2BReady) {
            skill2BCooldownTimer += delta;
            if (skill2BCooldownTimer >= PADDLE_SKILL_COOLDOWN) {
                isSkill2BReady = true;
                skill2BCooldownTimer = PADDLE_SKILL_COOLDOWN; // Reset về max để tính toán frame đúng
            }
        }
        if (isSkill2Start) {
            paddleShieldTime += delta;
            if (paddleShieldTime >= 4f) {
                owner.setInvincible(false);
                isSkill2Start = false;
                cleanup();
            }
        }

        // Không xử lý input ở đây nữa, để Boss3Stage xử lý thông qua InputManager

    }

    @Override
    public void enter(Paddle paddle) {
        if (paddle.getState() > 0) {
            paddle.setState(paddle.getState() - 1);
            paddle.setPosition(paddle.getX() - 34, paddle.getY());
        }
        paddleHoneyShield = new PaddleHoneyShield(honeyShield, paddle.getX(), paddle.getY(), paddle.getState());
        skill2BCooldownTimer = 0;
        isSkill2Start = true;
        paddleShieldTime = 0;
    }

    public void activate(Paddle paddle) {
        if (isSkill2BReady) {
            paddleHoneyShield = new PaddleHoneyShield(honeyShield, paddle.getX(), paddle.getY(), paddle.getState());
            owner.getStage().addActor(paddleHoneyShield);
            owner.setInvincible(true);
            isSkill2Start = true;
            isSkill2BReady = false;
            skill2BCooldownTimer = 0;
            paddleShieldTime = 0;
        }
    }

    @Override
    public void cleanup() {
        if (paddleHoneyShield != null) {
            paddleHoneyShield.remove();
            paddleHoneyShield = null;
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
