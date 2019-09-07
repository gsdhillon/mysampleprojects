package pkilib;

/**
 * DecryptRSAResult.java
 * Created on Jul 14, 2009, 1:53:03 PM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class DecryptRSAResult {
    public int statusCode = -1;
    public byte[] keyBytes;
    public String description;
    /**
     * 
     */
    public void show(){
        System.out.println("-----------DecryptRSAResult-----------------");
        System.out.println("Status code = "+statusCode);
        /*for(int i=0;i<keyBytes.length;i++){
            System.out.print(keyBytes[i]+" ");
        }
        System.out.println();*/
        System.out.println("Description = "+description);
        System.out.println("-----------End DecryptRSAResult-------------");
    }
}
