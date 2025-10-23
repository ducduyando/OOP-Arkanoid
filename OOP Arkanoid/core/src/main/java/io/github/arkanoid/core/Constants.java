package io.github.arkanoid.core;

import com.badlogic.gdx.math.Vector2;

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

    /** Transition. */
    public static final int TRANSITION_WIDTH = 1920;
    public static final int TRANSITION_HEIGHT = 1080;

    /** Paddle. */
    public static final int PADDLE_WIDTH = 464;
    public static final int PADDLE_HEIGHT = 56;
    public static final Vector2 PADDLE_VELOCITY = new Vector2(900f, 900f);
    public static final float MAX_BOUNCE_ANGLE = (float) Math.toRadians(60);
    public static final float PADDLE_INITIAL_X = (SCREEN_WIDTH - PADDLE_WIDTH) / 2f;
    public static final float PADDLE_INITIAL_Y = 150f;

    /** Brick. */
    public static final int BRICK_ROWS = 4;
    public static final int BRICK_COLS = 10;
    public static final int BRICK_WIDTH = 192;
    public static final int BRICK_HEIGHT = 96;

    /** Power Up. */
    public static final int POWER_UP_WIDTH = 1920;
    public static final int POWER_UP_HEIGHT = 1080;

    public static final float SKILL_COOLDOWN = 20f;

    /** Ball. */
    public static final int BALL_WIDTH = 64;
    public static final int BALL_HEIGHT = 64;
    public static final Vector2 BALL_VELOCITY = new Vector2(900f, 900f);
    public static final int BALL_UPGRADED_DAMAGE = 15;


    /** Boss1. */
    public static final int BOSS1_WIDTH = 272;
    public static final int BOSS1_HEIGHT = 224;
    public static final Vector2 BOSS1_VELOCITY = new Vector2(900f, 900f);
    public static final float BOSS1_INITIAL_X = (SCREEN_WIDTH - BOSS1_WIDTH) / 2f;
    public static final float BOSS1_INITIAL_Y = SCREEN_HEIGHT * 0.6f;

    /** Boss1 Skill 1. */
    public static final int BOSS1_SKILL1_WIDTH = 80;
    public static final int BOSS1_SKILL1_HEIGHT = 144;

    public static final float BOMB_SPEED_Y = 400f;
    public static final float BOSS_STOP_TIME = 1f;

    /** Boss1 Skill 2. */
    public static final int LASER_WIDTH = 80;
    public static final int LASER_HEIGHT = 1080;

    /** Boss2. */
    public static final int BOSS2_WIDTH = 448;
    public static final int BOSS2_HEIGHT = 272;
    public static final Vector2 BOSS2_VELOCITY = new Vector2(900f, 900f);
    public static final float BOSS2_INITIAL_X = (SCREEN_WIDTH - BOSS2_WIDTH) / 2f;
    public static final float BOSS2_INITIAL_Y = SCREEN_HEIGHT * 0.6f;

    /** Boss1 Skill 1. */
    public static final int BOSS2_SKILL1_WIDTH = 96;
    public static final int BOSS2_SKILL1_HEIGHT = 96;

    public static final float BEE_SPEED_Y = 400f;
}
