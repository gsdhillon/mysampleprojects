package applet.reports;

import applet.services.MyCalendar;
import applet.services.UserMuster;
import applet.session.MSC;
import applet.session.MyApplet;
import applet.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Calendar;
import javax.swing.JComboBox;
import lib.gui.MyButton;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
/**
 * MusterReport.java
 */
public class MusterReport extends MyApplet{
    private AttendanceReportTM tableModel;
    private MyTextField searchTextName = new MyTextField("search name");
    private UserMuster[] userMusters = null;
    //attendance report
    private Calendar todayCalendar = Calendar.getInstance();
    private JComboBox<String> comboMonth;
    private String[] monthsMM;//keeps months  01 02 ... 12
    private String[] yearsYY;//keeps years 11 10 09
    private int[] months;//keeps months 0 - 11
    private int[] years;//keeps years 2013 2012
    private String[] mothAndYears;//keeps January_2013, ...
    private int numMonths = 12;
    private String divName = "";
    @Override
    public void init() {
        try{
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
    private void createMonthCombo() throws Exception{
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
    }
    /**
     * 
     *
    private void getUserMasterList() {
        try {
            MSC msc = MyUtils.getMSC("UserMasterServlet");
            msc.openOS();
            msc.println("getUserList");
            msc.closeOS();

            msc.openIS();
            String response = msc.readLine();
            msc.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            //MyUtils.showMessage(response);
            Depacketizer d = new Depacketizer(response);
            int rowCount = d.getInt();
            User[] users = new User[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {
                users[i] = new User();
                users[i].userID = d.getString();
                users[i].name = d.getString();
                users[i].desig = d.getString();
                users[i].division = d.getString();
                users[i].dob = new MyDate(d.getString(), MyDate.FORMAT_DD_MM_YYYY);
            }
            tableModel.setData(users);
        } catch (Exception e) {
            MyUtils.showException("GetUserList",e);
            tableModel.setData(null);
        }
    }*/
    /**
     *
     * @param index
     */
    private void viewReport() {
        try{
            int index = comboMonth.getSelectedIndex();
            userMusters = getAttendanceDetailsAll(index);
            tableModel.setData(userMusters);
        }catch(Exception e){
            MyUtils.showException("Getting Report", e);
        }

    }
//    /**
//     *
//     */
//    private void exportMusterToExcel() {
//        try{
//            int index = comboMonth.getSelectedIndex();
//            if(userMusters == null){
//                userMusters = getAttendanceDetailsAll(index);
//                tableModel.setData(userMusters);
//            }
//            if(userMusters != null){
//                MyExcel.exportMuster(userMusters, divName, years[index], months[index]);
//            }
//        }catch(Exception e){
//            MyUtils.showException("Export Excel", e);
//        }
//    }
    /**
     * 
     */
//    private void exportReportToExcel(){
//        try {
//            int index = comboMonth.getSelectedIndex();
//            if(userMusters == null){
//                userMusters = getAttendanceDetailsAll(index);
//                tableModel.setData(userMusters);
//            }
//            if(userMusters != null){
//                File file = MyFile.chooseFileForSave("Attendace_"+divName+"_"+mothAndYears[index]+".xls");
//                if(file != null){
//                    MyExcel.exportJTable(tableModel, file);
//                }
//            }
//        } catch (Exception exception) {
//            MyLog.showException(exception);
//        }
//    }
   /**
     *
     * @param mm
     * @param yy
     */
    private UserMuster[] getAttendanceDetailsAll(int index) {
        try{
            MSC msc = MyUtils.getMSC("MusterServlet");
            msc.openOS();
            msc.println("getUserMusterForAll");
            msc.println(monthsMM[index]);
            msc.println(yearsYY[index]);
            msc.closeOS();
            //
            msc.openIS();
            //get no of employees
            String response = msc.readLine();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return null;
            }
            int numEmps = Integer.parseInt(response);
            divName = msc.readLine();
            UserMuster[] musters = new UserMuster[numEmps];
            //get attendance details of all employees
            for(int k=0;k<numEmps;k++){
                musters[k] = new UserMuster();
                //make empty muster
                musters[k].attendance = new Attendance[31];
                for(int i=0;i<31;i++){
                    musters[k].attendance[i] = new Attendance(months[index], years[index]);
                    musters[k].attendance[i].setDay(i+1);//1-31
                }
                //get attendane
                String musterPacket = msc.readLine();
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
                    int j = day-1;
                    musters[k].attendance[j].setData(d);
                }
                //calculate total Leaves/Abscents/AvgHrsWork etc
                musters[k].calculateAggregateValues();
            }
            msc.closeIS();
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
                viewReport();
            }
        };
        p.add(b);
//        //
//        b = new MyButton("Export Report") {
//            @Override
//            public void onClick() {
//                exportReportToExcel();
//            }
//        };
//        p.add(b);
//        //
//        b = new MyButton("Export Muster") {
//            @Override
//            public void onClick() {
//                exportMusterToExcel();
//            }
//        };
//        p.add(b);
      //  p.add(new MyLabel(MyLabel.TYPE_LABEL, " Search Name:", JLabel.RIGHT));
        p.add(searchTextName);
        return p;
    }
}