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
        System.out.println("Main: Initializing Singleton managers...");

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

        System.out.println("Main: All Singleton managers initialized");

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
                } else if (currentStage instanceof NameInputStage) {
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
                    // Go to NameInputStage first, then TutorialStage
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
                ((PowerUpMenuStage) nextStage).setLayerNumber(0);
                changeStage(nextStage);
                return;
            }

        } else if (currentStage instanceof PowerUpMenuStage powerUpMenuStage) {

            if (powerUpNumber == 0) {

                nextStage = new Boss2Stage();
                loadingScreen = new LoadingScreen(stageTextures[2]);

                currentFlow = GameFlow.LOADING;
                powerUpNumber++;

                if (powerUpMenuStage.getSelectedOption() == PowerUpMenu.Option.SKILL1) {
                    ((Boss2Stage) nextStage).setSkill1ASelected(true);
                } else if (powerUpMenuStage.getSelectedOption() == PowerUpMenu.Option.SKILL2) {
                    ((Boss2Stage) nextStage).setSkill1ASelected(false);
                }

            } else if (powerUpNumber == 1) {
                nextStage = new Boss3Stage();
                loadingScreen = new LoadingScreen(stageTextures[3]);

                currentFlow = GameFlow.LOADING;
                powerUpNumber++;

                if (powerUpMenuStage.getSelectedOption() == PowerUpMenu.Option.SKILL1) {
                    ((Boss2Stage) nextStage).setSkill1ASelected(true);
                } else if (powerUpMenuStage.getSelectedOption() == PowerUpMenu.Option.SKILL2) {
                    ((Boss2Stage) nextStage).setSkill1ASelected(false);
                }

            }

        } else if (currentStage instanceof Boss2Stage boss2Stage) {
            if (boss2Stage.isGameOver()) {
                nextStage = new GameLoseStage();
                changeStage(nextStage);
                return;
            } else {
                // Boss defeated - show power up menu
                nextStage = new PowerUpMenuStage();
                ((PowerUpMenuStage) nextStage).setLayerNumber(1);
                changeStage(nextStage);
                return;
            }
        } else if (currentStage instanceof NameInputStage) {
            // After name input, go to TutorialStage with loading screen
            nextStage = new TutorialStage();
            loadingScreen = new LoadingScreen(stageTextures[0]);
            currentFlow = GameFlow.LOADING;
        } else if (currentStage instanceof GameLoseStage) {
            // After lose screen, go to RankStage
            nextStage = new RankStage();
            changeStage(nextStage);
            return;
        } else if (currentStage instanceof RankStage) {
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
                break;
            case 1:
                nextStage = new Boss1Stage(saveData);
                loadingScreen = new LoadingScreen(stageTextures[1]);
                break;
            case 2:
                nextStage = new Boss2Stage(saveData);
                loadingScreen = new LoadingScreen(stageTextures[2]);
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
        System.out.println("Main: Disposing application...");

        if (currentStage != null) {
            currentStage.exit();
        }

        // Dispose all Singleton managers
        disposeSingletons();

        batch.dispose();
        for (Texture texture : stageTextures) {
            texture.dispose();
        }

        System.out.println("Main: Application disposed");
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

        // AudioManager removed - not being used

        // Note: GameManager and InputManager don't need explicit disposal
        // as they don't hold disposable resources

        System.out.println("Main: All Singleton managers disposed");
    }
}
