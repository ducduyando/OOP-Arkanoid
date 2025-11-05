package io.github.arkanoid.boss2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.paddle.Paddle;
import io.github.arkanoid.paddle.PaddleBallUpgrade;
import io.github.arkanoid.paddle.PaddleLaserEffect;

import static io.github.arkanoid.core.Constants.*;
import static io.github.arkanoid.core.MusicManager.*;

public class BeeEnemy extends Actor {
    private final Texture texture;
    private Rectangle hitBox;

    private long beeSoundId;

    public BeeEnemy(Texture texture, float x, float y) {
        this.texture = texture;
        setPosition(x - BOSS2_SKILL1_WIDTH / 2f, y);
        setSize(BOSS2_SKILL1_WIDTH, BOSS2_SKILL1_HEIGHT);
        this.hitBox = new Rectangle(getX(), getY(), BOSS2_SKILL1_WIDTH, BOSS2_SKILL1_HEIGHT);

        this.beeSoundId = playEffect("beeSound");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        moveBy(0, -BEE_SPEED_Y * delta);
        hitBox.setPosition(getX(), getY());

        if (getY() + getHeight() < 0) {
            this.remove();
            return;
        }

        if (getStage() != null) {
            Actor[] actors = getStage().getActors().toArray();
            for (Actor actor : actors) {
                if (actor.equals(this) || !actor.hasParent()) continue;
                if (actor instanceof Paddle paddle) {
                    Rectangle paddleRect = paddle.getHitBox();
                    if (hitBox.overlaps(paddleRect)) {
                        paddle.takeDamage();
                        this.remove();
                        return;
                    }
                }


                if (actor instanceof Ball ball) {
                    Rectangle ballRect = ball.getHitBox();
                    if (hitBox.overlaps(ballRect)) {
                        this.remove();
                        return;
                    }
                }

                if (actor instanceof PaddleBallUpgrade paddleBallUpgrade) {
                    Rectangle paddleBallUpgradeRect = paddleBallUpgrade.getHitBox();
                    if (hitBox.overlaps(paddleBallUpgradeRect)) {
                        this.remove();
                        return;
                    }
                }

                if (actor instanceof PaddleLaserEffect laser) {
                    Rectangle laserRect = laser.getHitbox();
                    if (hitBox.overlaps(laserRect)) {
                        this.remove();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public boolean remove() {
        stopEffect("beeSound", beeSoundId);
        return super.remove();
    }
}
