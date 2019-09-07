/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services;

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
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import lib.gui.MyButton;
import lib.gui.MyPanel;

/**
 *
 * @author pradnya
 */
public class SelfReport extends MyPanel {
//public class SelfReport extends MyApplet {

    MonthlyMuster MM;
    String ReportName;
    MyApplet applet;
    MyPanel MonthPane;

    public SelfReport(MyApplet applet, String ReportName) {
        //public void init() {
        this.ReportName = ReportName;
        this.applet = applet;
        this.setLayout(new BorderLayout());
        MyPanel formPanel = createMonthPanel();
        this.add(formPanel, BorderLayout.CENTER);
        setVisible(true);

    }

    private MyPanel createMonthPanel() {
        //MyPanel formPanel = new MyPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));
        try {
            MonthPane = new MyPanel(new BorderLayout());
            MonthPane.setBorder(BorderFactory.createTitledBorder("Select Month"));
            MonthPane.setPreferredSize(new Dimension(300, 100));

            MM = new MonthlyMuster();
            MonthPane.add(MM.createTopPanel(), BorderLayout.CENTER);
            MM.b.setVisible(false);
            MM.c.setVisible(false);
            MM.p.setBackground(new Color(250,250,250));
            //add buttons panel to login panel
            MyPanel p = new MyPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            p.add(new MyButton("View") {

                @Override
                public void onClick() {
                    switch (ReportName) {

                        case "AbsentReport":
                            ((AbsentApplet) applet).showMonthlyMuster(MM.comboMonth.getSelectedIndex());
                            break;
                        case "OutDoorDuty":
                            ((OutDoorDutyApplet) applet).showMonthlyMuster(MM.comboMonth.getSelectedIndex());
                            break;
                        case "WeekOffApplet":
                            ((WeekOffApplet) applet).showMonthlyMuster(MM.comboMonth.getSelectedIndex());
                            break;
                        case "OverTimeApplet":
                            ((OverTimeApplet) applet).showMonthlyMuster(MM.comboMonth.getSelectedIndex());
                            break;
                        case "OnLeaveReport":
                            ((OnLeaveApplet) applet).showMonthlyMuster(MM.comboMonth.getSelectedIndex());
                            break;
                        case "EarlyReport":
                            ((EarlyApplet) applet).showMonthlyMuster(MM.comboMonth.getSelectedIndex());
                            break;
                        case "LateReport":
                            ((LateApplet) applet).showMonthlyMuster(MM.comboMonth.getSelectedIndex());
                            break;
                        case "SingleSwipe":
                            ((SingleSwipeApplet) applet).showMonthlyMuster(MM.comboMonth.getSelectedIndex());
                            break;
                        case "AttendanceReport":
                            ((AttendanceApplet) applet).showMonthlyMuster(MM.comboMonth.getSelectedIndex());
                            break;
                    }
                }
            });
            p.add(new MyButton("Cancel") {

                @Override
                public void onClick() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
            MonthPane.add(p, BorderLayout.SOUTH);

        } catch (Exception ex) {
            MyUtils.showException("Show Self Applet GUI", ex);
        }
        return MonthPane;
    }
}
