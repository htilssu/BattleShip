package com.htilssu.dataPlayer;

import com.htilssu.entity.component.Coordinate;
import com.htilssu.entity.component.Ship2;
import com.htilssu.manager.SoundManager;
import com.htilssu.ui.screen.Player2Screen;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayerData {

    private static final int MAX_SHIPS = 4;
    private final int[][] attackData = new int[11][11];
    private final int[][] selfData = new int[11][11];
    private final ArrayList<Ship2> fleet = new ArrayList<>();
    private Player2Screen player;
    private int numberOfShipSunk = 0;

    public PlayerData(Player2Screen player) {
        this.player = player;
    }

    // Thêm một tàu mới vào flotilla
    public void addShip(ArrayList<Coordinate> coords) {
        // Kiểm tra xem tàu có bị chồng chéo không hoặc Kiểm tra xem tàu có nằm ở biên của lưới
        // không
        if (isEdge(coords) || isOverlap(coords)) {
            SoundManager.playSound(SoundManager.ERROR_SOUND);
            System.out.println("DAT TAU KHONG HOP LE! DAT LAI.");
            return;
        }
        else {
            SoundManager.playSound(SoundManager.PUT_SHIP_SOUND);
            // Nếu không có tàu nào chồng lên nhau và danh sách tàu chưa đầy, thêm tàu mới
            fleet.add(new Ship2(coords));
            setSelfData(coords);
            System.out.println("ĐAT TAU THANH CONG.");
        }
    }

    // Kiểm tra xem tàu có nằm ở biên của lưới không
    private boolean isEdge(ArrayList<Coordinate> coords) {
        for (Coordinate coord : coords) {
            int x = coord.getX();
            int y = coord.getY();
            if (x >= 11 || y >= 11) {
                return true;
            }
        }
        return false;
    }

    // Kiểm tra xem tàu có bị chồng chéo không
    private boolean isOverlap(ArrayList<Coordinate> coords) {
        for (Coordinate coord : coords) {
            int x = coord.getX();
            int y = coord.getY();
            if (selfData[x][y] == 1) {
                return true;
            }
        }
        return false;
    }

    // returns number of ships count
    public int shipsCount() {
        int temp = fleet.size();
        return temp;
    }

    // Trả về danh sách các tàu trong flotilla
    public ArrayList<Ship2> getFleet() {
        return fleet;
    }

    // Trả về dữ liệu tự bảo vệ của người chơi
    public int[][] getSelfData() {
        return selfData;
    }

    //Cập nhật dữ liệu tàu vào lưới tự bảo vệ
    public void setSelfData(ArrayList<Coordinate> coords) {
        for (Coordinate coord : coords) {
            int x = coord.getX();
            int y = coord.getY();
            selfData[x][y] = 1;
        }
    }

    //Tấn công tàu
    public void attackShip(Coordinate hitCord) {
        Iterator itr = fleet.iterator();
        while (itr.hasNext()) {
            Ship2 temp = (Ship2) itr.next();
            //temp.Hit(hitCord); // Hit the point in the ship
        }
    }

    //Cập nhật dữ liệu tấn công
    public void setAttackData(int x, int y, String result) {
        if (result.equals("success")) {
            attackData[x][y] = 1;
        }
        else if (result.equals("failure")) {
            attackData[x][y] = 2;
        }
    }

    //Lấy số lượng tàu đã bị đánh chìm
    public int getNumberOfOwnShipSunk() {
        return numberOfShipSunk;
    }

    //Lấy dữ liệu tấn công
    public int[][] getAttackData() {
        return attackData;
    }

    //Lấy dữ liệu từ ô cụ thể
    public int getDataFromCell(int x, int y) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == x && j == y) {
                    return selfData[i][j];
                }
            }
        }
        return -1;
    }

    /*All the boolean methods to check for state*/
    // isHit trả về true nếu có tàu tại thời điểm bị đánh
    public boolean isHit(Coordinate point) {
        for (int i = 0; i < fleet.size(); ) {
            Ship2 temp = fleet.get(i);
            if (temp.isPointHit(point)) {
                return true;
            }
            else {
                i++; // increase the counter if the the ship i didn't contain a point
            }
        }
        return false; // must return false because
    }

    // Kiểm tra người chơi thua
    public boolean isPlayerLost() {
        if (fleet.size() == 0) {
            return true; // Player lost
        }
        else {
            return false;
        }
    }

    // Kiểm tra tàu bị đánh chìm
    public boolean isSunk(Coordinate hitCord) {
        for (int i = 0; i < fleet.size(); ) {
            Ship2 temp = fleet.get(i);
            if (temp.isShipSunk()) {
                numberOfShipSunk++;
                fleet.remove(i);
                return true;
            }
            else {
                i++; // increase the counter if the ship wasn't sunk
            }
        }
        return false;
    }
}
