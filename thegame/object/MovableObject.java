package thegame.object;

public abstract class MovableObject extends GameObject {
    protected int dx, dy;
    public abstract void move();
}