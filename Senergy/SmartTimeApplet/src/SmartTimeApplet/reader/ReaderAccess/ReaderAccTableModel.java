/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.ReaderAccess;

import javax.swing.JCheckBox;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class ReaderAccTableModel extends MyTableModel {

    private Assignreaderclass[] AS;

    /**
     */
    public ReaderAccTableModel() throws Exception {
        super();
        addColumn(new MyTableColumn("", 50, MyTableColumn.TYPE_CHECK_BOX) {

            @Override
            public JCheckBox getValueAt(int row) {
                return AS[row].checkBox;
            }
        });
        addColumn(new MyTableColumn("ReaderNo", 100, MyTableColumn.TYPE_INT) {

            @Override
            public String getValueAt(int row) {
                return AS[row].readerno;
            }
        });
        addColumn(new MyTableColumn("Location", 180, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return AS[row].readerloc;
            }
        });
        addColumn(new MyTableColumn("Reader IP", 180, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return AS[row].readerIP;
            }
        });
    }

    @Override
    public void exchange(int row1, int row2) {
        Assignreaderclass temp = AS[row1];
        AS[row1] = AS[row2];
        AS[row2] = temp;
    }

    public void setData(Assignreaderclass[] reader) {
        rowCount = 0;
        this.AS = reader;
        if (reader != null) {
            rowCount = reader.length;
        }
        fireTableDataChanged();
    }

    public boolean isRowChecked(int row) {
        return AS[row].checkBox.isSelected();
    }

    public void setSelectedRow(int row) {
        AS[row].checkBox.setSelected(true);
    }

    public void setUnSelectedRow(int row) {
        AS[row].checkBox.setSelected(false);
    }

    @Override
    public void sortTableRows(int col) {
    }
}
