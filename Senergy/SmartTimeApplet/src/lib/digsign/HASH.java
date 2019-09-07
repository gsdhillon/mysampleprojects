package lib.digsign;

import java.security.*;
/**
 * HASH.java
 */
public class HASH {
    public static String getMD5(String s) throws Exception{
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(s.getBytes(),0,s.length());
        return Base64Tool.base64Encode(m.digest());
    }
    public static byte[] getSHA1(byte[] data) throws Exception{
        MessageDigest m=MessageDigest.getInstance("SHA-1");
        m.update(data);
        return m.digest();
    }
}