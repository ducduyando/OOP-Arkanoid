package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

import static io.github.arkanoid.Constants.*;

public class LoadingStage extends Actor {
    protected enum Process {
        LOADING,
        DONE
    }
    private float stateTime = 0f;
    private TextureRegion currentFrame;
    private final Animation<TextureRegion> loading;
    Texture stage;
    Texture loadingBar = new Texture("stages/" + "loading.png");
    private Process process;
    private final int maxFrameLoading = loadingBar.getHeight() / LOADING_HEIGHT;
    TextureRegion[] loadFrames = new TextureRegion[maxFrameLoading];
    LoadingStage(Texture stage) {
        this.stage = stage;
        process = Process.LOADING;
        for (int i = 0; i < maxFrameLoading; i++) {
            loadFrames[i] = new TextureRegion(loadingBar, 0,  i * LOADING_HEIGHT, LOADING_WIDTH, LOADING_HEIGHT);
        }
        this.currentFrame = loadFrames[1];
        this.loading = new Animation<>(FRAME_DURATION * 3, loadFrames);
    }
    public Process getProcess() {
        return process;
    }

    @Override
    public void act(float delta) {
        stateTime += delta;
        if (process == Process.LOADING) {
            currentFrame = loading.getKeyFrame(stateTime, false);
        }
        if (loading.isAnimationFinished(stateTime)) {
            process = Process.DONE;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int x = (SCREEN_WIDTH - LOADING_WIDTH) / 2;
        int y = SCREEN_HEIGHT / 2 - LOADING_HEIGHT;
        batch.draw(stage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.draw(currentFrame, x, y, LOADING_WIDTH, LOADING_HEIGHT);
    }

    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (loadingBar != null) {
            loadingBar.dispose();
        }
    }
}
