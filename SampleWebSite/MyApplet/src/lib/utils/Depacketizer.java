package lib.utils;
/**
 */
public class Depacketizer{
    private byte[] packet;
    private int size;
    private int index;
    private int currSize = 256;
    private byte[] temp = new byte[currSize];
    private final String HEAD = "APP_PACKET";
    private final String TAIL = "END_APP_PACKET";
    /**
     */
    public Depacketizer(String packet) throws Exception{
        if(!packet.startsWith(HEAD)){
            throw new Exception("Invalid_APP_PACKET");
        }
        this.packet = packet.getBytes();
        index = HEAD.length()+1;
        size = this.packet.length;
    }
    /**
     * return next token as string
     */
    public String getString() throws Exception{
        int i = 0;
        if(index >= size){
            throw new Exception("APP_PACKET_Underflow");
        }
        while(index < size && packet[index] != ':'){
            byte b1 = packet[index++];
            if(b1 == ';'){
                byte b2 = packet[index++];
                if(b2 == b1){
                   temp[i++] = b1;
                   if(i==currSize) checkSize();
                }else if(b2 == 'n'){
                    temp[i++] = '\n';
                    if(i==currSize) checkSize();
                }else if (b2 == ':'){
                    temp[i++] = ':';
                    if(i==currSize) checkSize();
                }else{
                    throw new Exception("Invalid_APP_PACKET_Char");
                }
            }else{
                temp[i++] = b1;
                if(i==currSize) checkSize();
            }
        }
        index++;
        if(i==0){
            return "";
        }else{
            String  tempString = new String(temp,0,i);
            return tempString;
        }
    }
    /**
     * return next token as int
     */
    public int getInt() throws Exception{
        return Integer.parseInt(getString());
    }
    /**
     * return next token as float
     */
    public float getFloat() throws Exception{
        return Float.parseFloat(getString());
    }
    /**
     * 
     * @return ''
     */
    public boolean isEmpty() throws Exception{
        if(index+1+TAIL.length()>=size){
            return true;
        }else{
            return false;
        }
    }
    /**
     */
    private void checkSize() {
        currSize += currSize;
        byte[] newArray = new byte[currSize];
        System.arraycopy(temp, 0, newArray, 0, temp.length);
        temp = newArray;
    }
}