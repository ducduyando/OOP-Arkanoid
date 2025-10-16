package io.github.arkanoid;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Constants {
    /** Screen. */
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;
    public static final int PLAY_BUTTON_WIDTH = 1328;
    public static final int PLAY_BUTTON_HEIGHT = 272;
    public static final int PAUSE_BUTTON_WIDTH = 1488;
    public static final int PAUSE_BUTTON_HEIGHT = 272;
    public static final int HP_WIDTH = 1920;
    public static final int HP_HEIGHT = 144;
    public static final int LOADING_WIDTH = 896;
    public static final int LOADING_HEIGHT = 336;
    public static final int ROWS = 6;
    public static final int COLS = 12;
    public static final float FRAME_DURATION = 0.15f;

    /** Screen boundary. */
    public static final int LEFT_BOUNDARY = 0;
    public static final int RIGHT_BOUNDARY = SCREEN_WIDTH;
    public static final int UP_BOUNDARY = SCREEN_HEIGHT - HP_HEIGHT;
    public static final int DOWN_BOUNDARY = 0;

    /** Bar. */
    public static final int BAR_WIDTH = 464;
    public static final int BAR_HEIGHT = 56;
    public static final Vector2 BAR_VELOCITY = new Vector2(900f, 900f);
    public static final float MAX_BOUNCE_ANGLE = (float) Math.toRadians(60);

    public static final int POWER_UP_WIDTH = 1920;
    public static final int POWER_UP_HEIGHT = 1080;

    public static final float SKILL2_COOLDOWN = 20f;

    /** Ball. */
    public static final int BALL_WIDTH = 64;
    public static final int BALL_HEIGHT = 64;
    public static final Vector2 BALL_VELOCITY = new Vector2(900f, 900f);

    /** Boss1. */
    public static final int BOSS1_WIDTH = 272;
    public static final int BOSS1_HEIGHT = 224;
    public static final Vector2 BOSS1_VELOCITY = new Vector2(900f, 900f);

    /** Boss1 Skill1. */
    public static final int BOSS1_SKILL1_WIDTH = 80;
    public static final int BOSS1_SKILL1_HEIGHT = 144;

    /** Bomb. */
    public static final float BOMB_SPEED_Y = 400f;
    public static final float BOSS_STOP_TIME = 1f;

    public static final int LASER_WIDTH = 80;
    public static final int LASER_HEIGHT = 1080;

    /** Brick. */
    public static final int BRICK_ROWS = 4;
    public static final int BRICK_COLS = 10;
    public static final int BRICK_PADDING = 8;
    public static final float BRICK_START_X = 80f;
    public static final float BRICK_START_Y = SCREEN_HEIGHT - 150f;
    public static final int BRICK_WIDTH = (int) ((SCREEN_WIDTH - 2 * BRICK_START_X - (BRICK_COLS - 1) * BRICK_PADDING) / BRICK_COLS);
    public static final int BRICK_HEIGHT = (int) (BRICK_WIDTH / 2.5f);
    public static final float BRICK_SPACING_X = BRICK_WIDTH + BRICK_PADDING;
    public static final float BRICK_SPACING_Y = BRICK_HEIGHT + BRICK_PADDING;
    public static final float START_X = BRICK_START_X;
    public static final float START_Y = BRICK_START_Y;

    /** Asset paths used by BrickStage. */
    public static final String BRICK_BACKGROUND_PATH = "menu/layer0.png";
    public static final String BAR_TEXTURE_PATH = "Bar.png";
    public static final String BALL_NORMAL_TEXTURE_PATH = "ball/normal.png";
    public static final String BRICK_TEXTURE_BLUE_PATH = "brick/blue.png";
    public static final String BRICK_TEXTURE_GREEN_PATH = "brick/green.png";
    public static final String BRICK_TEXTURE_ORANGE_PATH = "brick/orange.png";
    public static final String BRICK_TEXTURE_RED_PATH = "brick/red.png";

}


