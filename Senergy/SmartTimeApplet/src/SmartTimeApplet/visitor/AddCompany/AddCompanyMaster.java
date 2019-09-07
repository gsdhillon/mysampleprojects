package SmartTimeApplet.visitor.AddCompany;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import SmartTimeApplet.visitor.vis_app.AddCompanyDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import lib.ComboArray.ComboArray;
import lib.gui.*;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author nbpatil
 */
public class AddCompanyMaster extends MyPanel {

    //private MyLabel lblCompanyId;
    private MyTextField txtNm;
    private MyTextField txtAdd1;
    private JComboBox cmbCmTy;
    private MyTextField txtCty;
    private MyTextField txtSte;
    private MyTextField txtpin;
    private MyTextField txtPhNum;
    private MyButton btAddCmp;
    private MyButton btCnc;
    private AddCompany addCompanyapplet;
    private String Job;
    private String PCOM_ID;
    private MyTextField txtPhNum2;
    private MyTextField txtPhNum3;
    private  MyLabel lblph2;
    private MyTextField txtAdd2;
    private MyTextField txtAdd3;
    private JComboBox CmbState;
    private JComboBox cmbCity;
    private MyTextField txtCompanyId;
    boolean openDialog1 = false;
    private AddCompanyDialog myDialog;
    private MyApplet parentApplet;
    
    public AddCompanyMaster(boolean openDialog, AddCompanyDialog myDialog) {
        this.openDialog1 = openDialog;
        this.myDialog = myDialog;
        this.parentApplet = MyUtils.applet;
    }
    
    public AddCompanyMaster(boolean openDialog) {
        this.openDialog1 = openDialog;
    }

    public AddCompanyMaster(AddCompany addCompanyapplet, String Job, String PCOM_ID) {
        try {
            this.addCompanyapplet = addCompanyapplet;
            this.Job = Job;
            this.PCOM_ID = PCOM_ID;
            this.add(addCompanyPanel());
            if (Job.equals("update")) {
                FillForm();
            }
        } catch (Exception ex) {
            MyUtils.showException("updateCompany", ex);
        }
    }

