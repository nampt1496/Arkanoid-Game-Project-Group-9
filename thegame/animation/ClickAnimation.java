package thegame.animation;

import java.net.URL;
import javax.sound.sampled.*;

public class ClickAnimation {
    private static Clip clickClip;

    static {
        try {
            URL soundURL = ClickAnimation.class.getResource("/thegame/sound/source/click.wav");
            if (soundURL != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
                clickClip = AudioSystem.getClip();
                clickClip.open(audioIn);
            }
        } catch (Exception e) {
            System.err.println("Không thể tải sẵn âm thanh click: " + e.getMessage());
        }
    }

    public static void playClickSound() {
        if (clickClip == null) return;
        if (clickClip.isRunning()) clickClip.stop();
        clickClip.setFramePosition(0);
        clickClip.start();
    }

    public static void attachClickSound(javax.swing.JButton button) {
        button.addActionListener(e -> playClickSound());
    }
}