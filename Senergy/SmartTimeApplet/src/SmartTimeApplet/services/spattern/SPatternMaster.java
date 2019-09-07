/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.spattern;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author nbpatil
 */
public class SPatternMaster extends MyPanel implements DocumentListener {

    public MyButton btnAdd, btnCancel;
    public JComboBox[] cmbSCode = new JComboBox[10];
    public MyTextField txtSPCode;
    public MyTextField[] txtDay = new MyTextField[10];
    private SPattern SPatternApplet;
    private String Job;
    private String SPCode;
    private String[] shift;

    public SPatternMaster(SPattern SPatternApplet, String Job, String SPCode) {
        this.SPatternApplet = SPatternApplet;
        this.Job = Job;
        this.SPCode = SPCode;
        addSPatternPanel();
        getShift();
        addListeners();
        if (Job.equals("update")) {
            FillForm();
        }
    }
    
    private void addListeners() {
        for (int i = 0; i < 10; i++) {
            ((AbstractDocument) txtDay[i].getDocument()).setDocumentFilter(new MyTextFilter());
            txtDay[i].getDocument().addDocumentListener(this);
            txtDay[i].getDocument().putProperty("name", i + "");
        }
        cmbSCode[0].setEnabled(true);
        addShift(cmbSCode[0]);
        txtDay[0].setEnabled(true);

    }

