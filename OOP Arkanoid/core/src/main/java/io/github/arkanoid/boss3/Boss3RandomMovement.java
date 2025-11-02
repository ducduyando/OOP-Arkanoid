package io.github.arkanoid.boss3;

import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.BossSkill;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;
import static io.github.arkanoid.core.Constants.HP_HEIGHT;
import static io.github.arkanoid.core.Constants.SCREEN_HEIGHT;

public class Boss3RandomMovement implements BossSkill {

    private final Boss3 owner;
    private BossSkill nextSkill;
    private boolean hasArrived = false;
    private float cooldownTimer = 0f;
    private final float COOLDOWN_DURATION = 2f;

    private float skillTimer = 0f;
    private final float SKILL_INTERVAL = 5f;

    private float targetX;
    private float targetY;
    private final float[][] positionGridX = new float [ROWS][COLS];
    private final float[][] positionGridY = new float [ROWS][COLS];

    private Vector2 targetPosition;
    private final Random random = new Random();

    public Boss3RandomMovement(Boss3 owner) {
        this.owner = owner;

        float cellXSize = (SCREEN_WIDTH - BOSS3_WIDTH - BOSS3_LEFT_WIDTH - BOSS3_RIGHT_WIDTH) / (float) (COLS);
        float cellYSize = (SCREEN_HEIGHT / 2f - BOSS3_HEIGHT - HP_HEIGHT) / (float) (ROWS);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                positionGridX[r][c] = BOSS3_LEFT_WIDTH + c * cellXSize;
                positionGridY[r][c] = SCREEN_HEIGHT / 2f + r * cellYSize;

            }
        }
        chooseRandomTarget();
    }

    public void setNextSkill(BossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    public void chooseRandomTarget() {
        int r = random.nextInt(ROWS);
        int c = random.nextInt(COLS);
        targetX = positionGridX[r][c];
        targetY = positionGridY[r][c];
        targetPosition = new Vector2(targetX, targetY);
    }

    @Override
    public void update(Boss boss, float delta) {

    }

    public void updateMovement(Boss boss, float delta) {
        if (!hasArrived) {
            Vector2 currentPosition = new Vector2(boss.getX(), boss.getY());

            if (currentPosition.dst(targetPosition) < boss.velocity.len() * delta) {
                boss.setPosition(targetPosition.x, targetPosition.y);
                hasArrived = true;
                cooldownTimer = 0f;
            } else {
                Vector2 direction = targetPosition.cpy().sub(currentPosition).nor();
                boss.moveBy(direction.x * boss.velocity.x * delta, direction.y * boss.velocity.y * delta);
            }
        } else {
            cooldownTimer += delta;

            if (cooldownTimer >= COOLDOWN_DURATION) {
                chooseRandomTarget();
                hasArrived = false;
            }
        }
    }


    @Override
    public void enter(Boss boss) {

        this.hasArrived = false;
        this.cooldownTimer = 0f;
        skillTimer = 0f;
        chooseRandomTarget();

    }

    @Override
    public void cleanup() {

    }
}
