/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.UserAccess;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import lib.Utility.SimpleUtilities;
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
public class AddUserApplet extends MyApplet implements ActionListener {

    Container contentsPane;
    public DivisionTM tablemodel;
    MyPanel pantxt;
    JComboBox cmbUser;
    JRadioButton rdOffice, rdAdmin;
    MyTextField txtname;
    MyButton btnAdd, btnEdit, btnCancel, btnDelete;
    JPasswordField txtPwd;
    JTextArea taSelDiv;
    MyTable tblDiv;

    @Override
    public void init() {
        try {
            super.init();
            setLayout(new BorderLayout());
            MyPanel formPanel = AddUserApplet();
            add(formPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            MyUtils.showMessage("AddUserApplet : " + ex);
        }
    }

    public MyPanel AddUserApplet() throws Exception {
        MyPanel MainPanel = new MyPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));
        MyPanel panuseracc = new MyPanel(new BorderLayout());
        panuseracc.setBorder(BorderFactory.createTitledBorder("Office / Admin Access"));
        panuseracc.setPreferredSize(new Dimension(700, 400));

        MyPanel panel = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        MyPanel panSelOffAdm = new MyPanel(new GridLayout(1, 2));

        ButtonGroup bg = new ButtonGroup();
        rdOffice = new JRadioButton("Office", true);
        rdOffice.addActionListener(this);
        rdOffice.setActionCommand("Office");
        bg.add(rdOffice);
        panSelOffAdm.add(rdOffice);

        rdAdmin = new JRadioButton("Admin");
        rdAdmin.addActionListener(this);
        rdAdmin.setActionCommand("Admin");
        bg.add(rdAdmin);
        panSelOffAdm.add(rdAdmin);
        panel.add(panSelOffAdm);
        panuseracc.add(panel, BorderLayout.NORTH);

        MyPanel panCenter = new MyPanel(new BorderLayout());
        MyPanel panDetails = new MyPanel(new BorderLayout());
        MyPanel panlbl = new MyPanel(new GridLayout(2, 1));
        MyLabel lblName = new MyLabel(1, "User Name:");
        panlbl.add(lblName);

        MyLabel lblPassword = new MyLabel(1, "Password:");
        panlbl.add(lblPassword);

        panDetails.add(panlbl, BorderLayout.WEST);

        pantxt = new MyPanel(new GridLayout(2, 1));

        cmbUser = new JComboBox();
        cmbUser.addActionListener(this);
        cmbUser.setActionCommand("SelectUser");
        pantxt.add(cmbUser);

        txtPwd = new JPasswordField();
        pantxt.add(txtPwd);
        panDetails.add(pantxt, BorderLayout.CENTER);
        panCenter.add(panDetails, BorderLayout.NORTH);

        MyPanel panTable = new MyPanel(new BorderLayout());
        tablemodel = new DivisionTM();
        getDivList();
        tblDiv = new MyTable(tablemodel);
        tblDiv.setEnabled(false);
        panTable.add(tblDiv.getGUI());

        panCenter.add(panTable, BorderLayout.CENTER);

        MyPanel panSelDiv = new MyPanel(new BorderLayout());
        taSelDiv = new JTextArea();
        taSelDiv.setBorder(BorderFactory.createLineBorder(Color.gray));
        taSelDiv.setEnabled(false);
        panSelDiv.add(taSelDiv, BorderLayout.CENTER);
        panCenter.add(panSelDiv, BorderLayout.EAST);
        panuseracc.add(panCenter, BorderLayout.CENTER);

        MyPanel panbtn = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        btnAdd = new MyButton("ADD", 2) {

            @Override
            public void onClick() {
                if ("ADD".equals(btnAdd.getText())) {
                    addtext();
                    btnCancel.setEnabled(true);
                    btnDelete.setEnabled(false);
                    btnEdit.setEnabled(false);
                    txtPwd.setText("");
                    disableRadioButtons();
                    clearAllSelection();
                    tblDiv.setEnabled(true);
                    btnAdd.setText("SAVE");
                    btnAdd.repaint();
                    btnAdd.validate();
                } else if ("SAVE".equals(btnAdd.getText())) {
                    saveUserType();
                    btnEdit.setEnabled(true);
                    btnCancel.setEnabled(false);
                    btnDelete.setEnabled(true);
                    tblDiv.setEnabled(false);
                    btnAdd.setText("ADD");
                    btnAdd.repaint();
                    btnAdd.validate();
                }
            }
        };
        panbtn.add(btnAdd);

