package thegame.gameplay;

import thegame.animation.CollideAnimation;

public class Lives {
    private int lives = 3;

    public Lives() {}

    public Lives(int lives) {
        this.lives = lives;
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
            CollideAnimation.playLose();
        }
    }

    public int getLives() {
        return lives;
    }

    public void reset() {
        lives = 3;
    }
}
