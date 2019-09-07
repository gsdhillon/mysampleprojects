package TableModels;


import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 * @author GAURAV
 */
public class DHeadTableModel extends MyTableModel{

    
    private  String[] HeadName;
    public MyTableColumn myTableColumn;
    /**
     */
    public DHeadTableModel() throws Exception{
         myTableColumn = new MyTableColumn("Names", 100, MyTableColumn.TYPE_INT) {

                                          @Override
                                          public String getValueAt(int row) {
                                              return HeadName[row] ;
                                          }
                                      };
        addColumn(myTableColumn);
        
    }
    @Override
    public void exchange(int row1, int row2) {
        String temp = HeadName[row1];
        HeadName[row1] = HeadName[row2];
        HeadName[row2] = temp;
    }
    /**
     */
    public void setData(String[] name) {
        rowCount =  0;
        this.HeadName = name;
        if(name != null){
            rowCount = name.length;
        }
        fireTableDataChanged();
    }

   
}
     
