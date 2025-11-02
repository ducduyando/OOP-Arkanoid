package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import io.github.arkanoid.entities.MiniBoss;

import static io.github.arkanoid.core.Constants.*;

public class Boss3L extends MiniBoss {
    private Boss3 boss3;
    private Boss3Skill1A boss3LSkill;
    private Boss3RandomSkill boss3LRRandomSkill;

    public Boss3L(Boss3 boss3, int maxHp) {
        super("3L", BOSS3_LEFT_WIDTH, BOSS3_LEFT_HEIGHT, maxHp);
        this.boss3 = boss3;
        boss3LSkill = new Boss3Skill1A(boss3, this);
        boss3LRRandomSkill = new Boss3RandomSkill(this);

        float x = boss3.getX() - BOSS3_LEFT_WIDTH;
        float y = boss3.getY();
        setPosition(x, y);
        hitBox = new Rectangle(x, y, getWidth(), getHeight());
        setOrigin(BOSS3_LEFT_WIDTH / 2f, BOSS3_LEFT_HEIGHT / 2f);

        boss3LSkill.setNextSkill(boss3LRRandomSkill);
        boss3LRRandomSkill.setNextSkill(boss3LSkill);

        setSkill(boss3LRRandomSkill);
    }

    public Texture getSkillTexture() {
        return skillTexture;
    }

    public Texture getTargetTexture() {
        return targetTexture;
    }

    @Override
    public void act(float delta) {
        float x = boss3.getX() - BOSS3_LEFT_WIDTH;
        float y = boss3.getY();
        setPosition(x, y);
        super.act(delta);
    }
}
