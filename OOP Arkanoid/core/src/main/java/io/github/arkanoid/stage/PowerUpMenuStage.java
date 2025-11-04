package io.github.arkanoid.stage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.ui.PowerUpMenu;

public class PowerUpMenuStage implements GameStage {
    private Stage gdxStage;
    private PowerUpMenu powerUpMenu;
    private Texture powerUpMenuTexture;

    private int powerUpNumber = 0;

    public void setPowerUpNumber(int powerUpNumber) {
        this.powerUpNumber = powerUpNumber;
    }

    @Override
    public void enter() {
        this.gdxStage = new Stage(new ScreenViewport());

        this.powerUpMenuTexture = new Texture("PowerUp/" + "layer" + powerUpNumber + ".png");
        this.powerUpMenu = new PowerUpMenu(powerUpMenuTexture);

        this.gdxStage.addActor(this.powerUpMenu);
    }

    @Override
    public void update(float delta) {
        this.gdxStage.act(delta);
    }

    @Override
    public void exit() {
        this.powerUpMenuTexture.dispose();
        this.powerUpMenu.dispose();
        this.gdxStage.dispose();
    }

    @Override
    public Stage getGdxStage() {
        return this.gdxStage;
    }

    @Override
    public boolean isFinished() {
        return this.powerUpMenu.isOptionChosen();
    }

    public PowerUpMenu.Option getSelectedOption() {
        return this.powerUpMenu.getOption();
    }
}
