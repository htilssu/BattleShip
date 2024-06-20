package com.htilssu.manager;

import com.htilssu.util.AssetUtils;

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
    private static final Map<Integer, AudioInputStream> soundMap = new HashMap<>();
    static boolean isBackgroundPlaying = false;
    private static Clip backgroundClip;

    static {
        soundMap.put(BOOM_SOUND, AssetUtils.loadSound("/sound/A_BoomSound.wav"));
        soundMap.put(DUCK_SOUND, AssetUtils.loadSound("/sound/A_DuckSound.wav"));
        soundMap.put(PUT_SHIP_SOUND, AssetUtils.loadSound("/sound/A_PutShipSound.wav"));
        soundMap.put(ATTACK_SOUND, AssetUtils.loadSound("/sound/A_SoundAttack.wav"));
        soundMap.put(BACKGROUND_TEST, AssetUtils.loadSound("/sound/A_SoundNen.wav"));
    }


    public static void playSound(int soundName) {
        try {
            //Lấy InputStreamSound từ map
            var audioInputStream = soundMap.get(soundName);


            // Tạo clip
            var clip = AudioSystem.getClip();
            // Mở clip
            clip.open(audioInputStream);
            clip.start();

        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void playBackGround(int backgroundSound) {
        var audioInputStream = soundMap.get(backgroundSound);
        if (backgroundClip != null) backgroundClip.stop();

        isBackgroundPlaying = false;

        try {
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

}