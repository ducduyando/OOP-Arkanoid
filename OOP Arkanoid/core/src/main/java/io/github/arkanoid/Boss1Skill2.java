package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.Constants.*;

public class Boss1Skill2 extends Actor {
    private Animation<TextureRegion> laserAnimation;
    private float stateTime = 0f;
    private boolean finished = false;
    private Rectangle hitbox;

    public Boss1Skill2(Texture texture, float bossX, float bossY) {
        int maxFrame = texture.getWidth() / LASER_WIDTH;
        TextureRegion[] frames = new TextureRegion[maxFrame];
        for (int i = 0; i < maxFrame; i++) {
            frames[i] = new TextureRegion(texture, LASER_WIDTH * i, 0, LASER_WIDTH, LASER_HEIGHT);
        }

        laserAnimation = new Animation<>(0.1f, frames);

        float x = bossX + BOSS1_WIDTH / 2f - LASER_WIDTH / 2f;
        float y = bossY - LASER_HEIGHT;

        setPosition(x, y);
        setSize(LASER_WIDTH, LASER_HEIGHT);

        hitbox = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        if (finished) return;

        stateTime += delta;
        hitbox.setPosition(getX(), getY());

        if (laserAnimation.isAnimationFinished(stateTime)) {
            finished = true;
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (finished) return;
        TextureRegion currentFrame = laserAnimation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean isFinished() {
        return finished;
    }
}
