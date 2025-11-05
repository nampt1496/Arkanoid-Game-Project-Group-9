package thegame.power;

import java.awt.*;
import java.util.ArrayList;
import thegame.animation.CollideAnimation;
import thegame.object.ball.NormalBall;
import thegame.object.paddle.Paddle;

public class AddScorePowerUp extends PowerUp {
    private final Image image;
    private final int scoreBonus; 

    public AddScorePowerUp(int x, int y, int width, int height, int scoreBonus) {
        super(x, y, Color.YELLOW);
        this.width = width;
        this.height = height;
        this.scoreBonus = scoreBonus;

        String imgPath;
        if (scoreBonus == 100) {
            imgPath = "/thegame/Picture/Source/a100.png";
        } else if (scoreBonus == 250) {
            imgPath = "/thegame/Picture/Source/a250.png";
        } else if (scoreBonus == -1000) {
            imgPath = "/thegame/Picture/Source/a1000.png";
        }else if (scoreBonus == -500) {
            imgPath = "/thegame/Picture/Source/a5000.png";
        }else {
            imgPath = "/thegame/Picture/Source/a500.png";
        }

        image = Toolkit.getDefaultToolkit().getImage(
            getClass().getResource(imgPath)
        );
    }

    public int getScoreBonus() {
        return scoreBonus;
    }

    @Override
    protected void applyEffect(Paddle paddle, ArrayList<NormalBall> balls) {
        CollideAnimation.playAddScore(); 
    }

    @Override
    public void removeEffect(Paddle paddle, ArrayList<NormalBall> balls) {
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width + 40, height, null);
    }

    @Override
    public void draw() {}
}