package thegame.object.paddle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Paddle {
    private int x, y, width, height;
    private boolean leftPressed, rightPressed;
    private final int SPEED = 7;


    private final int baseWidth;
    private boolean expanded = false;
    private Timer expandTimer;

    // Giới hạn min/max width (tránh quá nhỏ hoặc quá lớn)
    private final int MIN_WIDTH = 30;
    private final int MAX_WIDTH = 600;

    public Paddle(int x, int y, int width, int height) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.baseWidth = width;
    }

    public void update() {
        if (leftPressed) x = Math.max(25, x - SPEED);
        if (rightPressed) x = Math.min(675 - width, x + SPEED);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setLeftPressed(boolean b) { leftPressed = b; }
    public void setRightPressed(boolean b) { rightPressed = b; }

    public int getX() { return x; }
    public int getY() { return y; }

    // Sử dụng setWidth nội bộ có clamp
    public void setWidth(int newWidth) {
        if (newWidth < MIN_WIDTH) newWidth = MIN_WIDTH;
        if (newWidth > MAX_WIDTH) newWidth = MAX_WIDTH;

        // Giữ tâm paddle (center) khi đổi width
        int center = x + width / 2;
        this.width = newWidth;
        this.x = center - width / 2;

        // Clamp vị trí x để paddle không ra ngoài biên (biên game dựa trên GameView của bạn)
        // 25 là biên trái (theo logic cũ), 675 là giới hạn phải trước đó.
        if (x < 25) x = 25;
        if (x > 675 - width) x = 675 - width;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // Mới: trả về baseWidth (cần cho giảm kích thước nếu dùng stacking)
    public int getBaseWidth() { return baseWidth; }

    /**
     * Mở rộng paddle tạm thời.
     */
    public void expand(int amount, int durationMs) {
        if (!expanded) {
            setWidth(this.width + amount);  // tăng width lần đầu
            expanded = true;
        }
        // nếu đang expanded thì không cộng thêm, chỉ reset timer

        // Hủy timer cũ nếu còn chạy
        if (expandTimer != null && expandTimer.isRunning()) {
            expandTimer.stop();
        }

        // Tạo timer mới
        expandTimer = new Timer(durationMs, e -> {
            setWidth(baseWidth);
            expanded = false;
            expandTimer.stop();
        });
        expandTimer.setRepeats(false);
        expandTimer.start();
    }

    /**
     * Nếu muốn reset ngay về kích thước gốc (ví dụ khi đổi level)
     */
    public void resetToBase() {
        if (expandTimer != null && expandTimer.isRunning()) expandTimer.stop();
        setWidth(baseWidth);
        expanded = false;
    }
    public void shrink(int amount) {
        setWidth(this.width - amount);
        expanded = false;

        // Nếu có timer đang chạy cho mở rộng, dừng lại
        if (expandTimer != null && expandTimer.isRunning()) {
            expandTimer.stop();
        }
    }
}
