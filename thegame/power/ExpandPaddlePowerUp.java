package thegame.power;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import thegame.object.ball.NormalBall;
import thegame.object.paddle.Paddle;
import java.util.ArrayList;
public class ExpandPaddlePowerUp extends PowerUp {

    private final Image icon;
    private final int EXPAND_AMOUNT = 50; // tăng chiều rộng

    public ExpandPaddlePowerUp(int x, int y, int width, int height) {
        super(x, y, Color.PINK);
        this.width = width;
        this.height = height;

        icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/thegame/Picture/Source/morong.png")
        );
    }

    @Override
    protected void applyEffect(Paddle paddle, ArrayList<NormalBall> balls) {
        final int DURATION_MS = 5000; // 5 giây
        paddle.expand(EXPAND_AMOUNT, DURATION_MS);
    }

    @Override
    public void removeEffect(Paddle paddle, ArrayList<NormalBall> balls) {
        paddle.resetToBase(); // trả paddle về baseWidth
    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(icon, x, y, width + 40, height, null);
    }
    @Override
    public void draw() {}
}
