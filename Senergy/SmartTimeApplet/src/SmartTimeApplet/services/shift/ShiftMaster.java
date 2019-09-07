/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.shift;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import lib.Utility.DateUtilities;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author pradnya
 */
public class ShiftMaster extends MyPanel implements ChangeListener {
    
    public MyTextField txtShiftCode, txtSTime;
    public JRadioButton rbNormalShift, rbNightShift;
    public MyButton btnAdd, btnCancel;
    public JSpinner StartTime, EndTime;
    public JTable tlbShift;
    public MyLabel lblShowWHour;
    private String Job;
    private String ShiftCode;
    private Shift ShiftApplet;
    
    public ShiftMaster(Shift ShiftApplet, String Job, String ShiftCode) {
        this.ShiftApplet = ShiftApplet;
        this.Job = Job;
        this.ShiftCode = ShiftCode;
        addShiftPanel();
        addListeners();
        if (Job.equals("update")) {
            FillForm();
        }
    }
    
    private void addListeners() {
        StartTime.addChangeListener(this);
        EndTime.addChangeListener(this);
    }
    
    private String foo() {
        if (rbNormalShift.isSelected()) {
            return "0";
        } else if (rbNightShift.isSelected()) {
            return "1";
        } else {
            return "0";
        }
    }
    
    private void biz(String a) {
        if (a.equals("1")) {
            rbNightShift.setSelected(true);
        } else {
            rbNormalShift.setSelected(true);
        }
    }
    
    public boolean FormFilled() {
        if (txtShiftCode.getText().equals("")) {
            MyUtils.showMessage("Enter Shift Code");
            return false;
        } else if (lblShowWHour.getText().equals("")) {
            MyUtils.showMessage("Set StartTime and EndTime");
            return false;
        }
        return true;
    }
    
    private void addShift() {
        if (FormFilled()) {
            String Packet;
            Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("ShiftMasterFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("AddShift");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else {
                        ShiftApplet.showHomePanel();
                    }
                } catch (Exception ex) {
                    MyUtils.showException("Add Shift", ex);
                }
            }
        }
    }
    
    private void updateShift() {
        if (FormFilled()) {
            String Packet;
            Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("ShiftMasterFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("UpdateShift");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else {
                        ShiftApplet.showHomePanel();
                    }
                } catch (Exception ex) {
                    MyUtils.showException("Update Shift", ex);
                }
            }
        }
    }
    
    public String CreatePacket() {
        try {
            Packetizer a = new Packetizer();
            a.addString(txtShiftCode.getText());
            a.addString(foo());
            String spinner1 = StartTime.getValue().toString();
            String[] split1 = spinner1.split(" ");
            
            a.addString(split1[3]);
            String spinner2 = EndTime.getValue().toString();
            String[] split2 = spinner2.split(" ");
            
            a.addString(split2[3]);
            if (rbNormalShift.isSelected()) {
//                CalTimeDiff(split1[3], split2[3], 0);
                DateUtilities.getTimeDiff(split1[3], split2[3]);
            } else {
//                CalTimeDiff(split1[3], split2[3], 1);
                DateUtilities.getTimeDiff(split1[3], split2[3]);
            }
            a.addString(lblShowWHour.getText());
            return a.getPacket();
        } catch (Exception ex) {
            MyUtils.showException("Create Packet", ex);
            return "PacketFail";
        }
    }
    
    private void FillForm() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ShiftMasterFormServlet");
            myHTTP.openOS();
            myHTTP.println("FormDetails");
            myHTTP.println(ShiftCode);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
            } else {
                Depacketizer dp = new Depacketizer(result);
                txtShiftCode.setText(dp.getString());
                biz(dp.getString());
                StartTime.setValue(getTime(dp.getString()));
                EndTime.setValue(getTime(dp.getString()));
            }
        } catch (Exception ex) {
            MyUtils.showException("FillForm", ex);
        }
    }
    
    private Date getTime(String time) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, Integer.parseInt(time.substring(0, 2)),
                Integer.parseInt(time.substring(3, 5)), Integer.parseInt(time.substring(6, 8)));
        return cal.getTime();
    }

