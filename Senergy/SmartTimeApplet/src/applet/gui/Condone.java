/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Masters;

import java.awt.*;
import java.util.Date;
import javax.swing.*;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;

/**
 *
 * @author Chittaranjan Moolya
 */
public class Condone extends JFrame {

    private Container contentsPane;
    public JButton bAdd, bCancel;
    public MyTextField txtCondoneTime;
    public JSpinner CondoneTime;

    public Condone() {


        setTitle("Condone Master");           //Tittle Of Form
        setSize(500, 250);
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
        bAdd.setToolTipText("Add New Condone");
        PButtons.add(bAdd);

        bCancel = new MyButton("Cancel", 2, Color.WHITE) {

            @Override
            public void onClick() {

                System.exit(0);
            }
        };
        bCancel.setToolTipText("Cancel Process");
        PButtons.add(bCancel);

        MainPanel.add(PButtons, BorderLayout.SOUTH//<editor-fold defaultstate="collapsed" desc="It is a Main Panel Of this Form">
                );
        //</editor-fold>


        MyPanel A1 = new MyPanel(new GridLayout(5, 1));
        A1.setBorder(BorderFactory.createLineBorder(Color.black));

        MyPanel A2 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyLabel lblCondoneDate = new MyLabel(1, "  Condone  Date :");
        A2.add(lblCondoneDate);

        JDatePicker picker = JDateComponentFactory.createJDatePicker();
        JComponent guiElement = (JComponent) picker;
        A2.add(guiElement);

        A1.add(A2);


        MyPanel A3 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyLabel lblCondoneShift = new MyLabel(1, "  Condone shift :");
        A3.add(lblCondoneShift);

        JComboBox cmboEmpCode = new JComboBox();
        cmboEmpCode.setPreferredSize(new Dimension(200, 22));
        A3.add(cmboEmpCode);

        JCheckBox ChkAllShift = new JCheckBox(" All Shift");
        A3.add(ChkAllShift);

        A1.add(A3);

        MyPanel A4 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyLabel lblCondoneMode = new MyLabel(1, "   Condone Mode :");
        A4.add(lblCondoneMode);

        JComboBox cmboCondoneMode = new JComboBox();
        cmboCondoneMode.setPreferredSize(new Dimension(200, 22));
        A4.add(cmboCondoneMode);

        A1.add(A4);

        MyPanel A5 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyLabel lblCondoneTime = new MyLabel(1, "   Condone Time :");
        A5.add(lblCondoneTime);

        CondoneTime = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(CondoneTime, "HH:mm:ss");
        timeEditor.setForeground(new Color(103, 213, 83));
        CondoneTime.setEditor(timeEditor);
        CondoneTime.setValue(new Date()); // will only show the current time
        CondoneTime.setPreferredSize(new Dimension(200, 25));

        A5.add(CondoneTime);

        A1.add(A5);

        MyPanel A6 = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        MyLabel lblCondoneReason = new MyLabel(1, " Condone Reason :");
        A6.add(lblCondoneReason);

        txtCondoneTime = new MyTextField();
        txtCondoneTime.setPreferredSize(new Dimension(300, 22));
        A6.add(txtCondoneTime);

        A1.add(A6);


        MainPanel.add(A1, BorderLayout.CENTER);

        contentsPane = getContentPane();
        contentsPane.setLayout(new BorderLayout());
        contentsPane.add(MainPanel, BorderLayout.CENTER);

    }

    public static void main(String args[]) {
        Condone Condonee = new Condone();
        Condonee.setVisible(true);
    }
}
