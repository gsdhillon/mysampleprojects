package cpnds.gui.AutoCorrelation;

import cpnds.data.MyData;
import cpnds.gui.ADCAcquisition.ADCAcquisitionData;
import gui.MyButton;
import gui.MyLabel;
import gui.MyPanel;import gui.MyRadioButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;



/**
 * @type     : Java Class
 * @name     : AutoCorrelationOptionsTab
 * @file     : AutoCorrelationOptionsTab.java
 * @created  : May 15, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class AutoCorrelationOptionsTab extends MyPanel implements ActionListener{
    private MyRadioButton rb1 = new MyRadioButton("Start a new Auto Correlation Operation");
    private MyRadioButton rb2 = new MyRadioButton("Analyze recently acquired Data through Auto Correlation");
    private MyRadioButton rb3 = new MyRadioButton("Analyze multiple previous results");
    private ButtonGroup bGroup;

    /**
     * Constructor
     */
    public AutoCorrelationOptionsTab(){
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

        String s = "AUTO CORRELATION";
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL_HEADING, s, JLabel.CENTER);
        l.setPreferredSize(new Dimension(width, rowHeight));
        op.add(l);

        int verticalGap = 20;
        int numRows = 0;

        MyPanel inputPanel = new MyPanel(new BorderLayout(0, verticalGap), "");
        MyPanel p = new MyPanel(new GridLayout(0, 1, 0, verticalGap), null);

        //options
        bGroup = new ButtonGroup();
        bGroup.add(rb1);
        p.add(rb1);
        numRows++;

        bGroup.add(rb2);
        p.add(rb2);
        numRows++;

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
      * @param p
      */
    private void addMyPanel(MyPanel p){
        removeAll();
        add(p);
        validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Start")){
            if(rb1.isSelected()){
               if(ADCAcquisitionData.newDataAvailable){
                    addMyPanel(new NewAutoCorrelationPanel(this));
                }else{
                    MyData.showInfoMessage(
                            "Operation not possible!!\n"
                            + "ADCAcuisition is not done!");
                }
            }else if(rb2.isSelected()){
                addMyPanel(new AnalyzeAutoCoorelation(this));
            }else if(rb3.isSelected()){
                addMyPanel(new AnalyzeAutoCorrMultResults(this));
            }
            bGroup.clearSelection();
        }
    }

    /**
     *
     */
    public void showAnalysisPanel(){
        addMyPanel(new AnalyzeAutoCoorelation(this));
    }
}