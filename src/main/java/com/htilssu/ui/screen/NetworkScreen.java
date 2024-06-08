package com.htilssu.ui.screen;

import static com.htilssu.util.AssetUtils.getAsset;
import static com.htilssu.util.AssetUtils.loadAsset;

import com.htilssu.BattleShip;
import com.htilssu.entity.Sprite;
import com.htilssu.multiplayer.Client;
import com.htilssu.setting.GameSetting;
import com.htilssu.ui.component.GameButton;
import com.htilssu.util.AssetUtils;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import javax.swing.*;

public class NetworkScreen extends JPanel implements ComponentListener {
    BattleShip battleShip;
    BufferedImage backGroundAsset;
    BufferedImage blurHostListArea;
    JButton refreshButton;

    int margin = 50;

    public NetworkScreen(BattleShip battleShip) {
        this.battleShip = battleShip;
        setLayout(null);
        setFocusable(true);
        backGroundAsset = loadAsset("/sea_of_thief.png");
        blurHostListArea = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        // TODO: replace refresh image
        refreshButton =
                new GameButton(new Sprite(getAsset(AssetUtils.ASSET_REFRESH_BUTTON)));
        refreshButton.setBounds(
                (getWidth() - (GameSetting.TILE_SIZE * 3)) / 3 - margin,
                getHeight() - margin - (int) (GameSetting.TILE_SIZE * 2 * GameSetting.SCALE),
                (int) (GameSetting.TILE_SIZE * 4 * GameSetting.SCALE),
                (int) (GameSetting.TILE_SIZE * 2 * GameSetting.SCALE));
        addComponentListener(this);
        add(refreshButton);
        setComponentZOrder(refreshButton, 0);
    }

    public void refreshNetwork() {
        Client client = battleShip.getClient();
        client.scanHost();

        for (InetAddress address : client.getHostList()) {
            //      add(new GameButton());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        BufferedImage hostListText = getAsset(AssetUtils.ASSET_HOST_LIST_TEXT);
        g2d.drawImage(backGroundAsset, 0, 0, getWidth(), getHeight(), null);
        g2d.fillRoundRect(
                (int) (margin * GameSetting.SCALE * 2),
                margin / 2 + hostListText.getHeight() / 2,
                (int) (getWidth() - margin * 2 * GameSetting.SCALE * 2),
                getHeight() - margin * 2,
                20,
                20);

        g2d.drawImage(hostListText, (getWidth() - hostListText.getWidth()) / 2, 25, null);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        refreshButton.setBounds(
                (getWidth() - (int) (GameSetting.TILE_SIZE * 4 * GameSetting.SCALE)) / 3 - margin,
                getHeight() - margin - (int) (GameSetting.TILE_SIZE * 2 * GameSetting.SCALE),
                (int) (GameSetting.TILE_SIZE * 4 * GameSetting.SCALE),
                (int) (GameSetting.TILE_SIZE * 2 * GameSetting.SCALE));
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
