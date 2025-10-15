package Game.object.ball;

import Game.object.paddle.Paddle;
import java.awt.*;

public class NormalBall {
    private double x, y, dx, dy;
    private final int SIZE;

    public NormalBall(double x, double y, int size, double dx, double dy) {
        this.x = x; this.y = y;
        this.SIZE = size;
        this.dx = dx; this.dy = dy;
    }

    public void move(int panelWidth, int panelHeight) {
        x += dx;
        y += dy;
        if (x <= 0 || x >= panelWidth - SIZE) dx = -dx;
        if (y <= 0) dy = -dy;
    }

    public void bounceOnPaddle(Paddle paddle) {
        dy = -Math.abs(dy);
        double hitPos = (x + SIZE / 2.0 - paddle.getX()) / paddle.getWidth() - 0.5;
        dx += hitPos * 2.0;
        y = paddle.getY() - SIZE;
    }

    public void bounceOnBrick() {
        dy = -dy;
    }

    public boolean isOutOfBounds(int height) {
        return y >= height;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, SIZE, SIZE);
    }

    public void reset() {
        x = 820; y = 400;
        dx = 1.9; dy = 1.9;
    }

    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
    public int getSize() { return SIZE; }
}