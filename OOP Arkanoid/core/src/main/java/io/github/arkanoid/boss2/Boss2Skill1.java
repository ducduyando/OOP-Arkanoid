package io.github.arkanoid.boss2;

import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.BossSkill;
import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss2Skill1 implements BossSkill {
    private final Boss2 owner;
    private BossSkill nextSkill;
    private final Boss2RandomMovement movementController;

    private boolean isSkill1Done = true;

    private final float SPAWN_INTERVAL = 5f;
    private int beeCountToSpawn = 0;

    private float stopTimer = 0f;
    private boolean isStopped = false;
    private int actionCounter = 0;
    private float spawnDelayTimer = 0f;
    private final float SPAWN_BEE_DELAY = 0.5f;

    private float targetX;
    private final float[] positionGridX = new float [COLS];

    private final Random random = new Random();

    public Boss2Skill1(Boss2 owner, Boss2RandomMovement movementController) {
        this.owner = owner;
        this.movementController = movementController;

    }

    public void setNextSkill(BossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    public void chooseNewTarget() {
        Random random = new Random();
        int c = random.nextInt(COLS);
        targetX = positionGridX[c];
    }

    public boolean isSkill1Done() {
        return isSkill1Done;
    }

    public void cleanup() {}

    public void enter(Boss boss) {
        this.actionCounter = 0;
        this.isStopped = false;
        this.stopTimer = 0f;
        this.isSkill1Done = false;

        movementController.chooseRandomTarget();
        movementController.setHasArrived(false);
    }
    public void update(Boss boss, float delta) {
            stopTimer += delta;
         // moi 5 giay la bat dau tha ong
        if(!isStopped) {
            if (stopTimer >= SPAWN_INTERVAL) {
                stopTimer = 0f;
                isStopped = true; // Chuyen sang che do tha ong
                beeCountToSpawn = random.nextInt(3) + 5;
                actionCounter = 0;
                spawnDelayTimer = 0f;

                // Sau khi kích hoạt thả ong, Boss nên chuyển sang vị trí mới để chuẩn bị cho chuỗi tiếp theo
                movementController.chooseRandomTarget();
                movementController.setHasArrived(false);
            }
        }
        // tha tung con ong lan luot
        if(isStopped) {
            spawnDelayTimer += delta;

            if(spawnDelayTimer >= SPAWN_BEE_DELAY && actionCounter < beeCountToSpawn) {
                spawnDelayTimer = 0f;
                owner.spawnBeeFromTop();

                actionCounter++;

                if(actionCounter >= beeCountToSpawn) {
                    isStopped = false;
                    stopTimer = 0f;

                    if (nextSkill != null) {
                        isSkill1Done = true;
                        boss.setSkill(nextSkill);
                    }
                }
            }
        }

    }
}
