package io.github.arkanoid.boss3;

import io.github.arkanoid.entities.MiniBoss;

import static io.github.arkanoid.core.Constants.*;

public class Boss3L extends MiniBoss {
    private Boss3 boss3;


    public Boss3L(Boss3 boss3, int maxHp) {
        super("3L", BOSS3_LEFT_WIDTH, BOSS3_LEFT_HEIGHT, maxHp);
        this.boss3 = boss3;

        float x = boss3.getX() + BOSS3_WIDTH;
        float y = boss3.getY();
        setPosition(x, y);
        hitbox.setPosition(x, y);
        setSize(BOSS3_LEFT_WIDTH, BOSS3_LEFT_HEIGHT);
        setOrigin(BOSS3_LEFT_WIDTH / 2f, BOSS3_LEFT_HEIGHT / 2f);
    }

    @Override
    public void act(float delta) {
        float x = boss3.getX() + BOSS3_WIDTH;
        float y = boss3.getY();
        setPosition(x, y);
        super.act(delta);
    }
}
