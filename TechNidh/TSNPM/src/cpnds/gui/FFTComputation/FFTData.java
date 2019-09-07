package cpnds.gui.FFTComputation;

import cpnds.MyGraphs.DataPoint;
import cpnds.data.MyData;
import cpnds.gui.ADCAcquisition.ADCAcqParams;
import java.io.File;
import java.io.FileInputStream;

/**
 * @type     : Java Class
 * @name     : AutoCorrelationData
 * @file     : AutoCorrelationData.java
 * @created  : May 29, 2011 11:43:39 PM
 * @version  : 1.2
 */
public class FFTData {
    public static final byte DISPLAY_FFT = 0;
    public static final byte DISPLAY_PSD = 1;
    public static final String dirName = "FFT_DATA";
    public static final String dataFileName = "RAW_FFT_DATA";
    public static final String paramsFileName = "FFT_PARAMS";
    public static String recentDir = "";
    public static boolean newDataAvailable = false;
    /**
     * @return
     */
    public static File getRecentDataFile() throws Exception{
        File f = new File(recentDir+"/"+dataFileName);
        return f;
    }
    /**
     * @return
     */
    public static File getRecentParamFile() throws Exception{
        File f = new File(recentDir+"/"+paramsFileName);
        return f;
    }
    /**
     * @return
     */
    public static File getParamFile(File dataFile) throws Exception{
        File f = new File(dataFile.getParent()+"/"+paramsFileName);
        return f;
    }
    /**
     * @return
     */
    public static File getParamFileFromExpDir(File expDir) throws Exception{
        File f = new File(expDir, dirName+"/"+paramsFileName);
        return f;
    }
    /**
     *
     * @return
     * @throws Exception
     */
    public static String createDirectories() throws Exception{
        recentDir = MyData.createOperationFolder(dirName);
        return recentDir;
    }

    /**
     *
     * @param f
     * @param adcAcqParams
     * @param fftParams
     * @throws Exception
     */
    public static void calculateAllResults(File f, ADCAcqParams adcAcqParams,
                                    FFTParams fftParams) throws Exception{
        FileInputStream fis = new FileInputStream(f);
        int bytesRead = 0;
        int chunckSize = MyData.CHUNCK_SIZE;
        int dataLen;
        int off = 0;
        while((bytesRead = fis.read(MyData.dataBuffer, off, chunckSize))>0){
            off += bytesRead;
        }
        fis.close();
        dataLen = off;
        //check len of data should be multiple of 8
        //n/2 floats X n/2 floats Y
        if(dataLen%8 != 0){
            throw new Exception("LenofDataNot 8X = " + dataLen);
        }
        int numPoints = dataLen/8;
        float[] real = new float[numPoints];
        float[] imag = new float[numPoints];

        //make float array of real numbers
        int offSet = 0;
        for(int i=0;i<numPoints;i++){
            int intBits = ((MyData.dataBuffer[offSet+3] & 0x00FF) << 24) |
                          ((MyData.dataBuffer[offSet+2] & 0x00FF) << 16) |
                          ((MyData.dataBuffer[offSet+1] & 0x00FF) << 8) |
                          (MyData.dataBuffer[offSet+0] & 0x00FF);
            real[i] = Float.intBitsToFloat(intBits);
            offSet += 4;
        }
        //make float array of imag numbers
        offSet = 4*numPoints;
        for(int i=0;i<numPoints;i++){
            int intBits = ((MyData.dataBuffer[offSet+3] & 0x00FF) << 24) |
                          ((MyData.dataBuffer[offSet+2] & 0x00FF) << 16) |
                          ((MyData.dataBuffer[offSet+1] & 0x00FF) << 8) |
                          (MyData.dataBuffer[offSet+0] & 0x00FF);
            imag[i] = Float.intBitsToFloat(intBits);
            offSet += 4;
        }
        //TODO compute Freq and amplitude Y_FFT, Y_PSD
        //make XFreq, YFFT and YPSD
        //DataPoint[] points = new DataPoint[numPoints];
        int[] xFreq = new int[numPoints];
        float[] yFFT = new float[numPoints];
        float[] yPSD = new float[numPoints];
        float fs = adcAcqParams.getFS();
        for(int i=0;i<numPoints;i++){
            xFreq[i] = (int)(fs*i)/numPoints;
            yFFT[i] = (float)Math.hypot(real[i], imag[i]);
            yPSD[i] = (real[i]*real[i] + imag[i]*imag[i])/(numPoints*fs);
        }

        //compute RMS value and set into fftParams
        fftParams.rmsVal = computeRMS(real, imag, 0, real.length);
        fftParams.weightedFFT = computeWeightedFFT(xFreq, yFFT, 0, xFreq.length);
        fftParams.weightedPSD = computeWeightedPSD(xFreq, yPSD, 0, xFreq.length);
        //calculate MIN,MAX,and AVG for FFT and PSD both
        fftParams.calculateMinMaxAvgx(xFreq, yFFT, yPSD);

        //calculate decade wise results
        fftParams.prepareDecades(xFreq);
        for(int i=0;i<fftParams.numDecades;i++){
            int start = fftParams.decades[i].startIndex;
            int end = fftParams.decades[i].endIndex;
            //calculate decade wise rms, weighted fft, weighted PSD
            fftParams.decades[i].rmsVal = computeRMS(real, imag, start, end);
            fftParams.decades[i].weightedFFT = computeWeightedFFT(xFreq, yFFT, start, end);
            fftParams.decades[i].weightedPSD = computeWeightedPSD(xFreq, yPSD, start, end);
            //calculate decade wise maxval , minval , avgval
            fftParams.decades[i].calculateMinMaxAvgx(xFreq, yFFT, yPSD);
        }
    }

