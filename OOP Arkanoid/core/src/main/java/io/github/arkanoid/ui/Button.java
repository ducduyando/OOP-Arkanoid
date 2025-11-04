package io.github.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.core.Constants.*;

public class Button extends Actor {
    private TextureRegion currentFrame;
    private float stateTime = 0f;
    private final Texture buttonSprite = new Texture("Menu/" + "layer" + 4 + ".png");
    private Sound swichSound = Gdx.audio.newSound(Gdx.files.internal("SFX/" + "Switch" + ".wav"));
    public enum Mode {
        PLAY,
        LOAD,
        QUIT
    }
    private boolean isGameModeChosen = false;

    private final Animation<TextureRegion> playAnimation;
    private final Animation<TextureRegion> loadAnimation;
    private final Animation<TextureRegion> quitAnimation;
    private Mode mode = Mode.PLAY;

    public Button() {
        TextureRegion[] playFrames = new TextureRegion[2];
        TextureRegion[] loadFrames = new TextureRegion[2];
        TextureRegion[] quitFrames = new TextureRegion[2];

        for (int i = 0; i < 2; i++) {
            playFrames[i] = new TextureRegion(buttonSprite, i * PLAY_BUTTON_WIDTH, 0, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        for (int i = 0; i < 2; i++) {
            loadFrames[i] = new TextureRegion(buttonSprite, i * PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        for (int i = 0; i < 2; i++) {
            quitFrames[i] = new TextureRegion(buttonSprite, i * PLAY_BUTTON_WIDTH, 2 * PLAY_BUTTON_HEIGHT, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        this.playAnimation = new Animation<>(FRAME_DURATION * 3, playFrames);
        this.loadAnimation = new Animation<>(FRAME_DURATION * 3, loadFrames);
        this.quitAnimation = new Animation<>(FRAME_DURATION * 3, quitFrames);

        this.currentFrame = playFrames[0];

        setPosition((SCREEN_WIDTH - PLAY_BUTTON_WIDTH) /2f, 300);
        setSize(PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        setOrigin(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f);

    }

    public boolean isGameModeChosen() {
        return isGameModeChosen;
    }

    public Mode getMode() {
        return mode;
    }

    public void resetChoice() {
        isGameModeChosen = false;
    }

    @Override
    public void act(float delta) {
        stateTime += delta;
        if (mode == Mode.PLAY) {
            currentFrame = playAnimation.getKeyFrame(stateTime, true);
        }
        else if (mode == Mode.LOAD) {
            currentFrame = loadAnimation.getKeyFrame(stateTime, true);
        }
        else if (mode == Mode.QUIT) {
            currentFrame = quitAnimation.getKeyFrame(stateTime, true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            isGameModeChosen = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
            || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
            || Gdx.input.isKeyJustPressed(Input.Keys.S)
            || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (mode == Mode.PLAY) {
                mode = Mode.QUIT;
            } else if (mode == Mode.LOAD) {
                mode = Mode.PLAY;
            } else if (mode == Mode.QUIT) {
                mode = Mode.LOAD;
            }
            isGameModeChosen = false;

            swichSound.play();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
            || Gdx.input.isKeyJustPressed(Input.Keys.UP)
            || Gdx.input.isKeyJustPressed(Input.Keys.W)
            || Gdx.input.isKeyJustPressed(Input.Keys.D)) {

            if (mode == Mode.PLAY) {
                mode = Mode.LOAD;
            } else if (mode == Mode.LOAD) {
                mode = Mode.QUIT;
            } else if (mode == Mode.QUIT) {
                mode = Mode.PLAY;
            }
            isGameModeChosen = false;

            swichSound.play();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }

    public void dispose() {

        buttonSprite.dispose();
        swichSound.dispose();
    }
}
