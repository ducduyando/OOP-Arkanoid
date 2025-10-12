package io.github.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static io.github.arkanoid.Constants.*;

public class Menu {
    private SpriteBatch batch;
    private Texture sheet;
    private TextureRegion[][] buttons;
    private int result = -1;

    private Texture menuBG1;
    private Texture menuGlitch2;
    private Texture menuText3;
    private Texture menuFrame4;

    private float glitchY;
    private boolean glitchMovingDown = true;

    private boolean playHover = false;
    private boolean quitHover = false;
    private int selectedButton = 0;
    private float keyCooldown = 0f;

    private float animTimer = 0f;

    public Menu() {
        batch = new SpriteBatch();
        menuBG1 = new Texture(Gdx.files.internal("MenuBG1.png"));
        menuGlitch2 = new Texture(Gdx.files.internal("MenuGlitch2.png"));
        menuText3 = new Texture(Gdx.files.internal("MenuText3.png"));
        menuFrame4 = new Texture(Gdx.files.internal("MenuFrame4.png"));
        glitchY = 0;
        sheet = new Texture(Gdx.files.internal("MenuButtons5.png"));
        buttons = TextureRegion.split(sheet, sheet.getWidth() / 2, sheet.getHeight() / 2);
    }

    public int showMenu() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();
        animTimer += delta;
        updateGlitchAnimation(delta);

        float centerX = SCREEN_WIDTH / 2f;
        float buttonAreaY = SCREEN_HEIGHT * 0.4f;
        float buttonWidth = BUTTON_FRAME_WIDTH * BUTTON_SCALE;
        float buttonHeight = BUTTON_FRAME_HEIGHT * BUTTON_SCALE;
        float playX = centerX - BUTTON_SPACING;
        float quitX = centerX + BUTTON_SPACING;

        if (keyCooldown > 0) keyCooldown -= delta;

        // Chỉ kiểm tra hover cho button hiện tại
        if (selectedButton == 0) {
            playHover = isMouseHover(centerX, buttonAreaY, buttonWidth, buttonHeight);
            quitHover = false;
        } else {
            quitHover = isMouseHover(centerX, buttonAreaY, buttonWidth, buttonHeight);
            playHover = false;
        }

        if (keyCooldown <= 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                selectedButton = 0;
                keyCooldown = MENU_KEY_COOLDOWN;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                selectedButton = 1;
                keyCooldown = MENU_KEY_COOLDOWN;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D) ||
            Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playHover = (selectedButton == 0);
            quitHover = (selectedButton == 1);
        }

        if (Gdx.input.justTouched()) {
            if (playHover) result = 0;
            else if (quitHover) result = 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            result = selectedButton;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) result = 1;

        batch.begin();
        batch.draw(menuBG1, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.draw(menuGlitch2, 0, glitchY, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.draw(menuText3, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.draw(menuFrame4, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        if (selectedButton == 0) {
            TextureRegion playTexture = getAnimatedButton(0, true);
            batch.draw(playTexture, centerX - buttonWidth / 2, buttonAreaY - buttonHeight / 2, buttonWidth, buttonHeight);
        } else {
            TextureRegion quitTexture = getAnimatedButton(1, true);
            batch.draw(quitTexture, centerX - buttonWidth / 2, buttonAreaY - buttonHeight / 2, buttonWidth, buttonHeight);
        }

        batch.end();

        return result;
    }

    private TextureRegion getAnimatedButton(int buttonIndex, boolean active) {
        int frameCount = 2;
        int row = buttonIndex; // 0: PLAY, 1: QUIT
        int currentFrame = active ? ((int)(animTimer / MENU_ANIM_FRAME_TIME) % frameCount) : 0;
        return buttons[row][currentFrame];
    }


    private boolean isMouseHover(float centerX, float centerY, float width, float height) {
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        return mouseX >= centerX - width / 2 && mouseX <= centerX + width / 2 &&
            mouseY >= centerY - height / 2 && mouseY <= centerY + height / 2;
    }

    private void updateGlitchAnimation(float delta) {
        if (glitchMovingDown) {
            glitchY -= MENU_GLITCH_SPEED * delta;
            if (glitchY <= -MENU_GLITCH_RANGE) {
                glitchY = -MENU_GLITCH_RANGE;
                glitchMovingDown = false;
            }
        } else {
            glitchY += MENU_GLITCH_SPEED * delta;
            if (glitchY >= MENU_GLITCH_RANGE) {
                glitchY = MENU_GLITCH_RANGE;
                glitchMovingDown = true;
            }
        }
    }


    public void dispose() {
        batch.dispose();
        sheet.dispose();
        menuBG1.dispose();
        menuGlitch2.dispose();
        menuText3.dispose();
        menuFrame4.dispose();
    }
}
