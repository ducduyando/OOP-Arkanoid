package io.github.arkanoid.entities;

public interface BossSkill {
    void update(Boss boss, float delta);
    void enter(Boss boss);
    void cleanup();
}
