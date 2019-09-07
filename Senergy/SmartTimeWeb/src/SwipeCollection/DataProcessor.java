package SwipeCollection;


public interface DataProcessor {
    /**
     * implement this method for server1 and server2 accordingly
     * for Server1 dataLen should be 74
     * for Server2 dataLen should be 112
     * @param buffer
     * @param off
     * @param len
     */
    public void processData(byte[] buffer, int off, int len,String ipaddress);
}
