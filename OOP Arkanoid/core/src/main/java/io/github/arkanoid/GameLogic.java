package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.Constants.*;



public class GameLogic {

    Ball ballRef;
    Bar barRef;
    Boss1 boss1Ref;

    GameLogic (Ball ballRef, Bar barRef, Boss1 boss1Ref) {
        this.ballRef = ballRef;
        this.barRef = barRef;
        this.boss1Ref = boss1Ref;
    }

    public float bounceAngle (Rectangle ballRect, Rectangle objectRect) {
        float objectCenterX = objectRect.getX() + objectRect.getWidth() / 2f;
        float ballCenterX = ballRect.getX() + ballRect.getWidth() / 2f;

        float relativeIntersect = (ballCenterX - objectCenterX) / (objectRect.getWidth() / 2f);

        relativeIntersect = Math.max(-1f, Math.min(1f, relativeIntersect));

        return relativeIntersect * MAX_BOUNCE_ANGLE;
    }

    public void launch() {
        if (!ballRef.isLaunched()) {
            float barCenterX = barRef.getX() + barRef.getWidth() / 2f;
            float ballX = barCenterX - BALL_WIDTH / 2f;
            float ballY = barRef.getY() + barRef.getHeight();
            ballRef.setPosition(ballX, ballY);

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                ballRef.setLaunched(true);
                ballRef.velocityVector.setX(0f);
                ballRef.velocityVector.setY(BALL_VELOCITY_Y);
            }
        }
    }

    public void barCollision() {
        Rectangle ballRect = ballRef.getHitBox();
        Rectangle barRect = barRef.getHitBox();
        if (ballRect.overlaps(barRect)) {
            if (ballRef.velocityVector.getY() < 0) {

                ballRef.setY(barRef.getY() + barRef.getHeight());

                float speed = ballRef.velocityVector.length();

                ballRef.velocityVector.setX(speed * (float) Math.sin(bounceAngle(ballRect, barRect)));
                ballRef.velocityVector.setY(Math.abs(speed * (float) Math.cos(bounceAngle(ballRect, barRect))));
            }
        }
    }

    public void boundaryCollision(float delta) {
        if (ballRef.getY() <= DOWN_BOUNDARY) {
            int currentState = barRef.getState() + 1;
            barRef.setState(currentState);
            ballRef.resetLaunch();
        }
        if (ballRef.getX() + ballRef.velocityVector.getX() * delta <= LEFT_BOUNDARY || ballRef.getX() + BALL_WIDTH + + ballRef.velocityVector.getX() * delta  >= RIGHT_BOUNDARY) {
            ballRef.velocityVector.mulX(-1);
        }
        if (ballRef.getY() + BALL_HEIGHT + ballRef.velocityVector.getY() * delta >= UP_BOUNDARY) {
            ballRef.velocityVector.mulY(-1);
        }
    }

    public void bossCollision() {
        Rectangle barRect = barRef.getHitBox();
        Rectangle ballRect = ballRef.getHitBox();
        Rectangle boss1Rect = boss1Ref.getHitBox();
        if (ballRect.overlaps(boss1Rect)) {

            float speed = ballRef.velocityVector.length();

            ballRef.velocityVector.setX(speed * (float) Math.sin(bounceAngle(ballRect, boss1Rect)));
            ballRef.velocityVector.setY(Math.abs(speed * (float) Math.cos(bounceAngle(ballRect, boss1Rect))));
        }

        if (barRect.overlaps(boss1Rect)) {
            int curretnState = barRef.getState() + 1;
            barRef.setState(curretnState);
        }
    }

}
