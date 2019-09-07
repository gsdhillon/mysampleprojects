/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.division;

import SmartTimeApplet.services.employee.Employee;
import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Division extends MyApplet {

    private DivisionTableModel tableModel;
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
            contentPane.validate();
            contentPane.repaint();
        } catch (Exception e) {
            MyUtils.showException("Division Applet", e);
        }
    }

    private MyPanel CreateHomePanel() throws Exception {
        MyPanel ret = new MyPanel();
        ret.setLayout(new BorderLayout());
        tableModel = new DivisionTableModel();
        MyPanel p = new MyPanel(new GridLayout(1, 6));
        getDivisionMasterList();
        MyButton b2 = new MyButton("Add Division") {

            @Override
            public void onClick() {
                addDivision();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update Division") {

            @Override
            public void onClick() {
                updateDivision();
            }
        };
        p.add(b3);

        MyButton b4 = new MyButton("Delete Division") {

            @Override
            public void onClick() {
                if (MyUtils.confirm("Do you want to DELETE ?", "DELETE")) {
                    deleteDivision();
                } else {
                    return;
                }

            }
        };
        p.add(b4);
        p.add(new MyLabel(MyLabel.TYPE_LABEL, "<html>Search Division Name:</html>"));
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

    private void addDivision() {
        contentPane.removeAll();
        contentPane.add(new DivisionMaster(this, "add", "0"));
        contentPane.validate();
        contentPane.repaint();

    }

    private void updateDivision() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Employee Selected");
            return;
        }
        String DivCode = (String) table.getValueAt(row, 0);

        contentPane.removeAll();
        contentPane.add(new DivisionMaster(this, "update", DivCode));
        contentPane.validate();
        contentPane.repaint();

    }

    private void deleteDivision() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Division Selected");
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("DivisionFormServlet");
            myHTTP.openOS();
            myHTTP.println("DeleteDivision");
            myHTTP.println(id);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("Error")) {
                MyUtils.showMessage(result);
            }
            getDivisionMasterList();
        } catch (Exception ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    private void getDivisionMasterList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("DivisionFormServlet");
            myHTTP.openOS();
            myHTTP.println("getDivisionList");
            myHTTP.closeOS();

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

            DivisionClass[] Divisions = new DivisionClass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {

                Divisions[i] = new DivisionClass();
                Divisions[i].DivisionID = d.getString();
                Divisions[i].DivisionName = d.getString();
                Divisions[i].DivisionHeadName = d.getString();
                Divisions[i].DivisionHeadID = d.getString();

            }
            tableModel.setData(Divisions);
        } catch (Exception e) {
            MyUtils.showException("GetDivisionList", e);
            tableModel.setData(null);
        }
    }

    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        getDivisionMasterList();
        contentPane.validate();
        contentPane.repaint();
    }
}
