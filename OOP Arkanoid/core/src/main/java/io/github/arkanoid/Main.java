package io.github.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static io.github.arkanoid.Constants.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    Texture barImage;
    Texture ballImage;
    Texture bossHealthBarImage;

    Bar bar;
    Ball ball;
    HealthBar bossHealthBar;
    Button button;

    Boss1 boss1;

    GameLogic gameLogic;

    ParallaxBackground menuBackground;
    ParallaxBackground parallaxBackground;

    Stage stage;

    int gameState = 0;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());

        Texture[] menuTextures = new Texture[4];

        for (int i = 0; i < 4; i++) {
            menuTextures[i] = new Texture("menu_layer" + (i + 1) + ".png");
        }

        float[] menuSpeeds = new float[] {0f, 100f, 0f, 0f};

        menuBackground = new ParallaxBackground(menuTextures, menuSpeeds);

        Texture[] bgTextures = new Texture[5];

        for (int i = 0; i < 5; i++) {
            bgTextures[i] = new Texture("background_layer" + i + ".png");
        }

        float[] bgSpeeds = new float[] {0f, 50f, 40f, 30f, 20f};

        parallaxBackground =  new ParallaxBackground(bgTextures,bgSpeeds);

        barImage = new Texture("Bar.png");
        ballImage = new Texture("Ball.png");
        bossHealthBarImage = new Texture("HealthBar.png");

        bar = new Bar(barImage, 0, 0);
        ball = new Ball(ballImage, 0, 0);

        float bossInitialX = (SCREEN_WIDTH - BOSS1_WIDTH) / 2f;
        float bossInitialY = SCREEN_HEIGHT * 0.6f;

        boss1 = new Boss1("Boss1",bossInitialX, bossInitialY, 100);
        bossHealthBar = new HealthBar(bossHealthBarImage, boss1);
        button = new Button();
        gameLogic = new GameLogic(ball, bar, boss1);

        stage.addActor(menuBackground);
        stage.addActor(button);

    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        float delta = Gdx.graphics.getDeltaTime();

        if (gameState == 0) {
            if (button.isGameModeChosen()) {
                if (button.getMode() == Button.Mode.PLAY) {

                    gameState = 1;

                    button.remove();
                    menuBackground.remove();

                    stage.addActor(parallaxBackground);

                    stage.addActor(ball);
                    stage.addActor(bar);
                    stage.addActor(bossHealthBar);

                    stage.addActor(boss1);

                }
                else {
                    Gdx.app.exit();
                }
            }
        }

        if (gameState == 1) {
            gameLogic.launch();
            gameLogic.barCollision();
            gameLogic.boundaryCollision(delta);
            gameLogic.bossCollision();
            gameLogic.skillCollision(stage);
        }

        stage.act(delta);

        stage.draw();

    }

    @Override
    public void dispose() {
        barImage.dispose();
        ballImage.dispose();
        bossHealthBarImage.dispose();
        menuBackground.dispose();
        button.dispose();
        boss1.dispose();
        parallaxBackground.dispose();
        stage.dispose();
    }
}
