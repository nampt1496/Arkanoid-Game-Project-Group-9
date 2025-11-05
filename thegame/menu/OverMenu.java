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
    private int live;

    public OverMenu(GameManager manager, String playerName, int point, int live) {
        this.manager = manager;
        this.playerName = playerName;
        this.point = point;
        this.live = live;
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

        Graphics2D g2 = (Graphics2D) g.create();
        Graphics2D g3 = (Graphics2D) g.create();
        try {
            Font pixelFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/thegame/font/pixel2.otf")
            );
            pixelFont = pixelFont.deriveFont(35f);
            g2.setFont(pixelFont);
            g3.setFont(pixelFont.deriveFont(25f));
        } catch (Exception e) {
            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
        }
        g2.setColor(Color.GREEN);
        g3.setColor(Color.RED);
        boolean checkpoint = false;
        String pointText = "Point: " + point;
        String pointText2 = null;
        if(point <= 0 && live > 0) {
            checkpoint = true; 
            pointText2 = "(You lose due to negative points)";
        }
        String playerText = "Player: " + playerName;
        FontMetrics fm = g2.getFontMetrics();
        FontMetrics fm1 = g3.getFontMetrics();
        if (checkpoint) {
            int xPlayer = (getWidth() - fm.stringWidth(playerText)) / 2;
            int xPoint = (getWidth() - fm.stringWidth(pointText)) / 2;
            int xnotice = (getWidth() - fm1.stringWidth(pointText2)) / 2;
            int yBase = getHeight() - 330;
            g2.drawString(playerText, xPlayer, yBase);
            g2.drawString(pointText, xPoint, yBase + 35);
            g3.drawString(pointText2, xnotice, yBase + 70);
        } else {
            int xPlayer = (getWidth() - fm.stringWidth(playerText)) / 2;
            int xPoint = (getWidth() - fm.stringWidth(pointText)) / 2;
            int yBase = getHeight() - 310;
            g2.drawString(playerText, xPlayer, yBase);
            g2.drawString(pointText, xPoint, yBase + 35);
        }
    }
}
