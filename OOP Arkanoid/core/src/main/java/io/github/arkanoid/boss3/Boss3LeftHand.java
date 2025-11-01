package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.MiniBoss;

import static io.github.arkanoid.core.Constants.*;

public class Boss3LeftHand extends MiniBoss {
    private Boss3 boss3;


    public Boss3LeftHand(Boss3 boss3, int maxHp) {
        super("3L", BOSS3_LEFT_HAND_WIDTH, BOSS3_LEFT_HAND_HEIGHT, maxHp);
        this.boss3 = boss3;

        float x = boss3.getX() + BOSS3_WIDTH;
        float y = boss3.getHeight();
        setPosition(x, y);
        getHitbox().setPosition(x, y);
        setSize(BOSS3_LEFT_HAND_WIDTH, BOSS3_LEFT_HAND_HEIGHT);
        setOrigin(BOSS3_LEFT_HAND_WIDTH / 2f, BOSS3_LEFT_HAND_HEIGHT / 2f);
    }

    @Override
    public void act(float delta) {
        float x = boss3.getX() + BOSS3_WIDTH;
        float y = boss3.getY();
        setPosition(x, y);
        super.act(delta);
    }
}
