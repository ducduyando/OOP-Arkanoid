package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import io.github.arkanoid.entities.*;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss3Skill1A implements FinalBossSkill {

    private Boss3 owner;
    private FinalBossSkill nextSkill;
    private final int REPEAT_COUNT = 3;

    private int actionCounter = 0;
    private float stopTimer = 0f;

    private final float[][] positionTargetX = new float[ROWS][COLS];
    private final float[][] positionTargetY = new float[ROWS][COLS];

    private boolean isBoss3LSkillFinished = false;

    public Boss3Skill1A(Boss3 owner) {
        this.owner = owner;


        float cellXSize = (SCREEN_WIDTH - BOSS3_SKILL_LEFT_WIDTH) / ((float) (COLS - 1));
        float cellYSize = (SCREEN_HEIGHT * 0.6f - BOSS3_SKILL_LEFT_HEIGHT) / ((float) (ROWS - 1));

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                positionTargetX[r][c] = cellXSize * c + BOSS3_SKILL_LEFT_WIDTH / 2f;
                positionTargetY[r][c] = cellYSize * r + BOSS3_SKILL_LEFT_HEIGHT / 2f;
            }
        }
    }

    public boolean isBoss3LSkillFinished() {
        return isBoss3LSkillFinished;
    }

    public void setNextSkill(FinalBossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    public void launchNewRocket() {
        Random random = new Random();
        int r = random.nextInt(ROWS);
        int c = random.nextInt(COLS);
        float targetX = positionTargetX[r][c];
        float targetY = positionTargetY[r][c];
        owner.skill1A(targetX, targetY);
    }

    @Override
    public void update(FinalBoss finalBoss, float delta) {
        if (actionCounter >= REPEAT_COUNT) {
            finalBoss.setSkill(nextSkill);
        }
        else {
            launchNewRocket();
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
