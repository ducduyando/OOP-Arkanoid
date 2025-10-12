package io.github.arkanoid;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import static io.github.arkanoid.Constants.*;

public class Boss1BombingSkill implements BossSkill {
    private final Boss1 owner;
    private BossSkill nextSkill;
    private final int REPEAT_COUNT = 3;

    private float stopTimer = 0f;
    private boolean isStopped = false;
    private int actionCounter = 0;
    private float targetX;
    private float targetY;
    private final float[][] positionGridX = new float [ROWS][COLS];
    private final float[][] positionGridY = new float [ROWS][COLS];

    public Boss1BombingSkill(Boss1 owner) {
        this.owner = owner;

        float cellXSize = (SCREEN_WIDTH - BOSS1_WIDTH) / (float) (COLS);
        float cellYSize = (SCREEN_HEIGHT / 2f - BOSS1_HEIGHT - HP_HEIGHT) / (float) (ROWS);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                positionGridX[r][c] = c * cellXSize;
                positionGridY[r][c] = SCREEN_HEIGHT / 2f + r * cellYSize;

            }
        }
    }

    public void setNextSkill(BossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    public void chooseNewTarget() {
        Random random = new Random();
        int r = random.nextInt(ROWS);
        int c = random.nextInt(COLS);
        targetX = positionGridX[r][c];
        targetY = positionGridY[r][c];
    }

    @Override
    public void enter(Boss boss) {
        this.actionCounter = 0;
        this.isStopped = false;
        chooseNewTarget();
    }

    @Override
    public void update(Boss boss, float delta) {
        if (isStopped) {
            stopTimer += delta;
            if (stopTimer >= BOSS_STOP_TIME) {
                isStopped = false;
                stopTimer = 0f;
                if (actionCounter >= REPEAT_COUNT) {
                    boss.setSkill(nextSkill);
                }
                else {
                    chooseNewTarget();
                }
            }
        } else {
            Vector2 currentPos = new Vector2(boss.getX(), boss.getY());
            Vector2 targetPos = new Vector2(targetX, targetY);

            if (currentPos.dst(targetPos) <= boss.velocity.len() * delta) {
                boss.setPosition(targetX, targetY);
                isStopped = true;
                owner.dropBomb();
                actionCounter++;
            } else {
                Vector2 dir = targetPos.sub(currentPos).nor();
                boss.moveBy(dir.x * boss.velocity.x * delta, dir.y * boss.velocity.y * delta);
            }
        }
    }
}
