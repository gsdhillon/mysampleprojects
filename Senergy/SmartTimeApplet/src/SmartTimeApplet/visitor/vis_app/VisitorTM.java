package SmartTimeApplet.visitor.vis_app;

import javax.swing.JCheckBox;
import javax.swing.table.TableColumn;
import lib.gui.table.MyCheckBoxCellEditor;
import lib.gui.table.MyCheckBoxCellRenderer;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 * VisitorTM.java
 */
public class VisitorTM extends MyTableModel {
    private Visitor[] visitorList;
    private JCheckBox[] checkBoxList;
    public VisitorTM() throws Exception {
        //chechBox
        addColumn(new MyTableColumn("", 50, MyTableColumn.TYPE_CHECK_BOX, true) {
            @Override
            public JCheckBox getValueAt(int row) {
                return checkBoxList[row];
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
        //VisitorID
        addColumn(new MyTableColumn("VisitorID", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return visitorList[row].pvisID;
            }
        });
        //CompanyID
        addColumn(new MyTableColumn("CompanyID", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return visitorList[row].companyID;
            }
        });
        //VisitorName
        addColumn(new MyTableColumn("VisitorName", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return visitorList[row].name;
            }
        });
        //Occupation
        addColumn(new MyTableColumn("Occupation", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return visitorList[row].desig;
            }
        });
        //PVC
        addColumn(new MyTableColumn("PVC", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return visitorList[row].pvcYesNo;
            }
        });
        //Gender
        addColumn(new MyTableColumn("Gender", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return visitorList[row].sex;
            }
        });

    }
    /**
     *
     * @param visitorList
     */
    public void setData(Visitor[] visitorList) {
        rowCount = 0;
        this.visitorList = visitorList;
        if (visitorList != null) {
            rowCount = visitorList.length;
        }
        checkBoxList = new JCheckBox[rowCount];
        for(int i=0;i<rowCount;i++){
            checkBoxList[i] = new JCheckBox();
        }
        fireTableDataChanged();
    }
    /**
     *
     * @param row1
     * @param row2
     */
    @Override
    public void exchange(int row1, int row2) {
        Visitor temp = visitorList[row1];
        visitorList[row1] = visitorList[row2];
        visitorList[row2] = temp;
        
        JCheckBox temp1 = checkBoxList[row1];
        checkBoxList[row1] = checkBoxList[row2];
        checkBoxList[row2] = temp1;
    }
    /**
     * MANOJ
     * @param row
     * @return
     */
    public boolean isRowChecked(int row){
        return checkBoxList[row].isSelected();
    }

    public Visitor getVisitor(int row) {
        return visitorList[row];
    }
}