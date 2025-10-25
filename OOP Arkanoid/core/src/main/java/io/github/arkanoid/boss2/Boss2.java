package io.github.arkanoid.boss2;

import com.badlogic.gdx.graphics.Texture;
import io.github.arkanoid.boss1.Boss1Skill1;
import io.github.arkanoid.entities.Boss;
import static io.github.arkanoid.core.Constants.*;
import java.util.Random;

public class Boss2 extends Boss {

    private final BossRandomMovement randomMovement;
    private final Random random = new Random();

    Boss2Skill1 beeSpawningSkill;
    Boss2Skill2 shieldSkill;

    public Boss2(int number, float x, float y, int maxHp) {
        super(number, x, y, BOSS2_WIDTH, BOSS2_HEIGHT, BOSS2_VELOCITY, maxHp);

        this.randomMovement = new BossRandomMovement(this);


        HoneyShield honeyShield = new HoneyShield(this.skill2Texture, getX(), getY());

        beeSpawningSkill = new Boss2Skill1(this, randomMovement);
        shieldSkill = new Boss2Skill2(this, honeyShield);

        beeSpawningSkill.setNextSkill(randomMovement);
        shieldSkill.setNextSkill(randomMovement);

        setSkill(randomMovement);
    }

    public Boss2Skill2 getShieldSkill() {
        return shieldSkill;
    }

    public Boss2Skill1 getBeeSpawningSkill() {
        return beeSpawningSkill;
    }

    public void act(float delta) {
        randomMovement.update(this, delta);
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
            Random random = new Random();
            float spawnX = random.nextFloat((SCREEN_WIDTH - BOSS2_SKILL1_WIDTH) + (BOSS2_SKILL1_WIDTH / 2f));

            getStage().addActor(new BeeEnemy(this.skill1Texture, spawnX, spawnY));
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (state == State.NORMAL) {
            this.setHp(this.getHp() - damage);
            this.skill1();
            if (this.getHp() <= 0) {
                this.setHp(0);
            }

            if (this.getHp() > 0) {
                this.state = State.TAKING_DAMAGE;
                this.takeDamageTimer = 0f;
            }
        }
    }
}
