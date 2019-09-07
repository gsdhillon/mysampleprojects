/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.Applications;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javacodes.connection.*;
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
 * @author nbpatil
 */
public class CEPAppServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CEPAppServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CEPAppServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MyHTTPConnection myHTTPConn = null;
        LoginSession loginSession = null;
        try {
            response.setContentType("text/plain");
            myHTTPConn = new MyHTTPConnection(request, response);
            /**
             * ******* Get Login Session *****************
             */
            HttpSession session = request.getSession(true);
            int[] reqTypes = {
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            //Obtain the session object, create a new session if doesn't exist
            loginSession = LoginSession.getLoginSession(session, reqTypes);
            String ReqType = myHTTPConn.readLine();
            if ("getApplicationDetails".equals(ReqType)) {
                getApplicationDetails(myHTTPConn, loginSession, session, request);
            }
            System.out.println("calling completed jsp file");
        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    /**
     * 
     * @param myHTTPConn
     * @param loginSession
     * @param session
     * @param request 
     */
    private void getApplicationDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession, HttpSession session, HttpServletRequest request) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        Packetizer main = new Packetizer();
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String packet = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(packet);
            int size = dp.getInt();

            Packetizer visitorPacket;
            String companyID = "";
            String hostID = "";
            String applicantID = "";
            String approverID = "";

            Packetizer p = new Packetizer();
            visitorPacket = new Packetizer();

            for (int i = 0; i < size; i++) {
                String PVIS_ID = dp.getString();
                String strQuery = "select PVIS_ID,PCOM_ID,AppDate,DATE_FORMAT(AppTime, '%H:%i:%s') as AppTime,ApplicantID,HostID,ApproverID1,ApproverID2,Purpose,VistPlace,EntryAt from visapplication where PVIS_ID=?";
                ResultSet rs;
                pstm = conn.prepareStatement(strQuery);
                pstm.setString(1, PVIS_ID);
                rs = pstm.executeQuery();

                visitorPacket.setCounter();
                if (rs.next()) {
                    p = new Packetizer();
                    visitorPacket.addString(rs.getString("PVIS_ID"));
                    companyID = rs.getString("PCOM_ID");
                    p.addString(rs.getString("AppDate"));
                    p.addString(rs.getString("AppTime"));
                    applicantID = rs.getString("ApplicantID");
                    hostID = rs.getString("HostID");
                    approverID = rs.getString("ApproverID1");
                    p.addString(rs.getString("Purpose"));
                    p.addString(rs.getString("VistPlace"));
                    p.addString(rs.getString("EntryAt"));
                    visitorPacket.incrCounter();
                }
            }
            System.out.println(" p.getPacket() : " + p.getPacket());
            main.addString(p.getPacket());//first packet

            String visPacket = getVisitorDetails(myHTTPConn, loginSession, visitorPacket.getPacket());
            main.addString(visPacket);//2nd packet

            System.out.println(" visPacket : " + visPacket);

            String compPacket = getCompanyDetails(myHTTPConn, loginSession, companyID);
            main.addString(compPacket);//3rd packet

            System.out.println(" compPacket : " + compPacket);

            String hostPacket = getHostDetails(myHTTPConn, loginSession, hostID);
            main.addString(hostPacket);//4rth packet

            System.out.println(" hostPacket : " + hostPacket);

            String applicantPacket = getApplicantDetails(myHTTPConn, loginSession, applicantID);
            main.addString(applicantPacket);//5th packet

            System.out.println(" applicantPacket : " + applicantPacket);

            String approverPacket = getApproverDetails(myHTTPConn, loginSession, approverID);
            main.addString(approverPacket);//6th packet

            System.out.println(" approverPacket : " + approverPacket);

            session = request.getSession(true);
            session.setAttribute("data", main.getPacket());

            myHTTPConn.println(main.getPacket());

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
//        return main.getPacket();
    }

    private String getVisitorDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession, String visIDPacket) {
        System.out.println(" visIDPacket : " + visIDPacket);
        DBConnection conn = null;
        PreparedStatement pstm = null;
        Packetizer visitor = new Packetizer();
        try {
            System.out.println("Opening visitor connection");
            conn = AppContext.getDBConnection();
            System.out.println("Opened visitor connection");
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                System.out.println("ERROR:Could not get database connection");
                return "";
            }
            Depacketizer dp = new Depacketizer(visIDPacket);
            int size = dp.getInt();
            visitor.setCounter();
            for (int i = 0; i < size; i++) {
                String visID = dp.getString();
                String strQuery = "select VisName,Sex,Designation,PVC from vis_app_visitors where PVIS_ID=?";
                ResultSet rs;
                pstm = conn.prepareStatement(strQuery);
                System.out.println(" strQuery :" + strQuery);
                pstm.setString(1, visID);
                rs = pstm.executeQuery();
                if (rs.next()) {
                    visitor.addString(rs.getString("VisName"));
                    visitor.addString(visID);
                    int sex = rs.getInt("Sex");
                    if (sex == 0) {
                        visitor.addString("Male");
                    } else if (sex == 1) {
                        visitor.addString("Female");
                    }

                    visitor.addString(rs.getString("Designation"));
                    int pvc = rs.getInt("PVC");
                    if (pvc == 0) {
                        visitor.addString("No");
                    } else if (pvc == 1) {
                        visitor.addString("Yes");
                    }
                    visitor.incrCounter();
                }
            }
            System.out.println(" visitor.getPacket() :" + visitor.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
            System.out.println(" getVisitorDetails :" + e);
        } finally {
            AppContext.close(pstm, conn);
        }

        return visitor.getPacket();
    }

    private String getCompanyDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession, String companyID) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        Packetizer compPacket = new Packetizer();
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return "";
            }

            String strQuery = "select ComName,ComType,ComAddr from vis_app_company where PCOM_ID=?";
            ResultSet rs;
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, companyID);
            rs = pstm.executeQuery();
            if (rs.next()) {
                rs.beforeFirst();
                while (rs.next()) {
                    compPacket.addString(rs.getString(1));
                    int companytype = rs.getInt(2);
                    String comptype = "None";
                    if (companytype == 0) {
                        comptype = "Private";
                    } else if (companytype == 1) {
                        comptype = "Govt.";
                    } else if (companytype == 2) {
                        comptype = "SemiGovt.";
                    }
                    compPacket.addString(comptype);
                    compPacket.addString(rs.getString(3));
                }
            } else {
                compPacket.addString("");
                compPacket.addString("");
                compPacket.addString("");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
        return compPacket.getPacket();
    }
    /**
     * 
     * @param myHTTPConn
     * @param loginSession
     * @param hostID
     * @return 
     */
    private String getHostDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession, String hostID) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        Packetizer hostPacket = new Packetizer();
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return "";
            }

            String strQuery = "select EmpName,(select DivName from division where division.DivCode=employeemaster.DivCode) as division,MobileNo from employeemaster where EmpCode=?";
            ResultSet rs;
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, hostID);
            rs = pstm.executeQuery();
            if (rs.next()) {
                rs.beforeFirst();
                while (rs.next()) {
                    hostPacket.addString(hostID);
                    hostPacket.addString(rs.getString(1));
                    hostPacket.addString(rs.getString(2));
                    hostPacket.addString(rs.getString(3));
                }
            } else {
                hostPacket.addString("");
                hostPacket.addString("");
                hostPacket.addString("");
                hostPacket.addString("");
            }

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
        return hostPacket.getPacket();
    }

    private String getApplicantDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession, String applicantID) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        Packetizer applicantPacket = new Packetizer();
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return "";
            }

            String strQuery = "select EmpName,(select DivName from division where division.DivCode=employeemaster.DivCode) as division,MobileNo from employeemaster where EmpCode=?";
            ResultSet rs;
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, applicantID);
            rs = pstm.executeQuery();
            if (rs.next()) {
                rs.beforeFirst();
                while (rs.next()) {
                    applicantPacket.addString(applicantID);
                    applicantPacket.addString(rs.getString(1));
                    applicantPacket.addString(rs.getString(2));
                    applicantPacket.addString(rs.getString(3));
                }
            } else {
                applicantPacket.addString("");
                applicantPacket.addString("");
                applicantPacket.addString("");
                applicantPacket.addString("");
            }

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
        return applicantPacket.getPacket();
    }

    private String getApproverDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession, String approverID) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        Packetizer approverPacket = new Packetizer();
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return "";
            }

            String strQuery = "select EmpName,(select DivName from division where division.DivCode=employeemaster.DivCode) as division,CCNo from employeemaster where EmpCode=?";
            ResultSet rs;
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, approverID);
            rs = pstm.executeQuery();
            if (rs.next()) {
                rs.beforeFirst();
                while (rs.next()) {
                    approverPacket.addString(rs.getString(1));
                    approverPacket.addString(rs.getString(2));
                    approverPacket.addString(rs.getString(3));
                }
            } else {
                approverPacket.addString("");
                approverPacket.addString("");
                approverPacket.addString("");
                approverPacket.addString("");
            }

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
        return approverPacket.getPacket();
    }
}
