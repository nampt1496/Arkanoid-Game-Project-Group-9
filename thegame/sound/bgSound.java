package thegame.sound;

import java.net.URL;
import javax.sound.sampled.*;

public class bgSound {
    private static Clip clip; 

    public static void play(String path) {
        stop(); 
        try {
            URL soundURL = bgSound.class.getResource(path);
            if (soundURL == null) {
                System.err.println("Không tìm thấy file nhạc: " + path);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    public static void playSequential(String first, String second) {
        stop();
        try {
            URL sound1 = bgSound.class.getResource(first);
            URL sound2 = bgSound.class.getResource(second);
            if (sound1 == null || sound2 == null) return;

            // Phát file 1
            AudioInputStream audioIn1 = AudioSystem.getAudioInputStream(sound1);
            Clip clip1 = AudioSystem.getClip();
            clip1.open(audioIn1);
            clip1.start();

            // Phát file 2 cùng lúc
            AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(sound2);
            Clip clip2 = AudioSystem.getClip();
            clip2.open(audioIn2);
            clip2.start();

            // Lưu 1 trong 2 clip để có thể stop() sau này
            clip = clip1;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}