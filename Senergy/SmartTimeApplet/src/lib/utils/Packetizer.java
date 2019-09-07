package lib.utils;

/**
 */
public class Packetizer {

    private int currLen = 128 * 1024;
    private int target = 4 * 1024 * 1024;
    private int occupied = 0;
    /**
     * string buffer*
     */
    private StringBuffer data = new StringBuffer(currLen);
    public int count = 0;
    private boolean counterSet = false;
    private final String HEAD = "APP_PACKET";
    private final String TAIL = "END_APP_PACKET";

    /**
     */
    public Packetizer() {
    }

    /**
     * ';' character is special character ;; means ; ;: means : ;n means new
     * line character (\n) if string is null 0 length string will be added
     */
    public void addString(String string) throws Exception {
        if (string == null) {
            if (occupied + 1 > currLen) {
                increaseCapacity();
            }
            data.append(":");
            occupied += 1;
            return;
        }
        string = string.replaceAll(";", ";;").replaceAll("\n", ";n").replaceAll(":", ";:");
        while (occupied + (string.length() + 1) > currLen) {
            increaseCapacity();
        }
        data.append(string).append(":");
        occupied += string.length() + 1;
    }

    /**
     *
     * @param val
     * @throws java.lang.Exception
     */
    public void addInt(int val) throws Exception {
        String s = Integer.toString(val);
        addString(s);
    }

//    public void addIntArr(int val[]) throws Exception {
//        for (int i : val) {
//            String s = Integer.toString(i);
//            addString(s);
//        }
//    }
    /**
     *
     * @param val
     * @throws java.lang.Exception
     */
    public void addBool(boolean bool) throws Exception {
        String s;
        if (bool) {
            s = Integer.toString(1);
        } else {
            s = Integer.toString(0);
        }
        addString(s);
    }

    /**
     *
     * @param val
     * @throws java.lang.Exception
     */
    public void addFloat(float val) throws Exception {
        String s = Float.toString(val);
        addString(s);
    }

    /**
     *
     */
    public void setCounter() {
        counterSet = true;
    }

    /**
     *
     */
    public void incrCounter() {
        count++;
    }

    /**
     */
    public String getPacket() {
        String packet;
        if (!counterSet) {
            packet = HEAD + ":"
                    + data
                    + TAIL;
        } else {
            packet = HEAD + ":"
                    + count + ":"
                    + data
                    + TAIL;
        }
        //System.out.println("Packet1 len - "+packet.length()/100+"k");  
        return packet;
    }

    /**
     *
     */
    private void increaseCapacity() throws Exception {
        currLen += currLen;
        if (currLen > target) {
            throw new Exception("PacketizerBuffFullException");
        }
        StringBuffer data1 = new StringBuffer(currLen);
        data1.append(data);
        data = data1;
        //System.out.println("Capacity - "+currLen);
    }
}
