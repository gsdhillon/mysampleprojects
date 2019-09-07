package applet.services;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import lib.utils.Depacketizer;

/**
 * AttendanceDetails.java
 * Created on Aug 01, 2011, 12:48:00 PM
 * @author 
 */
public class AttendanceDetail{
    private Calendar todayCalendar;
    public int day = 0;
    public String timeIn = " ";
    public String timeOut = " ";
    public int minutesWorked = 0; 
    public Color bgColor = MyCalendar.bgColor;
    public String leaveName = null;
    public Color inColor = Color.BLACK;
    public Color outColor = Color.BLACK;
    public boolean isHoliday = false;
    public String holidayReason = " ";
    public AttendanceDetail(Calendar today){
        todayCalendar = today;
    }
    /**
     * 
     * @param calendar
     * @return '' 
     */
    public JPanel getDayReportPanel(Calendar calendar){
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        //set hrs worked and bgColor
        String hrsWorked = "       ";
        boolean weekEnd = (weekDay==1 || weekDay==7);
        boolean onLeave = leaveName!=null && !leaveName.trim().equals("");
        if(minutesWorked>0){
            if(weekEnd || isHoliday || onLeave){
                bgColor = new Color(171,227,254);//blue
            }else{
                if(minutesWorked>+510){
                   bgColor = new Color(200,255,159);//pink
                }else if(minutesWorked<450){
                   bgColor = new Color(242,187,247);//green
                }else{
                   bgColor = new Color(171,227,254);//blue
                }
            }
            NumberFormat nf = new DecimalFormat("00");
            int hrs = minutesWorked/60;
            int minutes = minutesWorked%60;
            hrsWorked = "["+nf.format(hrs)+":"+nf.format(minutes)+"] ";
        }

        //create panel
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBackground(bgColor);

        //add IN-OUT Times
        JPanel inOutPanel = new JPanel(new GridLayout(1, 2));
        inOutPanel.setBackground(bgColor);
        JLabel inLabel = new JLabel(timeIn, JLabel.LEFT);
        if(!isHoliday && !weekEnd && !onLeave){
            inLabel.setForeground(inColor);
        }
        inLabel.setFont(new Font("Arial",Font.PLAIN, 14));
        inOutPanel.add(inLabel);
        JLabel outLabel = new JLabel(timeOut, JLabel.RIGHT);
        if(!isHoliday && !weekEnd && !onLeave){
            outLabel.setForeground(outColor);
        }
        outLabel.setFont(new Font("Arial",Font.PLAIN, 14));
        inOutPanel.add(outLabel);
        panel.add(inOutPanel);

        //ADD MonthDay
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(bgColor);
        JLabel dayLabel = new JLabel(day+"", JLabel.CENTER);
        dayLabel.setFont(new Font("Arial",Font.BOLD, 20));
        //get the week day number of month day i
        if(weekEnd){
            dayLabel.setForeground(Color.RED);
        }else if(isHoliday){
            dayLabel.setForeground(Color.BLUE);
            dayLabel.setToolTipText(holidayReason);
        }else{
            dayLabel.setForeground(Color.BLACK);
        }
        middlePanel.add(dayLabel, BorderLayout.CENTER);
        JLabel hrsWorkedLabel = new JLabel(hrsWorked, JLabel.CENTER);
        hrsWorkedLabel.setFont(new Font("Arial",Font.BOLD, 13));
        middlePanel.add(hrsWorkedLabel, BorderLayout.EAST);
        panel.add(middlePanel);

        //add attendance status label
        JLabel label = new JLabel("",JLabel.CENTER);
        label.setFont(new Font("Arial",Font.PLAIN, 14));
        if(onLeave){
            label.setText(leaveName);
            label.setForeground(Color.BLUE);
        }else if(weekDay==1 || weekDay==7 || isHoliday || 
                !timeIn.trim().equals("") || !timeOut.trim().equals("")){
            label.setText(" ");
        }else if(!calendar.after(todayCalendar)){
            //IN OR OUT not found, Leave Not Found, No Holiday
            //And Date is not future So Mark Absent
            label.setText("Absent");
            label.setForeground(Color.RED);
        }else{
            label.setText(" ");
        }
        panel.add(label);
        return panel;
    }
    public void show(){
        //System.out.println(day+" "+timeIn+ " "+timeOut+" "+leaveName+" "+holidayReason);
    }
    /**
     *
     * @param d
     * @throws Exception
     */
    public void setData(Depacketizer d) throws Exception{
        String firstTimeIn = d.getString();
        String lastTimeOut = d.getString();
        inColor = d.getString().equals("Y")?Color.RED:Color.BLACK;
        outColor = d.getString().equals("Y")?Color.RED:Color.BLACK;
        leaveName = d.getString();
        //set In out Time and hrs worked
        DateFormat df = new SimpleDateFormat("yy-MM-dd#HH:mm:ss");
        Date in = null;
        Date out = null;
        //System.out.println(day+" "+firstTimeIn+" "+lastTimeOut);
        if(firstTimeIn!=null && firstTimeIn.length()>=17){
           timeIn = " "+firstTimeIn.substring(9, 14);
           in = df.parse(firstTimeIn);
        }
        if(lastTimeOut!=null && lastTimeOut.length()>=17){
           timeOut = lastTimeOut.substring(9, 14)+" ";
           out = df.parse(lastTimeOut);
        }
        //System.out.println(day+" "+in+" "+out);
        if(in != null && out != null){
           int secondsOut =(int)(out.getTime()/1000);
           int secondsIn = (int)(in.getTime()/1000);
           minutesWorked = (secondsOut - secondsIn)/60;
        }
    }
}
