package io.github.arkanoid.paddle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

import static io.github.arkanoid.core.Constants.*;


public class PaddleSkill2B implements PaddleSkill {

    private final Paddle owner;
    private float skill2BCooldownTimer = 0f;
    private boolean isSkill2BReady = true;
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

        paddleHoneyShield.setPosition(paddle.getX(), paddle.getY());
        if (!isSkill2BReady) {
            skill2BCooldownTimer += delta;
            if (skill2BCooldownTimer >= PADDLE_SKILL_COOLDOWN) {
                isSkill2BReady = true;
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.K) && isSkill2BReady) {
            owner.getStage().addActor(paddleHoneyShield);
            owner.setInvincible(true);
            isSkill2Start = true;
            isSkill2BReady = false;
        }

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

    @Override
    public void cleanup() {
        if (paddleHoneyShield != null) {
            paddleHoneyShield.remove();
            paddleHoneyShield = null;
        }
    }
}
