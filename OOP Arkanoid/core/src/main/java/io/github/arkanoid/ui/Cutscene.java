package io.github.arkanoid.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.core.Constants.*;

public class Cutscene extends Actor {
    public enum State {
        LOADING,
        DONE
    }

    private float stateTime = 0f;
    private TextureRegion currentFrame;
    private Texture cutsceneBackground;
    private Texture cutscene;
    private State state;

    private Animation<TextureRegion> cutsceneAnimation;

    public Cutscene() {
        cutsceneBackground = new Texture("");
        cutscene = new Texture("");
        state = State.LOADING;

        int maxFrame = cutscene.getHeight() / CUTSCENE_HEIGHT;
        TextureRegion[] frames = new TextureRegion[maxFrame];
        for (int i = 0; i < maxFrame; i++) {
            frames[i] = new TextureRegion(cutsceneBackground, 0, i * CUTSCENE_HEIGHT, CUTSCENE_WIDTH, CUTSCENE_HEIGHT);
        }
        cutsceneAnimation = new Animation<>(FRAME_DURATION * 1.5f, frames);
        currentFrame = frames[0];
    }

    public State getState() {
        return state;
    }

    @Override
    public void act(float delta) {
        stateTime += delta;
        currentFrame = cutsceneAnimation.getKeyFrame(stateTime, false);
        if (cutsceneAnimation.isAnimationFinished(stateTime)) {
            state = State.DONE;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float x = (SCREEN_WIDTH - CUTSCENE_WIDTH) / 2f;
        float y = (SCREEN_HEIGHT - CUTSCENE_HEIGHT) / 2f;
        batch.draw(cutsceneBackground, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.draw(currentFrame, x, y, CUTSCENE_WIDTH, CUTSCENE_HEIGHT);

    }

}
