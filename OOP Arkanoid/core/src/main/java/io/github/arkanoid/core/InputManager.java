package io.github.arkanoid.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.HashMap;
import java.util.Map;

import static io.github.arkanoid.core.Constants.*;


/**
 * InputManager - Singleton Pattern
 * Quản lý input và key bindings của game
 */
public class InputManager {
    // Singleton instance
    private static InputManager instance;

    // Key bindings
    private Map<String, Integer> keyBindings;

    // Key states for preventing multiple triggers
    private Map<Integer, Boolean> keyPressed;
    private Map<Integer, Boolean> keyJustPressed;

    // Private constructor
    private InputManager() {
        keyBindings = new HashMap<>();
        keyPressed = new HashMap<>();
        keyJustPressed = new HashMap<>();

        // Set default key bindings
        setDefaultKeyBindings();
    }

    /**
     * Lấy instance duy nhất của InputManager (Singleton)
     */
    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    /**
     * Set default key bindings
     */
    private void setDefaultKeyBindings() {
        keyBindings.put(ACTION_MOVE_LEFT, Input.Keys.A);
        keyBindings.put(ACTION_MOVE_RIGHT, Input.Keys.D);
        keyBindings.put(ACTION_SKILL_1, Input.Keys.J);
        keyBindings.put(ACTION_SKILL_2, Input.Keys.K);
        keyBindings.put(ACTION_PAUSE, Input.Keys.P);
        keyBindings.put(ACTION_CONFIRM, Input.Keys.ENTER);
        keyBindings.put(ACTION_CANCEL, Input.Keys.ESCAPE);
    }

    /**
     * Update input states - call this every frame
     */
    public void update() {
        // Update key states
        for (Integer keyCode : keyBindings.values()) {
            boolean currentlyPressed = Gdx.input.isKeyPressed(keyCode);
            boolean wasPressed = keyPressed.getOrDefault(keyCode, false);

            keyJustPressed.put(keyCode, currentlyPressed && !wasPressed);
            keyPressed.put(keyCode, currentlyPressed);
        }
    }

    /**
     * Check if action is currently pressed
     */
    public boolean isActionPressed(String action) {
        Integer keyCode = keyBindings.get(action);
        if (keyCode == null) {
            System.err.println("InputManager: Action '" + action + "' not found");
            return false;
        }
        return keyPressed.getOrDefault(keyCode, false);
    }

    /**
     * Check if action was just pressed (single trigger)
     */
    public boolean isActionJustPressed(String action) {
        Integer keyCode = keyBindings.get(action);
        if (keyCode == null) {
            System.err.println("InputManager: Action '" + action + "' not found");
            return false;
        }
        return keyJustPressed.getOrDefault(keyCode, false);
    }

    /**
     * Direct LibGDX key check (for backward compatibility)
     */
    public boolean isKeyPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }

    /**
     * Direct LibGDX key just pressed check
     */
    public boolean isKeyJustPressed(int keyCode) {
        return Gdx.input.isKeyJustPressed(keyCode);
    }

    /**
     * Set custom key binding
     */
    public void setKeyBinding(String action, int keyCode) {
        keyBindings.put(action, keyCode);
        System.out.println("InputManager: Set key binding '" + action + "' to key " + keyCode);
    }

    /**
     * Get key code for action
     */
    public Integer getKeyBinding(String action) {
        return keyBindings.get(action);
    }

    /**
     * Reset to default key bindings
     */
    public void resetToDefaults() {
        keyBindings.clear();
        setDefaultKeyBindings();
        System.out.println("InputManager: Reset to default key bindings");
    }

    /**
     * Get all key bindings
     */
    public Map<String, Integer> getAllKeyBindings() {
        return new HashMap<>(keyBindings);
    }

    /**
     * Check for movement input (returns -1 for left, 1 for right, 0 for none)
     */
    public float getMovementInput() {
        float movement = 0f;

        if (isActionPressed(ACTION_MOVE_LEFT)) {
            movement -= 1f;
        }
        if (isActionPressed(ACTION_MOVE_RIGHT)) {
            movement += 1f;
        }

        return movement;
    }

    /**
     * Check if any key is pressed
     */
    public boolean isAnyKeyPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ANY_KEY);
    }

    /**
     * Get key name from key code
     */
    public String getKeyName(int keyCode) {
        return Input.Keys.toString(keyCode);
    }

    /**
     * Log current key bindings
     */
    public void logKeyBindings() {

        for (Map.Entry<String, Integer> entry : keyBindings.entrySet()) {
            String action = entry.getKey();
            int keyCode = entry.getValue();
            String keyName = getKeyName(keyCode);
        }

    }

}
