package cpnds.gui.ADCAcquisition;

import cpnds.data.MyData;
import cpnds.gui.AutoCorrelation.AutoCorrData;
import cpnds.gui.CrossCorrelation.CrossCorrData;
import cpnds.gui.FFTComputation.FFTData;
import cpnds.gui.PrincipleCompAnalysis.PCAData;
import java.io.File;

/**
 * @type     : Java Class
 * @name     : ADCAcquisitionData
 * @file     : ADCAcquisitionData.java
 * @created  : May 26, 2011 11:43:39 PM
 * @version  : 1.2
 */
public class ADCAcquisitionData {
    public static final String dirName = "ADC_ACQ_DATA";
    public static final String dataFileName = "RAW_ADC_DATA";
    public static final String paramsFileName = "ADC_ACQ_PARAMS";
    public static String recentDir = "";
    public static boolean newDataAvailable = false;
    public static int numOfPoints = 0;
    public static int uid = 0;
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
        MyData.createExperimentFolder();
        recentDir =  MyData.createOperationFolder(dirName);
        return recentDir;
    }

    /**
     *
     * @param dataFile
     * @throws Exception
     */
    public static void deleteData(File expDir) throws Exception {
        //get ADC directory and files
        File adcDir = new File(expDir, ADCAcquisitionData.dirName);
        if(adcDir.exists()){
            File[] adcFiles = adcDir.listFiles();
            for(int i=0;i<adcFiles.length;i++){
                adcFiles[i].delete();
            }
        }
        adcDir.delete();
        //get FFT directory and files
        File fftDir = new File(expDir, FFTData.dirName);
        if(fftDir.exists()){
            File[] fftFiles = fftDir.listFiles();
            for(int i=0;i<fftFiles.length;i++){
                fftFiles[i].delete();
            }
        }
        fftDir.delete();
        //get Auto Corr directory and files
        File autoCorrDir = new File(expDir, AutoCorrData.dirName);
        if(autoCorrDir.exists()){
            File[] autoCorrFiles = autoCorrDir.listFiles();
            for(int i=0;i<autoCorrFiles.length;i++){
                autoCorrFiles[i].delete();
            }
        }
        autoCorrDir.delete();
        //getCross Corr directory and files
        File crossCorrDir = new File(expDir, CrossCorrData.dirName);
        if(crossCorrDir.exists()){
            File[] crossCorrFiles = crossCorrDir.listFiles();
            for(int i=0;i<crossCorrFiles.length;i++){
                crossCorrFiles[i].delete();
            }
        }
        crossCorrDir.delete();
        //PCA directory and files
        File pcaDir = new File(expDir, PCAData.dirName);
        if(pcaDir.exists()){
            File[] pcaFiles = pcaDir.listFiles();
            for(int i=0;i<pcaFiles.length;i++){
                pcaFiles[i].delete();
            }
        }
        pcaDir.delete();
        //check unknown
        File[] unknownFiles = expDir.listFiles();
        if(unknownFiles!=null && unknownFiles.length>0){
            String list = "While deleting "+expDir.getParentFile().getName()+"/"+expDir.getName()+
                          "\nUnkwon Files/Dirs found:";
            for(int i=0;i<unknownFiles.length;i++){
                list += "\n"+unknownFiles[i].getName();
            }
            MyData.showErrorMessage(list);
            throw new Exception("DeleteFailed "+expDir.getParentFile().getName()+"/"+expDir.getName());
        }else{
            File dateDir = expDir.getParentFile();
            if(!expDir.delete()){
                throw new Exception("DeleteFailed "+expDir.getParentFile().getName()+"/"+expDir.getName());
            }
            //further if dateDir is empty delete it also
            try{
                File[] list = dateDir.listFiles();
                if(list==null||list.length==0){
                    dateDir.delete();
                }
            }catch(Exception ex){
            }
        }
    }
}