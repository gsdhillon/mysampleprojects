package cpnds.gui.FFTComputation;

import cpnds.data.MyData;

/**
 * @type     : Java Class
 * @name     : Decade
 * @file     : Decade.java
 * @created  : Jul 3, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class Decade {
    public int startIndex = -1;
    public int endIndex = -1;
    public int startFreq;
    public int endFreq;
    public int minValX_FFT = -1;
    public int maxValX_FFT = -1;
    public int minValX_PSD = -1;
    public int maxValX_PSD = -1;

    public float minVal_FFT = 0.0f;
    public float maxVal_FFT = 0.0f;
    public float avgVal_FFT = 0.0f;
    public float minVal_PSD = 0.0f;
    public float maxVal_PSD = 0.0f;
    public float avgVal_PSD = 0.0f;
    public float rmsVal = 0.0f;
    public float weightedFFT = 0.0f;
    public float weightedPSD = 0.0f;

    public static final int LEN = 68;
    /**
     *
     * @param buffer
     * @param offset
     * @return
     * @throws Exception
     */
    public int setData(byte[] buffer, int offset) throws Exception{
        MyData.putIntBytesToBuffer(startIndex, buffer, offset);offset+=4;
        MyData.putIntBytesToBuffer(endIndex, buffer, offset);offset+=4;
        MyData.putIntBytesToBuffer(startFreq, buffer, offset);offset+=4;
        MyData.putIntBytesToBuffer(endFreq, buffer, offset);offset+=4;
        MyData.putIntBytesToBuffer(minValX_FFT, buffer, offset);offset+=4;
        MyData.putIntBytesToBuffer(maxValX_FFT, buffer, offset);offset+=4;
        MyData.putIntBytesToBuffer(minValX_PSD, buffer, offset);offset+=4;
        MyData.putIntBytesToBuffer(maxValX_PSD, buffer, offset);offset+=4;

        MyData.putFloatBytesToBuffer(minVal_FFT, buffer, offset);offset+=4;
        MyData.putFloatBytesToBuffer(maxVal_FFT, buffer, offset);offset+=4;
        MyData.putFloatBytesToBuffer(avgVal_FFT, buffer, offset);offset+=4;
        MyData.putFloatBytesToBuffer(minVal_PSD, buffer, offset);offset+=4;
        MyData.putFloatBytesToBuffer(maxVal_PSD, buffer, offset);offset+=4;
        MyData.putFloatBytesToBuffer(avgVal_PSD, buffer, offset);offset+=4;
        MyData.putFloatBytesToBuffer(rmsVal, buffer, offset);offset+=4;
        MyData.putFloatBytesToBuffer(weightedFFT, buffer, offset);offset+=4;
        MyData.putFloatBytesToBuffer(weightedPSD, buffer, offset);offset+=4;
        return offset;
    }
    /**
     *
     * @param buffer
     * @param offset
     * @return
     * @throws Exception
     */
    public int getData(byte[] buffer, int offset) throws Exception{
        startIndex = MyData.getInt(buffer, offset);offset+=4;
        endIndex = MyData.getInt(buffer, offset);offset+=4;
        startFreq = MyData.getInt(buffer, offset);offset+=4;
        endFreq = MyData.getInt(buffer, offset);offset+=4;
        minValX_FFT = MyData.getInt(buffer, offset);offset+=4;
        maxValX_FFT = MyData.getInt(buffer, offset);offset+=4;
        minValX_PSD = MyData.getInt(buffer, offset);offset+=4;
        maxValX_PSD = MyData.getInt(buffer, offset);offset+=4;

        minVal_FFT = MyData.getFloat(buffer, offset);offset+=4;
        maxVal_FFT = MyData.getFloat(buffer, offset);offset+=4;
        avgVal_FFT = MyData.getFloat(buffer, offset);offset+=4;
        minVal_PSD = MyData.getFloat(buffer, offset);offset+=4;
        maxVal_PSD = MyData.getFloat(buffer, offset);offset+=4;
        avgVal_PSD = MyData.getFloat(buffer, offset);offset+=4;
        rmsVal = MyData.getFloat(buffer, offset);offset+=4;
        weightedFFT = MyData.getFloat(buffer, offset);offset+=4;
        weightedPSD = MyData.getFloat(buffer, offset);offset+=4;

        return offset;
    }

    public Decade(int xStart, int xEnd){
        startFreq = xStart;
        endFreq = xEnd;
    }
    public String getLabel(){
        return getString(startFreq)+" - "+getString(endFreq);
    }
    /**
     *
     * @param val
     * @return
     */
    private String getString(long val){
        if(val < 1000){
            return String.valueOf(val)+"Hz";
        }else if(val < 1000000){
            return String.valueOf(val/1000.0)+"KHz";
        }else{
            return String.valueOf(val/1000000.0)+"MHz";
        }
    }
    public void show(int count){
        //MyData.showInfoMessage("Decade "+count+" "+startIndex+" - "+endIndex);
    }
    /**
     * 
     * @param xFreq
     * @param yFFT
     * @param yPSD
     */
    public void calculateMinMaxAvgx(int[] xFreq, float[] yFFT, float[] yPSD) {
        //TODO NIDHI Check decadewise MIN MAX AVG for FFT and PSD 12-08-2011

        int length = (endIndex-startIndex);
        if(length <= 0){
            return;//decade may be empty
        }

        //calculate MIN MAX and AVG FFT
        minVal_FFT = Float.MAX_VALUE;
        maxVal_FFT = - Float.MAX_VALUE;
        double sumFFT = yFFT[0];
        for (int i = startIndex; i < endIndex; i++) {
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
        minVal_PSD = Float.MAX_VALUE;
        maxVal_PSD = - Float.MAX_VALUE;
        double sum_PSD = yPSD[0];
        for (int i = startIndex; i < endIndex; i++) {
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
}
