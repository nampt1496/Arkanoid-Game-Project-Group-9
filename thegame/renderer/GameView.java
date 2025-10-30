package thegame.renderer;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import thegame.level.BaseLevel;
import thegame.object.ball.NormalBall;
import thegame.object.brick.Brick;
import thegame.object.paddle.Paddle;
import thegame.setting.SettingManager;

public class GameView extends JPanel {
    private Paddle paddle;
    private NormalBall ball;
    private ArrayList<Brick> bricks;
    private Image bgImg, paddleImg, ballImg, startImg, overImg, victoryImg;
    private BaseLevel baseLevel;
    private Image cachedBG;
    private Image[] brickImgs = new Image[6];

    public GameView(Paddle paddle, NormalBall ball, ArrayList<Brick> bricks, BaseLevel baseLevel) {
        this.paddle = paddle;
        this.ball = ball;
        this.bricks = bricks;
        this.baseLevel = baseLevel;
        setDoubleBuffered(true);

        bgImg = SettingManager.getBackgroundImage(700, 750);

        paddleImg = new ImageIcon(getClass().getResource("/thegame/Picture/paddle.png")).getImage();
        ballImg = new ImageIcon(getClass().getResource("/thegame/Picture/ball.png")).getImage();
        startImg = new ImageIcon(getClass().getResource("/thegame/Picture/start.png")).getImage();
        overImg = new ImageIcon(getClass().getResource("/thegame/Picture/gameOver.png")).getImage();
        victoryImg = new ImageIcon(getClass().getResource("/thegame/Picture/victory.png")).getImage();
        for (int i = 0; i < 6; i++) {
        brickImgs[i] = new ImageIcon(
            getClass().getResource("/thegame/Picture/b" + (i + 1) + ".png")
            ).getImage();
        }
    }

    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }

    public Image getBackgroundImage() {
        return bgImg;
    }

    public void refreshBackground() {
        this.bgImg = SettingManager.getBackgroundImage(700, 750);
        this.cachedBG = null; 
    }

    public void setBackgroundImage(Image newBg) {
        this.bgImg = newBg;
        this.cachedBG = null; 
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (cachedBG == null) {
            cachedBG = createImage(getWidth(), getHeight());
            Graphics2D gbg = (Graphics2D) cachedBG.getGraphics();
            gbg.drawImage(bgImg, 0, 70, getWidth(), getHeight(), this);
            gbg.dispose();
        }
        g2.drawImage(cachedBG, 0, 0, this);

        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            if (!brick.isDestroyed()) {
                int rowIndex = (brick.y - 120) / 33;
                if (rowIndex < 0) rowIndex = 0;
                if (rowIndex > 5) rowIndex = 5;

                g2.drawImage(
                    brickImgs[rowIndex],
                    brick.x, brick.y,
                    brick.width, brick.height,
                    this
                );
            }
        }

        g2.drawImage(paddleImg, paddle.getX(), paddle.getY(),
                paddle.getWidth(), paddle.getHeight(), this);
        g2.drawImage(ballImg, ball.getX(), ball.getY(),
                ball.getSize(), ball.getSize(), this);

        drawHUD(g2);
    }

    private void drawHUD(Graphics2D g2) {
        try {
            Font pixelFont = Font.createFont(
                Font.TRUETYPE_FONT,
                getClass().getResourceAsStream("/thegame/font/pixel2.otf")
            );
            pixelFont = pixelFont.deriveFont(23f);
            g2.setFont(pixelFont);
        } catch (Exception e) {
            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        }

        g2.setColor(new Color(0, 0, 0, 255));
        g2.fillRect(0, 0, getWidth(), 70);

        
        g2.setColor(Color.CYAN);
        g2.drawString("Player: " + baseLevel.getPlayer().getName(), 25, 30);

        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + baseLevel.getScore(), 25, 60);

        g2.setColor(Color.ORANGE);
        g2.drawString("Level: " + baseLevel.getLevelName(), getWidth() - 120, 30);

        g2.setColor(Color.PINK);
        g2.drawString("Lives: 3", getWidth() - 120, 60);
    }
}