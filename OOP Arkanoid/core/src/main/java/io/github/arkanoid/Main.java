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

import java.awt.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    SpriteBatch batch;
    OrthographicCamera camera;

    Texture barImage;
    Texture ballImage;

    Bar bar;
    Ball ball;

    Stage stage;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        barImage = new Texture("bar.png");
        ballImage = new Texture("ball.png");

        bar = new Bar(barImage, 0, 0);
        ball = new Ball(ballImage, 100, 200);

        stage = new Stage();

        stage.addActor(ball);
        stage.addActor(bar);

    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.GRAY);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.end();

        stage.act();
        stage.draw();
    }


}
