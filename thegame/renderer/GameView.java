package thegame.renderer;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import thegame.level.BaseLevel;
import thegame.object.ball.NormalBall;
import thegame.object.brick.Brick;
import thegame.object.brick.StrongBrick;
import thegame.object.brick.UnbreakableBrick;
import thegame.object.paddle.Paddle;
import thegame.power.PowerUp;
import thegame.setting.SettingManager;

public class GameView extends JPanel {
    private Paddle paddle;
    private ArrayList<NormalBall> balls;
    private ArrayList<Brick> bricks;
    private Image bgImg, ballImg, startImg, overImg, victoryImg;
    private Image[] paddleImgs = new Image[3];
    private BaseLevel baseLevel;
    private Image cachedBG;
    private Image[] brickImgs = new Image[6];
    private int currentFrame = 0;
    private Timer animationTimer;
    private ArrayList<PowerUp> powerUps;

    public GameView(Paddle paddle,  ArrayList<NormalBall> balls, ArrayList<Brick> bricks, BaseLevel baseLevel) {
        this.paddle = paddle;
        this.balls = balls;
        this.bricks = bricks;
        this.baseLevel = baseLevel;
        this.powerUps = new ArrayList<>();

        setDoubleBuffered(true);

        bgImg = SettingManager.getBackgroundImage(700, 750);

        for (int i = 0; i < 3; i++) {
            paddleImgs[i] = new ImageIcon(
                    getClass().getResource("/thegame/Picture/paddle" + (i + 1) + ".png")
            ).getImage();
        }
        animationTimer = new Timer(120, e -> {
            currentFrame = (currentFrame + 1) % paddleImgs.length;
        });
        animationTimer.start();

        ballImg = new ImageIcon(getClass().getResource("/thegame/Picture/ball.png")).getImage();
        startImg = new ImageIcon(getClass().getResource("/thegame/Picture/start.png")).getImage();
        overImg = new ImageIcon(getClass().getResource("/thegame/Picture/gameOver.png")).getImage();
        victoryImg = new ImageIcon(getClass().getResource("/thegame/Picture/victory.png")).getImage();

//        for (int i = 0; i < 6; i++) {
//            brickImgs[i] = new ImageIcon(getClass().getResource("/thegame/Picture/b" + (i + 1) + ".png")).getImage();
//        }
        brickImgs[0] = new ImageIcon(getClass().getResource("/thegame/Picture/brickdo.png")).getImage();
        brickImgs[1] = new ImageIcon(getClass().getResource("/thegame/Picture/brickxam.png")).getImage();
        brickImgs[2] = new ImageIcon(getClass().getResource("/thegame/Picture/brickcam.png")).getImage();
        brickImgs[3] = new ImageIcon(getClass().getResource("/thegame/Picture/brickxanhlacay.png")).getImage();
        brickImgs[4] = new ImageIcon(getClass().getResource("/thegame/Picture/bricknau.png")).getImage();
        brickImgs[5] = new ImageIcon(getClass().getResource("/thegame/Picture/bricktim.png")).getImage();
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

        for (Brick brick : bricks) {
            if (brick.isDestroyed()) continue;

            if (brick instanceof StrongBrick) {
                StrongBrick sbrick = (StrongBrick) brick;
                Image img = sbrick.getCurrentImage();
                if (img != null) {
                    g2.drawImage(img, brick.x, brick.y, brick.width, brick.height, this);
                } else {
                    g2.setColor(brick.getColor());
                    g2.fillRect(brick.x, brick.y, brick.width, brick.height);
                }
            }
            else if (brick instanceof UnbreakableBrick) {
                g2.drawImage(brickImgs[1], brick.x, brick.y, brick.width, brick.height, this);
            }
            else {
                int rowIndex = Math.max(0, Math.min(5, (brick.y - 120) / 33));
                g2.drawImage(brickImgs[rowIndex], brick.x, brick.y, brick.width, brick.height, this);
            }
        }

        if (baseLevel.getPowerUps() != null) {
            for (PowerUp p : baseLevel.getPowerUps()) {
                p.draw(g2);
            }
        }
        g2.drawImage(paddleImgs[currentFrame], paddle.getX(), paddle.getY(),
                paddle.getWidth(), paddle.getHeight(), this);
        for (NormalBall b : balls) {
            g2.drawImage(ballImg, b.getX(), b.getY(), b.getSize(), b.getSize(), this);
        }

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
        g2.drawString("NOTICE: you can press ESC to PAUSE (51)" , 210, 30);

        g2.setColor(Color.RED);
        g2.drawString("Score: " + baseLevel.getScore(), 25, 60);

        g2.setColor(Color.ORANGE);
        g2.drawString("Level: " + baseLevel.getLevelName(), getWidth() - 120, 30);

        g2.setColor(Color.PINK);
        g2.drawString("Lives: 3", getWidth() - 120, 60);
    }
}
