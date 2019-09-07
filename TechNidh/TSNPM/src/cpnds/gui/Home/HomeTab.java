package cpnds.gui.Home;

import cpnds.CommPort.MyCommPort;
import cpnds.data.LogReports;
import cpnds.data.MyData;
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
 * @version  : 1.2
 */
public class HomeTab extends MyPanel implements ActionListener{
    private JComboBox portListCombo;
    private MyLabel comPortLabel;
    private MyLabel dataDirLabel;

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
        MyPanel op = new MyPanel(new FlowLayout(FlowLayout.CENTER, 800, 50));
        op.setDefaultBG();

        int rowHeight = 35;
        int width = 700;

        String s = "COMPUTATION OF POWER FROM NEUTRON DETECTOR SIGNALS";
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL_HEADING, s, JLabel.CENTER);
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

        //Data directory
        lp.add(new MyLabel(MyLabel.TYPE_LABEL, "Data Directory: "));
        dataDirLabel = new MyLabel(MyLabel.TYPE_DATA, MyData.dataPath, JLabel.LEFT);
        rp.add(dataDirLabel);
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
        MyPanel bp = MyButton.getButtonPanel();
        bp.add(portListCombo);
        MyButton b = new MyButton(this, "OpenPort", "Open");
        bp.add(b);
        b = new MyButton(this, "RefreshPortList", "PortList", "Refresh");
        bp.add(b);
        b = new MyButton(this, "ClosePort", "Close");
        bp.add(b);
        b = new MyButton(this, "TestUART", "Test");
        bp.add(b);
        b = new MyButton(this, "Settings", "Settings", "Settings");
        bp.add(b);
        b = new MyButton(this, "Experiments", "Experiments");
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
            if(MyData.GENERATE_DUMMY_DATA){
                MyData.showInfoMessage("Please set generate dummy data FALSE!");
                return;
            }
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
            if(MyData.GENERATE_DUMMY_DATA){
                MyData.showInfoMessage("Please set generate dummy data FALSE!");
                return;
            }
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
            if(MyData.GENERATE_DUMMY_DATA){
                MyData.showInfoMessage("Please set generate dummy data FALSE!");
                return;
            }
            try{
                MyData.myCommPort.close();
            }catch(Exception ex){
                LogReports.logError(ex);
            }
            setPortLabel();
        }else if(e.getActionCommand().equals("TestUART")){
            if(MyData.GENERATE_DUMMY_DATA){
                MyData.showInfoMessage("Please set generate dummy data FALSE!");
                return;
            }
            try{
                MyData.myCommPort.testBBStatus(false);
                MyData.showInfoMessage("UART is Ready.");
            }catch(Exception ex){
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
         }else if(e.getActionCommand().equals("Settings")){
            new SettingsDialog().setVisible(true);
            dataDirLabel.setText(MyData.dataPath);
        }else if(e.getActionCommand().equals("Experiments")){
            SearchExperiment se = new SearchExperiment(this);
            if(se.dataFound){
                addMyPanel(se);
            }
        }else if(e.getActionCommand().equals("Exit")){
            MyData.closeApplication();
        }
    }

    /**
      *
      * @param p
      */
    private void addMyPanel(MyPanel p){
        removeAll();
        add(p);
        validate();
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