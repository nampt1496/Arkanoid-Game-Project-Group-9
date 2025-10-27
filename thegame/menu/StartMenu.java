package thegame.menu;

import java.awt.*;
import javax.swing.*;
import thegame.game.GameManager;

public class StartMenu extends JPanel {
    private final GameManager manager;
    private Image startImg;

    public StartMenu(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        startImg = new ImageIcon(getClass().getResource("/thegame/Picture/start.png")).getImage();

        JButton startButton = new JButton("CHƠI");
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