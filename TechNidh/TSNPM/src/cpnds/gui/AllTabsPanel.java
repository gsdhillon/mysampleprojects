package cpnds.gui;

import cpnds.gui.Home.HomeTab;
import cpnds.data.MyData;
import cpnds.gui.ADCAcquisition.*;
import gui.MyPanel;
import java.awt.GridLayout;
import cpnds.gui.AutoCorrelation.AutoCorrelationOptionsTab;
import cpnds.gui.CrossCorrelation.CrossCorrelationOptionsTab;
import cpnds.gui.FFTComputation.FFTComputationOptionsTab;
import cpnds.gui.PrincipleCompAnalysis.PCAnalysisOptionsTab;
import gui.MyConstants;
import java.awt.Color;
import javax.swing.JTabbedPane;


/**
 * @type     : Java Class
 * @name     : AllTabsPanel
 * @file     : AllTabsPanel.java
 * @created  : Feb 6, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class AllTabsPanel extends MyPanel{
    /**
     * Constructor
     */
    public AllTabsPanel(){
        super(new GridLayout(1,1));
        setDefaultBG();
        JTabbedPane tp = new JTabbedPane();
        tp.setFont(MyConstants.FONT_TABS);
        tp.setForeground(MyConstants.FG_TABS);
        tp.addTab("   Home   ", new HomeTab());
        tp.addTab(" ADC Acquisition ", new ADCAcquisitionOptionsTab());
        tp.addTab(" FFT Computation ", new FFTComputationOptionsTab());
        tp.addTab(" Auto Correlation ", new AutoCorrelationOptionsTab());
        tp.addTab(" Cross Correlation ", new CrossCorrelationOptionsTab());
        tp.addTab(" Principal Component Analysis ", new PCAnalysisOptionsTab());
        //tp.addTab(" Combined Analysis ", new CombinedAnalysisOptionsTab());
        add(tp);
        MyData.openDefaultPort();
    }
}