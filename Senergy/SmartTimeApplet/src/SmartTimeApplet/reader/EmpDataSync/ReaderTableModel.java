/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.EmpDataSync;

import SmartTimeApplet.reader.ReaderMaster.ReaderMasterClass;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class ReaderTableModel extends MyTableModel {
    private ReaderMasterClass[] RM;

    /**
     */
    public ReaderTableModel() throws Exception {
        addColumn(new MyTableColumn("ReaderNo", 100, MyTableColumn.TYPE_INT) {

            @Override
            public String getValueAt(int row) {
                return RM[row].rdrNo;
            }
        });
        addColumn(new MyTableColumn("Location", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return RM[row].door;
            }
        });
    }

    @Override
    public void exchange(int row1, int row2) {
        ReaderMasterClass temp = RM[row1];
        RM[row1] = RM[row2];
        RM[row2] = temp;
    }

    /**
     */
    public void setData(ReaderMasterClass[] reader) {
        rowCount = 0;
        this.RM = reader;
        if (reader != null) {
            rowCount = reader.length;
        }
        fireTableDataChanged();
    }
}
