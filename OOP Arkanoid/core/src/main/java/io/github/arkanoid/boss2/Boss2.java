package io.github.arkanoid.boss2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.entities.Boss;
import static io.github.arkanoid.core.Constants.*;

public class Boss2 extends Boss {
    private Boss2Skill1 skill1;
    private BossRandomMovement randomMove;

    public Boss2(int number ,float x, float y, int maxHp) {
        super(number, x, y, BOSS2_WIDTH, BOSS2_HEIGHT, BOSS2_VELOCITY, maxHp);

        skill1Texture = new Texture("boss2/"+ "skill" + "1" + ".png");
        this.randomMove = new BossRandomMovement(this);
        this.skill1 = new Boss2Skill1(this, this.randomMove);
        this.randomMove.setNextSkill(this.skill1);
        this.skill1.setNextSkill(this.randomMove);
        setSkill(randomMove);
    }

    public Texture getSkill1Texture() {
        return skill1Texture;
    }

    public void onHitByBullet() {
        if (getStage() != null && this.skill1 != null) {
            this.skill1.spawnBeeOnHit(getStage(), getX(), getY());
        }
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (!isDead()) {
            onHitByBullet();
        }
    }

    public void dispose() {
        super.dispose();
        if (skill1Texture != null) skill1Texture.dispose();
    }
}
