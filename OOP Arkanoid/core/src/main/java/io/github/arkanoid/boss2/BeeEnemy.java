package io.github.arkanoid.boss2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.paddle.Paddle;

public class BeeEnemy extends Actor {
    private final Texture texture;
    private float speed = 250f;
    private Rectangle hitBox;

    public BeeEnemy(float x, float y, String texturePath) {
        this.texture = new Texture(texturePath);
        setBounds(x, y, 96, 96);
        this.hitBox = new Rectangle(x, y, 96, 96);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        moveBy(0, -speed * delta);
        hitBox.setPosition(getX(), getY());

        if (getY() + getHeight() < 0) {
            remove();
            return;
        }

        if (getStage() != null) {
            for (Actor actor : getStage().getActors()) {
                if (actor instanceof Paddle paddle) {
                    Rectangle paddleRect = paddle.getHitBox();
                    if (hitBox.overlaps(paddleRect)) {
                        paddle.takeDamage();
                        remove();
                        return;
                    }
                }

                if (actor instanceof Ball ball) {
                    Rectangle ballRect = ball.getHitBox();
                    if (hitBox.overlaps(ballRect)) {
                        remove();
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
