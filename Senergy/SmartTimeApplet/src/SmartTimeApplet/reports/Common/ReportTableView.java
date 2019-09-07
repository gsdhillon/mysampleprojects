/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.Common;

import SmartTimeApplet.reports.Absent.AbsentApplet;
import SmartTimeApplet.reports.Attendance.AttendanceApplet;
import SmartTimeApplet.reports.EarlyLate.EarlyApplet;
import SmartTimeApplet.reports.EarlyLate.LateApplet;
import SmartTimeApplet.reports.OnLeave.OnLeaveApplet;
import SmartTimeApplet.reports.OutDoorDuty.OutDoorDutyApplet;
import SmartTimeApplet.reports.OverTime.OverTimeApplet;
import SmartTimeApplet.reports.SingleSwipe.SingleSwipeApplet;
import SmartTimeApplet.reports.WeekOff.WeekOffApplet;
import lib.session.MyApplet;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import lib.excel.MyExcel;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.table.MyTable;
import lib.gui.table.MyTableModel;
import lib.utils.Depacketizer;
import lib.utils.MyFile;
import lib.utils.MyLog;

/**
 *
 * @author pradnya
 */
public class ReportTableView extends MyPanel {

    private String strHeading;
    private String strFrmDate, strToDate;
    private int iTotalRec;
    private MyTable tlbDetails;
    private MyButton btnPrint, btnCancel;
    public MyTableModel tableModel;
    private MyPanel MainPanel;
    public String ReportHeading;
    public MyTable table;
    private MyPanel panNorth;
    private String division;
    private String divType;
    MyLabel lblDate;
    boolean Todays;

    public void setTableModel(MyTableModel tableModel) {
        this.tableModel = tableModel;
        tlbDetails = new MyTable(tableModel);
        MainPanel.add(tlbDetails.getGUI(), BorderLayout.CENTER);
    }

