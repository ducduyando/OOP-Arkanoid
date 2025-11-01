package io.github.arkanoid.core;

import io.github.arkanoid.stage.GameStage;
import java.util.Stack;

/**
 * SceneManager - Singleton Pattern
 * Quản lý chuyển đổi giữa các stage/scene
 */
public class SceneManager {
    // Singleton instance
    private static SceneManager instance;

    // Stage management
    private GameStage currentStage;
    private GameStage nextStage;
    private Stack<GameStage> stageHistory;

    // Transition state
    private boolean isTransitioning = false;
    private float transitionTime = 0f;
    private float transitionDuration = 1f;

    // Stage types enum for easier management
    public enum StageType {
        MENU,
        TUTORIAL,
        BOSS1,
        BOSS2,
        BOSS3,
        POWER_UP_MENU,
        RANK,
        NAME_INPUT,
        GAME_LOSE
    }

    // Private constructor
    private SceneManager() {
        stageHistory = new Stack<>();

    }

    /**
     * Lấy instance duy nhất của SceneManager (Singleton)
     */
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Set current stage
     */
    public void setCurrentStage(GameStage stage) {
        if (currentStage != null) {
            stageHistory.push(currentStage);
            currentStage.exit();
        }

        currentStage = stage;
        if (currentStage != null) {
            currentStage.enter();

        }
    }

    /**
     * Transition to new stage
     */
    public void transitionTo(GameStage newStage) {


        nextStage = newStage;
        isTransitioning = true;
        transitionTime = 0f;

    }

    /**
     * Go back to previous stage
     */
    public void goBack() {


        GameStage previousStage = stageHistory.pop();
        transitionTo(previousStage);
    }

    /**
     * Update scene manager
     */
    public void update(float delta) {
        // Handle transition
        if (isTransitioning) {
            transitionTime += delta;

            if (transitionTime >= transitionDuration) {
                // Complete transition
                completeTransition();
            }
        }

        // Update current stage
        if (currentStage != null && !isTransitioning) {
            currentStage.update(delta);

        }
    }

    /**
     * Complete transition to next stage
     */
    private void completeTransition() {
        if (currentStage != null) {
            stageHistory.push(currentStage);
            currentStage.exit();
        }

        currentStage = nextStage;
        nextStage = null;
        isTransitioning = false;
        transitionTime = 0f;

        if (currentStage != null) {
            currentStage.enter();

        }
    }



    /**
     * Render current stage
     */
    public void render() {
        if (currentStage != null) {
            // Handle rendering based on stage type
            if (currentStage instanceof io.github.arkanoid.ui.NameInputStage) {
                ((io.github.arkanoid.ui.NameInputStage) currentStage).draw();
            } else if (currentStage instanceof io.github.arkanoid.stage.RankStage) {
                ((io.github.arkanoid.stage.RankStage) currentStage).draw();
            }
            // Add more stage types as needed
        }
    }

    /**
     * Clear stage history
     */
    public void clearHistory() {
        // Dispose all stages in history
        while (!stageHistory.isEmpty()) {
            GameStage stage = stageHistory.pop();
            stage.exit();
        }
    }

    /**
     * Get current stage
     */
    public GameStage getCurrentStage() {
        return currentStage;
    }

    /**
     * Check if transitioning
     */
    public boolean isTransitioning() {
        return isTransitioning;
    }

    /**
     * Get transition progress (0.0 to 1.0)
     */
    public float getTransitionProgress() {
        if (!isTransitioning) return 0f;
        return Math.min(transitionTime / transitionDuration, 1f);
    }

    /**
     * Set transition duration
     */
    public void setTransitionDuration(float duration) {
        this.transitionDuration = Math.max(0.1f, duration);
    }

    /**
     * Get stage history size
     */
    public int getHistorySize() {
        return stageHistory.size();
    }

    /**
     * Check if can go back
     */
    public boolean canGoBack() {
        return !stageHistory.isEmpty();
    }

    /**
     * Force exit current stage
     */
    public void exitCurrentStage() {
        if (currentStage != null) {
            currentStage.exit();
            currentStage = null;
        }
    }



    /**
     * Dispose scene manager
     */
    public void dispose() {
        // Exit current stage
        if (currentStage != null) {
            currentStage.exit();
            currentStage = null;
        }

        // Exit next stage if exists
        if (nextStage != null) {
            nextStage.exit();
            nextStage = null;
        }

        // Clear history
        clearHistory();

        isTransitioning = false;
        transitionTime = 0f;

    }
}
