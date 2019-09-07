/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.LeaveApplication;

import SmartTimeApplet.COFFApplication.COFFTableModel;
import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import lib.Utility.DateUtilities;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 *
 * @author pradnya
 */
public class LeaveApplicationPanel extends MyPanel {

    JComponent ToDt, FromDt;
    MyTable tblCOFF;
    MyLabel lblecode, lblename,
            lblshowename, lblCCno,
            lblshoccno, lblDesig,
            lblShowDesig, lblShowDiv,
            lblpurpose, lblshowecode,
            lblrecCode, panshowrecode,
            lblrecName, lblappCode,
            lblshowappcode, lblappName,
            lblshowappname, panshowrecname;
    COFFTableModel tablemodel;
    String LeaveAppID;
    MyTextField txtpurpose, txtReason;
    String command1;
    LaeveApplicationApplet LaeveApplicationApplet1;
    MyButton btnAddMore, btnUpdate;
    JComboBox cmbLeaveType;
    MyLabel lblStatus;

    public LeaveApplicationPanel(LaeveApplicationApplet LaeveApplicationApplet, String cmd, String AppID) {
        this.setLayout(new BorderLayout());
        this.command1 = cmd;
        this.LeaveAppID = AppID;
        this.LaeveApplicationApplet1 = LaeveApplicationApplet;
        this.setPreferredSize(new Dimension(LaeveApplicationApplet1.getWidth(), LaeveApplicationApplet1.getHeight() - 100));
//        MyPanel panMain = new MyPanel(new BorderLayout());
        MyPanel panNorth = new MyPanel(new GridLayout(2, 1, 5, 10), "Employee Details : ");

        MyPanel pang1 = new MyPanel(new GridLayout());

        MyPanel panempcode = new MyPanel(new BorderLayout());
        lblecode = new MyLabel(1, "EmpCOde : ");
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
        this.add(panNorth, BorderLayout.NORTH);

        MyPanel panCenterCenter = new MyPanel(new BorderLayout(), "Leave Details");
        panCenterCenter.setSize(LaeveApplicationApplet.getWidth(), LaeveApplicationApplet.getHeight() / 10);
        MyPanel panlbl = new MyPanel(new GridLayout(4, 1, 1, 1));


        MyLabel lblLeaveType = new MyLabel(1, "Leave Type : ");
        panlbl.add(lblLeaveType);

        MyLabel lblReason = new MyLabel(1, "Reason :    ");
        panlbl.add(lblReason);

        MyLabel lblFromdt = new MyLabel(1, "From Date :    ");
        panlbl.add(lblFromdt);

        MyLabel lblTodt = new MyLabel(1, "To Date:          ");
        panlbl.add(lblTodt);
        panCenterCenter.add(panlbl, BorderLayout.WEST);

        MyPanel TxtPan = new MyPanel(new GridLayout(4, 1, 1, 1));
        MyPanel pancmbLtypr = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        String leavetype[] = {"EL", "CL", "ML", "SCL", "Other Leave", "Tour", "HPL"};
        cmbLeaveType = new JComboBox(leavetype);
        cmbLeaveType.setPreferredSize(new Dimension(200, 30));
        pancmbLtypr.add(cmbLeaveType);
        TxtPan.add(pancmbLtypr);

        MyPanel panreason = new MyPanel(new BorderLayout());
        txtReason = new MyTextField();
        panreason.add(txtReason);
        TxtPan.add(panreason);

        MyPanel pandt1 = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        JDatePicker picker = JDateComponentFactory.createJDatePicker();
        FromDt = (JComponent) picker;
        pandt1.add(FromDt);
        TxtPan.add(pandt1);

        MyPanel pandt2 = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        JDatePicker picker1 = JDateComponentFactory.createJDatePicker();
        ToDt = (JComponent) picker1;
        pandt2.add(ToDt);
        TxtPan.add(pandt2);

        panCenterCenter.add(TxtPan, BorderLayout.CENTER);

        MyPanel panStatus = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        lblStatus = new MyLabel(2, "");
        lblStatus.setText("Pending");
        panStatus.add(lblStatus);
        panCenterCenter.add(panStatus, BorderLayout.SOUTH);
        this.add(panCenterCenter, BorderLayout.CENTER);

        MyPanel panSouth = new MyPanel(new BorderLayout());
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
        panSouth.add(panHeadDetails, BorderLayout.CENTER);

        MyPanel pansouthsouth = new MyPanel(new FlowLayout());
        MyButton btnSave = new MyButton("SAVE") {

            @Override
            public void onClick() {
                if (!checkLeaveAppAvailable()) {
                    switch (command1) {
                        case "Add":
                            saveApplication();
                            break;
                        case "Update":
                            updateApplication(LeaveAppID);
                            break;
                    }
                }
            }
        };
        pansouthsouth.add(btnSave);

        MyButton btncancel = new MyButton("CANCEL") {

            @Override
            public void onClick() {
                LaeveApplicationApplet1.showHomePanel();
            }
        };
        pansouthsouth.add(btncancel);

        panSouth.add(pansouthsouth, BorderLayout.SOUTH);

        this.add(panSouth, BorderLayout.SOUTH);
        getAproovingAuthority();
    }

