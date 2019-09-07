/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.reports;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
 *
 * @author pradnya
 */
public class ReportFormServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String remoteAddr = req.getRemoteAddr();
        try {
            resp.setContentType("text/plain");
            try (PrintWriter out = resp.getWriter()) {
                out.println("Error: this servlet does not support the GET method!");
            }
        } catch (Exception e) {
            System.out.println("GET request from " + remoteAddr);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MyHTTPConnection myHTTPConn = null;
        LoginSession loginSession = null;
        try {
            resp.setContentType("text/plain");
            myHTTPConn = new MyHTTPConnection(req, resp);
            /**
             * ******* Get Login Session *****************
             */
            HttpSession session = req.getSession(false);
            int[] reqTypes = {
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            loginSession = LoginSession.getLoginSession(session, reqTypes);

            String ReqType = myHTTPConn.readLine();
            switch (ReqType) {
                case "AbsentReport":
                    getAbsentReport(myHTTPConn, loginSession);
                    break;
                case "OutDoorDuty":
                    getReport(myHTTPConn, loginSession, "OutDoorDuty");
                    break;
                case "WeekOffApplet":
                    getReport(myHTTPConn, loginSession, "WeekOffApplet");
                    break;
                case "OverTimeApplet":
                    getReport(myHTTPConn, loginSession, "OverTimeApplet");
                    break;
                case "OnLeaveReport":
                    getReport(myHTTPConn, loginSession, "OnLeaveReport");
                    break;
                case "GetEmp":
                    getEmp(myHTTPConn, loginSession);
                    break;
                case "getSelEmpDetails":
                    getSelEmpDetails(myHTTPConn, loginSession);
                    break;
                case "checkHead":
                    checkHead(myHTTPConn, loginSession);
                    break;
                case "Division":
                case "Section":
                case "WorkLocation":
                    getComboList(myHTTPConn, loginSession, ReqType);
                    break;
                default:
                    myHTTPConn.println("unknownRequest");
                    System.out.println("unknownRequest");
                    break;
            }

        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }
    }

    private void checkHead(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        int usertype = loginSession.getLoginType();
        if (usertype == 0) {
            checkDivSecHead(myHTTPConn, loginSession);
        } else if (usertype == 1) {
            checkOfficeHead(myHTTPConn, loginSession, usertype);
        }
    }

    private void getReport(MyHTTPConnection myHTTPConn, LoginSession loginSession, String reqType) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String[] divNm = null;
            String strDivision = "";
            String secNm = "";
            String getDiv = "";
            String rptStatus = "";

            String query = "";
            String rptType = myHTTPConn.readLine();
            switch (reqType) {
//                case "AbsentReport":
//                    rptStatus = "'" + "AA" + "'";
//                    break;
                case "OutDoorDuty":
                    rptStatus = "'" + "OD" + "'";
                    break;
                case "WeekOffApplet":
                    rptStatus = "'" + "WW" + "'";
                    break;
                case "OverTimeApplet":
                    rptStatus = "'" + "XX*" + "'";
                    break;
                case "OnLeaveReport":
                    rptStatus = "'" + "LL%" + "'";
                    break;
            }
            if ("Full".equals(rptType)) {
                String mainpacket = myHTTPConn.readLine();
                String rptWise = myHTTPConn.readLine();
                String divPacket = myHTTPConn.readLine();

                Depacketizer d = new Depacketizer(mainpacket);

                String frmDate = d.getString();
                String ToDate = d.getString();
                String checkHead = d.getString();

                Depacketizer div = new Depacketizer(divPacket);
                int divSize = div.getInt();
                divNm = new String[divSize];
                for (int i = 0; i < divSize; i++) {
                    divNm[i] = div.getString();
                    if (i == 0) {
                        strDivision = " division.DivName=" + "'" + divNm[i] + "'";
                        getDiv = "";
                    } else if (i > 0) {
                        strDivision = "(" + strDivision + " or division.DivName=" + "'" + divNm[i] + "')";
                        getDiv = ",division.DivName";
                    }
                }
                switch (reqType) {
//                    case "AbsentReport":
//                        query = "select "
//                                + "muster.`date`,"
//                                + "employeemaster.CCNo,"
//                                + "muster.EmpCode,"
//                                + "employeemaster.EmpName" + getDiv + ","
//                                + "sectionmaster.SecName,"
//                                + "muster.ShiftCode,"
//                                + "muster.Remark "
//                                + "from  "
//                                + "employeemaster,"
//                                + "division,"
//                                + "sectionmaster,"
//                                + "muster "
//                                + "where "
//                                + "muster.EmpCode=employeemaster.EmpCode "
//                                + "and "
//                                + "employeemaster.DivCode=division.DivCode "
//                                + "and "
//                                + strDivision
//                                + " and "
//                                + "employeemaster.SecCode=sectionmaster.SecCode "
//                                + "and "
//                                + "`Date`= '" + frmDate + "' ";
//                        break;
                    case "OutDoorDuty":
                        query = "select "
                                + "muster.`date`,"
                                + "employeemaster.CCNo,"
                                + "muster.EmpCode,"
                                + "employeemaster.EmpName" + getDiv + ","
                                + "(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as SecName,"
                                + "muster.ShiftCode,"
                                + "muster.Remark "
                                + "from  "
                                + "employeemaster,"
                                + "division,"
                                + "muster "
                                + "where "
                                + "muster.EmpCode=employeemaster.EmpCode "
                                + "and "
                                + "employeemaster.DivCode=division.DivCode "
                                + "and "
                                + strDivision + " "
                                + "and "
                                + "muster.status=" + rptStatus + " "
                                + "and "
                                + "`Date` "
                                + "between "
                                + "'" + frmDate + "'" + " "
                                + "and "
                                + "'" + ToDate + "'" + " ";
                        break;
                    case "WeekOffApplet":
                        query = "select "
                                + "muster.`date`,"
                                + "employeemaster.CCNo,"
                                + "muster.EmpCode,"
                                + "employeemaster.EmpName" + getDiv + ","
                                + "(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as SecName,"
                                + "muster.ShiftCode,"
                                + "DAYNAME(muster.`date`) as Day,"
                                + "muster.Remark "
                                + "from  "
                                + "employeemaster,"
                                + "division,"
                                + "muster "
                                + "where "
                                + "muster.EmpCode=employeemaster.EmpCode "
                                + "and "
                                + "employeemaster.DivCode=division.DivCode "
                                + "and "
                                + strDivision + " "
                                + "and "
                                + "muster.status=" + rptStatus + " "
                                + "and "
                                + "`Date` "
                                + "between "
                                + "'" + frmDate + "'" + " "
                                + "and "
                                + "'" + ToDate + "'" + " ";
                        break;
                    case "OverTimeApplet":
                        query = "select "
                                + "employeemaster.CCNo,"
                                + "muster.EmpCode,"
                                + "employeemaster.EmpName" + getDiv + ","
                                + "(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as SecName,"
                                + "muster.`date`,"
                                + "muster.Wrkhrs,"
                                + "muster.exthrs "
                                + "from "
                                + "employeemaster,"
                                + "division,"
                                + "muster "
                                + "where "
                                + "muster.EmpCode=employeemaster.EmpCode "
                                + "and "
                                + "employeemaster.DivCode=division.DivCode "
                                + "and "
                                + strDivision + " "
                                + "and "
                                + "muster.status=" + rptStatus + " "
                                + "and "
                                + "`Date` "
                                + "between "
                                + "'" + frmDate + "'" + " "
                                + "and "
                                + "'" + ToDate + "'" + " ";
                        break;
                    case "OnLeaveReport":
                        query = "select "
                                + "muster.`date`,"
                                + "employeemaster.CCNo,"
                                + "muster.EmpCode,"
                                + "employeemaster.EmpName" + getDiv + ","
                                + "(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as SecName,"
                                + "muster.LeaveNature,"
                                + "muster.Remark "
                                + "from  "
                                + "employeemaster,"
                                + "division,"
                                + "muster "
                                + "where "
                                + "muster.EmpCode=employeemaster.EmpCode "
                                + "and "
                                + "employeemaster.DivCode=division.DivCode "
                                + "and "
                                + strDivision + " "
                                + "and "
                                + "muster.status "
                                + "like "
                                + rptStatus + " "
                                + " and "
                                + "`Date` "
                                + "between "
                                + "'" + frmDate + "'" + " "
                                + "and "
                                + "'" + ToDate + "'" + " ";
                        break;
                }
            }
            if ("Selected".equals(rptType)) {
                String DivSecWorkL = "";
                String mainpacket = myHTTPConn.readLine();
                String rptWise = myHTTPConn.readLine();
                String selEmployee = myHTTPConn.readLine();
                selEmployee = selEmployee.trim();

                Depacketizer d = new Depacketizer(mainpacket);

                String frmDate = d.getString();
                String ToDate = d.getString();
                String division = d.getString();
                String section = d.getString();
                String workL = d.getString();
                String strDivSecWL = "";
                String[] Employee;
                String strEmp = "";
                String from = "";
                String and = "";

                if (!"".equals(selEmployee)) {
                    Employee = selEmployee.split(" ");
                    for (int i = 0; i < Employee.length; i++) {
                        if (i == 0) {
                            strEmp = " muster.EmpCode=" + "'" + Employee[i] + "'";
                        }
                        strEmp = strEmp + " or " + "muster.EmpCode=" + "'" + Employee[i] + "'";
                    }
                }
                switch (rptWise) {
                    case "Division":
                        DivSecWorkL = ",(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as SecName";
                        if (!"".equals(division)) {
                            strDivSecWL = " and division.DivName=" + "'" + division + "'";
                            from = "from employeemaster,division,muster";
                            and = "";
                        }
                        break;
                    case "Section":
                        DivSecWorkL = "";
                        if (!"".equals(division) && !"".equals(section)) {
                            strDivSecWL = " and division.DivName=" + "'" + division + "'" + " and " + " sectionmaster.SecName=" + "'" + section + "'";
                            from = "from employeemaster,division,sectionmaster,muster";
                            and = "and employeemaster.SecCode=sectionmaster.SecCode ";
                        }
                        break;
                    case "WorkLoc":
                        DivSecWorkL = "";
                        if (!"".equals(division) && !"".equals(section) && !"".equals(workL)) {
                            // strDeptSecWL = " and department.DeptName=" + "'" + dept + "'" + " and " + " sectionmaster.SecName=" + "'" + section + "'" + " and " + " muster.WorkingPlace=" + "'" + workL + "'";
                            strDivSecWL = " and division.DivName=" + "'" + division + "'" + " and " + " sectionmaster.SecName=" + "'" + section + "'";
                            from = "from employeemaster,division,sectionmaster,muster";
                            and = "and employeemaster.SecCode=sectionmaster.SecCode ";
                        }
                        break;
                }
                switch (reqType) {
//                    case "AbsentReport":
                    case "OutDoorDuty":
                        query = "select "
                                + "muster.`date`,"
                                + "employeemaster.CCNo,"
                                + "muster.EmpCode,"
                                + "employeemaster.EmpName"
                                + DivSecWorkL + ","
                                + "muster.ShiftCode,"
                                + "muster.Remark "
                                + from
                                + " where "
                                + "muster.EmpCode=employeemaster.EmpCode "
                                + "and "
                                + "(" + strEmp + ") "
                                + "and "
                                + "employeemaster.DivCode=division.DivCode "
                                + strDivSecWL + ""
                                + "and "
                                + "muster.status=" + rptStatus + " "
                                + and
                                + " and "
                                + "`Date` "
                                + "between "
                                + "'" + frmDate + "'" + " "
                                + "and "
                                + "'" + ToDate + "'" + " ";
                        break;
                    case "WeekOffApplet":
                        query = "select "
                                + "muster.`date`,"
                                + "employeemaster.CCNo,"
                                + "muster.EmpCode,"
                                + "employeemaster.EmpName"
                                + DivSecWorkL + ","
                                + "muster.ShiftCode,"
                                + "DAYNAME(muster.`date`) as Day,"
                                + "muster.Remark "
                                + from
                                + " where "
                                + "muster.EmpCode=employeemaster.EmpCode "
                                + "and "
                                + "(" + strEmp + ") "
                                + "and employeemaster.DivCode=division.DivCode "
                                + strDivSecWL + ""
                                + "and "
                                + "muster.status=" + rptStatus + " "
                                + and
                                + " and "
                                + "`Date` "
                                + "between "
                                + "'" + frmDate + "'" + " "
                                + "and "
                                + "'" + ToDate + "'" + " ";
                        break;
                    case "OverTimeApplet":
                        query = "select "
                                + "muster.`date`,"
                                + "employeemaster.CCNo,"
                                + "muster.EmpCode,"
                                + "employeemaster.EmpName" + DivSecWorkL + ","
                                + "muster.ShiftCode,"
                                + "muster.`date`,"
                                + "muster.Wrkhrs,"
                                + "muster.exthrs "
                                + from
                                + " where "
                                + "muster.EmpCode=employeemaster.EmpCode "
                                + "and "
                                + "(" + strEmp + ") "
                                + "and "
                                + "employeemaster.DivCode=division.DivCode " + strDivSecWL
                                + "and "
                                + "muster.status=" + rptStatus + " "
                                + and
                                + " and "
                                + "`Date` "
                                + "between "
                                + "'" + frmDate + "'"
                                + " and "
                                + "'" + ToDate + "'" + " ";
                        break;
                    case "OnLeaveReport":
                        //  query = "select muster.`date`,employeemaster.CCNo,muster.EmpCode,employeemaster.EmpName" + DeptSecWorkL + ",muster.LeaveNature,muster.Remark from employeemaster,department,sectionmaster,muster where muster.EmpCode=employeemaster.EmpCode and (" + strEmp + ") and employeemaster.DiviCode=department.DeptCode " + strDeptSecWL + "and muster.status=" + rptStatus + " and employeemaster.SectionCode=sectionmaster.SecCode and `Date` between " + "'" + strDate + "'" + " and " + "'" + ToDate + "'" + " ";
                        query = "select "
                                + "muster.`date`,"
                                + "employeemaster.CCNo,muster.EmpCode,"
                                + "employeemaster.EmpName" + DivSecWorkL + ","
                                + "muster.LeaveNature,"
                                + "muster.Remark "
                                + from
                                + " where "
                                + "muster.EmpCode=employeemaster.EmpCode "
                                + "and (" + strEmp + ") "
                                + "and "
                                + "employeemaster.DivCode=division.DivCode "
                                + strDivSecWL + ""
                                + "and "
                                + "muster.status "
                                + "like " + rptStatus
                                + and
                                + " and "
                                + "`Date` "
                                + "between "
                                + "" + "'" + frmDate + "'" + " "
                                + "and "
                                + "'" + ToDate + "'" + " ";
                        break;
                }
            }
            Packetizer p;

            try (ResultSet rs = stmt.executeQuery(query)) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int col = rsmd.getColumnCount();
                p = new Packetizer();
                p.setCounter();
                while (rs.next()) {
                    for (int i = 1; i <= col; i++) {
                        p.addString(rs.getString(i));
                    }
                    p.incrCounter();
                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getEmp(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String query = "";
            String SearchBy = myHTTPConn.readLine();
            switch (SearchBy) {
                case "Division": {
                    String Division = myHTTPConn.readLine();
                    query = "select "
                            + "employeemaster.EmpCode,"
                            + "employeemaster.EmpName "
                            + "from "
                            + "employeemaster,"
                            + "division "
                            + "where "
                            + "division.DivName=" + "'" + Division + "'" + " "
                            + "and "
                            + "employeemaster.DivCode=division.DivCode";
                    break;
                }
                case "Section": {
                    String Section = myHTTPConn.readLine();
                    query = "select "
                            + "employeemaster.EmpCode,"
                            + "employeemaster.EmpName "
                            + "from "
                            + "employeemaster,"
                            + "sectionmaster "
                            + "where "
                            + "sectionmaster.SecName=" + "'" + Section + "'" + " "
                            + "and "
                            + "employeemaster.SecCode=sectionmaster.SecCode";
                    break;
                }
                case "WorkLocation": {
                    String Division = myHTTPConn.readLine();
                    String Section = myHTTPConn.readLine();
                    String WLoc = myHTTPConn.readLine();
                    query = "select "
                            + "employeemaster.EmpCode,"
                            + "employeemaster.EmpName "
                            + "from "
                            + "employeemaster,"
                            + "division,"
                            + "sectionmaster "
                            + "where "
                            + "division.DivName=" + "'" + Division + "'" + " "
                            + "and "
                            + "sectionmaster.SecName=" + "'" + Section + "'" + " "
                            + "and "
                            + "employeemaster.wlocation_code=" + "'" + WLoc + "'" + "  "
                            + "and "
                            + "employeemaster.SecCode=sectionmaster.SecCode";
                    break;
                }
            }
            Packetizer p;
            try (ResultSet rs = stmt.executeQuery(query)) {
                p = new Packetizer();
                p.setCounter();
                while (rs.next()) {
                    p.addString(rs.getString(1));
                    p.addString(rs.getString(2));
                    p.incrCounter();
                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getComboList(MyHTTPConnection myHTTPConn, LoginSession loginSession, String ReqType) {
        DBConnection conn = null;
        Statement stmt = null;
        Packetizer p = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            //get connection
            String query = "";
            switch (ReqType) {
                case "Division":
                    String divcode = myHTTPConn.readLine();
                    query = "SELECT "
                            + "DivName "
                            + "FROM "
                            + "division "
                            + "where "
                            + "DivCode=" + "'" + divcode + "'";
                    try (ResultSet rs = stmt.executeQuery(query)) {
                        p = new Packetizer();
                        p.setCounter();
                        while (rs.next()) {
                            p.addString(rs.getString(1));
                            p.incrCounter();
                        }
                    }
                    break;
                case "Section":
                    String divNm = myHTTPConn.readLine();
                    query = "select "
                            + "sectionmaster.SecName "
                            + "from "
                            + "sectionmaster,"
                            + "division "
                            + "where "
                            + "division.DivName=" + "'" + divNm + "'" + " "
                            + "and "
                            + "sectionmaster.DivCode=division.DivCode";
                    try (ResultSet rs = stmt.executeQuery(query)) {
                        p = new Packetizer();
                        p.setCounter();
                        while (rs.next()) {
                            p.addString(rs.getString(1));
                            p.incrCounter();
                        }
                    }
                    break;
                case "WorkLocation":
                    query = "SELECT wlocation_code,WLocation FROM worklocation";
                    try (ResultSet rs = stmt.executeQuery(query)) {
                        p = new Packetizer();
                        p.setCounter();
                        while (rs.next()) {
                            p.addString(rs.getString(1) + " " + rs.getString(2));
                            p.incrCounter();
                        }
                    }
                    break;
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    public void checkOfficeHead(MyHTTPConnection myHTTPConn, LoginSession loginSession, int usertype) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String Type = myHTTPConn.readLine();
            String headType = null;
            usertype = loginSession.getLoginType();
            String username = loginSession.getUserID();
            String access_string = "";
            String division = "", section = "";
            Depacketizer div = null, sec = null;

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
                div = new Depacketizer(div_packet.getPacket());
                sec = new Depacketizer(sec_packet.getPacket());

            }
            if ("Division".equals(Type)) {
                int deptsize = div.getInt();
                if (deptsize > 0) {
                    for (int i = 0; i < deptsize; i++) {
                        if (i == 0) {
                            division = " DivCode='" + div.getString() + "'";
                        } else {
                            division = division + " OR DivCode='" + div.getString() + "'";
                        }
                    }
                    query = "Select "
                            + "DivName "
                            + "from "
                            + "division "
                            + "where "
                            + division;
                    Type = "DivHead";
                } else {
                    division = "";
                }

            } else if ("Section".equals(Type)) {
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
                    query = "Select "
                            + "sectionmaster.SecName,"
                            + "division.DivName "
                            + "from "
                            + "sectionmaster,"
                            + "division "
                            + "where "
                            + "sectionmaster.DivCode=division.DivCode "
                            + "and "
                            + division + section;
                    Type = "SectionHead";

                } else {
                    section = "";
                }
            }
            ResultSet rs1 = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            if (rs1.next()) {
                p.addString(Type);
                rs1.beforeFirst();
                while (rs1.next()) {
                    switch (Type) {
                        case "DivHead":
                            p.addString(rs1.getString(1));
                            break;
                        case "SectionHead":
                            p.addString(rs1.getString(1));//section name
                            p.addString(rs1.getString(2));//division name of above section
                            break;
                    }
                    p.incrCounter();
                }
                rs1.close();
            }
            myHTTPConn.println(p.getPacket());

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    public void checkDivSecHead(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String Type = myHTTPConn.readLine();
            String empCode = myHTTPConn.readLine();
            String query = "";
            if ("Division".equals(Type)) {
                query = "Select "
                        + "DivName "
                        + "from "
                        + "division "
                        + "where "
                        + "DivHeadCode=" + "'" + empCode + "'";
                Type = "DivHead";
            } else if ("Section".equals(Type)) {
                query = "Select "
                        + "sectionmaster.SecName,"
                        + "division.DivName "
                        + "from "
                        + "sectionmaster,"
                        + "division "
                        + "where "
                        + "sectionmaster.DivCode=division.DivCode "
                        + "and "
                        + "SecHeadCode=" + "'" + empCode + "'";
                Type = "SectionHead";
            }

            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            if (rs.next()) {
                p.addString(Type);
                rs.beforeFirst();
                while (rs.next()) {

                    switch (Type) {
                        case "DivHead":
                            p.addString(rs.getString(1));
//                        p.addString(Type);
                            break;
                        case "SectionHead":
                            p.addString(rs.getString(1));
//                        p.addString(Type);
                            p.addString(rs.getString(2));
                            break;
                    }
                    p.incrCounter();
                }
                rs.close();
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getAbsentReport(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String query = "";
            String rptType = myHTTPConn.readLine();
            String mainpacket = myHTTPConn.readLine();
            String rptWise = myHTTPConn.readLine();
            String selEmployee = myHTTPConn.readLine();
            selEmployee = selEmployee.trim();

            Depacketizer d = new Depacketizer(mainpacket);

            String strDate = d.getString();
            String[] Employee;
            String strEmp = "";

            if (!"".equals(selEmployee)) {
                Employee = selEmployee.split(" ");
                for (int i = 0; i < Employee.length; i++) {
                    if (i == 0) {
                        strEmp = " EmpCode=" + "'" + Employee[i] + "'";
                    }
                    strEmp = strEmp + " or " + " EmpCode=" + "'" + Employee[i] + "'";
                }
            }
            query = "select distinct EmpCode from muster where (" + strEmp + ") and `Date`= '" + strDate + "'";

//            }
            Packetizer p;
            System.out.println("Absent query :" + query);
            try (ResultSet rs = stmt.executeQuery(query)) {
                p = new Packetizer();
                p.setCounter();
                while (rs.next()) {
                    p.addString(rs.getString(1));
                    p.incrCounter();
                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getSelEmpDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String emppacket = myHTTPConn.readLine();
            String date = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(emppacket);
            int packetsize = dp.getInt();
            String empcode = "";
            Packetizer main = new Packetizer();
            main.setCounter();
            String strQuery = "Select CCNo,"
                    + "EmpCode,"
                    + "EmpName,"
                    + "(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as Section "
                    + "from "
                    + "employeemaster where EmpCode=?";
            ResultSet rs;
            pstm = conn.prepareStatement(strQuery);
            if (packetsize != 0) {
                for (int i = 0; i < packetsize; i++) {
                    Packetizer p = new Packetizer();
                    empcode = dp.getString();
                    pstm.setString(1, empcode);
                    rs = pstm.executeQuery();
                    if (rs.next()) {
                        p.addString(rs.getString(1));
                        p.addString(rs.getString(2));
                        p.addString(rs.getString(3));
                        p.addString(rs.getString(4));
                        p.addString(getShiftFromRoster(myHTTPConn, loginSession, date, empcode));
                        main.addString(p.getPacket());
                        main.incrCounter();
                    }
                }
            }

            myHTTPConn.println(main.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private String getShiftFromRoster(MyHTTPConnection myHTTPConn, LoginSession loginSession, String date, String empcode) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        String shiftcode = "";
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return "";
            }
            String splitdate[] = date.split("-");
            String year = splitdate[0];
            String month = splitdate[1];
            String day = splitdate[2];

            if (month.length() < 2) {
                month = "0" + month;
            } else {
                month = "" + month;
            }

            if (day.length() < 2) {
                day = "0" + day;
            } else {
                day = "" + day;
            }
            String query = "select ? from shiftroster where Year=? and Month=? ";
            ResultSet rs;
            pstm = conn.prepareStatement(query);
            pstm.setString(1, day);
            pstm.setString(2, year);
            pstm.setString(3, month);
            rs = pstm.executeQuery();
            if (rs.next()) {
                shiftcode = rs.getString(1);
                System.out.println("shiftcode111:" + shiftcode);
                return shiftcode;
            } else {
                shiftcode = getShiftFromEmpmaster(myHTTPConn, loginSession, empcode);
                System.out.println("shiftcode222:" + shiftcode);
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
        return shiftcode;
    }

    private String getShiftFromEmpmaster(MyHTTPConnection myHTTPConn, LoginSession loginSession, String empcode) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        String shiftcode = "";
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return "";
            }
            String query = "select ShiftCode from employeemaster where EmpCode=?";
            ResultSet rs;
            pstm = conn.prepareStatement(query);
            pstm.setString(1, empcode);
            rs = pstm.executeQuery();
            if (rs.next()) {
                shiftcode = rs.getString(1);
                System.out.println("shiftcode111:" + shiftcode);
                return shiftcode;
            } else {
                shiftcode = "";
            }


        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
        return shiftcode;
    }
}