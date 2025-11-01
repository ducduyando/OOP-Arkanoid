package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.BossSkill;
import io.github.arkanoid.entities.MiniBoss;
import io.github.arkanoid.entities.MiniBossSkill;

import java.util.Random;

public class Boss3NormalRandomSkill implements MiniBossSkill {

    private final MiniBoss owner;
    private MiniBossSkill nextSkill;

    private float skillTimer = 0f;
    private float skillInterval;

    public Boss3NormalRandomSkill(MiniBoss owner) {
        this.owner = owner;
    }

    public void setNextSkill(MiniBossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    @Override
    public void update(MiniBoss miniBoss, float delta) {
        skillTimer += delta;
        if (skillTimer >= skillInterval) {
            if (nextSkill != null) {
                owner.setSkill(nextSkill);
            }
        }
    }

    @Override
    public void enter(MiniBoss miniBoss) {
        skillInterval = (float) (new Random().nextInt(2) + 4);
        skillTimer = 0;
    }

    @Override
    public void cleanup() {

    }
}
