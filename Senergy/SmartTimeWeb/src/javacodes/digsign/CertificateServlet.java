package javacodes.digsign;

import java.io.IOException;
import java.io.PrintWriter;
import javacodes.connection.AppContext;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * CertificateServlet.java
 */
public class CertificateServlet extends HttpServlet {
   /**
     * Handles the HTTP
     * <code>GET</code> method.
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
            PrintWriter out = resp.getWriter();
            out.println("Error: this servlet does not support the GET method!");
            out.close();
        } catch (Exception e) {
            System.out.println("GET request from " + remoteAddr);
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        MyHTTPConnection myHTTPConn = null;
        LoginSession loginSession = null;
        try {

            resp.setContentType("text/plain");
            myHTTPConn = new MyHTTPConnection(req, resp);

            HttpSession session = req.getSession(false);
            int[] reqTypes = {
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            loginSession = LoginSession.getLoginSession(session, reqTypes);

            String ReqType = myHTTPConn.readLine();

            if (ReqType.equals("getcert_sno")) {

            } else if (ReqType.equals("uploadcert")) {

            }
        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }
    }
}
