package lib.reader;

/**
 */
public interface Reader {
    public final int ffKeyNo = 0;
    public final int madKeyNo = 1;
    public final int empKeyNo = 2;
    public final int visitorKeyNo = 3;
    //use derived keyNo for verifying a card
    public final int derivedKeyNo = 13;
    /**
     * UID in HEX
     * @param keyNo
     * @return
     * @throws Exception
     */
    public String readUID(int keyNo) throws Exception;
    /**
     *
     * @param block
     * @param keyNo
     * @return
     * @throws Exception
     */
    public byte[] readBlock(int block, int keyNo) throws Exception;
    /**
     * 
     * @param block
     * @param dataBytes
     * @param off
     * @param keyNo
     * @throws Exception
     */
    public void writeBlock(int block, byte[] dataBytes, int off, int keyNo) throws Exception;
}
