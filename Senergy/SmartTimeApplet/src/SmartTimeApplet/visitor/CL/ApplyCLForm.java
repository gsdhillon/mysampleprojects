/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.CL;

/**
 *
 * @author Roop
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import lib.session.MyApplet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;



/**
 *
 * @author nbp
 */
public class ApplyCLForm extends JFrame implements ActionListener{
private MyButton btnSubmit;
private MyButton btnCancel;
private MyTextField txtNmOfOrz;
private MyTextField txtPurOfVst;
private JComponent FormDt;
private JComponent FormTm;
private JComponent ToDt;
private JComponent ToTm;


Container con;

public ApplyCLForm(){

        con=getContentPane();
        
        MyPanel Mainpan=new MyPanel(new BorderLayout(),"CL Form");
        Mainpan.setBackground(Color.WHITE);
 
        MyPanel Emppan=new MyPanel(new GridLayout(1,1),"Applicant");
        Emppan.setBackground(Color.WHITE);
        
        MyPanel AppPan=new MyPanel(new GridLayout(2,3));
        AppPan.setBackground(Color.WHITE);
        
        MyPanel lblPan=new MyPanel(new GridLayout(1,3));
        
        MyLabel lblcmpId = new MyLabel(1,"Emp No:");
        lblPan.add(lblcmpId,BorderLayout.WEST); 
        lblcmpId.setForeground(new Color(0,128,0));
        AppPan.add(lblPan);       
       
        
        MyLabel lblcmpNm = new MyLabel(1,"CC No: ");
        lblPan.add(lblcmpNm,BorderLayout.CENTER);
        lblcmpNm.setForeground(new Color(0,128,0));
        AppPan.add(lblPan);   
       
     
        MyLabel lblEmail = new MyLabel(1, "EMail_Id:");
        lblPan.add(lblEmail,BorderLayout.EAST);
        lblEmail.setForeground(new Color(0,128,0));
        AppPan.add(lblPan);
        Emppan.add(AppPan);
        
        
      
   
        MyPanel lblPan1=new MyPanel(new GridLayout(1,3));
        
        MyLabel lblName = new MyLabel(1,"Name:");
        lblPan1.add(lblName,BorderLayout.WEST); 
        lblName.setForeground(new Color(0,128,0));
        AppPan.add(lblPan1);       
       
        
        MyLabel lblDesign = new MyLabel(1,"Design:");
        lblPan1.add(lblDesign,BorderLayout.CENTER);
        lblDesign.setForeground(new Color(0,128,0));
        AppPan.add(lblPan1);   
       
     
        MyLabel lblDiv = new MyLabel(1, "Div:");
        lblPan1.add(lblDiv,BorderLayout.EAST);
        lblDiv.setForeground(new Color(0,128,0));
        AppPan.add(lblPan1);
        Emppan.add(AppPan);
       
        MyPanel SubPan=new MyPanel(new BorderLayout()); 
        
        MyPanel CntPan=new MyPanel(new GridLayout(6,1));
        MyPanel TxtPan= new MyPanel(new GridLayout(6,1));
        
        MyPanel cmnPan=new MyPanel(new BorderLayout());  
        
        MyLabel lblNmO = new MyLabel(1, "Name Of Orgnization:");
        lblNmO.setForeground(new Color(0,128,0));
        cmnPan.add(lblNmO,BorderLayout.WEST);
        CntPan.add(cmnPan);
      
        MyPanel cmnPan1=new MyPanel(new BorderLayout());
        
        MyLabel lblPrvst = new MyLabel(1, "Purpose Of Visit:   ");
        lblPrvst.setForeground(new Color(0,128,0));
        cmnPan1.add(lblPrvst,BorderLayout.WEST);
        CntPan.add(cmnPan1);
       
        MyPanel cmnPan2=new MyPanel(new BorderLayout());  
        MyLabel lblfrmdt = new MyLabel(1, "Form Date:          ");
        lblfrmdt.setForeground(new Color(0,128,0));
        cmnPan2.add(lblfrmdt,BorderLayout.WEST);
        CntPan.add(cmnPan2);
        
        MyPanel cmnPan3=new MyPanel(new BorderLayout());  
        MyLabel lblfrmtm = new MyLabel(1, "Form Time:          ");
        lblfrmtm.setForeground(new Color(0,128,0));
        cmnPan3.add(lblfrmtm,BorderLayout.WEST);
        CntPan.add(cmnPan3);
  
        MyPanel cmnPan4=new MyPanel(new BorderLayout());  
        MyLabel lblToDt = new MyLabel(1, "To Date:            ");
        lblToDt.setForeground(new Color(0,128,0));
        cmnPan4.add(lblToDt,BorderLayout.WEST);
        CntPan.add(cmnPan4);
        
        MyPanel cmnPan5=new MyPanel(new BorderLayout());
        MyLabel lblToTm = new MyLabel(1, "To Time:            ");
        lblToTm.setForeground(new Color(0,128,0));
        cmnPan5.add(lblToTm,BorderLayout.WEST);
        CntPan.add(cmnPan5);
        SubPan.add(CntPan);
          
       
        txtNmOfOrz=new MyTextField();
        cmnPan.add(txtNmOfOrz,BorderLayout.CENTER);
        TxtPan.add(cmnPan);
       
        txtPurOfVst=new MyTextField();
        cmnPan1.add(txtPurOfVst);
        TxtPan.add(cmnPan1);
         
        JDatePicker picker = JDateComponentFactory.createJDatePicker();
        FormDt = (JComponent) picker;
        cmnPan2.add(FormDt);
        TxtPan.add(cmnPan2);
          
        FormTm = new JSpinner(new SpinnerDateModel());
        cmnPan3.add(FormTm);
        TxtPan.add(cmnPan3);
       
        JDatePicker picker1 = JDateComponentFactory.createJDatePicker();
        ToDt = (JComponent) picker;
        cmnPan4.add(ToDt);
        TxtPan.add(cmnPan4);
        
        ToTm = new JSpinner(new SpinnerDateModel());
        cmnPan5.add(ToTm);
        TxtPan.add(cmnPan5);
        SubPan.add(TxtPan,BorderLayout.CENTER);
      
       
       
        
        
        MyPanel CenPan=new MyPanel(new BorderLayout());
        
        MyPanel RmdrPan=new MyPanel(new GridLayout(1,2));
        RmdrPan.setBackground(Color.WHITE);
        
        MyPanel RmdrPan1=new MyPanel(new GridLayout(3,1),"Recommender");
        RmdrPan1.setBackground(Color.WHITE);
        
        
         
        MyLabel lblEmp_No = new MyLabel(1,"Emp_No:");
        RmdrPan1.add(lblEmp_No,BorderLayout.WEST); 
        lblEmp_No.setForeground(new Color(0,128,0));
        RmdrPan.add(RmdrPan1);    
       
        
        MyLabel lblName1 = new MyLabel(1,"Name:");
        RmdrPan1.add(lblName1,BorderLayout.WEST);
        lblName1.setForeground(new Color(0,128,0));
        RmdrPan.add(RmdrPan1);  
       
     
        MyLabel lblEmail_Id = new MyLabel(1, "Email_ID:");
        RmdrPan1.add(lblEmail_Id,BorderLayout.WEST);
        lblEmail_Id.setForeground(new Color(0,128,0));
        RmdrPan.add(RmdrPan1);
        CenPan.add(RmdrPan);
       
        
        MyPanel AppPan1=new MyPanel(new GridLayout(3,1),"Approver");
        AppPan1.setBackground(Color.WHITE);
        
        
        MyLabel lblEmp_No1 = new MyLabel(1,"Emp_No:");
        AppPan1.add(lblEmp_No1,BorderLayout.WEST); 
        lblEmp_No1.setForeground(new Color(0,128,0));
        RmdrPan.add(AppPan1);       
       
        
        MyLabel lblName2 = new MyLabel(1,"Name:");
        AppPan1.add(lblName2,BorderLayout.WEST);
        lblName2.setForeground(new Color(0,128,0));
        RmdrPan.add(AppPan1);    
       
     
        MyLabel lblEmail_Id1 = new MyLabel(1, "Email_ID:");
        AppPan1.add(lblEmail_Id1,BorderLayout.WEST);
        lblEmail_Id1.setForeground(new Color(0,128,0));
        RmdrPan.add(AppPan1);
        CenPan.add(RmdrPan);
        
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
        
       
        
        btnCancel=new MyButton("Cancel", 2, Color.WHITE) {

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
        con.add(Mainpan,BorderLayout.CENTER);
        setSize(750,600);
        setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        
}
    
public static void main(String args[]) {
    ApplyCLForm applyCl=new ApplyCLForm();
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
   
}
