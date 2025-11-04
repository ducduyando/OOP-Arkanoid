package io.github.arkanoid.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.arkanoid.boss1.BombProjectile;
import io.github.arkanoid.boss1.LaserEffect;
import io.github.arkanoid.boss2.BeeEnemy;
import io.github.arkanoid.boss3.Rocket;
import io.github.arkanoid.boss3.Chainsaw;
import io.github.arkanoid.boss3.Spike;
import io.github.arkanoid.entities.FinalBoss;
import io.github.arkanoid.paddle.Paddle;
import io.github.arkanoid.paddle.PaddleLaserEffect;
import io.github.arkanoid.paddle.PaddleSkill1A;
import io.github.arkanoid.paddle.PaddleSkill1B;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.entities.Boss;

import static io.github.arkanoid.core.Constants.*;



public class GameLogic {

    Paddle paddleRef;
    Boss bossRef;
    FinalBoss finalBossRef;

    public GameLogic(Paddle paddleRef) {
        this.paddleRef = paddleRef;
    }

    public GameLogic(Paddle paddleRef, Boss bossRef) {
        this.paddleRef = paddleRef;
        this.bossRef = bossRef;
    }

    public GameLogic(Paddle paddleRef, FinalBoss finalBossRef) {
        this.paddleRef = paddleRef;
        this.finalBossRef = finalBossRef;
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
            float paddleCenterX = paddleRef.getX() + paddleRef.getWidth() / 2f;
            float ballX = paddleCenterX - BALL_WIDTH / 2f;
            float ballY = paddleRef.getY() + paddleRef.getHeight();

            if (!(ball instanceof PaddleSkill1A)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    ball.setLaunched(true);
                    ball.setVelocity(0f, BALL_VELOCITY.y);
                }
            }
            ball.setPosition(ballX, ballY);
        }
    }

    public boolean paddleCollision(Ball ball) {
        // Force check game over first
        if (paddleRef.isGameOver()) {
            System.out.println("GameLogic: paddleCollision skipped - paddle is game over (state: " + paddleRef.getState() + ")");
            return false;
        }

        Rectangle ballRect = ball.getHitBox();
        Rectangle paddleRect = paddleRef.getHitBox();

        // Double check - if paddle rect is empty, skip
        if (paddleRect.width <= 0 || paddleRect.height <= 0) {
            return false;
        }

        if (ballRect.overlaps(paddleRect)) {
            if (ball.getVelocity().y < 0) {

                ball.setY(paddleRef.getY() + paddleRef.getHeight());

                float speed = ball.getVelocity().len();
                ball.setVelocity(speed * (float) Math.sin(bounceAngle(ballRect, paddleRect)), Math.abs(speed * (float) Math.cos(bounceAngle(ballRect, paddleRect))));
            }
            return true;
        }
        return false;
    }

    public void boundaryCollision(Ball ball, float delta, int topBoundary) {
        if (ball.getY() <= DOWN_BOUNDARY) {
            if (ball instanceof PaddleSkill1A) {
                ((PaddleSkill1A) ball).startSkill1Cooldown();
                ((PaddleSkill1A) ball).cleanup();

            } else {
                paddleRef.takeDamage();

            }
            ball.resetLaunch();
        }
        if (ball.getX() + ball.getVelocity().x * delta <= LEFT_BOUNDARY) {
            ball.setPosition(LEFT_BOUNDARY, ball.getY());
            reflect(ball.getVelocity(), new Vector2(1,0));
        }
        if (ball.getX() + BALL_WIDTH + ball.getVelocity().x * delta  >= RIGHT_BOUNDARY) {
            ball.setPosition(RIGHT_BOUNDARY - BALL_WIDTH, ball.getY());
            reflect(ball.getVelocity(), new Vector2(-1, 0));
        }
        if (ball.getY() + BALL_HEIGHT + ball.getVelocity().y * delta >= topBoundary) {
            ball.setPosition(ball.getX(),  topBoundary - BALL_HEIGHT);
            reflect(ball.getVelocity(), new Vector2(0, -1));
        }
    }

    public void bossCollision(Ball ball) {
        if (bossRef == null) {
            return;
        }

        Rectangle paddleRect = paddleRef.getHitBox();
        Rectangle ballRect = ball.getHitBox();
        Rectangle bossRect = bossRef.getHitBox();

        if (ballRect.overlaps(bossRect)) {
            bossRef.takeDamage(BALL_DAMAGE);

            if (bossRef.isDead()) {
                ball.resetLaunch();
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

            Vector2 reflectVector = reflect(ball.getVelocity(), normal);
            ball.setVelocity(reflectVector.x, reflectVector.y);
        }

        if (paddleRect.overlaps(bossRect)) {
            paddleRef.takeDamage();
        }
    }

    public void finalBossCollision(Ball ball) {
        if (finalBossRef == null) {
            return;
        }
        Rectangle paddleRect = paddleRef.getHitBox();
        Rectangle ballRect = ball.getHitBox();
        Rectangle finalBossRect = finalBossRef.getHitBox();

        if (ballRect.overlaps(finalBossRect)) {
            finalBossRef.takeDamage(BALL_DAMAGE);
            float ballCenterX = ballRect.x + ballRect.width / 2f;
            float ballCenterY = ballRect.y + ballRect.height / 2f;
            float finalBossCenterX = finalBossRect.x + finalBossRect.width / 2f;
            float finalBossCenterY = finalBossRect.y + finalBossRect.height / 2f;

            float overlapX = (ballRect.width / 2 + finalBossRect.width / 2f) - Math.abs(ballCenterX - finalBossCenterX);
            float overlapY = (ballRect.height / 2 + finalBossRect.height / 2f) - Math.abs(ballCenterY - finalBossCenterY);

            Vector2 normal = new Vector2();

            if (overlapX < overlapY) {
                if (ballCenterX < finalBossCenterX) {
                    normal.set(-1, 0);
                    ball.setX(finalBossRect.x - ballRect.width);
                } else {
                    normal.set(1, 0);
                    ball.setX(finalBossRect.x + finalBossRect.width);
                }
            } else {
                if (ballCenterY < finalBossCenterY) {
                    normal.set(0, -1);
                    ball.setY(finalBossRect.y - ballRect.height);
                } else {
                    normal.set(0, 1);
                    ball.setY(finalBossRect.y + finalBossRect.height);
                }
            }

            Vector2 reflectVector = reflect(ball.getVelocity(), normal);
            ball.setVelocity(reflectVector.x, reflectVector.y);
        }
        if (paddleRect.overlaps(finalBossRect)) {
            paddleRef.takeDamage();
        }
    }

    public void skillCollision(Stage stage, Ball ball) {
        Rectangle paddleHitbox = paddleRef.getHitBox();
        Rectangle ballHitBox = ball.getHitBox();
        for (Actor actor : stage.getActors()) {
            if (actor instanceof BombProjectile bomb) {
                if (bomb.getHitbox().overlaps(paddleHitbox)) {
                    paddleRef.takeDamage();
                    bomb.remove();
                }
            }
            else if (actor instanceof LaserEffect laser) {
                if (laser.getHitbox().overlaps(paddleHitbox)) {
                    paddleRef.takeDamage();
                }
            }
            else if (actor instanceof BeeEnemy bee) {
                if (bee.getHitBox().overlaps(paddleHitbox)) {
                    paddleRef.takeDamage();
                    bee.remove();
                }
            }
            else if (actor instanceof Chainsaw saw) {
                if (saw.getHitBox().overlaps(paddleHitbox)) {
                    paddleRef.takeDamage();
                }
            }
            else if (actor instanceof Rocket rocket) {
                if (rocket.getHitBox().overlaps(paddleHitbox)) {
                    paddleRef.takeDamage();
                }
            }
            else if (actor instanceof Spike spike) {
                if (spike.getHitBox().overlaps(ballHitBox)) {
                    float ballCenterX = ballHitBox.x + ballHitBox.width / 2f;
                    float ballCenterY = ballHitBox.y + ballHitBox.height / 2f;
                    float finalBossCenterX = spike.getHitBox().x + spike.getHitBox().width / 2f;
                    float finalBossCenterY = spike.getHitBox().y + spike.getHitBox().height / 2f;

                    float overlapX = (ballHitBox.width / 2 + spike.getHitBox().width / 2f) - Math.abs(ballCenterX - finalBossCenterX);
                    float overlapY = (ballHitBox.height / 2 + spike.getHitBox().height / 2f) - Math.abs(ballCenterY - finalBossCenterY);

                    Vector2 normal = new Vector2();

                    if (overlapX < overlapY) {
                        if (ballCenterX < finalBossCenterX) {
                            normal.set(-1, 0);
                            ball.setX(spike.getHitBox().x - ballHitBox.width);
                        } else {
                            normal.set(1, 0);
                            ball.setX(spike.getHitBox().x + spike.getHitBox().width);
                        }
                    } else {
                        if (ballCenterY < finalBossCenterY) {
                            normal.set(0, -1);
                            ball.setY(spike.getHitBox().y - ballHitBox.height);
                        } else {
                            normal.set(0, 1);
                            ball.setY(spike.getHitBox().y + spike.getHitBox().height);
                        }
                    }

                    Vector2 reflectVector = reflect(ball.getVelocity(), normal);
                    ball.setVelocity(reflectVector.x, reflectVector.y);
                }
            }
        }
    }

    public void paddleLaserCollision(PaddleSkill1B paddleStage1Skill2)  {
        PaddleLaserEffect paddleLaserEffect = paddleStage1Skill2.getPaddleLaserEffect();

        if (paddleLaserEffect == null) {
            return;
        }

        Rectangle paddleLaserRect = paddleLaserEffect.getHitbox();

        if (bossRef != null) {
            Rectangle bossRect = bossRef.getHitBox();
            if (paddleLaserRect.overlaps(bossRect)) {
                bossRef.takeDamage(PADDLE_DAMAGE);
            }
        }

        if (finalBossRef != null) {
            Rectangle finalBossRect = finalBossRef.getHitBox();
            if (paddleLaserRect.overlaps(finalBossRect)) {
                finalBossRef.takeDamage(PADDLE_DAMAGE);
            }
        }
    }
}
