package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.Constants.*;

public class LoadingStage extends Actor {
    protected enum State {
        TRANSITING,
        LOADING,
        DONE
    }

    private float stateTime = 0f;
    private TextureRegion currentFrame;
    Texture stage;
    private State state;

    private final Animation<TextureRegion> transitingAnimation;
    Texture transitingTexture = new Texture("stages/" + "transition" + ".png");
    private final int maxTransitingFrames = transitingTexture.getWidth() / SCREEN_WIDTH;
    TextureRegion[] transitingFrames = new TextureRegion[maxTransitingFrames];

    private final Animation<TextureRegion> loadingAnimation;
    Texture loadingBarTexture = new Texture("stages/" + "loading" + ".png");
    private final int maxLoadingFrames = loadingBarTexture.getHeight() / LOADING_HEIGHT;
    TextureRegion[] loadingFrames = new TextureRegion[maxLoadingFrames];

    LoadingStage(Texture stage) {
        this.stage = stage;
        state = State.TRANSITING;
        for (int i = 0; i < maxTransitingFrames; i++) {
            transitingFrames[i] = new TextureRegion(transitingTexture, i * SCREEN_WIDTH, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        this.transitingAnimation = new Animation<>(FRAME_DURATION * 1.5f, transitingFrames);

        this.currentFrame = transitingFrames[0];

        for (int i = 0; i < maxLoadingFrames; i++) {
            loadingFrames[i] = new TextureRegion(loadingBarTexture, 0,  i * LOADING_HEIGHT, LOADING_WIDTH, LOADING_HEIGHT);
        }
        this.loadingAnimation = new Animation<>(FRAME_DURATION * 1.5f, loadingFrames);
    }
    public State getState() {
        return state;
    }

    @Override
    public void act(float delta) {
        stateTime += delta;

        if (state == State.TRANSITING) {
            currentFrame = transitingAnimation.getKeyFrame(stateTime, false);
            if (transitingAnimation.isAnimationFinished(stateTime)) {
                state = State.LOADING;
                stateTime = 0f;
            }
        }

        else if (state == State.LOADING) {
            currentFrame = loadingAnimation.getKeyFrame(stateTime, false);
            if (loadingAnimation.isAnimationFinished(stateTime)) {
                state = State.DONE;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int x = (SCREEN_WIDTH - LOADING_WIDTH) / 2;
        int y = SCREEN_HEIGHT / 2 - LOADING_HEIGHT;
        if (state == State.TRANSITING) {
            batch.draw(currentFrame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        else if (state == State.LOADING) {
            batch.draw(stage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            batch.draw(currentFrame, x, y, LOADING_WIDTH, LOADING_HEIGHT);
        }
    }

    public void dispose() {
        if (transitingTexture != null) {
            transitingTexture.dispose();
        }
        if (loadingBarTexture != null) {
            loadingBarTexture.dispose();
        }
    }
}
