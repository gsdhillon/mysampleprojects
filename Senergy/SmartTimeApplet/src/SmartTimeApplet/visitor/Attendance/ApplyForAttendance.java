/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.Attendance;

import lib.session.MyApplet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Normalizer.Form;
import javax.swing.*;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 *
 * @author Roop
 */
public class ApplyForAttendance extends JFrame implements ActionListener {

    private MyButton btnSubmit;
    private MyButton btnCancel;
    private MyTextField txtRFNSIC;
    private MyTextField txtDt;
    private JSpinner InTime; 
    private JSpinner OutTime; 
    //private JComponent Form;
    //private JComboBox cmbCausalLeave;
    Container con;

    public ApplyForAttendance() {

        con = getContentPane();
        MyPanel Mainpan = new MyPanel(new BorderLayout(), "Attendance Time Entery Form");
        Mainpan.setBackground(Color.white);

        MyPanel Emppan = new MyPanel(new GridLayout(1, 1), "Applicant");
        Emppan.setBackground(Color.WHITE);

        MyPanel AppPan = new MyPanel(new GridLayout(2, 3));
        AppPan.setBackground(Color.WHITE);

        MyPanel lblPan = new MyPanel(new GridLayout(1, 3));

        MyLabel lblcmpId = new MyLabel(1, "Emp No:");
        lblPan.add(lblcmpId, BorderLayout.WEST);
        lblcmpId.setForeground(new Color(0, 128, 0));
        AppPan.add(lblPan);


        MyLabel lblcmpNm = new MyLabel(1, "CC No: ");
        lblPan.add(lblcmpNm, BorderLayout.CENTER);
        lblcmpNm.setForeground(new Color(0, 128, 0));
        AppPan.add(lblPan);


        MyLabel lblEmail = new MyLabel(1, "EMail_Id:");
        lblPan.add(lblEmail, BorderLayout.EAST);
        lblEmail.setForeground(new Color(0, 128, 0));
        AppPan.add(lblPan);
        Emppan.add(AppPan);




        MyPanel lblPan1 = new MyPanel(new GridLayout(1, 3));

        MyLabel lblName = new MyLabel(1, "Name:");
        lblPan1.add(lblName, BorderLayout.WEST);
        lblName.setForeground(new Color(0, 128, 0));
        AppPan.add(lblPan1);


        MyLabel lblDesign = new MyLabel(1, "Design:");
        lblPan1.add(lblDesign, BorderLayout.CENTER);
        lblDesign.setForeground(new Color(0, 128, 0));
        AppPan.add(lblPan1);


        MyLabel lblDiv = new MyLabel(1, "Div:");
        lblPan1.add(lblDiv, BorderLayout.EAST);
        lblDiv.setForeground(new Color(0, 128, 0));
        AppPan.add(lblPan1);
        Emppan.add(AppPan);




        MyPanel SubPan2 = new MyPanel(new GridLayout(3, 3));

        MyPanel addlPan = new MyPanel(new BorderLayout());
        MyLabel lbladddl = new MyLabel(1, "Reason For Not Swiping ICard:");
        lbladddl.setForeground(new Color(0, 128, 0));
        addlPan.add(lbladddl, BorderLayout.WEST);
        SubPan2.add(addlPan);


        txtRFNSIC = new MyTextField();
        addlPan.add(txtRFNSIC);
        SubPan2.add(addlPan);


        MyPanel GrpPan = new MyPanel(new BorderLayout());
        MyLabel lblGrnd = new MyLabel(1, "Date:                        ");
        lblGrnd.setForeground(new Color(0, 128, 0));
        GrpPan.add(lblGrnd, BorderLayout.WEST);
        SubPan2.add(GrpPan);


        txtDt = new MyTextField();
        GrpPan.add(txtDt);
        SubPan2.add(GrpPan);


        MyPanel GrpPan1 = new MyPanel(new BorderLayout());
        MyLabel lblSNo = new MyLabel(1, "Time Details:");
        GrpPan1.add(lblSNo, BorderLayout.WEST);
        lblSNo.setForeground(new Color(0, 128, 0));
        SubPan2.add(GrpPan1);

        MyPanel AttPan=new MyPanel(new BorderLayout());
        AttPan.setBackground(Color.white);
       
        MyPanel AttPan1=new MyPanel(new GridLayout(1,4));
         
        MyLabel lbltimein=new MyLabel(1,"TimeIn:HH");
        lbltimein.setForeground(new Color(0,128,0));
        AttPan1.add(lbltimein,BorderLayout.CENTER);
        AttPan.add(AttPan1);
        SubPan2.add(AttPan);
         
         JSpinner  InTime = new JSpinner(new SpinnerDateModel());
         AttPan1.add(InTime,BorderLayout.CENTER);
         AttPan.add(AttPan1);
         SubPan2.add(AttPan1);
        
        MyLabel lbltimeout=new MyLabel(1,"TimeOut:HH");
        lbltimeout.setForeground(new Color(0,128,0));
        AttPan1.add(lbltimeout,BorderLayout.WEST);
        AttPan.add(AttPan1);
        SubPan2.add(AttPan);
         
      
         JSpinner OutTime=new JSpinner (new SpinnerDateModel());
         AttPan1.add(OutTime,BorderLayout.EAST);
         AttPan.add(AttPan1);
         SubPan2.add(AttPan);
         
         
         MyPanel CenPan = new MyPanel(new BorderLayout());

         MyPanel RmdrPan = new MyPanel(new GridLayout(1, 2));
         RmdrPan.setBackground(Color.WHITE);

         MyPanel RmdrPan1 = new MyPanel(new GridLayout(3, 1), "Recommender");
         RmdrPan1.setBackground(Color.WHITE);



        MyLabel lblEmp_No = new MyLabel(1, "Emp_No:");
        RmdrPan1.add(lblEmp_No, BorderLayout.WEST);
        lblEmp_No.setForeground(new Color(0, 128, 0));
        RmdrPan.add(RmdrPan1);


        MyLabel lblName1 = new MyLabel(1, "Name:");
        RmdrPan1.add(lblName1, BorderLayout.WEST);
        lblName1.setForeground(new Color(0, 128, 0));
        RmdrPan.add(RmdrPan1);


        MyLabel lblEmail_Id = new MyLabel(1, "Email_ID:");
        RmdrPan1.add(lblEmail_Id, BorderLayout.WEST);
        lblEmail_Id.setForeground(new Color(0, 128, 0));
        RmdrPan.add(RmdrPan1);
        CenPan.add(RmdrPan);

        MyPanel AppPan1 = new MyPanel(new GridLayout(3, 1), "Approver");
        AppPan1.setBackground(Color.WHITE);


        MyLabel lblEmp_No1 = new MyLabel(1, "Emp_No:");
        AppPan1.add(lblEmp_No1, BorderLayout.WEST);
        lblEmp_No1.setForeground(new Color(0, 128, 0));
        RmdrPan.add(AppPan1);


        MyLabel lblName2 = new MyLabel(1, "Name:");
        AppPan1.add(lblName2, BorderLayout.WEST);
        lblName2.setForeground(new Color(0, 128, 0));
        RmdrPan.add(AppPan1);


        MyLabel lblEmail_Id1 = new MyLabel(1, "Email_ID:");
        AppPan1.add(lblEmail_Id1, BorderLayout.WEST);
        lblEmail_Id1.setForeground(new Color(0, 128, 0));
        RmdrPan.add(AppPan1);
        CenPan.add(RmdrPan);


        MyPanel btnMainPan = new MyPanel(new BorderLayout());

        MyPanel btnPan = new MyPanel(new GridLayout(1, 3), "Applicant");
        btnPan.setBackground(Color.WHITE);

        btnSubmit = new MyButton("Submit", 2, Color.WHITE) {

            @Override
            public void onClick() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        btnPan.add(btnSubmit);


        btnCancel = new MyButton("Close", 2, Color.WHITE) {

            @Override
            public void onClick() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        btnPan.add(btnCancel);
        btnMainPan.add(btnPan, BorderLayout.SOUTH);

        Mainpan.setLayout(new GridLayout(4, 1));
        Mainpan.add(Emppan);
        Mainpan.add(SubPan2);
        Mainpan.add(CenPan);
        Mainpan.add(btnMainPan);
        con.add(Mainpan, BorderLayout.CENTER);
        setSize(750, 600);
        setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public static void main(String args[]) {
        ApplyForAttendance applyForAttendance = new ApplyForAttendance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
