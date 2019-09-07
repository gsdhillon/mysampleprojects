/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.EmpEntryReport;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class EmpEntryTableModel extends MyTableModel {

    private EmpEntryClass[] EmpEntry;

    public EmpEntryTableModel() throws Exception {
        addColumn(new MyTableColumn("Reader Location", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return EmpEntry[row].readerloc;
            }
        });
        addColumn(new MyTableColumn("Reader Type", 100, MyTableColumn.TYPE_INT) {

            @Override
            public String getValueAt(int row) {
                String reader_type = "";
                switch (EmpEntry[row].readertype) {
                    case "0":
                        reader_type = "IN";
                        break;
                    case "1":
                        reader_type = "OUT";
                        break;
                }
                return reader_type;
            }
        });
        addColumn(new MyTableColumn("Time", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return EmpEntry[row].time;
            }
        });
    }

    @Override
    public void exchange(int row1, int row2) {
        EmpEntryClass temp = EmpEntry[row1];
        EmpEntry[row1] = EmpEntry[row2];
        EmpEntry[row2] = temp;
    }

    /**
     */
    public void setData(EmpEntryClass[] EmpEntry) {
        rowCount = 0;
        this.EmpEntry = EmpEntry;
        if (EmpEntry != null) {
            rowCount = EmpEntry.length;
        }
        fireTableDataChanged();
    }
}
