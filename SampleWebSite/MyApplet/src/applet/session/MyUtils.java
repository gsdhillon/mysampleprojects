package applet.session;

import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import lib.utils.Depacketizer;
/**
 */
public class MyUtils {
    public static final String VERSION = "1.0";
    private static String basePath = "http://localhost:8080";
    private static String sessionID = null;
    public static MyApplet applet = null;
    /**
     * 
     * @param applet
     * @throws Exception
     */
    public static void setServerPath(MyApplet applet) throws Exception{
        MyUtils.applet = applet;
        URL url = applet.getDocumentBase();
        String protocol = url.getProtocol();
        String host = url.getHost();
        int portNo = url.getPort();
        String port = "";
        if(portNo > 0  && portNo != 80 ){
            port = ":"+portNo;
        }
        basePath = protocol+"://"+host+port;
        try{
            sessionID = applet.getParameter("SessionID");
        }catch(Exception e){
            System.out.println("Could not get SessionID info:"+e.getMessage());
        }
        //System.out.println("Server="+basePath+", Version="+VERSION+", SessionID="+sessionID);
    }
    /**
     * 
     * @return '' 
     */
    public static String getBasePath() {
        return basePath;
    }
    /**
     * 
     * @param servletName
     * @return ''
     * @throws Exception 
     */
    public static URLConnection getURLConnection(String servletName) throws Exception{
        URL servletURL = new URL(basePath + "/" + servletName);
        URLConnection conn = servletURL.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(true);
        conn.setRequestProperty("Cookie", "JSESSIONID=" + sessionID);  
        return conn;
    }
    /**
     * 
     * @param servletName
     * @return ''
     * @throws Exception 
     */
    public static MSC getMSC(String servletName) throws Exception{
        URLConnection conn = getURLConnection(servletName);
        //conn.setRequestProperty("Content-Type",  "binary/data");
        return new MSC(conn);
    }
    /**
     * 
     * @param relativePath
     * @return ''
     * @throws Exception 
     */
    public static URL getRelativeURL(String relativePath) throws Exception{
        URL url = new URL(basePath + "/" + relativePath);
        return url;
    }
    /**
     *
     * @param relativePath
     * @return
     */
    public static String getRelativeURLString(String relativePath){
        return basePath + "/" + relativePath;
    }
    /**
     * Note: month is 0-11
     * @return
     * @throws Exception
     */
    public static Calendar getTodayDateFull() throws Exception{
        MSC msc = MyUtils.getMSC("MyDataServlet");
        msc.openOS();
        msc.println("getCurrentDateTime");//DD-MM-YYYY-HH24-MI-SS
        msc.closeOS();
        msc.openIS();
        String response = msc.readLine();
        msc.closeIS();
        if (response.startsWith("ERROR")) {
            throw new Exception(response);
        }
        //showMessage(response);
        Depacketizer d = new Depacketizer(response); 
        //get today date
        int dd = d.getInt();
        int mm_0_11 = d.getInt()-1;
        int yyyy = d.getInt();
        int hh = d.getInt();
        int mi = d.getInt();
        int ss = d.getInt();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.clear();
        todayCalendar.set(yyyy, mm_0_11, dd, hh, mi, ss);
        return todayCalendar;
    }
    /**
     * Note: month is 0-11
     * @return
     * @throws Exception
     */
    public static Calendar getTodayDate() throws Exception{
        MSC msc = MyUtils.getMSC("MyDataServlet");
        msc.openOS();
        msc.println("getCurrentDateTime");//DD-MM-YYYY-HH24-MI-SS
        msc.closeOS();
        msc.openIS();
        String response = msc.readLine();
        msc.closeIS();
        if (response.startsWith("ERROR")) {
            throw new Exception(response);
        }
        //showMessage(response);
        Depacketizer d = new Depacketizer(response);
        //get today date
        int dd = d.getInt();
        int mm_0_11 = d.getInt()-1;
        int yyyy = d.getInt();
        int hh = d.getInt();
        int mi = d.getInt();
        int ss = d.getInt();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.clear();
        todayCalendar.set(yyyy, mm_0_11, dd, 0, 0, 0);
        return todayCalendar;
    }
    /**
     *
     * @param message
     */
    public static void showMessage(String message){
        if(message == null) message = "null";
        message = message.replaceAll(";", "\n");
        JTextArea ta = new JTextArea(message);
        ta.setFont(new Font("MONOSPACED", Font.PLAIN, 14));
        JScrollPane sp = new JScrollPane(ta);
        if((ta.getPreferredSize().height+30)>500){
            sp.setPreferredSize(new Dimension(
                                ta.getPreferredSize().width+30,
                                500));
        }else{
            sp.setPreferredSize(new Dimension(
                                ta.getPreferredSize().width+30,
                                ta.getPreferredSize().height+30));
        }
        JOptionPane.showMessageDialog(applet, ta, 
                "", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     *
     * @param cal
     * @param days
     * @return
     * @throws Exception
     */
    public static Calendar addDays(Calendar cal, long days) throws Exception{
        long x =cal.getTimeInMillis();
        x +=(days*86400000);    //24*60*60*1000=86400000 millisec ina day
        Calendar temp = Calendar.getInstance();
        temp.clear();
        temp.setTimeInMillis(x);
        return temp;
    }
    /**
     *
     * @param msg
     * @param e
     */
    public static void showException(String msg, Exception e){
        e.printStackTrace();
        String message = "Exception on "+msg+":\n"+e.getLocalizedMessage();
        showMessage(message);
    }

    /**
     * @param dd_mm_yyyy  - in dd/mm/yyyy
     * @return
     * @throws Exception
     */
    public static Calendar convertToCalendar(String dd_mm_yyyy) throws Exception{
        StringTokenizer st = new StringTokenizer(dd_mm_yyyy, "/");
        int dd = Integer.parseInt(st.nextToken());
        int mm = Integer.parseInt(st.nextToken());
        int yy = Integer.parseInt(st.nextToken());
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(yy, mm-1, dd);//*** mm is 0-11
        return calendar;
    }
    /**
     *
     * @param cal
     * @return
     */
    public static String calendarToString(Calendar cal){
        return cal.get(Calendar.DATE)
                +"/"+(cal.get(Calendar.MONTH)+1)//MONTH in calendar is 0-11
                +"/"+cal.get(Calendar.YEAR);
    }
}