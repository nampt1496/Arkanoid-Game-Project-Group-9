package thegame.setting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import thegame.level.BaseLevel;
import thegame.sound.bgSound;

public class SettingManager {

    private static int backgroundIndex = 0;
    private static String musicPath = "/thegame/sound/source/bg.wav";

    private static final String SPRITE_PATH = "/thegame/Picture/map.png";
    private static BufferedImage fullSheet;

    private static final Rectangle[] BG_REGIONS = {
        new Rectangle(0, 0, 224, 240),
        new Rectangle(233, 0, 223, 240),
        new Rectangle(464, 0, 224, 240),
        new Rectangle(697, 0, 223, 240),
        new Rectangle(929, 0, 222, 240)
    };

    static {
        try {
            URL url = SettingManager.class.getResource(SPRITE_PATH);
            if (url != null) {
                fullSheet = ImageIO.read(url);
            } else {
                System.err.println("Không tìm thấy file nền: " + SPRITE_PATH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBackgroundIndex(int index) {
        if (index >= 0 && index < BG_REGIONS.length) {
            backgroundIndex = index;
        }
    }

    public static int getBackgroundIndex() {
        return backgroundIndex;
    }

    public static Image getBackgroundImage(int width, int height) {
        if (fullSheet == null) return null;
        Rectangle r = BG_REGIONS[backgroundIndex];
        BufferedImage cropped = fullSheet.getSubimage(r.x, r.y, r.width, r.height);
        return cropped.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public static void setMusicPath(String path) {
        musicPath = path;
    }

    public static String getMusicPath() {
        return musicPath;
    }

    public static void pauseMusic() {
        bgSound.pause();
    }

    public static void applySettings() {
        bgSound.stop();
        bgSound.play(musicPath);
    }

    public static void applyBackgroundTo(BaseLevel level) {
        if (level != null && level.getView() != null) {
            level.getView().refreshBackground();
        }
    }
}