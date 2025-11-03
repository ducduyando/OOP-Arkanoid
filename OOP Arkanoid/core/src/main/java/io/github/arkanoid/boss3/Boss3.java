package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.g2d.Batch;
import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.FinalBoss;
import io.github.arkanoid.paddle.Paddle;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss3 extends FinalBoss {

    public enum Boss3State{
        NORMAL,
        UPGRADE
    }

    Paddle paddle;
    Boss3Skill1A rocketSkill;
    Boss3Skill1B sawSkill;

    Boss3Skill2A spikeSkill;
    Boss3Skill2B laserSkill;

    Boss3RandomMovement randomMovement;
    private boolean isUsingSkill2A = false;
    private boolean isSkill2AFinished = false;

    Boss3State boss3State = Boss3State.NORMAL;

    public Boss3(float x, float y, int maxHp, Paddle paddle) {
        super(x, y, BOSS3_WIDTH, BOSS3_HEIGHT, BOSS3_VELOCITY, maxHp);

        this.paddle = paddle;

        this.rocketSkill = new Boss3Skill1A(this);
        this.sawSkill = new Boss3Skill1B(this);
        this.randomMovement = new Boss3RandomMovement(this);

        rocketSkill.setNextSkill(randomMovement);
        sawSkill.setNextSkill(randomMovement);

        spikeSkill = new Boss3Skill2A(this);
        laserSkill = new Boss3Skill2B(this, paddle);

        spikeSkill.setNextSkill(randomMovement);
        laserSkill.setNextSKill(randomMovement);

        setSkill(randomMovement);
    }

    public Boss3State getBoss3State() {
        return boss3State;
    }

    public Boss3Skill1A getRocketSkill() {
        return rocketSkill;
    }

    public Boss3Skill1B getSawSkill() {
        return sawSkill;
    }

    public Boss3Skill2A getSpikeSkill() {
        return spikeSkill;
    }

    public void setUsingSkill2A(boolean usingSkill2A) {
        isUsingSkill2A = usingSkill2A;
    }

    public boolean isSkill2AFinished() {
        return isSkill2AFinished;
    }

    public void setSkill2AFinished(boolean skill2AFinished) {
        isSkill2AFinished = skill2AFinished;
    }

    public Boss3Skill2B getLaserSkill() {
        return laserSkill;
    }

    public void skill1A(float targetX, float targetY) {
        if (getStage() != null) {
            getStage().addActor(new Rocket(this.skill1ATexture, this.target1ATexture, targetX, targetY));
        }
    }

    public void skill1B(float targetX, float targetY, boolean isVertical) {
        if (getStage() != null) {
            getStage().addActor(new Saw(this.skill1BTexture, this.target1BTexture, targetX, targetY, isVertical));
        }
    }

    public void skill2A() {
        if (getStage() != null) {
            hitBox.setSize(0, 0);
            getStage().addActor(new Spike(this.skill2ATexture, this, getX(), getY(), paddle));
        }
    }

    public void skill2B(float targetX, float targetY, boolean isLeftEye) {
        if (getStage() != null) {
            getStage().addActor(new Laser(this.skill2BTexture, this, paddle, targetX, targetY, isLeftEye));
        }
    }

    @Override
    public void act(float delta) {
        if (boss3State == Boss3State.NORMAL) {
            randomMovement.updateMovement(this, delta);
            hitBox.setPosition(getX(), getY());

            if (state == State.DYING) {
                deathTimer += delta;

                currentFrame = deathAnimation.getKeyFrame(deathTimer, false);

                if (deathAnimation.isAnimationFinished(deathTimer)) {
                    boss3State = Boss3State.UPGRADE;
                    setSkill(randomMovement);
                    state = State.NORMAL;
                    setHp(getMaxHp());
                    deathTimer = 0;
                }
                return;
            }

            if (isDead()) {
                state = State.DYING;
                if (currentSkill != null) {
                    currentSkill.cleanup();
                }
                return;
            }
            if (state == State.TAKING_DAMAGE) {
                takeDamageTimer += delta;

                currentFrame = takeDamageAnimation.getKeyFrame(takeDamageTimer, false);

                if (takeDamageAnimation.isAnimationFinished(takeDamageTimer)) {
                    state = State.NORMAL;
                }
            }
            else if (state == State.NORMAL) {
                stateTime += delta;
                currentFrame = animation.getKeyFrame(stateTime, true);
            }
            currentSkill.update(this, delta);
        }
        else if (boss3State == Boss3State.UPGRADE) {
            randomMovement.updateMovement(this, delta);
            super.act(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isUsingSkill2A) {
            super.draw(batch, parentAlpha);
        }
    }
}
