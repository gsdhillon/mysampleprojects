/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.AddVisitor;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import SmartTimeApplet.visitor.vis_app.AddVisitorDialog;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import lib.gui.*;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponent;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 *
 * @author nbpatil
 */
public class AddVisitorMaster extends MyPanel {

    private MyTextField txtCompany;
    private MyTextField txtVisitor;
    private MyTextField txtCntry;
    private MyButton btAddCmp;
    private MyButton btCnc;
    private AddNewVisitor addNewVisitorapplet;
    private MyTextField txtvisitorid;
    private MyTextField txtdesign;
    private JComboBox cmbPvc;
    private JComboBox cmbidProof;
    private JComboBox cmbSex;
    private JComponent PVC_Validity;
    private MyTextField txtphNum;
    private MyTextField txtIdProof;
    private String str;
    private String Job;
    private String PVIS_ID;
    boolean openDialog1 = false;
    MyApplet parentApplet;
    AddVisitorDialog addVisitorDialog;
    
    public AddVisitorMaster(boolean openDialog, AddVisitorDialog addVisitorJDiolog) {
        this.openDialog1 = openDialog;
        this.parentApplet = MyUtils.applet;
        this.addVisitorDialog = addVisitorJDiolog;
    }

    //Container con;
    //static final int xPosition = 30, yPosition = 30;
    public AddVisitorMaster(AddNewVisitor addNewVisitorapplet, String Job, String PVIS_ID) {
        try {
            this.addNewVisitorapplet = addNewVisitorapplet;
            this.Job = Job;
            this.PVIS_ID = PVIS_ID;
            this.setLayout(new GridLayout(1, 1));
            this.add(addvisitorPanel());
            if (Job.equals("update")) {
                VisitorFillForm();
            }
        } catch (Exception ex) {
            MyUtils.showException("AddNewVisitor", ex);

        }
    }
    //Use for  form Validation

    private boolean FormFilled() {
        if (txtCompany.getText().equals("") || txtVisitor.getText().equals("")
                || txtdesign.getText().equals("") || txtIdProof.getText().equals("") || txtCntry.getText().equals("")
                || txtphNum.getText().equals("")) {

            MyUtils.showMessage("Form not filled");
            return false;
        }
        return true;
    }

