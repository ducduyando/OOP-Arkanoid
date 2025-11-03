package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.paddle.Paddle;
import static io.github.arkanoid.core.Constants.*;

public class Laser extends Actor {

    Texture laserTexture;
    Boss3 owner;
    Paddle paddle;

    private final Vector2 LASER_SPEED = new Vector2(400f, 400f);

    private final int EYE_DST = 120;
    private final int EYE_L_X = 55;
    private final int EYE_R_X = 175;
    private final int EYE_Y = 184;

    private float eyeX;
    private float eyeY;

    private float rotationAngle;
    private Rectangle hitBox;

    private Vector2 target;
    private Vector2 direction;

    public Laser(Texture laserTexture, Boss3 owner, Paddle paddle, float targetX, float targetY, boolean isLeftEye) {

        this.laserTexture = laserTexture;
        this.owner = owner;
        this.paddle = paddle;

        if (isLeftEye) {
            target = new Vector2(targetX - EYE_DST / 2f, targetY);
            eyeX = owner.getX() + EYE_L_X;
            eyeY = owner.getY() + EYE_Y;
        }
        else {
            target = new Vector2(targetX + EYE_DST / 2f, targetY);
            eyeX = owner.getX() + EYE_R_X;
            eyeY = owner.getY() + EYE_Y;
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
}
