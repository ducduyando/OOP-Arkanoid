package io.github.arkanoid.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.core.Constants;

import static io.github.arkanoid.core.Constants.*;

public class GameLoseStage implements GameStage {
    private Stage stage;
    private LoseEffectActor loseEffectActor;
    private boolean isFinished = false;
    private boolean isQuitRequested = false;

    public GameLoseStage() {
    }

    @Override
    public void enter() {
        System.out.println("GameLoseStage: enter() called");
        stage = new Stage(new ScreenViewport());
        loseEffectActor = new LoseEffectActor();
        stage.addActor(loseEffectActor);
        Gdx.input.setInputProcessor(stage);
        System.out.println("GameLoseStage: setup complete");
    }

    @Override
    public void update(float delta) {
        stage.act(delta);

        if (loseEffectActor.isAnimationComplete()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                isFinished = true;
                isQuitRequested = true;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void exit() {
        if (loseEffectActor != null) {
            loseEffectActor.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }

    @Override
    public Stage getGdxStage() {
        return stage;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    public boolean isQuitRequested() {
        return isQuitRequested;
    }

    public void drawDirect(SpriteBatch batch) {
        if (loseEffectActor != null) {
            loseEffectActor.drawDirect(batch);
        }
    }


    private static class LoseEffectActor extends Actor {
        private final Texture lose1Texture;
        private final Texture lose2Texture;
        private final Texture lose3Texture;

        private Animation<TextureRegion>[] loseAnimation;
        private int lostNumber = 0;

        private TextureRegion currentFrame;
        private float stateTime = 0f;
        private boolean animationComplete = false;

        public LoseEffectActor() {

            lose1Texture = new Texture("Lose/" + "scene0" + ".png");
            lose2Texture = new Texture("Lose/" + "scene1" + ".png");
            lose3Texture = new Texture("Lose/" + "scene2" + ".png");

            loseAnimation = new Animation[3];

            int maxFrame1 = lose1Texture.getWidth() / SCREEN_WIDTH;
            TextureRegion[] lose1Frames = new TextureRegion[maxFrame1];
            for (int i = 0; i < maxFrame1; i++) {
                lose1Frames[i] = new TextureRegion(lose1Texture, i * SCREEN_WIDTH, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            }
            loseAnimation[0] = new Animation<>(FRAME_FALL_DURATION, lose1Frames);

            int maxFrame2 = lose2Texture.getWidth() / SCREEN_WIDTH;
            TextureRegion[] lose2Frames = new TextureRegion[maxFrame2];
            for (int i = 0; i < maxFrame2; i++) {
                lose2Frames[i] = new TextureRegion(lose2Texture, i * SCREEN_WIDTH, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            }
            loseAnimation[1] = new Animation<>(FRAME_FALL_DURATION, lose2Frames);

            int maxFrame3 = lose3Texture.getWidth() / SCREEN_WIDTH;
            TextureRegion[] lose3Frames = new TextureRegion[maxFrame3];
            for (int i = 0; i < maxFrame3; i++) {
                lose3Frames[i] = new TextureRegion(lose3Texture, i * SCREEN_WIDTH, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            }
            loseAnimation[2] = new Animation<>(FRAME_FALL_DURATION, lose3Frames);

            this.currentFrame = lose1Frames[0];

            setPosition(0, 0);
            setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        }

        public boolean isAnimationComplete() {
            return animationComplete;
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (!animationComplete) {
                stateTime += delta;
                currentFrame = loseAnimation[lostNumber].getKeyFrame(FRAME_DURATION * 1.5f, false);
                if (loseAnimation[lostNumber].isAnimationFinished(stateTime)) {
                    stateTime = 0;
                    lostNumber++;
                    if (lostNumber >= loseAnimation.length) {
                        animationComplete = true;
                    }
                }
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {

        }


        public void drawDirect(SpriteBatch batch) {
            if (currentFrame != null) {
                batch.draw(currentFrame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            }
        }

        public void dispose() {
            if (lose1Texture != null) {
                lose1Texture.dispose();
            }
            if (lose2Texture != null) {
                lose2Texture.dispose();
            }
            if (lose3Texture != null) {
                lose3Texture.dispose();
            }
        }
    }
}
