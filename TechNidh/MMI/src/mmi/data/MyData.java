package mmi.data;
import mmi.CommPort.MyCommPort;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import mmi.gui.MainFrame;
import gui.MyButton;
import java.awt.Font;
import java.io.FileOutputStream;
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
 * @version  : 1.0.0
 */
public class MyData{
    public static boolean debug = false;
    public static int delayBetBytes = 0;
    public static int CHANNEL_GUI_HEIGHT = 240;
    public static int CHANNEL_GUI_WIDTH = 450;
    public static final String applicationName = "MMI";
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
    //other data
    public static String logFolder = "LogReports";
    public static String settingsFile = "settings.txt";
    public static String systemDate = "";
    public static String loginTime = "";
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
            //appHome = appHome.replaceAll("/", "\\\\");
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
            String debugParam = readParameter("DEBUG");
            if(debugParam.equalsIgnoreCase("TRUE")){
                debug = true;
                CHANNEL_GUI_HEIGHT = 300;
            }else{
                debug = false;
            }
            delayBetBytes = Integer.parseInt(readParameter("DELAY"));
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
            String debugParam = "FALSE";
            if(debug){
                debugParam = "TRUE";
            }
            pw.println("DEBUG="+debugParam);
            pw.println("DELAY="+delayBetBytes);
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
    public static File chooseFile(File currentFile, final String nameStart) throws Exception{
        if(currentFile == null){
            currentFile = new File(appHome);
        }else{
            currentFile = currentFile.getParentFile();
        }
        JFileChooser jfc = new JFileChooser(currentFile);
        jfc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.isDirectory() || f.getName().startsWith(nameStart)){
                    return true;
                }else{
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return "mmi Data File";
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
     * @param msg
     */
    public static void showErrorMessage(String msg){
        if(msg==null){
            msg = "Error ocurred!";
        }
        JOptionPane.showMessageDialog(mainFrame, msg, "mmi", JOptionPane.ERROR_MESSAGE);
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
        JOptionPane.showMessageDialog(mainFrame,msg, "mmi", JOptionPane.ERROR_MESSAGE);
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
        JOptionPane.showMessageDialog(mainFrame,msg, "mmi", JOptionPane.ERROR_MESSAGE);
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
        System.exit(0);
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
            hex += " "+c1+c2;
        }
    	return hex;
    }
    /**
     *
     * @param buffer
     * @return
     */
    public static String hexifyReverseOrder(byte[] buffer){
        String hex = "";
        for(int i=0;i<buffer.length;i++){
            char c1 = hexDigits[(buffer[i] & 0xf0) >> 4];
            char c2 = hexDigits[buffer[i] & 0x0f];
            hex = " "+c1+c2+hex;
        }
    	return hex;
    }
    private static char[] hexDigits =
    {'0', '1', '2', '3', '4', '5', '6', '7',
     '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
}