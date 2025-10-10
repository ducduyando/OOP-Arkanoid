package io.github.arkanoid;

public interface BossSkill {
    void update(Boss boss, float delta);
    void enter(Boss boss);
}
