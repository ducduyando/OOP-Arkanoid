package io.github.arkanoid;

public interface BarSkill {
    void update(Bar bar, float delta);
    void enter(Bar bar);
    void cleanup();
}
