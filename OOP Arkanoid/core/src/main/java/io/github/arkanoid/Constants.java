package io.github.arkanoid;

public class Constants {
    /** Screen. */
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;
    public static final int BUTTON_WIDTH = 880;
    public static final int BUTTON_HEIGHT = 272;
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
    public static final float BAR_VELOCITY_X = 900f;
    public static final float BAR_VELOCITY_Y = 900f;
    public static final float MAX_BOUNCE_ANGLE = (float) Math.toRadians(60);

    /** Ball. */
    public static final int BALL_WIDTH = 64;
    public static final int BALL_HEIGHT = 64;
    public static final float BALL_VELOCITY_X = 900f;
    public static final float BALL_VELOCITY_Y = 900f;

    /** Boss1. */
    public static final int BOSS1_WIDTH = 272;
    public static final int BOSS1_HEIGHT = 224;
    public static final float BOSS1_VELOCITY_X = 900f;
    public static final float BOSS1_VELOCITY_Y = 900f;

    /** Boss1 Skill1. */
    public static final int BOSS1_SKILL1_WIDTH = 80;
    public static final int BOSS1_SKILL1_HEIGHT = 144;

    /** Bomb. */
    public static final float BOMB_SPEED_Y = 400f;
    public static final float BOSS_STOP_TIME = 1f;

    public static final int LASER_WIDTH = 80;
    public static final int LASER_HEIGHT = 1080;

    /** Menu buttons. */
    public static final int BUTTON_FRAME_WIDTH = 440;
    public static final int BUTTON_FRAME_HEIGHT = 272;
    public static final float BUTTON_SCALE = 0.8f;
    public static final float BUTTON_SPACING = 500f;

    /** Menu animation. */
    public static final float MENU_ANIM_FRAME_TIME = 0.4f;
    public static final float MENU_GLITCH_SPEED = 30f;
    public static final float MENU_GLITCH_RANGE = 20f;
    public static final float MENU_KEY_COOLDOWN = 0.2f;

    /** Pause Menu Buttons. */
    public static final int PAUSE_BUTTON_WIDTH = BUTTON_WIDTH;
    public static final int PAUSE_BUTTON_HEIGHT = BUTTON_HEIGHT;
    public static final float PAUSE_MENU_Y_OFFSET = 500f;
}
