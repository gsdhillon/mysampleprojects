/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.leaveconfig;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;

/**
 *
 * @author GAURAV
 */
public class LeaveConfig extends MyApplet {

    private LConfigTableModel tableModel;
    private MyTable table;
    private MyPanel homePanel;
    private Container contentPane;

    @Override
    public void init() {
        try {
            super.init();
            contentPane = getContentPane();
            homePanel = CreateHomePanel();
            contentPane.add(homePanel);
        } catch (Exception e) {
            MyUtils.showException("LeaveConfig", e);
        }
    }

    /**
     *
     */
    private MyPanel CreateHomePanel() throws Exception {
        MyPanel ret = new MyPanel();
        ret.setLayout(new BorderLayout());
        tableModel = new LConfigTableModel();
        MyPanel p = new MyPanel(new GridLayout(1, 5));
        getLConfigMasterList();
        MyButton b2 = new MyButton("Add Leave Configuration") {

            @Override
            public void onClick() {
                AddLConfig();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update Leave Configuration") {

            @Override
            public void onClick() {
                UpdateLConfig();
            }
        };
        p.add(b3);

        MyButton b4 = new MyButton("Delete LeaveCofiguration") {

            @Override
            public void onClick() {
                if (MyUtils.confirm("Do you want to DELETE ?", "DELETE")) {
                    DeleteLConfig();
                } else {
                    return;
                }

            }
        };
        p.add(b4);
        p.add(new MyLabel(MyLabel.TYPE_LABEL, "<html>Search Leave Name:</html>"));
        MyTextField searchTextName = new MyTextField();
        p.add(searchTextName);
        ret.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);

        table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_EXACT_MATCH);
        table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        ret.add(table.getGUI(), BorderLayout.CENTER);
        return ret;
    }

    private void AddLConfig() {
        contentPane.removeAll();
        contentPane.add(new LConfigMaster(this, "add", "0"));
        contentPane.validate();
        contentPane.repaint();
    }

    private void UpdateLConfig() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Employee Selected");
            return;
        }
        String LeaveCode = (String) table.getValueAt(row, 0);
        contentPane.removeAll();
        contentPane.add(new LConfigMaster(this, "update", LeaveCode));
        contentPane.validate();
        contentPane.repaint();
    }

    private void DeleteLConfig() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No row Selected");
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        try {
            MyUtils.showMessage(id);
            MyHTTP myHTTP = MyUtils.createServletConnection("LConfigFormServlet");
            myHTTP.openOS();
            myHTTP.println("DeleteLConfig");
            myHTTP.println(id);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("Error")) {
                MyUtils.showMessage(result);
            }
            getLConfigMasterList();
        } catch (Exception ex) {
            MyUtils.showException("LeaveConfig", ex);
        }
    }

    private void getLConfigMasterList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("LConfigFormServlet");
            myHTTP.openOS();
            myHTTP.println("getLConfigList");
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

            LConfigClass[] LConfigs = new LConfigClass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {

                LConfigs[i] = new LConfigClass();
                LConfigs[i].LeaveCode = d.getString();
                LConfigs[i].LeaveName = d.getString();
                LConfigs[i].LeaveDiscription = d.getString();
                LConfigs[i].EL = d.getString();
                LConfigs[i].CL = d.getString();
                LConfigs[i].ML = d.getString();
                LConfigs[i].SCL = d.getString();
                LConfigs[i].HPL = d.getString();
                LConfigs[i].EOL = d.getString();
                LConfigs[i].TourLeave = d.getString();
                LConfigs[i].CML = d.getString();
                LConfigs[i].HCL = d.getString();
                LConfigs[i].OtherLeave = d.getString();
                LConfigs[i].NoOfDays = d.getString();
                LConfigs[i].NoOfTimesAllowed = d.getString();
                LConfigs[i].NoOfDaysAtTime = d.getString();
                LConfigs[i].AccumulationAllowed = d.getString();
                LConfigs[i].MaxAccumulationAllowed = d.getString();
                LConfigs[i].IsHalfDayType = d.getString();
                LConfigs[i].IsEnCashmentType = d.getString();
                LConfigs[i].IsOffBefore = d.getString();
                LConfigs[i].IsOffAfter = d.getString();
                LConfigs[i].IsOffTrapped = d.getString();
                LConfigs[i].IsHolidayTrapped = d.getString();
                LConfigs[i].IsNegativeBalanceAllowed = d.getString();
                LConfigs[i].IsAccountable = d.getString();
                LConfigs[i].Type = d.getString();
                LConfigs[i].OLRemark = d.getString();
            }
            tableModel.setData(LConfigs);
        } catch (Exception e) {
            MyUtils.showMessage("Error: " + e);
            MyUtils.showException("GetLConfigList", e);
            tableModel.setData(null);
        }
    }

    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        getLConfigMasterList();
        contentPane.validate();
        contentPane.repaint();
    }
}
