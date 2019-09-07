package SwipeCollection;


import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class MU {
    private static char[] hexDigits =
    {'0', '1', '2', '3', '4', '5', '6', '7',
     '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    /**
     *
     * @param f
     * @return hexified hash bytes
     * @throws Exception
     */
    public static String getHash(String home, String empno) throws Exception{

        int x = Integer.parseInt(empno);
        empno = x+"";
        

        File f = new File(home+"/Photo/"+empno+".JPG");

        if(!f.exists()){
            throw new Exception("File "+f.getAbsolutePath()+" Not Found");
        }
        MessageDigest m =MessageDigest.getInstance("SHA-1");
        FileInputStream fis = new FileInputStream(f);
        byte[] buffer = new byte[1024*4];
        int len = 0;
        while((len = fis.read(buffer)) > 0) {
            m.update(buffer,0,len);
        }
        fis.close();
        return hexify(m.digest());
    }

    /**
     * @param b
     */
    public static String hexify(byte b){
    	char c1 = hexDigits[(b & 0xf0) >> 4];
    	char c2 = hexDigits[b & 0x0f];
    	return ""+c1+c2;
    }
    /**
     *
     * @param shortVal
     * @return
     */
    public static String hexify(short shortVal) {
        int number = shortVal;
        String s = "";
        for (int i = 0; i < 4; i++) {
            s = (hexDigits[number & 0x000f])+s;
            number = number >> 4;
        }
        return s;
    }
    /**
     *
     * @param intVal
     * @return
     */
    public static String hexify(int intVal) {
        String s = "";
        for (int i = 0; i < 8; i++) {
            s = (hexDigits[intVal & 0x000f])+s;
            intVal = intVal >> 4;
        }
        return s;
    }
    /**
     *
     * @param bytes
     * @return
     */
    public static String hexify(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(hexDigits[(bytes[0] & 0xf0) >> 4]);
        buffer.append(hexDigits[bytes[0] & 0x0f]);
        for (int i = 1; i < bytes.length; i++) {
            //buffer.append(':');
            buffer.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
            buffer.append(hexDigits[bytes[i] & 0x0f]);
        }
        return buffer.toString();
    }
    /**
     *
     * @param bytes
     * @return
     */
    public static String hexify(byte[] bytes, int start, int len) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(hexDigits[(bytes[start] & 0xf0) >> 4]);
        buffer.append(hexDigits[bytes[start] & 0x0f]);
        for (int i = start+1; i < start+len; i++) {
            //buffer.append(':');
            buffer.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
            buffer.append(hexDigits[bytes[i] & 0x0f]);
        }
        return buffer.toString();
    }
    /**
     * convert hex data to bytes
     * @param bytes
     * @return
     */
    public static byte[] deHexify(String hex) throws Exception{
            if((hex.length()%2)!= 0){
                    throw new Exception("Data in hex is of ODD Length!");
            }
        byte[] buffer = new byte[hex.length()/2];
        char[] chars = hex.toCharArray();
        for(int i=0;i<chars.length;i=i+2){
            //get value of ith char
            int val1 = 0;
            int j;
            for(j=0;j<16;j++){
                if(chars[i] == hexDigits[j]){
                    val1 = j;
                    break;
                }
            }
            if(j==16){
                throw new Exception("Invalid Char "+chars[i]+" at position "+(i+1));
            }
            //get value of i+1th char
            int val2 = 0;
            for(j=0;j<16;j++){
                if(chars[i+1] == hexDigits[j]){
                    val2 = j;
                    break;
                }
            }
            if(j==16){
                throw new Exception("Invalid Char "+chars[i+1]+" at position "+(i+2));
            }
            buffer[i/2] = (byte) ((val1 << 4) | val2);
        }
        return buffer;
    }
    /**
     * @param response
     * @return
     */
    public static String getStatusCode(byte[] response){
        if(response == null || response.length < 2){
            return "NULL";
        }
        int len = response.length;
        String statusCode = MU.hexify(response[len-2])+ MU.hexify(response[len-1]);
        return statusCode;
    }
    /**
     * @param response
     * @return
     */
    public static byte[] getData(byte[] response){
        if(response == null || response.length < 2){
            return null;
        }
        if(response.length==2){
            return new byte[0];
        }else{
            byte[] data = new byte[response.length-2];
            arrayCopy(response, 0, data, 0, response.length-2);
            return data;
        }
    }

    public static Date getCurrentDate(){
        return new Date();
    }

    /**
     *
     * @return formated current datetime string in ddMMyyHHmmss
     */
    public static int getDateInSeconds(){
        return (int)((new Date().getTime())/1000L);
    }

    /**
     *
     * @return
     */
    public static long getCurrentTime(){
        return (getCurrentDate().getTime());
    }
    /**
     *
     * @return
     */
    public static String getCurrentDateTimeDisplay(){
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return f.format(getCurrentDate());
    }
    /**
     *
     * @param dateTime in ddMMyyHHmmss
     * @return
     */
    public static Date stringToDate(String dateTime){
        try{
            DateFormat f = new SimpleDateFormat("ddMMyyHHmmss");
            return f.parse(dateTime);
        }catch (Exception e) {
            return null;
        }
    }
    /**
     *
     * @param dateTime in ddMMyyHHmmss
     * @return
     */
    public static String toTime(String dateTime){
        try{
            DateFormat f1 = new SimpleDateFormat("ddMMyyHHmmss");
            DateFormat f2 = new SimpleDateFormat("HH:mm:ss");
            return f2.format(f1.parse(dateTime));
        }catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param dateTime in ddMMyyHHmmss
     * @return
     */
    public static String toDisplayFormat(String dateTime){
        try{
            DateFormat f1 = new SimpleDateFormat("ddMMyyHHmmss");
            DateFormat f2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            return f2.format(f1.parse(dateTime));
        }catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param date - dd-MM-yyyy
     * @return - No. of seconds passed 01-01-1970 00:00:00 IST in Hexadecimal four bytes
     * @throws Exception
     */
    public static String dateToSeconds(String date) throws Exception{
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        f.setTimeZone(TimeZone.getTimeZone("IST"));
        Date d = f.parse(date);
        long l = d.getTime()/1000L;
        return hexify((int) l);
    }

    /**
     *
     * @param date in format (dd-MM-yyyy HH:mm:ss OR dd-MM-yyyy )in IST
     * @return - No. of seconds passed 01-01-1970 00:00:00 IST Hexadecimal four bytes
     * @throws Exception
     */
    public static String dateStringToSeconds(String date) throws Exception{
        if(date.length()==10){
                date += " 00:00:00";
        }
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        f.setTimeZone(TimeZone.getTimeZone("IST"));
        Date d = f.parse(date);
        long l = d.getTime()/1000L;
        return hexify((int) l);
    }
    /**
     *
     * @param seconds - No. of seconds passed 01-01-1970 00:00:00 IST
     * @return - dd-MM-yyyy HH:mm:ss
     */
    public static Date secondsToDate(int seconds){
        long time = seconds * 1000L;
        Date d = new Date(time);
        return d;
    }
    /**
     *
     * @param seconds - No. of seconds passed 01-01-1970 00:00:00 IST
     * @return - dd-MM-yyyy
     */
    public static String secondsToDateString(int seconds){
        long time = seconds * 1000L;
        Date d = new Date(time);
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        f.setTimeZone(TimeZone.getTimeZone("IST"));
        return f.format(d);
    }

    /**
     *
     * @param seconds - No. of seconds passed 01-01-1970 00:00:00 IST
     * @return - dd-MM-yyyy HH:mm:ss
     */
    public static String secondsToDateStringFull(int seconds){
        long time = seconds * 1000L;
        Date d = new Date(time);
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        f.setTimeZone(TimeZone.getTimeZone("IST"));
        return f.format(d);
    }

    public static void waitMS(long ms){
        try{
            Thread.sleep(ms);
        }catch (Exception e){
        }
    }

    public static int getInt(byte[] buffer, int offset){
        int val = 0;
        for(int j=0;j<4;j++){
            val = (val << 8) | (0x000000ff & buffer[offset+j]);
        }
        return val;
    }
    
    public static short getShort(byte[] buffer, int offset){
        int val = 0;
        for(int j=0;j<2;j++){
            val = (val << 8) | (0x000000ff & buffer[offset+j]);
        }
        return (short) val;
    }

    public static void arrayCopy(byte[] src, int srcIndex, byte[] dest, int destIndex, int len) {
        for(int i=0;i<len;i++){
            dest[destIndex+i] = src[srcIndex+i];
        }
    }

    public static boolean arrayCompare(byte[] src, int srcIndex, byte[] dest, int destIndex, int len) {
        for(int i=0;i<len;i++){
            if(src[srcIndex+i] != dest[destIndex+i]){
                return false;
            }
        }
        return true;
    }
    /**
     * convert four byte hex int to 5 byte decimal
     * @param response
     * @param start
     */
    public static void convertHexToDecimal(byte[] response, int start) {
        int empno = 0;
        empno |= (response[start] & 0x000000FF);
        for(int i=start+1;i<start+4;i++){
                empno <<= 8;
                empno |= (response[i]&0x000000FF);
        }
        response[start+4]  = (byte) (empno%10+48);
        for(int i=start+3;i>=start;i--){
                empno /= 10;
                response[i]  = (byte) (empno%10+48);
        }
    }
    
}
