package SwipeCollection;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;
import javacodes.connection.AppContext;

public class UDPServer extends Thread {
    //private String ip;

    private DataProcessor dataProcessor = null;
    private int port;
    private DatagramSocket serverSocket;
    private boolean started = false;

    /**
     *
     * @param displayPanel
     */
    public UDPServer(int port) {//String ip,
        //this.ip = ip;
        this.port = port;
    }

    /**
     *
     * @param dataProcessor
     */
    public void setDataProcessor(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    /**
     *
     */
    public void stopMe() {
        started = false;
        serverSocket.close();
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            //port may be occupied or network not initialized
            for (int i = 0; i < 100; i++) {
                try {
                    serverSocket = new DatagramSocket(port);
                    System.out.println("ServerSocket set");
                    started = true;
                    break;
                } catch (Exception e) {
                    System.out.println("Trial " + i + " failed - " + e.getMessage());
                    MU.waitMS(1000);
                }
            }
            if (started != true) {
                throw new Exception("Could not create ServerSocket on port = " + port);
            }
            //serverSocket = new DatagramSocket(port, InetAddress.getByName(ip));
            while (started) {
                try {
                    AppContext.swipeCollectorStat = "Started on port=" + port + " at:" + new Date();
                    byte[] receiveData = new byte[256];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    new SwipeCollectionThread(receivePacket).start();
                } catch (Exception e) {
                    System.out.println("Exception in UDPTread- " + e.getMessage());
                }
            }//while loop
        } catch (Exception ex) {
            ex.printStackTrace();
            AppContext.swipeCollectorStat = "Could not start - " + ex.getMessage();
        }
    }

    /**
     *
     */
    private class SwipeCollectionThread extends Thread {

        private DatagramPacket receivePacket = null;

        public SwipeCollectionThread(DatagramPacket receivePacket) {
            this.receivePacket = receivePacket;
        }

        @Override
        public void run() {
            try {
                // handle received packet                            
                DatagramPacket replyPacket = handlePacket(receivePacket);

                if (replyPacket != null) {
                    DatagramSocket replySocket = new DatagramSocket();
                    replySocket.send(replyPacket);
                }

                //ReaderCheckIn(receivePacket.getData());
            } catch (Exception e) {
                System.out.println("Exception in SwipeCollectionThread - " + e.getMessage());
            }

        }
    }

    /**
     * TODO MANOJ - please see this method. This method is used for receiving
     * data of both Server1 and Server2. It calculates CRC and compare with CRC
     * received in last 2 bytes. If CRC is matched it returns a 8 byte reply
     * packet. Other wise it return NULL. Data len for Server1 must be 74 and
     * data len for Server2 must be 112. Header and footer should be same for
     * both.
     *
     * @param receivePacket
     * @return
     */
    private DatagramPacket handlePacket(final DatagramPacket receivePacket) {
        try {
            //
            InetAddress replyAddress = receivePacket.getAddress();
            int replyPort = receivePacket.getPort();
            String ipaddress = replyAddress.getHostAddress().toString();

            byte[] receiveBuffer = receivePacket.getData();
            int headerLen = 0;//MANOJ - check header len. change if needed
            int crcLen = 2;//CRC
            int packetLen = receivePacket.getLength();
            int dataLen = packetLen - headerLen - crcLen;
            int packetOffset = receivePacket.getOffset();
            //copy last two bytes CRC received in a seperate array
            byte[] crcReceived = new byte[2];
            System.arraycopy(receiveBuffer, packetOffset + packetLen - 2, crcReceived, 0, 2);
            //calculate CRC of received packet (including header) in another array
            byte[] crcCalculated = getCRC(receiveBuffer, packetOffset, packetLen - 2);
            if (Arrays.equals(crcReceived, crcCalculated)) {
                if (dataProcessor != null) {
                    //call processData method of Server1/Server2
                    dataProcessor.processData(receiveBuffer, packetOffset + headerLen, dataLen, ipaddress);
                } else {
                    System.out.println("data Processor was null");
                    //log about data processor
                }
            } else {
                System.out.println("CRC not Macthed");
            }
            if (Arrays.equals(crcReceived, crcCalculated)) {
                //send reply packet
                //MANOJ - check reply data. change if needed
                byte[] replyData = new byte[8];
                replyData[0] = receiveBuffer[0];
                replyData[1] = receiveBuffer[1];
                replyData[2] = 0;
                replyData[3] = 0;
                replyData[4] = 0;
                replyData[5] = 0;
                crcCalculated = getCRC(replyData, 0, 6);
                replyData[6] = crcCalculated[0];
                replyData[7] = crcCalculated[1];
//                DatagramPacket replyPacket = new DatagramPacket(replyData, replyData.length, replyAddress, this.port);
                DatagramPacket replyPacket = new DatagramPacket(replyData, replyData.length, replyAddress, replyPort);
                return replyPacket;
            } else {
                System.out.println("CRC not matched");
                return null;
                //MANOJ - on CRC not matching I'm not sending reply packet
                //you can make the reply packet as above if needed
                //send -ve ack
            }
        } catch (Exception e) {
            System.out.println("Exception " + e);
            //  MyData.printException("PanelPC UDPServerPanelPC process", e);
            //send -ve ack
            return null;
        }
    }

