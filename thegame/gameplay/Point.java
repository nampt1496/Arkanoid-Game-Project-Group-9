package thegame.gameplay;

public class Point {
    private int score = 0;

    public void addScore(int value) {
        score += value;
    }

    public int getScore() {
        return score;
    }
}