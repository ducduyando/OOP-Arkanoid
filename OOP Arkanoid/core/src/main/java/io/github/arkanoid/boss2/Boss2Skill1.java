package io.github.arkanoid.boss2;

import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.arkanoid.entities.Boss;
import io.github.arkanoid.entities.BossSkill;
import java.util.Random;

import static io.github.arkanoid.core.Constants.*;

public class Boss2Skill1 implements BossSkill {
    private final Boss2 owner;
    private BossSkill nextSkill;
    private final BossRandomMovement movementSkill;
     private float time = 0f;
     private final float spawn_interval = 5f;

     private int beeCountToSpawn = 0;
     private int spawnBees = 0;
     private float spawnDelayTimer = 0f;
     private final float SPAWN_BEE_DELAY = 1f;
private boolean isSpawning = false;

     private final Random random = new Random();

     public Boss2Skill1(Boss2 owner, BossRandomMovement movementSkill) {
         this.owner = owner;
         this.movementSkill = movementSkill;
     }
     public void setNextSkill(BossSkill nextSkill) {
         this.nextSkill = nextSkill;
     }

     public void cleanup() {}
    public void enter(Boss boss) {
         time =0f;
         isSpawning =false;
        movementSkill.enter(boss);
    }
    public void update(Boss boss, float delta) {
         time += delta;
         // moi 5 giay la bat dau tha ong
        if(!isSpawning && time >= spawn_interval) {
            time =0f;
            isSpawning = true;
            beeCountToSpawn = random.nextInt(3) + 5;
            spawnBees =0;
            spawnDelayTimer = 0f;
        }
        // tha tung con ong lan luot
        if(isSpawning) {
            spawnDelayTimer += delta;
            if(spawnDelayTimer >= SPAWN_BEE_DELAY && spawnBees < beeCountToSpawn) {
                spawnDelayTimer = 0f;
                spawnBeeBeLowHp(boss.getStage());
                spawnBees++;
                if(spawnBees >= beeCountToSpawn) {
                    isSpawning = false;
                    time = 0f;
                    if (nextSkill != null) {
                        boss.setSkill(nextSkill);
                    }
                }
            }
        }
        if (!isSpawning) {
            movementSkill.update(boss, delta);
        }
    }
    // vi tri cac con ong con
    private void spawnBeeBeLowHp(Stage stage) {
        if(stage == null) return;
        stage.addActor(new BeeEnemy(random.nextFloat() * (SCREEN_WIDTH - 96), SCREEN_HEIGHT -HP_HEIGHT -96, "boss2/" + "skill" + "1" + ".png"));
    }

    // boss2 bi trung thi tha 1 con ong ngay duoi boss
    public void spawnBeeOnHit(Stage stage, float bossX, float bossY) {
        if (stage == null) return;
        stage.addActor(new BeeEnemy(bossX + BOSS2_WIDTH / 2f - 48, bossY, "boss2/"+ "skill" + "1" + ".png"));
    }


}
