/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.ReaderMaster;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author Pradnya
 */
public class ReaderMaster extends MyPanel implements ActionListener {

    public MyButton btnSubmit, btnCancel;
    public MyTextField txtReaderNo, txtDoor, txtDiv, txtSelfIP, txtServerIP, txtServerPort, txtListenPort, txtSubMask, txtGateIP;
    public JComboBox cmbReaderModel, cmbReaderZone;
    ReaderMasterApplet ReaderApplet;
    String readerNo;
    String Job;

    public ReaderMaster(ReaderMasterApplet ReaderApplet, String Job, String readerNo) {
        try {
            this.ReaderApplet = ReaderApplet;
            this.Job = Job;
            this.readerNo = readerNo;

            AddReaderMaster();
            fillDefualtDetails();
            if (Job.equals("update")) {
                fillForm();
            }
        } catch (Exception ex) {
            MyUtils.showException("Reader Master", ex);
        }


    }

    private void fillForm() throws Exception {

        txtReaderNo.setEditable(false);

        MyHTTP myHTTP = MyUtils.createServletConnection("ReaderMasterFormServlet");
        myHTTP.openOS();
        myHTTP.println("ReaderDetails");
        myHTTP.println(readerNo);
        myHTTP.closeOS();
        myHTTP.openIS();
        String result = myHTTP.readLine();
        myHTTP.closeIS();
        if (result.startsWith("ERROR")) {
            MyUtils.showException("Database Query", new Exception(result));
        } else {
            Depacketizer dp = new Depacketizer(result);
            txtReaderNo.setText(readerNo);
            txtDoor.setText(dp.getString());
            txtDiv.setText(dp.getString());
            cmbReaderModel.setSelectedIndex(dp.getInt());
            cmbReaderZone.setSelectedIndex(dp.getInt());
            txtSelfIP.setText(dp.getString());
            txtServerIP.setText(dp.getString());
            txtSubMask.setText(dp.getString());
            txtGateIP.setText(dp.getString());
            txtServerPort.setText(dp.getString());
            txtListenPort.setText(dp.getString());

        }
    }

    private void fillDefualtDetails() {
        txtSelfIP.setText("192.168.0.0");
        txtServerIP.setText("192.168.0.0");
        txtServerPort.setText("3001");
        txtGateIP.setText("192.168.0.0");
        txtSubMask.setText("255.255.255.0");
        txtListenPort.setText("3000");
    }

    private boolean FormFilled() {
        if ("".equals(txtReaderNo.getText())) {
            MyUtils.showMessage("Enter Reader No");
            return false;
        } else if ("".equals(txtDoor.getText())) {
            MyUtils.showMessage("Enter Location");
            return false;
        } else if ("".equals(txtGateIP.getText())) {
            MyUtils.showMessage("Enter Gateway IP Address");
            return false;
        } else if ("".equals(txtSelfIP.getText())) {
            MyUtils.showMessage("Enter Self IP Address");
            return false;
        } else if ("".equals(txtServerIP.getText())) {
            MyUtils.showMessage("Enter Server IP Address");
            return false;
        } else if ("".equals(txtServerPort.getText())) {
            MyUtils.showMessage("Enter Server Port");
            return false;
        } else if ("".equals(txtListenPort.getText())) {
            MyUtils.showMessage("Enter ListenPort");
            return false;
        }
        return true;
    }

