package io.github.arkanoid.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.arkanoid.stage.*;
import io.github.arkanoid.ui.*;
import io.github.arkanoid.ui.Button;
import io.github.arkanoid.ui.NameInputStage;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private enum GameFlow {
        RUNNING,
        LOADING
    }

    private GameFlow currentFlow = GameFlow.RUNNING;
    private GameStage currentStage;
    private GameStage nextStage;
    private LoadingScreen loadingScreen;

    private SpriteBatch batch;

    private Texture[] stageTextures;

    private int powerUpNumber = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Initialize all Singleton managers
        initializeSingletons();

        stageTextures = new Texture[4];
        stageTextures[0] = new Texture("Stages/" + "stage0" + ".png");
        stageTextures[1] = new Texture("Stages/" + "stage1" + ".png"); // Boss 1
        stageTextures[2] = new Texture("Stages/" + "stage2" + ".png"); // Boss 2
        stageTextures[3] = new Texture("Stages/" + "stage3" + ".png"); // Boss 3

        changeStage(new MenuStage());
    }

    /**
     * Initialize all Singleton managers
     */
    private void initializeSingletons() {


        // Initialize GameManager
        GameManager gameManager = GameManager.getInstance();
        gameManager.reset();

        // Initialize InputManager
        InputManager inputManager = InputManager.getInstance();
        inputManager.logKeyBindings();

        // AudioManager removed - not being used

        // Initialize ResourceManager
        ResourceManager resourceManager = ResourceManager.getInstance();
        resourceManager.preloadGameResources();
        resourceManager.preloadBossResources();

        // Initialize SceneManager
        SceneManager sceneManager = SceneManager.getInstance();



        // Singleton managers are ready to use
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (currentFlow == GameFlow.LOADING) {
            loadingScreen.act(delta);
            batch.begin();
            loadingScreen.draw(batch, 1f);
            batch.end();

            if (loadingScreen.getState() == LoadingScreen.State.DONE) {
                currentFlow = GameFlow.RUNNING;

                if (nextStage != null) {
                    changeStage(nextStage);
                } else {
                    Gdx.app.exit();
                }

                loadingScreen.dispose();
                loadingScreen = null;
                nextStage = null;
            }
        } else {
            if (currentStage != null) {
                currentStage.update(delta);

                if (currentStage instanceof GameLoseStage) {
                    batch.begin();
                    ((GameLoseStage) currentStage).drawDirect(batch);
                    batch.end();
                }
                else if (currentStage instanceof GameWinStage) {
                    batch.begin();
                    ((GameWinStage) currentStage).drawDirect(batch);
                    batch.end();
                }
                else if (currentStage instanceof NameInputStage) {
                    ((NameInputStage) currentStage).draw();
                } else if (currentStage instanceof RankStage) {
                    ((RankStage) currentStage).draw();
                } else {
                    currentStage.getGdxStage().draw();
                }

                if (currentStage.isFinished()) {
                    handleStageTransition();
                }
            }
        }
    }

    private void handleStageTransition() {

        if (currentStage instanceof MenuStage) {
            MenuStage menuStage = (MenuStage) currentStage;
            Button.Mode choice = menuStage.getSelectedMode();
            switch (choice) {
                case PLAY:

                    nextStage = new NameInputStage();
                    changeStage(nextStage);
                    break;
                case LOAD:
                    loadSavedGame();
                    break;
                case QUIT:
                    Gdx.app.exit();
                    break;
            }
        } else if (currentStage instanceof TutorialStage tutorialStage) {
            if (tutorialStage.isGameOver()) {
                nextStage = new GameLoseStage();
                changeStage(nextStage);
                return;
            } else {

                nextStage = new Boss1Stage();
                loadingScreen = new LoadingScreen(stageTextures[1]);
                currentFlow = GameFlow.LOADING;
            }
        } else if (currentStage instanceof Boss1Stage boss1Stage) {
            if (boss1Stage.isGameOver()) {
                nextStage = new GameLoseStage();
                changeStage(nextStage);
                return;
            } else {
                nextStage = new PowerUpMenuStage();
                ((PowerUpMenuStage) nextStage).setPowerUpNumber(0);
                changeStage(nextStage);
                return;
            }

        } else if (currentStage instanceof PowerUpMenuStage powerUpMenuStage) {

            if (powerUpNumber == 0) {

                nextStage = new Boss2Stage();
                loadingScreen = new LoadingScreen(stageTextures[2]);

                currentFlow = GameFlow.LOADING;
                powerUpNumber++;

                // Store skill1 selection in GameManager for Boss2Stage
                if (powerUpMenuStage.getSelectedOption() == PowerUpMenu.Option.SKILL1) {
                    GameManager.getInstance().setHasSkill1A(true);
                } else if (powerUpMenuStage.getSelectedOption() == PowerUpMenu.Option.SKILL2) {
                    GameManager.getInstance().setHasSkill1A(false);
                }

            } else if (powerUpNumber == 1) {
                nextStage = new Boss3Stage();
                loadingScreen = new LoadingScreen(stageTextures[3]);

                currentFlow = GameFlow.LOADING;
                powerUpNumber++;

                // Set skill2 selection based on PowerUpMenu choice
                if (powerUpMenuStage.getSelectedOption() == PowerUpMenu.Option.SKILL1) {
                    // Player chose SKILL1 -> use skill2A (Bee Bullet)
                    GameManager.getInstance().setHasSkill2A(true);

                } else if (powerUpMenuStage.getSelectedOption() == PowerUpMenu.Option.SKILL2) {
                    // Player chose SKILL2 -> use skill2B (Honey Shield)
                    GameManager.getInstance().setHasSkill2A(false);

                }
            }

        } else if (currentStage instanceof Boss2Stage boss2Stage) {
            if (boss2Stage.isGameOver()) {
                nextStage = new GameLoseStage();
                changeStage(nextStage);
                return;
            } else {

                nextStage = new PowerUpMenuStage();
                ((PowerUpMenuStage) nextStage).setPowerUpNumber(1);
                changeStage(nextStage);
                return;
            }
        } else if (currentStage instanceof Boss3Stage boss3Stage) {
            if (boss3Stage.isGameOver()) {
                nextStage = new GameLoseStage();
                changeStage(nextStage);
                return;
            } else {
                nextStage = new GameWinStage();
                changeStage(nextStage);
                return;
            }
        } else if (currentStage instanceof NameInputStage) {

            nextStage = new TutorialStage();
            loadingScreen = new LoadingScreen(stageTextures[0]);
            currentFlow = GameFlow.LOADING;
        } else if (currentStage instanceof GameLoseStage) {
            // After lose screen, go to RankStage
            nextStage = new RankStage();
            changeStage(nextStage);
            return;
        }
        else if (currentStage instanceof GameWinStage) {
            nextStage = new RankStage();
            changeStage(nextStage);
            return;
        }
        else if (currentStage instanceof RankStage) {
            // After rank screen, go back to MenuStage
            nextStage = new MenuStage();
            changeStage(nextStage);
            return;
        }
    }
    private void loadSavedGame() {
        if (!Save.hasSave()) {

            changeStage(new TutorialStage());
            return;
        }

        Save.SaveData saveData = Save.loadGame();
        if (saveData == null) {

            changeStage(new TutorialStage());
            return;
        }


        switch (saveData.stageNumber) {
            case 0:
                nextStage = new TutorialStage(saveData);
                loadingScreen = new LoadingScreen(stageTextures[0]);
                powerUpNumber = 0; // Reset powerUpNumber for Tutorial
                break;
            case 1:
                nextStage = new Boss1Stage(saveData);
                loadingScreen = new LoadingScreen(stageTextures[1]);
                powerUpNumber = 0; // Boss1 completed will show first PowerUpMenu
                break;
            case 2:
                nextStage = new Boss2Stage(saveData);
                loadingScreen = new LoadingScreen(stageTextures[2]);
                powerUpNumber = 1; // Boss2 completed will show second PowerUpMenu -> Boss3
                break;
            case 3:
                nextStage = new Boss3Stage(saveData);
                loadingScreen = new LoadingScreen(stageTextures[3]);
                powerUpNumber = 2; // Boss3 is final stage
                break;
            default:
                // ko save game thi load se la new game
                changeStage(new TutorialStage());
                return;
        }

        currentFlow = GameFlow.LOADING;
    }

    private void changeStage(GameStage newStage) {
        if (currentStage != null) {
            currentStage.exit();
        }
        currentStage = newStage;
        if (currentStage != null) {
            currentStage.enter();
            Gdx.input.setInputProcessor(currentStage.getGdxStage());
        }
    }

    @Override
    public void dispose() {

        if (currentStage != null) {
            currentStage.exit();
        }

        // Dispose all Singleton managers
        disposeSingletons();

        batch.dispose();
        for (Texture texture : stageTextures) {
            texture.dispose();
        }


    }

    /**
     * Dispose all Singleton managers
     */
    private void disposeSingletons() {
        System.out.println("Main: Disposing Singleton managers...");

        // Dispose SceneManager
        SceneManager.getInstance().dispose();

        // Dispose ResourceManager
        ResourceManager.getInstance().dispose();



        System.out.println("Main: All Singleton managers disposed");
    }
}
