package io.github.arkanoid.boss2;

import com.badlogic.gdx.graphics.Texture;
import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.BossSkill;

public class Boss2Skill2 implements BossSkill {

    private Texture shieldStateTexture;
    private Boss2 owner;
    private HoneyShield honeyShield;
    private BossSkill nextSkill;
    private float shieldTime = 0;

    private boolean isSkill2Done = true;

    public Boss2Skill2(Boss2 owner, HoneyShield honeyShield) {
        this.honeyShield = honeyShield;
        this.owner = owner;
        shieldStateTexture = owner.skill2Texture;
    }

    public void setNextSkill(BossSkill nextSkill) {
        this.nextSkill = nextSkill;
    }

    public HoneyShield getHoneyShield() {
        return honeyShield;
    }

    public boolean isSkill2Done() {
        return isSkill2Done;
    }

    @Override
    public void update(Boss boss, float delta) {
        if (honeyShield.isHasShield()) {
            owner.setShield(true);
            shieldTime += delta;
            honeyShield.setPosition(boss.getX(), boss.getY());
            honeyShield.setShieldDuration(boss.getStateTime());
            if (shieldTime >= 3f) {
                shieldTime = 0f;
                honeyShield.setHasShield(false);
                boss.setSkill(nextSkill);
                owner.setShield(false);
                isSkill2Done = true;
            }
        }

    }

    @Override
    public void enter(Boss boss) {

        honeyShield.setHasShield(true);
        honeyShield.setPosition(boss.getX(), boss.getY());
        honeyShield.setShieldDuration(boss.getStateTime());
        boss.getStage().addActor(honeyShield);
        boss.setState(Boss.State.NORMAL);

        isSkill2Done = false;

    }

    @Override
    public void cleanup() {
        if (honeyShield != null) {
            honeyShield.remove();
            honeyShield = null;
        }
    }
}
