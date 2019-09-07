/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.division;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import TableModels.DHeadTableModel;
import TableModels.MyList;
import java.awt.*;
import java.awt.event.KeyEvent;
import lib.gui.*;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author GAURAV
 */
public class DivisionMaster extends MyPanel {
    
    public MyTextField txtDivCode;
    public MyTextField txtDivHCode;
    public MyTextField txtDivNm;
    public MyTextField txtDivHNm;
    public MyButton btnAdd;
    public MyButton btnCancel;
    public DHeadTableModel tableModel1;
    public MyTable table;
    public MyList HeadName;
    private Division DivisionApplet;
    private String Job;
    private String DivCode;
    
    public DivisionMaster(Division DivisionApplet, String Job, String DivCode) {
        try {
            this.DivisionApplet = DivisionApplet;
            this.Job = Job;
            this.DivCode = DivCode;
            addDivisionPanel();
            if (Job.equals("update")) {
                FillForm();
            }
        } catch (Exception ex) {
            MyUtils.showException("Division Master", ex);
        }
        
    }
    
    private void FillForm() {
        txtDivCode.setEditable(false);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("DivisionFormServlet");
            myHTTP.openOS();
            myHTTP.println("FillForm");
            myHTTP.println(DivCode);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            if (Result.startsWith("ERROR")) {
                MyUtils.showMessage(Result);
            } else {
                Depacketizer dp = new Depacketizer(Result);
                txtDivCode.setText(dp.getString());
                txtDivNm.setText(dp.getString());
                txtDivHCode.setText(dp.getString());
                txtDivHNm.setText(dp.getString());
            }
        } catch (Exception ex) {
            MyUtils.showException("Fill Form", ex);
        }
    }
    
    private String CreatePacket() {
        try {
            Packetizer a = new Packetizer();
            a.addString(txtDivCode.getText());
            a.addString(txtDivNm.getText());
            a.addString(txtDivHCode.getText());
            a.addString(txtDivHNm.getText());
            
            return a.getPacket();
        } catch (Exception ex) {
            MyUtils.showException("Create Packet", ex);
            return "PacketFail";
        }
    }
    
    private boolean FormFilled() {
        if (txtDivCode.getText().equals("")) {
            MyUtils.showMessage("Enter Division code");
            return false;
        } else if (txtDivNm.getText().equals("")) {
            MyUtils.showMessage("Enter Division Name");
            return false;
        } else if (txtDivHCode.getText().equals("")) {
            MyUtils.showMessage("Enter Division Head code");
            return false;
        } else if (txtDivHNm.getText().equals("")) {
            MyUtils.showMessage("Enter Division Head Name");
            return false;
        }
        return true;
    }
    
    private void addDivision() {
        if (FormFilled()) {
            String Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("DivisionFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("AddDivision");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else {
                        DivisionApplet.showHomePanel();
                    }
                } catch (Exception ex) {
                    MyUtils.showException("Add Division", ex);
                }
            }
        }
    }
    
    private void updateDivision() {
        if (FormFilled()) {
            String Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("DivisionFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("UpdateDivision");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    myHTTP.closeIS();
                    //MyUtils.showMessage(result);
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else {
                        DivisionApplet.showHomePanel();
                    }
                } catch (Exception ex) {
                    MyUtils.showException("Update Division", ex);
                }
            }
        }
        
    }
    
    private void addDivisionPanel() throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2;
        int height = screenSize.height / 3;
        setSize(width, height - 30);
        
        MyPanel EastPanel = new MyPanel(new BorderLayout());
        EastPanel.setPreferredSize(new Dimension(200, 300));
        tableModel1 = new DHeadTableModel();
        table = new MyTable(tableModel1);
        EastPanel.add(table.getGUI(), BorderLayout.CENTER);
        
        MyPanel MainPanel = new MyPanel(new BorderLayout());        
        MyPanel panDivDet = new MyPanel(new BorderLayout(10, 5), "Division Details");
        
        MyPanel panDivCode = new MyPanel(new BorderLayout());        
        MyPanel panlblDivCode = new MyPanel(new GridLayout(2, 1));
        
        MyLabel lbldivCode = new MyLabel(1, "Division Code:");
        panlblDivCode.add(lbldivCode);
        
        MyLabel lbldivHeadCode = new MyLabel(1, "Division Head Code:");
        panlblDivCode.add(lbldivHeadCode);
        
        panDivCode.add(panlblDivCode, BorderLayout.WEST);
        
        MyPanel pantxtDivCode = new MyPanel(new GridLayout(2, 1, 10, 5));
        
        txtDivCode = new MyTextField(10);
        pantxtDivCode.add(txtDivCode);
        
        txtDivHCode = new MyTextField(10);
        txtDivHCode.addKeyListener(new MyKeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    getDivHeadName(txtDivHCode.getText());
                    
                }
            }
        });
        pantxtDivCode.add(txtDivHCode);        
        panDivCode.add(pantxtDivCode, BorderLayout.CENTER);        
        panDivDet.add(panDivCode, BorderLayout.WEST);

        // adding dept Name and dept head Name using borderlayout and grid layout

        MyPanel panDivNm = new MyPanel(new BorderLayout());        
        MyPanel panlblDivNm = new MyPanel(new GridLayout(2, 1));
        
        MyLabel lbldivNm = new MyLabel(1, "Division Name:");
        panlblDivNm.add(lbldivNm);
        
        MyLabel lbldivHNm = new MyLabel(1, "Division Head Name:");
        panlblDivNm.add(lbldivHNm);
        
        panDivNm.add(panlblDivNm, BorderLayout.WEST);
        
        MyPanel pantxtDivNm = new MyPanel(new GridLayout(2, 1, 0, 10));        
        txtDivNm = new MyTextField();
        pantxtDivNm.add(txtDivNm);
        
        txtDivHNm = new MyTextField();
        txtDivHNm.setEnabled(false);
        pantxtDivNm.add(txtDivHNm);
        
        panDivNm.add(pantxtDivNm, BorderLayout.CENTER);        
        panDivDet.add(panDivNm, BorderLayout.CENTER);
        MainPanel.add(panDivDet, BorderLayout.NORTH);

        //adding Buttons
        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Add Division Details");
        if (Job.equals("add")) {
            btnAdd = new MyButton("Add Division", 2, Color.WHITE) {
                
                @Override
                public void onClick() {
                    addDivision();
                }
            };
        } else if (Job.equals("update")) {
            btnAdd = new MyButton("Update Division", 2, Color.WHITE) {
                
                @Override
                public void onClick() {
                    updateDivision();
                }
            };
        }
        panButtons.add(btnAdd);
        
        btnCancel = new MyButton("Cancel", 2, Color.WHITE) {
            
            @Override
            public void onClick() {
                DivisionApplet.showHomePanel();
            }
        };
        panButtons.add(btnCancel);

        MainPanel.add(panButtons, BorderLayout.CENTER);

        HeadName = new MyList();
        //MainPanel.add(HeadName,BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(MainPanel, BorderLayout.CENTER);
        this.add(EastPanel, BorderLayout.EAST);
        
        setVisible(true);
        
    }
    
    private void getDivHeadName(String DivHeadCode) {
        String Packet = CreatePacket();
        if (!Packet.equals("PacketFail")) {
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("DivisionFormServlet");
                myHTTP.openOS();
                myHTTP.println("GetDivHeadCode");
                myHTTP.println(DivHeadCode);
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                if (result.startsWith("ERROR")) {
                    MyUtils.showMessage(result);
                } else {
                    if (!"Not Found".equals(result)) {
                        txtDivHNm.setText(result);
                    } else {
                        txtDivHNm.setText("");
                        MyUtils.showMessage("Employee not present! Enter another DivHeadCode");
                        return;
                    }
                }
            } catch (Exception ex) {
                MyUtils.showException("getDivHeadName", ex);
            }
            
        }
        
    }
}
