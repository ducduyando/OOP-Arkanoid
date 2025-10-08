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
                ballRef.velocityVector.set(0f, BALL_VELOCITY_Y);
            }
        }
    }

    public void barCollision() {
        Rectangle ballRect = ballRef.getHitBox();
        Rectangle barRect = barRef.getHitBox();
        if (ballRect.overlaps(barRect)) {
            if (ballRef.velocityVector.y < 0) {

                ballRef.setY(barRef.getY() + barRef.getHeight());

                float speed = ballRef.velocityVector.len();
                ballRef.velocityVector.set(speed * (float) Math.sin(bounceAngle(ballRect, barRect)), Math.abs(speed * (float) Math.cos(bounceAngle(ballRect, barRect))));
            }
        }
    }

    public void boundaryCollision(float delta) {
        if (ballRef.getY() <= DOWN_BOUNDARY) {
            int currentState = barRef.getState() + 1;
            barRef.setState(currentState);
            ballRef.resetLaunch();
        }
        if (ballRef.getX() + ballRef.velocityVector.x * delta <= LEFT_BOUNDARY || ballRef.getX() + BALL_WIDTH + ballRef.velocityVector.x * delta  >= RIGHT_BOUNDARY) {
            ballRef.velocityVector.x =  -ballRef.velocityVector.x;
        }
        if (ballRef.getY() + BALL_HEIGHT + ballRef.velocityVector.y * delta >= UP_BOUNDARY) {
            ballRef.velocityVector.y =  -ballRef.velocityVector.y;
        }
    }

    public void bossCollision() {
        Rectangle barRect = barRef.getHitBox();
        Rectangle ballRect = ballRef.getHitBox();
        Rectangle bossRect = boss1Ref.getHitBox();

        if (ballRect.overlaps(bossRect)) {
            boss1Ref.takeDamage(10);
            float speed = ballRef.velocityVector.len();
            float ballCenterX = ballRect.getX() + ballRect.getWidth() / 2f;
            float ballCenterY = ballRect.getY() + ballRect.getHeight() / 2f;
            float bossCenterX = bossRect.getX() + bossRect.getWidth() / 2f;
            float bossCenterY = bossRect.getY() + bossRect.getHeight() / 2f;
            float angle = (float) Math.atan2(ballCenterY - bossCenterY, ballCenterX - bossCenterX);

            ballRef.velocityVector.x = speed * (float) Math.sin(angle);

            float newVeY = Math.abs(speed * (float) Math.cos(angle));
            if ((ballRect.getY() + ballRect.getHeight() / 2f) < (bossRect.getY() + bossRect.getHeight() / 2f)) {
                newVeY *= -1;
            }

            ballRef.velocityVector.y = newVeY;

            if ((ballRect.getY() + ballRect.getHeight() / 2f) < (bossRect.getY() + bossRect.getHeight() / 2f)) {
                ballRef.setY(bossRect.getY() - BALL_HEIGHT);
            } else {
                ballRef.setY(bossRect.getY() + bossRect.getHeight());
            }

            ballRef.getHitBox().setPosition(ballRef.getX(), ballRef.getY());
        }

        if (barRect.overlaps(bossRect)) {
            int currentState = barRef.getState() + 1;
            barRef.setState(currentState);
        }

    }

    }
