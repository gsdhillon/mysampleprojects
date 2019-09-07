/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.employee;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import lib.Utility.SimpleUtilities;
import lib.gui.*;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponent;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 *
 */
public class EmployeeMaster extends MyPanel implements ActionListener {

    private MyTextField txtCCNo;
    private MyTextField txtEmpCode;
    private MyTextField txtEmpNm;
    private JComboBox cmbDesig, cmbPost;
    private JComponent dob;
    private JComponent doj;
    private JComponent dol;
    private JComboBox cmbDivCode;
    private MyTextField txtDivNm;
    private MyTextField txtDivHead;
    private JComboBox cmbCatCode, cmbAccessLevel;
    private MyTextField txtCatNm;
    private MyTextField txtReport;
    private MyButton btnAdd;
    private MyButton btnCancel;
    private MyTextField txtSecHead;
    private JComboBox cmbSecNm;
    private JComboBox cmbWorkL;
    private MyTextArea taAdd;
    private MyTextField txtBalLeave;
    private JComboBox cmbLCode;
    private JComboBox cmbSex;
    private JComboBox cmbDShift;
    private JComboBox cmbSSwipe;
    private JComboBox cmbWOff1;
    private JComboBox cmbWOff2;
    private Employee EmployeeApplet;
    private String Job;
    private String EmpCode;
    private MyPanel panEmpDetail;
    JCheckBox chkBalLeave;

    public EmployeeMaster(Employee EmployeeApplet, String Job, String EmpCode) {
        try {
            this.EmployeeApplet = EmployeeApplet;
            this.Job = Job;
            this.EmpCode = EmpCode;
            addEmployeePanel();
            initialDetails();
            selectionEvents();
            if (Job.equals("update")) {
                FillForm();
            }
        } catch (Exception ex) {
            MyUtils.showException("Employee Master", ex);
        }

    }

    private void initialDetails() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("EmployeeFormServlet");
            myHTTP.openOS();
            myHTTP.println("InitValues");
            myHTTP.closeOS();
            myHTTP.openIS();

            String catcode = myHTTP.readLine();
            if (catcode.startsWith("ERROR")) {
                MyUtils.showMessage(catcode);
                return;
            }
            Depacketizer dp = new Depacketizer(catcode);
            while (!dp.isEmpty()) {
                cmbCatCode.addItem(dp.getString());
            }

            String divcode = myHTTP.readLine();
            if (divcode.startsWith("ERROR")) {
                MyUtils.showMessage(divcode);
                return;
            }
            dp = new Depacketizer(divcode);
            while (!dp.isEmpty()) {
                cmbDivCode.addItem(dp.getString());
            }

            String WLocation = myHTTP.readLine();
            if (WLocation.startsWith("ERROR")) {
                MyUtils.showMessage(WLocation);
                return;
            }
            dp = new Depacketizer(WLocation);
            while (!dp.isEmpty()) {
                cmbWorkL.addItem(dp.getString());
            }

            String Shift = myHTTP.readLine();
            if (Shift.startsWith("ERROR")) {
                MyUtils.showMessage(Shift);
                return;
            }
            dp = new Depacketizer(Shift);
            while (!dp.isEmpty()) {
                cmbDShift.addItem(dp.getString());
            }

            String Leave = myHTTP.readLine();
            if (Leave.startsWith("ERROR")) {
                MyUtils.showMessage(Leave);
                return;
            }
            dp = new Depacketizer(Leave);
            while (!dp.isEmpty()) {
                cmbLCode.addItem(dp.getString());
            }

            String designation = myHTTP.readLine();
            if (designation.startsWith("ERROR")) {
                MyUtils.showMessage(designation);
                return;
            }
            dp = new Depacketizer(designation);
            while (!dp.isEmpty()) {
                cmbDesig.addItem(dp.getString());
            }

