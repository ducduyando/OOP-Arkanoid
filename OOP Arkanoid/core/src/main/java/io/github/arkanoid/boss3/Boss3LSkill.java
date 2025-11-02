package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.MiniBoss;
import io.github.arkanoid.entities.MiniBossSkill;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss3LSkill implements MiniBossSkill {

    private Rocket rocket1;
    private Rocket rocket2;
    private Rocket rocket3;

    private Boss3 boss3;
    private Boss3L boss3L;

    private MiniBossSkill nextSkill;

    private float rocketTimer = 0f;
    private final float NEXT_ROCKET_DELAY1 = 0.5f;
    private boolean rocket2Added = false;

    private final float NEXT_ROCKET_DELAY2 = 1f;
    private boolean rocket3Added = false;

    private final float[][] positionTargetX = new float[ROWS][COLS];
    private final float[][] positionTargetY = new float[ROWS][COLS];

    private boolean isBoss3LSkillFinished = false;

    public Boss3LSkill(Boss3 boss3, Boss3L boss3L) {
        this.boss3 = boss3;
        this.boss3L = boss3L;

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

    public void setNextSkill(MiniBossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    @Override
    public void update(MiniBoss miniBoss, float delta) {

        rocketTimer += delta;
        if (rocketTimer >= NEXT_ROCKET_DELAY1 && !rocket2Added) {
            boss3.getStage().addActor(rocket2);
            rocket2Added = true;
        }
        if (rocketTimer >= NEXT_ROCKET_DELAY2 && !rocket3Added) {
            boss3.getStage().addActor(rocket3);
            rocket3Added = true;
        }

        if (rocket1.isRocketSkillFinished() && rocket2.isRocketSkillFinished() && rocket3.isRocketSkillFinished()) {
            isBoss3LSkillFinished = true;
            miniBoss.setSkill(nextSkill);
        }

    }

    @Override
    public void enter(MiniBoss miniBoss) {
        int c;
        int r;
        c = new Random().nextInt(COLS);
        r = new Random().nextInt(ROWS);
        rocket1 = new Rocket(boss3L.getSkillTexture(), boss3L.getTargetTexture(), positionTargetX[r][c], positionTargetY[r][c]);
        c = new Random().nextInt(COLS);
        r = new Random().nextInt(ROWS);
        rocket2 = new Rocket(boss3L.getSkillTexture(), boss3L.getTargetTexture(), positionTargetX[r][c], positionTargetY[r][c]);
        c = new Random().nextInt(COLS);
        r = new Random().nextInt(ROWS);
        rocket3 = new Rocket(boss3L.getSkillTexture(), boss3L.getTargetTexture(), positionTargetX[r][c], positionTargetY[r][c]);

        boss3.getStage().addActor(rocket1);
        isBoss3LSkillFinished = false;

        rocketTimer = 0;
        rocket2Added = false;
        rocket3Added = false;
    }

    @Override
    public void cleanup() {
        if (rocket1 != null) {
            rocket1.remove();
            rocket1 = null;
        }
        if (rocket2 != null) {
            rocket2.remove();
            rocket2 = null;
        }
        if (rocket3 != null) {
            rocket3.remove();
            rocket3 = null;
        }
    }
}
