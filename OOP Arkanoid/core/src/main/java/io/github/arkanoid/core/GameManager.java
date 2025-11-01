package io.github.arkanoid.core;

/**
 * GameManager - Singleton Pattern
 * Quản lý trạng thái toàn cục của game
 */
public class GameManager {
    // Singleton instance
    private static GameManager instance;

    // Game state variables
    private int currentStage = 0;
    private int playerLives = 3;
    private int score = 0;
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private String currentPlayerName = "Player";

    // Power-up states
    private boolean hasSkill1A = false;
    private boolean hasSkill2A = false;
    private int powerUpSelection = 0;

    // Private constructor để ngăn tạo instance từ bên ngoài
    private GameManager() {
        // Initialize default values
        reset();
    }

    /**
     * Lấy instance duy nhất của GameManager (Singleton)
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Reset game về trạng thái ban đầu
     */
    public void reset() {
        currentStage = 0;
        playerLives = 3;
        score = 0;
        isPaused = false;
        isGameOver = false;
        currentPlayerName = "Player";
        hasSkill1A = false;
        hasSkill2A = false;
        powerUpSelection = 0;

    }

    // Getters and Setters
    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int stage) {
        this.currentStage = stage;
    }

    public void nextStage() {
        currentStage++;
    }

    public int getPlayerLives() {
        return playerLives;
    }

    public void setPlayerLives(int lives) {
        this.playerLives = lives;
    }

    public void loseLife() {
        if (playerLives > 0) {
            playerLives--;
            if (playerLives <= 0) {
                isGameOver = true;
            }
        }
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;

    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void setCurrentPlayerName(String name) {
        this.currentPlayerName = name != null && !name.trim().isEmpty() ? name : "Player";
    }

    public boolean hasSkill1A() {
        return hasSkill1A;
    }

    public void setHasSkill1A(boolean hasSkill1A) {
        this.hasSkill1A = hasSkill1A;
    }

    public boolean hasSkill2A() {
        return hasSkill2A;
    }

    public void setHasSkill2A(boolean hasSkill2A) {
        this.hasSkill2A = hasSkill2A;
    }

    public int getPowerUpSelection() {
        return powerUpSelection;
    }

    public void setPowerUpSelection(int selection) {
        this.powerUpSelection = selection;
    }

    /**
     * Kiểm tra xem có thể tiếp tục game không
     */
    public boolean canContinue() {
        return !isGameOver && playerLives > 0;
    }


}
