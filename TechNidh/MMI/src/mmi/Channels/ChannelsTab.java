package mmi.Channels;

import gui.MyButton;
import gui.MyComboBox;
import gui.MyLabel;
import gui.MyPanel;
import gui.MyWaitDialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import mmi.data.LogReports;
import mmi.data.MyData;

/**
 * @type     : Java Class
 * @name     : ChannelsTab
 * @file     : ChannelsTab.java
 * @created  : May 14, 2011 11:43:39 AM
 * @version  : 1.0.0
 */
public class ChannelsTab extends MyPanel implements ActionListener{
    private MyComboBox comboChannel;
    private String[] channels = {
                                    "Channel-1",
                                    "Channel-2",
                                    "Channel-3",
                                    "Channel-4",
                                    "Channel-5"
                                };
    private MyComboBox comboDelay;
    private String[] delayBetBytes = {
                                    " 0 ms",
                                    " 1 ms",
                                    " 2 ms",
                                    " 3 ms",
                                    " 4 ms",
                                    " 5 ms",
                                    " 6 ms",
                                    " 7 ms",
                                    " 8 ms",
                                    " 9 ms",
                                    "10 ms",
                                    "1 sec"
                                    };
    private int[] delays = {    0,
                                1,
                                2,
                                3,
                                4,
                                5,
                                6,
                                7,
                                8,
                                9,
                                10,
                                1000
                            };
    private MyPanel channelParamPanel;
    private ChannelData[] channelParams = new ChannelData[5];

    /**
     * Constructor
     */
    public ChannelsTab(){
        super(new FlowLayout(FlowLayout.CENTER, 800, 0));
        setDefaultBG();
        int width = 600;
        MyLabel l = new MyLabel();
        l.setPreferredSize(new Dimension(width, 30));
        add(l);
        l = new MyLabel(
                MyLabel.TYPE_LABEL_HEADING,
                "Man Machine Interface – Traingle Waveform Generator",
                JLabel.CENTER);
        l.setPreferredSize(new Dimension(width, 25));
        add(l);
        l = new MyLabel();
        l.setPreferredSize(new Dimension(width, 20));
        add(l);
        //
        MyPanel middlePanel = new MyPanel(new FlowLayout(FlowLayout.CENTER, 0, 0), "");
        //
        MyPanel p = new MyPanel(new FlowLayout(FlowLayout.CENTER, 0, 0), null);
        p.add(new MyLabel(MyLabel.TYPE_LABEL, "Select Channel: "));
        comboChannel = new MyComboBox(channels);
        comboChannel.setPreferredSize(new Dimension(150, 25));
        comboChannel.setActionCommand("ChannelChanged");
        comboChannel.addActionListener(this);
        p.add(comboChannel);
        p.setPreferredSize(new Dimension(width, 25));
        middlePanel.add(p);
        //
        for(int i=0;i<5;i++){
            channelParams[i] = new ChannelData(i+1);
        }
        channelParamPanel = new MyPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        channelParamPanel.setPreferredSize(new Dimension(width,
                                                MyData.CHANNEL_GUI_HEIGHT));
        middlePanel.add(channelParamPanel);

        //buttons panel
        MyPanel bp = new MyPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0), null);
        
        if(MyData.debug){
            bp.add(new MyLabel(MyLabel.TYPE_LABEL, "Delay between bytes: "), BorderLayout.WEST);
            comboDelay = new MyComboBox(delayBetBytes);
            comboDelay.setPreferredSize(new Dimension(150, 25));
            bp.add(comboDelay);
        }
        
        MyButton b = new MyButton(this, "Send", "Send", "Send");
        bp.add(b);
        bp.setPreferredSize(new Dimension(MyData.CHANNEL_GUI_WIDTH, 40));
        middlePanel.add(bp);

        //
        middlePanel.setPreferredSize(new Dimension(width+10, 
                MyData.CHANNEL_GUI_HEIGHT+100));
        add(middlePanel);
        //
        showChannelParams(0);
    }
    /**
     *
     */
    public final void showChannelParams(int channelNo){
        channelParamPanel.removeAll();
        channelParamPanel.add(channelParams[channelNo].getGUI());
        channelParamPanel.validate();
        channelParamPanel.repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("ChannelChanged")){
            showChannelParams(comboChannel.getSelectedIndex());
        }else if(e.getActionCommand().equals("Send")){
            final MyWaitDialog waitDialog = new MyWaitDialog(MyData.mainFrame, 400, 100);
            new Thread(){
                @Override
                public void run(){
                    sendData(waitDialog);
                    waitDialog.setVisible(false);
                }
            }.start();
            waitDialog.setVisible(true);
        }
    }

    private void sendData(MyWaitDialog waitDialog){
        try{
            //TODO Nidhi sending data bytes of all channels
            byte[] allData = new byte[50];
            for(int i=0;i<5;i++){
                byte[] data = channelParams[i].getDataBytes();
                System.arraycopy(data, 0, allData, 10*i, 10);
            }
            waitDialog.status.setText("Sending data ...");
            int delay = MyData.delayBetBytes;
            if(MyData.debug){
                delay = delays[comboDelay.getSelectedIndex()];
            }
            MyData.myCommPort.writeData(allData, delay);
            waitDialog.setVisible(false);
            MyData.showInfoMessage("Data sent successfully.");
        }catch(Exception ex){
            waitDialog.setVisible(false);
            LogReports.logError(ex);
            MyData.showErrorMessage(ex);
        }
    }
}