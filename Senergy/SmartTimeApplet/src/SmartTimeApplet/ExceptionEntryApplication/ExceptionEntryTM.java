/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.ExceptionEntryApplication;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class ExceptionEntryTM extends MyTableModel {

    ExceptionEntryClass[] exc;

    public ExceptionEntryTM() throws Exception {
        addColumn(new MyTableColumn("Exc Entry ID", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return exc[row].appid;
            }
        });
        addColumn(new MyTableColumn("Application date", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return exc[row].appdate;
            }
        });
        addColumn(new MyTableColumn("Pre-In", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return exc[row].prein;
            }
        });
        addColumn(new MyTableColumn("Applied In", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return exc[row].currin;
            }
        });
        addColumn(new MyTableColumn("Pre-out", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return exc[row].preout;
            }
        });
        addColumn(new MyTableColumn("Applied Out", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return exc[row].currout;
            }
        });
        addColumn(new MyTableColumn("Pre-Status", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return exc[row].presatus;
            }
        });
        addColumn(new MyTableColumn("Applied Status", 60, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return exc[row].currstatus;
            }
        });
    }

    @Override
    public void exchange(int row1, int row2) {
        ExceptionEntryClass temp = exc[row1];
        exc[row1] = exc[row2];
        exc[row2] = temp;
    }

    public void setData(ExceptionEntryClass[] exc) {
        rowCount = 0;
        this.exc = exc;
        if (exc != null) {
            rowCount = exc.length;
        }
        fireTableDataChanged();
    }
}
