
package io.github.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;


public abstract class GdxTestRunner {

    @BeforeAll
    public static void init() {
        // Tải các thư viện native
        HeadlessNativesLoader.load();

        // Khởi tạo ứng dụng không đầu
        new HeadlessApplication(new ApplicationAdapter() {});

        // ---- PHẦN QUAN TRỌNG NHẤT ----
        // Lớp Texture cần Gdx.gl để hoạt động.
        // Chúng ta phải "giả lập" (mock) nó.
        Gdx.gl = Mockito.mock(GL20.class);
    }


    protected static Texture createMockTexture(int width, int height) {
        // Tạo một Pixmap nhỏ với màu đỏ
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 0f, 0f, 1f); // Màu đỏ
        pixmap.fill();

        // Tạo Texture từ Pixmap
        Texture texture = new Texture(pixmap);
        pixmap.dispose(); // Giải phóng Pixmap

        return texture;
    }

    protected static Texture createMockTexture() {
        return createMockTexture(32, 32);
    }
}
