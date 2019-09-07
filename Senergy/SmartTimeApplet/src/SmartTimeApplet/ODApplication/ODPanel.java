/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.ODApplication;

/**
 *
 * @author Roop
 */
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import lib.Utility.DateUtilities;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 *
 * @author pradnya
 */
public class ODPanel extends MyPanel {

    private MyButton btnSubmit;
    private MyButton btnCancel;
    private MyTextField txtNmOfOrz, txtecode, txtename, txtPlace;
    private MyTextField txtPurOfVst;
    private JComponent FromDt;
    private JSpinner FromTm, ToTm;
    private JComponent ToDt;
    MyLabel lblCCno, lblDesign, lblDiv, lblRecEmp_No, lblRecName1, lblAprovEmp_No1;
    MyLabel lblAprovName2, lblappdate, lblaprovdate, lblstatus;
    OutDoorDutyApplet outDoorDutyApplet;
    Container con;

    public ODPanel(final OutDoorDutyApplet outDoorDutyApplet, final String cmd, final String srno) {
        this.outDoorDutyApplet = outDoorDutyApplet;
        this.setLayout(new BorderLayout());
        MyLabel lblHeader = new MyLabel(2, "OutDoor Application");
        this.add(lblHeader, BorderLayout.NORTH);
        MyPanel panMain = new MyPanel(new BorderLayout());
        panMain.setLayout(new BorderLayout());

        MyPanel panCenter = new MyPanel(new BorderLayout());
        MyPanel panApp = new MyPanel(new GridLayout(2, 1, 5, 5), "Applicant");
        MyPanel AppPan1 = new MyPanel(new BorderLayout());

        MyPanel panecode = new MyPanel(new BorderLayout());
        MyLabel lblcmpId = new MyLabel(1, "Emp No:");
        panecode.add(lblcmpId, BorderLayout.WEST);

        txtecode = new MyTextField(10);
        panecode.add(txtecode, BorderLayout.CENTER);

        AppPan1.add(panecode, BorderLayout.WEST);

        MyPanel panename = new MyPanel(new BorderLayout(5, 0));
        MyLabel lblName = new MyLabel(1, "Name:");
        panename.add(lblName, BorderLayout.WEST);

        txtename = new MyTextField();
        panename.add(txtename, BorderLayout.CENTER);
        AppPan1.add(panename);

        panApp.add(AppPan1);

        MyPanel AppPan2 = new MyPanel(new GridLayout(1, 3));
        lblCCno = new MyLabel(1, "CC No: ");
        AppPan2.add(lblCCno);

        lblDesign = new MyLabel(1, "Design:");
        AppPan2.add(lblDesign);

        lblDiv = new MyLabel(1, "Div:");
        AppPan2.add(lblDiv);
        panApp.add(AppPan2);
        panCenter.add(panApp, BorderLayout.NORTH);

        MyPanel SubPan = new MyPanel(new BorderLayout(), "Details");
        SubPan.setSize(outDoorDutyApplet.getWidth(), outDoorDutyApplet.getHeight() / 10);
        MyPanel CntPan = new MyPanel(new GridLayout(7, 1, 1, 1));


        MyLabel lblNmO = new MyLabel(1, "Name Of Orgnization : ");
        CntPan.add(lblNmO);

        MyLabel lblPlace = new MyLabel(1, "Place of Visit :    ");
        CntPan.add(lblPlace);

        MyLabel lblPrvst = new MyLabel(1, "Purpose Of Visit :    ");
        CntPan.add(lblPrvst);

        MyLabel lblfrmdt = new MyLabel(1, "From Date:          ");
        CntPan.add(lblfrmdt);

        MyLabel lblfrmtm = new MyLabel(1, "From Time:          ");
        CntPan.add(lblfrmtm);

        MyLabel lblToDt = new MyLabel(1, "To Date:            ");
        CntPan.add(lblToDt);

        MyLabel lblToTm = new MyLabel(1, "To Time:            ");
        CntPan.add(lblToTm);
        SubPan.add(CntPan, BorderLayout.WEST);

        MyPanel TxtPan = new MyPanel(new GridLayout(7, 1, 1, 1));
        txtNmOfOrz = new MyTextField();
        TxtPan.add(txtNmOfOrz);

        txtPlace = new MyTextField();
        TxtPan.add(txtPlace);

        txtPurOfVst = new MyTextField();
        TxtPan.add(txtPurOfVst);

        MyPanel pandt1 = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        JDatePicker picker = JDateComponentFactory.createJDatePicker();
        FromDt = (JComponent) picker;
        pandt1.add(FromDt);
        TxtPan.add(pandt1);

        MyPanel pantm1 = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        FromTm = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor1 = new JSpinner.DateEditor(FromTm, "HH:mm:ss");
        FromTm.setEditor(timeEditor1);
        pantm1.add(FromTm);
        TxtPan.add(pantm1);

        MyPanel pandt2 = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        JDatePicker picker1 = JDateComponentFactory.createJDatePicker();
        ToDt = (JComponent) picker1;
        pandt2.add(ToDt);
        TxtPan.add(pandt2);

        MyPanel pantm2 = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        ToTm = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(ToTm, "HH:mm:ss");
        ToTm.setEditor(timeEditor2);
        pantm2.add(ToTm);
        TxtPan.add(pantm2);

        SubPan.add(TxtPan, BorderLayout.CENTER);
        panCenter.add(SubPan, BorderLayout.CENTER);

        MyPanel panstatus = new MyPanel(new GridLayout(2, 1), "Status");

        MyPanel pandates = new MyPanel(new GridLayout(1, 2));

        lblappdate = new MyLabel(1, "Application date: " + DateUtilities.getCurrentDate());
        pandates.add(lblappdate);

        lblaprovdate = new MyLabel(1, "Approve date: ");
        pandates.add(lblaprovdate);
        panstatus.add(pandates);

        MyPanel panstat = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        lblstatus = new MyLabel(1, "Status : ");
        panstat.add(lblstatus, BorderLayout.WEST);

        MyLabel lblshowstatus = new MyLabel(2, "");
        panstat.add(lblshowstatus, BorderLayout.CENTER);
        panstatus.add(panstat);
        panCenter.add(panstatus, BorderLayout.SOUTH);


        panMain.add(panCenter, BorderLayout.CENTER);

        MyPanel panG3 = new MyPanel(new BorderLayout());
        MyPanel CenPan = new MyPanel(new BorderLayout());

        MyPanel RmdrPan = new MyPanel(new GridLayout(1, 2));

        MyPanel RmdrPan1 = new MyPanel(new GridLayout(2, 1), "Recommender");

        lblRecEmp_No = new MyLabel(1, "Emp_No:");
        RmdrPan1.add(lblRecEmp_No);
        RmdrPan.add(RmdrPan1);

        lblRecName1 = new MyLabel(1, "Name:");
        RmdrPan1.add(lblRecName1);
        RmdrPan.add(RmdrPan1);

//        MyLabel lblRecEmail_Id = new MyLabel(1, "Email_ID:");
//        RmdrPan1.add(lblRecEmail_Id);
        RmdrPan.add(RmdrPan1);
        CenPan.add(RmdrPan);

        MyPanel ApprovPan1 = new MyPanel(new GridLayout(2, 1), "Approver");

        lblAprovEmp_No1 = new MyLabel(1, "Emp_No:");
        ApprovPan1.add(lblAprovEmp_No1);
        RmdrPan.add(ApprovPan1);

        lblAprovName2 = new MyLabel(1, "Name:");
        ApprovPan1.add(lblAprovName2);
        RmdrPan.add(ApprovPan1);

//        MyLabel lblAproveEmail_Id1 = new MyLabel(1, "Email_ID:");
//        ApprovPan1.add(lblAproveEmail_Id1);
        RmdrPan.add(ApprovPan1);
        CenPan.add(RmdrPan);
        panG3.add(CenPan, BorderLayout.CENTER);

        MyPanel btnMainPan = new MyPanel(new FlowLayout());

        btnSubmit = new MyButton("Submit", 2, Color.WHITE) {

            @Override
            public void onClick() {
                switch (cmd) {
                    case "Add":
                        saveApplication();
                        break;
                    case "Update":
                        updateApplication(srno);
                        break;
                }
            }
        };
        btnMainPan.add(btnSubmit);

        btnCancel = new MyButton("Cancel", 2, Color.WHITE) {

            @Override
            public void onClick() {
                outDoorDutyApplet.showHomePanel();
            }
        };
        btnMainPan.add(btnCancel);
        panG3.add(btnMainPan, BorderLayout.SOUTH);
        panMain.add(panG3, BorderLayout.SOUTH);
        this.add(panMain, BorderLayout.CENTER);
        getAproovingAuthority();
        if ("Update".equals(cmd)) {
            getFillForm(srno);
        }

    }

