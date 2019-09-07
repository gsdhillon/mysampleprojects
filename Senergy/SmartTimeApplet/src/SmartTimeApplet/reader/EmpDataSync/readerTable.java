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
public class readerTable extends MyTableModel {

    private readerClass[] RC;

    /**
     */
    public readerTable() throws Exception {
        super();
        addColumn(new MyTableColumn("", 40, MyTableColumn.TYPE_CHECK_BOX) {

            @Override
            public JCheckBox getValueAt(int row) {
                return RC[row].checkBox;
            }
        });

        addColumn(new MyTableColumn("ReaderNo", 60, MyTableColumn.TYPE_INT) {

            @Override
            public String getValueAt(int row) {
                return RC[row].readerNo;
            }
        });
        addColumn(new MyTableColumn("Location", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return RC[row].readerLoc;
            }
        });
    }

    @Override
    public void exchange(int row1, int row2) {
        readerClass temp = RC[row1];
        RC[row1] = RC[row2];
        RC[row2] = temp;
    }

    /**
     */
    public void setData(readerClass[] reader) {
        if (reader == null) {
            rowCount = 0;
            this.RC = null;
        } else {
            rowCount = reader.length;
            this.RC = reader;
        }
        fireTableDataChanged();
    }

    public boolean isRowChecked(int row) {
        return RC[row].checkBox.isSelected();
    }
    public void setSelectedRow(int row){
        RC[row].checkBox.setSelected(true);
    }
}
