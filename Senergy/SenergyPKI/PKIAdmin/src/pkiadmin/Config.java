package pkiadmin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class Config{
    public static String path;
    public static String pkiServerURLBase = "http://localhost:8084/RPGPKIServer";
    /**
     * set all config parameters
     */
    public static boolean setParameters(){
        try{
            path = setApplicationPath();
            pkiServerURLBase = readParameter("pkiServerURLBase");
            return true;
        }catch(Exception e){
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
            e.printStackTrace();
            return false;
        }
     }
    /**
     * read the value of specified parameter from configuration file
     */
    private static String readParameter(String name) throws Exception{
        FileInputStream fis = new FileInputStream(new File(path+"/config.txt")); 
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String record = br.readLine();
        while(record != null){
            record = record.trim();
            if(record.startsWith(name)){
                String result = record.substring((record.indexOf('=')+1)).trim();
                br.close();
                return result;
            }
            record = br.readLine();
        }
        br.close();
        throw new Exception("ParameterNotFound -"+name);
    }
    /**
     * Detect the abselute path of the project and set the path variable
     */
    public static String setApplicationPath(){
        String s = Config.class.getResource("Config.class").getPath();
        s = s.replaceAll("%20", " ");
        s = s.substring(s.indexOf('/'), s.indexOf(APP_NAME)+APP_NAME.length());
        System.out.println("Application path = "+s);
        return s;
    }
    
    private static final String APP_NAME = "PKIAdmin";
}