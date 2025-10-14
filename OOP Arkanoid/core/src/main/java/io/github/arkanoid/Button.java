package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import static io.github.arkanoid.Constants.*;

public class Button extends Actor {
    private TextureRegion currentFrame;
    private float stateTime = 0f;
    private final Texture buttonSprite = new Texture("menu/" + "layer" + 4 + ".png");
    protected enum Mode {
        PLAY,
        QUIT
    }
    private boolean isGameModeChosen = false;

    private final Animation<TextureRegion> playAnimation;
    private final Animation<TextureRegion> quitAnimation;
    private Mode mode = Mode.PLAY;

    Button () {
        TextureRegion[] playFrames = new TextureRegion[2];
        TextureRegion[] quitFrames = new TextureRegion[2];

        for (int i = 0; i < 2; i++) {
            playFrames[i] = new TextureRegion(buttonSprite, i * PLAY_BUTTON_WIDTH, 0, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        for (int i = 0; i < 2; i++) {
            quitFrames[i] = new TextureRegion(buttonSprite, i * PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        this.playAnimation = new Animation<>(FRAME_DURATION * 3, playFrames);
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

    @Override
    public void act(float delta) {
        stateTime += delta;
        if (mode == Mode.PLAY) {
            currentFrame = playAnimation.getKeyFrame(stateTime, true);
        }
        else if (mode == Mode.QUIT) {
            currentFrame = quitAnimation.getKeyFrame(stateTime, true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            isGameModeChosen = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
            || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
            || Gdx.input.isKeyJustPressed(Input.Keys.UP)
            || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
            || Gdx.input.isKeyJustPressed(Input.Keys.W)
            || Gdx.input.isKeyJustPressed(Input.Keys.S)
            || Gdx.input.isKeyJustPressed(Input.Keys.A)
            || Gdx.input.isKeyJustPressed(Input.Keys.D)) {

            if (mode == Mode.PLAY) {
                mode = Mode.QUIT;
            }
            else if (mode == Mode.QUIT) {
                mode = Mode.PLAY;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }

    public void dispose() {
        buttonSprite.dispose();
    }
}

