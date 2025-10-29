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
        startImg = new ImageIcon(getClass().getResource("/thegame/Picture/start.png")).getImage();

        JButton startButton = new JButton("CHƠI");
        thegame.animation.ClickAnimation.attachClickSound(startButton);
        startButton.setBounds(380, 600, 200, 50);
        startButton.addActionListener(e -> manager.startGame());
        add(startButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(startImg, 0, 0, getWidth(), getHeight(), this);
    }    
}