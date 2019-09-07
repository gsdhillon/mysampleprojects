/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.Common;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class EmpTableModel extends MyTableModel {

    private EmpClass[] Emp;

    public EmpTableModel() throws Exception {

        addColumn(new MyTableColumn("Emp No", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return Emp[row].EmpNo;
            }
        });

        addColumn(new MyTableColumn("Emp Name", 300, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return Emp[row].EmpName;
            }
        });
    }

    @Override
    public void exchange(int row1, int row2) {
        EmpClass temp = Emp[row1];
        Emp[row1] = Emp[row2];
        Emp[row2] = temp;
    }

    public void setData(EmpClass[] Employee) {
        rowCount = 0;
        this.Emp = Employee;
        if (Employee != null) {
            rowCount = Employee.length;
        }
        fireTableDataChanged();
    }
}
