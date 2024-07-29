package com.htilssu.ui.component;

import com.htilssu.util.AssetUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CustomSliderUI extends BasicSliderUI {

    private static final int THUMB_WIDTH = 8;
    private static final int THUMB_HEIGHT = 16;
    private static final int TRACK_HEIGHT = 12;
    private BufferedImage thumbImage;
    private BufferedImage trackImage;

    public CustomSliderUI(JSlider b) {
        super(b);
        loadThumbImage();
        loadTrackImage();
    }

    private void loadThumbImage() {
        thumbImage = AssetUtils.loadImage("/images/track.png");
    }

    private void loadTrackImage() {
        trackImage = AssetUtils.loadImage("/images/sider.png");
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setDoubleBuffered(true);
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(THUMB_WIDTH, THUMB_HEIGHT);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            c.setOpaque(false);
        }

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        super.paint(g2d, c);
        g2d.dispose();
    }

    @Override
    public void paintFocus(Graphics g) {
        // Do not paint focus to remove focus border
    }

    @Override
    public void paintTrack(Graphics g) {
        if (trackImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Rectangle trackBounds = trackRect;
            int trackX = trackBounds.x;
            int trackY = trackBounds.y + (trackBounds.height - TRACK_HEIGHT) / 2;
            g2d.drawImage(trackImage, trackX, trackY, trackBounds.width, TRACK_HEIGHT, null);

            g2d.dispose();
        }
    }

    @Override
    public void paintThumb(Graphics g) {
        if (thumbImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int thumbX = thumbRect.x + (thumbRect.width - THUMB_WIDTH) / 2;
            int thumbY = thumbRect.y + (thumbRect.height - THUMB_HEIGHT) / 2;
            g2d.drawImage(thumbImage, thumbX, thumbY, THUMB_WIDTH, THUMB_HEIGHT, null);

            g2d.dispose();
        }
    }
}