    public MyPanel addvisitorPanel() throws Exception {
        MyPanel main = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        try {
            MyPanel VstPan = new MyPanel(new BorderLayout(5, 5), "addNew Visitor");
            VstPan.setPreferredSize(new Dimension(880, 600 / 3));


            MyPanel VstPan1 = new MyPanel(new GridLayout(1, 3, 5, 5));

            MyPanel CntPan = new MyPanel(new BorderLayout());
            MyPanel CntPan1 = new MyPanel(new BorderLayout());

            MyLabel lblcmpId = new MyLabel(1, "VisitorId:   ");
            CntPan.add(lblcmpId, BorderLayout.WEST);
            lblcmpId.setForeground(new Color(89, 32, 222));

            txtvisitorid = new MyTextField();
            txtvisitorid.setEditable(false);
            txtvisitorid.setText(getVisitorID());
            CntPan.add(txtvisitorid, BorderLayout.CENTER);
            VstPan1.add(CntPan);


            MyLabel lblcmpNm = new MyLabel(1, "    CompanyId:");
            CntPan1.add(lblcmpNm, BorderLayout.WEST);
            lblcmpNm.setForeground(new Color(89, 32, 222));

            MyPanel panCmp = new MyPanel(new BorderLayout());
            txtCompany = new MyTextField();
            txtCompany.setEditable(false);
            panCmp.add(txtCompany, BorderLayout.CENTER);

            MyButton btnSelcompany = new MyButton("Select Company") {

                @Override
                public void onClick() {
                    if (openDialog1 == false) {
                        new SelectCompanyDialog(addNewVisitorapplet,txtCompany).setVisible(true);
                    } else {
                        new SelectCompanyDialog(parentApplet, txtCompany).setVisible(true);
                    }
                }
            };
            panCmp.add(btnSelcompany, BorderLayout.EAST);

            CntPan1.add(panCmp, BorderLayout.CENTER);
            VstPan1.add(CntPan1);
            VstPan.add(VstPan1, BorderLayout.NORTH);

            MyPanel VstPan1Nm = new MyPanel(new GridLayout(1, 3, 5, 5));
            MyPanel pang1 = new MyPanel(new BorderLayout());
            MyPanel panlblg1 = new MyPanel(new GridLayout(3, 1, 5, 5));
            MyLabel lblNm = new MyLabel(1, "VstName*:");
            lblNm.setForeground(new Color(89, 32, 222));
            panlblg1.add(lblNm);

            MyLabel lblCty = new MyLabel(1, "Designation*:");
            lblCty.setForeground(new Color(89, 32, 222));
            panlblg1.add(lblCty);

            MyLabel lbsex = new MyLabel(1, "Sex:");
            lbsex.setForeground(new Color(89, 32, 222));
            panlblg1.add(lbsex);
            pang1.add(panlblg1, BorderLayout.WEST);

            MyPanel pantxtg1 = new MyPanel(new GridLayout(3, 1, 5, 5));
            txtVisitor = new MyTextField(20);
            txtVisitor.addKeyListener(new MyKeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyChar() == '\n') {
                        txtVisitor.getText();
                    }

                }
            });
            pantxtg1.add(txtVisitor);

            txtdesign = new MyTextField();
            pantxtg1.add(txtdesign);

            String strcmbSex[] = {"Male","FeMale"};
            cmbSex = new JComboBox(strcmbSex);
            pantxtg1.add(cmbSex);

            pang1.add(pantxtg1, BorderLayout.CENTER);

            VstPan1Nm.add(pang1);

            MyPanel pang2 = new MyPanel(new BorderLayout());
            MyPanel panlblg2 = new MyPanel(new GridLayout(3, 1, 5, 5));
            MyLabel lblPvc = new MyLabel(1, "PVC*:");
            lblPvc.setForeground(new Color(89, 32, 222));
            panlblg2.add(lblPvc);

            MyLabel lbidproof = new MyLabel(1, "ID_Proof");
            lbidproof.setForeground(new Color(89, 32, 222));
            panlblg2.add(lbidproof);

            MyLabel lblcntry = new MyLabel(1, "Nationality ");
            lblcntry.setForeground(new Color(89, 32, 222));
            panlblg2.add(lblcntry);
            pang2.add(panlblg2, BorderLayout.WEST);

            MyPanel pantxtg2 = new MyPanel(new GridLayout(3, 1, 5, 5));
            String strcmbPvc[] = {"NO", "YES"};
            cmbPvc = new JComboBox(strcmbPvc);
            pantxtg2.add(cmbPvc);

            String idproof[] = {"VoterId", "Pan Card", "Adhar Card", "driving licence"};
            cmbidProof = new JComboBox(idproof);

            cmbidProof.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {

                        str = cmbidProof.getSelectedItem().toString();
                        txtIdProof.setText(str);

                    }
                }
            });
            pantxtg2.add(cmbidProof);

            txtCntry = new MyTextField(20);
            pantxtg2.add(txtCntry);
            pang2.add(pantxtg2, BorderLayout.CENTER);
            VstPan1Nm.add(pang2);


            MyPanel pang3 = new MyPanel(new GridLayout(3, 1, 5, 5));

            MyPanel panpvcval = new MyPanel(new BorderLayout());
            MyLabel lblPvcvali = new MyLabel(1, "PVC_Validity");
            lblPvcvali.setForeground(new Color(89, 32, 222));
            panpvcval.add(lblPvcvali, BorderLayout.WEST);

            JDatePicker picker = JDateComponentFactory.createJDatePicker();
            PVC_Validity = (JComponent) picker;
            panpvcval.add(PVC_Validity, BorderLayout.CENTER);
            pang3.add(panpvcval);

            txtIdProof = new MyTextField();
            txtIdProof.setText(str);
            pang3.add(txtIdProof);

            MyPanel panphoneno = new MyPanel(new BorderLayout());
            MyLabel lbladdedby = new MyLabel(1, " PhoneNumber");
            lbladdedby.setForeground(new Color(89, 32, 222));

            panphoneno.add(lbladdedby, BorderLayout.WEST);


            txtphNum = new MyTextField(20);
            panphoneno.add(txtphNum, BorderLayout.CENTER);

            pang3.add(panphoneno);
            VstPan1Nm.add(pang3);
            VstPan.add(VstPan1Nm, BorderLayout.CENTER);

            MyPanel BtPan = new MyPanel(new FlowLayout(FlowLayout.CENTER));
            if (openDialog1 == false) {


                switch (Job) {
                    case "add":
                        btAddCmp = new MyButton("Add Visitor", 2) {

                            @Override
                            public void onClick() {
                                addNewVisitor();
                            }
                        };
                        break;
                    case "update":
                        btAddCmp = new MyButton("Update Visitor", 2) {

                            @Override
                            public void onClick() {
                                updateVstInfo();
                            }
                        };
                        break;
                }
                BtPan.add(btAddCmp);
            } else {
                btAddCmp = new MyButton("Add Visitor", 2) {

                    @Override
                    public void onClick() {

                        addNewVisitor();
                    }
                };
                BtPan.add(btAddCmp);

            }
            btCnc = new MyButton("Cancel", 2) {

                @Override
                public void onClick() {
                    if (openDialog1 == false) {
                        addNewVisitorapplet.showHomePanel();
                    } else if (openDialog1 == true) {
                        addVisitorDialog.closeDialog();
                    }
                }
            };

            BtPan.add(btCnc);


            VstPan.add(BtPan, BorderLayout.SOUTH);

            main.add(VstPan);

        } catch (Exception e) {
        }
        return main;
    }

    //AddNew Visitor
    private void addNewVisitor() {
        if (FormFilled()) {
            String Packet = createPacket();
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
                myHTTP.openOS();
                myHTTP.println("addNewVisitor");
                myHTTP.println(Packet);
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                if (result.startsWith("ERROR")) {
                    //MyUtils.showMessage(result);
                } else {
                    if (openDialog1 == false) {
                        addNewVisitorapplet.showHomePanel();
                    } else if (openDialog1 == true) {
                        addVisitorDialog.closeDialog();
                    }
                }
            } catch (Exception ex) {
                MyUtils.showException("add NewVisitor", ex);
            }
        }
    }
    //using for Create Packet

    private String createPacket() {
        Packetizer a = new Packetizer();
        try {

            a.addString(txtvisitorid.getText());
            a.addString(txtCompany.getText());
            a.addString(txtVisitor.getText());
            a.addString(txtdesign.getText());
            a.addInt(cmbPvc.getSelectedIndex());
            DateModel<?> model = ((JDateComponent) (PVC_Validity)).getModel();
            String date = model.getYear() + "-" + (model.getMonth() + 1) + "-" + model.getDay();
            a.addString(date);
            a.addString(txtIdProof.getText());
            a.addInt(cmbSex.getSelectedIndex());
            a.addString(txtCntry.getText());
            a.addString(txtphNum.getText());


            return a.getPacket();
        } catch (Exception e) {

            MyUtils.showMessage("create Packet");
        }
        return "packet Fail";

    }

    private void VisitorFillForm() {
        //txtCompanyId.setEditable(false);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
            myHTTP.openOS();
            myHTTP.println("VstFillForm");
            myHTTP.println(PVIS_ID);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            if (Result.startsWith("ERROR")) {
                MyUtils.showMessage(Result);
            } else {
                Depacketizer dp = new Depacketizer(Result);
                int size = dp.getInt();
                txtvisitorid.setText(dp.getString());
                txtCompany.setText(dp.getString());
                txtVisitor.setText(dp.getString());
                String strdesg=dp.getString();
               
               txtdesign.setText(strdesg);
               
                cmbPvc.setSelectedIndex(dp.getInt());

                String dt1 = dp.getString();
                if ((dt1 != null) & (!"".equals(dt1))) {
                    MyUtils.setDate(PVC_Validity, dt1);
                }

                txtIdProof.setText(dp.getString());
                cmbSex.setSelectedIndex(dp.getInt());
                txtCntry.setText(dp.getString());
                txtphNum.setText(dp.getString());



                //txtaddedby.setText(dp.getString());
            }
        } catch (Exception ex) {
            MyUtils.showException("Fill VisitorForm", ex);
        }

    }

    //Update Visitor Info
    public void updateVstInfo() {

        if (FormFilled()) {
            String Packet = createPacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
                    myHTTP.openOS();
                    myHTTP.println("updateVisitorInfo");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    myHTTP.closeIS();
                    //MyUtils.showMessage(result);
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else {
                        addNewVisitorapplet.showHomePanel();
                    }
                } catch (Exception ex) {
                    MyUtils.showException("Update CmpInfo", ex);
                }
            }
        }
    }

    public static String getIdProof(int index) {
        String Idproof = "";
        switch (index) {
            case 0:
                Idproof = "VoterId";
                break;
            case 1:
                Idproof = "PanCard";
                break;
            case 2:
                Idproof = "AdharCard";
                break;
            case 3:
                Idproof = "drivingLicance";
                break;

            default:
                Idproof = "Non";
        }
        return Idproof;
    }

    //For get VisitorID
    private String getVisitorID() {
        String pvisid = "";
        DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
        Date date = new Date();
        String currdate = dateFormat.format(date);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
            myHTTP.openOS();
            myHTTP.println("getVisitorId");
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);

            } else {
                if (!"".equals(result)) {

                    String strconcat = result;


                    int count;
                    count = Integer.parseInt(strconcat) + 1;

                    String strCount = "0000" + count;


                    String App1 = strCount.substring((strCount.length() - 4), strCount.length());
                    pvisid = "V" + currdate + App1;
                    System.out.println("pvisid" + pvisid);
                    
                } else {
                    pvisid = "V" + currdate + 0001;

                }
            }
        } catch (Exception ex) {
            MyUtils.showException("getCompanyID", ex);
        }

        return pvisid;

    }

    private void addWindowListener(WindowAdapter windowAdapter) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
