package io.github.arkanoid.paddle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import static io.github.arkanoid.core.Constants.*;


public class PaddleSkill2A implements PaddleSkill {
    private final Paddle owner;
    private float skill2ACooldownTimer = 0f;
    private boolean isSkill2AReady = true;

    public boolean isSkill2AReady() {
        return isSkill2AReady;
    }

    public void setIsSkill2AReady(boolean ready) {
        this.isSkill2AReady = ready;
    }

    private Texture beeTexture;
    public PaddleSkill2A(Paddle owner) {
        this.owner = owner;
        this.beeTexture = new Texture("PaddleSkill/" + "2a" + ".png");
    }

    @Override
    public void update(Paddle paddle, float delta) {
        if (!isSkill2AReady) {
            skill2ACooldownTimer -= delta;
            if (skill2ACooldownTimer <= 0) {
                isSkill2AReady = true;
            }
        }
    }

    public void fire(Paddle paddle) {
        isSkill2AReady = false;
        skill2ACooldownTimer = PADDLE_SKILL_COOLDOWN;

        float spawnX = paddle.getX() + paddle.getWidth() / 2f;
        float spawnY = paddle.getY() + paddle.getHeight();

        PaddleBeeBullet bee = new PaddleBeeBullet(beeTexture, spawnX, spawnY);
        if (owner.getStage() != null) {
            owner.getStage().addActor(bee);
        }
    }


    @Override
    public void enter(Paddle paddle) {

    }

    @Override
    public void cleanup() {
    }


    public float getSkill2ACooldownTimer() {
        return skill2ACooldownTimer;
    }

    public void setSkill2ACooldownTimer(float timer) {
        this.skill2ACooldownTimer = timer;
    }
}
