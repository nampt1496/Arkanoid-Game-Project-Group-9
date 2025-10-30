package thegame.menu;

import java.awt.*;
import javax.swing.*;
import thegame.game.GameManager;
import thegame.sound.bgSound;

public class StartMenu extends JPanel {
    private final GameManager manager;
    private Image startImg;

    public StartMenu(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        bgSound.play("/thegame/sound/source/intro2.wav");


        JButton startButton = createInvisibleButton(190, 465, 310, 60);
        startButton.addActionListener(e -> manager.startGame());
        add(startButton);

        JButton settingButton = createInvisibleButton(190, 560, 310, 60);
        //settingButton.addActionListener(e -> manager.showSetting());
        settingButton.addActionListener(e -> manager.showSetting(false));
        add(settingButton);

        JButton exitButton = createInvisibleButton(270, 655, 150, 60);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

        startImg = new ImageIcon(getClass().getResource("/thegame/Picture/start.png")).getImage();
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
        g.drawImage(startImg, 0, 0, getWidth(), getHeight(), this);
    }    
}