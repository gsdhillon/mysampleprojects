package cpnds.gui.FFTComputation;

import cpnds.gui.ADCAcquisition.*;
import cpnds.MyGraphs.LineGraph;
import cpnds.data.MyData;
import cpnds.MyGraphs.DataPoint;
import cpnds.data.LogReports;
import gui.MyPanel;
import gui.MyButton;
import gui.MyComboBox;
import gui.MyLabel;
import gui.MyTable;
import gui.MyTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * @type     : Java Class
 * @name     : AnalyzeFFTMultResults
 * @file     : AnalyzeFFTMultResults.java
 * @created  : Jun 22, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class AnalyzeFFTMultResults extends MyPanel implements ActionListener{
    private LineGraph lineGraph;
    private String title = "FFT COMPUTATION, PREVIOUS RESULTS";
    private MyLabel titleLabel;
    private String labelOfDataY = "";
    private String labelOfDataX = "Input Power";
    private DataPoint[] allPoints;
    private ADCAcqParams[] adcAcqParamsAll = null;
    private FFTParams[] fftParamsAll = null;
    private ADCAcqParams[] adcAcqParamsSelected = null;
    private FFTParams[] fftParamsSelected = null;
    private MyComboBox comboResltType;
    private FFTResultsTM tm = new FFTResultsTM();
    private MyTable dataTable = new MyTable(tm);
    private JScrollPane sp = new JScrollPane(dataTable);

    private MyPanel middlePanel = new MyPanel(new GridLayout(1,1));
    private MyButton dataGraphSwitchButton;
    private FFTComputationOptionsTab parent;
    /**
     * Constructor
     */
    public AnalyzeFFTMultResults(FFTComputationOptionsTab parent){
        super(new BorderLayout());
        this.parent = parent;
        int rowHeight = 30;
        int width = 1000;

        //Top panel
        MyPanel topPanel = new MyPanel(new FlowLayout(FlowLayout.CENTER,0,4));
        topPanel.setDefaultBG();
        topPanel.setPreferredSize(new Dimension(width, rowHeight));

        //add Title
        titleLabel = new MyLabel(MyLabel.TYPE_LABEL_HEADING, title, JLabel.CENTER);
        topPanel.add(titleLabel);

        add(topPanel, BorderLayout.NORTH);

        //add middle panel
        
        add(middlePanel, BorderLayout.CENTER);

        //buttons panel
        MyPanel bp = MyButton.getButtonPanel();

        MyButton b = new MyButton(this, "Browse", "", "Browse");
        bp.add(b);

        //Min Max Avg option
        comboResltType = new MyComboBox(new String[]{
            "Input power Vs Min val",//index 0
            "Input power Vs Max val",//index 1
            "Input power Vs Avg val",//index 2
            "Input power Vs RMS val",//index 3
            "InputPower Vs WeightedVal",//index 4
         });
        comboResltType.setPreferredSize(new Dimension(240, 22));//GG
        bp.add(comboResltType);
        b = new MyButton(this, "Show", "Show");
        bp.add(b);
        bp.add(new JLabel("   "));
        //Data and Graph Switch button
        dataGraphSwitchButton = new MyButton(this, "ShowData", "ShowData", "Data");
        bp.add(dataGraphSwitchButton);
        b = new MyButton(this, "Back", "Back", "Back");
        bp.add(b);
        add(bp, BorderLayout.SOUTH);
        try{
            readAllPreviousResults();
            comboResltType.setSelectedIndex(0);
            showResults();
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }
    /**
     *
     */
    private void readAllPreviousResults() throws Exception{
        File dataDir = new File(MyData.dataPath);
        File[] dateDirs = dataDir.listFiles();
        if(dateDirs==null || dateDirs.length==0){
            MyData.showInfoMessage("No Experiment Data found");
            return;
        }
        //count no. of exp dirs
        int count = 0;
        for(int k=0;k<dateDirs.length;k++){
            File[] timeDirs = dateDirs[k].listFiles();
            //MyData.showInfoMessage(dateDirs[k].getName());
            if(timeDirs==null||timeDirs.length==0){
                continue;
            }else{
                for(int i=0;i<timeDirs.length;i++){
                    File file = FFTData.getParamFileFromExpDir(timeDirs[i]);
                    if(!file.exists()){
                        continue;
                    }else{
                        count++;
                    }
                }
            }
        }

        //read adcparams files from all exp dirs
        adcAcqParamsAll = new ADCAcqParams[count];
        fftParamsAll = new FFTParams[count];
        count = 0;
        for(int k=0;k<dateDirs.length;k++){
            File[] timeDirs = dateDirs[k].listFiles();
            if(timeDirs==null||timeDirs.length==0){
                continue;
            }
            for(int i=0;i<timeDirs.length;i++){
                File file = FFTData.getParamFileFromExpDir(timeDirs[i]);
                if(!file.exists()){
                    continue;
                }
                
                fftParamsAll[count] = new FFTParams();
                fftParamsAll[count].read(file);

                adcAcqParamsAll[count] = new ADCAcqParams();
                file = ADCAcquisitionData.getParamFileFromExpDir(timeDirs[i]);
                adcAcqParamsAll[count].read(file);

                count++;
            }
        }
        fftParamsSelected = fftParamsAll;
        adcAcqParamsSelected = adcAcqParamsAll;
        tm.setData(adcAcqParamsSelected, fftParamsSelected);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       if(e.getActionCommand().equals("Browse")){
            try{
                new OpenMulFFTExpDialog();
                tm.setData(adcAcqParamsSelected, fftParamsSelected);
                showResults();
            }catch(Exception ex){
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
        }else if(e.getActionCommand().equals("Show")){
            try{
                showResults();
            }catch(Exception ex){
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
        }else if(e.getActionCommand().equals("ShowData")){
            showData();
        }else if(e.getActionCommand().equals("ShowGraph")){
            showGraph();
        }else if(e.getActionCommand().equals("Back")){
            parent.showOptionPane();
        }
    }
    /**
     *
     */
    private void showResults() throws Exception{
        if(fftParamsSelected==null){
            return;
        }
        allPoints = new DataPoint[fftParamsSelected.length];
        String type = "";
        for(int i=0;i<allPoints.length;i++){
           allPoints[i] = new DataPoint();
           allPoints[i].sno = i;
           allPoints[i].x = (int)adcAcqParamsSelected[i].inputPower;
           allPoints[i].uid = adcAcqParamsSelected[i].uid;
           if(comboResltType.getSelectedIndex()==0){
               allPoints[i].y = fftParamsSelected[i].getMinVal();
               type = "Min Values"
                       + ", Type="+fftParamsSelected[i].getDisplayType()
                       + ", "+fftParamsSelected[i].getSelectedDecade();
           }else if(comboResltType.getSelectedIndex()==1){
               allPoints[i].y = fftParamsSelected[i].getMaxVal();
               type = "Max Values"
                       + ", Type="+fftParamsSelected[i].getDisplayType()
                       + ", "+fftParamsSelected[i].getSelectedDecade();
           }else if(comboResltType.getSelectedIndex()==2){
               allPoints[i].y = fftParamsSelected[i].getAvgVal();
               type = "Average Values"
                       + ", Type="+fftParamsSelected[i].getDisplayType()
                       + ", "+fftParamsSelected[i].getSelectedDecade();
           }else if(comboResltType.getSelectedIndex()==3){
               allPoints[i].y = fftParamsSelected[i].getRMSVal();
               type = "RMS Values"
                       + ", Type="+fftParamsSelected[i].getDisplayType()
                       + ", "+fftParamsSelected[i].getSelectedDecade();
           }else if(comboResltType.getSelectedIndex()==4){
               allPoints[i].y = fftParamsSelected[i].getWeightedVal();
               type = "Weighted "
                       + fftParamsSelected[i].getDisplayType()
                       + ", "+fftParamsSelected[i].getSelectedDecade();
           }
        }
        //read params and set min, max, avg and save also

        //show graph
        labelOfDataY = type;
        drawGraph(allPoints);

        //set Title
        titleLabel.setText(title+" : "+type);
    }

    /**
     *
     * @param points
     * @throws Exception
     */
    private void drawGraph(DataPoint[] points) throws Exception{
        //create graph
        lineGraph = new LineGraph(points, labelOfDataX, labelOfDataY);
        lineGraph.showUID = true;
        //add graph in the panel
        showGraph();
    }
    /**
     *
     */
    private void showData() {
        //if(adcAcqParams == null) return;
        dataGraphSwitchButton.setCommandTextIcon("ShowGraph", "ShowGraph", "Graph");
        //dataGraphSwitchButton.setText("ShowGraph");
       // dataGraphSwitchButton.setActionCommand("ShowGraph");
        middlePanel.removeAll();
        middlePanel.add(sp);
        middlePanel.validate();
        middlePanel.repaint();
    }

    /**
     *
     */
    private void showGraph() {
        if(lineGraph==null) return;
        dataGraphSwitchButton.setCommandTextIcon("ShowData", "ShowData", "Data");
        //dataGraphSwitchButton.setText("ShowData");
        //dataGraphSwitchButton.setActionCommand("ShowData");
        middlePanel.removeAll();
        middlePanel.add(lineGraph.getScrollPane());
        middlePanel.validate();
        middlePanel.repaint();
    }
    /**
     *
     */
    private class OpenMulFFTExpDialog extends JDialog implements ActionListener{
        private String title = "Select Experiment to analyze";
        private MyLabel titleLabel;
        private FFTResultsTM tm = new FFTResultsTM();
        private MyTable dataTable = new MyTable(tm);
        private JScrollPane sp = new JScrollPane(dataTable);
        private MyComboBox comboDisplayType = new MyComboBox(new String[]{"FFT  ", "PSD  "});
        private MyComboBox comboDecade = new MyComboBox(new String[]{
              "All Points  ", "Decade 1 ",
              "Decade 2", "Decade 3",
              "Decade 4", "Decade 5",
              "Decade 6", "Decade 7"});
        public byte displayType = FFTData.DISPLAY_FFT;
        /**
         * Constructor
         */
        public OpenMulFFTExpDialog() {
            super(MyData.mainFrame, true);

            //add Title
            titleLabel = new MyLabel(MyLabel.TYPE_LABEL_HEADING, title, JLabel.CENTER);
            add(titleLabel, BorderLayout.NORTH);

            //add middle panel
            add(sp, BorderLayout.CENTER);

            //buttons panel
            MyPanel bp = new MyPanel(new FlowLayout(FlowLayout.RIGHT,0,2), null);
            //add search UID field
            bp.add(new MyLabel(MyLabel.TYPE_LABEL, "SearchUID:"));
            MyTextField tf = new MyTextField(10);
            bp.add(tf);
            dataTable.addSearchField(0, tf, MyTable.EXACT_MATCH);

            comboDisplayType.setPreferredSize(new Dimension(80, 24));
            bp.add(comboDisplayType);
            comboDecade.setPreferredSize(new Dimension(160, 24));
            bp.add(comboDecade);
            
            MyButton  b = new MyButton(this, "Open", "Open Selected", "Feed");
            bp.add(b);
            b = new MyButton(this, "Cancel", "Cancel", "Cancel");
            bp.add(b);

            add(bp, BorderLayout.SOUTH);
            tm.setData(adcAcqParamsAll, fftParamsAll);
            setSize(MyData.width, 500);
            setLocationRelativeTo(MyData.mainFrame);
            setVisible(true);
        }
         @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Open")){
                try{
                    openSelected();
                    setVisible(false);
                }catch (Exception ex) {
                    LogReports.logError(ex);
                    MyData.showErrorMessage(ex);
                }
            }else if(e.getActionCommand().equals("Cancel")){
                setVisible(false);
            }
        }
        /**
         *
         * @throws Exception
         */
        private void openSelected() throws Exception{
            for(int i=0;i<dataTable.getRowCount();i++){
                fftParamsAll[i].setSelectedDecade(comboDecade.getSelectedIndex()-1);//-1 for all
                fftParamsAll[i].setDisplayType((byte)comboDisplayType.getSelectedIndex());
             }
            if(dataTable.getSelectedRowCount()<1){
                return;
            }
            fftParamsSelected = new FFTParams[dataTable.getSelectedRowCount()];
            adcAcqParamsSelected = new ADCAcqParams[dataTable.getSelectedRowCount()];
            int count = 0;
            for(int i=0;i<dataTable.getRowCount();i++){
                if(dataTable.isRowSelected(i)){
                    fftParamsSelected[count] = fftParamsAll[i];
                    adcAcqParamsSelected[count] = adcAcqParamsAll[i];
                    count++;
                }
            }
        }
    }
}