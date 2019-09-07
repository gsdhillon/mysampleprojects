package applet.reports;

import applet.services.UserMuster;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;
/**
 */
public class AttendanceReportTM extends MyTableModel{
    private  UserMuster[] userMusters;
    /**
     */
    public AttendanceReportTM() throws Exception{
        addColumn(new MyTableColumn("UserID", 100, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return userMusters[row].user.userID ;
            }
        });
        addColumn(new MyTableColumn("Name", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return userMusters[row].user.name ;
            }
        });
        addColumn(new MyTableColumn("Desig", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return userMusters[row].user.desig ;
            }
        });
        addColumn(new MyTableColumn("Late", 50, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return String.valueOf(userMusters[row].user.numLate);
            }
        });
        addColumn(new MyTableColumn("Early", 50, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return String.valueOf(userMusters[row].user.numEarly);
            }
        });
        addColumn(new MyTableColumn("Leaves", 50, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return String.valueOf(userMusters[row].user.numLeave);
            }
        });
        addColumn(new MyTableColumn("Absents", 50, MyTableColumn.TYPE_INT) {
            @Override
            public String getValueAt(int row) {
                return String.valueOf(userMusters[row].user.numAbsent);
            }
        });
        addColumn(new MyTableColumn("AvgHrsW", 50, MyTableColumn.TYPE_FLOAT) {
            @Override
            public String getValueAt(int row) {
                return String.valueOf(userMusters[row].user.getAvgHrsWrk());
            }
        });
    }
    @Override
    public void exchange(int row1, int row2) {
        UserMuster temp = userMusters[row1];
        userMusters[row1] = userMusters[row2];
        userMusters[row2] = temp;
    }
    /**
     */
    public void setData(UserMuster[] userMusters) {
        rowCount =  0;
        this.userMusters = userMusters;
        if(userMusters != null){
            rowCount = userMusters.length;
        }
        fireTableDataChanged();
    }
}