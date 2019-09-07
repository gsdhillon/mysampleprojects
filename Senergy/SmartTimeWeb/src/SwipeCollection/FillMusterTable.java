/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SwipeCollection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javacodes.connection.DBConnection;

/**
 *
 * @author pradnya
 */
public class FillMusterTable {

    boolean isholiday = false;
    boolean isWeekOff = false;
    boolean isOnLeave = false;
    boolean isCompOff = false;
    boolean isOverTime = false;
    boolean isAbsent = false;
    boolean isOutSwipemiss = false;
    boolean iscame_in_another_shift = false;
    String str_work_place = "";
    String str_emp_shift = "";
    String str_category_code = "";
    byte bytweek_off1 = 0, bytweek_off2 = 0;
    byte bytStatus = 1;
    String strleave_code = "";
    byte is_single_swipe_allow = 0;//0-nothing,1-not allow,2-allow
    Date dtvalidity;
    Date dtswipedate;
    String login_time = "00:00:00";
    String logout_time = "00:00:00";
    String late_time = "00:00:00";
    String early_time = "00:00:00";
    String extra_work = "00:00:00";
    String strvalidity;
    String strswipedate;
    String swipetime = "00:00:00";
    String strleave_nature = "";
    int reader_type;
    /*
     * use to retrieve category details category
     */
    String grace_late_time = "00:00:00";
    String grace_early_time = "00:00:00";
    String over_time_limit = "00:00:00";
    //Shift Details
    String strshiftstarttime = "09:00:00";
    String strshiftendtime = "17:00:00";
    String strworkhours = "08:00:00";
    int intshifttype;
    String temp_shift;//temporary shift if shift is not available in shiftroster
    String swipeDateTime;
    String empcode;

    public FillMusterTable() {
    }

