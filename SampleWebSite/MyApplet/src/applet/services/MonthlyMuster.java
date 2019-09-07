package applet.services;

import applet.session.MSC;
import applet.session.MyApplet;
import applet.session.MyUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.*;
import lib.utils.Depacketizer;
/*
 * MonthlyMuster.java
 * Created on Aug 01, 2011, 12:48:00 PM
 */
public class MonthlyMuster extends MyApplet implements ActionListener{
    private AttendanceDetail[] attendanceDetails;
    private String[] monthsMM;//keeps months  01 02 ... 12
    private String[] yearsYY;//keeps years 11 10 09
    private int[] months;//keeps months 0 - 11
    private int[] years;//keeps years 2011 2010
    private String[] mothAndYears;//keeps January - 2011 ...
    private int numMonths = 12;
    private Calendar todayCalendar = Calendar.getInstance();
    private Container contentPane;
    private JComboBox<String> comboMonth;
    private JPanel centerPanel;
    private JLabel monthLabel;
    private JLabel userNameLabel;
    /**
     * 
     */
    private MyCalendar myCalendar = new MyCalendar() {
        @Override
        public JPanel getCustomDayPanel(Calendar calendar) {
            int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
            return attendanceDetails[monthDay-1].getDayReportPanel(calendar);
        }
    };
    @Override
    public void init() {
        try{
            super.init();/*Gurmeet 18-08-11*//**/
            todayCalendar = MyUtils.getTodayDate();//
            myCalendar.setToday(todayCalendar);
            contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());
            centerPanel = new JPanel(new BorderLayout());
            contentPane.add(centerPanel, BorderLayout.CENTER);
            contentPane.add(createTopPanel(), BorderLayout.NORTH);
            showMuster(0);
         }catch(Exception e){
            MyUtils.showException("Opening Monthly Muster", e);
         }    
    }
    /**
     * 
     * @return '' 
     */
    private JPanel createTopPanel(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER,2,4));
        p.setPreferredSize(new Dimension(600,30));
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        p.add(new JLabel("Month:", JLabel.RIGHT));
        //make combo
        monthsMM = new String[numMonths];
        yearsYY = new String[numMonths];
        months = new int[numMonths];
        years = new int[numMonths];
        mothAndYears = new String[numMonths];
        //set months and years fields
        int mm_0_11 = todayCalendar.get(Calendar.MONTH);//Calendar month is 0-11
        int yyyy = todayCalendar.get(Calendar.YEAR);
        for(int i=0;i<numMonths;i++){
            months[i] = mm_0_11;
            years[i] = yyyy;
            monthsMM[i] = (mm_0_11+1)<10?"0"+(mm_0_11+1):""+(mm_0_11+1);
            yearsYY[i] = (yyyy-2000)<10?"0"+(yyyy-2000):""+(yyyy-2000);
            mothAndYears[i] = MyCalendar.monthName[mm_0_11]+"_"+yyyy;
            mm_0_11 -= 1;
            if(mm_0_11 == -1){
                mm_0_11 = 11;
                yyyy -= 1;
            }
        }
        comboMonth = new JComboBox<>(mothAndYears);
        comboMonth.setFont(new Font("MONOSPACED",Font.BOLD,13));
        p.add(comboMonth);
        JButton b = new JButton("Show");
        b.setActionCommand("ShowMuster");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Close");
        b.setActionCommand("Close");
        b.addActionListener(this);
        p.add(b);
        monthLabel = new JLabel(" ");
        monthLabel.setForeground(Color.red);
        monthLabel.setFont(new Font("MONOSPACED", Font.BOLD, 14));
        p.add(monthLabel);
        //user name
        userNameLabel = new JLabel(" ");
        userNameLabel.setForeground(Color.blue);
        userNameLabel.setFont(new Font("MONOSPACED", Font.BOLD, 14));
        p.add(userNameLabel);
        return p;
    }
    /**
     * 
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
        if("ShowMuster".equals(e.getActionCommand())){
            int index = comboMonth.getSelectedIndex();
            showMuster(index);
        }else if("Close".equals(e.getActionCommand())){
            showThanks();
        }
    }
    /**
     * 
     * @param mm
     * @param yy 
     */
    private void getAttendanceDetails(int index) {
        try{
            clearAttendanceData();
            MSC msc = MyUtils.getMSC("MusterServlet");
            msc.openOS();
            msc.println("getMuster");
            msc.println(monthsMM[index]);
            msc.println(yearsYY[index]);
            msc.closeOS();
            //
            msc.openIS();
            //get attendance details
            String MusterPacket = msc.readLine();
            msc.closeIS();
            if (MusterPacket.startsWith("ERROR")) {
                MyUtils.showMessage(MusterPacket);
                return;
            }
            //MyUtils.showMessage(response);
            Depacketizer d = new Depacketizer(MusterPacket);
            int dayCount = d.getInt();
            userNameLabel.setText(d.getString());//only in first record
            //get all docs
            for (int i = 0; i < dayCount; i++) {
                int day = d.getInt();
                int j = day-1;
                attendanceDetails[j].setData(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyUtils.showMessage("ERROR:Could not get attendance records!\n" + e.getMessage());
        }
    }
    /**
     * 
     */
    private void clearAttendanceData() {
        attendanceDetails = new AttendanceDetail[31];
        for(int i=0;i<31;i++){
            attendanceDetails[i] = new AttendanceDetail(todayCalendar);
            attendanceDetails[i].day = i+1;
        }
    }
    /**
     * 
     * @param index 
     */
    private void showMuster(int index) {
        getAttendanceDetails(index);
        centerPanel.removeAll();
        centerPanel.add(myCalendar.getMonthPanel(years[index], months[index]));
        centerPanel.validate();
        monthLabel.setText(MyCalendar.monthNameFull[months[index]]+", "+years[index]);
    }
}