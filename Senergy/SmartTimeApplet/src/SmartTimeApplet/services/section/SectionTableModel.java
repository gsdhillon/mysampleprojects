package SmartTimeApplet.services.section;


import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 * @author GAURAV
 */
public class SectionTableModel extends MyTableModel{
    private  SectionClass[] Section;
    /**
     */
    public SectionTableModel() throws Exception{
        addColumn(new MyTableColumn("Division Name", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return Section[row].DivisionName ;
            }
        });
        addColumn(new MyTableColumn("Section ID", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return Section[row].SectionID ;
            }
        });
        addColumn(new MyTableColumn("Section Name", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return Section[row].SectionName ;
            }
        });
        addColumn(new MyTableColumn("Sec Head Name", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return Section[row].SectionHeadName ;
            }
        });
        addColumn(new MyTableColumn("Sec Head ID", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return Section[row].SectionHeadID;
            }
        });
    }
    @Override
    public void exchange(int row1, int row2) {
        SectionClass temp = Section[row1];
        Section[row1] = Section[row2];
        Section[row2] = temp;
    }
    /**
     */
    public void setData(SectionClass[] Sections) {
        rowCount =  0;
        this.Section = Sections;
        if(Sections != null){
            rowCount = Sections.length;
        }
        fireTableDataChanged();
    }
     
}