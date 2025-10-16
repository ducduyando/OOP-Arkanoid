package io.github.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
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
    LoadingStage loadingStage;
    int stageNumber = 0; // Tạo số đếm chỉ phần tử trong mảng Stages, thay đổi khi qua màn.
    Boss1 boss1;
    GameLogic gameLogic;

    ParallaxBackground menuBackground;
    ParallaxBackground parallaxBackground;
    PauseMenu pauseMenu;

    PowerUpMenu powerUpMenu;

    Texture[] Stages = new Texture[1]; // Tạo mảng lưu stage textures.
    Texture[] powerUpMenuList = new Texture[1];
    Stage stage;

    double gameState = 0;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());

        Texture[] menuTextures = new Texture[4];

        for (int i = 0; i < 4; i++) {
            menuTextures[i] = new Texture( "menu/" + "layer" + i + ".png");
        }

        float[] menuSpeeds = new float[] {0f, 100f, 0f, 0f};

        menuBackground = new ParallaxBackground(menuTextures, menuSpeeds);

        Texture[] bgTextures = new Texture[5];

        for (int i = 0; i < 5; i++) {
            bgTextures[i] = new Texture(  "background/" + "layer" + i + ".png");
        }

        float[] bgSpeeds = new float[] {0f, 50f, 40f, 30f, 20f};

        parallaxBackground =  new ParallaxBackground(bgTextures,bgSpeeds);

        pauseMenu = new PauseMenu();


        barImage = new Texture("Bar.png");
        ballImage = new Texture("Ball.png");
        bossHealthBarImage = new Texture("HealthBar.png");

        bar = new Bar(barImage, 0, 0);
        ball = new Ball(ballImage, 0, 0);

        for (int i = 0; i < Stages.length; i++) {
            Stages[i] = new Texture("stages/stage" + (i + 1) + ".png");// Thêm các textures vào mảng Stages.
            powerUpMenuList[i] = new Texture("brick/" + "blue.png"); // Truyen tam thoi hihi.

        }

        float bossInitialX = (SCREEN_WIDTH - BOSS1_WIDTH) / 2f;
        float bossInitialY = SCREEN_HEIGHT * 0.6f;

        boss1 = new Boss1(1,bossInitialX, bossInitialY, 100);
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
            if (button.isGameModeChosen() && button.getMode() == Button.Mode.PLAY) {
                gameState = 0.5;

                loadingStage = new LoadingStage(Stages[stageNumber]);

                button.remove();
                menuBackground.remove();

                stage.addActor(loadingStage);
            } else if (button.isGameModeChosen() && button.getMode() == Button.Mode.QUIT)  {
                Gdx.app.exit();
            }
        }

        else if (gameState == 0.5) {
            loadingStage.act(delta);
            stage.draw();

            if (loadingStage.getProcess() == LoadingStage.Process.DONE) {
                gameState = 1;

                stage.addActor(parallaxBackground);

                stage.addActor(ball);
                stage.addActor(bar);
                stage.addActor(bossHealthBar);

                stage.addActor(boss1);

                loadingStage.remove();
                loadingStage.dispose();
            }
        }

        else if (gameState == 1) {
            if (boss1.isDead() && boss1.isReadyToDeath) {
                powerUpMenu = new PowerUpMenu(powerUpMenuList[stageNumber]); // thu xem day co duoc khong nha
                gameState = 3;
                powerUpMenu.reset();
                stage.addActor(powerUpMenu);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.P) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                gameState = 2;
                pauseMenu.reset();
                stage.addActor(pauseMenu);
            }
            gameLogic.launch();
            gameLogic.barCollision();
            gameLogic.boundaryCollision(delta);
            gameLogic.bossCollision();
            gameLogic.skillCollision(stage);
        }
        else if (gameState == 2) {
            // Unpause
            if (Gdx.input.isKeyJustPressed(Input.Keys.P) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                gameState = 1;
                pauseMenu.remove();
            }

            if (pauseMenu.isOptionChosen()) {
                if (pauseMenu.getOption() == PauseMenu.Option.RESUME) {
                    gameState = 1;
                    pauseMenu.remove();
                } else {
                    Gdx.app.exit();
                }
            }
        }
        else if (gameState == 3) {
            powerUpMenu.act(delta);

            if (powerUpMenu.isOptionChosen()) {
                if (powerUpMenu.getOption() == PowerUpMenu.Option.SKILL1) {
                    ball.setDamage(20);
                } else {

                }
                gameState = 0.5;
                powerUpMenu.remove();
            }
        }

        if (gameState == 2) {

            pauseMenu.act(delta);

            stage.draw();

        } else {

            stage.act(delta);
            stage.draw();
        }



    }

    @Override
    public void dispose() {
        barImage.dispose();
        ballImage.dispose();
        bossHealthBarImage.dispose();
        menuBackground.dispose();
        button.dispose();
        loadingStage.dispose();
        boss1.dispose();
        parallaxBackground.dispose();
        pauseMenu.dispose();
        powerUpMenu.dispose();
        stage.dispose();
    }
}