    private void getShift() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SPatternFormServlet");
            myHTTP.openOS();
            myHTTP.println("ShiftList");
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer d = new Depacketizer(result);
                int rowCount = d.getInt();
                shift = new String [rowCount];
                for (int i = 0; i < rowCount; i++) {
                    shift[i] = d.getString();
                }
            }
        } catch (Exception ex) {
            MyUtils.showException("Get Shift", ex);
        }
    }
    private void FillForm() {
        txtSPCode.setEditable(false);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SPatternFormServlet");
            myHTTP.openOS();
            myHTTP.println("FillForm");
            myHTTP.println(SPCode);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                txtSPCode.setText(SPCode);
                Depacketizer d = new Depacketizer(result);
                int RowCount = d.getInt();
                for (int i = 0; i < RowCount; i++) {
                    String SPatternCode = d.getString();
                    int Position = d.getInt();
                    String ShiftCode = d.getString();
                    String days = d.getString();
                    cmbSCode[Position].setSelectedItem(ShiftCode);
                    txtDay[Position].setText(days);
                }

            }

        } catch (Exception ex) {
            MyUtils.showException("FillForm", ex);
        }
    }

    private void addShift(JComboBox cmb) {
        cmb.addItem("00");
        for (int i = 0; i < shift.length; i++) {
            if (shift[i] == null) {
                break;
            }
            cmb.addItem(shift[i]);
        }
    }

    private Integer TSDays(int name) {
        int ret = 0;
        for (int i = 0; i < name; i++) {
            if(txtDay[i].getText().equals("")){
                return -1;
            }
            ret += Integer.parseInt(txtDay[i].getText());
        }
        return ret;
    }

    private void removeShift(JComboBox cmb) {
        cmb.removeAllItems();
    }

    private class MyTextFilter extends DocumentFilter {

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset,
                String string, AttributeSet attr)
                throws BadLocationException {
            Document document = fb.getDocument();
            int name = Integer.parseInt((String) document.getProperty("name"));
            String text = document.getText(0, offset);

            try {
                int val = Integer.parseInt(text + string);

                if (val > 7 || val < 1) {
                    MyUtils.showMessage("Enter the value between 1-7");
                    return;
                }
                int totalDays=TSDays(name);
                if(totalDays==-1){
                    MyUtils.showMessage("Invalid Form Filling");
                    return;
                }
                if (val > (30 - totalDays)) {
                    MyUtils.showMessage("Total days in a Shift pattern cannot exceed 30");
                    return;
                }
            } catch (NumberFormatException e) {
                MyUtils.showMessage("Enter Integer only");
                return;
            }
            super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb,
                int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            if (length > 0) {
                fb.remove(offset, length);
            }
            insertString(fb, offset, string, attr);
        }
    }
    /*
     * 
     */
    private void addSPattern(){
        if (FormFilled()) {
                String Packet = CreatePacket();
                if (!Packet.equals("PacketFail")) {
                    try {
                        MyHTTP myHTTP = MyUtils.createServletConnection("SPatternFormServlet");
                        myHTTP.openOS();
                        myHTTP.println("AddSPattern");
                        myHTTP.println(Packet);
                        myHTTP.closeOS();
                        myHTTP.openIS();
                        String result = myHTTP.readLine();
                        if (result.startsWith("ERROR")) {
                            MyUtils.showMessage(result);
                        } else {
                            SPatternApplet.showHomePanel();

                        }
                    } catch (Exception ex) {
                        MyUtils.showException("Add SHift Pattern", ex);
                    }
                }
            }
    }
    
    private void updateSPattern(){
        if (FormFilled()) {
                String Packet = CreatePacket();
                if (!Packet.equals("PacketFail")) {
                    try {
                        MyHTTP myHTTP = MyUtils.createServletConnection("SPatternFormServlet");
                        myHTTP.openOS();
                        myHTTP.println("UpdateSPattern");
                        myHTTP.println(Packet);
                        myHTTP.closeOS();
                        myHTTP.openIS();
                        String result = myHTTP.readLine();
                        if (result.startsWith("ERROR")) {
                            MyUtils.showMessage(result);
                        } else {
                            SPatternApplet.showHomePanel();

                        }
                    } catch (Exception ex) {
                        MyUtils.showException("Update Shift Pattern", ex);
                    }
                }
            }
    }
    
    

    private String CreatePacket() {
        try {
            Packetizer a = new Packetizer();

            a.addString(txtSPCode.getText());
            for (int i = 0; i < 10; i++) {
                if (txtDay[i].getText().equals("")) {
                    break;
                }
                a.addInt(i);
                a.addString((String) cmbSCode[i].getSelectedItem());
                a.addString(txtDay[i].getText());

            }
            return a.getPacket();
        } catch (Exception ex) {
            return "PacketFail";
        }
    }

    private boolean FormFilled() {
        if (txtSPCode.getText().equals("")) {
            MyUtils.showMessage("Shift Pattern Code not filled");
            return false;
        } else if (checkCMBox()) {
            MyUtils.showMessage("Form not filled");
            return false;
        }
        return true;
    }

    private boolean checkCMBox() {
        for (int i = 0; i < 10; i++) {
            if (txtDay[i].isEnabled()) {
                if (txtDay[i].getText().equals("")) {
                    if (i < 9) {
                        if (txtDay[i + 1].isEnabled()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean checkAhead(int name) {
        for (int i = name + 1; i < 10; i++) {
            if (txtDay[i].isEnabled()) {
                if (!txtDay[i].getText().equals("")) {
                    return false;
                }
            } else {
                break;
            }
        }

        return true;
    }
    

    private void addSPatternPanel() {

        Dimension screenSize = SPatternApplet.getSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setSize(width - 400, height / 2);
        MyPanel MainPanel = new MyPanel(new BorderLayout());
        MainPanel.setBackground(Color.WHITE);

        MyPanel panMainNorth = new MyPanel(new BorderLayout(), "Shift Pattern Code");
        panMainNorth.setBackground(Color.WHITE);

        MyPanel panSPCode = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        panSPCode.setBackground(Color.WHITE);

        MyLabel lblSPCode = new MyLabel(1, "Shift Pattern Code : ");
        lblSPCode.setBackground(Color.WHITE);
        panSPCode.add(lblSPCode);

        txtSPCode = new MyTextField();
        txtSPCode.setPreferredSize(new Dimension(60, 25));
        panSPCode.add(txtSPCode);

        panMainNorth.add(panSPCode, BorderLayout.NORTH);


        MyPanel panShiftCode = new MyPanel(new GridLayout(2, 11, 5, 5), "Configure Shift Pattern");
        panShiftCode.setPreferredSize(new Dimension(width / 2, height / 7));
        panShiftCode.setBackground(Color.WHITE);

        MyLabel lblShiftCode = new MyLabel(1, "ShiftCode:");

        lblShiftCode.setBackground(Color.WHITE);
        panShiftCode.add(lblShiftCode);

        for (int i = 0; i < 10; i++) {
            cmbSCode[i] = new JComboBox();
            cmbSCode[i].setEnabled(false);
            panShiftCode.add(cmbSCode[i]);
        }

        MyLabel lblDays = new MyLabel(1, "Days :");
        lblDays.setBackground(Color.WHITE);
        panShiftCode.add(lblDays);

        for (int i = 0; i < 10; i++) {
            txtDay[i] = new MyTextField();
            txtDay[i].setEnabled(false);
            panShiftCode.add(txtDay[i]);
        }

        panMainNorth.add(panShiftCode, BorderLayout.CENTER);

        MainPanel.add(panMainNorth, BorderLayout.NORTH);

        //Adding tabel to the center
        MyPanel panCenter = new MyPanel(new BorderLayout());
        panCenter.setBackground(Color.WHITE);

        //adding Buttons
        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Add Shift Details");
        panButtons.setBackground(Color.WHITE);
        if(Job.equals("add")){
        btnAdd = new MyButton("Add Shift Pattern", 2, Color.WHITE) {

            @Override
            public void onClick() {
                addSPattern();
            }
        };
        }else if(Job.equals("update")){
          btnAdd = new MyButton("Update Shift Pattern", 2, Color.WHITE) {

            @Override
            public void onClick() {
                updateSPattern();
            }
        };  
        }
        panButtons.add(btnAdd);

        btnCancel = new MyButton("Cancel", 2, Color.WHITE) {

            @Override
            public void onClick() {
                SPatternApplet.showHomePanel();
            }
        };
        panButtons.add(btnCancel);
        panCenter.add(panButtons, BorderLayout.SOUTH);
        MainPanel.add(panCenter, BorderLayout.CENTER);

        //adding Status label to the south of main frame
        MyPanel panStatus = new MyPanel();
        panStatus.setBackground(Color.WHITE);
        panStatus.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        MyLabel lblStatus = new MyLabel(1, "");
        lblStatus.setPreferredSize(new Dimension(width / 3 - 15, height / 25));

        lblStatus.setBackground(Color.WHITE);
        panStatus.add(lblStatus);

        MainPanel.add(panStatus, BorderLayout.SOUTH);

        this.setLayout(new BorderLayout());
        this.add(MainPanel, BorderLayout.CENTER);
        setVisible(true);

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            String text = e.getDocument().getText(0, e.getDocument().getLength());
            int name = Integer.parseInt((String) e.getDocument().getProperty("name"));
            if (name < 9) {
                if (!cmbSCode[name + 1].isEnabled()) {
                    cmbSCode[name + 1].setEnabled(true);
                    txtDay[name + 1].setEnabled(true);
                    addShift(cmbSCode[name + 1]);
                }
            }
        } catch (BadLocationException ex) {
            MyUtils.showException("Insert Update", ex);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        try {
            String text = e.getDocument().getText(0, e.getDocument().getLength());
            int name = Integer.parseInt((String) e.getDocument().getProperty("name"));
            if (name < 9) {
                if (text.equals("")) {
                    if (checkAhead(name)) {
                        cmbSCode[name + 1].setEnabled(false);
                        txtDay[name + 1].setEnabled(false);
                        removeShift(cmbSCode[name + 1]);
                    }
                }
            }
        } catch (BadLocationException ex) {
            MyUtils.showException("Insert Update", ex);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
