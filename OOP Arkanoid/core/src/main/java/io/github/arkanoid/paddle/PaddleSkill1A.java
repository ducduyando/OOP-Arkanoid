package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;
import io.github.arkanoid.core.Constants;
import io.github.arkanoid.entities.Ball;

import static io.github.arkanoid.core.Constants.*;

public class PaddleSkill1A extends Ball implements PaddleSkill  {

    public enum Phase {
        CHARGING,
        FIRING,
        DONE
    }

    Texture ballUpgrade;

    private final Paddle owner;
    private PaddleBallUpgrade paddleBallUpgrade;
    private float skill1ACooldownTimer = PADDLE_SKILL_COOLDOWN;
    private float skill1AFiringTime = 0f;
    private Phase currentPhase = Phase.DONE;

    private boolean skill1AReady = true;
    public float getSkill1ACooldownTimer() {
        return skill1ACooldownTimer;
    }
    public void setSkill1ACooldownTimer(float skill1ACooldownTimer) {
        this.skill1ACooldownTimer = skill1ACooldownTimer;
    }
    public boolean isSkill1AReady() {
        return skill1AReady;
    }

    public void setSkill1AReady(boolean skill1AReady) {
        this.skill1AReady = skill1AReady;
    }

    public void startSkill1Cooldown() {
        skill1AReady = false;
        skill1ACooldownTimer = PADDLE_SKILL_COOLDOWN;
    }

    public PaddleSkill1A(Paddle paddle) {
        super(paddle.getX() + (PADDLE_WIDTH - BALL_WIDTH) / 2f,paddle.getY() + PADDLE_HEIGHT);
        owner = paddle;
        ballUpgrade = new Texture("PaddleSkill/" + "1a" + ".png");
    }

    @Override
    public void update(Paddle paddle, float delta) {
        // Always update cooldown timer
        if (!skill1AReady) {
            skill1ACooldownTimer -= delta;
            if (skill1ACooldownTimer <= 0) {
                skill1AReady = true;
            }
        }

        // Update skill effects if ball is active
        if (paddleBallUpgrade != null) {
            if (currentPhase == Phase.CHARGING) {
                // Short charging phase
                skill1AFiringTime += delta;
                if (skill1AFiringTime >= 0.5f) { // 0.5 second charging
                    currentPhase = Phase.FIRING;
                    setLaunched(true); // Launch the ball
                    skill1AFiringTime = 0f;
                }
            } else if (currentPhase == Phase.FIRING) {
                if (isLaunched()) {
                    moveBy(getVelocity().x * delta, getVelocity().y * delta);
                    setHitBox(getX(), getY());
                    paddleBallUpgrade.getHitBox().setPosition(getX(), getY());
                    paddleBallUpgrade.setPosition(getX(), getY());
                    
                    skill1AFiringTime += delta;
                    // End skill after 5 seconds or if ball goes off screen
                    if (skill1AFiringTime >= 5f || getY() > UP_BOUNDARY || getY() < DOWN_BOUNDARY) {
                        cleanup();
                        currentPhase = Phase.DONE;
                        startSkill1Cooldown();
                    }
                }
            }
        }
    }

    @Override
    public void enter(Paddle paddle) {
        setPosition(paddle.getX() + (PADDLE_WIDTH - BALL_WIDTH) / 2f,paddle.getY() + PADDLE_HEIGHT);
        paddleBallUpgrade = new PaddleBallUpgrade(ballUpgrade, paddle.getX(), paddle.getY());
        owner.getStage().addActor(this.paddleBallUpgrade);
        currentPhase = Phase.CHARGING;
        skill1AFiringTime = 0f;
        setLaunched(false); // Reset launch state
    }

    @Override
    public void cleanup() {
        if (paddleBallUpgrade != null) {
            paddleBallUpgrade.remove();
            paddleBallUpgrade = null;
        }
        currentPhase = Phase.DONE;
        setLaunched(false);
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public float getSkill1AFiringTime() {
        return skill1AFiringTime;
    }

    public static float getSKILL1A_FIRING_DURATION() {
        return 5f;
    }
}
