package cpnds.gui.PrincipleCompAnalysis;

import cpnds.CommPort.MyCommPort;
import cpnds.data.LogReports;
import cpnds.data.MyData;
import cpnds.gui.ADCAcquisition.ADCAcquisitionData;
import gui.MyButton;
import gui.MyLabel;
import gui.MyPanel;
import gui.MyRadioButton;
import gui.MyTextField;
import gui.MyWaitDialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;


/**
 * @type     : Java Class
 * @name     : PCAnalysisOptionsTab
 * @file     : PCAnalysisOptionsTab.java
 * @created  : May 14, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class PCAnalysisOptionsTab extends MyPanel implements ActionListener{
    private MyTextField textStartingSampleNumber;
    private MyRadioButton rb1;
    private MyRadioButton rb2;
    private MyRadioButton rb3;
    private ButtonGroup bGroup;

    private String help =
        "1. To perform new PCA operation, enter 'Starting Sample No.', \n"
      + "    choose 'Start new PCA' option and press Start button.\n"
      + "2. To see the acquired PCA result, choose 'See acquired PCA result' option and press Start button.";
    /**
     * Constructor
     */
    public PCAnalysisOptionsTab(){
        super(new GridLayout(1, 1));
        showOptionPane();
    }

    /**
     *
     */
    public final void showOptionPane(){
        MyPanel op = new MyPanel(new FlowLayout(FlowLayout.CENTER, 800, 50));
        op.setDefaultBG();

        int rowHeight = 35;
        int width = 500;

        String s = "PRINCIPAL COMPONENT ANALYSIS";
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL_HEADING, s, JLabel.CENTER);
        l.setPreferredSize(new Dimension(width, rowHeight));
        op.add(l);

        int verticalGap = 20;
        int numRows = 0;

        MyPanel inputPanel = new MyPanel(new BorderLayout(0, verticalGap), "");
        MyPanel p = new MyPanel(new GridLayout(0, 1, 0, verticalGap), null);

