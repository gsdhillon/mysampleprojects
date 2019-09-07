package mmi.data;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @type     : Java Class
 * @name     : LogReports
 * @file     : LogReports.java
 * @created  : Aug 11, 2010 1:08:07 PM
 * @version  : 1.0.0
 */
public class LogReports {
    /**
     * 
     */
    public static void login(String user){
        PrintWriter pw = null;
        try{
            pw = getPrintWriter();
            pw.println("USER: "+user+" - Logged IN at: "+currentTimeString());
        }catch(Exception ex){
            //ex.printStackTrace();
        }finally{
            try{
                pw.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    /**
     * 
     */
    public static void logout(String user){
        PrintWriter pw = null;
        try{
            pw = getPrintWriter();
            pw.println("USER: "+user+" - Logged OUT at: "+currentTimeString());
        }catch(Exception ex){
            //ex.printStackTrace();
        }finally{
            try{
                pw.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    /**
     * 
     */
    public static void println(String msg){
        PrintWriter pw = null;
        try{
            pw = getPrintWriter();
            pw.println("Log At : "+currentTimeString()+" - "+msg);
        }catch(Exception ex){
            //ex.printStackTrace();
        }finally{
            try{
                pw.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    /**
     * 
     * @param e
     */
    public static void logError(Exception e){
        PrintWriter pw = null;
        try{
            pw = getPrintWriter();
            pw.println("-------------- Exception at : "+currentTimeString()+" --------------");
            e.printStackTrace(pw);
            pw.println("--------------------- END of Exception -----------------------");
        }catch(Exception ex){
            //ex.printStackTrace();
        }finally{
            try{
                pw.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    /**
     * 
     * @return
     */
    private static PrintWriter getPrintWriter() throws Exception{
        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        String day =  dateFormat.format(new Date());
        File f = new File(MyData.appHome+"/"+MyData.logFolder+"/"+day+".txt");
        PrintWriter pw;
        if(MyData.debug){
                pw = new PrintWriter(System.out);
            }else{
                pw = new PrintWriter(new FileWriter(f, true), true);
            }
        return pw;
    }

    /**
     *
     * @return date string
     */
    public static String currentTimeString(){
        DateFormat f = new SimpleDateFormat("HH:mm:ss");
        f.setTimeZone(TimeZone.getTimeZone("IST"));
        return f.format(new Date());
    }
}