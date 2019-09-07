/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.shiftroster;

import SmartTimeApplet.services.employee.UserTableModel;
import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import lib.User;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
import lib.utils.Helper.Tuple;
import lib.utils.Packetizer;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponent;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

public class SRAddNewMaster extends MyPanel implements ActionListener {

    public MyButton btnAdd, btnCancel;
    public MyTextField txtEmp, txtSPattern, txtSearch;
    public MyTextField[] txtShift = new MyTextField[31];
    public JComboBox cmbSPCode;
    public MyTable table;
    private User[] users;
    private ShiftRoster ShiftRosterApplet;
    private String[] userID;
    private UserTableModel tableModel;
    private JDatePicker dtpicker;

    public SRAddNewMaster(ShiftRoster ShiftRosterApplet, String[] userID) {
        try {
            this.ShiftRosterApplet = ShiftRosterApplet;
            this.userID = userID;
            addSRPanel();
            FillForm();

        } catch (Exception ex) {
            MyUtils.showException("SrAddNewMaster", ex);
        }
    }

    private void FillForm() {
        try {
            String UserIDs = PacketUserID();
            MyHTTP myHTTP = MyUtils.createServletConnection("SRFormServlet");
            myHTTP.openOS();
            myHTTP.println("FillFormAdd");
            myHTTP.println(UserIDs);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer d = new Depacketizer(result);
                int rowCount = d.getInt();
                users = new User[rowCount];
                //get all docs
                for (int i = 0; i < rowCount; i++) {
                    users[i] = new User();
                    users[i].userID = d.getString();
                    users[i].name = d.getString();
                    users[i].desig = d.getString();
                    users[i].division = d.getString();
                    String date1 = d.getString();
                    users[i].section = d.getString();
                }
                tableModel.setData(users);
            }
            setShiftPattern();
        } catch (Exception ex) {
            MyUtils.showException("FillForm", ex);
        }
    }

    private void setShiftPattern() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SRFormServlet");
            myHTTP.openOS();
            myHTTP.println("ShiftPatternList");
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer d = new Depacketizer(result);
                int rowCount = d.getInt();
                for (int i = 0; i < rowCount; i++) {
                    cmbSPCode.addItem(d.getString());
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("Set Shift Pattern", ex);

        }
    }

    private String PacketUserID() throws Exception {
        Packetizer p = new Packetizer();
        for (int i = 0; i < userID.length; i++) {
            p.addString(userID[i]);
        }
        return p.getPacket();
    }

    private void deleteEmp() {
        int row = table.getSelectedRow();
        User[] temp = users;

        users = new User[temp.length - 1];
        userID = new String[users.length];
        for (int i = 0; i < row; i++) {
            users[i] = temp[i];
            userID[i] = users[i].userID;
        }
        for (int i = row; i < temp.length - 1; i++) {
            users[i] = temp[i + 1];
            userID[i] = users[i].userID;
        }

        tableModel.setData(users);
    }

    private boolean FormFilled() {
        if (cmbSPCode.getSelectedIndex() == 0) {
            MyUtils.showMessage("Select Shift Pattern Code");
            return false;
        }
        return true;
    }

    private String CreatePacket() {
        Packetizer p = new Packetizer();
        try {
            p.addString(cmbSPCode.getSelectedItem().toString());
            DateModel model = ((JDateComponent) dtpicker).getModel();
            String month = "0" + (model.getMonth() + 1);
            String Day = "0" + model.getDay();
            String date = model.getYear() + "-" + month.substring(month.length() - 2) + "-" + Day.substring(Day.length() - 2);
            p.addString(date);
            for (int i = 0; i < userID.length; i++) {

                p.addString(userID[i]);
            }
        } catch (Exception ex) {
            MyUtils.showException("Create Packet", ex);
            return "PacketFail";

        }
        return p.getPacket();
    }

    private void addRoster() {
        try {
            if (FormFilled()) {
                String Packet = CreatePacket();
                if (!Packet.equals("PacketFail")) {
                    MyHTTP myHTTP = MyUtils.createServletConnection("SRFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("AddRoster");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    myHTTP.closeIS();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else if (result.startsWith("WARNING")) {
                        if (MyUtils.confirm(result, "WARNING!!")) {
                            myHTTP.openOS();
                            myHTTP.println("YES");
                        }
                    } else {
                        ShiftRosterApplet.showHomePanel();
                    }
                }
            }

        } catch (Exception ex) {
            MyUtils.showException("Add Roster", ex);
        }
    }

    private void addSRPanel() throws Exception {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        this.setSize(width - 320, height - 290);


        MyPanel MainPanel = new MyPanel(new BorderLayout());

        MyPanel panMainNorth = new MyPanel(new GridLayout(1, 2), "Select Start Date");
        MyPanel panStartDT = new MyPanel(new GridLayout(1, 2));
        MyLabel lblStartDT = new MyLabel(1, "Shift Start Date : ");
        panStartDT.add(lblStartDT);

        dtpicker = JDateComponentFactory.createJDatePicker();
        JComponent guiElement2 = (JComponent) dtpicker;
        ((JDateComponent) dtpicker).getModel().setDay(1);
        panStartDT.add(guiElement2);
        panMainNorth.add(panStartDT);

        MyPanel panSPCode = new MyPanel(new GridLayout(1, 2));
        MyLabel lblSPCode = new MyLabel(1, "Shift Pattern Code", JLabel.CENTER);
        panSPCode.add(lblSPCode);

        cmbSPCode = new JComboBox();
        cmbSPCode.addItem("Select Shift Pattern");
        cmbSPCode.addActionListener(this);
        cmbSPCode.setActionCommand("SPCode");
        panSPCode.add(cmbSPCode);
        panMainNorth.add(panSPCode);
        MainPanel.add(panMainNorth, BorderLayout.NORTH);

        MyPanel panMainCenter = new MyPanel(new BorderLayout(), "Configure Shift Roster");
        MyPanel panEmp = new MyPanel(new BorderLayout());
        panEmp.setPreferredSize(new Dimension(width, height / 3));
        MyPanel buttonsPanel = new MyPanel(new GridLayout(1, 3));
        MyButton btnDelEmp = new MyButton("Delete Employee") {

            @Override
            public void onClick() {
                deleteEmp();
            }
        };
        buttonsPanel.add(btnDelEmp);
        buttonsPanel.add(new MyLabel(MyLabel.TYPE_LABEL, " Search Employee:"));
        MyTextField searchTextName = new MyTextField();
        buttonsPanel.add(searchTextName);
        panEmp.add(buttonsPanel, BorderLayout.NORTH);
        tableModel = new UserTableModel();
        table = new MyTable(tableModel);
        table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        table.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panEmp.add(table.getGUI(), BorderLayout.CENTER);
        panMainCenter.add(panEmp, BorderLayout.NORTH);

        MyPanel panMainSRoster = new MyPanel(new BorderLayout());
        MyPanel panSRoster = new MyPanel(new GridLayout(4, 16, 2, 5), "Shift Roster");
        MyLabel[] lbl = new MyLabel[31];

        for (int i = 0; i < 31; i++) {
            String a = "0" + Integer.toString(i + 1);
            lbl[i] = new MyLabel(1, a.substring(a.length() - 2), JLabel.CENTER);

            panSRoster.add(lbl[i]);
            txtShift[i] = new MyTextField();
            txtShift[i].setEditable(false);
            panSRoster.add(txtShift[i]);

        }

        panMainSRoster.add(panSRoster, BorderLayout.CENTER);

        MainPanel.add(panMainCenter, BorderLayout.CENTER);

        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Add Holiday Details");
        btnAdd = new MyButton("Add Roster", 2) {

            @Override
            public void onClick() {
                addRoster();
            }
        };
        panButtons.add(btnAdd);

        btnCancel = new MyButton("Cancel", 2) {

            @Override
            public void onClick() {
                ShiftRosterApplet.showHomePanel();
            }
        };
        panButtons.add(btnCancel);

        panMainSRoster.add(panButtons, BorderLayout.SOUTH);
        panMainCenter.add(panMainSRoster, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(MainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("SPCode")) {
            try {
                clearTextField();
                if (cmbSPCode.getSelectedIndex() != 0) {

                    String SPCode = (String) cmbSPCode.getSelectedItem();
                    MyHTTP myHTTP = MyUtils.createServletConnection("SRFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("SPatternDetails");
                    myHTTP.println(SPCode);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    myHTTP.closeIS();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else {
                        Depacketizer dp = new Depacketizer(result);
                        int rowCount = dp.getInt();
                        Tuple[] tuple = new Tuple[rowCount];
                        for (int i = 0; i < rowCount; i++) {
                            tuple[i] = new Tuple(dp.getInt(), dp.getString(), dp.getInt());

                        }
                        int i = 0, j, k = 0;
                        int day = ((JDateComponent) dtpicker).getModel().getDay();
                        j = day - 1;
                        while (i < rowCount) {
                            if (k < tuple[i].days) {
                                if (j < 31) {
                                    txtShift[j].setText(tuple[i].shift);
                                    j++;
                                    k++;
                                } else {
                                    j = 0;
                                }
                            } else {
                                k = 0;
                                i++;
                            }
                        }
                    }

                }
            } catch (Exception ex) {
                MyUtils.showException("Action SPCode", ex);
            }
        }

    }

    private void clearTextField() {
        for (int i = 0; i < 31; i++) {
            txtShift[i].setText("");
        }
    }
}