/*
        //textStartingSampleNumber
        MyPanel p1 = new MyPanel(new BorderLayout());
        l = new MyLabel(MyLabel.TYPE_LABEL, "Starting Sample Number:");
        p1.add(l,BorderLayout.WEST);
        textStartingSampleNumber = new MyTextField();
        p1.add(textStartingSampleNumber,BorderLayout.CENTER);
        p.add(p1);
        numRows++;*/

        //options
        bGroup = new ButtonGroup();
        rb1 = new MyRadioButton("Start new PCA");
        bGroup.add(rb1);
        p.add(rb1);
        numRows++;

        rb2 = new MyRadioButton("See acquired PCA result");
        bGroup.add(rb2);
        p.add(rb2);
        numRows++;

        rb3 = new MyRadioButton("Analyze multiple previous results");
        bGroup.add(rb3);
        p.add(rb3);
        numRows++;

        inputPanel.add(p, BorderLayout.CENTER);

        //Buttons
        MyPanel bp = MyButton.getButtonPanel();
        bp.add(new MyButton(this, "Help", "", "Help"));
        l = new MyLabel(MyLabel.TYPE_LABEL, "Starting Sample Number:");
        bp.add(l);
        textStartingSampleNumber = new MyTextField(8);
        bp.add(textStartingSampleNumber);
        bp.add(new MyButton(this, "Start", "Start", "Start"));
        inputPanel.add(bp, BorderLayout.SOUTH);
        numRows++;

        int height = inputPanel.getPreferredSize().height;
        //inputPanel.setPreferredSize(new Dimension(width, height));
        op.add(inputPanel);

        removeAll();
        add(op);
        validate();
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Start")){
            if(rb1.isSelected()){
                if(ADCAcquisitionData.newDataAvailable){
                    startPCA();
                }else{
                    MyData.showInfoMessage(
                            "Operation not possible!!\n"
                            + "ADCAcuisition is not done!");
                }
                
            }else if(rb2.isSelected()){
                showResult();
            }else if(rb3.isSelected()){
                addMyPanel(new AnalyzePCAMultResults(this));
            }
            bGroup.clearSelection();
        }else if(e.getActionCommand().equals("Help")){
            MyData.showInfoMessage(help);
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

    /**
     * 
     */
    private void startPCA(){
        final MyWaitDialog waitDialog = new MyWaitDialog(MyData.mainFrame, 400, 100);
        new Thread(){
            @Override
            public void run(){
                startPCA(waitDialog);
                waitDialog.setVisible(false);
            }
        }.start();
        waitDialog.setVisible(true);
    }

    /**
     * 
     */
    private void startPCA(MyWaitDialog waitDialog) {
        try{

            //count Rate per sec
            pcaParams.startingSampleNumber = 0;
            try{
                pcaParams.startingSampleNumber = Integer.parseInt(textStartingSampleNumber.getText());
                if(pcaParams.startingSampleNumber < 0){
                    throw new Exception("Negative StartingSampleNumber not allowed."+
                        "\nPlease choose a value between 0 - "+(ADCAcquisitionData.numOfPoints-1));
                }
                if(pcaParams.startingSampleNumber >= ADCAcquisitionData.numOfPoints){
                    throw new Exception("Please choose StartingSampleNumber < "+ADCAcquisitionData.numOfPoints+
                        "\nADCAcquisition is done on "+ADCAcquisitionData.numOfPoints+" points only.");
                }
            }catch(Exception e){
                throw new Exception("Invalid InputCountRate - "+e.getMessage());
            }


            //make frame
            byte[] frame = new byte[18];
            frame[0] = 0x4F;//O
            frame[1] = 0x4B;//K
            frame[2] = pcaParams.processID;
            MyData.putIntBytesToBuffer(pcaParams.startingSampleNumber, frame, 3);
            for(int i=7;i<=13;i++){
                frame[i] = 0x00;//padding
            }
            frame[14] = 0x00;//status byte 1
            frame[15] = 0x00;//status byte 2
            frame[16] = 0x4F;//O
            frame[17] = 0x4B;//K

            //set status bytes
            MyData.setStatusBytes(frame);

        //    MyData.showErrorMessage("DATA="+MyData.hexify(frame));

            //UART_TEST
            waitDialog.status.setText("Checking status of the device..");
            MyData.myCommPort.testBBStatus(true);

            //send data
            MyData.myCommPort.writeFrame(frame);

            //waitForTime for data
            waitDialog.status.setText("Waiting for PC Analysis");
            MyData.myCommPort.waitForData();

            //create operation directory
            PCAData.createDirectories();

            //read first set of data
            File file = PCAData.getRecentDataFile();
            MyData.myCommPort.readDataFrame(file, MyCommPort.RESULT_TYPE_PCA);
            //write PCA value in paramsFile itself*****
            pcaParams.pcaVal = readPCAVal(file);

            //writing parameters in the param file
            file = PCAData.getRecentParamFile();
            pcaParams.uid = MyData.uid;
            pcaParams.write(file);

             //set data available flag
            PCAData.newDataAvailable = true;
            waitDialog.setVisible(false);
            //check value of startingSampleNumber by reading params file back
            //it will verify conversion of int to bytes and bytes to int back
            //pcaParams.read(file);
            MyData.showInfoMessage("PCA completed successfully.");
                    //+ "startingSampleNumber saved in params file="+pcaParams.startingSampleNumber);
        }catch(Exception e){
            waitDialog.setVisible(false);
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }
    /**
     * 
     */
    private void showResult() {
        try{
            File file;
            if(PCAData.newDataAvailable){
                file = PCAData.getRecentDataFile();
            }else{
                file = MyData.chooseFile(null);
            }
            if(file==null){
                return;
            }
            float pcaVal = readPCAVal(file);
            MyData.showInfoMessage("PCA Value = "+pcaVal);
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }

    private float readPCAVal(File file) throws Exception{
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4];
        fis.read(buffer);

        int intBits = ((buffer[3] & 0x00FF) << 24) |
                      ((buffer[2] & 0x00FF) << 16) |
                      ((buffer[1]  & 0x00FF) << 8) |
                       (buffer[0]  & 0x00FF);
        float pcaVal = Float.intBitsToFloat(intBits);
        return pcaVal;
    }
    //PCAParams
    PCAParams pcaParams = new PCAParams();
}