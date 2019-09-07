/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.LeaveApplication;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class LeaveApplicationTM extends MyTableModel {

    LeaveApplicationClass[] Leave;

    public LeaveApplicationTM() throws Exception {
        addColumn(new MyTableColumn("Application ID", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return Leave[row].appID;
            }
        });
        addColumn(new MyTableColumn("Leave type", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return Leave[row].leavetype;
            }
        });
        addColumn(new MyTableColumn("From Date", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return Leave[row].fromdate;
            }
        });
        addColumn(new MyTableColumn("To Date", 150, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return Leave[row].todate;
            }
        });
        addColumn(new MyTableColumn("Reason", 150, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return Leave[row].reason;
            }
        });
        addColumn(new MyTableColumn("Status", 150, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return Leave[row].status;
            }
        });

    }

    @Override
    public void exchange(int row1, int row2) {
        LeaveApplicationClass temp = Leave[row1];
        Leave[row1] = Leave[row2];
        Leave[row2] = temp;
    }

    public void setData(LeaveApplicationClass[] Leave) {
        rowCount = 0;
        this.Leave = Leave;
        if (Leave != null) {
            rowCount = Leave.length;
        }
        fireTableDataChanged();
    }
}
