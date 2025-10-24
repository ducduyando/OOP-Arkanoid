package io.github.arkanoid.boss2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.paddle.Paddle;

import static io.github.arkanoid.core.Constants.*;

public class BeeEnemy extends Actor {
    private final Texture texture;
    private Rectangle hitBox;

    public BeeEnemy(float x, float y, String texturePath) {
        this.texture = new Texture(texturePath);
        setPosition(x, y);
        this.hitBox = new Rectangle(x, y, BOSS2_SKILL1_WIDTH, BOSS2_SKILL1_HEIGHT);
    }

    public BeeEnemy(Texture texture, float x, float y) {
        this.texture = texture;
        setPosition(x - BOSS2_SKILL1_WIDTH / 2f, y);
        setSize(BOSS2_SKILL1_WIDTH, BOSS2_SKILL1_HEIGHT);
        this.hitBox = new Rectangle(getX(), getY(), BOSS2_SKILL1_WIDTH, BOSS2_SKILL1_HEIGHT);
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


}
