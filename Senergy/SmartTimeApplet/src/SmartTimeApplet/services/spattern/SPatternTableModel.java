package SmartTimeApplet.services.spattern;


import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 * @author GAURAV
 */
public class SPatternTableModel extends MyTableModel{
    private  SPatternClass[] SPattern;
    /**
     */
    public SPatternTableModel() throws Exception{
        addColumn(new MyTableColumn("Pattern Code", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].patternCode ;
            }
        });
        addColumn(new MyTableColumn("Shift", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].shift[0] ;
            }
        });
        addColumn(new MyTableColumn("Days", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].days[0] ;
            }
        });
        addColumn(new MyTableColumn("Shift", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].shift[1] ;
            }
        });
        addColumn(new MyTableColumn("Days", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].days[1] ;
            }
        });
        addColumn(new MyTableColumn("Shift", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].shift[2] ;
            }
        });
        addColumn(new MyTableColumn("Days", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].days[2] ;
            }
        });
        addColumn(new MyTableColumn("Shift", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].shift[3] ;
            }
        });
        addColumn(new MyTableColumn("Days", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].days[3] ;
            }
        });
        addColumn(new MyTableColumn("Shift", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].shift[4] ;
            }
        });
        addColumn(new MyTableColumn("Days", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].days[4] ;
            }
        });
        addColumn(new MyTableColumn("Shift", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].shift[5] ;
            }
        });
        addColumn(new MyTableColumn("Days", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].days[5] ;
            }
        });
        addColumn(new MyTableColumn("Shift", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].shift[6] ;
            }
        });
        addColumn(new MyTableColumn("Days", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].days[6] ;
            }
        });
        addColumn(new MyTableColumn("Shift", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].shift[7] ;
            }
        });
        addColumn(new MyTableColumn("Days", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].days[7] ;
            }
        });
        addColumn(new MyTableColumn("Shift", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].shift[8] ;
            }
        });
        addColumn(new MyTableColumn("Days", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].days[8] ;
            }
        });
        addColumn(new MyTableColumn("Shift", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].shift[9] ;
            }
        });
        addColumn(new MyTableColumn("Days", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return SPattern[row].days[9] ;
            }
        });
        
    }
    @Override
    public void exchange(int row1, int row2) {
        SPatternClass temp = SPattern[row1];
        SPattern[row1] = SPattern[row2];
        SPattern[row2] = temp;
    }
    /**
     */
    public void setData(SPatternClass[] Holidays) {
        rowCount =  0;
        this.SPattern = Holidays;
        if(Holidays != null){
            rowCount = Holidays.length;
        }
        fireTableDataChanged();
    }
     
}