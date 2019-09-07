package SmartTimeApplet.services.employee;

import lib.User;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 */
public class UserTableModel extends MyTableModel {

    private User[] users;

    /**
     */
    public UserTableModel() throws Exception {
        addColumn(new MyTableColumn("EmpID", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return users[row].userID;
            }
        });
        addColumn(new MyTableColumn("Name", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return users[row].name;
            }
        });
        addColumn(new MyTableColumn("Desig", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return users[row].desig;
            }
        });
        addColumn(new MyTableColumn("Division", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return users[row].division;
            }
        });
        addColumn(new MyTableColumn("Section", 100, MyTableColumn.TYPE_DATE_SHORT) {

            @Override
            public String getValueAt(int row) {
                return users[row].section.toString();
            }
        });
    }

    @Override
    public void exchange(int row1, int row2) {
        User temp = users[row1];
        users[row1] = users[row2];
        users[row2] = temp;
    }

    /**
     */
    public void setData(User[] users) {
        rowCount = 0;
        this.users = users;
        if (users != null) {
            rowCount = users.length;
        }
        fireTableDataChanged();
    }
}