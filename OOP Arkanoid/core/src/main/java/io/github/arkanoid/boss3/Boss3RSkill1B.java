package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.MiniBoss;
import io.github.arkanoid.entities.MiniBossSkill;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss3RSkill1B implements MiniBossSkill {

    Boss3 boss3;
    Boss3R boss3R;

    private Saw verticalSaw;
    private Saw horizontalSaw;

    private MiniBossSkill nextSkill;

    private boolean isBoss3RSkillFinished = false;

    private final float[] positionTargetX = new float[COLS];
    private final float[] positionTargetY = new float[ROWS];

    public Boss3RSkill1B(Boss3 boss3, Boss3R boss3RightHand) {
        this.boss3 = boss3;
        this.boss3R = boss3RightHand;

        float cellXSize = (SCREEN_WIDTH - BOSS3_SKILL_RIGHT_WIDTH) / (float) (COLS);
        float cellYSize = (SCREEN_HEIGHT - BOSS3_SKILL_RIGHT_HEIGHT - HP_HEIGHT) / (float) (ROWS);

        for (int c = 0; c < COLS; c++) {
            positionTargetX[c] = BOSS3_SKILL_RIGHT_WIDTH / 2f + c * cellXSize;
        }
        for (int r = 0; r < ROWS; r++) {
            positionTargetY[r] = BOSS3_SKILL_RIGHT_HEIGHT / 2f + r * cellYSize;
        }
    }

    public boolean isBoss3RSkillFinished() {
        return isBoss3RSkillFinished;
    }

    public void setNextSkill(MiniBossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    @Override
    public void update(MiniBoss miniBoss, float delta) {
        if (horizontalSaw.isSawSkillFinished() && verticalSaw.isSawSkillFinished()) {
            isBoss3RSkillFinished = true;
            miniBoss.setSkill(nextSkill);
        }
    }

    @Override
    public void enter(MiniBoss miniBoss) {
        horizontalSaw = new Saw(boss3R.getSkillTexture(), boss3R.getTargetTexture(),
            positionTargetX[new Random().nextInt(COLS)], 0);

        verticalSaw = new Saw(boss3R.getSkillTexture(), boss3R.getTargetTexture(),
            0, positionTargetY[new Random().nextInt(ROWS)]);

        boss3.getStage().addActor(horizontalSaw);
        boss3.getStage().addActor(verticalSaw);

        isBoss3RSkillFinished = false;
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
