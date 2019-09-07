package cpnds.gui.ADCAcquisition;

import cpnds.MyGraphs.LineGraph;
import cpnds.data.LogReports;
import cpnds.data.MyData;
import cpnds.MyGraphs.DataPoint;
import cpnds.data.DataTableModel;
import gui.MyPanel;
import gui.MyButton;
import gui.MyLabel;
import gui.MyTable;
import gui.MyTextField;
import gui.MyWaitDialog;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * @type     : Java Class
 * @name     : AnalyzeADCAcquisition
 * @file     : AnalyzeADCAcquisition.java
 * @created  : Jan 13, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class AnalyzeADCAcquisition extends MyPanel implements ActionListener{
    private LineGraph lineGraph;
    private File dataFile = null;
    private File paramsFile = null;
    private String title = "ACQUISITION, GUI ANALYSIS AND STORAGE OF THE DATA";
    private MyLabel titleLabel;
    private String labelOfDataY = "ADC Value Obtained";
    private String labelOfDataX = "Sample No.";
    private DataPoint[] allPoints;
    private DataPoint[] zoomPoints;

    private DataTableModel tm = new DataTableModel();
    private MyTable dataTable = new MyTable(tm);
    private JScrollPane sp = new JScrollPane(dataTable);
    //adcAcqParams
    private ADCAcqParams adcAcqParams = new ADCAcqParams();
    private MyPanel middlePanel = new MyPanel(new GridLayout(1,1));
    private MyTextField textStartX = new MyTextField(8);
    private MyTextField textEndX = new MyTextField(8);
    private MyButton dataGraphSwitchButton;
    private ADCAcquisitionOptionsTab parent;
    private String help = LineGraph.getHelp();

    /**
     * Constructor
     */
    public AnalyzeADCAcquisition(ADCAcquisitionOptionsTab parent){
        super(new BorderLayout());
        this.parent = parent;
        int width = 1000;
        
        //Top panel
        MyPanel topPanel = new MyPanel(new BorderLayout());
        topPanel.setDefaultBG();
        //add Title
        titleLabel = new MyLabel(MyLabel.TYPE_LABEL_HEADING, title, JLabel.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        //parameters panel
        topPanel.add(adcAcqParams.sp, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        //add middle panel
        add(middlePanel, BorderLayout.CENTER);

        //buttons panel
        MyPanel bp = MyButton.getButtonPanel();

        MyButton  b = new MyButton(this, "Browse", "", "Browse");
        bp.add(b);
        bp.add(new JLabel("   "));
        bp.add(new MyLabel(MyLabel.TYPE_LABEL, "From:"));
        bp.add(textStartX);
        bp.add(new MyLabel(MyLabel.TYPE_LABEL, " To:"));
        bp.add(textEndX);
        b = new MyButton(this, "ZoomIN", "", "Zoom");
        bp.add(b);
        b = new MyButton(this, "ZoomOut", "" ,"ZoomOut");
        bp.add(b);
        bp.add(new JLabel("   "));
        dataGraphSwitchButton = new MyButton(this, "ShowData", "ShowData", "Data");
        bp.add(dataGraphSwitchButton);
        bp.add(new MyButton(this, "Delete", "Delete", "Delete"));
        bp.add(new MyButton(this, "Help", "", "Help"));
        bp.add(new MyButton(this, "ShowInputWave", "Show Input Wave"));
        bp.add(new MyButton(this, "Back", "", "Back"));
        add(bp, BorderLayout.SOUTH);
        if(ADCAcquisitionData.newDataAvailable){
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
                    dataFile = ADCAcquisitionData.getRecentDataFile();
                    paramsFile = ADCAcquisitionData.getRecentParamFile();
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
                paramsFile = ADCAcquisitionData.getParamFile(dataFile);
                showFileData();
            }catch(Exception ex){
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
        }else if(e.getActionCommand().equals("ZoomIN")){
            zoomTO();
        }else if(e.getActionCommand().equals("ZoomOut")){
            zoomOut();
        }else if(e.getActionCommand().equals("ShowData")){
            showData();
        }else if(e.getActionCommand().equals("ShowGraph")){
            showGraph();
        }else if(e.getActionCommand().equals("Delete")){
            try{
                if(!MyData.showConfirm("Do you realy want to delete this data")){
                    return;
                }
                File expDir = dataFile.getParentFile().getParentFile();
                ADCAcquisitionData.deleteData(expDir);
                MyData.showInfoMessage("Experiment "+expDir.getParentFile().getName()+"/"+expDir.getName()+" deleted successfully.");
            }catch(Exception ex){
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
        }else if(e.getActionCommand().equals("Help")){
            MyData.showInfoMessage(help);
        }else if(e.getActionCommand().equals("ShowInputWave")){
            if(adcAcqParams.uid == 0){
                MyData.showInfoMessage("Open an experiment first!");
            }else{
                new InputWaveDialog(adcAcqParams).setVisible(true);
            }
        }else if(e.getActionCommand().equals("Back")){
            parent.showOptionPane();
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
            allPoints = MyData.readDataPoints(dataFile);
            //read params and set min, max, avg and save also
            adcAcqParams.read(paramsFile);
            adcAcqParams.calculateMinMaxAvg(allPoints);
            adcAcqParams.write(paramsFile);

            /*
            minValLabel.setText(Float.toString(adcAcqParams.minVal));
            avgValLabel.setText(Float.toString(adcAcqParams.avgVal));
            maxValLabel.setText(Float.toString(adcAcqParams.maxVal));
            inputPowerLabel.setText(Float.toString(adcAcqParams.inputPower));
            snapShotTimeLabel.setText(Float.toString(adcAcqParams.getSnapshotTime())+" Sec");
            */
            //show graph
            drawGraph(allPoints);
 
            //create JTable
            tm.setData(allPoints);

            //set Title
            titleLabel.setText(title+" : "+MyData.getFileInfo(dataFile));
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }

    /**
     *
     */
    private void zoomTO() {
        try{
            int zoomFrom = Integer.parseInt(textStartX.getText());
            int zoomTo = Integer.parseInt(textEndX.getText());
            int zoomLen = zoomTo - zoomFrom + 1;
            if(zoomFrom < 0 || zoomTo > (allPoints.length-1) || zoomLen < 2){
                throw new Exception("ZOOM_IN RANGE INVALID");
            }
            
            zoomPoints = new DataPoint[zoomLen];
            for (int i = 0, j=zoomFrom;  i<zoomPoints.length; i++, j++) {
                zoomPoints[i] = allPoints[j];
            }

            //show graph
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
            //draw graph
            drawGraph(allPoints);

            //create JTable
            tm.setData(allPoints);
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
        lineGraph = new LineGraph(points, labelOfDataX, labelOfDataY);
        lineGraph.showSNo=true;
        //add graph in the panel
        middlePanel.removeAll();
        middlePanel.add(lineGraph.getScrollPane());
        middlePanel.validate();
        middlePanel.repaint();
        dataGraphSwitchButton.setCommandTextIcon("ShowData", "ShowData", "Data");
      //  dataGraphSwitchButton.setText("ShowData");
      //  dataGraphSwitchButton.setActionCommand("ShowData");
    }
    /**
     *
     */
    private void showData() {
        if(allPoints == null) return;
        dataGraphSwitchButton.setCommandTextIcon("ShowGraph", "ShowGraph", "Graph");
       // dataGraphSwitchButton.setText("ShowGraph");
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
}