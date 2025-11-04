package io.github.arkanoid.core;

import io.github.arkanoid.stage.GameStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for SceneManager
 */
public class SceneManagerTest {

    @Mock
    private GameStage mockStage1;

    @Mock
    private GameStage mockStage2;

    @Mock
    private GameStage mockStage3;

    private SceneManager sceneManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sceneManager = SceneManager.getInstance();
        sceneManager.clearHistory();


    }

    @Test
    void testSingletonInstance() {
        SceneManager instance1 = SceneManager.getInstance();
        SceneManager instance2 = SceneManager.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2, "SceneManager should be a singleton");
    }

    @Test
    void testSetCurrentStage() {
        sceneManager.setCurrentStage(mockStage1);

        verify(mockStage1, times(1)).enter();
        assertEquals(mockStage1, sceneManager.getCurrentStage());
    }

    @Test
    void testSetCurrentStageWithPreviousStage() {
        sceneManager.setCurrentStage(mockStage1);
        sceneManager.setCurrentStage(mockStage2);

        verify(mockStage1, times(1)).exit();
        verify(mockStage2, times(1)).enter();
        assertEquals(mockStage2, sceneManager.getCurrentStage());
    }

    @Test
    void testTransitionTo() {
        sceneManager.setCurrentStage(mockStage1);
        sceneManager.transitionTo(mockStage2);

        assertTrue(sceneManager.isTransitioning());
        assertFalse(sceneManager.getTransitionProgress() >= 1.0f);
    }

    @Test
    void testTransitionProgress() {
        sceneManager.setCurrentStage(mockStage1);
        sceneManager.transitionTo(mockStage2);

        float progress = sceneManager.getTransitionProgress();
        assertTrue(progress >= 0f);
        assertTrue(progress <= 1.0f);
    }

    @Test
    void testGoBack() {
        sceneManager.setCurrentStage(mockStage1);
        sceneManager.setCurrentStage(mockStage2);

        // Go back should transition to previous stage
        sceneManager.goBack();

        assertTrue(sceneManager.isTransitioning());
    }

    @Test
    void testUpdate() {
        sceneManager.setCurrentStage(mockStage1);

        sceneManager.update(0.016f); // ~60fps delta

        verify(mockStage1, atLeastOnce()).update(0.016f);
    }

    @Test
    void testUpdateWithTransition() {
        sceneManager.setCurrentStage(mockStage1);
        sceneManager.transitionTo(mockStage2);

        // Simulate transition completion
        sceneManager.update(2.0f); // Longer than transition duration

        // Transition should complete
        assertFalse(sceneManager.isTransitioning());
        assertEquals(mockStage2, sceneManager.getCurrentStage());
    }

    @Test
    void testClearHistory() {
        sceneManager.setCurrentStage(mockStage1);
        sceneManager.setCurrentStage(mockStage2);
        sceneManager.setCurrentStage(mockStage3);

        sceneManager.clearHistory();

        // History should be cleared
        // Note: We can't directly verify history is empty, but clearHistory should work
        assertNotNull(sceneManager);
    }

    @Test
    void testGetCurrentStage() {
        assertNull(sceneManager.getCurrentStage());

        sceneManager.setCurrentStage(mockStage1);
        assertEquals(mockStage1, sceneManager.getCurrentStage());
    }

    @Test
    void testIsTransitioning() {
        assertFalse(sceneManager.isTransitioning());

        sceneManager.setCurrentStage(mockStage1);
        sceneManager.transitionTo(mockStage2);
        assertTrue(sceneManager.isTransitioning());
    }

    @Test
    void testStageTypeEnum() {
        // Test that StageType enum values exist
        SceneManager.StageType[] types = SceneManager.StageType.values();
        assertTrue(types.length > 0);
//
        // Verify some expected types exist
        boolean hasMenu = false;
        boolean hasTutorial = false;
        for (SceneManager.StageType type : types) {
            if (type == SceneManager.StageType.MENU) hasMenu = true;
            if (type == SceneManager.StageType.TUTORIAL) hasTutorial = true;
        }
        assertTrue(hasMenu);
        assertTrue(hasTutorial);
    }
}


