package SmartTimeApplet.services;

import java.awt.*;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

    public static int getMonthDays(int month, int year) {
        int days = 0;
        boolean isLeapYear = false;
        int tempyear = Integer.parseInt("20" + year);
        if (tempyear % 4 == 0) {
            isLeapYear = true;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 2:
                if (isLeapYear == false) {
                    days = 28;
                } else {
                    days = 29;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;

        }
        return days;
    }
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
    public static Color bgColor = new Color(254, 253, 231);//Color.WHITE;//
    public static Color bgColorBlank = new Color(160, 160, 160);//Color.GRAY;;//
    private Calendar todayCalendar;

    public MyCalendar() {
        todayCalendar = Calendar.getInstance();
    }

    public void setToday(Calendar calendar) {
        todayCalendar = calendar;
    }

    /**
     *
     * @param year 2011
     * @param month 0-11
     */
    public JPanel getMonthPanel(int year, int month) {
        JPanel mothPanel = new JPanel(new BorderLayout());
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, 1);
        int startDay = calendar.get(Calendar.DAY_OF_WEEK);//1 to 7
        int numDays = numDays(calendar);
        //week days name panel
        mothPanel.add(getWeekDaysNamePanel(), BorderLayout.NORTH);
        JPanel monthDaysPanel = new JPanel(new GridLayout(0, 7));
        //fill days
        int endDay = numDays;//last day in 5th row
        int numExtraDayDay = 0;
        if ((startDay + numDays - 1) / 7.0 > 5) {
            numExtraDayDay = (startDay + numDays - 1) % 7;
            endDay = numDays - numExtraDayDay;
            for (int day = endDay + 1; day <= numDays; day++) {
                calendar.set(Calendar.DAY_OF_MONTH, day);
                monthDaysPanel.add(getDayPanel(calendar));
            }
        }
        for (int i = 1 + numExtraDayDay; i < startDay; i++) {
            monthDaysPanel.add(getBlankDayPanel());
        }
        for (int day = 1; day <= endDay; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            monthDaysPanel.add(getDayPanel(calendar));
        }
        for (int i = 0; i < 35 - numDays - startDay + 1; i++) {
            monthDaysPanel.add(getBlankDayPanel());
        }
        monthDaysPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mothPanel.add(monthDaysPanel, BorderLayout.CENTER);
        return mothPanel;
    }

    private boolean leap(int y) {
        if (y % 400 == 0) {
            return true;
        }
        if (y % 100 == 0) {
            return false;
        }
        if (y % 4 == 0) {
            return true;
        }
        return false;
    }

    private int numDays(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);//month 0-11
        if (month == 1) {
            if (leap(year)) {
                return 29;
            } else {
                return 28;
            }
        } else if (month == 3 || month == 5 || month == 8 || month == 10) {
            return 30;
        } else {
            return 31;
        }
    }

    private JPanel getWeekDaysNamePanel() {
        JPanel weekDaysPanel = new JPanel(new GridLayout(1, 7));
        weekDaysPanel.setPreferredSize(new Dimension(600, 30));
        weekDaysPanel.setBackground(new Color(250, 243, 163));
        weekDaysPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        for (int i = 0; i < 7; i++) {
            JLabel l = new JLabel(dayName[i], JLabel.CENTER);
            l.setFont(new Font("Times", Font.PLAIN, 20));
            l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            if (i == 0) {//|| i == 6
                l.setForeground(Color.RED);
            }
            weekDaysPanel.add(l);
        }
        return weekDaysPanel;
    }

    private JPanel getBlankDayPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setBackground(bgColorBlank);
        return panel;
    }

    public JPanel getDayPanel(Calendar calendar) {
        JPanel panel = getCustomDayPanel(calendar);
        if (calendar.compareTo(todayCalendar) == 0) {
            panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        } else {
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        return panel;
    }

    public abstract JPanel getCustomDayPanel(Calendar calendar);
}
