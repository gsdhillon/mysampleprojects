package cpnds.data;

import cpnds.MyGraphs.DataPoint;
import gui.MyTableModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @type     : Java Class
 * @name     : DataTableModel
 * @file     : DataTableModel.java
 * @created  : May 27, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class DataTableModel extends MyTableModel{
    private DataPoint[] data;
    private int[] colWidth = {50, 100, 400};
    private String[] colNames = {"S.No.", "X Value", "Y Value"};
    private final NumberFormat f = new DecimalFormat("#000000.000000000000");

    @Override
    public int getColumnWidth(int col) {
        return colWidth[col];
    }

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }
    /**
     * 
     * @return
     */
    public int getRowCount() {
        return data!=null?data.length:0;
    }
    /**
     *
     * @return
     */
    public int getColumnCount() {
        return colNames.length;
    }
    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex==0){
            return data[rowIndex].sno+"";
        }else if(columnIndex==1){
            return data[rowIndex].x+"";
        }else if(columnIndex==2){
            return f.format(data[rowIndex].y);
        }else{
            return "";
        }
    }

    public void setData(DataPoint[] data){
        this.data = data;
        fireTableDataChanged();
    }
}
