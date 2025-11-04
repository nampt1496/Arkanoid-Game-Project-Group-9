package thegame.power;

import thegame.object.paddle.Paddle;
import thegame.object.ball.NormalBall;

import java.awt.Graphics;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.Toolkit;
public class FastBallPowerUp extends PowerUp {
    private final Image icon;
    private final double SPEED_MULTIPLIER = 1.6;
    private final int DURATION_MS = 4000;
    @Override
    public void draw( ){}

    private double oldVx = 0;
    private double oldVy = 0;


    private boolean active = false;
    private Timer revertTimer;
    public FastBallPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height);
        icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/thegame/Picture/Source/nhanh.png")
        );
    }


    @Override
    protected void applyEffect(Paddle paddle, NormalBall ball) {

        double vx = ball.getDx();
        double vy = ball.getDy();

        if (!active) {
            // lưu lại để revert sau
            oldVx = vx;
            oldVy = vy;

            // đặt vận tốc mới: nhân cả hai thành phần
            ball.setVelocity(vx * SPEED_MULTIPLIER, vy * SPEED_MULTIPLIER);
            active = true;
        } else {
            // nếu đã active, không nhân thêm nữa - chỉ gia hạn timer
        }

        // (Hủy timer cũ nếu đang chạy)
        if (revertTimer != null && revertTimer.isRunning()) {
            revertTimer.stop();
        }

        // Tạo timer để revert về vận tốc cũ sau DURATION_MS
        revertTimer = new Timer(DURATION_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kiểm tra: nếu bóng vẫn còn (không null) thì revert
                // Đặt lại vận tốc về oldVx/oldVy (an toàn hơn so với trừ đi)
                ball.setVelocity(oldVx, oldVy);
                active = false;
                revertTimer.stop();
            }
        });
        revertTimer.setRepeats(false);
        revertTimer.start();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(icon, x, y, width+40, height, null);
    }
}
