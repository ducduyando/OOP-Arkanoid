package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.FinalBoss;
import io.github.arkanoid.entities.FinalBossSkill;
import io.github.arkanoid.entities.MiniBoss;
import io.github.arkanoid.entities.MiniBossSkill;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss3Skill1B implements FinalBossSkill {
    Boss3 owner;
    private FinalBossSkill nextSkill;
    private final int REPEAT_COUNT = 2;

    private int actionCounter = 0;

    private final float[] positionTargetX = new float[COLS];
    private final float[] positionTargetY = new float[ROWS];

    private boolean isBoss3Skill1BFinished = false;

    public Boss3Skill1B(Boss3 owner) {
        this.owner = owner;

        float cellXSize = (SCREEN_WIDTH - BOSS3_SKILL_RIGHT_WIDTH) / (float) (COLS);
        float cellYSize = (SCREEN_HEIGHT - BOSS3_SKILL_RIGHT_HEIGHT - HP_HEIGHT) / (float) (ROWS);

        for (int c = 0; c < COLS; c++) {
            positionTargetX[c] = BOSS3_SKILL_RIGHT_WIDTH / 2f + c * cellXSize;
        }
        for (int r = 0; r < ROWS; r++) {
            positionTargetY[r] = BOSS3_SKILL_RIGHT_HEIGHT / 2f + r * cellYSize;
        }
    }

    public boolean isBoss3Skill1BFinished() {
        return isBoss3Skill1BFinished;
    }

    public void setNextSkill(FinalBossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    public void launchNewSaw(boolean isVertical) {
        Random random = new Random();
        int r = random.nextInt(ROWS);
        int c = random.nextInt(COLS);
        float targetX = positionTargetX[c];
        float targetY = positionTargetY[r];
        if (isVertical) {
            owner.skill1B(targetX, 0, isVertical);
        }
        else {
            owner.skill1B(0, targetY, isVertical);

        }
    }

    @Override
    public void update(FinalBoss finalBoss, float delta) {
        if (actionCounter >= REPEAT_COUNT) {
            finalBoss.setSkill(nextSkill);
        }
        else {
            if (actionCounter == 0) {
                launchNewSaw(true);
            }
            else {
                launchNewSaw(false);
            }
            actionCounter++;
        }
    }

    @Override
    public void enter(FinalBoss finalBoss) {
        this.actionCounter = 0;
    }

    @Override
    public void cleanup() {
    }
}
