package thegame.power;

import thegame.object.paddle.Paddle;
import thegame.object.ball.NormalBall;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FastBallPowerUp extends PowerUp {
    private final double SPEED_MULTIPLIER = 1.6; // nhân tốc độ lên 1.6 lần (thay đổi tuỳ thích)
    private final int DURATION_MS = 4000; // 4 giây
    @Override
    public void draw( ){}
    // Lưu velocity ban đầu để revert
    private double oldVx = 0;
    private double oldVy = 0;

    // Trạng thái active và timer để revert
    private boolean active = false;
    private Timer revertTimer;
    public FastBallPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Khi power-up được kích hoạt (đã va chạm với paddle).
     * Lưu ý: chúng ta dùng các getter/setter của NormalBall: getVx(), getVy(), setVelocity(vx, vy).
     * Nếu NormalBall chưa có getVx()/getVy(), xem phần hướng dẫn bên dưới.
     */
    @Override
    protected void applyEffect(Paddle paddle, NormalBall ball) {
        // Nếu chưa có getter cho velocity, bạn sẽ cần thêm vào NormalBall.
        // Lấy vận tốc hiện tại (giả sử NormalBall có getVx/getVy)
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
        g.setColor(Color.ORANGE); // màu khác để phân biệt với Expand (xanh lá)
        g.fillOval(x, y, width, height); // vẽ hình tròn cho variety
    }
}
