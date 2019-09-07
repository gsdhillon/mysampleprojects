package SmartTimeApplet.services.shiftroster;

import SmartTimeApplet.services.employee.UserTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import lib.User;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.session.MyApplet;
import lib.session.MyHTTP;
import lib.session.MyUtils;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

public class ShiftRoster extends MyApplet implements ActionListener {

    private UserTableModel tableModel;
    private MyTable table;
    private Container contentPane;
    private MyPanel homePanel;
    MyPanel panSearch;
    JRadioButton rbDiv, rbSec, rbAll;
    JComboBox cmbDiv, cmbSec;
    int usertype;

    @Override
    public void init() {
        try {
            super.init();
            usertype = Integer.parseInt(getParameter("usertype"));
            contentPane = getContentPane();
            homePanel = CreateHomePanel();
            contentPane.add(homePanel);
        } catch (Exception e) {
            MyUtils.showException("Shift Roster", e);
        }
    }

    /**
     *
     */
    private MyPanel CreateHomePanel() throws Exception {
        MyPanel ret = new MyPanel();
        ret.setLayout(new BorderLayout());
        tableModel = new UserTableModel();

        MyPanel main = new MyPanel(new BorderLayout());
        MyPanel p = new MyPanel(new GridLayout(1, 5));
        getEmployeeMasterList();
        MyButton b2 = new MyButton("Add Employee Roster") {

            @Override
            public void onClick() {
                addRoster();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update Employee Roster") {

            @Override
            public void onClick() {
                updateRoster();
            }
        };
        p.add(b3);

        MyButton b4 = new MyButton("Delete Employee Roster") {

            @Override
            public void onClick() {
                if (MyUtils.confirm("Do you want to DELETE ?", "DELETE")) {
                    deleteRoster();
                } else {
                    return;
                }

            }
        };
        p.add(b4);
        p.add(new MyLabel(MyLabel.TYPE_LABEL, "<html>Search Employee : </html>"));
        MyTextField searchTextName = new MyTextField();
        p.add(searchTextName);
        main.add(p, BorderLayout.NORTH);

        if (usertype != 1) {
            panSearch = new MyPanel(new GridLayout(2, 3, 6, 10), "Search By");

            ButtonGroup rbbuttonGroup3 = new ButtonGroup();
            rbDiv = new JRadioButton("Division/Section");
            rbDiv.setBackground(new Color(250, 250, 250));
            rbDiv.addActionListener(this);
            rbDiv.setActionCommand("SearchDivWise");
            rbbuttonGroup3.add(rbDiv);
            panSearch.add(rbDiv);

            MyLabel lblfill = new MyLabel();
            panSearch.add(lblfill);

            rbAll = new JRadioButton("All", true);
            rbAll.setBackground(new Color(250, 250, 250));
            rbAll.addActionListener(this);
            rbAll.setActionCommand("ViewAll");
            rbbuttonGroup3.add(rbAll);
            panSearch.add(rbAll);

            cmbDiv = new JComboBox();
            cmbDiv.addItem("Select");
            cmbDiv.addActionListener(this);
            cmbDiv.setActionCommand("selectDiv");
            FillCombo(cmbDiv, "Division", "");
            panSearch.add(cmbDiv);

            cmbSec = new JComboBox();
            cmbSec.addItem("Select");
            cmbSec.addActionListener(this);
            cmbSec.setActionCommand("selectSection");
            panSearch.add(cmbSec);

            cmbDiv.setEnabled(false);
            cmbSec.setEnabled(false);

            main.add(panSearch, BorderLayout.CENTER);
        }
        ret.add(main, BorderLayout.NORTH);

        table = new MyTable(tableModel);

        table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_EXACT_MATCH);
        ret.add(table.getGUI(), BorderLayout.CENTER);
        return ret;
    }

    private void addRoster() {
        //loadPage("services/addShiftRoster.jsp", "_self");
        int[] row = table.getSelectedRows();
        String[] UserID = new String[row.length];
        for (int i = 0; i < row.length; i++) {
            UserID[i] = (String) table.getValueAt(row[i], 0);
        }
        contentPane.removeAll();
        contentPane.add(new SRAddNewMaster(this, UserID));
        contentPane.validate();
        contentPane.repaint();
    }

