package com.htilssu.manager;

import com.htilssu.util.AssetUtils;
import com.htilssu.util.GameLogger;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class SoundManager {

    public static final int BOOM_SOUND = 1;
    public static final int DUCK_SOUND = 2;
    public static final int PUT_SHIP_SOUND = 3;
    public static final int ATTACK_SOUND = 4;
    public static final int BACKGROUND_TEST = 5;
    public static final int START_SOUND = 6;
    public static final int ERROR_SOUND = 7;
    private static final Map<Integer, String> soundMap = new HashMap<>();
    static boolean isBackgroundPlaying = false;
    private static Clip backgroundClip;

    static {
        soundMap.put(BOOM_SOUND, "/sounds/A_BoomSound.wav");
        soundMap.put(DUCK_SOUND, "/sounds/A_DuckSound.wav");
        soundMap.put(PUT_SHIP_SOUND, "/sounds/A_PutShipSound.wav");
        soundMap.put(ATTACK_SOUND, "/sounds/A_SoundAttack.wav");
        soundMap.put(BACKGROUND_TEST, "/sounds/A_SoundNen.wav");
        soundMap.put(START_SOUND, "/sounds/A_SoundStart.wav");
        soundMap.put(ERROR_SOUND, "/sounds/A_ErrorSound.wav");
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
            GameLogger.log(e.getMessage());
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

        } catch (LineUnavailableException | IOException e) {
            GameLogger.log(e.getMessage());

        }
    }

    ///ham delay
    public static void wait_Giay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            GameLogger.log(e.getMessage());
        }
    }
}
