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
    private boolean isLaunched = false;
    private Bar barRef;

    Ball(Texture texture, Bar bar, float x, float y) {
        this.textureRegion = new TextureRegion(texture);
        this.barRef = bar;
        setPosition(x, y);
        setSize(BALL_WIDTH, BALL_HEIGHT);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
    }

    public Rectangle getBound(){
        return new Rectangle(getX(), getY(), BALL_WIDTH, BALL_HEIGHT);
    }

    public void resetLaunch() {
        isLaunched = false;
    }

    public void launch() {
        if (!isLaunched) {
            float barCenterX = barRef.getX() + BAR_WIDTH / 2f;
            float ballX = barCenterX - BALL_WIDTH / 2f;
            float ballY = barRef.getY() + BAR_HEIGHT;
            setPosition(ballX, ballY);

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                isLaunched = true;
                velocityVector.setX(0f);
                velocityVector.setY(BALL_VELOCITY_Y);
            }
        }
    }

    public void boundaryCollision() {
        if (getY() <= DOWN_BOUNDARY) {
            resetLaunch();
        }
        if (getX() <= LEFT_BOUNDARY || getX() + BALL_WIDTH >= RIGHT_BOUNDARY) {
            this.velocityVector.mulX(-1);
        }
        if (getY() + BALL_HEIGHT >= UP_BOUNDARY) {
            this.velocityVector.mulY(-1);
        }
    }

    public void barCollision() {
        Rectangle ballRect = getBound();
        Rectangle barRect = barRef.getBound();
        if (ballRect.overlaps(barRect)) {
            if (getY() + BALL_HEIGHT > barRect.getY() && velocityVector.getY() < 0) {

                setY(barRef.getY() + barRef.getHeight());

                float barCenterX = barRef.getX() + barRef.getWidth() / 2f;
                float ballCenterX = getX() + getWidth() / 2f;

                float relativeIntersect = (ballCenterX - barCenterX) / (barRef.getWidth() / 2f);

                relativeIntersect = Math.max(-1f, Math.min(1f, relativeIntersect));

                float bounceAngle = relativeIntersect * MAX_BOUNCE_ANGLE;

                float speed = velocityVector.length();

                velocityVector.setX(speed * (float) Math.sin(bounceAngle));
                velocityVector.setY(Math.abs(speed * (float) Math.cos(bounceAngle)));
            }
        }
    }

    @Override
    public void act(float delta) {

        launch();
        if (isLaunched) {
            moveBy(velocityVector.getX(), velocityVector.getY());
            boundaryCollision();
            barCollision();
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
