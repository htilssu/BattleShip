package com.htilssu.entity.component;

import com.htilssu.dataPlayer.PlayerData;
import com.htilssu.screen.Start2Player;
import com.htilssu.setting.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SelfGrid extends BattleGrid {
  private String gridType = "selfGrid";  // Chuỗi xác định loại lưới (ở đây là "selfGrid").
  private boolean isSelfGridListener;  // Biến boolean để kiểm tra xem listener của lưới có đang hoạt động hay không.
  private String name;  // Tên người chơi
  private Point secondNextPoint = new Point(0, 0);  // Tọa độ của ô liền kề thứ hai từ firstPoint.
  private JPanel secondNextCell = null;  // Biến JPanel lưu trữ ô liền kề thứ hai này.
  private Point thirdNextPoint = new Point(0, 0);  // Tọa độ của ô liền kề thứ ba từ firstPoint.
  private JPanel thirdNextCell = null;  // Biến JPanel lưu trữ ô liền kề thứ ba này.
  private Start2Player battleShip;  // Tham chiếu đến đối tượng BattleShip.
  private JPanel thePanel = new JPanel();  // Biến JPanel được sử dụng để tạm thời lưu trữ ô hiện tại.
  private boolean isHorizontal = true; // Biến để xác định hướng của tàu
  public static final int MAXIMIZED_BOTH = JFrame.MAXIMIZED_BOTH / 6;   //lay kich thuoc full man hinh
  SoundPlayer soundPlayer = new SoundPlayer();

  public SelfGrid(String name, Start2Player battleShip) {
    super();
    this.name = name;
    this.battleShip = battleShip;
  }

  // Phương thức này gọi getComponentAt để lấy JPanel tại tọa độ được chỉ định và gán nó cho biến thePanel:
  public void getJpanel(Point newPoint) {
    thePanel = this.getComponentAt(newPoint);
    if (thePanel == null) {
      System.err.println("The panel at " + newPoint + " is null.");
    }
  }

  public void getComp2(Point newPoint) {
    secondNextCell = this.getComponentAt(newPoint);
  }

  public void getComp3(Point newPoint) {
    thirdNextCell = this.getComponentAt(newPoint);
  }

  // Phương thức getCell tạo ra các ô JPanel và gán listener cho chúng.
  // Khi một ô được nhấp chuột, nó sẽ tính toán các tọa độ và đặt các tàu lên lưới.
  @Override
  protected JPanel getCell() {
    JPanel firstCell = new JPanel();
    firstCell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    firstCell.setPreferredSize(new Dimension(25, 25));

    firstCell.setBackground(new Color(0, 0, 0, 0)); // Đặt màu nền trong suốt

    firstCell.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (isSelfGridListener) {

          Point firstPoint = firstCell.getLocation();
          int x = (int) (firstPoint.getX() / 25 + 1);
          int y = (int) (firstPoint.getY() / 25 + 1);
          System.out.println("Clicked at (" + x + "," + y + ")");

          // Kiểm tra xem tọa độ đã được đặt thuyền chưa và số lượng tàu đủ chưa
          PlayerData playerData = name.equals("Player1") ? battleShip.getPlayer1Data() : battleShip.getPlayer2Data();
          if (playerData.getSelfData()[x][y] == 1) {
            System.out.println("Toa do da bi trung. Vui long chon khac!");
            return;
          }
          if (playerData.shipsCount() >= 4) {
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
            } else { // Thêm tàu nằm dọc
              coords.add(new Coordinate(x, y + i));
            }
          }

          if (name.equals("Player1")) {
            battleShip.getPlayer1Data().addShip(coords);
            // Tạo một khoảng thời gian chờ
            soundPlayer.wait_Giay(200);
            draw();

          } else if (name.equals("Player2")) {
            battleShip.getPlayer2Data().addShip(coords);
            // Tạo một khoảng thời gian chờ
            soundPlayer.wait_Giay(200);
            draw();
          }

          // Get components at secondNextPoint and thirdNextPoint
          getComp2(secondNextPoint);
          getComp3(thirdNextPoint);
        }
      }
    });
    return firstCell;
  }
  // Phương thức để xoay tàu
  public void rotateShip() {
    isHorizontal = !isHorizontal; //Đảo ngược trạng thái hướng của tàu
  }

  // Phương thức này vẽ lại lưới sau khi các tàu đã được đặt.
  // Nó lấy dữ liệu của người chơi và tô màu các ô tương ứng.
  public void draw() {
    int[][] temp = null;
    PlayerData playerData = null;

    // Lấy dữ liệu của người chơi tương ứng
    if (name.equals("Player1")) {
      playerData = battleShip.getPlayer1Data();
    } else if (name.equals("Player2")) {
      playerData = battleShip.getPlayer2Data();
    }

    if (playerData != null) {
      temp = playerData.getSelfData();
      //thay đổi màu nền của các ô JPanel dựa trên giá trị của phần tử tương ứng trong mảng temp.
      for (int i = 0; i < 11; i++) {
        for (int j = 0; j < 11; j++) {
          //Kiểm tra xem phần tử tại vị trí [i][j] trong mảng temp có giá trị bằng 1 hay không
          // (1 có thể đại diện cho việc tàu đã được đặt tại vị trí này).
          if (temp[i][j] == 1) {
            int x = numberToPanel(i);
            int y = numberToPanel(j);

            Point p = new Point(x, y);
            //Gọi phương thức getJpanel(p) để lấy JPanel tại tọa độ p và gán nó cho biến thePanel.
            getJpanel(p);
            //Thay đổi màu nền của JPanel
            //có tàu tại vị trí hàng i và cột j.
            if (temp[i][j] == 1) {
              if (thePanel != null) {
                thePanel.setBackground(Color.RED);
              } else {
                System.err.println("Error: Panel at (" + i + "," + j + ") is null.");
              }
            }
          }
        }
      }
    }
  }

  //Hàm numberToPanel(int s) chuyển đổi giá trị s từ hệ tọa độ dữ liệu (0-10) thành hệ tọa độ giao diện đồ họa.
  public int numberToPanel(int s) {
    return (s - 1) * 25;
  }

  public void setSelfGridListener(boolean selfGridListener) {
    this.isSelfGridListener = selfGridListener;
  }

  public boolean getSelfGridListener() {
    return isSelfGridListener;
  }

  public String getGridType() {
    return gridType;
  }
}