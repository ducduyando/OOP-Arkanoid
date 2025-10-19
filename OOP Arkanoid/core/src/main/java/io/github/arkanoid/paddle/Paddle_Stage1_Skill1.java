package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.universal.Ball;

import static io.github.arkanoid.universal.Constants.*;

public class Paddle_Stage1_Skill1 extends Ball {

    private boolean isSkill1Ready = true;

    private float skill1CooldownTimer = SKILL_COOLDOWN;

    public void startSkill1Cooldown() {
        isSkill1Ready = false;
        skill1CooldownTimer = SKILL_COOLDOWN;
    }

    public boolean isSkill1Ready() {
        return isSkill1Ready;
    }

    Paddle_Stage1_Skill1(Texture texture, float x, float y) {
        super(texture, x, y);
        setDamage(BALL_STAGE1_DAMAGE);
    }

    @Override
    public void act(float delta) {
        if (super.isLaunched()) {
            Vector2 velocity = getVelocity();
            moveBy(velocity.x * delta, velocity.y * delta);
            setHitBox(getX(), getY());
        }
        if (!isSkill1Ready) {
            skill1CooldownTimer -= delta;
            if (skill1CooldownTimer <= 0) {
                isSkill1Ready = true;
            }
        }
    }
}
