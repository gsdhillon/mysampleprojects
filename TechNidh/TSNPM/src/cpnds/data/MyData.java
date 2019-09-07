package cpnds.data;
import cpnds.MyGraphs.DataPoint;
import cpnds.CommPort.MyCommPort;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import cpnds.gui.MainFrame;
import gui.MyButton;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.TimeZone;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

/**
 * @type     : Java Class
 * @name     : MyData
 * @file     : MyData.java
 * @created  : Feb 03, 2011 1:08:07 PM
 * @version  : 1.2
 */
public class MyData{
    public static final boolean debug = false;
    public static final String applicationName = "TSNPM";
    public static MyCommPort myCommPort = null;
    public static boolean portOpened = false;
    public static String appHome;
    public static MainFrame mainFrame = null;
    public static int width = 1024;
    public static int height = 738;

    /**************** DATA **************/
    //all params in settings.txt
    public static String userID = "";
    public static String password = "";
    public static String userName = "";
    public static String comPort = "";
    public static String connTimeOut = "";
    public static String dataPath = "";
    public static String generateDummyData = "FALSE";
    public static String showPrintableGraphs = "FALSE";
    //other data
    public static String logFolder = "LogReports";
    public static String settingsFile = "settings.txt";
    public static String systemDate = "";
    public static String loginTime = "";
    public static String experimentDataFolder;
    public static int uid=1;//unique int val for each experiment
    public static final int CHUNCK_SIZE = 512*1024;
    public static final int MAX_FLOAT_NUMS = 4*1024*1024;
    public static final int DATA_BUFFER_SIZE = 4*MAX_FLOAT_NUMS+CHUNCK_SIZE;
    public static byte[] dataBuffer = new byte[DATA_BUFFER_SIZE];
    /*************** END OF DATA *********/
    /**
     *
     */
    public static void initialize(MainFrame mainFrame){
        MyData.mainFrame = mainFrame;
        //set application home path
        try{
            String s = MyData.class.getResource("MyData.class").getPath();
            s = s.replaceAll("%20", " ");
            //System.out.println(s);
            if(s.indexOf(applicationName+".jar")>0){
                s = s.substring(0, s.lastIndexOf(applicationName));
            }
            appHome = s.substring(s.indexOf('/')+1, s.lastIndexOf(applicationName)+applicationName.length());
            appHome = appHome.replaceAll("/", "\\\\");
            System.out.println(appHome);
        }catch(Exception e){
            LogReports.logError(e);
        }

        //read userID and Password
        readSettingsFile();

        //setr date time
        try{
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            systemDate = dateFormat.format(new Date());
            loginTime = LogReports.currentTimeString();
        }catch(Exception e){
            LogReports.logError(e);
        }

        //create folders if do not exist
        try{
            //create folders
            createDirectory(appHome+"/"+logFolder);
            MyButton.iconPath = appHome+"/Icons";

        }catch(Exception e){
            LogReports.logError(e);
        }

        //set screen resolution
        try{
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            MyData.width = screenSize.width;//1024;
            MyData.height = screenSize.height-30;//738;
        }catch(Exception e){
            LogReports.logError(e);
        }

        //initialize comm port
        try{
            myCommPort = new MyCommPort();
            MyCommPort.TIME_OUT = Integer.parseInt(connTimeOut);
            GENERATE_DUMMY_DATA = generateDummyData.endsWith("TRUE");
        }catch(Exception e){
            LogReports.logError(e);
        }
    }
    /**
     * 
     * @throws Exception
     */
    public static void readSettingsFile(){
        try{
            userID = readParameter("USER_ID");
            password = readParameter("PASSWORD");
            userName = readParameter("USER_NAME");
            comPort = readParameter("COM_PORT");
            connTimeOut = readParameter("CONNECTION_TIME_OUT");
            dataPath = readParameter("DATA_PATH");
            showPrintableGraphs = readParameter("SHOW_PRINTABLE_GRAPHS");
            generateDummyData = readParameter("GENERATE_DUMMY_DATA");
        }catch(Exception e){
            LogReports.logError(e);
        }
    }

