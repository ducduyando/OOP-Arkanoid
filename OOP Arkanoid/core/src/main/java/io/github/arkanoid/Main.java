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

    Menu menu;
    int gameState = 0;
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

        batch.begin();
        batch.end();

        stage.act();
        stage.draw();





        if(bar.getBound().intersects(ball.getBound())){
            if (!ball.isWasColliding()) {
                // Kiem tra ball dang huong nao
                float ballCenterX = ball.getX() + ball.getWidth() / 2.0f;
                float ballCenterY = ball.getY() + ball.getHeight() / 2.0f;
                float barCenterX = bar.getX() + bar.getWidth() / 2.0f;
                float barCenterY = bar.getY() + bar.getHeight() / 2.0f; // Tinh toan khoang cach tam ball voi barr
                 float deltaX = ballCenterX - barCenterX;
                 float deltaY = ballCenterY - barCenterY;
                 // Xac dinh canh nao cua bar bi va cham
                if (Math.abs(deltaX) / bar.getWidth() > Math.abs(deltaY) / bar.getHeight()) {
                    // Va cham tu ben trai hoac ben phai
                    if (deltaX > 0) {
                        // Ball o ben phai
                        ball.reverseX();
                        // tranh bi ket lai thanh bar
                        ball.setPosition(bar.getX() + bar.getWidth() + 1, ball.getY());
                    }
                    else {
                        // Ball o ben trai
                        ball.reverseX();
                        ball.setPosition(bar.getX() - ball.getWidth() - 1, ball.getY());
                    }
                }
                else { // Va cham tu phia ben duoi
                     if (deltaY > 0) {
                         // Ball o phia tren bar
                         ball.reverseY();
                         ball.setPosition(ball.getX(), bar.getY() + bar.getHeight() + 1);
                     } else {
                         // Ball o phía duoi bar
                         ball.reverseY(); ball.setPosition(ball.getX(), bar.getY() - ball.getHeight() - 1);
                     }
                } ball.setWasColliding(true);
            }
        } else {
            // Reset trang thai collision khi ko co va cham
            ball.setWasColliding(false);
        }
    }
}
