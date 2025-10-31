// Đặt trong: core/src/test/java/io/github/arkanoid/
package io.github.arkanoid; // Hoặc package gốc của bạn

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

/**
 * Lớp cơ sở (base class) này khởi tạo một môi trường GDX headless
 * để các lớp test khác có thể kế thừa và chạy.
 */
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
}
