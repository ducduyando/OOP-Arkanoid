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
    public TextureRegion currentFrame;
    public final Animation<TextureRegion> animation;
    public Vector2 velocity;
    protected Rectangle hitBox;
    private int hp;

    protected BossSkill currentSkill;
    protected float stateTime = 0f;

    Boss(Texture texture, float x, float y, int boss_width, int boss_height, float velocity_x, float velocity_y, int maxHp) {
        int frameCount = texture.getWidth() / boss_width;

        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(texture, i * boss_width, 0, boss_width, boss_height);
        }

        this.animation = new Animation<TextureRegion>(FRAME_DURATION, frames);
        this.animation.setPlayMode(Animation.PlayMode.LOOP);
        this.currentFrame = frames[0];

        setPosition(x, y);
        setSize(boss_width, boss_height);
        setOrigin(boss_width / 2f, boss_height / 2f);

        this.hp = maxHp;
        this.velocity = new Vector2(velocity_x, velocity_y);
        this.hitBox = new Rectangle(x, y, boss_width, boss_height);
    }

    public void setSkill(BossSkill newSkill) {
        this.currentSkill = newSkill;
        if (this.currentSkill != null) {
            this.currentSkill.enter(this);
        }
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
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
        if (!isDead() && currentSkill != null) {
            currentSkill.update(this, delta);
        }

        stateTime += delta;
        currentFrame = animation.getKeyFrame(stateTime, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
