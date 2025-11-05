package thegame.object;
import java.awt.*;
public abstract class GameObject {
    protected int x, y, width, height;
    public abstract void draw();
    public abstract void draw(Graphics g);
}
