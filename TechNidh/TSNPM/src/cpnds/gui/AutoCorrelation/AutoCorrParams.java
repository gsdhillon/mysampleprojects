package cpnds.gui.AutoCorrelation;

import cpnds.MyGraphs.DataPoint;
import cpnds.data.MyData;
import cpnds.gui.ADCAcquisition.ADCAcqParams;
import gui.MyTable;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JScrollPane;

/**
 * @type     : Java Class
 * @name     : AutoCorrParams
 * @file     : AutoCorrParams.java
 * @created  : May 15, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class AutoCorrParams {
    public final byte processID = 73;
    public int uid=0;
    public File paramFile;
    public byte noOfPoints=0;
    //following results are saved with params file
    public int minValX = -1;//newG
    public float minVal = 0.0f;
    public int maxValX = -1;//newG
    public float maxVal = 0.0f;
    public float avgVal = 0.0f;

    public static final int noOfThTPSets = 5;//newG
    public float[] thresholdVal = new float[noOfThTPSets];//newG
    public float[] avgTimePeriod = new float[noOfThTPSets];//newG

    public static String[] setNosThTP ={"1","2","3","4","5"};
    
    private final int LEN = 66;//newG

    private int selectedSetOfthTP = 0;

    private ADCAcqParams[] adcAcqParams = new ADCAcqParams[1];
    private AutoCorrParams[] autoCorrParams = new AutoCorrParams[1];
    private AutoCorrResultsTM paramsTM = new AutoCorrResultsTM();
    private MyTable paramsTable = new MyTable(paramsTM);
    public JScrollPane sp = new JScrollPane(paramsTable);
    /**
     * 
     */
    public AutoCorrParams(){
        for(int i=0;i<noOfThTPSets;i++){
            thresholdVal[i] = 0.0f;
            avgTimePeriod[i] = 0.0f;
        }

        autoCorrParams[0] = this;
        paramsTable.setColumnSelectionAllowed(true);
        paramsTable.setRowHeight(23);
        sp.setPreferredSize(new Dimension(1000, 45));
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    /**
     *
     * @return
     */
    public int getSelectedThTPNo() {
        return selectedSetOfthTP;
    }
    /**
     *
     * @param adcParams
     */
    public void setData(ADCAcqParams adcParams){
        adcAcqParams[0] = adcParams;
        paramsTM.setData(adcAcqParams, autoCorrParams);
    }
    /**
     * 
     * @param sno
     */
    public void setSelectedSetOfthTP(int i) throws Exception{
        if(i<0 || i >= noOfThTPSets){
            throw new Exception("SetNoOfThTP Invalid [0-4 Allowed] got "+i);
        }
        selectedSetOfthTP = i;
        paramsTM.fireTableDataChanged();
    }
    /**
     *
     * @param th
     */
    public void setTh(float th){
        thresholdVal[selectedSetOfthTP] = th;
    }

    /**
     *
     * @param th
     */
    public void setAvgTP(float avgTP){
        avgTimePeriod[selectedSetOfthTP] = avgTP;
    }

    /**
     *
     * @return
     */
    public float getTh(){
        return thresholdVal[selectedSetOfthTP];
    }
    /**
     *
     * @return
     */
    public float getAvgTP(){
        return avgTimePeriod[selectedSetOfthTP];
    }

    /**
     *
     * @return
     */
    public int getSignalFreq(){
        if(avgTimePeriod[selectedSetOfthTP]>0){
            return (int)(1.0/avgTimePeriod[selectedSetOfthTP]);
        }else{
            return 0;
        }
    }

    /**
     *
     */
    public void refreshData(){
        paramsTM.fireTableDataChanged();
    }

    /**
     * 
     * @param file
     * @throws Exception
     */
    public void write(File file) throws Exception{
        //writing parameters in the param file
        byte[] params = new byte[LEN];
        params[0] = processID;
        MyData.putIntBytesToBuffer(uid, params, 1);
        params[5] = noOfPoints;
        MyData.putIntBytesToBuffer(minValX, params, 6);//newG
        MyData.putFloatBytesToBuffer(minVal, params, 10);
        MyData.putIntBytesToBuffer(maxValX, params, 14);//newG
        MyData.putFloatBytesToBuffer(maxVal, params, 18);
        MyData.putFloatBytesToBuffer(avgVal, params, 22);
        int off = 26;
        for(int i=0;i<noOfThTPSets;i++){
            MyData.putFloatBytesToBuffer(thresholdVal[i], params, off);off+=4;
            MyData.putFloatBytesToBuffer(avgTimePeriod[i], params, off);off+=4;
        }
        MyData.writeParameterFile(file, params, 0, LEN);
    }
    /**
     *
     * @param file
     * @throws Exception
     */
    public void read(File file) throws Exception{
        //read input params from file
        byte[] buffer = MyData.readParameterFile(file);
        if(buffer[0] != processID){
            throw new Exception("INVALID AUTO_CORR_PARAM FILE PID = "+buffer[0]);
        }
        if(buffer.length != LEN){//newG
            throw new Exception("Old version of AUTO_CORR_PARAMS file");
        }
        uid = MyData.getInt(buffer, 1);
        noOfPoints = buffer[5];
        minValX = MyData.getInt(buffer, 6);//newG
        minVal = MyData.getFloat(buffer, 10);
        maxValX = MyData.getInt(buffer, 14);//newG
        maxVal = MyData.getFloat(buffer, 18);
        avgVal = MyData.getFloat(buffer, 22);
        int off = 26;
        for(int i=0;i<noOfThTPSets;i++){
            thresholdVal[i] = MyData.getFloat(buffer, off);off+=4;
            avgTimePeriod[i] = MyData.getFloat(buffer, off);off+=4;
        }
        paramFile = file;
    }
    /**
     *
     * @return
     */
    public int getNoOfPoints(){
        return (int)Math.pow(2, noOfPoints);
    }
    /**
     * calculate min max and avg value
     * save into the file also
     * @param points
     * @throws Exception
     */
    public void calculateMinMaxAvg(DataPoint[] points) throws Exception{
        //calculate MIN MAX and AVG
        minVal = Float.MAX_VALUE;
        maxVal = - Float.MAX_VALUE;
        double sum = 0;
        for (int i = 0; i < points.length; i++) {
            if(minVal > points[i].y){
                minVal = points[i].y;
                minValX = i;
            }
            if(maxVal < points[i].y){
                maxVal = points[i].y;
                maxValX = i;
            }
            sum += points[i].y;
        }
        avgVal = (float)(sum/points.length);
    }

    public String getDate() {
        try{
            String s = paramFile.getParentFile().getParentFile().getParentFile().getName();
            return s.substring(5);
        }catch(Exception e){
            return "null";
        }
    }

    public String getTime() {
        try{
            String s = paramFile.getParentFile().getParentFile().getName();
            return s.substring(5);
        }catch(Exception e){
            return "null";
        }
    }

}