            String post = myHTTP.readLine();
            if (post.startsWith("ERROR")) {
                MyUtils.showMessage(post);
                return;
            }
            dp = new Depacketizer(post);
            while (!dp.isEmpty()) {
                cmbPost.addItem(dp.getString());
            }
            myHTTP.closeIS();
        } catch (Exception ex) {
            MyUtils.showException("Initial Details", ex);
        }

    }

    private void FillForm() {
        try {
            txtEmpCode.setEditable(false);

            MyHTTP myHTTP = MyUtils.createServletConnection("EmployeeFormServlet");
            myHTTP.openOS();
            myHTTP.println("FormDetails");
            myHTTP.println(EmpCode);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showException("Database Query", new Exception(result));
            } else {
                setValues(result);
            }
        } catch (Exception ex) {
            MyUtils.showException("Fill Form", ex);
        }
    }

    private void setValues(String Packet) {
        try {
            Depacketizer dp = new Depacketizer(Packet);
            txtEmpNm.setText(dp.getString());
            txtEmpCode.setText(dp.getString());
            txtEmpCode.setEditable(false);
            txtCCNo.setText(dp.getString());
            switch (dp.getString()) {
                case "1":
                    cmbSex.setSelectedItem("Male");
                    break;
                case "2":
                    cmbSex.setSelectedItem("Female");
                    break;
                default:
                    cmbSex.setSelectedIndex(0);
                    break;
            }
            String dt1 = dp.getString();
            if ((dt1 != null) & (!"".equals(dt1))) {
                MyUtils.setDate(dob, dt1);
            }
            String dt2 = dp.getString();
            if ((dt2 != null) & (!"".equals(dt2))) {
                MyUtils.setDate(dol, dt2);
            }
            String dt3 = dp.getString();
            if ((dt3 != null) & (!"".equals(dt3))) {
                MyUtils.setDate(doj, dt3);
            }
            txtDivNm.setText(dp.getString());
            String desigcode = dp.getString();
            String designame = dp.getString();
            cmbDesig.setSelectedItem(desigcode + " " + designame);
            String WLcode = dp.getString();
            String strworkloc = dp.getString();
            cmbWorkL.setSelectedItem(WLcode + " " + strworkloc);
            String accesslevel = dp.getString();
            if ((!"".equals(accesslevel)) & (accesslevel != null)) {
                cmbAccessLevel.setSelectedIndex(Integer.parseInt(accesslevel));
            } else {
                cmbAccessLevel.setSelectedIndex(0);
            }
            cmbDivCode.setSelectedItem(dp.getString());
            String SecNm = dp.getString();
            for (int i = 0; i < cmbSecNm.getItemCount(); i++) {
                if (((String) cmbSecNm.getItemAt(i)).startsWith(SecNm)) {
                    cmbSecNm.setSelectedIndex(i);
                    break;
                }
            }
            cmbDShift.setSelectedItem(dp.getString());
            String strcatcode = dp.getString();
            if (!"".equals(strcatcode) || strcatcode != null) {
                cmbCatCode.setSelectedItem(strcatcode);
            } else {
                cmbCatCode.setSelectedIndex(0);
            }

            String strleavecode = dp.getString();
            if (!"".equals(strleavecode) || strleavecode != null) {
                cmbLCode.setSelectedItem(strleavecode);
            } else {
                cmbLCode.setSelectedIndex(0);
            }

            String woff1 = dp.getString();
            if ("".equals(woff1)) {
                cmbWOff1.setSelectedIndex(0);
            } else {
                cmbWOff1.setSelectedItem(MyUtils.weekDay(Integer.parseInt(woff1)));
            }
            String woff2 = dp.getString();
            if ("".equals(woff2)) {
                cmbWOff2.setSelectedIndex(0);
            } else {
                cmbWOff2.setSelectedItem(MyUtils.weekDay(Integer.parseInt(woff2)));
            }
            String singleSw = dp.getString();
//            MyUtils.showMessage("swa :" + singleSw);
            if (!"".equals(singleSw) && singleSw != null) {
                switch (singleSw) {
                    case "0":
                        cmbSSwipe.setSelectedItem("Select Yes/No");
                        break;
                    case "1":
                        cmbSSwipe.setSelectedItem("No");
                        break;
                    case "2":
                        cmbSSwipe.setSelectedItem("Yes");
                        break;
                }
            } else {
                cmbSSwipe.setSelectedItem("Select Yes/No");
            }
            txtBalLeave.setText(dp.getString());
            String bal = txtBalLeave.getText();
            int preballeave;
            if ((!"".equals(bal)) & (bal != null)) {
                preballeave = Integer.parseInt(bal);
            } else {
                preballeave = 0;
            }
            if (preballeave > 0) {
                chkBalLeave.setSelected(true);
            } else {
                chkBalLeave.setSelected(false);
            }
            txtReport.setText(dp.getString());
            int postcode = Integer.parseInt(dp.getString());
            String poststatus = dp.getString();
            cmbPost.setSelectedItem(postcode + " " + poststatus);
            taAdd.setText(dp.getString());
        } catch (Exception ex) {
            MyUtils.showException("setValues ", ex);
        }
    }

    private void selectionEvents() {
        cmbDivCode.addActionListener(this);
        cmbDivCode.setActionCommand("DivCode");
        cmbCatCode.addActionListener(this);
        cmbCatCode.setActionCommand("CatCode");
        cmbSecNm.addActionListener(this);
        cmbSecNm.setActionCommand("SecName");

    }

    private boolean FormFilled() {

        if (txtEmpNm.getText().equals("")) {
            MyUtils.showMessage("Enter Employee Name");
            return false;
        }
        if (txtEmpCode.getText().equals("")) {
            MyUtils.showMessage("Enter Emplyee Code");
            return false;
        }
        if (cmbDShift.getSelectedItem().equals("Select Default Shift")) {
            MyUtils.showMessage("Select shift");
            return false;
        }
//        if (txtCCNo.getText().equals("")) {
//            MyUtils.showMessage("Enter CC no.");
//            return false;
//        }

        if (cmbDivCode.getSelectedItem().equals("Select Division Code")) {
            MyUtils.showMessage("Division Code not Selected");
            return false;
        }
//        if (cmbSSwipe.getSelectedItem().equals("Select Yes/No")) {
//            MyUtils.showMessage("Swipe not Selected");
//            return false;
//        }
        DateModel model1 = ((JDateComponent) (dob)).getModel();
        if ((!model1.isSelected())) {
            MyUtils.showMessage("Date Of Birth not Selected");
            return false;
        }
        DateModel model2 = ((JDateComponent) (doj)).getModel();
        if ((!model2.isSelected())) {
            MyUtils.showMessage("Date Of Joining not Selected");
            return false;
        }
        DateModel model3 = ((JDateComponent) (dol)).getModel();
        if ((!model3.isSelected())) {
            MyUtils.showMessage("Date Of Leaving not Selected");
            return false;
        }
        return true;
    }

    private String CreatePacket() {
        Packetizer a = new Packetizer();
        try {
            a.addString(txtCCNo.getText());
            a.addString(txtEmpCode.getText());
            a.addString(txtEmpNm.getText());
            String desig[] = (cmbDesig.getSelectedItem().toString()).split(" ");
            a.addString(desig[0]);
            if (cmbSex.getSelectedItem().equals("Male")) {
                a.addInt(1);
            } else if (cmbSex.getSelectedItem().equals("Female")) {
                a.addInt(2);
            } else {
                a.addInt(0);
            }
            String month, day;
            DateModel<?> model = ((JDateComponent) (dob)).getModel();
            month = (model.getMonth() + 1) < 10 ? "0" + (model.getMonth() + 1) : "" + (model.getMonth() + 1);
            day = model.getDay() < 10 ? "0" + model.getDay() : "" + model.getDay();
            String date = model.getYear() + "-" + month + "-" + day;
            a.addString(date);

            model = ((JDateComponent) (doj)).getModel();
            month = (model.getMonth() + 1) < 10 ? "0" + (model.getMonth() + 1) : "" + (model.getMonth() + 1);
            day = model.getDay() < 10 ? "0" + model.getDay() : "" + model.getDay();
            date = model.getYear() + "-" + month + "-" + day;
            a.addString(date);

            model = ((JDateComponent) (dol)).getModel();
            month = (model.getMonth() + 1) < 10 ? "0" + (model.getMonth() + 1) : "" + (model.getMonth() + 1);
            day = model.getDay() < 10 ? "0" + model.getDay() : "" + model.getDay();
            date = model.getYear() + "-" + month + "-" + day;
            a.addString(date);

            a.addString(txtDivNm.getText());
            String cmbworkloc = cmbWorkL.getSelectedItem().toString();
            String workloc[] = cmbworkloc.split(" ");
            a.addString(workloc[0]);
            a.addString((String) cmbDivCode.getSelectedItem());
            a.addString(((String) cmbSecNm.getSelectedItem()).substring(0, 5));
            a.addString((String) cmbDShift.getSelectedItem());

            String strcatcode = cmbCatCode.getSelectedItem().toString();
            if (!"Select Category Code".equals(strcatcode)) {
                a.addString(strcatcode);
            } else {
                a.addString("");
            }
            String strlcode = cmbLCode.getSelectedItem().toString();
            if (!"Select Leave Code".equals(strlcode)) {
                a.addString(strlcode);
            } else {
                a.addString("");
            }
            a.addInt(MyUtils.weekIndex((String) cmbWOff1.getSelectedItem()));
            a.addInt(MyUtils.weekIndex((String) cmbWOff2.getSelectedItem()));
            if (cmbSSwipe.getSelectedItem().equals("No")) {
                a.addInt(1);
            } else if (cmbSSwipe.getSelectedItem().equals("Yes")) {
                a.addInt(2);
            } else {
                a.addInt(1);
            }
            a.addString(taAdd.getText());
            a.addString(txtReport.getText());
            if (!"".equals(txtBalLeave.getText())) {
                a.addInt(Integer.parseInt(txtBalLeave.getText()));
            } else {
                a.addInt(0);
            }
            a.addInt(cmbAccessLevel.getSelectedIndex());
            String post[] = (cmbPost.getSelectedItem().toString()).split(" ");
            a.addInt(Integer.parseInt(post[0]));
        } catch (Exception ex) {
            MyUtils.showException("CreatePacket", ex);
        } finally {
            return a.getPacket();
        }

    }

    private void addEmployee() {
        if (FormFilled()) {
            String Packet = CreatePacket();
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("EmployeeFormServlet");
                myHTTP.openOS();
                myHTTP.println("AddEmployee");
                myHTTP.println(Packet);
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                if (result.equals("Inserted")) {
                    EmployeeApplet.showHomePanel();
                } else {
                    MyUtils.showMessage(result);
                }
            } catch (Exception ex) {
                MyUtils.showException("add Employee", ex);
            }

        }
    }

    private void updateEmployee() {
        if (FormFilled()) {
            try {
                String Packet = CreatePacket();

                MyHTTP myHTTP = MyUtils.createServletConnection("EmployeeFormServlet");
                myHTTP.openOS();
                myHTTP.println("UpdateEmployee");
                myHTTP.println(Packet);
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                myHTTP.closeIS();
                if (result.equals("Updated")) {
                    EmployeeApplet.showHomePanel();
                } else {
                    MyUtils.showMessage(result);
                }
            } catch (Exception ex) {
                MyUtils.showException("Update Employee", ex);
            }
        }
    }

    public void addEmployeePanel() {

        MyPanel MainPanel = new MyPanel(new BorderLayout());
        panEmpDetail = new MyPanel(new GridLayout(1, 2, 20, 2), "Employee Details");
        panEmpDetail.setSize(EmployeeApplet.getWidth(), EmployeeApplet.getHeight() / 6);
        MyPanel panEmpG1 = new MyPanel(new GridLayout(4, 2, 0, 2));
        panEmpG1.setSize(EmployeeApplet.getWidth(), EmployeeApplet.getHeight() / 6);

        MyLabel lblCCNo = new MyLabel(1, "CC No : ");
        panEmpG1.add(lblCCNo);

        txtCCNo = new MyTextField();
        panEmpG1.add(txtCCNo);

        MyLabel lblEmpCode = new MyLabel(1, "Employee Code : ");
        panEmpG1.add(lblEmpCode);

        txtEmpCode = new MyTextField();
        panEmpG1.add(txtEmpCode);

        MyLabel lblEmpNm = new MyLabel(1, "Employee Name : ");
        panEmpG1.add(lblEmpNm);

        txtEmpNm = new MyTextField();
        panEmpG1.add(txtEmpNm);

        MyLabel lblempost = new MyLabel(1, "Post : ");
        panEmpG1.add(lblempost);

        cmbPost = new JComboBox();
        panEmpG1.add(cmbPost);

        panEmpDetail.add(panEmpG1);

        MyPanel panEmpG2 = new MyPanel(new GridLayout(4, 2, 20, 2));
        panEmpG2.setSize(EmployeeApplet.getWidth(), EmployeeApplet.getHeight() / 6);

        MyLabel lblDesig = new MyLabel(1, "Designation : ");
        panEmpG2.add(lblDesig);

        cmbDesig = new JComboBox();
        cmbDesig.setPreferredSize(new Dimension(50, 15));
        panEmpG2.add(cmbDesig);

        MyLabel lblDOB = new MyLabel(1, "Date Of Birth : ");
        panEmpG2.add(lblDOB);

        JDatePicker picker = JDateComponentFactory.createJDatePicker();
        dob = (JComponent) picker;
        panEmpG2.add(dob);

        MyLabel lblDOJ = new MyLabel(1, "Date Of Joining : ");
        panEmpG2.add(lblDOJ);

        JDatePicker picker2 = JDateComponentFactory.createJDatePicker();
        doj = (JComponent) picker2;
        panEmpG2.add(doj);

        MyLabel lblDOL = new MyLabel(1, "Date Of Leaving : ");
        panEmpG2.add(lblDOL);

        JDatePicker picker3 = JDateComponentFactory.createJDatePicker();
        dol = (JComponent) picker3;
        panEmpG2.add(dol);

        panEmpDetail.add(panEmpG2);
        MainPanel.add(panEmpDetail, BorderLayout.NORTH);

        MyPanel panStatus = new MyPanel();
        panStatus.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        MyLabel lblStatus = new MyLabel(1, "");
        panStatus.add(lblStatus);

        MainPanel.add(panStatus, BorderLayout.SOUTH);

        MyPanel panCenter = new MyPanel(new BorderLayout());

        MyPanel panDeptCat = new MyPanel(new GridLayout(1, 2, 20, 2), "Division Details");

        MyPanel panDept = new MyPanel(new GridLayout(3, 2, 20, 2));

        MyLabel lblDeptCode = new MyLabel(1, "Division Code : ");
        panDept.add(lblDeptCode);

        cmbDivCode = new JComboBox();
        cmbDivCode.addItem("Select Division Code");
        panDept.add(cmbDivCode);

        MyLabel lblDeptNm = new MyLabel(1, "Division Name : ");
        panDept.add(lblDeptNm);

        txtDivNm = new MyTextField();
        txtDivNm.setEditable(false);
        panDept.add(txtDivNm);

        MyLabel lblDeptHead = new MyLabel(1, "Division Head : ");
        panDept.add(lblDeptHead);

        txtDivHead = new MyTextField();
        txtDivHead.setEditable(false);
        panDept.add(txtDivHead);

        MyPanel panCat = new MyPanel(new GridLayout(3, 2, 20, 2));

        MyLabel lblCatCode = new MyLabel(1, "Category Code : ");
        panCat.add(lblCatCode);

        cmbCatCode = new JComboBox();
        cmbCatCode.addItem("Select Category Code");
        panCat.add(cmbCatCode);

        MyLabel lblCatNm = new MyLabel(1, "Category Name : ");
        panCat.add(lblCatNm);

        txtCatNm = new MyTextField();
        txtCatNm.setEditable(false);
        panCat.add(txtCatNm);

        MyLabel lblReport = new MyLabel(1, "Reporting to : ");
        panCat.add(lblReport);

        txtReport = new MyTextField();
        panCat.add(txtReport);

        panDeptCat.add(panDept);
        panDeptCat.add(panCat);
        panCenter.add(panDeptCat, BorderLayout.NORTH);

        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Edit Employee Details");
        if (Job.equals("add")) {
            btnAdd = new MyButton("Add Employee", 2, Color.WHITE) {

                @Override
                public void onClick() {
                    addEmployee();
                }
            };
        } else if (Job.equals("update")) {
            btnAdd = new MyButton("Update Employee", 2) {

                @Override
                public void onClick() {
                    updateEmployee();
                }
            };
        }
        panButtons.add(btnAdd);

        btnCancel = new MyButton("Cancel", 2) {

            @Override
            public void onClick() {

                EmployeeApplet.showHomePanel();
            }
        };
        panButtons.add(btnCancel);

        panCenter.add(panButtons, BorderLayout.SOUTH);

        MyPanel panSecNShift = new MyPanel(new BorderLayout());

        MyPanel panSection = new MyPanel(new GridLayout(1, 2, 20, 2), "Section Details");

        MyPanel panSectionG1 = new MyPanel(new GridLayout(2, 2, 20, 2));

        MyLabel lblSecNm = new MyLabel(1, "Section Name :");
        panSectionG1.add(lblSecNm);

        cmbSecNm = new JComboBox();
        cmbSecNm.addItem("First Select Division Code");
        panSectionG1.add(cmbSecNm);

        MyLabel lblWorkL = new MyLabel(1, "Work Location :");
        panSectionG1.add(lblWorkL);

        cmbWorkL = new JComboBox();
        cmbWorkL.addItem("Select Work Location");
        panSectionG1.add(cmbWorkL);

        panSection.add(panSectionG1);

        MyPanel panSectionG2 = new MyPanel(new GridLayout(2, 2, 20, 2));
        MyLabel lblSecHead = new MyLabel(1, "Section Head :");
        panSectionG2.add(lblSecHead);

        txtSecHead = new MyTextField();
        txtSecHead.setEditable(false);
        panSectionG2.add(txtSecHead);

        MyLabel lblFill = new MyLabel();
        panSectionG2.add(lblFill);
        panSection.add(panSectionG2);

        MyPanel panShift = new MyPanel(new BorderLayout(), "Shift Details");

        MyPanel panShiftWest = new MyPanel(new GridLayout(4, 2, 0, 2));

        MyLabel lblSex = new MyLabel(1, "Sex :");
        panShiftWest.add(lblSex);

        cmbSex = new JComboBox();
        cmbSex.addItem("Select Sex");
        cmbSex.addItem("Male");
        cmbSex.addItem("Female");
        panShiftWest.add(cmbSex);

        MyLabel lblDShift = new MyLabel(1, "Defualt Shift :");
        panShiftWest.add(lblDShift);

        cmbDShift = new JComboBox();
        cmbDShift.addItem("Select Default Shift");
        panShiftWest.add(cmbDShift);

        MyLabel lblSSwipe = new MyLabel(1, "Single Swipe Allowed :");
        panShiftWest.add(lblSSwipe);

        cmbSSwipe = new JComboBox();
        cmbSSwipe.addItem("Select Yes/No");
        cmbSSwipe.addItem("No");
        cmbSSwipe.addItem("Yes");
        panShiftWest.add(cmbSSwipe);

        MyLabel lblLCode = new MyLabel(1, "Leave Code :");
        panShiftWest.add(lblLCode);

        cmbLCode = new JComboBox();
        cmbLCode.addItem("Select Leave Code");
        panShiftWest.add(cmbLCode);

        MyPanel panShiftCenter = new MyPanel(new BorderLayout());

        MyPanel panRdrAcc = new MyPanel(new FlowLayout());
        cmbAccessLevel = new JComboBox(SimpleUtilities.fillReaderAccessCombo());
        cmbAccessLevel.setPreferredSize(new Dimension(150, 25));
        panRdrAcc.add(cmbAccessLevel);
        panShiftCenter.add(panRdrAcc, BorderLayout.NORTH);

        MyPanel panRemainLeave = new MyPanel();

        chkBalLeave = new JCheckBox("PreBal.Leave");
        panRemainLeave.add(chkBalLeave);

        txtBalLeave = new MyTextField(2);
        panRemainLeave.add(txtBalLeave);

        panShiftCenter.add(panRemainLeave, BorderLayout.SOUTH);


        MyPanel panShiftEast = new MyPanel(new GridLayout(2, 1, 0, 2));

        MyPanel panWOff = new MyPanel(new GridLayout(2, 2, 0, 2));

        MyLabel lblWOff1 = new MyLabel(1, "Week Off 1 :");
        panWOff.add(lblWOff1);

        cmbWOff1 = new JComboBox();
        addWeekDays(cmbWOff1);
        panWOff.add(cmbWOff1);

        MyLabel lblWOff2 = new MyLabel(1, "Week Off 2 :");

        panWOff.add(lblWOff2);

        cmbWOff2 = new JComboBox();
        addWeekDays(cmbWOff2);
        panWOff.add(cmbWOff2);

        MyPanel panAddress = new MyPanel(new GridLayout(1, 2));

        MyLabel lblAddrs = new MyLabel(1, "Address :");
        panAddress.add(lblAddrs);

        taAdd = new MyTextArea(2, 10);
        taAdd.setLineWrap(true);
        taAdd.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane a = new JScrollPane(taAdd);
        panAddress.add(a);

        panShiftEast.add(panWOff);
        panShiftEast.add(panAddress);


        panShift.add(panShiftWest, BorderLayout.WEST);
        panShift.add(panShiftCenter, BorderLayout.CENTER);
        panShift.add(panShiftEast, BorderLayout.EAST);

        panSecNShift.add(panSection, BorderLayout.NORTH);
        panSecNShift.add(panShift, BorderLayout.CENTER);

        panCenter.add(panSecNShift, BorderLayout.CENTER);
        MainPanel.add(panCenter, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(MainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void addWeekDays(JComboBox cmb) {
        cmb.addItem("none");
        cmb.addItem("Sunday");
        cmb.addItem("Monday");
        cmb.addItem("Tuesday");
        cmb.addItem("Wednesday");
        cmb.addItem("Thrusday");
        cmb.addItem("Friday");
        cmb.addItem("Saturday");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals("DivCode")) {

                String DivCode = (String) cmbDivCode.getSelectedItem();

                if (DivCode.equals("Select Division Code")) {
                    return;
                }
                MyHTTP myHTTP = MyUtils.createServletConnection("EmployeeFormServlet");
                myHTTP.openOS();
                myHTTP.println("DivCode");
                myHTTP.println(DivCode);
                myHTTP.closeOS();
                myHTTP.openIS();
                String r = myHTTP.readLine();
                myHTTP.closeIS();
                if (r.startsWith("ERROR")) {
                    MyUtils.showMessage(r);
                } else {
                    Depacketizer dp = new Depacketizer(r);
                    txtDivNm.setText(dp.getString());
                    txtDivHead.setText(dp.getString());
                    cmbSecNm.removeAllItems();
                    cmbSecNm.addItem("Select Section Name");
                    while (!dp.isEmpty()) {
                        cmbSecNm.addItem(dp.getString());

                    }
                }

            } else if (e.getActionCommand().equals("CatCode")) {

                String CatCode = (String) cmbCatCode.getSelectedItem();
                if (CatCode.equals("Select Category Code")) {
                    return;
                }
                MyHTTP myHTTP = MyUtils.createServletConnection("EmployeeFormServlet");
                myHTTP.openOS();
                myHTTP.println("CatCode");
                myHTTP.println(CatCode);
                myHTTP.closeOS();
                myHTTP.openIS();
                String r = myHTTP.readLine();
                myHTTP.closeIS();

                if (r.startsWith("ERROR")) {
                    MyUtils.showMessage(r);
                } else {
                    Depacketizer dp = new Depacketizer(r);
                    txtCatNm.setText(dp.getString());
                }
            } else if (e.getActionCommand().equals("SecName")) {
                getSectionHead();
            }
        } catch (Exception ex) {
            MyUtils.showException("ActionListener", ex);
        }
    }

    private void getSectionHead() {
        try {
            if (cmbSecNm.getItemCount() == 0) {
                return;
            }

            String SecName = (String) cmbSecNm.getSelectedItem();

            if (SecName.equals("Select Section Name") || SecName.equals("First Select Division Code")) {
                return;
            }
            String sec_code[] = SecName.split(" ");
            MyHTTP myHTTP = MyUtils.createServletConnection("EmployeeFormServlet");
            myHTTP.openOS();
            myHTTP.println("SecName");
            myHTTP.println(sec_code[0]);
            myHTTP.closeOS();
            myHTTP.openIS();
            String r = myHTTP.readLine();
            myHTTP.closeIS();
            txtSecHead.setText(r);

        } catch (Exception ex) {
            Logger.getLogger(EmployeeMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