    private String createPacket() {
        Packetizer p = new Packetizer();
        try {
            p.addString(txtecode.getText());
            p.addString(txtPlace.getText());
            p.addString(txtNmOfOrz.getText());
            p.addString(txtPurOfVst.getText());
            p.addString(DateUtilities.getDate(FromDt) + " " + DateUtilities.getTimeFromJSpinner(FromTm.getValue().toString()));//from date time
            p.addString(DateUtilities.getDate(ToDt) + " " + DateUtilities.getTimeFromJSpinner(ToTm.getValue().toString()));//to date time
            String srNo = txtecode.getText() + DateUtilities.getCurrentDate() + DateUtilities.getCurrentTime();
            p.addString(srNo);
            p.addString(DateUtilities.getCurrentDate());//application date
        } catch (Exception ex) {
            Logger.getLogger(ODPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p.getPacket();
    }

    private void saveApplication() {
        String Packet = createPacket();
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("OutDoorFormServlet");
            myHTTP.openOS();
            myHTTP.println("SaveODApplication");
            myHTTP.println(Packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            MyUtils.showMessage(" Packet : " + Packet);
            if (result.equals("Inserted")) {
                outDoorDutyApplet.showHomePanel();
            } else {
                MyUtils.showMessage(result);
            }
        } catch (Exception ex) {
            MyUtils.showException("Insert ODApplication", ex);
        }
    }

    private void updateApplication(String srno) {
        String Packet = createPacket();
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("OutDoorFormServlet");
            myHTTP.openOS();
            myHTTP.println("updateODApplication");
            myHTTP.println(Packet);
            myHTTP.println(srno);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.equals("Updated")) {
                outDoorDutyApplet.showHomePanel();
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
                txtecode.setText(dp.getString());
                txtename.setText(dp.getString());
                lblCCno.setText("CC No: " + dp.getString());
                lblDesign.setText("Design: " + dp.getString());
                lblDiv.setText("Div: " + dp.getString());
                lblAprovEmp_No1.setText("Emp_No: " + dp.getString());//division head code
                lblAprovName2.setText("Name:   " + dp.getString());//division head name
                lblRecEmp_No.setText("Emp_No: " + dp.getString());//section head code
                lblRecName1.setText("Name:   " + dp.getString());//section head name
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
                Depacketizer dp = new Depacketizer(result);
                txtNmOfOrz.setText(dp.getString());
                txtPlace.setText(dp.getString());
                txtPurOfVst.setText(dp.getString());
                String fromdatetime = dp.getString();
                if (!"".equals(fromdatetime) || fromdatetime != null) {
                    String tempdatetime[] = fromdatetime.split(" ");
                    FromTm.setValue(DateUtilities.getTime(tempdatetime[1]));
                    MyUtils.setDate(FromDt, tempdatetime[0]);
                }
                String todatetime = dp.getString();
                if (!"".equals(todatetime) || todatetime != null) {
                    String tempdatetime[] = todatetime.split(" ");
                    ToTm.setValue(DateUtilities.getTime(tempdatetime[1]));
                    MyUtils.setDate(ToDt, tempdatetime[0]);
                }
                lblappdate.setText("Application date: " + dp.getString());
                lblaprovdate.setText("Approve date: " + dp.getString());
                lblstatus.setText("Status : " + dp.getString());
            }
        } catch (Exception ex) {
            MyUtils.showException("getFillForm", ex);
        }
    }
}
