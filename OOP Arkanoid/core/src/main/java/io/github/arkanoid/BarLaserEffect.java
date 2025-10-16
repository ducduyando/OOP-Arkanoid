package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.Constants.*;

public class BarLaserEffect extends Actor {
    private Animation<TextureRegion> barLaserAnimation;
    private float stateTime = 0f;
    private Rectangle hitbox;

    private boolean isAnimationFinished = false;

    public BarLaserEffect(Texture texture, float barX, float barY) {
        int maxFrames = texture.getWidth() / LASER_WIDTH;
        TextureRegion[] frames = new TextureRegion[maxFrames];
        for (int i = 0; i < maxFrames; i++) {
            frames[i] = new TextureRegion(texture, LASER_WIDTH * i, 0, LASER_WIDTH, LASER_HEIGHT);
        }
        barLaserAnimation = new Animation<>(stateTime, frames);
        barLaserAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        float x = barX + BAR_WIDTH / 2f - LASER_WIDTH / 2f;
        float y = barY + BAR_HEIGHT;
        setPosition(x, y);
        setSize(LASER_WIDTH, LASER_HEIGHT);

        hitbox = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public boolean isAnimationDone() {
        return isAnimationFinished;
    }

    @Override
    public void act(float delta) {
        hitbox.setPosition(getX(), getY());

        if (!isAnimationFinished) {
            stateTime += delta;
            if (barLaserAnimation.isAnimationFinished(stateTime)) {
                isAnimationFinished = true;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion currentFrame = barLaserAnimation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

}
