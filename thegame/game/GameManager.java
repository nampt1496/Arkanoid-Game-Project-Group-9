package thegame.game;

import javax.swing.*;
import thegame.level.BaseLevel;
import thegame.menu.StartMenu;

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

    /** Hàm được gọi khi bấm Start trong menu */
    public void startGame() {
        currentLevel = new BaseLevel(); // sau này có các level mới có thể điều chỉnh.

        // gán callback khi game over
        currentLevel.setOnGameOver(() -> {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Mày đã thua, chơi lại ko em?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION
            );
            if (option == JOptionPane.YES_OPTION) {
                currentLevel.reset();
                currentLevel.start();
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