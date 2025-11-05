package thegame.level;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import thegame.animation.CollideAnimation;
import thegame.object.ball.NormalBall;
import thegame.object.brick.*;
import thegame.power.*;

public class Level1 extends BaseLevel {

    protected void initBricks() {
        bricks = new ArrayList<>();
        int brickWidth = 65, brickHeight = 25, spacing = 8;
        int startX = 50;
        int startY = 100;
        java.util.List<String> lines = readLevelLayout("thegame/level/level1.txt");
        for(int row = 0; row < lines.size(); row++){
            String[] tokens = lines.get(row).split("\\s+");
            for(int col = 0; col < tokens.length; col++){
                String symbol = tokens[col];
                int x = startX + col * (brickWidth + spacing);
                int y = startY + row * (brickHeight + spacing);
                switch(symbol){
                    case "u":
                        bricks.add(new UnbreakableBrick(x, y, brickWidth, brickHeight, Color.BLACK));
                        break;
                    case "b":
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, Color.YELLOW, 1));
                        break;
                    case "s":
                        bricks.add(new StrongBrick(x, y, brickWidth, brickHeight, Color.RED));
                    default:
                        break;
                }
            }
        }
    }
    public Level1(String playerName) {
        super(playerName);
        this.levelName = "Level1";
        initBricks();
        gameView.setBricks(bricks);
    }
    private java.util.List<String> readLevelLayout(String path) {
        java.util.List<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            String line;
            while((line = br.readLine())!=null){
                if(!line.trim().isEmpty()){
                    lines.add(line.trim());
                }
            }
        }catch(IOException e){
            System.out.println("file ko ton tai");
        }
        return lines;
    }
    @Override
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
            }

            Rectangle ballRect = ball.getBounds();
            Rectangle paddleRect = paddle.getBounds();

            if (ballRect.intersects(paddleRect)) {
                ball.bounceOnPaddle(paddle);
            }

            for (Brick brick : bricks) {
                if (!brick.isDestroyed() && ballRect.intersects(brick.getBounds())) {
                    ball.bounceOnBrick(brick);

                    if (brick instanceof UnbreakableBrick) {
                    } else {
                        brick.hit();

                        if (brick instanceof StrongBrick) {
                            if (brick.isDestroyed()) { 
                                addScore(15);
                                CollideAnimation.playAddScore();
                            }
                        } else {
                            CollideAnimation.playAddScore();
                            addScore(10);
                        }

                        if (brick.isDestroyed() && Math.random() < 0.3) {
                            double r = Math.random();
                            if (r < 0.25) {
                                powerUps.add(new ExpandPaddlePowerUp(brick.x, brick.y, 20, 20));
                            } else if (r < 0.45) {
                                powerUps.add(new FastBallPowerUp(brick.x, brick.y, 20, 20));
                            } else if (r < 0.60) {
                                powerUps.add(new SlowBall(brick.x, brick.y, 20, 20));
                            } else if (r < 0.70) {
                                powerUps.add(new TripleBallPowerUp(brick.x, brick.y));
                            } else {
                                double s = Math.random();
                                int bonus;
                                if (s < 0.4) bonus = -1000;          
                                else if (s < 0.65) bonus = -500;    
                                else if (s < 0.8) bonus = 100;     
                                else if (s < 0.92) bonus = 250;   
                                else bonus = 500;               
                                powerUps.add(new AddScorePowerUp(brick.x, brick.y, 20, 20, bonus));
                            }
                        }
                    }
                    break;
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

        // cập nhật powerup
        ArrayList<PowerUp> toRemove = new ArrayList<>();
        Rectangle paddleRect = paddle.getBounds();

        for (PowerUp powerUp : powerUps) {
            powerUp.update();
            if (powerUp.getBounds().intersects(paddleRect)) {
                if (activePowerUp != null && activePowerUp.isActive()) {
                    activePowerUp.removeEffect(paddle, balls);
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
            if (powerUp.getY() > gameView.getHeight()) toRemove.add(powerUp);
        }

        powerUps.removeAll(toRemove);

        // kiểm tra thắng
        boolean allDestroyed = true;
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && !(brick instanceof UnbreakableBrick)) {
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
}
