package cpnds.gui.FFTComputation;


import cpnds.data.LogReports;
import cpnds.data.MyData;
import cpnds.gui.ADCAcquisition.ADCAcqParams;
import gui.MyButton;
import gui.MyLabel;
import gui.MyPanel;import gui.MyRadioButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;



/**
 * @type     : Java Class
 * @name     : AnalyzeFFTDataPanel
 * @file     : AnalyzeFFTDataPanel.java
 * @created  : May 15, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class AnalyzeFFTDataPanel extends MyPanel implements ActionListener{
    private MyRadioButton rb1;//Display data in form of FFT
    private MyRadioButton rb2;//Display data in form of PSD
    private ButtonGroup bGroup;
    private FFTComputationOptionsTab parent;

    private String help =
        "1. To see all the FFT analysis results,  press ShowResults button.";
    /**
     * Constructor
     */
    public AnalyzeFFTDataPanel(FFTComputationOptionsTab parent){
        super(new GridLayout(1, 1));
        this.parent = parent;
        showOptionPane();
    }

    /**
     *
     */
    public final void showOptionPane(){
        MyPanel op = new MyPanel(new FlowLayout(FlowLayout.CENTER, 800, 50));
        op.setDefaultBG();
        int rowHeight = 35;
        int width = 600;
        String s = "ACQUISITION, GUI ANALYSIS AND STORAGE OF \"FFT DATA\"";
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL_HEADING, s, JLabel.CENTER);
        l.setPreferredSize(new Dimension(width, rowHeight));
        op.add(l);
        int verticalGap = 20;
        int numRows = 0;
        MyPanel inputPanel = new MyPanel(new BorderLayout(0, verticalGap), "");
        MyPanel p = new MyPanel(new GridLayout(0, 1, 0, verticalGap), null);
        //options
        bGroup = new ButtonGroup();
        rb1 = new MyRadioButton("Display data in form of FFT");
        bGroup.add(rb1);
        p.add(rb1);
        numRows++;
        rb2 = new MyRadioButton("Display data in form of PSD");
        bGroup.add(rb2);
        p.add(rb2);
        numRows++;
        inputPanel.add(p, BorderLayout.CENTER);
        //Buttons
        MyPanel bp = MyButton.getButtonPanel();
        bp.add(new MyButton(this, "Back", "", "Back"));
        bp.add(new MyButton(this, "Help", "", "Help"));
        bp.add(new MyButton(this, "ShowResults", "ShowResults"));
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
                addMyPanel(new DisplayDataInFFT(this));
            }else if(rb2.isSelected()){
                addMyPanel(new DisplayDataInPSD(this));
            }
            bGroup.clearSelection();
        }/*else if(e.getActionCommand().equals("ComputeRMSPower")){
            try{
                File f;
                if(FFTData.newDataAvailable){
                    f = FFTData.getRecentDataFile();
                }else{
                    f = MyData.chooseFile(null);
                }
                float rmsPower = FFTData.computeRMSPower(f);
                MyData.showInfoMessage("RMS POWER = "+rmsPower);
            }catch(Exception ex){
                MyData.showErrorMessage("Exception - "+ex.getMessage());
            }
        }*/else if(e.getActionCommand().equals("ShowResults")){
            try {
                File dataFile;
                if(FFTData.newDataAvailable){
                    dataFile = FFTData.getRecentDataFile();
                }else{
                    dataFile = MyData.chooseFile(null);
                }
                File paramsFile = FFTData.getParamFile(dataFile);
                FFTParams fftParams = new FFTParams();
                fftParams.read(paramsFile);

                ///  --- 04-09-2011 Recalculate all results and save
                ADCAcqParams adcAcqParams = new ADCAcqParams();
                File adcParamFile = adcAcqParams.getParamFile(paramsFile.getParentFile().getParent());
                adcAcqParams.read(adcParamFile);
                FFTData.calculateAllResults(dataFile, adcAcqParams, fftParams);
                fftParams.write(paramsFile);
                ///   end 04-09-2011

                fftParams.showResults();
            } catch (Exception ex) {
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
        }else if(e.getActionCommand().equals("Help")){
            MyData.showInfoMessage(help);
        }else if(e.getActionCommand().equals("Back")){
            parent.showOptionPane();
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

    public void showDisplayFFT(){
        addMyPanel(new DisplayDataInFFT(this));
    }
}