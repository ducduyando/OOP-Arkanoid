package io.github.arkanoid.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

    @Override
    public void create() {
        batch = new SpriteBatch();

        stageTextures = new Texture[3];
        stageTextures[0] = new Texture("stages/stage0.png"); // Tutorial
        stageTextures[1] = new Texture("stages/stage1.png"); // Boss 1
        stageTextures[2] = new Texture("stages/stage2.png");

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
                changeStage(nextStage);
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
                    System.out.println("Starting new game...");
                    changeStage(new TutorialStage());
                    break;
                case LOAD:
                    System.out.println("Loading saved game...");
                    // TODO: Thêm logic tải game và chuyển đến màn chơi đã lưu
                    // Ví dụ: changeStage(loadGameAndGetStage());
                    break;
                case QUIT:
                    System.out.println("Exiting game...");
                    Gdx.app.exit();
                    break;
            }
        }
        else if (currentStage instanceof TutorialStage) {
            nextStage = new Boss1Stage();
            loadingScreen = new LoadingScreen(stageTextures[1]);
            currentFlow = GameFlow.LOADING;
        }
        else if (currentStage instanceof Boss1Stage) {
            nextStage = new PowerUpMenuStage();
            loadingScreen = new LoadingScreen(stageTextures[2]);
            currentFlow = GameFlow.LOADING;
        }
        else if (currentStage instanceof PowerUpMenuStage) {
            PowerUpMenuStage powerUpStage = (PowerUpMenuStage) currentStage;
            if (powerUpStage.getSelectedOption() == PowerUpMenu.Option.SKILL1) {
                System.out.println("Skill 1 chosen!");
            }
            else  {
                System.out.println("Skill 2 chosen!");
            }
            changeStage(new TutorialStage());
        }
    }

    private void changeStage(GameStage newStage) {
        if (currentStage != null) {
            currentStage.exit(); // Dọn dẹp stage cũ
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
        // Dọn dẹp các texture đã tải trước
        for (Texture texture : stageTextures) {
            texture.dispose();
        }
    }
}
