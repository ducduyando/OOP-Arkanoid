package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.entities.Ball;

import static io.github.arkanoid.core.Constants.*;

public class PaddleSkill1A extends Ball implements PaddleSkill  {

    Texture ballUpgrade;

    private final Paddle owner;
    private PaddleBallUpgrade paddleBallUpgrade;
    private float skill1ACooldownTimer = SKILL_COOLDOWN;

    private boolean skill1AReady = true;

    public boolean isSkill1AReady() {
        return skill1AReady;
    }

    public void setSkill1AReady(boolean skill1AReady) {
        this.skill1AReady = skill1AReady;
    }

    public void startSkill1Cooldown() {
        skill1ACooldownTimer = SKILL_COOLDOWN;
    }

    public PaddleSkill1A(Paddle paddle) {
        super(paddle.getX() + (PADDLE_WIDTH - BALL_WIDTH) / 2f,paddle.getY() + PADDLE_HEIGHT);
        owner = paddle;
        ballUpgrade = new Texture("PaddleSkill/" + "1a" + ".png");
    }

    @Override
    public void update(Paddle paddle, float delta) {
        if (isLaunched()) {
            moveBy(getVelocity().x * delta, getVelocity().y * delta);
            setHitBox(getX(), getY());
            paddleBallUpgrade.setPosition(getX(), getY());
        }
        if (!skill1AReady) {
            skill1ACooldownTimer -= delta;
            if (skill1ACooldownTimer <= 0) {
                skill1AReady = true;
            }
        }
    }

    @Override
    public void enter(Paddle paddle) {
        setPosition(paddle.getX() + (PADDLE_WIDTH - BALL_WIDTH) / 2f,paddle.getY() + PADDLE_HEIGHT);
        paddleBallUpgrade = new PaddleBallUpgrade(ballUpgrade, paddle.getX(), paddle.getY());
        owner.getStage().addActor(this.paddleBallUpgrade);
    }

    @Override
    public void cleanup() {
        if (paddleBallUpgrade != null) {
            paddleBallUpgrade.remove();
            paddleBallUpgrade = null;
        }
    }
}
