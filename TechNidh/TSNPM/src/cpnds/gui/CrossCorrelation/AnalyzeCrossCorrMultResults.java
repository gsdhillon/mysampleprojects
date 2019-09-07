package cpnds.gui.CrossCorrelation;

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
 * @name     : AnalyzeCrossCorrMultResults
 * @file     : AnalyzeCrossCorrMultResults.java
 * @created  : Jun 22, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class AnalyzeCrossCorrMultResults extends MyPanel implements ActionListener{
    private LineGraph lineGraph;
    private String title = "CROSS CORRELATION, PREVIOUS RESULTS";
    private MyLabel titleLabel;
    private String labelOfDataY = "";
    private String labelOfDataX = "Input Power";
    private DataPoint[] allPoints;

    private ADCAcqParams[] adcAcqParamsAll = null;
    private CrossCorrParams[] crossCorrParamsAll = null;
    private CrossCorrCommonParams[] crossCorrCommonParamsAll = null;

    private ADCAcqParams[] adcAcqParamsSelected = null;
    private CrossCorrParams[] crossCorrParamsSelected = null;
    private CrossCorrCommonParams[] crossCorrCommonParamsSelected = null;

    private String resultNoString = "Preferred Results";
    private MyComboBox comboResultNo = new MyComboBox(new String[]{
        "Preferred Results",
        "Result - 1",
        "Result - 2",
        "Result - 3",
        "Result - 4",
        "Result - 5",
        "Result - 6",
        "Result - 7",
        "Result - 8",
        "Result - 9",
        "Result - 10",
    });

    private MyComboBox comboResltType;
    private CrossCorrResultsTM tm = new CrossCorrResultsTM();
    private MyTable dataTable = new MyTable(tm);
    private JScrollPane sp = new JScrollPane(dataTable);

    private MyPanel middlePanel = new MyPanel(new GridLayout(1,1));
    private MyButton dataGraphSwitchButton;
    private CrossCorrelationOptionsTab parent;
    /**
     * Constructor
     */
    public AnalyzeCrossCorrMultResults(CrossCorrelationOptionsTab parent){
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
        bp.add(new MyLabel(MyLabel.TYPE_LABEL, "Result:"));
        comboResultNo.setPreferredSize(new Dimension(170, 24));
        bp.add(comboResultNo);
        MyButton  b = new MyButton(this, "Load", "", "Load");
        bp.add(b);

        b = new MyButton(this, "Browse", "", "Browse");
        bp.add(b);

        //Min Max Avg option
        comboResltType = new MyComboBox(new String[]{
            "Input power Vs Min Value",//index 0
            "Input power Vs Max Value",//index 1
            "Input power Vs Avg Value",//index 2
            "Input power Vs TimePrd",//index 3
            "Input power Vs Frequency", //index 4
            "Input power Vs MaxAllResults" //index 5
        });
        comboResltType.setPreferredSize(new Dimension(260, 24));//GG
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

        //0 means preferred result
        int resultNo = comboResultNo.getSelectedIndex();
        resultNoString = (String) comboResultNo.getSelectedItem();
        int preResultNo = 1;
        //count no. of exp dirs
        int numExperiments = 0;
        CrossCorrCommonParams commonParams = new CrossCorrCommonParams();
        for(int k=0;k<dateDirs.length;k++){
            File[] timeDirs = dateDirs[k].listFiles();
            //MyData.showInfoMessage(dateDirs[k].getName());
            if(timeDirs==null||timeDirs.length==0){
                continue;
            }else{
                for(int i=0;i<timeDirs.length;i++){
                    //check cross corr param file for resNo exists or not
                    File file = CrossCorrData.getCommonParamFileFromExpDir(timeDirs[i]);
                    if(!file.exists()){
                        continue;
                    }
                    commonParams.read(file);
                    if(resultNo == 0){
                        preResultNo = commonParams.preferredResult;
                        file = CrossCorrData.getParamFileFromExpDir(timeDirs[i], preResultNo);
                    }else{
                        file = CrossCorrData.getParamFileFromExpDir(timeDirs[i], resultNo);
                    }
                    if(!file.exists()){
                        continue;
                    }
                    numExperiments++;
                }
            }
        }
        //read adcparams files from all exp dirs
        crossCorrCommonParamsAll = new CrossCorrCommonParams[numExperiments];
        adcAcqParamsAll = new ADCAcqParams[numExperiments];
        crossCorrParamsAll = new CrossCorrParams[numExperiments];

        int count = 0;
        for(int k=0;k<dateDirs.length;k++){
            File[] timeDirs = dateDirs[k].listFiles();
            if(timeDirs==null||timeDirs.length==0){
                continue;
            }
            for(int i=0;i<timeDirs.length;i++){
                //first read common params file
                File file = CrossCorrData.getCommonParamFileFromExpDir(timeDirs[i]);
                if(!file.exists()){
                    continue;
                }
                crossCorrCommonParamsAll[count] = new CrossCorrCommonParams();
                crossCorrCommonParamsAll[count].read(file);
                //now read params file
                crossCorrParamsAll[count] = new CrossCorrParams();
                if(resultNo == 0){
                    preResultNo = crossCorrCommonParamsAll[count].preferredResult;
                    file = CrossCorrData.getParamFileFromExpDir(timeDirs[i], preResultNo);
                    //MyData.showInfoMessage("preferred Result =  "+preResultNo);
                }else{
                    file = CrossCorrData.getParamFileFromExpDir(timeDirs[i], resultNo);
                }
                if(!file.exists()){
                    continue;
                }
                crossCorrParamsAll[count].read(file);
                
                //read adc params
                adcAcqParamsAll[count] = new ADCAcqParams();
                file = ADCAcquisitionData.getParamFileFromExpDir(timeDirs[i]);
                adcAcqParamsAll[count].read(file);
                count++;
            }
        }
        
        /*****
        //filter out DECADE/YES if selected
        crossCorrParamsSelected = crossCorrParamsAll;
        adcAcqParamsSelected = adcAcqParamsAll;
        crossCorrCommonParamsSelected = crossCorrCommonParamsAll;
        int decadeOptions = 2;//ALL EXP
        if(comboResultNo.getSelectedIndex()==1){
            decadeOptions = CrossCorrParams.DECADE_NO;
        }else if(comboResultNo.getSelectedIndex()==2){
            decadeOptions = CrossCorrParams.DECADE_YES;
        }
        //user want eaither DECADE_YES Only OR DECADE_NO only
        if(decadeOptions!=2){
            //first count
            int numExpsFiltered = 0;
            for(int sno=0;sno<numExperiments;sno++){
                if(crossCorrParamsSelected[sno].decadeOption == decadeOptions){
                    numExpsFiltered++;
                }
            }
            //now put filtered back to ALL
            adcAcqParamsAll = new ADCAcqParams[numExpsFiltered];
            crossCorrParamsAll = new CrossCorrParams[numExpsFiltered];
            crossCorrCommonParamsAll = new CrossCorrCommonParams[numExpsFiltered];
            count = 0;
            for(int sno=0;sno<numExperiments;sno++){
                if(crossCorrParamsSelected[sno].decadeOption == decadeOptions){
                    crossCorrParamsAll[count] = crossCorrParamsSelected[sno];
                    adcAcqParamsAll[count] = adcAcqParamsSelected[sno];
                    crossCorrCommonParamsAll[count] = crossCorrCommonParamsSelected[sno];
                    count++;
                }
            }
        }****/
        //set selected same as all
        crossCorrParamsSelected = crossCorrParamsAll;
        adcAcqParamsSelected = adcAcqParamsAll;
        crossCorrCommonParamsSelected = crossCorrCommonParamsAll;
        tm.setData(adcAcqParamsSelected, 
                        crossCorrParamsSelected, crossCorrCommonParamsSelected);
        MyData.showInfoMessage(resultNoString + " of all experiments is being loaded.\n"
                + "To load other results choose result no. from \n "
                + "the combo list and press Load button.");
    }
    /**
     * 
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Load")){
            try{
                //check decade yes/no option
                readAllPreviousResults();
                //comboResltType.setSelectedIndex(0);
                showResults();
            }catch(Exception ex){
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
        }else if(e.getActionCommand().equals("Browse")){
            try{
                new OpenMulCrossCorrExpDialog();
                tm.setData(adcAcqParamsSelected, 
                        crossCorrParamsSelected, crossCorrCommonParamsSelected);
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
        if(crossCorrParamsSelected==null){
            return;
        }
        allPoints = new DataPoint[crossCorrParamsSelected.length];
        String type = "";
        for(int i=0;i<allPoints.length;i++){
           allPoints[i] = new DataPoint();
           allPoints[i].sno = i;
           allPoints[i].x = (int)adcAcqParamsSelected[i].inputPower;
           allPoints[i].uid = adcAcqParamsSelected[i].uid;
           if(comboResltType.getSelectedIndex()==0){
               allPoints[i].y = crossCorrParamsSelected[i].minVal;
               type = "Min Values";
           }else if(comboResltType.getSelectedIndex()==1){
               allPoints[i].y = crossCorrParamsSelected[i].maxVal;
               type = "Max Values";
           }else if(comboResltType.getSelectedIndex()==2){
               allPoints[i].y = crossCorrParamsSelected[i].avgVal;
               type = "Average Values";
           }else if(comboResltType.getSelectedIndex()==3){
               allPoints[i].y = crossCorrParamsSelected[i].getAvgTP();
               type = "Time Period, ThNo.="+
                       (crossCorrParamsSelected[i].getSelectedThTPNo()+1);
           }else if(comboResltType.getSelectedIndex()==4){
               allPoints[i].y = crossCorrParamsSelected[i].getSignalFreq();
               type = "Signal Frequency, ThNo.="+
                       (crossCorrParamsSelected[i].getSelectedThTPNo()+1);
           }else if(comboResltType.getSelectedIndex()==5){
               allPoints[i].y = crossCorrCommonParamsSelected[i].maxVal;
               type = "AllResultMax Values";
           }
        }
        //read params and set min, max, avg and save also
        type = type + ", "+resultNoString;
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
        //dataGraphSwitchButton.setActionCommand("ShowGraph");
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
        dataGraphSwitchButton.setText("ShowData");
        dataGraphSwitchButton.setActionCommand("ShowData");
        middlePanel.removeAll();
        middlePanel.add(lineGraph.getScrollPane());
        middlePanel.validate();
        middlePanel.repaint();
    }
    /**
     *
     */
    private class OpenMulCrossCorrExpDialog extends JDialog implements ActionListener{
        private String title = "Select Experiment to analyze";
        private MyLabel titleLabel;
        private CrossCorrResultsTM tm = new CrossCorrResultsTM();
        private MyTable dataTable = new MyTable(tm);
        private JScrollPane sp = new JScrollPane(dataTable);
        private MyComboBox comboSetThTP = new MyComboBox(CrossCorrParams.setNosThTP);
        /**
         * Constructor
         */
        public OpenMulCrossCorrExpDialog() {
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

            bp.add(new MyLabel(MyLabel.TYPE_LABEL, "ThValNo.:"));
            comboSetThTP.setPreferredSize(new Dimension(45, 24));
            bp.add(comboSetThTP);

            MyButton b = new MyButton(this, "Open", "Open Selected", "Feed");
            bp.add(b);
            b = new MyButton(this, "Cancel", "Cancel", "Cancel");
            bp.add(b);

            add(bp, BorderLayout.SOUTH);
            tm.setData(adcAcqParamsAll, crossCorrParamsAll, crossCorrCommonParamsAll);
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
                crossCorrParamsAll[i].setSelectedSetOfThTP(comboSetThTP.getSelectedIndex());
            }
            if(dataTable.getSelectedRowCount()<=0){
                return;
            }
            crossCorrParamsSelected = new CrossCorrParams[dataTable.getSelectedRowCount()];
            adcAcqParamsSelected = new ADCAcqParams[dataTable.getSelectedRowCount()];
            int count = 0;

            for(int i=0;i<dataTable.getRowCount();i++){
                if(dataTable.isRowSelected(i)){
                    crossCorrParamsSelected[count] = crossCorrParamsAll[i];
                    adcAcqParamsSelected[count] = adcAcqParamsAll[i];
                    crossCorrCommonParamsSelected[count] = crossCorrCommonParamsAll[count];
                    count++;
                }
            }
        }
    }
}