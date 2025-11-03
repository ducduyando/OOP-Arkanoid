package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.boss2.Boss2;
import com.badlogic.gdx.math.Rectangle;
import io.github.arkanoid.boss3.Boss3;

import java.awt.*;
import static io.github.arkanoid.core.Constants.*;

public class PaddleBeeBullet extends Actor {
    private  final Texture texture;
    private Rectangle hitBox;

    public PaddleBeeBullet(Texture texture, float x, float y) {
        this.texture = texture;
        setPosition(x - BOSS2_SKILL1_WIDTH / 2f, y);
        setSize(BOSS2_SKILL1_WIDTH, BOSS2_SKILL1_HEIGHT);
        this.hitBox = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        moveBy(0,BEE_BULLET_SPEED_Y * delta);
        hitBox.setPosition(getX(), getY());

        if (getY() > SCREEN_HEIGHT) {
            this.remove();
            return;
        }
        if (getStage() != null) {
            for (Actor actor : getStage().getActors()) {
                if (actor == this) continue;
                if(actor instanceof Boss3 boss3) {
                    if(hitBox.overlaps(boss3.getHitBox()) ) {
                        boss3.takeDamage(10);
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
}
