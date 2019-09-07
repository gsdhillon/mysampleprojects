package cpnds.gui.FFTComputation;

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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
/**
 * @type     : Java Class 
 * @name     : DisplayDataInFFT
 * @file     : DisplayDataInFFT.java
 * @created  : May 29, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class DisplayDataInFFT extends MyPanel implements ActionListener{
    private LineGraph lineGraph;
    private File dataFile = null;
    private File paramsFile = null;
    private String title = "Display Data in form of FFT";
    private MyLabel titleLabel;
    private String labelOfDataY = "FFT Component";
    private String labelOfDataX = "Frequency";
    private DataPoint[] allPoints;
    private DataPoint[] zoomPoints;
    private MyTextField textStartX = new MyTextField(8);
    private MyTextField textEndX = new MyTextField(8);
    private DataTableModel tm = new DataTableModel();
    private MyTable dataTable = new MyTable(tm);
    private JScrollPane sp = new JScrollPane(dataTable);

    private MyPanel middlePanel = new MyPanel(new GridLayout(1,1));
    private MyComboBox comboZooming;
    private MyButton dataGraphSwitchButton;
    private AnalyzeFFTDataPanel parent;

    private String help = LineGraph.getHelp();
    /**
     * Constructor
     */
    public DisplayDataInFFT(AnalyzeFFTDataPanel parent){
        super(new BorderLayout());
        this.parent = parent;
        int rowHeight = 30;
        int width = 1000;

        //Top panel
        MyPanel topPanel = new MyPanel(new BorderLayout());
        topPanel.setDefaultBG();
        //add Title
        titleLabel = new MyLabel(MyLabel.TYPE_LABEL_HEADING, title, JLabel.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        //parameters panel
        topPanel.add(fftParams.sp, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        //add middle panel
        
        add(middlePanel, BorderLayout.CENTER);

        //buttons panel
        MyPanel bp = MyButton.getButtonPanel();
        MyButton  b = new MyButton(this, "Browse", "", "Browse");
        bp.add(b);
        bp.add(new JLabel("  "));

        //Simple Zooming
        bp.add(new MyLabel(MyLabel.TYPE_LABEL, "From:"));
        bp.add(textStartX);
        bp.add(new MyLabel(MyLabel.TYPE_LABEL, " To:"));
        bp.add(textEndX);
        b = new MyButton(this, "ZoomIN1", "", "Zoom");
        bp.add(b);

        //decade Zooming
        bp.add(new MyLabel(MyLabel.TYPE_LABEL, "Decade:"));
        comboZooming = new MyComboBox();
        comboZooming.setPreferredSize(new Dimension(200, 22));
        bp.add(comboZooming);

        b = new MyButton(this, "ZoomIN", "", "Zoom");
        bp.add(b);

        b = new MyButton(this, "ZoomOut", "","ZoomOut");
        bp.add(b);
        
        bp.add(new JLabel(" "));
        dataGraphSwitchButton = new MyButton(this, "ShowData", "ShowData", "Data");
        bp.add(dataGraphSwitchButton);

        bp.add(new MyButton(this, "Help", "", "Help"));

        b = new MyButton(this, "Back", "", "Back");
        bp.add(b);
        add(bp, BorderLayout.SOUTH);

        //open recent data file
        if(FFTData.newDataAvailable){
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
                    dataFile = FFTData.getRecentDataFile();
                    paramsFile = FFTData.getRecentParamFile();
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
                paramsFile = FFTData.getParamFile(dataFile);
                showFileData();
            }catch(Exception ex){
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
        }else if(e.getActionCommand().equals("ZoomIN")){
            zoomTo();
        }else if(e.getActionCommand().equals("ZoomIN1")){
            zoomTo1();
        }else if(e.getActionCommand().equals("ZoomOut")){
            zoomOut();
        }else if(e.getActionCommand().equals("ShowData")){
            showData();
        }else if(e.getActionCommand().equals("ShowGraph")){
            showGraph();
        }else if(e.getActionCommand().equals("Help")){
            MyData.showInfoMessage(help);
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
            //First read adc params
            File adcParamFile = adcAcqParams.getParamFile(paramsFile.getParentFile().getParent());
            adcAcqParams.read(adcParamFile);
            //read fft params
            fftParams.read(paramsFile);
            //read data from data file and make X,Y points and calc RMS also
            allPoints = FFTData.readData(dataFile,adcAcqParams,fftParams, FFTData.DISPLAY_FFT);
            //Cal MIN max and writing back removed on 11-08-11
            fftParams.setData(adcAcqParams);
            fftParams.setDisplayType(FFTData.DISPLAY_FFT);

            //make decades
            fftParams.fillDecades(comboZooming);

            //draw graph
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
    private void zoomTo() {
        try{
            int index = comboZooming.getSelectedIndex();
            if(index==0){
                zoomOut();
                return;
            }
            Decade decade = fftParams.decades[index-1];
            //settings selected decade will show params of this decade
            fftParams.setSelectedDecade(index-1);

            if(decade.endIndex == -1){
                MyData.showInfoMessage("No point in this decade!");
                if(index < fftParams.numDecades){
                    comboZooming.setSelectedIndex(index+1);
                }
                return;
            }
            int zoomFrom = decade.startIndex;
            int zoomTo = decade.endIndex;
            int zoomLen = zoomTo - zoomFrom + 1;
            if(zoomFrom < 0 || zoomTo > (allPoints.length-1) || zoomLen < 1){
                throw new Exception("ZOOM_IN RANGE INVALID "+zoomFrom+" "+zoomTo);
            }
            zoomPoints = new DataPoint[zoomLen];
            for (int i = 0, j=zoomFrom;  i<zoomPoints.length; i++, j++) {
                zoomPoints[i] = allPoints[j];
            }
            //show graph
            drawGraph(zoomPoints);
            if(index<fftParams.numDecades){
                comboZooming.setSelectedIndex(index+1);
            }
            //create JTable
            tm.setData(zoomPoints);
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }

    /**
     * Simple Zooming
     */
    private void zoomTo1() {
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
            fftParams.setSelectedDecade(-1);
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
            comboZooming.setSelectedIndex(0);
            textStartX.setText(allPoints[0].sno+"");
            textEndX.setText(allPoints[allPoints.length-1].sno+"");

            fftParams.setSelectedDecade(-1);
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
        //create graph
        lineGraph = new LineGraph(points, LineGraph.TYPE_PICK_MAX, labelOfDataX, labelOfDataY);
        lineGraph.showSNo = true;
        //add graph in the panel
        showGraph();
        comboZooming.setSelectedIndex(0);
        textStartX.setText(points[0].sno+"");
        textEndX.setText(points[points.length-1].sno+"");
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
       // dataGraphSwitchButton.setActionCommand("ShowData");
        middlePanel.removeAll();
        middlePanel.add(lineGraph.getScrollPane());
        middlePanel.validate();
        middlePanel.repaint();
    }
    

    //FFTParams
    FFTParams fftParams = new FFTParams();
    ADCAcqParams adcAcqParams = new ADCAcqParams();

}