package com.htilssu.utils;

/*
* Đối tượng Coordinate chứa hai giá trị nguyên x và y, tương ứng với các tọa độ trên lưới.*/
public class Coordinate {
    int x;
    int y;
    //Toa do(x,y)
    public Coordinate(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    //Phương thức này so sánh tọa độ hiện tại với một đối tượng Coordinate khác.
    public boolean compareCoord(Coordinate coordinate){
        if(coordinate.getX() == this.x && coordinate.getY() == this.y){
            return true;
        }
        return false;
    }
    //De tim bug
    public String toString(){
        return "\nX=" + x + " and Y=" + y;
    }

}
