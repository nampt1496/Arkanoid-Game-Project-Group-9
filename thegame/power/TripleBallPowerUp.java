package thegame.power;

import java.awt.*;
import java.util.ArrayList;
import thegame.object.ball.NormalBall;
import thegame.object.paddle.Paddle;

public class TripleBallPowerUp extends PowerUp {
    private Image image;
    public TripleBallPowerUp(int x, int y) {
        super(x, y, Color.ORANGE); 
        image = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/thegame/Picture/Source/babong.png")
        );
    }

    @Override
    protected void applyEffect(Paddle paddle, ArrayList<NormalBall> balls) {
        if (balls.size() >= 3) return;

        int size = balls.get(0).getSize();

        double baseX = paddle.getX() + paddle.getWidth() / 2.0;
        double baseY = paddle.getY() - size - 5;

        if (balls.size() == 1) {
            NormalBall leftBall = new NormalBall(baseX - 10, baseY, size, -2, -2);
            NormalBall rightBall = new NormalBall(baseX + 10, baseY, size, 2, -2);

            balls.add(leftBall);
            balls.add(rightBall);
        }
        else if (balls.size() == 2) {
            NormalBall middleBall = new NormalBall(baseX, baseY, size, 0, -2);
            balls.add(middleBall);
        }
    }

    @Override
    public void removeEffect(Paddle paddle, ArrayList<NormalBall> balls) {
    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width + 40, height, null);
    }
    @Override
    public void draw(){}
}
