package io.github.arkanoid.stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private Texture tex0, tex1, tex2;
    private Animation<TextureRegion> anim0, anim1, anim2;
    private Animation<TextureRegion>[] animations;

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
        tex0 = new Texture("scene0.png");
        tex1 = new Texture("scene1.png");
        tex2 = new Texture("scene2.png");

        TextureRegion[][] regions0 = TextureRegion.split(tex0, tex0.getWidth(), tex0.getHeight() / 4);
        anim0 = new Animation<>(Constants.FRAME_DURATION, flattenV(regions0));
        anim0.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] regions1 = TextureRegion.split(tex1, tex1.getWidth(), tex1.getHeight() / 8);
        anim1 = new Animation<>(Constants.FRAME_DURATION, flattenV(regions1));
        anim1.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] regions2 = TextureRegion.split(tex2, tex2.getWidth(), tex2.getHeight() / 4);
        anim2 = new Animation<>(Constants.FRAME_DURATION, flattenV(regions2));
        anim2.setPlayMode(Animation.PlayMode.NORMAL);

        animations = new Animation[]{anim0, anim1, anim2};
    }
    @Override
    public void update(float delta) {
        if (isFinished) {
            return;
        }
        stateTime += delta;
        Animation<TextureRegion> currentAnim = animations[currentAnimation];
        if(currentAnim.isAnimationFinished(stateTime)) {
            currentAnimation++;
            stateTime = 0;
        }

        if (currentAnimation >= animations.length) {
            isFinished = true;
        }
    }
    public void drawDirect(SpriteBatch batch) {
        if (isFinished || currentAnimation >= animations.length) {
            return;
        }

        // Xoa màn hình
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Lay frame hien tai
        Animation<TextureRegion> currentAnim = animations[currentAnimation];
        TextureRegion currentFrame = currentAnim.getKeyFrame(stateTime, false);

        // Ve frame ra giua man hinh
        float x = (Constants.SCREEN_WIDTH - currentFrame.getRegionWidth()) / 2f;
        float y = (Constants.SCREEN_HEIGHT - currentFrame.getRegionHeight()) / 2f;

        batch.draw(currentFrame, x, y);
    }

    @Override
    public void exit() {
        tex0.dispose();
       tex1.dispose();
        tex2.dispose();
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
