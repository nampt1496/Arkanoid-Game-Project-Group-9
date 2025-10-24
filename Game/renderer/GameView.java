    package Game.renderer;

    import Game.level.BaseLevel;
    import Game.menu.PlayerName;
    import Game.object.ball.NormalBall;
    import Game.object.brick.Brick;
    import Game.object.paddle.Paddle;
    import java.awt.*;
    import java.util.ArrayList;
    import javax.swing.*;

    public class GameView extends JPanel {
        private Paddle paddle;
        private NormalBall ball;
        private ArrayList<Brick> bricks;
        private Image bgImg, paddleImg, ballImg, startImg;
        private PlayerName player;
        private int score;
        private String levelName;
        private BaseLevel baseLevel;

        
        public GameView(Paddle paddle, NormalBall ball, ArrayList<Brick> bricks, BaseLevel baseLevel) {
            this.paddle = paddle;
            this.ball = ball;
            this.bricks = bricks;
            this.baseLevel = baseLevel;
            setDoubleBuffered(true);

            // Load ảnh
            bgImg = new ImageIcon(getClass().getResource("/Game/Picture/galaxybg.jpg")).getImage();
            paddleImg = new ImageIcon(getClass().getResource("/Game/Picture/paddle.png")).getImage();
            ballImg = new ImageIcon(getClass().getResource("/Game/Picture/ball.png")).getImage();
            startImg = new ImageIcon(getClass().getResource("/Game/Picture/start.png")).getImage();

        }

        
        public void setBricks(ArrayList<Brick> bricks) {
            this.bricks = bricks;
        }

        private Image cachedBG; // thêm biến cache

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // 🔹 Cache nền
            if (cachedBG == null) {
                cachedBG = createImage(getWidth(), getHeight());
                Graphics2D gbg = (Graphics2D) cachedBG.getGraphics();
                gbg.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
                gbg.dispose();
            }
            g2.drawImage(cachedBG, 0, 0, this);

            // 🔹 Vẽ vật thể game
            g2.drawImage(paddleImg, paddle.getX(), paddle.getY(),
                        paddle.getWidth(), paddle.getHeight(), this);
            g2.drawImage(ballImg, ball.getX(), ball.getY(),
                        ball.getSize(), ball.getSize(), this);

            for (Brick brick : bricks) {
                if (!brick.isDestroyed()) {
                    Color base = brick.getColor();
                    GradientPaint gp = new GradientPaint(
                        brick.x, brick.y, base.brighter(),
                        brick.x, brick.y + brick.height, base.darker()
                    );
                    g2.setPaint(gp);
                    g2.fillRoundRect(brick.x, brick.y, brick.width, brick.height, 15, 15);
                }
            }

            drawHUD(g2);
        }

        private void drawHUD(Graphics2D g2) {
            g2.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

            g2.setColor(Color.CYAN);
            g2.drawString("Player: " + baseLevel.getPlayer().getName(), 25, 35);

            g2.setColor(Color.CYAN);
            g2.drawString("Score: " + "9999999", 25, 60);

            g2.setColor(Color.BLACK);
            g2.drawString("Level: " + baseLevel.getLevelName(), getWidth() - 180, 35);

            g2.setColor(Color.BLACK);
            g2.drawString("Lives: " + "9999", getWidth() - 180,60);
        }
    }