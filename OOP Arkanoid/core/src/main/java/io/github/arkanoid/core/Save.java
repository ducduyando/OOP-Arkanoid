package io.github.arkanoid.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.List;

import static io.github.arkanoid.core.Constants.*;
public class Save {
    private static final String SAVE_FILE = "game_save";
    private static final Preferences pref = Gdx.app.getPreferences(SAVE_FILE);

    public static void saveGame(int stageNumber, int bossHP, int paddleState, int bricksRemaining,
                                float paddleX, float paddleY, float ballX, float ballY,
                                float ballVelX, float ballVelY, boolean ballLaunched,
                                float bossX, float bossY, boolean isSkillASelected , float skill1ACooldownTimer,
                                float skill1BCooldownTimer) {
        pref.putInteger("stageNumber", stageNumber);
        pref.putInteger("bossHP", bossHP);
        pref.putInteger("paddleState", paddleState);
        pref.putInteger("bricksRemaining", bricksRemaining);

        pref.putFloat("paddleX", paddleX);
        pref.putFloat("paddleY", paddleY);
        pref.putFloat("ballX", ballX);
        pref.putFloat("ballY", ballY);
        pref.putFloat("ballVelX", ballVelX);
        pref.putFloat("ballVelY", ballVelY);
        pref.putBoolean("ballLaunched", ballLaunched);
        pref.putFloat("bossX", bossX);
        pref.putFloat("bossY", bossY);

        pref.putBoolean("isSkillASelected", isSkillASelected);
        pref.putFloat("skill1ACooldownTimer", skill1ACooldownTimer);
        pref.putFloat("skill1BCooldownTimer", skill1BCooldownTimer);
        pref.flush();
    }

    public static void saveGameWithBrickPositions(int stageNumber, int bossHP, int paddleState, List<BrickPosition> brickPositions,
                                                  float paddleX, float paddleY, float ballX, float ballY,
                                                  float ballVelX, float ballVelY, boolean ballLaunched,
                                                  float bossX, float bossY) {
        pref.putInteger("stageNumber", stageNumber);
        pref.putInteger("bossHP", bossHP);
        pref.putInteger("paddleState", paddleState);
        pref.putInteger("bricksRemaining", brickPositions.size());

        // ADDED: Save positions and ball state
        pref.putFloat("paddleX", paddleX);
        pref.putFloat("paddleY", paddleY);
        pref.putFloat("ballX", ballX);
        pref.putFloat("ballY", ballY);
        pref.putFloat("ballVelX", ballVelX);
        pref.putFloat("ballVelY", ballVelY);
        pref.putBoolean("ballLaunched", ballLaunched);
        pref.putFloat("bossX", bossX);
        pref.putFloat("bossY", bossY);

        // Lưu vị trí từng viên gạch
        for (int i = 0; i < brickPositions.size(); i++) {
            BrickPosition pos = brickPositions.get(i);
            pref.putFloat("brick_" + i + "_x", pos.x);
            pref.putFloat("brick_" + i + "_y", pos.y);
            pref.putInteger("brick_" + i + "_textureIndex", pos.textureIndex);
        }

        pref.flush();
        Gdx.app.log("SAVE", "Saved " + brickPositions.size() + " bricks with positions");
    }

    public static void saveGameWithBees(int stageNumber, int bossHP, int paddleState, List<BeePosition> beePositions,
                                        float paddleX, float paddleY, float ballX, float ballY,
                                        float ballVelX, float ballVelY, boolean ballLaunched,
                                        float bossX, float bossY
                                        ) {
        pref.putInteger("stageNumber", stageNumber);
        pref.putInteger("bossHP", bossHP);
        pref.putInteger("paddleState", paddleState);
        pref.putInteger("bricksRemaining", 0);
        pref.putInteger("beesCount", beePositions.size());
        pref.putFloat("paddleX", paddleX);
        pref.putFloat("paddleY", paddleY);
        pref.putFloat("ballX", ballX);
        pref.putFloat("ballY", ballY);
        pref.putFloat("ballVelX", ballVelX);
        pref.putFloat("ballVelY", ballVelY);
        pref.putBoolean("ballLaunched", ballLaunched);
        pref.putFloat("bossX", bossX);
        pref.putFloat("bossY", bossY);

        for (int i = 0; i < beePositions.size(); i++) {
            BeePosition pos = beePositions.get(i);
            pref.putFloat("bee_" + i + "_x", pos.x);
            pref.putFloat("bee_" + i + "_y", pos.y);
        }

        pref.flush();
    }

