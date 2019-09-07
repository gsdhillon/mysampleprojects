/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.section;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import lib.gui.*;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author nbpatil
 */
public class SectionMaster extends MyPanel implements ActionListener {

    public MyTextField txtSecCode;
    public MyTextField txtSecHCode;
    public MyTextField txtSecNm;
    public MyTextField txtSecHNm;
    public MyButton btnAdd;
    public MyButton btnCancel;
    public JComboBox cmbDivCd;
    public MyTextField txtDivNm;
    private Section SectionApplet;
    private String Job;
    private String SecCode;
    private ArrayList<tuple> idname = new ArrayList<tuple>();

    public SectionMaster(Section SectionApplet, String Job, String SecCode) {
        this.SectionApplet = SectionApplet;
        this.Job = Job;
        this.SecCode = SecCode;
        addSectionPanel();
        addListeners();
        getDivCode();
        getEmpName();
        if (Job.equals("update")) {
            FillForm();
        }
    }

    private void FillForm() {
        txtSecCode.setEditable(false);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SectionFormServlet");
            myHTTP.openOS();
            myHTTP.println("FillForm");
            myHTTP.println(SecCode);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer dp = new Depacketizer(result);
                txtSecCode.setText(dp.getString());
                txtSecNm.setText(dp.getString());
                txtSecHCode.setText(dp.getString());
                txtSecHNm.setText(dp.getString());
                cmbDivCd.setSelectedItem(dp.getString());
            }
        } catch (Exception e) {
            MyUtils.showException("Fill Form", e);
        }

    }

    private void addListeners() {
        cmbDivCd.addActionListener(this);
        cmbDivCd.setActionCommand("DivHead");
    }

    private void getDivCode() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SectionFormServlet");
            myHTTP.openOS();
            myHTTP.println("DivCode");
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer dp = new Depacketizer(result);
                while (!dp.isEmpty()) {
                    cmbDivCd.addItem(dp.getString());
                }
            }
        } catch (Exception e) {
            MyUtils.showException("Get Division Code", e);
        }
    }

    private void setDivName(String DivCode) {
        try {

            MyHTTP myHTTP = MyUtils.createServletConnection("SectionFormServlet");
            myHTTP.openOS();
            myHTTP.println("DivName");
            myHTTP.println(DivCode);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                txtDivNm.setText(result);
            }
        } catch (Exception ex) {
            MyUtils.showException("AddSection", ex);
        }
    }

    private void getEmpName() {
        try {

            MyHTTP myHTTP = MyUtils.createServletConnection("SectionFormServlet");   // emp name can be taken from Division Servlet
            myHTTP.openOS();
            myHTTP.println("empName");
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer dp = new Depacketizer(result);
                int count = dp.getInt();
                for (int i = 0; i < count; i++) {
                    idname.add(new tuple(dp.getString(), dp.getString()));
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("AddSection", ex);
        }
    }

    private String CreatePacket() {
        try {
            Packetizer a = new Packetizer();
            a.addString(txtSecCode.getText());
            a.addString(txtSecNm.getText());
            a.addString(txtSecHCode.getText());
            a.addString(txtSecHNm.getText());
            a.addString((String) cmbDivCd.getSelectedItem());
            return a.getPacket();
        } catch (Exception ex) {
            MyUtils.showException("Create Packet", ex);
            return "PacketFail";
        }
    }

    private boolean FormFilled() {
        if (cmbDivCd.getSelectedIndex() == 0) {
            MyUtils.showMessage("Division Code not selected");
            return false;
        } else if (txtSecCode.getText().equals("") || txtSecHCode.getText().equals("") || txtSecHNm.getText().equals("") || txtSecNm.getText().equals("")) {
            MyUtils.showMessage("Form not filled");
            return false;
        }
        return true;
    }

    private void addSection() {
        if (FormFilled()) {
            String Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("SectionFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("AddSection");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else {
                        SectionApplet.showHomePanel();
                    }
                } catch (Exception ex) {
                    MyUtils.showException("Add Section", ex);
                }
            }
        }
    }

    private void updateSection() {
        if (FormFilled()) {
            String Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("SectionFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("UpdateSection");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    myHTTP.closeIS();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else {
                        SectionApplet.showHomePanel();
                    }
                } catch (Exception ex) {
                    MyUtils.showException("Update Section", ex);
                }
            }
        }

    }

    private void addSectionPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2;
        int height = screenSize.height / 3;
        setSize(width, height - 30);

        MyPanel MainPanel = new MyPanel(new BorderLayout());
        MyPanel panSecDiv = new MyPanel(new BorderLayout());
        MyPanel panDiv = new MyPanel(new GridLayout(1, 3, 30, 5), "Division Details");
        panDiv.setPreferredSize(new Dimension(width, height / 5));

        MyPanel panDCode = new MyPanel(new BorderLayout());
        MyLabel lblDivCd = new MyLabel(1, "Division Code:");
        panDCode.add(lblDivCd, BorderLayout.WEST);

        cmbDivCd = new JComboBox();
        cmbDivCd.addItem("Select Division");
        panDCode.add(cmbDivCd, BorderLayout.CENTER);

        panDiv.add(panDCode);

        MyPanel panDivNm = new MyPanel(new BorderLayout());

        MyLabel lblDivNm = new MyLabel(1, "Division Name:");
        panDivNm.add(lblDivNm, BorderLayout.WEST);

        txtDivNm = new MyTextField();
        txtDivNm.setEditable(false);
        panDivNm.add(txtDivNm, BorderLayout.CENTER);
        panDiv.add(panDivNm);
        MyLabel lblFill = new MyLabel();
        panDiv.add(lblFill);
        panSecDiv.add(panDiv, BorderLayout.NORTH);

        MyPanel panSecDet = new MyPanel(new BorderLayout(10, 5), "Section Details");
        MyPanel panSecCode = new MyPanel(new BorderLayout());
        MyPanel panlblSecCode = new MyPanel(new GridLayout(2, 1));
        MyLabel lbldivCode = new MyLabel(1, "Section Code:");
        panlblSecCode.add(lbldivCode);

        MyLabel lbldivHeadCode = new MyLabel(1, "Section Head Code:");
        panlblSecCode.add(lbldivHeadCode);

        panSecCode.add(panlblSecCode, BorderLayout.WEST);

        MyPanel pantxtDivCode = new MyPanel(new GridLayout(2, 1, 10, 5));
        txtSecCode = new MyTextField(10);
        pantxtDivCode.add(txtSecCode);

        txtSecHCode = new MyTextField(10);
        txtSecHCode.addKeyListener(new MyKeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    getSectionHeadName(txtSecHCode.getText());
                }
            }
        });
        pantxtDivCode.add(txtSecHCode);
        panSecCode.add(pantxtDivCode, BorderLayout.CENTER);
        panSecDet.add(panSecCode, BorderLayout.WEST);

        // adding dept Name and dept head Name using borderlayout and grid layout

        MyPanel panSecNm = new MyPanel(new BorderLayout());
        MyPanel panlblSecNm = new MyPanel(new GridLayout(2, 1));
        MyLabel lblSecNm = new MyLabel(1, "Section Name:");
        panlblSecNm.add(lblSecNm);

        MyLabel lblSecHNm = new MyLabel(1, "Section Head Name:");
        panlblSecNm.add(lblSecHNm);

        panSecNm.add(panlblSecNm, BorderLayout.WEST);

        MyPanel pantxtSecNm = new MyPanel(new GridLayout(2, 1, 0, 10));
        txtSecNm = new MyTextField();
        pantxtSecNm.add(txtSecNm);

        txtSecHNm = new MyTextField();
        txtSecHNm.setEnabled(false);
        pantxtSecNm.add(txtSecHNm);

        panSecNm.add(pantxtSecNm, BorderLayout.CENTER);

        panSecDet.add(panSecNm, BorderLayout.CENTER);

        panSecDiv.add(panSecDet, BorderLayout.CENTER);
        MainPanel.add(panSecDiv, BorderLayout.NORTH);

        //adding Buttons
        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Add Section Details");
        if (Job.equals("add")) {
            btnAdd = new MyButton("Add Section", 2, Color.WHITE) {

                @Override
                public void onClick() {
                    addSection();
                }
            };
        } else if (Job.equals("update")) {
            btnAdd = new MyButton("Update Section", 2, Color.WHITE) {

                @Override
                public void onClick() {
                    updateSection();
                }
            };
        }
        panButtons.add(btnAdd);

        btnCancel = new MyButton("Cancel", 2, Color.WHITE) {

            @Override
            public void onClick() {
                SectionApplet.showHomePanel();
            }
        };
        panButtons.add(btnCancel);

        MainPanel.add(panButtons, BorderLayout.CENTER);

        //adding Status label to the south of main frame

        MyPanel panStatus = new MyPanel();
        panStatus.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        MyLabel lblStatus = new MyLabel(1, "");
        lblStatus.setPreferredSize(new Dimension(width - 420, height / 35));

        panStatus.add(lblStatus);
        MainPanel.add(panStatus, BorderLayout.SOUTH);
        this.setLayout(new BorderLayout());
        this.add(MainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("DivHead")) {
            if (cmbDivCd.getSelectedIndex() != 0) {
                setDivName((String) cmbDivCd.getSelectedItem());
            }
        }
    }

    public class tuple {

        String id = null;
        String name = null;

        public tuple(String a, String s) {
            this.id = a;
            this.name = s;
        }
    }

    private void getSectionHeadName(String SecHeadCode) {
        String Packet = CreatePacket();
        if (!Packet.equals("PacketFail")) {
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("DivisionFormServlet");
                myHTTP.openOS();
                myHTTP.println("GetDivHeadCode");//using same method for division as well as section from DivisionFormServlet
                myHTTP.println(SecHeadCode);
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                if (result.startsWith("ERROR")) {
                    MyUtils.showMessage(result);
                } else {
                    if (!"Not Found".equals(result)) {
                        txtSecHNm.setText(result);
                    } else {
                        txtSecHNm.setText("");
                        MyUtils.showMessage("Employee not present! Enter another SectionHeadCode");
                        return;
                    }
                }
            } catch (Exception ex) {
                MyUtils.showException("getDivHeadName", ex);
            }
        }
    }
}
