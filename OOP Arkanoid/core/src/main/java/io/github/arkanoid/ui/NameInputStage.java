package io.github.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.core.Save;
import io.github.arkanoid.stage.GameStage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import static io.github.arkanoid.core.Constants.*;


public class NameInputStage implements GameStage  {
    private Stage stage;
    private TextField nameTextField;
    private Skin skin;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private boolean isFinished = false;
    public NameInputStage() {
        batch = new SpriteBatch();
    }

    // Constructor with callback for when name input is complete
    public NameInputStage(Runnable onComplete) {
        this();
    }

    @Override
    public void enter() {


            stage = new Stage(new ScreenViewport());
            Gdx.input.setInputProcessor(stage);

            backgroundTexture = new Texture("Rank/" + "name" + ".png");

        skin = new Skin();
        BitmapFont font;
          FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("pixel.ttf"));
         FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 100;
            parameter.color = Color.WHITE;
            parameter.minFilter = Texture.TextureFilter.Linear;
            parameter.magFilter = Texture.TextureFilter.Linear;
            parameter.flip = false;

            font = generator.generateFont(parameter);
            generator.dispose();




        skin.add("default-font", font);


        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.CLEAR);
        pixmap.fill();
        Texture transparentTexture = new Texture(pixmap);
        pixmap.dispose();

        TextureRegionDrawable background = new TextureRegionDrawable(new TextureRegion(transparentTexture));


        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = background;
        textFieldStyle.cursor = background;

       skin.add("default", textFieldStyle);

        String savedName = "";
            savedName = Save.loadPlayerName();



        nameTextField = new TextField(savedName, skin, "default");
        nameTextField.setMessageText("Enter your name...");

        float fieldX = (SCREEN_WIDTH - FIELD_WIDTH) / 2f;
        float fieldY = SCREEN_HEIGHT / 2f - 170f;
        nameTextField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        nameTextField.setPosition(fieldX, fieldY);

        stage.addActor(nameTextField);



    }

    @Override
    public void update(float delta) {
        stage.act(Math.min(delta, 1 / 30f));

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            String playerName = nameTextField.getText().trim();
            if (!playerName.isEmpty()) {
                Save.savePlayerName(playerName);
                isFinished = true;
            }
        }

        // Check for ESCAPE to cancel
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isFinished = true;
        }
    }

    public void draw() {

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (batch != null && backgroundTexture != null) {
                batch.begin();
                batch.draw(backgroundTexture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                batch.end();
            }

            stage.draw();

    }

    @Override
    public void exit() {
        dispose();
    }

    @Override
    public Stage getGdxStage() {
        return this.stage;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }


    public void dispose() {
         stage.dispose();
         skin.dispose();
         batch.dispose();
         backgroundTexture.dispose();
    }

    public String getPlayerName() {
        return nameTextField != null ? nameTextField.getText().trim() : "";
    }
}

