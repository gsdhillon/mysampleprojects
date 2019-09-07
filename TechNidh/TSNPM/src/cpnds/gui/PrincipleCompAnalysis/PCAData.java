package cpnds.gui.PrincipleCompAnalysis;

import cpnds.data.MyData;
import java.io.File;

/**
 * @type     : Java Class
 * @name     : PCAData
 * @file     : PCAData.java
 * @created  : May 26, 2011 11:43:39 PM
 * @version  : 1.2
 */
public class PCAData {
    public static final String dirName = "PCA_DATA";
    public static final String dataFileName = "RAW_PCA_DATA";
    public static final String paramsFileName = "PCA_PARAMS";
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
}