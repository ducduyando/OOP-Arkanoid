package io.github.arkanoid.boss1;

import com.badlogic.gdx.graphics.Texture;
import io.github.arkanoid.universal.Boss;

import static io.github.arkanoid.universal.Constants.*;

public class Boss1 extends Boss {



    public Boss1(int number, float x, float y, int maxHp) {
        super(number, x, y, BOSS1_WIDTH, BOSS1_HEIGHT, BOSS1_VELOCITY, maxHp);

        BossCentering centering = new BossCentering(this);
        Boss1_Skill1 bombingSkill = new Boss1_Skill1(this);
        Boss1_Skill2 laserSkill = new Boss1_Skill2(this);

        centering.setNextSkills(bombingSkill, laserSkill);
        bombingSkill.setNextSkill(centering);
        laserSkill.setNextSkill(centering);

        setSkill(centering);
    }

    public Texture getSkill1Texture() {
        return this.skill1Texture;
    }

    public Texture getSkill2Texture() {
        return this.skill2Texture;
    }

    public void skill1() {
        if (getStage() != null) {
            getStage().addActor(new BombProjectile(this.skill1Texture, getX() + getWidth() / 2, getY()));
        }
    }

    public void skill2() {
        if (getStage() != null) {
            getStage().addActor(new LaserEffect(this.skill2Texture, getX() + getWidth() / 2, getY()));
        }
    }

}
