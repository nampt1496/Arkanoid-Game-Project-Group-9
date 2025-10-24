package Game.level;

import Game.menu.PlayerName;
import Game.object.ball.NormalBall;
import Game.object.brick.Brick;
import Game.object.paddle.Paddle;
import Game.renderer.GameView;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class BaseLevel {
    private GameView gameView;
    private Paddle paddle;
    private NormalBall ball;
    private ArrayList<Brick> bricks;
    private Timer gameTimer;
    private Runnable onGameOver; // callback để báo cho GameManager
    private PlayerName player;
    private int score;
    private String levelName;

    public BaseLevel() {
        this.player = new PlayerName("Toan");
        this.score = 0;
        this.levelName = "Base";
        paddle = new Paddle(580, 710, 120, 15);
        ball = new NormalBall(820, 400, 15, 4, 4);
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

    public PlayerName getPlayer() { return player; }
    public int getScore() { return score; }
    public String getLevelName() { return levelName; }

    public void addScore(int points) { this.score += points; }

    private void initBricks() {
        bricks = new ArrayList<>();
        int rows = 6, cols = 14, brickWidth = 82, brickHeight = 25, spacing = 8;
        int totalWidth = cols * (brickWidth + spacing) - spacing;
        int startX = (1280 - totalWidth) / 2;
        int startY = 80;
        // Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.PINK};
        Color[] colors = {
            new Color(0xADD8E6),
            new Color(0xB0CFDE),
            new Color(0xC9DFEC),
            new Color(0xD5D6EA),
            new Color(0xE3E4FA),
            new Color(0xDBE9FA)
        };

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (brickWidth + spacing);
                int y = startY + row * (brickHeight + spacing);
                Color color = colors[row % colors.length];
                bricks.add(new Brick(x, y, brickWidth, brickHeight, color, 1));
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