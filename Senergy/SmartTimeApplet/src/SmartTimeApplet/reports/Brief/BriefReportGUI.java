/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.Brief;

import SmartTimeApplet.reports.Common.EmpTableModel;
import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import net.sourceforge.jdatepicker.JDateComponent;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import lib.gui.MyButton;
import lib.gui.MyPanel;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;

/**
 *
 * @author pradnya
 *
 * @Used for Muster,Absent,WeekOff,OverTime,On OutDoorDuty,OverTime
 */
public class BriefReportGUI extends MyPanel implements ActionListener {

    public JDatePicker picker1;
    public MyButton btnSelAll, btnClr, btnView, btnExcel, btnCancel;
    public EmpTableModel tableModel;
    public MyTable table;
    private MyApplet applet;
    private String empCode;
    private String strdate;

    public BriefReportGUI(String empCode) {
        this.empCode = empCode;
        addReportPanel();
    }

    private void addReportPanel() {
        try {
            MyPanel MainPanel = new MyPanel(new BorderLayout());
            MyPanel panMainNorth = new MyPanel(new FlowLayout(), "Select Date");


            MyPanel panFrmDt = new MyPanel(new GridLayout(1, 1), "Date");
            picker1 = JDateComponentFactory.createJDatePicker();
            JComponent guiElement1 = (JComponent) picker1;
            panFrmDt.add(guiElement1);
            panMainNorth.add(panFrmDt);


            MainPanel.add(panMainNorth, BorderLayout.CENTER);


            MyPanel panShowRpt = new MyPanel(new FlowLayout(), "");
            btnView = new MyButton("View", 2) {

                @Override
                public void onClick() {
                    View();
                }
            };
            btnView.addActionListener(this);
            btnView.setActionCommand("ViewReport");

            panShowRpt.add(btnView);

            btnExcel = new MyButton("Export to Excel", 2) {

                @Override
                public void onClick() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
            panShowRpt.add(btnExcel);

            btnCancel = new MyButton("Cancel", 2) {

                @Override
                public void onClick() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
            panShowRpt.add(btnCancel);

            MainPanel.add(panShowRpt, BorderLayout.SOUTH);
            this.setLayout(new BorderLayout());
            this.add(MainPanel, BorderLayout.CENTER);
            setVisible(true);
        } catch (Exception ex) {
            MyUtils.showException("BriefReportGUI", ex);
        }
    }

    private boolean FormFilled() {
        if ((!((JDateComponent) this.picker1).getModel().isSelected())) {
            MyUtils.showMessage("Select DATE");
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (FormFilled()) {
            if (e.getActionCommand().equals("ViewReport")) {
            }
        }
    }

//    private boolean checkHead(String empCode, String Type) throws Exception {
//        MyHTTP myHTTP = MyUtils.createServletConnection("ReportFormServlet");
//        myHTTP.openOS();
//        myHTTP.println("checkHead");
//        myHTTP.println(Type);
//        myHTTP.println(empCode);
//        myHTTP.closeOS();
//        myHTTP.openIS();
//        String result = myHTTP.readLine();
//        myHTTP.closeIS();
//        Depacketizer d = new Depacketizer(result);
//        int rowCount = d.getInt();
//        
//        if (rowCount != 0) {
//            if (result.startsWith("ERROR")) {
//                MyUtils.showException("Database ERROR", new Exception(result));
//            }
//            String[] name = new String[rowCount];
//            String[] empState = new String[rowCount];
//            DeptNm = new String[rowCount];
//            SectionNm = new String[rowCount];
//            
//            for (int i = 0; i < rowCount; i++) {
//                name[i] = d.getString();
//                empState[i] = d.getString();
//                if ("SectionHead".equals(empState[i])) {
//                    DeptNm[i] = d.getString();
//                    cmbDept.addItem(DeptNm[i]);
//                    cmbSec.addItem(name[i]);
//                    SectionNm[i] = name[i];
//                }
//                
//                if ("DeptHead".equals(empState[i])) {
//                    cmbDept.addItem(name[i]);
//                }
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }
    private void View() {
        callServlet(strdate, empCode);
    }

    private void callServlet(String date, String empCode) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ReportFormServlet");
            myHTTP.openOS();
            myHTTP.println("BriefReport");
            myHTTP.println(date);
            myHTTP.println(empCode);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            MyUtils.showMessage("result : " + result);
            if (result.startsWith("ERROR")) {
                MyUtils.showException("Database ERROR", new Exception(result));
            } else {

                //((BriefReportApplet) applet).showReportPanel(Packet, result, deptPacket, rptWise);
            }
        } catch (Exception ex) {
            Logger.getLogger(BriefReportGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void FillCombo(JComboBox myComboBox, String str, String deptNm) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ReportFormServlet");
            myHTTP.openOS();
            myHTTP.println(str);
            myHTTP.println(deptNm);
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
            for (int i = 0; i < rowCount; i++) {
                myComboBox.addItem(d.getString());
            }
        } catch (Exception e) {
            MyUtils.showException("GetComboList", e);
            tableModel.setData(null);
        }
    }
}
