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

import static io.github.arkanoid.Constants.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    SpriteBatch batch;
    OrthographicCamera camera;

    Texture barImage;
    Texture ballImage;

    Texture boss1Image;

    Bar bar;
    Ball ball;
    GameLogic gameLogic;

    Boss1 boss1;

    Stage stage;

    Menu menu;
    int gameState = 0;
    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        barImage = new Texture("Bar.png");
        ballImage = new Texture("Ball.png");
        boss1Image = new Texture("Boss1.png");

        bar = new Bar(barImage, 0, 0);
        ball = new Ball(ballImage, 0, 0);

        boss1 = new Boss1(boss1Image, 700, 700);

        gameLogic = new GameLogic(ball, bar, boss1);
        stage = new Stage();

        stage.addActor(ball);
        stage.addActor(bar);

        stage.addActor(boss1);

        menu = new Menu();

    }

    @Override
    public void render() {
        if (gameState == 0) {
            int result = menu.showMenu();
            if (result == 0) {
                gameState = 1; // New Game
            } else if (result == 1) {
                Gdx.app.exit();
            }
            return;
        }
        ScreenUtils.clear(Color.GRAY);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        float delta = Gdx.graphics.getDeltaTime();
        stage.act(delta);
        gameLogic.launch();
        gameLogic.barCollision();
        gameLogic.boundaryCollision(delta);
        gameLogic.bossCollision();
        stage.draw();

    }

    @Override
    public void dispose() {
        batch.dispose();
        barImage.dispose();
        ballImage.dispose();
        boss1Image.dispose();
        stage.dispose();
        menu.dispose();
    }
}
