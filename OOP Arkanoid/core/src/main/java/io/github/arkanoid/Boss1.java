package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static io.github.arkanoid.Constants.*;
import static io.github.arkanoid.Constants.BOSS1_HEIGHT;
import static io.github.arkanoid.Constants.BOSS1_WIDTH;
import static io.github.arkanoid.Constants.LEFT_BOUNDARY;
import static io.github.arkanoid.Constants.RIGHT_BOUNDARY;
//m
public class Boss1 extends Boss {
    private int currentFrame = 0;
    private float animationTimer = 0f;
    private float direction = 1f;
    private float stopTimer = 0f;
    private boolean isStopped = false;
    private final int maxFrame;
    private final float cell_x_size;
    private final float cell_y_size;
    private final float[][] positionX = new float[ROWS][COLS];
    private final float[][] positionY = new float[ROWS][COLS];
    private float targetX;
    private float targetY;

    Boss1(Texture texture, float x, float y) {
        super(x, y);
        this.textureRegion = new TextureRegion(texture, 0, 0, BOSS1_WIDTH, BOSS1_HEIGHT);
        this.velocityVector = new Vector2(BOSS1_VELOCITY_X, BOSS1_VELOCITY_Y);
        this.hitBox = new Rectangle(x, y, BOSS1_WIDTH, BOSS1_HEIGHT);
        maxFrame = texture.getWidth() / BOSS1_WIDTH;
        setSize(BOSS1_WIDTH, BOSS1_HEIGHT);

        cell_x_size = (float) (SCREEN_WIDTH - BOSS1_WIDTH) / COLS;
        cell_y_size = (float) (SCREEN_HEIGHT / 2 - BOSS1_HEIGHT) / COLS;

        for(int r = 0;r < ROWS;r++) {
            for(int c = 0;c < COLS;c++) {

                positionX[r][c] = c * cell_x_size;
                positionY[r][c] = SCREEN_HEIGHT / 2f + r * cell_y_size;
            }
        }
    }

    private void chooseNewTarget() {
        do{
            int r = (int) (Math.random() * ROWS);
            int c = (int) (Math.random() * COLS);
            targetX = positionX[r][c];
            targetY = positionY[r][c];
        }
        while (Math.abs(targetX - getX()) <= cell_x_size && Math.abs(targetY - getY()) <= cell_y_size);


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
			if(stopTimer >= 2f){
				isStopped = false;
				stopTimer = 0;
				chooseNewTarget();
			}
		}
		else{
        float prevX = getX();
        moveBy(direction * speed,0);
        float dx= targetX -getX();
         float dy =targetY -getY();
         if(Math.abs(dy) >2f){
             float vy =Math.signum(dy) *(speed / 2f);
             moveBy(0, vy);
         }
            if(Math.abs(dx) >4f){
                float vx =Math.signum(dx) *(speed / 4f);
                moveBy(vx, 0);
            }
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
