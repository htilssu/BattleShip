package com.htilssu.entity.component;

import com.htilssu.dataPlayer.PlayerData;
import com.htilssu.manager.SoundManager;
import com.htilssu.ui.component.GamePanel;
import com.htilssu.ui.screen.Player2Screen;
import com.htilssu.ui.screen.Start2Player;
import com.htilssu.util.AssetUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 Represents the player's own grid
 */
public class AttackGrid extends BattleGrid {
    private String name;
    private int enemyShipSunkPlayer1 = 0;
    private int enemyShipSunkPlayer2 = 0;
    private boolean isAttackGridListener ;
    private Start2Player battleShip;
    private Player2Screen player;
    private JPanel thePanel = null;

    public AttackGrid(String name,Start2Player startScreen,Player2Screen player) {
        super();
        this.name = name;
        this.player = player;
        this.battleShip = startScreen;

        isInsideGrid();
    }

    //theo dõi khi chuột ra khỏi lưới
    private void isInsideGrid() {
        //chỉ xóa các ô mẫu khi chuột di chuyển ra khỏi lưới.
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!isInsideGrid(e.getPoint())) {
                    clearHighlight();
                }
            }
        });
        //xóa tất cả các ô mẫu ngay khi chuột rời khỏi lưới, không cần phải kiểm tra vị trí cụ thể của chuột.
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                clearHighlight();
            }
        });
    }
    // Kiểm tra xem chuột có đang nằm trong khu vực lưới không(nam ngoai tra ve false)
    private boolean isInsideGrid(Point p) {
        return p.x >= 0 && p.x < self.getWidth() && p.y >= 0 && p.y < self.getHeight();
    }

    //Khi người chơi nhấp vào một ô, nó sẽ kiểm tra xem lưới tấn công có đang hoạt động hay không (isAttackGridListener),
    //và nếu có, nó sẽ xử lý cuộc tấn công.
    @Override
    protected JPanel getCell()
    {
        GamePanel panel = new GamePanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.setPreferredSize(new Dimension(38, 38));

        panel.setBackground(new Color(0, 0, 0, 0)); // Đặt màu nền trong suốt

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(isAttackGridListener) {
                    Point i = panel.getLocation();
                    double xPos = (i.getX() / 38 + 1); // cập nhật phép tính theo kích thước mới
                    int x = (int) xPos;
                    double yPos = (i.getY() / 38 + 1); // cập nhật phép tính theo kích thước mới
                    int y = (int) yPos;

                    if (name.equals("Player1")) {
                        if(!battleShip.getTakeTurnAttack()) {
                            battleShip.setTakeTurnAttack(true);
                            Coordinate hit = new Coordinate(x, y);
                            battleShip.getPlayer2Data().attackShip(hit);

                            boolean success = battleShip.getPlayer2Data().isHit(hit);
                            if (success) {
                                SoundManager.playSound(SoundManager.BOOM_SOUND);
                                battleShip.getPlayer1Data().setAttackData(x, y, "success");
                                SoundManager.wait_Giay(250);
                                draw();
                            } else {
                                SoundManager.playSound(SoundManager.DUCK_SOUND);
                                battleShip.getPlayer1Data().setAttackData(x, y, "failure");
                                SoundManager.wait_Giay(250);
                                draw();
                            }

                            boolean isSunk = battleShip.getPlayer2Data().isSunk(hit);
                            if (isSunk) {
                                //kiem tra chien thang truoc
                                boolean lost = battleShip.getPlayer2Data().isPlayerLost();
                                if (lost) {
                                    battleShip.setState(battleShip.getEndOfTheGame());
                                    JOptionPane.showMessageDialog(null, "You(player 1) WON! Congratulations!\nClick OK will Exit the game");
                                    battleShip.player1Turn();
                                }

                                enemyShipSunkPlayer1++;
                                battleShip.getPlayer1().enemyShipSunk.setText(Integer.toString(enemyShipSunkPlayer1));
                                JOptionPane.showMessageDialog(null,"Thuyen dich bi danh chim!\nnhan OK chuyen sang man hinh player2");
                                player.hideScreen();
                                battleShip.getPlayer2().showScreen();
                                String ownShipSunkPlayer2 = Integer.toString(battleShip.getPlayer2Data().getNumberOfOwnShipSunk());
                                battleShip.getPlayer2().ownShipSunk.setText(ownShipSunkPlayer2);
                            }
                        }

                    }
                    if (name.equals("Player2")) {
                        if(battleShip.getTakeTurnAttack()) {
                            battleShip.setTakeTurnAttack(false);
                            Coordinate hit = new Coordinate(x, y);
                            battleShip.getPlayer1Data().attackShip(hit);

                            boolean success = battleShip.getPlayer1Data().isHit(hit);
                            if (success) {
                                SoundManager.playSound(SoundManager.BOOM_SOUND);
                                System.out.print("player2 attack");
                                battleShip.getPlayer2Data().setAttackData(x, y, "success");
                                SoundManager.wait_Giay(250);
                                draw();
                            } else {
                                SoundManager.playSound(SoundManager.DUCK_SOUND);
                                battleShip.getPlayer2Data().setAttackData(x, y, "failure");
                                SoundManager.wait_Giay(250);
                                draw();
                            }

                            boolean isSunk = battleShip.getPlayer1Data().isSunk(hit);
                            if (isSunk) {
                                //kiem tra chien thang truoc
                                boolean lost = battleShip.getPlayer1Data().isPlayerLost();
                                if (lost) {
                                    battleShip.setState(battleShip.getEndOfTheGame());
                                    JOptionPane.showMessageDialog(null, "You(player 2) WON! Congratulations!\nClick OK will Exit the game");
                                    battleShip.player2turn();
                                }

                                enemyShipSunkPlayer2++;
                                battleShip.getPlayer2().enemyShipSunk.setText(Integer.toString(enemyShipSunkPlayer2));
                                JOptionPane.showMessageDialog(null, "Chuc mung! Thuyen da duoc danh chim!\nnhan OK chuyen sang man hinh player1");
                                player.hideScreen();
                                battleShip.getPlayer1().showScreen();
                                String ownShipSunkPlayer1 = Integer.toString(battleShip.getPlayer1Data().getNumberOfOwnShipSunk());
                                battleShip.getPlayer1().ownShipSunk.setText(ownShipSunkPlayer1);
                            }
                        }
                    }
                }
            }
        });
        // MouseMotionListener để xử lý sự kiện di chuyển chuột
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Kiểm tra xem tọa độ đã được đặt thuyền chưa và số lượng tàu đủ chưa
                PlayerData playerData = name.equals("Player1") ? battleShip.getPlayer1Data() : battleShip.getPlayer2Data();
                if (isAttackGridListener) {
                    Point firstPoint = panel.getLocation();
                    int x = (int) (firstPoint.getX() / 38 );
                    int y = (int) (firstPoint.getY() / 38 );

                    // Hiển thị ô mẫu cho thuyền
                    highlightCells(x, y);
                }
            }
        });

        return panel;
    }
    // Phương thức để hiển thị các ô mẫu
    private void highlightCells(int startX, int startY) {
        // Xóa các ô mẫu hiện tại
        clearHighlight();
            int x =  startX;
            int y =  startY;

            GamePanel cell = (GamePanel) self.getComponentAt(new Point(x * 38, y * 38));
            if (cell != null) {
                if (!Color.WHITE.equals(cell.getBackground()) && !Color.RED.equals(cell.getBackground())) {
                    cell.setBackground(Color.BLUE);
                    cell.setBackgroundImage(AssetUtils.getImage(AssetUtils.ASSET_SELECT)); // Đặt màu cho ô mẫu
                }
            }
    }

    // Phương thức để xóa các ô mẫu
    private void clearHighlight() {
        for (Component comp : self.getComponents()) {
            if (comp instanceof JPanel) {
                GamePanel cell = (GamePanel) comp;
                if (!Color.WHITE.equals(cell.getBackground()) && !Color.RED.equals(cell.getBackground())) {
                    cell.setBackground(new Color(0, 0, 0, 0));
                    cell.setOpaque(false);
                    cell.setBackgroundImage(null); // Xóa hình ảnh nền
                }
            }
        }
    }

    public void setAttackGridListener (boolean attackGridListener){
        this.isAttackGridListener = attackGridListener;
    }
    public void getJpanel(Point newPoint){
        thePanel = this.getComponentAt(newPoint);
    }

    public void draw(){
        int[][] temp=null;
        if(name.equals("Player1")){
            temp = battleShip.getPlayer1Data().getAttackData();
        }
        else if(name.equals("Player2")){
            temp = battleShip.getPlayer2Data().getAttackData();
        }

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++)
            {
                //trung tau
                if(temp[i][j]==1){
                    int x = numberToPanel(i);
                    int y = numberToPanel(j);

                    Point p = new Point(x,y);
                    getJpanel(p);
                    thePanel.setBackground(Color.RED);
                    if (thePanel instanceof GamePanel gamePanel) {
                        gamePanel.setBackgroundImage(AssetUtils.getImage(AssetUtils.ASSET_SHOOT_MISS));
                    }
                }
                //khong trung tau
                if(temp[i][j]==2){
                    int x = numberToPanel(i);
                    int y = numberToPanel(j);

                    Point p = new Point(x,y);
                    getJpanel(p);
                    thePanel.setBackground(Color.WHITE);
                    if (thePanel instanceof GamePanel gamePanel) {
                        gamePanel.setBackgroundImage(AssetUtils.getImage(AssetUtils.ASSET_SHOOT_MISS));
                    }
                }
            }
        }
    }

    public int numberToPanel(int s){
        int temp = (s-1)*38;
        return temp;
    }
}
