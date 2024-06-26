package com.htilssu.ui.component;

import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

public class GameProgress extends JPanel {

    BufferedImage backgroundImage;
    BufferedImage progressImage;
    int progressWidth;
    int maxProgress;

    public GameProgress(int maxProgress) {
        setOpaque(false);
        setBorder(null);

        this.maxProgress = maxProgress * 200;
        backgroundImage = AssetUtils.getImage(AssetUtils.ASSET_PROGRESS_BACKGROUND);
        progressImage = AssetUtils.getImage(AssetUtils.ASSET_PROGRESS);

        setSize(800, 30);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        progressWidth = width;
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2d = (Graphics2D) g.create();

        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        g2d.setClip(new Area(new Rectangle(0, 0, progressWidth, getHeight())));
        g2d.drawImage(progressImage, 0, 0, getWidth(), getHeight(), null);

        super.paintComponent(g);
    }

    public void setProgress(int timeCountDown) {
        //calculate progress width to smooth progress bar
        progressWidth = (int) ((double) timeCountDown / maxProgress * getWidth());
        repaint();
    }
}
