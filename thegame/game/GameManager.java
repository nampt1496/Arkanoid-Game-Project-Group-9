package thegame.game;

import java.awt.Image;
import javax.swing.*;
import thegame.gameplay.GameState;
import thegame.level.*;
import thegame.menu.OverMenu;
import thegame.menu.StartMenu;
import thegame.setting.SettingManager;
import thegame.setting.SettingMenu;
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

    public void startGame(String playerName) {
        bgSound.stop();
        gameState.setState(GameState.State.RUNNING);

        currentLevel = new Level2(playerName); // sau này có các level mới có thể điều chỉnh.
        bgSound.play(SettingManager.getMusicPath());
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
        bgSound.play("/thegame/sound/source/victory.wav");
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

    public void showSetting(boolean fromPause) {
        thegame.sound.bgSound.pause();

        Image bgImage = null;
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/thegame/Picture/map.png"));
            Image fullImage = icon.getImage();

            int x = 0, y = 0, w = 223, h = 240;
            java.awt.image.BufferedImage buffered = new java.awt.image.BufferedImage(
                    fullImage.getWidth(null),
                    fullImage.getHeight(null),
                    java.awt.image.BufferedImage.TYPE_INT_ARGB
            );
            java.awt.Graphics2D g2 = buffered.createGraphics();
            g2.drawImage(fullImage, 0, 0, null);
            g2.dispose();

            java.awt.image.BufferedImage cropped = buffered.getSubimage(x, y, w, h);
            bgImage = cropped.getScaledInstance(700, 750, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentPane(new SettingMenu(this, fromPause, bgImage));
        revalidate();
        repaint();
    }

    public BaseLevel getCurrentLevel() {
        return currentLevel;
    }

    public void backToMainMenu() {
        bgSound.stop();
        setContentPane(new StartMenu(this));
        revalidate();
        repaint();
    }
}
