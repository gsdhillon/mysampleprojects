package cpnds.gui.CrossCorrelation;

import cpnds.MyGraphs.DataPoint;
import cpnds.data.MyData;
import java.io.File;
/**
 * @type     : Java Class
 * @name     : CrossCorrCommonParams
 * @file     : CrossCorrCommonParams.java
 * @created  : Aug 12, 2011 11:43:39 PM
 * @version  : 1.2
 */
public class CrossCorrCommonParams {
    public File paramFile;
    public final byte processID = 74;
    public int uid = 0;
    public int maxValResultNo = -1;
    public int maxValX = -1;
    public float maxVal = - Float.MAX_VALUE;
    public int preferredResult = 1;//ADDED 15-08-11 7:10PM
    private int intOne = 0;        //ADDED 15-08-11 7:10PM
    private int intTwo = 0;        //ADDED 15-08-11 7:10PM
    private float floatOne = 0;    //ADDED 15-08-11 7:10PM
    private float floatTwo = 0;    //ADDED 15-08-11 7:10PM
    private final int LEN = 37;
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
        MyData.putIntBytesToBuffer(maxValResultNo, params, 5);
        MyData.putIntBytesToBuffer(maxValX, params, 9);
        MyData.putFloatBytesToBuffer(maxVal, params, 13);
        MyData.putIntBytesToBuffer(preferredResult, params, 17);
        MyData.putIntBytesToBuffer(intOne, params, 21);
        MyData.putIntBytesToBuffer(intTwo, params, 25);
        MyData.putFloatBytesToBuffer(floatOne, params, 29);
        MyData.putFloatBytesToBuffer(floatTwo, params, 33);
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
            throw new Exception("INVALID CROSS_CORR_COMM_PARAMS FILE PID = "+buffer[0]);
        }
        if(buffer.length < 17){
            throw new Exception("Old version of CROSS_CORR_COMM_PARAMS file");
        }
        uid = MyData.getInt(buffer, 1);
        maxValResultNo = MyData.getInt(buffer, 5);
        maxValX = MyData.getInt(buffer, 9);
        maxVal = MyData.getFloat(buffer, 13);
        //For Backward compatibility
        if(buffer.length != LEN){
            preferredResult = 1;
            return;
        }
        preferredResult = MyData.getInt(buffer, 17);
        intOne = MyData.getInt(buffer, 21);
        intTwo = MyData.getInt(buffer, 25);
        floatOne = MyData.getFloat(buffer, 29);
        floatTwo = MyData.getFloat(buffer, 33);
        paramFile = file;
    }
    /**
     *
     * @param resultNo
     * @param points
     * @throws Exception
     */
    public void updateCommonMaxVal(int resultNo, DataPoint[] points) throws Exception{
        //maxVal = - Float.MAX_VALUE; already initialized
        for (int i = 0; i < points.length; i++) {
            if(maxVal < points[i].y){
                maxVal = points[i].y;
                maxValX = i;
                maxValResultNo = resultNo;
            }
        }
    }
    /**
     *
     * @return
     */
    public String getDate() {
        try{
            String s = paramFile.getParentFile().getParentFile().getParentFile().getName();
            return s.substring(5);
        }catch(Exception e){
            return "null";
        }
    }
    /**
     * 
     * @return
     */
    public String getTime() {
        try{
            String s = paramFile.getParentFile().getParentFile().getName();
            return s.substring(5);
        }catch(Exception e){
            return "null";
        }
    }
}