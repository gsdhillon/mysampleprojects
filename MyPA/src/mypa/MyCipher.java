package mypa;

import java.awt.Color;
import java.awt.Dimension;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
/**
 * @version 1.0
 * @author  Gurmeet Singh - gsdhillon@gmail.com
 */
@SuppressWarnings({"CallToThreadDumpStack", "UseSpecificCatch"})
public class MyCipher{
    private static final String MY_IV = "Gurmeet Singh Dh";
    private static final String KEY_SPEC = "PBKDF2WithHmacSHA1";
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;
    private static final String ALGORITHM_SPEC = "AES/CBC/PKCS5Padding";
    private static final String CHAR_SET = "UTF-8";
    private static final String MY_SALT = "gsdhillon@gmail.com";
    private static final int ITERATION_COUNT = 1976;
    /**
     * 
     * @return 
     */
    private static char[] getPassPhrase(){
        JPasswordField pf = new JPasswordField();
        pf.setBackground(new Color(250, 250, 255));
        pf.setPreferredSize(new Dimension(80,35));
        JOptionPane.showMessageDialog(null, pf, "Enter Pass Phrase", JOptionPane.QUESTION_MESSAGE);
        return pf.getPassword();
    }
    /**
     * 
     * @param input
     * @return 
     */
    public static String encrypt(byte[] input) throws Exception{
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_SPEC);
        byte[] salt = MY_SALT.getBytes(CHAR_SET);
        SecretKey key = skf.generateSecret(new PBEKeySpec(getPassPhrase(), salt, ITERATION_COUNT, KEY_SIZE));
        SecretKeySpec aesKeySpec = new SecretKeySpec(key.getEncoded(), ALGORITHM);//
        Cipher cipher = Cipher.getInstance(ALGORITHM_SPEC);
        IvParameterSpec iv = new IvParameterSpec(MY_IV.getBytes(CHAR_SET));
        cipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, iv);
        byte[] output = cipher.doFinal(input);
        return MyBase64.encode(output);
    }
    /**
     * 
     * @param input
     * @return 
     */
    public static byte[] decrypt(String input) throws Exception{
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_SPEC);
        byte[] salt = MY_SALT.getBytes(CHAR_SET);
        SecretKey key = skf.generateSecret(new PBEKeySpec(getPassPhrase(), salt, ITERATION_COUNT, KEY_SIZE));
        SecretKeySpec aesKeySpec = new SecretKeySpec(key.getEncoded(), ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(MY_IV.getBytes(CHAR_SET));
        Cipher cipher = Cipher.getInstance(ALGORITHM_SPEC);
        cipher.init(Cipher.DECRYPT_MODE, aesKeySpec, iv);
        byte[] output = cipher.doFinal(MyBase64.decode(input));
        return output;
    }
    
    /*
    * RFC-2045 (Section 6.8)
    * @author Gurmeet Singh
    */
    private static class MyBase64 {
    //   private static final String UTF8 = "UTF-F";
        private static final char PADD = '=';
        private static final byte[] encoding = {
            //RFC-2045 (Section 6.8)
            (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F',
            (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
            (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R',
            (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X',
            (byte) 'Y', (byte) 'Z',
            (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
            (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l',
            (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r',
            (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x',
            (byte) 'y', (byte) 'z',
            (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
            (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '/'};
        private static final byte[] decoding;
        static {
            decoding = new byte[128];
            for (int i = 0; i < encoding.length; i++) {
                decoding[encoding[i]] = (byte) i;
            }
        }
        /**
        */
        private MyBase64() {
        }
        /**
        *
        * @param in
        * @return
        */
        public static String encode(byte[] in) throws Exception{
            if ((in == null) || (in.length == 0)) {
                throw new IllegalArgumentException("NULL or empty input bytes!");
            }
            byte buffer[] = new byte[((in.length + 2) / 3) * 4];
            //3-byte to 4-byte conversion
            int src, dest;
            for (src = 0, dest = 0; src < in.length - 2; src += 3) {
                buffer[dest++] = encoding[(in[src] >>> 2) & 077];
                buffer[dest++] = encoding[(in[src + 1] >>> 4) & 017 | (in[src] << 4) & 077];
                buffer[dest++] = encoding[(in[src + 2] >>> 6) & 003 | (in[src + 1] << 2) & 077];
                buffer[dest++] = encoding[in[src + 2] & 077];
            }
            //Convert the last 1 or 2 bytes
            if (src < in.length) {
                buffer[dest++] = encoding[(in[src] >>> 2) & 077];
                if (src < in.length - 1) {
                    buffer[dest++] = encoding[(in[src + 1] >>> 4) & 017 | (in[src] << 4) & 077];
                    buffer[dest++] = encoding[(in[src + 1] << 2) & 077];
                } else {
                    buffer[dest++] = encoding[(in[src] << 4) & 077];
                }
            }
            //Add padding to the end of encoded data
            while (dest < buffer.length) {
                buffer[dest] = (byte) PADD;
                dest++;
            }
            String result = new String(buffer);//, UTF8
            return result;
        }

        /**
        *
        * @param in
        * @return
        */
        public static byte[] decode(String in) throws Exception{
            if ((in == null) || (in.length() == 0)) {
                throw new IllegalArgumentException("NULL or empty input base64 string!");
            }
            byte[] data = in.getBytes();//UTF8
            //remove padding '=' from the end of base64 data
            int endOfBase64Chars = data.length;
            while (data[endOfBase64Chars - 1] == '=') {
                endOfBase64Chars--;
            }
            byte buffer[] = new byte[endOfBase64Chars - data.length / 4];
            //ASCII-printable to 0-63 conversion
            for (int i = 0; i < data.length; i++) {
                data[i] = decoding[data[i]];
            }
            //4-byte to 3-byte conversion
            int srcIndex, destIndex;
            for (srcIndex = 0, destIndex = 0; destIndex < buffer.length - 2; srcIndex += 4, destIndex += 3) {
                buffer[destIndex] = (byte) (((data[srcIndex] << 2) & 255) | ((data[srcIndex + 1] >>> 4) & 003));
                buffer[destIndex + 1] = (byte) (((data[srcIndex + 1] << 4) & 255) | ((data[srcIndex + 2] >>> 2) & 017));
                buffer[destIndex + 2] = (byte) (((data[srcIndex + 2] << 6) & 255) | (data[srcIndex + 3] & 077));
            }
            //Handle last 1 or 2 bytes
            if (destIndex < buffer.length) {
                buffer[destIndex] = (byte) (((data[srcIndex] << 2) & 255) | ((data[srcIndex + 1] >>> 4) & 003));
            }
            if (++destIndex < buffer.length) {
                buffer[destIndex] = (byte) (((data[srcIndex + 1] << 4) & 255) | ((data[srcIndex + 2] >>> 2) & 017));
            }
            return buffer;
        }
    }
}