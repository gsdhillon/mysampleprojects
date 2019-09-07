/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.Utility;

import lib.session.MyUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponent;

/**
 *
 * @author pradnya
 */
public class DateUtilities {

    public static String getDate(JComponent datepicker) {
        String month, day;
        DateModel<?> model = ((JDateComponent) (datepicker)).getModel();
        month = (model.getMonth() + 1) < 10 ? "0" + (model.getMonth() + 1) : "" + (model.getMonth() + 1);
        day = model.getDay() < 10 ? "0" + model.getDay() : "" + model.getDay();
        String date = model.getYear() + "-" + month + "-" + day;
        return date;
    }

    public static String getTimeFromJSpinner(String SpinnerValue) {
        String value[] = SpinnerValue.split(" ");
        return value[3];
    }

    public static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentDateTimeWithoutColon() {
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDateWithoutColon(String yyyy_MM_dd) {
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        Date date = null;
        try {
            date = dateFormat.parse(yyyy_MM_dd);
        } catch (ParseException ex) {
            Logger.getLogger(DateUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateFormat.format(date);
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static Date getTime(String time) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, Integer.parseInt(time.substring(0, 2)),
                Integer.parseInt(time.substring(3, 5)), Integer.parseInt(time.substring(6, 8)));
        return cal.getTime();
    }

    public static boolean checkDateSelected(JComponent component, String msg) {
        DateModel model1 = ((JDateComponent) (component)).getModel();
        if ((!model1.isSelected())) {
            MyUtils.showMessage(msg + " not Selected");
            return false;
        }
        return true;
    }

    public static void setSpinnerTime(JSpinner spinner, String value) {
        spinner.setValue(getTime(value));
    }

    public static String getSpinnerTime(JSpinner spinner) {
        String spinner2 = spinner.getValue().toString();
        String[] split2 = spinner2.split(" ");
        return split2[3];
    }

    public static String getTimeDiff(String time1, String time2) throws ParseException {
        String[] split1 = time1.split(":");
        String[] split2 = time2.split(":");
        Integer[] Time = new Integer[3];
        String WorkHour = "";
        for (byte i = 0; i <= 2; i++) {
            if (Integer.parseInt(split1[i]) > Integer.parseInt(split2[i])) {
                if (i == 0) {
                    Time[i] = (Integer.parseInt(split2[i]) + 24) - Integer.parseInt(split1[i]);
                } else {
                    Time[i] = (Integer.parseInt(split2[i]) + 60) - Integer.parseInt(split1[i]);
                }
            } else {
                Time[i] = Integer.parseInt(split2[i]) - Integer.parseInt(split1[i]);
                if (i == 0) {
                    if ((Integer.parseInt(split1[0])) != (Integer.parseInt(split2[0]))) {
                        if ((Integer.parseInt(split1[1])) > (Integer.parseInt(split2[1]))) {
                            Time[i] = Time[i] - 1;
                        }
                    }
                }
            }

            if (i == 0) {
                if (Integer.toString(Time[i]).length() == 1) {
                    WorkHour = "0" + new Integer(Time[i]).toString();
                } else {
                    WorkHour = new Integer(Time[i]).toString();
                }
            } else {
                if (Integer.toString(Time[i]).length() == 1) {
                    WorkHour = WorkHour + ":0" + new Integer(Time[i]).toString();
                } else {
                    WorkHour = WorkHour + ":" + new Integer(Time[i]).toString();
                }
            }
        }
        return WorkHour;
    }

    public static long getTimeDiffInSeconds(String time1, String time2) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(time1);
        Date date2 = format.parse(time2);
        long difference = (date2.getTime() - date1.getTime()) / 1000;
//        String time = (((difference / 3600) < 10 ? "0" + (difference / 3600) : "" + (difference / 3600)) //hours
//                + ":" + (((difference % 3600) / 60) < 10 ? "0" + ((difference % 3600) / 60) : "" + ((difference % 3600) / 60)) //minutes
//                + ":" + ((difference % 60) < 10 ? "0" + (difference % 60) : "" + (difference % 60)));//seconds
        return difference;
    }

    public static Date convertStringToDate(String strdate) {
        Date date = null;
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = (Date) formatter.parse(strdate);
        } catch (ParseException ex) {
            MyUtils.showMessage(" convertStringToDate : " + ex);
        }
        return date;
    }

    public static String changeFormateOfDate(String strdd_MM_yyyy) {
        String reformattedStr = "";
        try {
            SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
            Date dt = fromUser.parse(strdd_MM_yyyy);
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            reformattedStr = myFormat.format(dt);

        } catch (ParseException ex) {
            Logger.getLogger(DateUtilities.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("reformattedStr : " + reformattedStr + " ex: " + ex);
        }
        return reformattedStr;
    }

    public static String changeFormateOfDatedd_MM_YYYY(String stryyyy_MM_dd) {
        String reformattedStr = "";
        try {
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = fromUser.parse(stryyyy_MM_dd);
            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
            reformattedStr = myFormat.format(dt);

        } catch (ParseException ex) {
            Logger.getLogger(DateUtilities.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("reformattedStr : " + reformattedStr + " ex: " + ex);
        }
        return reformattedStr;
    }

    public static int getDayOfWeek(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(convertStringToDate(date));
        int day = cal.get(Calendar.DAY_OF_WEEK);//sunday=1 and saturday=7
        return day;
    }
}
