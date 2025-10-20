package io.github.arkanoid.boss2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.entities.Boss;

import static io.github.arkanoid.core.Constants.*;

public class Boss2 extends Boss {

    public Boss2(float x, float y, int maxHp) {

        super(2, x, y, BOSS2_WIDTH, BOSS2_HEIGHT, new Vector2(BOSS2_VELOCITY), maxHp);

        Boss2Centering centering = new Boss2Centering(this);
        setSkill(centering);

    }
}
