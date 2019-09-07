package lib.MathConverters;

/**
 *
 * @author pradnya
 */
public class Converter {

    public static long byteToLong(int data[]) {
        long a = 0;
        for (int i = 0; i < data.length; i++) {
            a *= 256;
            a += data[i];
        }
        return a;
    }

    //for converting hex UID into byte 
   public static byte[] hexToByte(String data) {
        byte arrbyte[] = new byte[data.length()/2];
        int val = (data.length()/2)-1;
        for (int i = data.length(); i > 0; i = i - 2) {
            int start = i - 2;
            int end = i;
            arrbyte[val] = ((byte) (Integer.parseInt(data.substring(start, end), 16)));
            val--;
        }
        return arrbyte;
    }
   
     private String stringToHex(String str) {
        char[] chars = str.toCharArray();
        StringBuffer strBuffer = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            strBuffer.append(Integer.toHexString((int) chars[i]));
        }
        return strBuffer.toString();
    }
     
}
