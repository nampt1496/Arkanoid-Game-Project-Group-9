package thegame.menu;

import java.awt.*;
import javax.swing.*;
import thegame.game.GameManager;

public class PauseMenu extends JPanel {
    private final GameManager manager;
    private Image pauseImg;
    public PauseMenu(GameManager manager) {
            this.manager = manager;
            setLayout(null);

            JButton resumeButton = createInvisibleButton(190, 425, 310, 65);
            resumeButton.addActionListener(e -> manager.resumeGame());
            add(resumeButton);

            JButton settingButton = createInvisibleButton(190, 540, 310, 60);
            settingButton.addActionListener(e -> manager.showSetting(true));
            add(settingButton);

            JButton exitButton = createInvisibleButton(270, 645, 150, 60);
            exitButton.addActionListener(e -> System.exit(0));
            add(exitButton);

            pauseImg = new ImageIcon(getClass().getResource("/thegame/Picture/pause.png")).getImage();
    }

    private JButton createInvisibleButton(int x, int y, int w, int h) {
            JButton button = new JButton();
            button.setBounds(x, y, w, h);
            thegame.animation.ClickAnimation.attachClickSound(button);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            return button;
    }

    @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(pauseImg, 0, 0, getWidth(), getHeight(), this);
        }
}
