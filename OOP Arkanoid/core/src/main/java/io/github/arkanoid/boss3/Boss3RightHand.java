package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.MiniBoss;

import static io.github.arkanoid.core.Constants.*;

public class Boss3RightHand extends MiniBoss {

    private Boss3 boss3;
    private final float ROTATION_SPEED = 180f;

    public Boss3RightHand(Boss3 boss3, int maxHp) {
        super("3R", BOSS3_RIGHT_HAND_WIDTH, BOSS3_RIGHT_HAND_HEIGHT, maxHp);
        this.boss3 = boss3;

        float x = boss3.getX() - BOSS3_RIGHT_HAND_WIDTH;
        float y = boss3.getHeight();
        setPosition(x, y);
        getHitbox().setPosition(x, y);
        setSize(BOSS3_RIGHT_HAND_WIDTH, BOSS3_RIGHT_HAND_HEIGHT);
        setOrigin(BOSS3_RIGHT_HAND_WIDTH / 2f, BOSS3_RIGHT_HAND_HEIGHT / 2f);
    }

    @Override
    public void act(float delta) {
        this.rotateBy(ROTATION_SPEED * delta);
        float x = boss3.getX() - BOSS3_RIGHT_HAND_WIDTH;
        float y = boss3.getY();
        setPosition(x, y);
        super.act(delta);
    }
}
