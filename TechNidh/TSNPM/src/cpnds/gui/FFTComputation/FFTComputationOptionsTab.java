package cpnds.gui.FFTComputation;


import cpnds.data.MyData;
import cpnds.gui.ADCAcquisition.ADCAcquisitionData;
import gui.MyButton;
import gui.MyLabel;
import gui.MyPanel;import gui.MyRadioButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;



/**
 * @type     : Java Class
 * @name     : FFTComputationOptionsTab
 * @file     : FFTComputationOptionsTab.java
 * @created  : May 15, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class FFTComputationOptionsTab extends MyPanel implements ActionListener{
    private MyRadioButton rb1;
    private MyRadioButton rb2;
    private MyRadioButton rb3;
    private ButtonGroup bGroup;
    /**
     * Constructor
     */
    public FFTComputationOptionsTab(){
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
        int width = 600;

        String s = "FFT COMPUTATION";
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL_HEADING, s, JLabel.CENTER);
        l.setPreferredSize(new Dimension(width, rowHeight));
        op.add(l);

        int verticalGap = 20;
        int numRows = 0;

        MyPanel inputPanel = new MyPanel(new BorderLayout(0, verticalGap), "");
        MyPanel p = new MyPanel(new GridLayout(0, 1, 0, verticalGap), null);

        //options
        bGroup = new ButtonGroup();
        rb1 = new MyRadioButton("Start a new FFT Computation");
        bGroup.add(rb1);
        p.add(rb1);
        numRows++;

        rb2 = new MyRadioButton("Analyze recently acquired Data through FFT");
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
        MyButton b = new MyButton(this, "Start", "Start", "Start");
        bp.add(b);

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
                    addMyPanel(new NewFFTComputationPanel(this));
                }else{
                    MyData.showInfoMessage(
                            "Operation not possible!!\n"
                            + "ADCAcuisition is not done!");
                }
            }else if(rb2.isSelected()){
                addMyPanel(new AnalyzeFFTDataPanel(this));
            }else if(rb3.isSelected()){
                addMyPanel(new AnalyzeFFTMultResults(this));
            }
            bGroup.clearSelection();
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
    public void showAnalysisPanel(){
        AnalyzeFFTDataPanel p = new AnalyzeFFTDataPanel(this);
        //p.showDisplayFFT();
        addMyPanel(p);
    }
}