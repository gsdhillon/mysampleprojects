package cpnds.gui.ADCAcquisition;

import cpnds.CommPort.MyCommPort;
import cpnds.data.LogReports;
import gui.MyButton;
import gui.MyLabel;
import gui.MyPanel;
import gui.MyTextField;
import gui.MyWaitDialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import cpnds.data.MyData;
import gui.MyComboBox;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JLabel;
/**
 * @type     : Java Class
 * @name     : NewADCAcquisitionPanel
 * @file     : NewADCAcquisitionPanel.java
 * @created  : May 15, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class NewADCAcquisitionPanel extends MyPanel implements ActionListener{
    private MyComboBox comboFsSnapshotTime;
    private MyComboBox comboSamplingAvg;
    private MyTextField textPulseHeight;
    private MyTextField textPulseWidth;
    private MyTextField textInputPower;
    private MyTextField textCountRate;
    private MyTextField textRiseTime;
    private MyTextField textFallTime;
    private MyButton startButton;
    private ADCAcquisitionOptionsTab parent;
    /**
     * Constructor
     */
    public NewADCAcquisitionPanel(ADCAcquisitionOptionsTab parent){
        super(new FlowLayout(FlowLayout.CENTER, 800, 50));
        setDefaultBG();
        
        this.parent = parent;

        int rowHeight = 35;
        int width = 650;

        String s = "NEW ADC ACQUISITION AND STORAGE OF THE DATA";
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL_HEADING, s, JLabel.CENTER);
        l.setPreferredSize(new Dimension(width, rowHeight));
        add(l);

        int verticalGap = 20;
        int numRows = 0;

        MyPanel inputPanel = new MyPanel(new BorderLayout(0, verticalGap), "");
        MyPanel p = new MyPanel(new BorderLayout(2, 0), null);
        MyPanel left = new MyPanel(new GridLayout(0, 1, 0, verticalGap), null);
        MyPanel right = new MyPanel(new GridLayout(0, 1, 0, verticalGap), null);

        //Process ID
        l = new MyLabel(MyLabel.TYPE_LABEL, "ProcessID:");
        left.add(l);
        l = new MyLabel(MyLabel.TYPE_DATA, adcAcqParams.processID+"", JLabel.LEFT);
        right.add(l);
        numRows++;

        //optionFsSnapshotTime
        l = new MyLabel(MyLabel.TYPE_LABEL, "Choose Fs and TSnap:");
        comboFsSnapshotTime = new MyComboBox(adcAcqParams.fsTable);
        comboFsSnapshotTime.setActionCommand("ComboChanged");
        comboFsSnapshotTime.addActionListener(this);

        left.add(l);
        right.add(comboFsSnapshotTime);
        numRows++;

        //textPulseHeight
        l = new MyLabel(MyLabel.TYPE_LABEL, "Pulse Height(0-5V):");
        textPulseHeight = new MyTextField();
        left.add(l);
        right.add(textPulseHeight);
        numRows++;

        //textSamplingAvg
        l = new MyLabel(MyLabel.TYPE_LABEL, "Sampling Average:");

        String[] samplingAvgList = new String[8];
        samplingAvgList[0] = "No sampling avg - 0";
        int towPower_i = 2;
        for(int i=1;i<=7;i++){
            samplingAvgList[i] = "2^"+i+" - "+towPower_i;
            towPower_i *= 2;
        }
        comboSamplingAvg = new MyComboBox(samplingAvgList);
        left.add(l);
        right.add(comboSamplingAvg);
        numRows++;

        //textPulseWidth
        l = new MyLabel(MyLabel.TYPE_LABEL, "Pulse Width(1-255 NanoSec):");
        textPulseWidth = new MyTextField();
        left.add(l);
        right.add(textPulseWidth);
        numRows++;

        //textInputPower
        l = new MyLabel(MyLabel.TYPE_LABEL, "Input Power(Flux 0.0-10^12 nv):");
        textInputPower = new MyTextField();
        left.add(l);
        right.add(textInputPower);
        numRows++;

        //textCountRate
        l = new MyLabel(MyLabel.TYPE_LABEL, "Input Count Rate(0-10MHz Int):");
        textCountRate = new MyTextField();
        left.add(l);
        right.add(textCountRate);
        numRows++;

        //rise
        l = new MyLabel(MyLabel.TYPE_LABEL, "Rise Time(1-255 NanoSec):");
        textRiseTime = new MyTextField();
        left.add(l);
        right.add(textRiseTime);
        numRows++;

        //fall
        l = new MyLabel(MyLabel.TYPE_LABEL, "Fall Time(1-255 NanoSec):");
        textFallTime = new MyTextField();
        left.add(l);
        right.add(textFallTime);
        numRows++;

        p.add(left,BorderLayout.WEST);
        p.add(right,BorderLayout.CENTER);

        inputPanel.add(p, BorderLayout.CENTER);
        //Buttons
        MyPanel bp = MyButton.getButtonPanel();
        
        MyButton b = new MyButton(this, "Back", "Back", "Back");
        bp.add(b);

        startButton = new MyButton(this, "Start", "Start", "Send");
        startButton.setEnabled(false);
        bp.add(startButton);

        inputPanel.add(bp, BorderLayout.SOUTH);
        numRows++;
        
        int height = inputPanel.getPreferredSize().height;//inputPanel.getPreferredSize().height;
        inputPanel.setPreferredSize(new Dimension(width, height));
        add(inputPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("ComboChanged")){
            if(comboFsSnapshotTime.getSelectedIndex()!=0){
                startButton.setEnabled(true);
            }else{
                startButton.setEnabled(false);
            }
        }else if(e.getActionCommand().equals("Start")){
            final MyWaitDialog waitDialog = new MyWaitDialog(MyData.mainFrame, 400, 100);
            new Thread(){
                @Override
                public void run(){
                    startAcquisition(waitDialog);
                    waitDialog.setVisible(false);
                }
            }.start();
            waitDialog.setVisible(true);
        }else if(e.getActionCommand().equals("Back")){
            parent.showOptionPane();
        }
    }

    /**
     * 
     */
    private void startAcquisition(MyWaitDialog waitDialog) {
         try{
            //Fs and Snapshot time table index
            adcAcqParams.fsTableIndex = (byte)(comboFsSnapshotTime.getSelectedIndex());
            
            //pulse height
            adcAcqParams.pulseHeight = 0.0f;
            try{
                String s = textPulseHeight.getText();
                adcAcqParams.pulseHeight = Float.parseFloat(s);
                int decIndex = s.indexOf('.');
                if(decIndex != -1 && decIndex < (s.length()-6)){
                    throw new Exception("InvalidPulseHeight - more than 5 decimal point");
                }
            }catch(Exception e){
                throw new Exception("InvalidPulseHeight - "+e.getMessage());
            }
            if(adcAcqParams.pulseHeight<0 || adcAcqParams.pulseHeight>5){
                throw new Exception("InvalidPulseHeight - should be between 0.0-5.0");
            }
            
            //sampling average
            adcAcqParams.samplingAvg = 0;
            try{
                int i = comboSamplingAvg.getSelectedIndex();
                if(i>0){
                    adcAcqParams.samplingAvg = (int)Math.pow(2, i);
                }
            }catch(Exception e){
                throw new Exception("InvalidSamplingAvg - "+e.getMessage());
            }

            //pulse width
            adcAcqParams.pulseWidth = 0;
            try{
                String s = textPulseWidth.getText();
                adcAcqParams.pulseWidth = Integer.parseInt(s);
            }catch(Exception e){
                throw new Exception("InvalidPulseWidth - "+e.getMessage());
            }
            if(adcAcqParams.pulseWidth<1 || adcAcqParams.pulseWidth>255){
                throw new Exception("InvalidPulseWidth - should be between 1-255");
            }

            //input power
            adcAcqParams.inputPower = 0.0f;
            try{
                adcAcqParams.inputPower = Float.parseFloat(textInputPower.getText());
                if(adcAcqParams.inputPower > Math.pow(10, 12)){
                    throw new Exception("InvalidInputPower > 10^12");
                }
            }catch(Exception e){
                throw new Exception("InvalidInputPower - "+e.getMessage());
            }

            //count Rate per sec
            adcAcqParams.inputCountRate = 0;
            try{
                adcAcqParams.inputCountRate = Integer.parseInt(textCountRate.getText());
                if(adcAcqParams.inputCountRate > Math.pow(10, 7)){
                    throw new Exception("InvalidInputCountRate > 10MHz");
                }
            }catch(Exception e){
                throw new Exception("InvalidInputCountRate - "+e.getMessage());
            }

            //rise time
            adcAcqParams.riseTime = 0;
            try{
                String s = textRiseTime.getText();
                adcAcqParams.riseTime = Integer.parseInt(s);
            }catch(Exception e){
                throw new Exception("InvalidRiseTime - "+e.getMessage());
            }
            if(adcAcqParams.riseTime<1 || adcAcqParams.riseTime>255){
                throw new Exception("InvalidRiseTime - should be between 1-255");
            }

            //fall time
            adcAcqParams.fallTime = 0;
            try{
                String s = textFallTime.getText();
                adcAcqParams.fallTime = Integer.parseInt(s);
            }catch(Exception e){
                throw new Exception("InvalidRiseTime - "+e.getMessage());
            }
            if(adcAcqParams.fallTime<1 || adcAcqParams.fallTime>255){
                throw new Exception("InvalidFallTime - should be between 1-255");
            }

            //make frame
            byte[] frame = new byte[18];
            frame[0] = 0x4F;//O
            frame[1] = 0x4B;//K
            frame[2] = adcAcqParams.processID;
            frame[3] = adcAcqParams.fsTableIndex;
            MyData.putFloatBytesToBuffer(adcAcqParams.pulseHeight, frame, 4);
            frame[8] = (byte)adcAcqParams.samplingAvg;
            frame[9] = (byte)adcAcqParams.pulseWidth;
            for(int i=10;i<=13;i++){
                frame[i] = 0x00;//padding
            }
            frame[14] = 0x00;//status byte 1
            frame[15] = 0x00;//status byte 2
            frame[16] = 0x4F;//O
            frame[17] = 0x4B;//K
            
            //set status bytes
            MyData.setStatusBytes(frame);

  ///          if(!MyData.showConfirm(MyData.hexify(frame))){
   //             return;
   //         }

            //TODO new code for UART_TEST is here following line only
            waitDialog.status.setText("Checking status of the device..");
            MyData.myCommPort.testBBStatus(false);

            //create directory sturcture for current date and time
            ADCAcquisitionData.createDirectories();

            //send data
            MyData.myCommPort.writeFrame(frame);

            //waitForTime for data
            waitDialog.status.setText("Waiting for ADC Acquisition to complete");
            MyData.myCommPort.waitForData();

            //read first set of data
            waitDialog.status.setText("Result is available , Getting it !!");
            File file = ADCAcquisitionData.getRecentDataFile();
            adcAcqParams.numOfPoints = MyData.myCommPort.readDataFrame(file, MyCommPort.RESULT_TYPE_FLOATS);
            //writing parameters in the param file
            file = ADCAcquisitionData.getRecentParamFile();
            adcAcqParams.uid = MyData.uid;
            adcAcqParams.write(file);
            //ADCAcquisition done set the flags
            ADCAcquisitionData.newDataAvailable = true;
            ADCAcquisitionData.numOfPoints = adcAcqParams.numOfPoints;
            ADCAcquisitionData.uid = adcAcqParams.uid;
            waitDialog.setVisible(false);
            MyData.showInfoMessage("New ADC Acquisition completed successfully."
                    + "\nPlease note the following -"
                    + "\nNumber of sample points = "+adcAcqParams.numOfPoints
                    + "\nExperiment UID = "+adcAcqParams.uid);
            parent.showAnalysisPanel();
        }catch(Exception e){
            waitDialog.setVisible(false);
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }
    //adcAcqParams
    ADCAcqParams adcAcqParams = new ADCAcqParams();
}