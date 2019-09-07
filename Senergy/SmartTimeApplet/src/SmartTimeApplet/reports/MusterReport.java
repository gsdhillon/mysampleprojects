package SmartTimeApplet.reports;

import SmartTimeApplet.services.MyCalendar;
import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import lib.excel.MyExcel;
import lib.gui.MyButton;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
import lib.utils.MyFile;
import lib.utils.MyLog;

/**
 * MusterReport.java
 */
public class MusterReport extends MyApplet {

    private AttendanceReportTM tableModel;
    private MyTextField searchTextName = new MyTextField("search name");
    private UserMuster[] userMusters = null;
    //attendance report
    private Calendar todayCalendar = Calendar.getInstance();
    private JComboBox comboMonth;
    private String[] monthsMM;//keeps months  01 02 ... 12
    private String[] yearsYY;//keeps years 11 10 09
    private int[] months;//keeps months 0 - 11
    private int[] years;//keeps years 2013 2012
    private String[] mothAndYears;//keeps January_2013, ...
    private int numMonths = 12;
    private String divName = "";

    @Override
    public void init() {
        try {
            super.init();
            setLayout(new BorderLayout());
            createMonthCombo();
            tableModel = new AttendanceReportTM();
            MyPanel p = createHeaderPanel();
            add(p, BorderLayout.NORTH);
            //create Documents table
            MyTable table = new MyTable(tableModel);
            table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
            add(table.getGUI(), BorderLayout.CENTER);
        } catch (Exception e) {
            MyUtils.showException("UserMaster", e);
        }
    }

    /**
     *
     * @throws Exception
     */
    private void createMonthCombo() throws Exception {
        //
        todayCalendar = MyUtils.getTodayDate();
        //make combo
        monthsMM = new String[numMonths];
        yearsYY = new String[numMonths];
        months = new int[numMonths];
        years = new int[numMonths];
        mothAndYears = new String[numMonths];
        //set months and years fields
        int mm_0_11 = todayCalendar.get(Calendar.MONTH);//Calendar month is 0-11
        int yyyy = todayCalendar.get(Calendar.YEAR);
        for (int i = 0; i < numMonths; i++) {
            months[i] = mm_0_11;
            years[i] = yyyy;
            monthsMM[i] = (mm_0_11 + 1) < 10 ? "0" + (mm_0_11 + 1) : "" + (mm_0_11 + 1);
            yearsYY[i] = (yyyy - 2000) < 10 ? "0" + (yyyy - 2000) : "" + (yyyy - 2000);
            mothAndYears[i] = MyCalendar.monthName[mm_0_11] + "_" + yyyy;
            mm_0_11 -= 1;
            if (mm_0_11 == -1) {
                mm_0_11 = 11;
                yyyy -= 1;
            }
        }
        comboMonth = new JComboBox(mothAndYears);
        comboMonth.setFont(new Font("MONOSPACED", Font.BOLD, 13));
    }

    private void viewReport() {
        try {
//            ProgressDialog pd = new ProgressDialog(this, "Loading...");
//            final JProgressBar bar = new JProgressBar(0, 250000);
//            bar.setValue(1000);
//            bar.setIndeterminate(false);
//            JOptionPane j = new JOptionPane(bar);
//            final JDialog d = j.createDialog(j, "Expierment X");
//            d.pack();
//            d.setVisible(true);
//            bar.setValue(40000);
            int index = comboMonth.getSelectedIndex();
            userMusters = getAttendanceDetailsAll(index);
            tableModel.setData(userMusters);
//            pd.disposeProgress();
        } catch (Exception e) {
            MyUtils.showException("Getting Report", e);
        }
    }

    private void exportMusterToExcel() {
        try {
            int index = comboMonth.getSelectedIndex();
            if (userMusters == null) {
                userMusters = getAttendanceDetailsAll(index);
                tableModel.setData(userMusters);
            }
            if (userMusters != null) {
                MyExcel.exportMuster(userMusters, divName, years[index], months[index]);
            }
        } catch (Exception e) {
            MyUtils.showException("Export Excel", e);
        }
    }

    private void exportReportToExcel() {
        try {
            int index = comboMonth.getSelectedIndex();
            if (userMusters == null) {
                userMusters = getAttendanceDetailsAll(index);
                tableModel.setData(userMusters);
            }
            if (userMusters != null) {
                File file = MyFile.chooseFileForSave("Attendace_" + divName + "_" + mothAndYears[index] + ".xls");
                if (file != null) {
                    MyExcel.exportJTable(tableModel, file, "Muster Report ", divName, 2);
                }
            }
        } catch (Exception exception) {
            MyLog.showException(exception);
        }
    }

    /**
     *
     * @param mm
     * @param yy
     */
    private UserMuster[] getAttendanceDetailsAll(int index) {
        try {////
            MyHTTP myHTTP = MyUtils.createServletConnection("MusterServlet");
            myHTTP.openOS();
            myHTTP.println("getUserMusterForAll");
            myHTTP.println(monthsMM[index]);
            myHTTP.println(yearsYY[index]);
            myHTTP.closeOS();
            //
            myHTTP.openIS();
            //get no of employees
            String response = myHTTP.readLine();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return null;
            }
            int numEmps = Integer.parseInt(response);
            divName = myHTTP.readLine();
            UserMuster[] musters = new UserMuster[numEmps];
            //get attendance details of all employees
            for (int k = 0; k < numEmps; k++) {
                musters[k] = new UserMuster();
                //make empty muster
                musters[k].attendance = new EmpAttendance[31];
                for (int i = 0; i < 31; i++) {
                    musters[k].attendance[i] = new EmpAttendance(months[index], years[index]);
                    musters[k].attendance[i].setDay(i + 1);//1-31
                }
                //get attendane
                String musterPacket = myHTTP.readLine();
                if (musterPacket.startsWith("ERROR")) {
                    MyUtils.showMessage(musterPacket);
                    return null;
                }
                Depacketizer d = new Depacketizer(musterPacket);
                int dayCount = d.getInt();
                musters[k].user.userID = d.getString();//user_id only in first record
                musters[k].user.name = d.getString();//name only in first record
                musters[k].user.desig = d.getString();//name only in first record
                //get all attendance records
                for (int i = 0; i < dayCount; i++) {
                    int day = d.getInt();
                    int j = day - 1;
                    musters[k].attendance[j].setData(d);
                }
                //calculate total Leaves/Abscents/AvgHrsWork etc
                musters[k].calculateAggregateValues();
            }
            myHTTP.closeIS();
            return musters;
        } catch (Exception e) {
            e.printStackTrace();
            MyUtils.showMessage("ERROR:Could not get attendance records!\n" + e.getMessage());
            return null;
        }
    }

    /**
     *
     * @return
     */
    private MyPanel createHeaderPanel() {
        MyPanel p = new MyPanel(new GridLayout(1, 5));
        //
        p.add(comboMonth);
        //
        MyButton b = new MyButton("View Report") {

            @Override
            public void onClick() {
//                MyUtils.showMessage("Hello world");
                viewReport();
            }
        };
        p.add(b);
        //
        b = new MyButton("Export Report") {

            @Override
            public void onClick() {
                exportReportToExcel();
            }
        };
        p.add(b);
        //
        b = new MyButton("Export Muster") {

            @Override
            public void onClick() {
                exportMusterToExcel();
            }
        };
        p.add(b);
        p.add(searchTextName);
        return p;
    }
}