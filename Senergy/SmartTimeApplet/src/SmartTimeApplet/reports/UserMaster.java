package SmartTimeApplet.reports;

import SmartTimeApplet.EmpEntryReport.EmpDailyEntryReport;
import SmartTimeApplet.services.employee.UserTableModel;
import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.User;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;

/**
 * UserMaster.java
 */
public class UserMaster extends MyApplet {

    private UserTableModel tableModel;
    private MyPanel homePanel;
    public Container con;
    MyTable table;

    @Override
    public void init() {
        try {
            super.init();
            con = getContentPane();
            LoadPanel(createPanel());
        } catch (Exception e) {
            MyUtils.showException("UserMaster", e);
        }
    }

    public void LoadPanel(MyPanel LoadPanel) throws Exception {
        con = getContentPane();
        homePanel = CreateHomePanel(LoadPanel);
        con.add(homePanel);
    }

    public MyPanel CreateHomePanel(MyPanel LoadPanel) throws Exception {
        MyPanel AR = new MyPanel();
        AR.setLayout(new BorderLayout());
        AR.add(LoadPanel, BorderLayout.CENTER);
        return AR;
    }

    public void showHomePanel() {
        try {
            LoadPanel(createPanel());
        } catch (Exception ex) {
            Logger.getLogger(UserMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public MyPanel createPanel() {
        MyPanel panMain = new MyPanel(new BorderLayout());
        try {
            tableModel = new UserTableModel();
            MyPanel p = new MyPanel(new GridLayout(1, 3));
            MyButton b = new MyButton("Get Employee List") {

                @Override
                public void onClick() {
                    getUserMasterList();
                }
            };
            b.doClick();
            p.add(b);

            MyButton b1 = new MyButton("Employee Entry Report") {

                @Override
                public void onClick() {
                    showEmployeeEntryReport();
                }
            };
            p.add(b1);
            p.add(new MyLabel(MyLabel.TYPE_LABEL, " Search Name:"));
            MyTextField searchTextName = new MyTextField();
            p.add(searchTextName);
            panMain.add(p, BorderLayout.NORTH);
            //create Documents table
            table = new MyTable(tableModel);

            table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_EXACT_MATCH);
            table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
            panMain.add(table.getGUI(), BorderLayout.CENTER);

        } catch (Exception ex) {
            MyUtils.showMessage("createPanel : " + ex);
        }
        return panMain;
    }

    private void getUserMasterList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("UserMasterServlet");
            myHTTP.openOS();
            myHTTP.println("getUserList");
            myHTTP.closeOS();

            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            Depacketizer d = new Depacketizer(response);
            int rowCount = d.getInt();
            User[] users = new User[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {
                users[i] = new User();
                users[i].userID = d.getString();
                users[i].name = d.getString();
                users[i].desig = d.getString();
                users[i].division = d.getString();
                users[i].section = d.getString();
            }
            tableModel.setData(users);
        } catch (Exception e) {
            MyUtils.showException("GetUserList", e);
            tableModel.setData(null);
        }
    }

    public void showEmployeeEntryReport() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Employee Selected");
            return;
        }
        String EmpCode = (String) table.getValueAt(row, 0);
        con.removeAll();
        EmpDailyEntryReport ee = new EmpDailyEntryReport();
        con.add(ee.CreatePanel("Team", EmpCode));
        con.validate();
        con.repaint();
    }
}
