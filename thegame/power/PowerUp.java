package thegame.power;

import thegame.object.GameObject;
import thegame.object.paddle.Paddle;
import thegame.object.ball.NormalBall;
import java.awt.Rectangle;

public abstract class PowerUp extends GameObject {
    protected boolean active = false;
    protected int speed = 3; // tốc độ rơi

    public PowerUp(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Cập nhật vị trí (rơi xuống)
    public void update() {
        y += speed;
    }

    // Khi chạm paddle
    public void activate(Paddle paddle, NormalBall ball) {
        if (!active) {
            active = true;
            applyEffect(paddle, ball);
        }
    }

    // Hiệu ứng khi được kích hoạt
    protected abstract void applyEffect(Paddle paddle,NormalBall  ball);

    // Nếu bạn muốn hiệu ứng chỉ tạm thời, override để xóa hiệu ứng
    protected void removeEffect(Paddle paddle, NormalBall ball) {}

    // Lấy hình chữ nhật để kiểm tra va chạm
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() {
        return y;
    }

    public boolean isActive() {
        return active;
    }
}
