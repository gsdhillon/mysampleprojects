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
import lib.utils.Packetizer;

/**
 *
 * @author nbpatil
 */
public class commonservlet extends HttpServlet {

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
            out.println("<title>Servlet commonservlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet commonservlet at " + request.getContextPath() + "</h1>");
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
                case "getAproovingAuthority":
                    getAproovingAuthority(myHTTPConn, loginSession);
                    break;
            }

        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }




    }

    private void getAproovingAuthority(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String empcode = loginSession.getUserID();
            String empname = "";
            String ccno = "";
            String designation = "";
            String divname = "";
            String divheadcode = "";
            String secheadcode = "";
            String divHeadname = "";
            String secheadname = "";

            String strQuery = "Select "
                    + "EmpName,"
                    + "CCNo,"
                    + "(select DesigName from designationmaster where designationmaster.DesigID=employeemaster.DesigID) as designation,"
                    + "(select DivName from division where division.DivCode=employeemaster.DivCode) as DivName,"
                    + "(select DivHeadCode from division where division.DivCode=employeemaster.DivCode) as DivHeadCode,"
                    + "(select SecHeadCode from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as SecHeadCode "
                    + "from "
                    + "employeemaster "
                    + "where "
                    + "EmpCode=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, empcode);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    empname = rs.getString("EmpName");
                    ccno = rs.getString("CCNo");
                    designation = rs.getString("designation");
                    divname = rs.getString("DivName");
                    divheadcode = rs.getString("DivHeadCode");
                    secheadcode = rs.getString("SecHeadCode");
                }
            }
            //Getting Division head name
            if (!"".equals(divheadcode) && divheadcode != null) {
                strQuery = "Select EmpName from employeemaster where EmpCode=?";
            }
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, divheadcode);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    divHeadname = rs.getString("EmpName");
                } else {
                    divHeadname = "";
                }
            }
            //Getting Section head name
            if (!"".equals(secheadcode) && secheadcode != null) {
                strQuery = "Select EmpName from employeemaster where EmpCode=?";
            }
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, secheadcode);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    secheadname = rs.getString("EmpName");
                } else {
                    secheadname = "";
                }
            }
            Packetizer p = new Packetizer();
            p.addString(empcode);
            p.addString(empname);
            p.addString(ccno);
            p.addString(designation);
            p.addString(divname);
            p.addString(divheadcode);
            p.addString(divHeadname);
            p.addString(secheadcode);
            p.addString(secheadname);
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }
}
