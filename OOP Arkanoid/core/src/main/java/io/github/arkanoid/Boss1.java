package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

import static io.github.arkanoid.Constants.*;

public class Boss1 extends Boss {
    private int currentFrame = 0;
    private float animationTimer = 0f;
    private float stopTimer = 0f;
    private boolean isStopped = false;
    private final int maxFrame;
    private int skill1Counter = 1;
    private boolean isSkill1Paused = true;
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

        float cell_x_size = (float) (SCREEN_WIDTH - BOSS1_WIDTH) / COLS;
        float cell_y_size = (float) (SCREEN_HEIGHT / 2 - BOSS1_HEIGHT) / ROWS;

        for(int r = 0;r < ROWS;r++) {
            for(int c = 0;c < COLS;c++) {

                positionX[r][c] = c * cell_x_size;
                positionY[r][c] = SCREEN_HEIGHT / 2f + r * cell_y_size;
            }
        }
        chooseNewTarget();
    }

    public void toCenter(float delta) {
        Vector2 currentPosition = new Vector2(getX(), getY());
        Vector2 centerPosition = new Vector2((SCREEN_WIDTH - BOSS1_WIDTH) / 2f,  (SCREEN_HEIGHT / 2f - BOSS1_HEIGHT + SCREEN_HEIGHT) / 2f);

        float distance = currentPosition.dst(centerPosition);
        if (distance < velocityVector.len() * delta) {
            setPosition(centerPosition.x, centerPosition.y);
            skill1Counter = 0;
            isSkill1Paused = false;
            isStopped = true;
        } else {
            Vector2 direction = centerPosition.sub(currentPosition).nor();
            moveBy(direction.x * velocityVector.x * delta, direction.y * velocityVector.y * delta);
        }
    }

    private void chooseNewTarget() {
        Random random = new Random();
        int r = random.nextInt(ROWS);
        int c = random.nextInt(COLS);
        targetX = positionX[r][c];
        targetY = positionY[r][c];
    }

    public void skill1(float delta) {
        if(isSkill1Paused) {
            stopTimer += delta;
            if(stopTimer >= 2f){
                isSkill1Paused = false;
                stopTimer = 0;
                skill1Counter ++;
                chooseNewTarget();
            }
        }
        else {
            Vector2 currentPosition = new Vector2(getX(), getY());
            Vector2 targetPosition = new Vector2(targetX, targetY);

            float distance = currentPosition.dst(targetPosition);
            if (distance < velocityVector.len() * delta) {
                setPosition(targetX, targetY);
                isSkill1Paused = true;
            } else {
                Vector2 direction = targetPosition.sub(currentPosition).nor();
                moveBy(direction.x * velocityVector.x * delta, direction.y * velocityVector.y * delta);
            }
        }
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public void act(float delta) {
        if (!isDead()) {
            if (!isStopped) {
                if (skill1Counter <= 3) {
                    skill1 (delta);
                }
                else {
                    toCenter(delta);
                }
            }
            else {
                stopTimer += delta;
                if(stopTimer >= 3f){
                    isStopped = false;
                    stopTimer = 0;
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
}
