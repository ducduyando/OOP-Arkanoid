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
        private final Texture loseTexture;
        private final Animation<TextureRegion> loseAnimation;
        private TextureRegion currentFrame;
        private float stateTime = 0f;
        private boolean animationComplete = false;

        public LoseEffectActor() {
            loseTexture = new Texture("WinLose/" + "lose" + ".png");


            int frameHeight = loseTexture.getHeight() / LOSE_FRAME_COUNT;
            TextureRegion[] loseFrames = new TextureRegion[LOSE_FRAME_COUNT];

            for (int i = 0; i < LOSE_FRAME_COUNT; i++) {
                loseFrames[i] = new TextureRegion(loseTexture, 0, i * frameHeight, loseTexture.getWidth(), frameHeight);

            }

            this.loseAnimation = new Animation<>(FRAME_FALL_DURATION, loseFrames);
            this.currentFrame = loseFrames[0];

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
                currentFrame = loseAnimation.getKeyFrame(stateTime, false);
                if (loseAnimation.isAnimationFinished(stateTime)) {
                    animationComplete = true;
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
            if (loseTexture != null) {
                loseTexture.dispose();
            }
        }
    }
}
