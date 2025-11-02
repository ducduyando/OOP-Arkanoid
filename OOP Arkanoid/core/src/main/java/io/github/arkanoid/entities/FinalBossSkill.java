package io.github.arkanoid.entities;

public interface FinalBossSkill {
    void update(FinalBoss finalBoss, float delta);
    void enter(FinalBoss finalBoss);
    void cleanup();
}
