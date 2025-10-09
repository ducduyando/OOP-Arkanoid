package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static io.github.arkanoid.Constants.*;

public class Boss1Skill1 {
//
    private Animation<TextureRegion> animation;
    private TextureRegion currentFrame;
    private float stateTime = 0f;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle hitBox;
    private boolean active = true;
    public Boss1Skill1(Texture texture, float startX, float startY) {
        int frameCount = 3; // chia 3 frame ngang
        int frameWidth = texture.getWidth() / frameCount;
        int frameHeight = texture.getHeight();

        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(texture, i * frameWidth, 0, frameWidth, frameHeight);
        }
        this.animation = new Animation<TextureRegion>(BOMB_FRAME_DURATION, frames);
        this.animation.setPlayMode(Animation.PlayMode.LOOP);
        this.currentFrame = frames[0];

        this.position = new Vector2(startX - frameWidth / 2f, startY);
        this.velocity = new Vector2(0, -BOMB_SPEED_Y); // Bắn thẳng xuống
        this.hitBox = new Rectangle(position.x, position.y, frameWidth, frameHeight);
    }
    public void update(float delta) {
        if (!active) return;
        stateTime += delta;
        currentFrame = animation.getKeyFrame(stateTime, true);
        position.add(velocity.x * delta, velocity.y * delta);
        hitBox.set(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        if (position.y + currentFrame.getRegionHeight() < 0) {
            active = false;
        }
    }
    public void render(Batch batch) {
        if (active) {
            batch.draw(currentFrame, position.x, position.y);
        }
    }

    public boolean isActive() {
        return active;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void deactivate() {
        active = false;
    }

    public Vector2 getPosition() {
        return position;
    }
}
