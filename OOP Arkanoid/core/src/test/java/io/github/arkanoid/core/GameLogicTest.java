package io.github.arkanoid.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.GdxTestRunner;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.paddle.Paddle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.github.arkanoid.core.Constants.UP_BOUNDARY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for GameLogic
 */
public class GameLogicTest extends GdxTestRunner {

    @Mock
    private Texture mockTexture;

    @Mock
    private Boss mockBoss;

    private Paddle paddle;
    private Ball ball;
    private GameLogic gameLogic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paddle = new Paddle(mockTexture, 100f, 50f);
        ball = new Ball(150f, 150f);
        gameLogic = new GameLogic(paddle);
    }

    @Test
    void testBounceAngleCalculation() {
        Rectangle ballRect = new Rectangle(200f, 100f, 64f, 64f);
        Rectangle paddleRect = new Rectangle(150f, 50f, 464f, 56f);

        float angle = gameLogic.bounceAngle(ballRect, paddleRect);
        assertTrue(angle >= -Constants.MAX_BOUNCE_ANGLE);
        assertTrue(angle <= Constants.MAX_BOUNCE_ANGLE);
    }

    @Test
    void testBounceAngleCentered() {
        Rectangle ballRect = new Rectangle(200f, 100f, 64f, 64f);
        Rectangle paddleRect = new Rectangle(168f, 50f, 464f, 56f); // Centered

        float angle = gameLogic.bounceAngle(ballRect, paddleRect);
        assertEquals(0f, angle, 0.01f);
    }

    @Test
    void testBounceAngleLeftSide() {
        Rectangle ballRect = new Rectangle(100f, 100f, 64f, 64f);
        Rectangle paddleRect = new Rectangle(150f, 50f, 464f, 56f);

        float angle = gameLogic.bounceAngle(ballRect, paddleRect);
        assertTrue(angle < 0); // Negative angle for left side
    }

    @Test
    void testBounceAngleRightSide() {
        Rectangle ballRect = new Rectangle(500f, 100f, 64f, 64f);
        Rectangle paddleRect = new Rectangle(150f, 50f, 464f, 56f);

        float angle = gameLogic.bounceAngle(ballRect, paddleRect);
        assertTrue(angle > 0); // Positive angle for right side
    }

    @Test
    void testReflectVector() {
        Vector2 incoming = new Vector2(1f, 1f);
        Vector2 normal = new Vector2(0f, -1f);

        Vector2 reflected = GameLogic.reflect(incoming, normal);
        assertNotNull(reflected);
        assertEquals(1f, reflected.x, 0.01f);
        assertEquals(-1f, reflected.y, 0.01f); // Should reflect vertically
    }

    @Test
    void testReflectHorizontal() {
        Vector2 incoming = new Vector2(1f, 0f);
        Vector2 normal = new Vector2(-1f, 0f);

        Vector2 reflected = GameLogic.reflect(incoming, normal);
        assertEquals(-1f, reflected.x, 0.01f);
        assertEquals(0f, reflected.y, 0.01f);
    }

    @Test
    void testBoundaryCollisionBottom() {
        ball.setPosition(100f, 0f);
        ball.setVelocity(0f, -100f);
        ball.setLaunched(true);

        gameLogic.boundaryCollision(ball, 0.1f, UP_BOUNDARY);

        // Ball should reset launch when hitting bottom
        assertFalse(ball.isLaunched());
    }

    @Test
    void testBoundaryCollisionLeft() {
        ball.setPosition(0f, 100f);
        ball.setVelocity(-100f, 0f);
        ball.setLaunched(true);

        float initialX = ball.getX();
        gameLogic.boundaryCollision(ball, 0.1f, UP_BOUNDARY);

        // Velocity should be reflected
        assertTrue(ball.getVelocity().x > 0); // Should bounce right
    }

    @Test
    void testBoundaryCollisionRight() {
        ball.setPosition(Constants.RIGHT_BOUNDARY - Constants.BALL_WIDTH, 100f);
        ball.setVelocity(100f, 0f);
        ball.setLaunched(true);

        gameLogic.boundaryCollision(ball, 0.1f, UP_BOUNDARY);

        // Velocity should be reflected
        assertTrue(ball.getVelocity().x < 0); // Should bounce left
    }

    @Test
    void testBoundaryCollisionTop() {

        ball.setPosition(100f, UP_BOUNDARY - Constants.BALL_HEIGHT);
        ball.setVelocity(0f, 100f);
        ball.setLaunched(true);

        gameLogic.boundaryCollision(ball, 0.1f, UP_BOUNDARY);

        // Velocity should be reflected downward
        assertTrue(ball.getVelocity().y < 0);
    }

    @Test
    void testGameLogicWithBoss() {
        GameLogic gameLogicWithBoss = new GameLogic(paddle, mockBoss);
        assertNotNull(gameLogicWithBoss);
    }

    @Test
    void testGameLogicWithFinalBoss() {
        // This test would require FinalBoss mock
        // Just testing constructor exists
        assertNotNull(gameLogic);
    }

    @Test
    void testPaddleCollisionWithGameOverPaddle() {
        paddle.setState(7); // Game over state
        assertTrue(paddle.isGameOver());

        ball.setLaunched(true);
        float initialY = ball.getY();

        gameLogic.paddleCollision(ball);

        // Should not change ball position when paddle is game over
        assertEquals(initialY, ball.getY(), 0.01f);
    }
}