    private void FillForm() {
        //txtCompanyId.setEditable(false);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
            myHTTP.openOS();
            myHTTP.println("FillForm");
            myHTTP.println(PCOM_ID);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            if (Result.startsWith("ERROR")) {
                MyUtils.showMessage(Result);
            } else {
                Depacketizer dp = new Depacketizer(Result);
                int size = dp.getInt();
                txtCompanyId.setText(dp.getString());
                txtNm.setText(dp.getString());
                String str = dp.getString();

                //String addarr[] = str.split("\\$");
                  String addarr[] = str.split("#");
                
                 for (int i = 0; i < addarr.length; i++) {
                        if (i == 0) {
                            txtAdd1.setText(addarr[i]);
                        } else if (i == 1) {
                            txtAdd2.setText(addarr[i]);
                        } else if (i == 2) {
                            txtAdd3.setText(addarr[i]);
                        }
                    }
//                txtAdd1.setText(addarr[0]);
//                txtAdd2.setText(addarr[1]);
//                txtAdd3.setText(addarr[2]);
                cmbCmTy.setSelectedIndex(dp.getInt());
                CmbState.setSelectedItem(dp.getString());
                cmbCity.setSelectedItem(dp.getString());
                txtpin.setText(dp.getString());
                txtPhNum.setText(dp.getString());
                txtPhNum2.setText(dp.getString());
                txtPhNum3.setText(dp.getString());

                //txtaddedby.setText(dp.getString());
            }
        } catch (Exception ex) {
            MyUtils.showException("Fill Form", ex);
        }

    }

    //Use for  form Validation
    private boolean FormFilled() {


        if (cmbCmTy.getSelectedItem().equals("Select Type")) {
            MyUtils.showMessage("Division Code not Selected..!");
            return false;
        } else if (CmbState.getSelectedItem().equals("Select Type")) {
            MyUtils.showMessage("Division Code not Selected..!");
            return false;
        } else if ("".equals(txtNm.getText())) {
            MyUtils.showMessage("Enter Company Name...!");
        }
//        else if (cmbCity.getSelectedItem().equals("Select Type")) {
//            MyUtils.showMessage("Division Code not Selected");
//            return false;
//        }


        if (txtNm.getText().equals("") || txtAdd1.getText().equals("")
                || txtpin.getText().equals("")
                || txtPhNum.getText().equals("")) {

            MyUtils.showMessage("Form not filled");
            return false;
        }
        return true;
    }

    // For add Main Panel in Form
    public MyPanel addCompanyPanel() throws Exception {

        MyPanel MainPan1 = new MyPanel(new BorderLayout(), "add New Company");
        try {
            MyPanel panEmpDetail = new MyPanel(new GridLayout(4, 1, 5, 5));

            MyPanel panVst = new MyPanel(new GridLayout(1, 2, 5, 5));
            MyPanel CntPan = new MyPanel(new BorderLayout());
            MyPanel CntPan1 = new MyPanel(new BorderLayout());

            MyLabel lblcmpId = new MyLabel(1, "CompanyId:    ");
            CntPan.add(lblcmpId, BorderLayout.WEST);
            lblcmpId.setForeground(new Color(89, 32, 222));
            panVst.add(CntPan);

            MyLabel lblcmpNm = new MyLabel(1, "   Name:");
            CntPan1.add(lblcmpNm, BorderLayout.WEST);
            lblcmpNm.setForeground(new Color(89, 32, 222));
            panVst.add(CntPan1);

            txtCompanyId = new MyTextField();
            txtCompanyId.setEditable(false);
            CntPan.add(txtCompanyId, BorderLayout.CENTER);
            panVst.add(CntPan);

            txtNm = new MyTextField();

            txtNm.addKeyListener(new MyKeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {

//                    if (e.getKeyCode() == KeyEvent.VK_TAB) {
//                        String strcompname = txtNm.getText();
//                        if (!"".equals(strcompname)) {
//                            char ch = strcompname.charAt(0);
//                            txtCompanyId.setText(getCompanyId(ch));
//                        }
//                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                        String strcompname = txtNm.getText();
//                        if (!"".equals(strcompname)) {
//                            char ch = strcompname.charAt(0);
//                            txtCompanyId.setText(getCompanyId(ch));
//                        }
//                    }

                    if (e.getKeyChar() == '\n') {
                        String strcompname = txtNm.getText();
                        char ch = strcompname.charAt(0);
                        txtCompanyId.setText(getCompanyId(ch));
                    }
                }
            });
            CntPan1.add(txtNm, BorderLayout.CENTER);

            panVst.add(CntPan1);
            panEmpDetail.add(panVst);

            MyPanel cmpPan = new MyPanel(new GridLayout(1, 3, 5, 5));

            MyPanel CntPan2 = new MyPanel(new BorderLayout());
            MyPanel cmplblPan = new MyPanel(new BorderLayout());
            MyPanel cmpTxtPan = new MyPanel(new BorderLayout());

            MyLabel lbladd = new MyLabel(1, "Address Line1:");
            CntPan2.add(lbladd, BorderLayout.WEST);
            lbladd.setForeground(new Color(89, 32, 222));
            cmpPan.add(CntPan2);

            MyLabel lbladdline2 = new MyLabel(1, " Line2:");
            cmplblPan.add(lbladdline2, BorderLayout.WEST);
            lbladdline2.setForeground(new Color(89, 32, 222));
            cmpPan.add(cmplblPan);

            MyLabel lbladdline3 = new MyLabel(1, "  Line3:");
            cmpTxtPan.add(lbladdline3, BorderLayout.WEST);
            lbladdline3.setForeground(new Color(89, 32, 222));
            cmpPan.add(cmpTxtPan);
            panEmpDetail.add(cmpPan);

            txtAdd1 = new MyTextField();
            CntPan2.add(txtAdd1, BorderLayout.CENTER);
            cmpPan.add(CntPan2);

            txtAdd2 = new MyTextField();

            cmplblPan.add(txtAdd2, BorderLayout.CENTER);
            cmpPan.add(cmplblPan);

            txtAdd3 = new MyTextField();
            cmpTxtPan.add(txtAdd3, BorderLayout.CENTER);
            cmpPan.add(cmpTxtPan);
            panEmpDetail.add(cmpPan);

            MyPanel companyPan = new MyPanel(new GridLayout(1, 4, 5, 5));

            MyPanel cmpcntPan = new MyPanel(new BorderLayout());
            MyPanel companyPan1 = new MyPanel(new BorderLayout());
            MyPanel companyPan2 = new MyPanel(new BorderLayout());
            MyPanel companyPan3 = new MyPanel(new BorderLayout());

            MyLabel lblCmTy = new MyLabel(1, "Company Type: ");
            cmpcntPan.add(lblCmTy, BorderLayout.WEST);
            lblCmTy.setForeground(new Color(89, 32, 222));
            cmpPan.add(cmpcntPan);

            MyLabel lblSte = new MyLabel(1, "  State:");
            companyPan1.add(lblSte, BorderLayout.WEST);
            lblSte.setForeground(new Color(89, 32, 222));
            companyPan.add(companyPan1);

            MyLabel lbCty = new MyLabel(1, "   City:");
            companyPan2.add(lbCty, BorderLayout.WEST);
            lbCty.setForeground(new Color(89, 32, 222));
            companyPan.add(companyPan2);
            //pancmpDetail.add(cmpPan);

            MyLabel lblcmptype = new MyLabel(1, "   Pin:");
            companyPan3.add(lblcmptype, BorderLayout.WEST);
            lblcmptype.setForeground(new Color(89, 32, 222));
            companyPan.add(companyPan3);
            panEmpDetail.add(companyPan);

            String strcmbCmTy[] = {"None", "Private", "Govt", "SemiGovt"};
            cmbCmTy = new JComboBox(strcmbCmTy);
            //addComType(cmbCmTy);
            cmpcntPan.add(cmbCmTy, BorderLayout.CENTER);
            companyPan.add(cmpcntPan);

            CmbState = new JComboBox(ComboArray.FillStates());
            companyPan1.add(CmbState, BorderLayout.CENTER);

            CmbState.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String state = CmbState.getSelectedItem().toString();
                        ComboArray.fillArrayToCombo(ComboArray.FillCity(state), cmbCity);
                    }
                }
            });

            companyPan.add(companyPan1);

            cmbCity = new JComboBox(ComboArray.FillCity("AndraPradesh"));
            companyPan2.add(cmbCity, BorderLayout.CENTER);
            companyPan.add(companyPan2);

            txtpin = new MyTextField();
            companyPan3.add(txtpin, BorderLayout.CENTER);
            companyPan.add(companyPan3);
            panEmpDetail.add(companyPan);

            MyPanel company1Pan = new MyPanel(new GridLayout(1, 3, 5, 5));

            MyPanel companyPan4 = new MyPanel(new BorderLayout());
            MyPanel companyPan5 = new MyPanel(new BorderLayout());
            MyPanel companyPan6 = new MyPanel(new BorderLayout());

            lblph2 = new MyLabel(1, "Phone Number: ");
            companyPan4.add(lblph2, BorderLayout.WEST);
            lblph2.setForeground(new Color(89, 32, 222));
            company1Pan.add(companyPan4);

            MyLabel lbPh3 = new MyLabel(1, " Number2:");
            companyPan5.add(lbPh3, BorderLayout.WEST);
            lbPh3.setForeground(new Color(89, 32, 222));
            company1Pan.add(companyPan5);
            //pancmpDetail.add(cmpPan);

            MyLabel lblPhNum = new MyLabel(1, "  Number3:");
            companyPan6.add(lblPhNum, BorderLayout.WEST);
            lblPhNum.setForeground(new Color(89, 32, 222));
            company1Pan.add(companyPan6);
            panEmpDetail.add(company1Pan);

            txtPhNum = new MyTextField();
            companyPan4.add(txtPhNum, BorderLayout.CENTER);
            
             txtPhNum.addKeyListener(new KeyAdapter() {
                @Override
                        public void keyPressed(KeyEvent EVT) {
                                String value = txtPhNum.getText();
//                                int l = value.length();
                                if (EVT.getKeyChar() >= '0' && EVT.getKeyChar() <= '9') {
//                                        txtPhNum.setEditable(true);
//                                       lblph2.setText("");
                                } 
//                              else {
//                                        txtPhNum.setEditable(false);
//                                       lblph2.setText("*Enter numeric");
//                                        
//                                }
                        }
                });

            company1Pan.add(companyPan4);

            txtPhNum2 = new MyTextField();
            companyPan5.add(txtPhNum2, BorderLayout.CENTER);
            company1Pan.add(companyPan5);

            txtPhNum3 = new MyTextField();
            companyPan6.add(txtPhNum3, BorderLayout.CENTER);
            company1Pan.add(companyPan6);
            panEmpDetail.add(company1Pan);

            MyPanel MainPan2 = new MyPanel(new BorderLayout());
            MyPanel BtPan = new MyPanel(new FlowLayout(FlowLayout.CENTER));
