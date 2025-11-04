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
import static io.github.arkanoid.core.MusicManager.*;

public class GameLoseStage implements GameStage {
    private Stage stage;
    private LoseEffectActor loseEffectActor;
    private boolean isFinished = false;
    private boolean isQuitRequested = false;


    public GameLoseStage() {
    }

    @Override
    public void enter() {
        stage = new Stage(new ScreenViewport());
        loseEffectActor = new LoseEffectActor();
        stage.addActor(loseEffectActor);
        Gdx.input.setInputProcessor(stage);

        playMusic("defeatedTheme");
    }

    @Override
    public void update(float delta) {
        stage.act(delta);

        if (loseEffectActor.isAnimationFinished()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                isFinished = true;
                isQuitRequested = false;
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
        private final Texture[] loseTextures = new Texture[3];

        private final Animation<TextureRegion>[] loseAnimation = new Animation[3];
        private int sceneNumber = 0;

        private final TextureRegion[][] loseFrames = new TextureRegion[3][];
        private TextureRegion currentFrame;
        private float stateTime = 0f;
        private boolean isAnimationFinished = false;

        public LoseEffectActor() {
            for (int i = 0; i < 3; i++) {
                loseTextures[i] = new Texture("Lose/" + "scene" + i + ".png");
            }

            for (int i = 0; i < 3; i++) {
                int maxFrames = loseTextures[i].getHeight() / SCREEN_HEIGHT;
                loseFrames[i] = new TextureRegion[maxFrames];
                for (int j = 0; j < maxFrames; j++) {
                    loseFrames[i][j] = new TextureRegion(loseTextures[i], 0, j * SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
                }
                loseAnimation[i] = new Animation<>(FRAME_DURATION * 1.5f, loseFrames[i]);
            }

            this.currentFrame = loseFrames[0][0];

            setPosition(0, 0);
            setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        }

        public boolean isAnimationFinished() {
            return isAnimationFinished;
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (!isAnimationFinished) {
                stateTime += delta;
                currentFrame = loseAnimation[sceneNumber].getKeyFrame(stateTime, false);
                if (loseAnimation[sceneNumber].isAnimationFinished(stateTime)) {
                    stateTime = 0f;
                    sceneNumber++;
                    if (sceneNumber >= loseAnimation.length) {
                        isAnimationFinished = true;
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
            for (Texture texture : loseTextures) {
                if (texture != null) {
                    texture.dispose();
                }
            }
        }
    }
}
