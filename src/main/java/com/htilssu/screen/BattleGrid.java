package com.htilssu.screen;

import javax.swing.*;
import java.awt.*;

public abstract class BattleGrid extends JPanel {
    JPanel self;
    private JPanel temp;

    /* một lưới được tạo ra bằng cách sử dụng JPanel và GridLayout.
    Lưới này có kích thước 10x10, tức là có 100 ô.*/

    public BattleGrid() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        self = new JPanel();
        self.setLayout(new GridLayout(0, 10));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                temp = getCell();
                self.add(temp);
            }
        }
        //Mỗi ô trong lưới được tạo ra bằng cách gọi phương thức getCell();
        this.add(self);
    }

    // Lấy ra thành phần (component) tại một điểm cụ thể trên BattleGrid(Chạy qua tất cả thành phần con của lưới(self)
    // p nằm trong thành phần nào.
    public JPanel getComponentAt(Point p) {
        Component comp = null;
        for (Component child : self.getComponents()) {
            if (child.getBounds().contains(p)) {
                comp = child;
            }
        }
        return (JPanel) comp;
    }

    protected abstract JPanel getCell();
    /*Phương thức getCell chịu trách nhiệm trả về một JPanel
        đại diện cho một ô (cell) trong lưới. Phương thức này được thiết kế
        để được triển khai trong các lớp con của BattleGrid, cho phép mỗi ô trong lưới
        có thể có giao diện và hành vi khác nhau.*/
}
