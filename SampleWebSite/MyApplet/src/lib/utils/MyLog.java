package lib.utils;

import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 */
public class MyLog {
    private static String home = ".";
    private static boolean debug = false;
    /**
     *
     * @param home
     */
    public static void setHome(String home){
        MyLog.home = home;
    }
    /**
     *
     * @param debug
     */
    public static void setDebug(boolean debug){
        MyLog.debug = debug;
    }
    /**
     * 
     */
    public static void login(String user){
        PrintWriter pw = null;
        try{
            if(!debug){
                pw = getPrintWriter();
                pw.println("USER: "+user+" - Logged IN at: "+time());
            }else{
               System.out.println("USER: "+user+" - Logged IN at: "+time());
            }
            
        }catch(Exception ex){
            //ex.printStackTrace();
        }finally{
            try{
                pw.close();
            }catch(Exception ex){
                //ex.printStackTrace();
            }
        }
    }
    /**
     * 
     */
    public static void logout(String user){
        PrintWriter pw = null;
        try{
            if(!debug){
                pw = getPrintWriter();
                pw.println("USER: "+user+" - Logged OUT at: "+time());
            }else{
                System.out.println("USER: "+user+" - Logged OUT at: "+time());
            }
            
        }catch(Exception ex){
            //ex.printStackTrace();
        }finally{
            try{
                pw.close();
            }catch(Exception ex){
                //ex.printStackTrace();
            }
        }
    }
    /**
     * 
     */
    public static void showDebugMessage(String msg){
        if(debug){
            println(msg);
        }
    }
    /**
     *
     */
    private static void println(String msg){
        PrintWriter pw = null;
        try{
            if(!debug){
                pw = getPrintWriter();
                pw.println("At : "+time()+" - "+msg);
            }else{
                System.out.println(msg);
            }
        }catch(Exception ex){
            //ex.printStackTrace();
        }finally{
            try{
                pw.close();
            }catch(Exception ex){
                //ex.printStackTrace();
            }
        }
    }
    /**
     *
     * @param e
     */
    private static void logError(String msg, Exception e){
        PrintWriter pw = null;
        try{
            if(!debug){
                pw = getPrintWriter();
                pw.println("Exception while "+msg+" at : "+time());
                e.printStackTrace(pw);
            }else{
                e.printStackTrace();
            }
        }catch(Exception ex){
            //ex.printStackTrace();
        }finally{
            try{
                pw.close();
            }catch(Exception ex){
                //ex.printStackTrace();
            }
        }
    }
    /**
     *
     * @param msg
     */
    public static void showInfoMsg(String msg){
        showMessage(msg, JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     *
     * @param msg
     */
    public static void showErrorMsg(String msg){
        println(msg);
        showMessage(msg, JOptionPane.ERROR_MESSAGE);
    }
    /**
     *
     * @param msg
     * @param exception
     */
    public static void showException(Exception exception){
        logError("", exception);
        showErrorMsg("Exception :\n"+exception.getLocalizedMessage());
    }
    /**
     *
     * @param msg
     * @param exception
     */
    public static void showException(String msg, Exception exception){
        logError(msg, exception);
        showErrorMsg("Exception "+msg+":\n"+exception.getMessage());
    }
    /**
     *
     * @param message
     * @param type
     */
    private static void showMessage(String message, int type){
        if(message == null) message = "null";
        message = message.replaceAll(";", "\n");
        JTextArea ta = new JTextArea(message);
        ta.setFont(new Font("MONOSPACED", Font.PLAIN, 14));
        JScrollPane sp = new JScrollPane(ta);
        if((ta.getPreferredSize().height+30)>500){
            sp.setPreferredSize(new Dimension(
                                ta.getPreferredSize().width+30,
                                500));
        }else{
            sp.setPreferredSize(new Dimension(
                                ta.getPreferredSize().width+30,
                                ta.getPreferredSize().height+30));
        }
        JOptionPane.showMessageDialog(null, ta, "", type);
    }
    /**
     * 
     * @return
     */
    private static PrintWriter getPrintWriter(){
        try{
            File directory = new File(home+"/LogReports");
            if(directory.exists() || directory.mkdir()){
                DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
                String day =  dateFormat.format(new Date());
                File f = new File(home+"/LogReports/"+day+".txt");
                return new PrintWriter(new FileWriter(f, true), true);
            }else{
                File f = new File(home+"/sdd_log.txt");
                return new PrintWriter(new FileWriter(f, true), true);
            }
        }catch(Exception e){
            return new PrintWriter(System.out);
        }
    }
    /**
     *
     * @return date string
     */
    public static String time(){
        String timeZone = "IST";//"GMT"
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        f.setTimeZone(TimeZone.getTimeZone(timeZone));
        return f.format(new Date())+" "+timeZone;
    }
    /**
     *
     * @return date string
     */
    public static String timeCompact(){
        String timeZone = "IST";
        DateFormat f = new SimpleDateFormat("ddMMyy_HHmm");
        f.setTimeZone(TimeZone.getTimeZone(timeZone));
        return f.format(new Date());
    }
}