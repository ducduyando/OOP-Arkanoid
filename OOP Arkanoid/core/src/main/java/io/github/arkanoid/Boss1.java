package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.Constants.*;
import static io.github.arkanoid.Constants.BOSS1_HEIGHT;
import static io.github.arkanoid.Constants.BOSS1_WIDTH;
import static io.github.arkanoid.Constants.LEFT_BOUNDARY;
import static io.github.arkanoid.Constants.RIGHT_BOUNDARY;

public class Boss1 extends Boss {
    private int currentFrame = 0;
    private float animationTimer = 0f;
    private float direction = 1f;
    private float stopTimer = 0f;
    private boolean isStopped = false;
    private final int maxFrame;

    Boss1(Texture texture, float x, float y) {
        super(x, y);
        this.textureRegion = new TextureRegion(texture, 0, 0, BOSS1_WIDTH, BOSS1_HEIGHT);
        this.velocityVector = new Vector(BOSS1_VELOCITY_X, BOSS1_VELOCITY_Y);
        this.hitBox = new Rectangle(x, y, BOSS1_WIDTH, BOSS1_HEIGHT);
        maxFrame = texture.getWidth() / BOSS1_WIDTH;
        setSize(BOSS1_WIDTH, BOSS1_HEIGHT);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public void act(float delta) {
        if (isDead()) return;
        float speed = BOSS1_VELOCITY_X * delta;
        if(isStopped) {
            stopTimer += delta;
            if(stopTimer >= 3f){
                isStopped = false;
                stopTimer = 0;
            }
        }
        else{
            float prevX = getX();
            moveBy(direction * speed,0);
            hitBox.setPosition(getX(),getY());
            float centerX = (SCREEN_WIDTH -  BOSS1_WIDTH) / 2f;
            boolean crossedCenterToRight = prevX < centerX && getX() >= centerX;
            boolean crossedCenterToLeft = prevX > centerX && getX() <= centerX;
            if(crossedCenterToRight || crossedCenterToLeft){
                setX(centerX);
                isStopped = true;
            }
            if (getX() <= LEFT_BOUNDARY) {
                setX(LEFT_BOUNDARY);
                direction = 1;
            } else if (getX() + getWidth() >= RIGHT_BOUNDARY) {
                setX(RIGHT_BOUNDARY - getWidth());
                direction = -1;
            }
        }
        animationTimer += delta;
        if (animationTimer >= delta * 10) {
            animationTimer = 0f;
            currentFrame = (currentFrame + 1) % maxFrame;
            this.textureRegion.setRegion(BOSS1_WIDTH * currentFrame, 0, BOSS1_WIDTH, BOSS1_HEIGHT);
        }

        hitBox.setPosition(getX(), getY());
    }
}
