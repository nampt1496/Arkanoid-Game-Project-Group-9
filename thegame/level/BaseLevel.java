package thegame.level;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import thegame.animation.ClickAnimation;
import thegame.animation.CollideAnimation;
import thegame.gameplay.Point;
import thegame.menu.PlayerName;
import thegame.object.ball.NormalBall;
import thegame.object.brick.Brick;
import thegame.object.paddle.Paddle;
import thegame.renderer.GameView;
import thegame.sound.bgSound;
import thegame.power.PowerUp;
import thegame.power.ExpandPaddlePowerUp;
import thegame.power.FastBallPowerUp;

public class BaseLevel {
    private GameView gameView;
    private Paddle paddle;
    private NormalBall ball;
    private ArrayList<Brick> bricks;
    private Timer gameTimer;
    private Runnable onGameOver; // callback để báo cho GameManager
    private Runnable onVictory;
    private Runnable onPause;
    private PlayerName player;
    private Point point = new Point();
    private String levelName;
    private boolean ballLaunched = false;
    private ArrayList<PowerUp> powerUps = new ArrayList<>();

    public BaseLevel(String playerName) {
        this.player = new PlayerName(playerName);
        this.levelName = "Base";
        paddle = new Paddle(310, 700, 120, 15);
        ball = new NormalBall(paddle.getX() + paddle.getWidth() / 2 -7, paddle.getY() - 20, 15, 0, 0);
        initBricks();
        gameView = new GameView(paddle, ball, bricks, this);

        setupKeyControls();
        gameView.setFocusable(true);
        gameView.requestFocusInWindow();

        gameTimer = new Timer(5, e -> update());

    }

    public void setOnGameOver(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }

    public void setOnVictory(Runnable onVictory) {
        this.onVictory = onVictory;
    }
    public void setOnPause(Runnable onPause) {
        this.onPause = onPause;
    }
    public PlayerName getPlayer() { return player; }
    public String getLevelName() { return levelName; }
    public void addScore(int value) { point.addScore(value); }
    public int getScore() { return point.getScore(); }

    private void initBricks() {
        bricks = new ArrayList<>();
        int rows = 6, cols = 8;
        int brickWidth = 65, brickHeight = 25, spacing = 8;

        int totalWidth = cols * brickWidth + (cols - 1) * spacing;
        int startX = (700 - totalWidth) / 2;
        int startY = 120;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (brickWidth + spacing);
                int y = startY + row * (brickHeight + spacing);
                bricks.add(new Brick(x, y, brickWidth, brickHeight, Color.WHITE, 1));
            }
        }
    }

    private void setupKeyControls() {
        InputMap im = gameView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = gameView.getActionMap();

        im.put(KeyStroke.getKeyStroke("pressed LEFT"), "leftPressed");
        am.put("leftPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paddle.setLeftPressed(true);
                CollideAnimation.playPaddleMove();
            }
        });

        im.put(KeyStroke.getKeyStroke("released LEFT"), "leftReleased");
        am.put("leftReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paddle.setLeftPressed(false);
            }
        });

        im.put(KeyStroke.getKeyStroke("pressed RIGHT"), "rightPressed");
        am.put("rightPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paddle.setRightPressed(true);
                CollideAnimation.playPaddleMove();
            }
        });

        im.put(KeyStroke.getKeyStroke("released RIGHT"), "rightReleased");
        am.put("rightReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paddle.setRightPressed(false);
            }
        });

        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "launchBall");
        am.put("launchBall", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ballLaunched) {
                    ball.setVelocity(4, 4);
                    ballLaunched = true;
                }
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pauseGame");
        am.put("pauseGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClickAnimation.playClickSound();
                pauseGame();
            }
        });
    }

    private void pauseGame() {
        if (gameTimer.isRunning()) {
            bgSound.pause();
            gameTimer.stop();
            if (onPause != null) onPause.run();
        }
    }

    public void resumeGame() {
        if (!gameTimer.isRunning()) {
            gameView.requestFocusInWindow();
            gameTimer.start();
            bgSound.resume();
        }
    }

    private void update() {
        paddle.update();
        if (!ballLaunched) {
            ball.setPosition(
                    paddle.getX() + paddle.getWidth() / 2 - ball.getSize() / 2,
                    paddle.getY() - ball.getSize() - 5
            );
        } else {
            ball.move(gameView.getWidth(), gameView.getHeight());
        }

        Rectangle ballRect = ball.getBounds();
        Rectangle paddleRect = paddle.getBounds();

        if (ballRect.intersects(paddleRect)) {
            ball.bounceOnPaddle(paddle);
        }
        // 🔹 Cập nhật vị trí và kiểm tra va chạm cho các PowerUp
        ArrayList<PowerUp> toRemove = new ArrayList<>();

        for (PowerUp powerUp : powerUps) {
            // Cập nhật vị trí (rơi xuống)
            powerUp.update();

            // Nếu PowerUp chạm paddle → kích hoạt hiệu ứng
            if (powerUp.getBounds().intersects(paddleRect)) {
                powerUp.activate(paddle, ball);
                toRemove.add(powerUp);
            }

            // Nếu PowerUp rơi quá màn hình → loại bỏ
            if (powerUp.getY() > gameView.getHeight()) {
                toRemove.add(powerUp);
            }
        }

// Xóa các power-up đã dùng hoặc rơi khỏi màn hình
        powerUps.removeAll(toRemove);


        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ballRect.intersects(brick.getBounds())) {
                brick.hit();
                ball.bounceOnBrick();
                if ("Toan".equals(player.getName())) {
                    addScore(100);
                } else {
                    addScore(10);
                }
                if (brick.isDestroyed()) {
                    // Xác suất 30% rơi ra power-up (tổng)
                    if (Math.random() < 1) {
                        // Chọn loại power-up: 50% Expand, 50% FastBall (bạn chỉnh tỉ lệ ở đây)
                        if (Math.random() < 0.5) {
                            powerUps.add(new ExpandPaddlePowerUp(
                                    brick.x + brick.width / 2 - 10,
                                    brick.y + brick.height / 2,
                                    20, 20
                            ));
                        } else {
                            powerUps.add(new FastBallPowerUp(
                                    brick.x + brick.width / 2 - 10,
                                    brick.y + brick.height / 2,
                                    20, 20
                            ));
                        }
                    }
                }

                break;

            }
        }

        if (ball.isOutOfBounds(gameView.getHeight())) {
            gameTimer.stop();
            if (onGameOver != null) onGameOver.run();
        }
        boolean allDestroyed = true;
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                allDestroyed = false;
                break;
            }
        }
        if (allDestroyed) {
            gameTimer.stop();
            if (onVictory != null) onVictory.run();
        }

        gameView.repaint();
    }

    public GameView getView() {
        return gameView;
    }

    public void start() {
        paddle.setLeftPressed(false);
        paddle.setRightPressed(false);
        gameTimer.start();
    }
    //thanhtung
    public ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }

}
