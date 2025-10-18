package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static io.github.arkanoid.Constants.*;



public class GameLogic {

    Bar barRef;
    Boss bossRef;

    GameLogic (Bar barRef, Boss bossRef) {
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


    public void launch(Ball ball) {
        if (!ball.isLaunched()) {
            float barCenterX = barRef.getX() + barRef.getWidth() / 2f;
            float ballX = barCenterX - BALL_WIDTH / 2f;
            float ballY = barRef.getY() + barRef.getHeight();

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !(ball instanceof Bar_Stage1_Skill1)) {
                ball.setLaunched(true);
                ball.velocityVector.set(0f, BALL_VELOCITY.y);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && ball instanceof Bar_Stage1_Skill1) {
                ball.velocityVector.set(0f, BALL_VELOCITY.y);
            }
            ball.setPosition(ballX, ballY);
        }
    }

    public void barCollision(Ball ball) {
        Rectangle ballRect = ball.getHitBox();
        Rectangle barRect = barRef.getHitBox();
        if (ballRect.overlaps(barRect)) {
            if (ball.velocityVector.y < 0) {

                ball.setY(barRef.getY() + barRef.getHeight());

                float speed = ball.velocityVector.len();
                ball.velocityVector.set(speed * (float) Math.sin(bounceAngle(ballRect, barRect)), Math.abs(speed * (float) Math.cos(bounceAngle(ballRect, barRect))));
            }
        }
    }

    public void boundaryCollision(Ball ball, float delta, int topBoundary) {
        if (ball.getY() <= DOWN_BOUNDARY) {
            if (ball instanceof Bar_Stage1_Skill1) {
                ball.remove();
                ((Bar_Stage1_Skill1) ball).startSkill1Cooldown();
            } else {
                barRef.takeDamage();
                ball.resetLaunch();
            }
        }
        if (ball.getX() + ball.velocityVector.x * delta <= LEFT_BOUNDARY) {
            ball.setPosition(LEFT_BOUNDARY, ball.getY());
            ball.velocityVector.x =  -ball.velocityVector.x;
        }
        if (ball.getX() + BALL_WIDTH + ball.velocityVector.x * delta  >= RIGHT_BOUNDARY) {
            ball.setPosition(RIGHT_BOUNDARY - BALL_WIDTH, ball.getY());
            ball.velocityVector.x =  -ball.velocityVector.x;
        }
        if (ball.getY() + BALL_HEIGHT + ball.velocityVector.y * delta >= topBoundary) {
            ball.setPosition(ball.getX(),  topBoundary - BALL_HEIGHT);
            ball.velocityVector.y =  -ball.velocityVector.y;
        }
    }

    public void bossCollision(Ball ball) {
        Rectangle barRect = barRef.getHitBox();
        Rectangle ballRect = ball.getHitBox();
        Rectangle bossRect = bossRef.getHitBox();

        if (ballRect.overlaps(bossRect)) {
            if (bossRef instanceof Boss1) {
                int newDamage = ball.getDamage();
                bossRef.takeDamage(newDamage);
                if (bossRef.isDead()) {
                    ball.resetLaunch();
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
                    ball.setX(bossRect.x - ballRect.width);
                } else {
                    normal.set(1, 0);
                    ball.setX(bossRect.x + bossRect.width);
                }
            } else {
                if (ballCenterY < bossCenterY) {
                    normal.set(0, -1);
                    ball.setY(bossRect.y - ballRect.height);
                } else {
                    normal.set(0, 1);
                    ball.setY(bossRect.y + bossRect.height);
                }
            }

            ball.velocityVector = reflect(ball.velocityVector, normal);
        }

        if (barRect.overlaps(bossRect)) {
            barRef.takeDamage();
        }
    }

    public void skillCollision(Stage stage) {
        Rectangle barHitbox = barRef.getHitBox();
        for (Actor actor : stage.getActors()) {
            if (actor instanceof BombProjectile bomb) {
                if (bomb.getHitbox().overlaps(barHitbox)) {
                    barRef.takeDamage();
                    bomb.remove();
                }
            }
            else if (actor instanceof LaserEffect laser) {
                if (laser.getHitbox().overlaps(barHitbox)) {
                    barRef.takeDamage();
                }
            }
        }
    }

    public void barLaserCollision(Bar_Stage1_Skill2 barStage1Skill2)  {
        BarLaserEffect barLaserEffect = barStage1Skill2.getBarLaserEffect();

        if (barLaserEffect == null) {
            return;
        }

        Rectangle barLaserRect = barLaserEffect.getHitbox();
        Rectangle bossRect = bossRef.getHitBox();

        if (barLaserRect.overlaps(bossRect)) {
            bossRef.takeDamage(BAR_STAGE1_DAMAGE);
        }
    }
}
