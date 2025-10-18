package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.Constants.*;


public class Boss extends Actor {
    protected enum State {
        NORMAL,
        TAKING_DAMAGE,
        DYING,
        TRANSITION
    }

    public final Texture skill1Texture;
    public final Texture skill2Texture;

    public TextureRegion currentFrame;
    public final Animation<TextureRegion> animation;
    public Vector2 velocity;
    protected Rectangle hitBox;
    private int hp;
    private final int maxHp;
    protected State state = State.NORMAL;

    protected BossSkill currentSkill;
    protected float stateTime = 0f;

    protected final Animation<TextureRegion> takeDamageAnimation;
    protected float takeDamageTimer = 0f;

    protected final Animation<TextureRegion> deathAnimation;
    protected float deathTimer = 0f;

    protected final Animation<TextureRegion> transitionAnimation;
    protected float transitionTimer = 0f;

    protected boolean isTransitionDone = false;


    Boss(int number, float x, float y, int bossWidth, int bossHeight, Vector2 velocity, int maxHp) {

        Texture normalSprite = new Texture("boss" + number + "/" + "normal" + ".png");
        Texture takeDamageSprite = new Texture("boss" + number + "/" + "take_damage" + ".png");
        Texture deathSprite = new Texture("boss" + number + "/" + "death" + ".png");
        Texture transitionSprite = new Texture("powerUp/" + "transition" + ".png");
        this.skill1Texture = new Texture("boss" + number + "/" + "skill1" + ".png");
        this.skill2Texture = new Texture("boss" + number + "/" + "skill2" + ".png");

        int frameCount = normalSprite.getWidth() / bossWidth;

        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(normalSprite, i * bossWidth, 0, bossWidth, bossHeight);
        }

        this.animation = new Animation<TextureRegion>(FRAME_DURATION, frames);
        this.animation.setPlayMode(Animation.PlayMode.LOOP);
        this.currentFrame = frames[0];

        int takeDamageFrameCount = takeDamageSprite.getWidth() / bossWidth;
        TextureRegion[] takeDamageFrames = new TextureRegion[takeDamageFrameCount];
        for (int i = 0; i < takeDamageFrameCount; i++) {
            takeDamageFrames[i] = new TextureRegion(takeDamageSprite, i * bossWidth, 0, bossWidth, bossHeight);
        }

        this.takeDamageAnimation = new Animation<TextureRegion>(FRAME_DURATION, takeDamageFrames);

        int deathFrameCount = deathSprite.getWidth() / bossWidth;
        TextureRegion[] deathFrames = new TextureRegion[deathFrameCount];
        for (int i = 0; i < deathFrameCount; i++) {
            deathFrames[i] = new TextureRegion(deathSprite, i * bossWidth, 0, bossWidth, bossHeight);
        }

        this.deathAnimation = new Animation<TextureRegion>(FRAME_DURATION, deathFrames);

        int transitionFrameCount = transitionSprite.getWidth() / TRANSITION_WIDTH;
        TextureRegion[] transitionFrames = new TextureRegion[transitionFrameCount];
        for (int i = 0; i < transitionFrameCount; i++) {
            transitionFrames[i] = new TextureRegion(transitionSprite ,0, TRANSITION_WIDTH * i, TRANSITION_WIDTH, TRANSITION_HEIGHT);
        }

        this.transitionAnimation = new Animation<TextureRegion>(FRAME_DURATION * 1.5f, transitionFrames);

        setPosition(x, y);
        setSize(bossWidth, bossHeight);
        setOrigin(bossWidth / 2f, bossHeight / 2f);

        this.hp = maxHp;
        this.maxHp = maxHp;
        this.velocity = velocity;
        this.hitBox = new Rectangle(x, y, getWidth(), getHeight());
    }

    public void setSkill(BossSkill newSkill) {
        this.currentSkill = newSkill;
        if (this.currentSkill != null) {
            this.currentSkill.enter(this);
        }
    }

    public void takeDamage(int damage) {
        if (state == State.NORMAL) {
            this.hp -= damage;
            if (this.hp <= 0) {
                this.hp = 0;
            }

            this.state =  State.TAKING_DAMAGE;
            this.takeDamageTimer = 0f;
        }
    }

    public int  getHp() {
        return hp;
    }

    public void setHp(int newHp) {
        if (newHp >= 0 && newHp <= maxHp) {
            this.hp = newHp;
            if (hp <= 0) {
                state = State.DYING;
            } else if (state == State.DYING) {
                state = State.NORMAL;
            }
        }
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public void act(float delta) {
        hitBox.setPosition(getX(), getY());

        if (state == State.DYING) {
            deathTimer += delta;

            currentFrame = deathAnimation.getKeyFrame(deathTimer, false);

            if (deathAnimation.isAnimationFinished(deathTimer)) {
                state = State.TRANSITION;
            }
            return;
        }

        if (isDead() && state != State.TRANSITION && state != State.DYING) {
            state = State.DYING;
            hitBox.setSize(0,0);

            if (currentSkill != null) {
                currentSkill.cleanup();
            }

            return;
        }

        if (state == State.TRANSITION) {
            transitionTimer += delta;
            currentFrame = transitionAnimation.getKeyFrame(transitionTimer, false);

            if (transitionAnimation.isAnimationFinished(transitionTimer)) {
                isTransitionDone = true;
                this.remove();
            }
            return;
        }

        if (state == State.TAKING_DAMAGE) {
            takeDamageTimer += delta;

            currentFrame = takeDamageAnimation.getKeyFrame(takeDamageTimer, false);

            if (takeDamageAnimation.isAnimationFinished(takeDamageTimer)) {
                setOrigin(getWidth() / 2f, getHeight() / 2f);
                state = State.NORMAL;
            }
        }

        else if (state == State.NORMAL) {
            stateTime += delta;
            currentFrame = animation.getKeyFrame(stateTime, true);
        }

        currentSkill.update(this, delta);
    }


    public void dispose() {
        skill1Texture.dispose();
        skill2Texture.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float drawX;
        float drawY;
        float drawWidth;
        float drawHeight;
        if (state == State.TRANSITION) {
            drawX = 0;
            drawY = 0;
            drawWidth = SCREEN_WIDTH;
            drawHeight = SCREEN_HEIGHT;
        } else {
            drawX = getX();
            drawY = getY();
            drawWidth = getWidth();
            drawHeight = getHeight();
        }
        batch.draw(currentFrame, drawX, drawY, getOriginX(), getOriginY(), drawWidth, drawHeight, getScaleX(), getScaleY(), getRotation());
    }
}
