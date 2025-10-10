package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.Constants.*;

public class LaserEffect extends Actor {
    private Animation<TextureRegion> laserAnimation;
    private float stateTime = 0f;
    private Rectangle hitbox;

    private boolean isAnimationFinished = false;

    public LaserEffect(Texture texture, float bossX, float bossY) {
        int maxFrame = texture.getWidth() / LASER_WIDTH;
        TextureRegion[] frames = new TextureRegion[maxFrame];
        for (int i = 0; i < maxFrame; i++) {
            frames[i] = new TextureRegion(texture, LASER_WIDTH * i, 0, LASER_WIDTH, LASER_HEIGHT);
        }

        laserAnimation = new Animation<>(FRAME_DURATION, frames);
        laserAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        float x = bossX + BOSS1_WIDTH / 2f - LASER_WIDTH / 2f;
        float y = bossY - LASER_HEIGHT;

        setPosition(x, y);
        setSize(LASER_WIDTH, LASER_HEIGHT);
        hitbox = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void destroy() {
        this.remove();
    }

    public boolean isAnimationDone() {
        return isAnimationFinished;
    }

    @Override
    public void act(float delta) {
        hitbox.setPosition(getX(), getY());

        if (!isAnimationFinished) {
            stateTime += delta;
            if (laserAnimation.isAnimationFinished(stateTime)) {
                isAnimationFinished = true;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion currentFrame = laserAnimation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

}
