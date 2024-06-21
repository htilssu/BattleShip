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
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static com.htilssu.util.AssetUtils.*;

public class NetworkScreen extends GamePanel implements ComponentListener {
    BattleShip battleShip;
    List<HostListItem> hostListItems = new ArrayList<>();
    List<Component> listStruct = new ArrayList<>();
    BufferedImage listHostBackground;
    GamePanel listHostPanel;
    int margin = 50;
    private GamePanel hostListTextPanel;
    private JPanel buttonPanel;
    private GamePanel hostSettingPanel;

    public NetworkScreen(BattleShip battleShip) {
        super();
        this.battleShip = battleShip;
        listHostBackground = AssetUtils.loadImage("/ListHostBG.jpg");

        setBackgroundImage(AssetUtils.loadImage("/sea_of_thief.png"));
        setLayout(null);
        setFocusable(true);

        addComponentListener(this);

        initListHost();
        initListHostText();
        initBackButton();
        initHostSettingPanel();
    }

    private void initListHost() {
        listHostPanel = new GamePanel();
        listHostPanel.setBackground(new Color(.1f, .2f, .3f, .9f));

        listHostPanel.setLayout(new BoxLayout(listHostPanel, BoxLayout.Y_AXIS));


        add(listHostPanel);
        listHostPanel.add(Box.createVerticalStrut(margin));

//        initListHostHeader();


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

    private void initHostSettingPanel() {
        hostSettingPanel = new GamePanel(AssetUtils.loadImage("/Menu_Bg.png"));

        hostSettingPanel.setLayout(new BoxLayout(hostSettingPanel, BoxLayout.Y_AXIS));
        //create exit button
        var exitButton = new GamePanel(AssetUtils.loadImage("/Icon_X.png"));

        exitButton.setMaximumSize(new Dimension(32, 32));
        exitButton.setPreferredSize(exitButton.getMaximumSize());
        exitButton.setAlignmentX(RIGHT_ALIGNMENT);
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideHostSettingPanel();
            }
        });

        hostSettingPanel.add(Box.createVerticalStrut(20));
        hostSettingPanel.add(new GamePanel() {{
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(Box.createHorizontalGlue());
            add(exitButton);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            add(Box.createHorizontalStrut(30));
        }});
        hostSettingPanel.setSize(new Dimension(600, 450));

        hostSettingPanel.setVisible(true);
    }

    private void initListHostButton() {
        buttonPanel = GamePanel.createHorizontalBox();
        buttonPanel.setBackground(com.htilssu.util.Color.TRANSPARENT);

        var refreshButton = new GamePanel(AssetUtils.getImage(ASSET_REFRESH_BUTTON));
        refreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                refreshNetwork();
            }
        });
        Dimension buttonSize = new Dimension(64 * 3, 64);
        refreshButton.setPreferredSize(buttonSize);
        refreshButton.setMaximumSize(buttonSize);
        var createHost = new GamePanel(AssetUtils.loadImage("/Icon_Add.png"));
        createHost.setMaximumSize(new Dimension(64, 64));
        createHost.setPreferredSize(createHost.getMaximumSize());

        createHost.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (hostSettingPanel.isVisible()) {
                    hideHostSettingPanel();
                }
                else showHostSettingPanel();

            }
        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(createHost);
        buttonPanel.add(Box.createHorizontalGlue());


        listHostPanel.add(Box.createVerticalGlue());
        listHostPanel.add(buttonPanel);
    }

    private void hideHostSettingPanel() {
        int targetWidth = 0; // The desired width
        int targetHeight = 0; // The desired height
        int speed = 40; // The speed of the animation

        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            // Decrease the size of the panel
            int newWidth = Math.max(hostSettingPanel.getWidth() - speed, targetWidth);
            int newHeight = Math.max(hostSettingPanel.getHeight() - speed, targetHeight);
            hostSettingPanel.setSize(newWidth, newHeight);

            updateUI();
            repaint();

            // Center the panel
            hostSettingPanel.setLocation((getWidth() - newWidth) / 2, (getHeight() - newHeight) / 2);

            // Stop the timer when the target size is reached
            if (newWidth == targetWidth && newHeight == targetHeight) {
                ((Timer) e.getSource()).stop();
                hostSettingPanel.setVisible(false);
                remove(hostSettingPanel);
            }
        });
        timer.start();
    }

    public void refreshNetwork() {
        Client client = battleShip.getClient();
        client.scanHost();
    }

    private void showHostSettingPanel() {
        add(hostSettingPanel, 0);
        updateUI();

        hostSettingPanel.setSize(0, 0);
        hostSettingPanel.setLocation(getWidth() / 2, getHeight() / 2);
        hostSettingPanel.setVisible(true);

        int targetWidth = 600;
        int targetHeight = 450;
        int speed = 40;

        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            int newWidth = Math.min(hostSettingPanel.getWidth() + speed, targetWidth);
            int newHeight = Math.min(hostSettingPanel.getHeight() + speed, targetHeight);
            hostSettingPanel.setSize(newWidth, newHeight);
            updateUI();
            repaint();
            hostSettingPanel.setLocation((getWidth() - newWidth) / 2, (getHeight() - newHeight) / 2);

            if (newWidth == targetWidth && newHeight == targetHeight) {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    public void updateListHost(List<InetAddress> hostList) {

        for (HostListItem hostListItem : hostListItems) {
            listHostPanel.remove(hostListItem);
        }

        for (Component component : listStruct) {
            listHostPanel.remove(component);
        }

        listStruct.clear();
        hostListItems.clear();

        for (InetAddress host : hostList) {
            HostListItem hostListItem = new HostListItem(host.getHostName(), host, "Ready");
            hostListItem.setRadius(20);
            hostListItems.add(hostListItem);
            var struct = Box.createVerticalStrut(20);
            listHostPanel.add(struct);
            listStruct.add(struct);
            listHostPanel.add(hostListItem);
        }

        updateUI();
        repaint();
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


    class HostListItem extends GamePanel implements MouseListener {
        String hostName;
        InetAddress ipAddress;
        String status;

        public HostListItem(String hostName, InetAddress ipAddress, String status) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            this.hostName = hostName;
            this.ipAddress = ipAddress;
            this.status = status;

            setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
            setPreferredSize(new Dimension(Integer.MAX_VALUE, 64));

            addMouseListener(this);


            add(Box.createHorizontalStrut(15));
            initChildComponent();
            add(Box.createHorizontalStrut(15));
        }

        private void initChildComponent() {
            var contentPanel = GamePanel.createHorizontalBox();
            contentPanel.setRadius(10);
            contentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            contentPanel.setBackgroundImage(AssetUtils.getImage(ASSET_TEXT_FIELD));

            var labelHostName = new JLabel(hostName);
            labelHostName.setForeground(Color.WHITE);
            var labelStatus = new JLabel(status);
            labelStatus.setForeground(Color.WHITE);

            //set font
            labelHostName.setFont(gameFont);
            labelHostName.setAlignmentY(CENTER_ALIGNMENT);
            labelStatus.setFont(gameFont);
            labelHostName.setBackground(Color.WHITE);

            contentPanel.add(Box.createHorizontalStrut(margin));
            contentPanel.add(labelHostName);
            contentPanel.add(Box.createHorizontalGlue());
            contentPanel.add(new JLabel(ipAddress.getHostAddress()) {{
                setAlignmentX(CENTER_ALIGNMENT);
                setForeground(Color.WHITE);
                setFont(gameFont);
            }});
            contentPanel.add(Box.createHorizontalGlue());
            contentPanel.add(labelStatus);
            contentPanel.add(Box.createHorizontalStrut(margin));

            add(contentPanel);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 1){
                battleShip.getClient().connect(this.ipAddress,GameSetting.DEFAULT_PORT);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getDefaultCursor());
        }
    }
}
