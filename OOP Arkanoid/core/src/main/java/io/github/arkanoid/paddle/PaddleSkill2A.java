package io.github.arkanoid.paddle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import static io.github.arkanoid.core.Constants.*;


public class PaddleSkill2A implements PaddleSkill {
    private final Paddle owner;
    private float skill2ACooldownTimer = 0f;
    private boolean isSkill2AReady = true;

    private float spawnTimer = 0f;
    private int actionCounter = 0;
    private boolean isSkillActivated = false;

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
                actionCounter = 0;
            }
        }
        else {
            if (isSkillActivated) {
                if (actionCounter >= MAX_BEES) {
                    isSkill2AReady = false;
                    isSkillActivated = false;
                    skill2ACooldownTimer = PADDLE_SKILL_COOLDOWN;
                }
                else {
                    spawnTimer += delta;
                    if (spawnTimer >= SPAWN_BEE_DELAY) {
                        spawnTimer = 0;
                        launchNewBee();
                        actionCounter++;
                    }
                }
            }
        }
    }

    public void launchNewBee() {
        if (this.owner.getStage() != null) {
            float spawnX = owner.getX() + owner.getWidth() / 2f;
            float spawnY = owner.getY() + owner.getHeight();

            this.owner.getStage().addActor(new PaddleBeeBullet(beeTexture, spawnX, spawnY));
        }
    }

    public void fire() {
        isSkillActivated = true;
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
