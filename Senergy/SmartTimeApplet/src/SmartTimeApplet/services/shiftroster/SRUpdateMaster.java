/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.shiftroster;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import com.toedter.calendar.JCalendar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;


public class SRUpdateMaster extends MyPanel implements ActionListener {

    public MyButton btnAdd, btnCancel;
    public JComboBox cmbYear, cmbMonth;
    public MyTextField txtEmp, txtSPattern, txtStartDate;
    public JComboBox[] cmb = new JComboBox[31];
    private ShiftRoster SRApplet;
    private String userID;
    private String[] shift;
    private String Name;
    private MonthRoster[] mr;
    private ArrayList<String>[] year;

    public SRUpdateMaster(ShiftRoster SRApplet, String userID, String Name) {
        this.SRApplet = SRApplet;
        this.userID = userID;
        this.Name = Name;
        addSRUpdatePanel();
        getShift();
        addShift();
        FillForm();
    }

    private void addShift() {
        for (int i = 0; i < 31; i++) {
            for (int j = 0; j < shift.length; j++) {
                cmb[i].addItem(shift[j]);
            }
        }
    }

    private void getShift() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SPatternFormServlet");
            myHTTP.openOS();
            myHTTP.println("ShiftList");
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer d = new Depacketizer(result);
                int rowCount = d.getInt();
                shift = new String[rowCount];
                for (int i = 0; i < rowCount; i++) {
                    shift[i] = d.getString();
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("Get Shift", ex);
        }
    }

    private void FillForm() {
        txtEmp.setText(userID + " " + Name);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SRFormServlet");
            myHTTP.openOS();
            myHTTP.println("FillFormUpdate");
            myHTTP.println(userID);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer dp = new Depacketizer(result);
                int rowCount = dp.getInt();
                mr = new MonthRoster[rowCount];
                for (int i = 0; i < rowCount; i++) {
                    mr[i] = new MonthRoster();
                    mr[i].EmpCode = dp.getString();
                    mr[i].Month = dp.getString();
                    //addItem(cmbMonth,mr[i].Month);
                    mr[i].Year = dp.getString();
                    addItem(cmbYear, mr[i].Year);
                    mr[i].ShiftStartDate = dp.getString();
                    mr[i].SPCode = dp.getString();
                    for (int j = 0; j < 31; j++) {
                        mr[i].days[j] = dp.getString();
                    }
                }

                int Count = cmbYear.getItemCount();
                year = new ArrayList[Count];
                for (int i = 0; i < Count; i++) {
                    year[i] = new ArrayList<String>();
                }
                for (int i = 0; i < mr.length; i++) {
                    for (int j = 0; j < Count; j++) {
                        if ((mr[i].Year).equals(cmbYear.getItemAt(j))) {
                            year[j].add(mr[i].Month);
                        }
                    }
                }
                addListeners();
                txtStartDate.setText(mr[0].ShiftStartDate);
                txtSPattern.setText(mr[0].SPCode);
                cmbYear.setSelectedItem(mr[0].Year);
                cmbMonth.setSelectedItem(mr[0].Month);
            }
        } catch (Exception ex) {
            MyUtils.showException("Fill Form", ex);
        }
    }

    public void addListeners() {
        cmbYear.addActionListener(this);
        cmbYear.setActionCommand("Year");
        cmbMonth.addActionListener(this);
        cmbMonth.setActionCommand("Month");

    }

    private void setMonths() {
        cmbMonth.removeActionListener(this);
        cmbYear.removeActionListener(this);
        cmbMonth.removeAllItems();
        int Index = cmbYear.getSelectedIndex();

        for (int i = 0; i < year[Index].size(); i++) {
            cmbMonth.addItem(IntToMonth(year[Index].get(i)));
        }

        cmbMonth.addActionListener(this);
        cmbYear.addActionListener(this);
    }

    private void setShifts() {
        cmbMonth.removeActionListener(this);
        cmbYear.removeActionListener(this);
        String[] days = null;
        for (int i = 0; i < mr.length; i++) {
            if (mr[i].Year.equals(cmbYear.getSelectedItem().toString())) {
                if (IntToMonth(mr[i].Month).equals(cmbMonth.getSelectedItem().toString())) {

                    days = new String[mr[i].days.length];
                    System.arraycopy(mr[i].days, 0, days, 0, mr[i].days.length);
                    break;
                }
            }
        }
        for (int i = 0; i < 31; i++) {
            cmb[i].setSelectedItem(days[i]);
        }
        cmbMonth.addActionListener(this);
        cmbYear.addActionListener(this);
    }

    private void addItem(JComboBox cmb, String Item) {
        boolean exists = false;
        for (int index = 0; index < cmb.getItemCount() && !exists; index++) {
            if (Item.equals(cmb.getItemAt(index))) {
                exists = true;
            }
        }
        if (!exists) {
            cmb.addItem(Item);
        }
    }

    private String CreatePacket() {
        Packetizer p = new Packetizer();
        try {
            p.addString(userID);
            p.addInt(MonthToInt(cmbMonth.getSelectedItem().toString()));
            p.addString(cmbYear.getSelectedItem().toString());
            for (int i = 0; i < 31; i++) {
                p.addString(cmb[i].getSelectedItem().toString());
            }
        } catch (Exception ex) {
            MyUtils.showException("CreatePacket", ex);
            return "PacketFail";
        }
        return p.getPacket();
    }

    private void UpdateRoster() {
        try {
            String Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                MyHTTP myHTTP = MyUtils.createServletConnection("SRFormServlet");
                myHTTP.openOS();
                myHTTP.println("UpdateRoster");
                myHTTP.println(Packet);
                myHTTP.closeOS();
                myHTTP.openIS();
                String Result = myHTTP.readLine();
                myHTTP.closeIS();
                if (Result.startsWith("ERROR:")) {
                    MyUtils.showMessage(Result);
                } else {
                    if (MyUtils.confirm("Return Home", "")) {
                        SRApplet.showHomePanel();
                    }
                }
            }

        } catch (Exception ex) {
            MyUtils.showException("Update Roster", ex);
        }
    }

    private void addSRUpdatePanel() {

        Dimension screenSize = SRApplet.getSize();
        int width = screenSize.width;
        int height = screenSize.height;
        this.setPreferredSize(new Dimension(width, (4 * height) / 5));

        MyPanel MainPanel = new MyPanel(new BorderLayout());

        MyPanel panStartDT = new MyPanel(new FlowLayout(FlowLayout.LEFT), "Start Date");
        MyLabel lblStartDT = new MyLabel(1, "Shift Start Date : ");
        panStartDT.add(lblStartDT);

        txtStartDate = new MyTextField(25);
        txtStartDate.setEditable(false);
        panStartDT.add(txtStartDate);

        MainPanel.add(panStartDT, BorderLayout.NORTH);

        MyPanel panMainCenter = new MyPanel(new BorderLayout());

        MyPanel panEmp = new MyPanel(new GridLayout(1, 2));

        MyPanel panlbltxtEmp = new MyPanel(new BorderLayout(), "Employee");
        panlbltxtEmp.setPreferredSize(new Dimension(width, (2 * height) / 5));

        MyPanel panlblEmp = new MyPanel(new GridLayout(4, 1, 0, 5));

        MyLabel lblEmpNm = new MyLabel(1, "Employee Name: ");
        panlblEmp.add(lblEmpNm);

        MyLabel lblYear = new MyLabel(1, "Select Year: ");
        panlblEmp.add(lblYear);

        MyLabel lblMonth = new MyLabel(1, "Select Month: ");
        panlblEmp.add(lblMonth);

        MyLabel lblPatternCd = new MyLabel(1, "Shift Pattern Code: ");
        panlblEmp.add(lblPatternCd);

        panlbltxtEmp.add(panlblEmp, BorderLayout.WEST);

        MyPanel pantxtEmp = new MyPanel(new GridLayout(4, 1, 0, 20));

        txtEmp = new MyTextField();
        txtEmp.setEditable(false);
        pantxtEmp.add(txtEmp);

        cmbYear = new JComboBox();

        pantxtEmp.add(cmbYear);

        cmbMonth = new JComboBox();
        pantxtEmp.add(cmbMonth);

        txtSPattern = new MyTextField();
        txtSPattern.setEditable(false);
        pantxtEmp.add(txtSPattern);

        panlbltxtEmp.add(pantxtEmp, BorderLayout.CENTER);

        panEmp.add(panlbltxtEmp);

        MyPanel panCalendar = new MyPanel(new BorderLayout(), "Refer Calendar");

        JCalendar calendar = new JCalendar();
        calendar.putClientProperty("JCalendar.headerStyle", "None");

        panCalendar.add(calendar, BorderLayout.CENTER);

        panEmp.add(panCalendar);

        panMainCenter.add(panEmp, BorderLayout.NORTH);
        MyPanel panMainSRoster = new MyPanel(new BorderLayout());
        MyPanel panSRoster = new MyPanel(new GridLayout(4, 16, 2, 5), "Shift Roster");

        MyLabel[] lbl = new MyLabel[31];


        for (int i = 0; i < 31; i++) {
            String a = "00" + Integer.toString(i + 1);
            lbl[i] = new MyLabel(1, a.substring(a.length() - 2), JLabel.CENTER);

            panSRoster.add(lbl[i]);
            cmb[i] = new JComboBox();

            cmb[i].addItem("");
            cmb[i].addItem("00");
            panSRoster.add(cmb[i]);
        }
        panMainSRoster.add(panSRoster, BorderLayout.CENTER);

        MainPanel.add(panMainCenter, BorderLayout.CENTER);
        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Add ShiftRoster Details");
        btnAdd = new MyButton("Update Roster", 2) {

            @Override
            public void onClick() {
                UpdateRoster();
            }
        };

        panButtons.add(btnAdd);
        btnCancel = new MyButton("Cancel", 2) {

            @Override
            public void onClick() {
                SRApplet.showHomePanel();
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
        if (e.getActionCommand().equals("Year")) {
            setMonths();
            setShifts();
        } else if (e.getActionCommand().equals("Month")) {
            setShifts();
        } else {
            MyUtils.showMessage("Unknown Command");
        }
    }

    private class MonthRoster {

        public String EmpCode;
        public String Month;
        public String Year;
        public String ShiftStartDate;
        public String SPCode;
        public String[] days = new String[31];
    }

    private String IntToMonth(Object i) {
        int a = Integer.parseInt(i.toString());
        String ret = "";
        switch (a) {
            case 1:
                ret = "January";
                break;
            case 2:
                ret = "Febuary";
                break;
            case 3:
                ret = "March";
                break;
            case 4:
                ret = "April";
                break;
            case 5:
                ret = "May";
                break;
            case 6:
                ret = "June";
                break;
            case 7:
                ret = "July";
                break;
            case 8:
                ret = "August";
                break;
            case 9:
                ret = "September";
                break;
            case 10:
                ret = "October";
                break;
            case 11:
                ret = "November";
                break;
            case 12:
                ret = "December";
                break;
        }
        return ret;
    }

    private int MonthToInt(String month) {
        int ret = 0;
        for (int i = 0; i < 12; i++) {
            if (month.equals(IntToMonth(i))) {
                ret = i;
                break;
            }
        }
        return ret;
    }
}
