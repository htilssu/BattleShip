package com.htilssu.entity;

import com.htilssu.component.Coordinate;

// Lớp Ship đại diện cho một con TÀU
public class Ship {
    // Mảng tọa độ của tàu
    Coordinate[] coordinates;

    // Mảng boolean để kiểm tra xem các tọa độ tương ứng đã bị bắn trúng hay chưa.
    boolean[] hits;

    // Constructor cho tàu với 2 tọa độ
    public Ship(Coordinate a, Coordinate b) {
        this.coordinates = new Coordinate[] {a, b};
        this.hits = new boolean[2]; // Mảng boolean có 2 phần tử
    }

    // Constructor cho tàu với 3 tọa độ
    public Ship(Coordinate a, Coordinate b, Coordinate c) {
        this.coordinates = new Coordinate[] {a, b, c};
        this.hits = new boolean[3]; // Mảng boolean có 3 phần tử
    }

    // Constructor cho tàu với 4 tọa độ
    public Ship(Coordinate a, Coordinate b, Coordinate c, Coordinate d) {
        this.coordinates = new Coordinate[] {a, b, c, d};
        this.hits = new boolean[4]; // Mảng boolean có 4 phần tử
    }

    // Constructor cho tàu với 5 tọa độ
    public Ship(Coordinate a, Coordinate b, Coordinate c, Coordinate d, Coordinate e) {
        this.coordinates = new Coordinate[] {a, b, c, d, e};
        this.hits = new boolean[5]; // Mảng boolean có 5 phần tử
    }

    // Phương thức kiểm tra xem một điểm có bị bắn trúng không. Nếu trúng thì gán hit = true.
    public boolean isPointHit(Coordinate hit) {
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i].compareCoord(hit)) {
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

    // Phương thức dùng để in ra tọa độ của tàu, gỡ lỗi.
    public void printShip() {
        for (Coordinate coordinate : coordinates) {
            System.out.print("(" + coordinate.getX() + ", " + coordinate.getY() + ") ");
        }
        System.out.println();
    }
}
