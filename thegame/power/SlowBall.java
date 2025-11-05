package thegame.power;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import thegame.object.ball.NormalBall;
import thegame.object.paddle.Paddle;

public class SlowBall extends PowerUp {
    private final Image icon;
    private final double SPEED_MULTIPLIER = 0.6;
    private final int DURATION_MS = 4000;

    public static boolean active = false;
    private Timer revertTimer;

    public SlowBall(int x, int y, int width, int height) {
        super(x, y, java.awt.Color.CYAN);
        this.width = width;
        this.height = height;

        icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/thegame/Picture/Source/cham.png")
        );
    }

    @Override
    protected void applyEffect(Paddle paddle, ArrayList<NormalBall> balls) {
        if (!active) {
            active = true;

            for (NormalBall ball : balls) {
                double vx = ball.getDx();
                double vy = ball.getDy();

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
