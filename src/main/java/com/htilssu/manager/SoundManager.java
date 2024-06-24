package com.htilssu.manager;

import com.htilssu.util.AssetUtils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class SoundManager {

    private static Clip backgroundClip;
    private static final Map<Integer, AudioInputStream> soundMap = new HashMap<>();
    private static int currentVolume = 100; // Giá trị âm lượng mặc định (0-100)
    private static boolean isBackgroundPlaying = false;

    public static final int BOOM_SOUND = 1;
    public static final int DUCK_SOUND = 2;
    public static final int PUT_SHIP_SOUND = 3;
    public static final int ATTACK_SOUND = 4;
    public static final int BACKGROUND_TEST = 5;
    public static final int BACKGROUND_MENU = 6;

    static {
        soundMap.put(BOOM_SOUND, AssetUtils.loadSound("/sound/A_BoomSound.wav"));
        soundMap.put(DUCK_SOUND, AssetUtils.loadSound("/sound/A_DuckSound.wav"));
        soundMap.put(PUT_SHIP_SOUND, AssetUtils.loadSound("/sound/A_PutShipSound.wav"));
        soundMap.put(ATTACK_SOUND, AssetUtils.loadSound("/sound/A_SoundAttack.wav"));
        soundMap.put(BACKGROUND_TEST, AssetUtils.loadSound("/sound/A_SoundNen.wav"));
        soundMap.put(BACKGROUND_MENU, AssetUtils.loadSound("/sound/Action_4.wav"));
    }

    public static void playSound(int soundName) {
        try {
            var audioInputStream = soundMap.get(soundName);
            var clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            setVolume(clip, currentVolume); //  Đặt âm lượng cho hiệu ứng âm thanh
            clip.start();
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void playBackGround(int backgroundSound) {
        var audioInputStream = soundMap.get(backgroundSound);
        if (isBackgroundPlaying && backgroundClip != null && backgroundClip.isRunning()) {
            return; // Nếu nhạc nền đã đang phát thì không làm gì cả
        }

        if (backgroundClip != null) {
            backgroundClip.stop();
            backgroundClip.close();
        }
        isBackgroundPlaying = false;

        try {
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioInputStream);
            setVolume(backgroundClip, currentVolume); //  Đặt âm lượng cho nhạc nền
            backgroundClip.start();
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            isBackgroundPlaying = true;
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
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
}
