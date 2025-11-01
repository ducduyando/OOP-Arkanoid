package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.MiniBossSkill;

import static io.github.arkanoid.core.Constants.*;

public class Boss3 extends Boss {

    public enum Boss3State {
        NORMAL,
        UPGRADE
    }

    private Boss3RandomMovement randomMovement;
    private Boss3RightHand boss3RightHand;
    private Boss3LeftHand boss3LeftHand;

    private Boss3State boss3State = Boss3State.NORMAL;


    public Boss3(int number, float x, float y, int maxHp) {
        super(number, x, y, BOSS3_WIDTH, BOSS3_HEIGHT, BOSS3_VELOCITY, maxHp);
        randomMovement = new Boss3RandomMovement(this);
        boss3LeftHand = new Boss3LeftHand(this, 3);
        boss3RightHand = new Boss3RightHand(this, 3);

        getStage().addActor(boss3RightHand);
        getStage().addActor(boss3LeftHand);
    }

    public Boss3RightHand getBoss3RightHand() {
        return boss3RightHand;
    }

    public Boss3LeftHand getBoss3LeftHand() {
        return boss3LeftHand;
    }

    @Override
    public void act(float delta) {
        if (boss3State == Boss3State.NORMAL) {
            randomMovement.updateMovement(this, delta);

        }
    }
}
