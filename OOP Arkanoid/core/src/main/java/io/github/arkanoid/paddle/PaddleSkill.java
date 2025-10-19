package io.github.arkanoid.paddle;

public interface PaddleSkill {
    void update(Paddle paddle, float delta);
    void enter(Paddle paddle);
    void cleanup();
}
