package com.htilssu.entity.component;

import com.htilssu.dataPlayer.PlayerData;
import com.htilssu.manager.SoundManager;
import com.htilssu.ui.component.GamePanel;
import com.htilssu.ui.screen.Start2Player;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class SelfGrid extends BattleGrid {

    public static final int MAXIMIZED_BOTH = JFrame.MAXIMIZED_BOTH / 6;
    private String gridType = "selfGrid";  // Chuỗi xác định loại lưới (ở đây là "selfGrid").
    private boolean isSelfGridListener;
    // Biến boolean để kiểm tra xem listener của lưới có đang hoạt động hay không.
    private String name;  // Tên người chơi
    private Start2Player battleShip;  // Tham chiếu đến đối tượng BattleShip.
    private JPanel thePanel = new JPanel();
    // Biến JPanel được sử dụng để tạm thời lưu trữ ô hiện tại.
    private boolean isHorizontal = true; // Biến để xác định hướng của tàu
    //lay kich thuoc full man hinh

    public SelfGrid(String name, Start2Player battleShip) {
        super();
        this.name = name;
        this.battleShip = battleShip;

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
        //xóa tất cả các ô mẫu ngay khi chuột rời khỏi lưới, không cần phải kiểm tra vị trí cụ
        // thể của chuột.
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

    // Phương thức để xóa các ô mẫu
    private void clearHighlight() {
        for (Component comp : self.getComponents()) {
            if (comp instanceof JPanel) {
                GamePanel cell = (GamePanel) comp;
                if (!Color.RED.equals(cell.getBackground())) {
                    cell.setBackground(new Color(0, 0, 0, 0));
                    cell.setOpaque(false);
                    cell.setBackgroundImage(null); // Xóa hình ảnh nền
                }
            }
        }
    }

    // Phương thức getCell tạo ra các ô JPanel và gán listener cho chúng.
    // Khi một ô được nhấp chuột, nó sẽ tính toán các tọa độ và đặt các tàu lên lưới.
    @Override
    protected JPanel getCell() {
        GamePanel firstCell = new GamePanel();
        firstCell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        firstCell.setPreferredSize(new Dimension(34, 34));

        firstCell.setBackground(com.htilssu.util.Color.TRANSPARENT); // Đặt màu nền trong suốt

        firstCell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isSelfGridListener) {

                    Point firstPoint = firstCell.getLocation();
                    int x = (int) (firstPoint.getX() / 34 + 1);
                    int y = (int) (firstPoint.getY() / 34 + 1);
                    System.out.println("Clicked at (" + x + "," + y + ")");

                    // Kiểm tra xem tọa độ đã được đặt thuyền chưa và số lượng tàu đủ chưa
                    PlayerData playerData = name.equals("Player1") ? battleShip.getPlayer1Data()
                                                                   : battleShip.getPlayer2Data();
                    if (playerData.getSelfData()[x][y] == 1) {
                        SoundManager.playSound(SoundManager.ERROR_SOUND);
                        System.out.println("Toa do da bi trung. Vui long chon khac!");
                        return;
                    }
                    if (playerData.shipsCount() >= 4) {
                        SoundManager.playSound(SoundManager.ERROR_SOUND);
                        System.out.println("SO LUONG TAU DA DU!");
                        return;
                    }

                    ArrayList<Coordinate> coords = new ArrayList<>();
                    coords.add(new Coordinate(x, y));

                    // Tính toán kích thước tàu dựa trên kích thước đội tàu hiện tại
                    int shipSize = playerData.getFleet().size() + 2;
                    for (int i = 1; i < shipSize; i++) {
                        if (isHorizontal) { // Thêm tàu nằm ngang
                            coords.add(new Coordinate(x + i, y));
                        }
                        else { // Thêm tàu nằm dọc
                            coords.add(new Coordinate(x, y + i));
                        }
                    }

                    if (name.equals("Player1")) {
                        battleShip.getPlayer1Data().addShip(coords);
                        // Tạo một khoảng thời gian chờ
                        SoundManager.wait_Giay(200);
                        draw();

                    }
                    else if (name.equals("Player2")) {
                        battleShip.getPlayer2Data().addShip(coords);
                        // Tạo một khoảng thời gian chờ
                        SoundManager.wait_Giay(200);
                        draw();
                    }

                }
            }
        });

        // MouseMotionListener để xử lý sự kiện di chuyển chuột
        firstCell.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Kiểm tra xem tọa độ đã được đặt thuyền chưa và số lượng tàu đủ chưa
                PlayerData playerData = name.equals("Player1") ? battleShip.getPlayer1Data()
                                                               : battleShip.getPlayer2Data();
                if (isSelfGridListener) {
                    Point firstPoint = firstCell.getLocation();
                    int x = (int) (firstPoint.getX() / 34);
                    int y = (int) (firstPoint.getY() / 34);

                    // Hiển thị ô mẫu cho thuyền
                    highlightCells(x, y, playerData.getFleet().size() + 2);
                }
            }
        });

        return firstCell;
    }

    public void draw() {
        int[][] temp = null;
        PlayerData playerData = null;

        // Lấy dữ liệu của người chơi tương ứng
        if (name.equals("Player1")) {
            playerData = battleShip.getPlayer1Data();
        }
        else if (name.equals("Player2")) {
            playerData = battleShip.getPlayer2Data();
        }

        if (playerData != null) {
            temp = playerData.getSelfData();
            //thay đổi màu nền của các ô JPanel dựa trên giá trị của phần tử tương ứng trong mảng
            // temp.
            for (int i = 0; i < 11; i++) {
                for (int j = 0; j < 11; j++) {
                    //Kiểm tra xem phần tử tại vị trí [i][j] trong mảng temp có giá trị bằng 1
                    // hay không
                    // (1 có thể đại diện cho việc tàu đã được đặt tại vị trí này).
                    if (temp[i][j] == 1) {
                        int x = numberToPanel(i);
                        int y = numberToPanel(j);

                        Point p = new Point(x, y);
                        //Gọi phương thức getJpanel(p) để lấy JPanel tại tọa độ p và gán nó cho
                        // biến thePanel.
                        getJpanel(p);
                        //Thay đổi màu nền của JPanel
                        //có tàu tại vị trí hàng i và cột j.
                        if (thePanel != null) {
                            thePanel.setBackground(Color.RED);
                        }
                        else {
                            System.err.println("Error: Panel at (" + i + "," + j + ") is null.");
                        }
                    }
                }
            }
        }
    }

    // Phương thức để hiển thị các ô mẫu
    private void highlightCells(int startX, int startY, int shipSize) {
        // Xóa các ô mẫu hiện tại
        clearHighlight();

        for (int i = 0; i < shipSize; i++) {
            int x = isHorizontal ? startX + i : startX;
            int y = isHorizontal ? startY : startY + i;

            GamePanel cell = (GamePanel) self.getComponentAt(new Point(x * 34, y * 34));
            if (cell != null) {
                if (!Color.RED.equals(cell.getBackground())) {
                    cell.setBackground(Color.BLUE);
                    cell.setBackgroundImage(
                            AssetUtils.getImage(AssetUtils.ASSET_SELECT)); // Đặt màu cho ô mẫu
                }
            }
        }
    }

    //Hàm numberToPanel(int s) chuyển đổi giá trị s từ hệ tọa độ dữ liệu (0-10) thành hệ tọa độ
    // giao diện đồ họa.
    public int numberToPanel(int s) {
        return (s - 1) * 34;
    }

    // Phương thức này gọi getComponentAt để lấy JPanel tại tọa độ được chỉ định và gán nó cho
    // biến thePanel:
    public void getJpanel(Point newPoint) {
        thePanel = this.getComponentAt(newPoint);
        if (thePanel == null) {
            System.err.println("The panel at " + newPoint + " is null.");
        }
    }

    // Phương thức để xoay tàu
    public void rotateShip() {
        isHorizontal = !isHorizontal; //Đảo ngược trạng thái hướng của tàu
    }

    public boolean getSelfGridListener() {
        return isSelfGridListener;
    }

    public void setSelfGridListener(boolean selfGridListener) {
        this.isSelfGridListener = selfGridListener;
    }

    public String getGridType() {
        return gridType;
    }
}
