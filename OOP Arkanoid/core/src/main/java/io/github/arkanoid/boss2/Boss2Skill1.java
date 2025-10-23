package io.github.arkanoid.boss2;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.BossSkill;
import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss2Skill1 implements BossSkill {
    private final Boss2 owner;
    private BossSkill nextSkill;
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

    public Boss2Skill1(Boss2 owner) {
        this.owner = owner;

        float cellXSize = (SCREEN_WIDTH - BOSS2_SKILL1_WIDTH) / (float) (COLS);

        for (int c = 0; c < COLS; c++) {
            positionGridX[c] = c *  cellXSize;
        }
    }

    public void setNextSkill(BossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    public void chooseNewTarget() {
        Random random = new Random();
        int c = random.nextInt(COLS);
        targetX = positionGridX[c];
    }

    public void cleanup() {}

    public void enter(Boss boss) {
        this.actionCounter = 0;
        this.isStopped = false;
        chooseNewTarget();
    }
    public void update(Boss boss, float delta) {
        /**     stopTimer += delta;
         // moi 5 giay la bat dau tha ong
        if(!isStopped && stopTimer >= SPAWN_INTERVAL) {
            stopTimer = 0f;
            isStopped = true;
            beeCountToSpawn = random.nextInt(3) + 5;
            actionCounter = 0;
            spawnDelayTimer = 0f;
        }
        // tha tung con ong lan luot
        if(isStopped) {
            spawnDelayTimer += delta;
            if(spawnDelayTimer >= SPAWN_BEE_DELAY && actionCounter < beeCountToSpawn) {
                spawnDelayTimer = 0f;
                spawnBeeBeLowHp(boss.getStage());
                actionCounter++;
                if(actionCounter >= beeCountToSpawn) {
                    isStopped = false;
                    stopTimer = 0f;
                    if (nextSkill != null) {
                        boss.setSkill(nextSkill);
                    }
                }
            }
        }
        */

        if (isStopped) {
            stopTimer += delta;
            if (stopTimer >= SPAWN_BEE_DELAY) {
                isStopped = false;
                stopTimer = 0f;
                if (actionCounter >= beeCountToSpawn) {
                    boss.setSkill(nextSkill);
                }
            }
        } else {
            owner.skill1();
            actionCounter++;
        }
    }
}
