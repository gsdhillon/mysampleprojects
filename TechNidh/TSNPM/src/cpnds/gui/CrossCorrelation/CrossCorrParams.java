package cpnds.gui.CrossCorrelation;

import cpnds.MyGraphs.DataPoint;
import cpnds.data.MyData;
import cpnds.gui.ADCAcquisition.ADCAcqParams;
import gui.MyLabel;
import gui.MyTable;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JScrollPane;

/**
 * @type     : Java Class
 * @name     : CrossCorrParams
 * @file     : CrossCorrParams.java
 * @created  : May 15, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class CrossCorrParams {
    public static final int DECADE_YES = 1;
    public static final int DECADE_NO = 0;
    public final byte processID = 74;
    public int uid=0;
    public File paramFile;
    public byte resultNo;
    public byte noOfPoints=0;
    public byte decadeOption;
    public float startFreq;
    public float endFreq;
    //following results are saved with params file
    public int minValX = -1;//newG
    public float minVal = 0.0f;
    public int maxValX = -1;//newG
    public float maxVal = 0.0f;
    public float avgVal = 0.0f;
    public float ccFreq = 0.0f;

    public static final int noOfThTPSets = 5;//newG
    public float[] thresholdVal = new float[noOfThTPSets];//newG
    public float[] avgTimePeriod = new float[noOfThTPSets];//newG

    public static String[] setNosThTP ={"1","2","3","4","5"};

    private final int LEN = 80;//newG

    private int selectedSetOfthTP = 0;

    private ADCAcqParams[] adcAcqParams = new ADCAcqParams[1];
    private CrossCorrCommonParams[] crossCorrCommonParams = new CrossCorrCommonParams[1];
    private CrossCorrParams[] crossCorrParams = new CrossCorrParams[1];
    private CrossCorrResultsTM paramsTM = new CrossCorrResultsTM();
    private MyTable paramsTable = new MyTable(paramsTM);
    public JScrollPane sp = new JScrollPane(paramsTable);
    /**
     *
     */
    public CrossCorrParams(){
        for(int i=0;i<noOfThTPSets;i++){
            thresholdVal[i] = 0.0f;
            avgTimePeriod[i] = 0.0f;
        }

        crossCorrParams[0] = this;
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
     * @param crossCorComParams
     */
    public void setData(ADCAcqParams adcParams, CrossCorrCommonParams crossCorComParams){
        adcAcqParams[0] = adcParams;
        crossCorrCommonParams[0] = crossCorComParams;
        paramsTM.setData(adcAcqParams, crossCorrParams, crossCorrCommonParams);
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
        params[5] = resultNo;
        params[6] = noOfPoints;
        params[7] = decadeOption;
        MyData.putFloatBytesToBuffer(startFreq, params, 8);
        MyData.putFloatBytesToBuffer(endFreq, params, 12);
        MyData.putIntBytesToBuffer(minValX, params, 16);//newG
        MyData.putFloatBytesToBuffer(minVal, params, 20);
        MyData.putIntBytesToBuffer(maxValX, params, 24);//newG
        MyData.putFloatBytesToBuffer(maxVal, params, 28);
        MyData.putFloatBytesToBuffer(avgVal, params, 32);
        MyData.putFloatBytesToBuffer(ccFreq, params, 36);
        int off = 40;
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
            throw new Exception("INVALID CROSS_CORR_PARAM FILE PID = "+buffer[0]);
        }
        if(buffer.length != LEN){//newG
            throw new Exception("Old version of CROSS_CORR_PARAMS file");
        }
        uid = MyData.getInt(buffer, 1);
        resultNo = buffer[5];
        noOfPoints = buffer[6];
        decadeOption = buffer[7];
        startFreq = MyData.getFloat(buffer, 8);
        endFreq = MyData.getFloat(buffer, 12);
        minValX = MyData.getInt(buffer, 16);//newG
        minVal = MyData.getFloat(buffer, 20);
        maxValX = MyData.getInt(buffer, 24);//newG
        maxVal = MyData.getFloat(buffer, 28);
        avgVal = MyData.getFloat(buffer, 32);
        ccFreq = MyData.getFloat(buffer, 36);
        int off = 40;
        for(int i=0;i<noOfThTPSets;i++){
            thresholdVal[i] = MyData.getFloat(buffer, off);off+=4;
            avgTimePeriod[i] = MyData.getFloat(buffer, off);off+=4;
        }
        paramFile = file;
    }
    /**
     *
     * @param sno
     */
    public void setSelectedSetOfThTP(int i) throws Exception{
        if(i<0 || i >= noOfThTPSets){
            throw new Exception("SetNoOfThTP Invalid [0-4 Allowed] got "+i);
        }
        selectedSetOfthTP = i;
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
    /**
     *
     */
    public void calculateCrossCorrFreq(){
        //calculate parameters
        if(decadeOption==DECADE_NO){
            ccFreq = (float)(startFreq  + (resultNo-1)* ((endFreq - startFreq )/9.0));
        }else{
            ccFreq = (float)Math.pow(10,(resultNo-1));
        }
    }
    /**
     * 
     * @return
     */
    public String getDecadeOption(){
        if(decadeOption==DECADE_YES){
            return "YES";
        }else{
            return "NO";
        }
    }
    /**
     *
     * @return
     */
    public int getNoOfPoints(){
        return (int)Math.pow(2, noOfPoints);
    }
    /**
     *
     * @param ccFreqLabel
     */
    public void show(MyLabel ccFreqLabel) {
        //set CFreq Field
        if(ccFreq<10000){
            ccFreqLabel.setText(Float.toString(ccFreq)+" Hz");
        }else if(ccFreq < 10000000){
            ccFreqLabel.setText(Float.toString(ccFreq/1000)+"KHz");
        }else{
            ccFreqLabel.setText(Float.toString(ccFreq/1000000)+"MHz");
        }
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