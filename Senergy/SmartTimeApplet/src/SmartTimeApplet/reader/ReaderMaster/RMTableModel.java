/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.ReaderMaster;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author Pradnya
 */
public class RMTableModel extends MyTableModel {

    private ReaderMasterClass[] RM;

    /**
     */
    public RMTableModel() throws Exception {
        addColumn(new MyTableColumn("ReaderNo", 100, MyTableColumn.TYPE_INT) {

            @Override
            public String getValueAt(int row) {
                return RM[row].rdrNo;
            }
        });
        addColumn(new MyTableColumn("Location", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return RM[row].door;
            }
        });
        addColumn(new MyTableColumn("Division", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return RM[row].div;
            }
        });
        addColumn(new MyTableColumn("ReaderType", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                String readerType = "";
                switch (RM[row].readerType) {
                    case "0":
                        readerType = "IN";
                        break;
                    case "1":
                        readerType = "OUT";
                        break;
                    case "2":
                        readerType = "IN/OUT";
                        break;
                }
                return readerType;
            }
        });
        addColumn(new MyTableColumn("SelfIP", 100, MyTableColumn.TYPE_INT) {

            @Override
            public String getValueAt(int row) {
                return RM[row].selfIP;
            }
        });

        addColumn(new MyTableColumn("ServerIP", 100, MyTableColumn.TYPE_INT) {

            @Override
            public String getValueAt(int row) {
                return RM[row].serverIP;
            }
        });
        addColumn(new MyTableColumn("Zone", 100, MyTableColumn.TYPE_INT) {

            @Override
            public String getValueAt(int row) {
                return RM[row].readerZone;
            }
        });
    }

    @Override
    public void exchange(int row1, int row2) {
        ReaderMasterClass temp = RM[row1];
        RM[row1] = RM[row2];
        RM[row2] = temp;
    }

    /**
     */
    public void setData(ReaderMasterClass[] reader) {
        rowCount = 0;
        this.RM = reader;
        if (reader != null) {
            rowCount = reader.length;
        }
        fireTableDataChanged();
    }
}
