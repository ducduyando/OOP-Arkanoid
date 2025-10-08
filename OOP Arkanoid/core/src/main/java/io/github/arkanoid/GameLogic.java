package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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

    private static final Vector2 tempVec = new Vector2();
    public static Vector2 reflect(Vector2 incoming, Vector2 normal) {
        // Công thức: v' = v - 2  (v . n)  n
        // Bước 1: Tính tích vô hướng (dot product)
        float dot = incoming.dot(normal);

        // Bước 2: Tính phần vector (2  dot  n) và lưu vào vector tạm
        tempVec.set(normal).scl(2 * dot);

        // Bước 3: Lấy vector incoming trừ đi vector tạm
        incoming.sub(tempVec);

        return incoming;
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

        // 1. Kiểm tra va chạm
        if (ballRect.overlaps(bossRect)) {
            // Gây sát thương cho trùm (bạn đã làm đúng)
            boss1Ref.takeDamage(10);

            // 2. Tính toán các giá trị cần thiết
            float ballCenterX = ballRect.x + ballRect.width / 2;
            float ballCenterY = ballRect.y + ballRect.height / 2;
            float bossCenterX = bossRect.x + bossRect.width / 2;
            float bossCenterY = bossRect.y + bossRect.height / 2;

            // Tính toán độ "xâm nhập" của bóng vào trùm ở cả hai trục
            float overlapX = (ballRect.width / 2 + bossRect.width / 2) - Math.abs(ballCenterX - bossCenterX);
            float overlapY = (ballRect.height / 2 + bossRect.height / 2) - Math.abs(ballCenterY - bossCenterY);

            Vector2 normal = new Vector2();

            // 3. Xác định cạnh va chạm và vector pháp tuyến
            if (overlapX < overlapY) {
                // Đây là va chạm bên hông (trái hoặc phải)
                if (ballCenterX < bossCenterX) {
                    // Va chạm vào cạnh trái của trùm
                    normal.set(-1, 0);
                    ballRef.setX(bossRect.x - ballRect.width); // Đẩy bóng ra ngoài
                } else {
                    // Va chạm vào cạnh phải của trùm
                    normal.set(1, 0);
                    ballRef.setX(bossRect.x + bossRect.width); // Đẩy bóng ra ngoài
                }
            } else {
                // Đây là va chạm ở trên hoặc dưới
                if (ballCenterY < bossCenterY) {
                    // Va chạm vào cạnh dưới của trùm
                    normal.set(0, -1);
                    ballRef.setY(bossRect.y - ballRect.height); // Đẩy bóng ra ngoài
                } else {
                    // Va chạm vào cạnh trên của trùm
                    normal.set(0, 1);
                    ballRef.setY(bossRect.y + bossRect.height); // Đẩy bóng ra ngoài
                }
            }

            // 4. Thực hiện phản xạ!
            ballRef.velocityVector = reflect(ballRef.velocityVector, normal);
        }

        // Phần code va chạm giữa thanh và trùm giữ nguyên
        if (barRect.overlaps(bossRect)) {
            int currentState = barRef.getState() + 1;
            barRef.setState(currentState);
        }
    }
    }
