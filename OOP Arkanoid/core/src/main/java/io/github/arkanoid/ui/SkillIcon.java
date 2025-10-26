package io.github.arkanoid.ui;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.paddle.Paddle;

import static io.github.arkanoid.core.Constants.*;

public class SkillIcon extends Actor {
    private final Paddle paddle;
    private final Texture iconTexture;
    private final TextureRegion[] frames;
    private final String skillKey;

    public SkillIcon(Paddle paddle, Texture iconTexture, String skillKey, float x, float y) {
        this.paddle = paddle;
        this.iconTexture = iconTexture;
        this.skillKey = skillKey;

        frames = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            frames[i] = new TextureRegion(iconTexture, i * SKILL_ICON_WIDTH, 0, SKILL_ICON_WIDTH, SKILL_ICON_HEIGHT);
        }

        setPosition(x, y);
        setSize(SKILL_ICON_WIDTH, SKILL_ICON_HEIGHT);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion currentFrame = null;
        if(skillKey.equals("J")) {
            if (paddle.isSkillASelected()) {
                currentFrame = frames[1]; // PaddleSkill1Av Always frame 2
            }
            else {
                float cooldown = paddle.getSkill1BCooldownTimer();

                if (cooldown <= 0) {
                    currentFrame = frames[1];

                }
                else {
                    float elapsedTime = MAX_COOLDOWN - cooldown;
                    float completionRatio = elapsedTime / MAX_COOLDOWN;
                    int[] cooldownFrames = {0, 4, 3, 2};
                    int numCooldownFrames = cooldownFrames.length;

                    int cooldownIndex = (int) (completionRatio * numCooldownFrames);
                    cooldownIndex = Math.min(cooldownIndex, numCooldownFrames - 1); // Đảm bảo không vượt quá chỉ số cuối (3)

                    currentFrame = frames[cooldownFrames[cooldownIndex]];
                }
            }
            if (currentFrame != null) {
                batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}
