/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.OD;

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
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
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
public class ApplyOD extends JFrame implements ActionListener{
private MyButton btnAddMoreCoFF;
private MyButton btnSubmit;
private MyButton btnCancel;
private MyTextField txtGrp;
private MyTextField txtadddurlv;
private MyTextField txtaddMore;
private JComponent Form;
private JComboBox cmbCausalLeave;

Container con;

public ApplyOD(){

        con=getContentPane();
        MyPanel Mainpan=new MyPanel(new BorderLayout(),"OD Duty Form");
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
       
        MyPanel SubPan=new MyPanel(new GridLayout(3,1)); 
        
        MyPanel SubPan2=new MyPanel(new GridLayout(2,2));
        
        MyPanel addlPan=new MyPanel(new BorderLayout());
        MyLabel lbladddl = new MyLabel(1, "Address During Leave:");
        lbladddl.setForeground(new Color(0,128,0));
        addlPan.add(lbladddl,BorderLayout.WEST);
        SubPan2.add(addlPan);
        SubPan.add(SubPan2);
          
        txtadddurlv=new MyTextField();
        addlPan.add(txtadddurlv);
        SubPan2.add(addlPan);
        SubPan.add(SubPan2);
        
        MyPanel GrpPan=new MyPanel(new BorderLayout());
        MyLabel lblGrnd = new MyLabel(1, "Ground:              ");
        lblGrnd.setForeground(new Color(0,128,0));
        GrpPan.add(lblGrnd,BorderLayout.WEST);
        SubPan2.add(GrpPan);
        SubPan.add(SubPan2);
        
        
        txtGrp = new MyTextField();
        GrpPan.add(txtGrp);
        SubPan2.add(GrpPan);
        SubPan.add(SubPan2);
       
         
                
        MyPanel SNoPan=new MyPanel(new GridLayout(2,3));
        SNoPan.setBackground(Color.WHITE);
        
        MyPanel SNoPan1=new MyPanel(new GridLayout(1,3));
        SNoPan.setBackground(Color.WHITE);
        
        MyLabel lblSNo = new MyLabel(1,"SNo:");
        SNoPan1.add(lblSNo,BorderLayout.WEST); 
        lblSNo.setForeground(new Color(0,128,0));
        SNoPan.add(SNoPan1);      
       
        
        MyLabel lblCoFFdt = new MyLabel(1,"Leave Date:");
        SNoPan1.add(lblCoFFdt,BorderLayout.WEST);
        lblCoFFdt.setForeground(new Color(0,128,0));
        SNoPan.add(SNoPan1);    
       
     
        MyLabel lblCoFFInL = new MyLabel(1, "Leave Type:");
        SNoPan1.add(lblCoFFInL,BorderLayout.WEST);
        lblCoFFInL.setForeground(new Color(0,128,0));
        SNoPan.add(SNoPan1); 
        SubPan.add(SNoPan);
        
        MyPanel SNoPan2=new MyPanel(new GridLayout(1,3));
        
        MyLabel lbltest=new MyLabel(1,"1.");
        SNoPan2.add(lbltest,BorderLayout.WEST);
        SNoPan.add(SNoPan2);
        
        JDatePicker picker = JDateComponentFactory.createJDatePicker();
        Form = (JComponent) picker;
        SNoPan2.add(Form,BorderLayout.CENTER);
        SNoPan.add(SNoPan2);
       
      
        cmbCausalLeave=new JComboBox();
        cmbCausalLeave.addItem("Casual Leave");
        cmbCausalLeave.addItem("Half Q Forenoon");
        cmbCausalLeave.addItem("Half Q Afternoon");
        cmbCausalLeave.addItem("Special CL");
        SNoPan2.add(cmbCausalLeave,BorderLayout.CENTER);
        SNoPan.add(SNoPan2);
        SubPan.add(SNoPan);
       
      
        
        MyPanel AddPan=new MyPanel(new GridLayout(1,2));
        AddPan.setBackground(Color.WHITE);
     
          
        MyPanel AddPan1=new MyPanel(new BorderLayout());
        txtaddMore=new MyTextField();
        
        AddPan1.add(txtaddMore,BorderLayout.CENTER);
        AddPan.add(AddPan1);
        
        
        btnAddMoreCoFF=new MyButton("AddMoreCL",2,Color.WHITE) {

            @Override
            public void onClick() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        AddPan1.add(btnAddMoreCoFF,BorderLayout.EAST);
        AddPan.add(AddPan1);
        SubPan.add(AddPan);
        
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
ApplyOD ApplyOd=new ApplyOD();
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
   
}
