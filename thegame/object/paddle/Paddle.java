package thegame.object.paddle;

import java.awt.*;
import javax.swing.Timer;

public class Paddle {
    private int x, y, width, height;
    private boolean leftPressed, rightPressed;
    private final int SPEED = 5;


    private final int baseWidth;
    private boolean expanded = false;
    private Timer expandTimer;

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

    public void setWidth(int newWidth) {
        if (newWidth < MIN_WIDTH) newWidth = MIN_WIDTH;
        if (newWidth > MAX_WIDTH) newWidth = MAX_WIDTH;

        int center = x + width / 2;
        this.width = newWidth;
        this.x = center - width / 2;

        
        if (x < 25) x = 25;
        if (x > 675 - width) x = 675 - width;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public int getBaseWidth() { return baseWidth; }

    /**
     * Mở rộng paddle tạm thời.
     */
    public void expand(int amount, int durationMs) {
        if (!expanded) {
            setWidth(this.width + amount); 
            expanded = true;
        }
        

        if (expandTimer != null && expandTimer.isRunning()) {
            expandTimer.stop();
        }

        expandTimer = new Timer(durationMs, e -> {
            setWidth(baseWidth);
            expanded = false;
            expandTimer.stop();
        });
        expandTimer.setRepeats(false);
        expandTimer.start();
    }

    
    public void resetToBase() {
        if (expandTimer != null && expandTimer.isRunning()) expandTimer.stop();
        setWidth(baseWidth);
        expanded = false;
    }
    public void shrink(int amount) {
        setWidth(this.width - amount);
        expanded = false;

        if (expandTimer != null && expandTimer.isRunning()) {
            expandTimer.stop();
        }
    }
}
