package lib.utils;

import SmartTimeApplet.EmpEntryReport.EmpDailyEntryReport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponent;
import net.sourceforge.jdatepicker.JDatePicker;
/**
 * DD/MM/YYYY
 * DD/MM/YYYY HH:MM:SS (in 24 hrs)
 */
public class MyDate extends java.util.Date{
    public static final String FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String FORMAT_DD_MM_YYYY_HH_MI_SS = "dd/MM/yyyy HH:mm:ss";
    /**
     * ---------------Java Date Format
     * y  Year  Year  1996; 96
     * M  Month in year  Month  July; Jul; 07 
     * d  Day in month  Number  10
     * H  Hour in day (0-23)  Number  0
     * m  Minute in hour  Number  30
     * s  Second in minute  Number  55
     * E  Day in week  Text  Tuesday; Tue
     * F  Day of week in month  Number  2
     * S  Millisecond  Number  978
     * h  Hour in am/pm (1-12)  Number  12
     * a  Am/pm marker  Text  PM
     * D  Day in year  Number  189
     * k  Hour in day (1-24)  Number  24
     * K  Hour in am/pm (0-11)  Number  0
     * z  Time zone  General time zone  Pacific Standard Time; PST; GMT-08:00
     * Z  Time zone  RFC 822 time zone  - 0800
     * --------------Oracle Date Format
     * MM Numeric month (e.g., 07)
     * MON Abbreviated month name (e.g., JUL)
     * MONTH Full month name (e.g., JULY)
     * DD Day of month (e.g., 24)
     * DY Abbreviated name of day (e.g., FRI)
     * YYYY 4-digit year (e.g., 1998)
     * YY Last 2 digits of the year (e.g., 98)
     * RR Like YY, but the two digits are rounded to a year in the range 1950 to 2049.
     * AM (or PM) Meridian indicator
     * HH Hour of day (1-12)
     * HH24 Hour of day (0-23)
     * MI Minute (0-59)
     * SS Second (0-59)
     */
    /**
     *
     * @param type
     * @throws Exception
     */
    public MyDate(){
        super();
    }
    /**
     *
     * @param type
     * @throws Exception
     */
    public MyDate(String dateString, String format) throws Exception{
        super();
        DateFormat f = new SimpleDateFormat(format);
        f.setTimeZone(TimeZone.getTimeZone("IST"));
        setTime(f.parse(dateString).getTime());
    }
    /**
     *
     * @param type
     * @throws Exception
     */
    public MyDate(java.sql.Date sqlDate){
        //super();
        setTime(sqlDate.getTime());
    }
    /**
     * Zone is IST
     * @param dateString
     * @param format
     * @throws Exception
     */
    public void setDate(String dateString, String format)  throws Exception{
        DateFormat f = new SimpleDateFormat(format);
        f.setTimeZone(TimeZone.getTimeZone("IST"));
        setTime(f.parse(dateString).getTime());
    }
    /**
     *
     * @return
     */
    public java.sql.Date getSQLDate(){
        java.sql.Date sqlDate = new java.sql.Date(this.getTime());
        return sqlDate;
    }
    /**
     * 
     * @param seconds
     * @return
     */
    public MyDate addSeconds(int seconds){
        long t1 = this.getTime();
        MyDate d = new MyDate();
        d.setTime(t1+seconds*1000);
        return d;
    }
    /**
     *
     * @return
     */
    @Override
    public String toString(){
        DateFormat f = new SimpleDateFormat(FORMAT_DD_MM_YYYY);
        f.setTimeZone(TimeZone.getTimeZone("IST"));
        return f.format(this);
    }

    /**
     *
     * @return
     */
    public String toString(String format){
        DateFormat f = new SimpleDateFormat(format);
        f.setTimeZone(TimeZone.getTimeZone("IST"));
        return f.format(this);
    }
    /**
     * returns date string of JDatePicker in yyyy-MM-dd format
     * @param picker
     * @return 
     */
    public static String getDate(JDatePicker picker) throws Exception{
        DateModel model = ((JDateComponent) picker).getModel();
        String date1 = model.getYear() + "-" + (model.getMonth() + 1) + "-" + (model.getDay());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = dateFormat.parse(date1);
        String caldate = dateFormat.format(dt);
        return caldate;
    }
}