/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.Brief;

import SmartTimeApplet.reports.Common.TupleClass;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import lib.gui.MyPanel;
import lib.gui.table.MyTableColumn;
import lib.utils.Depacketizer;

/**
 *
 * @author nbp
 */
public class BriefReportApplet extends MyApplet{
    
       private Container contentPane;
    private MyPanel homePanel;

    @Override
    public void init() {
        try {
            super.init();
            String empCode = "";
            empCode = getParameter("userID");
            LoadPanel(new BriefReportGUI(empCode));

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

    public void showReportPanel(String result) {
//        try {

//            Depacketizer d = new Depacketizer(result);
//            int rowCount = d.getInt();
//
//            BriefReportView brv= new BriefReportView();
//           // ReportTableModel tablemodel = new ReportTableModel(getTuples());
//
//            if (result.startsWith("ERROR")) {
//                MyUtils.showMessage(result);
//                return;
//            }
//            int col = tablemodel.numColumns;
//            ReportClass[] Absent = new ReportClass[rowCount];
//            //get all doc
//            for (int i = 0; i < rowCount; i++) {
//                Absent[i] = new ReportClass();
//                for (int j = 0; j < col; j++) {
//                    Absent[i].returnVals[j] = d.getString();
//                }
//            }
//            tablemodel.setData(Absent);
//
//            contentPane.removeAll();
//            contentPane.add(rtv);
//            rtv.setTableModel(tablemodel);
//            contentPane.validate();
//            contentPane.repaint();
//        } catch (Exception ex) {
//            MyUtils.showException("Show ReportTableView", ex);
//        }
    }

    public TupleClass[] getTuples(String rptType, String deptNamePacket) throws Exception {

        TupleClass[] tpl = null;
        String deptNm = "";

        if ("Full".equals(rptType)) {
            Depacketizer dept = new Depacketizer(deptNamePacket);
            int deptSize = dept.getInt();
            if (deptSize != 0 && deptSize == 1) {
                deptNm = "SingleDept";
            }
        }

        if ("Department".equals(rptType) || "SingleDept".equals(deptNm)) {
            tpl = new TupleClass[8];
            tpl[0] = new TupleClass("Date", 80, MyTableColumn.TYPE_DATE_SHORT);
            tpl[1] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
            tpl[2] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
            tpl[3] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
            tpl[4] = new TupleClass("Section", 80, MyTableColumn.TYPE_STRING);
            tpl[5] = new TupleClass("Shift", 80, MyTableColumn.TYPE_STRING);
            tpl[6] = new TupleClass("Remark", 80, MyTableColumn.TYPE_STRING);

        } else if ("Section".equals(rptType)) {
            tpl = new TupleClass[6];
            tpl[0] = new TupleClass("Date", 80, MyTableColumn.TYPE_DATE_SHORT);
            tpl[1] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
            tpl[2] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
            tpl[3] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
            tpl[4] = new TupleClass("Shift", 80, MyTableColumn.TYPE_STRING);
            tpl[5] = new TupleClass("Remark", 80, MyTableColumn.TYPE_STRING);
        } else if ("WorkLoc".equals(rptType)) {
            tpl = new TupleClass[6];
            tpl[0] = new TupleClass("Date", 80, MyTableColumn.TYPE_DATE_SHORT);
            tpl[1] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
            tpl[2] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
            tpl[3] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
            tpl[4] = new TupleClass("Shift", 80, MyTableColumn.TYPE_STRING);
            tpl[5] = new TupleClass("Remark", 80, MyTableColumn.TYPE_STRING);
        } else if ("Full".equals(rptType)) {
            tpl = new TupleClass[8];
            tpl[0] = new TupleClass("Date", 80, MyTableColumn.TYPE_DATE_SHORT);
            tpl[1] = new TupleClass("CCNo", 80, MyTableColumn.TYPE_STRING);
            tpl[2] = new TupleClass("EmpNo", 80, MyTableColumn.TYPE_STRING);
            tpl[3] = new TupleClass("EmpName", 80, MyTableColumn.TYPE_STRING);
            tpl[4] = new TupleClass("Department", 80, MyTableColumn.TYPE_STRING);
            tpl[5] = new TupleClass("Section", 80, MyTableColumn.TYPE_STRING);
            tpl[6] = new TupleClass("Shift", 80, MyTableColumn.TYPE_STRING);
            tpl[7] = new TupleClass("Remark", 80, MyTableColumn.TYPE_STRING);
        }
        return tpl;
    } 
    
    
    
}
