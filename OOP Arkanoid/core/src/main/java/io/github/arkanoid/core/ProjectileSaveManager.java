package io.github.arkanoid.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.arkanoid.boss1.BombProjectile;
import io.github.arkanoid.boss1.LaserEffect;
import io.github.arkanoid.boss2.BeeEnemy;

import java.util.ArrayList;
import java.util.List;

public class ProjectileSaveManager {
    public static class BeeData {
        public float x, y;

        public BeeData() {}

        public BeeData(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class BombData {
        public float x, y;
        public float stateTime;

        public BombData() {}

        public BombData(float x, float y, float stateTime) {
            this.x = x;
            this.y = y;
            this.stateTime = stateTime;
        }
    }

    public static class LaserData {
        public float x, y;
        public float stateTime; // For animation state

        public LaserData() {}

        public LaserData(float x, float y, float stateTime) {
            this.x = x;
            this.y = y;
            this.stateTime = stateTime;
        }
    }
    public static class RankEntry {
        public String name;
        public float time;
        public int stage;

        public RankEntry(String name, float time, int stage) {
            this.name = name;
            this.time = time;
            this.stage = stage;
        }
    }

    // Container for all projectile data
    public static class ProjectileData {
        public List<BeeData> bees = new ArrayList<>();
        public List<BombData> bombs = new ArrayList<>();
        public List<LaserData> lasers = new ArrayList<>();

        public boolean isEmpty() {
            return bees.isEmpty() && bombs.isEmpty() && lasers.isEmpty();
        }

        public int getTotalCount() {
            return bees.size() + bombs.size() + lasers.size();
        }
    }


    public static ProjectileData collectProjectiles(Stage stage) {
        ProjectileData data = new ProjectileData();

        if (stage == null) {
            return data;
        }

        for (Actor actor : stage.getActors()) {
            if (actor instanceof BeeEnemy bee) {
                data.bees.add(new BeeData(bee.getX(), bee.getY()));
            }
            else if (actor instanceof BombProjectile bomb) {
                data.bombs.add(new BombData(bomb.getX(), bomb.getY(), bomb.getStateTime()));
            }
            else if (actor instanceof LaserEffect laser) {
                data.lasers.add(new LaserData(laser.getX(), laser.getY(), laser.getStateTime()));
            }
        }

        return data;
    }


    public static void restoreProjectiles(Stage stage, ProjectileData data) {
        if (stage == null || data == null) {
            return;
        }

        for (BeeData beeData : data.bees) {
            BeeEnemy bee = new BeeEnemy(new Texture("Boss2/" + "skill" + "1" + ".png"),
                beeData.x,
                beeData.y);
            stage.addActor(bee);
        }

        for (BombData bombData : data.bombs) {
            BombProjectile bomb = new BombProjectile(
                new Texture("Boss1/" + "skill" + "1" + ".png"),
                bombData.x,
                bombData.y
            );
            bomb.setStateTime(bombData.stateTime);
            stage.addActor(bomb);
        }

        // Restore lasers
        for (LaserData laserData : data.lasers) {
            LaserEffect laser = new LaserEffect(
                new Texture("Boss1/" + "skill" + "2" + ".png"),
                laserData.x,
                laserData.y
            );
            laser.setStateTime(laserData.stateTime);
            stage.addActor(laser);
        }
    }

    public static void clearProjectiles(Stage stage) {
        if (stage == null) {
            return;
        }

        List<Actor> toRemove = new ArrayList<>();
        for (Actor actor : stage.getActors()) {
            if (actor instanceof BeeEnemy ||
                actor instanceof BombProjectile ||
                actor instanceof LaserEffect) {
                toRemove.add(actor);
            }
        }

        for (Actor actor : toRemove) {
            actor.remove();
        }
    }
}
