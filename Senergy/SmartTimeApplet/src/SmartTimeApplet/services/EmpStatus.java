/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services;

import java.awt.Color;

/**
 *
 * @author pradnya
 */
public class EmpStatus {

    public String getStatus(String status, String rptName) {
        Color in = null;
        Color out = null;
        String strstatus = "";
        AttendanceDetail.bgColor = null;
        switch (rptName) {

            case "AttendanceReport":
                switch (status) {
                    case "HH":
                        strstatus = "Holiday";
                        in = new Color(22, 154, 85);//green
                        out = new Color(22, 154, 85);//green
                        break;
                    case "HH*":
                        strstatus = "<html>Work on Holiday</html>";
                        in = new Color(22, 154, 85);//green
                        out = new Color(22, 154, 85);//green
                        break;
                    case "HHL":
                        strstatus = "<html>Late Coming on Holiday</html>";
                        in = new Color(255, 0, 0);//Red
                        out = new Color(22, 154, 85);//green
                        break;
                    case "HHE":
                        strstatus = "<html>Early Going on Holiday</html>";
                        in = new Color(22, 154, 85);//green
                        out = new Color(255, 0, 0);//Red
                        break;
                    case "HH#":
                        strstatus = "<html>Late Coming & Early Going onHoliday</html>";
                        in = new Color(255, 0, 0);//Red
                        out = new Color(255, 0, 0);//Red
                        break;
                    case "HHS":
                        strstatus = "<html>Came In Other Shift onHoliday</html>";
                        break;
                    case "HHI":
                        in = new Color(22, 154, 85);//green
                        out = new Color(22, 154, 85);//green
                        strstatus = "<html>InSwipe Missing Holiday</html>";//using html tag for wrapping long text
                        AttendanceDetail.bgColor = new Color(201, 249, 254);//(softblue)
                        break;
                    case "LL":
                        strstatus = "OnLeave";
                        in = new Color(22, 154, 85);//green
                        out = new Color(22, 154, 85);//green
                        break;
                    case "LLI":
                        strstatus = "InSwipe Missing OnLeave";
                        in = new Color(22, 154, 85);//green
                        out = new Color(22, 154, 85);//green
                        break;
                    case "LLL":
                        strstatus = "<html>Late Coming OnLeave</html>";
                        in = new Color(255, 0, 0);//Red
                        out = new Color(22, 154, 85);//green
                        break;
                    case "LLE":
                        strstatus = "<html>Early Going OnLeave</html>";
                        in = new Color(22, 154, 85);//green
                        out = new Color(255, 0, 0);//Red
                        break;
                    case "LL#":
                        strstatus = "<html>Late Coming & Early Going onLeave</html>";
                        in = new Color(255, 0, 0);//Red
                        out = new Color(255, 0, 0);//Red
                        break;
                    case "LLS":
                        strstatus = "<html>Came In Other Shift onLeave</html>";
                        break;
                    case "LL*":
                        in = new Color(22, 154, 85);//green
                        out = new Color(22, 154, 85);//green
                        strstatus = "<html>Worked OnLeave</html>";
                        AttendanceDetail.bgColor = new Color(250, 240, 247);//softpink
                        break;
                    case "OD":
                        strstatus = "OutDoor";
                        AttendanceDetail.bgColor = new Color(255, 255, 224);//softyellow
                        break;
                    case "XX":
                        strstatus = "Present";
                        in = new Color(22, 154, 85);//green
                        out = new Color(22, 154, 85);//green
                        break;
                    case "XX#":
                        strstatus = "<html>Late Coming & Early Going</html>";
                        in = new Color(255, 0, 0);//Red
                        out = new Color(255, 0, 0);//Red
                        break;
                    case "XX*":
                        strstatus = "OverTime";
                        in = new Color(22, 154, 85);//green
                        out = new Color(22, 154, 85);//green
                        break;
                    case "XXL":
                        strstatus = "Late Coming";
                        in = new Color(255, 0, 0);//Red
                        out = new Color(22, 154, 85);//green
                        break;
                    case "XXE":
                        strstatus = "Early Going";
                        in = new Color(22, 154, 85);//green
                        out = new Color(255, 0, 0);//Red
                        break;
                    case "XXS":
                        strstatus = "<html>Came In Other Shift</html>";
                        break;
                    case "AA*":
                        strstatus = "InSwipeMissing";
                        in = new Color(255, 0, 0);//red
                        out = new Color(22, 154, 85);//green
                        break;
                    case "AA#":
                        strstatus = "<html>OutSwipe Missing</html>";
                        in = new Color(22, 154, 85);//green
                        out = new Color(255, 0, 0);//red
                        AttendanceDetail.bgColor = new Color(255, 227, 227);//softpink
                        break;
                    case "AA":
                        strstatus = "Absent";
                        AttendanceDetail.bgColor = new Color(250, 214, 212);//softred
                        break;
                    case "WW":
                        strstatus = "WeekOff";
                        break;
                    case "WW#":
                        strstatus = "<html>Late Coming & Early Going OnWeekOff</html>";
                        break;
                    case "WW*":
                        strstatus = "Worked on WeekOff";
                        AttendanceDetail.bgColor = new Color(171, 227, 254);//blue
                        break;
                    case "WWL":
                        strstatus = "<html>Late Coming On WeekOff</html>";
                        AttendanceDetail.bgColor = new Color(171, 227, 254);//blue
                        break;
                    case "WWE":
                        strstatus = "<html>Early Going On WeekOff</html>";
                        AttendanceDetail.bgColor = new Color(171, 227, 254);//blue
                        break;
                    case "WWI":
                        strstatus = "<html>InSwipe Missing WeekOff</html>";
                        AttendanceDetail.bgColor = new Color(171, 227, 254);//blue
                        break;
                    case "WWS":
                        strstatus = "<html>Came in other shift on WeekOff</html>";
                        AttendanceDetail.bgColor = new Color(171, 227, 254);//blue
                        break;
                    default:
                        strstatus = "";
                        AttendanceDetail.bgColor = new Color(255, 255, 255);//white
                        break;
                }
                break;
            case "AbsentReport":
                switch (status) {
                    case "AA":
                        strstatus = "Absent";
                        AttendanceDetail.bgColor = new Color(250, 214, 212);//softred
                        break;
                    default:
                        AttendanceDetail.bgColor = new Color(255, 255, 255);//white
                        break;
                }
                break;
            case "OutDoorDuty":
                switch (status) {
                    case "OD":
                        strstatus = "OutDoor";
                        AttendanceDetail.bgColor = new Color(255, 255, 224);//sofyellow
                        break;
                    default:
                        AttendanceDetail.bgColor = new Color(255, 255, 255);//white
                        break;
                }
                break;
            case "WeekOffApplet":
                switch (status) {
                    case "WW":
                    case "WW#":
                    case "WW*":
                        strstatus = "WeekOff";
                        AttendanceDetail.bgColor = new Color(180, 237, 254);//blue
                        break;
                    default:
                        AttendanceDetail.bgColor = new Color(255, 255, 255);//white
                        break;
                }
                break;
            case "OverTimeApplet":
                switch (status) {
                    case "XX*":
                        strstatus = "OverTime";
                        AttendanceDetail.bgColor = new Color(255, 227, 255);//softpink
                        break;
                    default:
                        AttendanceDetail.bgColor = new Color(255, 255, 255);//white
                        break;
                }
                break;
            case "OnLeaveReport":
                switch (status) {
                    case "LL":
                    case "LL#":
                    case "LL*":
                    case "LLS":
                    case "LLE":
                    case "LLL":
                        strstatus = "OnLeave";
                        AttendanceDetail.bgColor = new Color(227, 250, 250);//softgreen
                        break;
                    default:
                        AttendanceDetail.bgColor = new Color(255, 255, 255);//white
                        break;
                }
                break;
            case "EarlyReport":
                switch (status) {
                    case "XXE":
                        strstatus = "Early Going";
                        out = new Color(255, 0, 0);//red
                        AttendanceDetail.bgColor = new Color(255, 227, 227);//softpink
                        break;
                    default:
                        AttendanceDetail.bgColor = new Color(255, 255, 255);//white
                        break;
                }
                break;
            case "LateReport":
                switch (status) {
                    case "XXL":
                        strstatus = "Late Coming";
                        in = new Color(255, 0, 0);//red
                        AttendanceDetail.bgColor = new Color(227, 227, 227);//softpink
                        break;
                    default:
                        AttendanceDetail.bgColor = new Color(255, 255, 255);//white
                        break;
                }
                break;
            case "SingleSwipe":
                switch (status) {
                    case "AA*":
                        strstatus = "<html>InSwipe Missing</html>";
                        in = new Color(255, 0, 0);//red
                        AttendanceDetail.bgColor = new Color(227, 227, 227);//softpink
                        break;
                    case "AA#":
                        strstatus = "<html>OutSwipe Missing</html>";
                        out = new Color(255, 0, 0);//red
                        AttendanceDetail.bgColor = new Color(255, 227, 227);//softpink
                        break;
                    default:
                        AttendanceDetail.bgColor = new Color(255, 255, 255);//white
                        break;
                }
                break;

        }

        AttendanceDetail.inColor = in;
        AttendanceDetail.outColor = out;
        status = "";
        return strstatus;
    }
}