//Adding Button 
            if (openDialog1 == false) {

                switch (Job) {
                    case "add":
                        btAddCmp = new MyButton("Add Company", 2) {

                            @Override
                            public void onClick() {
                                addNewCompany();
                            }
                        };
                        break;
                    case "update":
                        btAddCmp = new MyButton("Update Company", 2) {

                            @Override
                            public void onClick() {
                                updatecmpInfo();
                            }
                        };
                        break;
                }
                BtPan.add(btAddCmp);


            } else {
                btAddCmp = new MyButton("Add Company", 2) {

                    @Override
                    public void onClick() {
                        addNewCompany();

                    }
                };

            }

            BtPan.add(btAddCmp);

            btCnc = new MyButton("Cancel", 2) {

                @Override
                public void onClick() {
                    if (openDialog1 == false) {
                        addCompanyapplet.ShowHomePanel();
                    } else if (openDialog1 == true) {
                        myDialog.closeDialog();
                    }
                }
            };

            BtPan.add(btCnc);

            MainPan2.add(BtPan, BorderLayout.CENTER);
            MainPan1.setLayout(new BorderLayout(3, 1));

            MainPan1.add(panEmpDetail, BorderLayout.CENTER);
            MainPan1.add(MainPan2, BorderLayout.SOUTH);

        } catch (Exception e) {
            MyUtils.showMessage("Create Company Panel : " + e);
            e.printStackTrace();
        }
        return MainPan1;
    }

    //Submitt AddNew Companydetails into database
    private void addNewCompany() {
        if (FormFilled()) {
            if (!"".equals(txtNm.getText())) {
                String strcompname = txtNm.getText();
                char ch = strcompname.charAt(0);
                txtCompanyId.setText(getCompanyId(ch));
            }
            String Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
                    myHTTP.openOS();
                    myHTTP.println("addNewCompany");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    if (result.startsWith("ERROR")) {

                        MyUtils.showMessage(result);
                    } else {
                        if (openDialog1 == false) {
                            addCompanyapplet.ShowHomePanel();
                        } else if (openDialog1 == true) {
                            myDialog.closeDialog();
                        }
                    }
                } catch (Exception ex) {
                    MyUtils.showException("add NewCompany", ex);
                }


            }
        }
    }
    //update Cmpinformation     

    public void updatecmpInfo() {

        if (FormFilled()) {
            String Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
                    myHTTP.openOS();
                    myHTTP.println("updateCmpInfo");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    myHTTP.closeIS();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else {
                        addCompanyapplet.ShowHomePanel();
                    }
                } catch (Exception ex) {
                    MyUtils.showException("Update CmpInfo", ex);
                }
            }
        }
    }

    //getCompanyId For ApplyCep 
    private void getCompanyInfo(String companyid) {

        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
            myHTTP.openOS();
            myHTTP.println("getCompanyInfo");
            myHTTP.println(companyid);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            Depacketizer d = new Depacketizer(result);
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);

            } else {
                if (!"Not Found".equals(result)) {

                    //String strcmp[] = {d.getString()};

                    txtCompanyId.setText(d.getString());

                } else {

                    MyUtils.showMessage("Company not present! Enter another Compnayid");
                    return;
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("getCompanyInfo", ex);
        }

        //}

    }

    //get CompanyID for AddNew Company
    private String getCompanyId(char firstchar) {
        String compId = "";
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
            myHTTP.openOS();
            myHTTP.println("getCmpId");
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
//            Depacketizer d = new Depacketizer(result);
            if (result.startsWith("ERROR")) {
                // MyUtils.showMessage(result);
            } else {
                if (!"".equals(result)) {

                    String strlastid = result;
                    int no = Integer.parseInt(strlastid.substring(1));
                    int strCount = no + 1;
                    compId = firstchar + "00" + strCount;

                } else {
                    compId = firstchar + "001";
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("getCompanyID", ex);
        }
        return compId;
    }

    private String CreatePacket() {
        Packetizer a = new Packetizer();
        try {

            a.addString(txtCompanyId.getText());
            a.addString(txtNm.getText());
            a.addString(txtAdd1.getText() + "#" + (txtAdd2.getText()) + "#" + (txtAdd3.getText()));
            a.addInt(cmbCmTy.getSelectedIndex());
            a.addString(CmbState.getSelectedItem().toString());
            a.addString(cmbCity.getSelectedItem().toString());
            a.addString(txtpin.getText());
            a.addString(txtPhNum.getText());
            a.addString(txtPhNum2.getText());
            a.addString(txtPhNum3.getText());
            // a.addString(txtaddedby.getText());

            return a.getPacket();
        } catch (Exception e) {

            MyUtils.showMessage("create Packet");
        }
        return "packet Fail";

    }
}
