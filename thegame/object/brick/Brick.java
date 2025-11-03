package thegame.object.brick;

import java.awt.*;

public class Brick {
    public int x, y, width, height;
    protected int hitsRequired;
    protected int hitsTaken = 0;
    protected Color color;

    public Brick(int x, int y, int width, int height, Color color, int hitsRequired) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        this.color = color; this.hitsRequired = hitsRequired;
    }

    public void hit() { hitsTaken++; }
    public boolean isDestroyed() { return hitsTaken >= hitsRequired; }
    public Color getColor() { return color; }
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
}
