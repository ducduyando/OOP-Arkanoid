package io.github.arkanoid.boss1;

import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.BossSkill;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss1Skill2 implements BossSkill {
    private enum Phase {
        CHARGING,
        SWEEPING,
    }

    private final Boss1 owner;
    private BossSkill nextSkill;
    private Phase currentPhase;
    private LaserEffect laserInstance;
    private int sweepDirection;

    public Boss1Skill2(Boss1 owner) {
        this.owner = owner;
    }

    public void setNextSkill(BossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    @Override
    public void enter(Boss boss) {
        this.laserInstance = new LaserEffect(owner.getSkill2Texture(), boss.getX(), boss.getY());
        owner.getStage().addActor(this.laserInstance);

        currentPhase = Phase.CHARGING;

        if (new Random().nextBoolean()) {
            sweepDirection = 1;
        } else {
            sweepDirection = -1;
        }
    }

    @Override
    public void cleanup() {
        if (laserInstance != null) {
            laserInstance.remove();
            laserInstance = null;
        }
    }

    @Override
    public void update(Boss boss, float delta) {
        if (laserInstance == null) {
            boss.setSkill(nextSkill);
            return;
        }

        if (currentPhase == Phase.CHARGING) {
            if (laserInstance.isLaserFinished()) {
                currentPhase = Phase.SWEEPING;
            }
        } else  if (currentPhase == Phase.SWEEPING) {
            boss.moveBy(SWEEP_SPEED * sweepDirection * delta, 0);
            float laserX = boss.getX() + BOSS1_WIDTH / 2f - LASER_WIDTH / 2f;
            laserInstance.setX(laserX);

            boolean hitBoundary = (sweepDirection == -1 && boss.getX() <= LEFT_BOUNDARY) ||
                (sweepDirection == 1 && boss.getX() + boss.getWidth() >= RIGHT_BOUNDARY);

            if (hitBoundary) {
                cleanup();
            }
        }
    }

}
