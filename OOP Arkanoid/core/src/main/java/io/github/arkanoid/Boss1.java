package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

import static io.github.arkanoid.Constants.*;

public class Boss1 extends Boss {
    private final Texture bombTexture;
    private final Texture laserTexture;

    Boss1(Texture texture, Texture bombTexture, Texture laserTexture, float x, float y, int maxHp) {
        super(texture ,x, y, BOSS1_WIDTH, BOSS1_HEIGHT, BOSS1_VELOCITY_X, BOSS1_VELOCITY_Y, maxHp);
        this.bombTexture = bombTexture;
        this.laserTexture = laserTexture;

        Consumer<Boss> dropBombAction = (b) -> {
            if (getStage() != null) {
                getStage().addActor(new Boss1Skill1(this.bombTexture, b.getX() + b.getWidth() / 2f, b.getY()));
            }
        };

        Consumer<Boss> shootLaserAction = (b) -> {
            if (getStage() != null) {
                getStage().addActor(new Boss1Skill2(this.laserTexture, b.getX(), b.getY()));
            }
        };

        StationaryAttackSkill laserSkill = new StationaryAttackSkill(5f, shootLaserAction);

        MoveToRandomPointAndActSkill bombingSkill = new MoveToRandomPointAndActSkill(3, dropBombAction, laserSkill);

        setSkill(bombingSkill);
    }

}
