package thegame.object.ball;

import java.awt.*;
import thegame.animation.CollideAnimation;
import thegame.object.brick.Brick;
import thegame.object.brick.UnbreakableBrick;
import thegame.object.paddle.Paddle;

public class NormalBall {
    private double x, y, dx, dy;
    private final int SIZE;
    private static final double FIXED_SPEED = 2.0;

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

        double speed = Math.sqrt(dx * dx + dy * dy);
        dx = dx / speed * FIXED_SPEED;
        dy = dy / speed * FIXED_SPEED;

        y = paddle.getY() - SIZE;
    }

    public void bounceOnBrick(Brick brick) {
        Rectangle brickRect = brick.getBounds();
        Rectangle ballRect = getBounds();
        double vx = dx;
        double vy = dy;

        double overlapLeft = ballRect.getMaxX() - brickRect.getMinX();
        double overlapRight = brickRect.getMaxX() - ballRect.getMinX();
        double overlapTop = ballRect.getMaxY() - brickRect.getMinY();
        double overlapBottom = brickRect.getMaxY() - ballRect.getMinY();

        double nx = 0, ny = 0;
        double minX = Math.min(overlapLeft, overlapRight);
        double minY = Math.min(overlapTop, overlapBottom);
        if (minX < minY)
            nx = (overlapLeft < overlapRight) ? -1 : 1;
        else
            ny = (overlapTop < overlapBottom) ? -1 : 1;

        double dot = vx * nx + vy * ny;
        dx = vx - 2 * dot * nx;
        dy = vy - 2 * dot * ny;

        double speed = Math.sqrt(dx * dx + dy * dy);
        dx = dx / speed * FIXED_SPEED;
        dy = dy / speed * FIXED_SPEED;

        if (nx != 0) x += nx * 2;
        if (ny != 0) y += ny * 2;
        if (brick instanceof UnbreakableBrick) {
            CollideAnimation.playBallWall(); 
        } else {
            CollideAnimation.playBrickBreak(); 
        }
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