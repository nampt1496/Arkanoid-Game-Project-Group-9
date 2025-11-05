package thegame.level;

import java.awt.*;
import java.io.*;
import thegame.object.ball.NormalBall;
import thegame.power.*;
import java.util.ArrayList;
import thegame.object.brick.*;

public class Level2 extends BaseLevel {

    protected void initBricks() {
        bricks = new ArrayList<>();
        int brickWidth = 65, brickHeight = 25, spacing = 8;
        int startX = 50;
        int startY = 100;
        java.util.List<String> lines = readLevelLayout("thegame/level/level2.txt");
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
    public Level2(String playerName) {
        super(playerName);
        this.levelName = "Level2";
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
                    ball.bounceOnBrick(brick.getBounds());

                    // xử lý theo từng loại
                    if (brick instanceof UnbreakableBrick) {
                        // không vỡ
                    } else {
                        brick.hit();

                        if (brick instanceof StrongBrick) {
                            if (brick.isDestroyed()) addScore(15);
                        } else {
                            addScore(10);
                        }

                        // tạo PowerUp ngẫu nhiên khi gạch vỡ
                        if (brick.isDestroyed() && Math.random() < 1) {
                            double r = Math.random();
                            if (r < 0.33) {
                                powerUps.add(new ExpandPaddlePowerUp(brick.x, brick.y, 20, 20));
                            } else if (r < 0.66) {
                                powerUps.add(new FastBallPowerUp(brick.x, brick.y, 20, 20));
                            } else {
                                powerUps.add(new TripleBallPowerUp(brick.x, brick.y));
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
                powerUp.activate(paddle, balls);
                activePowerUp = powerUp;
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
