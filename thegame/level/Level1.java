package thegame.level;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import thegame.object.brick.*;
import thegame.object.ball.NormalBall;
import thegame.object.paddle.Paddle;
import thegame.renderer.GameView;

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
                if (brick instanceof Brick && !(brick instanceof StrongBrick) && !(brick instanceof UnbreakableBrick)) {
                    brick.hit();
                    addScore(10);
                }
                else if(brick instanceof StrongBrick){
                    brick.hit();
                    if(brick.isDestroyed()){
                        addScore(15);
                    }
                }
                else if (brick instanceof UnbreakableBrick) {
                }
                break;
            }
        }

        // Kiểm tra bóng rơi xuống đáy màn hình (thua)
        if (ball.isOutOfBounds(gameView.getHeight())) {
            gameTimer.stop();
            if (onGameOver != null) onGameOver.run();
        }

        // Kiểm tra thắng
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
