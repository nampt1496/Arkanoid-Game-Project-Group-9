package thegame.power;

import thegame.object.paddle.Paddle;
import thegame.object.ball.NormalBall;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FastBallPowerUp extends PowerUp {
    private final Image icon;
    private final double SPEED_MULTIPLIER = 1.6;
    private final int DURATION_MS = 4000;

    private boolean active = false;
    private Timer revertTimer;

    public FastBallPowerUp(int x, int y, int width, int height) {
        super(x, y, java.awt.Color.CYAN);
        this.width = width;
        this.height = height;

        icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/thegame/Picture/Source/nhanh.png")
        );
    }

    @Override
    protected void applyEffect(Paddle paddle, ArrayList<NormalBall> balls) {
        if (!active) {
            active = true;

            // Nhân tốc độ của tất cả bóng hiện có
            for (NormalBall ball : balls) {
                double vx = ball.getDx();
                double vy = ball.getDy();

                // Nhân tốc độ
                ball.setVelocity(vx * SPEED_MULTIPLIER, vy * SPEED_MULTIPLIER);
            }
        }

        if (revertTimer != null && revertTimer.isRunning()) {
            revertTimer.stop();
        }

        revertTimer = new Timer(DURATION_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (NormalBall ball : balls) {
                    double vx = ball.getDx() / SPEED_MULTIPLIER;
                    double vy = ball.getDy() / SPEED_MULTIPLIER;
                    ball.setVelocity(vx, vy);
                }
                active = false;
                revertTimer.stop();
            }
        });
        revertTimer.setRepeats(false);
        revertTimer.start();
    }

    @Override
    public void removeEffect(Paddle paddle, ArrayList<NormalBall> balls) {
        for (NormalBall ball : balls) {
            double vx = ball.getDx() / SPEED_MULTIPLIER;
            double vy = ball.getDy() / SPEED_MULTIPLIER;
            ball.setVelocity(vx, vy);
        }
        active = false;
        if (revertTimer != null) revertTimer.stop();
    }
    @Override
    public void draw(Graphics g) {
        g.drawImage(icon, x, y, width + 40, height, null);
    }

    @Override
    public void draw() {}
}
