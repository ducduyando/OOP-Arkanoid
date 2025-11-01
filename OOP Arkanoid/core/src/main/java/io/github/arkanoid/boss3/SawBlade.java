package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import static io.github.arkanoid.core.Constants.*;

public class SawBlade extends Actor {
    private TextureRegion currentFrame;
    private Texture sawBladeTexture;
    private Texture targetTexture;

    private Animation<TextureRegion> sawBladeAnimation;
    private Animation<TextureRegion> predictionAnimation;
}
