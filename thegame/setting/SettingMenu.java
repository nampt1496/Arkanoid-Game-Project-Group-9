package thegame.setting;

import java.awt.*;
import javax.swing.*;
import thegame.animation.ClickAnimation;
import thegame.game.GameManager;
import thegame.sound.bgSound;

public class SettingMenu extends JPanel {
    private final GameManager manager;
    private final boolean fromPause;
    private final Font pixelFont;
    private final Image backgroundImg;

    private final String[] backgrounds = {
        "Xanh nuoc bien mem mai de nhin"
        , "Xanh la hoa mat chong mat"
        , "Xanh bien nhg ma kho nhin hon mot ti"
        , "Mau do kha de nhin"
        , "Chon mau nay thi thoi thoat game luon di"
    };
    private final String[] musics = {
        "Mac dinh (nhe nhang, chill chill)", "Giai dieu tinh yeu (Remix)", "SummerTime-K391 (remix)"
    };

    public SettingMenu(GameManager manager, boolean fromPause, Image bgImage) {
        this.manager = manager;
        this.fromPause = fromPause;
        this.backgroundImg = bgImage;

        Font tmp;
        try {
            tmp = Font.createFont(Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/thegame/font/pixel2.otf")).deriveFont(22f);
        } catch (Exception e) {
            tmp = new Font("Consolas", Font.BOLD, 20);
        }
        pixelFont = tmp;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(50, 60, 40, 60));

        add(createTitle("GAME SETTINGS"));
        add(Box.createVerticalStrut(25));
        add(createSubtitle("BackGround"));
        add(Box.createVerticalStrut(20));

        for (int i = 0; i < backgrounds.length; i++) {
            add(createOptionRow(backgrounds[i], i, true));
            add(Box.createVerticalStrut(20));
        }

        add(Box.createVerticalStrut(30));
        add(createSubtitle("Sound"));
        add(Box.createVerticalStrut(20));

        for (int i = 0; i < musics.length; i++) {
            add(createOptionRow(musics[i], i, false));
            add(Box.createVerticalStrut(20));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private JLabel createTitle(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(pixelFont.deriveFont(50f));
        label.setForeground(Color.CYAN);
        return label;
    }

    private JLabel createSubtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(pixelFont);
        label.setForeground(Color.YELLOW);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JPanel createOptionRow(String name, int index, boolean isBackground) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        JLabel label = new JLabel(name);
        label.setFont(pixelFont);
        label.setForeground(Color.WHITE);

        JButton selectBtn = new JButton("Chon");
        selectBtn.setFont(pixelFont);
        selectBtn.setForeground(Color.GREEN);
        selectBtn.setBackground(new Color(100, 255, 200));
        selectBtn.setContentAreaFilled(false);
        selectBtn.setBorderPainted(false);
        selectBtn.setFocusPainted(false);
        selectBtn.setOpaque(false);

        selectBtn.addActionListener(e -> {
            ClickAnimation.playClickSound();
            if (isBackground) {
                SettingManager.setBackgroundIndex(index);
                SettingManager.applyBackgroundTo(manager.getCurrentLevel()); 
                System.out.println("Đã chọn nền: " + name);
            }
            else {
                String track = switch (index) {
                    case 1 -> "/thegame/sound/source/gdty.wav";
                    case 2 -> "/thegame/sound/source/summertime.wav";
                    default -> "/thegame/sound/source/bg.wav";
                };
                bgSound.stop();
                bgSound.play(track);
                SettingManager.setMusicPath(track);
                System.out.println("Đã chọn nhạc: " + name);
            }

            if (fromPause) {
                manager.showPauseMenu();
            } else {
                manager.setContentPane(new thegame.menu.StartMenu(manager));
                manager.revalidate();
                manager.repaint();
            }
        });

        row.add(label, BorderLayout.CENTER);
        row.add(selectBtn, BorderLayout.EAST);
        return row;
    }
}