package io.github.arkanoid.paddle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.core.Constants.*;

public class Paddle extends Actor {

    TextureRegion textureRegion;
    Rectangle hitBox;
    private float stopTimer = 0f;
    private boolean isInvincible = false;
    private int state = 0;
    private float blinkTimer = 0f;
    private boolean isVisible = true;

    private PaddleSkill1A skill1A;
    private PaddleSkill1B skill1B;
    private PaddleSkill2A skill2A; // New skill2A

    private boolean isSkillASelected = true;
    private PaddleSkill activeSkill;
    public Paddle(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture, 0, 0, PADDLE_WIDTH, PADDLE_HEIGHT);
        this.hitBox = new Rectangle(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
        setPosition(x, y);
        setSize(PADDLE_WIDTH, PADDLE_HEIGHT);
        setOrigin(getWidth() / 2f, getHeight() / 2f);

    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

    public int getState() {
        return state;
    }

    public void setState(int newState) {
        this.state = newState;
        this.textureRegion.setRegion(PADDLE_WIDTH * newState, 0, PADDLE_WIDTH, PADDLE_HEIGHT);

        float newWidth = PADDLE_WIDTH - (64 * newState);
        float newXOffset = 32 * newState;

        this.hitBox.setWidth(newWidth);
        this.hitBox.setPosition(getX() + newXOffset, getY());

    }

    public void resetState() {
        this.state = 0;
    }

    public Rectangle getHitBox(){
        return hitBox;
    }

    public void takeDamage() {
        if (!isInvincible) {
            int currentState = getState() + 1;
            setState(currentState);
            isInvincible = true;
            stopTimer = 0f;
        }
    }

    public boolean isSkillASelected() {
        return isSkillASelected;
    }

    public float getSkill1ACooldownTimer() {
        if (skill1A != null) {
            return skill1A.getSkill1ACooldownTimer();
        }
        return SKILL_COOLDOWN;


    }

    public float getSkill1BCooldownTimer() {
        if (skill1B != null) {
            return skill1B.getSkill1BCooldownTimer();
        }
        return SKILL_COOLDOWN;
    }

    public void initializeSkills(boolean isSkillASelected, float skill1ACooldownTimer, float skill1BCooldownTimer) {
        if (this.skill1A == null) {
            this.skill1A = new PaddleSkill1A(this);
        }
        if (this.skill1B == null) {
            this.skill1B = new PaddleSkill1B(this);
        }

        this.isSkillASelected = isSkillASelected;

        this.skill1A.setSkill1ACooldownTimer(skill1ACooldownTimer);
        this.skill1A.setSkill1AReady(skill1ACooldownTimer <= 0);

        this.skill1B.setSkill1BCooldownTimer(skill1BCooldownTimer);
        this.skill1B.setIsSkill1BReady(skill1BCooldownTimer <= 0);

        this.activeSkill = isSkillASelected ? this.skill1A : this.skill1B;
    }

    public void initializeSkill2A() {
        if (this.skill2A == null) {
            this.skill2A = new PaddleSkill2A(this);
        }
    }

    public void initializeSkill2A(float skill2ACooldownTimer) {
        initializeSkill2A();
        this.skill2A.setSkill2ACooldownTimer(skill2ACooldownTimer);
        this.skill2A.setIsSkill2AReady(skill2ACooldownTimer <= 0);
    }

    public PaddleSkill2A getSkill2A() {
        return skill2A;
    }

    public float getSkill2ACooldownTimer() {
        if (skill2A != null) {
            return skill2A.getSkill2ACooldownTimer();
        }
        return SKILL_COOLDOWN;
    }

    public PaddleSkill getActiveSkill() {
        return activeSkill;
    }

    public PaddleSkill1B getSkill1B() {
        return skill1B;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible) {
            batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A) && getX() + 32 * state > LEFT_BOUNDARY) {
            moveBy(-PADDLE_VELOCITY.x * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && getX() + PADDLE_WIDTH - 32 * state < RIGHT_BOUNDARY) {
            moveBy(PADDLE_VELOCITY.x * delta, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && getY() > DOWN_BOUNDARY) {
            moveBy(0, -PADDLE_VELOCITY.y * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && getY() + PADDLE_HEIGHT + BALL_HEIGHT < UP_BOUNDARY) {
            moveBy(0, PADDLE_VELOCITY.y * delta);
        }

        if (isInvincible) {
            stopTimer += delta;
            blinkTimer += delta;
            if (blinkTimer >= FRAME_DURATION) {
                isVisible = !isVisible;
                blinkTimer = 0f;
            }

            if (stopTimer >= 2f) {
                isInvincible = false;
                stopTimer = 0f;
                blinkTimer = 0f;
                isVisible = true;
            }
        }

        float currentXOffset = 32 * state;
        hitBox.setPosition(getX() + currentXOffset, getY());

        // Update skill2A if available
        if (skill2A != null) {
            skill2A.update(this, delta);
        }
    }
}
