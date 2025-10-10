package io.github.arkanoid;

import com.badlogic.gdx.graphics.Texture;

import static io.github.arkanoid.Constants.*;

public class Boss1 extends Boss {
    private final Texture bombTexture;
    private final Texture laserTexture;

    Boss1(Texture texture, Texture bombTexture, Texture laserTexture, float x, float y, int maxHp) {
        super(texture ,x, y, BOSS1_WIDTH, BOSS1_HEIGHT, BOSS1_VELOCITY_X, BOSS1_VELOCITY_Y, maxHp);
        this.bombTexture = bombTexture;
        this.laserTexture = laserTexture;

        Boss1Centering centering = new Boss1Centering(this);
        Boss1BombingSkill bombingSkill = new Boss1BombingSkill(this);
        Boss1SweepingLaser laserSkill = new Boss1SweepingLaser(this);

        centering.setNextSkills(bombingSkill, laserSkill);
        bombingSkill.setNextSkill(centering);
        laserSkill.setNextSkill(centering);

        setSkill(centering);
    }

    public Texture getBombTexture() {
        return bombTexture;
    }

    public Texture getLaserTexture() {
        return laserTexture;
    }

    public void dropBomb() {
        if (getStage() != null) {
            getStage().addActor(new BombProjectile(this.bombTexture, getX() + getWidth() / 2, getY()));
        }
    }

}
