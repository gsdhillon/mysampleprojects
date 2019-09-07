/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.ODApplication;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class OutDoorTM extends MyTableModel {

    OutDoorClass[] OD;

    public OutDoorTM() throws Exception {
        addColumn(new MyTableColumn("SrNo", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return OD[row].SrNo;
            }
        });
        addColumn(new MyTableColumn("Date", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return OD[row].appdate;
            }
        });
        addColumn(new MyTableColumn("OutDoor date", 150, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return OD[row].odFrmdate;
            }
        });
        addColumn(new MyTableColumn("OutDoor date", 150, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return OD[row].odTodate;
            }
        });
        addColumn(new MyTableColumn("Approve date", 150, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return OD[row].approvedate;
            }
        });
        addColumn(new MyTableColumn("Company", 150, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return OD[row].company;
            }
        });
        addColumn(new MyTableColumn("purpose", 150, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return OD[row].purpose;
            }
        });
        addColumn(new MyTableColumn("Status", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return OD[row].status;
            }
        });
    }

    @Override
    public void exchange(int row1, int row2) {
        OutDoorClass temp = OD[row1];
        OD[row1] = OD[row2];
        OD[row2] = temp;
    }

    public void setData(OutDoorClass[] OD) {
        rowCount = 0;
        this.OD = OD;
        if (OD != null) {
            rowCount = OD.length;
        }
        fireTableDataChanged();
    }
}
