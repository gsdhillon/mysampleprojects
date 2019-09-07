package gurmeet.auth;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author ADMIN
 */
@RequestScoped
public class User {
    private static final long timeToLive = 1000*60*30;//30 minute
    //
    private String role;
    private String userId;
    private long loginTime;
    boolean authOK = false;
        
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" + "role=" + role + ", userId=" + userId + ", loginTime=" + loginTime + '}';
    }

   

    public boolean authenticate(String password) {
        return ("fw0r".equals(password));
    }

    public String getToken() throws Exception{
        loginTime = System.currentTimeMillis();
        //verify signature **TODO
        String sign = getSign();
        return Base64.encode((role+":"+userId+":"+loginTime+":"+sign).getBytes("UTF-8"));
    }
    /**
     * 
     * @param token
     * @param requiredRoles
     * @param requestInfo
     * @return
     * @throws Exception 
     */
    public boolean verify(String token, String[] requiredRoles, String requestInfo) throws Exception{
        //parse token
        String[] params = new String(Base64.decode(token), "UTF-8").split(":");
        role = params[0];
        userId = params[1];
        loginTime = Long.parseLong(params[2]);
        String sign = params[3];
        
        //check sign
        if(!isSignValid(sign)){
            System.out.println("invalid token user "+this);
            return false;
        }

        //check expiry
        long validityUpto = loginTime + timeToLive;
        long currentTime = System.currentTimeMillis();
        if(currentTime > validityUpto){
            System.out.println("token expired for user "+this);
            return false;
        }
        
        //check role
        if(requiredRoles==null || requiredRoles.length==0){
            System.out.println("*** required roles not set for service "+requestInfo );
            return false;
        }
        boolean found = false;
        for(String arole : requiredRoles){
            if(role.equals(arole)){
                found = true;
                break;
            }
        }
        if(!found){
            System.out.println("Auth failed - Role '"+role+"' "
                    + "is not allowed for service "+requestInfo );
            return false;
        }

        //all ok
        System.out.println("Security check OK user "+this);
        return true;
    }

    private String getSign() {
       return "[signbyteshere]";
    }

    private boolean isSignValid(String sign) {
        return "[signbyteshere]".equals(sign);
    }
}