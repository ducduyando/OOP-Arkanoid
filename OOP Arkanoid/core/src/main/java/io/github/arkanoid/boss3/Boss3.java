package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.FinalBoss;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss3 extends FinalBoss {

    Boss3Skill1A rocketSkill;
    Boss3Skill1B sawSkill;
    Boss3RandomMovement randomMovement;

    public Boss3(float x, float y, int maxHp) {
        super(x, y, BOSS3_WIDTH, BOSS3_HEIGHT, BOSS3_VELOCITY, maxHp);

        this.rocketSkill = new Boss3Skill1A(this);
        this.sawSkill = new Boss3Skill1B(this);
        this.randomMovement = new Boss3RandomMovement(this);

        rocketSkill.setNextSkill(randomMovement);
        sawSkill.setNextSkill(randomMovement);

        setSkill(randomMovement);
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

    @Override
    public void act(float delta) {
        hitBox.setPosition(getX(), getY());
        if (this.state == State.NORMAL) {
            randomMovement.updateMovement(this, delta);
            if (state == State.NORMAL) {
                stateTime += delta;
                currentFrame = animation.getKeyFrame(stateTime, true);
            } else if (state == State.TAKING_DAMAGE) {
                takeDamageTimer += delta;

                currentFrame = takeDamageAnimation.getKeyFrame(takeDamageTimer, false);

                if (takeDamageAnimation.isAnimationFinished(takeDamageTimer)) {
                    state = State.NORMAL;
                }
            } else if (state == State.DYING) {
                deathTimer += delta;
                currentFrame = deathAnimation.getKeyFrame(deathTimer, false);
            }
        }
    }
}
