package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.GL20;

public class Menu {
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture bg;
    private final String[] labels = {"New Game", "Exit", "Setting", "How to play"};

    public Menu() {
        batch = new SpriteBatch();
        bg = new Texture(Gdx.files.internal("abackground.png"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    private int result = -1;

    public int showMenu() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float startY = Gdx.graphics.getHeight() / 2f + 100;

        for (int i = 0; i < labels.length; i++) {
            boolean hovered = isMouseHover(i, startY);
            font.setColor(hovered ? Color.RED : Color.WHITE);
            font.draw(batch, labels[i], Gdx.graphics.getWidth() / 2f, startY - i * 50, 0, Align.center, false);
        }

        batch.end();

        if (Gdx.input.justTouched()) {
            int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            int index = getMenuIndex(mouseY, startY);
            if (index >= 0) result = index;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) result = 1;
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) result = 0;

        return result;
    }

    private boolean isMouseHover(int i, float startY) {
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        float textY = startY - i * 50;
        return mouseX >= Gdx.graphics.getWidth() / 2f - 100 &&
            mouseX <= Gdx.graphics.getWidth() / 2f + 100 &&
            mouseY >= textY - 20 && mouseY <= textY + 20;
    }

    private int getMenuIndex(int mouseY, float startY) {
        for (int i = 0; i < labels.length; i++) {
            float textY = startY - i * 50;
            if (mouseY >= textY - 20 && mouseY <= textY + 20)
                return i;
        }
        return -1;
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        bg.dispose();
    }
}

