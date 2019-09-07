package SmartTimeApplet.services;

import lib.session.MyUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URLConnection;
import java.util.Calendar;
import javax.swing.*;
import lib.gui.MyPanel;
import lib.utils.Depacketizer;
/*
 */

public final class MonthlyMuster extends MyPanel implements ActionListener {

    private AttendanceDetail[] attendanceDetails;
    private String[] monthsMM;//keeps months  01 02 ... 12
    private String[] yearsYY;//keeps years 11 10 09
    private int[] months;//keeps months 0 - 11
    private int[] years;//keeps years 2011 2010
    private String[] mothAndYears;//keeps January - 2011 ...
    private int numMonths = 12;
    public JComboBox comboMonth;
    private JPanel centerPanel;
    private Calendar todayCalendar = Calendar.getInstance();
    private JLabel monthLabel;
    private String rptName;
    public JPanel p;
    public JButton b, c;
    private int attendanceindex;
    /**
     *
     */
    private MyCalendar myCalendar = new MyCalendar() {

        @Override
        public JPanel getCustomDayPanel(Calendar calendar) {
            int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
            return attendanceDetails[monthDay - 1].getDayReportPanel(calendar, rptName);
        }
    };

    public MonthlyMuster(String rptName, int index) {
        try {
            this.rptName = rptName;
            attendanceindex = index;
            todayCalendar = getTodayDate(rptName);
            myCalendar.setToday(todayCalendar);
            this.setLayout(new BorderLayout());
            centerPanel = new JPanel(new BorderLayout());
            this.add(centerPanel, BorderLayout.CENTER);
            this.add(createTopPanel(), BorderLayout.NORTH);
            if ("AttendanceReport".equals(rptName) || "AbsentReport".equals(rptName) || "OutDoorDuty".equals(rptName) || "WeekOffApplet".equals(rptName) || "OverTimeApplet".equals(rptName) || "OnLeaveReport".equals(rptName) || "EarlyReport".equals(rptName) || "LateReport".equals(rptName) || "SingleSwipe".equals(rptName)) {
                showMuster(attendanceindex);
                comboMonth.setSelectedIndex(attendanceindex);
            } else if("MonthlyShiftDetails".equals(rptName)){
                
            }else{
                showMuster(0);
            }
        } catch (Exception e) {
            MyUtils.showException("Opening Monthly Muster", e);
        }
    }

    /**
     *
     * @return ''
     */
    public MonthlyMuster() {
    }

    public JPanel createTopPanel() {
        p = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 4));
        p.setPreferredSize(new Dimension(600, 30));
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        p.add(new JLabel("Month:", JLabel.RIGHT));
        //make combo
        monthsMM = new String[numMonths];
        yearsYY = new String[numMonths];
        months = new int[numMonths];
        years = new int[numMonths];
        mothAndYears = new String[numMonths];
        //set months and years fields
        int m = todayCalendar.get(Calendar.MONTH);//Calendar month is 0-11
        int y = todayCalendar.get(Calendar.YEAR);
        for (int i = 0; i < numMonths; i++) {
            months[i] = m;
            years[i] = y;
            monthsMM[i] = (m + 1) < 10 ? "0" + (m + 1) : "" + (m + 1);
            yearsYY[i] = (y - 2000) < 10 ? "0" + (y - 2000) : "" + (y - 2000);
            mothAndYears[i] = MyCalendar.monthName[m] + ", " + y;
            m -= 1;
            if (m == -1) {
                m = 11;
                y -= 1;
            }
        }
        comboMonth = new JComboBox(mothAndYears);
        comboMonth.setFont(new Font("MONOSPACED", Font.BOLD, 13));
        p.add(comboMonth);
        b = new JButton("Show");
        b.setActionCommand("ShowMuster");
        b.addActionListener(this);
        p.add(b);
        c = new JButton("Close");
        c.setActionCommand("Close");
        c.addActionListener(this);
        p.add(c);
        monthLabel = new JLabel(" ");
        monthLabel.setForeground(Color.red);
        monthLabel.setFont(new Font("MONOSPACED", Font.BOLD, 14));
        p.add(monthLabel);

        return p;
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "ShowMuster":
                int index = comboMonth.getSelectedIndex();
                showMuster(index);
                break;
            case "Close":
                // showThanks();
                break;
        }
    }

    /**
     *
     * @param mm
     * @param yy
     */
    private void getAttendanceDetails(String mm, String yy, String rptName) {
        try {
            clearAttendanceData();
            URLConnection conn = MyUtils.getURLConnection("MusterServlet");
            PrintStream out = new PrintStream(conn.getOutputStream());
            out.println("getMuster");
            out.println(mm);
            out.println(yy);
            out.println(rptName);
            out.close();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            //get holiday list
            String response = br.readLine();
            if (response.startsWith("ERROR")) {
                br.close();
                MyUtils.showMessage(response);
                return;
            }
            Depacketizer d1 = new Depacketizer(response);
            int holidayCount = d1.getInt();
            for (int i = 0; i < holidayCount; i++) {
                int day = Integer.parseInt(d1.getString());
                int j = day - 1;
                attendanceDetails[j].isHoliday = true;
                attendanceDetails[j].holidayReason = d1.getString();
            }
            // get attendance details
            response = br.readLine();
            br.close();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            Depacketizer d2 = new Depacketizer(response);
            int dayCount = d2.getInt();

            //get all docs
            for (int i = 0; i < dayCount; i++) {
                int day = Integer.parseInt(d2.getString());
                int j = day - 1;
                String timeIn = d2.getString();
                String timeOut = d2.getString();
                String status = d2.getString();
                attendanceDetails[j].setInOutData(timeIn, timeOut, status);
                attendanceDetails[j].leaveName = status;
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
        for (int i = 0; i < 31; i++) {
            attendanceDetails[i] = new AttendanceDetail(todayCalendar);
            attendanceDetails[i].day = i + 1;
        }
    }

    /**
     *
     * @param index
     */
    private void showMuster(int index) {
        getAttendanceDetails(monthsMM[index], yearsYY[index], rptName);
        centerPanel.removeAll();
        centerPanel.add(myCalendar.getMonthPanel(years[index], months[index]));
        centerPanel.validate();
        monthLabel.setText(MyCalendar.monthNameFull[months[index]] + ", " + years[index]);
    }

    private static Calendar getTodayDate(String rptName) {
        Calendar todayCalendar = Calendar.getInstance();
        int dd = todayCalendar.get(Calendar.DAY_OF_MONTH);
        int mm_0_11 = todayCalendar.get(Calendar.MONTH);
        int yyyy = todayCalendar.get(Calendar.YEAR);
        int hh = todayCalendar.get(Calendar.HOUR);
        int mi = todayCalendar.get(Calendar.MINUTE);
        int ss = todayCalendar.get(Calendar.SECOND);
        todayCalendar.clear();
        todayCalendar.set(yyyy, mm_0_11, dd, 0, 0, 0);
        return todayCalendar;
    }
}
