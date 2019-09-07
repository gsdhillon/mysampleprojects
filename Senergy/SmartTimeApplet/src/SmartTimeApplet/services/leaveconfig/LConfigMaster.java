/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.leaveconfig;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author nbpatil
 */
public class LConfigMaster extends MyPanel implements ActionListener {

    private MyButton btnAdd;
    private MyButton btnCancel;
    private MyTextField txtLCode;
    private MyTextField txtLDesc;
    private MyTextField txtLvNm;
    private MyTextField txtMaxAllowedDays;
    private MyTextField txtEL;
    private MyTextField txtCL;
    private MyTextField txtML;
    private MyTextField txtSCL;
    private MyTextField txtOL;
    private MyTextField txtTour;
    private MyTextField txtHPL;
    private MyTextField txtMaxNoTimeAllowed;
    private MyTextField txtMaxDaysAtTime;
    private MyTextField txtHalfCL;
    private MyTextField txtRemark;
    private MyTextField txtMxDyAccum;
    private JCheckBox chkAccumuln;
    private JCheckBox chkEncase;
    private JCheckBox chkNegativeBal;
    private JCheckBox chkWOffL;
    private JCheckBox chkHolidayL;
    private JComboBox cmbLeave;
    private LeaveConfig LConfigApplet;
    private String Job;
    private String LeaveCode;

    public LConfigMaster(LeaveConfig LConfig, String Job, String LeaveCode) {
        try {
            this.LConfigApplet = LConfig;
            this.Job = Job;
            this.LeaveCode = LeaveCode;
            addLConfigPanel();
            addActionListener();
            if (Job.equals("update")) {
                FillForm();
            }
        } catch (Exception ex) {
            MyUtils.showException("LConfigMaster", ex);
        }
    }

    private void addActionListener() {
        chkAccumuln.addActionListener(this);
        chkAccumuln.setActionCommand("Accumulation");
    }

    private void FillForm() throws Exception {
        txtLCode.setEditable(false);
        MyHTTP myHTTP = MyUtils.createServletConnection("LConfigFormServlet");
        myHTTP.openOS();
        myHTTP.println("FormDetails");
        myHTTP.println(LeaveCode);
        myHTTP.closeOS();
        myHTTP.openIS();
        String result = myHTTP.readLine();
        myHTTP.closeIS();
        if (result.startsWith("ERROR")) {
            MyUtils.showException("Database Query", new Exception(result));
        } else {
            Depacketizer d = new Depacketizer(result);
            txtLCode.setText(d.getString());
            txtLvNm.setText(d.getString());
            txtLDesc.setText(d.getString());
            txtEL.setText(d.getString());
            txtCL.setText(d.getString());
            txtML.setText(d.getString());
            txtSCL.setText(d.getString());
            txtHPL.setText(d.getString());
            d.getString();
            txtTour.setText(d.getString());
            d.getString();
            d.getString();
            txtOL.setText(d.getString());
            txtMaxAllowedDays.setText(d.getString());
            txtMaxNoTimeAllowed.setText(d.getString());
            txtMaxDaysAtTime.setText(d.getString());
            chkAccumuln.setSelected(biz(d.getString()));
            txtMxDyAccum.setText(d.getString());
            d.getString();
            chkEncase.setSelected(biz(d.getString()));
            d.getString();
            d.getString();
            chkWOffL.setSelected(biz(d.getString()));
            chkHolidayL.setSelected(biz(d.getString()));
            chkNegativeBal.setSelected(biz(d.getString()));
            d.getString();
            cmbLeave.setSelectedItem(d.getString());
            txtRemark.setText(d.getString());
        }
    }

    private String foo(JCheckBox chk) {
        if (chk.isSelected()) {
            return "1";
        } else {
            return "0";
        }
    }

    private boolean biz(String a) {
        if (a.equals("1")) {
            return true;
        } else {
            return false;
        }

    }

    public boolean FormFilled() {
        if (txtLCode.getText().equals("") || txtLDesc.getText().equals("") || txtLvNm.getText().equals("") || txtRemark.getText().equals("")) {
            MyUtils.showMessage("Form Not Filled");
            return false;
        }

        return true;
    }

