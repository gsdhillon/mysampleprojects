/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.ExceptionEntryApplication;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import lib.Utility.DateUtilities;
import lib.Utility.SimpleUtilities;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

public class ExceptionEntry extends JPanel implements ActionListener, ChangeListener {

    JButton bSave,
            bCancel;
    MyTextField txtRemark,
            txtPreRemark,
            txtWorkHour,
            txtExtrawrk,
            txtpreIn,
            txtpreOut,
            txtpreWrkHr,
            txtpreExtrawork,
            txtLate,
            txtEarly,
            txtstatus,
            txtpreLate,
            txtprestatus,
            txtpreEarly;
    JSpinner DTPIn,
            DTPOut;
    MyLabel lblrecCode,
            lblShowDiv,
            lblShowDesig,
            lblDesig,
            lblshoccno,
            lblCCno,
            lblshowename,
            lblename,
            lblshowecode,
            lblecode,
            panshowrecode,
            lblrecName,
            panshowrecname,
            lblappCode,
            lblshowappcode,
            lblappName,
            lblshowappname;
    ExceptionEntryApplet ExceptionEntryApplet;
    JComboBox cmboShift;
    MyButton btneditShift,
            btngetShift;
    JComponent dtDate;
    String overtimelimit = "00:00:00";
    String compofflimit = "00:00:00";
    String gracelatetime = "00:00:00";
    String graceEarlytime = "00:00:00";
    String App_ID = "";
    String command1;
    String workhorlimit = "00:00:00";
    String late = "00:00:00";
    String early = "00:00:00";
    String status = "00:00:00";
    String shiftstarttm = "09:00:00";
    String shiftendtime = "17:00:00";
    static String tempstatus = "";

