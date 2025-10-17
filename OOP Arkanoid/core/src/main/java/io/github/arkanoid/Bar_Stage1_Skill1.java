package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;

import static io.github.arkanoid.Constants.SKILL_COOLDOWN;

public class Bar_Stage1_Skill1 extends Ball {

    private final int extraDamage = 5;
    private boolean isSkill1Ready = true;

    private float skill1CooldownTimer = SKILL_COOLDOWN;

    public void startSkill1Cooldown() {
        isSkill1Ready = false;
        skill1CooldownTimer = SKILL_COOLDOWN;
    }

    public boolean isSkill1Ready() {
        return isSkill1Ready;
    }

    Bar_Stage1_Skill1(Texture texture, float x, float y) {
        super(texture, x, y);
        setDamage(getDamage() + extraDamage);
    }

    @Override
    public void act(float delta) {
        if (super.isLaunched()) {
            moveBy(velocityVector.x * delta, velocityVector.y * delta);
            hitBox.setPosition(getX(), getY());
        }
        if (!isSkill1Ready) {
            skill1CooldownTimer -= delta;
            if (skill1CooldownTimer <= 0) {
                isSkill1Ready = true;
            }
        }
    }
}
