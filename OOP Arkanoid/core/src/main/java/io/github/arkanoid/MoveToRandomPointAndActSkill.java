package io.github.arkanoid;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;
import java.util.function.Consumer;

import static io.github.arkanoid.Constants.*;
import static io.github.arkanoid.Constants.BOSS1_HEIGHT;
import static io.github.arkanoid.Constants.BOSS1_WIDTH;
import static io.github.arkanoid.Constants.SCREEN_HEIGHT;

public class MoveToRandomPointAndActSkill implements BossSkill {
    private final Consumer<Boss> action;
    private final BossSkill nextSkill;
    private final int repeatCount;

    private float stopTimer = 0f;
    private boolean isStopped = false;
    private int actionCounter = 1;
    private final float[][] positionX = new float[ROWS][COLS];
    private final float[][] positionY = new float[ROWS][COLS];
    private float targetX;
    private float targetY;

    public MoveToRandomPointAndActSkill(int repeatCount, Consumer<Boss> action, BossSkill nextSkill) {
        this.action = action;
        this.nextSkill = nextSkill;
        this.repeatCount = repeatCount;

        float cell_x_size = (float) (SCREEN_WIDTH - BOSS1_WIDTH) / COLS;
        float cell_y_size = (float) (SCREEN_HEIGHT / 2 - BOSS1_HEIGHT) / ROWS;

        for(int r = 0;r < ROWS;r++) {
            for(int c = 0;c < COLS;c++) {

                positionX[r][c] = c * cell_x_size;
                positionY[r][c] = SCREEN_HEIGHT / 2f + r * cell_y_size;
            }
        }
    }

    private void chooseNewTarget() {
        Random random = new Random();
        int r = random.nextInt(ROWS);
        int c = random.nextInt(COLS);
        targetX = positionX[r][c];
        targetY = positionY[r][c];
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
                stopTimer = 0;
                if (actionCounter >= repeatCount) {
                    boss.setSkill(nextSkill);
                } else {
                    chooseNewTarget();
                }
            }
        } else {
            Vector2 currentPos = new Vector2(boss.getX(), boss.getY());
            Vector2 targetPos = new Vector2(targetX, targetY);
            if (currentPos.dst(targetPos) < boss.velocity.len() * delta) {
                boss.setPosition(targetPos.x, targetPos.y);
                isStopped = true;
                action.accept(boss);
                actionCounter++;
            } else {
                Vector2 dir = targetPos.sub(currentPos).nor();
                boss.moveBy(dir.x * boss.velocity.x * delta, dir.y * boss.velocity.y * delta);
            }
        }
    }
}
