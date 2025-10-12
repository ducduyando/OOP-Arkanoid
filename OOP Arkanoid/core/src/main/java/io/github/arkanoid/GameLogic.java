package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static io.github.arkanoid.Constants.*;



public class GameLogic {

    Ball ballRef;
    Bar barRef;
    Boss bossRef;

    GameLogic (Ball ballRef, Bar barRef, Boss bossRef) {
        this.ballRef = ballRef;
        this.barRef = barRef;
        this.bossRef = bossRef;
    }

    public float bounceAngle (Rectangle ballRect, Rectangle objectRect) {
        float objectCenterX = objectRect.getX() + objectRect.getWidth() / 2f;
        float ballCenterX = ballRect.getX() + ballRect.getWidth() / 2f;

        float relativeIntersect = (ballCenterX - objectCenterX) / (objectRect.getWidth() / 2f);

        relativeIntersect = Math.max(-1f, Math.min(1f, relativeIntersect));

        return relativeIntersect * MAX_BOUNCE_ANGLE;
    }

    private static final Vector2 tempVec = new Vector2();
    public static Vector2 reflect(Vector2 incoming, Vector2 normal) {
        float dot = incoming.dot(normal);
        tempVec.set(normal).scl(2 * dot);
        incoming.sub(tempVec);

        return incoming;
    }


    public void launch() {
        if (!ballRef.isLaunched()) {
            float barCenterX = barRef.getX() + barRef.getWidth() / 2f;
            float ballX = barCenterX - BALL_WIDTH / 2f;
            float ballY = barRef.getY() + barRef.getHeight();

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                ballRef.setLaunched(true);
                ballRef.velocityVector.set(0f, BALL_VELOCITY_Y);
            }
            ballRef.setPosition(ballX, ballY);
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
            barRef.takeDamage();
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
        Rectangle bossRect = bossRef.getHitBox();

        if (ballRect.overlaps(bossRect)) {
            if (bossRef instanceof Boss1) {
                bossRef.takeDamage(15);
                if (bossRef.isDead()) {
                    ballRef.resetLaunch();
                }
            }

            float ballCenterX = ballRect.x + ballRect.width / 2;
            float ballCenterY = ballRect.y + ballRect.height / 2;
            float bossCenterX = bossRect.x + bossRect.width / 2;
            float bossCenterY = bossRect.y + bossRect.height / 2;

            float overlapX = (ballRect.width / 2 + bossRect.width / 2) - Math.abs(ballCenterX - bossCenterX);
            float overlapY = (ballRect.height / 2 + bossRect.height / 2) - Math.abs(ballCenterY - bossCenterY);

            Vector2 normal = new Vector2();

            if (overlapX < overlapY) {
                if (ballCenterX < bossCenterX) {
                    normal.set(-1, 0);
                    ballRef.setX(bossRect.x - ballRect.width);
                } else {
                    normal.set(1, 0);
                    ballRef.setX(bossRect.x + bossRect.width);
                }
            } else {
                if (ballCenterY < bossCenterY) {
                    normal.set(0, -1);
                    ballRef.setY(bossRect.y - ballRect.height);
                } else {
                    normal.set(0, 1);
                    ballRef.setY(bossRect.y + bossRect.height);
                }
            }

            ballRef.velocityVector = reflect(ballRef.velocityVector, normal);
        }

        if (barRect.overlaps(bossRect)) {
            barRef.takeDamage();
        }
    }

    public void skillCollision(Stage stage) {
        Rectangle barHitbox = barRef.getHitBox();
        for (Actor actor : stage.getActors()) {
            if (actor instanceof BombProjectile) {
                BombProjectile bomb = (BombProjectile) actor;
                if (bomb.getHitbox().overlaps(barHitbox)) {
                    barRef.takeDamage();
                    bomb.remove();
                }
            }
            else if (actor instanceof LaserEffect) {
                LaserEffect laser = (LaserEffect) actor;
                if (laser.getHitbox().overlaps(barHitbox)) {
                    barRef.takeDamage();
                }
            }
        }
    }
}
