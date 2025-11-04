package thegame.power;

import java.awt.Rectangle;
import thegame.object.GameObject;
import thegame.object.ball.NormalBall;
import thegame.object.paddle.Paddle;

public abstract class PowerUp extends GameObject {
    protected boolean active = false;
    protected int speed = 3; 

    public PowerUp(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update() {
        y += speed;
    }

    public void activate(Paddle paddle, NormalBall ball) {
        if (!active) {
            active = true;
            applyEffect(paddle, ball);
        }
    }

    protected abstract void applyEffect(Paddle paddle,NormalBall  ball);

    protected void removeEffect(Paddle paddle, NormalBall ball) {}

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