    public FillMusterTable(DBConnection conn,
            Statement stmt,
            String swipeDateTime,
            String empcode,
            String readerIP,
            String reader_access) {
        try {
            this.swipeDateTime = swipeDateTime;
            this.empcode = empcode;
            String tempdatetime[] = swipeDateTime.split(" ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            strswipedate = tempdatetime[0]; //Date
            swipetime = tempdatetime[1];    //Time
            isholiday = checkHoliday(conn, stmt, strswipedate); //Checking holiday
            if (!getEmployee(conn, stmt, empcode)) {//getting employee details
                return;
            } else {
            }
            try {
                if (!("".equals(strvalidity)) & (strvalidity != null)) {

                    dtvalidity = (Date) dateFormat.parse(strvalidity);//converting string into date
                }
                if (!("".equals(strswipedate)) & (strswipedate != null)) {
                    dtswipedate = (Date) dateFormat.parse(strswipedate);
                }
            } catch (ParseException ex) {
                Logger.getLogger(FillMusterTable.class.getName()).log(Level.SEVERE, null, ex);
                insertProcessFailedStatus(conn, stmt);//if any exception occures then processed field of swipedatatable should update to false
                return;
            }

//            if ((dtvalidity.compareTo(dtswipedate)) == 0 || (dtvalidity.compareTo(dtswipedate)) == 1) { //(dtvalidity.compareTo(dtswipedate)) == 0 gives if validity equals swipedate and (dtvalidity.compareTo(dtswipedate)) == 1 gives if validity greater than swipedate
            getCategoryDetails(conn, stmt, str_category_code);//get category details
            ShiftDetails(conn, stmt, empcode);//getShiftDetails of employee
            checkEmpLeave(conn, stmt, strleave_code, empcode); //CheckLeave
            isCompOff = checkCompOff(conn, stmt, strswipedate, empcode);//Check CompOff
            reader_type = getReaderAccess(conn, stmt, readerIP, reader_access);//getting reader access
            if ((reader_type == 1) || (reader_type == 3)) {
                calculateInSwipe(conn, stmt, strswipedate, empcode, swipetime);  //Add In Swipe
            } else if (reader_type == 2) {
                calculateOutSwipe(conn, stmt, strswipedate, empcode, swipetime);//Add out Swipe
            }
            calculateWorkHours(conn, stmt, empcode);  //calculating Over Time,working hours,Employee status
//            }
        } catch (SQLException ex) {
            Logger.getLogger(FillMusterTable.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("FillMusterTable :" + ex);
            insertProcessFailedStatus(conn, stmt);//if any exception occures then processed field of swipedatatable should update to false
        } catch (Exception ex) {
            Logger.getLogger(FillMusterTable.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("FillMusterTable :" + ex);
            insertProcessFailedStatus(conn, stmt);//if any exception occures then processed field of swipedatatable should update to false
        }
    }

    /**
     *
     * @param empcode = employees ID
     * @return
     */
    private boolean getEmployee(DBConnection conn, Statement stmt, String empcode) {
        boolean isEmployeePresent = false;
        try {
            conn.connect();
            stmt = conn.createStatement();

            String query = "select "
                    + "(select WLocation from worklocation where worklocation.wlocation_code=employeemaster.wlocation_code) as workplace,"
                    + "CatCode,"
                    + "WeekOff1,"
                    + "WeekOff2,"
                    + "ShiftCode,"
                    + "LeaveCode,"
                    + "SingSwAllowed,"
                    + "Validity "
                    + "from "
                    + "employeemaster "
                    + "where "
                    + "EmpCode='" + empcode + "'";
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    str_work_place = rs.getString(1);
                    str_category_code = rs.getString(2);
                    bytweek_off1 = (byte) rs.getInt(3);
                    bytweek_off2 = (byte) rs.getInt(4);
                    temp_shift = rs.getString(5);
                    strleave_code = rs.getString(6);
                    is_single_swipe_allow = (byte) rs.getInt(7);
                    strvalidity = rs.getString(8);
                    isEmployeePresent = true;
                } else {
                    isEmployeePresent = false;
                }
                rs.close();
                stmt.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(FillMusterTable.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Get Employee : " + ex);
            isEmployeePresent = false;
            insertProcessFailedStatus(conn, stmt);

        } finally {
            conn.close();
        }
        return isEmployeePresent;
    }

    /**
     *
     * @param swipedate = date on which employee has swipes the card
     * @return
     * @throws Exception
     */
    private boolean checkHoliday(DBConnection conn, Statement stmt, String swipedate) throws Exception {
        boolean is_holiday = false;

        conn.connect();
        stmt = conn.createStatement();

        String query = "select "
                + "HolidayName "
                + "from "
                + "holiday "
                + "where "
                + "HolidayDate='" + swipedate + "'";

        try (ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                is_holiday = true;//if holiday found on that day
            } else {
                is_holiday = false;//if holiday does not found on that day 
            }
            rs.close();
        } finally {
            conn.close();
            stmt.close();
        }
        return is_holiday;
    }

    /**
     *
     * @param conn
     * @param stmt
     * @param str_category_code
     * @throws SQLException
     */
    private void getCategoryDetails(DBConnection conn, Statement stmt, String str_category_code) throws SQLException {
        try {
            conn.connect();
            stmt = conn.createStatement();

            String query = "SELECT "
                    + "GraceLateTime,"
                    + "GraceEarlyTime,"
                    + "OverTimeLimit "
                    + "from "
                    + "category "
                    + "where "
                    + "CatCode='" + str_category_code + "'";

            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    grace_late_time = rs.getString(1);
                    grace_early_time = rs.getString(2);
                    over_time_limit = rs.getString(3);
                    rs.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("Get Category Details : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } finally {
            conn.close();
            stmt.close();
        }
    }

    /**
     *
     * @param conn
     * @param stmt
     * @param empcode
     */
    private void ShiftDetails(DBConnection conn, Statement stmt, String empcode) {
        String ymd[] = strswipedate.split("-");
        int day = Integer.parseInt(ymd[2]);//getting day no of month
        String col_day;
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dtswipedate);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); //getting day of week from prodate
            if (day > 0 && day < 10) {//making column name to getting shift from shiftroster
                col_day = "d0" + day;
            } else {
                col_day = "d" + day;
            }
            str_emp_shift = getShiftRoster(conn, stmt, col_day, empcode);//getShift from shiftroster
            getShiftDetails(conn, stmt, str_emp_shift, temp_shift, dayOfWeek);//get shift details from shiftmaster
        } catch (Exception ex) {
            System.out.println("Shift Details : " + ex);
            insertProcessFailedStatus(conn, stmt);
        }
    }

