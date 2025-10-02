package io.github.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    SpriteBatch batch;
    OrthographicCamera camera;
    Texture ball;
    Texture bar;

    Rectangle ballRect;
    Rectangle barRect;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        ball = new Texture("ball.png");
        bar = new Texture("bar.png");

        ballRect = new Rectangle(100f, 200f, ball.getWidth()/2f, ball.getHeight()/2f);
        barRect = new Rectangle(0f, 0f, bar.getWidth()/2f, bar.getHeight()/2f);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLUE);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        barRect.x ++;

        batch.begin();
        batch.draw(ball, ballRect.x, ballRect.y, ballRect.width, ballRect.height);
        batch.draw(bar, barRect.x, barRect.y, barRect.width, barRect.height);
        batch.end();
    }


}
