package io.github.arkanoid.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.entities.Boss;

import static io.github.arkanoid.core.Constants.*;

public class HealthBar extends Actor {
    private final Boss owner;
    private final TextureRegion[] frames;
    private final int maxFrame;

    public HealthBar(Texture healthBarTexture, Boss owner) {
        this.owner = owner;

        int frameWidth = HP_WIDTH;
        int frameHeight = HP_HEIGHT;
        this.maxFrame = healthBarTexture.getHeight() / frameHeight;

        this.frames = new TextureRegion[maxFrame];
        for (int i = 0; i < maxFrame; i++) {
            frames[i] = new TextureRegion(healthBarTexture, 0, frameHeight * i, frameWidth, frameHeight);
        }

        setSize(frameWidth, frameHeight);
        setPosition(0, SCREEN_HEIGHT - HP_HEIGHT);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float percentHpLost = (float) (owner.getMaxHp() - owner.getHp()) / owner.getMaxHp();
        int frameIndex = (int) (percentHpLost * (maxFrame - 1));

        frameIndex = Math.min(maxFrame - 1, Math.max(0, frameIndex));

        batch.draw(frames[frameIndex], getX(), getY(), getWidth(), getHeight());
    }
}
