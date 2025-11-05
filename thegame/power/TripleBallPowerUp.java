package thegame.power;

import java.awt.*;
import java.util.ArrayList;
import thegame.object.ball.NormalBall;
import thegame.object.paddle.Paddle;

public class TripleBallPowerUp extends PowerUp {
    private Image image;
    public TripleBallPowerUp(int x, int y) {
        super(x, y, Color.ORANGE); 
        image =   Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/thegame/Picture/Source/babong.png")
        );
    }

    @Override
    protected void applyEffect(Paddle paddle, ArrayList<NormalBall> balls) {
        if (balls.size() >= 3) return;

        ArrayList<NormalBall> newBalls = new ArrayList<>();

        double baseX = paddle.getX() + paddle.getWidth() / 2.0;
        double baseY = paddle.getY() - 20; 
        int size = balls.get(0).getSize(); 

        NormalBall leftBall = new NormalBall(baseX - 10, baseY, size, -2, -2);
        NormalBall rightBall = new NormalBall(baseX + 10, baseY, size, 2, -2);

        newBalls.add(leftBall);
        newBalls.add(rightBall);

        balls.addAll(newBalls);
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
