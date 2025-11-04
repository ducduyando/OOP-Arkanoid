package io.github.arkanoid.boss3;

import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.entities.FinalBoss;
import io.github.arkanoid.entities.FinalBossSkill;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;
import static io.github.arkanoid.core.Constants.HP_HEIGHT;
import static io.github.arkanoid.core.Constants.SCREEN_HEIGHT;

public class Boss3RandomMovement implements FinalBossSkill {

    private final Boss3 owner;
    private FinalBossSkill nextSkill;
    private boolean hasArrived = false;
    private float cooldownTimer = 0f;
    private float skillTimer = 0f;

    private float targetX;
    private float targetY;
    private final float[][] positionGridX = new float [ROWS][COLS];
    private final float[][] positionGridY = new float [ROWS][COLS];

    private Vector2 targetPosition;
    private final Random random = new Random();

    public void setNextSkill(FinalBossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    public Boss3RandomMovement(Boss3 owner) {
        this.owner = owner;

        float cellXSize = (SCREEN_WIDTH - BOSS3_WIDTH) / (float) (COLS);
        float cellYSize = (SCREEN_HEIGHT / 2f - BOSS3_HEIGHT - HP_HEIGHT) / (float) (ROWS);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                positionGridX[r][c] = c * cellXSize;
                positionGridY[r][c] = SCREEN_HEIGHT / 2f + r * cellYSize;

            }
        }
        chooseRandomTarget();
    }

    public void chooseRandomTarget() {
        int r = random.nextInt(ROWS);
        int c = random.nextInt(COLS);
        targetX = positionGridX[r][c];
        targetY = positionGridY[r][c];
        targetPosition = new Vector2(targetX, targetY);
    }

    public void updateMovement(FinalBoss finalBoss, float delta) {
        if (!hasArrived) {
            Vector2 currentPosition = new Vector2(finalBoss.getX(), finalBoss.getY());

            if (currentPosition.dst(targetPosition) < finalBoss.velocity.len() * delta) {
                finalBoss.setPosition(targetPosition.x, targetPosition.y);
                hasArrived = true;
                cooldownTimer = 0f;
            } else {
                Vector2 direction = targetPosition.cpy().sub(currentPosition).nor();
                finalBoss.moveBy(direction.x * finalBoss.velocity.x * delta, direction.y * finalBoss.velocity.y * delta);
            }
        } else {
            cooldownTimer += delta;

            if (cooldownTimer >= BOSS3_COOLDOWN_DURATION) {
                chooseRandomTarget();
                hasArrived = false;
            }
        }
    }


    @Override
    public void update(FinalBoss finalBoss, float delta) {

        skillTimer += delta;
        if (skillTimer >= BOSS3_SKILL_INTERVAL) {
            if (owner.getBoss3State() == Boss3.Boss3State.NORMAL) {
                if (new Random().nextBoolean()) {
                    nextSkill = owner.getRocketSkill();
                } else {
                    nextSkill = owner.getChainsawSkill();
                }
            }
            else {
                if (new Random().nextBoolean()) {
                    nextSkill = owner.getSpikeSkill();
                } else {
                    nextSkill = owner.getLaserSkill();
                }
            }
            finalBoss.setSkill(nextSkill);
        }

    }

    @Override
    public void enter(FinalBoss finalBoss) {

        this.hasArrived = false;
        this.cooldownTimer = 0f;
        skillTimer = 0f;
        chooseRandomTarget();

    }

    @Override
    public void cleanup() {

    }
}
