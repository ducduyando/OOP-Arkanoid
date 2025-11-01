package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import io.github.arkanoid.entities.MiniBoss;
import io.github.arkanoid.entities.MiniBossSkill;

import static io.github.arkanoid.core.Constants.*;

public class Boss3RightHand extends MiniBoss {

    private Boss3 boss3;
    private Boss3SkillRightHand boss3SkillRightHand;
    private Boss3NormalRandomSkill boss3NormalRandomSkill;

    public Boss3RightHand(Boss3 boss3, int maxHp) {
        super("3R", BOSS3_RIGHT_HAND_WIDTH, BOSS3_RIGHT_HAND_HEIGHT, maxHp);
        this.boss3 = boss3;
        boss3SkillRightHand = new Boss3SkillRightHand(boss3, this);
        boss3NormalRandomSkill = new Boss3NormalRandomSkill(this);


        float x = boss3.getX() - BOSS3_RIGHT_HAND_WIDTH;
        float y = boss3.getHeight();
        setPosition(x, y);
        hitbox.setPosition(x, y);
        setSize(BOSS3_RIGHT_HAND_WIDTH, BOSS3_RIGHT_HAND_HEIGHT);
        setOrigin(BOSS3_RIGHT_HAND_WIDTH / 2f, BOSS3_RIGHT_HAND_HEIGHT / 2f);

        boss3SkillRightHand.setNextSkill(boss3NormalRandomSkill);
        boss3NormalRandomSkill.setNextSkill(boss3SkillRightHand);

        setSkill(boss3NormalRandomSkill);

    }



    public Boss3SkillRightHand getBoss3SkillRightHand() {
        return boss3SkillRightHand;
    }

    public Texture getSkillTexture() {
        return skillTexture;
    }

    public Texture getTargetTexture() {
        return targetTexture;
    }

    @Override
    public void act(float delta) {
        this.rotateBy(ROTATION_SPEED * delta);
        float x = boss3.getX() - BOSS3_RIGHT_HAND_WIDTH;
        float y = boss3.getY();
        setPosition(x, y);
        super.act(delta);
    }
}
