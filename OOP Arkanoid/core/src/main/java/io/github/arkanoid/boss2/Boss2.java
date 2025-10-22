package io.github.arkanoid.boss2;

import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.entities.Boss;

import static io.github.arkanoid.core.Constants.*;

public class Boss2 extends Boss {

    public Boss2(int number ,float x, float y, int maxHp) {
        super(number, x, y, BOSS2_WIDTH, BOSS2_HEIGHT, new Vector2(BOSS2_VELOCITY), maxHp);

        BossRandomMovement movement = new BossRandomMovement(this);
        setSkill(movement);

    }
}
