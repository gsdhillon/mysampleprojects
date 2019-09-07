/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.Applications;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.Utility.DateUtilities;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author nbpatil
 */
public class ExceptionEntryServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ExceptionEntryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ExceptionEntryServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

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
                case "getApplicationDetailsList":
                    getApplicationDetailsList(myHTTPConn, loginSession);
                    break;
                case "getPreDetails":
                    getPreDetails(myHTTPConn, loginSession);
                    break;
                case "fillShiftCombo":
                    fillShiftCombo(myHTTPConn, loginSession);
                    break;
                case "getEmpShift":
                    getEmpShift(myHTTPConn, loginSession);
                    break;
                case "getShiftDetails":
                    getShiftDetails(myHTTPConn, loginSession);
                    break;
                case "getMusterDetails":
                    getMusterDetails(myHTTPConn, loginSession);
                    break;
                case "getEmpCategoryDetails":
                    getEmpCategoryDetails(myHTTPConn, loginSession);
                    break;
                case "addExceptionEntry":
                    addExceptionEntry(myHTTPConn, loginSession);
                    break;
                case "updateExceptionEntry":
                    updateExceptionEntry(myHTTPConn, loginSession);
                    break;
                case "cancelExceptionEntry":
                    cancelExceptionEntry(myHTTPConn, loginSession);
                    break;
                case "getEmployeeDayStatus":
                    ifMusterRecordNotFound(myHTTPConn, loginSession, "");
                    break;

            }

        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }
    }

    private void getApplicationDetailsList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String empcode = loginSession.getUserID();
            String strQuery = "select AppId,Date,PrevLin,Lin,PrevLout,Lout,Prevstatus,Attstatus from exceptionentry where EmpCode=?";
            ResultSet rs;
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, empcode);
            rs = pstm.executeQuery();
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                for (int i = 1; i <= 8; i++) {
                    p.addString(rs.getString(i));
                }
                p.incrCounter();
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void getPreDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String AppId = myHTTPConn.readLine();
            String strQuery = "select Date,ShiftCode,Lin,Lout,Wrkhrs,exthrs,PrevLin,PrevLout,PrevWrkhrs,Prevexthrs,late,early,Attstatus,Remark,Prevlate,Prevearly,Prevstatus from exceptionentry where AppId=?";
            ResultSet rs;
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, AppId);
            rs = pstm.executeQuery();
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                for (int i = 1; i <= 17; i++) {
                    p.addString(rs.getString(i));
                }
                p.incrCounter();
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void getMusterDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String strdate = myHTTPConn.readLine();
            String EmpCode = loginSession.getUserID();
            String strQuery = "select ShiftCode,login,logout,Wrkhrs,exthrs,status,Remark from muster where Date=? and EmpCode=?";
            ResultSet rs;
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, strdate);
            pstm.setString(2, EmpCode);
            rs = pstm.executeQuery();
            Packetizer p = new Packetizer();
            p.setCounter();
            if (rs.next()) {
                for (int i = 1; i <= 7; i++) {
                    p.addString(rs.getString(i));
                }
                p.incrCounter();
            } else {
                int status = ifMusterRecordNotFound(myHTTPConn, loginSession, strdate);
                if (status == 0) {
                    p.addString("AA");//status  
                } else if (status == 1) {
                    p.addString("WW");//status  
                } else if (status == 2) {
                    p.addString("HH");//status  
                } else if (status == 3) {
                    p.addString("LL");//status  
                } else if (status == 4) {
                    p.addString("OD");//status  
                } else if (status == 5) {
                    p.addString("CF");//status  
                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private int ifMusterRecordNotFound(MyHTTPConnection myHTTPConn, LoginSession loginSession, String strdate) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        int isstatus = 0;//0=absent,1=weekoff,2=holiday,3=onleave,4=outdoor,5=compoff
        boolean seperatecall = false;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return 0;
            }
            if ("".equals(strdate)) {
                strdate = myHTTPConn.readLine();
                seperatecall = true;
            }
            String ymd[] = strdate.split("-");
            String year = ymd[0];
            String month = ymd[0];
            String day = ymd[0];
            day = "d" + day;


            Date dtdate = DateUtilities.convertStringToDate(strdate);
            Calendar c = Calendar.getInstance();
            c.setTime(dtdate);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            String EmpCode = loginSession.getUserID();
            String strQuery = "select ? from shiftroster where EmpCode=? and Year=? and Month=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, day);
            pstm.setString(2, EmpCode);
            pstm.setString(3, year);
            pstm.setString(4, month);
            try (ResultSet rs = pstm.executeQuery()) {//checking for week off in ShiftRoster
                if (rs.next()) {
                    String shift = rs.getString(1);
                    if ("00".equals(shift)) {
                        isstatus = 1;
                    }
                } else {
                    isstatus = checkDefaultWeekOff(myHTTPConn, loginSession, EmpCode, dayOfWeek);
                    if (isstatus == 0) {//if week off is not there then checking for holiday
                        String strQuery2 = "select HolidayDate from holiday where HolidayDate=?";
                        pstm = conn.prepareStatement(strQuery2);
                        pstm.setString(1, strdate);
                        try (ResultSet rs1 = pstm.executeQuery()) {//checking for default week off in employeemaster if in shiftroster not found
                            if (rs1.next()) {
                                isstatus = 2;//holiday
                            } else {
                                isstatus = checkLeave(myHTTPConn, loginSession, EmpCode, dtdate);
                                if (isstatus == 0) {//if employee is not on Leave then checking for Outdoor
                                    isstatus = checkOutDoor(myHTTPConn, loginSession, EmpCode, dtdate);
                                }
                                if (isstatus == 0) {
                                    checkCompOff(myHTTPConn, loginSession, EmpCode, strdate);
                                }
                            }
                        }
                    }
                }
                if (seperatecall == true) {
                    myHTTPConn.println("" + isstatus);
                }
            } catch (Exception e) {
                AppContext.log(loginSession, e);
                myHTTPConn.println("ERROR:" + e.getMessage());
            } finally {
                AppContext.close(pstm, conn);
            }
        } catch (Exception ex) {
            Logger.getLogger(ExceptionEntryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isstatus;
    }

    private int checkDefaultWeekOff(MyHTTPConnection myHTTPConn, LoginSession loginSession, String EmpCode, int dayOfWeek) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        int isstatus = 0;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return 0;
            }
            String strQuery1 = "select WeekOff1,WeekOff2 from employeemaster where EmpCode=?";
            pstm = conn.prepareStatement(strQuery1);
            pstm.setString(1, EmpCode);
            try (ResultSet rs1 = pstm.executeQuery()) {//checking for default week off in employeemaster if in shiftroster not found
                if (rs1.next()) {
                    int weekoff1 = rs1.getInt(1);
                    int weekoff2 = rs1.getInt(2);
                    if (weekoff1 == dayOfWeek) {
                        isstatus = 1;//weekoff
                    } else if (weekoff2 == dayOfWeek) {
                        isstatus = 1;//weekoff
                    }
                }
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
        return isstatus;
    }

    private int checkLeave(MyHTTPConnection myHTTPConn, LoginSession loginSession, String EmpCode, Date date) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        int isstatus = 0;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return 0;
            }
            String strQuery3 = "select FromDate,ToDate,status from leaveapplication where EmpCode=?";
            pstm = conn.prepareStatement(strQuery3);
            pstm.setString(1, EmpCode);
            try (ResultSet rs2 = pstm.executeQuery()) {
                while (rs2.next()) {
                    String fromdate, todate, status;
                    fromdate = rs2.getString(1);
                    todate = rs2.getString(2);
                    status = rs2.getString(3);
                    if ("10".equals(status)) {
                        Date dtfrom = DateUtilities.convertStringToDate(fromdate);
                        Date dtto = DateUtilities.convertStringToDate(todate);
                        if ((dtfrom.compareTo(date) == 0) || dtfrom.compareTo(date) > 0) {
                            if ((dtto.compareTo(date) == 0) || (dtto.compareTo(date) > 0)) {
                                isstatus = 3;//OnLeave
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
        return isstatus;
    }

    private int checkOutDoor(MyHTTPConnection myHTTPConn, LoginSession loginSession, String EmpCode, Date date) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        int isstatus = 0;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return 0;
            }
            String strQuery4 = "select odFromdate,odTodate,status from outdoor where EmpCode=?";
            pstm = conn.prepareStatement(strQuery4);
            pstm.setString(1, EmpCode);
            try (ResultSet rs2 = pstm.executeQuery()) {//checking for outdoor
                while (rs2.next()) {
                    String fromdate, todate, status;
                    fromdate = rs2.getString(1);
                    todate = rs2.getString(2);
                    status = rs2.getString(3);
                    if ("10".equals(status)) {
                        Date dtfrom = DateUtilities.convertStringToDate(fromdate);
                        Date dtto = DateUtilities.convertStringToDate(todate);
                        if ((dtfrom.compareTo(date) == 0) || dtfrom.compareTo(date) > 0) {
                            if ((dtto.compareTo(date) == 0) || (dtto.compareTo(date) > 0)) {
                                isstatus = 4;//Outdoor
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
        return isstatus;
    }

    private int checkCompOff(MyHTTPConnection myHTTPConn, LoginSession loginSession, String EmpCode, String date) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        int isstatus = 0;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return 0;
            }
            String strQuery4 = "select coffentryid from compoffentry where EmpCode=? and coffdate=? and status='10'";
            pstm = conn.prepareStatement(strQuery4);
            pstm.setString(1, EmpCode);
            pstm.setString(2, date);
            try (ResultSet rs2 = pstm.executeQuery()) {//checking for compoff
                if (rs2.next()) {
                    isstatus = 5;//CompOff
                }
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
        return isstatus;
    }

    private void fillShiftCombo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String strQuery = "select ShiftCode from shiftmaster";
            pstm = conn.prepareStatement(strQuery);
            Packetizer p;
            try (ResultSet rs = pstm.executeQuery()) {
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
            AppContext.close(pstm, conn);
        }
    }

    private void getEmpShift(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String year = myHTTPConn.readLine();
            String month = myHTTPConn.readLine();
            String day = myHTTPConn.readLine();
            day = "d" + day;
            String strQuery = "select ? from shiftroster where Year=? and Month=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, day);
            pstm.setString(2, year);
            pstm.setString(3, month);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    myHTTPConn.println(rs.getString(1));
                    return;
                } else {
                    getDefaultEmpShift(myHTTPConn, loginSession);//getting default shift from employeemaster if shift not found in shiftroster
                }
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void getDefaultEmpShift(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String empcode = loginSession.getUserID();
            String strQuery = "select ShiftCode from employeemaster where EmpCode=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, empcode);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    myHTTPConn.println(rs.getString(1));
                    return;
                }
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void getShiftDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String shiftcode = myHTTPConn.readLine();
            String strQuery = "select StartTime1,EndTime1,WorkingHrs from shiftmaster where ShiftCode=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, shiftcode);
            Packetizer p = new Packetizer();
            p.setCounter();
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    p.addString(rs.getString(1));
                    p.addString(rs.getString(2));
                    p.addString(rs.getString(3));
                    p.incrCounter();
                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void getEmpCategoryDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String empcode = loginSession.getUserID();
            String catcode = "";
            String strQuery = "select CatCode from employeemaster where EmpCode=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, empcode);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    catcode = rs.getString(1);
                }
            }
            Packetizer p = new Packetizer();
            p.setCounter();
            if (!"".equals(catcode)) {
                String strQuery1 = "select OverTimeLimit,CompOffLimit,GraceLateTime,GraceEarlyTime from category where CatCode=?";
                pstm = conn.prepareStatement(strQuery1);
                pstm.setString(1, catcode);

                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        p.addString(rs.getString(1));
                        p.addString(rs.getString(2));
                        p.addString(rs.getString(3));
                        p.addString(rs.getString(4));
                        p.incrCounter();
                    }

                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void addExceptionEntry(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String packet = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(packet);

            String AppID = dp.getString();
            String appdate = dp.getString();
            String shiftcode = dp.getString();
            String in = dp.getString();
            String out = dp.getString();
            String wrkhr = dp.getString();
            String extrawrk = dp.getString();
            String prein = dp.getString();
            String preout = dp.getString();
            String preworkhr = dp.getString();
            String preextrawork = dp.getString();
            String late = dp.getString();
            String early = dp.getString();
            String status = dp.getString();
            String remark = dp.getString();
            String prelate = dp.getString();
            String preearly = dp.getString();
            String prestatus = dp.getString();
            String empcode = loginSession.getUserID();


            String strQuery = "Insert into "
                    + "exceptionentry "
                    + "(AppId,"
                    + "Date,"
                    + "EmpCode,"
                    + "ShiftCode,"
                    + "Lin,"
                    + "Lout,"
                    + "Wrkhrs,"
                    + "exthrs,"
                    + "PrevLin,"
                    + "PrevLout,"
                    + "PrevWrkhrs,"
                    + "Prevexthrs,"
                    + "late,"
                    + "early,"
                    + "Attstatus,"
                    + "Remark,"
                    + "Prevlate,"
                    + "Prevearly,"
                    + "Prevstatus) "
                    + "values "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, AppID);
            pstm.setString(2, appdate);
            pstm.setString(3, empcode);
            pstm.setString(4, shiftcode);
            pstm.setString(5, in);
            pstm.setString(6, out);
            pstm.setString(7, wrkhr);
            pstm.setString(8, extrawrk);
            pstm.setString(9, prein);
            pstm.setString(10, preout);
            pstm.setString(11, preworkhr);
            pstm.setString(12, preextrawork);
            pstm.setString(13, late);
            pstm.setString(14, early);
            pstm.setString(15, status);
            pstm.setString(16, remark);
            pstm.setString(17, prelate);
            pstm.setString(18, preearly);
            pstm.setString(19, prestatus);
            pstm.executeUpdate();
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void updateExceptionEntry(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String packet = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(packet);

            String AppID = dp.getString();
            String appdate = dp.getString();
            String shiftcode = dp.getString();
            String in = dp.getString();
            String out = dp.getString();
            String wrkhr = dp.getString();
            String extrawrk = dp.getString();
            String prein = dp.getString();
            String preout = dp.getString();
            String preworkhr = dp.getString();
            String preextrawork = dp.getString();
            String late = dp.getString();
            String early = dp.getString();
            String status = dp.getString();
            String remark = dp.getString();
            String prelate = dp.getString();
            String preearly = dp.getString();
            String prestatus = dp.getString();
            String empcode = loginSession.getUserID();


            String strQuery = "update "
                    + "exceptionentry "
                    + "set Date=?,"
                    + "EmpCode=?,"
                    + "ShiftCode=?,"
                    + "Lin=?,"
                    + "Lout=?,"
                    + "Wrkhrs=?,"
                    + "exthrs=?,"
                    + "PrevLin=?,"
                    + "PrevLout=?,"
                    + "PrevWrkhrs=?,"
                    + "Prevexthrs=?,"
                    + "late=?,"
                    + "early=?,"
                    + "Attstatus=?,"
                    + "Remark=?,"
                    + "Prevlate=?,"
                    + "Prevearly=?,"
                    + "Prevstatus=? where AppId=?"
                    + "values "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, appdate);
            pstm.setString(2, empcode);
            pstm.setString(3, shiftcode);
            pstm.setString(4, in);
            pstm.setString(5, out);
            pstm.setString(6, wrkhr);
            pstm.setString(7, extrawrk);
            pstm.setString(8, prein);
            pstm.setString(9, preout);
            pstm.setString(10, preworkhr);
            pstm.setString(11, preextrawork);
            pstm.setString(12, late);
            pstm.setString(13, early);
            pstm.setString(14, status);
            pstm.setString(15, remark);
            pstm.setString(16, prelate);
            pstm.setString(17, preearly);
            pstm.setString(18, prestatus);
            pstm.setString(19, AppID);
            pstm.executeUpdate();
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void cancelExceptionEntry(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String CFID = myHTTPConn.readLine();
            String strQuery = "Update compoffentry set status='Canceled' where coffentryid=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, CFID);
            pstm.executeUpdate();
            myHTTPConn.println("Deleted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }
}