        btnEdit = new MyButton("EDIT", 2) {

            @Override
            public void onClick() {
                if (cmbUser.getItemCount() > 0) {
                    if ("EDIT".equals(btnEdit.getText())) {
                        btnCancel.setEnabled(true);
                        btnAdd.setEnabled(false);
                        btnDelete.setEnabled(false);
                        disableRadioButtons();
                        cmbUser.setEnabled(false);
                        tblDiv.setEnabled(true);
                        btnEdit.setText("SAVE");
                        btnEdit.repaint();
                        btnEdit.validate();
                    } else if ("SAVE".equals(btnEdit.getText())) {
                        btnCancel.setEnabled(false);
                        btnAdd.setEnabled(true);
                        btnDelete.setEnabled(true);
                        enableRadioButtons();
                        cmbUser.setEnabled(true);
                        tblDiv.setEnabled(false);
                        updateUserType();
                        btnEdit.setText("EDIT");
                        btnEdit.repaint();
                        btnEdit.validate();
                    }
                }
            }
        };
        panbtn.add(btnEdit);

        btnDelete = new MyButton("DELETE", 2) {

            @Override
            public void onClick() {
                if (cmbUser.getItemCount() > 0) {
                    if (MyUtils.confirm("Do you want to DELETE ?", "DELETE")) {
                        deleteUser();
                    }
                }
            }
        };
        panbtn.add(btnDelete);



        btnCancel = new MyButton("CANCEL", 2) {

            @Override
            public void onClick() {
                btnCancel.setEnabled(false);
                tblDiv.setEnabled(false);
                btnAdd.setEnabled(true);
                btnDelete.setEnabled(true);
                btnEdit.setEnabled(true);
                cmbUser.setEnabled(true);
                btnAdd.setText("ADD");
                btnAdd.repaint();
                btnAdd.validate();
                btnEdit.setText("EDIT");
                btnEdit.repaint();
                btnEdit.validate();
                enableRadioButtons();
                addCombo();
                if (cmbUser.getItemCount() > 0) {
                    getFormDetails(cmbUser.getItemAt(0).toString());
                } else {
                    clearAllSelection();
                }
            }
        };
        btnCancel.setEnabled(false);
        panbtn.add(btnCancel);

