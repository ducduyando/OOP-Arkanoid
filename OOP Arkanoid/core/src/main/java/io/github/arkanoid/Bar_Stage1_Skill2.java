package io.github.arkanoid;

import java.util.Random;

import static io.github.arkanoid.Constants.*;

public class Bar_Stage1_Skill2 {
    private enum Phase {
        CHARGING,
        FIRING,
        DONE
    }
    private float barLaserTime = 0f;
    private final Bar owner;
    private Phase currentPhase;
    private BarLaserEffect barLaserEffect;

    Bar_Stage1_Skill2(Bar owner) {
        this.owner = owner;
        this.currentPhase = Phase.DONE;
    }

    public void enter(Bar bar) {
        this.barLaserEffect = new BarLaserEffect(bar.getBarLaserEffect(), bar.getX(), bar.getY());
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
            }
        }
    }

    public boolean isDone() {
        return currentPhase == Phase.DONE;
    }

    public BarLaserEffect getBarLaserEffect() { // Thêm phương thức getter cho va chạm.
        return barLaserEffect;
    }
}
