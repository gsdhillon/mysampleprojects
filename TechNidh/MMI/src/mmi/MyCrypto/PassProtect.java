package mmi.MyCrypto;

import mmi.data.LogReports;
import java.security.MessageDigest;

/**
 * @type     : Java Class
 * @name     : PassProtect
 * @file     : PassProtect.java
 * @created  : Jun 21, 2011 11:43:39 PM
 * @version  : 1.0.0
 */
public class PassProtect {
    public static String getDigest(char[] pass){
        try{
            String passString = new String(pass);
            if(passString.equals("")) return "";
            byte[] passBytes = passString.getBytes("UTF-8");
            MessageDigest shah1Digest = MessageDigest.getInstance("SHA1");
            byte[] degestBytes = shah1Digest.digest(passBytes);
            return Base64.base64Encode(degestBytes);
        }catch(Exception e){
            LogReports.logError(e);
            return "00000000";
        }
    }
}
