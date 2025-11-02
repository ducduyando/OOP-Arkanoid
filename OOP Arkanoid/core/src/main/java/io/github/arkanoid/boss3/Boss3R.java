package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import io.github.arkanoid.entities.MiniBoss;

import static io.github.arkanoid.core.Constants.*;

public class Boss3R extends MiniBoss {

    private Boss3 boss3;
    private Boss3RSkill boss3SkillRightHand;
    private Boss3RandomSkill boss3NormalRandomSkill;

    public Boss3R(Boss3 boss3, int maxHp) {
        super("3R", BOSS3_RIGHT_WIDTH, BOSS3_RIGHT_HEIGHT, maxHp);
        this.boss3 = boss3;
        boss3SkillRightHand = new Boss3RSkill(boss3, this);
        boss3NormalRandomSkill = new Boss3RandomSkill(this);


        float x = boss3.getX() + BOSS3_WIDTH;
        float y = boss3.getHeight();
        setPosition(x, y);
        hitBox = new Rectangle(x, y, getWidth(), getHeight());
        setSize(BOSS3_RIGHT_WIDTH, BOSS3_RIGHT_HEIGHT);
        setOrigin(BOSS3_RIGHT_WIDTH / 2f, BOSS3_RIGHT_HEIGHT / 2f);

        boss3SkillRightHand.setNextSkill(boss3NormalRandomSkill);
        boss3NormalRandomSkill.setNextSkill(boss3SkillRightHand);

        setSkill(boss3NormalRandomSkill);

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
        float x = boss3.getX() + BOSS3_WIDTH;
        float y = boss3.getY();
        setPosition(x, y);
        super.act(delta);
    }
}
