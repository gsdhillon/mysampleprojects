/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.worklocation;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author nbpatil
 */
public class WLTableModel extends MyTableModel {

    private WorkLocClass[] WLocation;

    public WLTableModel() throws Exception {
        addColumn(new MyTableColumn("Work Location Code", 50, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return WLocation[row].WLocationCode;
            }
        });

        addColumn(new MyTableColumn("Work Location", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return WLocation[row].WLocation;
            }
        });


    }

    @Override
    public void exchange(int row1, int row2) {
        WorkLocClass temp = WLocation[row1];
        WLocation[row1] = WLocation[row2];
        WLocation[row2] = temp;
    }

    public void setData(WorkLocClass[] WLocations) {
        rowCount = 0;
        this.WLocation = WLocations;
        if (WLocations != null) {
            rowCount = WLocations.length;
        }
        fireTableDataChanged();
    }
}
