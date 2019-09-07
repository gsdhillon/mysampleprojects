package pkilib;

/**
 * SignVerifyResult.java
 * Created on Jul 14, 2009, 1:53:03 PM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class GetKeyResult {
    public int statusCode = -1;
    public String encryptedKey;
    public String description;
    /**
     * 
     */
    public void show(){
        System.out.println("-----------GetKeyResult-----------------");
        System.out.println("Status code = "+statusCode);
        System.out.println("Encrypted key len  = "+encryptedKey.length());
        System.out.println("Description = "+description);
        System.out.println("-----------End GetKeyResult-------------");
    }
}
