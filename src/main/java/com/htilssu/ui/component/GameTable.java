package com.htilssu.ui.component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class GameTable extends JTable {
    public GameTable(TableModel dm) {
        super(dm);
    }
}
