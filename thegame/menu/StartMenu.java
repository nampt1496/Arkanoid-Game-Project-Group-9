package thegame.menu;

import java.awt.*;
import javax.swing.*;
import thegame.game.GameManager;
import thegame.sound.bgSound;

public class StartMenu extends JPanel {
    private final GameManager manager;
    private Image startImg;
    private JTextField nameField;
    private Font arcadeFont; 

    public StartMenu(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        bgSound.play("/thegame/sound/source/intro2.wav");

        try {
            arcadeFont = Font.createFont(Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/thegame/font/pixel2.otf"));
            arcadeFont = arcadeFont.deriveFont(Font.PLAIN, 26f);
        } catch (Exception e) {
            arcadeFont = new Font("Arial", Font.BOLD, 26);
        }

        JLabel nameLabel = new JLabel("ENTER YOUR NAME:");
        nameLabel.setFont(arcadeFont);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(220, 350, 350, 40);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(arcadeFont.deriveFont(Font.PLAIN, 22f));
        nameField.setForeground(Color.YELLOW);
        nameField.setBackground(Color.BLACK);
        nameField.setCaretColor(Color.WHITE);
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBounds(220, 390, 260, 40);
        add(nameField);

        JButton startButton = createInvisibleButton(190, 465, 310, 60);
        startButton.addActionListener(e -> {
            String playerName = nameField.getText().trim();

            if (playerName.isEmpty()) {
                showArcadeMessage("Please enter your name!");
                return;
            }

            if (playerName.length() > 7) {
                showArcadeMessage("Name must be under 8 characters!");
                return;
            }

            manager.startGame(playerName);
        });
        add(startButton);

        JButton settingButton = createInvisibleButton(190, 560, 310, 60);
        settingButton.addActionListener(e -> manager.showSetting(false));
        add(settingButton);

        JButton exitButton = createInvisibleButton(270, 655, 150, 60);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

        startImg = new ImageIcon(getClass().getResource("/thegame/Picture/start.png")).getImage();
    }

    private void showArcadeMessage(String message) {
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(arcadeFont.deriveFont(Font.PLAIN, 22f));
        label.setForeground(Color.RED);

        JOptionPane.showMessageDialog(
                this,
                label,
                "NOTICE",
                JOptionPane.PLAIN_MESSAGE
        );
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