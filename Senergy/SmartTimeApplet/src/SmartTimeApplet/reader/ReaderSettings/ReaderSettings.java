/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.ReaderSettings;

/**
 *
 * @author Gaurav
 */
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import lib.Classes.RSClass;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

public class ReaderSettings extends MyPanel implements ActionListener {

    public JComboBox cmbReaderNo, cmbBaudRate, cmbReaderMode, cmbAPB1;
    public JComboBox cmbAPB2, cmbChkHolidy, cmbPinBio, cmbValDOB, cmbMutiRej;
    public MyTextField txtLocation, txtDiv, txtSelfIP, txtSubMask, txtGateIP;
    public MyLabel lblDate;
    public MyTextField txtAPBLevel;
    public MyTextField txtServerIP1, txtServerIP2, txtBuild, txtServerPort, txtListenPort, txtReaderNo;
    public MyTextField txtAPBBlock, txtUseRDB, txtAutoOff, txtUCode, txtUnUsedEntry, txtCarRDelay;
    public MyTextField txtLEDely, txtDoorOpnAlrm, txtKyTmOut, txtPUTmOut, txtPUCnt, txtOnLRespTmOut;
    public MyTextField txtAppID1, txtAppID2, txtRdrStr, txtTotlRec, txtRecOnS1, txtRecOnS2;
    public MyButton btnIPSet, btnSetMnu1, btnSetMnu2, btnSetMnu3, btnGet, btnDefault, btnSetAll, btnExit;
    public JCheckBox chkRdrStr, chkRecS2, chkRecS1, chkDate;
    public RSClass p = new RSClass();
    ReaderSettingApplet SettingApplet;
    String readerNo;
    MyPanel MainPanel;

    public ReaderSettings(ReaderSettingApplet SettingApplet, String readerNo, String readerList[]) {
        this.SettingApplet = SettingApplet;
        this.readerNo = readerNo;
        addReaderSettings(readerList);
        addListeners();
    }

    private void FillForm() {
        try {
            setEntries();
        } catch (Exception e) {
            MyUtils.showException("FillForm", e);
        }
    }

    private void setCmbAPB() {
        // removeListeners();
        try {
            int apb = Integer.parseInt(txtAPBLevel.getText());
            if (apb >= 0 && apb < 16) {
                cmbAPB1.setSelectedIndex(0);
                cmbAPB2.setEnabled(true);
                cmbAPB2.setSelectedIndex(apb);
            } else if (apb >= 16 && apb < 32) {
                cmbAPB1.setSelectedIndex(1);
                cmbAPB2.setEnabled(true);
                cmbAPB2.setSelectedIndex(apb - 16);
            } else if (apb == 32) {
                cmbAPB1.setSelectedIndex(2);
                cmbAPB2.setSelectedIndex(16);
                cmbAPB2.setEnabled(false);
            }

        } catch (Exception ex) {
            MyUtils.showException("ERROR: ", ex);
        }
        // addListeners();
    }

    private void setAPB() {
        // removeListeners();
        try {
            int apb1 = cmbAPB1.getSelectedIndex();
            int apb2 = cmbAPB2.getSelectedIndex();
            switch (apb1) {
                case 0:
                    cmbAPB2.setEnabled(true);
                    if (apb2 != 16) {
                        txtAPBLevel.setText(Integer.toString(apb2));
                    } else {
                        txtAPBLevel.setText("0");
                    }
                    break;
                case 1:
                    cmbAPB2.setEnabled(true);
                    if (apb2 != 16) {
                        txtAPBLevel.setText(Integer.toString(apb2 + 16));
                    } else {
                        txtAPBLevel.setText("16");
                    }
                    break;
                case 2:
                    txtAPBLevel.setText("32");
                    cmbAPB2.setSelectedIndex(16);
                    cmbAPB2.setEnabled(false);
                    break;
            }

        } catch (Exception ex) {
            MyUtils.showException("ERROR: ", ex);
        }
        // addListeners();
    }

