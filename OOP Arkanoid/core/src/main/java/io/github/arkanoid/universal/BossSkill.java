package io.github.arkanoid.universal;

public interface BossSkill {
    void update(Boss boss, float delta);
    void enter(Boss boss);
    void cleanup();
}
