package mmi.Home;

import mmi.CommPort.MyCommPort;
import mmi.data.LogReports;
import mmi.data.MyData;
import gui.MyButton;
import gui.MyLabel;
import gui.MyPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 * @type     : Java Class
 * @name     : HomeTab
 * @file     : HomeTab.java
 * @created  : May 14, 2011 11:43:39 AM
 * @version  : 1.0.0
 */
public class HomeTab extends MyPanel implements ActionListener{
    private JComboBox portListCombo;
    private MyLabel comPortLabel;
    /**
     * Constructor
     */
    public HomeTab(){
        super(new GridLayout(1, 1));
        try{
            String[] portList = MyCommPort.listPorts();
            if(portList==null){
                portListCombo = new JComboBox();
            }else{
                portListCombo = new JComboBox(portList);
            }
        }catch(Exception e){
            portListCombo = new JComboBox();
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
        portListCombo.setPreferredSize(new Dimension(150, 25));
        showOptionPane();
    }
    /**
     *
     */
    public final void showOptionPane(){
        MyPanel op = new MyPanel(new FlowLayout(FlowLayout.CENTER, 600, 50));
        op.setDefaultBG();

        int rowHeight = 35;
        int width = 600;

        MyLabel l = new MyLabel(
                MyLabel.TYPE_LABEL_HEADING,
                "Man Machine Interface – Traingle Waveform Generator",
                JLabel.CENTER);
        l.setPreferredSize(new Dimension(width, rowHeight));
        op.add(l);

        int verticalGap = 20;
        int numRows = 0;

        MyPanel inputPanel = new MyPanel(new BorderLayout(0, verticalGap), "");

        MyPanel p = new MyPanel(new BorderLayout(), null);
        MyPanel lp = new MyPanel(new GridLayout(0, 1, 0, verticalGap), null);
        MyPanel rp = new MyPanel(new GridLayout(0, 1, 0, verticalGap), null);

        //
        lp.add(new MyLabel(MyLabel.TYPE_LABEL, "User Name: "));
        rp.add(new MyLabel(MyLabel.TYPE_DATA, MyData.userName, JLabel.LEFT));
        numRows++;

        //
        lp.add(new MyLabel(MyLabel.TYPE_LABEL, "Logged-IN: "));
        rp.add(new MyLabel(MyLabel.TYPE_DATA, MyData.systemDate+" "+MyData.loginTime, JLabel.LEFT));
        numRows++;

        //Application Home
        lp.add(new MyLabel(MyLabel.TYPE_LABEL, "Application Home: "));
        rp.add(new MyLabel(MyLabel.TYPE_DATA, MyData.appHome, JLabel.LEFT));
        numRows++;

        //COM_PORT used
        lp.add(new MyLabel(MyLabel.TYPE_LABEL, "COM PORT used: "));
        comPortLabel = new MyLabel(MyLabel.TYPE_DATA, "", JLabel.LEFT);
        rp.add(comPortLabel);
        numRows++;

        p.add(lp, BorderLayout.WEST);
        p.add(rp, BorderLayout.CENTER);
        inputPanel.add(p, BorderLayout.CENTER);

        //buttons panel
        MyPanel bp = new MyPanel(new FlowLayout(FlowLayout.RIGHT, 2 , 2));
        bp.setPreferredSize(new Dimension(width, rowHeight));
        bp.add(portListCombo);
        MyButton b = new MyButton(this, "OpenPort", "Open");
        bp.add(b);
        b = new MyButton(this, "RefreshPortList", "PortList", "Refresh");
        bp.add(b);
        b = new MyButton(this, "ClosePort", "Close");
        bp.add(b);
        b = new MyButton(this, "Settings", "Settings", "Settings");
        bp.add(b);
        b = new MyButton(this, "Exit", "Exit", "Close");
        bp.add(b);
        inputPanel.add(bp, BorderLayout.SOUTH);
        numRows++;

        int height = inputPanel.getPreferredSize().height;
        inputPanel.setPreferredSize(new Dimension(width, height));
        op.add(inputPanel);

        setPortLabel();
        removeAll();
        add(op);
        validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("OpenPort")){
            try{
                String port = (String)portListCombo.getSelectedItem();
                if(port == null) return;
                port = port.substring(0, port.indexOf(" - "));
                //MyData.showInfoMessage("|"+port+"|");
                MyData.myCommPort.close();
                MyData.openPort(port);
             }catch(Exception ex){
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
            setPortLabel();
        }else if(e.getActionCommand().equals("RefreshPortList")){
            MyData.myCommPort.close();
            portListCombo.removeAllItems();
            try{
                String[] portList = MyCommPort.listPorts();
                if(portList != null){
                    for(int i=0;i<portList.length;i++){
                        portListCombo.addItem(portList[i]);
                    }
                }
            }catch(Exception ex){
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
            portListCombo.validate();
            setPortLabel();
        }else if(e.getActionCommand().equals("ClosePort")){
            try{
                MyData.myCommPort.close();
            }catch(Exception ex){
                LogReports.logError(ex);
            }
            setPortLabel();
        }else if(e.getActionCommand().equals("Settings")){
            new SettingsDialog().setVisible(true);
        }else if(e.getActionCommand().equals("Exit")){
            MyData.closeApplication();
        }
    }

    private void setPortLabel() {
        if(MyData.portOpened){
            comPortLabel.setForeground(Color.green.darker());
            comPortLabel.setText(MyData.comPort + " OPENED");
        }else{
            comPortLabel.setForeground(Color.red.darker());
            comPortLabel.setText(MyData.comPort + " NOT OPENED");
        }
    }
}