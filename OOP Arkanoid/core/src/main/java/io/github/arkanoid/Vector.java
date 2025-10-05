package io.github.arkanoid;

public class Vector {
    private float x;
    private float y;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void plusX(float delta) {
        this.x += delta;
    }

    public void plusY(float delta) {
        this.y += delta;
    }

    public void mulX(float delta) {
        this.x *= delta;
    }

    public void mulY(float delta) {
        this.y *= delta;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }
}
