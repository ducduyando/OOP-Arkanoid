package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;

import static io.github.arkanoid.Constants.*;

public class Bar_Stage1_Skill2 implements BarSkill {
    private enum Phase {
        CHARGING,
        FIRING,
        DONE
    }

    Texture laserEffect;

    private float barLaserTime = 0f;
    private final Bar owner;
    private Phase currentPhase;
    private BarLaserEffect barLaserEffect;

    private float skill2CooldownTimer = SKILL_COOLDOWN;
    private boolean isSkill2Ready = true;

    public void startSkill2Cooldown() {
        isSkill2Ready = false;
        skill2CooldownTimer = SKILL_COOLDOWN;
    }

    public boolean isSkill2Ready() {
        return isSkill2Ready;
    }

    Bar_Stage1_Skill2(Bar owner) {
        laserEffect = new Texture("boss1/" + "skill2" + ".png");

        this.owner = owner;
        this.currentPhase = Phase.DONE;
    }

    public void enter(Bar bar) {
        this.barLaserEffect = new BarLaserEffect(laserEffect, bar.getX(), bar.getY());
        owner.getStage().addActor(this.barLaserEffect);
        currentPhase = Phase.CHARGING;
    }

    public void update(Bar bar, float delta) {
        float x = bar.getX() + BAR_WIDTH / 2f - LASER_WIDTH / 2f;
        barLaserEffect.setX(x);
        if (currentPhase == Phase.CHARGING) {
            if (barLaserEffect.isAnimationDone()) {
                currentPhase = Phase.FIRING;
                barLaserTime = 0f;
            }

        } else {
            barLaserTime += delta;
            if (barLaserTime >= 4f) {
                barLaserEffect.remove();
                barLaserEffect = null;
                currentPhase = Phase.DONE;
                startSkill2Cooldown();
            }
        }

        if (!isSkill2Ready) {
            skill2CooldownTimer -= delta;
            if (skill2CooldownTimer <= 0) {
                isSkill2Ready = true;
            }
        }
    }

    public void cleanup() {
        if (barLaserEffect != null) {
            barLaserEffect.remove();
            barLaserEffect = null;
        }
    }

    public boolean isFiring() {
        return currentPhase == Phase.FIRING;
    }
    public boolean isDone() {
        return currentPhase == Phase.DONE;
    }

    public BarLaserEffect getBarLaserEffect() { // Thêm phương thức getter cho va chạm.
        return barLaserEffect;
    }
}