    public ExceptionEntry(final ExceptionEntryApplet ExceptionEntryApplet, String command, String AppID) {

        this.setSize(700, 800);
        this.setLayout(new BorderLayout());
        this.App_ID = AppID;
        this.command1 = command;
        if (!"Add".equals(command)) {
            generateAppId();
        }

        MyPanel MainPanel = new MyPanel(new BorderLayout(), "");

        MyPanel panNorth = new MyPanel(new GridLayout(2, 1, 5, 10), "Employee Details : ");

        MyPanel pang1 = new MyPanel(new GridLayout());

        MyPanel panempcode = new MyPanel(new BorderLayout());
        lblecode = new MyLabel(1, "EmpCode : ");
        panempcode.add(lblecode, BorderLayout.WEST);

        lblshowecode = new MyLabel(1, "");
        panempcode.add(lblshowecode, BorderLayout.CENTER);
        pang1.add(panempcode);
        panNorth.add(pang1);

        MyPanel panempname = new MyPanel(new BorderLayout());

        lblename = new MyLabel(1, "Name : ");
        panempname.add(lblename, BorderLayout.WEST);

        lblshowename = new MyLabel(1, "");
        panempname.add(lblshowename, BorderLayout.CENTER);
        pang1.add(panempname);

        MyPanel pang2 = new MyPanel(new GridLayout(1, 3));

        MyPanel panccno = new MyPanel(new BorderLayout());
        lblCCno = new MyLabel(1, "CCNo :    ");
        panccno.add(lblCCno, BorderLayout.WEST);

        lblshoccno = new MyLabel(1, "");
        panccno.add(lblshoccno, BorderLayout.CENTER);
        pang2.add(panccno);

        MyPanel panDesig = new MyPanel(new BorderLayout());

        lblDesig = new MyLabel(1, "Designation : ");
        panDesig.add(lblDesig, BorderLayout.WEST);

        lblShowDesig = new MyLabel(1, "");
        panDesig.add(lblShowDesig, BorderLayout.CENTER);
        pang2.add(panDesig);

        MyPanel panDiv = new MyPanel(new BorderLayout());
        MyLabel lbldiv = new MyLabel(1, "Division : ");
        panDiv.add(lbldiv, BorderLayout.WEST);

        lblShowDiv = new MyLabel(1, "");
        panDiv.add(lblShowDiv, BorderLayout.CENTER);
        pang2.add(panDiv);
        panNorth.add(pang2);
        MainPanel.add(panNorth, BorderLayout.NORTH);

        MyPanel A2 = new MyPanel(new BorderLayout(), "Details");

        MyPanel C21 = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        MyLabel lblDate = new MyLabel(1, "Date :");
        C21.add(lblDate);

        JDatePicker picker = JDateComponentFactory.createJDatePicker();
        dtDate = (JComponent) picker;
        C21.add(dtDate);

        btngetShift = new MyButton("Get Shift") {

            @Override
            public void onClick() {
                if (DateUtilities.checkDateSelected(dtDate, "Date")) {
                    getEmployeeDayStatus(DateUtilities.getDate(dtDate));
                    getEmpShift();
                    getShiftDetails();
                } else {
                    MyUtils.showMessage("Select Date !");
                }
            }
        };
        C21.add(btngetShift);

        A2.add(C21, BorderLayout.NORTH);

        MyPanel B22 = new MyPanel(new BorderLayout());

        MyPanel C31 = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        MyLabel lblShift = new MyLabel(1, "Shift:");
        C31.add(lblShift);

        cmboShift = new JComboBox();
        addAcctionListener();
        cmboShift.setEnabled(false);
        C31.add(cmboShift);

        btneditShift = new MyButton("Edit Shift") {

            @Override
            public void onClick() {
                cmboShift.setEnabled(true);
            }
        };
        C31.add(btneditShift);
        B22.add(C31, BorderLayout.NORTH);

        MyPanel panMainGrid = new MyPanel(new GridLayout(1, 2));

        MyPanel pangrid1 = new MyPanel(new BorderLayout());
        MyPanel panlblgrid1 = new MyPanel(new GridLayout(4, 1, 5, 5));
        MyLabel lblIn = new MyLabel(1, "In :");
        panlblgrid1.add(lblIn, BorderLayout.WEST);

        MyLabel lblOut = new MyLabel(1, "Out :");
        panlblgrid1.add(lblOut);

        MyLabel lblWrkHuor = new MyLabel(1, "Wrk Hrs :");
        panlblgrid1.add(lblWrkHuor);

        MyLabel lblExtwrk = new MyLabel(1, "ExWrkHrs :");
        panlblgrid1.add(lblExtwrk);

        pangrid1.add(panlblgrid1, BorderLayout.WEST);

        MyPanel pantxtgrid1 = new MyPanel(new GridLayout(4, 1, 5, 5));
        DTPIn = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(DTPIn, "HH:mm:ss");
        timeEditor.setForeground(new Color(103, 213, 83));
        DTPIn.setEditor(timeEditor);
        pantxtgrid1.add(DTPIn);

        DTPOut = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(DTPOut, "HH:mm:ss");
        timeEditor2.setForeground(new Color(103, 213, 83));
        DTPOut.setEditor(timeEditor2);
        pantxtgrid1.add(DTPOut);

        txtWorkHour = new MyTextField();
        pantxtgrid1.add(txtWorkHour);

        txtExtrawrk = new MyTextField();
        pantxtgrid1.add(txtExtrawrk);

        pangrid1.add(pantxtgrid1, BorderLayout.CENTER);
        panMainGrid.add(pangrid1);

        MyPanel pangrid2 = new MyPanel(new BorderLayout());
        MyPanel panlblgrid2 = new MyPanel(new GridLayout(4, 1, 5, 5));

        MyLabel lblpreIn = new MyLabel(1, "Pre In :");
        panlblgrid2.add(lblpreIn);

        MyLabel lblpreOut = new MyLabel(1, "Pre Out :");
        panlblgrid2.add(lblpreOut);

        MyLabel lblpreWrkHuor = new MyLabel(1, "Pre Wrk Hrs :");
        panlblgrid2.add(lblpreWrkHuor);

        MyLabel lblpreExtwrk = new MyLabel(1, "Pre ExWrkHrs :");
        panlblgrid2.add(lblpreExtwrk);
        pangrid2.add(panlblgrid2, BorderLayout.WEST);

        MyPanel pantxtgrid2 = new MyPanel(new GridLayout(4, 1, 5, 5));

        txtpreIn = new MyTextField();
        pantxtgrid2.add(txtpreIn);

        txtpreOut = new MyTextField();
        pantxtgrid2.add(txtpreOut);

        txtpreWrkHr = new MyTextField();
        pantxtgrid2.add(txtpreWrkHr);

        txtpreExtrawork = new MyTextField();
        pantxtgrid2.add(txtpreExtrawork);

        pangrid2.add(pantxtgrid2, BorderLayout.CENTER);
        panMainGrid.add(pangrid2);

        MyPanel pangrid3 = new MyPanel(new BorderLayout());
        MyPanel panlblgrid3 = new MyPanel(new GridLayout(4, 1, 5, 5));
        MyLabel lblLate = new MyLabel(1, "Late :");
        panlblgrid3.add(lblLate);

        MyLabel lblStatus = new MyLabel(1, "Status :");
        panlblgrid3.add(lblStatus);

        MyLabel lblEarly = new MyLabel(1, "Early :");
        panlblgrid3.add(lblEarly);

        MyLabel lblRemark = new MyLabel(1, "Remark :");
        panlblgrid3.add(lblRemark);
        pangrid3.add(panlblgrid3, BorderLayout.WEST);


        MyPanel pantxtgrid3 = new MyPanel(new GridLayout(4, 1, 5, 5));

        txtLate = new MyTextField();
        pantxtgrid3.add(txtLate);

        txtstatus = new MyTextField();
        pantxtgrid3.add(txtstatus);

        txtEarly = new MyTextField();
        pantxtgrid3.add(txtEarly);

        txtRemark = new MyTextField();
        pantxtgrid3.add(txtRemark);
        pangrid3.add(pantxtgrid3, BorderLayout.CENTER);
        panMainGrid.add(pangrid3);

        MyPanel pangrid4 = new MyPanel(new BorderLayout());
        MyPanel panlblgrid4 = new MyPanel(new GridLayout(4, 1, 5, 5));

        MyLabel lblpreLate = new MyLabel(1, "Pre Late :");
        panlblgrid4.add(lblpreLate);

        MyLabel lblpreStatus = new MyLabel(1, "Pre Status :");
        panlblgrid4.add(lblpreStatus);

        MyLabel lblpreEarly = new MyLabel(1, "Pre Early :");
        panlblgrid4.add(lblpreEarly);

        MyLabel lblpreRemark = new MyLabel(1, "Pre Remark :");
        panlblgrid4.add(lblpreRemark);

        pangrid4.add(panlblgrid4, BorderLayout.WEST);

        MyPanel pantxtgrid4 = new MyPanel(new GridLayout(4, 1, 5, 5));
        txtpreLate = new MyTextField();
        pantxtgrid4.add(txtpreLate);

        txtprestatus = new MyTextField();
        pantxtgrid4.add(txtprestatus);

        txtpreEarly = new MyTextField();
        pantxtgrid4.add(txtpreEarly);

        txtPreRemark = new MyTextField();
        pantxtgrid4.add(txtPreRemark);

        pangrid4.add(pantxtgrid4, BorderLayout.CENTER);
        panMainGrid.add(pangrid4);

        B22.add(panMainGrid, BorderLayout.CENTER);
        A2.add(B22, BorderLayout.CENTER);

        MyPanel panHeadDetails = new MyPanel(new GridLayout(1, 2));

        MyPanel panp1 = new MyPanel(new GridLayout(2, 1), "Recomender");
        MyPanel panreccode = new MyPanel(new BorderLayout());
        lblrecCode = new MyLabel(1, "EmpCode : ");
        panreccode.add(lblrecCode, BorderLayout.WEST);

        panshowrecode = new MyLabel(1, "");
        panreccode.add(panshowrecode, BorderLayout.CENTER);
        panp1.add(panreccode);

        MyPanel panrecname = new MyPanel(new BorderLayout());
        lblrecName = new MyLabel(1, "Name :    ");
        panrecname.add(lblrecName, BorderLayout.WEST);

        panshowrecname = new MyLabel(1, "");
        panrecname.add(panshowrecname, BorderLayout.CENTER);
        panp1.add(panrecname);
        panHeadDetails.add(panp1);

        MyPanel panp2 = new MyPanel(new GridLayout(2, 1), "Approver");
        MyPanel panappcode = new MyPanel(new BorderLayout());
        lblappCode = new MyLabel(1, "EmpCode : ");
        panappcode.add(lblappCode, BorderLayout.WEST);

        lblshowappcode = new MyLabel(1, "");
        panappcode.add(lblshowappcode, BorderLayout.CENTER);
        panp2.add(panappcode);

        MyPanel panappname = new MyPanel(new BorderLayout());
        lblappName = new MyLabel(1, "Name :    ");
        panappname.add(lblappName, BorderLayout.WEST);

        lblshowappname = new MyLabel(1, "");
        panappname.add(lblshowappname, BorderLayout.CENTER);
        panp2.add(panappname, BorderLayout.CENTER);
        panHeadDetails.add(panp2);
        A2.add(panHeadDetails, BorderLayout.SOUTH);

        MainPanel.add(A2, BorderLayout.CENTER);

        MyPanel A3 = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        bSave = new MyButton("Save", 2) {

            @Override
            public void onClick() {
                switch (command1) {
                    case "Add":
                        addExceptionEntry();
                        break;
                    case "update":
                        updateExceptionEntry(App_ID);
                        break;
                }
            }
        };

        bSave.setToolTipText("Save Exception");
        A3.add(bSave);

        bCancel = new MyButton("Cancel", 2) {

            @Override
            public void onClick() {
                ExceptionEntryApplet.showHomePanel();
            }
        };
        bCancel.setToolTipText("Cancel Process");
        A3.add(bCancel);

        MainPanel.add(A3, BorderLayout.SOUTH);

        this.add(MainPanel);
        getAprovingAuthority();
        fillShiftCombo();
        getEmpCategoryDetails();
        addChangeListener();
        switch (command1) {
            case "Add":
                getMusterDetails(AppID);
                break;
            case "update":
                getPreDetails(AppID);
                break;
        }
    }

