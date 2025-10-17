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
    enum GameState {
        MENU,
        LOADING_TO_TUTORIAL,
        TUTORIAL,
        LOADING_TO_STAGE_1,
        STAGE_1,
        LOADING_TO_STAGE_2,
        STAGE_2,
        POWER_UP_MENU
    }

    private GameState gameState = GameState.MENU;
    private boolean isPaused = false;

    private Stage stage;

    /** Textures. */
    private Texture barImage;
    private Texture ballImage;
    private Texture bossHealthBarImage;
    private Texture barStage1Skill1Image;
    private final Texture[] stageTextures = new Texture[2]; // Tạo mảng lưu stage textures.
    private final Texture[] chooseSkill = new Texture[stageTextures.length - 1];

    /** Game Object. */
    private Bar bar;
    private Ball ball;
    private Boss1 boss1;
    private GameLogic gameLogic;

    /** UI & Stages. */
    private HealthBar bossHealthBar;
    private Button button;
    private LoadingStage loadingStage;
    private ParallaxBackground menuBackground;
    private ParallaxBackground parallaxBackground;
    private PauseMenu pauseMenu;
    private PowerUpMenu powerUpMenu;
    private Tutorial tutorial;

    /** Skills and stages. */
    private Bar_Stage1_Skill1 barStage1Skill1;
    private Bar_Stage1_Skill2 barStage1Skill2;
    private int stageNumber = 0;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // --Assets loading--
        Texture[] menuTextures = new Texture[4];
        for (int i = 0; i < 4; i++) {
            menuTextures[i] = new Texture( "menu/" + "layer" + i + ".png");
        }
        menuBackground = new ParallaxBackground(menuTextures, new float[] {0f, 100f, 0f, 0f});

        Texture[] bgTextures = new Texture[5];
        for (int i = 0; i < 5; i++) {
            bgTextures[i] = new Texture(  "background/" + "layer" + i + ".png");
        }
        parallaxBackground =  new ParallaxBackground(bgTextures,new float[] {0f, 50f, 40f, 30f, 20f});

        barImage = new Texture("Bar.png");
        ballImage = new Texture("ball/normal.png");
        bossHealthBarImage = new Texture("HealthBar.png");
        barStage1Skill1Image = new Texture("ball/" + "upgrade" + ".png");

        stageTextures[0] = new Texture("stages/" + "stage" + 0 + ".png");
        for (int i = 1; i < stageTextures.length; i++) {
            stageTextures[i] = new Texture("stages/" + "stage" + i + ".png");
            if (i - 1 < chooseSkill.length) {
                chooseSkill[i - 1] = new Texture("powerUp/" + "layer" + (i - 1) + ".png");
            }
        }

        // --Object initialization--
        bar = new Bar(barImage, 0, 0);
        ball = new Ball(ballImage, 0, 0);

        float bossInitialX = (SCREEN_WIDTH - BOSS1_WIDTH) / 2f;
        float bossInitialY = SCREEN_HEIGHT * 0.6f;
        boss1 = new Boss1(1,bossInitialX, bossInitialY, 100);
        bossHealthBar = new HealthBar(bossHealthBarImage, boss1);

        gameLogic = new GameLogic(bar, boss1);

        // --UI initialization--
        pauseMenu = new PauseMenu();
        if (stageNumber < chooseSkill.length) {
            powerUpMenu = new PowerUpMenu(chooseSkill[stageNumber]);
        }
        button = new Button();

        // --First stage initialization--
        stage.addActor(menuBackground);
        stage.addActor(button);
        gameState = GameState.MENU;
    }

    @Override
    public void render() {

        ScreenUtils.clear(0, 0, 1, 1);
        float delta = Gdx.graphics.getDeltaTime();

        // --Menu handle--
        if (gameState == GameState.TUTORIAL || gameState == GameState.STAGE_1) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.P) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                isPaused = !isPaused;
                if (isPaused) {
                    pauseMenu.reset();
                    stage.addActor(pauseMenu);
                } else {
                    pauseMenu.remove();
                }
            }
        }

        if (!isPaused) {
            switch (gameState) {
                case MENU:
                    if (button.isGameModeChosen()) {
                        if (button.getMode() == Button.Mode.PLAY) {
                            button.remove();
                            menuBackground.remove();

                            gameState = GameState.LOADING_TO_TUTORIAL;
                            loadingStage = new LoadingStage(stageTextures[stageNumber]);
                            stage.addActor(loadingStage);
                        } else if (button.getMode() == Button.Mode.QUIT) {
                            Gdx.app.exit();
                        } else {
                            button.resetChoice();
                        }
                    }
                    break;

                case LOADING_TO_TUTORIAL:
                    if (loadingStage.getState() == LoadingStage.State.DONE) {
                        loadingStage.remove();
                        loadingStage = null;
                        tutorial = new Tutorial();
                        gameState = GameState.TUTORIAL;
                    }
                    break;

                case TUTORIAL:
                    if (tutorial != null) {
                        tutorial.act(delta);
                        tutorial.draw();
                        if (tutorial.isFinished()) {
                            stageNumber++;
                            gameState = GameState.LOADING_TO_STAGE_1;
                            loadingStage = new LoadingStage(stageTextures[stageNumber]);
                            stage.addActor(loadingStage);
                        }
                    }
                    break;

                case LOADING_TO_STAGE_1:
                    if (loadingStage.getState() == LoadingStage.State.DONE) {
                        stage.addActor(parallaxBackground);
                        stage.addActor(ball);
                        stage.addActor(bar);
                        stage.addActor(bossHealthBar);
                        stage.addActor(boss1);
                        loadingStage.remove();
                        loadingStage = null;
                        gameState = GameState.STAGE_1;
                    }
                    break;

                case STAGE_1:
                    gameLogic.launch(ball);
                    gameLogic.barCollision(ball);
                    gameLogic.boundaryCollision(ball, delta, UP_BOUNDARY);
                    gameLogic.bossCollision(ball);
                    gameLogic.skillCollision(stage);

                    if (boss1.isDead() && boss1.isReadyToDeath) {
                        gameState = GameState.POWER_UP_MENU;
                        powerUpMenu.reset();
                        stage.addActor(powerUpMenu);
                    }
                    break;

                case LOADING_TO_STAGE_2:
                    break;

                case STAGE_2:
                    if (barStage1Skill1 != null) {
                        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)
                            && !barStage1Skill1.isLaunched()
                            && barStage1Skill1.isSkill1Ready()) {

                            gameLogic.launch(barStage1Skill1);
                            gameLogic.barCollision(barStage1Skill1);
                            gameLogic.boundaryCollision(barStage1Skill1, delta, UP_BOUNDARY);
                            gameLogic.bossCollision(barStage1Skill1);

                        }
                    }
                    else if (barStage1Skill2 != null) {
                        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)
                            && barStage1Skill2.isSkill2Ready()) {
                            barStage1Skill2.enter(bar);
                        }

                        if (barStage1Skill2.isFiring()) {

                        }

                        if (!barStage1Skill2.isDone()) {
                            barStage1Skill2.update(bar, delta);
                        }
                    }
                    gameLogic.launch(ball);
                    gameLogic.barCollision(ball);
                    gameLogic.boundaryCollision(ball, delta, UP_BOUNDARY);
                    gameLogic.bossCollision(ball);
                    gameLogic.skillCollision(stage);
                    break;

                case POWER_UP_MENU:
                    if (powerUpMenu.isOptionChosen()) {
                        if (powerUpMenu.getOption() == PowerUpMenu.Option.SKILL1) {
                            barStage1Skill1 = new Bar_Stage1_Skill1(barStage1Skill1Image,
                                bar.getX(), bar.getY());
                        } else {
                            barStage1Skill2 = new Bar_Stage1_Skill2(bar);
                        }

                        // CẢI THIỆN LOGIC: Sau khi chọn skill, nên chuyển sang màn gạch tiếp theo
                        gameState = GameState.MENU;
                        loadingStage = new LoadingStage(stageTextures[stageNumber]);
                        stage.addActor(loadingStage);

                        // Xóa các actor của màn boss cũ
                        parallaxBackground.remove();
                        ball.remove();
                        bar.remove();
                        bossHealthBar.remove();
                        boss1.remove();
                        powerUpMenu.remove();
                    }
                    break;
            }
        } else {
            if (pauseMenu.isOptionChosen()) {
                if (pauseMenu.getOption() == PauseMenu.Option.RESUME) {
                    isPaused = false;
                    pauseMenu.remove();
                } else {
                    Gdx.app.exit();
                }
            }
        }

        // --- Cập nhật và vẽ toàn bộ Stage ---
        stage.act(delta);
        stage.draw();
    }

    public boolean isPaused() {
        return isPaused;
    }


    @Override
    public void dispose() {


        if (stage != null) stage.dispose();

        if (barImage != null) barImage.dispose();
        if (ballImage != null) ballImage.dispose();
        if (bossHealthBarImage != null) bossHealthBarImage.dispose();
        if (barStage1Skill1Image != null) barStage1Skill1Image.dispose();

        if (menuBackground != null) menuBackground.dispose();
        if (parallaxBackground != null) parallaxBackground.dispose();

        if (button != null) button.dispose();
        if (pauseMenu != null) pauseMenu.dispose();
        if (powerUpMenu != null) powerUpMenu.dispose();

        // SỬA: Thêm dispose cho brickStage nếu nó chưa được dispose
        if (tutorial != null) tutorial.dispose();
        if (loadingStage != null) loadingStage.dispose();

        if (boss1 != null) boss1.dispose();

        // SỬA: Duyệt qua mảng để dispose từng texture, tránh memory leak
        if (stageTextures != null) {
            for (Texture texture : stageTextures) {
                if (texture != null) texture.dispose();
            }
        }
        if (chooseSkill != null) {
            for (Texture texture : chooseSkill) {
                if (texture != null) texture.dispose();
            }
        }
    }
}
