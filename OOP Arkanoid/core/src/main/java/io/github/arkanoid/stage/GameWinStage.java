package io.github.arkanoid.stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.arkanoid.core.Constants;

public class GameWinStage implements GameStage {
    private Stage stage;
    private boolean isFinished = false;

    private Texture texture0, texture1, texture2;
    private Animation<TextureRegion> animation0, animation1, animation2;
    private Animation<TextureRegion>[] animation;

    private int currentAnimation = 0;
    private float stateTime = 0f;

    public GameWinStage() {

    }

    private TextureRegion[] flattenV(TextureRegion[][] regions) {
        TextureRegion[] flat = new TextureRegion[regions.length];
        for(int i = 0; i< regions.length; i++) {
            flat[i] = regions[i][0];
        }
        return flat;
    }
    @Override
    public void enter() {
        this.stage = new Stage(new ScreenViewport());
        this.isFinished = false;
        this.currentAnimation = 0;
        this.stateTime = 0;
        texture0 = new Texture("Win/" + "scene0" + ".png");
        texture1 = new Texture("Win/" + "scene1" + ".png");
        texture2 = new Texture("Win/" + "scene2" + ".png");

        TextureRegion[][] regions0 = TextureRegion.split(texture0, texture0.getWidth(), texture0.getHeight() / 4);
        animation0 = new Animation<>(Constants.FRAME_DURATION * 3, flattenV(regions0));
        animation0.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] regions1 = TextureRegion.split(texture1, texture1.getWidth(), texture1.getHeight() / 8);
        animation1 = new Animation<>(Constants.FRAME_DURATION * 3, flattenV(regions1));
        animation1.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] regions2 = TextureRegion.split(texture2, texture2.getWidth(), texture2.getHeight() / 4);
        animation2 = new Animation<>(Constants.FRAME_DURATION * 3, flattenV(regions2));
        animation2.setPlayMode(Animation.PlayMode.NORMAL);

        animation = new Animation[]{animation0, animation1, animation2};
    }
    @Override
    public void update(float delta) {
        if (isFinished) {
            return;
        }
        stateTime += delta;
        Animation<TextureRegion> currentAnim = animation[currentAnimation];
        if(currentAnim.isAnimationFinished(stateTime)) {
            currentAnimation++;
            stateTime = 0;
        }

        if (currentAnimation >= animation.length) {
            isFinished = true;
        }
    }
    public void drawDirect(SpriteBatch batch) {
        if (isFinished || currentAnimation >= animation.length) {
            return;
        }

        // Xoa màn hình
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Lay frame hien tai
        Animation<TextureRegion> currentAnim = animation[currentAnimation];
        TextureRegion currentFrame = currentAnim.getKeyFrame(stateTime, false);

        // Ve frame ra giua man hinh
        float x = (Constants.SCREEN_WIDTH - currentFrame.getRegionWidth()) / 2f;
        float y = (Constants.SCREEN_HEIGHT - currentFrame.getRegionHeight()) / 2f;

        batch.draw(currentFrame, x, y);
    }

    @Override
    public void exit() {
        texture0.dispose();
       texture1.dispose();
        texture2.dispose();
         stage.dispose();
    }

    @Override
    public Stage getGdxStage() {
        return stage;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
 }