    private void addReader() {
        if (FormFilled()) {
            String Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("ReaderMasterFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("AddReader");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else {
                        ReaderApplet.showHomePanel();
                        //loadPage("services/category.jsp","_self");

                    }
                } catch (Exception ex) {
                    MyUtils.showException("AddCategory", ex);
                }
            }
        }
    }

    private void updateReader() {
        if (FormFilled()) {
            String Packet = CreatePacket();
            if (!Packet.equals("PacketFail")) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("ReaderMasterFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("UpdateReader");
                    myHTTP.println(Packet);
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    myHTTP.closeIS();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showException("Database ERROR", new Exception(result));
                    } else {
                        ReaderApplet.showHomePanel();
                    }
                } catch (Exception ex) {
                    MyUtils.showException("Updateserverlet", ex);

                }
            }
        }
    }

    private String CreatePacket() {
        try {
            Packetizer a = new Packetizer();
            a.addString(txtReaderNo.getText());
            a.addString(txtDoor.getText());
            a.addString(txtDiv.getText());
            a.addInt(cmbReaderModel.getSelectedIndex());
            a.addInt(cmbReaderZone.getSelectedIndex());
            a.addString(txtSelfIP.getText());
            a.addString(txtServerIP.getText());
            a.addString(txtSubMask.getText());
            a.addString(txtGateIP.getText());
            a.addString(txtServerPort.getText());
            a.addString(txtListenPort.getText());
            return a.getPacket();

        } catch (Exception ex) {
            MyUtils.showException("Create Packet", ex);
            return "PacketFail";
        }
    }

    public void AddReaderMaster() {
        MyPanel MainPanel = new MyPanel(new BorderLayout());

        MyPanel panReader = new MyPanel(new BorderLayout());
        MyPanel panReaderNorth = new MyPanel(new GridLayout(1, 2), "Reader Details");
        MyPanel panlbltxtReader = new MyPanel(new BorderLayout());
        MyPanel panlblReader = new MyPanel(new GridLayout(3, 1, 10, 5));

        MyLabel lblReaderNo = new MyLabel(1, "Reader No:");
        panlblReader.add(lblReaderNo);

        MyLabel lblDoor = new MyLabel(1, "Location:");
        panlblReader.add(lblDoor);

        MyLabel lblDiv = new MyLabel(1, "Division:");
        panlblReader.add(lblDiv);

        panlbltxtReader.add(panlblReader, BorderLayout.WEST);

        MyPanel pantxtReader = new MyPanel(new GridLayout(3, 1, 10, 5));
        MyPanel panReaderNo = new MyPanel(new BorderLayout(10, 0));

        txtReaderNo = new MyTextField(5);
        panReaderNo.add(txtReaderNo, BorderLayout.WEST);
        MyLabel lblReaderLim = new MyLabel(1, "(1-90)");
        panReaderNo.add(lblReaderLim, BorderLayout.CENTER);
        pantxtReader.add(panReaderNo);

        txtDoor = new MyTextField();
        pantxtReader.add(txtDoor);
        txtDiv = new MyTextField();
        pantxtReader.add(txtDiv);
        panlbltxtReader.add(pantxtReader, BorderLayout.CENTER);
        panReaderNorth.add(panlbltxtReader);

        MyPanel panModZone = new MyPanel(new BorderLayout());
        MyPanel panlblModZone = new MyPanel(new GridLayout(3, 1, 10, 5));

        MyLabel lblReaderMod = new MyLabel(1, "Reader Model:");
        panlblModZone.add(lblReaderMod);

        MyLabel lblReaderZone = new MyLabel(1, "Reader Zone:");
        panlblModZone.add(lblReaderZone);

        MyLabel lblfill1 = new MyLabel();
        panlblModZone.add(lblfill1);

        panModZone.add(panlblModZone, BorderLayout.WEST);

        MyPanel pancmbModZone = new MyPanel(new GridLayout(3, 1, 10, 5));
        String ReaderModel[] = {"SmartGuard", "MiniGuard"};
        cmbReaderModel = new JComboBox(ReaderModel);
        pancmbModZone.add(cmbReaderModel);

        String ReaderZone[] = {"0", "ZONE-1", "ZONE-2", "ZONE-3", "ZONE-4", "ZONE-5", "ZONE-6"};
        cmbReaderZone = new JComboBox(ReaderZone);
        pancmbModZone.add(cmbReaderZone);

        MyLabel lblfill2 = new MyLabel();
        pancmbModZone.add(lblfill2);

        panModZone.add(pancmbModZone, BorderLayout.CENTER);

        panReaderNorth.add(panModZone);

        panReader.add(panReaderNorth, BorderLayout.NORTH);

        MyPanel panIPSettings = new MyPanel(new GridLayout(1, 3, 30, 5), "IP Settings");
        MyPanel panIP = new MyPanel(new BorderLayout());
        MyPanel panlblIPAdd = new MyPanel(new GridLayout(2, 1));

        MyLabel lblSelfIP = new MyLabel(1, "Self IP:");
        panlblIPAdd.add(lblSelfIP);
        MyLabel lblServerIP = new MyLabel(1, "Server IP:");
        panlblIPAdd.add(lblServerIP);
        panIP.add(panlblIPAdd, BorderLayout.WEST);

        MyPanel pantxtIPAdd = new MyPanel(new GridLayout(2, 1));
        txtSelfIP = new MyTextField();
        pantxtIPAdd.add(txtSelfIP);

        txtServerIP = new MyTextField();
        pantxtIPAdd.add(txtServerIP);
        panIP.add(pantxtIPAdd, BorderLayout.CENTER);

        panIPSettings.add(panIP);

        MyPanel panSubGate = new MyPanel(new BorderLayout());
        MyPanel panlblSubGate = new MyPanel(new GridLayout(2, 1));
        MyLabel lblSubMask = new MyLabel(1, "SubNet Mask:");
        panlblSubGate.add(lblSubMask);

        MyLabel lblGetIP = new MyLabel(1, "Gateway IP:");
        panlblSubGate.add(lblGetIP);
        panSubGate.add(panlblSubGate, BorderLayout.WEST);

        MyPanel pantxtSubGate = new MyPanel(new GridLayout(2, 1));
        txtSubMask = new MyTextField();
        pantxtSubGate.add(txtSubMask);

        txtGateIP = new MyTextField();
        pantxtSubGate.add(txtGateIP);
        panSubGate.add(pantxtSubGate, BorderLayout.CENTER);
        panIPSettings.add(panSubGate);

        MyPanel panPort = new MyPanel(new BorderLayout(30, 5));
        MyPanel panlblSPort = new MyPanel(new GridLayout(2, 1));
        MyLabel lblServerPort = new MyLabel(1, "Server Port:");
        panlblSPort.add(lblServerPort);

        MyLabel lblListenPort = new MyLabel(1, "Listen Port:");
        panlblSPort.add(lblListenPort);
        panPort.add(panlblSPort, BorderLayout.WEST);

        MyPanel pantxtSPort = new MyPanel(new GridLayout(2, 1));
        txtServerPort = new MyTextField("3001");
        pantxtSPort.add(txtServerPort);

        txtListenPort = new MyTextField("3000");
        pantxtSPort.add(txtListenPort);
        panPort.add(pantxtSPort, BorderLayout.CENTER);
        panIPSettings.add(panPort);
        panReader.add(panIPSettings, BorderLayout.CENTER);

        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Add Reader");
        if (Job.equals("add")) {
            btnSubmit = new MyButton("Add Reader", 2, Color.WHITE) {

                @Override
                public void onClick() {
                    addReader();
                }
            };
        } else if (Job.equals("update")) {
            btnSubmit = new MyButton("Update Reader", 2, Color.WHITE) {

                @Override
                public void onClick() {
                    updateReader();
                }
            };
        }
        btnSubmit.setToolTipText("Add New Reader");
        panButtons.add(btnSubmit);

        btnCancel = new MyButton("Cancel", 2, Color.WHITE) {

            @Override
            public void onClick() {
                ReaderApplet.showHomePanel();
            }
        };
        btnCancel.setToolTipText("Cancel Process");
        panButtons.add(btnCancel);
        panReader.add(panButtons, BorderLayout.SOUTH);
        MainPanel.add(panReader, BorderLayout.NORTH);
        this.setLayout(new BorderLayout());
        this.add(MainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
