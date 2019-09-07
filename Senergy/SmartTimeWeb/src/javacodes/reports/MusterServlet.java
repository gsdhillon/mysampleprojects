package javacodes.reports;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 * MusterServlet.java
 */
public class MusterServlet extends HttpServlet {

    /**
     *
     * @param req
     * @param resp
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MyHTTPConnection myHTTPConn = null;
        try {
            resp.setContentType("text/plain");
            myHTTPConn = new MyHTTPConnection(req, resp);
            HttpSession session = req.getSession(false);//Get Login Session 
            int[] reqTypes = {
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            LoginSession loginSession = LoginSession.getLoginSession(session, reqTypes);//Read in the message from the servlet
            String msg = myHTTPConn.readLine();//Write the message back to the applet
            String empCode = loginSession.getUserID();

            if (msg.equals("getMuster")) {
                getMuster(empCode, myHTTPConn);
            } else if (msg.equals("getUserMusterForAll")) {
                getUserMusterForAll(loginSession, myHTTPConn);
            } else if (msg.equals("isHoliday")) {
                isHoliday(loginSession, myHTTPConn);
            } else {
                myHTTPConn.println("ERROR:unknown request");
            }
        } catch (Exception e) {
            try {
                if (myHTTPConn != null) {
                    myHTTPConn.println("ERROR:" + e.getMessage());
                }
                e.printStackTrace();
            } catch (Exception ex) {
            }
        } finally {
            AppContext.close(myHTTPConn);
        }
    }

    /**
     *
     * @param br
     * @param pw
     */
    private void getMuster(String empno, MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {

            String mm = myHTTPConn.readLine();
            String yy = myHTTPConn.readLine();
            String rptName = myHTTPConn.readLine();
            String strstatus = "";
            if (!"MonthlyShiftDetails".equals(rptName)) {
                switch (rptName) {
                    case "AbsentReport":
                        strstatus = " status='AA' AND ";
                        break;
                    case "OutDoorDuty":
                        strstatus = " status='OD' AND ";
                        break;
                    case "WeekOffApplet":
                        strstatus = " status like 'WW%' AND ";
                        break;
                    case "OverTimeApplet":
                        strstatus = " status='XX*' AND ";
                        break;
                    case "OnLeaveReport":
                        strstatus = " status like 'LL%' AND ";
                        break;
                    case "EarlyReport":
                        strstatus = " status='XXE' AND ";
                        break;
                    case "LateReport":
                        strstatus = " status='XXL' AND ";
                        break;
                    case "SingleSwipe":
                        strstatus = " status='AA*' OR status='AA#' AND ";
                        break;
                    default:
                        strstatus = "";
                        break;
                }
                conn = AppContext.getDBConnection();
                if (conn == null) {
                    myHTTPConn.println("ERROR:Could not get database connection");
                }
                stmt = conn.createStatement();
                //get holiday list
                String query =
                        "select "
                        + "DAYOFMONTH(HolidayDate) as DAY, "
                        + "HolidayName "
                        + "from "
                        + "holiday "
                        + "where "
                        + "DATE_FORMAT(HolidayDate,'%y') = '" + yy + "'AND "
                        + "DATE_FORMAT(HolidayDate,'%m') = '" + mm + "' ";
                ResultSet rs = stmt.executeQuery(query);
                Packetizer p1 = new Packetizer();
                p1.setCounter();
                while (rs.next()) {
                    p1.addString(rs.getString("DAY"));
                    p1.addString(rs.getString("HolidayName"));
                    p1.incrCounter();
                }
                rs.close();
                myHTTPConn.println(p1.getPacket());
                //get attendance details
                query =
                        "select "
                        + "DAYOFMONTH(`Date`) as DAY, "
                        + "login, "
                        + "logout,"
                        + "status "
                        + "from "
                        + "muster "
                        + "where "
                        + strstatus
                        + " EmpCode = " + empno + " AND "
                        + "DATE_FORMAT(`Date`,'%m') = '" + mm + "' AND "
                        + "DATE_FORMAT(`Date`,'%y') = '" + yy + "' ";
                rs = stmt.executeQuery(query);
                Packetizer p2 = new Packetizer();
                p2.setCounter();
                while (rs.next()) {
                    p2.addString(rs.getString("DAY"));
                    p2.addString(rs.getString("login"));
                    p2.addString(rs.getString("logout"));
                    p2.addString(rs.getString("status"));
                    p2.incrCounter();
                }
                rs.close();
                myHTTPConn.println(p2.getPacket());
            } else {
                getMonthlyShiftDetails(myHTTPConn, mm, yy, empno);
            }
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR:" + e.toString().replaceAll("\n", ";"));
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     *
     * @param myHTTPConn
     */
    private void getUserMusterForAll(LoginSession loginSession, MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            String mm = myHTTPConn.readLine();
            String yy = myHTTPConn.readLine();
            String division = "";
//            division = loginSession.getDivision();//"QCP";
            conn = AppContext.getDBConnection();//get connection
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            int usertype = loginSession.getLoginType();
            ResultSet rs = null;
            if (usertype == 0) {//if usertype is user
                rs = getEmployeeList(myHTTPConn, loginSession);//if employee is division head
            } else if (usertype == 1) {//if usertype is office
                stmt = conn.createStatement();
                rs = getEmpListForOffice(myHTTPConn, usertype, loginSession.getUserID(), stmt);
            } else if (usertype == 2) {//if usertype is admin
            }
            int count = 0;
            rs.last();
            int totalrows = rs.getRow();
            String[][] emp = new String[totalrows][3];
            rs.beforeFirst();
            while (rs.next()) {
                emp[count][0] = rs.getString("EmpCode");
                emp[count][1] = rs.getString("EmpName");
                emp[count][2] = rs.getString("designation");
                count++;
            }
            rs.close();
            myHTTPConn.println(String.valueOf(count));
            myHTTPConn.println(division);
            //send all emps musters
            for (int i = 0; i < count; i++) {
                stmt = conn.createStatement();
                rs = getMusterData(stmt, emp[i][0], mm + yy);
                Packetizer p = new Packetizer();
                p.setCounter();
                p.addString(emp[i][0]);//user_id only in first record
                p.addString(emp[i][1]);//name only in first record
                p.addString(emp[i][2]);//name only in first record
                while (rs.next()) {
                    p.addString(rs.getString(1));
                    p.addString(rs.getString(2));
                    p.addString(rs.getString(3));
                    p.addString(rs.getString(4));
                    p.addString(rs.getString(5));
                    p.addString(rs.getString(6));
                    p.incrCounter();
                }
                myHTTPConn.println(p.getPacket());
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     *
     * @param stmt
     * @param empcode
     * @param mmyy
     * @return
     * @throws Exception
     */
    private ResultSet getMusterData(Statement stmt, String empcode, String mmyy) throws Exception {

        String query = "SELECT "
                + "DATE_FORMAT(`Date`, '%d') DAY, "
                + "DATE_FORMAT(login, '%y-%m-%d#%H:%i:%S') TIME_IN, "
                + "DATE_FORMAT(logout, '%y-%m-%d#%H:%i:%S') TIME_OUT, "
                + "late, "
                + "early, "
                + "status "
                + "FROM "
                + "muster "
                + "WHERE "
                + "EmpCode = " + empcode + " AND "
                + "DATE_FORMAT(`Date`, '%m%y') = '" + mmyy + "' "
                + "ORDER BY "
                + "DAY ";
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    private String getCurrentDateTime() {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                return "ERROR:Could not get database connection";
            }
            stmt = conn.createStatement();
            String query =
                    "SELECT "
                    + "Date_Format(sysdate,'%d') as DD, "
                    + "Date_Format(sysdate,'%m') as MM, "
                    + "year(sysdate) as yyyy, "
                    + "Date_Format(sysdate,'%k') as HH24, "
                    + "Date_Format(sysdate,'%i') as MI, "
                    + "Date_Format(sysdate,'%s') as SS "
                    + "FROM "
                    + "`DUAL`";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            Packetizer p = new Packetizer();
            p.addInt(Integer.parseInt(rs.getString("DD")));
            p.addInt(Integer.parseInt(rs.getString("MM")) - 1);//send month 0-11
            p.addInt(Integer.parseInt(rs.getString("YYYY")));
            p.addInt(Integer.parseInt(rs.getString("HH24")));
            p.addInt(Integer.parseInt(rs.getString("MI")));
            p.addInt(Integer.parseInt(rs.getString("SS")));
            rs.close();
            return p.getPacket();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR:" + e.toString().replaceAll("\n", ";");
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     */
    private String getTodayDate() {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            if (conn == null) {
                return "ERROR:Could not get database connection";
            }
            stmt = conn.createStatement();
            String query =
                    "select "
                    + "Date_Format(sysdate,'%d') as DD, "
                    + "Date_Format(sysdate,'%m') as MM, "
                    + "year(sysdate) as yyyy "
                    + "from "
                    + "`DUAL` ";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            Packetizer p = new Packetizer();
            p.addInt(Integer.parseInt(rs.getString("DD")));
            p.addInt(Integer.parseInt(rs.getString("MM")) - 1);//send month 0-11
            p.addInt(Integer.parseInt(rs.getString("YYYY")));
            rs.close();
            return p.getPacket();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR:" + e.toString().replaceAll("\n", ";");
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     *
     * @param bs
     * @param br
     * @param pw
     */
    private String getHolidays(MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            String mm = myHTTPConn.readLine();
            String yy = myHTTPConn.readLine();
            conn = AppContext.getDBConnection();
            if (conn == null) {
                return "ERROR:Could not get database connection";
            }
            stmt = conn.createStatement();
            //get holiday list
            String query =
                    "select "
                    + "Date_Format(HolidayDate,'%d') DAY,"
                    + "HolidayName "
                    + "from "
                    + "holiday "
                    + "where "
                    + "Date_Format(HolidayDate,'%y') = '" + yy + "'AND "
                    + "Date_Format(HolidayDate,'%m') = '" + mm + "' ";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p1 = new Packetizer();
            p1.setCounter();
            while (rs.next()) {
                p1.addString(rs.getString("DAY"));
                p1.addString(rs.getString("REASON"));
                p1.incrCounter();
            }
            rs.close();
            return p1.getPacket();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR:" + e.toString().replaceAll("\n", ";");
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     *
     * @param req
     * @param resp
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String remoteAddr = "";
        try {
            remoteAddr = req.getRemoteAddr();
            resp.setContentType("text/plain");
            PrintWriter out = resp.getWriter();
            out.println("Error: this servlet does not support the GET method!");
            out.close();
        } catch (Exception e) {
            System.out.println("GET request from " + remoteAddr);
        }
    }// </editor-fold>

    public ResultSet getEmployeeList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = AppContext.getDBConnection();//get connection
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            if (checkHead(myHTTPConn, loginSession)) {
                String packet = getDivAndSection(myHTTPConn, loginSession);
                String div_packet, sec_packet;

                Depacketizer dp = new Depacketizer(packet);
                div_packet = dp.getString();
                sec_packet = dp.getString();
                String division = "", section = "";

                Depacketizer div = new Depacketizer(div_packet);
                int deptsize = div.getInt();

                if (deptsize > 0) {
                    for (int i = 0; i < deptsize; i++) {
                        if (i == 0) {
                            division = " DivCode='" + div.getString() + "'";
                        } else {
                            division = division + " OR DivCode='" + div.getString() + "'";
                        }
                    }
                } else {
                    division = "";
                }
                Depacketizer sec = new Depacketizer(sec_packet);
                int secsize = sec.getInt();

                if (secsize > 0) {
                    for (int i = 0; i < secsize; i++) {
                        if (i == 0) {
                            if (!"".equals(division)) {
                                section = " OR SecCode='" + sec.getString() + "'";
                            } else {
                                section = " SecCode='" + sec.getString() + "'";
                            }
                        } else {
                            section = section + " OR SecCode='" + sec.getString() + "'";
                        }
                    }
                } else {
                    section = "";
                }
                stmt = conn.createStatement();
                String query =
                        "select "
                        + "EmpCode, "
                        + "EmpName, "
                        + "(select DesigName from designationmaster where designationmaster.DesigID=employeemaster.DesigID) as designation "
                        + "from "
                        + "employeemaster where " + division + section;
                rs = stmt.executeQuery(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            return rs;
        }
    }

    public boolean checkHead(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        boolean checkHead = false;
        try {
            conn = AppContext.getDBConnection(); //get connection
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            String userid = loginSession.getUserID();
            stmt = conn.createStatement();
            String query = "select EmpStatus from employeemaster where EmpCode='" + userid + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String status = rs.getString(1);
                if ("3".equals(status)) {
                    checkHead = true;
                }
                rs.close();
            } else {
                checkHead = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
        return checkHead;
    }

    public String getDivAndSection(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        String divSec = "";
        try {
            conn = AppContext.getDBConnection();//get connection
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            stmt = conn.createStatement();
            Packetizer p = new Packetizer();
            Packetizer div = new Packetizer();
            Packetizer sec = new Packetizer();
            String query = "select DivCode from division where DivHeadCode='" + loginSession.getUserID() + "'";
            ResultSet rs1 = stmt.executeQuery(query);
            div.setCounter();
            while (rs1.next()) {
                div.addString(rs1.getString(1));
                div.incrCounter();
            }
            rs1.close();

            query = "select SecCode from sectionmaster where SecHeadCode='" + loginSession.getUserID() + "'";
            ResultSet rs2 = stmt.executeQuery(query);
            sec.setCounter();
            while (rs2.next()) {
                sec.addString(rs2.getString(1));
                sec.incrCounter();
            }
            rs2.close();
            p.addString(div.getPacket());
            p.addString(sec.getPacket());
            divSec = p.getPacket();
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
        return divSec;
    }

    private ResultSet getEmpListForOffice(MyHTTPConnection myHTTPConn, int usertype, String username, Statement stmt) {
        String access_string = "";
        String division = "", section = "";
        ResultSet rs1 = null;
        try {
            String query = "Select "
                    + "AccessString "
                    + "from "
                    + "useraccess "
                    + "where "
                    + "UserName='" + username + "' "
                    + "and "
                    + "usertype=" + usertype;
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                access_string = rs.getString(1);
                String acc_arr[] = access_string.split(" ");
                Packetizer div_packet = new Packetizer();
                div_packet.setCounter();
                Packetizer sec_packet = new Packetizer();
                sec_packet.setCounter();
                for (int i = 0; i < acc_arr.length; i++) {
                    char ch = acc_arr[i].charAt(0);
                    if (ch == '#') {
                        div_packet.addString(acc_arr[i].substring(1));
                        div_packet.incrCounter();
                    } else if (ch == '@') {
                        sec_packet.addString(acc_arr[i].substring(1));
                        sec_packet.incrCounter();
                    }
                }
                Depacketizer div = new Depacketizer(div_packet.getPacket());
                Depacketizer sec = new Depacketizer(sec_packet.getPacket());

                int deptsize = div.getInt();
                if (deptsize > 0) {
                    for (int i = 0; i < deptsize; i++) {
                        if (i == 0) {
                            division = " DivCode='" + div.getString() + "'";
                        } else {
                            division = division + " OR DivCode='" + div.getString() + "'";
                        }
                    }
                } else {
                    division = "";
                }
                int secsize = sec.getInt();
                if (secsize > 0) {
                    for (int i = 0; i < secsize; i++) {
                        if (i == 0) {
                            if (!"".equals(division)) {
                                section = " OR SecCode='" + sec.getString() + "'";
                            } else {
                                section = " SecCode='" + sec.getString() + "'";
                            }
                        } else {
                            section = section + " OR SecCode='" + sec.getString() + "'";
                        }
                    }
                } else {
                    section = "";
                }
            }
            if ((!"".equals(division)) || (!"".equals(section))) {
                query =
                        "select "
                        + "EmpCode, "
                        + "EmpName, "
                        + "(select DesigName from designationmaster where designationmaster.DesigID=employeemaster.DesigID) as designation "
                        + "from "
                        + "employeemaster where " + division + section;
                rs1 = stmt.executeQuery(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            return rs1;
        }
    }

    private void isHoliday(LoginSession loginSession, MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        String divSec = "";
        try {
            conn = AppContext.getDBConnection();//get connection
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            String holidaydate = myHTTPConn.readLine();
            stmt = conn.createStatement();
            String query = "select HolidayName from holiday where HolidayDate='" + holidaydate + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                myHTTPConn.println("true");
            } else {
                myHTTPConn.println("false");
            }

        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }

    }

    private void getMonthlyShiftDetails(MyHTTPConnection myHTTPConn, String mm, String yy, String empno) {
        DBConnection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }

            String query = "select ? from shiftroster where EmpCode=? and Month=? and Year=?";
//            "select "
//                    + "DAYOFMONTH(HolidayDate) as DAY, "
//                    + "HolidayName "
//                    + "from "
//                    + "holiday "
//                    + "where "
//                    + "DATE_FORMAT(HolidayDate,'%y') = ? AND "
//                    + "DATE_FORMAT(HolidayDate,'%m') = ? ";
            ResultSet rs;
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, yy);
            pstmt.setString(2, mm);
            rs = pstmt.executeQuery();
            Packetizer p1 = new Packetizer();
            p1.setCounter();
            while (rs.next()) {
                p1.addString(rs.getString("DAY"));
                p1.addString(rs.getString("HolidayName"));
                p1.incrCounter();
            }
            rs.close();
            myHTTPConn.println(p1.getPacket());
            //get attendance details
            query = "select ";
//            "select "
//                    + "DAYOFMONTH(`Date`) as DAY, "
//                    + "login, "
//                    + "logout,"
//                    + "status "
//                    + "from "
//                    + "muster "
//                    + "where "
//                    + " EmpCode = " + empno + " AND "
//                    + "DATE_FORMAT(`Date`,'%m') = '" + mm + "' AND "
//                    + "DATE_FORMAT(`Date`,'%y') = '" + yy + "' ";
            rs = pstmt.executeQuery(query);
            Packetizer p2 = new Packetizer();
            p2.setCounter();
            while (rs.next()) {
                p2.addString(rs.getString("DAY"));
                p2.addString(rs.getString("login"));
                p2.addString(rs.getString("logout"));
                p2.addString(rs.getString("status"));
                p2.incrCounter();
            }
            rs.close();
            myHTTPConn.println(p2.getPacket());
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR:" + e.toString().replaceAll("\n", ";"));
        } finally {
            AppContext.close(pstmt, conn);
        }



    }
}