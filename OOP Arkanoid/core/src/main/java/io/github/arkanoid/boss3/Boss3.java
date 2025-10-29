package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.Boss;

import java.awt.*;

import static io.github.arkanoid.core.Constants.*;

public class Boss3 extends Boss {

    public enum bossState {
        NORMAL,
        UPGRADE
    }

    private Boss3RandomMovement randomMovement;


    public Boss3(int number, float x, float y, int maxHp) {
        super(number, x, y, BOSS3_WIDTH, BOSS3_HEIGHT, BOSS3_VELOCITY, maxHp);
        randomMovement = new Boss3RandomMovement(this);
    }
}
