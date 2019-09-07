/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.Applications;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import lib.Utility.SimpleUtilities;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author pradnya
 */
public class CoffFormServlet extends HttpServlet {

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
            out.println("<title>Servlet OutDoorFormServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OutDoorFormServlet at " + request.getContextPath() + "</h1>");
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
                case "checkCompOffAvailable":
                    checkCompOffAvailable(myHTTPConn, loginSession);
                    break;
                case "addMoreCompOff":
                    addMoreCompOff(myHTTPConn, loginSession);
                    break;
                case "updateCFApplication":
                    updateCFApplication(myHTTPConn, loginSession);
                    break;
                case "cancelCFApplication":
                    cancelCFApplication(myHTTPConn, loginSession);
                    break;
                case "checkValidCompOff":
                    checkValidCompOff(myHTTPConn, loginSession);
                    break;
            }

        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }




    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

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
            String strQuery = "select coffentryid,appdate,coffdate,workeddate,purpose,status from compoffentry where EmpCode=?";
            ResultSet rs;
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, empcode);
            rs = pstm.executeQuery();
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                for (int i = 1; i <= 6; i++) {
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

    private void updateCFApplication(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String CFDate = dp.getString();
            String workeddate = dp.getString();
            String purpose = dp.getString();
            String CFID = dp.getString();

            String strQuery = "Update compoffentry set coffdate=?,workeddate=?,purpose=? where coffentryid=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, CFDate);
            pstm.setString(2, workeddate);
            pstm.setString(3, purpose);
            pstm.setString(4, CFID);
            pstm.executeUpdate();
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void cancelCFApplication(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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

    private void addMoreCompOff(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String compoffdate = dp.getString();
            String workeddate = dp.getString();
            String purpose = dp.getString();

            String appdate = DateUtilities.getCurrentDate();
            String EmpCode = loginSession.getUserID();
            String coffentryid = "CF" + DateUtilities.getCurrentDate() + DateUtilities.getCurrentTime() + SimpleUtilities.generateRandomNumber();
            String strQuery = "Insert into compoffentry (coffentryid,EmpCode,appdate,coffdate,workeddate,purpose,status) values (?,?,?,?,?,?,'pending')";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, coffentryid);
            pstm.setString(2, EmpCode);
            pstm.setString(3, appdate);
            pstm.setString(4, compoffdate);
            pstm.setString(5, workeddate);
            pstm.setString(6, purpose);
            pstm.executeUpdate();
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void checkCompOffAvailable(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String compoffdate = dp.getString();
            String workeddate = dp.getString();
            String strQuery = "select coffentryid from compoffentry where coffdate=? and workeddate=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, compoffdate);
            pstm.setString(2, workeddate);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                myHTTPConn.println(rs.getString(1));
            } else {
                myHTTPConn.println("Not Found");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void checkValidCompOff(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String compoffdate = dp.getString();
            String workeddate = dp.getString();
            String strQuery = "select HolidayName from holiday where HolidayDate=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, workeddate);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
//                myHTTPConn.println("Holiday");
                myHTTPConn.println("true");
                return;
            } else {
                checkWeekOffShiftRoster(myHTTPConn, loginSession, workeddate);
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void checkWeekOffShiftRoster(MyHTTPConnection myHTTPConn, LoginSession loginSession, String workeddate) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String empcode = loginSession.getUserID();
            String ymd[] = workeddate.split("-");
            String day = "d" + ymd[2];

            String strQuery = "select ? from shiftroster where EmpCode=? and Year=? and Month=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, day);
            pstm.setString(2, empcode);
            pstm.setString(3, ymd[0]);//year
            pstm.setString(4, ymd[1]);//month
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                String shift = rs.getString(1);
                if ("00".equals(shift)) {
//                    myHTTPConn.println("WeekOff");
                    myHTTPConn.println("true");
                    return;
                } else {
                    checkForExtraWork(myHTTPConn, loginSession, empcode, workeddate);
                }
            } else {
                checkDefaultWeekOffInEmployee(myHTTPConn, loginSession, workeddate);
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void checkDefaultWeekOffInEmployee(MyHTTPConnection myHTTPConn, LoginSession loginSession, String workeddate) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String empcode = loginSession.getUserID();
            String ymd[] = workeddate.split("-");
            String day = "d" + ymd[2];

            String strQuery = "select WeekOff1,WeekOff2 from employeemaster where EmpCode=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, empcode);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                int weekoff1 = rs.getInt(1);
                int weekoff2 = rs.getInt(2);
                int dayofWeek = DateUtilities.getDayOfWeek(workeddate);
                if (dayofWeek == weekoff1) {
//                    myHTTPConn.println("WeekOff");
                    myHTTPConn.println("true");
                    return;
                } else if (dayofWeek == weekoff2) {
//                    myHTTPConn.println("WeekOff");
                    myHTTPConn.println("true");
                    return;
                } else {
                    checkForExtraWork(myHTTPConn, loginSession, empcode, workeddate);
                }
            } else {
                checkForExtraWork(myHTTPConn, loginSession, empcode, workeddate);
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void checkForExtraWork(MyHTTPConnection myHTTPConn, LoginSession loginSession, String empcode, String workeddate) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String ymd[] = workeddate.split("-");
            String day = "d" + ymd[2];

            String strQuery = "select status,exthrs from muster where EmpCode=? and Date=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, empcode);
            pstm.setString(2, workeddate);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                String status = rs.getString(1);
                String extrawork = rs.getString(2);
                if (!"AA*".equals(status)) {
                    if (status.length() == 3) {
                        char ch = status.charAt(2);

                        if (ch == '*' || "XX*".equals(status)) {
//                            myHTTPConn.println("ExtraWork");
                            myHTTPConn.println("true");
                            return;
                        }
                    } else {
//                        myHTTPConn.println("None");
                        myHTTPConn.println("false");
                    }
                } else {
//                    myHTTPConn.println("None");
                    myHTTPConn.println("false");
                }
            } else {
//                myHTTPConn.println("None");
                myHTTPConn.println("false");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }
}
