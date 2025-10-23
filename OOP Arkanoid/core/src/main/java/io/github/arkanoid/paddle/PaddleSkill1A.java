package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.entities.Ball;

import static io.github.arkanoid.core.Constants.*;

public class PaddleSkill1A extends Ball {

    private boolean isSkill1AReady = true;

    private float skill1ACooldownTimer = SKILL_COOLDOWN;

    public void startSkill1Cooldown() {
        isSkill1AReady = false;
        skill1ACooldownTimer = SKILL_COOLDOWN;
    }

    public boolean isSkill1AReady() {
        return isSkill1AReady;
    }

    public PaddleSkill1A(Texture texture, float x, float y) {
        super(texture, x, y);
        setDamage(BALL_UPGRADED_DAMAGE);
    }

    public void updateSkill1ACoolDown(float delta) {
        if (!isSkill1AReady) {
            skill1ACooldownTimer -= delta;
            if (skill1ACooldownTimer <= 0f) {
                isSkill1AReady = true;
            }
        }
    }

    @Override
    public void act(float delta) {
        if (super.isLaunched()) {
            Vector2 velocity = getVelocity();
            moveBy(velocity.x * delta, velocity.y * delta);
            setHitBox(getX(), getY());
        }
    }
}
