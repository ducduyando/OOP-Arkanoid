package io.github.arkanoid.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GameManager
 */
public class GameManagerTest {

    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        // Reset singleton before each test
        gameManager = GameManager.getInstance();
        gameManager.reset();
    }

    @Test
    void testSingletonInstance() {
        GameManager instance1 = GameManager.getInstance();
        GameManager instance2 = GameManager.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2, "GameManager should be a singleton");
    }

    @Test
    void testInitialState() {
        assertEquals(0, gameManager.getCurrentStage());
        assertEquals(3, gameManager.getPlayerLives());
        assertEquals(0, gameManager.getScore());
        assertFalse(gameManager.isPaused());
        assertFalse(gameManager.isGameOver());
        assertEquals("Player", gameManager.getCurrentPlayerName());
        assertFalse(gameManager.hasSkill1A());
        assertFalse(gameManager.hasSkill2A());
        assertEquals(0, gameManager.getPowerUpSelection());
    }

    @Test
    void testReset() {
        // Modify state
        gameManager.setCurrentStage(2);
        gameManager.setPlayerLives(1);
        gameManager.addScore(1000);
        gameManager.setPaused(true);
        gameManager.setGameOver(true);
        gameManager.setCurrentPlayerName("TestPlayer");
        gameManager.setHasSkill1A(true);
        gameManager.setHasSkill2A(true);

        // Reset
        gameManager.reset();

        // Verify reset
        assertEquals(0, gameManager.getCurrentStage());
        assertEquals(3, gameManager.getPlayerLives());
        assertEquals(0, gameManager.getScore());
        assertFalse(gameManager.isPaused());
        assertFalse(gameManager.isGameOver());
        assertEquals("Player", gameManager.getCurrentPlayerName());
        assertFalse(gameManager.hasSkill1A());
        assertFalse(gameManager.hasSkill2A());
        assertEquals(0, gameManager.getPowerUpSelection());
    }

    @Test
    void testStageManagement() {
        gameManager.setCurrentStage(1);
        assertEquals(1, gameManager.getCurrentStage());

        gameManager.nextStage();
        assertEquals(2, gameManager.getCurrentStage());

        gameManager.nextStage();
        assertEquals(3, gameManager.getCurrentStage());
    }

    @Test
    void testLivesManagement() {
        gameManager.setPlayerLives(5);
        assertEquals(5, gameManager.getPlayerLives());

        gameManager.loseLife();
        assertEquals(4, gameManager.getPlayerLives());

        gameManager.loseLife();
        assertEquals(3, gameManager.getPlayerLives());
    }

    @Test
    void testLoseLifeGameOver() {
        gameManager.setPlayerLives(1);
        assertFalse(gameManager.isGameOver());

        gameManager.loseLife();
        assertEquals(0, gameManager.getPlayerLives());
        assertTrue(gameManager.isGameOver());
    }

    @Test
    void testLoseLifeBelowZero() {
        gameManager.setPlayerLives(0);
        gameManager.loseLife();
        assertEquals(0, gameManager.getPlayerLives());
    }

    @Test
    void testScoreManagement() {
        gameManager.addScore(100);
        assertEquals(100, gameManager.getScore());

        gameManager.addScore(250);
        assertEquals(350, gameManager.getScore());

        gameManager.setScore(500);
        assertEquals(500, gameManager.getScore());
    }

    @Test
    void testPauseState() {
        assertFalse(gameManager.isPaused());

        gameManager.setPaused(true);
        assertTrue(gameManager.isPaused());

        gameManager.setPaused(false);
        assertFalse(gameManager.isPaused());
    }

    @Test
    void testGameOverState() {
        assertFalse(gameManager.isGameOver());

        gameManager.setGameOver(true);
        assertTrue(gameManager.isGameOver());

        gameManager.setGameOver(false);
        assertFalse(gameManager.isGameOver());
    }

    @Test
    void testPlayerName() {
        gameManager.setCurrentPlayerName("TestPlayer");
        assertEquals("TestPlayer", gameManager.getCurrentPlayerName());

        // Test empty name handling
        gameManager.setCurrentPlayerName("");
        assertEquals("Player", gameManager.getCurrentPlayerName());

        // Test null name handling
        gameManager.setCurrentPlayerName(null);
        assertEquals("Player", gameManager.getCurrentPlayerName());
    }

    @Test
    void testSkillStates() {
        assertFalse(gameManager.hasSkill1A());
        assertFalse(gameManager.hasSkill2A());

        gameManager.setHasSkill1A(true);
        assertTrue(gameManager.hasSkill1A());

        gameManager.setHasSkill2A(true);
        assertTrue(gameManager.hasSkill2A());
    }

    @Test
    void testPowerUpSelection() {
        gameManager.setPowerUpSelection(1);
        assertEquals(1, gameManager.getPowerUpSelection());

        gameManager.setPowerUpSelection(2);
        assertEquals(2, gameManager.getPowerUpSelection());
    }

    @Test
    void testCanContinue() {
        assertTrue(gameManager.canContinue());

        gameManager.setGameOver(true);
        assertFalse(gameManager.canContinue());

        gameManager.setGameOver(false);
        gameManager.setPlayerLives(0);
        assertFalse(gameManager.canContinue());

        gameManager.setPlayerLives(1);
        assertTrue(gameManager.canContinue());
    }
}

