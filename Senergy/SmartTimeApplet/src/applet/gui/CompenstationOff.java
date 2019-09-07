package Masters;

import java.awt.*;
import javax.swing.*;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 *
 * @author Chittaranjan Moolya
 */
public class CompenstationOff extends JFrame {

    public JButton bAdd, bCancel;
    private Container contentsPane;
    public MyTextField txtSrNo, txtSearch, txtEmpName, txtCCNO, txtDesign, txtDepartment, txtSectionName;
    public JRadioButton OptCCNo, OptEmpName;
    public JTable tlbCompen;

    public CompenstationOff() {

        setTitle("COMPENSATORY OFF APPLICATION MASTER");           //Tittle Of Form
        setSize(1000, 460);
        setResizable(false);

        MyPanel MainPanel = new MyPanel(new BorderLayout());

        MyPanel PButtons = new MyPanel(new FlowLayout());
        PButtons.setBorder(BorderFactory.createLineBorder(Color.black));

        bAdd = new MyButton("Add New", 2, Color.WHITE) {

            @Override
            public void onClick() {
                System.out.println("You Pressed Add Button");
            }
        };
        bAdd.setToolTipText("Add New Category");
        PButtons.add(bAdd);

        bCancel = new MyButton("Cancel", 2, Color.WHITE) {

            @Override
            public void onClick() {

                System.exit(0);
            }
        };
        bCancel.setToolTipText("Cancel Process");
        PButtons.add(bCancel);

        MainPanel.add(PButtons, BorderLayout.SOUTH);


        MyPanel A1 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyPanel B1 = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        B1.setPreferredSize(new Dimension(986, 35));
        B1.setBorder(BorderFactory.createLineBorder(Color.black));

        MyLabel lblSrNo = new MyLabel(1, " Sr No:");
        B1.add(lblSrNo);

        txtSrNo = new MyTextField();
        txtSrNo.setPreferredSize(new Dimension(350, 22));
        B1.add(txtSrNo);

        A1.add(B1);

//        MyPanel B2 = new MyPanel(new FlowLayout(FlowLayout.CENTER));
//        B2.setBorder(BorderFactory.createLineBorder(Color.black));
//
//        MyLabel lblSearch = new MyLabel(1, "     Search By");
//        lblSearch.setFont(new Font("Verdana", Font.BOLD, 20));
//        lblSearch.setForeground(Color.red);
//        B2.add(lblSearch);
//
//        MyLabel lblSpace1 = new MyLabel();
//        lblSpace1.setPreferredSize(new Dimension(50, 22));
//        B2.add(lblSpace1);
//
//        ButtonGroup OptButtonGroup = new ButtonGroup();
//        OptCCNo = new JRadioButton("CC No", true);
////        OptCCNo.setForeground(new Color(103, 213, 83));
////        OptCCNo.setBackground(new Color(255, 255, 255));
//        OptButtonGroup.add(OptCCNo);
//        B2.add(OptCCNo);
//
//        OptEmpName = new JRadioButton("Employee Name", true);
//        OptButtonGroup.add(OptEmpName);
//        B2.add(OptEmpName);
//
//        txtSearch = new MyTextField();
//        txtSearch.setPreferredSize(new Dimension(410, 22));
//        B2.add(txtSearch);
//
//        A1.add(B2);

        MainPanel.add(A1, BorderLayout.NORTH);


        MyPanel A2 = new MyPanel(new BorderLayout());


        MyPanel C1 = new MyPanel(new FlowLayout());
//        Tablepanel.setPreferredSize(new Dimension(1000, 1000));
//        C1.setBackground(Color.red);
//        Tablepanel.setBorder(BorderFactory.createLineBorder(Color.black));
        tlbCompen = new JTable();
        tlbCompen.setPreferredScrollableViewportSize(new Dimension(975, 160));
        JScrollPane TableContainer = new JScrollPane(tlbCompen);

        C1.add(TableContainer);
        A2.add(C1, BorderLayout.SOUTH);

        MyPanel C2 = new MyPanel(new BorderLayout());

        MyPanel C3 = new MyPanel(new BorderLayout());
        C3.setBorder(BorderFactory.createLineBorder(Color.black));

        MyPanel C4 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyPanel C5 = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        MyLabel lblSelectWorkDate = new MyLabel(1, "Select Work Date :");
//        lblSelectWorkDate.setPreferredSize(new Dimension(50, 22));
        C5.add(lblSelectWorkDate);


        JDatePicker picker = JDateComponentFactory.createJDatePicker();
        JComponent guiElement = (JComponent) picker;
        C5.add(guiElement);

        C4.add(C5);


        MyPanel C6 = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        MyLabel lblLeaveRequired = new MyLabel(1, "              Select Date on Which Leave Require:");
//        lblSelectWorkDate.setPreferredSize(new Dimension(50, 22));
        C6.add(lblLeaveRequired);


        JDatePicker picker2 = JDateComponentFactory.createJDatePicker();
        JComponent guiElement2 = (JComponent) picker2;
        C6.add(guiElement2);

        C4.add(C6);

        C3.add(C4, BorderLayout.SOUTH);


        MyPanel C7 = new MyPanel(new GridLayout(3, 2), "Employee Details");

        MyPanel C8 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyLabel lblEmpCode = new MyLabel(1, "   Employee Code :");
        C8.add(lblEmpCode);

        JComboBox cmboEmpCode = new JComboBox();
        cmboEmpCode.setPreferredSize(new Dimension(200, 22));
        C8.add(cmboEmpCode);

        C7.add(C8);


        MyPanel C9 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyLabel lblEmpName = new MyLabel(1, "       Employee Name :");
        C9.add(lblEmpName);

        txtEmpName = new MyTextField();
        txtEmpName.setPreferredSize(new Dimension(300, 22));
        C9.add(txtEmpName);

        C7.add(C9);


        MyPanel C10 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyLabel lblCCNO = new MyLabel(1, "            CCNo :");
        C10.add(lblCCNO);

        txtCCNO = new MyTextField();
        txtCCNO.setPreferredSize(new Dimension(200, 22));
        C10.add(txtCCNO);

        C7.add(C10);


        MyPanel C11 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyLabel lblDESIGN = new MyLabel(1, "         Designation :");
        C11.add(lblDESIGN);

        txtDesign = new MyTextField();
        txtDesign.setPreferredSize(new Dimension(300, 22));
        C11.add(txtDesign);

        C7.add(C11);

        MyPanel C12 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyLabel lblDepartment = new MyLabel(1, "      Department :");
        C12.add(lblDepartment);

        txtDepartment = new MyTextField();
        txtDepartment.setPreferredSize(new Dimension(200, 22));
        C12.add(txtDepartment);

        C7.add(C12);


        MyPanel C13 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyLabel lblSectionName = new MyLabel(1, "        Section Name :");
        C13.add(lblSectionName);

        txtSectionName = new MyTextField();
        txtSectionName.setPreferredSize(new Dimension(300, 22));
        C13.add(txtSectionName);

        C7.add(C13);

        C3.add(C7, BorderLayout.CENTER);

        C3.add(C4, BorderLayout.SOUTH);

        C2.add(C3, BorderLayout.SOUTH);
        A2.add(C2, BorderLayout.CENTER);


        MainPanel.add(A2, BorderLayout.CENTER);

        contentsPane = getContentPane();
        contentsPane.setLayout(new BorderLayout());
        contentsPane.add(MainPanel, BorderLayout.CENTER);
    }

    public static void main(String args[]) {
        CompenstationOff Compensatory = new CompenstationOff();
        Compensatory.setVisible(true);
    }
}
