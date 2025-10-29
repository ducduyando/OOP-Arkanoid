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

    private Texture cutscene1Texture;
    private Texture cutscene2Texture;
    private Texture cutscene3Texture;
    private Texture cutscene4Texture;

    private State state;

    private int cutsceneNumber = 0;

    private Animation<TextureRegion>[] cutsceneAnimation;



    public Cutscene() {

        cutscene1Texture = new Texture("Cutscene/" + "scene0" + ".png");
        cutscene2Texture = new Texture("Cutscene/" + "scene1" + ".png");
        cutscene3Texture = new Texture("Cutscene/" + "scene2" + ".png");
        cutscene4Texture = new Texture("Cutscene/" + "scene3" + ".png");
        state = State.LOADING;
        cutsceneAnimation = new Animation[4];

        int maxFrame1 = cutscene1Texture.getHeight() / SCREEN_HEIGHT;
        TextureRegion[] cutscene1 = new TextureRegion[maxFrame1];
        for (int i = 0; i < maxFrame1; i++) {
            cutscene1[i] = new TextureRegion(cutscene1Texture, 0, i * SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        cutsceneAnimation[0] = new Animation<>(FRAME_DURATION * 1.5f, cutscene1);

        int maxFrame2 = cutscene2Texture.getHeight() / SCREEN_HEIGHT;
        TextureRegion[] cutscene2 = new TextureRegion[maxFrame2];
        for (int i = 0; i < maxFrame2; i++) {
            cutscene2[i] = new TextureRegion(cutscene2Texture, 0, i * SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        cutsceneAnimation[1] = new Animation<>(FRAME_DURATION * 1.5f, cutscene2);

        int maxFrame3 = cutscene3Texture.getHeight() / SCREEN_HEIGHT;
        TextureRegion[] cutscene3 = new TextureRegion[maxFrame3];
        for (int i = 0; i < maxFrame3; i++) {
            cutscene3[i] = new TextureRegion(cutscene3Texture, 0, i * SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        cutsceneAnimation[2] = new Animation<>(FRAME_DURATION * 1.5f, cutscene3);

        int maxFrame4 = cutscene4Texture.getHeight() / SCREEN_HEIGHT;
        TextureRegion[] cutscene4 = new TextureRegion[maxFrame4];
        for (int i = 0; i < maxFrame4; i++) {
            cutscene4[i] = new TextureRegion(cutscene4Texture, 0, i * SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        cutsceneAnimation[3] = new Animation<>(FRAME_DURATION * 1.5f, cutscene4);

        currentFrame = cutscene1[0];
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
