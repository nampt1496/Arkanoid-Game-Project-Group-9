package thegame.power;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import thegame.object.GameObject;
import thegame.object.ball.NormalBall;
import thegame.object.paddle.Paddle;

public abstract class PowerUp extends GameObject {
    protected boolean active = false;
    protected int speed = 1;
    protected Color color = Color.WHITE;
    protected final int WIDTH = 20;
    protected final int HEIGHT = 20;

    public PowerUp(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.width = WIDTH;
        this.height = HEIGHT;
    }

    public void update() {
        y += speed; // rơi xuống
    }

    /**
     * Kích hoạt power-up cho paddle và toàn bộ danh sách bóng.
     */
    public void activate(Paddle paddle, ArrayList<NormalBall> balls) {
        if (!active) {
            active = true;
            applyEffect(paddle, balls);
        }
    }

    /**
     * Mỗi power-up con sẽ định nghĩa hành vi riêng ở đây.
     */
    protected abstract void applyEffect(Paddle paddle, ArrayList<NormalBall> balls);

    public abstract void removeEffect(Paddle paddle, ArrayList<NormalBall> balls);

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

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
