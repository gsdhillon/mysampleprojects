package pkilib;

/**
 * MyBuffer.java
 * Created on Mar 14, 2009 at 3:08:45 PM
 * @author Gurmeet Singh
 */
public class MyBuffer {
    public byte[] buff;
    public int off = 0;
    public int len = 0;
    /**
     * 
     */
    public MyBuffer(int size){
        buff = new byte[size];
    }
    /**
     * 
     * @return byte[]
     */
    public byte[] getCopy(){
        byte[] b = new byte[len];
        System.arraycopy(buff, off, b, 0, len);
        return b;
    }
    /**
     * 
     */
    public void append(byte[] data){
        System.arraycopy(buff, off, data, 0, data.length);
        System.out.println(data.length+" ");
        len += data.length;
    }
}
