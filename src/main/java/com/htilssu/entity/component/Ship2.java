package com.htilssu.entity.component;

// Lớp Ship đại diện cho một con TÀU

import java.util.ArrayList;

public class Ship2 {

    private ArrayList<Coordinate> coordinates;
    private boolean[] hits;

    // Constructor chấp nhận một danh sách tọa độ và tự động xác định kích thước tàu
    public Ship2(ArrayList<Coordinate> coords) {
        this.coordinates = new ArrayList<>(coords);
        this.hits = new boolean[coords.size()];
    }

    //
    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    // Phương thức kiểm tra xem một điểm có bị bắn trúng không. Nếu trúng thì gán hit = true.
    public boolean isPointHit(Coordinate hit) {
        for (int i = 0; i < coordinates.size(); i++) {
            if (coordinates.get(i).compareCoord(hit)) {
                hits[i] = true;
                return true;
            }
        }
        return false;
    }

    // Phương thức kiểm tra xem tàu đã bị chìm hay chưa
    public boolean isShipSunk() {
        for (boolean hit : hits) {
            if (!hit) {
                return false;
            }
        }
        return true;
    }

}
