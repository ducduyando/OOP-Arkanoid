package io.github.arkanoid;

import java.util.function.Consumer;

public class StationaryAttackSkill implements BossSkill{
    private final Consumer<Boss> attackAction;
    private final float attackCooldown;
    private float cooldownTimer = 0f;

    public StationaryAttackSkill(float attackCooldown, Consumer<Boss> attackAction) {
        this.attackAction = attackAction;
        this.attackCooldown = attackCooldown;
    }

    @Override
    public void enter(Boss boss) {
        this.cooldownTimer = attackCooldown / 2f;
    }

    @Override
    public void update(Boss boss, float delta) {
        cooldownTimer += delta;
        if (cooldownTimer >= attackCooldown) {
            attackAction.accept(boss);
            cooldownTimer = 0f;
        }
    }
}
