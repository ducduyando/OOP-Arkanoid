package io.github.arkanoid.stage;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.ui.Button;
import io.github.arkanoid.ui.Button.*;
import io.github.arkanoid.ui.ParallaxBackground;
import org.w3c.dom.Text;

public class
MenuStage implements GameStage {
    private Stage gdxStage;
    private ParallaxBackground menuBackground;
    private Button menuButton;

    private Texture[] menuTextures;

    @Override
    public void enter() {
        this.gdxStage = new Stage(new ScreenViewport());

        menuTextures = new Texture[4];
        for (int i = 0; i < 4; i++) {
            menuTextures[i] = new Texture("Menu/" + "layer" + i + ".png");
        }
        this.menuBackground = new ParallaxBackground(menuTextures, new float[] {0f, 100f, 0f, 0f}, false);

        this.menuButton = new Button();

        this.gdxStage.addActor(this.menuBackground);
        this.gdxStage.addActor(this.menuButton);
    }

    @Override
    public void update(float delta) {
        this.gdxStage.act(delta);
    }

    @Override
    public void exit() {
        this.menuBackground.dispose();
        this.menuButton.dispose();
        this.gdxStage.dispose();
    }

    @Override
    public Stage getGdxStage() {
        return this.gdxStage;
    }

    @Override
    public boolean isFinished() {
        return this.menuButton.isGameModeChosen();
    }

    public Button.Mode getSelectedMode() {
        return this.menuButton.getMode();
    }
}
