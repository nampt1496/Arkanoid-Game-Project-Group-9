package thegame.menu;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import thegame.game.GameManager;
import thegame.sound.bgSound;

public class OverMenu extends JPanel {
    private final GameManager manager;
    private Image overImg;

    public OverMenu(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        // Bắt sự kiện click chuột để quay lại StartMenu
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                bgSound.stop();
                manager.backToMainMenu();
            }
        });

        // Ảnh nền Game Over
        overImg = new ImageIcon(getClass().getResource("/thegame/Picture/gameOver.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(overImg, 0, 0, getWidth(), getHeight(), this);
    }
}
