/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.UserAccess;

import javax.swing.JCheckBox;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class DivisionTM extends MyTableModel {

    DivisionClass[] div;

    public DivisionTM() throws Exception {
        super();
        addColumn(new MyTableColumn("", 20, MyTableColumn.TYPE_CHECK_BOX) {

            @Override
            public JCheckBox getValueAt(int row) {
                return div[row].checkBox;
            }
        });

        addColumn(new MyTableColumn("Div Code", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return div[row].divcode;
            }
        });
        addColumn(new MyTableColumn("Division", 150, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return div[row].divname;
            }
        });
    }

    @Override
    public void exchange(int row1, int row2) {
        DivisionClass temp = div[row1];
        div[row1] = div[row2];
        div[row2] = temp;
    }

    public void setData(DivisionClass[] div) {
        rowCount = 0;
        this.div = div;
        if (div != null) {
            rowCount = div.length;
        }
        fireTableDataChanged();
    }

    public boolean isRowChecked(int row) {
        return div[row].checkBox.isSelected();
    }

    public void setSelectedRow(int row) {
        div[row].checkBox.setSelected(true);
    }

    public void setUnSelectedRow(int row) {
        div[row].checkBox.setSelected(false);
    }
}
