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
    BrickStage brickStage;

    Bar_Stage1_Skill2 barStage1Skill2;

    Texture[] stageTextures = new Texture[2]; // Tạo mảng lưu stage textures.
    Texture[] chooseSkill = new Texture[1];
    Stage stage;

    double gameState = 0;
    boolean paused = false;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

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



        barImage = new Texture("Bar" + ".png");
        ballImage = new Texture("ball/" + "normal" + ".png");
        bossHealthBarImage = new Texture("HealthBar.png");

        bar = new Bar(barImage, 0, 0);
        ball = new Ball(ballImage, 0, 0);

        stageTextures[0] = new Texture("stages/" + "stage" + 0 + ".png");
        for (int i = 1; i < stageTextures.length; i++) {
            stageTextures[i] = new Texture("stages/" + "stage" + i + ".png"); // Thêm các textures vào mảng Stages.
            chooseSkill[i - 1] = new Texture("powerUp/" + "layer" + (i - 1) + ".png");
        }

        powerUpMenu = new PowerUpMenu(chooseSkill[stageNumber]);

        float bossInitialX = (SCREEN_WIDTH - BOSS1_WIDTH) / 2f;
        float bossInitialY = SCREEN_HEIGHT * 0.6f;

        boss1 = new Boss1(1,bossInitialX, bossInitialY, 100);
        bossHealthBar = new HealthBar(bossHealthBarImage, boss1);

        button = new Button();
        gameLogic = new GameLogic(ball, bar, boss1);
        stage.addActor(menuBackground);
        stage.addActor(button);

        barStage1Skill2 = new Bar_Stage1_Skill2(bar);

    }

    @Override
    public void render() {

        ScreenUtils.clear(0, 0, 1, 1);
        float delta = Gdx.graphics.getDeltaTime();
        if (gameState == 0) {
            if (button.isGameModeChosen()) {
                if (button.getMode() == Button.Mode.PLAY) {
                    gameState = 0.5;
                    loadingStage = new LoadingStage(stageTextures[stageNumber]);
                    button.remove();
                    menuBackground.remove();
                    stage.addActor(loadingStage);
                } else if (button.getMode() == Button.Mode.QUIT) {
                    Gdx.app.exit();
                } else {
                    button.resetChoice();
                }
            }
        }

        if (gameState == 0.5) { // Loading to brick stage
            if (loadingStage != null) {
                loadingStage.act(delta);
                stage.draw();

                if (loadingStage.getProcess() == LoadingStage.Process.DONE) {
                    loadingStage.remove();
                    loadingStage.dispose();
                    loadingStage = null;

                    brickStage = new BrickStage();
                    gameState = 1; // enter brick stage
                }
            }
        }
        else if (gameState == 1) { // Brick gameplay
            if (!paused) {
                if (brickStage != null) {
                    brickStage.act(delta);
                    brickStage.draw();
                    if (brickStage.isFinished()) {
                        gameState = 1.5; // prepare to boss
                        brickStage.dispose();
                        brickStage = null;
                        stageNumber++;
                        loadingStage = new LoadingStage(stageTextures[stageNumber]);
                        stage.addActor(loadingStage);
                    }
                }
            }
            // pause
            if (Gdx.input.isKeyJustPressed(Input.Keys.P) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                paused = !paused;
                if (paused) { pauseMenu.reset(); stage.addActor(pauseMenu); } else { pauseMenu.remove(); }
            }
        }
        else if (gameState == 1.5) { // Loading to boss stage
            if (loadingStage != null) {
                loadingStage.act(delta);
                stage.draw();
                if (loadingStage.getProcess() == LoadingStage.Process.DONE) {
                    loadingStage.remove();
                    loadingStage.dispose();
                    loadingStage = null;

                    stage.addActor(parallaxBackground);
                    stage.addActor(ball);
                    stage.addActor(bar);
                    stage.addActor(bossHealthBar);
                    stage.addActor(boss1);
                    gameState = 2; // enter boss
                }
            }
        }
        else if (gameState == 2) { // Boss gameplay
            if (!paused) {
                gameLogic.launch();
                gameLogic.barCollision();
                gameLogic.boundaryCollision(delta, UP_BOUNDARY);
                gameLogic.bossCollision();
                gameLogic.skillCollision(stage);

                if (boss1.isDead() && boss1.isReadyToDeath) {
                    paused = false;
                    powerUpMenu.reset();
                    stage.addActor(powerUpMenu);
                    gameState = 3;
                }
            }
            // pause toggle
            // pause toggle
            if (Gdx.input.isKeyJustPressed(Input.Keys.P) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                paused = !paused;
                if (paused) { pauseMenu.reset(); stage.addActor(pauseMenu); } else { pauseMenu.remove(); }
            }
        }
        else if (gameState == 3) {

            if (powerUpMenu.isOptionChosen()) {
                if (powerUpMenu.getOption() == PowerUpMenu.Option.SKILL1) {
                    ball.setDamage(20);
                } else {
                    barStage1Skill2 = new Bar_Stage1_Skill2(bar);

                }
                gameState = 0.5;
                powerUpMenu.remove();
            }
        }

        if (gameState == 3) {
            powerUpMenu.act(delta);

            stage.draw();
        } else if (paused) {
            pauseMenu.act(delta);
            // handle pause menu input
            if (pauseMenu.isOptionChosen()) {
                if (pauseMenu.getOption() == PauseMenu.Option.RESUME) {
                    paused = false;
                    pauseMenu.remove();
                } else {
                    Gdx.app.exit();
                }
            }
            stage.draw();
        } else {

            stage.act(delta);
            stage.draw();
        }
        }

    public boolean isPaused() {
        return paused;
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