    /**
     *
     */
    private void updateRoster() {

        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Employee Selected");
            return;
        }
        String userID = (String) table.getValueAt(row, 0);
        String Name = (String) table.getValueAt(row, 1);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SRFormServlet");
            myHTTP.openOS();
            myHTTP.println("CheckRoster");
            myHTTP.println(userID);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            myHTTP.closeIS();
            if (Result.startsWith("ERROR")) {
                MyUtils.showMessage(Result);
            } else if (Result.equals("NotPresent")) {
                MyUtils.showMessage("Shift Roster is not made for " + Name);
            } else {
                contentPane.removeAll();
                contentPane.add(new SRUpdateMaster(this, userID, Name));
                contentPane.validate();
                contentPane.repaint();
            }
        } catch (Exception ex) {
            MyUtils.showMessage("Add employeeserverlet" + ex);
        }
    }

    private void deleteRoster() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Employeee Selected");
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SRFormServlet");
            myHTTP.openOS();
            myHTTP.println("DeleteRoster");
            myHTTP.println(id);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("Error")) {
                MyUtils.showMessage(result);
            }
            getEmployeeMasterList();
        } catch (Exception ex) {
            MyUtils.showException("Delete Roster", ex);
        }
    }

    private void getEmployeeMasterList() {
        try {
            MyHTTP myHTTP;
            if (usertype != 1) {//adminuser
                myHTTP = MyUtils.createServletConnection("UserMasterServlet");
                myHTTP.openOS();
                myHTTP.println("getAllUserList");
                myHTTP.closeOS();
            } else {//office user
                myHTTP = MyUtils.createServletConnection("SRFormServlet");
                myHTTP.openOS();
                myHTTP.println("getEmployeeList");
                myHTTP.closeOS();
            }
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            //MyUtils.showMessage(response);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            switch (e.getActionCommand()) {
                case "SearchDivWise":
                    cmbDiv.setEnabled(true);
                    cmbSec.setEnabled(true);
                    break;
                case "selectDiv":
                    String div1 = cmbDiv.getSelectedItem().toString();
                    if (!"Select".equals(div1)) {
                        cmbSec.removeAllItems();
                        cmbSec.addItem("Select");
                        FillCombo(cmbSec, "Section", getSplitedString(div1, 0));//getting splitted string
                        getSelectedEmpList();
                    }

                    break;
                case "ViewAll":
                    cmbDiv.setEnabled(false);
                    cmbSec.setEnabled(false);
                    getEmployeeMasterList();
                    break;
                case "selectSection":
                    getSelectedEmpList();
                    break;
            }
        } catch (Exception ex) {
            MyUtils.showMessage("actionPerformed : " + ex);
        }
    }

    public void FillCombo(JComboBox myComboBox, String str, String divcode) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("UserMasterServlet");
            myHTTP.openOS();
            myHTTP.println(str);
            myHTTP.println(divcode);
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
            for (int i = 0; i < rowCount; i++) {
                String code_name = d.getString() + "  " + d.getString();
                myComboBox.addItem(code_name);
            }
        } catch (Exception e) {
            MyUtils.showException("FillCombo", e);
            tableModel.setData(null);
        }
    }

    private void getSelectedEmpList() {
        try {
            String div = cmbDiv.getSelectedItem().toString();
            String sec = cmbSec.getSelectedItem().toString();
            String searchDiStr = "", searchSecStr = "";

            if (!"Select".equals(div)) {
                searchDiStr = getSplitedString(div, 0);
            } else {
                tableModel.setData(null);
            }
            if (!"Select".equals(sec)) {
                searchSecStr = getSplitedString(sec, 0);
            }
            Packetizer p = new Packetizer();
            p.addString(searchDiStr);
            p.addString(searchSecStr);
            MyHTTP myHTTP = MyUtils.createServletConnection("UserMasterServlet");
            myHTTP.openOS();
            myHTTP.println("getSelectedEmpList");
            myHTTP.println(p.getPacket());
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

        } catch (Exception ex) {
            tableModel.setData(null);
        }
    }

    public String getSplitedString(String str, int returnPos) {
        String divcodename[] = str.split(" ");
        return divcodename[returnPos];
    }

    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        getEmployeeMasterList();
        contentPane.validate();
        contentPane.repaint();
    }
}
