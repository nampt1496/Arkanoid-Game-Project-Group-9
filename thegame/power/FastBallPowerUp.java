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

    // Lưu lại vận tốc cũ của các bóng để revert
    private ArrayList<double[]> oldVelocities = new ArrayList<>();

    public FastBallPowerUp(int x, int y, int width, int height) {
        super(x, y, java.awt.Color.CYAN); // hoặc Color.ORANGE, tùy bạn thích
        this.width = width;   // thêm 2 dòng này để PowerUp biết kích thước riêng
        this.height = height;

        icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/thegame/Picture/Source/nhanh.png")
        );
    }

    @Override
    protected void applyEffect(Paddle paddle, ArrayList<NormalBall> balls) {
        if (!active) {
            active = true;
            oldVelocities.clear();

            // ✅ Nhân tốc độ của tất cả bóng hiện có
            for (NormalBall ball : balls) {
                double vx = ball.getDx();
                double vy = ball.getDy();

                // Lưu lại vận tốc gốc để revert sau
                oldVelocities.add(new double[]{vx, vy});

                // Nhân tốc độ
                ball.setVelocity(vx * SPEED_MULTIPLIER, vy * SPEED_MULTIPLIER);
            }
        }

        // Nếu timer cũ đang chạy, dừng lại để gia hạn thời gian
        if (revertTimer != null && revertTimer.isRunning()) {
            revertTimer.stop();
        }

        // ✅ Sau DURATION_MS giây thì revert về tốc độ ban đầu
        revertTimer = new Timer(DURATION_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < balls.size() && i < oldVelocities.size(); i++) {
                    double[] oldV = oldVelocities.get(i);
                    NormalBall b = balls.get(i);
                    b.setVelocity(oldV[0], oldV[1]);
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
        for (int i = 0; i < balls.size() && i < oldVelocities.size(); i++) {
            double[] oldV = oldVelocities.get(i);
            NormalBall b = balls.get(i);
            b.setVelocity(oldV[0], oldV[1]);
        }
        active = false;
        if (revertTimer != null) revertTimer.stop();
    }
    @Override
    public void draw(Graphics g) {
        g.drawImage(icon, x, y, width + 40, height, null);
    }

    @Override
    public void draw() {} // Có thể bỏ nếu không cần
}
