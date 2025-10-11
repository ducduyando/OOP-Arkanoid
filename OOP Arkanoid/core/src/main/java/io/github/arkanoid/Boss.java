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
    }

    public TextureRegion currentFrame;
    public final Animation<TextureRegion> animation;
    public Vector2 velocity;
    protected Rectangle hitBox;
    private int hp;

    protected BossSkill currentSkill;
    protected float stateTime = 0f;

    protected final Animation<TextureRegion> takeDamageAnimation;
    protected State state = State.NORMAL;
    protected float takeDamageTimer = 0f;

    Boss(Texture texture, Texture takeDamage, float x, float y, int bossWidth, int bossHeight, float velocity_x, float velocity_y, int maxHp) {
        int frameCount = texture.getWidth() / bossWidth;

        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(texture, i * bossWidth, 0, bossWidth, bossHeight);
        }

        this.animation = new Animation<TextureRegion>(FRAME_DURATION, frames);
        this.animation.setPlayMode(Animation.PlayMode.LOOP);
        this.currentFrame = frames[0];

        int takeDamageFrameCount = takeDamage.getWidth() / bossWidth;
        TextureRegion[] takeDamageFrames = new TextureRegion[takeDamageFrameCount];
        for (int i = 0; i < takeDamageFrameCount; i++) {
            takeDamageFrames[i] = new TextureRegion(takeDamage, i * bossWidth, 0, bossWidth, bossHeight);
        }

        this.takeDamageAnimation = new Animation<TextureRegion>(FRAME_DURATION, takeDamageFrames);

        setPosition(x, y);
        setSize(bossWidth, bossHeight);
        setOrigin(bossWidth / 2f, bossHeight / 2f);

        this.hp = maxHp;
        this.velocity = new Vector2(velocity_x, velocity_y);
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

    public boolean isDead() {
        return hp <= 0;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public void act(float delta) {
        hitBox.setPosition(getX(), getY());
        if (isDead()) {
            this.remove();
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

            currentSkill.update(this, delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
