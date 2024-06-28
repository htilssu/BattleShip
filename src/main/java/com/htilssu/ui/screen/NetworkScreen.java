package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.manager.ScreenManager;
import com.htilssu.manager.SoundManager;
import com.htilssu.multiplayer.Client;
import com.htilssu.setting.GameSetting;
import com.htilssu.ui.component.GameButton;
import com.htilssu.ui.component.GameLabel;
import com.htilssu.ui.component.GamePanel;
import com.htilssu.ui.component.GameTextField;
import com.htilssu.util.AssetUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static com.htilssu.util.AssetUtils.*;

public class NetworkScreen extends GamePanel implements ComponentListener {

    private final BattleShip battleShip;
    private final List<HostItem> hostItemList = new ArrayList<>();
    private final int margin = 50;
    private final BufferedImage hostItemBackground;
    private GamePanel textHostPanel, hostListPanel, hostSettingPanel, hostItemPanel;
    private GameTextField hostNameTextField;
    private JPanel buttonPanel;

    public NetworkScreen(BattleShip battleShip) {
        super();
        this.battleShip = battleShip;
        hostItemBackground = AssetUtils.loadImage("/images/Item_TextField.png");

        setBackgroundImage(AssetUtils.loadImage("/images/sea_of_thief.png"));
        setLayout(null);
        setFocusable(true);

        addComponentListener(this);

        initListHost();
        initListHostText();
        initBackButton();
        initHostSettingPanel();
    }

    private void initListHost() {
        hostListPanel = new GamePanel();
        hostListPanel.setBackground(new Color(.1f, .2f, .3f, .9f));

        hostListPanel.setLayout(new BoxLayout(hostListPanel, BoxLayout.Y_AXIS));

        add(hostListPanel);
        hostListPanel.add(Box.createVerticalStrut(margin));

        hostListPanel.add(Box.createVerticalStrut(margin));
        initListHostItem();
        initListHostButton();
    }

    private void initListHostText() {
        textHostPanel = new GamePanel(getImage(AssetUtils.ASSET_HOST_LIST_TEXT));
        textHostPanel.setSize(GameSetting.TILE_SIZE * 12, GameSetting.TILE_SIZE * 2);
        add(textHostPanel, 0);
    }

    private void initBackButton() {
        var backButton = new GameButton(AssetUtils.getImage(ASSET_BACK_BUTTON));

        backButton.addActionListener(e -> battleShip.changeScreen(ScreenManager.MENU_SCREEN));

        backButton.setSize(new Dimension(64, 64));
        backButton.setLocation(40, 40);

        add(backButton, 0);
    }

