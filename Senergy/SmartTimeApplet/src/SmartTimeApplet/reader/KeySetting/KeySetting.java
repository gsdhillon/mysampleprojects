/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.KeySetting;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author pradnya
 */
public class KeySetting extends MyPanel implements ActionListener {

    public JRadioButton rbAllReader, rbSelReader;
    public JTable tlbReader;
    KeySettingApplet keySettingApplet;
    String readerNo;
    String ipAdd;
    MyLabel lblLabel, lblstatus;
    MyPanel MainPanel;
    JPasswordField keyword;
    JComboBox cmbKeyAB, cmbKeyNum;
    String selReader;
    int countReader;

    public KeySetting(final KeySettingApplet keySettingApplet, String selReader, int countReader) {
        this.keySettingApplet = keySettingApplet;
        this.selReader = selReader;
        this.countReader = countReader;
        MainPanel = new MyPanel(new BorderLayout(10, 10), "Key Setting");

        MainPanel.setPreferredSize(new Dimension(300, 290));
        MyPanel panCenter = new MyPanel(new BorderLayout(2, 5));

        MyPanel panCenterCenter = new MyPanel(new BorderLayout());

        MyPanel panlblCenter = new MyPanel(new GridLayout(3, 1, 2, 10));

        MyLabel lblKeyAB = new MyLabel(1, "Key A / B:");
        panlblCenter.add(lblKeyAB);

        MyLabel lblKeyNum = new MyLabel(1, "Key Number:");
        panlblCenter.add(lblKeyNum);

        MyLabel lblKeyWord = new MyLabel(1, "Key Word:");
        panlblCenter.add(lblKeyWord);

        panCenterCenter.add(panlblCenter, BorderLayout.WEST);

        MyPanel pantxtCenter = new MyPanel(new GridLayout(3, 1, 2, 10));

        String key[] = {"A", "B"};
        cmbKeyAB = new JComboBox(key);
        pantxtCenter.add(cmbKeyAB);

        String keynum[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"};
        cmbKeyNum = new JComboBox(keynum);
        pantxtCenter.add(cmbKeyNum);

        keyword = new JPasswordField(12);
        keyword.setText("FFFFFFFFFFFF");
        pantxtCenter.add(keyword);

        panCenterCenter.add(pantxtCenter, BorderLayout.CENTER);

        panCenter.add(panCenterCenter, BorderLayout.CENTER);

        MyPanel panCenterSouth = new MyPanel(new BorderLayout(2, 5));

        lblstatus = new MyLabel(1, "");
        lblstatus.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblstatus.setPreferredSize(new Dimension(300, 80));
        panCenterSouth.add(lblstatus, BorderLayout.CENTER);

        lblLabel = new MyLabel(1, "");
        lblLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblLabel.setPreferredSize(new Dimension(300, 30));
        panCenterSouth.add(lblLabel, BorderLayout.SOUTH);
        panCenter.add(panCenterSouth, BorderLayout.SOUTH);

        MainPanel.add(panCenter, BorderLayout.CENTER);


        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER));

        MyButton btnStartUpld = new MyButton("Start Upload", 2) {

            @Override
            public void onClick() {
                setKey();
            }
        };
        panButtons.add(btnStartUpld);

        MyButton btnCancel = new MyButton("Cancel", 2) {

            @Override
            public void onClick() {
                keySettingApplet.showHomePanel();
            }
        };
        panButtons.add(btnCancel);
        MainPanel.add(panButtons, BorderLayout.SOUTH);

        visibleLabelFalse();

        this.setLayout(new FlowLayout());
        this.add(MainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void visibleLabelFalse() {
        lblstatus.setVisible(false);
        lblLabel.setVisible(false);
        MainPanel.setPreferredSize(new Dimension(300, 180));
    }

    private void visibleLabelTrue() {
        lblstatus.setVisible(true);
        lblLabel.setVisible(true);
        MainPanel.setPreferredSize(new Dimension(300, 290));
    }

    private boolean FormFilled() {
        if (keyword.getPassword().length == 0) {
            MyUtils.showMessage("Enter Key Word");
            return false;
        } else if (keyword.getPassword().length < 12 || keyword.getPassword().length > 12) {
            MyUtils.showMessage("Enter Key Word of 12 letters");
            return false;
        }
        return true;
    }

    private void setKey() {
        if (FormFilled()) {
            try {
                String packet = createPacket();
                MyHTTP myHTTP = MyUtils.createServletConnection("RSCommServlet");
                myHTTP.openOS();
                myHTTP.println("SetKey");
                myHTTP.println(packet);
                myHTTP.println("" + countReader);
                myHTTP.println(selReader);
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                myHTTP.closeIS();
                if (result.startsWith("ERROR:")) {
                    MyUtils.showMessage(result);
                } else {
                    Depacketizer dp = new Depacketizer(result);
                    StringBuilder msg = new StringBuilder();
                    String[] readerResponses = new String[dp.getInt()];
                    for (int i = 0; i < readerResponses.length; i++) {
                        readerResponses[i] = dp.getString();
                        msg.append(readerResponses[i]).append("\n");
                    }
                    MyUtils.showMessage(msg.toString());//printing that reader have uploaded key successfully or not
                }
            } catch (Exception e) {
                MyUtils.showException("Set Key", e);
            }

        }
    }

    private String createPacket() {
        Packetizer p = new Packetizer();
        try {
            String strkeyword = "";
            String charkeyWord = new String(keyword.getPassword());
            for (int i = 0; i < charkeyWord.length() - 1; i += 2) {
                String output = charkeyWord.substring(i, (i + 2)); //grab the hex in pairs                
                int decimal = Integer.parseInt(output, 16);//convert hex to decimal                
                strkeyword = strkeyword + ((char) decimal);//convert the decimal to character
            }
            p.addInt(cmbKeyAB.getSelectedIndex());
            p.addInt(cmbKeyNum.getSelectedIndex());
            p.addString(strkeyword);

        } catch (Exception ex) {
            MyUtils.showException("CreateKeySettingPacket", ex);
        }
        return p.getPacket();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