    /**
     *
     * @throws Exception
     */
    public static void writeSettingsFile(){
        PrintWriter pw = null;
        try{
            File f = new File(appHome+"/"+settingsFile);
            pw = new PrintWriter(f);
            pw.println("USER_ID="+userID);
            pw.println("PASSWORD="+password);
            pw.println("USER_NAME="+userName);
            pw.println("COM_PORT="+comPort);
            pw.println("CONNECTION_TIME_OUT="+connTimeOut);
            pw.println("DATA_PATH="+dataPath);
            pw.println("SHOW_PRINTABLE_GRAPHS="+showPrintableGraphs);
            pw.println("GENERATE_DUMMY_DATA="+generateDummyData);
        }catch(Exception e){
            LogReports.logError(e);
        }finally{
            if(pw!=null){
                pw.close();
            }
        }
    }

    /**
     * read the value of specified parameter from configuration file
     */
    private static String readParameter(String name) throws Exception{
        String record = null;
        String result = null;
        BufferedReader br = null;
        try {
            File f = new File(appHome+"/"+settingsFile);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            record = br.readLine();
            while(record != null){
                record = record.trim();
                if(record.startsWith(name)){
                    result = record.substring((record.indexOf('=')+1)).trim();
                    br.close();
                    //System.out.println(name+"="+result);
                    return result;
                }
                record = br.readLine();
            }
            throw new Exception("PARAMETER "+name+" NOT FOUND");
        }finally{
            if(br != null) br.close();
        }
    }
    /**
     * 
     * @param f
     * @return
     */
    public static String getFileInfo(File f){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("IST"));
        String dateTime = df.format(new Date(f.lastModified()));
        return f.getName().substring(4)+" on "+dateTime;
    }


    /**
     *
     * @return
     */
    public static void openPort(String portName){
        //initialize comm port
        comPort = portName;
        myCommPort.open();
    }

    /**
     * 
     */
    public static void openDefaultPort(){
        //initialize comm port
        //try{Thread.sleep(2000);}catch(Exception e){}
        myCommPort.open();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public static void createExperimentFolder() throws Exception{
        //create date
        DateFormat format = new SimpleDateFormat("yy-MM-dd");
        String dateYYMMDD = "DATE " + format.format(new Date());
        String dateDir = MyData.dataPath+"/"+dateYYMMDD;
        MyData.createDirectory(dateDir);
        //create time
        format = new SimpleDateFormat("HH-mm-ss");
        String timeHHMMSS = "TIME " + format.format(new Date());
        String timeDir = dateDir + "/"+timeHHMMSS;
        MyData.createDirectory(timeDir);
        experimentDataFolder = timeDir;
        readUID();
        uid++;
        writeUID();
        writeUIDDirs();
    }
    /**
     *
     */
    private static void readUID(){
        try{
            File uidFile = new File(dataPath+"/uid");
            FileInputStream fis = new FileInputStream(uidFile);
            byte[] buff = new byte[4];
            if(fis.read(buff)<4){
                fis.close();
                uid = 0;
                return;
            }
            fis.close();
            uid = ((buff[3] & 0x00FF) << 24) |
                          ((buff[2] & 0x00FF) << 16) |
                          ((buff[1] & 0x00FF) << 8) |
                           (buff[0] & 0x00FF);
        }catch(Exception e){
            uid = 0;
        }
    }
    /**
     *
     */
    private static void writeUID() throws Exception{
        try{
            File uidFile = new File(dataPath+"/uid");
            FileOutputStream fos = new FileOutputStream(uidFile);
            int numInt = uid;
            byte[] buffer = new byte[4];
            buffer[0] = (byte)(numInt & 0x00FF);
            numInt = numInt >> 8;
            buffer[1] = (byte)(numInt & 0x00FF);
            numInt = numInt >> 8;
            buffer[2] = (byte)(numInt & 0x00FF);
            numInt = numInt >> 8;
            buffer[3] = (byte)(numInt & 0x00FF);
            fos.write(buffer);
            fos.close();
        }catch(Exception e){
            throw new Exception("Could not update UID file");
        }
    }

    /**
     *
     */
    private static void writeUIDDirs() throws Exception{
        try{
            File f = new File(experimentDataFolder);
            String dateDirName = f.getParentFile().getName();
            String timeDirName = f.getName();
            //open list file
            File uidDirFile = new File(dataPath+"/uid_dirs_list");
            PrintWriter pw = new PrintWriter(new FileWriter(uidDirFile,true));
            //write UID
            int numInt = uid;
            byte[] buffer = new byte[4];
            buffer[0] = (byte)(numInt & 0x00FF);
            numInt = numInt >> 8;
            buffer[1] = (byte)(numInt & 0x00FF);
            numInt = numInt >> 8;
            buffer[2] = (byte)(numInt & 0x00FF);
            numInt = numInt >> 8;
            buffer[3] = (byte)(numInt & 0x00FF);
            pw.println(new String(buffer,"UTF-8")+"|"+dateDirName+"|"+timeDirName);
            pw.close();
        }catch(Exception e){
            throw new Exception("Could not update UID_DIR_LIST file");
        }
    }

    /**
     * Create Operation Folder under experiment directory
     * @return
     * @throws Exception
     */
    public static String createOperationFolder(String dirName) throws Exception{
        String recentDir = experimentDataFolder+"/"+dirName;
        MyData.createDirectory(recentDir);
        return recentDir;
    }

    /**
     *
     * @param folderPath
     */
    public static void createDirectory(String folderPath) throws Exception{
        File directory = new File(folderPath);
        if(!directory.exists()){
            if(!directory.mkdir()){
                throw new Exception("Could not create data folder\n "
                                      +directory.getAbsolutePath());
            }
        }
    }
    /**
     *
     * @return
     */
    public static File chooseFile(File currentFile) throws Exception{
        if(currentFile == null){
            currentFile = new File(MyData.dataPath);
        }else{
            currentFile = currentFile.getParentFile();
        }
        JFileChooser jfc = new JFileChooser(currentFile);
        jfc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.isDirectory() || f.getName().startsWith("RAW_")){
                    return true;
                }else{
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return "CPNDS Data File";
            }
        });
        int result = jfc.showOpenDialog(MyData.mainFrame);
        if(result == JFileChooser.APPROVE_OPTION){
            return jfc.getSelectedFile();
        }else{
            return null;
        }
    }
    /**
     * 
     * @param ms
     */
    public static void waitForTime(long ms){
        try{
            Thread.sleep(ms);
        }catch(Exception e){
            
        }
    }
    /**
     * converts float to byte array and
     * put the bytes in buffer starting from off
     * least significant byte is placed first (at off+0)
     * @param numFloat
     * @return
     */
    public static void putFloatBytesToBuffer(float numFloat, byte[] buffer, int off) throws Exception{
        if(buffer.length<off+4){
            throw new Exception("OverFlow");
        }
        int intBits = Float.floatToIntBits(numFloat);
        buffer[off+0] = (byte)(intBits & 0x00FF);
        intBits = intBits >> 8;
        buffer[off+1] = (byte)(intBits & 0x00FF);
        intBits = intBits >> 8;
        buffer[off+2] = (byte)(intBits & 0x00FF);
        intBits = intBits >> 8;
        buffer[off+3] = (byte)(intBits & 0x00FF);
    }

    /**
     * converts integer to byte array and
     * put the bytes in buffer starting from off
     * least significant byte is placed first (at off+0)
     * @param numInt
     * @return
     */
    public static void putIntBytesToBuffer(int numInt, byte[] buffer, int off) throws Exception{
        if(buffer.length<off+4){
            throw new Exception("OverFlow");
        }
        buffer[off+0] = (byte)(numInt & 0x00FF);
        numInt = numInt >> 8;
        buffer[off+1] = (byte)(numInt & 0x00FF);
        numInt = numInt >> 8;
        buffer[off+2] = (byte)(numInt & 0x00FF);
        numInt = numInt >> 8;
        buffer[off+3] = (byte)(numInt & 0x00FF);
    }

    /**
     * make integer from 4 bytes of buffer starting from off
     * least significant byte comes first (off+0)
     * byte & 0x00FF makes byte to integer (treating byte as unsigned byte)
     * @param buffer
     * @param offSet
     * @return
     */
    public static int getInt(byte[] buffer, int offSet) throws Exception{
        if(buffer == null || buffer.length < (offSet+4)){
            throw new Exception("BufferLenWrong");
        }
        int number =     ((buffer[offSet+3] & 0x00FF) << 24) |
                          ((buffer[offSet+2] & 0x00FF) << 16) |
                          ((buffer[offSet+1] & 0x00FF) << 8) |
                           (buffer[offSet+0] & 0x00FF);
         return number;
    }

     /**
     * make float from 4 bytes of buffer starting from off
     * least significant byte comes first (off+0)
     * byte & 0x00FF makes byte to integer (treating byte as unsigned byte)
     * @param buffer
     * @param offSet
     * @return
     */
    public static float getFloat(byte[] buffer, int offSet) throws Exception{
        if(buffer == null || buffer.length < (offSet+4)){
            throw new Exception("BufferLenWrong");
        }
        int number =  ((buffer[offSet+3] & 0x00FF) << 24) |
                       ((buffer[offSet+2] & 0x00FF) << 16) |
                        ((buffer[offSet+1] & 0x00FF) << 8) |
                         (buffer[offSet+0] & 0x00FF);
        return Float.intBitsToFloat(number);
    }

    /**
     * expected frame of length 18 bytes
     * 0,1 and 16,17 should be O,K and O,K
     * 14,15 two bytes (16 bit) are status bits for each of the data byte
     * from 2 to 13 (12 bytes data).
     * Bit at sno th position from left is 1 means sno th data byte (out of 12)
     * is either 0x0D OR 0x0A.
     * data byte 0x0D will be replaced with 0x47
     * data byte 0x0A will be replaced with 0x46
     * @param frame
     * @throws Exception
     */
    public static void setStatusBytes(byte[] frame) throws Exception{
        //DO NOTHING
        return;
        /**
         * code is OK but now there is no need for it.
         * problem of readLine has been solved now - 27-05-2011
         * BUT this method will be called before sending every packet to BB
         * we can use this method to put check sum or some extra parameters
         * in future. Nidhi and Mohit agreed.

        if(frame == null || frame.length != 18){
            throw new Exception("FrameLenNot18");
        }
        if(frame[0] != 0x4F || frame[1] != 0x4B){
            throw new Exception("FrameStartNotOK");
        }
        if(frame[16] != 0x4F || frame[17] != 0x4B){
            throw new Exception("FrameEndNotOK");
        }
        int status = 0x00000000;
        int ithBitOneFromLeft;
        for(int sno=0;sno<12;sno++){
            //for byte no. sno (0-11)
            ithBitOneFromLeft = 0x80000000 >>> sno;//unsigned right shift (0 fill)
            if(frame[sno+2] == 0x0A){
                status |= ithBitOneFromLeft;
                frame[sno+2] = 0x46;
                //System.out.println(sno+ " " + status + " " + ithBitOneFromLeft);
            }else if(frame[sno+2] == 0x0D){
                status |= ithBitOneFromLeft;
                frame[sno+2] = 0x47;
                //System.out.println(sno+ " " + status + " " + ithBitOneFromLeft);
            }
        }
        //extract two bytes from left and put in the frame
        frame[14] = (byte) ( (status >>> 24) & 0x00FF);
        frame[15] = (byte) ( (status >>> 16) & 0x00FF);*/
    }

    /**
     *
     * @param file
     * @param buffer
     * @param off
     * @param len
     */
    public static void writeParameterFile(File file, byte[] buffer,
                                    int off, int len) throws Exception{
        //write data to the file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(buffer, off, len);
        fos.close();
    }

    /**
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static byte[] readParameterFile(File f) throws Exception{
        FileInputStream fis = new FileInputStream(f);
        int len = fis.available();
        byte[] buffer = new byte[len];
        fis.read(buffer);
        fis.close();
        return buffer;
    }
  
    /**
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static DataPoint[] readDataPoints(File f) throws Exception{
        FileInputStream fis = new FileInputStream(f);
        int bytesRead = 0;
        int chunckSize = MyData.CHUNCK_SIZE;
        int dataLen;
        int off = 0;
        while((bytesRead = fis.read(dataBuffer, off, chunckSize))>0){
            off += bytesRead;
        }
        dataLen = off;
        fis.close();
        //check len of data should be multiple of 4
        if(dataLen%4 != 0){
            throw new Exception("LenofDataNot 4X = " + dataLen);
        }
        int numFloats = dataLen/4;
        if(numFloats>MAX_FLOAT_NUMS){
            throw new Exception("DATA_FILE_SIZE > 4MB");
        }
        DataPoint[] points = new DataPoint[numFloats];
        //make float array

        for(int i=0;i<numFloats;i++){
            int offSet = 4*i;//OK+4byteInt
            int intBits = ((dataBuffer[offSet+3] & 0x00FF) << 24) |
                          ((dataBuffer[offSet+2] & 0x00FF) << 16) |
                          ((dataBuffer[offSet+1] & 0x00FF) << 8) |
                          (dataBuffer[offSet+0] & 0x00FF);
            points[i] = new DataPoint();
            points[i].sno = i;
            points[i].y = Float.intBitsToFloat(intBits);
            points[i].x = i;
            /*for(int j=0;j<4;j++){
                System.out.print(hexify(buffer2[offSet+j])+" " );
            }
            System.out.println(" = " + data[sno]);*/
        }
        return points;
    }

    /**
     *
     * @param msg
     */
    public static void showErrorMessage(String msg){
        if(msg==null){
            msg = "Error ocurred!";
        }
        JOptionPane.showMessageDialog(mainFrame,msg, "CPNDS", JOptionPane.ERROR_MESSAGE);
    }

    /**
     *
     * @param msg
     */
    public static void showErrorMessage(Exception e){
        String msg = e.getClass().getSimpleName();
        String msg1 = e.getMessage();
        if(msg1!=null){
            msg += msg1;
        }
        JOptionPane.showMessageDialog(mainFrame,msg, "CPNDS", JOptionPane.ERROR_MESSAGE);
    }

    /**
     *
     * @param msg
     */
    public static void showErrorMessage(String msg, Exception e){
        if(msg==null){
            msg = "";
        }else{
            msg += "\n";
        }
        msg += e.getClass().getSimpleName();
        String msg1 = e.getMessage();
        if(msg1!=null){
            msg += msg1;
        }
        JOptionPane.showMessageDialog(mainFrame,msg, "CPNDS", JOptionPane.ERROR_MESSAGE);
    }
    /**
     *
     * @param msg
     */
    public static void showInfoMessage(String msg){
        JOptionPane.showMessageDialog(mainFrame, msg, null, JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     *
     * @param msg
     */
    public static void showInfoMessageTA(String msg){
        JTextArea ta = new JTextArea(msg);
        ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        int taHeight = ta.getPreferredSize().height;
        if(taHeight > 400){
            JScrollPane sp = new JScrollPane(ta);
            sp.setPreferredSize(new Dimension(ta.getPreferredSize().width+30, 400));
            JOptionPane.showMessageDialog(mainFrame, sp, null, JOptionPane.INFORMATION_MESSAGE);
        }else{
            ta.setPreferredSize(new Dimension(ta.getPreferredSize().width+30, taHeight+20));
            JOptionPane.showMessageDialog(mainFrame, ta, null, JOptionPane.INFORMATION_MESSAGE);
        }
    }
    /**
     *
     * @param msg
     */
    public static boolean showConfirm(String msg){
        if(JOptionPane.YES_OPTION ==
                JOptionPane.showConfirmDialog(
                        mainFrame,msg, null, JOptionPane.YES_NO_OPTION)){
            return true;
        }else{
            return false;
        }
    }
    /**
     *
     */
    public static void closeApplication(){
        if(myCommPort!=null){
            myCommPort.close();
        }
        mainFrame.dispose();
        System.exit(1);
    }
    /**
     *
     * @param buffer
     * @return
     */
    public static String hexify(byte[] buffer){
        String hex = "";
        for(int i=0;i<buffer.length;i++){
            char c1 = hexDigits[(buffer[i] & 0xf0) >> 4];
            char c2 = hexDigits[buffer[i] & 0x0f];
            hex += ""+c1+c2;
        }
    	return hex;
    }
    private static char[] hexDigits =
    {'0', '1', '2', '3', '4', '5', '6', '7',
     '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    //To generate dummy data if BB is not connected
    public static boolean GENERATE_DUMMY_DATA = false;
}