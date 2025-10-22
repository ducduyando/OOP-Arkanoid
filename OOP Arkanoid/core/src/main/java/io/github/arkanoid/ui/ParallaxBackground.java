package io.github.arkanoid.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.arkanoid.core.Constants.*;

public class ParallaxBackground extends Actor {
    private static class ParallaxLayer {
        Texture texture;
        float speed;
        float x1;
        float x2;
        float y1;
        float y2;
        public enum Direction {
            HORIZONTAL,
            VERTICAL
        }
        Direction direction;

        public ParallaxLayer(Texture texture, float speed, Direction direction) {
            this.texture = texture;
            this.speed = speed;
            this.direction = direction;
            if (direction == Direction.HORIZONTAL) {
                this.x1 = 0;
                this.x2 = SCREEN_WIDTH;
                this.y1 = 0;
                this.y2 = 0;
            }
            if (direction == Direction.VERTICAL) {
                this.x1 = 0;
                this.x2 = 0;
                this.y1 = 0;
                this.y2 = SCREEN_HEIGHT;
            }
        }

        public void update(float delta) {
            if (direction == Direction.HORIZONTAL) {
                x1 -= speed * delta;
                x2 -= speed * delta;

                if (x1 + SCREEN_WIDTH <= 0) {
                    x1 = SCREEN_WIDTH;
                }

                if (x2 + SCREEN_WIDTH <= 0) {
                    x2 = SCREEN_WIDTH;
                }
            }
            if (direction == Direction.VERTICAL) {
                y1 -= speed * delta;
                y2 -= speed * delta;

                if (y1 + SCREEN_HEIGHT <= 0) {
                    y1 = SCREEN_HEIGHT;
                }

                if (y2 + SCREEN_HEIGHT <= 0) {
                    y2 = SCREEN_HEIGHT;
                }
            }
        }

        public void draw(Batch batch) {
            batch.draw(texture, x1, y1, SCREEN_WIDTH, SCREEN_HEIGHT);
            batch.draw(texture, x2, y2, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
    }

    private final ParallaxLayer[] layers;
    private final Texture[] textures;

    public ParallaxBackground(Texture[] textures, float[] speeds, boolean horizontal) {
        this.textures = textures;
        this.layers = new ParallaxLayer[textures.length];
        ParallaxLayer.Direction direction;
        if (horizontal) {
            direction = ParallaxLayer.Direction.HORIZONTAL;
        }
        else {
            direction = ParallaxLayer.Direction.VERTICAL;
        }
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new ParallaxLayer(textures[i], speeds[i], direction);
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
