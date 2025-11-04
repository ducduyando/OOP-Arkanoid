package io.github.arkanoid.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.arkanoid.GdxTestRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static io.github.arkanoid.core.Constants.*;

/**
 * Test class for Ball
 */
public class BallTest extends GdxTestRunner {

    @Mock
    private Texture mockTexture;

    private Ball ball;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ball = new Ball(100f, 200f);
    }

    @Test
    void testBallCreationWithPosition() {
        assertEquals(100f, ball.getX(), 0.01f);
        assertEquals(200f, ball.getY(), 0.01f);
        assertEquals(BALL_WIDTH, ball.getWidth(), 0.01f);
        assertEquals(BALL_HEIGHT, ball.getHeight(), 0.01f);
    }

    @Test
    void testBallCreationWithTexture() {
        Ball ballWithTexture = new Ball(mockTexture, 50f, 75f);
        assertEquals(50f, ballWithTexture.getX(), 0.01f);
        assertEquals(75f, ballWithTexture.getY(), 0.01f);
    }

    @Test
    void testInitialVelocity() {
        Vector2 velocity = ball.getVelocity();
        assertNotNull(velocity);
        assertEquals(BALL_VELOCITY.x, velocity.x, 0.01f);
        assertEquals(BALL_VELOCITY.y, velocity.y, 0.01f);
    }

    @Test
    void testSetVelocity() {
        ball.setVelocity(500f, 300f);
        Vector2 velocity = ball.getVelocity();
        assertEquals(500f, velocity.x, 0.01f);
        assertEquals(300f, velocity.y, 0.01f);
    }

    @Test
    void testResetVelocity() {
        ball.setVelocity(500f, 300f);
        ball.resetVelocity();
        Vector2 velocity = ball.getVelocity();
        assertEquals(0f, velocity.x, 0.01f);
        assertEquals(0f, velocity.y, 0.01f);
    }

    @Test
    void testHitBox() {
        Rectangle hitBox = ball.getHitBox();
        assertNotNull(hitBox);
        assertEquals(100f, hitBox.x, 0.01f);
        assertEquals(200f, hitBox.y, 0.01f);
        assertEquals(BALL_WIDTH, hitBox.width, 0.01f);
        assertEquals(BALL_HEIGHT, hitBox.height, 0.01f);
    }

    @Test
    void testSetHitBox() {
        ball.setHitBox(150f, 250f);
        Rectangle hitBox = ball.getHitBox();
        assertEquals(150f, hitBox.x, 0.01f);
        assertEquals(250f, hitBox.y, 0.01f);
    }



    @Test
    void testLaunchState() {
        assertFalse(ball.isLaunched());

        ball.setLaunched(true);
        assertTrue(ball.isLaunched());

        ball.setLaunched(false);
        assertFalse(ball.isLaunched());
    }

    @Test
    void testResetLaunch() {
        ball.setLaunched(true);
        ball.resetLaunch();
        assertFalse(ball.isLaunched());
    }

    @Test
    void testActWhenNotLaunched() {
        float initialX = ball.getX();
        float initialY = ball.getY();

        ball.setLaunched(false);
        ball.act(0.1f);

        assertEquals(initialX, ball.getX(), 0.01f);
        assertEquals(initialY, ball.getY(), 0.01f);
    }

    @Test
    void testActWhenLaunched() {
        ball.setLaunched(true);
        ball.setVelocity(100f, 200f);

        float initialX = ball.getX();
        float initialY = ball.getY();

        ball.act(0.1f);

        assertEquals(initialX + 10f, ball.getX(), 0.01f);
        assertEquals(initialY + 20f, ball.getY(), 0.01f);

        Rectangle hitBox = ball.getHitBox();
        assertEquals(ball.getX(), hitBox.x, 0.01f);
        assertEquals(ball.getY(), hitBox.y, 0.01f);
    }

    @Test
    void testHitBoxUpdatesWithPosition() {
        ball.setPosition(300f, 400f);
        Rectangle hitBox = ball.getHitBox();
        assertEquals(300f, hitBox.x, 0.01f);
        assertEquals(400f, hitBox.y, 0.01f);
    }
}