    public static void saveGameWithProjectiles(int stageNumber, int bossHP, int paddleState,
                                               ProjectileSaveManager.ProjectileData projectileData,
                                               float paddleX, float paddleY, float ballX, float ballY,
                                               float ballVelX, float ballVelY, boolean ballLaunched,
                                               float bossX, float bossY, boolean isSkillASelected,
                                               float skill1ACooldownTimer,
                                               float skill1BCooldownTimer) {
        pref.putInteger("stageNumber", stageNumber);
        pref.putInteger("bossHP", bossHP);
        pref.putInteger("paddleState", paddleState);
        pref.putInteger("bricksRemaining", 0); // No bricks in boss stages

        // Save positions and ball state
        pref.putFloat("paddleX", paddleX);
        pref.putFloat("paddleY", paddleY);
        pref.putFloat("ballX", ballX);
        pref.putFloat("ballY", ballY);
        pref.putFloat("ballVelX", ballVelX);
        pref.putFloat("ballVelY", ballVelY);
        pref.putBoolean("ballLaunched", ballLaunched);
        pref.putFloat("bossX", bossX);
        pref.putFloat("bossY", bossY);

        pref.putBoolean("isSkillASelected", isSkillASelected);
        pref.putFloat("skill1ACooldownTimer", skill1ACooldownTimer);
        pref.putFloat("skill1BCooldownTimer", skill1BCooldownTimer);
        // Save projectile data
        saveProjectileData(projectileData);

        pref.flush();

    }

    private static void saveProjectileData(ProjectileSaveManager.ProjectileData data) {
        // Save bees
        pref.putInteger("beesCount", data.bees.size());
        for (int i = 0; i < data.bees.size(); i++) {
            ProjectileSaveManager.BeeData bee = data.bees.get(i);
            pref.putFloat("bee_" + i + "_x", bee.x);
            pref.putFloat("bee_" + i + "_y", bee.y);
        }

        // Save bombs
        pref.putInteger("bombsCount", data.bombs.size());
        for (int i = 0; i < data.bombs.size(); i++) {
            ProjectileSaveManager.BombData bomb = data.bombs.get(i);
            pref.putFloat("bomb_" + i + "_x", bomb.x);
            pref.putFloat("bomb_" + i + "_y", bomb.y);
            pref.putFloat("bomb_" + i + "_stateTime", bomb.stateTime);
        }

        // Save lasers
        pref.putInteger("lasersCount", data.lasers.size());
        for (int i = 0; i < data.lasers.size(); i++) {
            ProjectileSaveManager.LaserData laser = data.lasers.get(i);
            pref.putFloat("laser_" + i + "_x", laser.x);
            pref.putFloat("laser_" + i + "_y", laser.y);
            pref.putFloat("laser_" + i + "_stateTime", laser.stateTime);
        }
    }

    public static boolean hasSave() {
        return pref.contains("stageNumber");
    }

