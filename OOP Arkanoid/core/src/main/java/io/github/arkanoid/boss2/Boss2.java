package io.github.arkanoid.boss2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.boss1.BombProjectile;
import io.github.arkanoid.boss1.LaserEffect;
import io.github.arkanoid.entities.Boss;
import static io.github.arkanoid.core.Constants.*;
import java.util.Random;

public class Boss2 extends Boss {

    private final BossRandomMovement movementController;
    private final Random random = new Random();
    public Boss2(int number, float x, float y, int maxHp) {
        super(number, x, y, BOSS2_WIDTH, BOSS2_HEIGHT, BOSS2_VELOCITY, maxHp);

        this.movementController = new BossRandomMovement(this);


        Boss2Skill1 beeSpawningSkill = new Boss2Skill1(this, movementController);
        beeSpawningSkill.setNextSkill(beeSpawningSkill);

        setSkill(beeSpawningSkill);
    }

    public void act(float delta) {
        movementController.update(this, delta);
        super.act(delta);
    }

    public Texture getSkill1Texture() {
        return skill1Texture;
    }

    public Texture getSkill2Texture() {
        return skill2Texture;
    }

    public void skill1() {
        if (getStage() != null) {
            getStage().addActor(new BeeEnemy(this.skill1Texture, getX() + getWidth() / 2 + 90 , getY() -40 ));
        }
    }

    /**
    public void skill2() {
        if (getStage() != null) {
            getStage().addActor(new HoneyShield(this.skill2Texture, getX() + getWidth() / 2, getY()));
        }
    }
     */
    public void spawnBeeFromTop() {
        if (getStage() != null) {
            float spawnY = UP_BOUNDARY;
            float spawnX = random.nextFloat() * (SCREEN_WIDTH - BOSS2_SKILL1_WIDTH) + (BOSS2_SKILL1_WIDTH / 2f);

            getStage().addActor(new BeeEnemy(this.skill1Texture, spawnX, spawnY));
        }
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (!isDead()) {
            this.skill1();
        }
    }
}
