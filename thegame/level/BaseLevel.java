package thegame.level;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import thegame.animation.ClickAnimation;
import thegame.animation.CollideAnimation;
import thegame.gameplay.Lives;
import thegame.gameplay.Point;
import thegame.menu.PlayerName;
import thegame.object.ball.NormalBall;
import thegame.object.brick.Brick;
import thegame.object.paddle.Paddle;
import thegame.power.*;
import thegame.renderer.GameView;
import thegame.sound.bgSound;

public class BaseLevel {
    protected GameView gameView;
    protected Paddle paddle;
    protected ArrayList<NormalBall> balls;
    public ArrayList<Brick> bricks;
    protected Timer gameTimer;
    protected Runnable onGameOver;
    protected Runnable onVictory;
    protected Runnable onPause;
    protected PlayerName player;
    protected Point point = new Point();
    protected String levelName;
    protected boolean ballLaunched = false;
    protected ArrayList<PowerUp> powerUps = new ArrayList<>();
    protected PowerUp activePowerUp = null;
    public Lives lives ;

    public BaseLevel(String playerName) {
        this.player = new PlayerName(playerName);
        this.levelName = "Level3";
        paddle = new Paddle(310, 700, 120, 15);
        lives = new Lives(3);
        balls = new ArrayList<>();
        balls.add(new NormalBall(
                paddle.getX() + paddle.getWidth() / 2 - 7,
                paddle.getY() - 20,
                15, 0, 0
        ));

        initBricks();
        gameView = new GameView(paddle, balls, bricks, this, lives);

        setupKeyControls();
        gameView.setFocusable(true);
        gameView.requestFocusInWindow();

        gameTimer = new Timer(5, e -> update());
    }

    public void setOnGameOver(Runnable onGameOver) { this.onGameOver = onGameOver; }
    public void setOnVictory(Runnable onVictory) { this.onVictory = onVictory; }
    public void setOnPause(Runnable onPause) { this.onPause = onPause; }
    public PlayerName getPlayer() { return player; }
    public String getLevelName() { return levelName; }
    public void addScore(int value) { point.addScore(value); }
    public int getScore() { return point.getScore(); }
    public ArrayList<PowerUp> getPowerUps() { return powerUps; }

    protected void initBricks() {
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

    protected void setupKeyControls() {
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
                    for (NormalBall b : balls) {
                        b.setVelocity(2, 2);
                    }
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

    protected void pauseGame() {
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

    protected void update() {
        paddle.update();

        ArrayList<NormalBall> toRemoveBalls = new ArrayList<>();

        for (NormalBall ball : balls) {
            if (!ballLaunched) {
                ball.setPosition(
                        paddle.getX() + paddle.getWidth() / 2 - ball.getSize() / 2,
                        paddle.getY() - ball.getSize() - 5
                );
            } else {
                ball.move(gameView.getWidth(), gameView.getHeight());
                if (!FastBallPowerUp.active && !SlowBall.active) {
                    double speed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());
                    if (Math.abs(speed - 2.0) > 0.05) {
                        double ratio = 2.0 / speed;
                        ball.setVelocity(ball.getDx() * ratio, ball.getDy() * ratio);
                    }
                }
            }

            Rectangle ballRect = ball.getBounds();
            Rectangle paddleRect = paddle.getBounds();

            if (ballRect.intersects(paddleRect)) {
                ball.bounceOnPaddle(paddle);
            }

            Brick collided = null;
            double minDist = Double.MAX_VALUE;

            for (Brick brick : bricks) {
                if (!brick.isDestroyed() && ballRect.intersects(brick.getBounds())) {
                    double dx = (ballRect.getCenterX() - brick.getBounds().getCenterX());
                    double dy = (ballRect.getCenterY() - brick.getBounds().getCenterY());
                    double dist = Math.sqrt(dx * dx + dy * dy);

                    if (dist < minDist) {
                        minDist = dist;
                        collided = brick;
                    }
                }
            }

            if (collided != null) {
                collided.hit();
                if (collided.isDestroyed()) {
                    CollideAnimation.playAddScore();
                }
                ball.bounceOnBrick(collided);               
                addScore("Toan".equals(player.getName()) ? 1000 : 10);

                if (collided.isDestroyed() && Math.random() < 0.8) {
                    double r = Math.random();
                    int powerX = collided.x + collided.width / 2 - 10;
                    int powerY = collided.y;

                    if (r < 0.8) {
                        double s = Math.random();
                        int bonus;
                        if (s < 0.4) bonus = -1000;          
                        else if (s < 0.65) bonus = -500;    
                        else if (s < 0.8) bonus = 100;     
                        else if (s < 0.92) bonus = 250;      
                        else bonus = 500;                    
                        powerUps.add(new AddScorePowerUp(powerX, powerY, 20, 20, bonus));

                    } else {
                        double t = Math.random();
                        if (t < 0.1) {
                            powerUps.add(new TripleBallPowerUp(powerX, powerY)); 
                        } else if (t < 0.4) {
                            powerUps.add(new ExpandPaddlePowerUp(powerX, powerY, 20, 20)); 
                        } else if (t < 0.7) {
                            powerUps.add(new FastBallPowerUp(powerX, powerY, 20, 20)); 
                        } else {
                            powerUps.add(new SlowBall(powerX, powerY, 20, 20));
                        }
                    }
                }
            }

            if (ball.isOutOfBounds(gameView.getHeight())) {
                toRemoveBalls.add(ball);
            }
        }

        balls.removeAll(toRemoveBalls);

        if (balls.isEmpty()) {
            lives.loseLife();
            if(lives.getLives() <= 0) {
                gameTimer.stop();
                if (onGameOver != null) onGameOver.run();
            }
            else {
                resetBall();
            }
        }

        ArrayList<PowerUp> toRemove = new ArrayList<>();
        Rectangle paddleRect = paddle.getBounds();

        for (PowerUp powerUp : powerUps) {
            powerUp.update();

            if (powerUp.getBounds().intersects(paddleRect)) {
                if (activePowerUp != null && activePowerUp.isActive()) {
                    activePowerUp.removeEffect(paddle, balls);
                    activePowerUp = null;
                }
                CollideAnimation.playAddScore();
                powerUp.activate(paddle, balls);
                activePowerUp = powerUp;
                if (powerUp instanceof AddScorePowerUp) {
                    AddScorePowerUp scorePower = (AddScorePowerUp) powerUp;
                    addScore(scorePower.getScoreBonus());
                }
                toRemove.add(powerUp);
            }

            if (powerUp.getY() > gameView.getHeight()) {
                toRemove.add(powerUp);
            }
        }
        powerUps.removeAll(toRemove);


        boolean allDestroyed = true;
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                allDestroyed = false;
                break;
            }
        }
        if (allDestroyed) {
            gameTimer.stop();
            if (onVictory != null && getScore() > 0) {onVictory.run();}
            else {onGameOver.run();}
        }

        gameView.repaint();
    }

    public GameView getView() { return gameView; }

    public void start() {
        paddle.setLeftPressed(false);
        paddle.setRightPressed(false);
        gameTimer.start();
    }

    protected void resetBall() {
        powerUps.clear();
        balls.add(new NormalBall(
                paddle.getX() + paddle.getWidth() / 2 - 7,
                paddle.getY() - 20,
                15, 0, 0
        ));
        ballLaunched = false;
    }

    public Lives getLives() {
        return lives;
    }

}
