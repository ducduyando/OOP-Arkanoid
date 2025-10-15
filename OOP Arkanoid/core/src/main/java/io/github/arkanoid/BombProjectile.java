package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.Constants.*;

public class BombProjectile extends Actor {
    private final Animation<TextureRegion> animation;
    private float stateTime = 0f;
    private final Rectangle hitBox;

    public BombProjectile(Texture texture, float x, float y) {
        int frameCount = texture.getWidth() / BOSS1_SKILL1_WIDTH;

        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(texture, i * BOSS1_SKILL1_WIDTH, 0, BOSS1_SKILL1_WIDTH, BOSS1_SKILL1_HEIGHT);
        }
        this.animation = new Animation<>(FRAME_DURATION, frames);
        this.animation.setPlayMode(Animation.PlayMode.LOOP);

        setPosition(x - BOSS1_SKILL1_WIDTH / 2f, y);
        setSize(BOSS1_SKILL1_WIDTH, BOSS1_SKILL1_HEIGHT);
        this.hitBox = new Rectangle(getX(), getY(), BOSS1_SKILL1_WIDTH, BOSS1_SKILL1_HEIGHT);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        moveBy(0, -BOMB_SPEED_Y * delta);
        hitBox.setPosition(getX(), getY());

        if (getY() + getHeight() < 0) {
            this.remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getHitbox() {
        return hitBox;
    }
}
