package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.manager.ScreenManager;
import com.htilssu.multiplayer.Client;
import com.htilssu.setting.GameSetting;
import com.htilssu.ui.component.GameButton;
import com.htilssu.ui.component.GamePanel;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static com.htilssu.util.AssetUtils.*;

public class NetworkScreen extends GamePanel implements ComponentListener {
    BattleShip battleShip;
    List<HostListItem> hostListItems = new ArrayList<>();
    GamePanel listHostPanel;
    int margin = 50;
    private GamePanel hostListTextPanel;
    private JPanel buttonPanel;

    public NetworkScreen(BattleShip battleShip) {
        super();
        this.battleShip = battleShip;

        setBackgroundImage(AssetUtils.loadImage("/sea_of_thief.png"));
        setLayout(null);
        setFocusable(true);

        addComponentListener(this);

        initListHost();


        initListHostText();
        initBackButton();
    }

    private void initListHost() {
        listHostPanel = new GamePanel();
        listHostPanel.setBackground(new Color(.1f, .2f, .3f, .9f));

        listHostPanel.setLayout(new BoxLayout(listHostPanel, BoxLayout.Y_AXIS));


        add(listHostPanel);

        initListHostHeader();

        initListHostButton();
    }

    private void initListHostText() {
        hostListTextPanel = new GamePanel(getImage(AssetUtils.ASSET_HOST_LIST_TEXT));
        hostListTextPanel.setSize(GameSetting.TILE_SIZE * 12, GameSetting.TILE_SIZE * 2);
        add(hostListTextPanel, 0);
    }

    private void initBackButton() {
        var backButton = new GameButton(AssetUtils.getImage(ASSET_BACK_BUTTON));

        backButton.addActionListener(e -> battleShip.changeScreen(ScreenManager.MENU_SCREEN));

        backButton.setSize(new Dimension(64, 64));
        backButton.setLocation(40, 40);

        add(backButton, 0);
    }

    private void initListHostHeader() {

        var font = new Font("Arial", Font.BOLD, 20);

        JLabel hostName = new JLabel("Host Name");
        hostName.setForeground(Color.WHITE);
        hostName.setFont(font);
        JLabel ipAddress = new JLabel("IP Address");
        ipAddress.setForeground(Color.WHITE);
        ipAddress.setFont(font);
        JLabel status = new JLabel("Status");
        status.setForeground(Color.WHITE);
        status.setFont(font);

        var container = new JPanel();
        container.add(Box.createHorizontalStrut(margin));
        container.add(hostName);
        container.add(Box.createHorizontalGlue());
        container.add(ipAddress);
        container.add(Box.createHorizontalGlue());
        container.add(status);
        container.add(Box.createHorizontalStrut(margin));

        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        container.setBackground(com.htilssu.util.Color.TRANSPARENT);

        listHostPanel.add(Box.createVerticalStrut(margin));
        listHostPanel.add(container);
        updateUI();
    }

    private void initListHostButton() {
        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        var refreshButton = new GameButton(AssetUtils.getImage(ASSET_BACK_BUTTON));
        refreshButton.addActionListener(e -> refreshNetwork());
        Dimension buttonSize = new Dimension(64 * 3, 64);
        refreshButton.setPreferredSize(buttonSize);
        refreshButton.setMaximumSize(buttonSize); // Set the maximum size
        var testBtn = new GameButton(AssetUtils.getImage(ASSET_REFRESH_BUTTON));
        testBtn.addActionListener(e -> refreshNetwork());
        testBtn.setMaximumSize(buttonSize); // Set the maximum size
        testBtn.setPreferredSize(buttonSize); // Set the maximum size

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(testBtn);
        buttonPanel.add(Box.createHorizontalGlue());


        listHostPanel.add(buttonPanel);
    }

    public void refreshNetwork() {
        Client client = battleShip.getClient();
        client.scanHost();
    }

    public void updateListHost(List<InetAddress> hostList) {
        hostListItems.clear();
        for (int i = 0, hostListSize = hostList.size(); i < hostListSize; i++) {

        }
    }


    @Override
    public void componentResized(ComponentEvent e) {
        listHostPanel.setSize((int) (getWidth() - margin * GameSetting.SCALE * 6),
                              getHeight() - margin * 2);
        listHostPanel.setMaximumSize(new Dimension((int) (getWidth() - margin * GameSetting.SCALE * 6),
                                                   getHeight() - margin * 3));
        listHostPanel.setLocation((getWidth() - listHostPanel.getWidth()) / 2,
                                  (getHeight() - listHostPanel.getHeight()) / 2);
        hostListTextPanel.setLocation((getWidth() - hostListTextPanel.getWidth()) / 2,
                                      listHostPanel.getY() - hostListTextPanel.getHeight() / 2);

        buttonPanel.setLocation(listHostPanel.getX(), listHostPanel.getY() + listHostPanel.getHeight());
        buttonPanel.setSize(new Dimension(listHostPanel.getWidth(), 64));

        listHostPanel.setRadius(40);

        updateUI();
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


    class HostListItem extends GamePanel {
        String hostName;
        InetAddress ipAddress;
        String status;
        private boolean isSelected;

        public HostListItem(String hostName, InetAddress ipAddress, String status, Point point) {
            super();
            this.hostName = hostName;
            this.ipAddress = ipAddress;
            this.status = status;
            setLocation(point);
        }
    }
}
