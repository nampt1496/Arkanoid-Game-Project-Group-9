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
    private String playerName;
    private int point;

    public OverMenu(GameManager manager, String playerName, int point) {
        this.manager = manager;
        this.playerName = playerName;
        this.point = point;
        setLayout(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                bgSound.stop();
                manager.backToMainMenu();
            }
        });

        overImg = new ImageIcon(getClass().getResource("/thegame/Picture/gameOver.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(overImg, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2 = (Graphics2D) g;
        try {
            Font pixelFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/thegame/font/pixel2.otf")
            );
            pixelFont = pixelFont.deriveFont(35f);
            g2.setFont(pixelFont);
        } catch (Exception e) {
            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
        }
        g2.setColor(Color.GREEN);

        String playerText = "Player: " + playerName;
        String pointText = "Point: " + point;

        FontMetrics fm = g2.getFontMetrics();
        int xPlayer = (getWidth() - fm.stringWidth(playerText)) / 2 ;
        int xPoint = (getWidth() - fm.stringWidth(pointText)) / 2 ;
        int yBase = getHeight() - 310;

        g2.drawString(playerText, xPlayer, yBase);
        g2.drawString(pointText, xPoint, yBase + 35);
    }
}
