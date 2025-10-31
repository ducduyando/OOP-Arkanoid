package io.github.arkanoid.boss3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

import static io.github.arkanoid.core.Constants.*;

public class MiniBoss extends Actor {
    public enum State {
        NORMAL,
        TAKING_DAMAGE,
        DYING
    }

    private final Boss3 boss3;

    private final Texture normalSprite;
    private final Texture takeDamageSprite;
    private final Texture deathSprite;
    private Rectangle hitbox;
    private Texture skillTexture;
    private String handKey;

    private int hp;
    private final int maxHp;

    private State state = State.NORMAL;
    private float stateTimer = 0f;
    private float takeDamageTimer = 0f;
    private float deathTimer = 0f;

    private boolean isVisible = false;
    private boolean isReadyToDeath = false;

    private TextureRegion currentFrame;

    private Animation<TextureRegion> normalAnimation;
    private Animation<TextureRegion> takeDamageAnimation;
    private Animation<TextureRegion> deathAnimation;

    private final float ROTATION_SPEED = 180f;

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean isReadyToDeath() {
        return isReadyToDeath;
    }

    public void takeDamage() {
        if (state == State.NORMAL) {
            hp--;
            isVisible = true;
            if (hp <= 0) {
                state = State.DYING;
                hp = 0;
            } else {
                state = State.TAKING_DAMAGE;
                takeDamageTimer = 0;
            }
        }
    }

    public MiniBoss(Boss3 boss3, String handKey, int maxHp) {
        this.boss3 = boss3;
        this.handKey = handKey;
        this.maxHp = maxHp;

        hp = maxHp;

        int normalMaxFrame;
        int takeDamageMaxFrame;
        int deathMaxFrame;

        if (handKey.equals("Right")) {

            normalSprite = new Texture("Boss3R/" + "normal" + ".png");
            takeDamageSprite = new Texture("Boss3R/" + "take_damage" + ".png");
            deathSprite = new Texture("Boss3R/" + "death" + ".png");
            skillTexture = new Texture("Boss3R/" + "skill" + ".png");

            normalMaxFrame = normalSprite.getWidth() / BOSS3_RIGHT_HAND_WIDTH;
            takeDamageMaxFrame = takeDamageSprite.getWidth() / BOSS3_RIGHT_HAND_WIDTH;
            deathMaxFrame = deathSprite.getWidth() / BOSS3_RIGHT_HAND_WIDTH;

            float x = boss3.getX() - BOSS3_RIGHT_HAND_WIDTH;
            float y = boss3.getHeight();
            setPosition(x, y);
            hitbox.setPosition(x, y);
            setSize(BOSS3_RIGHT_HAND_WIDTH, BOSS3_RIGHT_HAND_HEIGHT);
            setOrigin(BOSS3_RIGHT_HAND_WIDTH / 2f, BOSS3_RIGHT_HAND_HEIGHT / 2f);
        } else {

            normalSprite = new Texture("Boss3L/" + "normal" + ".png");
            takeDamageSprite = new Texture("Boss3L/" + "take_damage" + ".png");
            deathSprite = new Texture("Boss3L/" + "death" + ".png");
            skillTexture = new Texture("Boss3L/" + "skill" + ".png");

            normalMaxFrame = normalSprite.getWidth() / BOSS3_LEFT_HAND_WIDTH;
            takeDamageMaxFrame = takeDamageSprite.getWidth() / BOSS3_LEFT_HAND_WIDTH;
            deathMaxFrame = deathSprite.getWidth() / BOSS3_LEFT_HAND_WIDTH;

            float x = boss3.getX() + BOSS3_WIDTH;
            float y = boss3.getHeight();
            setPosition(x, y);
            hitbox.setPosition(x, y);
            setSize(BOSS3_LEFT_HAND_WIDTH, BOSS3_LEFT_HAND_HEIGHT);
            setOrigin(BOSS3_LEFT_HAND_WIDTH / 2f, BOSS3_LEFT_HAND_HEIGHT / 2f);
        }

        TextureRegion[] normalFrames = new TextureRegion[normalMaxFrame];
        for (int i = 0; i < normalMaxFrame; i++) {
            normalFrames[i] = new TextureRegion(normalSprite, i * getWidth(), 0, getWidth(), getHeight());
        }
        normalAnimation = new Animation<>(FRAME_DURATION, normalFrames);

        TextureRegion[] takeDamageFrames = new TextureRegion[takeDamageMaxFrame];
        for (int i = 0; i < takeDamageMaxFrame; i++) {
            takeDamageFrames[i] = new TextureRegion(takeDamageSprite, i * getWidth(), 0, getWidth(), getHeight());
        }
        takeDamageAnimation = new Animation<>(FRAME_DURATION, takeDamageFrames);

        TextureRegion[] deathFrames = new TextureRegion[deathMaxFrame];
        for (int i = 0; i < deathMaxFrame; i++) {
            deathFrames[i] = new TextureRegion(deathSprite, i * getWidth(), 0, getWidth(), getHeight());
        }
        deathAnimation = new Animation<>(FRAME_DURATION, deathFrames);
    }

    @Override
    public void act(float delta) {
        if (handKey.equals("Right")) {
            this.rotateBy(ROTATION_SPEED * delta);
            float x = boss3.getX() - BOSS3_RIGHT_HAND_WIDTH;
            float y = boss3.getY();
            setPosition(x, y);
            hitbox.setPosition(x, y);
        } else {
            float x = boss3.getX() + BOSS3_WIDTH;
            float y = boss3.getY();
            setPosition(x, y);
            hitbox.setPosition(x, y);
        }

        if (state == State.NORMAL) {
            stateTimer += delta;
            currentFrame = normalAnimation.getKeyFrame(stateTimer, true);
        }
        else if (state == State.TAKING_DAMAGE) {
            takeDamageTimer += delta;
            currentFrame = takeDamageAnimation.getKeyFrame(takeDamageTimer, false);
            if (takeDamageAnimation.isAnimationFinished(takeDamageTimer)) {
                takeDamageTimer = 0f;
                isVisible = false;
                state = State.NORMAL;
            }
        }
        else if (state == State.DYING) {
            deathTimer += delta;
            currentFrame = deathAnimation.getKeyFrame(deathTimer, false);
            if (deathAnimation.isAnimationFinished(deathTimer)) {
                deathTimer = 0f;
                isReadyToDeath = true;
                remove();
            }
        }


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
