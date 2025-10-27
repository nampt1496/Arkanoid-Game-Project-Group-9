package thegame.renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import thegame.level.BaseLevel;
import thegame.object.ball.NormalBall;
import thegame.object.brick.Brick;
import thegame.object.paddle.Paddle;

public class GameView extends JPanel {
    private Paddle paddle;
    private NormalBall ball;
    private ArrayList<Brick> bricks;
    private Image bgImg, paddleImg, ballImg, startImg;
    private BaseLevel baseLevel;
    private Image cachedBG;
    private Image[] brickImgs = new Image[6];

    public GameView(Paddle paddle, NormalBall ball, ArrayList<Brick> bricks, BaseLevel baseLevel) {
        this.paddle = paddle;
        this.ball = ball;
        this.bricks = bricks;
        this.baseLevel = baseLevel;
        setDoubleBuffered(true);

        // 🔹 Load full sprite background (map.png)
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/thegame/Picture/map.png"));
            Image fullImage = icon.getImage();

            // Tạo BufferedImage từ Image (tránh lỗi ép kiểu)
            int w = fullImage.getWidth(null);
            int h = fullImage.getHeight(null);
            BufferedImage buffered = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buffered.createGraphics();
            g2.drawImage(fullImage, 0, 0, null);
            g2.dispose();

            
            //BufferedImage cropped = buffered.getSubimage(232, 0, 224, 240);
            BufferedImage cropped = buffered.getSubimage(233, 0, 223, 240);

            //Phóng to để vừa khung game 
            bgImg = cropped.getScaledInstance(700, 750, Image.SCALE_SMOOTH);

        } catch (Exception e) {
            e.printStackTrace();
            // Nếu lỗi, dùng nền đen thay thế
            bgImg = new BufferedImage(1280, 800, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = ((BufferedImage) bgImg).createGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 1280, 800);
            g.dispose();
        }

        // 🔹 Load các ảnh còn lại
        paddleImg = new ImageIcon(getClass().getResource("/thegame/Picture/paddle.png")).getImage();
        ballImg = new ImageIcon(getClass().getResource("/thegame/Picture/ball.png")).getImage();
        startImg = new ImageIcon(getClass().getResource("/thegame/Picture/start.png")).getImage();
        for (int i = 0; i < 6; i++) {
        brickImgs[i] = new ImageIcon(
            getClass().getResource("/thegame/Picture/b" + (i + 1) + ".png")
            ).getImage();
        }
    }

    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- Vẽ nền game ---
        if (cachedBG == null) {
            cachedBG = createImage(getWidth(), getHeight());
            Graphics2D gbg = (Graphics2D) cachedBG.getGraphics();
            gbg.drawImage(bgImg, 0, 70, getWidth(), getHeight(), this);
            gbg.dispose();
        }
        g2.drawImage(cachedBG, 0, 0, this);

        // --- Vẽ bricks ---
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            if (!brick.isDestroyed()) {
                // Tính hàng (0–5)
                int rowIndex = (brick.y - 80) / 33;
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

        // --- Paddle & Ball ---
        g2.drawImage(paddleImg, paddle.getX(), paddle.getY(),
                paddle.getWidth(), paddle.getHeight(), this);
        g2.drawImage(ballImg, ball.getX(), ball.getY(),
                ball.getSize(), ball.getSize(), this);

        // --- Vẽ HUD (đè lên, không chiếm layout) ---
        drawHUD(g2);
    }

    private void drawHUD(Graphics2D g2) {
        //g2.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
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

        // Vẽ 1 thanh mờ đen overlay ở trên cùng (đè lên nền)
        g2.setColor(new Color(0, 0, 0, 255));
        g2.fillRect(0, 0, getWidth(), 70);

        // Vẽ text
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