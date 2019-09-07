package lib.reader;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.InputStream;
import java.io.OutputStream;
import lib.utils.MU;
/**
 */
public class SenergyReader implements Reader{
    private SerialPort serialPort;
    private boolean isConnected = false;
    private int speed = 9800;
    private OutputStream out;
    private InputStream in;
    private String serialPortName;
    /**
     * 
     */
    public SenergyReader(String serialPortName) {
        this.serialPortName = serialPortName;
    }

    /**
     *
     * @param portName
     * @throws Exception
     */
    public void connect() throws Exception{
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);
        if (portIdentifier.isCurrentlyOwned()) {
            throw new Exception("PortAlreadyOccupied");
        }
        //
        CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
        if (!(commPort instanceof SerialPort)) {
            throw new Exception("PortNotASerialPort");
        }
        //
        serialPort = (SerialPort) commPort;
        serialPort.setSerialPortParams(
                speed,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE
        );
        out = serialPort.getOutputStream();
        out.write(13);
        //
        try {
            Thread.sleep(80);
        } catch (Exception ex1) {
        }
        in = serialPort.getInputStream();
        //clear input buffer buffer
        if (in.available() > 0) {
            byte byteReceived[] = new byte[in.available()];
            in.read(byteReceived);
            System.out.println("Reader response = "+new String(byteReceived));
        }
        //
        isConnected = true;
    }
    /**
     * 
     */
    public void close() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if(serialPort != null){
                serialPort.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * call this method only once for a new reader
     * and then pass keyNum for read/write blocks accordingly
     * only derived key will be loaded dynamically
     * use keyNum 13 for that
     * @throws Exception
     */
    public void loadDerivedKey(String derivedKey) throws Exception{
        loadKeySet(derivedKey, Reader.derivedKeyNo);
    }
    /**
     *
     * @param keySet
     * @throws Exception
     */
    private void loadKeySet(String keySet, int keyNo) throws Exception{
        if (!isConnected || out == null) {
            throw new Exception("ReaderNotConnected.");
        }
        //
        if(keyNo < 0 || keyNo > 15){
            throw new Exception("Invalid KeyNum - "+keyNo);
        }
        String keyNum = Integer.toString(keyNo);
        if(keyNo < 10){
            keyNum = "0"+keyNum;
        }
        if (keySet.length() != 12) {
            throw new Exception("Wrong KeySetLength "+keySet.length()+" LoadKeySet");
        }
        //
        out.write(("LO0" + keyNum).getBytes());
        try {
            Thread.sleep(80);
        } catch (Exception ex1) {
        }
        out.write(keySet.getBytes());
        try {
            Thread.sleep(80);
        } catch (Exception ex1) {
        }
        if (in.available() <= 0) {
            throw new Exception("LoadKeySet Failed in=0");
        }
        byte[] b = new byte[in.available()];
        in.read(b);
        if (b[0] != 48) {
            throw new Exception("LoadKeySet b[0]="+b[0]);
        }
    }
    /**
     * Now it will return 8 bytes UID
     * @param keyNum - Pass madKeyNum for personalized cards and ffKey for blank cards
     * @return
     * @throws Exception
     */
    @Override
    public String readUID(int keyNo) throws Exception{
        if (!isConnected || out == null) {
            throw new Exception("ReaderNotConnected.");
        }
        //
        if(keyNo < 0 || keyNo > 15){
            throw new Exception("Invalid KeyNum - "+keyNo);
        }
        String keyNum = Integer.toString(keyNo);
        if(keyNo < 10){
            keyNum = "0"+keyNum;
        }
        //Read block no. 001 i.e. Second block of sector 0 (MAD1)
        out.write(("RE0"+keyNum+"001").getBytes());
        try {
            Thread.sleep(80);
        } catch (Exception ex) {
        }
        if (in.available() <=  0) {
            throw new Exception("ReadUIDFailed 001 keyNum="+keyNum);
        }
        byte[] b = new byte[in.available()];
        in.read(b);

        if (b[0] != 48) {
            throw new Exception("ReadUIDFailed 001 b[0]="+b[0]+" keyNum="+keyNum);
        }
        byte[] byteUID = new byte[4];
        System.arraycopy(b, 2, byteUID, 0, 4);
        return MU.hexify(byteUID);
    }
    /**
     *
     * @param block
     * @return
     * @throws Exception
     */
    @Override
    public byte[] readBlock(int blockNo, int keyNo) throws Exception{
        if (!isConnected || out == null) {
            throw new Exception("ReaderNotConnected.");
        }
        //
        if(keyNo < 0 || keyNo > 15){
            throw new Exception("Invalid KeyNum - "+keyNo);
        }
        String keyNum = Integer.toString(keyNo);
        if(keyNo < 10){
            keyNum = "0"+keyNum;
        }
        //
        if(blockNo < 0 || blockNo > 159){
            throw new Exception("Invalid blockNo - "+blockNo);
        }
        String block = Integer.toString(blockNo);
        if(blockNo < 10){
            block = "00"+block;
        }else if(blockNo < 100){
            block = "0"+block;
        }
        //
        
        out.write(("RE0" + keyNum + block).getBytes());
        try {
            Thread.sleep(80);
        } catch (Exception ex) {
        }
        if (in.available() <= 0) {
            throw new Exception("ReadCardFailed");
        }
        byte[] b = new byte[in.available()];
        in.read(b);
        if (b[0] != 48) {
            throw new Exception("ReadCardFailed");
        }
        byte[] byteData = new byte[16];
        System.arraycopy(b, 7, byteData, 0, 16);
        return byteData;
    }

    /**
     *
     * @param block
     * @param data
     * @throws Exception
     *
    public void writeBlock(String block, String data) throws Exception{
        writeBlock(block, data, defaultKeyNum);
    }*/
    /**
     *
     * @param block
     * @param data
     */
    @Override
    public void writeBlock(int blockNo, byte[] dataBytes, int off, int keyNo) throws Exception{
        if (!isConnected || out == null) {
            throw new Exception("ReaderNotConnected.");
        }
        if(keyNo < 0 || keyNo > 15){
            throw new Exception("Invalid KeyNum - "+keyNo);
        }
        String keyNum = Integer.toString(keyNo);
        if(keyNo < 10){
            keyNum = "0"+keyNum;
        }
        //
        if(blockNo < 0 || blockNo > 159){
            throw new Exception("Invalid blockNo - "+blockNo);
        }
        String block = Integer.toString(blockNo);
        if(blockNo < 10){
            block = "00"+block;
        }else if(blockNo < 100){
            block = "0"+block;
        }
        //
        if (dataBytes == null || dataBytes.length < (off+16)) {
            throw new Exception("WriteFailed - dataBytes < 16");
        }
        out.write(("WR0" + keyNum + block).getBytes());
        try {
            Thread.sleep(80);
        } catch (Exception ex1) {
        }
        out.write(dataBytes, off, 16);
        try {
            Thread.sleep(80);
        } catch (Exception ex1) {
        }
        if (in.available() <= 0) {
            throw new Exception("WriteBlockFailed "+block+" keyNum="+keyNum);
        }

        byte[] b = new byte[in.available()];
        in.read(b);
        if (b[0] != 48) {
            throw new Exception("WriteBlockFailed  "+block+" b[0]="+b[0]+" keyNum="+keyNum);
        }
    }
     /**
     * call this method only once for a new reader
     * and then pass keyNum for read/write blocks accordingly
     * only derived key will be loaded dynamically
     * use keyNum 13 for that purpose
     * @throws Exception
     */
    public void loadAllKeySet() throws Exception{
        loadKeySet(ffKey.substring(0, 12), Reader.ffKeyNo);
        loadKeySet(madKey.substring(0, 12), Reader.madKeyNo);
        loadKeySet(empKey.substring(0, 12), Reader.empKeyNo);
        loadKeySet(visitorKey.substring(0, 12), Reader.visitorKeyNo);
        // 13 will be used for DerivedKey
        System.out.println("********** KeySets Loaded in the Reader ********");
    }
 //
    public static final String accessConditions =       "FF078069";
    public static final String ffKey =                  "FFFFFFFFFFFFFF078069FFFFFFFFFFFF";
    public static final String madKey =                 "FFFFFFFFFFFFFF078069FFFFFFFFFFFF";
    public static final String empKey =                 "FFFFFFFFFFFFFF078069FFFFFFFFFFFF";
    public static final String visitorKey =             "FFFFFFFFFFFFFF078069FFFFFFFFFFFF";

}