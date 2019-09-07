package lib.sampleGUI;

import javax.swing.JCheckBox;
import javax.swing.table.TableColumn;
import lib.gui.table.MyCheckBoxCellEditor;
import lib.gui.table.MyCheckBoxCellRenderer;
import lib.gui.table.MyComboBoxCellEditor;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;
import lib.gui.table.MyTextFieldCellEditor;

/**
 */
public class SampleTM extends MyTableModel{
    private SampleData[] data;
    public SampleTM() throws Exception{
        super();
        //TODO MANOJ return checkBox of data object for for 1st column
        //Column 0 
        addColumn(new MyTableColumn("Checked", 20, MyTableColumn.TYPE_CHECK_BOX, true){
            @Override
            public JCheckBox getValueAt(int row) {
                return data[row].checkBox;
            }
            @Override
            public void setCellRenderer(TableColumn column){
                column.setCellRenderer(new MyCheckBoxCellRenderer());
            }
            @Override
            public void setCellEditor(TableColumn column){
                column.setCellEditor(new MyCheckBoxCellEditor());
            }
        });
        ///Column 1
        addColumn(new MyTableColumn("EmpID", 100, MyTableColumn.TYPE_INT){
            @Override
            public String getValueAt(int row) {
                return data[row].prsn_id;
            }
        });
        ///Column 2
        addColumn(new MyTableColumn("Name", 100, MyTableColumn.TYPE_STRING, true){
            @Override
            public String getValueAt(int row) {
                return data[row].name;
            }
            @Override
            public void setCellEditor(TableColumn column){
                column.setCellEditor(new MyTextFieldCellEditor());
            }
            @Override
            public void setValueAt(int row, Object value) {
                data[row].name = (String) value;
            }
        });
        //Column 3
        addColumn(new MyTableColumn("Joining Date", 100, MyTableColumn.TYPE_DATE_FULL){
            @Override
            public String getValueAt(int row) {
                return data[row].someDate;
            }
        });
        //Column 4
        addColumn(new MyTableColumn("Shift", 100, MyTableColumn.TYPE_STRING, true){
            @Override
            public String getValueAt(int row) {
                return data[row].shift;
            }
            @Override
            public void setValueAt(int row, Object value) {
                data[row].shift = (String) value;
            }
            @Override
            public void setCellEditor(TableColumn column){
                String[] shifts = new String[]{"", "R1", "R2", "R3", "OFF"};
                column.setCellEditor(new MyComboBoxCellEditor(shifts));
            }
        });
    }
    /**
     *
     * @param row1
     * @param row2
     */
    @Override
    public void exchange(int row1, int row2) {
        SampleData temp = data[row1];
        data[row1] = data[row2];
        data[row2] = temp;
    }
    /**
     *
     * @param certInfo
     */
    public void setData(SampleData[] data) {
        if(data==null){
            rowCount = 0;
            this.data = null;
        }else{
            rowCount = data.length;
            this.data = data;
        }
        fireTableDataChanged();
    }
    /**
     * MANOJ
     * @param row
     * @return
     */
    public boolean isRowChecked(int row){
        return data[row].checkBox.isSelected();
    }
}