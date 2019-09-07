package gui;
import javax.swing.table.AbstractTableModel;

/**
 * @type     : Java Class
 * @name     : MyTableModel
 * @file     : MyTableModel.java
 * @created  : Aug 24, 2010 3:55:08 PM
 * @version  : 1.0.0
 */
public abstract class MyTableModel extends AbstractTableModel{
    public MyTableModel(){
    }
    public abstract int getColumnWidth(int col);

    public void sortTableRows(int col) {
        
    }
}