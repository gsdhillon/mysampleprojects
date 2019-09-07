package cpnds.gui.ADCAcquisition;

import gui.MyButton;
import gui.MyLabel;
import gui.MyPanel;
import gui.MyRadioButton;
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
 * @name     : ADCAcquisitionOptionsTab
 * @file     : ADCAcquisitionOptionsTab.java
 * @created  : May 14, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class ADCAcquisitionOptionsTab extends MyPanel implements ActionListener{
    private MyRadioButton rb1;
    private MyRadioButton rb2;
    private MyRadioButton rb3;
    private ButtonGroup bGroup;
    /**
     * Constructor
     */
    public ADCAcquisitionOptionsTab(){
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

        String s = "ADC ACQUISITION, GUI ANALYSIS AND STORAGE OF THE DATA";
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL_HEADING, s, JLabel.CENTER);
        l.setPreferredSize(new Dimension(width, rowHeight));
        op.add(l);

        int verticalGap = 20;
        int numRows = 0;

        MyPanel inputPanel = new MyPanel(new BorderLayout(0, verticalGap), "");
        MyPanel p = new MyPanel(new GridLayout(0, 1, 0, verticalGap), null);

        //options
        bGroup = new ButtonGroup();
        rb1 = new MyRadioButton("Start a new ADC Acquisition");
        bGroup.add(rb1);
        p.add(rb1);
        numRows++;

        rb2 = new MyRadioButton("Analyze recently acquired Data");
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
       // inputPanel.setPreferredSize(new Dimension(width, height));
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
                addMyPanel(new NewADCAcquisitionPanel(this));
            }else if(rb2.isSelected()){
                showAnalysisPanel();
            }else if(rb3.isSelected()){
                addMyPanel(new AnalyzeADCAcqMultResults(this));
            }
            bGroup.clearSelection();
        }
    }

    /**
     *
     */
    public void showAnalysisPanel(){
        addMyPanel(new AnalyzeADCAcquisition(this));
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
}