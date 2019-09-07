package cpnds.gui.CrossCorrelation;

import cpnds.CommPort.MyCommPort;
import cpnds.MyGraphs.DataPoint;
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
import gui.MyTextField;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JLabel;

/**
 * @type     : Java Class
 * @name     : NewCrossCorrelationPanel
 * @file     : NewCrossCorrelationPanel.java
 * @created  : Feb 6, 2011 1:28:34 AM
 * @version  : 1.2
 */
public class NewCrossCorrelationPanel extends MyPanel implements ActionListener{
    private CrossCorrelationOptionsTab parent;
    private String title = "NEW CROSS CORRELATION COMPUTATION";
    private MyComboBox crossCorrelationPointsCombo;
    private MyComboBox decadeCorrelationOption;
    private MyTextField startFreqText = new MyTextField("0.0");
    private MyTextField endFreqText = new MyTextField("0.0");
    private MyButton startButton;
    /**
     * Constructor
     */
    public NewCrossCorrelationPanel(CrossCorrelationOptionsTab parent){
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
        l = new MyLabel(MyLabel.TYPE_DATA, crossCorrParams.processID+"", JLabel.LEFT);
        right.add(l);
        numRows++;

        //check 2^n <= ADCAcqPoints NoOfPoints
        int n=0;
        for(;n<13;n++){
            if(Math.pow(2, (n+8))>ADCAcquisitionData.numOfPoints){
                break;
            }
        }
        //crossCorrelationPointsCombo
        l = new MyLabel(MyLabel.TYPE_LABEL, "No of points:");
        String[] noOfPointsText = new String[n];
        int points = 256;
        for(int i=0;i<n;i++){
            noOfPointsText[i] = "2^"+(8+i)+" - "+points;
            points *= 2;
        }
        crossCorrelationPointsCombo = new MyComboBox(noOfPointsText);
        left.add(l);
        right.add(crossCorrelationPointsCombo);
        numRows++;
        //Decade Cross Correlation Option
        l = new MyLabel(MyLabel.TYPE_LABEL, "Whether Decade Cross Correlation:");
        decadeCorrelationOption = new MyComboBox(new String[]{
            "NO",//index 0
            "YES"//index 1
        });
        decadeCorrelationOption.setSelectedIndex(1);
        decadeCorrelationOption.setActionCommand("DecadeOptionChanged");
        decadeCorrelationOption.addActionListener(this);
        left.add(l);
        right.add(decadeCorrelationOption);
        numRows++;

        //StartFrequency 
        l = new MyLabel(MyLabel.TYPE_LABEL, "Start Frequency[Hz]:");
        left.add(l);
        startFreqText.setEnabled(false);
        right.add(startFreqText);
        numRows++;

        //EndFrequency
        l = new MyLabel(MyLabel.TYPE_LABEL, "End Frequency[Hz]:");
        left.add(l);
        endFreqText.setEnabled(false);
        right.add(endFreqText);
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
        inputPanel.setPreferredSize(new Dimension(width,height));
        add(inputPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("DecadeOptionChanged")){
            if("NO".equals((String)decadeCorrelationOption.getSelectedItem())){
                startFreqText.setEnabled(true);
                endFreqText.setEnabled(true);
            }else{
                startFreqText.setEnabled(false);
                endFreqText.setEnabled(false);
            }
        }else if(e.getActionCommand().equals("Start")){
            final MyWaitDialog waitDialog = new MyWaitDialog(MyData.mainFrame, 400, 100);
            new Thread(){
                @Override
                public void run(){
                    startCrossCorrelationProcess(waitDialog);
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
    private void startCrossCorrelationProcess(MyWaitDialog waitDialog) {
        try{    
            crossCorrParams.noOfPoints = (byte)(crossCorrelationPointsCombo.getSelectedIndex()+8);

            if(Math.pow(2, crossCorrParams.noOfPoints) > ADCAcquisitionData.numOfPoints){
                throw new Exception("Please choose no. of points <= "+ADCAcquisitionData.numOfPoints+
                        "\nADCAcquisition is done on "+ADCAcquisitionData.numOfPoints+" points only.");
            }

            crossCorrParams.startFreq = 0.0f;
            crossCorrParams.endFreq = 0.0f;
            crossCorrParams.decadeOption = (byte)(decadeCorrelationOption.getSelectedIndex());
            if(crossCorrParams.decadeOption==CrossCorrParams.DECADE_NO){//NO
                try{
                    crossCorrParams.startFreq = Float.parseFloat(startFreqText.getText());
                }catch(Exception e){
                    throw new Exception("InvalidStartFreq - "+e.getMessage());
                }
                if(crossCorrParams.startFreq<1||crossCorrParams.startFreq>10000000.0){
                    throw new Exception("InvalidStartFreq Not 1-10000000");
                }
                try{
                    crossCorrParams.endFreq = Float.parseFloat(endFreqText.getText());
                }catch(Exception e){
                    throw new Exception("InvalidEndFreq - "+e.getMessage());
                }
                if(crossCorrParams.endFreq<1||crossCorrParams.endFreq>10000000.0){
                    throw new Exception("InvalidEndFreq Not 1-10000000");
                }
                if(crossCorrParams.endFreq<crossCorrParams.startFreq){
                    throw new Exception("EndFreq < StartFreq");
                }
            } 

            //number of results expected
            int numResults = 0;
            if(crossCorrParams.decadeOption==CrossCorrParams.DECADE_NO){
                numResults = 10;
            }else if(crossCorrParams.decadeOption==CrossCorrParams.DECADE_YES){
                numResults = 8;
            }else{
                throw new Exception("InvalidDecadeOption - "+crossCorrParams.decadeOption);
            }
            
            //make frame
            byte[] frame = new byte[18];
            frame[0] = 0x4F;//O
            frame[1] = 0x4B;//K
            frame[2] = crossCorrParams.processID;
            frame[3] = crossCorrParams.noOfPoints;
            frame[4] = crossCorrParams.decadeOption;
            MyData.putFloatBytesToBuffer(crossCorrParams.startFreq, frame, 5);
            MyData.putFloatBytesToBuffer(crossCorrParams.endFreq, frame, 9);
            frame[13] = 0x00;//padding
            frame[14] = 0x00;//status byte 1
            frame[15] = 0x00;//status byte 2
            frame[16] = 0x4F;//O
            frame[17] = 0x4B;//K
            
            //set status bytes
            MyData.setStatusBytes(frame);

            //make  ReplyFrame
            //******* Reply frame size is also changed to 18
            //******* So that at BB side only one read method can be used
            byte[] replyFrame = new byte[18];
            replyFrame[0] = 0x4F;//O
            replyFrame[1] = 0x4B;//K
            for(int i=2;i<=15;i++){
                replyFrame[i] = 0x00;
            }
            replyFrame[16] = 0x4F;//O
            replyFrame[17] = 0x4B;//K

         //   if(!MyData.showConfirm(MyData.hexify(frame))){
          //      return;
         //   }

            waitDialog.status.setText("Checking status of the device..");
            MyData.myCommPort.testBBStatus(true);

            //send data
            MyData.myCommPort.writeFrame(frame);

            //waitForTime for data
            waitDialog.status.setText("Waiting for cross correlation");
            MyData.myCommPort.waitForData();

            //generate operation directory
            CrossCorrData.createDirectories();

            //read first set of data
            waitDialog.status.setText("Result 1 is available , Getting it !!");
            File dataFile = CrossCorrData.getRecentDataFile(1);
            //false means data is float array not FFT (Comlex Number)
            MyData.myCommPort.readDataFrame(dataFile, MyCommPort.RESULT_TYPE_FLOATS);

            //read data back from the data file
            DataPoint[] dataPoints = MyData.readDataPoints(dataFile);//newG

            //writing parameters in the param file
            File commonParamfile = CrossCorrData.getRecentCommonParamsFile();
            crossCorrCommonParams.uid = MyData.uid;//newG
            //update common params
            crossCorrCommonParams.updateCommonMaxVal(1, dataPoints);//newG

            File paramfile = CrossCorrData.getRecentParamFile(1);
            crossCorrParams.uid = MyData.uid;
            crossCorrParams.resultNo = 1;
            crossCorrParams.calculateMinMaxAvg(dataPoints);//newG
            crossCorrParams.calculateCrossCorrFreq();//newG
            crossCorrParams.write(paramfile);//save params 1
            
            //read 7 or 9 more results
            for(int i=2;i<=numResults;i++){
                //send reply packet
                MyData.myCommPort.writeFrame(replyFrame);
                waitDialog.status.setText("Waiting for result "+i);
                MyData.myCommPort.waitForData();

                //get result no. i
                dataFile = CrossCorrData.getRecentDataFile(i);
                waitDialog.status.setText("Result "+i+" is available , Getting it !!");
                MyData.myCommPort.readDataFrame(dataFile, MyCommPort.RESULT_TYPE_FLOATS);

                //read data back from the data file
                dataPoints = MyData.readDataPoints(dataFile);//newG

                //update common params
                crossCorrCommonParams.updateCommonMaxVal(i, dataPoints);//newG

                //writing parameters in the param file
                paramfile = CrossCorrData.getRecentParamFile(i);
                crossCorrParams.resultNo = (byte)i;
                crossCorrParams.calculateMinMaxAvg(dataPoints);//newG
                crossCorrParams.calculateCrossCorrFreq();//newG
                crossCorrParams.write(paramfile);
            }
            //finally write CommonParamfile
            crossCorrCommonParams.write(commonParamfile);

            CrossCorrData.newDataAvailable = true;
            waitDialog.setVisible(false);
            MyData.showInfoMessage("New Cross Correlation completed successfully.");
            parent.showAnalysisPanel();
        }catch(Exception e){
            waitDialog.setVisible(false);
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }
    //CrossCorrParams
    CrossCorrParams crossCorrParams = new CrossCorrParams();
    CrossCorrCommonParams crossCorrCommonParams = new CrossCorrCommonParams();
}