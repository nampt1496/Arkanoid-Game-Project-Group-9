package thegame.object.ball;

import java.awt.*;
import thegame.animation.CollideAnimation;
import thegame.object.paddle.Paddle;

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
        if (x <= 25 || x >= panelWidth - SIZE - 25) {
            dx = -dx;
            CollideAnimation.playBallWall();
        }
        if (y <= 90) {
            dy = -dy;
            CollideAnimation.playBallWall();
        }
    }

    public void bounceOnPaddle(Paddle paddle) {
        CollideAnimation.playBallPaddle();
        dy = -Math.abs(dy);
        double hitPos = (x + SIZE / 2.0 - paddle.getX()) / paddle.getWidth() - 0.5;
        dx += hitPos * 2.0;
        y = paddle.getY() - SIZE;
    }

    public void bounceOnBrick(Rectangle brickRect) {
        Rectangle ballRect = getBounds();
        double ballCenterX = ballRect.getCenterX();
        double ballCenterY = ballRect.getCenterY();
        double brickCenterX = brickRect.getCenterX();
        double brickCenterY = brickRect.getCenterY();

        double dxFromBrick = ballCenterX - brickCenterX;
        double dyFromBrick = ballCenterY - brickCenterY;
        double absDX = Math.abs(dxFromBrick);
        double absDY = Math.abs(dyFromBrick);

        if (absDX > absDY) {
            dx = -dx;
            if (dxFromBrick > 0)
                x = brickRect.getMaxX() + 1; // đẩy ra ngoài
            else
                x = brickRect.getMinX() - SIZE - 1;
        } else {
            dy = -dy;
            if (dyFromBrick > 0)
                y = brickRect.getMaxY() + 1;
            else
                y = brickRect.getMinY() - SIZE - 1;
        }

        CollideAnimation.playBrickBreak();
    }


    public boolean isOutOfBounds(int height) {
        return y >= height;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, SIZE, SIZE);
    }

    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
    public int getSize() { return SIZE; }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setVelocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public int getRadius() { return SIZE / 2; }
}