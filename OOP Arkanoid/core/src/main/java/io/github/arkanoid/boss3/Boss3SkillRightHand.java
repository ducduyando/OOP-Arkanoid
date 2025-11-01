package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.MiniBoss;
import io.github.arkanoid.entities.MiniBossSkill;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss3SkillRightHand implements MiniBossSkill {

    Boss3 boss3;
    Boss3RightHand boss3RightHand;

    private Saw sawX;
    private Saw sawY;

    private MiniBossSkill nextSkill;

    private boolean boss3SkillRightHandDone = false;

    private final float[] positionTargetX = new float[ROWS];
    private final float[] positionTargetY = new float[COLS];

    public boolean isBoss3SkillRightHandDone() {
        return boss3SkillRightHandDone;
    }

    public Boss3SkillRightHand(Boss3 boss3, Boss3RightHand boss3RightHand) {
        this.boss3 = boss3;
        this.boss3RightHand = boss3RightHand;

        float cellXSize = (SCREEN_WIDTH - BOSS3_SKILL_RIGHT_HAND_WIDTH) / (float) (COLS);
        float cellYSize = (SCREEN_HEIGHT - BOSS3_SKILL_RIGHT_HAND_HEIGHT - HP_HEIGHT) / (float) (ROWS);

        for (int r = 0; r < ROWS; r++) {
            positionTargetX[r] = BOSS3_SKILL_RIGHT_HAND_WIDTH / 2f + r * cellXSize;
        }
        for (int c = 0; c < COLS; c++) {
            positionTargetY[c] = BOSS3_SKILL_RIGHT_HAND_HEIGHT / 2f + c * cellYSize;
        }
    }

    public void setNextSkill(MiniBossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    @Override
    public void update(MiniBoss miniBoss, float delta) {
        if (sawX.isSawSkillDone() && sawY.isSawSkillDone()) {
            boss3SkillRightHandDone = true;
            miniBoss.setSkill(nextSkill);
        }
    }

    @Override
    public void enter(MiniBoss miniBoss) {
        sawX = new Saw(boss3RightHand.getSkillTexture(), boss3RightHand.getTargetTexture(),
            positionTargetX[new Random().nextInt(ROWS)], 0);

        sawY = new Saw(boss3RightHand.getSkillTexture(), boss3RightHand.getTargetTexture(),
            0, positionTargetY[new Random().nextInt(COLS)]);

        boss3.getStage().addActor(sawX);
        boss3.getStage().addActor(sawY);

        boss3SkillRightHandDone = false;
    }

    @Override
    public void cleanup() {
        if (sawX != null) {
            sawY.remove();
            sawY = null;
        }
        if (sawY != null) {
            sawY.remove();
            sawY = null;
        }
    }
}
