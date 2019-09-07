package javacode;

import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpSession;
/**
 */
public class LoginSession{
    public static final String LOGIN_SESSION_ATTRIBUTE = "login_session";
    public static final int TYPE_USER = 0;
    public static final int TYPE_OFFICE = 1;
    public static final int TYPE_ADMIN = 2;
    private static final String[] loginTypes = {
        "User",//0
        "Office",//1
        "Administrator"//2
    };
    private int loginType = -1;
    private boolean valid;
    public String lastMsg;
    private String userID;
    private String name;
    private String desig;
    private String division;
    /**
     * 
     * @param loginType
     * @param loginID
     * @param pass
     */
    public LoginSession(HttpSession session, String loginID, String pass, int loginType){
        try{
            this.loginType = loginType;
            if(authenticateUser(loginID, pass, loginType)){
                session.setAttribute(LOGIN_SESSION_ATTRIBUTE, this);
                this.valid = true;
                this.lastMsg = "Authentiction successful";
            }else{
                this.valid = false;
            }
        }catch(Exception e){
            this.valid = false;
            this.lastMsg = "Authentiction Failed;"+e.getMessage();
        }
    }
    /**
     * 
     * @param session
     * @return ''
     */
    public static LoginSession getLoginSession(HttpSession session, int[] reqTypes) throws Exception{
        try{
            if(reqTypes==null || reqTypes.length==0){
                throw new Exception("ERROR:LoginType Unidentified!");
            }
            if(session == null){
                throw new Exception("ERROR:Session Expired. Please Re-Login!");
            }
            LoginSession bs = (LoginSession)session.getAttribute(LOGIN_SESSION_ATTRIBUTE);
            if(bs==null){
                throw new Exception("ERROR:Session Expired. Please Re-Login!");
            }
            if(!bs.isValid()){
                throw new Exception("ERROR:Session is not VALID.");
            }
            int loginType = bs.getLoginType();
            for(int i=0;i<reqTypes.length;i++){
                if(loginType==reqTypes[i]){
                    return bs;
                }
            }
            String msg = "ERROR:InvalidLoginType Required ["+loginTypes[reqTypes[0]];
            for(int i=1;i<reqTypes.length;i++){
                msg+= ", "+loginTypes[reqTypes[i]];
            }
            msg+= "]";
            throw new Exception(msg);  
        }catch(Exception e){
            throw e;
        }
    }
    /**
     * 
     * @return '' 
     */
    public String getDisplayName(){
        if(loginType == TYPE_OFFICE){
            return name+"["+division+"]";
        }else if(loginType == TYPE_ADMIN){
            return name+"[Administrator]";
        }else{
            return name;
        }
    }
    
    /**
     *
     * @param loginID
     * @param pass
     * @param loginType
     * @return
     */
    private boolean authenticateUser(String loginID, String pass, int loginType){
        DBConnection conn = null;
        Statement stmt = null;
        try{
            conn = AppContext.getDBConnection();
            if(conn==null){
                lastMsg = "Could not get database connection";
                return false;
            }
            // Query emp table
            stmt = conn.createStatement();
            MySQLQuery msq = new MySQLQuery("EMP_LOGIN");
            msq.setNumber("user_id", loginID);
            String query = msq.getQuery();
            ResultSet rs = stmt.executeQuery(query);
            if(!rs.next()){
                rs.close();
                lastMsg = "Login name '"+loginID+"' is not present!";
                return false;
            }
            if(!rs.getString("password").equals(pass)){
                rs.close();
                lastMsg = "Login password '"+loginID+"' incorrect!";
                return false;
            }
            userID = loginID;
            name = rs.getString("name");
            desig = rs.getString("desig");
            division = rs.getString("division");
            //TODO check login type also
            this.loginType = loginType;
            rs.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            lastMsg = e.getMessage();
            return false;
        }finally{
            AppContext.close(stmt, conn);
        }
    }

    public String getDesig() {
        return desig;
    }


    public String getDivision() {
        return division;
    }


    public String getLastMsg() {
        return lastMsg;
    }

    public int getLoginType() {
        return loginType;
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }
    /**
     * 
     */
    public boolean isValid(){
        return valid;
    }
    /**
     */
    public void invalidate(){
        valid = false;
    }
}