    private void initHostSettingPanel() {
        hostSettingPanel = new GamePanel(AssetUtils.loadImage("/images/Menu_Bg.png"));

        hostSettingPanel.setLayout(new BoxLayout(hostSettingPanel, BoxLayout.Y_AXIS));

        //create exit button
        var exitButton = new GamePanel(AssetUtils.loadImage("/images/Icon_X.png"));

        exitButton.setMaximumSize(new Dimension(32, 32));
        exitButton.setPreferredSize(exitButton.getMaximumSize());
        exitButton.setAlignmentX(RIGHT_ALIGNMENT);
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideHostSettingPanel();
            }
        });

        Box inputBox = getInputBox();


        hostSettingPanel.add(Box.createVerticalStrut(20));
        hostSettingPanel.add(new GamePanel() {{
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(Box.createHorizontalGlue());
            add(exitButton);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            setMinimumSize(getMaximumSize());
            setPreferredSize(getMaximumSize());
            add(Box.createHorizontalStrut(30));
        }});

        hostSettingPanel.add(Box.createVerticalStrut(30));
        hostSettingPanel.add(inputBox);

        hostSettingPanel.add(Box.createVerticalStrut(30));

        //add create host button
        GameButton createHostButton = new GameButton(AssetUtils.getImage(ASSET_BUTTON_2));
        createHostButton.setMaximumSize(new Dimension(64 * 2, 64));
        createHostButton.setPreferredSize(createHostButton.getMaximumSize());
        createHostButton.setMinimumSize(createHostButton.getMaximumSize());
        createHostButton.setAlignmentX(CENTER_ALIGNMENT);
        createHostButton.setText("Create");
        createHostButton.addActionListener(e -> {
            battleShip.getHost().setHostName(hostNameTextField.getText());
            battleShip.getHost().start();
            hideHostSettingPanel();
        });

        hostSettingPanel.add(createHostButton);


        hostSettingPanel.setVisible(false);
    }

    private void initListHostItem() {
        hostItemPanel = new GamePanel();
        hostItemPanel.setLayout(new BoxLayout(hostItemPanel, BoxLayout.Y_AXIS));
        hostListPanel.add(hostItemPanel);
        hostListPanel.add(Box.createVerticalGlue());

    }

    private void initListHostButton() {
        buttonPanel = GamePanel.createHorizontalBox();
        buttonPanel.setBackground(com.htilssu.util.Color.TRANSPARENT);

        var refreshButton = new GameButton(AssetUtils.getImage(ASSET_BUTTON_2));
        refreshButton.setPreferredSize(new Dimension(64 * 2, 64));
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(e -> {
            hostItemPanel.removeAll();
            repaint();
            refreshNetwork();
        });

        Dimension buttonSize = new Dimension(64 * 3, 64);
        refreshButton.setPreferredSize(buttonSize);
        refreshButton.setMaximumSize(buttonSize);
        var createHostButton = new GameButton(AssetUtils.getImage(ASSET_BUTTON_2));
        createHostButton.setMaximumSize(new Dimension(64 * 3, 64));
        createHostButton.setText("Create Host");
        createHostButton.setPreferredSize(createHostButton.getMaximumSize());

        createHostButton.addActionListener(e -> {
            if (!hostSettingPanel.isVisible()) showHostSettingPanel();
        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(createHostButton);
        buttonPanel.add(Box.createHorizontalGlue());


        hostListPanel.add(Box.createVerticalGlue());
        hostListPanel.add(buttonPanel);
        hostListPanel.add(Box.createVerticalStrut(20));
    }

    private void hideHostSettingPanel() {
        int targetWidth = 0;
        int targetHeight = 0;
        int speed = 40;

        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            int newWidth = Math.max(hostSettingPanel.getWidth() - speed, targetWidth);
            int newHeight = Math.max(hostSettingPanel.getHeight() - speed, targetHeight);
            hostSettingPanel.setSize(newWidth, newHeight);

            updateUI();
            repaint();

            hostSettingPanel.setLocation((getWidth() - newWidth) / 2, (getHeight() - newHeight) / 2);

            if (newWidth == targetWidth && newHeight == targetHeight) {
                ((Timer) e.getSource()).stop();
                hostSettingPanel.setVisible(false);
                remove(hostSettingPanel);
            }
        });
        timer.start();
    }

    private Box getInputBox() {
        var inputBox = Box.createHorizontalBox();
        //set test border

        Box inputContentBox = Box.createVerticalBox();
        inputContentBox.setMaximumSize(new Dimension(450, Integer.MAX_VALUE));
        inputContentBox.setPreferredSize(inputContentBox.getMaximumSize());

        inputBox.add(Box.createHorizontalGlue());
        inputBox.add(inputContentBox);
        inputBox.add(Box.createHorizontalGlue());


        //hostName input
        var hostNameBox = Box.createHorizontalBox();
        GameLabel hostNameLabel = new GameLabel("HostName: ");
        hostNameBox.add(hostNameLabel);
        hostNameBox.add(Box.createHorizontalGlue());

        inputContentBox.add(hostNameBox);


        hostNameTextField = new GameTextField();
        hostNameTextField.setName("hostName");
        hostNameTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        hostNameTextField.setRadius(20);


        inputContentBox.add(hostNameTextField);


        inputBox.updateUI();
        return inputBox;
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

        //focus to hostNameTextField
        hostNameTextField.requestFocus();

        int targetWidth = 600;
        int targetHeight = 450;
        int speed = 40;

        final Timer timer = getTimer(speed, targetWidth, targetHeight);
        timer.start();
    }

    private @NotNull Timer getTimer(int speed, int targetWidth, int targetHeight) {
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
        return timer;
    }

    public void updateListHost(List<InetAddress> hostList) {

        hostItemList.clear();
        //remove all child in hostListPanel

        for (InetAddress host : hostList) {
            HostItem hostItem = new HostItem(host.getHostName(), host, "Ready");
            hostItem.setRadius(20);
            hostItemPanel.add(Box.createVerticalStrut(20));
            hostItemPanel.add(hostItem);
        }
        SoundManager.playSound(SoundManager.NOTIFY_SOUND);
        updateUI();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        hostListPanel.setSize((int) (getWidth() - margin * GameSetting.SCALE * 6), getHeight() - margin * 2);
        hostListPanel.setMaximumSize(
                new Dimension((int) (getWidth() - margin * GameSetting.SCALE * 6), getHeight() - margin * 3));
        hostListPanel.setLocation((getWidth() - hostListPanel.getWidth()) / 2,
                                  (getHeight() - hostListPanel.getHeight()) / 2);
        textHostPanel.setLocation((getWidth() - textHostPanel.getWidth()) / 2,
                                  hostListPanel.getY() - textHostPanel.getHeight() / 2);

        buttonPanel.setLocation(hostListPanel.getX(), hostListPanel.getY() + hostListPanel.getHeight());
        buttonPanel.setSize(new Dimension(hostListPanel.getWidth(), 64));

        hostListPanel.setRadius(40);

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

    class HostItem extends GamePanel implements MouseListener {

        String hostName;
        InetAddress ipAddress;
        String status;

        public HostItem(String hostName, InetAddress ipAddress, String status) {
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
            contentPanel.setBackgroundImage(hostItemBackground);

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
            if (e.getClickCount() > 1) {
                Client client = battleShip.getClient();
                client.connect(this.ipAddress, GameSetting.DEFAULT_PORT);

                if (client.isConnected()) {
                    client.join();
                }
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
