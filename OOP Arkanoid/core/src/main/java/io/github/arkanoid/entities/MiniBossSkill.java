package io.github.arkanoid.entities;

public interface MiniBossSkill {
    void update(MiniBoss miniBoss, float delta);
    void enter(MiniBoss miniBoss);
    void cleanup();
}
