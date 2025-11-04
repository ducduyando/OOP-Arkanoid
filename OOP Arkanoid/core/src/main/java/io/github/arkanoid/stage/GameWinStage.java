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

import static io.github.arkanoid.core.MusicResource.*;

import static io.github.arkanoid.core.Constants.*;

public class GameWinStage implements GameStage {
    private Stage stage;
    private WinEffectActor winEffectActor;
    private boolean isFinished = false;
    private boolean isQuitRequested = false;


    public GameWinStage() {
    }

    @Override
    public void enter() {
        stage = new Stage(new ScreenViewport());
        winEffectActor = new WinEffectActor();
        stage.addActor(winEffectActor);
        Gdx.input.setInputProcessor(stage);

        VICTORY_THEME.play();
    }
    @Override
    public void update(float delta) {
        stage.act(delta);

        if (winEffectActor.isAnimationFinished()) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                isFinished = true;
                isQuitRequested = false;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void exit() {
        if (winEffectActor != null) {
            winEffectActor.dispose();
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
        if (winEffectActor != null) {
            winEffectActor.drawDirect(batch);
        }
    }

    private static class WinEffectActor extends Actor {
        private final Texture[] winTextures = new Texture[3];

        private final Animation<TextureRegion>[] winAnimation = new Animation[3];
        private int sceneNumber = 0;

        private final TextureRegion[][] winFrames = new TextureRegion[3][];
        private TextureRegion currentFrame;
        private float stateTime = 0f;
        private boolean isAnimationFinished = false;

        public WinEffectActor() {
            for (int i = 0; i < 3; i++) {
                winTextures[i] = new Texture("Win/" + "scene" + i + ".png");
            }

            for (int i = 0; i < 3; i++) {
                int maxFrames = winTextures[i].getHeight() / SCREEN_HEIGHT;
                winFrames[i] = new TextureRegion[maxFrames];
                for (int j = 0; j < maxFrames; j++) {
                    winFrames[i][j] =  new TextureRegion(winTextures[i], 0, j * SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
                }
                winAnimation[i] = new Animation<>(FRAME_DURATION * 1.5f, winFrames[i]);
            }

            this.currentFrame = winFrames[0][0];

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
                currentFrame = winAnimation[sceneNumber].getKeyFrame(stateTime, false);
                if (winAnimation[sceneNumber].isAnimationFinished(stateTime)) {
                    stateTime = 0f;
                    sceneNumber++;
                    if (sceneNumber >= winAnimation.length) {
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
            for (Texture texture : winTextures) {
                if (texture != null) {
                    texture.dispose();
                }
            }
        }
    }
 }
