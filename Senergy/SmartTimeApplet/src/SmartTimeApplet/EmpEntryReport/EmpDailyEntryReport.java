/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.EmpEntryReport;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponent;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 *
 * @author pradnya`
 */
public class EmpDailyEntryReport extends MyApplet {

    private EmpEntryTableModel tableModel;
    public JDatePicker picker1;
    String entryreportType = "";
    String empcode = "";
    Container con;

    @Override
    public void init() {
        try {
            super.init();
            entryreportType = getParameter("ReportType");
            empcode = getParameter("empcode");
            con = getContentPane();
            con.setLayout(new BorderLayout());
            con.add(CreatePanel(entryreportType, empcode));

        } catch (Exception e) {
            MyUtils.showException("EmpDailyEntryReport", e);
        }
    }

    public void setReportType(String type) {
        entryreportType = type;
    }

    public MyPanel CreatePanel(String entryreportType, String empcode) {
        MyPanel panMain = new MyPanel(new BorderLayout());
        try {
            con = getContentPane();
            this.entryreportType = entryreportType;
            this.empcode = empcode;
            String entrydate;
            tableModel = new EmpEntryTableModel();

            MyPanel p = new MyPanel(new FlowLayout(FlowLayout.LEFT));
            MyPanel p1 = new MyPanel(new BorderLayout(5, 2));

            MyLabel lblseldate = new MyLabel(1, "Select date");
            p1.add(lblseldate, BorderLayout.WEST);

            picker1 = JDateComponentFactory.createJDatePicker();
            JComponent guiElement1 = (JComponent) picker1;

            MyUtils.setDate(guiElement1, getTodayDate());
            p1.add(guiElement1, BorderLayout.CENTER);
            p.add(p1);

            MyButton btnshow = new MyButton("Show", 1) {
                @Override
                public void onClick() {
                    String entrydate = getDate(picker1);
                    getEmpEntryDetails(entrydate);
                }
            };
            p.add(btnshow);

            entrydate = getDate(picker1);
            getEmpEntryDetails(entrydate);
            panMain.add(p, BorderLayout.NORTH);
            //create Documents table
            MyTable table = new MyTable(tableModel);

            panMain.add(table.getGUI(), BorderLayout.CENTER);
//            if ("Team".equals(entryreportType)) {
//                MyPanel panSouth = new MyPanel(new FlowLayout(FlowLayout.CENTER));
//                MyButton btnCancel = new MyButton("CANCEL", 2) {
//
//                    @Override
//                    public void onClick() {
//                        con.removeAll();
//                        UserMaster um = new UserMaster();
//                        con.add(um.createPanel());
//                        con.validate();
//                        con.repaint();
//                    }
//                };
//                panSouth.add(btnCancel);
//                panMain.add(panSouth, BorderLayout.SOUTH);
//            }
        } catch (Exception ex) {
            Logger.getLogger(EmpDailyEntryReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return panMain;
    }
    /**
     *
     */
    private void getEmpEntryDetails(String entrydate) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("EmpEntryServlet");
            myHTTP.openOS();
            myHTTP.println("getEmpEntryDetails");
            myHTTP.println(entrydate);//getting date into string and sending to the servlet
            myHTTP.println(empcode);
            myHTTP.closeOS();

            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            Depacketizer d = new Depacketizer(response);
            int rowCount = d.getInt();
            EmpEntryClass[] EmpEntry = new EmpEntryClass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {
                EmpEntry[i] = new EmpEntryClass();
                EmpEntry[i].readerloc = d.getString();
                EmpEntry[i].readertype = d.getString();
                EmpEntry[i].time = d.getString();
            }
            tableModel.setData(EmpEntry);
        } catch (Exception e) {
            MyUtils.showException("GetUserList", e);
            tableModel.setData(null);
        }
    }
    private String getDate(JDatePicker picker) {
        String date1 = "";
        String caldate = "";
        try {
            DateModel model = ((JDateComponent) picker).getModel();
            date1 = model.getYear() + "-" + (model.getMonth() + 1) + "-" + (model.getDay());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = dateFormat.parse(date1);
            caldate = dateFormat.format(dt);


        } catch (ParseException ex) {
            Logger.getLogger(EmpDailyEntryReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return caldate;
    }

    private String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String todaydate = dateFormat.format(cal.getTime());
        int m1;
        String date1[] = todaydate.split("-");
        todaydate = date1[0] + "-" + date1[1] + "-" + date1[2];
        return todaydate;
    }
}
