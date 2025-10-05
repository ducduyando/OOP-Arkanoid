package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import static io.github.arkanoid.Constants.*;

public class Ball extends Actor {
    TextureRegion textureRegion;
    Vector velocityVector = new Vector(BALL_VELOCITY_X, BALL_VELOCITY_Y);
// check xem bong dc ban ra chua
 private boolean isLaunched = false;
 // lay vi tri thanh bar
    private Bar barRef;
    private static final ShapeRenderer shapeRenderer = new ShapeRenderer();
    Ball(Texture texture, Bar bar, float x, float y) {
        this.textureRegion = new TextureRegion(texture);
        this.barRef = bar;
        setPosition(x, y);
        setSize(BALL_WIDTH, BALL_HEIGHT);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
    }

    public Rectangle getBound(){
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void boundaryCollision() {
        if (getX() <= LEFT_BOUNDARY || getX() + getWidth() >= RIGHT_BOUNDARY) {
            this.velocityVector.mulX(-1);
        }
        if (getY() <= DOWN_BOUNDARY || getY() + getHeight() >= UP_BOUNDARY) {
            this.velocityVector.mulY(-1);
        }
    }

    public void barCollision() {
        Rectangle ballRect = getBound();
        Rectangle barRect = barRef.getBound();
        if (ballRect.overlaps(barRect)) {
            if (getY() + BALL_HEIGHT > barRect.getY() && velocityVector.getY() < 0) {

                setY(barRef.getY() + barRef.getHeight());

                // tinh goc phan xa
                float barCenterX = barRef.getX() + barRef.getWidth() / 2f;
                float ballCenterX = getX() + getWidth() / 2f;

                // khoang cach giua bong va tam bar
                float relativeIntersect = (ballCenterX - barCenterX) / (barRef.getWidth() / 2f);

                // khoang gioi han
                relativeIntersect = Math.max(-1f, Math.min(1f, relativeIntersect));

                // Goc phan xa that
                float bounceAngle = relativeIntersect * MAX_BOUNCE_ANGLE;

                // toc do tong cua bong
                float speed = velocityVector.length();

                // cap nhap lai gia tri vector
                velocityVector.setX(speed * (float) Math.sin(bounceAngle));
                // huong Y phai duong de di len tren (toa do y tang len tren)
                velocityVector.setY(Math.abs(speed * (float) Math.cos(bounceAngle)));
            }
        }
    }

    @Override
    public void act(float delta) {
        if (!isLaunched) {
            float barCenterX = barRef.getX() + barRef.getWidth() / 2f;
            float ballX = barCenterX - getWidth() / 2f;
            float ballY = barRef.getY() + barRef.getHeight();
            setPosition(ballX, ballY);

            // bam space de ban bong
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                isLaunched = true;
                // ban thang len tren
                velocityVector.setX(0f);
                velocityVector.setY(BALL_VELOCITY_Y);
            }
            return;
        }
        moveBy(velocityVector.getX(), velocityVector.getY());

        boundaryCollision();
        barCollision();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        // Ve hitbox
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());

        shapeRenderer.end();


        batch.begin();
    }

    public void resetLaunch() {
        isLaunched = false;
    }


}
