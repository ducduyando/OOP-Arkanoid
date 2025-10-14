package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.Constants.*;

public class PauseMenu extends Actor {

    private final Texture pauseBackground = new Texture("pause/layer0.png");
    private final Texture buttonSprite = new Texture("pause/layer1.png");

    private TextureRegion currentFrame;
    private int frameWidth;
    private int frameHeight;
    private float stateTime = 0f;

    protected enum Option { RESUME, QUIT }

    private Option option = Option.RESUME;
    private boolean isOptionChosen = false;

    private final Animation<TextureRegion> resumeAnimation;
    private final Animation<TextureRegion> quitAnimation;

    public PauseMenu() {
        this.frameWidth = buttonSprite.getWidth() / 2;
        this.frameHeight = buttonSprite.getHeight() / 2;

        TextureRegion[] resumeFrames = new TextureRegion[2];
        TextureRegion[] quitFrames = new TextureRegion[2];

        for (int i = 0; i < 2; i++) {
            resumeFrames[i] = new TextureRegion(
                buttonSprite,
                i * frameWidth,
                0,
                frameWidth,
                frameHeight
            );
        }

        for (int i = 0; i < 2; i++) {
            quitFrames[i] = new TextureRegion(
                buttonSprite,
                i * frameWidth,
                frameHeight,
                frameWidth,
                frameHeight
            );
        }

        this.resumeAnimation = new Animation<>(FRAME_DURATION * 3, resumeFrames);
        this.quitAnimation = new Animation<>(FRAME_DURATION * 3, quitFrames);

        this.currentFrame = resumeFrames[0];

        setPosition(
            (SCREEN_WIDTH - frameWidth) / 2f,
            SCREEN_HEIGHT / 2f - frameHeight / 2f
        );

        setSize(frameWidth, frameHeight);
    }

    public boolean isOptionChosen() {
        return isOptionChosen;
    }

    public Option getOption() {
        return option;
    }

    @Override
    public void act(float delta) {
        stateTime += delta;

        if (option == Option.RESUME) {
            currentFrame = resumeAnimation.getKeyFrame(stateTime, true);
        } else if (option == Option.QUIT) {
            currentFrame = quitAnimation.getKeyFrame(stateTime, true);
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
            option = (option == Option.RESUME) ? Option.QUIT : Option.RESUME;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(pauseBackground, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }

    public void reset() {
        isOptionChosen = false;
        option = Option.RESUME;
        stateTime = 0f;
    }

    public void dispose() {
        pauseBackground.dispose();
        buttonSprite.dispose();
    }
}
