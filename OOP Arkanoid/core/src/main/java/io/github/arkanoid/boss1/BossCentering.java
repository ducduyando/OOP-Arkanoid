package io.github.arkanoid.boss1;

import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.BossSkill;

import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class BossCentering implements BossSkill {
    private final Boss owner;
    private BossSkill bombingSkill;
    private BossSkill laserSkill;
    private boolean hasArrived = false;

    private float cooldownTimer = 0f;
    private final float COOLDOWN_DURATION = 3f;

    public BossCentering(Boss1 owner) {
        this.owner = owner;
    }

    public void setNextSkills(BossSkill bombingSkill, BossSkill laserSkill) {
        this.bombingSkill = bombingSkill;
        this.laserSkill = laserSkill;
    }

    @Override
    public void cleanup() {}

    @Override
    public void enter(Boss boss) {
        this.hasArrived = false;
        this.cooldownTimer = 0f;
    }

    @Override
    public void update(Boss boss, float delta) {
        if (!hasArrived) {
            Vector2 currentPosition = new Vector2(boss.getX(), boss.getY());
            Vector2 centerPoint = new Vector2((SCREEN_WIDTH - boss.getWidth()) / 2f, SCREEN_HEIGHT * 0.6f);

            if (currentPosition.dst(centerPoint) < boss.velocity.len() * delta) {
                boss.setPosition(centerPoint.x,  centerPoint.y);
                hasArrived = true;
            } else {
                Vector2 direction = centerPoint.sub(currentPosition).nor();
                boss.moveBy(direction.x * boss.velocity.x * delta, direction.y * boss.velocity.y * delta);
            }
        } else {
            cooldownTimer += delta;

            if (cooldownTimer >= COOLDOWN_DURATION) {
                if (new Random().nextBoolean()) {
                    boss.setSkill(bombingSkill);
                } else {
                    boss.setSkill(laserSkill);
                }
            }
        }
    }
}