        panuseracc.add(panbtn, BorderLayout.SOUTH);
        MainPanel.add(panuseracc);
        getComboList();
        if (cmbUser.getItemCount() > 0) {//filling form details on form loading
            getFormDetails(cmbUser.getItemAt(0).toString());
        } else {
            clearAllSelection();
        }
        return MainPanel;

    }

    public void getDivList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("UserAccessServlet");
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
            Depacketizer d = new Depacketizer(response);
            int rowCount = d.getInt();
            DivisionClass[] div = new DivisionClass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {
                div[i] = new DivisionClass();
                div[i].divcode = d.getString();
                div[i].divname = d.getString();
            }
            tablemodel.setData(div);
        } catch (Exception e) {
            MyUtils.showException("Get Division List", e);
            tablemodel.setData(null);
        }
    }

    public void getComboList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("UserAccessServlet");
            myHTTP.openOS();
            myHTTP.println("getComboList");
            myHTTP.println(getUserType());//sending usertype i.e. office or admin
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
            removeActionListener();
            cmbUser.removeAllItems();
            for (int i = 0; i < rowCount; i++) {
                String user = d.getString();
                cmbUser.addItem(user);
            }
            addActionListener();
        } catch (Exception e) {
            MyUtils.showException("getComboList : ", e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Office":
                getComboList();
                if (cmbUser.getItemCount() > 0) {//filling form details on form loading
                    getFormDetails(cmbUser.getItemAt(0).toString());
                } else {
                    clearAllSelection();
                }
                break;
            case "Admin":
                getComboList();
                if (cmbUser.getItemCount() > 0) {//filling form details on form loading
                    getFormDetails(cmbUser.getItemAt(0).toString());
                } else {
                    clearAllSelection();
                }
                break;
            case "SelectUser":
                if (cmbUser.getItemCount() > 0) {//filling form details on form loading
                    getFormDetails(cmbUser.getSelectedItem().toString());
                } else {
                    clearAllSelection();
                }
                break;
        }
    }

    private void disableRadioButtons() {
        rdOffice.setEnabled(false);
        rdAdmin.setEnabled(false);
    }

    private void enableRadioButtons() {
        rdOffice.setEnabled(true);
        rdAdmin.setEnabled(true);
    }

    private void saveUserType() {
        if (validatePasswordLength()) {
            String selRows = getSelectedRows().trim();
            if (!"".equals(selRows)) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("UserAccessServlet");
                    myHTTP.openOS();
                    myHTTP.println("saveUserType");
                    myHTTP.println(createPacket(txtname.getText()));
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String response = myHTTP.readLine();
                    myHTTP.closeIS();
                    if (response.startsWith("ERROR")) {
                        MyUtils.showMessage(response);
                        return;
                    } else {
                        if (cmbUser.getItemCount() > 0) {//filling form details on form loading
                            getFormDetails(cmbUser.getSelectedItem().toString());
                        } else {
                            clearAllSelection();
                        }
                        MyUtils.showMessage("Saved SuccessFully");
                    }
                } catch (Exception ex) {
                    MyUtils.showException("saveUserType : ", ex);
                }
            } else {
                MyUtils.showMessage("No Department Selected !");
                return;
            }
        } else {
            return;
        }
    }

    private String getUserType() {
        String usertype;
        if (rdOffice.isSelected()) {
            usertype = "1";
        } else {
            usertype = "2";
        }
        return usertype;
    }

    public String getSelectedRows() {
        int totalrows = tablemodel.getRowCount();
        String selRows = "";
        for (int i = 0; i < totalrows; i++) {
            if (tablemodel.isRowChecked(i)) {
                if (i == 0) {
                    selRows = "#" + (tablemodel.getValueAt(i, 1)).toString();
                } else {
                    selRows = selRows + " #" + tablemodel.getValueAt(i, 1);
                }
            }
        }
        return selRows;
    }

    private String createPacket(String username) {
        Packetizer p = new Packetizer();
        String password = SimpleUtilities.getPasswordText(txtPwd.getPassword());
        try {
            p.addString(getUserType());
            p.addString(username);
            p.addString(password);
            p.addString(getSelectedRows());
        } catch (Exception ex) {
            MyUtils.showMessage("createPacket : " + ex);
        }
        return p.getPacket();
    }

    private void addtext() {
        pantxt.removeAll();
        txtname = new MyTextField();
        pantxt.add(txtname);
        pantxt.add(txtPwd);
        pantxt.repaint();
        pantxt.validate();
    }

    private void addCombo() {
        pantxt.removeAll();
        pantxt.add(cmbUser);
        getComboList();
        pantxt.add(txtPwd);
        pantxt.repaint();
        pantxt.validate();
    }

    private void updateUserType() {
        if (validatePasswordLength()) {
            String selRows = getSelectedRows();
            if (!"".equals(selRows)) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("UserAccessServlet");
                    myHTTP.openOS();
                    myHTTP.println("updateUserType");
                    myHTTP.println(createPacket(cmbUser.getSelectedItem().toString()));
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String response = myHTTP.readLine();
                    myHTTP.closeIS();
                    if (response.startsWith("ERROR")) {
                        MyUtils.showMessage(response);
                        return;
                    } else {
                        if (cmbUser.getItemCount() > 0) {//filling form details on form loading
                            getFormDetails(cmbUser.getSelectedItem().toString());
                        } else {
                            clearAllSelection();
                        }
                        MyUtils.showMessage("Updated SuccessFully");
                    }
                } catch (Exception ex) {
                    MyUtils.showException("updateUserType : ", ex);
                }
            } else {
                MyUtils.showMessage("No Department Selected !");
                return;
            }
        } else {
            return;
        }
    }

    private void getFormDetails(String username) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("UserAccessServlet");
            myHTTP.openOS();
            myHTTP.println("getFormDetails");
            myHTTP.println(getUserType());
            myHTTP.println(username);
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
//            MyUtils.showMessage("response :" + response);
            Depacketizer dp = new Depacketizer(response);
            String password = dp.getString();
            setCheckedItems(dp.getString());
            txtPwd.setText(password);
        } catch (Exception ex) {
            MyUtils.showException("getFormDetails : ", ex);
        }
    }

    private void setCheckedItems(String accstring) {
        if ((!"".equals(accstring)) && (accstring != null)) {
            String access[] = accstring.split(" ");
            taSelDiv.setText("");
            clearAllSelection();
            taSelDiv.setText("   Selected Division List");
            for (int i = 0; i < access.length; i++) {
                String assign_div = access[i].substring(1);
                for (int j = 0; j < tablemodel.rowCount; j++) {
                    String divcode = tablemodel.getValueAt(j, 1).toString();
                    if (assign_div.equals(divcode)) {
                        tablemodel.setSelectedRow(j);
                        taSelDiv.append("\n" + assign_div + "  " + tablemodel.getValueAt(j, 2).toString());
                        tablemodel.fireTableDataChanged();
                    }
                }
            }
        } else {
            clearAllSelection();//if accesss has not assigned
        }
    }

    private void clearAllSelection() {
        int rows = tablemodel.getRowCount();
        for (int i = 0; i < rows; i++) {
            tablemodel.setUnSelectedRow(i);
        }
        taSelDiv.setText("");
        txtPwd.setText("");
    }

    private boolean validatePasswordLength() {
        char pwd[] = txtPwd.getPassword();
        if (pwd.length != 0 && pwd.length > 10) {
            MyUtils.showMessage("Enter Password between 0-10 characters.");
            return false;
        } else {
            return true;
        }
    }

    private void removeActionListener() {
        cmbUser.removeActionListener(this);
    }

    private void addActionListener() {
        cmbUser.addActionListener(this);
        cmbUser.setActionCommand("SelectUser");
    }

    private void deleteUser() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("UserAccessServlet");
            myHTTP.openOS();
            myHTTP.println("deleteUser");
            myHTTP.println(getUserType());
            myHTTP.println(cmbUser.getSelectedItem().toString());
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            getComboList();
            if (cmbUser.getItemCount() > 0) {//filling form details on form loading
                getFormDetails(cmbUser.getItemAt(0).toString());
            } else {
                clearAllSelection();
            }
        } catch (Exception ex) {
            MyUtils.showException("getFormDetails : ", ex);
        }
    }
}