    /**
     *
     * @param conn
     * @param stmt
     * @param str_leavecode
     * @param empcode
     */
    private void checkEmpLeave(DBConnection conn, Statement stmt, String str_leavecode, String empcode) {
        try {
            conn.connect();
            stmt = conn.createStatement();
            byte is_off_trapped = 0;
            byte is_holiday_trapped = 0;
            String leave_type = null;
            String query;
            query = "SELECT "
                    + "LeaveNature "
                    + "from  "
                    + "leaveapplication "
                    + "where "
                    + "EmpCode='" + empcode + "' "
                    + "AND "
                    + "(FromDate < '" + strswipedate + "'  "
                    + "AND "
                    + "ToDate > '" + strswipedate + "')";
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    strleave_nature = rs.getString(1);
                    rs.close();
                    query = "SELECT "
                            + "IsOffTrapped,"
                            + "IsHolidayTrapped,"
                            + "`Type` "
                            + "from "
                            + "leaveconfig "
                            + "where "
                            + "LeaveCode='" + str_leavecode + "'";
                    try (ResultSet rs1 = stmt.executeQuery(query)) {
                        if (rs1.next()) {
                            is_off_trapped = (byte) rs1.getInt(1);
                            is_holiday_trapped = (byte) rs1.getInt(2);
                            leave_type = rs1.getString(3);
                            rs1.close();
                        }
                        if ((is_off_trapped == 1) || (is_holiday_trapped == 1)) {
                            switch (leave_type) {
                                case "On Leave":
                                    isOnLeave = true;
                                    isholiday = false;
                                    isWeekOff = false;
                                    break;
                                case "Week Off":
                                    isOnLeave = false;
                                    isholiday = false;
                                    isWeekOff = true;
                                    break;
                                case "Holiday":
                                    isOnLeave = false;
                                    isholiday = true;
                                    isWeekOff = false;
                                    break;
                                default:
                                    isOnLeave = true;
                                    isholiday = false;
                                    isWeekOff = false;
                                    break;
                            }
                        }
                    }
                } else {
                    isOnLeave = false;
                }
            }
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("checkEmpLeave : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } catch (Exception ex) {
            System.out.println("checkEmpLeave : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } finally {
            conn.close();
        }

    }

