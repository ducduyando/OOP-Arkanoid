package io.github.arkanoid.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * ResourceManager - Singleton Pattern
 * Quản lý tài nguyên game (textures, fonts, etc.)
 */
public class ResourceManager {
    // Singleton instance
    private static ResourceManager instance;

    // Resource storage
    private Map<String, Texture> textures;
    private Map<String, BitmapFont> fonts;
    private Map<String, Texture[]> textureArrays;

    // Font generator for creating fonts
    private FreeTypeFontGenerator fontGenerator;

    // Private constructor
    private ResourceManager() {
        textures = new HashMap<>();
        fonts = new HashMap<>();
        textureArrays = new HashMap<>();


    }

    /**
     * Lấy instance duy nhất của ResourceManager (Singleton)
     */
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    /**
     * Load texture
     */
    public Texture loadTexture(String key, String filePath) {
        if (textures.containsKey(key)) {
            return textures.get(key);
        }

        try {
            Texture texture = new Texture(filePath);
            textures.put(key, texture);
            return texture;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get texture
     */
    public Texture getTexture(String key) {
        Texture texture = textures.get(key);
        if (texture == null) {
            System.err.println("ResourceManager: Texture '" + key + "' not found");
        }
        return texture;
    }

    /**
     * Load texture array (for animations)
     */
    public Texture[] loadTextureArray(String key, String basePath, int count) {
        if (textureArrays.containsKey(key)) {
            return textureArrays.get(key);
        }

        try {
            Texture[] textures = new Texture[count];
            for (int i = 0; i < count; i++) {
                String filePath = basePath + i + ".png";
                textures[i] = new Texture(filePath);
            }
            textureArrays.put(key, textures);
            return textures;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get texture array
     */
    public Texture[] getTextureArray(String key) {
        Texture[] textureArray = textureArrays.get(key);

        return textureArray;
    }

    /**
     * Create font with specified parameters
     */
    public BitmapFont createFont(String key, int size, Color color, boolean border) {
        if (fonts.containsKey(key)) {

            return fonts.get(key);
        }

        if (fontGenerator == null) {

            return null;
        }

        try {
            FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            parameter.color = color;
            parameter.minFilter = Texture.TextureFilter.Linear;
            parameter.magFilter = Texture.TextureFilter.Linear;
            parameter.flip = false;

            if (border) {
                parameter.borderWidth = 1;
                parameter.borderColor = Color.BLACK;
            }

            BitmapFont font = fontGenerator.generateFont(parameter);
            fonts.put(key, font);
            return font;
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * Get font
     */
    public BitmapFont getFont(String key) {
        BitmapFont font = fonts.get(key);

        return font;
    }

    /**
     * Preload common game resources
     */
    public void preloadGameResources() {

        // Load common textures
        loadTexture("paddle", "Universal/" + "paddle" + ".png");
        loadTexture("ball_normal", "Ball/" + "normal" + ".png");
        loadTexture("health_bar", "Universal/" + "health_bar" + ".png");

        // Load background layers for Stage1
        loadTextureArray("bg_stage1", "Background/Stage1/layer", 5);

        // Load background layers for Stage2
        loadTextureArray("bg_stage2", "Background/Stage2/layer", 6);

        // Load brick textures
        loadTexture("brick_red", "Brick/" + "red" + ".png");
        loadTexture("brick_blue", "Brick/" + "blue" + ".png");
        loadTexture("brick_green", "Brick/" + "green" + ".png");
        loadTexture("brick_orange", "Brick/" + "orange" + ".png");
        loadTexture("brick_background", "Brick/" + "background" + ".png");

        // Load UI textures
        loadTexture("rank_background", "Rank/" + "rank" + ".png");
        loadTexture("name_background", "Rank/" + "name" + ".png");

        // Create common fonts
        createFont("default_font", 40, Color.WHITE, false);
        createFont("large_font", 100, Color.WHITE, false);
        createFont("small_font", 20, Color.WHITE, false);


    }

    /**
     * Preload boss resources
     */
    public void preloadBossResources() {

        loadTexture("boss1", "Boss1/" + "boss1" + ".png");
        loadTexture("boss1_skill1", "Boss1/" + "skill1" + ".png");
        loadTexture("boss1_skill2", "Boss1/" + "skill2" + ".png");

        // Boss2 textures
        loadTexture("boss2", "Boss2/" + "boss2" + ".png");
        loadTexture("boss2_skill1", "Boss2/" + "skill1" +".png");
        loadTexture("boss2_skill2", "Boss2/" + "skill2" + ".png");

        // Boss3 textures
        loadTexture("boss3", "Boss3/" + "boss3" + ".png");
        loadTexture("boss3_left_hand", "Boss3/" + "left_hand" + ".png");
        loadTexture("boss3_right_hand", "Boss3/" + "right_hand" + ".png");


    }

    /**
     * Check if resource exists
     */
    public boolean hasTexture(String key) {
        return textures.containsKey(key);
    }

    public boolean hasFont(String key) {
        return fonts.containsKey(key);
    }

    public boolean hasTextureArray(String key) {
        return textureArrays.containsKey(key);
    }

    /**
     * Get resource counts
     */
    public int getTextureCount() {
        return textures.size();
    }

    public int getFontCount() {
        return fonts.size();
    }

    public int getTextureArrayCount() {
        return textureArrays.size();
    }



    /**
     * Dispose all resources
     */
    public void dispose() {
        // Dispose all textures
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();

        // Dispose all fonts
        for (BitmapFont font : fonts.values()) {
            font.dispose();
        }
        fonts.clear();

        // Dispose all texture arrays
        for (Texture[] textureArray : textureArrays.values()) {
            for (Texture texture : textureArray) {
                texture.dispose();
            }
        }
        textureArrays.clear();

        // Dispose font generator
        if (fontGenerator != null) {
            fontGenerator.dispose();
            fontGenerator = null;
        }


    }
}
