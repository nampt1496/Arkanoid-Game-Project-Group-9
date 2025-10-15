package Game.renderer;

import Game.object.ball.NormalBall;
import Game.object.brick.Brick;
import Game.object.paddle.Paddle;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class GameView extends JPanel {
    private Paddle paddle;
    private NormalBall ball;
    private ArrayList<Brick> bricks;
    private Image bgImg, paddleImg, ballImg, startImg;

    public GameView(Paddle paddle, NormalBall ball, ArrayList<Brick> bricks) {
        this.paddle = paddle;
        this.ball = ball;
        this.bricks = bricks;

        // Load ảnh
        bgImg = new ImageIcon(getClass().getResource("/Game/Picture/galaxybg.jpg")).getImage();
        paddleImg = new ImageIcon(getClass().getResource("/Game/Picture/paddle.png")).getImage();
        ballImg = new ImageIcon(getClass().getResource("/Game/Picture/ball.png")).getImage();
        startImg = new ImageIcon(getClass().getResource("/Game/Picture/start.png")).getImage();

    }

    
    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(paddleImg, paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight(), this);
        g.drawImage(ballImg, ball.getX(), ball.getY(), ball.getSize(), ball.getSize(), this);

        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                g.setColor(brick.getColor());
                g.fillRect(brick.x, brick.y, brick.width, brick.height);
                g.setColor(Color.WHITE);
                g.drawRect(brick.x, brick.y, brick.width, brick.height);
            }
        }
    }
}