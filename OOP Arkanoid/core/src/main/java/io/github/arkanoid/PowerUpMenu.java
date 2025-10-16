package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.Constants.*;


public class PowerUpMenu extends Actor {
    private final Texture powerUpBackground = new Texture("background/" + "layer0.png");
    private final Texture powerUpButton;

    private TextureRegion currentFrame;
    private float stateTime = 0f;

    protected enum Option { SKILL1, SKILL2 }
    private Option option = Option.SKILL1;
    private boolean isOptionChosen = false;

    private final Animation<TextureRegion> skill1Animation;
    private final Animation<TextureRegion> skill2Animation;

    public PowerUpMenu(Texture powerUpButton) {
        this.powerUpButton = powerUpButton;

        TextureRegion[] skill1Frames = new TextureRegion[2];
        TextureRegion[] skill2Frames = new TextureRegion[2];

        for(int i = 0; i < 2; i++) {
            skill1Frames[i] = new TextureRegion(powerUpButton, i * POWER_UP_WIDTH, 0,
                POWER_UP_WIDTH, POWER_UP_HEIGHT);

        }
        for (int i = 0; i < 2; i++) {
            skill2Frames[i] = new TextureRegion(
                powerUpButton,
                i * POWER_UP_WIDTH, POWER_UP_HEIGHT,
                POWER_UP_WIDTH, POWER_UP_HEIGHT);
        }

        this.skill1Animation = new Animation<>(FRAME_DURATION * 3, skill1Frames);
        this.skill2Animation= new Animation<>(FRAME_DURATION * 3, skill2Frames);

        this.currentFrame = skill1Frames[0];

        setPosition(
            (SCREEN_WIDTH - POWER_UP_WIDTH) / 2f,
            (SCREEN_HEIGHT - POWER_UP_HEIGHT) / 2f
        );

        setSize(POWER_UP_WIDTH, POWER_UP_HEIGHT);


    }

    public boolean isOptionChosen() {
        return isOptionChosen;
    }

    public Option getOption() {
        return option;
    }

    public void act(float delta) {
        stateTime += delta;

        if (option == Option.SKILL1) {
            currentFrame = skill1Animation.getKeyFrame(stateTime, true);
        } else if (option == Option.SKILL2) {
            currentFrame = skill2Animation.getKeyFrame(stateTime, true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
            || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            isOptionChosen = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)
            || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
            || Gdx.input.isKeyJustPressed(Input.Keys.W)
            || Gdx.input.isKeyJustPressed(Input.Keys.S)
            || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
            || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
            || Gdx.input.isKeyJustPressed(Input.Keys.A)
            || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            option = (option == PowerUpMenu.Option.SKILL1) ? PowerUpMenu.Option.SKILL2 : PowerUpMenu.Option.SKILL1;
        }
    }
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(powerUpBackground, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }

    public void reset() {
        isOptionChosen = false;
        option = PowerUpMenu.Option.SKILL1;
        stateTime = 0f;
    }

    public void dispose() {
        powerUpBackground.dispose();
        powerUpButton.dispose();
    }
}
