package io.github.arkanoid.ui;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.arkanoid.paddle.Paddle;
import io.github.arkanoid.paddle.PaddleSkill1B;
import io.github.arkanoid.paddle.PaddleSkill1A;
import io.github.arkanoid.paddle.PaddleSkill2A;
import io.github.arkanoid.paddle.PaddleSkill2B;

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
                if (skill1A.isSkill1AReady() && skill1A.getCurrentPhase() == PaddleSkill1A.Phase.DONE) {
                    // Skill ready và không đang sử dụng
                    currentFrame = frames[0];
                } else if (skill1A.getCurrentPhase() == PaddleSkill1A.Phase.FIRING ||
                          skill1A.getCurrentPhase() == PaddleSkill1A.Phase.CHARGING) {
                    // During active use, keep default frame 0
                    currentFrame = frames[0];
                } else {
                    // Skill đang trong cooldown
                    float cooldown = paddle.getSkill1ACooldownTimer();
                    if (cooldown > 0) {
                        float completionRatio = 1.0f - (cooldown / MAX_COOLDOWN); // 0 = bắt đầu cooldown, 1 = sắp ready

                        int[] cooldownFrames = {4, 3, 2, 1}; // Frame 4 = cooldown mới bắt đầu, Frame 1 = sắp ready
                        int numCooldownFrames = cooldownFrames.length;

                        int cooldownIndex = (int) (completionRatio * numCooldownFrames);
                        cooldownIndex = Math.min(cooldownIndex, numCooldownFrames - 1);

                        currentFrame = frames[cooldownFrames[cooldownIndex]];
                    } else {
                        currentFrame = frames[0]; // Fallback to ready frame
                    }
                }
            } else if (!isSkill1ASelected && skill1B != null) {
                if (skill1B.isSkill1BReady() && skill1B.isDone()) {
                    // Skill ready và không đang sử dụng
                    currentFrame = frames[0];
                } else if (skill1B.getCurrentPhase() == PaddleSkill1B.Phase.FIRING ||
                          skill1B.getCurrentPhase() == PaddleSkill1B.Phase.CHARGING) {
                    // During active use, keep default frame 0
                    currentFrame = frames[0];
                } else {
                    // Skill đang trong cooldown
                    float cooldown = paddle.getSkill1BCooldownTimer();
                    if (cooldown > 0) {
                        float completionRatio = 1.0f - (cooldown / MAX_COOLDOWN); // 0 = bắt đầu cooldown, 1 = sắp ready

                        int[] cooldownFrames = {4, 3, 2, 1}; // Frame 4 = cooldown mới bắt đầu, Frame 1 = sắp ready
                        int numCooldownFrames = cooldownFrames.length;

                        int cooldownIndex = (int) (completionRatio * numCooldownFrames);
                        cooldownIndex = Math.min(cooldownIndex, numCooldownFrames - 1);

                        currentFrame = frames[cooldownFrames[cooldownIndex]];
                    } else {
                        currentFrame = frames[0]; // Fallback to ready frame
                    }
                }
            }

            if (currentFrame != null) {
                batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());

            }
        } else if (skillKey.equals("K")) {
            PaddleSkill2A skill2A = paddle.getSkill2A();
            PaddleSkill2B skill2B = paddle.getSkill2B();
            boolean isSkill2ASelected = paddle.isSkill2ASelected();

            if (isSkill2ASelected && skill2A != null) {
                if (skill2A.isSkill2AReady()) {
                    currentFrame = frames[0]; // Ready frame
                } else {
                    // Show cooldown animation - skill2A ngay lập tức vào cooldown sau khi fire
                    float cooldown = paddle.getSkill2ACooldownTimer();
                    if (cooldown > 0) {
                        float completionRatio = 1.0f - (cooldown / MAX_COOLDOWN); // 0 = bắt đầu cooldown, 1 = sắp ready

                        int[] cooldownFrames = {4, 3, 2, 1}; // Frame 4 = cooldown mới bắt đầu, Frame 1 = sắp ready
                        int numCooldownFrames = cooldownFrames.length;

                        int cooldownIndex = (int) (completionRatio * numCooldownFrames);
                        cooldownIndex = Math.min(cooldownIndex, numCooldownFrames - 1);

                        currentFrame = frames[cooldownFrames[cooldownIndex]];
                    } else {
                        currentFrame = frames[0]; // Fallback to ready frame
                    }
                }
            } else if (!isSkill2ASelected && skill2B != null) {
                if (skill2B.isSkill2BReady() && skill2B.isDone()) {
                    currentFrame = frames[0]; // Ready frame
                } else if (!skill2B.isDone()) {
                    // During active shield, keep default frame 0
                    currentFrame = frames[0];
                } else {
                    // Show cooldown animation - skill2B sử dụng logic tăng từ 0 lên MAX_COOLDOWN
                    float cooldown = paddle.getSkill2BCooldownTimer();
                    if (cooldown >= 0 && cooldown < PADDLE_SKILL_COOLDOWN) {
                        float completionRatio = 1.0f - cooldown / MAX_COOLDOWN; // 0 = bắt đầu cooldown, 1 = sắp ready

                        int[] cooldownFrames = {4, 3, 2, 1}; // Frame 4 = cooldown mới bắt đầu, Frame 1 = sắp ready
                        int numCooldownFrames = cooldownFrames.length;

                        int cooldownIndex = (int) (completionRatio * numCooldownFrames);
                        cooldownIndex = Math.min(cooldownIndex, numCooldownFrames - 1);

                        currentFrame = frames[cooldownFrames[cooldownIndex]];
                    } else {
                        currentFrame = frames[0]; // Fallback to ready frame
                    }
                }
            }

            if (currentFrame != null) {
                batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}
