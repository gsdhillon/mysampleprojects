/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.shift;

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
 * @author pradnya
 */
public class Shift extends MyApplet {

    private ShiftMasterTableModel tableModel;
    private MyTable table;
    private Container contentPane;
    private MyPanel homePanel;

    @Override
    public void init() {
        try {
            super.init();
            contentPane = getContentPane();
            contentPane.setLayout(new GridLayout(1, 1));
            homePanel = createHomePanel();
            contentPane.add(homePanel);
        } catch (Exception e) {
            MyUtils.showException("ShiftMaster", e);
        }
    }

    private MyPanel createHomePanel() throws Exception {
        MyPanel homePanel = new MyPanel(new BorderLayout());
        tableModel = new ShiftMasterTableModel();
        MyPanel p = new MyPanel(new GridLayout(1, 5));

        getShiftMasterList();
        MyButton b2 = new MyButton("Add Shift") {

            @Override
            public void onClick() {
                addShift();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update Shift") {

            @Override
            public void onClick() {
                updateShift();
            }
        };
        p.add(b3);

        MyButton b4 = new MyButton("Delete Shift") {

            @Override
            public void onClick() {
                if (MyUtils.confirm("Do you want to DELETE ?", "DELETE")) {
                    deleteShift();
                } else {
                    return;
                }

            }
        };
        p.add(b4);
        p.add(new MyLabel(MyLabel.TYPE_LABEL, "<html> Search ShiftCode:</html>"));
        MyTextField searchTextName = new MyTextField();
        p.add(searchTextName);
        homePanel.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);

        table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        homePanel.add(table.getGUI(), BorderLayout.CENTER);
        return homePanel;
    }

    private void addShift() {
        //loadPage("services/AddShift.jsp", "_self");
        contentPane.removeAll();
        contentPane.add(new ShiftMaster(this, "add", "0"));
        contentPane.validate();
    }

    private void updateShift() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Shift Selected");
            return;
        }
        String ShiftCode = (String) table.getValueAt(row, 0);
        contentPane.removeAll();
        contentPane.add(new ShiftMaster(this, "update", ShiftCode));
        contentPane.validate();
        contentPane.repaint();
    }

    private void deleteShift() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Shift Selected");
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ShiftMasterFormServlet");
            myHTTP.openOS();
            myHTTP.println("DeleteShift");
            myHTTP.println(id);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("Error")) {
                MyUtils.showMessage(result);
            }
            getShiftMasterList();
        } catch (Exception ex) {
            MyUtils.showException("Delete Shift", ex);
        }
    }

    private void getShiftMasterList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ShiftMasterFormServlet");
            myHTTP.openOS();
            myHTTP.println("getShiftList");
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

            ShiftMasterClass[] Shifts = new ShiftMasterClass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {

                Shifts[i] = new ShiftMasterClass();
                Shifts[i].ShiftCode = d.getString();
                Shifts[i].Type = d.getString();
                Shifts[i].StartTime = d.getString();
                Shifts[i].EndTime = d.getString();
                Shifts[i].WorkHours = d.getString();

            }
            tableModel.setData(Shifts);
        } catch (Exception e) {
            MyUtils.showException("getShiftList", e);
            tableModel.setData(null);
        }
    }

    /**
     *
     */
    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        getShiftMasterList();
        contentPane.validate();
        contentPane.repaint();
    }
}