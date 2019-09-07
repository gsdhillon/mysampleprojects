package pkilib;

/**
 * EncryptDataResult.java
 * Created on Jul 14, 2009, 1:53:03 PM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class EncryptDataResult {
    public int statusCode = -1;
    public String encryptedData;
    public String description;
    /**
     * 
     */
    public void show(){
        System.out.println("-----------EncryptDataResult-----------------");
        System.out.println("Status code = "+statusCode);
        System.out.println("EncryptedData = "+encryptedData);
        System.out.println("Description = "+description);
        System.out.println("-----------End EncryptDataResult-------------");
    }
}
