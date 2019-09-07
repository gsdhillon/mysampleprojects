/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.OutputStatus;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author pradnya
 */
public class OutputStatus extends MyPanel {

    public JComboBox cmbRdrNo;
    public JCheckBox[] chk0d = new JCheckBox[16];
    public JCheckBox[] chk1d = new JCheckBox[16];
    public JCheckBox[] chk2d = new JCheckBox[16];
    public JCheckBox[] chk3d = new JCheckBox[16];
    public JCheckBox[] chkLcald = new JCheckBox[16];
    public JCheckBox[] chkALRMd = new JCheckBox[16];
    public JCheckBox[] chkIPd = new JCheckBox[8];
    public JTextField txtOP0, txt1P0, txt2P0, txt3P0, txtLclOP0, txtAlarmTone;
    public MyButton btnGet, btnSet, btnExt;
    String readerNo;
    String readerList[];
    OutputStatusApplet OutputApplet;
    int Checked[] = new int[21];

    public OutputStatus(final OutputStatusApplet OutputApplet, String readerNo, String readerList[]) {
        this.OutputApplet = OutputApplet;
        this.readerNo = readerNo;
        MyPanel MainPanel = new MyPanel(new BorderLayout());
        MyPanel panNorth = new MyPanel(new FlowLayout(FlowLayout.LEFT), "Reader No");
        MyLabel lblRdrNo = new MyLabel(1, "Reader No:");
        panNorth.add(lblRdrNo);

        cmbRdrNo = new JComboBox(readerList);
        cmbRdrNo.setSelectedItem(readerNo);
        cmbRdrNo.setPreferredSize(new Dimension(50, 20));
        panNorth.add(cmbRdrNo);
        MainPanel.add(panNorth, BorderLayout.NORTH);

        MyPanel panCenter = new MyPanel(new BorderLayout());
        MyPanel panOPStat = new MyPanel(new GridLayout(1, 8, 5, 5), "Output Status");

        MyPanel panOPStat0 = new MyPanel(new GridLayout(20, 1));

        MyLabel lblFill7 = new MyLabel();
        panOPStat0.add(lblFill7);

        MyLabel lblExtSW = new MyLabel(1, "EXIT SW");
        panOPStat0.add(lblExtSW);

        MyLabel lblDoor = new MyLabel(1, "DOOR");
        panOPStat0.add(lblDoor);

        MyLabel lblTamp = new MyLabel(1, "TAMPER");
        panOPStat0.add(lblTamp);

        MyLabel lblDr45 = new MyLabel(1, "DOOR 45");
        panOPStat0.add(lblDr45);

        MyLabel[] lblFill = new MyLabel[6];

        for (int i = 0; i <= 5; i++) {
            lblFill[i] = new MyLabel();
            panOPStat0.add(lblFill[i]);
        }

        MyLabel lblDURS = new MyLabel(1, "DURESS");
        panOPStat0.add(lblDURS);

        MyLabel lblMutiRj = new MyLabel(1, "MULTI REJECT");
        panOPStat0.add(lblMutiRj);

        MyLabel lblHost = new MyLabel(1, "HOST");
        panOPStat0.add(lblHost);

        MyLabel lblEmpCard = new MyLabel(1, "EMP CARD");
        panOPStat0.add(lblEmpCard);

        MyLabel lblVisCard = new MyLabel(1, "VISITOR CARD");
        panOPStat0.add(lblVisCard);

        MyLabel lblInVCard = new MyLabel(1, "INVALID CARD");
        panOPStat0.add(lblInVCard);

        MyLabel lblFill8 = new MyLabel();
        panOPStat0.add(lblFill8);

        MyLabel lblDTim = new MyLabel(1, "DELAY TIME");
        panOPStat0.add(lblDTim);

        MyLabel lblInSec = new MyLabel(1, "(1-25 Sec)");
        panOPStat0.add(lblInSec);

        panOPStat.add(panOPStat0);

        MyPanel panOPStat1 = new MyPanel(new GridLayout(20, 1));

        MyLabel lblOP0 = new MyLabel(1, "O/P 0");
        panOPStat1.add(lblOP0);

        for (int i = 0; i <= 15; i++) {
            chk0d[i] = new JCheckBox("d" + i);
            chk0d[i].setBackground(new Color(250, 250, 250));
            panOPStat1.add(chk0d[i]);
        }

        MyLabel lblFill9 = new MyLabel();
        panOPStat1.add(lblFill9);

        txtOP0 = new JTextField();
        txtOP0.setText("1");
        panOPStat1.add(txtOP0);

        panOPStat.add(panOPStat1);

        MyPanel panOPStat2 = new MyPanel(new GridLayout(20, 1));

        MyLabel lbl1P0 = new MyLabel(1, "O/P 1");
        panOPStat2.add(lbl1P0);

        for (int i = 0; i <= 15; i++) {
            chk1d[i] = new JCheckBox("d" + i);
            chk1d[i].setBackground(new Color(250, 250, 250));
            panOPStat2.add(chk1d[i]);
        }

        MyLabel lblFill10 = new MyLabel();
        panOPStat2.add(lblFill10);

        txt1P0 = new JTextField();
        txt1P0.setText("1");
        panOPStat2.add(txt1P0);

        panOPStat.add(panOPStat2);

        MyPanel panOPStat3 = new MyPanel(new GridLayout(20, 1));

        MyLabel lbl2P0 = new MyLabel(1, "O/P 2");
        panOPStat3.add(lbl2P0);

        for (int i = 0; i <= 15; i++) {
            chk2d[i] = new JCheckBox("d" + i);
            chk2d[i].setBackground(new Color(250, 250, 250));
            panOPStat3.add(chk2d[i]);
        }

        MyLabel lblFill11 = new MyLabel();
        panOPStat3.add(lblFill11);

        txt2P0 = new JTextField();
        txt2P0.setText("1");
        panOPStat3.add(txt2P0);

        panOPStat.add(panOPStat3);

        MyPanel panOPStat4 = new MyPanel(new GridLayout(20, 1));

        MyLabel lbl3P0 = new MyLabel(1, "O/P 3");
        panOPStat4.add(lbl3P0);

        for (int i = 0; i <= 15; i++) {
            chk3d[i] = new JCheckBox("d" + i);
            chk3d[i].setBackground(new Color(250, 250, 250));
            panOPStat4.add(chk3d[i]);
        }
        MyLabel lblFill12 = new MyLabel();
        panOPStat4.add(lblFill12);

        txt3P0 = new JTextField();
        txt3P0.setText("1");
        panOPStat4.add(txt3P0);

        panOPStat.add(panOPStat4);

        MyPanel panOPStat5 = new MyPanel(new GridLayout(20, 1));

        MyLabel lblLclP0 = new MyLabel(1, "Alarm Tone");
        panOPStat5.add(lblLclP0);

        for (int i = 0; i <= 15; i++) {
            chkLcald[i] = new JCheckBox("d" + i);
            chkLcald[i].setBackground(new Color(250, 250, 250));
            panOPStat5.add(chkLcald[i]);
        }

        MyLabel lblFill13 = new MyLabel();
        panOPStat5.add(lblFill13);

        txtLclOP0 = new JTextField();
        txtLclOP0.setText("1");
        panOPStat5.add(txtLclOP0);

        panOPStat.add(panOPStat5);

        MyPanel panOPStat6 = new MyPanel(new GridLayout(20, 1));

        MyLabel lblALRMP0 = new MyLabel(1, "Local O/P 0");
        panOPStat6.add(lblALRMP0);

        for (int i = 0; i <= 15; i++) {
            chkALRMd[i] = new JCheckBox("d" + i);
            chkALRMd[i].setBackground(new Color(250, 250, 250));
            panOPStat6.add(chkALRMd[i]);
        }

        MyLabel lblFill14 = new MyLabel();
        panOPStat6.add(lblFill14);

        txtAlarmTone = new JTextField();
        txtAlarmTone.setText("1");
        panOPStat6.add(txtAlarmTone);
        panOPStat.add(panOPStat6);

        MyPanel panOPStat7 = new MyPanel(new GridLayout(20, 1));
        MyLabel lblIPMask = new MyLabel(1, "Input Mask");
        panOPStat7.add(lblIPMask);

        for (int i = 0; i <= 7; i++) {
            chkIPd[i] = new JCheckBox("d" + i);
            chkIPd[i].setBackground(new Color(250, 250, 250));
            panOPStat7.add(chkIPd[i]);
        }
        chkIPd[0].setText("d0 (Exit SW)");
        chkIPd[1].setText("d1 (Door)");
        chkIPd[2].setText("d2 (Tamper)");
        chkIPd[3].setText("d3 (Door 45)");

        panOPStat.add(panOPStat7);
        panCenter.add(panOPStat, BorderLayout.CENTER);

        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        btnGet = new MyButton("Get", 2) {

            @Override
            public void onClick() {
                clearAllFields();
                getOutputStatus(getOutputStatusString());
            }
        };
        panButtons.add(btnGet);

        btnSet = new MyButton("Set", 2) {

            @Override
            public void onClick() {
//                setOutputStatus();
                setOutputStatusString(setOutputStatus());
                UpdateConfig();

            }
        };
        panButtons.add(btnSet);

        btnExt = new MyButton("Exit", 2) {

            @Override
            public void onClick() {
                OutputApplet.showHomePanel();
            }
        };
        panButtons.add(btnExt);
        panCenter.add(panButtons, BorderLayout.SOUTH);
        MainPanel.add(panCenter, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(MainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private int[] setOutputStatus() {

        Checked[0] = getCheckedBoxes(chk0d, 15);
        Checked[1] = getCheckedBoxes(chk0d, 7);
        Checked[2] = Integer.parseInt(txtOP0.getText()) * 10;

        Checked[3] = getCheckedBoxes(chk1d, 15);
        Checked[4] = getCheckedBoxes(chk1d, 7);
        Checked[5] = Integer.parseInt(txt1P0.getText()) * 10;

        Checked[6] = getCheckedBoxes(chk2d, 15);
        Checked[7] = getCheckedBoxes(chk2d, 7);
        Checked[8] = Integer.parseInt(txt2P0.getText()) * 10;

        Checked[9] = getCheckedBoxes(chk3d, 15);
        Checked[10] = getCheckedBoxes(chk3d, 7);
        Checked[11] = Integer.parseInt(txt3P0.getText()) * 10;

        Checked[12] = getCheckedBoxes(chkLcald, 15);
        Checked[13] = getCheckedBoxes(chkLcald, 7);
        Checked[14] = Integer.parseInt(txtLclOP0.getText()) * 10;

        Checked[15] = getCheckedBoxes(chkALRMd, 15);
        Checked[16] = getCheckedBoxes(chkALRMd, 7);
        Checked[17] = Integer.parseInt(txtAlarmTone.getText()) * 10;

        Checked[18] = getCheckedBoxes(chkIPd, 7);
        return Checked;
    }

    private int getCheckedBoxes(JCheckBox[] checkbox, int limit) {
        int ans = 1;
        final int var = 2;
        int total = 0;
        int initial = 0;
        if (limit == 7) {
            initial = 1;
            if (checkbox[0].isSelected() == true) {
                total = 1;
            }
        } else if (limit == 15) {
            initial = 9;
            if (checkbox[8].isSelected() == true) {
                total = 1;
            }
        }
        for (int i = initial; i <= limit; i++) {
            ans = ans * var;
            if (checkbox[i].isSelected() == true) {
                total = total + ans;
            }
        }
        return total;
    }

    private void getOutputStatus(int checked[]) {
        setCheckedBoxes(checked[0], chk0d, 15);
        setCheckedBoxes(checked[1], chk0d, 7);
        txtOP0.setText("" + checked[2] / 10);

        setCheckedBoxes(checked[3], chk1d, 15);
        setCheckedBoxes(checked[4], chk1d, 7);
        txt1P0.setText("" + checked[5] / 10);

        setCheckedBoxes(checked[6], chk2d, 15);
        setCheckedBoxes(checked[7], chk2d, 7);
        txt2P0.setText("" + checked[8] / 10);

        setCheckedBoxes(checked[9], chk3d, 15);
        setCheckedBoxes(checked[10], chk3d, 7);
        txt3P0.setText("" + checked[11] / 10);

        setCheckedBoxes(checked[12], chkLcald, 15);
        setCheckedBoxes(checked[13], chkLcald, 7);
        txtLclOP0.setText("" + checked[14] / 10);

        setCheckedBoxes(checked[15], chkALRMd, 15);
        setCheckedBoxes(checked[16], chkALRMd, 7);
        txtAlarmTone.setText("" + checked[17] / 10);

        setCheckedBoxes(checked[18], chkIPd, 7);
    }

    private void setCheckedBoxes(int val, JCheckBox checkBox[], int limit) {
        int ans = 1;
        final int var = 2;
        int initial = 0;

        if (limit == 7) {
            initial = 1;
            if (val % 2 == 1) {
                checkBox[0].setSelected(true);
            }
        }
        if (limit == 15) {
            initial = 9;
            if (val % 2 == 1) {
                checkBox[8].setSelected(true);
            }
        }
        for (int i = initial; i <= limit; i++) {
            ans = ans * var;
            if ((val & ans) == ans) {
                checkBox[i].setSelected(true);
            }
        }
    }

    private int[] getOutputStatusString() {
        int arrOutputStat[] = new int[20];
        try {
            String ReaderNo = cmbRdrNo.getSelectedItem().toString();
            Packetizer a = new Packetizer();
            a.addString(ReaderNo);
            String packet = a.getPacket();
            MyHTTP myHTTP = MyUtils.createServletConnection("RSCommServlet");
            myHTTP.openOS();
            myHTTP.println("GetOutputStatus");
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            myHTTP.closeIS();
            if (Result.startsWith("ERROR")) {
                MyUtils.showMessage(Result);
            } else {
                Depacketizer dp = new Depacketizer(Result);
                for (int i = 0; i < 20; i++) {
                    String val = "" + dp.getInt();
                    if (!"".equals(val)) {
                        arrOutputStat[i] = Integer.parseInt(val);
                    }
                }
            }

        } catch (Exception e) {
            MyUtils.showException("Get Reader Settings", e);
        }
        return arrOutputStat;
    }

    private void setOutputStatusString(int checked[]) {
        try {

            String ReaderNo = cmbRdrNo.getSelectedItem().toString();
            Packetizer a = new Packetizer();
            for (int i = 0; i < checked.length; i++) {
                a.addInt(checked[i]);
            }
            String packet = a.getPacket();
            MyHTTP myHTTP = MyUtils.createServletConnection("RSCommServlet");
            myHTTP.openOS();
            myHTTP.println("SetOutputStatus");
            myHTTP.println(ReaderNo);
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            myHTTP.closeIS();

        } catch (Exception e) {
            MyUtils.showException("Set Output Status", e);
        }
    }

    private void UpdateConfig() {
        try {
            String ReaderNo = cmbRdrNo.getSelectedItem().toString();
            // MyUtils.showMessage(packet);
            MyHTTP myHTTP = MyUtils.createServletConnection("RSCommServlet");
            myHTTP.openOS();
            myHTTP.println("UpdateConfig");
            myHTTP.println(ReaderNo);
//            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            myHTTP.closeIS();

            if (Result.startsWith("ERROR")) {
                MyUtils.showException("Database ERROR", new Exception(Result));
            }
        } catch (Exception e) {
            MyUtils.showException("Update Config", e);
        }
    }

    private void clearAllFields() {
        clearCheckBoxes(chk0d);
        clearCheckBoxes(chk1d);
        clearCheckBoxes(chk2d);
        clearCheckBoxes(chk3d);
        clearCheckBoxes(chkLcald);
        clearCheckBoxes(chkALRMd);
        clearCheckBoxes(chkIPd);
        txtOP0.setText("0");
        txt1P0.setText("0");
        txt2P0.setText("0");
        txt3P0.setText("0");
        txtLclOP0.setText("0");
        txtAlarmTone.setText("0");
    }

    private void clearCheckBoxes(JCheckBox checkBox[]) {
        for (int i = 0; i < checkBox.length; i++) {
            checkBox[i].setSelected(false);
        }
    }
}
