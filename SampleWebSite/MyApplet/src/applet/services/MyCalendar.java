package applet.services;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * MyCalendar.java
 * Created on Aug 01, 2011, 12:48:00 PM
 * @author 
 */
public abstract class MyCalendar {
    public static String[] dayName = {
        "SUN",
        "MON",
        "TUE",
        "WED",
        "THU",
        "FRI",
        "SAT"};
    public static String[] monthName = {
        "JAN", 
        "FEB", 
        "MAR", 
        "APR", 
        "MAY", 
        "JUN",
        "JUL",
        "AUG",
        "SEP",
        "OCT",
        "NOV",
        "DEC"};
    public static String[] mm = {
        "01", 
        "02", 
        "03", 
        "04", 
        "05", 
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12"
    };
    public static String[] monthNameFull = {
        "January", 
        "February", 
        "March", 
        "April", 
        "May", 
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"};
    public static Color bgColor = Color.WHITE;
    public static Color bgColorBlank = new Color(160,160,160);
    private Calendar todayCalendar;
    /**
     * 
     */
    public MyCalendar(){
        todayCalendar = Calendar.getInstance();
    }
    /**
     * 
     */
    public void setToday(Calendar calendar){
        todayCalendar = calendar;
    }
    /**
     * 
     * @param year   2011
     * @param month  0-11
     */
    public JPanel getMonthPanel(int year, int month){
        JPanel mothPanel = new JPanel(new BorderLayout());
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, 1);
        int startDay = calendar.get(Calendar.DAY_OF_WEEK);//1 to 7
        int numDays = numDays(calendar);
        //week days name panel
        mothPanel.add(getWeekDaysNamePanel(), BorderLayout.NORTH);
        JPanel monthDaysPanel = new JPanel(new GridLayout(0,7));
        //fill days
        int endDay = numDays;//last day in 5th row
        int numExtraDayDay = 0;
        if((startDay+numDays-1)/7.0>5){
            numExtraDayDay = (startDay+numDays-1)%7;
            endDay = numDays  - numExtraDayDay;
            for(int day=endDay+1; day<=numDays;day++){
                calendar.set(Calendar.DAY_OF_MONTH, day);
                monthDaysPanel.add(getDayPanel(calendar));
            }
        }
        for(int i=1+numExtraDayDay;i<startDay;i++){
            monthDaysPanel.add(getBlankDayPanel());
        }
        for(int day=1;day<=endDay;day++){
            calendar.set(Calendar.DAY_OF_MONTH, day);
            monthDaysPanel.add(getDayPanel(calendar));
        }
        for(int i=0;i<35-numDays-startDay+1;i++){
            monthDaysPanel.add(getBlankDayPanel());
        }
        monthDaysPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mothPanel.add(monthDaysPanel, BorderLayout.CENTER);
        return mothPanel;
    }
    /**
     */
    private boolean leap(int y){
        if(y%400 == 0) return true;
        if(y%100 == 0) return false;
        if(y%4 == 0)   return true;
        return false;
    }
    /**
     * 
     */
    private int numDays(Calendar calendar){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);//month 0-11
        if(month == 1){
            if(leap(year)){
                return 29;
            }else{
                return 28;
            }
        }else if(month == 3 || month == 5 || month == 8 || month == 10){
            return 30;
        }else{
            return 31;
        }
    }
    
    /**
     * 
     * @return '' 
     */
    private JPanel getWeekDaysNamePanel() {
        JPanel weekDaysPanel = new JPanel(new GridLayout(1,7));
        weekDaysPanel.setPreferredSize(new Dimension(600, 30));
        weekDaysPanel.setBackground(Color.WHITE);
        //weekDaysPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        for(int i=0;i<7;i++){
            JLabel l = new JLabel(dayName[i],JLabel.CENTER);
            l.setFont(new Font("Arial", Font.BOLD, 18));
            l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            if(i==0) l.setForeground(Color.RED);
            weekDaysPanel.add(l);
        }
        return weekDaysPanel;
    }
    
    /**
     * 
     * @return '' 
     */
    private JPanel getBlankDayPanel(){
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setBackground(bgColorBlank);
        return panel;
    }
    /**
     * 
     * @param calendar
     * @return '' 
     */
    public JPanel getDayPanel(Calendar calendar){
        JPanel panel = getCustomDayPanel(calendar);
        if(calendar.compareTo(todayCalendar)==0){
            panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        }else{
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        return panel;
    }
    public abstract JPanel getCustomDayPanel(Calendar calendar);
}