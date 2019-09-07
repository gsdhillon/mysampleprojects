/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.COFFApplication;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class COFFTableModel extends MyTableModel {

    CoffClass[] coff;

    public COFFTableModel() throws Exception {
        addColumn(new MyTableColumn("Compoff ID", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return coff[row].coffentryid;
            }
        });
        addColumn(new MyTableColumn("Application date", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return coff[row].coffentryid;
            }
        });
        addColumn(new MyTableColumn("CompOff Date", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return coff[row].coffdate;
            }
        });
        addColumn(new MyTableColumn("Worked Date", 150, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return coff[row].workeddate;
            }
        });
        addColumn(new MyTableColumn("Purpose", 150, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return coff[row].purpose;
            }
        });

    }

    @Override
    public void exchange(int row1, int row2) {
        CoffClass temp = coff[row1];
        coff[row1] = coff[row2];
        coff[row2] = temp;
    }

    public void setData(CoffClass[] coff) {
        rowCount = 0;
        this.coff = coff;
        if (coff != null) {
            rowCount = coff.length;
        }
        fireTableDataChanged();
    }
}
