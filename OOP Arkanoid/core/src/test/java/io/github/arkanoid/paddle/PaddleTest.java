
package io.github.arkanoid.paddle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import io.github.arkanoid.GdxTestRunner; // <-- Kế thừa lớp setup
import io.github.arkanoid.paddle.Paddle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// QUAN TRỌNG: Kế thừa GdxTestRunner
class PaddleTest extends GdxTestRunner {

    private Paddle paddle;
    private Texture dummyTexture;

    // Phương thức này chạy TRƯỚC MỖI @Test
    @BeforeEach
    void setUp() {
        // Tạo một Texture giả bằng cách tạo Pixmap 1x1 pixel
        // Thay vì load file không tồn tại
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        dummyTexture = new Texture(pixmap);
        pixmap.dispose();

        // Khởi tạo Paddle ở (100, 100)
        paddle = new Paddle(dummyTexture, 100, 100);
    }



    @Test
    void testPaddleInitialization() {
        assertNotNull(paddle);
        assertEquals(100, paddle.getX());
        assertEquals(100, paddle.getY());
        assertEquals(0, paddle.getState());
        assertFalse(paddle.isInvincible());
    }

    @Test
    void testTakeDamage_WhenNotInvincible() {
        // 1. Arrange (Sắp xếp)
        assertEquals(0, paddle.getState());
        assertFalse(paddle.isInvincible());

        // 2. Act (Hành động)
        paddle.takeDamage();

        // 3. Assert (Khẳng định)
        assertEquals(1, paddle.getState()); // Trạng thái tăng lên 1
        assertTrue(paddle.isInvincible()); // Trở nên bất tử
    }

    @Test
    void testTakeDamage_WhenInvincible() {
        // 1. Arrange
        paddle.setInvincible(true);
        paddle.setState(1);

        // 2. Act
        paddle.takeDamage();

        // 3. Assert
        assertEquals(1, paddle.getState());
        assertTrue(paddle.isInvincible());
    }

    @Test
    void testInvincibilityWearsOff_AfterAct() {
        // 1. Arrange
        paddle.takeDamage(); // Giờ paddle đang bất tử (state 1)
        assertTrue(paddle.isInvincible());

        // 2. Act
        // Chạy hàm act() trong 2 giây
        paddle.act(1.0f); // Tổng thời gian = 1.0s
        paddle.act(1.0f); // Tổng thời gian = 2.0s

        // Vẫn bất tử sau 2.0s
        assertTrue(paddle.isInvincible());

        // Chạy thêm 0.1s (vượt qua mốc 2f)
        paddle.act(0.1f); // Tổng thời gian = 2.1s

        // 3. Assert
        assertFalse(paddle.isInvincible()); // Không còn bất tử
        assertTrue(paddle.isVisible()); // Đảm bảo paddle hiện hình lại
    }

    @Test
    void testSetState_UpdatesHitBoxCorrectly() {
        // Giả sử PADDLE_WIDTH = 256 (bạn cần thay bằng giá trị hằng số của bạn)
        float PADDLE_WIDTH = 256; // Thay bằng Constants.PADDLE_WIDTH

        // 1. Arrange
        paddle.setPosition(100, 100);

        // 2. Act
        paddle.setState(2); // Set trạng thái 2

        // 3. Assert
        assertEquals(2, paddle.getState());

        // HitBox width = PADDLE_WIDTH - (64 * state)
        float expectedWidth = PADDLE_WIDTH - (64 * 2);
        assertEquals(expectedWidth, paddle.getHitBox().getWidth());

        // HitBox X = getX() + (32 * state)
        float expectedX = 100 + (32 * 2);
        assertEquals(expectedX, paddle.getHitBox().getX());
    }

    @Test
    void testGameOverLogic() {
        paddle.setState(6);
        assertFalse(paddle.isGameOver()); // Chưa game over

        paddle.setState(7);
        assertTrue(paddle.isGameOver()); // Đã game over
    }

    @Test
    void testGetHitBox_WhenGameOver() {
        // 1. Arrange
        paddle.setState(7); // Game over
        assertTrue(paddle.isGameOver());

        // 2. Act
        Rectangle hitBox = paddle.getHitBox();

        // 3. Assert
        // Dựa theo code của bạn, nó trả về 1 hình chữ nhật rỗng ở xa
        assertEquals(0, hitBox.getWidth());
        assertEquals(-1000, hitBox.getX());
    }
}
