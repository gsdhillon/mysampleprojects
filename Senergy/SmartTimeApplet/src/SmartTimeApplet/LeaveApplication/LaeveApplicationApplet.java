/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.LeaveApplication;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import lib.gui.MyButton;
import lib.gui.MyPanel;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;

/**
 *
 * @author pradnya
 */
public class LaeveApplicationApplet extends MyApplet {

    private LeaveApplicationTM tableModel;
    private MyTable table;
    private Container contentPane;
    private MyPanel homePanel;

    @Override
    public void init() {
        try {
            super.init();
            contentPane = getContentPane();
            homePanel = CreateHomePanel();
            contentPane.add(homePanel);
        } catch (Exception e) {
            MyUtils.showException("Laeve Application Applet", e);
        }
    }

    private MyPanel CreateHomePanel() throws Exception {
        MyPanel ret = new MyPanel();
        ret.setLayout(new BorderLayout());
        tableModel = new LeaveApplicationTM();
        MyPanel p = new MyPanel(new GridLayout(1, 5));
        getApplicationDetailsList();
        MyButton b2 = new MyButton("Add") {

            @Override
            public void onClick() {
                addLeaveAppPanel();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update") {

            @Override
            public void onClick() {
                updateRecord();
            }
        };
        p.add(b3);

        MyButton b4 = new MyButton("Cancel") {

            @Override
            public void onClick() {
                CancelRecord();
            }
        };
        p.add(b4);

//        p.add(new MyLabel(MyLabel.TYPE_LABEL, " Search ReaderNo:"));
//        MyTextField searchTextName = new MyTextField();
//        p.add(searchTextName);
        ret.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);

//        table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        ret.add(table.getGUI(), BorderLayout.CENTER);
        return ret;
    }

    private void addLeaveAppPanel() {
        contentPane.removeAll();
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        contentPane.add(new LeaveApplicationPanel(this, "Add", ""));
        contentPane.validate();
        contentPane.repaint();
    }

    private void updateRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Record Selected");
            return;
        }
        String srno = (String) table.getValueAt(row, 0);
        contentPane.removeAll();
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        contentPane.add(new LeaveApplicationPanel(this, "Update", srno));
        contentPane.validate();
        contentPane.repaint();
    }

    private void CancelRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Record Selected");
            return;
        }

        String srno = (String) table.getValueAt(row, 0);
        if (MyUtils.confirm("Do you want to Cancel " + srno, "Cancel")) {
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("OutDoorFormServlet");
                myHTTP.openOS();
                myHTTP.println("CancelODApplication");
                myHTTP.println(srno);
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                myHTTP.closeIS();
                if (result.startsWith("Error")) {
                    MyUtils.showMessage(result);
                }
                getApplicationDetailsList();
            } catch (Exception ex) {
                MyUtils.showException("DeleteRader", ex);
            }
        }
    }

    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(homePanel);
        getApplicationDetailsList();
        contentPane.validate();
        contentPane.repaint();
    }

    private void getApplicationDetailsList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("LeaveApplicationServlet");
            myHTTP.openOS();
            myHTTP.println("getApplicationDetailsList");
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            Depacketizer dp = new Depacketizer(response);
            int rowCount = dp.getInt();
            LeaveApplicationClass[] leave = new LeaveApplicationClass[rowCount];

            for (int i = 0; i < rowCount; i++) {
                leave[i] = new LeaveApplicationClass();
                leave[i].appID = dp.getString();
                leave[i].leavetype = dp.getString();
                leave[i].fromdate = dp.getString();
                leave[i].todate = dp.getString();
                leave[i].reason = dp.getString();
                leave[i].status = dp.getString();
            }
            tableModel.setData(leave);
        } catch (Exception e) {
            MyUtils.showException("getApplicationDetailsList", e);
            tableModel.setData(null);
        }
    }
}
