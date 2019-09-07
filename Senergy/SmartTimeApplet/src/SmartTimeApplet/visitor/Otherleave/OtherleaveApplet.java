/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.Otherleave;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 *
 * @author nbpatil
 */
public class OtherleaveApplet extends MyApplet implements ActionListener{
private MyButton btnSubmit;
private MyButton btnCancel;
private MyTextField txtLvTpy;
private MyTextField txtRsn;
private JComponent FormDt;
private JComponent ToDt;
private LeaveTableModel tableModel;
private MyTable table;
private Container contentPane;
private MyPanel HomMyPanel;
private JComboBox cmbLvtyp;
private MyTextField txtEmpNo;
private MyTextField txtccNo;
private MyTextField txtEmlId;
private MyTextField txtNm;
private MyTextField txtdes;
private MyTextField txtdiv;
    private MyTextField txtempNorcmdr;
    private MyTextField txtNmrcmdr;
    private MyTextField txtEmlrcmdr;
    private MyTextField txtempNoapp;
    private MyTextField txtempNmapp;
    private MyTextField txtempEmlapp;


    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
        try {
            super.init();
            contentPane=getContentPane();
            HomMyPanel=CreateHomePanel();
            contentPane.add(HomMyPanel);
            contentPane.validate();
            contentPane.repaint();
            
        } catch (Exception e) {
        }
        
       
        // TODO start asynchronous download of heavy resources
    }
    // TODO overwrite start(), stop() and destroy() methods

    
    public MyPanel CreateHomePanel() throws Exception{
    
        MyPanel Mainpan=new MyPanel(new BorderLayout(),"Attendance Time Entry Form");
        Mainpan.setBackground(Color.WHITE);
 
        MyPanel Emppan=new MyPanel(new GridLayout(1,1),"Applicant");
        Emppan.setBackground(Color.WHITE);
        
        MyPanel AppPan=new MyPanel(new GridLayout(2,3,5,5));
        AppPan.setBackground(Color.WHITE);
        
        MyPanel lblPan=new MyPanel(new GridLayout(1,3));
        
        MyPanel txtPan1=new MyPanel(new BorderLayout());
        MyPanel txtPan2=new MyPanel(new BorderLayout());
        MyPanel txtPan3=new MyPanel(new BorderLayout());
        
        
        MyLabel lblcmpId = new MyLabel(1,"Emp No:");
        txtPan1.add(lblcmpId,BorderLayout.WEST); 
        lblcmpId.setForeground(new Color(0,128,0));
        lblPan.add(txtPan1);
        
        //AppPan.add(lblPan);       
       
        txtEmpNo = new MyTextField();
        txtPan1.add(txtEmpNo, BorderLayout.CENTER);
        lblPan.add(txtPan1);
        //AppPan.add(lblPan);
        
        
        MyLabel lblcmpNm = new MyLabel(1,"CC No: ");
        txtPan2.add(lblcmpNm,BorderLayout.WEST);
        lblcmpNm.setForeground(new Color(0,128,0));
        lblPan.add(txtPan2);   
       
        
        txtccNo = new MyTextField();
        txtPan2.add(txtccNo, BorderLayout.CENTER);
        lblPan.add(txtPan2);
        //AppPan.add(lblPan);
        
        
     
        MyLabel lblEmail = new MyLabel(1, "EMail_Id:");
        txtPan3.add(lblEmail,BorderLayout.WEST);
        lblEmail.setForeground(new Color(0,128,0));
        lblPan.add(txtPan3);
        AppPan.add(lblPan);
        Emppan.add(AppPan);
        
        txtEmlId = new MyTextField();
        txtPan3.add(txtEmlId, BorderLayout.CENTER);
        lblPan.add(txtPan3);
        AppPan.add(lblPan);
        Emppan.add(AppPan);
        
      
   
        MyPanel lblPan1=new MyPanel(new GridLayout(1,3));
        
        MyPanel empPan=new MyPanel(new BorderLayout());
        MyPanel empPan1=new MyPanel(new BorderLayout());
        MyPanel empPan2=new MyPanel(new BorderLayout());
        
        
        MyLabel lblName = new MyLabel(1,"Name:  ");
        empPan.add(lblName,BorderLayout.WEST); 
        lblName.setForeground(new Color(0,128,0));
        lblPan1.add(empPan);
        //AppPan.add(lblPan1);       
       
        
        txtNm = new MyTextField();
        empPan.add(txtNm, BorderLayout.CENTER);
        lblPan1.add(empPan);
        //AppPan.add(lblPan);
        
        
        MyLabel lblDesign = new MyLabel(1,"Design:");
        empPan1.add(lblDesign,BorderLayout.WEST);
        lblDesign.setForeground(new Color(0,128,0));
        lblPan1.add(empPan1);
        
        
        txtdes = new MyTextField();
        empPan1.add(txtdes, BorderLayout.CENTER);
        lblPan1.add(empPan1);
        //AppPan.add(lblPan1);   
       
     
        MyLabel lblDiv = new MyLabel(1, "Div:    ");
        empPan2.add(lblDiv,BorderLayout.WEST);
        lblDiv.setForeground(new Color(0,128,0));
        lblPan1.add(empPan2);
        AppPan.add(lblPan1);
        Emppan.add(AppPan);
        
         txtdiv = new MyTextField();
        empPan2.add(txtdiv, BorderLayout.CENTER);
        lblPan1.add(empPan2);
        AppPan.add(lblPan1); 
        Emppan.add(AppPan);
       
       
        MyPanel SubPan=new MyPanel(new BorderLayout()); 
        
        MyPanel CntPan=new MyPanel(new GridLayout(4,1));
        MyPanel TxtPan= new MyPanel(new GridLayout(4,1));
        
        MyPanel cmnPan=new MyPanel(new BorderLayout());  
        
        MyLabel lblNmO = new MyLabel(1, "Leave Type:");
        lblNmO.setForeground(new Color(0,128,0));
        cmnPan.add(lblNmO,BorderLayout.WEST);
        CntPan.add(cmnPan);
      
        MyPanel cmnPan1=new MyPanel(new BorderLayout());
        
        MyLabel lblRsn = new MyLabel(1, "Reason:    ");
        lblRsn.setForeground(new Color(0,128,0));
        cmnPan1.add(lblRsn,BorderLayout.WEST);
        CntPan.add(cmnPan1);
       
        MyPanel cmnPan2=new MyPanel(new BorderLayout());  
        MyLabel lblfrmdt = new MyLabel(1, "Form Date: ");
        lblfrmdt.setForeground(new Color(0,128,0));
        cmnPan2.add(lblfrmdt,BorderLayout.WEST);
        CntPan.add(cmnPan2);
        
        
  
        MyPanel cmnPan3=new MyPanel(new BorderLayout());  
        MyLabel lblToDt = new MyLabel(1, "To Date:   ");
        lblToDt.setForeground(new Color(0,128,0));
        cmnPan3.add(lblToDt,BorderLayout.WEST);
        CntPan.add(cmnPan3);
        SubPan.add(CntPan);
        
        
        
        
       
        cmbLvtyp=new JComboBox();
        cmbLvtyp.addItem("Election Off");
        cmbLvtyp.addItem("RTC OFF");
        cmbLvtyp.addItem("Girisanchar");
        cmbLvtyp.addItem("Family Planning");
        cmbLvtyp.addItem("Sports");
        cmbLvtyp.addItem("Blood Donation");
        cmbLvtyp.addItem("Cultural Leave");
        cmnPan.add(cmbLvtyp,BorderLayout.CENTER);
        TxtPan.add(cmnPan);
        
       
        txtRsn=new MyTextField();
        cmnPan1.add(txtRsn);
        TxtPan.add(cmnPan1);
        SubPan.add(TxtPan,BorderLayout.CENTER);
        
       JDatePicker picker=JDateComponentFactory.createJDatePicker();
       FormDt=(JComponent) picker;
       cmnPan2.add(FormDt);
       TxtPan.add(cmnPan2);
    
       JDatePicker picker1=JDateComponentFactory.createJDatePicker();
       ToDt=(JComponent) picker1;
       cmnPan3.add(ToDt);
       TxtPan.add(cmnPan3);
    
        MyPanel CenPan=new MyPanel(new BorderLayout());
        
        MyPanel RmdrPan=new MyPanel(new GridLayout(1,2));
        RmdrPan.setBackground(Color.WHITE);
        
        MyPanel RmdrPan1=new MyPanel(new GridLayout(3,3,5,5),"Recommender");
        RmdrPan1.setBackground(Color.WHITE);
        
        
         
        MyLabel lblEmp_No = new MyLabel(1,"Emp_No:");
        RmdrPan1.add(lblEmp_No,BorderLayout.WEST); 
        lblEmp_No.setForeground(new Color(0,128,0));
        RmdrPan.add(RmdrPan1);    
       
        txtempNorcmdr = new MyTextField();
        RmdrPan1.add(txtempNorcmdr, BorderLayout.CENTER);
        RmdrPan.add(RmdrPan1);
        //AppPan.add(lblPan);
        
        MyLabel lblName1 = new MyLabel(1,"Name:");
        RmdrPan1.add(lblName1,BorderLayout.WEST);
        lblName1.setForeground(new Color(0,128,0));
        RmdrPan.add(RmdrPan1);  
       
        txtNmrcmdr = new MyTextField();
        RmdrPan1.add(txtNmrcmdr, BorderLayout.CENTER);
        RmdrPan.add(RmdrPan1);
        //AppPan.add(lblPan);
     
        MyLabel lblEmail_Id = new MyLabel(1, "Email_ID:");
        RmdrPan1.add(lblEmail_Id,BorderLayout.WEST);
        lblEmail_Id.setForeground(new Color(0,128,0));
        RmdrPan.add(RmdrPan1);
        CenPan.add(RmdrPan);
        
         txtEmlrcmdr = new MyTextField();
        RmdrPan1.add(txtEmlrcmdr, BorderLayout.CENTER);
        RmdrPan.add(RmdrPan1);
        CenPan.add(RmdrPan);
        //AppPan.add(lblPan);
       
        
        MyPanel AppPan1=new MyPanel(new GridLayout(3,3,5,5),"Approver");
        AppPan1.setBackground(Color.WHITE);
        
        
        MyLabel lblEmp_No1 = new MyLabel(1,"Emp_No:");
        AppPan1.add(lblEmp_No1,BorderLayout.WEST); 
        lblEmp_No1.setForeground(new Color(0,128,0));
        RmdrPan.add(AppPan1);       
       
        
        txtempNoapp = new MyTextField();
        AppPan1.add(txtempNoapp, BorderLayout.CENTER);
        RmdrPan.add(AppPan1);
        //AppPan.add(lblPan);
        
        
        MyLabel lblName2 = new MyLabel(1,"Name:");
        AppPan1.add(lblName2,BorderLayout.WEST);
        lblName2.setForeground(new Color(0,128,0));
        RmdrPan.add(AppPan1);    
       
        txtempNmapp = new MyTextField();
        AppPan1.add(txtempNmapp, BorderLayout.CENTER);
        RmdrPan.add(AppPan1);
        //AppPan.add(lblPan);
        
        MyLabel lblEmail_Id1 = new MyLabel(1, "Email_ID:");
        AppPan1.add(lblEmail_Id1,BorderLayout.WEST);
        lblEmail_Id1.setForeground(new Color(0,128,0));
        RmdrPan.add(AppPan1);
        CenPan.add(RmdrPan);
        
        txtempEmlapp = new MyTextField();
        AppPan1.add(txtempEmlapp, BorderLayout.CENTER);
        RmdrPan.add(AppPan1);
        //AppPan.add(lblPan);      

        
        MyPanel btnMainPan=new MyPanel(new BorderLayout());
        
        MyPanel btnPan=new MyPanel(new GridLayout(1,3),"Applicant");
        btnPan.setBackground(Color.WHITE);
        
        btnSubmit =new MyButton("Submit",2, Color.WHITE) {

            @Override
            public void onClick() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        btnPan.add(btnSubmit);
        
       
        
        btnCancel=new MyButton("Close", 2, Color.WHITE) {

            @Override
            public void onClick() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        btnPan.add(btnCancel);
        btnMainPan.add(btnPan,BorderLayout.SOUTH);
        
        Mainpan.setLayout(new GridLayout(4,1));
        Mainpan.add(Emppan);
        Mainpan.add(SubPan);
        Mainpan.add(CenPan);
        Mainpan.add(btnMainPan);
        contentPane.add(Mainpan,BorderLayout.CENTER);
        setSize(750,600);
        setVisible(true);
        //this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        
        
    return Mainpan;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    private void getApplicant(String EmpCode) {

        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");

            myHTTP.openOS();
            myHTTP.println("getApplicant");
            myHTTP.println(EmpCode);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            Depacketizer dp = new Depacketizer(result);
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);

            } else {
                if (!"Not Found".equals(result)) {

                    //txtApplicant.setText(dp.getString() + "      " + dp.getString() + "    " + dp.getString() + "" + dp.getString());

                } else {
                    //txtApplicant.setText("");
                    //txtcmpNm.setText("");

                    MyUtils.showMessage("Applicant not present! Enter another Compnayid");
                    return;
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("get Applicant Record", ex);
        }
    }
    
    //for  usiong recommonder
     private void getRecoomonder(String EmpCode) {

        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");

            myHTTP.openOS();
            myHTTP.println("getApplicant");
            myHTTP.println(EmpCode);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            Depacketizer dp = new Depacketizer(result);
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);

            } else {
                if (!"Not Found".equals(result)) {

                    //txtApplicant.setText(dp.getString() + "      " + dp.getString() + "    " + dp.getString() + "" + dp.getString());

                } else {
                    //txtApplicant.setText("");
                    //txtcmpNm.setText("");

                    MyUtils.showMessage("Applicant not present! Enter another Compnayid");
                    return;
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("get Applicant Record", ex);
        }
     }

      private void getApprover(String EmpCode) {

        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");

            myHTTP.openOS();
            myHTTP.println("getApplicant");
            myHTTP.println(EmpCode);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            Depacketizer dp = new Depacketizer(result);
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);

            } else {
                if (!"Not Found".equals(result)) {

                    //txtApplicant.setText(dp.getString() + "      " + dp.getString() + "    " + dp.getString() + "" + dp.getString());

                } else {
                    //txtApplicant.setText("");
                    //txtcmpNm.setText("");

                    MyUtils.showMessage("Approver not present! Enter another Approver");
                    return;
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("get Approver Record", ex);
        }
     }

}
