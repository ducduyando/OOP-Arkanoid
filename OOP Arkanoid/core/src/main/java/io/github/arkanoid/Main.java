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

    Boss1 boss1;
    Texture boss1Image;
    Texture boss1TakeDamage;
    Texture boss1Skill1Image;
    Texture boss1Skill2Image;

    GameLogic gameLogic;

    ParallaxBackground parallaxBackground;

    Stage stage;

    Menu menu;
    int gameState = 0;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());

        Texture[] bgTextures = new Texture[5];

        bgTextures[0] = new Texture("background_layer0.png");
        bgTextures[1] = new Texture("background_layer1.png");
        bgTextures[2] = new Texture("background_layer2.png");
        bgTextures[3] = new Texture("background_layer3.png");
        bgTextures[4] = new Texture("background_layer4.png");

        float[] bgSpeeds = new float[] {0f, 20f, 30f, 40f, 50f};

        parallaxBackground =  new ParallaxBackground(bgTextures,bgSpeeds);

        stage.addActor(parallaxBackground);

        barImage = new Texture("Bar.png");
        ballImage = new Texture("Ball.png");
        bossHealthBarImage = new Texture("HealthBar.png");

        boss1Image = new Texture("Boss1.png");
        boss1TakeDamage = new Texture("Boss1TakeDamage.png");
        boss1Skill1Image = new Texture("Boss1_Skill1.png");
        boss1Skill2Image = new Texture("Boss1_Skill2.png");
        bar = new Bar(barImage, 0, 0);
        ball = new Ball(ballImage, 0, 0);

        float bossInitialX = (SCREEN_WIDTH - BOSS1_WIDTH) / 2f;
        float bossInitialY = SCREEN_HEIGHT * 0.6f;

        boss1 = new Boss1(boss1Image, boss1TakeDamage, boss1Skill1Image, boss1Skill2Image,bossInitialX, bossInitialY, 100);
        bossHealthBar = new HealthBar(bossHealthBarImage, boss1);
        gameLogic = new GameLogic(ball, bar, boss1);

        stage.addActor(ball);
        stage.addActor(bar);
        stage.addActor(bossHealthBar);

        stage.addActor(boss1);

        menu = new Menu();

    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        if (gameState == 0) {
            int result = menu.showMenu();
            if (result == 0) {
                gameState = 1; // New Game
            } else if (result == 1) {
                Gdx.app.exit();
            }
            return;
        }

        float delta = Gdx.graphics.getDeltaTime();

        gameLogic.launch();
        gameLogic.barCollision();
        gameLogic.boundaryCollision(delta);
        gameLogic.bossCollision();
        gameLogic.skillCollision(stage);

        stage.act(delta);

        stage.draw();

    }

    @Override
    public void dispose() {
        barImage.dispose();
        ballImage.dispose();
        bossHealthBarImage.dispose();
        boss1Image.dispose();
        boss1Skill1Image.dispose();
        boss1Skill2Image.dispose();
        parallaxBackground.dispose();
        stage.dispose();
        menu.dispose();
    }
}