    private void getAprovingAuthority() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("commonservlet");
            myHTTP.openOS();
            myHTTP.println("getAproovingAuthority");
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer dp = new Depacketizer(result);
                lblshowecode.setText(dp.getString());
                lblshowename.setText(dp.getString());
                lblshoccno.setText(dp.getString());
                lblShowDesig.setText(dp.getString());
                lblShowDiv.setText(dp.getString());
                lblshowappcode.setText(dp.getString());//division head code
                lblshowappname.setText(dp.getString());//division head name
                panshowrecode.setText(dp.getString());//section head code
                panshowrecname.setText(dp.getString());//section head name
            }
        } catch (Exception ex) {
            MyUtils.showException("getFillForm", ex);
        }
    }

    private void getPreDetails(String AppId) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ExceptionEntryServlet");
            myHTTP.openOS();
            myHTTP.println("getPreDetails");
            myHTTP.println(AppId);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer dp = new Depacketizer(result);
                int size = dp.getInt();
                String date = dp.getString();
                MyUtils.setDate(dtDate, date);
                String shiftcode = dp.getString();
                if (shiftcode != null && !"".equals(shiftcode)) {
                    cmboShift.setSelectedItem(shiftcode);
                }
                String starttm = dp.getString();
                String endtime = dp.getString();
                DateUtilities.setSpinnerTime(DTPIn, starttm);
                DateUtilities.setSpinnerTime(DTPOut, endtime);

                txtWorkHour.setText(dp.getString());
                txtExtrawrk.setText(dp.getString());
                txtpreIn.setText(dp.getString());
                txtpreOut.setText(dp.getString());
                txtpreWrkHr.setText(dp.getString());
                txtpreExtrawork.setText(dp.getString());
                txtLate.setText(dp.getString());
                txtEarly.setText(dp.getString());
                txtstatus.setText(dp.getString());
                txtRemark.setText(dp.getString());
                txtpreLate.setText(dp.getString());
                txtpreEarly.setText(dp.getString());
                txtprestatus.setText(dp.getString());
            }
        } catch (Exception ex) {
            MyUtils.showException("getPreDetails", ex);
        }
    }

    private void fillShiftCombo() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ExceptionEntryServlet");
            myHTTP.openOS();
            myHTTP.println("fillShiftCombo");
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer dp = new Depacketizer(result);
                int size = dp.getInt();
                cmboShift.removeAllItems();
                cmboShift.addItem("Select Shift");
                for (int i = 0; i < size; i++) {
                    cmboShift.addItem(dp.getString());
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("fillShiftCombo", ex);
        }
    }

    private void getEmpShift() {
        String selectedDate = DateUtilities.getDate(dtDate);
        String ymd[] = selectedDate.split("-");

        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ExceptionEntryServlet");
            myHTTP.openOS();
            myHTTP.println("getEmpShift");
            myHTTP.println(ymd[0]);//year 
            myHTTP.println(ymd[1]);//month
            myHTTP.println(ymd[2]);//day
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                cmboShift.setSelectedItem(result);
            }
        } catch (Exception ex) {
            MyUtils.showException("getEmpShift", ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("SelectShift".equals(e.getActionCommand())) {
            getShiftDetails();
        }
    }

    private void getShiftDetails() {
        String shift = cmboShift.getSelectedItem().toString();
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ExceptionEntryServlet");
            myHTTP.openOS();
            myHTTP.println("getShiftDetails");
            myHTTP.println(shift);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer dp = new Depacketizer(result);
                int size = dp.getInt();
                if (size != 0) {
                    shiftstarttm = dp.getString();
                    DateUtilities.setSpinnerTime(DTPIn, shiftstarttm);
                    shiftendtime = dp.getString();
                    DateUtilities.setSpinnerTime(DTPOut, shiftendtime);
                    String workhours = dp.getString();
                    if ("".equals(workhours)) {
                        workhorlimit = DateUtilities.getTimeDiff(shiftstarttm, shiftendtime);
                    } else {
                        workhorlimit = workhours;
                    }
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("getShiftDetails", ex);
        }
    }

    private void getMusterDetails(String date) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ExceptionEntryServlet");
            myHTTP.openOS();
            myHTTP.println("getMusterDetails");
            myHTTP.println(date);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                MyUtils.showMessage("getMusterDetails result : " + result);
                Depacketizer dp = new Depacketizer(result);
                int size = dp.getInt();
                if (size != 0) {
                    String shiftcode = dp.getString();
                    if (shiftcode != null && !"".equals(shiftcode)) {
                        cmboShift.setSelectedItem(shiftcode);
                    }
                    String login = dp.getString();
                    String logout = dp.getString();
                    DateUtilities.setSpinnerTime(DTPIn, login);
                    DateUtilities.setSpinnerTime(DTPOut, logout);
                    txtpreIn.setText(login);
                    txtpreOut.setText(logout);
                    txtWorkHour.setText(dp.getString());
                    txtExtrawrk.setText(dp.getString());
                    txtstatus.setText(dp.getString());
                    txtRemark.setText(dp.getString());
                } else {
                    cmboShift.setSelectedIndex(0);
                    DateUtilities.setSpinnerTime(DTPIn, "00:00:00");
                    DateUtilities.setSpinnerTime(DTPOut, "00:00:00");
                    txtpreWrkHr.setText("");
                    txtpreExtrawork.setText("");
                    txtprestatus.setText("");
                    txtRemark.setText("");
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("getPreDetails", ex);
        }
    }

    private void getEmployeeDayStatus(String date) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ExceptionEntryServlet");
            myHTTP.openOS();
            myHTTP.println("getEmployeeDayStatus");
            myHTTP.println(date);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                int intstatus = Integer.parseInt(result);
                if (intstatus == 0) {
                    tempstatus = "AA";
                } else if (intstatus == 1) {
                    tempstatus = "WW";
                } else if (intstatus == 2) {
                    tempstatus = "HH";
                } else if (intstatus == 3) {
                    tempstatus = "LL";
                } else {
                    tempstatus = "XX";
                }
            }
            MyUtils.showMessage("Hello  getEmployeeDayStatus : " + tempstatus);
        } catch (Exception ex) {
            MyUtils.showException("getShiftDetails", ex);
        }
    }

    private void calculateShiftDetails() {
        txtExtrawrk.setText("");
        String shift = cmboShift.getSelectedItem().toString();
        status = "XX";
        if ("AA".equals(tempstatus)) {//if record not found then employee is absent but we are entering exception entry with in and out time then emlpoyee becomes present
            tempstatus = "XX";
        }
        if (!"Select Shift".equals(shift)) {
            try {
                String intime = DateUtilities.getSpinnerTime(DTPIn);
                String outtime = DateUtilities.getSpinnerTime(DTPOut);
                String worked_Hours = DateUtilities.getTimeDiff(intime, outtime);
                txtWorkHour.setText(worked_Hours);
                //calculating late coming
                int late_diff = (int) (DateUtilities.getTimeDiffInSeconds(shiftstarttm, intime)) / 60;//difference in minutes
                int grace_late_min = (int) (DateUtilities.getTimeDiffInSeconds("00:00:00", gracelatetime)) / 60;//difference in minutes

                if (late_diff > grace_late_min) {
                    //For Come In Another Shift And LAte Coming
                    if (late_diff > 360) {
                        status = tempstatus + "S";
                    } else {
                        int login_time_diff = (int) DateUtilities.getTimeDiffInSeconds(shiftstarttm, intime);
                        String late_time = (((login_time_diff) / 3600) + ":" + (((login_time_diff) % 3600) / 60) + ":" + (login_time_diff % 60));
                        txtLate.setText(late_time);
                        status = tempstatus + "L";
                    }
                } else if (late_diff < 0) {
                    late_diff = late_diff * (-1);
                    if (late_diff > 360) {
                        status = tempstatus + "S";
                    }
                }

                int early_diff = (int) DateUtilities.getTimeDiffInSeconds(shiftendtime, outtime);//difference in seconds
                System.out.println(" early_diff yyyyy : " + early_diff + " shiftendtime :" + shiftendtime + " outtime : " + outtime);
                int grace_early_min = (int) DateUtilities.getTimeDiffInSeconds("00:00:00", graceEarlytime) / 60;//difference in minutes

                if ((early_diff) < 0) {//early_diff converted seconds to minutes   //difference getting in minus value if emp has late so checking that emp has came late or not
                    early_diff = early_diff * (-1);
                    if ((early_diff / 60) > grace_early_min) {
                        String early_time = (((early_diff) / 3600) + ":" + (((early_diff) % 3600) / 60) + ":" + (early_diff % 60));
                        txtEarly.setText(early_time);

                        if (status.length() == 3) {
                            char ch = status.charAt(2);
                            if (ch == 'L') {
                                status = tempstatus + "#";
                            }
                        } else {
                            status = tempstatus + "E";
                        }
                    }
                } else {
                    long workedhours = (DateUtilities.getTimeDiffInSeconds(intime, outtime));//employee's total work in seconds
                    long workhourLimit = (DateUtilities.getTimeDiffInSeconds("00:00:00", workhorlimit));//shift time period in seconds
                    long overtimelimitinsec = (DateUtilities.getTimeDiffInSeconds("00:00:00", overtimelimit));
                    System.out.println("workedhours : " + workedhours + " workhourLimit : " + workhourLimit);
                    if ((workedhours) > workhourLimit) {//checking for overtime
                        long extrawork = workedhours - workhourLimit;
                        String extra_work = (((extrawork) / 3600) + ":" + (((extrawork) % 3600) / 60) + ":" + (extrawork % 60));
                        System.out.println("extra_work : " + extra_work);
                        if (extrawork > overtimelimitinsec) {
                            txtExtrawrk.setText(extra_work);
                            status = tempstatus + "*";
                        } else {
                            txtExtrawrk.setText("");
                        }
                    }
                }
                txtstatus.setText(status);
            } catch (ParseException ex) {
                MyUtils.showMessage("Calculate Shift Details : " + ex);
            }
        }
    }

    private void getEmpCategoryDetails() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ExceptionEntryServlet");
            myHTTP.openOS();
            myHTTP.println("getEmpCategoryDetails");
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer dp = new Depacketizer(result);
                int size = dp.getInt();

                if (size != 0) {
                    overtimelimit = dp.getString();
                    compofflimit = dp.getString();
                    gracelatetime = dp.getString();
                    graceEarlytime = dp.getString();
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("getEmpCategoryDetails", ex);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        calculateShiftDetails();
    }

    private String generateAppId() {
        String tempAppID = "E" + DateUtilities.getCurrentDate() + DateUtilities.getCurrentTime() + SimpleUtilities.generateRandomNumber();
        return tempAppID;
    }

    private String createPacket(String command, String appId) {
        Packetizer p = new Packetizer();
        try {
            switch (command) {
                case "Add":
                    p.addString(generateAppId());
                    break;
                case "update":
                    p.addString(appId);
                    break;
            }
            p.addString(cmboShift.getSelectedItem().toString());
            p.addString(DateUtilities.getSpinnerTime(DTPIn));
            p.addString(DateUtilities.getSpinnerTime(DTPOut));
            p.addString(txtWorkHour.getText());
            p.addString(txtExtrawrk.getText());
            p.addString(txtpreIn.getText());
            p.addString(txtpreOut.getText());
            p.addString(txtpreWrkHr.getText());
            p.addString(txtpreExtrawork.getText());
            p.addString(txtLate.getText());
            p.addString(txtEarly.getText());
            p.addString(txtstatus.getText());
            p.addString(txtRemark.getText());
            p.addString(txtpreLate.getText());
            p.addString(txtpreEarly.getText());
            p.addString(txtprestatus.getText());
        } catch (Exception ex) {
            MyUtils.showMessage("createPacket : " + ex);
        }
        return p.getPacket();
    }

    private void addExceptionEntry() {
        String packet = createPacket("Add", "");
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ExceptionEntryServlet");
            myHTTP.openOS();
            myHTTP.println("addExceptionEntry");
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            }
        } catch (Exception ex) {
            MyUtils.showException("addExceptionEntry", ex);
        }
    }

    private void updateExceptionEntry(String appID) {
        String packet = createPacket("update", appID);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ExceptionEntryServlet");
            myHTTP.openOS();
            myHTTP.println("updateExceptionEntry");
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            }
        } catch (Exception ex) {
            MyUtils.showException("updateExceptionEntry", ex);
        }
    }

    private void addChangeListener() {
        DTPIn.addChangeListener(this);
        DTPOut.addChangeListener(this);
    }

    private void addAcctionListener() {
        cmboShift.addActionListener(this);
        cmboShift.setActionCommand("SelectShift");
    }
}
