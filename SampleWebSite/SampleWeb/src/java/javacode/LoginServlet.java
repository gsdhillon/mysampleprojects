package javacode;

import java.io.*;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
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
            PrintWriter out = resp.getWriter();
            HttpSession session = req.getSession(false);
            if(session == null){
                out.println("ERROR: No session found.");
                return;
            }
            out.println("This is GET method.");
            out.close();
        }catch(Exception e){
            System.out.println("GET request from "+remoteAddr);
            e.printStackTrace();
        }
    }
    /**
     * 
     * @param req
     * @param resp 
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp){
        MCC mcc = null;
        try{
            mcc = new MCC(req, resp);
            resp.setContentType("text/plain");
            //get session
            HttpSession session = req.getSession(false);
            if(session == null){
                mcc.println("ERROR: No session found.");
                return;
            }
            //read the message from the servlet
            String msg = mcc.readLine();
            String remoteIP = req.getRemoteAddr();
            if(msg.equals("userlogin")){
                doUserLogin(session, mcc, remoteIP);
            }else{
                mcc.println("ERROR: Unknown request "+msg+" from "+remoteIP);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            AppContext.close(mcc);
        }
    }
    /**
     * 
     * @param session
     * @param br
     * @return ''
     */
    private void doUserLogin(HttpSession session, MCC mcc, String remoteIP){
        try{
            MySQLQuery.setSQLPath(this);
            String packet = mcc.readLine();
            Depacketizer d = new Depacketizer(packet);
            //read logintype username password
            int loginType = d.getInt();
            String user = d.getString();
            String pass = d.getString();
            mcc.println(doLogin(session,user,pass, loginType));
        }catch(Exception e){
            mcc.println("ERROR:"+e.getMessage());  
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
        //create session
        LoginSession loginSession = new LoginSession(session, user, pass, loginType);
        if(loginSession.isValid()){
            return("OK:logged in successfully as BARC employee");
        }else{
            return("ERROR:"+loginSession.lastMsg);
        }
    }
}