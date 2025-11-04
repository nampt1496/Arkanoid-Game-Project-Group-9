package thegame.object.brick;
import java.awt.*;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color, Integer.MAX_VALUE);
    }

    public void hit() {
    }

    public boolean isDestroyed() {
        return false;
    }
}
