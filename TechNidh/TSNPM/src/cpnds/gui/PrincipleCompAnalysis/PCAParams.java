package cpnds.gui.PrincipleCompAnalysis;

import cpnds.data.MyData;
import java.io.File;

/**
 * @type     : Java Class
 * @name     : PCAParams
 * @file     : PCAParams.java
 * @created  : Jul 01, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class PCAParams {
    public final byte processID = 75;
    public int uid=0;
    public File paramFile;
    public int startingSampleNumber;
    public float pcaVal = 0.0f;
    /**
     * 
     * @param file
     * @throws Exception
     */
    public void write(File file) throws Exception{
        //writing parameters in the param file
        byte[] params = new byte[13];
        params[0] = processID;
        MyData.putIntBytesToBuffer(uid, params, 1);
        MyData.putIntBytesToBuffer(startingSampleNumber, params, 5);
        MyData.putFloatBytesToBuffer(pcaVal, params, 9);
        MyData.writeParameterFile(file, params, 0, 13);
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
            throw new Exception("INVALID ADC_ACQ_PARAM FILE PID = "+buffer[0]);
        }
        uid = MyData.getInt(buffer, 1);
        startingSampleNumber = MyData.getInt(buffer, 5);
        pcaVal = MyData.getFloat(buffer, 9);
        paramFile = file;
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
