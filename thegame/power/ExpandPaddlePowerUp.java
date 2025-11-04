package thegame.power;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import thegame.object.ball.NormalBall;
import thegame.object.paddle.Paddle;
public class ExpandPaddlePowerUp extends PowerUp {

    private final Image icon;
    private final int EXPAND_AMOUNT = 50; // tăng chiều rộng bao nhiêu pixel

    public ExpandPaddlePowerUp(int x, int y, int width, int height) {
        super(x, y, width, height);
        icon = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/thegame/Picture/Source/morong.png")
        );
    }

    @Override
    protected void applyEffect(Paddle paddle, NormalBall ball) {
        final int DURATION_MS = 5000; // 5 giây, chỉnh tuỳ bạn
        paddle.expand(EXPAND_AMOUNT, DURATION_MS);
    }

    @Override
    public void draw( ){}

    public void draw(Graphics g) {
        g.drawImage(icon, x, y, width+40, height, null);
    }
}
