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

    private boolean isShield = false;

    private PaddleSkill1A skill1A;
    private PaddleSkill1B skill1B;
    private PaddleSkill2A skill2A; // New skill2A
    private PaddleSkill2B skill2B;

    private boolean isSkill1ASelected = true;
    private boolean isSkill2ASelected = true;

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

    public void setShield(boolean shield) {
        isShield = shield;
    }

    public void setState(int newState) {
        this.state = newState;
        this.textureRegion.setRegion(PADDLE_WIDTH * newState, 0, PADDLE_WIDTH, PADDLE_HEIGHT);

        float newWidth = PADDLE_WIDTH - (64 * newState);
        float newX = hitBox.getX() + hitBox.getWidth() / 2f - newWidth / 2f;

        if (isGameOver()) {
            this.hitBox.setSize(0, 0);
        } else {
            this.hitBox.setWidth(newWidth);
            this.hitBox.setPosition(newX, getY());
        }
    }

    public void resetState() {
        this.state = 0;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        if (!isGameOver()) {
            float newXOffset = 32 * state;
            this.hitBox.setPosition(x + newXOffset, y);
        }
    }

    public Rectangle getHitBox(){
        if (isGameOver()) {
            Rectangle emptyRect = new Rectangle(-1000, -1000, 0, 0);
            return emptyRect;
        }
        return hitBox;
    }

    public boolean isGameOver() {
        return state >= 7;
    }

    public void takeDamage() {
        if (!isInvincible && !isShield) {
            int currentState = getState() + 1;
            setState(currentState);
            isInvincible = true;
            stopTimer = 0f;

            float currentWidth = PADDLE_WIDTH - (64 * state);

        }
    }

    public boolean isSkill1ASelected() {
        return isSkill1ASelected;
    }

    public PaddleSkill1A getSkill1A() {
        return skill1A;
    }

    public void setSkill2ASelected(boolean skill2ASelected) {
        isSkill2ASelected = skill2ASelected;
    }

    public float getSkill1ACooldownTimer() {
        if (skill1A != null) {
            return skill1A.getSkill1ACooldownTimer();
        }
        return PADDLE_SKILL_COOLDOWN;


    }

    public float getSkill1BCooldownTimer() {
        if (skill1B != null) {
            return skill1B.getSkill1BCooldownTimer();
        }
        return PADDLE_SKILL_COOLDOWN;
    }

    public void initializeSkills(boolean isSkillASelected, float skill1ACooldownTimer, float skill1BCooldownTimer) {
        if (this.skill1A == null) {
            this.skill1A = new PaddleSkill1A(this);
        }
        if (this.skill1B == null) {
            this.skill1B = new PaddleSkill1B(this);
        }

        this.isSkill1ASelected = isSkillASelected;

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

    public void initializeSkill2(boolean isSkill2ASelected, float skill2ACooldownTimer, float skill2BCooldownTimer) {
        if (this.skill2A == null) {
            this.skill2A = new PaddleSkill2A(this);
        }
        if (this.skill2B == null) {
            this.skill2B = new PaddleSkill2B(this);
        }
        this.isSkill2ASelected = isSkill2ASelected;

        this.skill2A.setSkill2ACooldownTimer(skill2ACooldownTimer);
        this.skill2A.setIsSkill2AReady(skill2ACooldownTimer <= 0);

        this.skill2B.setSkill2BCooldownTimer(skill2BCooldownTimer);
        this.skill2B.setIsSkill2BReady(skill2BCooldownTimer <= 0);
    }

    public PaddleSkill2A getSkill2A() {
        return skill2A;
    }

    public void setSkill2A(PaddleSkill2A skill2A) {
        this.skill2A = skill2A;
    }

    public PaddleSkill2B getSkill2B() {
        return skill2B;
    }

    public boolean isSkill2ASelected() {
        return isSkill2ASelected;
    }

    public void setSkill2B(PaddleSkill2B skill2B) {
        this.skill2B = skill2B;
    }

    public void setIsSkill2ASelected(boolean isSkill2ASelected) {
        this.isSkill2ASelected = isSkill2ASelected;
    }

    public float getSkill2ACooldownTimer() {
        if (skill2A != null) {
            return skill2A.getSkill2ACooldownTimer();
        }
        return PADDLE_SKILL_COOLDOWN;
    }

    public float getSkill2BCooldownTimer() {
        if (skill2B != null) {
            return skill2B.getSkill2BCooldownTimer();
        }
        return PADDLE_SKILL_COOLDOWN;
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
        if (isGameOver()) {
            return;
        }

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
