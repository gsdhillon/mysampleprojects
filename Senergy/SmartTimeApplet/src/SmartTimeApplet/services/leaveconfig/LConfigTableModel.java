package SmartTimeApplet.services.leaveconfig;


import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 * @author GAURAV
 */
public class LConfigTableModel extends MyTableModel{
    private  LConfigClass[] LConfig;
    /**
     */
    public LConfigTableModel() throws Exception{
        addColumn(new MyTableColumn("LeaveCode", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].LeaveCode ;
            }
        });
        addColumn(new MyTableColumn("LeaveName", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].LeaveName ;
            }
        });
        
        addColumn(new MyTableColumn("LeaveDiscription", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].LeaveDiscription ;
            }
        });
        addColumn(new MyTableColumn("EL", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].EL;
            }
        });
        addColumn(new MyTableColumn("EL", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].EL;
            }
        });
        addColumn(new MyTableColumn("CL", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].CL;
            }
        });
        addColumn(new MyTableColumn("ML", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].ML;
            }
        });
        addColumn(new MyTableColumn("SCL", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].SCL;
            }
        });
        addColumn(new MyTableColumn("HPL", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].HPL;
            }
        });
        addColumn(new MyTableColumn("EOL", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].EOL;
            }
        });
        addColumn(new MyTableColumn("Tour Leave", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].TourLeave;
            }
        });
        addColumn(new MyTableColumn("CML", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].CML;
            }
        });
        addColumn(new MyTableColumn("HCL", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].HCL;
            }
        });
        addColumn(new MyTableColumn("Other Leave", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].OtherLeave;
            }
        });
        addColumn(new MyTableColumn("No Of Days", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].NoOfDays;
            }
        });
        addColumn(new MyTableColumn("No Of Times Allowed", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].NoOfTimesAllowed;
            }
        });
        addColumn(new MyTableColumn("No Of Days At Time", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].NoOfDaysAtTime;
            }
        });
        addColumn(new MyTableColumn("Accumulation Allowed", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].AccumulationAllowed;
            }
        });
        addColumn(new MyTableColumn("Max AccumulationAllowed", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].MaxAccumulationAllowed;
            }
        });
        addColumn(new MyTableColumn("Is Half Day Type", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].IsHalfDayType;
            }
        });
        addColumn(new MyTableColumn("Is Encashment Type", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].IsEnCashmentType;
            }
        });
        addColumn(new MyTableColumn("Is Off Before", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].IsOffBefore;
            }
        });
        addColumn(new MyTableColumn("Is Off After", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].IsOffAfter;
            }
        });
        addColumn(new MyTableColumn("Is Off Trapped", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].IsOffTrapped;
            }
        });
        addColumn(new MyTableColumn("Is Holiday Trapped", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].IsHolidayTrapped;
            }
        });
        addColumn(new MyTableColumn("Is Negative Balance Allowed", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].IsNegativeBalanceAllowed;
            }
        });
        addColumn(new MyTableColumn("Is Accountable", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].IsAccountable;
            }
        });
        addColumn(new MyTableColumn("Type", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].Type;
            }
        });
        addColumn(new MyTableColumn("Old Remark", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return LConfig[row].OLRemark;
            }
        });
    }
    @Override
    public void exchange(int row1, int row2) {
        LConfigClass temp = LConfig[row1];
        LConfig[row1] = LConfig[row2];
        LConfig[row2] = temp;
    }
    /**
     */
    public void setData(LConfigClass[] LConfig) {
        rowCount =  0;
        this.LConfig = LConfig;
        if(LConfig != null){
            rowCount = LConfig.length;
        }
        fireTableDataChanged();
    }
     
}