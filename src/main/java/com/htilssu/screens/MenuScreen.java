package com.htilssu.screens;

import javax.swing.*;
import java.awt.*;

public class MenuScreen extends JPanel {


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, 0, 300, 300);
    }
}
