package io.github.arkanoid.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;

public interface GameStage {
    void enter();
    void update(float delta);
    void exit();

    Stage getGdxStage();
    boolean isFinished();
}
