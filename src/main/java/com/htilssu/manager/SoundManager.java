package com.htilssu.manager;

import com.htilssu.util.AssetUtils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class SoundManager {

    private static Clip backgroundClip;
    public static final int BOOM_SOUND = 1;
    public static final int DUCK_SOUND = 2;
    public static final int PUT_SHIP_SOUND = 3;
    public static final int ATTACK_SOUND = 4;
    public static final int BACKGROUND_TEST = 5;
    private static final Map<Integer, AudioInputStream> soundMap = new HashMap<>();
    public static final int START_SOUND = 6;
    public static final int ERROR_SOUND = 7;

    static boolean isBackgroundPlaying = false;
    private static Clip backgroundClip;

    private static Map<Integer, String> soundMap = new HashMap<>();

    static {
        soundMap.put(BOOM_SOUND, "/sound/A_BoomSound.wav");
        soundMap.put(DUCK_SOUND, "/sound/A_DuckSound.wav");
        soundMap.put(PUT_SHIP_SOUND, "/sound/A_PutShipSound.wav");
        soundMap.put(ATTACK_SOUND, "/sound/A_SoundAttack.wav");
        soundMap.put(BACKGROUND_TEST, "/sound/A_SoundNen.wav");
        soundMap.put(START_SOUND, "/sound/A_SoundStart.wav");
        soundMap.put(ERROR_SOUND, "/sound/A_ErrorSound.wav");
    }

    public static synchronized void playSound(int soundName) {
        String filePath = soundMap.get(soundName);
        if (filePath == null) {
            System.err.println("Sound not found: " + soundName);
            return;
        }
/*Tạo AudioInputStream mới trong phương thức playSound: Đảm bảo rằng mỗi lần playSound được gọi,
một AudioInputStream mới sẽ được tạo và sử dụng, am thanh co the phat duoc nhieu lan.*/
        try (AudioInputStream audioInputStream = AssetUtils.loadSound(filePath)) {
            if (audioInputStream == null) {
                System.err.println("Could not load sound: " + filePath);
                return;
            }

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
///ham nay dung de lap lai am thanh
    public static synchronized void playBackGround(int backgroundSound) {
        String filePath = soundMap.get(backgroundSound);
        if (filePath == null) {
            System.err.println("Background sound not found: " + backgroundSound);
            return;
        }

        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
        isBackgroundPlaying = false;

        try (AudioInputStream audioInputStream = AssetUtils.loadSound(filePath)) {
            if (audioInputStream == null) {
                System.err.println("Could not load background sound: " + filePath);
                return;
            }

            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioInputStream);
            backgroundClip.start();
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            isBackgroundPlaying = true;

            //disable volume

            //TODO: remove
            FloatControl gainControl =
                    (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gainControl.getMinimum());
        } catch (LineUnavailableException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    ///ham delay
    public static void wait_Giay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
