package cpnds.gui.AutoCorrelation;

import cpnds.CommPort.MyCommPort;
import cpnds.data.LogReports;
import gui.MyButton;
import gui.MyLabel;
import gui.MyPanel;
import gui.MyWaitDialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import cpnds.data.MyData;
import cpnds.gui.ADCAcquisition.ADCAcquisitionData;
import gui.MyComboBox;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JLabel;


/**
 * @type     : Java Class
 * @name     : NewAutoCorrelationPanel
 * @file     : NewAutoCorrelationPanel.java
 * @created  : Feb 6, 2011 1:28:34 AM
 * @version  : 1.2
 */
public class NewAutoCorrelationPanel extends MyPanel implements ActionListener{
    private AutoCorrelationOptionsTab parent;
    private String title = "NEW AUTO CORRELATION COMPUTATION";
    private MyComboBox comboNoOfPoints;
    private MyButton startButton;
    /**
     * Constructor
     */
    public NewAutoCorrelationPanel(AutoCorrelationOptionsTab parent){
        super(new FlowLayout(FlowLayout.CENTER, 800, 50));
        setDefaultBG();

        this.parent = parent;
        int rowHeight = 35;
        int width = 600;
        //add title
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL_HEADING, title, JLabel.CENTER);
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
        l = new MyLabel(MyLabel.TYPE_DATA, autoCorrParams.processID+"", JLabel.LEFT);
        right.add(l);
        numRows++;

        //check 2^n <= ADCAcqPoints NoOfPoints
        int n=0;
        for(;n<13;n++){
            if(Math.pow(2, (n+8))>ADCAcquisitionData.numOfPoints){
                break;
            }
        }
        //autoCorrelationPointsCombo
        l = new MyLabel(MyLabel.TYPE_LABEL, "No of points:");
        String[] noOfPoints = new String[n];
        int points = 256;
        for(int i=0;i<n;i++){
            noOfPoints[i] = "2^"+(8+i)+" - "+points;
            points *= 2;
        }
        comboNoOfPoints = new MyComboBox(noOfPoints);
        left.add(l);
        right.add(comboNoOfPoints);
        numRows++;
        
        p.add(left,BorderLayout.WEST);
        p.add(right,BorderLayout.CENTER);
        inputPanel.add(p, BorderLayout.CENTER);

        //buttons
        MyPanel bp = MyButton.getButtonPanel();

        MyButton b = new MyButton(this, "Back", "Back", "Back");
        bp.add(b);

        startButton = new MyButton(this, "Start", "Start", "Send");
        bp.add(startButton);

        inputPanel.add(bp, BorderLayout.SOUTH);
        numRows++;

        int height = inputPanel.getPreferredSize().height;
        inputPanel.setPreferredSize(new Dimension(width, height));
        add(inputPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Start")){
            final MyWaitDialog waitDialog = new MyWaitDialog(MyData.mainFrame, 400, 100);
            new Thread(){
                @Override
                public void run(){
                    startAutoCorrelationProcess(waitDialog);
                    waitDialog.setVisible(false);
                }
            }.start();
            waitDialog.setVisible(true);
        }if(e.getActionCommand().equals("Back")){
            parent.showOptionPane();
        }
    }
    /**
     *
     */
    private void startAutoCorrelationProcess(MyWaitDialog waitDialog) {
        try{
            autoCorrParams.noOfPoints = (byte)(comboNoOfPoints.getSelectedIndex()+8);

            if(Math.pow(2, autoCorrParams.noOfPoints) > ADCAcquisitionData.numOfPoints){
                throw new Exception("Please choose no. of points <= "+ADCAcquisitionData.numOfPoints+
                        "\nADCAcquisition is done on "+ADCAcquisitionData.numOfPoints+" points only.");
            }

            
            //make frame
            byte[] frame = new byte[18];
            frame[0] = 0x4F;//O
            frame[1] = 0x4B;//K
            frame[2] = autoCorrParams.processID;
            frame[3] = (byte)autoCorrParams.noOfPoints;
            for(int i=4;i<=13;i++){
                frame[i] = 0x00;//padding
            }
            frame[14] = 0x00;//status byte 1
            frame[15] = 0x00;//status byte 2
            frame[16] = 0x4F;//O
            frame[17] = 0x4B;//K

            //set status bytes
            MyData.setStatusBytes(frame);

         //   if(!MyData.showConfirm(MyData.hexify(frame))){
          //      return;
         //   }

            waitDialog.status.setText("Checking status of the device..");
            MyData.myCommPort.testBBStatus(true);

            //send data
            MyData.myCommPort.writeFrame(frame);

            //waitForTime for data
            waitDialog.status.setText("Waiting for auto correlation");
            MyData.myCommPort.waitForData();

            //generate operation directory
            AutoCorrData.createDirectories();

            //read first set of data
            waitDialog.status.setText("Result is available , Getting it !!");
            File file = AutoCorrData.getRecentDataFile();
            MyData.myCommPort.readDataFrame(file, MyCommPort.RESULT_TYPE_FLOATS);

            //writing parameters in the param file
            file = AutoCorrData.getRecentParamFile();
            autoCorrParams.uid = MyData.uid;
            autoCorrParams.write(file);

            //set data available flag
            AutoCorrData.newDataAvailable = true;
            waitDialog.setVisible(false);
            MyData.showInfoMessage("New auto correlation completed successfully.");
            parent.showAnalysisPanel();
        }catch(Exception e){
            waitDialog.setVisible(false);
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }
    //AutoCorrParams
    AutoCorrParams autoCorrParams = new AutoCorrParams();
}