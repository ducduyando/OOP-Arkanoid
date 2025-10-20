package io.github.arkanoid.stage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.boss1.Boss1;
import io.github.arkanoid.core.GameLogic;
import io.github.arkanoid.entities.Ball;
import io.github.arkanoid.paddle.Paddle;
import io.github.arkanoid.ui.HealthBar;
import io.github.arkanoid.ui.ParallaxBackground;

import static io.github.arkanoid.core.Constants.*;

public class Boss1Stage implements GameStage {
    private Stage stage;
    private GameLogic gameLogic;

    private Paddle paddle;
    private Ball ball;
    private Boss1 boss1;
    private HealthBar bossHealthBar;
    private ParallaxBackground parallaxBackground;

    private Texture paddleImage;
    private Texture ballImage;
    private Texture bossHealthBarImage;
    private Texture[] bgTextures;

    @Override
    public void enter() {
        stage = new Stage(new ScreenViewport());

        this.paddleImage = new Texture("universal/" + "paddle" + ".png");
        this.ballImage = new Texture("ball/" + "normal" + ".png");
        this.bossHealthBarImage = new Texture("universal/" + "health_bar" + ".png");
        this.bgTextures = new Texture[5];
        for (int i = 0; i < 5; i++) {
            bgTextures[i] = new Texture("background/" + "layer" + i + ".png");
        }

        paddle = new Paddle(paddleImage, PADDLE_INITIAL_X, PADDLE_INITIAL_Y);
        ball = new Ball(ballImage, 0, 0);
        boss1 = new Boss1(1, BOSS1_INITIAL_X, BOSS1_INITIAL_Y, 100);
        bossHealthBar = new HealthBar(bossHealthBarImage, boss1);
        parallaxBackground = new ParallaxBackground(bgTextures, new float[]{0f, 50f, 40f, 30f, 20f});

        gameLogic = new GameLogic(paddle, boss1);

        stage.addActor(parallaxBackground);
        stage.addActor(bossHealthBar);
        stage.addActor(paddle);
        stage.addActor(ball);
        stage.addActor(boss1);
    }

    @Override
    public void update(float delta) {
        gameLogic.launch(ball);
        gameLogic.paddleCollision(ball);
        gameLogic.boundaryCollision(ball, delta, UP_BOUNDARY);
        gameLogic.bossCollision(ball);
        gameLogic.skillCollision(stage);

        stage.act(delta);
    }

    @Override
    public void exit() {
        paddleImage.dispose();
        ballImage.dispose();
        bossHealthBarImage.dispose();
        parallaxBackground.dispose();
        boss1.dispose();
        stage.dispose();
    }

    @Override
    public Stage getGdxStage() {
        return this.stage;
    }

    @Override
    public boolean isFinished() {
        return boss1.isDead() && boss1.isReadyToDeath();
    }
}
