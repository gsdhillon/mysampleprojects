package SmartTimeApplet.services.division;


import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 * @author GAURAV
 */
public class DivisionTableModel extends MyTableModel{
    private  DivisionClass[] Division;
    /**
     */
    public DivisionTableModel() throws Exception{
        addColumn(new MyTableColumn("DivisionID", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return Division[row].DivisionID ;
            }
        });
        addColumn(new MyTableColumn("DivisionName", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return Division[row].DivisionName ;
            }
        });
        
        addColumn(new MyTableColumn("DivisionHeadName", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return Division[row].DivisionHeadName ;
            }
        });
        addColumn(new MyTableColumn("DivisionHeadId", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return Division[row].DivisionHeadID;
            }
        });
    }
    @Override
    public void exchange(int row1, int row2) {
        DivisionClass temp = Division[row1];
        Division[row1] = Division[row2];
        Division[row2] = temp;
    }
    /**
     */
    public void setData(DivisionClass[] Divisions) {
        rowCount =  0;
        this.Division = Divisions;
        if(Divisions != null){
            rowCount = Divisions.length;
        }
        fireTableDataChanged();
    }
     
}