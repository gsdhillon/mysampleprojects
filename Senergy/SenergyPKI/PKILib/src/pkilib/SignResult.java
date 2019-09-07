package pkilib;

/**
 * SignVerifyResult.java
 * Created on Apr 2, 2009, 1:53:03 PM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class SignResult {
    public int statusCode = -1;
    public String description;
    public String certSNO;
    public String sign;
    public String signDate;
    /**
     * 
     */
    public void show(){
        System.out.println("-----------Sign Result-----------------");
        System.out.println("Status code = "+statusCode);
        System.out.println("Cert S.No.  = "+certSNO);
        System.out.println("Sign Date   = "+signDate);
        int signeLen = 0;
        if(sign!= null){
            signeLen = sign.length();
        }
        System.out.println("Sign len = "+signeLen);
        System.out.println("Description = "+description);
        System.out.println("-----------End Sign Result-------------");
    }
}
