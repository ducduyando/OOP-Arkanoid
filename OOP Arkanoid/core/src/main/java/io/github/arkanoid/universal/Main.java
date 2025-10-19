package io.github.arkanoid.universal;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.boss1.Boss1;
import io.github.arkanoid.paddle.Paddle;
import io.github.arkanoid.paddle.PaddleSkill1A;
import io.github.arkanoid.paddle.PaddleSkill1B;

import static io.github.arkanoid.universal.Constants.*;

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
    private Texture paddleImage;
    private Texture ballImage;
    private Texture bossHealthBarImage;
    private Texture paddleSkill1AImage;
    private final Texture[] stageTextures = new Texture[2]; // Tạo mảng lưu stage textures.
    private final Texture[] chooseSkill = new Texture[stageTextures.length - 1];

    /** Game Object. */
    private Paddle paddle;
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
    private PaddleSkill1A paddleSkill1A;
    private PaddleSkill1B paddleSkill1B;
    private int stageNumber = 0;

    /**Save. */
    private int loadedBricksRemaining = -1;
    private boolean isLoadedFromSave = false;
    private boolean delayTutorialCheck = false;

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

        paddleImage = new Texture("universal/" + "paddle" + ".png");
        ballImage = new Texture("ball/" + "normal" + ".png");
        bossHealthBarImage = new Texture("universal/" + "health_bar" + ".png");
        paddleSkill1AImage = new Texture("ball/" + "upgrade" + ".png");

        stageTextures[0] = new Texture("stages/" + "stage" + 0 + ".png");
        for (int i = 1; i < stageTextures.length; i++) {
            stageTextures[i] = new Texture("stages/" + "stage" + i + ".png");
            if (i - 1 < chooseSkill.length) {
                chooseSkill[i - 1] = new Texture("powerUp/" + "layer" + (i - 1) + ".png");
            }
        }

        // --Object initialization--
        paddle = new Paddle(paddleImage, (SCREEN_WIDTH - PADDLE_WIDTH) / 2f, 150);
        ball = new Ball(ballImage, 0, 0);

        float bossInitialX = (SCREEN_WIDTH - BOSS1_WIDTH) / 2f;
        float bossInitialY = SCREEN_HEIGHT * 0.6f;
        boss1 = new Boss1(1,bossInitialX, bossInitialY, 100);
        bossHealthBar = new HealthBar(bossHealthBarImage, boss1);

        gameLogic = new GameLogic(paddle, boss1);

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
                    Gdx.input.setInputProcessor(stage);
                } else {
                    pauseMenu.remove();
                    Gdx.input.setInputProcessor(stage);
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
                        }
                        else if(button.getMode() == Button.Mode.LOAD){
                            if (Save.hasSave()) {
                                Save.SaveData data = Save.loadGame();
                                stageNumber = data.stageNumber;
                                boss1.setHp(data.bossHP);
                                boss1.setPosition(data.bossX, data.bossY);

                                paddle.setHealth(data.paddleHP);
                                paddle.setPosition(data.paddleX, data.paddleY);
                                ball.setPosition(data.ballX, data.ballY);
                                ball.setVelocity(data.ballVelX, data.ballVelY);
                                ball.setLaunched(data.ballLaunched);
                                loadedBricksRemaining = data.bricksRemaining;
                                isLoadedFromSave = true;
                                button.remove();
                                menuBackground.remove();
                                if (stageNumber == 0 && data.bricksRemaining > 0) {
                                    gameState = GameState.LOADING_TO_TUTORIAL;
                                } else {
                                    // Đảm bảo stage number ít nhất là 1 khi vào boss fight
                                    if (stageNumber == 0) stageNumber = 1;
                                    gameState = GameState.LOADING_TO_STAGE_1;
                                }
                                loadingStage = new LoadingStage(stageTextures[stageNumber]);
                                stage.addActor(loadingStage);
                            }

                            else {
                                Gdx.app.log("Save", "No save found!");
                                button.resetChoice();
                            }
                        }
                        else if (button.getMode() == Button.Mode.QUIT) {
                            Gdx.app.exit();
                        }
                    }
                    break;

                case LOADING_TO_TUTORIAL:
                    if (loadingStage.getState() == LoadingStage.State.DONE) {
                        loadingStage.remove();
                        loadingStage = null;
                        tutorial = new Tutorial(paddle, ball);
                        // luu lai so gach duoc luu tu save
                        if (isLoadedFromSave && loadedBricksRemaining >= 0) {
                            Save.SaveData saveData = Save.loadGame();
                            if (saveData != null && saveData.brickPositions != null && !saveData.brickPositions.isEmpty()) {
                                tutorial.resetBricksWithPositions(saveData.brickPositions);
                            } else {
                                tutorial.resetBricks(loadedBricksRemaining);
                            }
                            delayTutorialCheck = true;
                            loadedBricksRemaining = -1;
                            isLoadedFromSave = false;
                        }
                        gameState = GameState.TUTORIAL;
                    }
                    break;

                case TUTORIAL:
                    if (tutorial != null) {
                        tutorial.act(delta);
                        tutorial.draw();

                        // Nếu được load từ save và tutorial đã hoàn thành, không tự động chuyển stage
                        if (delayTutorialCheck) {
                            delayTutorialCheck = false;
                            if (tutorial.isFinished()) {
                                // Nếu tutorial đã hoàn thành từ save, chuyển sang stage 1
                                stageNumber++;
                                gameState = GameState.LOADING_TO_STAGE_1;
                                loadingStage = new LoadingStage(stageTextures[stageNumber]);
                                stage.addActor(loadingStage);
                            }
                            break;
                        }

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
                        if (!isLoadedFromSave) {
                            paddle.setHealth(3);
                            paddle.setPosition(0, 0);
                            ball.setPosition(0, 0);
                            ball.resetLaunch();
                        }
                        isLoadedFromSave = false;
                        stage.addActor(parallaxBackground);
                        stage.addActor(ball);
                        stage.addActor(paddle);
                        stage.addActor(bossHealthBar);
                        stage.addActor(boss1);
                        loadingStage.remove();
                        loadingStage = null;
                        gameState = GameState.STAGE_1;
                    }
                    break;

                case STAGE_1:
                    gameLogic.launch(ball);
                    gameLogic.paddleCollision(ball);
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
                    if (paddleSkill1A != null) {
                        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)
                            && !paddleSkill1A.isLaunched()
                            && paddleSkill1A.isSkill1Ready()) {

                            gameLogic.launch(paddleSkill1A);
                            gameLogic.paddleCollision(paddleSkill1A);
                            gameLogic.boundaryCollision(paddleSkill1A, delta, UP_BOUNDARY);
                            gameLogic.bossCollision(paddleSkill1A);

                        }
                    }
                    else if (paddleSkill1B != null) {
                        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)
                            && paddleSkill1B.isSkill2Ready()) {
                            paddleSkill1B.enter(paddle);
                        }

                        if (paddleSkill1B.isFiring()) {
                            gameLogic.paddleLaserCollision(paddleSkill1B);
                        }

                        if (!paddleSkill1B.isDone()) {
                            paddleSkill1B.update(paddle, delta);
                        }

                        if (paddleSkill1B.isDone()) {
                            paddleSkill1B.cleanup();
                        }
                    }
                    gameLogic.launch(ball);
                    gameLogic.paddleCollision(ball);
                    gameLogic.boundaryCollision(ball, delta, UP_BOUNDARY);
                    gameLogic.bossCollision(ball);
                    gameLogic.skillCollision(stage);
                    break;

                case POWER_UP_MENU:
                    if (powerUpMenu.isOptionChosen()) {
                        if (powerUpMenu.getOption() == PowerUpMenu.Option.SKILL1) {
                            paddleSkill1A = new PaddleSkill1A(paddleSkill1AImage, 0, 0);
                            paddleSkill1B = null;
                            Gdx.app.log("PowerUp", "Skill 1 selected");
                        } else {
                            paddleSkill1B = new PaddleSkill1B(paddle);
                            paddleSkill1A = null;
                            Gdx.app.log("PowerUp", "Skill 2 selected");
                        }

                        // CẢI THIỆN LOGIC: Sau khi chọn skill, nên chuyển sang màn gạch tiếp theo
                        gameState = GameState.MENU;
                        loadingStage = new LoadingStage(stageTextures[stageNumber]);
                        stage.addActor(loadingStage);

                        // Xóa các actor của màn boss cũ
                        parallaxBackground.remove();
                        ball.remove();
                        paddle.remove();
                        bossHealthBar.remove();
                        boss1.remove();
                        powerUpMenu.remove();
                    }
                    break;
            }
            stage.act(delta);
        } else {
            if (pauseMenu != null) pauseMenu.act(delta);

            if (pauseMenu.isOptionChosen()) {
                switch (pauseMenu.getOption()) {
                    case SAVE :
                        int bricksRemaining = 0;
                        int saveStageNumber = stageNumber;

                        if (tutorial != null) {
                            java.util.List<Save.BrickPosition> brickPositions = tutorial.getBrickPositions();
                            bricksRemaining = brickPositions.size();
                            if (bricksRemaining == 0) {
                                saveStageNumber = 1;
                            }
                            Save.saveGameWithBrickPositions(saveStageNumber, boss1.getHp(), paddle.getHealth(), brickPositions,
                                paddle.getX(), paddle.getY(), ball.getX(), ball.getY(),
                                ball.velocityVector.x, ball.velocityVector.y, ball.isLaunched(),
                                boss1.getX(), boss1.getY());
                        } else {
                            Save.saveGame(saveStageNumber, boss1.getHp(), paddle.getHealth(), bricksRemaining,
                                paddle.getX(), paddle.getY(), ball.getX(), ball.getY(),
                                ball.velocityVector.x, ball.velocityVector.y, ball.isLaunched(),
                                boss1.getX(), boss1.getY());
                        }

                        isPaused = false;
                        pauseMenu.remove();
                        break;

                    case RESUME:
                        isPaused = false;
                        pauseMenu.remove();
                        break;

                    case QUIT:
                        Gdx.app.exit();
                        break;
                }
            }
        }
        stage.draw();
    }

    public boolean isPaused() {
        return isPaused;
    }


    @Override
    public void dispose() {
        stage.dispose();
        paddleImage.dispose();
        ballImage.dispose();
        bossHealthBarImage.dispose();
        paddleSkill1AImage.dispose();
        menuBackground.dispose();
        parallaxBackground.dispose();
        button.dispose();
        pauseMenu.dispose();
        powerUpMenu.dispose();
        tutorial.dispose();
        loadingStage.dispose();
        boss1.dispose();
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
