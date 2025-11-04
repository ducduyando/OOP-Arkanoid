package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.FinalBoss;
import io.github.arkanoid.entities.FinalBossSkill;
import static io.github.arkanoid.core.MusicManager.*;

public class Boss3Skill2A implements FinalBossSkill {

    FinalBossSkill nextSkill;
    Boss3 boss3;

    public Boss3Skill2A(Boss3 owner) {
        boss3 = owner;
    }

    public void setNextSkill(FinalBossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    @Override
    public void update(FinalBoss finalBoss, float delta) {
        if (boss3.isSkill2AFinished()) {
            finalBoss.setSkill(nextSkill);
        }
    }

    @Override
    public void enter(FinalBoss finalBoss) {

        boss3.setUsingSkill2A(true);
        boss3.skill2A();
        boss3.setSkill2AFinished(false);
    }

    @Override
    public void cleanup() {

    }
}
