/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.worklocation;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author pradnya
 */
public class WorkLocation extends MyApplet implements ActionListener {

    private WorkLocMaster WL;

    @Override
    public void init() {
        try {
            super.init();
            setLayout(new BorderLayout());
            WL = new WorkLocMaster();
            selectionEvents(WL);
            WL.btnAdd.setEnabled(false);
            MyPanel panel = new MyPanel(new GridLayout(1, 5));
            getWLocMasterList();
            MyButton b2 = new MyButton("Add WorkLocation") {

                @Override
                public void onClick() {
                    addWLocation();
                }
            };
            panel.add(b2);

            MyButton b3 = new MyButton("Update WorkLocation") {

                @Override
                public void onClick() {
                    WL.txtWorkLocationcode.setEnabled(false);
                    WL.txtWorkLocation.setEnabled(true);
                    WL.btnAdd.setEnabled(true);
                    WL.btnAdd.setText("SAVE");
                    WL.btnAdd.setActionCommand("updateWorkLocation");
                    getSelectedWL();
                }
            };
            panel.add(b3);

            MyButton b4 = new MyButton("Delete WorkLocation") {

                @Override
                public void onClick() {
                    if (MyUtils.confirm("Do you want to DELETE ?", "DELETE")) {
                        deleteWLocation();
                    } else {
                        return;
                    }

                }
            };
            panel.add(b4);
            panel.add(new MyLabel(MyLabel.TYPE_LABEL, "<html>Search WorkLocation:</html>"));
            MyTextField searchTextName = new MyTextField();
            panel.add(searchTextName);
            add(panel, BorderLayout.NORTH);
            //create Documents table
            (WL.table).addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
            add(WL, BorderLayout.CENTER);
        } catch (Exception e) {
            MyUtils.showException("WorkLocation", e);
        }
    }

    private void selectionEvents(WorkLocMaster WL) {

        WL.btnAdd.addActionListener(this);
        WL.btnAdd.setActionCommand("SAVE");
        WL.btnCancel.addActionListener(this);
        WL.btnCancel.setActionCommand("Cancel");
    }

    private void addWLocation() {
        WL.txtWorkLocationcode.setEnabled(true);
        WL.txtWorkLocation.setEnabled(true);
        WL.btnAdd.setEnabled(true);
        WL.btnAdd.setText("SAVE");
        WL.btnAdd.setActionCommand("AddWorkLocation");
    }

    private void deleteWLocation() {
        int row = WL.table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No WorkLocation Selected");
            return;
        }
        String WLoc = (String) WL.table.getValueAt(row, 0);
        //String Date = sqlDate(id);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("WorkLocationFormServlet");
            myHTTP.openOS();
            myHTTP.println("DeleteWorkLocation");
            myHTTP.println(WLoc);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("Error")) {
                MyUtils.showMessage(result);
            }
            getWLocMasterList();
            clearFields();
        } catch (Exception ex) {
            Logger.getLogger(WorkLocation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getWLocMasterList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("WorkLocationFormServlet");
            myHTTP.openOS();
            myHTTP.println("getWorkLocationList");
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

            WorkLocClass[] WLocations = new WorkLocClass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {

                WLocations[i] = new WorkLocClass();
                WLocations[i].WLocationCode = d.getString();
                WLocations[i].WLocation = d.getString();
            }
            WL.tableModel.setData(WLocations);
        } catch (Exception e) {
            MyUtils.showException("GetWorkLocationList", e);
            WL.tableModel.setData(null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("AddWorkLocation")) {
            if (FormFilled(WL)) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("WorkLocationFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("AddWorkLoc");
                    myHTTP.println(CreatePacket(WL));
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else if (result.equals("Inserted")) {
                        WL.txtWorkLocationcode.setEnabled(false);
                        WL.txtWorkLocation.setEnabled(false);
                        WL.btnAdd.setEnabled(false);
                        getWLocMasterList();
                        clearFields();
                    }
                } catch (Exception ex) {

                    Logger.getLogger(WorkLocation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (e.getActionCommand().equals("updateWorkLocation")) {
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("WorkLocationFormServlet");
                myHTTP.openOS();
                myHTTP.println("updateWorkLoc");
                myHTTP.println(CreatePacket(WL));
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                if (result.startsWith("ERROR")) {
                    MyUtils.showMessage(result);
                } else if (result.equals("updated")) {
                    WL.txtWorkLocation.setEnabled(false);
                    WL.btnAdd.setEnabled(false);
                    getWLocMasterList();
                    clearFields();
                }
            } catch (Exception ex) {
                MyUtils.showException("updateWLocation", ex);
            }
        } else {
            MyUtils.showMessage(e.getActionCommand());
        }
    }

    public boolean FormFilled(WorkLocMaster WL) {
        if (WL.txtWorkLocation.getText().equals("")) {
            MyUtils.showMessage("Form Not Filled");
            return false;
        }
        return true;
    }

    public String CreatePacket(WorkLocMaster WL) throws Exception {
        Packetizer a = new Packetizer();
        a.addString(WL.txtWorkLocationcode.getText());
        a.addString(WL.txtWorkLocation.getText());
        return a.getPacket();
    }

    public void clearFields() {
        WL.txtWorkLocationcode.setText("");
        WL.txtWorkLocation.setText("");
    }

    private void getSelectedWL() {
        boolean issel = false;
        for (int i = 0; i < WL.table.getRowCount(); i++) {
            if (WL.table.isRowSelected(i)) {
                WL.txtWorkLocationcode.setText((WL.table.getValueAt(i, 0)).toString());
//                WL.txtWorkLocationcode.repaint();
//                WL.txtWorkLocationcode.validate();
                WL.txtWorkLocation.setText((WL.table.getValueAt(i, 1)).toString());
//                WL.txtWorkLocation.repaint();
//                WL.txtWorkLocation.validate();
                issel = true;
                return;
            }
        }
        if (issel == false) {
            MyUtils.showMessage("No Work Location Selected.");
        }
    }
}