    /**
     *
     * @param conn
     * @param stmt
     * @param col_name
     * @param emp_code
     * @return
     * @throws SQLException
     */
    private String getShiftRoster(DBConnection conn, Statement stmt, String col_name, String emp_code) throws SQLException {
        String emp_shift = "";
        try {
            conn.connect();
            stmt = conn.createStatement();
            String query;
            query = "select "
                    + col_name
                    + " from "
                    + "shiftroster "
                    + "where "
                    + "EmpCode='" + emp_code + "'";
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    emp_shift = rs.getString(1);
                }
                rs.close();
            }
        } catch (Exception ex) {
            System.out.println("getShiftRoster : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } finally {
            conn.close();
            stmt.close();
        }
        return emp_shift;
    }

    /**
     *
     * @param conn
     * @param stmt
     * @param emp_shift
     * @param dayOfWeek
     * @throws SQLException
     */
    private void getShiftMaster(DBConnection conn, Statement stmt, String emp_shift, int dayOfWeek) throws SQLException {
        try {
            conn.connect();
            stmt = conn.createStatement();
            String query;
            query = "select "
                    + "StartTime1,"
                    + "EndTime2,"
                    + "WorkingHrs,"
                    + "ShiftType "
                    + "from "
                    + "shiftmaster "
                    + "where "
                    + "ShiftCode='" + emp_shift + "'";
            try (ResultSet rs = stmt.executeQuery(query)) {
                String strstarttime, strendtime, strwrkhr;
                if (rs.next()) {
                    str_emp_shift = emp_shift;
                    strstarttime = rs.getString(1);
                    strendtime = rs.getString(2);
                    strwrkhr = rs.getString(3);
                    intshifttype = rs.getInt(4);

                    if ((!"".equals(strstarttime)) & (strstarttime != null)) {
                        strshiftstarttime = strstarttime;
                    }
                    if ((!"".equals(strendtime)) & (strendtime != null)) {
                        strshiftendtime = strendtime;
                    }
                    if ((!"".equals(strwrkhr)) & (strwrkhr != null)) {
                        strworkhours = strwrkhr;
                    }
                } else {
                    str_emp_shift = "G";
                    if (bytweek_off1 == dayOfWeek) {
                        isWeekOff = true;
                    } else {
                        isWeekOff = false;
                    }
                    if (isWeekOff != true) {
                        if (bytweek_off2 == dayOfWeek) {
                            isWeekOff = true;
                        } else {
                            isWeekOff = false;
                        }
                    }
                }
                rs.close();
            }
        } catch (Exception ex) {
            System.out.println("getShiftMaster : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } finally {
            conn.close();
            stmt.close();
        }
    }

    /**
     * getting shift details of the employee
     *
     * @param emp_shift=employees shift which will calculate
     * @param temp_shift=employee default shift from employeemaster if shift has
     * not found in shiftroster
     * @param dayOfWeek=getting sequence number of day of week
     */
    private void getShiftDetails(DBConnection conn, Statement stmt, String emp_shift, String temp_shift, int dayOfWeek) {
        try {
            if (!"".equals(emp_shift)) {//if shift has found in the shift master
                if (!"00".equals(emp_shift)) {
                    getShiftMaster(conn, stmt, emp_shift, dayOfWeek); //if swipe day is not weekend then
                } else {
                    isWeekOff = true;
                }
            } else {//if shift has not found in the shift master
                getShiftMaster(conn, stmt, temp_shift, dayOfWeek);
            }
        } catch (SQLException ex) {
            System.out.println("getShiftDetails : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } catch (Exception e) {
            System.out.println("getShiftDetails : " + e);
        }
    }

    /**
     * Checking that employee has taken comp off or not
     *
     * @return true or false
     */
    private boolean checkCompOff(DBConnection conn, Statement stmt, String strswipedate, String empcode) {
        boolean is_comp_off = false;
        try {
            conn.connect();
            stmt = conn.createStatement();
            String query;
            query = "select "
                    + "status "
                    + "from "
                    + "compoffentry "
                    + "where "
                    + "EmpCode='" + empcode + "' "
                    + "and "
                    + "coffdate='" + strswipedate + "'";
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    is_comp_off = true;
                }
                rs.close();
            }
        } catch (Exception ex) {
            System.out.println("checkCompOff : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } finally {
            conn.close();
        }
        return is_comp_off;
    }

    /**
     * Getting Reader Access Of the employee
     *
     * @param readerIP
     * @param reader_access i.e In or Out or InOut
     * @return
     */
    private byte getReaderAccess(DBConnection conn, Statement stmt, String readerIP, String reader_access) {
        byte readertype = 0;
        byte readeraccess = 0;
        int int_reader_access = Integer.parseInt(reader_access);
        try {
            conn.connect();
            stmt = conn.createStatement();
            String query;
            query = "select "
                    + "ReaderType "
                    + "from "
                    + "readersetting "
                    + "where "
                    + "SelfIP='" + readerIP + "'";
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    readertype = (byte) rs.getInt(1);
                    rs.close();
                }
                if (readertype == 2) {               // IN/OUT
                    if (int_reader_access == 64 || int_reader_access == 128) {
                        readeraccess = 1;            // IN 
                    } else if (int_reader_access == 192 || int_reader_access == 224) {
                        readeraccess = 2;           // OUT 
                    } else {
                        readeraccess = 3;           // IN/OUT
                    }
                } else if (readertype == 0) {          // IN             
                    readeraccess = 1;
                } else if (readertype == 1) {           // OUT                  
                    readeraccess = 2;
                } else {
                    readeraccess = 0;
                }
            }
        } catch (Exception ex) {
            System.out.println("getReaderAccess : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } finally {
            conn.close();
        }
        return readeraccess;
    }

    /**
     * Calculating InSwipe of the employee
     */
    private void calculateInSwipe(DBConnection conn, Statement stmt, String strswipedate, String empcode, String swipe_time) {
        try {
            String strmusterlogin_time;
            int late_diff;
            int grace_late_min;
            int timediff;
            String query;
            byte isPresent = 1;//0-no need to do anything,1-insert new row,2-update existing row
            conn.connect();
            stmt = conn.createStatement();
            query = "select "
                    + "login "
                    + "from "
                    + "muster "
                    + "where "
                    + "EmpCode='" + empcode + "' "
                    + "AND "
                    + "Date='" + strswipedate + "'";
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    strmusterlogin_time = rs.getString(1);  //login time 
                    rs.close();
                    isPresent = 2;//if latest in swipe has found less than the swipe saved in muster then in swipe must be update
                    timediff = getTimeDiff(strmusterlogin_time, swipe_time);
                    if (!"00:00:00".equals(strmusterlogin_time)) {
                        if (timediff < 0) {
                            login_time = swipe_time;
                            isPresent = 2; //if latest in swipe has found less than the swipe saved in muster then in swipe must be update
                        } else {
                            login_time = strmusterlogin_time;
                            isPresent = 0;
                        }
                    } else {
                        login_time = swipe_time;
                    }

                } else {
                    isPresent = 1;//record not found insert new row
                    login_time = swipe_time;
                }
                late_diff = (getTimeDiff(strshiftstarttime, login_time)) / 60;//difference in minutes
                grace_late_min = (getTimeDiff("00:00:00", grace_late_time)) / 60;//difference in minutes
                if (late_diff > grace_late_min) {
                    //For Come In Another Shift And LAte Coming
                    if (late_diff > 360) {
                        iscame_in_another_shift = true;
                        bytStatus = 20;//came in other shift
                    } else {
                        iscame_in_another_shift = false;
                        int login_time_diff = getTimeDiff(strshiftstarttime, login_time);
                        late_time = (((login_time_diff) / 3600) + ":" + (((login_time_diff) % 3600) / 60) + ":" + (login_time_diff % 60));
                        bytStatus = 2;// Late Comming
                    }
                } else if (late_diff < 0) {
                    late_diff = late_diff * (-1);
                    if (late_diff > 360) {
                        iscame_in_another_shift = true;
                        bytStatus = 20;//came in other shift
                    } else {
                        iscame_in_another_shift = false;
                        bytStatus = 1;//
                    }
                } else {
                    bytStatus = 1;//present
                }
                rs.close();
            }
            addInSwipe(conn, stmt, empcode, isPresent);

        } catch (SQLException ex) {
            System.out.println("calculateInSwipe : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } catch (Exception ex) {
            System.out.println("calculateInSwipe : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } finally {
            conn.close();
        }
    }

    /**
     *
     * @param conn
     * @param stmt
     * @param empcode
     * @param isPresent
     */
    private void addInSwipe(DBConnection conn, Statement stmt, String empcode, byte isPresent) {
        try {
            conn.connect();
            stmt = conn.createStatement();
            String query;
            String emp_status = getEmpStatus(bytStatus);
            if (isPresent == 1) {
                query = "INSERT INTO muster("
                        + "EmpCode,"
                        + "ShiftCode,"
                        + "status,"
                        + "Remark,"
                        + "login,"
                        + "Date,"
                        + "late,"
                        + "LeaveNature,"
                        + "WorkingPlace) "
                        + "Values('"
                        + empcode + "','"
                        + str_emp_shift + "','"
                        + emp_status + "','"
                        + "" + "','"
                        + login_time + "','"
                        + strswipedate + "','"
                        + late_time + "','"
                        + strleave_nature + "','"
                        + str_work_place + "')";
                System.out.println("addInSwipe : " + query);
                stmt.executeUpdate(query);

            } else if (isPresent == 2) {
                query = "Update muster SET "
                        + "ShiftCode='" + str_emp_shift + "',"
                        + "status='" + emp_status + "',"
                        + "Remark='" + "" + "',"
                        + "login='" + login_time + "',"
                        + "late='" + late_time + "',"
                        + "LeaveNature='" + strleave_nature + "',"
                        + "WorkingPlace='" + str_work_place + "' "
                        + "WHERE `EmpCode` ='" + empcode + "' "
                        + "AND "
                        + "Date='" + strswipedate + "'";
                System.out.println("addInSwipe : " + query);
                stmt.executeUpdate(query);
            }
        } catch (Exception ex) {
            System.out.println("addInSwipe : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } finally {
            conn.close();
        }
    }

    /**
     * Giving Time Difference in seconds
     *
     * @param time1
     * @param time2
     * @return if time1 is grater than time2 then positive int else negative int
     * value
     * @throws ParseException
     */
    private int getTimeDiff(String time1, String time2) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(time1);
        Date date2 = format.parse(time2);
        long difference = date2.getTime() - date1.getTime();
        return (int) (difference / 1000);
    }

    /**
     * Calculating outswipe of the employee
     *
     * @param strswipedate = swipe date on which emp has swipes the card
     * @param empcode = employee ID
     * @param swipetime = swipe time
     */
    private void calculateOutSwipe(DBConnection conn, Statement stmt, String strswipedate, String empcode, String swipetime) {
        try {
            String strmusterlogout_time;
            String query;
            byte isPresent = 1;//0-no need to do anything,1-insert new row,2-update existing row
            int timediff;
            int grace_early_min;
            conn.connect();
            stmt = conn.createStatement();

            query = "select "
                    + "logout "
                    + "from "
                    + "muster "
                    + "where "
                    + "EmpCode='" + empcode + "' "
                    + "AND "
                    + "Date='" + strswipedate + "'";
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    strmusterlogout_time = rs.getString(1);
                    rs.close();
                    isPresent = 2;
                    timediff = getTimeDiff(strmusterlogout_time, swipetime);
                    if (!"00:00:00".equals(strmusterlogout_time)) {
                        if (timediff > 0) {
                            logout_time = swipetime;
                            isPresent = 2; //if latest in swipe has found greater than the swipe saved in muster then in swipe must be update
                        } else {
                            logout_time = strmusterlogout_time;
                            isPresent = 0;//0-no need to do anything,1-insert new row,2-update existing row
                        }
                    } else {
                        logout_time = swipetime;
                    }
                } else {
                    isPresent = 1;//if record has not found in the table
                    logout_time = swipetime;
                }
                int early_diff = (getTimeDiff(strshiftendtime, logout_time));//difference in seconds
                grace_early_min = (getTimeDiff("00:00:00", grace_early_time)) / 60;//difference in minutes

                if ((early_diff) < 0) {//early_diff converted seconds to minutes   //difference getting in minus value if emp has late so checking that emp has came late or not
                    early_diff = early_diff * (-1);
                    if ((early_diff / 60) > grace_early_min) {
                        bytStatus = 4;//early going
                        early_time = (((early_diff) / 3600) + ":" + (((early_diff) % 3600) / 60) + ":" + (early_diff % 60));
                    } else {
                        early_time = "00:00:00";
                    }
                }
            }
            addOutSwipe(conn, stmt, empcode, isPresent);
        } catch (Exception ex) {
            System.out.println("calculateOutSwipe : " + ex);
            insertProcessFailedStatus(conn, stmt);
        }
    }

    /**
     * adding all calculated values of outswipe in muster
     *
     * @param isPresent= 0 - no need to do anything,1=insert new row,2+update
     * existing row
     */
    private void addOutSwipe(DBConnection conn, Statement stmt, String empcode, byte isPresent) {
        try {
            conn.connect();
            stmt = conn.createStatement();
            String query;
            if (isPresent == 1) {//0-no need to do anything,1-insert new row,2-update existing row
                query = "INSERT INTO muster"
                        + "(EmpCode,"
                        + "ShiftCode,"
                        + "Remark,"
                        + "logout,"
                        + "Date,"
                        + "early,"
                        + "LeaveNature,"
                        + "WorkingPlace) "
                        + "Values('"
                        + empcode + "','"
                        + str_emp_shift + "','"
                        + "" + "','"
                        + logout_time + "','"
                        + strswipedate + "','"
                        + early_time + "','"
                        + strleave_nature + "','"
                        + str_work_place + "')";
                System.out.println("addOutSwipe : " + query);
                stmt.executeUpdate(query);

            } else if (isPresent == 2) {//0=no need to do anything,1=insert new row,2+update existing row
                query = "Update muster SET "
                        + "ShiftCode='" + str_emp_shift + "',"
                        + "Remark='" + "" + "',"
                        + "logout='" + logout_time + "',"
                        + "early='" + early_time + "',"
                        + "LeaveNature='" + strleave_nature + "',"
                        + "WorkingPlace='" + str_work_place + "' "
                        + "WHERE "
                        + "`EmpCode` ='" + empcode + "' "
                        + "and "
                        + "`Date`='" + strswipedate + "'";
                System.out.println("addOutSwipe : " + query);
                stmt.executeUpdate(query);

            }
        } catch (Exception ex) {
            System.out.println("addOutSwipe : " + ex);
            insertProcessFailedStatus(conn, stmt);
        } finally {
            conn.close();
        }
    }

    /**
     * calculating work hours that employee has worked
     *
     * @throws SQLException
     */
    private void calculateWorkHours(DBConnection conn, Statement stmt, String empcode) throws Exception {

        String query;
        int extrawork;
        String earlytime = "00:00:00";
        conn.connect();
        stmt = conn.createStatement();
        String tempstatus = "";
        query = "select "
                + "login,"
                + "logout,"
                + "early,"
                + "`status` "
                + "from "
                + "muster "
                + "where "
                + "EmpCode='" + empcode + "' "
                + "AND "
                + "Date='" + strswipedate + "'";
        try (ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                login_time = rs.getString(1);
                logout_time = rs.getString(2);
                earlytime = rs.getString(3);
                tempstatus = rs.getString(4);
            }
            rs.close();
        }
        if ((!"00:00:00".equals(login_time)) && (!"00:00:00".equals(logout_time))) {
            try {
                int workedhours = (getTimeDiff(login_time, logout_time));//employee's total work in seconds
                int workhourLimit = (getTimeDiff("00:00:00", strworkhours));//  / 60;//shift time period in minutes
                int overtimelimit = (getTimeDiff("00:00:00", over_time_limit));
                if ((workedhours / 60) >= 0) {//workedhours converted from minutes to seconds
                    if (workedhours == 0) {
                        strworkhours = "00:00:00";
                    } else if (workedhours < 3600) {   //if time has less than 1 hour i.e. less than 3600 seconds
                        strworkhours = ("00" + ":" + (((workedhours) % 3600) / 60) + ":" + (workedhours % 60));
                    } else {
                        strworkhours = (((workedhours) / 3600) + ":" + (((workedhours) % 3600) / 60) + ":" + (workedhours % 60));
                    }
                    if ((workedhours) > workhourLimit) {//checking for overtime
                        extrawork = (workedhours) - workhourLimit;
                        extra_work = (((extrawork) / 3600) + ":" + (((extrawork) % 3600) / 60) + ":" + (extrawork % 60));
                        if (extrawork > overtimelimit) {
                            isOverTime = true;
                        }
                    }
                }
            } catch (ParseException ex) {
                System.out.println("calculateWorkHours : " + ex);
                insertProcessFailedStatus(conn, stmt);
            }
        } else if ((!"00:00:00".equals(login_time)) && ("00:00:00".equals(logout_time))) {  //check Out swipe missing
            if (is_single_swipe_allow == 2) {//if single swipe allow
                if (iscame_in_another_shift == false) {
                    bytStatus = 4;//early going
                }
            } else {
                isOutSwipemiss = true;
            }
            early_time = "00:00:00";
            strworkhours = "00:00:00";
            extra_work = "00:00:00";
        } else if (("00:00:00".equals(login_time)) && (!"00:00:00".equals(logout_time))) { //check in swipe missing
            if (iscame_in_another_shift == false) {
                bytStatus = 10;//inswipe missing
                if (is_single_swipe_allow == 2) {//if single swipe allow
                    bytStatus = 2;//late coming
                }
            } else {
                bytStatus = 20;//came in another shift
            }
        } else if (("00:00:00".equals(login_time)) && ("00:00:00".equals(logout_time))) {  //check emp is absent
            isAbsent = true;
        }

        //Checking privious status if late coming as well as early going
        String emp_status = getEmpStatus(bytStatus);//getting default status 
        if (!"XXS".equals(tempstatus)) {  //if emp not came in othe shift
            if (("".equals(tempstatus)) || tempstatus == null) {//i.e tempstatus equals empty
                emp_status = getEmpStatus(bytStatus);
            } else if (tempstatus.length() == 3) {
                char ch = tempstatus.charAt(2);//if 3rd character is 'L' i.e. if emp has came late 
                if (ch == 'L') {//for early going and late coming
                    isOutSwipemiss = false;//if isOutSwipemiss==true then status will come like AA# not XXL if employee has came late
                    emp_status = getEmpStatus(bytStatus);
                    if (emp_status.length() == 3) {
                        char ch1 = emp_status.charAt(2);
                        if (ch1 == 'E') {
                            bytStatus = 5;//late coming and early going
                            emp_status = getEmpStatus(bytStatus);
                        } else {
                            emp_status = tempstatus;//if late coming but not early going then status should be remains late coming
                        }
                    } else {
                        emp_status = tempstatus;//if late coming but not early going then status should be remains late coming
                    }
                } else if ("AA#".equals(tempstatus) & (!"00:00:00".equals(logout_time))) { //all '#' status gives late coming and early going but AA# gives outswipe missing
                    emp_status = getEmpStatus(bytStatus);
                } else if (((ch == 'E') || ((ch == '#'))) & (!"00:00:00".equals(earlytime))) {//if employee has priviously steted as early going but after that new swipe time is greater than previous(i.e. greater than out time) then he should not be early going
                    emp_status = tempstatus;
                } else if ((ch == '#') && ("00:00:00".equals(earlytime))) {//only late coming,not early going
                    bytStatus = 4;//late coming
                    emp_status = getEmpStatus(bytStatus);//only late coming not early going
                } else {
                    emp_status = getEmpStatus(bytStatus);
                }
            } else if (tempstatus.length() == 2) {//if length of status is 2 i.e. status can be XX,HH,WW,LL,OD,CF,etc 
                if (!"00:00:00".equals(extra_work)) {
                    emp_status = getEmpStatus(bytStatus);
                } else {
                    if (!"XX".equals(tempstatus)) {
                        emp_status = tempstatus;
                    } else {
                        emp_status = getEmpStatus(bytStatus);
                    }
                }
            } else {
                emp_status = getEmpStatus(bytStatus);
            }
        } else {
            emp_status = tempstatus;
        }

        query = "Update muster SET "
                + "status='" + emp_status + "',"
                + "Remark='" + "" + "',"
                + "Wrkhrs='" + strworkhours + "',"
                + "exthrs='" + extra_work + "'"
                + "WHERE "
                + "`EmpCode` ='" + empcode + "' "
                + "and "
                + "`Date`='" + strswipedate + "'";
        System.out.println("calculateWorkHours : " + query);
        stmt.executeUpdate(query);
        conn.close();
        stmt.close();
    }

    /**
     * generating status of employee
     *
     * @param status=passing bytestatus
     * @return =employee status
     */
    private String getEmpStatus(byte status) {
        String empstatus = "";
        if (isholiday == true) {
            if (status == 1) {
                empstatus = "HH*";  // Working on Holiday
            } else if (status == 2) {
                empstatus = "HHL";	 // Working but Late Comming
            } else if (status == 4) {
                empstatus = "HHE";	// Working but Early Going
            } else if ((status == 5)) {
                empstatus = "HH#";//Working but Late Coming & Early Going
            } else if ((status == 13) || (status == 10)) {
                empstatus = "HHI";//InSwipeMissing
            } else if ((status == 20) || (status == 23)) {
                empstatus = "HHS";// working but come in other shift                
            } else {
                empstatus = "HH";//Holiday
            }
        } else if (isWeekOff == true) {
            if (status == 1) {
                empstatus = "WW*";  //Working on WeekOff               
            } else if (status == 2) {
                empstatus = "WWL";// working but Late Comming
            } else if (status == 4) {
                empstatus = "WWE";//Working but Early Going
            } else if (status == 5) {
                empstatus = "WW#";//Working but Late Coming & Early Going
            } else if ((status == 13) || (status == 10)) {
                empstatus = "WWI";// Working but In Swaipe missing
            } else if ((status == 20) || (status == 23)) {
                empstatus = "WWS";//working but came in ohter shift
            } else {
                empstatus = "WW";// WeekOff
            }
        } else if (isOnLeave == true) {
            if (status == 1) {
                empstatus = "LL*";
            } else if (status == 2) {
                empstatus = "LLL";//late coming
            } else if (status == 4) {
                empstatus = "LLE";//early going
            } else if (status == 5) {
                empstatus = "LL#";//Late Coming & Early Going
            } else if ((status == 13) || (status == 10)) {
                empstatus = "LLI";//Inswipe missing
            } else if (status == 20) {
                empstatus = "LLS";// came in ohter shift
            } else {
                empstatus = "LL";//onLeave
            }
        } else if (isCompOff == true) {
            empstatus = "CF";//compoff
        } else {
            if (status == 1) {
                if (isOverTime == true) {
                    empstatus = "XX*";//overtime
                } else {
                    empstatus = "XX";//present
                }
            } else if (status == 2) {
                empstatus = "XXL";//late coming
            } else if (status == 4) {
                empstatus = "XXE";//early going
            } else if (status == 5) {
                empstatus = "XX#";//late coming and earlygoing
            } else if ((status == 13) || (status == 10)) {
                empstatus = "AA*";  //In Swipe missing
            } else if ((status == 20) || (status == 23)) {
                empstatus = "XXS";
            }
        }
        if (isAbsent == true) {  /*
             * if employee is absent
             */
            empstatus = "AA";//if isAbsent true then default value of status is AA i.e. Absent
            if (isholiday == true) {
                empstatus = "HH"; //Holiday
            }
            if (isWeekOff == true) {
                empstatus = "WW";  //WeekOff
            }
            if (isOnLeave == true) {
                empstatus = "LL"; //OnLeave
            } else if (isCompOff == true) {
                empstatus = "CF"; //CompOff
            }
        }
        if (isOutSwipemiss == true) {
            if (status == 10) {
                empstatus = "AA";//Absent
            } else if ((status == 1) || (status == 2)) {
                empstatus = "AA#";//Out Swipe Missing
            } else if (status == 4) {
                empstatus = "XXE"; //Present but Early Going
            } else if ((status == 20) || (status == 23)) {
                empstatus = "XXS"; //Present but came in another shift
            }
        }
        return empstatus;
    }

    /**
     * if any exception occures then processed field of swipedatatable should
     * update to false
     */
    private void insertProcessFailedStatus(DBConnection conn, Statement stmt) {
        try {
            conn.connect();
            stmt = conn.createStatement();
            String query = "Update swipedata set Processed='F' where ProDateTime='" + swipeDateTime + "' and EmpCode='" + empcode + "'";
            stmt.executeUpdate(query);
        } catch (Exception ex) {
            System.out.println("");
        } finally {
            conn.close();
        }
    }
}
