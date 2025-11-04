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
    private final Texture[] cutsceneTextures = new Texture[4];
    private final Texture transitionTexture;
    private TextureRegion currentFrame;

    private final TextureRegion[][] cutsceneFrames = new TextureRegion[4][];
    private final TextureRegion[] transitionFrames;

    private State state;

    private int cutsceneNumber = 0;

    private Animation<TextureRegion>[] cutsceneAnimation = new Animation[5];



    public Cutscene() {
        state = State.LOADING;
        transitionTexture = new Texture("powerUp/" + "transition" + ".png");
        for (int i = 0; i < 4; i++) {
            cutsceneTextures[i] = new Texture("Cutscene/" + "scene" + i + ".png");
        }

        int maxTransitionFrame = transitionTexture.getWidth() / TRANSITION_WIDTH;
        transitionFrames = new TextureRegion[maxTransitionFrame];
        for (int i = 0; i < maxTransitionFrame; i++) {
            transitionFrames[i] = new TextureRegion(transitionTexture, TRANSITION_WIDTH * i, 0, TRANSITION_WIDTH, TRANSITION_HEIGHT);
        }
        cutsceneAnimation[0] = new Animation<>(FRAME_DURATION * 2f, transitionFrames);

        for (int i = 0; i < 4; i++) {
            int maxFrames = cutsceneTextures[i].getHeight() / SCREEN_HEIGHT;
            cutsceneFrames[i] = new TextureRegion[maxFrames];
            for (int j = 0; j < maxFrames; j++) {
                cutsceneFrames[i][j] = new TextureRegion(cutsceneTextures[i], 0, SCREEN_HEIGHT * j, SCREEN_WIDTH, SCREEN_HEIGHT);
            }
            cutsceneAnimation[i + 1] = new Animation<>(FRAME_DURATION * 2f, cutsceneFrames[i]);
        }

        this.currentFrame = transitionFrames[0];

        setPosition(0, 0);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public State getState() {
        return state;
    }

    @Override
    public void act(float delta) {
        stateTime += delta;
        currentFrame = cutsceneAnimation[cutsceneNumber].getKeyFrame(stateTime, false);
        if (cutsceneAnimation[cutsceneNumber].isAnimationFinished(stateTime)) {
            stateTime = 0;
            cutsceneNumber++;
            if (cutsceneNumber >= cutsceneAnimation.length) {
                state = State.DONE;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(currentFrame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);


    }

}
