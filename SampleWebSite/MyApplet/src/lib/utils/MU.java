package lib.utils;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
/**
 */
public class MU {
    public static final String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    public static final String DATE_FORMAT_FULL = "dd/MM/yyyy HH:mm:ss";
    /**
     * convert java.util.Date to string
     * @param d
     * @return string in DATE_FORMAT_FULL format
     */
    public static String dateToStringFull(Date d){
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_FULL);
        String date = df.format(d);
        return date;
    }
    /**
     * get string in DATE_FORMAT_FULL format and convert to java.util.Date
     * @param s
     * @return java.util.Date or null if invalid format
     */
    public static Date fullStringToDate(String s) throws Exception{
       SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_FULL);
       Date d = df.parse(s);
       return d;
    }
    /**
     * convert java.util.Date to string
     * @param d
     * @return string in DATE_FORMAT_SHORT format
     */
    public static String dateToString(Date d){
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_SHORT);
        String date = df.format(d);
        return date;
    }
    /**
     * get string in DATE_FORMAT_SHORT format and convert to java.util.Date
     * @param s
     * @return java.util.Date or null if invalid format
     */
    public static Date stringToDate(String s) throws Exception{
       SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_SHORT);
       Date d = df.parse(s);
       return d;
    }

    /**
     *
     * @param dd_mm_yyyy - in dd/mm/yyyy
     * @return '' - calendar
     * @throws Exception
     */
    public static Calendar convertToCalendar(String dd_mm_yyyy) throws Exception{
        StringTokenizer st = new StringTokenizer(dd_mm_yyyy, "/");
        int dd = Integer.parseInt(st.nextToken());
        int mm = Integer.parseInt(st.nextToken());
        int yy = Integer.parseInt(st.nextToken());
        Calendar applyDate = Calendar.getInstance();
        applyDate.clear();
        applyDate.set(yy, mm-1, dd);//*** mm is 0-11
        return applyDate;
    }

    /**
     * @param bytes
     * @return
     */
    public static String hexify(byte[] bytes) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7',
                            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder buffer = new StringBuilder();
        buffer.append(hexDigits[(bytes[0] & 0xf0) >> 4]);
        buffer.append(hexDigits[bytes[0] & 0x0f]);
        for (int i = 1; i < bytes.length; ++i) {
            //buffer.append(':');
            buffer.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
            buffer.append(hexDigits[bytes[i] & 0x0f]);
        }
        return buffer.toString();
    }
    /**
     *
     * @param number
     * @param digits
     * @return
     */
    public static String format(int number, int digits) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(digits);
        return nf.format(number);
    }
    /**
     *
     * @param msg
     */
    public static boolean confirm(String msg){
        if(JOptionPane.showConfirmDialog(null, msg, "",
                JOptionPane.YES_NO_OPTION)==JOptionPane.OK_OPTION){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 
     * @param dir
     * @throws Exception
     */
    public static File checkDirectoryOK(String path) throws Exception{
        File dir = new File(path);
        if(dir.exists()){
            if(!dir.isDirectory()){
                throw new Exception(dir.getName()+"NotADirectory");
            }else{
                return dir;
            }
        }else{
            if(MU.confirm("Directory '"+dir.getName()+"' does not exists.\nDo want to create it?")){
                if(!dir.mkdirs()){
                    throw new Exception(dir.getName()+" InvalidDirectoryName");
                }else{
                    return dir;
                }
            }else{
                throw new Exception("OperationCanceled!");
            }
        }
    }
}