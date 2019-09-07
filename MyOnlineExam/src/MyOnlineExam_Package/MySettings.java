package MyOnlineExam_Package;

import MyOnlineExam_Package.TakeExam.TakeExamFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created on Aug 10, 2013
 * @version 1.0.0
 * @author  : Gurmeet Singh, EMail-gsdhillon@gmail.com, Ph-9869117976
 */
@SuppressWarnings("CallToThreadDumpStack")
public class MySettings {
    public static final String APP_NAME = "MyOnlineExam";
   // public static TakeExamFrame mainFrame = null;
    public static String home = "C:";
    public static int width = 1024;
    public static int height = 768;
    //data
    public static String examHeader = "";
    /**
     *
     */
    
    public static void initialize(){//MainFrame mainFrame
       // MySettings.mainFrame = mainFrame;
        //set application home path
        try{
            String s = MySettings.class.getResource("MySettings.class").getPath();
            s = s.replaceAll("%20", " ");
            if(s.indexOf(APP_NAME+".jar")>0){//run from jar
                System.out.println("jar path = "+s);
                s = s.substring(0, s.lastIndexOf(APP_NAME+"/dist")+APP_NAME.length());
            }else{//run from class - NetBeans
                System.out.println("class path = "+s);
                s = s.substring(0, s.lastIndexOf(APP_NAME+"/build")+APP_NAME.length());
                System.out.println(s);
            }
            //remove initial /
            s = s.substring(s.indexOf('/')+1);
            home = s.replaceAll("/", "\\\\");
            System.out.println(home);
        }catch(Exception e){
            e.printStackTrace();
        }
        //read userID and Password
      //  readSettingsFile();

//        //setr date time
//        try{
//            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            systemDate = dateFormat.format(new Date());
//            loginTime = LogReports.currentTimeString();
//        }catch(Exception e){
//            LogReports.logError(e);
//        }

        //create folders if do not exist
        try{
            //create folders
            createDirectory(home+"/Data");
            examHeader = MySettings.readParameter(home+"/Data/examTitle.txt", "TITLE");
        }catch(Exception e){
            e.printStackTrace();
        }

        
        
        //set screen resolution
        try{
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            MySettings.width = screenSize.width;//1024;
            MySettings.height = screenSize.height;//768;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public static String readParameter(String filePath, String param) throws Exception{
        String record = null;
        String result = null;
        BufferedReader br = null;
        try {
            File f = new File(filePath);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            record = br.readLine();
            while(record != null){
                record = record.trim();
                if(record.startsWith(param+"=")){
                    result = record.substring((record.indexOf('=')+1)).trim();
                    br.close();
                    //System.out.println(name+"="+result);
                    return result;
                }
                record = br.readLine();
            }
            throw new Exception("File "+filePath+" NOT FOUND");
        }finally{
            if(br != null) br.close();
        }
    }
    
    /**
     *
     * @param folderPath
     */
    public static void createDirectory(String folderPath) throws Exception{
        File directory = new File(folderPath);
        if(!directory.exists()){
            if(!directory.mkdir()){
                throw new Exception("Could not create data folder\n "
                                      +directory.getAbsolutePath());
            }
        }
    }
}