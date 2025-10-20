package io.github.arkanoid.universal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.List;

import static io.github.arkanoid.universal.Constants.*;
public class Save {
    private static final String SAVE_FILE = "game_save";
    private static final Preferences pref = Gdx.app.getPreferences(SAVE_FILE);

    public static void saveGame(int stageNumber, int bossHP, int paddleState, int bricksRemaining,
                                float paddleX, float paddleY, float ballX, float ballY,
                                float ballVelX, float ballVelY, boolean ballLaunched,
                                float bossX, float bossY) {
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

        pref.flush();
        Gdx.app.log("SAVE", "Saved bricks remaining = " + bricksRemaining);
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
        data.paddleState = pref.getInteger("paddleState", 3);
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

        public float paddleX, paddleY;
        public float ballX, ballY;
        public float ballVelX, ballVelY;
        public boolean ballLaunched;
        public float bossX, bossY;
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
}
