package cpnds.gui.AutoCorrelation;

import cpnds.data.MyData;
import java.io.File;

/**
 * @type     : Java Class
 * @name     : AutoCorrData
 * @file     : AutoCorrData.java
 * @created  : May 29, 2011 11:43:39 PM
 * @version  : 1.2
 */
public class AutoCorrData {
    public static final String dirName = "AUTO_CORR_DATA";
    public static final String dataFileName = "RAW_AUTO_CORR_DATA";
    public static final String paramsFileName = "AUTO_CORR_PARAMS";
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
        recentDir =  MyData.createOperationFolder(dirName);
        return recentDir;
    }
}