package io.github.arkanoid.ui;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.paddle.Paddle;
import io.github.arkanoid.paddle.PaddleSkill1B;
import io.github.arkanoid.paddle.PaddleSkill1A;

import static io.github.arkanoid.core.Constants.*;

public class  SkillIcon extends Actor {
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
            PaddleSkill1A skill1A = paddle.getSkill1A();
            PaddleSkill1B skill1B = paddle.getSkill1B();
            boolean isSkill1ASelected = paddle.isSkill1ASelected();

            if (isSkill1ASelected && skill1A != null) {
                if (skill1A.getCurrentPhase() == PaddleSkill1A.Phase.FIRING ||
                    skill1A.getCurrentPhase() == PaddleSkill1A.Phase.CHARGING) {
                    
                    float firingTime = skill1A.getSkill1AFiringTime();
                    float maxFiringDuration = PaddleSkill1A.getSKILL1A_FIRING_DURATION();
                    float completionRatio = firingTime / maxFiringDuration;

                    int[] firingFrames = {1, 2, 3, 4};
                    int numFiringFrames = firingFrames.length;
                    int firingIndex = (int) (completionRatio * numFiringFrames);
                    firingIndex = Math.min(firingIndex, numFiringFrames - 1);

                    currentFrame = frames[firingFrames[firingIndex]];
                } else if (skill1A.isSkill1AReady()) {
                    currentFrame = frames[0]; // Ready frame
                } else {
                    // Show cooldown animation
                    float cooldown = paddle.getSkill1ACooldownTimer();
                    float elapsedTime = MAX_COOLDOWN - cooldown;
                    float completionRatio = elapsedTime / MAX_COOLDOWN;

                    int[] cooldownFrames = {1, 2, 3, 4};
                    int numCooldownFrames = cooldownFrames.length;

                    int cooldownIndex = (int) (completionRatio * numCooldownFrames);
                    cooldownIndex = Math.min(cooldownIndex, numCooldownFrames - 1);

                    currentFrame = frames[cooldownFrames[cooldownIndex]];
                }
            } else if (!isSkill1ASelected && skill1B != null) {
                // Handle Skill1B (Laser skill)
                if (skill1B.getCurrentPhase() == PaddleSkill1B.Phase.FIRING ||
                    skill1B.getCurrentPhase() == PaddleSkill1B.Phase.CHARGING) {

                    float laserTime = skill1B.getPaddleLaserTime();
                    float maxLaserDuration = PaddleSkill1B.getSKILL1B_LASER_DURATION();
                    float completionRatio = laserTime / maxLaserDuration;

                    int[] firingFrames = {1, 2, 3, 4};
                    int numFiringFrames = firingFrames.length;
                    int firingIndex = (int) (completionRatio * numFiringFrames);
                    firingIndex = Math.min(firingIndex, numFiringFrames - 1);

                    currentFrame = frames[firingFrames[firingIndex]];
                } else if (skill1B.isSkill1BReady()) {
                    currentFrame = frames[0]; // Ready frame
                } else {
                    // Show cooldown animation
                    float cooldown = paddle.getSkill1BCooldownTimer();
                    float elapsedTime = MAX_COOLDOWN - cooldown;
                    float completionRatio = elapsedTime / MAX_COOLDOWN;

                    int[] cooldownFrames = {1, 2, 3, 4};
                    int numCooldownFrames = cooldownFrames.length;

                    int cooldownIndex = (int) (completionRatio * numCooldownFrames);
                    cooldownIndex = Math.min(cooldownIndex, numCooldownFrames - 1);

                    currentFrame = frames[cooldownFrames[cooldownIndex]];
                }
            }

            if (currentFrame != null) {
                batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
            }
        } else if (skillKey.equals("K")) {
            // Handle Skill2 if needed
        }
    }
}
