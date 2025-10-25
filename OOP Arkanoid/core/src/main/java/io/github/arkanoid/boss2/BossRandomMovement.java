package io.github.arkanoid.boss2;
import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.BossSkill;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class BossRandomMovement implements BossSkill {
    private final Boss2 owner;
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

    public BossRandomMovement(Boss2 owner) {
        this.owner = owner;

        float cellXSize = (SCREEN_WIDTH - BOSS2_WIDTH) / (float) (COLS);
        float cellYSize = (SCREEN_HEIGHT / 2f - BOSS2_HEIGHT - HP_HEIGHT) / (float) (ROWS);

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

    public void setHasArrived(boolean hasArrived) {
        this.hasArrived = hasArrived;
    }

    public void setNextSkill(BossSkill nextSkill) {

    }

    @Override
    public void cleanup() {}

    @Override
    public void enter(Boss boss) {
        this.hasArrived = false;
        this.cooldownTimer = 0f;
        chooseRandomTarget();
    }



    @Override
    public void update(Boss boss, float delta) {

        skillTimer += delta;
        if (skillTimer >= SKILL_INTERVAL
            && owner.getBeeSpawningSkill().isSkill1Done()
            && owner.getShieldSkill().isSkill2Done()) {

            skillTimer = 0;
            if (new Random().nextBoolean()) {
                nextSkill = owner.getShieldSkill();
            } else {
                nextSkill = owner.getBeeSpawningSkill();
            }
            boss.setSkill(nextSkill);
            return;
        }

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
}
