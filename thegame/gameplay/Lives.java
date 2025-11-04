package thegame.gameplay;

public class Lives {
    private int lives = 3;
    public void loseLife() { if (lives > 0) lives--; }
}