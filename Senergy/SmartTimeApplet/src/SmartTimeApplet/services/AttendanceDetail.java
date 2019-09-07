package SmartTimeApplet.services;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AttendanceDetail {

    private Calendar todayCalendar;
    public int day = 0;
    public String timeIn = " ";
    public String timeOut = " ";
    public String status = "";
    public int minutesWorked = 0;
    static Color bgColor = MyCalendar.bgColor;
    static String strString = null;
    public String leaveName;
    static Color inColor = Color.BLACK;
    static Color outColor = Color.BLACK;
    public boolean isHoliday = false;
    public String holidayReason = " ";

    public AttendanceDetail(Calendar today) {
        todayCalendar = today;
    }

    public void setInColor(String colorHex) {
        inColor = getColor(colorHex);//Color.BLACK;//
    }

    public void setOutColor(String colorHex) {
        outColor = getColor(colorHex);//Color.BLACK;//
    }

    private Color getColor(String colorHex) {
        switch (colorHex) {
            case "#006600":
                return Color.GREEN.darker();
            case "#FF0000":
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }

    /**
     *
     * @param calendar
     * @return ''
     */
    public JPanel getDayReportPanel(Calendar calendar, String rptName) {
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        //set hrs worked and bgColor
        String hrsWorked = "       ";
        //boolean weekEnd = (weekDay == 1 || weekDay == 7);
        boolean weekEnd = (weekDay == 1);
        boolean onLeave = leaveName != null && !leaveName.trim().equals("");
        EmpStatus state = new EmpStatus();
        strString = state.getStatus(status, rptName);

        if (minutesWorked > 0) {
            NumberFormat nf = new DecimalFormat("00");
            int hrs = minutesWorked / 60;
            int minutes = minutesWorked % 60;
            hrsWorked = "[" + nf.format(hrs) + ":" + nf.format(minutes) + "] ";
        }

        //create panel
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBackground(bgColor);

        //add IN-OUT Times
        Font font;
        JPanel inOutPanel = new JPanel(new GridLayout(1, 2));
        inOutPanel.setBackground(bgColor);
        JLabel inLabel = new JLabel(timeIn, JLabel.LEFT);
        if (!isHoliday && !weekEnd && !onLeave) {
            inLabel.setForeground(inColor);
            font = new Font("Verdana", Font.BOLD, 10);
            inLabel.setFont(font);
            inLabel.setOpaque(true);
            inLabel.repaint();


        }
        inLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inOutPanel.add(inLabel);
        JLabel outLabel = new JLabel(timeOut, JLabel.RIGHT);
        if (!isHoliday && !weekEnd && !onLeave) {
            outLabel.setForeground(outColor);
            font = new Font("Verdana", Font.BOLD, 10);
            outLabel.setFont(font);
            outLabel.setOpaque(true);
            inLabel.repaint();
        }
        outLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inOutPanel.add(outLabel);
        panel.add(inOutPanel);

        //ADD MonthDay
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(bgColor);
        JLabel dayLabel = new JLabel(day + "", JLabel.CENTER);
        dayLabel.setFont(new Font("Verdana", Font.PLAIN, 22));

        JLabel lblHoliday = new JLabel(day + "", JLabel.CENTER);
        lblHoliday.setFont(new Font("Times", Font.PLAIN, 14));

        //get the week day number of month day i
        if (weekEnd) {
            dayLabel.setForeground(Color.RED);
        } else if (isHoliday) {
            dayLabel.setForeground(Color.BLUE);
            dayLabel.setToolTipText(holidayReason);
        } else {
            dayLabel.setForeground(Color.BLACK);
        }
        middlePanel.add(dayLabel, BorderLayout.CENTER);

        JLabel hrsWorkedLabel = new JLabel(hrsWorked, JLabel.CENTER);
        hrsWorkedLabel.setFont(new Font("Arial", Font.BOLD, 13));
        middlePanel.add(hrsWorkedLabel, BorderLayout.EAST);
        panel.add(middlePanel);

        //add status label
        JLabel label = new JLabel("", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        if (onLeave) {
            label.setText(leaveName);
            label.setForeground(Color.BLUE);
        } else if (isHoliday || !timeIn.trim().equals("") || !timeOut.trim().equals("")) {

            label.setText(" ");
        } else if (!calendar.after(todayCalendar)) {
            //IN OR OUT not found, Leave Not Found, No Holiday
            //And Date is not future So Mark Absent
            label.setText("");
            label.setForeground(Color.RED);
        } else {
            label.setText(" ");
        }
        //added by pradnya
        if (isHoliday) {
            label.setFont(new Font("Arial", Font.PLAIN, 10));
            label.setText(holidayReason);
            label.setForeground(Color.RED);
        }
        if (!"".equals(strString) && !strString.trim().equals("")) {
            label.setText(strString);
            label.setForeground(Color.BLUE);
        }
        inLabel.setForeground(inColor);
        outLabel.setForeground(outColor);

        panel.add(label);
        return panel;
    }

    public void show() {
        //System.out.println(day+" "+timeIn+ " "+timeOut+" "+leaveName+" "+holidayReason);
    }

    /**
     *
     * @param firstTimeIn yy-MM-dd#hh:mm:ss
     */
    public void setInOutData(String firstTimeIn, String lastTimeOut, String status) {
        try {
            this.status = status;
            //DateFormat df = new SimpleDateFormat("yy-MM-dd#HH:mm:ss");
            int inTime = 0;
            int outTime = 0;
            if (firstTimeIn != null) {
                timeIn = " " + firstTimeIn.substring(0, 5);
                String inTemp[];
                inTemp = firstTimeIn.split(":");
                for (int i = 0; i < inTemp.length; i++) {
                    if (inTemp[0] != null) {
                        inTime = 60 * 60 * (Integer.parseInt(inTemp[0]));
                    }
                    if (inTemp[1] != null) {
                        inTime = inTime + (60 * (Integer.parseInt(inTemp[1])));
                    }
                    if (inTemp[0] != null) {
                        inTime = inTime + (Integer.parseInt(inTemp[0]));
                    }
                }
            }
            if (lastTimeOut != null) {
                timeOut = " " + lastTimeOut.substring(0, 5);
                String outTemp[];
                outTemp = lastTimeOut.split(":");
                for (int i = 0; i < outTemp.length; i++) {
                    if (outTemp[0] != null) {
                        outTime = 60 * 60 * (Integer.parseInt(outTemp[0]));
                    }
                    if (outTemp[1] != null) {
                        outTime = outTime + (60 * (Integer.parseInt(outTemp[1])));
                    }
                    if (outTemp[0] != null) {
                        outTime = outTime + (Integer.parseInt(outTemp[0]));
                    }
                }
            }
            if (inTime != 0 && outTime != 0) {
                minutesWorked = (outTime - inTime) / 60;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
