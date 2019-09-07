/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.COFFApplication;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
public class COFFAplication extends MyPanel {

    JComponent dtCFDate, dtWorked;
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
    String compoffAppID;
    MyTextField txtpurpose;
    String command1;
    COFFApplicationApplet COFFApplicationApplet;
    MyButton btnAddMore, btnUpdate;

    public COFFAplication(final COFFApplicationApplet COFFApplicationApplet, String command, final COFFTableModel tablemodel, String packet) {
        this.setLayout(new BorderLayout());
        this.tablemodel = tablemodel;
        this.command1 = command;
        this.COFFApplicationApplet = COFFApplicationApplet;
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

        MyPanel panCOmpOff = new MyPanel(new BorderLayout(), "Add CompOff details");

        MyPanel pandatepurpose = new MyPanel(new BorderLayout(), "");
        MyPanel panGrid = new MyPanel(new GridLayout(2, 1, 10, 10));
        MyPanel pandate = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        MyLabel lblfrmdat = new MyLabel(1, "Comp Off Date : ");
        pandate.add(lblfrmdat);

        JDatePicker picker1 = JDateComponentFactory.createJDatePicker();
        dtCFDate = (JComponent) picker1;
        pandate.add(dtCFDate);

        MyLabel lbltodat = new MyLabel(1, "Worked Date : ");
        pandate.add(lbltodat);

        JDatePicker picker2 = JDateComponentFactory.createJDatePicker();
        dtWorked = (JComponent) picker2;
        pandate.add(dtWorked);

        panGrid.add(pandate);

        MyPanel panPurpose = new MyPanel(new BorderLayout());
        lblpurpose = new MyLabel(1, "Purpose : ");
        panPurpose.add(lblpurpose, BorderLayout.WEST);

        txtpurpose = new MyTextField();
        panPurpose.add(txtpurpose, BorderLayout.CENTER);
        panGrid.add(panPurpose);

        pandatepurpose.add(panGrid, BorderLayout.CENTER);
        MyPanel panbuttons = new MyPanel(new GridLayout(2, 1));
        btnAddMore = new MyButton("Add More") {

            @Override
            public void onClick() {
                command1 = "Add";
                COFFApplicationApplet.getApplicationDetailsList();
                enableButtons();
                btnAddMore.setEnabled(false);
            }
        };
        btnAddMore.setEnabled(false);
        panbuttons.add(btnAddMore);
        btnUpdate = new MyButton("Update") {

            @Override
            public void onClick() {
                int row = tblCOFF.getSelectedRow();
                if (row == -1) {
                    MyUtils.showMessage("No Record Selected");
                    return;
                }
                command1 = "Update";
                String datapacket = COFFApplicationApplet.createUpdatePacket(row);
                setData(datapacket);
                enableButtons();
                btnUpdate.setEnabled(false);
            }
        };
        btnUpdate.setEnabled(false);
        panbuttons.add(btnUpdate);
        pandatepurpose.add(panbuttons, BorderLayout.EAST);
        panCOmpOff.add(pandatepurpose, BorderLayout.NORTH);

        tblCOFF = new MyTable(tablemodel);
        panCOmpOff.add(tblCOFF.getGUI(), BorderLayout.CENTER);
        this.add(panCOmpOff, BorderLayout.CENTER);

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
                if (!checkCommpOffAvailable()) {
                    if (checkValidCompOff()) {
                        switch (command1) {
                            case "Add":
                                addMoreCompOff();
                                COFFApplicationApplet.getApplicationDetailsList();
                                break;
                            case "Update":
                                updateCompOff();
                                COFFApplicationApplet.getApplicationDetailsList();
                                break;
                        }
                        disableButtons();
                        btnUpdate.setEnabled(true);
                        btnAddMore.setEnabled(true);
                    }

                }
            }
        };
        pansouthsouth.add(btnSave);

        MyButton btncancel = new MyButton("CANCEL") {

            @Override
            public void onClick() {
                COFFApplicationApplet.showHomePanel();
            }
        };
        pansouthsouth.add(btncancel);

        panSouth.add(pansouthsouth, BorderLayout.SOUTH);

        this.add(panSouth, BorderLayout.SOUTH);
        getAproovingAuthority();
        if ("Update".equals(command1)) {
            setData(packet);
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

    public void addMoreCompOff() {
        try {
            String packet = createPacket();
            MyHTTP myHTTP = MyUtils.createServletConnection("CoffFormServlet");
            myHTTP.openOS();
            myHTTP.println("addMoreCompOff");
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            }
        } catch (Exception ex) {
            MyUtils.showException("addMoreCompOff", ex);
        }
    }

    public void updateCompOff() {
        try {
            String packet = createPacket();
            MyHTTP myHTTP = MyUtils.createServletConnection("CoffFormServlet");
            myHTTP.openOS();
            myHTTP.println("updateCFApplication");
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            }
        } catch (Exception ex) {
            MyUtils.showException("addMoreCompOff", ex);
        }
    }

    public String createPacket() {
        Packetizer p = new Packetizer();
        try {
            p.addString(DateUtilities.getDate(dtCFDate));
            p.addString(DateUtilities.getDate(dtWorked));
            p.addString(txtpurpose.getText());
            p.addString(compoffAppID);

        } catch (Exception ex) {
            MyUtils.showMessage("createPacket : " + ex);
        }
        return p.getPacket();
    }

    public boolean checkCommpOffAvailable() {
        try {
            String packet = createPacket();
            MyHTTP myHTTP = MyUtils.createServletConnection("CoffFormServlet");
            myHTTP.openOS();
            myHTTP.println("checkCompOffAvailable");
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
                    MyUtils.showMessage("Compensatory Off Application already exist ! Select new Dates");
                    return true;
                } else {
                    String cfid = response;
                    if (!compoffAppID.equals(cfid)) {
                        MyUtils.showMessage("Compensatory Off Application already exist ! Select new Dates");
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

    public void setData(String packet) {
        try {
            Depacketizer dp = new Depacketizer(packet);
            compoffAppID = dp.getString();
            String CFDate = dp.getString();
            String workedDate = dp.getString();
            String purpose = dp.getString();
            MyUtils.setDate(dtCFDate, CFDate);
            MyUtils.setDate(dtWorked, workedDate);
            txtpurpose.setText(purpose);
        } catch (Exception ex) {
            MyUtils.showMessage("set update data : " + ex);
        }
    }

    private void enableButtons() {
        dtCFDate.setEnabled(true);
        dtWorked.setEnabled(true);
        txtpurpose.setEnabled(true);
    }

    private void disableButtons() {
        dtCFDate.setEnabled(false);
        dtWorked.setEnabled(false);
        txtpurpose.setEnabled(false);
    }

    private boolean checkValidCompOff() {
        try {
            String packet = createPacket();
            MyHTTP myHTTP = MyUtils.createServletConnection("CoffFormServlet");
            myHTTP.openOS();
            myHTTP.println("checkValidCompOff");
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return false;
            }
            if ("true".equals(response)) {
                return true;
            } else {
                MyUtils.showMessage("Not a valid CompOff.Check working details.");
                return false;
            }


//            if (!"Update".equals(command1)) {
//                MyUtils.showMessage("Not Valid Compansatory Off.Check Working details.");
//                return true;
//            } else {
//                String cfid = response;
//                if (!compoffAppID.equals(cfid)) {
//                    MyUtils.showMessage("Not Valid Compansatory Off.Check Working details.");
//                    return true;
//                }
//            }
//        }
        } catch (Exception e) {
            MyUtils.showException("checkCommpOffAvailable", e);
            return false;
        }
    }
}