    /*
     * private void ReaderCheckIn(byte[] data) { int ReaderNo = data[0]; try {
     * DBConnection conn = new DBConnection(); conn.connect(); Statement stmt =
     * conn.createStatement(); String query = "INSERT INTO
     * readercheckin(ReaderNo)Values('" + ReaderNo + "')"; int executeUpdate =
     * stmt.executeUpdate(query); } catch (Exception ex) {
     * System.out.println("ReaderCheckIn Failed"); } }
     */
    /**
     *
     * @param sendBuffer
     * @param offset
     * @param dataLen
     * @return
     */
    public static byte[] getCRC(byte[] sendBuffer, int offset, int dataLen) {
        int index;
        char crcHigher = 0xFF;
        char crcLower = 0xFF;
        for (int i = 0; i < dataLen; i++) {
            index = (0x00ff) & (crcHigher ^ sendBuffer[offset + i]);
            crcHigher = (char) (crcLower ^ auchCRCHigher[index]);
            crcLower = (char) (auchCRCLower[index]);
        }
        byte[] crc = new byte[2];
        crc[0] = (byte) (crcHigher);
        crc[1] = (byte) (crcLower);
        return crc;
    }
    private static final char[] auchCRCHigher = {
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00,
        0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1,
        0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81,
        0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
        0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01,
        0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80,
        0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
        0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00,
        0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
        0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
        0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01,
        0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
        0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81,
        0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
        0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01,
        0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1,
        0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
        0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40
    };
    private static final char[] auchCRCLower = {
        0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06, 0x07, 0xC7, 0x05,
        0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD, 0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA,
        0xCB, 0x0B, 0xC9, 0x09, 0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA,
        0x1A, 0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14, 0xD4, 0xD5, 0x15,
        0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3, 0x11, 0xD1, 0xD0, 0x10, 0xF0,
        0x30, 0x31, 0xF1, 0x33, 0xF3, 0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35,
        0x34, 0xF4, 0x3C, 0xF4, 0xFD, 0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A, 0x3B,
        0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9, 0x29, 0xEB, 0x2B, 0x2A, 0xEA,
        0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED, 0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27,
        0xE7, 0xE6, 0x26, 0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60,
        0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67, 0xA5, 0x65, 0x64,
        0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F, 0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB,
        0x69, 0xA9, 0xA8, 0x68, 0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE,
        0x7E, 0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5, 0x77, 0xB7,
        0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71, 0x70, 0xB0, 0x50, 0x90, 0x91,
        0x51, 0x93, 0x53, 0x52, 0x92, 0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54,
        0x9C, 0x5C, 0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B, 0x99,
        0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89, 0x4B, 0x8B, 0x8A, 0x4A, 0x4E, 0x8E,
        0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C, 0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46,
        0x86, 0x82, 0x42, 0x43, 0x83, 0x41, 0x81, 0x80, 0x40
    };
}