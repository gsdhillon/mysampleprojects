/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.Absent;

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
import lib.gui.table.MyTable;
import lib.gui.table.MyTableColumn;
import lib.utils.Depacketizer;

/**
 *
 * @author pradnya
 */
public class AbsentApplet extends MyApplet {

    private Container contentPane;
    private MyPanel homePanel;
//    private static String rptWise;
//    private MyTable table;

    @Override
    public void init() {
        try {
            super.init();
            String empCode = "";
            empCode = getParameter("userID");
            AbsentReportGUI RG = new AbsentReportGUI(this, "AbsentReport", empCode);
            LoadPanel(RG);
        } catch (Exception e) {
            MyUtils.showException("AbsentApplet", e);
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

    public void showReportPanel(String Packet, String result, String divNamePacket, String rptWise, MyTable table) {
        try {
//            AbsentApplet.rptWise = rptWise;
//            this.table = table;
            Depacketizer d = new Depacketizer(result);
            int rowCount = d.getInt();
            ReportTableView rtv = new ReportTableView(this, Packet, divNamePacket, "AbsentReport", rowCount, rptWise);
            ReportTableModel tablemodel = new ReportTableModel(getTuples(rptWise, divNamePacket));

            if (result.startsWith("ERROR")) {
                MyUtils.showMessage(result);
                return;
            }
            int col = tablemodel.numColumns;
            ReportClass[] Absent = new ReportClass[rowCount];
            //get all doc
            for (int i = 0; i < rowCount; i++) {
                String showPacket = d.getString();
                Depacketizer dp = new Depacketizer(showPacket);
                Absent[i] = new ReportClass();
                for (int j = 0; j < col; j++) {
                    Absent[i].returnVals[j] = dp.getString();
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

    public TupleClass[] getTuples(String rptWise, String divNamePacket) throws Exception {

        TupleClass[] tpl = null;
        String divNm = "";

        if ("Full".equals(rptWise)) {
            Depacketizer div = new Depacketizer(divNamePacket);
            int divSize = div.getInt();
            if (divSize != 0 && divSize == 1) {
                divNm = "SingleDiv";
            }
        }
        tpl = new TupleClass[5];
        tpl[0] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
        tpl[1] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
        tpl[2] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
        tpl[3] = new TupleClass("Section", 80, MyTableColumn.TYPE_STRING);
        tpl[4] = new TupleClass("Shift", 80, MyTableColumn.TYPE_STRING);
        return tpl;
    }

    private void showSelfReport() {
        try {
            contentPane = getContentPane();
            contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));
            contentPane.add(new SelfReport(this, "AbsentReport"), BorderLayout.CENTER);

        } catch (Exception ex) {
            Logger.getLogger(AbsentApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showMonthlyMuster(int index) {
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new MonthlyMuster("AbsentReport", index), BorderLayout.CENTER);
        contentPane.validate();
        contentPane.repaint();
    }
}
