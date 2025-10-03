package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ball extends Actor {
    TextureRegion textureRegion;

    Ball(Texture texture, float x, float y) {
        this.textureRegion = new TextureRegion(texture);
        setPosition(x, y);
        setSize(texture.getWidth(), texture.getHeight());
        setOrigin(texture.getWidth() / 2f, texture.getHeight() / 2f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
