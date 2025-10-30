package thegame.sound;

import java.net.URL;
import javax.sound.sampled.*;

public class bgSound {
    private static Clip clip;
    private static long pausePosition = 0; 

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

    public static void pause() {
        if (clip != null && clip.isRunning()) {
            pausePosition = clip.getMicrosecondPosition(); 
            clip.stop();
        }
    }

    public static void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.setMicrosecondPosition(pausePosition);
            clip.start();
        }
    }

    public static void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
            pausePosition = 0;
        }
    }

    public static void playSequential(String first, String second) {
        stop();
        try {
            URL sound1 = bgSound.class.getResource(first);
            URL sound2 = bgSound.class.getResource(second);
            if (sound1 == null || sound2 == null) return;

            AudioInputStream audioIn1 = AudioSystem.getAudioInputStream(sound1);
            AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(sound2);

            Clip clip1 = AudioSystem.getClip();
            Clip clip2 = AudioSystem.getClip();

            clip1.open(audioIn1);
            clip2.open(audioIn2);

            clip1.start();
            clip2.start();

            clip = clip1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}