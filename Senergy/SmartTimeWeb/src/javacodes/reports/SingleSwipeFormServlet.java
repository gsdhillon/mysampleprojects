/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javax.servlet.ServletException;
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
public class SingleSwipeFormServlet extends HttpServlet {

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
                case "InSwipeMissing":
                    getSingleSwipeReport(myHTTPConn, loginSession, "InSwipeMissing");
                    break;
                case "OutSwipeMissing":
                    getSingleSwipeReport(myHTTPConn, loginSession, "OutSwipeMissing");
                    break;
                default:
                    myHTTPConn.println("unknownRequest");
                    break;
            }
        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }
    }

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
            out.println("<title>Servlet SingleSwipeFormServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SingleSwipeFormServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    private void getSingleSwipeReport(MyHTTPConnection myHTTPConn, LoginSession loginSession, String reqType) {
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
            String secNm;
            String getDiv = null;
            String rptStatus = null;
            String frmDate;
            String ToDate = null;

            String query = "";
            String rptType = myHTTPConn.readLine();

            switch (reqType) {
                case "InSwipeMissing":
                    rptStatus = "'" + "AA*" + "'";
                    break;
                case "OutSwipeMissing":
                    rptStatus = "'" + "AA#" + "'";
                    break;
            }



            if ("Full".equals(rptType)) {
                String mainpacket = myHTTPConn.readLine();
                String rptWise = myHTTPConn.readLine();
                String divPacket = myHTTPConn.readLine();

                Depacketizer d = new Depacketizer(mainpacket);
                frmDate = d.getString();
                ToDate = d.getString();

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
                        strDivision = strDivision + " or division.DivName=" + "'" + divNm[i] + "'";
                        getDiv = ",division.DivName";
                    }
                }
                if (!"".equals(strDivision)) {
                    strDivision = "(" + strDivision + ")";
                }
                switch (reqType) {
                    case "InSwipeMissing":
                    case "OutSwipeMissing":

                        query = "select "
                                + "muster.`date`,"
                                + "employeemaster.CCNo,"
                                + "muster.EmpCode,"
                                + "employeemaster.EmpName"
                                + getDiv + ","
                                + "(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as SecName,"
                                + "muster.ShiftCode "
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
                                + "muster.status="
                                + rptStatus + " "
                                + "and "
                                + "`Date` "
                                + "between "
                                + "'" + frmDate + "'" + " "
                                + "and "
                                + "'" + ToDate + "'" + " ";

                }
            }
            if ("Selected".equals(rptType)) {
                String DivSecWorkL = "";
                String mainpacket = myHTTPConn.readLine();
                String rptWise = myHTTPConn.readLine();
                String selEmployee = myHTTPConn.readLine();
                selEmployee = selEmployee.trim();

                Depacketizer d = new Depacketizer(mainpacket);
                frmDate = d.getString();
                ToDate = d.getString();

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
                    case "InSwipeMissing":
                    case "OutSwipeMissing":
                        query = "select "
                                + "muster.`date`,"
                                + "employeemaster.CCNo,"
                                + "muster.EmpCode,"
                                + "employeemaster.EmpName"
                                + DivSecWorkL + ","
                                + "muster.ShiftCode "
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
                                + "'" + ToDate + "'";
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
