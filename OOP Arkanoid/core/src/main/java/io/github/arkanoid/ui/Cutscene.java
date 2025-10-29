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

    private Texture cutscene1;
    private Texture cutscene2;
    private Texture cutscene3;
    private Texture cutscene4;

    private State state;

    private int cutsceneNumber = 0;

    private Animation<TextureRegion>[] cutsceneAnimation;



    public Cutscene() {

        cutscene1 = new Texture("Cutscene/" + "layer0" + ".png");
        cutscene2 = new Texture("Cutscene/" + "layer1" + ".png");
        cutscene3 = new Texture("Cutscene/" + "layer2" + ".png");
        cutscene4 = new Texture("Cutscene/" + "layer3" + ".png");
        state = State.LOADING;
        cutsceneAnimation = new Animation[4];

        int maxFrame1 = cutscene1.getHeight() / SCREEN_HEIGHT;
        TextureRegion[] frames1 = new TextureRegion[maxFrame1];
        for (int i = 0; i < maxFrame1; i++) {
            frames1[i] = new TextureRegion(cutscene1, 0, i * SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        cutsceneAnimation[0] = new Animation<>(FRAME_DURATION * 1.5f, frames1);

        int maxFrame2 = cutscene2.getHeight() / SCREEN_HEIGHT;
        TextureRegion[] frames2 = new TextureRegion[maxFrame2];
        for (int i = 0; i < maxFrame2; i++) {
            frames2[i] = new TextureRegion(cutscene2, 0, i * SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        cutsceneAnimation[1] = new Animation<>(FRAME_DURATION * 1.5f, frames2);

        int maxFrame3 = cutscene3.getHeight() / SCREEN_HEIGHT;
        TextureRegion[] frames3 = new TextureRegion[maxFrame3];
        for (int i = 0; i < maxFrame3; i++) {
            frames3[i] = new TextureRegion(cutscene3, 0, i * SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        cutsceneAnimation[2] = new Animation<>(FRAME_DURATION * 1.5f, frames3);

        int maxFrame4 = cutscene4.getHeight() / SCREEN_HEIGHT;
        TextureRegion[] frames4 = new TextureRegion[maxFrame4];
        for (int i = 0; i < maxFrame4; i++) {
            frames4[i] = new TextureRegion(cutscene4, 0, i * SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        cutsceneAnimation[3] = new Animation<>(FRAME_DURATION * 1.5f, frames4);

        currentFrame = frames1[0];
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
