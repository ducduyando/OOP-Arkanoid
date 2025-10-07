package io.github.arkanoid;

public class Constants {
    /** Screen. */
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;

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

    /** Screen boundary. */
    public static final int LEFT_BOUNDARY = 0;
    public static final int RIGHT_BOUNDARY = SCREEN_WIDTH;
    public static final int UP_BOUNDARY = SCREEN_HEIGHT;
    public static final int DOWN_BOUNDARY = 0;
}
