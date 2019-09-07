package SmartTimeApplet.services.category;


import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 * @author GAURAV
 */
public class CategoryTableModel extends MyTableModel{
    private  CategoryClass[] category;
    /**
     * 
     */
    
    public CategoryTableModel() throws Exception{
        addColumn(new MyTableColumn("Category Code", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return category[row].CatCode ;
            }
        });
        addColumn(new MyTableColumn("Category Name", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return category[row].CatName ;
            }
        });
        addColumn(new MyTableColumn("Over Time", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return category[row].OverTime ;
            }
        });
        addColumn(new MyTableColumn("Over Time Limit", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return category[row].OverTimeLimit ;
            }
        });
        addColumn(new MyTableColumn("Comp Off", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return category[row].CompOff;
            }
        });
        addColumn(new MyTableColumn("Comp Off limit", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return category[row].CompOffLimit;
            }
        });
        addColumn(new MyTableColumn("Grace Late Time", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return category[row].GraceLateTime;
            }
        });
        
        addColumn(new MyTableColumn("Grace Early Time", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return category[row].GraceEarlyTime;
            }
        });
        
    }
    @Override
    public void exchange(int row1, int row2) {
        CategoryClass temp = category[row1];
        category[row1] = category[row2];
        category[row2] = temp;
    }
    /**
     */
    public void setData(CategoryClass[] Categorys) {
        rowCount =  0;
        this.category = Categorys;
        if(Categorys != null){
            rowCount = Categorys.length;
        }
        fireTableDataChanged();
    }
     
}