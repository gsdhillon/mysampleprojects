package SwipeCollection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Statement;
import javacodes.connection.DBConnection;

/**
 * Server1.java Created on Sep 9, 2011, 10:30:04 AM
 */
public class SwipeProcessor implements DataProcessor {

    private int port;
    private static int numSwipes = 0;
    //swipe collection labels
    private UDPServer udpServer = null;
    static String q;

    /**
     *
     * @param displayPanel
     */
    public SwipeProcessor(int port) {//String ip,
        this.port = port;


    }

    /**
     *
     */
    public void start() {
        udpServer = new UDPServer(port);
        udpServer.setDataProcessor(this);
        udpServer.start();
    }

    private synchronized void incrCounter() {
        numSwipes++;

    }

    /**
     *
     * @param data
     * @param start
     * @param len
     * @param readerIP
     */
    @Override
    public void processData(byte[] data, int start, int len, String readerIP) {
        try {
            if (len < 19 || (len - 7) % 12 != 0) {
                System.out.print("Invalid Packet Length - " + len);
                return;
            }
            byte readerID = data[start];
            short swipeNo = MU.getShort(data, start + 5);
            System.out.println("Full Packet IP=" + readerIP + " ReaderNo = " + readerID + " Len=" + (len - 7) + " Status=" + data[start + 7]);
            for (int index = start + 7; index <= len - 12; index += 12) {
                incrCounter();
                System.out.println(numSwipes + " IP=" + readerIP + " ReaderNo = " + readerID + " SwipeNo = " + swipeNo + " Data=" + MU.hexify(data, index, 12));
                swipeNo++;
                saveData(readerID, readerIP, swipeNo, data, index, 12);
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void stop() {
        if (udpServer != null) {
            try {
                udpServer.stopMe();
                udpServer.join();
            } catch (Exception e) {
            }
            udpServer = null;
        }
    }

    /**
     *
     */
    public void test() {
        try {
            byte[] data = MU.deHexify(s);
            processData(data, 0, 74, "000.000.000.000");
        } catch (Exception e) {
        }
    }
    /**
     *
     */
    private String s =
            "00006CD42FEA000000FFFF4200005E84"
            + "4F000000"//SwipeDateTime
            + "202020202020"//Padding
            + "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";//ReaderSign

    /**
     *
     * @param readerID
     * @param readerIP
     * @param swipeNo
     * @param data
     * @param index
     * @param len
     */
    private void saveData(byte readerID, String readerIP, short swipeNo, byte[] data, int index, int len) {
        //dump in a local file
        Statement stmt;
        try {
            File folder = new File("D:/SwipeDataLog/2012/09/");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folder, "120915.log");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            PrintWriter pw = new PrintWriter(bw, true);
            String line = readerID + "," + readerIP + "," + swipeNo + "," + MU.hexify(data, index, len);
            pw.println(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //save in Dtabase
        DBConnection conn = null;
        try {
            //save in row swipe tabel with processed flag F
            String accessCode = String.valueOf(data[index] & 0xff);
            String empno = getEmpno(data, index + 1);
            String readerNo = String.valueOf(readerID);
            String recordPointer = String.valueOf(swipeNo);
            System.out.println("Date time 111111" + "20" + data[index + 7] + "-" + data[index + 6] + "-" + data[index + 5] + " " + data[index + 8] + ":" + data[index + 9] + ":" + data[index + 10]);
            String dateTime = "20" + data[index + 7] + "-" + data[index + 6] + "-" + data[index + 5] + " " + data[index + 8] + ":" + data[index + 9] + ":" + data[index + 10];
            String test = readerNo + "," + recordPointer + "," + empno + "," + empno + "," + dateTime + "," + accessCode;
            conn = new DBConnection();
            conn.connect();
            //MyUtils.showMessage("Hellow world");
            stmt = conn.createStatement();
            String query =
                    "INSERT INTO swipedata("
                    + "IPAddress,"
                    + "Reader_no,"
                    + "RecordNo,"
                    + "Uid,"
                    + "EmpCode,"
                    + "ProDateTime,"
                    + "Access,"
                    + "Test,"
                    + "Processed "
                    + ") Values ( "
                    + "'" + readerIP + "', "
                    + "'" + readerNo + "', "
                    + "'" + recordPointer + "', "
                    + "'" + empno + "', "
                    + "'" + empno + "', "
                    + "'" + dateTime + "', "
                    + "'" + accessCode + "', "
                    + "'" + test + "', "
                    + "'S' "
                    + ")";
            stmt.executeUpdate(query);
//            System.out.println("dateTime : " + dateTime);
//            System.out.println("query : " + query);
            // String autoNo = null;
            stmt.close();
            conn.close();
 
            FillMusterTable FM = new FillMusterTable(conn, stmt, dateTime, empno,readerIP,accessCode);
            //further processing
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        }

    }

    /**
     *
     * @param data
     * @param offSet
     * @return
     */
    private String getEmpno(byte[] data, int offSet) {
        try {
            int val = MU.getInt(data, offSet);
            val %= (0x1000000);

            if (val < 0 || val > 999999) {
                return "0";
            } else {
                return String.valueOf(val);
            }
        } catch (Exception e) {
            return "0";
        }
    }
}
