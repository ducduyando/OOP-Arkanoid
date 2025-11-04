package io.github.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.core.Constants.*;
import static io.github.arkanoid.core.MusicResource.*;


public class PowerUpMenu extends Actor {

    protected enum State {
        TRANSITION,
        DONE
    }

    protected State state = State.TRANSITION;


    private final Texture transitionSprite = new Texture("PowerUp/" + "transition" + ".png");

    private TextureRegion currentFrame;

    private float stateTime = 0f;

    public enum Option { SKILL1, SKILL2 }
    private Option option = Option.SKILL1;
    private boolean isOptionChosen = false;

    private final Animation<TextureRegion> skill1Animation;
    private final Animation<TextureRegion> skill2Animation;
    public final Animation<TextureRegion> transitionAnimation;


    public PowerUpMenu(Texture powerUpButton) {

        int transitionFrameCount = transitionSprite.getWidth() / TRANSITION_WIDTH;
        TextureRegion[] transitionFrames = new TextureRegion[transitionFrameCount];

        for (int i = 0; i < transitionFrameCount; i++) {
            transitionFrames[i] = new TextureRegion(transitionSprite ,TRANSITION_WIDTH * i, 0, TRANSITION_WIDTH, TRANSITION_HEIGHT);
        }

        this.transitionAnimation = new Animation<TextureRegion>(FRAME_DURATION * 1.5f, transitionFrames);

        TextureRegion[] skill1Frames = new TextureRegion[2];
        TextureRegion[] skill2Frames = new TextureRegion[2];

        for(int i = 0; i < 2; i++) {
            skill1Frames[i] = new TextureRegion(powerUpButton, i * POWER_UP_WIDTH, 0,
                POWER_UP_WIDTH, POWER_UP_HEIGHT);

        }
        for (int i = 0; i < 2; i++) {
            skill2Frames[i] = new TextureRegion(
                powerUpButton,
                i * POWER_UP_WIDTH, POWER_UP_HEIGHT,
                POWER_UP_WIDTH, POWER_UP_HEIGHT);
        }

        this.skill1Animation = new Animation<>(FRAME_DURATION * 3, skill1Frames);
        this.skill2Animation= new Animation<>(FRAME_DURATION * 3, skill2Frames);

        this.currentFrame = skill1Frames[0];

        setPosition(
            (SCREEN_WIDTH - POWER_UP_WIDTH) / 2f,
            (SCREEN_HEIGHT - POWER_UP_HEIGHT) / 2f
        );

        setSize(POWER_UP_WIDTH, POWER_UP_HEIGHT);


    }

    public boolean isOptionChosen() {
        return isOptionChosen;
    }

    public Option getOption() {
        return option;
    }

    public void act(float delta) {
        stateTime += delta;

        if (state == State.DONE) {
            if (option == Option.SKILL1) {
                currentFrame = skill1Animation.getKeyFrame(stateTime, true);
            } else if (option == Option.SKILL2) {
                currentFrame = skill2Animation.getKeyFrame(stateTime, true);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                isOptionChosen = true;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)
                || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
                || Gdx.input.isKeyJustPressed(Input.Keys.W)
                || Gdx.input.isKeyJustPressed(Input.Keys.S)
                || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
                || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
                || Gdx.input.isKeyJustPressed(Input.Keys.A)
                || Gdx.input.isKeyJustPressed(Input.Keys.D)) {

                SWITCH_SOUND.play();
                option = (option == PowerUpMenu.Option.SKILL1) ? PowerUpMenu.Option.SKILL2 : PowerUpMenu.Option.SKILL1;
            }
        }
        else {
            currentFrame = transitionAnimation.getKeyFrame(stateTime, false);
            if (transitionAnimation.isAnimationFinished(stateTime)) {
                state = State.DONE;
            }
        }
    }
    public void draw(Batch batch, float parentAlpha) {
        if (state == State.TRANSITION) {
            batch.draw(currentFrame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        } else {
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        }
    }

    public void reset() {
        isOptionChosen = false;
        option = Option.SKILL1;
        state = State.TRANSITION;
        stateTime = 0f;
    }

    public void dispose() {
        transitionSprite.dispose();
        SWITCH_SOUND.dispose();
    }
}
