package thegame.game;

import javax.swing.*;
import thegame.animation.ClickAnimation;
import thegame.level.BaseLevel;
import thegame.menu.StartMenu;
import thegame.menu.OverMenu;
import thegame.sound.bgSound;
import thegame.gameplay.GameState;

public class GameManager extends JFrame {
    private BaseLevel currentLevel;
    private final GameState gameState = new GameState();
    public GameManager() {

        UIManager.put("Button.focusInputMap", new javax.swing.InputMap());
        setTitle("Brick Breaker Game");
        setSize(700, 790);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // mở menu đầu tiên
        setContentPane(new StartMenu(this));
        setVisible(true);
        
    }

    public void startGame() {
        bgSound.stop();
        gameState.setState(GameState.State.RUNNING);
        currentLevel = new BaseLevel(); // sau này có các level mới có thể điều chỉnh.
        bgSound.play("/thegame/sound/source/bg.wav");
        // gán callback khi game over
        currentLevel.setOnGameOver(() -> {
            bgSound.stop();
            bgSound.playSequential("/thegame/sound/source/gover.wav", "/thegame/sound/source/female.wav");
            gameOver();
        });

        setContentPane(currentLevel.getView());
        revalidate();
        repaint();
        currentLevel.getView().requestFocusInWindow();
        currentLevel.start();
    }

    private void gameOver() {
        gameState.setState(GameState.State.GAME_OVER);
        ClickAnimation.playClickSound();

        // Hiển thị giao diện Game Over Menu
        setContentPane(new OverMenu(this));
        revalidate();
        repaint();
    }

    public void retryGame() {
        bgSound.stop();
        bgSound.play("/thegame/sound/source/bg.wav");
        startGame();
    }

    public void backToMainMenu() {
        bgSound.stop();
        setContentPane(new StartMenu(this));
        revalidate();
        repaint();
    }
}