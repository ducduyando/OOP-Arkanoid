package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.FinalBoss;
import io.github.arkanoid.entities.FinalBossSkill;
import io.github.arkanoid.paddle.Paddle;
import static io.github.arkanoid.core.Constants.*;

public class Boss3Skill2B implements FinalBossSkill {

    Boss3 owner;
    Paddle paddle;

    private final int REPEAT_COUNT = 5;
    private int actionCounter = 0;

    private final float LASER_DELAY = 0.5f;
    private float nextLaserTimer = 0f;

    private FinalBossSkill nextSKill;

    public Boss3Skill2B(Boss3 owner, Paddle paddle) {
        this.owner = owner;
        this.paddle = paddle;
    }

    public void setNextSKill(FinalBossSkill nextSKill) {
        this.nextSKill = nextSKill;
    }

    @Override
    public void update(FinalBoss finalBoss, float delta) {
        if (actionCounter >= REPEAT_COUNT) {
            finalBoss.setSkill(nextSKill);
        }
        else {
            nextLaserTimer += delta;
            if (nextLaserTimer >= LASER_DELAY) {
                nextLaserTimer = 0;
                owner.skill2B(paddle.getX() + PADDLE_WIDTH / 2f, paddle.getY() + PADDLE_HEIGHT, true);
                owner.skill2B(paddle.getX() + PADDLE_WIDTH / 2f, paddle.getY() + PADDLE_HEIGHT, false);

                actionCounter++;
            }
        }
    }

    @Override
    public void enter(FinalBoss finalBoss) {
        actionCounter = 0;
        nextLaserTimer = 0;
    }

    @Override
    public void cleanup() {

    }
}
