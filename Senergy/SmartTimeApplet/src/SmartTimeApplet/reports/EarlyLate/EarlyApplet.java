/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.EarlyLate;

import SmartTimeApplet.reports.Absent.AbsentApplet;
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
 * @author nbp
 */
public class EarlyApplet extends MyApplet {

    private Container contentPane;
    private MyPanel homePanel;
    public static int usertype;

    @Override
    public void init() {
        try {
            super.init();
            String empCode = "";
            empCode = getParameter("userID");
            usertype = Integer.parseInt(getParameter("usertype"));
            EarlyLateGUI RG = new EarlyLateGUI(this, "EarlyReport", empCode);
            LoadPanel(RG);
        } catch (Exception e) {
            MyUtils.showException("EarlyReportApplet", e);
        }
    }

    public void LoadPanel(MyPanel LoadPanel) throws Exception {
        contentPane = getContentPane();
        homePanel = CreateHomePanel(LoadPanel);
        contentPane.add(homePanel);
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
            Depacketizer d1 = new Depacketizer(Packet);
            boolean Todays = d1.getBool();

            Depacketizer d2 = new Depacketizer(result);
            int rowCount = d2.getInt();

            ReportTableView rtv = new ReportTableView(this, Packet, divNamePacket, "EarlyReport", rowCount, rptType);

            ReportTableModel tablemodel = new ReportTableModel(getTuples(rptType, divNamePacket, Todays));

            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
                return;
            }
            int col = tablemodel.numColumns;
            ReportClass[] Absent = new ReportClass[rowCount];
            //get all doc
            for (int i = 0; i < rowCount; i++) {
                Absent[i] = new ReportClass();
                for (int j = 0; j < col; j++) {
                    Absent[i].returnVals[j] = d2.getString();
                }
            }
            tablemodel.setData(Absent);

            contentPane.removeAll();
            contentPane.add(rtv);
            rtv.setTableModel(tablemodel);
            contentPane.validate();
            contentPane.repaint();
        } catch (Exception ex) {
            MyUtils.showException("Show ReportTableView", ex);
        }
    }

    public TupleClass[] getTuples(String rptType, String divNamePacket, boolean Todays) throws Exception {

        TupleClass[] tpl = null;
        String divNm = "";

        if ("Full".equals(rptType)) {
            Depacketizer division = new Depacketizer(divNamePacket);
            int divSize = division.getInt();
            if (divSize != 0 && divSize == 1) {
                divNm = "SingleDiv";
            }
        }
        if ("Division".equals(rptType) || "SingleDiv".equals(divNm)) {
            if (Todays == true) {
                tpl = new TupleClass[7];
            } else {
                tpl = new TupleClass[8];
            }
            tpl[0] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
            tpl[1] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
            tpl[2] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
            tpl[3] = new TupleClass("Section", 80, MyTableColumn.TYPE_STRING);
            tpl[4] = new TupleClass("Shift", 80, MyTableColumn.TYPE_STRING);
            if (Todays == true) {
                tpl[5] = new TupleClass("LogOut", 80, MyTableColumn.TYPE_STRING);
                tpl[6] = new TupleClass("EarlyBy", 80, MyTableColumn.TYPE_STRING);
            } else {
                tpl[5] = new TupleClass("Date", 80, MyTableColumn.TYPE_DATE_SHORT);
                tpl[6] = new TupleClass("LogOut", 80, MyTableColumn.TYPE_STRING);
                tpl[7] = new TupleClass("EarlyBy", 80, MyTableColumn.TYPE_STRING);
            }
        } else if ("Section".equals(rptType) || ("WorkLoc".equals(rptType))) {
            if (Todays == true) {
                tpl = new TupleClass[6];
            } else {
                tpl = new TupleClass[7];
            }
            tpl[0] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
            tpl[1] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
            tpl[2] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
            tpl[3] = new TupleClass("Shift", 80, MyTableColumn.TYPE_STRING);
            if (Todays == true) {
                tpl[4] = new TupleClass("LogOut", 80, MyTableColumn.TYPE_STRING);
                tpl[5] = new TupleClass("EarlyBy", 80, MyTableColumn.TYPE_STRING);
            } else {
                tpl[4] = new TupleClass("Date", 80, MyTableColumn.TYPE_DATE_SHORT);
                tpl[5] = new TupleClass("LogOut", 80, MyTableColumn.TYPE_STRING);
                tpl[6] = new TupleClass("EarlyBy", 80, MyTableColumn.TYPE_STRING);
            }
        } else if ("Full".equals(rptType)) {
            if (Todays == true) {
                tpl = new TupleClass[8];
            } else {
                tpl = new TupleClass[9];
            }
            tpl[0] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
            tpl[1] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
            tpl[2] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
            tpl[3] = new TupleClass("Division", 80, MyTableColumn.TYPE_STRING);
            tpl[4] = new TupleClass("Section", 80, MyTableColumn.TYPE_STRING);
            tpl[5] = new TupleClass("Shift", 80, MyTableColumn.TYPE_STRING);
            if (Todays == true) {
                tpl[6] = new TupleClass("LogOut", 80, MyTableColumn.TYPE_STRING);
                tpl[7] = new TupleClass("EarlyBy", 80, MyTableColumn.TYPE_STRING);
            } else {
                tpl[6] = new TupleClass("Date", 80, MyTableColumn.TYPE_DATE_SHORT);
                tpl[7] = new TupleClass("LogOut", 80, MyTableColumn.TYPE_STRING);
                tpl[8] = new TupleClass("EarlyBy", 80, MyTableColumn.TYPE_STRING);
            }
        }
        return tpl;
    }

    private void showSelfReport() {
        try {
            contentPane = getContentPane();
            contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));
            contentPane.add(new SelfReport(this, "EarlyReport"), BorderLayout.CENTER);

        } catch (Exception ex) {
            Logger.getLogger(AbsentApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showMonthlyMuster(int index) {
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new MonthlyMuster("EarlyReport", index), BorderLayout.CENTER);
        contentPane.validate();
        contentPane.repaint();
    }
}
