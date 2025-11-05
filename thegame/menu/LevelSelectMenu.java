package thegame.menu;

import java.awt.*;
import javax.swing.*;
import thegame.game.GameManager;

public class LevelSelectMenu extends JPanel {
    private final GameManager manager;
    private Image levelImage;
    private String playerName;

    public LevelSelectMenu(GameManager manager, String playerName) {
        this.manager = manager;
        this.playerName = playerName;
        setLayout(null);

        levelImage = new ImageIcon(getClass().getResource("/thegame/Picture/chooselevel.png")).getImage();

        JButton level1Button = createInvisibleButton(170, 310, 350, 70);
        level1Button.addActionListener(e -> manager.startGame(playerName, "Level1"));

        JButton level2Button = createInvisibleButton(170, 410, 350, 70);
        level2Button.addActionListener(e -> manager.startGame(playerName, "Level2"));

        JButton level3Button = createInvisibleButton(170, 510, 350, 70);
        level3Button.addActionListener(e -> manager.startGame(playerName, "Level3"));

        JButton backButton = createInvisibleButton(270, 640, 160, 60);
        backButton.addActionListener(e -> manager.backToMainMenu());

        add(level1Button);
        add(level2Button);
        add(level3Button);
        add(backButton);
    }

    private JButton createInvisibleButton(int x, int y, int w, int h) {
        JButton button = new JButton();
        button.setBounds(x, y, w, h);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        thegame.animation.ClickAnimation.attachClickSound(button);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(levelImage, 0, 0, getWidth(), getHeight(), this);
    }
}
