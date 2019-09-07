package SmartTimeApplet.services.holiday;


import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 * @author GAURAV
 */
public class HolidayTableModel extends MyTableModel{
    private  HolidayClass[] Holiday;
    /**
     */
    public HolidayTableModel() throws Exception{
        addColumn(new MyTableColumn("Holiday Date", 300, MyTableColumn.TYPE_DATE_SHORT) {
            @Override
            public String getValueAt(int row) {
                return Holiday[row].HolidayDate.toString() ;
            }
        });
        addColumn(new MyTableColumn("Holiday Name", 600, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return Holiday[row].HolidayName ;
            }
        });
        
        
    }
    @Override
    public void exchange(int row1, int row2) {
        HolidayClass temp = Holiday[row1];
        Holiday[row1] = Holiday[row2];
        Holiday[row2] = temp;
    }
    /**
     */
    public void setData(HolidayClass[] Holidays) {
        rowCount =  0;
        this.Holiday = Holidays;
        if(Holidays != null){
            rowCount = Holidays.length;
        }
        fireTableDataChanged();
    }
     
}