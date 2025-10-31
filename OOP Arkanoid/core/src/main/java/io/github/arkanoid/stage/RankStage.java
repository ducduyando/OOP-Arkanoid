package io.github.arkanoid.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.core.ProjectileSaveManager;
import io.github.arkanoid.core.Save;

import java.util.List;

import static io.github.arkanoid.core.Constants.*;

public class RankStage implements GameStage {
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;


    private Skin skin;
    private BitmapFont pixelFont;

    private boolean isFinished = false;

    public RankStage() {
        batch = new SpriteBatch();
    }

    @Override
    public void enter() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture("Rank/" + "rank" + ".png");
        skin = new Skin();
            FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("pixel.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size = 40;
            parameter.color = Color.WHITE;
            parameter.minFilter = Texture.TextureFilter.Linear;
            parameter.magFilter = Texture.TextureFilter.Linear;
            parameter.flip = false;

            pixelFont = generator.generateFont(parameter);
            generator.dispose();

            skin.add("default-font", pixelFont);

            Label.LabelStyle labelStyle = new Label.LabelStyle(pixelFont, Color.WHITE);
            skin.add("default", labelStyle);

        Table rankTable = new Table();
        rankTable.setFillParent(true);


        rankTable.align(Align.top).padTop(400f);


        float colWidth = 180f;
        float nameWidth = 300f;
        float pad = 30;

        rankTable.add(new Label("RANK", skin)).width(colWidth).pad(pad).align(Align.left);
        rankTable.add(new Label("NAME", skin)).width(nameWidth).pad(pad).align(Align.left);
        rankTable.add(new Label("STAGE", skin)).width(colWidth).pad(pad).align(Align.left);
        rankTable.add(new Label("TIME (s)", skin)).width(colWidth).pad(pad).align(Align.left);
        rankTable.row();

        List<ProjectileSaveManager.RankEntry> topPlayers = Save.loadRanks();


        for (int i = 0; i < topPlayers.size(); i++) {
            ProjectileSaveManager.RankEntry entry = topPlayers.get(i);
        }


        if (topPlayers.isEmpty()) {
            // Hiển thị nếu không có dữ liệu
            rankTable.add(new Label("No records yet", skin)).colspan(4).pad(pad).align(Align.center);
            rankTable.row();
            rankTable.add(new Label("Play to set records!", skin)).colspan(4).pad(pad).align(Align.center);
        } else {
            for (int i = 0; i < Math.min(3, topPlayers.size()); i++) {
                ProjectileSaveManager.RankEntry entry = topPlayers.get(i);

                String rankNum = (i + 1) + ".";
                String playerName = entry.name != null && !entry.name.trim().isEmpty() ? entry.name : "Unknown";
                String stageNum = String.valueOf(entry.stage);
                String timeStr = String.format("%.1f", entry.time);


                rankTable.add(new Label(rankNum, skin)).pad(pad).align(Align.left);
                rankTable.add(new Label(playerName, skin)).pad(pad).align(Align.left);
                rankTable.add(new Label(stageNum, skin)).pad(pad).align(Align.left);
                rankTable.add(new Label(timeStr, skin)).pad(pad).align(Align.left);
                rankTable.row();
            }
        }



        stage.addActor(rankTable);


    }

    @Override
    public void update(float delta) {
        stage.act(delta);

        // Check for SPACE key to finish
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            isFinished = true;
        }

        // Check for ESCAPE to finish
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isFinished = true;
        }
    }

    public void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // Draw background
        batch.draw(backgroundTexture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.end();

        stage.draw();
    }



    @Override
    public void exit() {
        dispose();
    }

    @Override
    public Stage getGdxStage() {
        return stage;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    public void dispose() {
        stage.dispose();
 batch.dispose();
       backgroundTexture.dispose();

       skin.dispose();
        pixelFont.dispose();
    }
}
