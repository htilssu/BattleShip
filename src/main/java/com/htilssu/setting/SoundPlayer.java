package com.htilssu.setting;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {

    private Clip clip;
    private File soundFile;
    private AudioInputStream audioInputStream;
    private boolean isPlaying = false;  // Đặt cờ hiệu đang phát

    public void playSound(String filePath) {
        try {
            // Mở tệp âm thanh
            soundFile = new File(filePath);
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);

            // Tạo clip
            clip = AudioSystem.getClip();

            // Mở luồng âm thanh
            clip.open(audioInputStream);

            // Phát âm thanh
            clip.start();
            // Đặt cờ hiệu đang phát là true
            isPlaying = true;

            // Đợi đến khi âm thanh phát xong
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    // Đặt cờ hiệu đang phát là false khi phát xong
                    isPlaying = false;
                }
            });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playSound_Nen()
    {
        playSound("D:\\CodeGame\\BattleShip\\BattleShip\\src\\main\\resources\\A_SoundNen.wav");
    }
    public void playSound_Boom()
    {
        playSound("D:\\CodeGame\\BattleShip\\BattleShip\\src\\main\\resources\\A_BoomSound.wav");
    }
    public void playSound_Duck()
    {
        playSound("D:\\CodeGame\\BattleShip\\BattleShip\\src\\main\\resources\\A_DuckSound.wav");
    }
    public void playSound_PutShip()
    {
        playSound("D:\\CodeGame\\BattleShip\\BattleShip\\src\\main\\resources\\A_PutShipSound.wav");
    }

    // Phương thức phát âm thanh "Attack" và đợi cho đến khi phát xong
    public void playSound_Attack() {
        playSound("D:\\CodeGame\\BattleShip\\BattleShip\\src\\main\\resources\\A_SoundAttack.wav");
        while (isPlaying) {
            // Chờ đến khi âm thanh "Attack" phát xong
            try {
                Thread.sleep(0); // Chờ 10 miligiây và kiểm tra lại
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void playSound_Start() {
        playSound("D:\\CodeGame\\BattleShip\\BattleShip\\src\\main\\resources\\A_SoundStart.wav");
    }
    public void wait_Giay(int a)
    {
        // Tạo một khoảng thời gian chờ
        try {
            Thread.sleep(a);
        } catch (InterruptedException x) {
            x.printStackTrace();
        }
    }
}

