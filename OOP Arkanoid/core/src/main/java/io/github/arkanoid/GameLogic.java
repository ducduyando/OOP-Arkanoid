package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.Constants.*;



public class GameLogic {

    Ball ballRef;
    Bar barRef;

    GameLogic (Ball ballRef, Bar barRef) {
        this.ballRef = ballRef;
        this.barRef = barRef;
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
        Rectangle ballRect = ballRef.getBound();
        Rectangle barRect = barRef.getBound();
        if (ballRect.overlaps(barRect)) {
            if (ballRef.getY() >= barRect.getY() + barRef.getHeight() && ballRef.velocityVector.getY() < 0) {

                ballRef.setY(barRef.getY() + barRef.getHeight());

                float barCenterX = barRef.getX() + barRef.getWidth() / 2f;
                float ballCenterX = ballRef.getX() + ballRef.getWidth() / 2f;

                float relativeIntersect = (ballCenterX - barCenterX) / (barRef.getWidth() / 2f);

                relativeIntersect = Math.max(-1f, Math.min(1f, relativeIntersect));

                float bounceAngle = relativeIntersect * MAX_BOUNCE_ANGLE;

                float speed = ballRef.velocityVector.length();

                ballRef.velocityVector.setX(speed * (float) Math.sin(bounceAngle));
                ballRef.velocityVector.setY(Math.abs(speed * (float) Math.cos(bounceAngle)));
            }
        }
    }

    public void boundaryCollision() {
        if (ballRef.getY() <= DOWN_BOUNDARY) {
            int currentState = barRef.getState() + 1;
            barRef.setState(currentState);
            ballRef.resetLaunch();
        }
        if (ballRef.getX() <= LEFT_BOUNDARY || ballRef.getX() + BALL_WIDTH >= RIGHT_BOUNDARY) {
            ballRef.velocityVector.mulX(-1);
        }
        if (ballRef.getY() + BALL_HEIGHT >= UP_BOUNDARY) {
            ballRef.velocityVector.mulY(-1);
        }
    }

}
