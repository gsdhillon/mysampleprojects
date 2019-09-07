package mytable.samples;

import javax.swing.table.TableColumn;
import mytable.MyColumnModel;
import mytable.MyTableModel;
import mytable.celleditors.MyCheckBoxCellEditor;
import mytable.celleditors.MyComboBoxCellEditor;
import mytable.celleditors.MyTextFieldCellEditor;
import mytable.cellrenderers.MyCheckBoxCellRenderer;
/**
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class SampleTM extends MyTableModel{
    private SampleData[] data;
    public SampleTM() throws Exception{
        //call to super is must
        super(true);
        //
        addColumn(new MyColumnModel("Long", 100, MyColumnModel.TYPE_BIG_NUMBER, true){
            @Override
            public String getValueAt(int row) {
                return data[row].prsn_id;
            }
            @Override
            public void setValueAt(int row, Object value) {
                data[row].prsn_id = (String) value;
            }
            @Override
            public void setCellEditor(TableColumn column){
                column.setCellEditor(new MyTextFieldCellEditor());
            }
        });
        //
        addColumn(new MyColumnModel("String", 200, MyColumnModel.TYPE_STRING, true){
            @Override
            public String getValueAt(int row) {
                return data[row].name;
            }
            @Override
            public void setValueAt(int row, Object value) {
                data[row].name = (String) value;
            }
            @Override
            public void setCellEditor(TableColumn column){
                column.setCellEditor(new MyTextFieldCellEditor());
            }
        });
        //
        addColumn(new MyColumnModel("BigNumber", 300, MyColumnModel.TYPE_BIG_NUMBER){
            @Override
            public String getValueAt(int row) {
                return data[row].prsn_id+data[row].prsn_id+data[row].prsn_id+data[row].prsn_id;
            }
        });
        //
        addColumn(new MyColumnModel("Date", 200, MyColumnModel.TYPE_DATE){
            @Override
            public String getValueAt(int row) {
                return data[row].someDate;
            }
        });
        //
        addColumn(new MyColumnModel("Double", 200, MyColumnModel.TYPE_DOUBLE, true){
            @Override
            public String getValueAt(int row) {
                return data[row].doubleVal;
            }
            @Override
            public void setValueAt(int row, Object value) {
                data[row].doubleVal = (String) value;
            }
            @Override
            public void setCellEditor(TableColumn column){
                column.setCellEditor(new MyTextFieldCellEditor());
            }
        });
        //
        addColumn(new MyColumnModel("Combo", 200, MyColumnModel.TYPE_STRING, true){
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
                String[] shifts = new String[]{"", "G1", "G2", "G3", "R1", "R2", "R3", "OFF"};
                column.setCellEditor(new MyComboBoxCellEditor(shifts));
            }
        });
        //
        addColumn(new MyColumnModel("Check", 60, MyColumnModel.TYPE_CHECK_BOX, true){
            @Override
            public Object getValueAt(int row) {
                return data[row].checked;
            }
            @Override
            public void setCellRenderer(TableColumn column){
                column.setCellRenderer(new MyCheckBoxCellRenderer());
            }
            @Override
            public void setCellEditor(TableColumn column){
                column.setCellEditor(new MyCheckBoxCellEditor());
            }
            @Override
            public void setValueAt(int row, Object value) {
                if(value instanceof Boolean){
                    data[row].checked = (Boolean)value;
                }else{
                    //do not change data
                }
                System.out.println("Checked "+row+" = "+data[row].checked);
            }
        });
    }
//    /**
//     *
//     * @param row1
//     * @param row2
//     */
//    @Override
//    public void exchange(int row1, int row2) {
//        SampleData temp = data[row1];
//        data[row1] = data[row2];
//        data[row2] = temp;
//    }
    @Override
    public void shuffle(int[] index) {
        SampleData[] tempData = new SampleData[rowCount];
        for(int i=0;i<rowCount;i++){
            tempData[i] = data[index[i]];
        }
        System.arraycopy(tempData, 0, data, 0, rowCount);
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
     * @param row
     * @return
     */
    public boolean isRowChecked(int row){
        return data[row].checked;
    }
}