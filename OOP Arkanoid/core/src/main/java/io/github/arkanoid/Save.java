package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

import static io.github.arkanoid.Constants.*;
public class Save {
    private static final String SAVE_FILE = "game_save";
    private static final Preferences pref = Gdx.app.getPreferences(SAVE_FILE);

    public static void saveGame(int stageNumber, int bossHP, int barHP, int bricksRemaining) {
        pref.putInteger("stageNumber", stageNumber);
        pref.putInteger("bossHP", bossHP);
        pref.putInteger("barHP", barHP);
        pref.putInteger("bricksRemaining", bricksRemaining);
        pref.flush();
        Gdx.app.log("SAVE", "Saved bricks remaining = " + bricksRemaining);
    }

    public static void saveGameWithBrickPositions(int stageNumber, int bossHP, int barHP, List<BrickPosition> brickPositions) {
        pref.putInteger("stageNumber", stageNumber);
        pref.putInteger("bossHP", bossHP);
        pref.putInteger("barHP", barHP);
        pref.putInteger("bricksRemaining", brickPositions.size());

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
        data.barHP = pref.getInteger("barHP", 3);
        data.bricksRemaining = pref.getInteger("bricksRemaining", 0);

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
        public int barHP;
        public int bricksRemaining;
        public List<BrickPosition> brickPositions;
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
