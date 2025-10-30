package thegame.object.paddle;

import java.awt.*;

public class Paddle {
    private int x, y, width, height;
    private boolean leftPressed, rightPressed;
    private final int SPEED = 7;

    public Paddle(int x, int y, int width, int height) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
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
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}