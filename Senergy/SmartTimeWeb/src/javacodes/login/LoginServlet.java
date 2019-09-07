package javacodes.login;

import java.io.PrintWriter;
import javacodes.connection.AppContext;
import javacodes.connection.MyHTTPConnection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.utils.Depacketizer;
/**
 */
public class LoginServlet extends HttpServlet {
    /**
     * 
     * @param req
     * @param resp 
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){
        String remoteAddr = req.getRemoteAddr();
        try{
            resp.setContentType("text/plain");
            try (PrintWriter out = resp.getWriter()) {
                HttpSession session = req.getSession(false);
                if(session == null){
                    out.println("ERROR: No session found.");
                    return;
                }
                out.println("This is GET method.");
            }
        }catch(Exception e){
            System.out.println("GET request from "+remoteAddr);
        }
    }
    /**
     * 
     * @param req
     * @param resp 
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp){
        MyHTTPConnection myHTTPConn = null;
        try{
            myHTTPConn = new MyHTTPConnection(req, resp);
            resp.setContentType("text/plain");
            //get session
            HttpSession session = req.getSession(false);
            if(session == null){
                myHTTPConn.println("ERROR: No session found.");
                return;
            }
            //read the message from the servlet
            String msg = myHTTPConn.readLine();
            String remoteIP = req.getRemoteAddr();
            if(msg.equals("userlogin")){
                doUserLogin(session, myHTTPConn, remoteIP);
            }else{
                myHTTPConn.println("ERROR: Unknown request "+msg+" from "+remoteIP);
            }
        }catch(Exception e){
        }finally{
            AppContext.close(myHTTPConn);
        }
    }
    /**
     * 
     * @param session
     * @param br
     * @return ''
     */
    private void doUserLogin(HttpSession session, MyHTTPConnection myHTTPConn, String remoteIP){
        try{
            String packet = myHTTPConn.readLine();
            Depacketizer d = new Depacketizer(packet);
            //read logintype username password
            int loginType = d.getInt();
            String user = d.getString();
            String pass = d.getString();
            String result = doLogin(session,user,pass, loginType);
            myHTTPConn.println(result);
        }catch(Exception e){
            myHTTPConn.println("ERROR:"+e.getMessage());  
        }
    }
    /**
     * 
     * @param session
     * @param user
     * @param pass
     * @return ''
     */
    private String doLogin(HttpSession session, String user, String pass, int loginType) {
        //create boost session
        LoginSession loginSession = new LoginSession(session, user, pass, loginType);
        if(loginSession.isValid()){
            return("OK:logged in successfully as BARC employee");
        }else{
            return("ERROR:"+loginSession.lastMsg);
        }
    }
}