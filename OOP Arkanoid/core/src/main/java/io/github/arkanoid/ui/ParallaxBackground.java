package io.github.arkanoid.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.core.Constants.*;

public class ParallaxBackground extends Actor {
    private static class ParallaxLayer {
        Texture texture;
        float speed;
        float y1;
        float y2;

        public ParallaxLayer(Texture texture, float speed) {
            this.texture = texture;
            this.speed = speed;
            this.y1 = 0;
            this.y2 = SCREEN_HEIGHT;
        }

        public void update(float delta) {
            y1 -= speed * delta;
            y2 -= speed * delta;

            if (y1 + SCREEN_HEIGHT <= 0) {
                y1 = y2 + SCREEN_HEIGHT;
            }

            if (y2 + SCREEN_HEIGHT <= 0) {
                y2 = y1  + SCREEN_HEIGHT;
            }
        }

        public void draw(Batch batch) {
            batch.draw(texture, 0, y1, SCREEN_WIDTH, SCREEN_HEIGHT);
            batch.draw(texture, 0, y2, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
    }

    private final ParallaxLayer[] layers;
    private final Texture[] textures;

    public ParallaxBackground(Texture[] textures, float[] speeds) {
        this.textures = textures;
        this.layers = new ParallaxLayer[textures.length];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new ParallaxLayer(textures[i], speeds[i]);
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (ParallaxLayer layer : layers) {
            layer.update(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (ParallaxLayer layer : layers) {
            layer.draw(batch);
        }
    }

    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
    }
}
