package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.entity.Sprite;
import com.htilssu.event.game.GameAction;
import com.htilssu.manager.GameManager;
import com.htilssu.multiplayer.Client;
import com.htilssu.render.Collision;
import com.htilssu.render.Renderable;
import com.htilssu.setting.GameSetting;
import com.htilssu.ui.component.GameButton;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static com.htilssu.util.AssetUtils.getImage;


public class NetworkScreen extends JPanel implements ComponentListener, MouseListener, FocusListener {
    private final BufferedImage hostListText;
    BattleShip battleShip;
    int selectedIndex = -1;
    BufferedImage backGroundAsset;
    List<HostListItem> hostListItems = new ArrayList<>();
    BufferedImage blurHostListArea;
    Sprite listHostBackground;
    GameButton refreshButton;
    int margin = 50;

    public NetworkScreen(BattleShip battleShip) {
        super();
        setLayout(new BorderLayout());
        setFocusable(true);
        addComponentListener(this);
        addMouseListener(this);
        addFocusListener(this);

        this.battleShip = battleShip;
        backGroundAsset = AssetUtils.loadImage("/sea_of_thief.png");
        blurHostListArea = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        refreshButton = new GameButton(new Sprite(getImage(AssetUtils.ASSET_REFRESH_BUTTON)));
        hostListText = getImage(AssetUtils.ASSET_HOST_LIST_TEXT);
        refreshButton.setBounds(0, 0, 300, 300);
    }

    private BufferedImage genListHostBackground() {
        BufferedImage rectangleImage = new BufferedImage((int) (getWidth() - margin * GameSetting.SCALE * 6), getHeight() - margin * 2, BufferedImage.TYPE_INT_ARGB);

        Graphics2D rectangleGraphics = rectangleImage.createGraphics();

        rectangleGraphics.setColor(new Color(.1f, .2f, .3f, .9f));
        rectangleGraphics.fillRoundRect(0, 0, rectangleImage.getWidth(), rectangleImage.getHeight(), 20, 20);

        return rectangleImage;
    }

    public void refreshNetwork() {
        Client client = battleShip.getClient();
        SwingUtilities.invokeLater(() -> {
            client.scanHost();
            updateListHost(client.getHostList());
            repaint();
        });
    }

    private void updateListHost(List<InetAddress> hostList) {
        hostListItems.clear();
        for (int i = 0, hostListSize = hostList.size(); i < hostListSize; i++) {
            InetAddress inetAddress = hostList.get(i);
            hostListItems.add(
                    new HostListItem("Host",
                            inetAddress,
                            "Ready",
                            new Point((getWidth() - listHostBackground.getWidth()) / 2 + margin,
                                    (getHeight() - listHostBackground.getHeight()) / 2 + margin + 50 + 60 * i)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawImage(backGroundAsset, 0, 0, getWidth(), getHeight(), null);
        if (listHostBackground != null) {
            listHostBackground.render(g);
        }
        g2d.drawImage(hostListText, (getWidth() - hostListText.getWidth()) / 2, (getHeight() - listHostBackground.getHeight()) / 2 - hostListText.getHeight() / 2, null);

        g2d.setColor(Color.WHITE);
        g2d.drawRect((getWidth() - listHostBackground.getWidth() + margin) / 2, (getHeight() - listHostBackground.getHeight()) / 2 + margin, listHostBackground.getWidth() - margin, 200);
        renderHeader(g);

        renderListHost(g);
    }

    private void renderListHost(Graphics g) {
        for (HostListItem hostListItem : hostListItems) {
            hostListItem.render(g);
        }
    }

    private void renderHeader(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g.getFontMetrics();
        int statusWidth = fm.stringWidth("Status");
        int ipWidth = fm.stringWidth("IP Address");

        //align center
        g.drawString("Host Name", (getWidth() - listHostBackground.getWidth()) / 2 + margin, (getHeight() - listHostBackground.getHeight()) / 2 + margin + 20);
        g.drawString("IP Address", (getWidth() - ipWidth) / 2, (getHeight() - listHostBackground.getHeight()) / 2 + margin + 20);
        g.drawString("Status", listHostBackground.getX() + listHostBackground.getWidth() - margin - statusWidth, (getHeight() - listHostBackground.getHeight()) / 2 + margin + 20);
    }


    @Override
    public void componentResized(ComponentEvent e) {
        BufferedImage list = genListHostBackground();
        listHostBackground = new Sprite((getWidth() - list.getWidth()) / 2, (getHeight() - list.getHeight()) / 2, list);

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

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0, hostListItemsSize = hostListItems.size(); i < hostListItemsSize; i++) {
            HostListItem hostListItem = hostListItems.get(i);

            if (hostListItem.isInside(e.getPoint())) {
                if (e.getClickCount() == 2) {
                    battleShip.getClient().connect(hostListItem.ipAddress, GameSetting.DEFAULT_PORT);

                    if (Client.getInstance().isConnected()) {
                        Client.getInstance().send(GameAction.JOIN,
                                GameManager.gamePlayer.getId(),
                                GameManager.gamePlayer.getName());
                    }
                }
                selectedIndex = i;
                hostListItem.setSelected(true);
                return;
            }
        }

        selectedIndex = -1;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void focusGained(FocusEvent e) {
        refreshNetwork();
    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    class HostListItem extends Collision implements Renderable {
        String hostName;
        InetAddress ipAddress;
        String status;
        private boolean isSelected;

        public HostListItem(String hostName, InetAddress ipAddress, String status, Point point) {
            this.hostName = hostName;
            this.ipAddress = ipAddress;
            this.status = status;
            setSize(listHostBackground.getWidth() - margin * 2, 50);
            setLocation(point);
        }


        @Override
        public void render(Graphics g) {
            g.setFont(new Font("Arial", Font.BOLD, 13));
            FontMetrics fm = g.getFontMetrics();
            int allHeight = fm.getHeight();
            int ipWidth = fm.stringWidth(ipAddress.getHostAddress());
            int statusWidth = fm.stringWidth(status);

            if (isSelected) {
                g.setColor(Color.RED);
                g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 20, 20);
                g.setColor(Color.WHITE);
            } else {
                g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 20, 20);
            }

            g.drawString(hostName, getX(), getY() + (getHeight() + allHeight / 2) / 2);
            g.drawString(ipAddress.getHostAddress(), getX() + (getWidth() - ipWidth) / 2, getY() + (getHeight() + allHeight / 2) / 2);
            g.drawString(status, getX() + getWidth() - statusWidth, getY() + (getHeight() + allHeight / 2) / 2);
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
    }
}
