/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.EmpDataSync;

import javax.swing.JCheckBox;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class EmpTableModel extends MyTableModel {

    EmpClass[] emp;

    public EmpTableModel() throws Exception {
        super();
        addColumn(new MyTableColumn("", 40, MyTableColumn.TYPE_CHECK_BOX) {

            @Override
            public JCheckBox getValueAt(int row) {
                return emp[row].checkBox;
            }
        });

        addColumn(new MyTableColumn("UID", 60, MyTableColumn.TYPE_INT) {

            @Override
            public String getValueAt(int row) {
                return emp[row].EmpUID;
            }
        });
        addColumn(new MyTableColumn("Name", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return emp[row].EmpName;
            }
        });
        addColumn(new MyTableColumn("EmpCode", 60, MyTableColumn.TYPE_INT) {

            @Override
            public String getValueAt(int row) {
                return emp[row].EmpCode;
            }
        });

    }

    @Override
    public void exchange(int row1, int row2) {
        EmpClass temp = emp[row1];
        emp[row1] = emp[row2];
        emp[row2] = temp;
    }

    public void setData(EmpClass[] emp) {
        rowCount = 0;
        this.emp = emp;
        if (emp != null) {
            rowCount = emp.length;
        }
        fireTableDataChanged();
    }

    public boolean isRowChecked(int row) {
        return emp[row].checkBox.isSelected();
    }

    public void setSelectedRow(int row) {
        emp[row].checkBox.setSelected(true);
    }
}