    private String createPacket() {
        Packetizer p = new Packetizer();
        try {
            p.addString(cmbLeaveType.getSelectedItem().toString());
            p.addString(txtReason.getText());
            p.addString(DateUtilities.getDate(FromDt));
            p.addString(DateUtilities.getDate(ToDt));
            p.addString(lblStatus.getText());
        } catch (Exception ex) {
            MyUtils.showMessage("Leave Application createPacket : " + ex);
        }
        return p.getPacket();
    }

    private void saveApplication() {
        String Packet = createPacket();
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("LeaveApplicationServlet");
            myHTTP.openOS();
            myHTTP.println("saveLeaveApplication");
            myHTTP.println(Packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            MyUtils.showMessage(" Packet : " + Packet);
            if (result.equals("Inserted")) {
                LaeveApplicationApplet1.showHomePanel();
            } else {
                MyUtils.showMessage(result);
            }
        } catch (Exception ex) {
            MyUtils.showException("Insert ODApplication", ex);
        }
    }

    private void updateApplication(String LeaveAppID) {
        String Packet = createPacket();
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("LeaveApplicationServlet");
            myHTTP.openOS();
            myHTTP.println("updateLeaveApplication");
            myHTTP.println(Packet);
            myHTTP.println(LeaveAppID);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.equals("Updated")) {
                LaeveApplicationApplet1.showHomePanel();
            } else {
                MyUtils.showMessage(result);
            }
        } catch (Exception ex) {
            MyUtils.showException("Insert ODApplication", ex);
        }
    }

    private void getAproovingAuthority() {
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

    private void getFillForm(String srno) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("OutDoorFormServlet");
            myHTTP.openOS();
            myHTTP.println("getFillForm");
            myHTTP.println(srno);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
//                Depacketizer dp = new Depacketizer(result);
////                cmbLeaveType.setText(dp.getString());
//                txtReason.setText(dp.getString());
//                txtPurOfVst.setText(dp.getString());
//                String fromdatetime = dp.getString();
//                if (!"".equals(fromdatetime) || fromdatetime != null) {
//                    String tempdatetime[] = fromdatetime.split(" ");
//                    FromTm.setValue(DateUtilities.getTime(tempdatetime[1]));
//                    MyUtils.setDate(FromDt, tempdatetime[0]);
//                }
//                String todatetime = dp.getString();
//                if (!"".equals(todatetime) || todatetime != null) {
//                    String tempdatetime[] = todatetime.split(" ");
//                    ToTm.setValue(DateUtilities.getTime(tempdatetime[1]));
//                    MyUtils.setDate(ToDt, tempdatetime[0]);
//                }
//                lblappdate.setText("Application date: " + dp.getString());
//                lblaprovdate.setText("Approve date: " + dp.getString());
//                lblstatus.setText("Status : " + dp.getString());
            }
        } catch (Exception ex) {
            MyUtils.showException("getFillForm", ex);
        }
    }

    public boolean checkLeaveAppAvailable() {
        try {
            String packet = createPacket();
            MyHTTP myHTTP = MyUtils.createServletConnection("LeaveApplicationServlet");
            myHTTP.openOS();
            myHTTP.println("checkLeaveAppAvailable");
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return false;
            }
            if (!"Not Found".equals(response)) {
                if (!"Update".equals(command1)) {
                    MyUtils.showMessage("Leave Application already exist ! Select new Dates");
                    return true;
                } else {
                    String cfid = response;
                    if (!LeaveAppID.equals(cfid)) {
                        MyUtils.showMessage("Leave Application already exist ! Select new Dates");
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            MyUtils.showException("checkCommpOffAvailable", e);
            return false;
        }
        return false;//unnecessary statement no use just to return something
    }
}