    public ReportTableView(final MyApplet applet, String Packet, String divNamePacket, final String ReportName, int TotalRecords, String rptType) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width / 2);
        int height = (int) (screenSize.height / 1.3);
        setSize(width, height);
        Depacketizer d = new Depacketizer(Packet);
        strHeading = ReportName;
        if ("EarlyReport".equals(strHeading) || "LateReport".equals(strHeading)) {
            Todays = d.getBool();
            if (Todays == true) {
                strFrmDate = d.getString();
            } else {
                strFrmDate = d.getString();
                strToDate = d.getString();
            }
        } else if ("AbsentReport".equals(strHeading)) {
            strFrmDate = d.getString();
        } else {
            strFrmDate = d.getString();
            strToDate = d.getString();
        }

        iTotalRec = TotalRecords;
        String strdiv = "";
        String strSec = "";
        String strWL = "";
        if (!("Full").equals(rptType)) {
            division = d.getString();
            if (!"AbsentReport".equals(strHeading)) {
                strSec = d.getString();
                strWL = d.getString();
            }
        } else {
            Depacketizer div = new Depacketizer(divNamePacket);
            int divSize = div.getInt();
            if (divSize > 0) {
                for (int i = 0; i < divSize; i++) {
                    if (i == 0) {
                        division = div.getString();
                        divType = "SingleDiv";
                    } else {
                        divType = "ManyDiv";
                    }
                }
            }
        }

        MainPanel = new MyPanel(new BorderLayout());
        if ("SingleDiv".equals(divType) || (!"Full".equals(rptType))) {
            panNorth = new MyPanel(new GridLayout(3, 1));
        } else {
            panNorth = new MyPanel(new GridLayout(2, 1));
        }

        MyPanel panHeading = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        MyLabel lblHeading = new MyLabel(3, strHeading);
        panHeading.add(lblHeading);
        panNorth.add(panHeading);

        MyPanel panDateRec = new MyPanel(new GridLayout(1, 2));
        MyPanel panDate = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        if ((strFrmDate != null) & (!"".equals(strFrmDate))) {
            strFrmDate = myFormat.format(fromUser.parse(strFrmDate));
        }
        if (!"AbsentReport".equals(strHeading)) {
            if ((strToDate != null) & (!"".equals(strToDate))) {
                strToDate = myFormat.format(fromUser.parse(strToDate));
            }
        }
        if ("EarlyReport".equals(strHeading) || "LateReport".equals(strHeading)) {
            if (Todays == true) {
                lblDate = new MyLabel(1, "Date: " + strFrmDate);
            } else {
                lblDate = new MyLabel(1, "From: " + strFrmDate + " To: " + strToDate);
            }
        } else if ("AbsentReport".equals(strHeading)) {
            lblDate = new MyLabel(1, "Date: " + strFrmDate);
        } else {
            lblDate = new MyLabel(1, "From: " + strFrmDate + " To: " + strToDate);
        }


        panDate.add(lblDate);
        panDateRec.add(panDate);

        MyPanel panTotal = new MyPanel(new FlowLayout(FlowLayout.RIGHT));
        MyLabel lblTotal = new MyLabel(1, "Total Records: " + iTotalRec);
        panTotal.add(lblTotal);
        panDateRec.add(panTotal);
        panNorth.add(panDateRec);

        MainPanel = new MyPanel(new BorderLayout());
        MyPanel panDivSec;
        MyPanel panSection;
        if ("SingleDiv".equals(divType) || (!"Full".equals(rptType))) {
            if (!"".equals(strWL)) {
                panDivSec = new MyPanel(new GridLayout(1, 3));
            } else {
                panDivSec = new MyPanel(new GridLayout(1, 2));
            }
            MyPanel panDiv = new MyPanel(new FlowLayout(FlowLayout.LEFT));
            MyLabel lblDiv = new MyLabel(1, "Division : " + division);
            panDiv.add(lblDiv);
            panDivSec.add(panDiv);

            if (!"".equals(strSec)) {
                if (!"".equals(strWL)) {
                    panSection = new MyPanel(new FlowLayout(FlowLayout.CENTER));
                } else {
                    panSection = new MyPanel(new FlowLayout(FlowLayout.RIGHT));
                }
                MyLabel lblSection = new MyLabel(1, "Section : " + strSec);
                panSection.add(lblSection);
                panDivSec.add(panSection);
            }
            if (!"".equals(strWL)) {
                MyPanel panWL = new MyPanel(new FlowLayout(FlowLayout.RIGHT));
                MyLabel lblWL = new MyLabel(1, "Work Loc : " + strWL);
                panWL.add(lblWL);
                panDivSec.add(panWL);
            }
            panNorth.add(panDivSec);
        }


        MainPanel.add(panNorth, BorderLayout.NORTH);

        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        btnPrint = new MyButton("Export To Excel", 2) {

            @Override
            public void onClick() {
                exportReportToExcel();
            }
        };
        panButtons.add(btnPrint);

        btnCancel = new MyButton("Cancel", 0) {

            @Override
            public void onClick() {
                switch (ReportName) {
                    case "AbsentReport":
                        ((AbsentApplet) applet).showHomePanel();
                        break;
                    case "OutDoorDuty":
                        ((OutDoorDutyApplet) applet).showHomePanel();
                        break;
                    case "WeekOffApplet":
                        ((WeekOffApplet) applet).showHomePanel();
                        break;
                    case "OverTimeApplet":
                        ((OverTimeApplet) applet).showHomePanel();
                        break;
                    case "OnLeaveReport":
                        ((OnLeaveApplet) applet).showHomePanel();
                        break;
                    case "EarlyReport":
                        ((EarlyApplet) applet).showHomePanel();
                        break;
                    case "LateReport":
                        ((LateApplet) applet).showHomePanel();
                        break;
                    case "InSwipeMissing":
                        ((SingleSwipeApplet) applet).showHomePanel();
                        break;
                    case "OutSwipeMissing":
                        ((SingleSwipeApplet) applet).showHomePanel();
                        break;
                    case "AttendanceReport":
                        ((AttendanceApplet) applet).showHomePanel();
                        break;
                }
            }
        };
        panButtons.add(btnCancel);
        MainPanel.add(panButtons, BorderLayout.SOUTH);
        this.setLayout(new BorderLayout());
        this.add(MainPanel, BorderLayout.CENTER);

        setVisible(true);

    }

    private void exportReportToExcel() {
        try {
            if (tableModel != null) {
                File file = MyFile.chooseFileForSave("Report_" + strHeading + ".xls");
                if (file != null) {
                    MyExcel.exportJTable(tableModel, file, strHeading, division, 2);//page orientation 1=portrait 2=landscape
                }
            }
        } catch (Exception exception) {
            MyLog.showException(exception);
        }
    }
}
