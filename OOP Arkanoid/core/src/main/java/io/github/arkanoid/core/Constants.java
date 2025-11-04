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

    /** Ball. */
    public static final int BALL_WIDTH = 64;
    public static final int BALL_HEIGHT = 64;
    public static final Vector2 BALL_VELOCITY = new Vector2(900f, 900f);
    public static int BALL_DAMAGE = 10;

    /** Brick. */
    public static final int BRICK_ROWS = 4;
    public static final int BRICK_COLS = 10;
    public static final int BRICK_WIDTH = 192;
    public static final int BRICK_HEIGHT = 96;

    /** Paddle. */
    public static final int PADDLE_WIDTH = 464;
    public static final int PADDLE_HEIGHT = 56;
    public static final Vector2 PADDLE_VELOCITY = new Vector2(900f, 900f);
    public static final float MAX_BOUNCE_ANGLE = (float) Math.toRadians(60);
    public static final float PADDLE_INITIAL_X = (SCREEN_WIDTH - PADDLE_WIDTH) / 2f;
    public static final float PADDLE_INITIAL_Y = 150f;

    public static final int PADDLE_DAMAGE = 10;

    public static final float PADDLE_SKILL_COOLDOWN = 12f;

    /** Screen boundary. */
    public static final int LEFT_BOUNDARY = 0;
    public static final int RIGHT_BOUNDARY = SCREEN_WIDTH;
    public static final int UP_BOUNDARY = SCREEN_HEIGHT - HP_HEIGHT;
    public static final int TUTORIAL_UP_BOUNDARY = SCREEN_HEIGHT - BALL_HEIGHT - BRICK_HEIGHT * BRICK_ROWS;
    public static final int DOWN_BOUNDARY = 0;

    /** Transition. */
    public static final int TRANSITION_WIDTH = 1920;
    public static final int TRANSITION_HEIGHT = 1080;

    /** Power Up. */
    public static final int POWER_UP_WIDTH = 1920;
    public static final int POWER_UP_HEIGHT = 1080;

    /** Boss1. */
    public static final int BOSS1_WIDTH = 272;
    public static final int BOSS1_HEIGHT = 224;
    public static final Vector2 BOSS1_VELOCITY = new Vector2(900f, 900f);
    public static final float BOSS1_INITIAL_X = (SCREEN_WIDTH - BOSS1_WIDTH) / 2f;
    public static final float BOSS1_INITIAL_Y = SCREEN_HEIGHT * 0.6f;

    /** Boss1 Centering. */
    public static final float BOSS1_COOLDOWN_DURATION = 3f;

    /** Boss1 Skill 1. */
    public static final int BOSS1_SKILL1_WIDTH = 80;
    public static final int BOSS1_SKILL1_HEIGHT = 144;

    public static final int MAX_BOMBS = 3;

    public static final float BOMB_SPEED_Y = 400f;
    public static final float BOSS_STOP_TIME = 1f;

    /** Boss1 Skill 2. */
    public static final int LASER_WIDTH = 80;
    public static final int LASER_HEIGHT = 1080;

    public static final float SWEEP_SPEED = 400f;

    /** Boss2. */
    public static final int BOSS2_WIDTH = 448;
    public static final int BOSS2_HEIGHT = 272;
    public static final Vector2 BOSS2_VELOCITY = new Vector2(900f, 900f);
    public static final float BOSS2_INITIAL_X = (SCREEN_WIDTH - BOSS2_WIDTH) / 2f;
    public static final float BOSS2_INITIAL_Y = SCREEN_HEIGHT * 0.6f;

    /** Boss2 Random moving. */
    public static final float BOSS2_COOLDOWN_DURATION = 2f;
    public static final float BOSS2_SKILL_INTERVAL = 5f;

    /** Boss2 Skill 1. */
    public static final int BOSS2_SKILL1_WIDTH = 96;
    public static final int BOSS2_SKILL1_HEIGHT = 96;

    public static final float SPAWN_INTERVAL = 5f;
    public static final float SPAWN_BEE_DELAY = 0.5f;
    public static final float BEE_SPEED_Y = 400f;

    /** Boss2 Skill 2. */
    public static final int BOSS2_SKILL2_WIDTH = 448;
    public static final int BOSS2_SKILL2_HEIGHT = 272;

    public static final float HEALING_COOLDOWN = 1.5f;
    public static final float MAX_SHIELD_DURATION = 4f;

    /** Boss3. */
    public static final int BOSS3_WIDTH = 248;
    public static final int BOSS3_HEIGHT = 336;
    public static final Vector2 BOSS3_VELOCITY = new Vector2(900f, 900f);
    public static final float BOSS3_INITIAL_X = (SCREEN_WIDTH - BOSS3_WIDTH) / 2f;
    public static final float BOSS3_INITIAL_Y = SCREEN_HEIGHT * 0.6f;

    /** Boss3 Random moving. */
    public static final float BOSS3_COOLDOWN_DURATION = 2f;
    public static final float BOSS3_SKILL_INTERVAL = 5f;

    /** Boss3 Skill 1-A. */
    public static final int BOSS3_SKILL_1A_WIDTH = 352;
    public static final int BOSS3_SKILL_1A_HEIGHT = 352;
    public static final int ROCKET_SPEED = 550;

    public static final int MAX_ROCKETS = 3;

    /** Boss3 Skill 1-B. */
    public static final int BOSS3_SKILL_1B_WIDTH = 256;
    public static final int BOSS3_SKILL_1B_HEIGHT = 256;
    public static final int HORIZONTAL_SAW_SPEED = 889;
    public static final int VERTICAL_SAW_SPEED = 500;

    public static final int MAX_SAWS = 2;

    /** Boss3 Skill 2-A. */
    public static final int BOSS3_SKILL_2A_WIDTH = 328;
    public static final int BOSS3_SKILL_2A_HEIGHT = 416;

    public static final int ROTATION_SPEED = 1080;

    /** Boss3 Skill 2-B. */
    public static final Vector2 LASER_SPEED = new Vector2(400f, 400f);

    public static final int EYES_DISTANCE = 120;
    public static final Vector2 LEFT_EYE_COORDINATE = new Vector2(55, 184);
    public static final Vector2 RIGHT_EYE_COORDINATE = new Vector2(175, 184);

    public static final int MAX_LASER_BEAMS = 5;
    public static final float LASER_DELAY = 0.5f;

    /** Paddle Skill 1-A. */
    public static final float SECOND_BALL_DELAY = 0.5f;

    /** Paddle Skill 1-B. */
    public static final float LASER_DURATION = 2f;

    /** Paddle Skill 2-A. */
    public static final int MAX_BEES = 3;
    public static final float NEXT_BEE_SPAWN_DELAY = 0.25f;
    public static final float BEE_BULLET_SPEED_Y = 300f;

    /** Paddle Skill 2-B. */
    public static final float HONEY_SHIELD_DURATION = 4f;

    /** Skill icons. */
    public static final int SKILL_ICON_WIDTH = 144;
    public static final int SKILL_ICON_HEIGHT = 144;
    public static  final float MAX_COOLDOWN = 12f;

    /** Save rank. */
    public static final int MAX_RANK_ENTRIES = 3;

    /** Name. */
    public static final int FIELD_WIDTH = 800;
    public static final int FIELD_HEIGHT = 120;
    public static final int MAX_NAME_LENGTH = 15;

    /** Rank. */
    public static final float COL_WIDTH = 260f;
    public static final float NAME_WIDTH = 350f;
    public static final float HORIZONTAL_PAD = 12f;
    public static final float VERTICAL_PAD = 8f;

    /** Default key bindings. */
    public static final String ACTION_MOVE_LEFT = "move_left";
    public static final String ACTION_MOVE_RIGHT = "move_right";
    public static final String ACTION_SKILL_1 = "skill_1";
    public static final String ACTION_SKILL_2 = "skill_2";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_CONFIRM = "confirm";
    public static final String ACTION_CANCEL = "cancel";

    /** Save. */
    public static final String SAVE_FILE = "game_save";
    public static final String RANK_PREFIX = "rank_";
    public static final String KEY_PLAYER_NAME = "playerName";
    public static final String KEY_TOTAL_GAME_TIME = "totalGameTime";

    public static float totalGameTime = 0f;
    public static boolean isGameStarted = false;
}
