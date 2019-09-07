package cpnds.gui.CrossCorrelation;

import cpnds.data.MyData;
import java.io.File;


/**
 * @type     : Java Class
 * @name     : CrossCorrData
 * @file     : CrossCorrData.java
 * @created  : May 26, 2011 11:43:39 PM
 * @version  : 1.2
 */
public class CrossCorrData {
    //public static final byte processID = 74;
    public static final String dirName = "CROSS_CORR_DATA";
    public static final String dataFileName = "RAW_CROSS_CORR_DATA";
    public static final String paramsFileName = "CROSS_CORR_PARAMS";
    public static String recentDir = "";
    public static boolean newDataAvailable = false;
    /**
     *
     * @param resultNo 1 - 10
     * @return
     */
    public static File getRecentDataFile(int resultNo) throws Exception{
        File f = new File(recentDir+"/"+getDataFileName(resultNo));
        return f;
    }
    /**
     *
     * @param resultNo 1 - 10
     * @return
     */
    public static File getRecentParamFile(int resultNo) throws Exception{
        File f = new File(recentDir+"/"+getParamsFileName(resultNo));
        return f;
    }
    /**
     * Get paramsFile corresponding to dataFile
     * @return
     */
    public static File getParamFile(File dataFile) throws Exception{
        //get result num from dataFileName itself
        String s = dataFile.getName();
        s = s.substring(dataFileName.length());
        File f = new File(dataFile.getParent()+"/"+paramsFileName+s);
        return f;
    }
    /**
     * @return
     */
    public static File getParamFileFromExpDir(File expDir, int resultNo) throws Exception{
        if(resultNo > 0 && resultNo < 10){
            File f = new File(expDir, dirName+"/"+paramsFileName+"_0"+resultNo);
            return f;
        }else if(resultNo>=10){
            File f = new File(expDir, dirName+"/"+paramsFileName+"_"+resultNo);
            return f;
        }else{
            return null;
        }
    }
    /**
     * For storing common max min avg
     * @param paramsFile
     * @return
     */
    public static File getRecentCommonParamsFile() throws Exception{
        return new File(recentDir+"/"+paramsFileName);
    }
    /**
     * For storing common max min avg
     * @param paramsFile
     * @return
     */
    public static File getCommonParamsFile(File paramsFile) throws Exception{
        return new File(paramsFile.getParentFile(), paramsFileName);
    }
    /**
     * @return
     */
    public static File getCommonParamFileFromExpDir(File expDir) throws Exception{
        return new File(expDir, dirName+"/"+paramsFileName);
    }
    
    /**
     * 
     * @param resultNo
     * @return
     */
    private static String getDataFileName(int resultNo){
        if(resultNo > 0 && resultNo < 10){
            return dataFileName+"_0"+resultNo;
        }else if(resultNo>=10){
            return dataFileName+"_"+resultNo;
        }else{
            return null;
        }
    }
    /**
     *
     * @param resultNo
     * @return
     */
    private static String getParamsFileName(int resultNo){
        if(resultNo > 0 && resultNo < 10){
            return paramsFileName+"_0"+resultNo;
        }else if(resultNo>=10){
            return paramsFileName+"_"+resultNo;
        }else{
            return null;
        }
    }
    /**
     * 
     * @param currentFile
     * @return
     */
    public static File getNextFile(File currentFile) throws Exception{
        int resultNo = Integer.parseInt(currentFile.getName().substring(20, 22));
        resultNo++;
        File f = new File(currentFile.getParent()+"/"+getDataFileName(resultNo));
        if(f!=null && f.exists()){
            return f;
        }else{
            return null;
        }
    }
    /**
     *
     * @param currentFile
     * @return
     */
    public static File getPrevFile(File currentFile) throws Exception{
        int resultNo = Integer.parseInt(currentFile.getName().substring(20, 22));
        resultNo--;
        File f = new File(currentFile.getParent()+"/"+getDataFileName(resultNo));
        if(f!=null && f.exists()){
            return f;
        }else{
            return null;
        }
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
}