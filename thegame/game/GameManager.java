package thegame.game;

import javax.swing.*;
import thegame.animation.ClickAnimation;
import thegame.level.BaseLevel;
import thegame.menu.StartMenu;
import thegame.sound.bgSound;

public class GameManager extends JFrame {
    private BaseLevel currentLevel;
    
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
        currentLevel = new BaseLevel(); // sau này có các level mới có thể điều chỉnh.
        bgSound.play("/thegame/sound/source/bg.wav");
        // gán callback khi game over
        currentLevel.setOnGameOver(() -> {
            bgSound.stop();
            bgSound.playSequential("/thegame/sound/source/gover.wav", "/thegame/sound/source/female.wav");
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Mày đã thua, chơi lại ko em?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION
            );
            ClickAnimation.playClickSound();
            if (option == JOptionPane.YES_OPTION) {
                currentLevel.reset();
                currentLevel.start();
                bgSound.play("/thegame/sound/source/bg.wav");
            } else {
                System.exit(0);
            }
        });

        setContentPane(currentLevel.getView());
        revalidate();
        repaint();
        currentLevel.getView().requestFocusInWindow();
        currentLevel.start();
    }
}