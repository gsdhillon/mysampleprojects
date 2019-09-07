package SmartTimeApplet.reports;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import lib.utils.Depacketizer;

public class EmpAttendance {

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
    public String hrsWorkedExcel;
    public String currdate = "";
    public boolean isholiday = false;

    /**
     *
     * @param month_0_11
     * @param yyyy
     */
    public EmpAttendance(int month_0_11, int yyyy) {
        this.calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(yyyy, month_0_11, 1, 0, 0, 0);
    }

    public void setDay(int day) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        currdate = dateFormat.format(calendar.getTime());
        isholiday = getHoliday();
        if (isholiday == true) {
            hrsWorkedExcel = "HH";
        } else {
            hrsWorkedExcel = "AA";
        }
    }

    public void setData(Depacketizer d) throws Exception {
        String inTimeString = d.getString();
        if (!"00-00-00#00:00:00".equals(inTimeString) && inTimeString.length() >= 17) {
            inTime = DATE_TIME_FORMAT.parse(inTimeString);
            inTextExcel = TIME_FORMAT.format(inTime);
        } else {
            inMissing = true;
        }
        String outTimeString = d.getString();
        if (!"00-00-00#00:00:00".equals(outTimeString) && outTimeString.length() >= 17) {
            outTime = DATE_TIME_FORMAT.parse(outTimeString);
            outTextExcel = TIME_FORMAT.format(outTime);
        } else {
            outMissing = true;
        }
        String template = d.getString();
        String tempearly = d.getString();
//        lateArrival = !d.getString().equals("00:00:00");
//        earlyDeparture = !d.getString().equals("00:00:00");
        status = d.getString();
        if (status.length() == 3) {
            String strStatus = status.substring(0, 2);
            switch (strStatus) {
                case "WW":
                    hrsWorkedExcel = "WW";
                    break;
                case "OD":
                    hrsWorkedExcel = "OD";
                    break;
                case "CF":
                    hrsWorkedExcel = "CF";
                    break;
            }
            char ch = status.charAt(2);
            if (ch == 'L') {//for late coming
                lateArrival = true;
            } else if (ch == 'E') {//for early going
                earlyDeparture = true;
            }
        } else if (status.length() == 2) {
            switch (status) {
                case "WW":
                    hrsWorkedExcel = "WW";
                    break;
                case "OD":
                    hrsWorkedExcel = "OD";
                    break;
                case "CF":
                    hrsWorkedExcel = "CF";
                    break;
            }
        }

        //
        if (((!"0000".equals(inTextExcel)) && (!"".equals(inTextExcel))) && ((!"0000".equals(outTextExcel)) && (!"".equals(outTextExcel)))) {
            int secondsOut = (int) (outTime.getTime() / 1000);
            int secondsIn = (int) (inTime.getTime() / 1000);
            secondsWorked = (secondsOut - secondsIn);
            int minutesWorked = secondsWorked / 60;
            NumberFormat nf = new DecimalFormat("00");
            int hrs = minutesWorked / 60;
            int minutes = minutesWorked % 60;
            hrsWorkedExcel = nf.format(hrs) + "." + nf.format(minutes);
        } else {
            hrsWorkedExcel = "AA";
        }
        if ((("0000".equals(inTextExcel)) || ("".equals(inTextExcel))) || (("0000".equals(outTextExcel)) || ("".equals(outTextExcel)))) {
            if (("0000".equals(inTextExcel)) || ("".equals(inTextExcel))) {
                inTextExcel = "AA*";
            } else if (("0000".equals(outTextExcel)) || ("".equals(outTextExcel))) {
                outTextExcel = "AA#";
            }
        } else if ((status.length() == 2) && (!"XX".equals(status))) {
            inTextExcel = "";
            outTextExcel = "";
            hrsWorkedExcel = status;
        } else {
            status = "PP";
        }
    }

    public float getDayFullOrHalf() {
        if ((!"".equals(status)) && (status != null) && (status.equals("PP"))) {
            return 1.0f;
        } else {
            return 0;
        }
    }

    public boolean isOnLeave() {
        if ((!"".equals(status)) && (status != null) && (status.equals("LL") || status.equals("CF"))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAbscent() {
        if (!"HH".equals(hrsWorkedExcel)) {
            if (((status != null) && (status.equals("AA"))) || status.equals("")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isHoliday() {
        return isholiday;
    }

    public boolean getHoliday() {
        boolean holiday = false;
        try {////
            MyHTTP myHTTP = MyUtils.createServletConnection("MusterServlet");
            myHTTP.openOS();
            myHTTP.println("isHoliday");
            myHTTP.println(currdate);
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                holiday = false;
            }
            if ("true".equals(response)) {
                holiday = true;
            } else {
                holiday = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyUtils.showMessage("ERROR:Could not get attendance records!\n" + e.getMessage());
            holiday = false;
        }
        return holiday;
    }
}
