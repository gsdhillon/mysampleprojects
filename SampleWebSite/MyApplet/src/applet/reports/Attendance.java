package applet.reports;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import lib.utils.Depacketizer;

/**
 * Class Attendance
 * Created on Nov 3, 2012
 * @version 1.0.0
 * @author
 */
public class Attendance {
    private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yy-MM-dd#HH:mm:ss");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HHmm");
    private Calendar calendar;
    public Date inTime = null;
    public Date outTime = null;
    public boolean lateArrival = false;
    public boolean inMissing = false;
    public boolean earlyDeparture = false;
    public boolean outMissing = false;
    public String status = "";//AA/HH/WW/LL/CF
    public int secondsWorked = 0;
    //
    public String inTextExcel = "";
    public String outTextExcel = "";
    public String hrsWorkedExcel = "";
    /**
     *
     * @param month_0_11
     * @param yyyy
     */
    public Attendance(int month_0_11, int yyyy) {
        this.calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(yyyy, month_0_11, 1, 0, 0, 0);
    }
    /**
     *
     * @param day
     */
    public void setDay(int day) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }
    /**
     *
     * @param d
     * @throws Exception
     */
    public void setData(Depacketizer d) throws Exception {
        String inTimeString = d.getString();
        if (inTimeString != null && inTimeString.length() >= 17) {
            inTime = DATE_TIME_FORMAT.parse(inTimeString);
            inTextExcel = TIME_FORMAT.format(inTime);
        }else{
            inMissing = true;
        }
        String outTimeString = d.getString();
        if (outTimeString != null && outTimeString.length() >= 17) {
            outTime = DATE_TIME_FORMAT.parse(outTimeString);
            outTextExcel = TIME_FORMAT.format(outTime);
        }else{
            outMissing = true;
        }
        lateArrival = d.getString().equals("Y");
        earlyDeparture = d.getString().equals("Y");
        status = d.getString();
        //
        if (inTime != null && outTime != null) {
            int secondsOut = (int) (outTime.getTime() / 1000);
            int secondsIn = (int) (inTime.getTime() / 1000);
            secondsWorked = (secondsOut - secondsIn);
            int minutesWorked = secondsWorked / 60;
            NumberFormat nf = new DecimalFormat("00");
            int hrs = minutesWorked / 60;
            int minutes = minutesWorked % 60;
            hrsWorkedExcel = nf.format(hrs) + "." + nf.format(minutes);
        }else{
            hrsWorkedExcel = "AA";
        }
        //
        if( status == null || status.length() < 2){
            if(inTime == null){
                inTextExcel = "AA*";
            }
            if(outTime == null){
                outTextExcel = "AA#";
            }
            //set status field to FULL of HALF day working
            if(inTime != null && outTime != null){
                status = "PP";//Full day working
            }else{
                status = "AA";//Absent
            }
        }else{
            hrsWorkedExcel = status;
            //check half CL/Half Coff etc and set status of half day working
        }
    }
    /**
     *
     * @return
     */
    public float getDayFullOrHalf(){
        if(status != null && status.equals("PP")){
            return 1.0f;
        }else{
            return 0;
        }
    }

    /**
     *
     * @return
     */
    public boolean isOnLeave(){
        if(status != null && (status.equals("LL") || status.equals("CF"))){
            return true;
        }else{
            return false;
        }
    }
    /**
     *
     * @return
     */
    public boolean isAbscent(){
        if(status != null && status.equals("AA")){
            return true;
        }else{
            return false;
        }
    }
}
