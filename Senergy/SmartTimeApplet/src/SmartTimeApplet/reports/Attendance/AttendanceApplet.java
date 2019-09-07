package SmartTimeApplet.reports.Attendance;

import SmartTimeApplet.reports.Common.ReportClass;
import SmartTimeApplet.reports.Common.ReportTableModel;
import SmartTimeApplet.reports.Common.ReportTableView;
import SmartTimeApplet.reports.Common.TupleClass;
import SmartTimeApplet.services.MonthlyMuster;
import SmartTimeApplet.services.SelfReport;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.gui.MyPanel;
import lib.gui.table.MyTableColumn;
import lib.utils.Depacketizer;

/**
 *
 * @author pradnya
 */
public class AttendanceApplet extends MyApplet {
    
    public Container contentPane;
    private MyPanel homePanel;
    
    @Override
    public void init() {
        try {
            super.init();
            String empCode = "";
            empCode = getParameter("userID");
            AttendanceGUI RG = new AttendanceGUI(this, "AttendanceReport", empCode);
                LoadPanel(RG);
            
        } catch (Exception e) {
            MyUtils.showException("AttendanceApplet", e);
        }
    }
    
    public void LoadPanel(MyPanel LoadPanel) throws Exception {
        try {
            contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());
            homePanel = CreateHomePanel(LoadPanel);
            contentPane.add(homePanel, BorderLayout.CENTER);
        } catch (Exception e) {
            MyUtils.showException("LoadPanelAttendanceApplet", e);
        }
        
    }
    
    public MyPanel CreateHomePanel(MyPanel LoadPanel) throws Exception {
        MyPanel AR = new MyPanel();
        AR.setLayout(new BorderLayout());
        AR.add(LoadPanel, BorderLayout.CENTER);
        return AR;
    }
    
    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        contentPane.validate();
        contentPane.repaint();
    }
    
    public void showReportPanel(String Packet, String result, String divNamePacket, String rptType) {
        try {
            
            Depacketizer d = new Depacketizer(result);
            int rowCount = d.getInt();
            ReportTableView rtv = new ReportTableView(this, Packet, divNamePacket, "AttendanceReport", rowCount, rptType);
            ReportTableModel tablemodel = new ReportTableModel(getTuples(rptType, divNamePacket));
            
            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
                return;
            }
            int col = tablemodel.numColumns;
            ReportClass[] Attendance = new ReportClass[rowCount];
            //get all doc
            for (int i = 0; i < rowCount; i++) {
                Attendance[i] = new ReportClass();
                for (int j = 0; j < col; j++) {
                    Attendance[i].returnVals[j] = d.getString();
                }
            }
            tablemodel.setData(Attendance);
            
            contentPane.removeAll();
            contentPane.add(rtv);
            rtv.setTableModel(tablemodel);
            contentPane.validate();
            contentPane.repaint();
        } catch (Exception ex) {
            MyUtils.showException("Show ReportTableView", ex);
        }
    }
    
    public TupleClass[] getTuples(String rptType, String divNamePacket) throws Exception {
        
        TupleClass[] tpl = null;
        String divNm = "";
        
        if ("Full".equals(rptType)) {
            Depacketizer div = new Depacketizer(divNamePacket);
            int divSize = div.getInt();
            if (divSize != 0 && divSize == 1) {
                divNm = "SingleDiv";
            }
        }
        if ("Division".equals(rptType) || "SingleDiv".equals(divNm)) {
            tpl = new TupleClass[12];
            tpl[0] = new TupleClass("Section", 80, MyTableColumn.TYPE_STRING);
            tpl[1] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
            tpl[2] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
            tpl[3] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
            tpl[4] = new TupleClass("Date", 80, MyTableColumn.TYPE_DATE_SHORT);
            tpl[5] = new TupleClass("Login", 80, MyTableColumn.TYPE_STRING);
            tpl[6] = new TupleClass("Logout", 80, MyTableColumn.TYPE_STRING);
            tpl[7] = new TupleClass("LateBy", 80, MyTableColumn.TYPE_STRING);
            tpl[8] = new TupleClass("EarlyBy", 80, MyTableColumn.TYPE_STRING);
            tpl[9] = new TupleClass("TotalWork", 80, MyTableColumn.TYPE_STRING);
            tpl[10] = new TupleClass("ExtraWork", 80, MyTableColumn.TYPE_STRING);
            tpl[11] = new TupleClass("Status", 80, MyTableColumn.TYPE_STRING);
            
        } else if ("Section".equals(rptType)) {
            tpl = new TupleClass[11];
            tpl[0] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
            tpl[1] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
            tpl[2] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
            tpl[3] = new TupleClass("Date", 80, MyTableColumn.TYPE_DATE_SHORT);
            tpl[4] = new TupleClass("Login", 80, MyTableColumn.TYPE_STRING);
            tpl[5] = new TupleClass("Logout", 80, MyTableColumn.TYPE_STRING);
            tpl[6] = new TupleClass("LateBy", 80, MyTableColumn.TYPE_STRING);
            tpl[7] = new TupleClass("EarlyBy", 80, MyTableColumn.TYPE_STRING);
            tpl[8] = new TupleClass("TotalWork", 80, MyTableColumn.TYPE_STRING);
            tpl[9] = new TupleClass("ExtraWork", 80, MyTableColumn.TYPE_STRING);
            tpl[10] = new TupleClass("Status", 80, MyTableColumn.TYPE_STRING);
        } else if ("WorkLoc".equals(rptType)) {
            tpl = new TupleClass[6];
            tpl[0] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
            tpl[1] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
            tpl[2] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
            tpl[3] = new TupleClass("Date", 80, MyTableColumn.TYPE_DATE_SHORT);
            tpl[4] = new TupleClass("Login", 80, MyTableColumn.TYPE_STRING);
            tpl[5] = new TupleClass("Logout", 80, MyTableColumn.TYPE_STRING);
            tpl[6] = new TupleClass("LateBy", 80, MyTableColumn.TYPE_STRING);
            tpl[7] = new TupleClass("EarlyBy", 80, MyTableColumn.TYPE_STRING);
            tpl[8] = new TupleClass("TotalWork", 80, MyTableColumn.TYPE_STRING);
            tpl[9] = new TupleClass("ExtraWork", 80, MyTableColumn.TYPE_STRING);
            tpl[10] = new TupleClass("Status", 80, MyTableColumn.TYPE_STRING);
        } else if ("Full".equals(rptType)) {
            tpl = new TupleClass[13];
            tpl[0] = new TupleClass("Division", 80, MyTableColumn.TYPE_STRING);
            tpl[1] = new TupleClass("Section", 80, MyTableColumn.TYPE_STRING);
            tpl[2] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
            tpl[3] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
            tpl[4] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
            tpl[5] = new TupleClass("Date", 80, MyTableColumn.TYPE_DATE_SHORT);
            tpl[6] = new TupleClass("Login", 80, MyTableColumn.TYPE_STRING);
            tpl[7] = new TupleClass("Logout", 80, MyTableColumn.TYPE_STRING);
            tpl[8] = new TupleClass("LateBy", 80, MyTableColumn.TYPE_STRING);
            tpl[9] = new TupleClass("EarlyBy", 80, MyTableColumn.TYPE_STRING);
            tpl[10] = new TupleClass("TotalWork", 80, MyTableColumn.TYPE_STRING);
            tpl[11] = new TupleClass("ExtraWork", 80, MyTableColumn.TYPE_STRING);
            tpl[12] = new TupleClass("Status", 80, MyTableColumn.TYPE_STRING);
        }
        return tpl;
    }
    
    private void showSelfReport() {
        try {
            contentPane = getContentPane();
            contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));
            contentPane.add(new SelfReport(this, "AttendanceReport"), BorderLayout.CENTER);
        } catch (Exception ex) {
            Logger.getLogger(AttendanceApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showMonthlyMuster(int index) {
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new MonthlyMuster("AttendanceReport", index), BorderLayout.CENTER);
        contentPane.validate();
        contentPane.repaint();
    }
}
