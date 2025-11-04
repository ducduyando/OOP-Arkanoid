package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.paddle.Paddle;
import static io.github.arkanoid.core.Constants.*;
import static io.github.arkanoid.core.MusicManager.*;

public class Laser extends Actor {

    Texture laserTexture;
    Boss3 owner;
    Paddle paddle;

    private float eyeX;
    private float eyeY;

    private float rotationAngle;
    private Rectangle hitBox;

    private Vector2 target;
    private Vector2 direction;

    private long laserSoundId;

    public Laser(Texture laserTexture, Boss3 owner, Paddle paddle, float targetX, float targetY, boolean isLeftEye) {

        this.laserTexture = laserTexture;
        this.owner = owner;
        this.paddle = paddle;

        if (isLeftEye) {
            target = new Vector2(targetX - EYES_DISTANCE / 2f, targetY);
            eyeX = owner.getX() + LEFT_EYE_COORDINATE.x;
            eyeY = owner.getY() + LEFT_EYE_COORDINATE.y;
        }
        else {
            target = new Vector2(targetX + EYES_DISTANCE / 2f, targetY);
            eyeX = owner.getX() + RIGHT_EYE_COORDINATE.x;
            eyeY = owner.getY() + RIGHT_EYE_COORDINATE.y;
        }

        float deltaX = target.x - eyeX;
        float deltaY = target.y - eyeY;

        rotationAngle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

        setSize(laserTexture.getWidth(), laserTexture.getHeight());
        setPosition(eyeX, eyeY);
        hitBox = new Rectangle(eyeX, eyeY, getWidth(), getHeight());
        setOrigin(0, 0);
        setRotation(rotationAngle);

        direction = target.cpy().sub(new Vector2(getX(), getY())).nor();

        this.laserSoundId = playEffect("laserSound");
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public void act(float delta) {
        if (hitBox.overlaps(paddle.getHitBox())) {
            paddle.takeDamage();
            remove();
            return;
        }
        if ((getY() + getHeight() <= DOWN_BOUNDARY) || (getY() >= UP_BOUNDARY - HP_HEIGHT)
            || getX() + getWidth() <= LEFT_BOUNDARY || getX() >= RIGHT_BOUNDARY) {

            remove();
        } else {
            moveBy(direction.x * LASER_SPEED.x * delta, direction.y * LASER_SPEED.y * delta);
        }
        hitBox.setPosition(getX(), getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(new TextureRegion(laserTexture), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public boolean remove() {
        stopEffect("laserSound", laserSoundId);
        return super.remove();
    }
}
