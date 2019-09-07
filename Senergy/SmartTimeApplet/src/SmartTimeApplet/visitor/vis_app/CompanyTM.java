package SmartTimeApplet.visitor.vis_app;

//import javax.swing.JCheckBox;
//import javax.swing.table.TableColumn;
//import lib.gui.table.MyCheckBoxCellEditor;
//import lib.gui.table.MyCheckBoxCellRenderer;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 * CompanyTM.java
 */
public class CompanyTM extends MyTableModel {
    private Company[] compnyList;
    public CompanyTM() throws Exception {
//        //CheckBox
//        addColumn(new MyTableColumn("", 50, MyTableColumn.TYPE_CHECK_BOX, true) {
//            @Override
//            public JCheckBox getValueAt(int row) {
//                return compnyList[row].checkBox;
//            }
//            @Override
//            public void setCellRenderer(TableColumn column) {
//                column.setCellRenderer(new MyCheckBoxCellRenderer());
//            }
//            @Override
//            public void setCellEditor(TableColumn column) {
//                column.setCellEditor(new MyCheckBoxCellEditor());
//            }
//        });
        //CompanyID
        addColumn(new MyTableColumn("CompanyID", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return compnyList[row].companyID;
            }
        });
        //CompanyName
        addColumn(new MyTableColumn("CompanyName", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return compnyList[row].companyName;
            }
        });
        //Address
        addColumn(new MyTableColumn("Address", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return compnyList[row].address;
            }
        });
        //Type
        addColumn(new MyTableColumn("Type", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return compnyList[row].type;
            }
        });
        //City
        addColumn(new MyTableColumn("City", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return compnyList[row].city;
            }
        });
        //State
        addColumn(new MyTableColumn("State", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public Object getValueAt(int row) {
                return compnyList[row].state;
            }
        });
        //PIN
        addColumn(new MyTableColumn("PIN", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public Object getValueAt(int row) {
                return compnyList[row].pin;
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        //Phone
        addColumn(new MyTableColumn("Phone", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public Object getValueAt(int row) {
                return compnyList[row].phone1+","+compnyList[row].phone2+","+compnyList[row].phone3;
            }
        });


    }
    @Override
    public void exchange(int row1, int row2) {
        Company temp = compnyList[row1];
        compnyList[row1] = compnyList[row2];
        compnyList[row2] = temp;
    }
    /**
     *
     * @param companyList
     */
    public void setData(Company[] companyList) {
        rowCount = 0;
        this.compnyList = companyList;
        if (companyList != null) {
            rowCount = companyList.length;
        }
        fireTableDataChanged();
    }
}