    /**
     * Computes RMS value from two array real and imag
     * @param real
     * @param imag
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    private static float computeRMS(float[] real, float[] imag,
                                        int start, int end) throws Exception{
        if(real.length != imag.length){
            throw new Exception("WrongLenRealImagNums");
        }

        int length = (end-start);
        if(length <= 0){
            return 0.0f;//decade may be empty
        }

        //Compute RMS Power
        //TODO - FFT Coumpute RMS
        //real array have real part of complex numbers and imag array have imag part
        double sumX2PlusY2 = 0.0;
        for(int i=start; i<end; i++){
            sumX2PlusY2 += (real[i]*real[i] + imag[i]*imag[i]);
        }
        return (float)(Math.sqrt(sumX2PlusY2) / length);
    }

    /**
     * Computes weightedFFT value from two array x and y
     * @param x
     * @param y
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    private static float computeWeightedFFT(int[] x, float[] y,
                                        int start, int end) throws Exception{
        if(x.length != y.length){
            throw new Exception("WrongLenRealImagNums");
        }

        int length = (end-start);
        if(length <= 0){
            return 0.0f;//decade may be empty
        }

        //Compute computeWeightedFFT
        //TODO - FFT Coumpute weightedFFT
        double sumXiYi= 0.0;
        double sumXi = 0.0;
        for(int i=start; i<end; i++){
            sumXiYi += (x[i]*y[i]);
            sumXi += x[i];
        }
        return (float)(sumXiYi / sumXi);
    }

    /**
     * Computes weightedPSD value from two array x and y
     * @param x
     * @param y
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    private static float computeWeightedPSD(int[] x, float[] y,
                                        int start, int end) throws Exception{
        if(x.length != y.length){
            throw new Exception("WrongLenRealImagNums");
        }

        int length = (end-start);
        if(length <= 0){
            return 0.0f;//decade may be empty
        }

        //Compute computeWeightedPSD
        //TODO - FFT Coumpute weightedPSD
        double sumXi2Yi2= 0.0;
        double sumXi = 0.0;
        for(int i=start; i<end; i++){
            //sumXi2Yi2 += (x[i]*x[i]*y[i]*y[i]);
            long xi = x[i];
            double yi = y[i];
            double x2y2 = xi*xi*yi*yi;
            if(x2y2 < 0.0){
                MyData.showInfoMessageTA("Data overflow while Calculating Weighted PSD:\n"
                        + "x["+i+"]="+x[i]+", y["+i+"]="+y[i]+", x^2*y^2="+x[i]*x[i]*y[i]*y[i]);
                return 0.0f;
            }
            sumXi2Yi2 += x2y2;
            sumXi += x[i];
        }
        return (float)(sumXi2Yi2 / sumXi);
    }

    /**
     *
     * @param f
     * @param indexOfFS
     * @param displayType
     * @return
     * @throws Exception
     */
    public static DataPoint[] readData(File f, ADCAcqParams adcAcqParams,
                        FFTParams fftParams, byte displayType) throws Exception{
        FileInputStream fis = new FileInputStream(f);
        int bytesRead = 0;
        int chunckSize = MyData.CHUNCK_SIZE;
        int dataLen;
        int off = 0;
        while((bytesRead = fis.read(MyData.dataBuffer, off, chunckSize))>0){
            off += bytesRead;
        }
        fis.close();
        dataLen = off;
        //check len of data should be multiple of 8
        //n/2 floats X n/2 floats Y
        if(dataLen%8 != 0){
            throw new Exception("LenofDataNot 8X = " + dataLen);
        }
        int numPoints = dataLen/8;
        float[] real = new float[numPoints];
        float[] imag = new float[numPoints];

        //make float array of real numbers
        int offSet = 0;
        for(int i=0;i<numPoints;i++){
            int intBits = ((MyData.dataBuffer[offSet+3] & 0x00FF) << 24) |
                          ((MyData.dataBuffer[offSet+2] & 0x00FF) << 16) |
                          ((MyData.dataBuffer[offSet+1] & 0x00FF) << 8) |
                          (MyData.dataBuffer[offSet+0] & 0x00FF);
            real[i] = Float.intBitsToFloat(intBits);
            offSet += 4;
        }

        //make float array of imag numbers
        offSet = 4*numPoints;
        for(int i=0;i<numPoints;i++){
            int intBits = ((MyData.dataBuffer[offSet+3] & 0x00FF) << 24) |
                          ((MyData.dataBuffer[offSet+2] & 0x00FF) << 16) |
                          ((MyData.dataBuffer[offSet+1] & 0x00FF) << 8) |
                          (MyData.dataBuffer[offSet+0] & 0x00FF);
            imag[i] = Float.intBitsToFloat(intBits);
            offSet += 4;
        }
        //make Xi and Yi
        DataPoint[] points = new DataPoint[numPoints];
        float fs = adcAcqParams.getFS();
        for(int i=0;i<numPoints;i++){
            points[i] = new DataPoint();
            points[i].sno = i;
            points[i].x = (long)(fs*i)/numPoints;
            //both x and fs is long why x becoming -ve just check few values
            //if(sno<5) MyData.showInfoMessage("sno="+sno+", fs="+fs+", fs*sno="+(fs*sno)+", numPoints="+numPoints+", x="+points[sno].x);
            //it was fs that was -ve so check adcAcqParams.getFS()
            if(displayType == DISPLAY_FFT){
                points[i].y = (float)Math.hypot(real[i], imag[i]);
            }else{//DISPLAY_PSD
                points[i].y = (real[i]*real[i] + imag[i]*imag[i])/(numPoints*fs);
            }
        }
        //MyData.showInfoMessage("Getting data "+(displayType==DISPLAY_FFT?"FFT":"PSD"));
        return points;
    }
}