package javacodes.vis_app;

import SmartTimeApplet.visitor.vis_app.VisitorForm;
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
 * VisAppObjectServlet.java
 */
public class VisAppObjectServlet extends HttpServlet {

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        MyHTTPConnection myHTTPConn = null;
        LoginSession loginSession = null;
        try {
            resp.setContentType("text/plain");
            myHTTPConn = new MyHTTPConnection(req, resp);
            HttpSession session = req.getSession(false);
            int[] reqTypes = {
                LoginSession.TYPE_USER
            };
            loginSession = LoginSession.getLoginSession(session, reqTypes);
            VisitorForm form = (VisitorForm) myHTTPConn.readObject();
            if (form.servletRequest.equals("submit_form")) {
                VisAppClass.submitNewForm(loginSession, form, myHTTPConn);
            }

        } catch (Exception e) {
            AppContext.log(loginSession, e);
        } finally {
            AppContext.close(myHTTPConn);
        }
    }

    /**
     *
     * @param req
     * @param resp
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
            //System.out.println("GET request from "+remoteAddr);
            AppContext.log(null, e);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "ReceiveObjects of VisApp";
    }// </editor-fold>
}