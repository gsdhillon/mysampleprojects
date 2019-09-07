package lib;

import java.text.DecimalFormat;
import javax.swing.JCheckBox;

/**
 */
public class User {

    public JCheckBox checkBox;
    public String userID;
    public String name;
    public String desig;
    public String division;
    public String section;
//    public MyDate dob;
    //attendance aggregate values
    public String shiftPattern = "13";
    public int numLate = 0;
    public int numEarly = 0;
    public int numLeave = 0;
    public int numAbsent = 0;
    public int avgSecondsWorked = 0;

    /**
     *
     * @return
     */
    public String getAvgHrsWrk() {
        int minutesWorked = avgSecondsWorked / 60;
        DecimalFormat nf = new DecimalFormat("00");
        int hrs = minutesWorked / 60;
        int minutes = minutesWorked % 60;
        String avgHrsWorked = nf.format(hrs) + "." + nf.format(minutes);
        return avgHrsWorked;
    }
}