//    private String CalTimeDiff(String spinner1, String spinner2, Integer type) {
//        String[] split1 = spinner1.split(":");
//        String[] split2 = spinner2.split(":");
//        Integer[] Time = new Integer[3];
//        String WorkHour = "";
//        for (byte i = 0; i <= 2; i++) {
//            if (Integer.parseInt(split1[i]) > Integer.parseInt(split2[i])) {
//                if (i == 0) {
//                    Time[i] = (Integer.parseInt(split2[i]) + 24) - Integer.parseInt(split1[i]);
//                } else {
//                    Time[i] = (Integer.parseInt(split2[i]) + 60) - Integer.parseInt(split1[i]);
//                }
//            } else {
//                Time[i] = Integer.parseInt(split2[i]) - Integer.parseInt(split1[i]);
//                if (i == 0) {
//                    if ((Integer.parseInt(split1[0])) != (Integer.parseInt(split2[0]))) {
//                        if ((Integer.parseInt(split1[1])) > (Integer.parseInt(split2[1]))) {
//                            Time[i] = Time[i] - 1;
//                        }
//                    }
//                }
//            }
//
//            if (i == 0) {
//                if (Integer.toString(Time[i]).length() == 1) {
//                    WorkHour = "0" + new Integer(Time[i]).toString();
//                } else {
//                    WorkHour = new Integer(Time[i]).toString();
//                }
//            } else {
//                if (Integer.toString(Time[i]).length() == 1) {
//                    WorkHour = WorkHour + ":0" + new Integer(Time[i]).toString();
//                } else {
//                    WorkHour = WorkHour + ":" + new Integer(Time[i]).toString();
//                }
//            }
//        }
//        return WorkHour;
//
//    }
    private void addShiftPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width / 3.4);
        int height = screenSize.height / 2;
        setSize(width, height);
        
        
        MyPanel MainPanel = new MyPanel(new BorderLayout());

        //adding shift code to the north of the main panel using border layout

        MyPanel panShiftCode = new MyPanel(new GridLayout(2, 1), "Add Shift Code");
        MyPanel panSCode = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        MyLabel lblShiftCode = new MyLabel(1, "Shift Code : ");
        panSCode.add(lblShiftCode);
        
        txtShiftCode = new MyTextField(10);
        panSCode.add(txtShiftCode);
        
        panShiftCode.add(panSCode);
        
        MyPanel panRadio = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        
        ButtonGroup rbbuttonGroup = new ButtonGroup();
        rbNormalShift = new JRadioButton("Normal Shift", true);
        rbbuttonGroup.add(rbNormalShift);
        panRadio.add(rbNormalShift);
        
        rbNightShift = new JRadioButton("Night Shift");
        rbbuttonGroup.add(rbNightShift);
        panRadio.add(rbNightShift);
        panShiftCode.add(panRadio);
        
        MainPanel.add(panShiftCode, BorderLayout.NORTH);

        //adding start and end time 
        MyPanel panMainCenter = new MyPanel(new BorderLayout(), "Shift Time");
        MyPanel panSTime = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Add Shift Time");
        MyPanel panSTimeGrd = new MyPanel(new GridLayout(3, 2, 0, 5));
        
        MyLabel lblSTime = new MyLabel(1, "Start Time : ");
        panSTimeGrd.add(lblSTime);
        
        MyPanel panStartT = new MyPanel(new BorderLayout());
        StartTime = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(StartTime, "HH:mm:ss");
        StartTime.setEditor(timeEditor);
        SpinnerModel model = StartTime.getModel();
        panStartT.add(StartTime, BorderLayout.WEST);
        panSTimeGrd.add(panStartT);
        
        MyLabel lblETime = new MyLabel(1, "End Time : ");
        panSTimeGrd.add(lblETime);
        
        MyPanel panEndT = new MyPanel(new BorderLayout());
        EndTime = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor1 = new JSpinner.DateEditor(EndTime, "HH:mm:ss");
        EndTime.setEditor(timeEditor1);
        panEndT.add(EndTime, BorderLayout.WEST);
        panSTimeGrd.add(panEndT);
        
        MyLabel lblWHour = new MyLabel(1, "Working Hours : ");
        panSTimeGrd.add(lblWHour);
        
        lblShowWHour = new MyLabel();
        lblShowWHour.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panSTimeGrd.add(lblShowWHour);
        panSTime.add(panSTimeGrd);
        panMainCenter.add(panSTime, BorderLayout.CENTER);

        //adding Buttons
        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Add Shift Details");
        if (Job.equals("add")) {
            btnAdd = new MyButton("Add Shift", 2, Color.WHITE) {
                
                @Override
                public void onClick() {
                    addShift();
                }
            };
        } else if (Job.equals("update")) {
            btnAdd = new MyButton("Update Shift", 2, Color.WHITE) {
                
                @Override
                public void onClick() {
                    updateShift();
                }
            };
        }
        btnAdd.setForeground(Color.GREEN);
        panButtons.add(btnAdd);
        
        btnCancel = new MyButton("Cancel", 2, Color.WHITE) {
            
            @Override
            public void onClick() {
                ShiftApplet.showHomePanel();
            }
        };
        btnCancel.setForeground(new Color(103, 213, 83));
        panButtons.add(btnCancel);
        panMainCenter.add(panButtons, BorderLayout.SOUTH);
        MainPanel.add(panMainCenter, BorderLayout.CENTER);
        //adding Status label to the south of main frame

        MyPanel panStatus = new MyPanel();
        
        panStatus.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        MyLabel lblStatus = new MyLabel(1, "");
        lblStatus.setPreferredSize(new Dimension(width / 3 - 15, height / 35));
        panStatus.add(lblStatus);
        
        MainPanel.add(panStatus, BorderLayout.SOUTH);
        
        
        this.setLayout(new BorderLayout());
        this.add(MainPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        
        String spinner1 = StartTime.getValue().toString();
        
        String[] Time1 = spinner1.split(" ");
        String spinner2 = EndTime.getValue().toString();
        
        String[] Time2 = spinner2.split(" ");
        try {
            if (rbNormalShift.isSelected()) {
                //            lblShowWHour.setText(CalTimeDiff(Time1[3], Time2[3], 0));
                lblShowWHour.setText(DateUtilities.getTimeDiff(Time1[3], Time2[3]));
            } else {
                //            lblShowWHour.setText(CalTimeDiff(Time1[3], Time2[3], 1));
                lblShowWHour.setText(DateUtilities.getTimeDiff(Time1[3], Time2[3]));
            }
        } catch (ParseException ex) {
            MyUtils.showMessage(" stateChanged : " + ex);
        }
    }
}
