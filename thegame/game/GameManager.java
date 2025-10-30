package thegame.game;

import javax.swing.*;
import thegame.gameplay.GameState;
import thegame.level.BaseLevel;
import thegame.menu.OverMenu;
import thegame.menu.StartMenu;
import thegame.sound.bgSound;

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

        setContentPane(new StartMenu(this));
        setVisible(true);
        
    }

    public void startGame() {
        bgSound.stop();
        gameState.setState(GameState.State.RUNNING);
        currentLevel = new BaseLevel(); // sau này có các level mới có thể điều chỉnh.
        bgSound.play("/thegame/sound/source/bg.wav");
        currentLevel.setOnGameOver(() -> {
            bgSound.stop();
            bgSound.playSequential("/thegame/sound/source/gover.wav", "/thegame/sound/source/female.wav");
            String name = currentLevel.getPlayer().getName();
            int point = currentLevel.getScore();
            gameOver(name, point);
        });

        currentLevel.setOnVictory(() -> {
            String name = currentLevel.getPlayer().getName();
            int point = currentLevel.getScore();
            showVictoryMenu(name, point);
        });

        currentLevel.setOnPause(() -> showPauseMenu());

        setContentPane(currentLevel.getView());
        revalidate();
        repaint();
        currentLevel.getView().requestFocusInWindow();
        currentLevel.start();
    }

    private void gameOver(String playerName, int point) {
        gameState.setState(GameState.State.GAME_OVER);
        setContentPane(new OverMenu(this, playerName, point));
        revalidate();
        repaint();
    }

    public void showVictoryMenu(String playerName, int point) {
        bgSound.stop();
        setContentPane(new thegame.menu.VictoryMenu(this, playerName, point));
        revalidate();
        repaint();
    }

    public void showPauseMenu() {
        gameState.setState(GameState.State.PAUSED);
        setContentPane(new thegame.menu.PauseMenu(this));
        revalidate();
        repaint();
    }

    public void resumeGame() {
        if (currentLevel != null) {
            setContentPane(currentLevel.getView());
            revalidate();
            repaint();
            currentLevel.resumeGame();
        }
    }

    public void backToMainMenu() {
        bgSound.stop();
        setContentPane(new StartMenu(this));
        revalidate();
        repaint();
    }
}