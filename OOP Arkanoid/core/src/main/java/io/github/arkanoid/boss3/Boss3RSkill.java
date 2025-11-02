package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.MiniBoss;
import io.github.arkanoid.entities.MiniBossSkill;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss3RSkill implements MiniBossSkill {

    Boss3 boss3;
    Boss3R boss3RightHand;

    private Saw verticalSaw;
    private Saw horizontalSaw;

    private MiniBossSkill nextSkill;

    private boolean isBoss3RSkillDone = false;

    private final float[] positionTargetX = new float[ROWS];
    private final float[] positionTargetY = new float[COLS];

    public Boss3RSkill(Boss3 boss3, Boss3R boss3RightHand) {
        this.boss3 = boss3;
        this.boss3RightHand = boss3RightHand;

        float cellXSize = (SCREEN_WIDTH - BOSS3_SKILL_RIGHT_WIDTH) / (float) (COLS);
        float cellYSize = (SCREEN_HEIGHT - BOSS3_SKILL_RIGHT_HEIGHT - HP_HEIGHT) / (float) (ROWS);

        for (int r = 0; r < ROWS; r++) {
            positionTargetX[r] = BOSS3_SKILL_RIGHT_WIDTH / 2f + r * cellXSize;
        }
        for (int c = 0; c < COLS; c++) {
            positionTargetY[c] = BOSS3_SKILL_RIGHT_HEIGHT / 2f + c * cellYSize;
        }
    }

    public boolean isBoss3RSkillDone() {
        return isBoss3RSkillDone;
    }

    public void setNextSkill(MiniBossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    @Override
    public void update(MiniBoss miniBoss, float delta) {
        if (horizontalSaw.isSawSkillDone() && verticalSaw.isSawSkillDone()) {
            isBoss3RSkillDone = true;
            miniBoss.setSkill(nextSkill);
        }
    }

    @Override
    public void enter(MiniBoss miniBoss) {
        horizontalSaw = new Saw(boss3RightHand.getSkillTexture(), boss3RightHand.getTargetTexture(),
            positionTargetX[new Random().nextInt(ROWS)], 0);

        verticalSaw = new Saw(boss3RightHand.getSkillTexture(), boss3RightHand.getTargetTexture(),
            0, positionTargetY[new Random().nextInt(COLS)]);

        boss3.getStage().addActor(horizontalSaw);
        boss3.getStage().addActor(verticalSaw);

        isBoss3RSkillDone = false;
    }

    @Override
    public void cleanup() {
        if (horizontalSaw != null) {
            horizontalSaw.remove();
            horizontalSaw = null;
        }
        if (verticalSaw != null) {
            verticalSaw.remove();
            verticalSaw = null;
        }
    }
}
