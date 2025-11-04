package io.github.arkanoid.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.core.Constants.*;

public class FinalBoss extends Actor {
    public enum State {
        NORMAL,
        TAKING_DAMAGE,
        DYING
    }

    private final Texture normalSprite;
    private final Texture takeDamageSprite;
    private final Texture deathSprite;
    public final Texture skill1ATexture;
    public final Texture skill1BTexture;
    public final Texture target1ATexture;
    public final Texture target1BTexture;
    public final Texture skill2ATexture;
    public final Texture skill2BTexture;

    public TextureRegion currentFrame;
    public final Animation<TextureRegion> animation;
    public Vector2 velocity;
    protected Rectangle hitBox;
    private int hp;
    private final int maxHp;
    protected State state = State.NORMAL;

    protected FinalBossSkill currentSkill;
    protected float stateTime = 0f;

    protected final Animation<TextureRegion> takeDamageAnimation;
    protected float takeDamageTimer = 0f;

    protected final Animation<TextureRegion> deathAnimation;
    protected float deathTimer = 0f;

    protected boolean isReadyToDeath = false;

    private Sound getHitSound;
    private Music deadMusic;
    private boolean isExiting = false;


    public FinalBoss(float x, float y, int bossWidth, int bossHeight, Vector2 velocity, int maxHp) {

        getHitSound = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Get hit" + ".wav"));
        deadMusic = Gdx.audio.newMusic(Gdx.files.internal("SFX/" + "Dead" + ".wav"));


        this.normalSprite = new Texture("Boss3/" + "normal" + ".png");
        this.takeDamageSprite = new Texture("Boss3/" + "take_damage" + ".png");
        this.deathSprite = new Texture("Boss3/" + "death" + ".png");
        this.skill1ATexture = new Texture("Boss3/" + "skill1A" + ".png");
        this.skill1BTexture = new Texture("Boss3/" + "skill1B" + ".png");
        this.target1ATexture = new Texture("Boss3/" + "target1A" + ".png");
        this.target1BTexture = new Texture("Boss3/" + "target1B" + ".png");
        this.skill2ATexture = new Texture("Boss3/" + "skill2A" + ".png");
        this.skill2BTexture = new Texture("Boss3/" + "skill2B" + ".png");

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

        setPosition(x, y);
        setSize(bossWidth, bossHeight);
        setOrigin(bossWidth / 2f, bossHeight / 2f);

        this.hp = maxHp;
        this.maxHp = maxHp;
        this.velocity = velocity;
        this.hitBox = new Rectangle(x, y, getWidth(), getHeight());
    }

    public void setSkill(FinalBossSkill newSkill) {
        this.currentSkill = newSkill;
        if (this.currentSkill != null) {
            this.currentSkill.enter(this);
        }
    }

    public FinalBossSkill getSkill() {
        return currentSkill;
    }

    public void takeDamage(int damage) {
        if (state == State.NORMAL) {
            this.hp -= damage;
            if (this.hp <= 0) {
                this.hp = 0;
            }
            this.state = State.TAKING_DAMAGE;
            this.takeDamageTimer = 0f;
            getHitSound.play();
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int newHp) {
        if (newHp >= 0 && newHp <= maxHp) {
            this.hp = newHp;
            if (hp <= 0) {
                state = State.DYING;
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

    public boolean isReadyToDeath() {
        return isReadyToDeath;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void act(float delta) {
        hitBox.setPosition(getX(), getY());

        if (state == State.DYING) {
            deathTimer += delta;

            currentFrame = deathAnimation.getKeyFrame(deathTimer, false);

            if (deathAnimation.isAnimationFinished(deathTimer)) {
                if (!isExiting) {
                    deadMusic.play();
                    isExiting = true;
                }
                else {
                    if (!deadMusic.isPlaying()) {
                        isReadyToDeath = true;
                        isExiting = false;
                        this.remove();
                    }
                }
            }
            return;
        }

        if (isDead()) {
            state = State.DYING;

            if (currentSkill != null) {
                currentSkill.cleanup();
            }

            return;
        }

        if (state == State.TAKING_DAMAGE) {
            takeDamageTimer += delta;

            currentFrame = takeDamageAnimation.getKeyFrame(takeDamageTimer, false);

            if (takeDamageAnimation.isAnimationFinished(takeDamageTimer)) {

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
        normalSprite.dispose();
        deathSprite.dispose();
        takeDamageSprite.dispose();
        skill1ATexture.dispose();
        skill1BTexture.dispose();
        skill2ATexture.dispose();
        skill2BTexture.dispose();
        if (getHitSound != null) {
            getHitSound.dispose();
        }
        if (deadMusic != null) {
            deadMusic.dispose();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
