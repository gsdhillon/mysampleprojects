package cpnds.gui.AutoCorrelation;

import cpnds.MyGraphs.LineGraph;
import cpnds.data.LogReports;
import cpnds.data.MyData;
import cpnds.MyGraphs.DataPoint;
import cpnds.data.DataTableModel;
import cpnds.gui.ADCAcquisition.ADCAcqParams;
import gui.MyPanel;
import gui.MyButton;
import gui.MyComboBox;
import gui.MyLabel;
import gui.MyTable;
import gui.MyTextField;
import gui.MyWaitDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
/**
 * @type     : Java Class 
 * @name     : AnalyzeAutoCoorelation
 * @file     : AnalyzeAutoCoorelation.java
 * @created  : May 29, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class AnalyzeAutoCoorelation extends MyPanel implements ActionListener{
    private LineGraph lineGraph;
    private File dataFile = null;
    private File paramsFile = null;
    private File adcParamFile = null;
    private String title = "ACQUISITION, GUI ANALYSIS AND STORAGE OF \"AUTO CORRELATION DATA\"";
    private MyLabel titleLabel;
    private DataPoint[] allPoints = null;
    private DataPoint[] zoomPoints = null;
    private DataPoint[] tpPoints = null;
    private int tpStart = 0;
    private int tpEnd = 0;

    private DataTableModel tm = new DataTableModel();
    private MyTable dataTable = new MyTable(tm);
    private JScrollPane sp = new JScrollPane(dataTable);

    private MyPanel middlePanel = new MyPanel(new GridLayout(1,1));
    private MyTextField textStartX = new MyTextField(8);
    private MyTextField textEndX = new MyTextField(8);
    private MyTextField textThVal = new MyTextField(8);
    private MyButton dataGraphSwitchButton;
    private MyButton tpFltTpButton;

    private MyComboBox comboSetThTP = new MyComboBox(AutoCorrParams.setNosThTP);

    private AutoCorrelationOptionsTab parent;

    private String help = LineGraph.getHelp();
    /**
     * Constructor
     */
    public AnalyzeAutoCoorelation(AutoCorrelationOptionsTab parent){
        super(new BorderLayout());
        this.parent = parent;

        //Top panel
        MyPanel topPanel = new MyPanel(new BorderLayout());
        topPanel.setDefaultBG();
        //add Title
        titleLabel = new MyLabel(MyLabel.TYPE_LABEL_HEADING, title, JLabel.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        //parameters panel
        topPanel.add(autoCorrParams.sp, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
       
        //add middle panel
        add(middlePanel, BorderLayout.CENTER);
        //buttons panel
        MyPanel bp = MyButton.getButtonPanel();
        bp.add(new MyButton(this, "Browse", "", "Browse"));

        bp.add(new MyLabel(MyLabel.TYPE_LABEL, "From:"));
        bp.add(textStartX);
        bp.add(new MyLabel(MyLabel.TYPE_LABEL, " To:"));
        bp.add(textEndX);
        bp.add(new MyButton(this, "ZoomIN", "", "Zoom"));
        bp.add(new MyButton(this, "ZoomOut", "", "ZoomOut"));
        dataGraphSwitchButton = new MyButton(this, "ShowData", "", "Data");
        bp.add(dataGraphSwitchButton);
        //Threshold and AvgTimePeriod
        bp.add(comboSetThTP);
        comboSetThTP.setPreferredSize(new Dimension(45, 24));
        bp.add(new MyLabel(MyLabel.TYPE_LABEL, "ThVal:"));
        bp.add(textThVal);
        bp.add(new MyButton(this, "SetTh", "SetTh"));
        tpFltTpButton = new MyButton(this, "ShowTPGraph", "ShowTPGraph");//"FiltAvgTP", "FiltAvgTP"
        bp.add(tpFltTpButton);
        bp.add(new MyButton(this, "Save", "", "Save"));
        bp.add(new MyButton(this, "Help", "", "Help"));
        bp.add(new MyButton(this, "Back", "", "Back"));
        add(bp, BorderLayout.SOUTH);
        //open recent data file
        if(AutoCorrData.newDataAvailable){
            openRecentData();
        }
    }
    /**
     *
     */
    private void openRecentData(){
        final MyWaitDialog waitDialog = new MyWaitDialog(MyData.mainFrame, 400, 100);
        new Thread(){
            @Override
            public void run(){
                try{
                    waitDialog.setMessage("Populating graph..");
                    dataFile = AutoCorrData.getRecentDataFile();
                    paramsFile = AutoCorrData.getRecentParamFile();
                    showFileData();
                    waitDialog.setVisible(false);
                }catch(Exception ex){
                    waitDialog.setVisible(false);
                    LogReports.logError(ex);
                    MyData.showErrorMessage(ex);
                }
            }
        }.start();
        waitDialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Browse")){
            try{
                dataFile = MyData.chooseFile(dataFile);
                if(dataFile==null) return;
                paramsFile = AutoCorrData.getParamFile(dataFile);
                showFileData();
            }catch(Exception ex){
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
        }else if(e.getActionCommand().equals("ZoomIN")){
            zoomTo();
        }else if(e.getActionCommand().equals("ZoomOut")){
            zoomOut();
        }else if(e.getActionCommand().equals("ShowData")){
            showData();
        }else if(e.getActionCommand().equals("ShowGraph")){
            showGraph();
        }else if(e.getActionCommand().equals("SetTh")){
            setThreshold();
        }else if(e.getActionCommand().equals("ShowTPGraph")){
            calculateTimePeriod();
        }else if(e.getActionCommand().equals("FiltAvgTP")){
            calculateFiltTimePeriod();
        }else if(e.getActionCommand().equals("Save")){
            saveResults();
        }else if(e.getActionCommand().equals("Help")){
            MyData.showInfoMessage(help);
        }else if(e.getActionCommand().equals("Back")){
            parent.showOptionPane();
        }
    }
    /**
     *
     */
    private void setThreshold() {
        try{
            autoCorrParams.setSelectedSetOfthTP(comboSetThTP.getSelectedIndex());
            autoCorrParams.setTh(Float.parseFloat(textThVal.getText()));
            autoCorrParams.refreshData();
            if(isTp){
                //draw original graph back
                isTp = false;
                thSet = true;
                drawGraph(allPoints);
                //show original data back
                tm.setData(allPoints);
            }else{
                lineGraph.setThresholdVal(autoCorrParams.getTh());
                thSet = true;
            }
            tpFltTpButton.setCommandText("ShowTPGraph", "ShowTPGraph");
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }
    /**
     *
     */
    private void calculateTimePeriod(){
        try{
            if(!thSet){
                MyData.showInfoMessage("Please set threshold value first!");
                return;
            }
            if(allPoints==null || allPoints.length<2){
                throw new Exception("NotEnoughPoints null or < 2");
            }
            //read ThVal
            int numThPoints = 0;
            for(int i=0;i<allPoints.length;i++){
                if(allPoints[i].y > autoCorrParams.getTh()){
                     numThPoints++;
                }
            }

            DataPoint[] thresholdPoints = new DataPoint[numThPoints];
            int j=0;
            for(int i=0;i<allPoints.length;i++){
                if(allPoints[i].y>autoCorrParams.getTh()){
                    thresholdPoints[j++] = allPoints[i];
                }
            }
            //TODO AutoCorr TimePeriodCalculation
            long[] diff = new long[numThPoints-1];
            tpPoints = new DataPoint[numThPoints-1];
            float fs = adcAcqParams.getFS();
            double sumTp = 0.0;
            for(int i=0;i<numThPoints-1;i++){
                diff[i] = thresholdPoints[i+1].x-thresholdPoints[i].x;
                tpPoints[i] = new DataPoint();
                tpPoints[i].sno = i;
                tpPoints[i].x = thresholdPoints[i+1].sno;//sample number
                tpPoints[i].y = (float)((double)diff[i]/fs);
                //check few results
                //if(sno<5)MyData.showInfoMessage("diff["+sno+"]="+diff[sno]+", fs="+fs+", tp["+sno+"]="+tpPoints[sno].y);
                sumTp += tpPoints[i].y;
            }

            tpStart = 0;
            tpEnd = tpPoints.length;
            
            autoCorrParams.setAvgTP((float)(sumTp/tpPoints.length));

            //show graph
            isTp = true;
            drawGraph(tpPoints);

            //create JTable
            tm.setData(tpPoints);
            autoCorrParams.refreshData();
            tpFltTpButton.setCommandText("FiltAvgTP", "FiltAvgTP");
            //MyData.showInfoMessage("Averageg Time Period = "+autoCorrParams.avgTimePeriod);
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }

    /**
     *
     */
    private void calculateFiltTimePeriod(){
        try{
            if(!isTp || tpEnd<=tpStart){
                MyData.showInfoMessage("Please filter time period graph first!");
                return;
            }
            //TODO Filtered Avg Time Perriod Calculation
            double sumTp = 0.0;
            for(int i=tpStart;i<tpEnd;i++){
                sumTp += tpPoints[i].y;
            }

            float fltAvgTP = (float)(sumTp/(tpEnd-tpStart));
            int fltSignalFreq = (int)(1.0/fltAvgTP);
            MyData.showInfoMessage(
                      "Filtered Avg TimePeriod   = "+fltAvgTP+"\n"
                    + "Filtered Avg Signal Freq. = "+fltSignalFreq);
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }

    /**
     *
     */
    private void saveResults() {
        try{
            autoCorrParams.write(paramsFile);
            MyData.showInfoMessage("Result saved successfully.");
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }

    /**
     * 
     */
    private void showFileData() {
        try{
            if(dataFile == null || !dataFile.exists()){
                return;
            }
            autoCorrParams.setSelectedSetOfthTP(comboSetThTP.getSelectedIndex());
            allPoints = MyData.readDataPoints(dataFile);

            //read params and set min, max, avg and save also
            autoCorrParams.read(paramsFile);
            autoCorrParams.calculateMinMaxAvg(allPoints);
            autoCorrParams.write(paramsFile);
            adcParamFile = adcAcqParams.getParamFile(paramsFile.getParentFile().getParent());
            adcAcqParams.read(adcParamFile);

            //show params
            autoCorrParams.setData(adcAcqParams);
     
            //set graph limits
            textStartX.setText(0+"");
            textEndX.setText(allPoints.length+"");

            //show threshold val
            textThVal.setText(autoCorrParams.getTh()+"");

            //draw graph
            isTp = false;
            thSet = false;
            drawGraph(allPoints);

            //create JTable
            tm.setData(allPoints);

            //set Title
            titleLabel.setText(title+" : "+MyData.getFileInfo(dataFile));
            tpFltTpButton.setCommandText("ShowTPGraph", "ShowTPGraph");
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }
    /**
     *
     */
    private void zoomTo() {
        try{
            int zoomFrom = Integer.parseInt(textStartX.getText());
            int zoomTo = Integer.parseInt(textEndX.getText());
            int zoomLen = zoomTo - zoomFrom + 1;
            //show zoomed graph data or tp
            if(!isTp){
                if(zoomFrom < 0 || zoomTo > (allPoints.length-1) || zoomLen < 2){
                    throw new Exception("ZOOM_IN RANGE INVALID");
                }
                zoomPoints = new DataPoint[zoomLen];
                for (int i = 0, j=zoomFrom;  i<zoomPoints.length; i++, j++) {
                    zoomPoints[i] = allPoints[j];
                }
                drawGraph(zoomPoints);
                //set For Next Zoom
                if(zoomTo<=(allPoints.length-3)){
                    textStartX.setText((zoomTo+1)+"");
                    zoomTo = zoomTo+zoomLen;
                    if(zoomTo>(allPoints.length-1)) zoomTo = allPoints.length-1;
                    textEndX.setText(zoomTo+"");
                }
                //create JTable
                tm.setData(zoomPoints);
            }else{
                if(zoomFrom < 0 || zoomTo > (allPoints.length-1) || zoomLen < 2){
                    throw new Exception("ZOOM_IN RANGE INVALID");
                }
                zoomPoints = new DataPoint[zoomLen];
                tpStart = zoomFrom;
                tpEnd = zoomTo+1;
                for (int i = 0, j=zoomFrom;  i<zoomPoints.length; i++, j++) {
                    zoomPoints[i] = tpPoints[j];
                }
                drawGraph(zoomPoints);
                //set For Next Zoom
                if(zoomTo<=(tpPoints.length-3)){
                    textStartX.setText((zoomTo+1)+"");
                    zoomTo = zoomTo+zoomLen;
                    if(zoomTo>(tpPoints.length-1)) zoomTo = tpPoints.length-1;
                    textEndX.setText(zoomTo+"");
                }
                //create JTable
                tm.setData(zoomPoints);
            }
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }
    /**
     *
     */
    private void zoomOut() {
        try{
            if(isTp){
                //set graph limits
                textStartX.setText(0+"");
                textEndX.setText(tpPoints.length+"");
                //draw graph
                drawGraph(tpPoints);
                //create JTable
                tm.setData(tpPoints);
            }else{
                //set graph limits
                textStartX.setText(0+"");
                textEndX.setText(allPoints.length+"");
                //draw graph
                drawGraph(allPoints);
                //create JTable
                tm.setData(allPoints);
            }
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }
    /**
     * 
     * @param points
     * @throws Exception
     */
    private void drawGraph(DataPoint[] points) throws Exception{
        //set graph limits
        textStartX.setText(points[0].sno+"");
        textEndX.setText(points[points.length-1].sno+"");
        //create graph
        if(isTp){
            lineGraph = new LineGraph(points, "Peak Sr. No.", "Time Periods");
            lineGraph.showSNo=true;
            lineGraph.lineColor = Color.RED;
            lineGraph.setAvgTimePeriod(autoCorrParams.getAvgTP());
        }else{
            lineGraph = new LineGraph(points, "Sample No.", "Auto Correlation Data");
            lineGraph.showSNo=true;
            if(thSet){
                lineGraph.setThresholdVal(autoCorrParams.getTh());
            }
        }
        //add graph in the panel
        showGraph();
    }
    /**
     * 
     */
    private void showData() {
        if(allPoints == null) return;
        dataGraphSwitchButton.setCommandTextIcon("ShowGraph", "", "Graph");
        //dataGraphSwitchButton.setText("Graph");
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
        dataGraphSwitchButton.setCommandTextIcon("ShowData", "", "Data");
       // dataGraphSwitchButton.setText("Data");
       // dataGraphSwitchButton.setActionCommand("ShowData");
        middlePanel.removeAll();
        middlePanel.add(lineGraph.getScrollPane());
        middlePanel.validate();
        middlePanel.repaint();
    }

    //AutoCorrParams
    private AutoCorrParams autoCorrParams = new AutoCorrParams();
    private ADCAcqParams adcAcqParams = new ADCAcqParams();
    private boolean thSet = false;
    private boolean isTp = false;
}