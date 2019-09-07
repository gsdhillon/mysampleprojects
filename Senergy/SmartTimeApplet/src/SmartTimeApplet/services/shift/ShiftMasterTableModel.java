/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.shift;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class ShiftMasterTableModel extends MyTableModel {

    private ShiftMasterClass[] ShiftMaster;

    public ShiftMasterTableModel() throws Exception {
        addColumn(new MyTableColumn("Shift Code", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return ShiftMaster[row].ShiftCode;
            }
        });
        addColumn(new MyTableColumn("Shift Type", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                if ("0".equals(ShiftMaster[row].Type)) {

                    return "NormalShift";

                } else {
                    return "NightShift";
                }
            }
        });
        addColumn(new MyTableColumn("Start Time", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return ShiftMaster[row].StartTime;
            }
        });
        addColumn(new MyTableColumn("End Time", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return ShiftMaster[row].EndTime;
            }
        });
        addColumn(new MyTableColumn("Working hours", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return ShiftMaster[row].WorkHours;
            }
        });
    }

    @Override
    public void exchange(int row1, int row2) {
        ShiftMasterClass temp = ShiftMaster[row1];
        ShiftMaster[row1] = ShiftMaster[row2];
        ShiftMaster[row2] = temp;
    }

    public void setData(ShiftMasterClass[] Shifts) {
        rowCount = 0;
        this.ShiftMaster = Shifts;
        if (Shifts != null) {
            rowCount = Shifts.length;
        }
        fireTableDataChanged();
    }
}
