package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.Constants.*;


public class Boss extends Actor {
    TextureRegion textureRegion;
    Vector velocityVector;
    Rectangle hitBox;
    private int hp;
    private final int maxFrame;
    private int currentFrame = 0;
    private float animationTimer = 0f;
    private final float frameDuration = 0.1f;
    private float direction=1f;//1=sang phai,-1=sang trai
    private float stopTimer=0f;
    private boolean isStopped=false;

    Boss(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture, 0, 0, BOSS1_WIDTH, BOSS1_HEIGHT);
        this.velocityVector = new Vector(BOSS1_VELOCITY_X, BOSS1_VELOCITY_Y);
        this.hitBox = new Rectangle(x, y, BOSS1_WIDTH, BOSS1_HEIGHT);
        maxFrame = texture.getWidth() / BOSS1_WIDTH;
        this.hp = 100;
        setPosition(x, y);
        setSize(BOSS1_WIDTH, BOSS1_HEIGHT);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isDead()) return;
        float speed=BOSS1_VELOCITY_X*delta;
        // o giua
        if(isStopped){
            stopTimer+=delta;
			if(stopTimer>=3f){
				isStopped=false;
				stopTimer=0;
			}
        }
        else{
			// di chuyen sang trai phai
			float prevX = getX();
			moveBy(direction*speed,0);
			hitBox.setPosition(getX(),getY());
			// neu cat qua diem giua man hinh thi dung lai 3s
			float centerX=(SCREEN_WIDTH/2f)-(BOSS1_WIDTH/2f);
			boolean crossedCenterToRight = direction > 0 && prevX < centerX && getX() >= centerX;
			boolean crossedCenterToLeft = direction < 0 && prevX > centerX && getX() <= centerX;
			if(crossedCenterToRight || crossedCenterToLeft){
				setX(centerX);
				isStopped = true;
			}
            // tao gioi han bien man hinh
            if (getX() <= LEFT_BOUNDARY) {
                setX(LEFT_BOUNDARY);
                direction = 1;
            } else if (getX() + getWidth() >= RIGHT_BOUNDARY) {
                setX(RIGHT_BOUNDARY - getWidth());
                direction = -1;
            }
        }
        animationTimer += delta;
        if (animationTimer >= frameDuration) {
            animationTimer -= frameDuration;
            currentFrame = (currentFrame + 1) % maxFrame;
            this.textureRegion.setRegion(BOSS1_WIDTH * currentFrame, 0, BOSS1_WIDTH, BOSS1_HEIGHT);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