    public static SaveData loadGame() {
        if(!hasSave()) {
            return null;
        }

        SaveData data = new SaveData();
        data.stageNumber = pref.getInteger("stageNumber", 0);
        data.bossHP = pref.getInteger("bossHP", 100);
        data.paddleState = pref.getInteger("paddleState ", 3);
        data.bricksRemaining = pref.getInteger("bricksRemaining", 0);

        data.paddleX = pref.getFloat("paddleX");
        data.paddleY = pref.getFloat("paddleY");
        data.ballX = pref.getFloat("ballX");
        data.ballY = pref.getFloat("ballY");
        data.ballVelX = pref.getFloat("ballVelX");
        data.ballVelY = pref.getFloat("ballVelY");
        data.ballLaunched = pref.getBoolean("ballLaunched", false);

        data.bossX = pref.getFloat("bossX", (SCREEN_WIDTH - BOSS1_WIDTH) / 2f);
        data.bossY = pref.getFloat("bossY", SCREEN_HEIGHT * 0.6f);

        data.isSkill1ASelected = pref.getBoolean("isSkill1ASelected", false);
        data.skill1ACooldownTimer = pref.getFloat("skill1ACooldownTimer", Constants.PADDLE_SKILL_COOLDOWN);
        data.skill1BCooldownTimer = pref.getFloat("skill1BCooldownTimer", Constants.PADDLE_SKILL_COOLDOWN);

        // Load vị trí gạch nếu có
        data.brickPositions = new ArrayList<>();
        for (int i = 0; i < data.bricksRemaining; i++) {
            if (pref.contains("brick_" + i + "_x")) {
                BrickPosition pos = new BrickPosition();
                pos.x = pref.getFloat("brick_" + i + "_x");
                pos.y = pref.getFloat("brick_" + i + "_y");
                pos.textureIndex = pref.getInteger("brick_" + i + "_textureIndex", 0);
                data.brickPositions.add(pos);
            }
        }
        data.beePositions = new ArrayList<>();
        int beesCount = pref.getInteger("beesCount", 0);
        for (int i = 0; i < beesCount; i++) {
            if (pref.contains("bee_" + i + "_x")) {
                BeePosition pos = new BeePosition();
                pos.x = pref.getFloat("bee_" + i + "_x");
                pos.y = pref.getFloat("bee_" + i + "_y");
                data.beePositions.add(pos);
            }
        }

        data.projectileData = loadProjectileData();

        return data;
    }

    private static ProjectileSaveManager.ProjectileData loadProjectileData() {
        ProjectileSaveManager.ProjectileData data = new ProjectileSaveManager.ProjectileData();

        // Load bees
        int beesCount = pref.getInteger("beesCount", 0);
        for (int i = 0; i < beesCount; i++) {
            if (pref.contains("bee_" + i + "_x")) {
                float x = pref.getFloat("bee_" + i + "_x");
                float y = pref.getFloat("bee_" + i + "_y");
                data.bees.add(new ProjectileSaveManager.BeeData(x, y));
            }
        }

        // Load bombs
        int bombsCount = pref.getInteger("bombsCount", 0);
        for (int i = 0; i < bombsCount; i++) {
            if (pref.contains("bomb_" + i + "_x")) {
                float x = pref.getFloat("bomb_" + i + "_x");
                float y = pref.getFloat("bomb_" + i + "_y");
                float stateTime = pref.getFloat("bomb_" + i + "_stateTime", 0f);
                data.bombs.add(new ProjectileSaveManager.BombData(x, y, stateTime));
            }
        }

        // Load lasers
        int lasersCount = pref.getInteger("lasersCount", 0);
        for (int i = 0; i < lasersCount; i++) {
            if (pref.contains("laser_" + i + "_x")) {
                float x = pref.getFloat("laser_" + i + "_x");
                float y = pref.getFloat("laser_" + i + "_y");
                float stateTime = pref.getFloat("laser_" + i + "_stateTime", 0f);
                data.lasers.add(new ProjectileSaveManager.LaserData(x, y, stateTime));
            }
        }

        return data;
    }

    public static void deleteSave() {
        pref.clear();
        pref.flush();
    }

    public static class SaveData {
        public int stageNumber;
        public int bossHP;
        public int paddleState;
        public int bricksRemaining;
        public List<BrickPosition> brickPositions;
        public List<BeePosition> beePositions; // Legacy - will be replaced by projectileData
        public ProjectileSaveManager.ProjectileData projectileData;

        public float paddleX, paddleY;
        public float ballX, ballY;
        public float ballVelX, ballVelY;
        public boolean ballLaunched;
        public float bossX, bossY;

        public boolean isSkill1ASelected;
        public float skill1ACooldownTimer;
        public float skill1BCooldownTimer;
        public boolean hasSkill2A;
        public float skill2ACooldownTimer;
    }

    public static class BrickPosition {
        public float x;
        public float y;
        public int textureIndex;

        public BrickPosition() {}

        public BrickPosition(float x, float y, int textureIndex) {
            this.x = x;
            this.y = y;
            this.textureIndex = textureIndex;
        }
    }

    public static class BeePosition {
        public float x;
        public float y;

        public BeePosition() {}

        public BeePosition(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
