package com.htilssu.ui.component;

import com.htilssu.util.AssetUtils;

import javax.swing.*;

public class GameLabel extends JLabel {

    public GameLabel(String text) {
        super(text);
        setFont(AssetUtils.gameFont.deriveFont(20f));
    }

    public GameLabel() {
        super();

        setFont(AssetUtils.gameFont.deriveFont(20f));
    }

    public void setFontSize(int size) {
        setFont(AssetUtils.gameFont.deriveFont((float) size));
    }

}
