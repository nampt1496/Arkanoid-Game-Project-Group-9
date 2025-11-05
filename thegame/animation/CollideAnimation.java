package thegame.animation;

import java.net.URL;
import javax.sound.sampled.*;

public class CollideAnimation {

    private static void playSound(String path) {
        new Thread(() -> {
            try {
                URL soundURL = CollideAnimation.class.getResource(path);
                if (soundURL == null) {
                    System.err.println("Không tìm thấy file âm thanh: " + path);
                    return;
                }

                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();

                clip.addLineListener(e -> {
                    if (e.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });

            } catch (Exception e) {
                System.err.println("Lỗi phát âm thanh: " + e.getMessage());
            }
        }).start();
    }

    public static void playBallWall() {
        playSound("/thegame/sound/source/ballhit0.wav");
    }

    public static void playBallPaddle() {
        playSound("/thegame/sound/source/paddle.wav");
    }

    public static void playBrickBreak() {
        playSound("/thegame/sound/source/brick.wav");
    }

    public static void playPaddleMove() {
        playSound("/thegame/sound/source/paddleMove.wav");
    }

    public static void playAddScore() {
        playSound("/thegame/sound/source/score.wav");
    }

    public static void playLose() {
        playSound("/thegame/sound/source/losing.wav");
    }
}