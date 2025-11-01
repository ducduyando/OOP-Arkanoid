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

    public static final int PADDLE_SKILL1_DAMAGE = 15;
    public static final int PADDLE_SKILL2_DAMAGE = 10;

    public static final float PADDLE_SKILL_COOLDOWN = 5f;

    /** Brick. */
    public static final int BRICK_ROWS = 4;
    public static final int BRICK_COLS = 10;
    public static final int BRICK_WIDTH = 192;
    public static final int BRICK_HEIGHT = 96;

    /** Power Up. */
    public static final int POWER_UP_WIDTH = 1920;
    public static final int POWER_UP_HEIGHT = 1080;


    /** Ball. */
    public static final int BALL_WIDTH = 64;
    public static final int BALL_HEIGHT = 64;
    public static final Vector2 BALL_VELOCITY = new Vector2(900f, 900f);


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

    /** Boss2 Skill 1. */
    public static final int BOSS2_SKILL1_WIDTH = 96;
    public static final int BOSS2_SKILL1_HEIGHT = 96;

    public static final float BEE_SPEED_Y = 400f;

    public static final int BOSS2_SKILL2_WIDTH = 448;
    public static final int BOSS2_SKILL2_HEIGHT = 272;

    /** Paddle Skill 2-A. */
    public static final float BEE_BULLET_SPEED_Y = 300f;
    private static final float SKILL_COOLDOWN2 = 1.0f;
    /** J. */
    public static final float SKILL_1A_COOLDOWN = 5.0f;
    public static final int SKILL_ICON_WIDTH = 144;
    public static final int SKILL_ICON_HEIGHT = 144;
    public static  final float MAX_COOLDOWN = 5.0f;

    /** Boss3. */
    public static final int BOSS3_WIDTH = 448;
    public static final int BOSS3_HEIGHT = 272;
    public static final Vector2 BOSS3_VELOCITY = new Vector2(900f, 900f);
    public static final float BOSS3_INITIAL_X = (SCREEN_WIDTH - BOSS3_WIDTH) / 2f;
    public static final float BOSS3_INITIAL_Y = SCREEN_HEIGHT * 0.6f;
    public static final int BOSS3_RIGHT_HAND_WIDTH = 256;
    public static final int BOSS3_RIGHT_HAND_HEIGHT = 256;

    public static final int BOSS3_LEFT_HAND_WIDTH = 256;
    public static final int BOSS3_LEFT_HAND_HEIGHT = 256;
    /** Save rank. */
    public static final int MAX_RANK_ENTRIES = 3;

    /** Name. */
    public static final int FIELD_WIDTH = 800;
    public static final int FIELD_HEIGHT = 120;

    /** Rank. */
    public static final float COL_WIDTH = 260f;
    public static final float NAME_WIDTH = 350f;
    public static final float PAD = 20f;

}
