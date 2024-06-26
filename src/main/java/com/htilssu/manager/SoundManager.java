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
    public static final int BACKGROUND_MENU = 6;
    public static final int START_SOUND = 7;
    public static final int ERROR_SOUND = 8;
    public static final int NOTIFY_SOUND = 9;
    private static final Map<Integer, String> soundMap = new HashMap<>();
    public static boolean flagVolumeplaySound = true;
    static boolean isBackgroundPlaying = false;
    private static int currentVolume = 100; // Giá trị âm lượng mặc định (0-100)
    private static Clip backgroundClip;
    private static FloatControl backgroundVolumeControl; //dieu chinh am luong

    static {
        soundMap.put(BOOM_SOUND, "/sounds/A_BoomSound.wav");
        soundMap.put(DUCK_SOUND, "/sounds/A_DuckSound.wav");
        soundMap.put(PUT_SHIP_SOUND, "/sounds/A_PutShipSound.wav");
        soundMap.put(ATTACK_SOUND, "/sounds/A_SoundAttack.wav");
        soundMap.put(BACKGROUND_TEST, "/sounds/A_SoundNen.wav");
        soundMap.put(START_SOUND, "/sounds/A_SoundStart.wav");
        soundMap.put(ERROR_SOUND, "/sounds/A_ErrorSound.wav");
        soundMap.put(NOTIFY_SOUND, "/sounds/A_NotifySound.wav");
        soundMap.put(BACKGROUND_MENU, "/sounds/Action_4.wav");
    }

    public static synchronized void playSound(int soundName) {
        if (!flagVolumeplaySound) {
            return;
        }
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
            setVolume(clip, currentVolume); //  Đặt âm lượng cho hiệu ứng âm thanh
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

    public static void setVolume(Clip clip, int volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float minVol = gainControl.getMinimum();
            float maxVolume = gainControl.getMaximum();
            float newVolume = -30 + Math.abs(-30 - maxVolume) * volume / 100;
            gainControl.setValue(newVolume);
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
            setVolume(backgroundClip, currentVolume); //  Đặt âm lượng cho nhạc nền
//            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            isBackgroundPlaying = true;
        } catch (LineUnavailableException | IOException e) {
            GameLogger.log(e.getMessage());

        }
    }

    public static void setBackgroundVolume(int volume) {
        currentVolume = volume;
        if (backgroundClip != null) {
            setVolume(backgroundClip, volume);
        }
    }

    public static int getCurrentVolume() {
        return currentVolume;
    }

    public static boolean isBackgroundPlaying() {
        return isBackgroundPlaying;
    }

    public static Clip getBackgroundClip() {
        return backgroundClip;
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
