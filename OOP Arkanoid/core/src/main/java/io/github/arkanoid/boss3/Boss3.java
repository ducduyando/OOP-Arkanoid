package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.FinalBoss;

import static io.github.arkanoid.core.Constants.*;

public class Boss3 extends FinalBoss {
    public Boss3(float x, float y, int maxHp) {
        super(x, y, BOSS3_WIDTH, BOSS3_HEIGHT, BOSS3_VELOCITY, maxHp);

        Boss3Skill1A sawSkill = new Boss3Skill1A(this);

    }

    public void skill1A(float x, float y) {
        if (getStage() != null) {
            getStage().addActor(new Rocket(this.skill1ATexture, ));
        }
    }


    @Override
    public void act(float delta) {
        hitBox.setPosition(getX(), getY());
        if (boss3State == Boss3State.NORMAL) {
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
                if (deathAnimation.isAnimationFinished(deathTimer)) {
                    boss3State = Boss3State.UPGRADE;
                }
            }
        }
    }
}
