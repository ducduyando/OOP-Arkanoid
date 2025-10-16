package io.github.arkanoid;

import java.util.Random;

import static io.github.arkanoid.Constants.*;

public class Bar_Stage1_Skill2 {
    private enum Phase {
        CHARGING,
        DONE
    }
    private float barLaserTime = 0f;
    private final Bar owner;
    private Phase currentPhase;
    private BarLaserEffect barLaserEffect;

    Bar_Stage1_Skill2(Bar owner) {
        this.owner = owner;
    }

    public void enter(Bar bar) {
        this.barLaserEffect = new BarLaserEffect(bar.getBarLaserEffect(), bar.getX(), bar.getY());
        owner.getStage().addActor(this.barLaserEffect);

        currentPhase = Phase.CHARGING;

    }

    public void update(Bar bar, float delta) {
        if (currentPhase == Phase.CHARGING) {
            if (barLaserEffect.isAnimationDone()) {
                currentPhase = Phase.DONE;
            }
        } else {

        }
    }
}