    public String CreatePacket() throws Exception {
        Packetizer a = new Packetizer();
        a.addString(txtLCode.getText());
        a.addString(txtLvNm.getText());
        a.addString(txtLDesc.getText());
        a.addString(txtEL.getText());
        a.addString(txtCL.getText());
        a.addString(txtML.getText());
        a.addString(txtSCL.getText());
        a.addString(txtHPL.getText());
        a.addString("0");
        a.addString(txtTour.getText());
        a.addString("0");
        a.addString("0");
        a.addString(txtOL.getText());
        a.addString(txtMaxAllowedDays.getText());
        a.addString(txtMaxNoTimeAllowed.getText());
        a.addString(txtMaxDaysAtTime.getText());
        a.addString(foo(chkAccumuln));
        a.addString(txtMxDyAccum.getText());
        a.addString("0");
        a.addString(foo(chkEncase));
        a.addString("0");
        a.addString("0");
        a.addString(foo(chkWOffL));
        a.addString(foo(chkHolidayL));
        a.addString(foo(chkNegativeBal));
        a.addString("0");
        a.addString((String) cmbLeave.getSelectedItem());
        a.addString(txtRemark.getText());


        return a.getPacket();

    }

    private void addLeaveConfig() {
        if (FormFilled()) {
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("LConfigFormServlet");
                myHTTP.openOS();
                myHTTP.println("AddLConfig");
                myHTTP.println(CreatePacket());
                myHTTP.closeOS();
                myHTTP.openIS();
                String Result = myHTTP.readLine();
                myHTTP.closeIS();
                if (Result.startsWith("ERROR")) {
                    MyUtils.showMessage(Result);
                } else if (Result.equals("Inserted")) {
                    LConfigApplet.showHomePanel();
                }
            } catch (Exception ex) {
                MyUtils.showException("Add Leave Config", ex);
            }
        }
    }

    private void updateLeaveConfig() {
        if (FormFilled()) {
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("LConfigFormServlet");
                myHTTP.openOS();
                myHTTP.println("UpdateLConfig");
                myHTTP.println(CreatePacket());
                myHTTP.closeOS();
                myHTTP.openIS();
                String Result = myHTTP.readLine();
                myHTTP.closeIS();
                if (Result.startsWith("ERROR")) {
                    MyUtils.showMessage(Result);
                } else if (Result.equals("Updated")) {
                    LConfigApplet.showHomePanel();
                }
            } catch (Exception ex) {
                MyUtils.showException("Update Leave Config", ex);
            }
        }
    }

    private void addLConfigPanel() {
        Dimension screenSize = LConfigApplet.getSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setSize(width - 430, height - 100);

        MyPanel MainPanel = new MyPanel(new BorderLayout());

        MyPanel panNorth = new MyPanel(new GridLayout(1, 2, 10, 2), "Leave Code");
        panNorth.setPreferredSize(new Dimension(screenSize.width, screenSize.height / 8));
        MyPanel panLCode = new MyPanel(new BorderLayout());
        MyPanel panlblCode = new MyPanel(new GridLayout(2, 1));
        MyLabel lblLCode = new MyLabel(1, "Leave Code :");
        panlblCode.add(lblLCode);

        MyLabel lblLDesc = new MyLabel(1, "Leave Description :");
        panlblCode.add(lblLDesc);
        panLCode.add(panlblCode, BorderLayout.WEST);

        MyPanel pantxtCode = new MyPanel(new GridLayout(2, 1));
        txtLCode = new MyTextField();
        txtLCode.setPreferredSize(new Dimension(35, 15));
        pantxtCode.add(txtLCode);

        txtLDesc = new MyTextField();
        pantxtCode.add(txtLDesc);

        panLCode.add(pantxtCode, BorderLayout.CENTER);
        panNorth.add(panLCode);

        MyPanel panLvNm = new MyPanel(new BorderLayout());
        MyPanel panlblLvNm = new MyPanel(new GridLayout(2, 1));

        MyLabel lblLvNm = new MyLabel(1, "Leave Name :");
        panlblLvNm.add(lblLvNm);

        MyLabel lblfill = new MyLabel();
        panlblLvNm.add(lblfill);

        panLvNm.add(panlblLvNm, BorderLayout.WEST);
        MyPanel pantxtLvNm = new MyPanel(new GridLayout(2, 1));

        txtLvNm = new MyTextField();
        pantxtLvNm.add(txtLvNm);

        panLvNm.add(pantxtLvNm, BorderLayout.CENTER);
        panNorth.add(panLvNm);

        MainPanel.add(panNorth, BorderLayout.NORTH);


        MyPanel panMainCenter = new MyPanel(new BorderLayout(), "Configure Leave");

        MyPanel panlblConfigL = new MyPanel(new GridLayout(10, 1));

        MyLabel lblEL = new MyLabel(1, "Maximum No of EL Allowed in year:");
        panlblConfigL.add(lblEL);

        MyLabel lblCL = new MyLabel(1, "Maximum No of CL Allowed in year:");
        panlblConfigL.add(lblCL);

        MyLabel lblML = new MyLabel(1, "Maximum No of ML Allowed in year:");
        panlblConfigL.add(lblML);

        MyLabel lblSCL = new MyLabel(1, "Maximum No of SCL Allowed in year:");
        panlblConfigL.add(lblSCL);

        MyLabel lblOL = new MyLabel(1, "Maximum No of Other Leave Allowed in year:");
        panlblConfigL.add(lblOL);

        MyLabel lblTour = new MyLabel(1, "Maximum No of Tour Allowed in year:");
        panlblConfigL.add(lblTour);

        MyLabel lblHPL = new MyLabel(1, "Maximum No of HPL Allowed in year:");
        panlblConfigL.add(lblHPL);

        MyLabel lblTDays = new MyLabel(1, "Total No of Days Allowed in year:");
        panlblConfigL.add(lblTDays);

        MyLabel lblTime = new MyLabel(1, "Maximum No of Time Allowed in year:");
        panlblConfigL.add(lblTime);

        MyLabel lblDaysAtTime = new MyLabel(1, "Maximum No of Days of Leave Allowed at a time:");
        panlblConfigL.add(lblDaysAtTime);

        panMainCenter.add(panlblConfigL, BorderLayout.WEST);

        MyPanel pantxtConfigL = new MyPanel(new BorderLayout());

        MyPanel pantxtConfigL1 = new MyPanel(new GridLayout(10, 1));

        txtEL = new MyTextField(5);
        txtEL.setText("0");
        pantxtConfigL1.add(txtEL);

        txtCL = new MyTextField("0");
        pantxtConfigL1.add(txtCL);

        txtML = new MyTextField("0");
        pantxtConfigL1.add(txtML);

        txtSCL = new MyTextField("0");
        pantxtConfigL1.add(txtSCL);

        txtOL = new MyTextField("0");
        pantxtConfigL1.add(txtOL);

        txtTour = new MyTextField("0");
        pantxtConfigL1.add(txtTour);

        txtHPL = new MyTextField("0");
        pantxtConfigL1.add(txtHPL);

        txtMaxAllowedDays = new MyTextField("0");
        pantxtConfigL1.add(txtMaxAllowedDays);

        txtMaxNoTimeAllowed = new MyTextField("0");
        pantxtConfigL1.add(txtMaxNoTimeAllowed);

        txtMaxDaysAtTime = new MyTextField("0");
        pantxtConfigL1.add(txtMaxDaysAtTime);
        pantxtConfigL.add(pantxtConfigL1, BorderLayout.WEST);

        MyPanel pantxtConfigL2 = new MyPanel(new GridLayout(10, 1));
        MyLabel lblfill21 = new MyLabel();
        pantxtConfigL2.add(lblfill21);

        MyLabel lblfill22 = new MyLabel();
        pantxtConfigL2.add(lblfill22);

        MyLabel lblfill23 = new MyLabel();
        pantxtConfigL2.add(lblfill23);

        MyPanel panlbltxtHalfCL = new MyPanel(new GridLayout(1, 2));
        MyLabel lblHalfCL = new MyLabel(1, "Half CL: ");
        panlbltxtHalfCL.add(lblHalfCL);

        txtHalfCL = new MyTextField("0");
        panlbltxtHalfCL.add(txtHalfCL);
        pantxtConfigL2.add(panlbltxtHalfCL);

        MyPanel panlbltxtRemark = new MyPanel(new GridLayout(1, 2));
        MyLabel lblRemark = new MyLabel(1, "Remark: ");
        panlbltxtRemark.add(lblRemark);

        txtRemark = new MyTextField();
        panlbltxtRemark.add(txtRemark);
        pantxtConfigL2.add(panlbltxtRemark);

        pantxtConfigL.add(pantxtConfigL2, BorderLayout.EAST);
        panMainCenter.add(pantxtConfigL, BorderLayout.CENTER);
        MainPanel.add(panMainCenter, BorderLayout.CENTER);

        MyPanel panMainSouth = new MyPanel(new BorderLayout());
        MyPanel panAccumul = new MyPanel(new FlowLayout(FlowLayout.CENTER, 20, 0), ".");
        MyPanel panlblAccEnc = new MyPanel(new GridLayout(2, 1));
        MyLabel lblAccum = new MyLabel(1, "Accumulation Allowed: ");
        panlblAccEnc.add(lblAccum);

        MyLabel lblEnca = new MyLabel(1, "Encasement Allowed: ");
        panlblAccEnc.add(lblEnca);
        panAccumul.add(panlblAccEnc);

        MyPanel panchkAccum = new MyPanel(new GridLayout(2, 1));
        chkAccumuln = new JCheckBox();
        panchkAccum.add(chkAccumuln);

        chkEncase = new JCheckBox();
        panchkAccum.add(chkEncase);
        panAccumul.add(panchkAccum);

        MyPanel panlblNegative = new MyPanel(new GridLayout(2, 1));
        MyLabel lblMxDyAccum = new MyLabel(1, "Maximum Days for Accumulation: ");
        panlblNegative.add(lblMxDyAccum);

        MyLabel lblNegativeBal = new MyLabel(1, "Negative Balance Allowed: ");
        panlblNegative.add(lblNegativeBal);
        panAccumul.add(panlblNegative);

        MyPanel pantxtchkNegve = new MyPanel(new GridLayout(2, 1));
        txtMxDyAccum = new MyTextField(3);
        txtMxDyAccum.setText("0");
        txtMxDyAccum.setEnabled(false);
        pantxtchkNegve.add(txtMxDyAccum);

        chkNegativeBal = new JCheckBox();
        pantxtchkNegve.add(chkNegativeBal);
        panAccumul.add(pantxtchkNegve);
        panMainSouth.add(panAccumul, BorderLayout.NORTH);

        MyPanel panLeave = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        panLeave.setPreferredSize(new Dimension(screenSize.width, screenSize.height / 8));
        MyPanel panLeaveCenter = new MyPanel(new GridLayout(2, 2));

        MyPanel panlblLeave = new MyPanel(new GridLayout(2, 1));
        MyLabel lblWOffL = new MyLabel(1, "If Weekoff falls inBetween Leave: ");
        panlblLeave.add(lblWOffL);

        MyLabel lblHolidayL = new MyLabel(1, "If Holiday falls inBetween Leave: ");
        panlblLeave.add(lblHolidayL);
        panLeaveCenter.add(panlblLeave);

        MyPanel panchkLeave = new MyPanel(new GridLayout(2, 1));
        chkWOffL = new JCheckBox();
        panchkLeave.add(chkWOffL);

        chkHolidayL = new JCheckBox();
        panchkLeave.add(chkHolidayL);
        panLeaveCenter.add(panchkLeave);

        MyPanel panConsiderL = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        MyLabel lblConsiderL = new MyLabel(1, "Should be considered as");
        panConsiderL.add(lblConsiderL);

        cmbLeave = new JComboBox();
        cmbLeave.addItem("On Leave");
        cmbLeave.addItem("Week Off");
        cmbLeave.addItem("Holiday");
        cmbLeave.addItem("Absent");
        panConsiderL.add(cmbLeave);

        panLeaveCenter.add(panConsiderL);
        panLeave.add(panLeaveCenter);
        panMainSouth.add(panLeave, BorderLayout.CENTER);

        //adding Buttons
        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Add Shift Details");
        panButtons.setForeground(new Color(103, 213, 83));
        panButtons.setBackground(new Color(255, 255, 255));
        if (Job.equals("add")) {
            btnAdd = new MyButton("Add LeaveConfig", 2, Color.WHITE) {

                @Override
                public void onClick() {
                    addLeaveConfig();
                }
            };
        } else if (Job.equals("update")) {
            btnAdd = new MyButton("Update LeaveConfig", 2, Color.WHITE) {

                @Override
                public void onClick() {
                    updateLeaveConfig();
                }
            };
        }
        btnAdd.setForeground(Color.GREEN);
        panButtons.add(btnAdd);

        btnCancel = new MyButton("Cancel", 2, Color.WHITE) {

            @Override
            public void onClick() {
                LConfigApplet.showHomePanel();
            }
        };
        btnCancel.setForeground(new Color(103, 213, 83));
        panButtons.add(btnCancel);

        panMainSouth.add(panButtons, BorderLayout.SOUTH);
        panMainCenter.add(panMainSouth, BorderLayout.SOUTH);
        this.setLayout(new GridLayout(1, 1));
        this.add(MainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Accumulation")) {
            if (chkAccumuln.isSelected()) {
                txtMxDyAccum.setEnabled(true);
            } else {
                txtMxDyAccum.setEnabled(false);
            }
        }
    }
}
