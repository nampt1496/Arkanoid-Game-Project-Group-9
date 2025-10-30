package thegame.level;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import thegame.animation.CollideAnimation;
import thegame.menu.PlayerName;
import thegame.object.ball.NormalBall;
import thegame.object.brick.Brick;
import thegame.object.paddle.Paddle;
import thegame.renderer.GameView;

public class BaseLevel {
    private GameView gameView;
    private Paddle paddle;
    private NormalBall ball;
    private ArrayList<Brick> bricks;
    private Timer gameTimer;
    private Runnable onGameOver; // callback để báo cho GameManager

    private Runnable onVictory;
    private PlayerName player;
    private int score;
    private String levelName;

    public BaseLevel() {
        this.player = new PlayerName("Toan");
        this.score = 0;
        this.levelName = "Base";
        paddle = new Paddle(310, 700, 120, 15);
        ball = new NormalBall(390, 400, 15, 5, 5);
        initBricks();
        gameView = new GameView(paddle, ball, bricks, this);

        setupKeyControls();
        gameView.setFocusable(true);
        gameView.requestFocusInWindow();

        gameTimer = new Timer(15, e -> update());
        
    }

    

    /** Cho phép GameManager gán callback */
    public void setOnGameOver(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }

    public void setOnVictory(Runnable onVictory) {
        this.onVictory = onVictory;
    }
    public PlayerName getPlayer() { return player; }
    public int getScore() { return score; }
    public String getLevelName() { return levelName; }

    public void addScore(int points) { this.score += points; }

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
    }

    private void update() {
        paddle.update();
        ball.move(gameView.getWidth(), gameView.getHeight());

        Rectangle ballRect = ball.getBounds();
        Rectangle paddleRect = paddle.getBounds();

        if (ballRect.intersects(paddleRect)) {
            ball.bounceOnPaddle(paddle);
        }

        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ballRect.intersects(brick.getBounds())) {
                brick.hit();
                ball.bounceOnBrick();
                break;
            }
        }

        // Gọi callback khi game over
        if (ball.isOutOfBounds(gameView.getHeight())) {
            gameTimer.stop();
            if (onGameOver != null) onGameOver.run();
        }
        // --- Điều kiện thắng ---
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

    public JPanel getView() {
        return gameView;
    }

    public void start() {
        paddle.setLeftPressed(false);
        paddle.setRightPressed(false);
        gameTimer.start();
    }

    public void reset() {
        paddle.reset();
        ball.reset();
        paddle.setLeftPressed(false);
        paddle.setRightPressed(false);
        initBricks();
        gameView.setBricks(bricks);
        gameView.repaint();
    }
}