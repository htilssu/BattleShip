package com.htilssu.util;

/*
 * Đối tượng Coordinate chứa hai giá trị nguyên x và y, tương ứng với các tọa độ trên lưới.*/
public class Coordinate {
    int x;
    int y;

    //Toa do(x,y)
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //Phương thức này so sánh tọa độ hiện tại với một đối tượng Coordinate khác.
    public boolean compareCoord(Coordinate coordinate) {
        if (coordinate.getX() == this.x && coordinate.getY() == this.y) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    //De tim bug
    public String toString() {
        return "\nX=" + x + " and Y=" + y;
    }

}
