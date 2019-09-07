package cpnds.gui.FFTComputation;

import cpnds.data.MyData;
import cpnds.gui.ADCAcquisition.ADCAcqParams;
import gui.MyComboBox;
import gui.MyTable;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JScrollPane;

/**
 * @type     : Java Class
 * @name     : FFTParams
 * @file     : FFTParams.java
 * @created  : May 15, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class FFTParams {
    public final byte processID = 72;
    public int uid=0;
    public File paramFile;
    public byte noOfPoints=0;
    public byte filteringMethodIndex;

    public float dcComponent_FFT = 0.0f;//newG
    public int minValX_FFT = -1;//newG
    public float minVal_FFT = 0.0f;
    public int maxValX_FFT = -1;//newG
    public float maxVal_FFT = 0.0f;
    public float avgVal_FFT = 0.0f;

    public float dcComponent_PSD = 0.0f;//newG
    public int minValX_PSD = -1;//newG
    public float minVal_PSD = 0.0f;//newG
    public int maxValX_PSD = -1;//newG
    public float maxVal_PSD = 0.0f;//newG
    public float avgVal_PSD = 0.0f;//newG
    
    public float rmsVal = 0.0f;
    public float weightedFFT = 0.0f;
    public float weightedPSD = 0.0f;

    public int numDecades = 7;//actual no. will be calculated later
    public static final int MAX_DECADES = 7;//newG
    public Decade[] decades = new Decade[MAX_DECADES];//newG

    private static final int LEN = 71+Decade.LEN*MAX_DECADES;//newG

    public int selectedDecade = -1;//-1 for all

    private ADCAcqParams[] adcAcqParams = new ADCAcqParams[1];
    private FFTParams[] fftParams = new FFTParams[1];
    private FFTResultsTM paramsTM;
    private MyTable paramsTable;
    public JScrollPane sp;
    private byte displayType = FFTData.DISPLAY_FFT;
    /**
     * 
     */
    public FFTParams(){
        //initialize decades start and end freq
        int start = 0;
        for(int i=0;i<MAX_DECADES;i++){
            int end = start>0?10*start:10;
            decades[i] = new Decade(start, end);//0-10, 10-100 etc
            start = end;
        }
        paramsTM = new FFTResultsTM();
        paramsTable = new MyTable(paramsTM);
        fftParams[0] = this;
        paramsTable.setColumnSelectionAllowed(true);
        paramsTable.setRowHeight(23);
        sp = new JScrollPane(paramsTable);
        sp.setPreferredSize(new Dimension(1000, 45));
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    /**
     *
     * @param adcParams
     * @param displayType
     */
    public void setData(ADCAcqParams adcParams){
        adcAcqParams[0] = adcParams;
        paramsTM.setData(adcAcqParams, fftParams);
    }
    /**
     * 
     * @param displayType
     */
    public void setDisplayType(byte displayType){
        this.displayType = displayType;
        paramsTM.fireTableDataChanged();
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
        params[6] = filteringMethodIndex;
        MyData.putIntBytesToBuffer(minValX_FFT, params, 7);//newG
        MyData.putFloatBytesToBuffer(minVal_FFT, params, 11);
        MyData.putIntBytesToBuffer(maxValX_FFT, params, 15);//newG
        MyData.putFloatBytesToBuffer(maxVal_FFT, params, 19);
        MyData.putFloatBytesToBuffer(avgVal_FFT, params, 23);

        MyData.putIntBytesToBuffer(minValX_PSD, params, 27);//newG
        MyData.putFloatBytesToBuffer(minVal_PSD, params, 31);//newG
        MyData.putIntBytesToBuffer(maxValX_PSD, params, 35);//newG
        MyData.putFloatBytesToBuffer(maxVal_PSD, params, 39);//newG
        MyData.putFloatBytesToBuffer(avgVal_PSD, params, 43);//newG
        
        MyData.putFloatBytesToBuffer(rmsVal, params, 47);
        MyData.putFloatBytesToBuffer(weightedFFT, params, 51);
        MyData.putFloatBytesToBuffer(weightedPSD, params, 55);
        MyData.putFloatBytesToBuffer(dcComponent_FFT, params, 59);//newG
        MyData.putFloatBytesToBuffer(dcComponent_PSD, params, 63);//newG

        MyData.putIntBytesToBuffer(numDecades, params, 67);//newG
        //get data from all decades
        int offset = 71;
        for(int i=0;i<MAX_DECADES;i++){
            offset = decades[i].setData(params, offset);//newG
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
            throw new Exception("INVALID FFT_PARAM FILE PID = "+buffer[0]);
        }
        if(buffer.length != LEN){//newG
            throw new Exception("Old version of FFT_PARAMS file");
        }
        uid = MyData.getInt(buffer, 1);
        noOfPoints = buffer[5];
        filteringMethodIndex = buffer[6];
        minValX_FFT = MyData.getInt(buffer, 7);//newG
        minVal_FFT = MyData.getFloat(buffer, 11);
        maxValX_FFT = MyData.getInt(buffer, 15);//newG
        maxVal_FFT = MyData.getFloat(buffer, 19);
        avgVal_FFT = MyData.getFloat(buffer, 23);

        minValX_PSD = MyData.getInt(buffer, 27);//newG
        minVal_PSD = MyData.getFloat(buffer, 31);//newG
        maxValX_PSD = MyData.getInt(buffer, 35);//newG
        maxVal_PSD = MyData.getFloat(buffer, 39);//newG
        avgVal_PSD = MyData.getFloat(buffer, 43);//newG

        rmsVal = MyData.getFloat(buffer, 47);
        weightedFFT = MyData.getFloat(buffer, 51);
        weightedPSD = MyData.getFloat(buffer, 55);
        dcComponent_FFT = MyData.getFloat(buffer, 59);//newG
        dcComponent_PSD = MyData.getFloat(buffer, 63);//newG

        numDecades = MyData.getInt(buffer, 67);//newG
        //fill data in all decades
        int offset = 71;
        for(int i=0;i<MAX_DECADES;i++){
            offset = decades[i].getData(buffer, offset);//newG
        }

        paramFile = file;
    }
    /**
     * separate for FFT and PSD
     * calculate min max and avg value
     * save into the file also
     * @param points
     * @throws Exception
     */
    public void calculateMinMaxAvgx(int[] xFreq, float[] yFFT, float[] yPSD) throws Exception{
        //TODO NIDHI Check MIN MAX AVG for FFT and PSD 10-08-2011
        //calculate MIN MAX and AVG FFT
        dcComponent_FFT = yFFT[0];
        minVal_FFT = Float.MAX_VALUE;
        maxVal_FFT = - Float.MAX_VALUE;
        double sumFFT = yFFT[0];
        for (int i = 1; i < yFFT.length; i++) {
            if(minVal_FFT > yFFT[i]){
                minVal_FFT = yFFT[i];
                minValX_FFT = xFreq[i];
            }
            if(maxVal_FFT < yFFT[i]){
                maxVal_FFT = yFFT[i];
                maxValX_FFT = xFreq[i];
            }
            sumFFT += yFFT[i];
        }
        avgVal_FFT = (float)(sumFFT/yFFT.length);

        //calculate MIN MAX and AVG FFT
        dcComponent_PSD = yPSD[0];
        minVal_PSD = Float.MAX_VALUE;
        maxVal_PSD = - Float.MAX_VALUE;
        double sum_PSD = yPSD[0];
        for (int i = 1; i < yPSD.length; i++) {
            if(minVal_PSD > yPSD[i]){
                minVal_PSD = yPSD[i];
                minValX_PSD = xFreq[i];
            }
            if(maxVal_PSD < yPSD[i]){
                maxVal_PSD = yPSD[i];
                maxValX_PSD = xFreq[i];
            }
            sum_PSD += yPSD[i];
        }
        avgVal_PSD = (float)(sum_PSD/yPSD.length);
    }

    public String getFiltMethod(){
        if(filteringMethodIndex<=0 || filteringMethodIndex>3){
            return "null";
        }else{
            return filteringMethods[filteringMethodIndex];
        }
    }
    public int getNoOfPoints(){
        return (int)Math.pow(2, noOfPoints);
    }
    public String[] filteringMethods = {
                                            "choose..",
                                            "Hamming",
                                            "Hanning",
                                            "Rectangular"
                                       };

    public void showResults() {
        MyData.showInfoMessageTA(
                  "UID               = "+uid+"\n"
                + "No. of Points     = "+getNoOfPoints()+"\n"
                + "Filtering Method  = "+getFiltMethod()+"\n"
                + "DC_Compnent_FFT   = "+dcComponent_FFT+"\n"
                + "MinY_FFT [X Val]  = "+minVal_FFT+" ["+minValX_FFT+"]\n"
                + "MaxY_FFT [X Val]  = "+maxVal_FFT+" ["+maxValX_FFT+"]\n"
                + "AvgY_FFT          = "+avgVal_FFT+"\n"
                + "DC_Compnent_PSD   = "+dcComponent_PSD+"\n"
                + "MinY_PSD [X Val]  = "+minVal_PSD+" ["+minValX_PSD+"]\n"
                + "MaxY_PSD [X Val]  = "+maxVal_PSD+" ["+maxValX_PSD+"]\n"
                + "AvgY_PSD          = "+avgVal_PSD+"\n"
                + "RMS Voltage       = "+rmsVal+" Volts\n"
                + "Weighted FFT      = "+weightedFFT+"\n"
                + "Weighted PSD      = "+weightedPSD+"\n");
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
    /**
     * 
     * @param xFreq
     */
    public void prepareDecades(int[] xFreq) {
        //TODO Nidhi - preparing decades
        //make decades
        int count = 0;
        decades[count].startIndex = 0;
        for(int i=0;i<xFreq.length;i++){
            if(decades[count].endFreq < xFreq[i]){
                decades[count].endIndex = i-1;
                decades[count].show(count);
                count++;
                while(decades[count].endFreq < xFreq[i]){
                    //keep endX -1 because no point is added in this decade
                    decades[count].show(count);
                    count++;//ith point
                }
                decades[count].startIndex = i;
            }
        }
        decades[count].endIndex = xFreq.length-1;
        numDecades = count;
    }
    /**
     * 
     * @param comboZooming
     */
    public void fillDecades(MyComboBox comboZooming) {
        comboZooming.removeAllItems();
        comboZooming.addItem("All Points");
        for(int i=0;i<numDecades;i++){
            comboZooming.addItem(decades[i].getLabel());
        }
        comboZooming.validate();
        comboZooming.setSelectedIndex(0);
    }
    /**
     * 
     * @param index
     */
    public void setSelectedDecade(int index) {
        selectedDecade = index;
        paramsTM.fireTableDataChanged();
    }
    /**
     *
     * @return
     */
    public String getSelectedDecade() {
        if(selectedDecade == -1 ){
            return "All Points";
        }else{
            return "Decade "+(selectedDecade+1);
        }
    }

    /**
     *
     * @return
     */
    public float getMinVal() {
        if(selectedDecade==-1){
            if(displayType==FFTData.DISPLAY_FFT){
                return minVal_FFT;
            }else{
                return minVal_PSD;
            }
        }else{
            if(displayType==FFTData.DISPLAY_FFT){
                return decades[selectedDecade].minVal_FFT;
            }else{
                return decades[selectedDecade].minVal_PSD;
            }
        }
    }

    /**
     * 
     * @return
     */
    public String getMinValCol() {
        if(selectedDecade==-1){
            if(displayType==FFTData.DISPLAY_FFT){
                return minVal_FFT+" ["+minValX_FFT+"]";
            }else{
                return minVal_PSD+" ["+minValX_PSD+"]";
            }
        }else{
            if(displayType==FFTData.DISPLAY_FFT){
                return decades[selectedDecade].minVal_FFT+" ["+decades[selectedDecade].minValX_FFT+"]";
            }else{
                return decades[selectedDecade].minVal_PSD+" ["+decades[selectedDecade].minValX_PSD+"]";
            }
        }
    }
    /**
     *
     * @return
     */
    public float getMaxVal() {
        if(selectedDecade==-1){
            if(displayType==FFTData.DISPLAY_FFT){
                return maxVal_FFT;
            }else{
                return maxVal_PSD;
            }
        }else{
            if(displayType==FFTData.DISPLAY_FFT){
                return decades[selectedDecade].maxVal_FFT;
            }else{
                return decades[selectedDecade].maxVal_PSD;
            }
        }
    }

    /**
     *
     * @return
     */
    public String getMaxValCol() {
        if(selectedDecade==-1){
            if(displayType==FFTData.DISPLAY_FFT){
                return maxVal_FFT+" ["+maxValX_FFT+"]";
            }else{
                return maxVal_PSD+" ["+maxValX_PSD+"]";
            }
        }else{
            if(displayType==FFTData.DISPLAY_FFT){
                return decades[selectedDecade].maxVal_FFT+" ["+decades[selectedDecade].maxValX_FFT+"]";
            }else{
                return decades[selectedDecade].maxVal_PSD+" ["+decades[selectedDecade].maxValX_PSD+"]";
            }
        }
    }

    /**
     *
     * @return
     */
    public float getAvgVal() {
        if(selectedDecade==-1){
            if(displayType==FFTData.DISPLAY_FFT){
                return avgVal_FFT;
            }else{
                return avgVal_PSD;
            }
        }else{
            if(displayType==FFTData.DISPLAY_FFT){
                return decades[selectedDecade].avgVal_FFT;
            }else{
                return decades[selectedDecade].avgVal_PSD;
            }
        }
    }
    /**
     *
     * @return
     */
    public String getAvgValCol() {
        if(selectedDecade==-1){
            if(displayType==FFTData.DISPLAY_FFT){
                return avgVal_FFT+"";
            }else{
                return avgVal_PSD+"";
            }
        }else{
            if(displayType==FFTData.DISPLAY_FFT){
                return decades[selectedDecade].avgVal_FFT+"";
            }else{
                return decades[selectedDecade].avgVal_PSD+"";
            }
        }
    }

    /**
     *
     * @return
     */
    public float getRMSVal() {
        if(selectedDecade==-1){
           return rmsVal;
        }else{
           return decades[selectedDecade].rmsVal;
        }
    }

    /**
     *
     * @return
     */
    public String getRMSValCol() {
        if(selectedDecade==-1){
            return rmsVal+" Volts";
        }else{
           return decades[selectedDecade].rmsVal+" Volts";
        }
    }
    /**
     *
     * @return
     */
    public float getWeightedVal() {
        if(selectedDecade==-1){
            if(displayType==FFTData.DISPLAY_FFT){
                return weightedFFT;
            }else{
                return weightedPSD;
            }
        }else{
            if(displayType==FFTData.DISPLAY_FFT){
                return decades[selectedDecade].weightedFFT;
            }else{
                return decades[selectedDecade].weightedPSD;
            }
        }
    }
    /**
     *
     * @return
     */
    public String getWeightedValCol() {
        if(selectedDecade == -1 ){
            if(displayType==FFTData.DISPLAY_FFT){
                return weightedFFT+"";
            }else{
                return weightedPSD+"";
            }
        }else{
            if(displayType==FFTData.DISPLAY_FFT){
                return decades[selectedDecade].weightedFFT+"";
            }else{
                return decades[selectedDecade].weightedPSD+"";
            }
        }
    }
    /**
     * 
     * @return
     */
    public String getDCVal() {
        if(displayType==FFTData.DISPLAY_FFT){
            return dcComponent_FFT+"";
        }else{
            return dcComponent_PSD+"";
        }
    }
    /**
     * 
     * @return
     */
    public String getDisplayType() {
        return (displayType==FFTData.DISPLAY_FFT)?"FFT":"PSD";
    }
}