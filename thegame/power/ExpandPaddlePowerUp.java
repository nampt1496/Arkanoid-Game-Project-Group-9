package thegame.power;

import thegame.object.paddle.Paddle;
import thegame.object.ball.NormalBall;
import java.awt.Color;
import java.awt.Graphics;

public class ExpandPaddlePowerUp extends PowerUp {

    private final int EXPAND_AMOUNT = 50; // tăng chiều rộng bao nhiêu pixel

    public ExpandPaddlePowerUp(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void applyEffect(Paddle paddle, NormalBall ball) {
        final int DURATION_MS = 5000; // 5 giây, chỉnh tuỳ bạn
        paddle.expand(EXPAND_AMOUNT, DURATION_MS);
    }

    @Override
    public void draw( ){}

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, width, height);
    }
}