    private void setEntries() {
        if (cmbReaderNo.getSelectedItem() != null) {
            String ReaderNo = cmbReaderNo.getSelectedItem().toString();
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("RSFormServlet");
                myHTTP.openOS();
                myHTTP.println("SetEntries");
                myHTTP.println(ReaderNo);
                myHTTP.closeOS();
                myHTTP.openIS();
                String Result = myHTTP.readLine();
                myHTTP.closeIS();
                if (Result.startsWith("ERROR")) {
                    MyUtils.showMessage(Result);
                } else {
                    p.clear();
                    p.ReaderNo = ReaderNo;
                    p.getDatabaseData(Result);
                    setData();
                }
            } catch (Exception e) {
                MyUtils.showException("SetEntries", e);
            }
        }
    }

    private void getData() {
        p.Location = txtLocation.getText();
        p.Division = txtDiv.getText();
        p.SelfIP = txtSelfIP.getText();
        p.ServerIP1 = txtServerIP1.getText();
        p.ServerIP2 = txtServerIP2.getText();
        p.SubnetMask = txtSubMask.getText();
        p.ListenPort = txtListenPort.getText();
        p.ServerPort = txtServerPort.getText();
        p.GateWayIP = txtGateIP.getText();
        p.ReaderNo = txtReaderNo.getText();
        p.DoorDelay = txtCarRDelay.getText();
        p.LedDelay = txtLEDely.getText();
        p.DoorOpenAlarm = txtDoorOpnAlrm.getText();
        p.KeyTimeOut = txtKyTmOut.getText();
        p.PUTmOut = txtPUTmOut.getText();
        p.PUCnt = txtPUCnt.getText();
        p.OnlineRespTimeout = txtOnLRespTmOut.getText();
        p.AppID1 = txtAppID1.getText();
        p.AppID2 = txtAppID2.getText();
        p.AntiPassBack = txtAPBBlock.getText();
        p.APBLevel = txtAPBLevel.getText();
        p.UseRdrDB = txtUseRDB.getText();
        p.AutoOffline = txtAutoOff.getText();
        p.UnitCode = txtUCode.getText();
        p.UnusedEntry = txtUnUsedEntry.getText();
        p.ReaderString = txtRdrStr.getText();
        p.Building = txtBuild.getText();
        p.TotalRec = txtTotlRec.getText();
        p.RecOnS1 = txtRecOnS1.getText();
        p.RecOnS2 = txtRecOnS2.getText();
        p.DateTime = lblDate.getText();
        p.BaudRate = cmbBaudRate.getSelectedIndex();
        p.ReaderMode = cmbReaderMode.getSelectedIndex();
        p.CheckShiftHoliday = cmbChkHolidy.getSelectedIndex();
        p.CheckPin = cmbPinBio.getSelectedIndex();
        p.CheckValidityDOB = cmbValDOB.getSelectedIndex();
        p.MultiRejAlarm = cmbMutiRej.getSelectedIndex();
    }

    private void setData() {
        txtLocation.setText(p.Location);
        txtDiv.setText(p.Division);
        txtSelfIP.setText(p.SelfIP);
        txtServerIP1.setText(p.ServerIP1);
        txtServerIP2.setText(p.ServerIP2);
        txtSubMask.setText(p.SubnetMask);
        txtListenPort.setText(p.ListenPort);
        txtServerPort.setText(p.ServerPort);
        txtGateIP.setText(p.GateWayIP);
        txtReaderNo.setText(p.ReaderNo);
        txtCarRDelay.setText(p.DoorDelay);
        txtLEDely.setText(p.LedDelay);
        txtDoorOpnAlrm.setText(p.DoorOpenAlarm);
        txtKyTmOut.setText(p.KeyTimeOut);
        txtPUTmOut.setText(p.PUTmOut);
        txtPUCnt.setText(p.PUCnt);
        txtOnLRespTmOut.setText(p.OnlineRespTimeout);
        txtAppID1.setText(p.AppID1);
        txtAppID2.setText(p.AppID2);
        txtAPBBlock.setText(p.AntiPassBack);
        txtAPBLevel.setText(p.APBLevel);
        setCmbAPB();
        txtUseRDB.setText(p.UseRdrDB);
        txtAutoOff.setText(p.AutoOffline);
        txtUCode.setText(p.UnitCode);
        txtUnUsedEntry.setText(p.UnusedEntry);
        txtRdrStr.setText(p.ReaderString);
        txtBuild.setText(p.Building);
        txtTotlRec.setText(p.TotalRec);
        txtRecOnS1.setText(p.RecOnS1);
        txtRecOnS2.setText(p.RecOnS2);
        lblDate.setText(p.DateTime);
        cmbBaudRate.setSelectedIndex(p.BaudRate);
        cmbReaderMode.setSelectedIndex(p.ReaderMode);
        cmbChkHolidy.setSelectedIndex(p.CheckShiftHoliday);
        cmbPinBio.setSelectedIndex(p.CheckPin);
        cmbValDOB.setSelectedIndex(p.CheckValidityDOB);
        cmbMutiRej.setSelectedIndex(p.MultiRejAlarm);
    }

    private void GetSettingsFromReader() {
        try {
            String ReaderNo = cmbReaderNo.getSelectedItem().toString();
            Packetizer a = new Packetizer();
            a.addString(ReaderNo);
            String packet = a.getPacket();
            MyHTTP myHTTP = MyUtils.createServletConnection("RSCommServlet");
            myHTTP.openOS();
            myHTTP.println("GetReaderInfo");
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            myHTTP.closeIS();
            if (Result.startsWith("ERROR")) {
                MyUtils.showMessage(Result);
            } else {
                Depacketizer dp = new Depacketizer(Result);
                String menu1Menu2 = dp.getString();
                String IPSettings = dp.getString();
                String rdrString = dp.getString();
                String recordPointer = dp.getString();
                String dateTime = dp.getString();
                p.clear();
                p.getReaderMenu1Menu2(menu1Menu2);
                p.getReaderIPSettings(IPSettings);
                p.getReaderString(rdrString);
                p.getReaderRecordPointer(recordPointer);
                p.getReaderDateTime(dateTime);
                setData();
                EnableSet();
            }

        } catch (Exception e) {
            MyUtils.showException("Get Reader Settings", e);
        }
    }

    private void EnableSet() {
        btnIPSet.setEnabled(true);
        btnSetMnu1.setEnabled(true);
        btnSetMnu2.setEnabled(true);
        btnSetMnu3.setEnabled(true);
        btnSetAll.setEnabled(true);
        btnDefault.setEnabled(true);
    }

    private void SetIPSettings(Boolean is_setall) {
        try {
            getData();
            String ReaderNo = cmbReaderNo.getSelectedItem().toString();
            String packet = p.CreatePacketIPSettings();
            MyHTTP myHTTP = MyUtils.createServletConnection("RSCommServlet");
            myHTTP.openOS();
            myHTTP.println("SetReaderIPSettings");
            myHTTP.println(ReaderNo);
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            myHTTP.closeIS();
            if (Result.startsWith("ERROR")) {
                MyUtils.showException("Database ERROR", new Exception(Result));

            } else {
                if (is_setall != true) {
                    MyUtils.showMessage("IP Settings set.");
                    UpdateConfig();
                    SaveIPSettingsToDB();
                }
            }

        } catch (Exception e) {
            MyUtils.showException("Set IPSettings", e);
        }
    }

    private void SetMenu1(Boolean is_setall) {
        try {
            getData();
            String ReaderNo = cmbReaderNo.getSelectedItem().toString();
            String packet = p.CreatePacketMenu1();
            MyHTTP myHTTP = MyUtils.createServletConnection("RSCommServlet");
            myHTTP.openOS();
            myHTTP.println("SetReaderMenu1");
            myHTTP.println(ReaderNo);
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            myHTTP.closeIS();
            if (Result.startsWith("ERROR")) {
                MyUtils.showException("Database ERROR", new Exception(Result));

            } else {
                if (is_setall != true) {
                    MyUtils.showMessage("Menu 1 Set.");
                    UpdateConfig();
                    saveMenu1ToDB();
                }
            }

        } catch (Exception e) {
            MyUtils.showException("Set Menu 1", e);
        }
    }

    private void SetMenu2(Boolean is_setall) {
        try {
            getData();
            String ReaderNo = cmbReaderNo.getSelectedItem().toString();
            String packet = p.CreatePacketMenu2();
            MyHTTP myHTTP = MyUtils.createServletConnection("RSCommServlet");
            myHTTP.openOS();
            myHTTP.println("SetReaderMenu2");
            myHTTP.println(ReaderNo);
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            myHTTP.closeIS();
            if (Result.startsWith("ERROR")) {
                MyUtils.showException("Database ERROR", new Exception(Result));

            } else {
                if (is_setall != true) {
                    MyUtils.showMessage("Menu 2 Set.");
                    UpdateConfig();
                    saveMenu2ToDB();
                }
            }

        } catch (Exception e) {
            MyUtils.showException("Set Menu 2", e);
        }
    }

    private void SetMenu3(Boolean is_setall) {
        try {
            getData();
            String ReaderNo = cmbReaderNo.getSelectedItem().toString();
            String packet = p.CreatePacketMenu3();
            MyHTTP myHTTP = MyUtils.createServletConnection("RSCommServlet");
            myHTTP.openOS();
            myHTTP.println("SetReaderMenu3");
            myHTTP.println(ReaderNo);
            myHTTP.println(packet);
            myHTTP.closeOS();
            myHTTP.openIS();
            String Result = myHTTP.readLine();
            myHTTP.closeIS();
            if (Result.startsWith("ERROR")) {
                MyUtils.showException("Database ERROR", new Exception(Result));

            } else {
                if (is_setall != true) {
                    MyUtils.showMessage("Menu 3 Set.");
                    UpdateConfig();
                    saveMenu3ToDB();
                }
            }

        } catch (Exception e) {
            MyUtils.showException("Set Menu 3", e);
        }
    }

    private void UpdateConfig() {
        try {
            getData();
            String ReaderNo = cmbReaderNo.getSelectedItem().toString();
            MyHTTP myHTTP = MyUtils.createServletConnection("RSCommServlet");
            myHTTP.openOS();
            myHTTP.println("UpdateConfig");
            myHTTP.println(ReaderNo);
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

    private void SetDefault() {
        setDefaultData();
    }

    private void SetAll() {
        SetIPSettings(true);
        SetMenu1(true);
        SetMenu2(true);
        SetMenu3(true);
        UpdateConfig();
        SaveIPSettingsToDB();
        saveMenu1ToDB();
        saveMenu2ToDB();
        saveMenu3ToDB();
        MyUtils.showMessage("All Settings set");
    }

    private void Exit() {
        SettingApplet.showHomePanel();
    }

    private void addListeners() {
        cmbReaderNo.addActionListener(this);
        cmbReaderNo.setActionCommand("ReaderNo");
        cmbAPB1.addActionListener(this);
        cmbAPB2.addActionListener(this);
        cmbAPB1.setActionCommand("APB");
        cmbAPB2.setActionCommand("APB");
        chkRecS2.addActionListener(this);
        chkRecS2.setActionCommand("ChkRecS2");
        chkRecS1.addActionListener(this);
        chkRecS1.setActionCommand("ChkRecS1");
        chkDate.addActionListener(this);
        chkDate.setActionCommand("ChkDate");
        chkRdrStr.addActionListener(this);
        chkRdrStr.setActionCommand("ChkRdrStr");
    }

    private void removeListeners() {
        cmbAPB1.removeActionListener(this);
        cmbAPB2.removeActionListener(this);

    }

    public void addReaderSettings(String readerList[]) {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width / 1.5);
        int height = (int) (screenSize.height / 1.1);
        setSize(width, height);

        MainPanel = new MyPanel(new BorderLayout());
        MyPanel panNoLocDiv = new MyPanel(new GridLayout(1, 3, 10, 5), "Reader Information");
        panNoLocDiv.setPreferredSize(new Dimension(width, height / 15));
        MyPanel panReaderNo = new MyPanel(new BorderLayout());
        MyLabel lblReaderNo = new MyLabel(1, "Select Reader No:");
        panReaderNo.add(lblReaderNo, BorderLayout.WEST);

        cmbReaderNo = new JComboBox(readerList);
        cmbReaderNo.setSelectedItem(readerNo);
        panReaderNo.add(cmbReaderNo, BorderLayout.CENTER);
        panNoLocDiv.add(panReaderNo);

        MyPanel panLocation = new MyPanel(new BorderLayout());
        MyLabel lblLocation = new MyLabel(1, "Location:");
        panLocation.add(lblLocation, BorderLayout.WEST);

        txtLocation = new MyTextField();
        panLocation.add(txtLocation, BorderLayout.CENTER);
        panNoLocDiv.add(panLocation);

        MyPanel panDiv = new MyPanel(new BorderLayout());
        MyLabel lblDiv = new MyLabel(1, "Division:");
        panDiv.add(lblDiv, BorderLayout.WEST);

        txtDiv = new MyTextField();
        panDiv.add(txtDiv, BorderLayout.CENTER);
        panNoLocDiv.add(panDiv);

        MainPanel.add(panNoLocDiv, BorderLayout.NORTH);
        MyPanel panMainCenter = new MyPanel(new BorderLayout());
        MyPanel panIPConfig = new MyPanel(new GridLayout(1, 3, 10, 5), "IP Settings");
        panIPConfig.setPreferredSize(new Dimension(width, height / 7));
        MyPanel panIP = new MyPanel(new BorderLayout());
        MyPanel panlblIPAdd = new MyPanel(new GridLayout(3, 1));

        MyLabel lblSelfIP = new MyLabel(1, "Self IP:");
        panlblIPAdd.add(lblSelfIP);
        MyLabel lblSubMask = new MyLabel(1, "SubNet Mask:");
        panlblIPAdd.add(lblSubMask);
        MyLabel lblGetIP = new MyLabel(1, "Gateway IP:");
        panlblIPAdd.add(lblGetIP);
        panIP.add(panlblIPAdd, BorderLayout.WEST);

        MyPanel pantxtIPAdd = new MyPanel(new GridLayout(3, 1));
        txtSelfIP = new MyTextField();
        pantxtIPAdd.add(txtSelfIP);
        txtSubMask = new MyTextField();
        pantxtIPAdd.add(txtSubMask);
        txtGateIP = new MyTextField();
        pantxtIPAdd.add(txtGateIP);
        panIP.add(pantxtIPAdd, BorderLayout.CENTER);
        panIPConfig.add(panIP);

        MyPanel panServerIP = new MyPanel(new BorderLayout());
        MyPanel panlblServerIP = new MyPanel(new GridLayout(3, 1));
        MyLabel lblServerIP1 = new MyLabel(1, "Server 1 IP:");
        panlblServerIP.add(lblServerIP1);
        MyLabel lblServerIP2 = new MyLabel(1, "Server 2 IP:");
        panlblServerIP.add(lblServerIP2);
        MyLabel lblBuild = new MyLabel(1, "Building:");
        panlblServerIP.add(lblBuild);
        panServerIP.add(panlblServerIP, BorderLayout.WEST);

        MyPanel pantxtServerIP = new MyPanel(new GridLayout(3, 1));
        txtServerIP1 = new MyTextField();
        pantxtServerIP.add(txtServerIP1);
        txtServerIP2 = new MyTextField();
        pantxtServerIP.add(txtServerIP2);
        MyPanel panBuild = new MyPanel(new BorderLayout());
        txtBuild = new MyTextField(5);
        panBuild.add(txtBuild, BorderLayout.WEST);
        MyLabel lbl99 = new MyLabel(1, "(99.999)");
        panBuild.add(lbl99, BorderLayout.CENTER);
        pantxtServerIP.add(panBuild);
        panServerIP.add(pantxtServerIP, BorderLayout.CENTER);
        panIPConfig.add(panServerIP);

        MyPanel panPort = new MyPanel(new BorderLayout(30, 5));
        MyPanel panlblSPort = new MyPanel(new GridLayout(3, 1));
        MyLabel lblServerPort = new MyLabel(1, "Server Port:");
        panlblSPort.add(lblServerPort);
        MyLabel lblListenPort = new MyLabel(1, "Listen Port:");
        panlblSPort.add(lblListenPort);
        MyLabel lblfill1 = new MyLabel();
        panlblSPort.add(lblfill1);
        panPort.add(panlblSPort, BorderLayout.WEST);

        MyPanel pantxtSPort = new MyPanel(new GridLayout(3, 1));
        txtServerPort = new MyTextField("3001");
        pantxtSPort.add(txtServerPort);
        txtListenPort = new MyTextField("3000");
        pantxtSPort.add(txtListenPort);
        MyPanel panbtnSet = new MyPanel(new FlowLayout(FlowLayout.RIGHT));

        btnIPSet = new MyButton("Set", 0) {

            @Override
            public void onClick() {
                SetIPSettings(false);
                UpdateConfig();
            }
        };
        btnIPSet.setEnabled(false);
        panbtnSet.add(btnIPSet);
        pantxtSPort.add(panbtnSet);
        panPort.add(pantxtSPort, BorderLayout.CENTER);

        panIPConfig.add(panPort);
        panMainCenter.add(panIPConfig, BorderLayout.NORTH);
        MainPanel.add(panMainCenter, BorderLayout.CENTER);

        //Reader Menu
        //Menu1
        MyPanel panMnu1Mnu2 = new MyPanel(new GridLayout(1, 2));
        MyPanel panMenu1 = new MyPanel(new BorderLayout(), "Menu1");
        MyPanel panlbltxtMenu1 = new MyPanel(new BorderLayout());
        MyPanel panlblMenu1 = new MyPanel(new GridLayout(12, 1, 2, 2));

        MyLabel lblReadrNo = new MyLabel(1, "Reader No:");
        panlblMenu1.add(lblReadrNo);

        MyLabel lblBRate = new MyLabel(1, "Baud Rate(Serial):");
        panlblMenu1.add(lblBRate);

        MyLabel lblReaderMode = new MyLabel(1, "Reader Mode:");
        panlblMenu1.add(lblReaderMode);

        MyLabel lblAPBLvl = new MyLabel(1, "APB Level:");
        panlblMenu1.add(lblAPBLvl);

        MyLabel lblAPBBlock = new MyLabel(1, "APB Block:");
        panlblMenu1.add(lblAPBBlock);

        MyLabel lblUseRDB = new MyLabel(1, "Use Reader Database:");
        panlblMenu1.add(lblUseRDB);

        MyLabel lblchkSHolidy = new MyLabel(1, "Check ShiftHoliday:");
        panlblMenu1.add(lblchkSHolidy);

        MyLabel lblpinBio = new MyLabel(1, "PIN/BIO Check:");
        panlblMenu1.add(lblpinBio);

        MyLabel lblvalDob = new MyLabel(1, "ValidityDOBCheck:");
        panlblMenu1.add(lblvalDob);

        MyLabel lblAutoOff = new MyLabel(1, "Auto OffLine:");
        panlblMenu1.add(lblAutoOff);

        MyLabel lblUCode = new MyLabel(1, "Unit Code:");
        panlblMenu1.add(lblUCode);

        MyLabel lblUEntry = new MyLabel(1, "Unused Entry:");
        panlblMenu1.add(lblUEntry);

        MyPanel panlblSet = new MyPanel(new FlowLayout(FlowLayout.RIGHT));

        MyLabel lblUnUseEntry = new MyLabel(1, "(0-All Store  1-Feed Back Store)");
        panlblSet.add(lblUnUseEntry);

        btnSetMnu1 = new MyButton("Set", 0) {

            @Override
            public void onClick() {
                SetMenu1(false);
                UpdateConfig();
            }
        };
        btnSetMnu1.setEnabled(false);
        panlblSet.add(btnSetMnu1);

        panlbltxtMenu1.add(panlblMenu1, BorderLayout.WEST);
        panMenu1.add(panlbltxtMenu1, BorderLayout.CENTER);

        MyPanel pantxtMenu1 = new MyPanel(new GridLayout(12, 1, 2, 2));
        MyPanel panReadrNo = new MyPanel(new BorderLayout(10, 0));

        txtReaderNo = new MyTextField(5);
        panReadrNo.add(txtReaderNo, BorderLayout.WEST);

        MyLabel lblReaderLim = new MyLabel(1, "(1-90)");
        panReadrNo.add(lblReaderLim, BorderLayout.CENTER);
        pantxtMenu1.add(panReadrNo);

        String[] strBaudRate = {"9600", "19200", "38400", "57600"};
        cmbBaudRate = new JComboBox(strBaudRate);
        pantxtMenu1.add(cmbBaudRate);

        String[] strRdrMode = {"OnLine", "OffLine", "Idle"};
        cmbReaderMode = new JComboBox(strRdrMode);
        pantxtMenu1.add(cmbReaderMode);

        MyPanel panAPBLvl = new MyPanel(new GridLayout(1, 3, 2, 0));
        txtAPBLevel = new MyTextField(3);
        txtAPBLevel.setEditable(false);
        txtAPBLevel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panAPBLvl.add(txtAPBLevel);

        String[] strAPB1 = {"IN", "OUT", "IN/OUT"};
        cmbAPB1 = new JComboBox(strAPB1);
        panAPBLvl.add(cmbAPB1);

        String[] strAPB2 = {"APB Level- 0", "APB Level- 1", "APB Level- 2", "APB Level- 3", "APB Level- 4", "APB Level- 5", "APB Level- 6", "APB Level- 7", "APB Level- 8", "APB Level- 9", "APB Level- 10", "APB Level- 11", "APB Level- 12", "APB Level- 13", "APB Level- 14", "APB Level- 15", "NO APB"};
        cmbAPB2 = new JComboBox(strAPB2);
        panAPBLvl.add(cmbAPB2);

        pantxtMenu1.add(panAPBLvl);

        MyPanel panAPBBlock = new MyPanel(new BorderLayout());
        txtAPBBlock = new MyTextField(5);
        panAPBBlock.add(txtAPBBlock, BorderLayout.WEST);

        MyLabel lblAPBBlck = new MyLabel(1, "(0,58)");
        panAPBBlock.add(lblAPBBlck, BorderLayout.CENTER);

        pantxtMenu1.add(panAPBBlock);


        MyPanel panUseRDB = new MyPanel(new BorderLayout());
        txtUseRDB = new MyTextField(5);
        panUseRDB.add(txtUseRDB, BorderLayout.WEST);

        MyLabel lblURDB = new MyLabel(1, "(0-255)");
        panUseRDB.add(lblURDB, BorderLayout.CENTER);

        pantxtMenu1.add(panUseRDB);

        String[] strShiftH = {"Enable Shift", "Enable Holiday", "Disable Both", "Enable Both"};
        cmbChkHolidy = new JComboBox(strShiftH);
        pantxtMenu1.add(cmbChkHolidy);

        String[] strPinBio = {"Disable Both", "Enable BIO", "Enable PIN", "Enable Both"};
        cmbPinBio = new JComboBox(strPinBio);
        pantxtMenu1.add(cmbPinBio);

        String[] strValDOB = {"Disable Both", "Enable Both", "Enable Validity", "Enable DOB"};
        cmbValDOB = new JComboBox(strValDOB);
        pantxtMenu1.add(cmbValDOB);

        MyPanel panAutoOff = new MyPanel(new BorderLayout());
        txtAutoOff = new MyTextField(5);
        panAutoOff.add(txtAutoOff, BorderLayout.WEST);

        MyLabel lblAOffL = new MyLabel(1, "(0-255 Sec.)");
        panAutoOff.add(lblAOffL, BorderLayout.CENTER);

        pantxtMenu1.add(panAutoOff);

        MyPanel panUCode = new MyPanel(new BorderLayout());
        txtUCode = new MyTextField(5);
        panUCode.add(txtUCode, BorderLayout.WEST);

        MyLabel lblUnitCode = new MyLabel(1, "(0,65-90)");
        panUCode.add(lblUnitCode, BorderLayout.CENTER);
        pantxtMenu1.add(panUCode);

        txtUnUsedEntry = new MyTextField();
        pantxtMenu1.add(txtUnUsedEntry);

        panlbltxtMenu1.add(pantxtMenu1, BorderLayout.CENTER);
        panMenu1.add(panlblSet, BorderLayout.SOUTH);
        panMnu1Mnu2.add(panMenu1);

        //Menu2
        MyPanel panMenu2 = new MyPanel(new BorderLayout(), "Menu2");
        MyPanel panlbltxtMenu2 = new MyPanel(new BorderLayout());
        MyPanel panlblMenu2 = new MyPanel(new GridLayout(12, 1, 2, 2));

        MyLabel lblCardRDelay = new MyLabel(1, "Card Read Delay:");
        panlblMenu2.add(lblCardRDelay);

        MyLabel lblLEDelay = new MyLabel(1, "LED Delay:");
        panlblMenu2.add(lblLEDelay);

        MyLabel lblDoorOpnAlrm = new MyLabel(1, "Door Open Alarm:");
        panlblMenu2.add(lblDoorOpnAlrm);

        MyLabel lblKyTmOut = new MyLabel(1, "Key Timeout:");
        panlblMenu2.add(lblKyTmOut);

        MyLabel lblPunchUTm = new MyLabel(1, "Punch Upload Timeout:");
        panlblMenu2.add(lblPunchUTm);

        MyLabel lblPunchUCnt = new MyLabel(1, "Punch Upload Count:");
        panlblMenu2.add(lblPunchUCnt);

        MyLabel lblOnLRespTmOut = new MyLabel(1, "OnLineRespTimeout:");
        panlblMenu2.add(lblOnLRespTmOut);

        MyLabel lblMultRejAlrm = new MyLabel(1, "Multireject Alarm:");
        panlblMenu2.add(lblMultRejAlrm);

        MyLabel lblAppID1 = new MyLabel(1, "Application ID #1:");
        panlblMenu2.add(lblAppID1);

        MyLabel lblAppID2 = new MyLabel(1, "Application ID #2:");
        panlblMenu2.add(lblAppID2);

        panlbltxtMenu2.add(panlblMenu2, BorderLayout.WEST);
        panMenu2.add(panlbltxtMenu2, BorderLayout.WEST);

        MyPanel pantxtMenu2 = new MyPanel(new GridLayout(12, 1, 2, 2));
        MyPanel pantxtCardRDelay = new MyPanel(new BorderLayout());

        txtCarRDelay = new MyTextField(5);
        pantxtCardRDelay.add(txtCarRDelay, BorderLayout.WEST);

        MyLabel lblCarRDelay = new MyLabel(1, "(0-255 Sec.)");
        pantxtCardRDelay.add(lblCarRDelay, BorderLayout.CENTER);
        pantxtMenu2.add(pantxtCardRDelay);

        MyPanel pantxtLEDly = new MyPanel(new BorderLayout());

        txtLEDely = new MyTextField(5);
        pantxtLEDly.add(txtLEDely, BorderLayout.WEST);

        MyLabel lblLEDely = new MyLabel(1, "(0-255 Sec.)");
        pantxtLEDly.add(lblLEDely, BorderLayout.CENTER);
        pantxtMenu2.add(pantxtLEDly);

        MyPanel panDoorOpnAlrm = new MyPanel(new BorderLayout());

        txtDoorOpnAlrm = new MyTextField(5);
        panDoorOpnAlrm.add(txtDoorOpnAlrm, BorderLayout.WEST);

        MyLabel lblDrOpnAlrm = new MyLabel(1, "(0-255 Sec.)");
        panDoorOpnAlrm.add(lblDrOpnAlrm, BorderLayout.CENTER);

        pantxtMenu2.add(panDoorOpnAlrm);

        MyPanel panKyTmOut = new MyPanel(new BorderLayout());

        txtKyTmOut = new MyTextField(5);
        panKyTmOut.add(txtKyTmOut, BorderLayout.WEST);

        MyLabel lblKeyTmOut = new MyLabel(1, "(0-255 Sec.)");
        panKyTmOut.add(lblKeyTmOut, BorderLayout.CENTER);
        pantxtMenu2.add(panKyTmOut);

        MyPanel panPUTmOut = new MyPanel(new BorderLayout());

        txtPUTmOut = new MyTextField(5);
        panPUTmOut.add(txtPUTmOut, BorderLayout.WEST);

        MyLabel lblPUTmOut = new MyLabel(1, "(0-10 Sec.)");
        panPUTmOut.add(lblPUTmOut, BorderLayout.CENTER);

        pantxtMenu2.add(panPUTmOut);

        MyPanel panPUCnt = new MyPanel(new BorderLayout());

        txtPUCnt = new MyTextField(5);
        panPUCnt.add(txtPUCnt, BorderLayout.WEST);

        MyLabel lblPUCnt = new MyLabel(1, "(0-10)");
        panPUCnt.add(lblPUCnt, BorderLayout.CENTER);

        pantxtMenu2.add(panPUCnt);

        MyPanel panOnLRespTmOut = new MyPanel(new BorderLayout());

        txtOnLRespTmOut = new MyTextField(5);
        panOnLRespTmOut.add(txtOnLRespTmOut, BorderLayout.WEST);

        MyLabel lblOLRespTmOut = new MyLabel(1, "(0-255 Sec.)");
        panOnLRespTmOut.add(lblOLRespTmOut, BorderLayout.CENTER);

        pantxtMenu2.add(panOnLRespTmOut);

        String[] strMultRej = {"Disable", "Enable"};
        cmbMutiRej = new JComboBox(strMultRej);
        pantxtMenu2.add(cmbMutiRej);

        MyPanel panAppID1 = new MyPanel(new BorderLayout());

        txtAppID1 = new MyTextField(5);
        panAppID1.add(txtAppID1, BorderLayout.WEST);

        MyLabel lblApp1 = new MyLabel(1, "(18433)");
        panAppID1.add(lblApp1, BorderLayout.CENTER);

        pantxtMenu2.add(panAppID1);

        MyPanel panAppID2 = new MyPanel(new BorderLayout());

        txtAppID2 = new MyTextField(5);
        panAppID2.add(txtAppID2, BorderLayout.WEST);

        MyLabel lblApp2 = new MyLabel(1, "(18434)");
        panAppID2.add(lblApp2, BorderLayout.CENTER);

        pantxtMenu2.add(panAppID2);

        panMenu2.add(pantxtMenu2, BorderLayout.CENTER);

        MyPanel panMnu2Set = new MyPanel(new FlowLayout(FlowLayout.RIGHT));

        btnSetMnu2 = new MyButton("Set", 0) {

            @Override
            public void onClick() {
                SetMenu2(false);
                UpdateConfig();
            }
        };
        btnSetMnu2.setEnabled(false);
        panMnu2Set.add(btnSetMnu2);
        panMenu2.add(panMnu2Set, BorderLayout.SOUTH);
        panMnu1Mnu2.add(panMenu2);
        panMainCenter.add(panMnu1Mnu2, BorderLayout.CENTER);

        //Menu3
        MyPanel panMnu3NBtn = new MyPanel(new BorderLayout());

        MyPanel panMnu3 = new MyPanel(new BorderLayout(), "Menu3");
        panMnu3.setPreferredSize(new Dimension(width, height / 5));

        MyPanel panMnu3Center = new MyPanel(new GridLayout(1, 2));

        MyPanel panlbltxtMnu31 = new MyPanel(new BorderLayout());

        MyPanel panlblMnu31 = new MyPanel(new GridLayout(3, 1));

        MyPanel panlblRdrStr = new MyPanel(new BorderLayout());

        MyLabel lblRdrStr = new MyLabel(1, "Reader String");
        panlblRdrStr.add(lblRdrStr, BorderLayout.CENTER);

        chkRdrStr = new JCheckBox();
        panlblRdrStr.add(chkRdrStr, BorderLayout.EAST);

        panlblMnu31.add(panlblRdrStr);

        MyLabel lblEmpStrt = new MyLabel(1, "Record On Server 2");
        panlblMnu31.add(lblEmpStrt);

        panlbltxtMnu31.add(panlblMnu31, BorderLayout.WEST);

        MyPanel txtMnu31 = new MyPanel(new GridLayout(3, 1));

        MyPanel panRdrStr = new MyPanel(new BorderLayout());
        txtRdrStr = new MyTextField(15);
        txtRdrStr.setHorizontalAlignment(JTextField.CENTER);
        txtRdrStr.setPreferredSize(new Dimension(25, 50));
        panRdrStr.add(txtRdrStr, BorderLayout.WEST);

        MyLabel lblRderStr = new MyLabel(1, "(16 Chr)");
        panRdrStr.add(lblRderStr, BorderLayout.CENTER);
        txtMnu31.add(panRdrStr);

        MyPanel panlblRecS2 = new MyPanel(new BorderLayout());
        chkRecS2 = new JCheckBox();

        panlblRecS2.add(chkRecS2, BorderLayout.WEST);
        txtRecOnS2 = new MyTextField(15);
        panlblRecS2.add(txtRecOnS2, BorderLayout.CENTER);

        txtMnu31.add(panlblRecS2);

        panlbltxtMnu31.add(txtMnu31, BorderLayout.CENTER);
        panMnu3Center.add(panlbltxtMnu31);

        MyPanel panMnu32 = new MyPanel(new BorderLayout());

        MyPanel panlblMnu32 = new MyPanel(new GridLayout(3, 1));

        MyLabel lblTotlRec = new MyLabel(1, "Total Record");
        panlblMnu32.add(lblTotlRec);

        MyPanel panlblRecS = new MyPanel(new BorderLayout());

        MyLabel lblRecS = new MyLabel(1, "Record on Server 1");
        panlblRecS.add(lblRecS, BorderLayout.CENTER);

        chkRecS1 = new JCheckBox();
        panlblRecS.add(chkRecS1, BorderLayout.EAST);

        panlblMnu32.add(panlblRecS);

        MyLabel lblRdrDt = new MyLabel(1, "Rdr Date Time");
        panlblMnu32.add(lblRdrDt);

        panMnu32.add(panlblMnu32, BorderLayout.WEST);


        MyPanel pantxtMnu32 = new MyPanel(new GridLayout(3, 1));

        txtTotlRec = new MyTextField(15);
        pantxtMnu32.add(txtTotlRec);
        txtTotlRec.setEditable(false);
        txtRecOnS1 = new MyTextField(15);
        pantxtMnu32.add(txtRecOnS1);

        lblDate = new MyLabel();
        lblDate.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pantxtMnu32.add(lblDate);
        panMnu32.add(pantxtMnu32, BorderLayout.CENTER);
        panMnu3Center.add(panMnu32);

        MyPanel panSetMnu3 = new MyPanel(new BorderLayout());
        MyLabel lblNote = new MyLabel(2, "(To enable SET buttons,GET successful reader settings)");

        panSetMnu3.add(lblNote, BorderLayout.CENTER);

        MyPanel panSetTime = new MyPanel(new FlowLayout(FlowLayout.RIGHT));

        chkDate = new JCheckBox("Set Date Time");
        panSetTime.add(chkDate);

        btnSetMnu3 = new MyButton("Set", 0) {

            @Override
            public void onClick() {
                SetMenu3(false);
                UpdateConfig();
            }
        };
        btnSetMnu3.setEnabled(false);
        panSetTime.add(btnSetMnu3);
        panSetMnu3.add(panSetTime, BorderLayout.EAST);


        panMnu3.add(panMnu3Center, BorderLayout.CENTER);
        panMnu3.add(panSetMnu3, BorderLayout.SOUTH);
        panMnu3NBtn.add(panMnu3, BorderLayout.CENTER);

        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        btnGet = new MyButton("Get", 0) {

            @Override
            public void onClick() {
                GetSettingsFromReader();
            }
        };
        panButtons.add(btnGet);

        btnDefault = new MyButton("Default", 0) {

            @Override
            public void onClick() {
                SetDefault();
                UpdateConfig();
            }
        };
        btnDefault.setEnabled(false);
        panButtons.add(btnDefault);

        btnSetAll = new MyButton("Set All", 0) {

            @Override
            public void onClick() {
                SetAll();
            }
        };
        btnSetAll.setEnabled(false);
        panButtons.add(btnSetAll);

        btnExit = new MyButton("Exit", 0) {

            @Override
            public void onClick() {
                Exit();
            }
        };
        panButtons.add(btnExit);
        panMnu3NBtn.add(panButtons, BorderLayout.SOUTH);


        panMainCenter.add(panMnu3NBtn, BorderLayout.SOUTH);

        this.setLayout(new BorderLayout());
        this.add(MainPanel, BorderLayout.CENTER);
        setVisible(true);
        FillForm();
//        addListeners();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "ReaderNo":
                setEntries();
                break;
            case "APB":
                setAPB();
                break;
            case "TxtAPB":
                setCmbAPB();
                break;
            case "ChkRdrStr":
                p.ChkRdrStr = chkRdrStr.isSelected();
                break;
            case "ChkRecS1":
                p.ChkRecS1 = chkRecS1.isSelected();
                break;
            case "ChkRecS2":
                p.ChkRecS2 = chkRecS2.isSelected();
                break;
            case "ChkDate":
                p.ChkDate = chkDate.isSelected();
                break;
            default:
                MyUtils.showMessage("Unknown Action Command " + e.getActionCommand());
                break;
        }
    }

    private void setDefaultData() {
        txtSelfIP.setText("192.168.0.99");
        txtServerIP1.setText("192.168.0.10");
        txtServerIP2.setText("192.168.0.11");
        txtSubMask.setText("255.255.255.0");
        txtListenPort.setText("3000");
        txtServerPort.setText("3001");
        txtGateIP.setText("198.168.0.1");
        txtReaderNo.setText("1");
        txtCarRDelay.setText("2");
        txtLEDely.setText("2");
        txtDoorOpnAlrm.setText("10");
        txtKyTmOut.setText("0");
        txtPUTmOut.setText("10");
        txtPUCnt.setText("1");
        txtOnLRespTmOut.setText("10");
        txtAppID1.setText("18433");
        txtAppID2.setText("18434");
        txtAPBBlock.setText("0");
        //  txtAPBLevel.setText(p.APBLevel);
        setCmbAPB();
        txtUseRDB.setText("1");
        txtAutoOff.setText("250");
        txtUCode.setText("0");
        txtUnUsedEntry.setText("0");
        txtRdrStr.setText("Senergy...");
        txtBuild.setText("101");
        cmbBaudRate.setSelectedIndex(p.BaudRate);
        cmbReaderMode.setSelectedIndex(p.ReaderMode);
        cmbChkHolidy.setSelectedIndex(p.CheckShiftHoliday);
        cmbPinBio.setSelectedIndex(p.CheckPin);
        cmbValDOB.setSelectedIndex(p.CheckValidityDOB);
        cmbMutiRej.setSelectedIndex(p.MultiRejAlarm);

    }

    private void SaveIPSettingsToDB() {
        try {
            Packetizer p = new Packetizer();
            p.addString(txtLocation.getText());
            p.addString(txtDiv.getText());
            p.addString(txtSelfIP.getText());

            p.addString(txtSubMask.getText());
            p.addString(txtGateIP.getText());
            p.addString(txtServerIP1.getText());
            p.addString(txtServerIP2.getText());

            p.addString(txtListenPort.getText());
            p.addString(txtServerPort.getText());

            MyHTTP myHTTP = MyUtils.createServletConnection("RSFormServlet");
            myHTTP.openOS();
            myHTTP.println("SaveIPSettingsToDB");
            myHTTP.println(p.getPacket());
            myHTTP.println(cmbReaderNo.getSelectedItem().toString());
            myHTTP.closeOS();

            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
        } catch (Exception ex) {
            MyUtils.showException("SaveIPSettingsToDB :", ex);
        }
    }

    private void saveMenu1ToDB() {
        try {
            Packetizer p = new Packetizer();
            p.addInt(Integer.parseInt(txtReaderNo.getText()));
            p.addInt(cmbBaudRate.getSelectedIndex());
            p.addInt(cmbReaderMode.getSelectedIndex());
            p.addInt(Integer.parseInt(txtAPBLevel.getText()));
            p.addInt(Integer.parseInt(txtUseRDB.getText()));
            p.addInt(cmbChkHolidy.getSelectedIndex());
            p.addInt(cmbPinBio.getSelectedIndex());
            p.addInt(cmbValDOB.getSelectedIndex());
            p.addInt(Integer.parseInt(txtAutoOff.getText()));

            MyHTTP myHTTP = MyUtils.createServletConnection("RSFormServlet");
            myHTTP.openOS();
            myHTTP.println("SaveMenu1ToDB");
            myHTTP.println(p.getPacket());
            myHTTP.println(cmbReaderNo.getSelectedItem().toString());
            myHTTP.closeOS();

            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
        } catch (Exception ex) {
            MyUtils.showException("saveMenu1ToDB :", ex);
        }
    }

    private void saveMenu2ToDB() {
        try {
            Packetizer p = new Packetizer();
            p.addInt(Integer.parseInt(txtCarRDelay.getText()));
            p.addInt(Integer.parseInt(txtLEDely.getText()));
            p.addInt(Integer.parseInt(txtDoorOpnAlrm.getText()));
            p.addInt(Integer.parseInt(txtKyTmOut.getText()));
            p.addInt(Integer.parseInt(txtOnLRespTmOut.getText()));
            p.addInt(cmbMutiRej.getSelectedIndex());
            p.addInt(Integer.parseInt(txtAppID1.getText()));
            p.addInt(Integer.parseInt(txtAppID2.getText()));

            MyHTTP myHTTP = MyUtils.createServletConnection("RSFormServlet");
            myHTTP.openOS();
            myHTTP.println("SaveMenu2ToDB");
            myHTTP.println(p.getPacket());
            myHTTP.println(cmbReaderNo.getSelectedItem().toString());
            myHTTP.closeOS();

            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
        } catch (Exception ex) {
            MyUtils.showException("saveMenu2ToDB :", ex);
        }
    }

    private void saveMenu3ToDB() {
        try {
            Packetizer p = new Packetizer();
            p.addString(txtRdrStr.getText());
            MyHTTP myHTTP = MyUtils.createServletConnection("RSFormServlet");
            myHTTP.openOS();
            myHTTP.println("SaveMenu3ToDB");
            myHTTP.println(p.getPacket());
            myHTTP.println(cmbReaderNo.getSelectedItem().toString());
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
        } catch (Exception ex) {
            MyUtils.showException("saveMenu3ToDB :", ex);
        }
    }
}
