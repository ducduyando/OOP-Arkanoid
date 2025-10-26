package io.github.arkanoid.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.arkanoid.stage.*;
import io.github.arkanoid.ui.*;
import io.github.arkanoid.ui.Button;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private enum GameFlow {
        RUNNING,
        LOADING
    }

    private GameFlow currentFlow = GameFlow.RUNNING;
    private GameStage currentStage;
    private GameStage nextStage; // Màn chơi sẽ đến sau khi loading xong
    private LoadingScreen loadingScreen;

    private SpriteBatch batch;

    private Texture[] stageTextures; // Mảng chứa ảnh nền của các màn chơi

    private int powerUpNumber = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();

        stageTextures = new Texture[4];
        stageTextures[0] = new Texture("Stages/" + "stage0" + ".png");
        stageTextures[1] = new Texture("Stages/" + "stage1" + ".png"); // Boss 1
        stageTextures[2] = new Texture("Stages/" + "stage2" + ".png"); // Boss 2
        stageTextures[3] = new Texture("Stages/" + "stage2" + ".png"); // Boss 3 tam thoi


        changeStage(new MenuStage());
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
                currentStage.getGdxStage().draw();

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
                    nextStage = new TutorialStage();
                    loadingScreen = new LoadingScreen(stageTextures[0]);
                    currentFlow = GameFlow.LOADING;
                    break;
                case LOAD:
                    loadSavedGame();
                    break;
                case QUIT:
                    Gdx.app.exit();
                    break;
            }
        } else if (currentStage instanceof TutorialStage) {
            nextStage = new Boss1Stage();
            loadingScreen = new LoadingScreen(stageTextures[1]);
            currentFlow = GameFlow.LOADING;
        } else if (currentStage instanceof Boss1Stage) {
            nextStage = new PowerUpMenuStage();
            ((PowerUpMenuStage) nextStage).setLayerNumber(0);
            changeStage(nextStage);

        } else if (currentStage instanceof PowerUpMenuStage powerUpMenuStage) {

            if (powerUpNumber == 0) {

                nextStage = new Boss2Stage();
                loadingScreen = new LoadingScreen(stageTextures[2]);

                currentFlow = GameFlow.LOADING;
                powerUpNumber++;

                if (powerUpMenuStage.getSelectedOption() == PowerUpMenu.Option.SKILL1) {
                    ((Boss2Stage) nextStage).setSkillASelected(true);
                } else if (powerUpMenuStage.getSelectedOption() == PowerUpMenu.Option.SKILL2) {
                    ((Boss2Stage) nextStage).setSkillASelected(false);
                }

            } else if (powerUpNumber == 1) {
                nextStage = new Boss2Stage();
                loadingScreen = new LoadingScreen(stageTextures[3]);

                currentFlow = GameFlow.LOADING;
                powerUpNumber++;

            }

        } else if (currentStage instanceof Boss2Stage) {
            nextStage = new PowerUpMenuStage();
            ((PowerUpMenuStage) nextStage).setLayerNumber(1);
            changeStage(nextStage);
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
        if (currentStage != null) {
            currentStage.exit();
        }
        batch.dispose();
        for (Texture texture : stageTextures) {
            texture.dispose();
        }
    }
}
