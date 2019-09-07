package pkilib;


import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * MyUtils.java
 * Created on Sep 28, 2007, 10:51:29 AM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class MyUtils {
    /**
     * convert java.util.Date to string
     * @param d
     * @return string in "dd/mm/yyyy HH:MM:SS" format
     */
    public static String dateToString(Date d){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = df.format(d);
        return date;
    }
    /**
     * get string in "dd/mm/yyyy HH:MM:SS" format and convert to java.util.Date
     * @param s
     * @return java.util.Date or null if invalid format
     */
    public static Date stringToDate(String s){
        try{
           SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
           Date d = df.parse(s);
           return d;
        }catch(Exception e){
            showException("while converting "+s+" to Date", e);
            return null;
        }
    }
    /**
     * 
     * @param msg
     */
    public static void showMessage(String msg){
        //System.out.println("Message - "+msg);
    }
    /**
     * 
     * @param msg
     */
    public static void showError(String msg){
        //System.out.println("Error - "+msg);
    }
    /**
     * 
     * @param msg
     * @param e
     */
    public static void showException(String msg, Exception e){
        //System.out.println("Exception report - "+msg);
        //e.printStackTrace();
    }
}