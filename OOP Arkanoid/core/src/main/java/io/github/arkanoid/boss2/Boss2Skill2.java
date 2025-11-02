package io.github.arkanoid.boss2;

import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.BossSkill;

public class Boss2Skill2 implements BossSkill {

    private final double COOLDOWN_HEALING = 1.5;
    private double healingTime = COOLDOWN_HEALING;

    private Boss2 owner;
    private HoneyShield honeyShield;
    private BossSkill nextSkill;
    private float shieldTime = 0;

    private boolean isSkill2Done = true;

    public Boss2Skill2(Boss2 owner, HoneyShield honeyShield) {
        this.honeyShield = honeyShield;
        this.owner = owner;
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
            owner.setShielded(true);

            shieldTime += delta;
            honeyShield.setPosition(boss.getX(), boss.getY());
            honeyShield.setShieldDuration(boss.getStateTime());

            if (shieldTime >= 4f) {
                shieldTime = 0f;
                honeyShield.setHasShield(false);

                owner.setShielded(false);
                isSkill2Done = true;

                boss.setSkill(nextSkill);
            }
            if (!owner.isHeal()) {
                healingTime += delta;
                if (healingTime >= COOLDOWN_HEALING) {
                    healingTime = 0;
                    owner.setHeal(true);
                }

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
