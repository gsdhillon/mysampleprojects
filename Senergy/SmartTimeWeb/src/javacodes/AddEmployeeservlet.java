package javacodes;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javacodes.connection.AppContext;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author GAURAV
 */
public class AddEmployeeservlet extends HttpServlet {
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MyHTTPConnection myHTTPConn = null;
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
            LoginSession loginSession = LoginSession.getLoginSession(session, reqTypes);
 
            /*
             * Read in the message from the servlet
             */
            String msg = myHTTPConn.readLine();
           
           // new FileWriter("test.txt");
            /*
             * Write the message back to the applet
             */
            if (msg.equals("AddEmployee")) {
                myHTTPConn.println("thanks");
               // AddEmployee(myHTTPConn);
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void AddEmployee(MyHTTPConnection myHTTPConn) {
        //myHTTPConn.
    }
}
