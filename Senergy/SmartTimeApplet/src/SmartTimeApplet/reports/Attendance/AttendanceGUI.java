/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.Attendance;

import SmartTimeApplet.reports.Common.CreatePacket;
import SmartTimeApplet.reports.Common.EmpClass;
import SmartTimeApplet.reports.Common.EmpTableModel;
import SmartTimeApplet.services.MonthlyMuster;
import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import lib.Utility.SimpleUtilities;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 *
 * @author pradnya
 */
public class AttendanceGUI extends MyPanel implements ActionListener {

    public JDatePicker picker1, picker2;
    public JRadioButton rbSel, rbFull, rbDiv, rbSec, rbWL;//rbSelf, rbTeam,
    public JComboBox cmbDiv, cmbSec, cmbWL;
    public MyButton btnSelAll, btnClr, btnView, btnExcel, btnCancel;
    public EmpTableModel tableModel;
    public MyTable table;
    private MyApplet applet;
    private String ReportName;
    private String empCode;
    private String[] DivNm;
    private String[] SectionNm;
    private String[] DivName;
    private boolean bCheckDivHead;
    private boolean bCheckSecHead;
    public JRadioButton rbPresent, rbAbsent, rbBoth;
    private MonthlyMuster MM;
    MyPanel panMainNorth1, panMainNorth2, MainPanel, panEmpSearch, panCentr;

    public AttendanceGUI(MyApplet applet, String ReportName, String empCode) {
        this.applet = applet;
        this.ReportName = ReportName;
        this.empCode = empCode;

        addMusterPanel();
    }

    private void addMusterPanel() {
        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) (screenSize.width / 3);
            int height = (int) (screenSize.height / 1.3);
            setSize(width, height);

            MainPanel = new MyPanel(new BorderLayout());

            panMainNorth1 = new MyPanel(new GridLayout(1, 4, 10, 5), "Select Date");
            MyLabel lblfill1 = new MyLabel();
            panMainNorth1.add(lblfill1);

            MyPanel panFrmDt = new MyPanel(new GridLayout(1, 1), "From");
            picker1 = JDateComponentFactory.createJDatePicker();
            JComponent guiElement1 = (JComponent) picker1;
            panFrmDt.add(guiElement1);
            panMainNorth1.add(panFrmDt);

            MyPanel panToDt = new MyPanel(new GridLayout(1, 1), "To");
            picker2 = JDateComponentFactory.createJDatePicker();
            JComponent guiElement2 = (JComponent) picker2;
            panToDt.add(guiElement2);
            panMainNorth1.add(panToDt);

            MyLabel lblfill2 = new MyLabel();
            panMainNorth1.add(lblfill2);

            MainPanel.add(panMainNorth1, BorderLayout.NORTH);
            MyPanel panMainCenter = new MyPanel(new BorderLayout());
            MyPanel panGrid = new MyPanel(new GridLayout(2, 1));

            MyPanel panAttendance = new MyPanel(new GridLayout(1, 3), "Choose Report For");
            ButtonGroup rbbuttonGroup2 = new ButtonGroup();

            MyPanel panPresent = new MyPanel(new FlowLayout(FlowLayout.CENTER));
            rbPresent = new JRadioButton("Only Present", true);
            rbPresent.setBackground(new Color(250, 250, 250));
            rbPresent.addActionListener(this);
            rbbuttonGroup2.add(rbPresent);
            panPresent.add(rbPresent);
            panAttendance.add(panPresent);

            MyPanel panAbsent = new MyPanel(new FlowLayout(FlowLayout.CENTER));
            rbAbsent = new JRadioButton("Only Absent");
            rbAbsent.setBackground(new Color(250, 250, 250));
            rbbuttonGroup2.add(rbAbsent);
            panAbsent.add(rbAbsent);
            panAttendance.add(panAbsent);

            MyPanel panBoth = new MyPanel(new FlowLayout(FlowLayout.CENTER));
            rbBoth = new JRadioButton("Both");
            rbBoth.setBackground(new Color(250, 250, 250));
            rbbuttonGroup2.add(rbBoth);
            panBoth.add(rbBoth);
            panAttendance.add(panBoth);

            panGrid.add(panAttendance);

            MyPanel panRptType = new MyPanel(new GridLayout(1, 2), "Choose Report Type");
            ButtonGroup rbbuttonGroup3 = new ButtonGroup();

            MyPanel panSel = new MyPanel(new FlowLayout(FlowLayout.CENTER));
            rbSel = new JRadioButton("Selected Report");
            rbSel.setBackground(new Color(250, 250, 250));
            rbSel.addActionListener(this);
            rbSel.setActionCommand("selectedRpt");
            rbbuttonGroup3.add(rbSel);
            panSel.add(rbSel);
            panRptType.add(panSel);

            MyPanel panFull = new MyPanel(new FlowLayout(FlowLayout.CENTER));
            rbFull = new JRadioButton("Full Report", true);
            rbFull.setBackground(new Color(250, 250, 250));
            rbFull.addActionListener(this);
            rbFull.setActionCommand("FullReport");
            rbbuttonGroup3.add(rbFull);
            panFull.add(rbFull);
            panRptType.add(panFull);
            panGrid.add(panRptType);
            panMainCenter.add(panGrid, BorderLayout.NORTH);

            panCentr = new MyPanel(new BorderLayout());
            MyPanel panSearch = new MyPanel(new GridLayout(2, 3, 6, 10), "Search By");

            ButtonGroup rbbuttonGroup4 = new ButtonGroup();
            rbDiv = new JRadioButton("Division", true);
            rbDiv.setBackground(new Color(250, 250, 250));
            rbDiv.addActionListener(this);
            rbDiv.setActionCommand("SearchDivWise");
            rbbuttonGroup4.add(rbDiv);
            panSearch.add(rbDiv);

            rbSec = new JRadioButton("Section");
            rbSec.setBackground(new Color(250, 250, 250));
            rbSec.addActionListener(this);
            rbSec.setActionCommand("SearchSectionWise");
            rbbuttonGroup4.add(rbSec);
            panSearch.add(rbSec);

            rbWL = new JRadioButton("Work Location");
            rbWL.setBackground(new Color(250, 250, 250));
            rbWL.addActionListener(this);
            rbWL.setActionCommand("SearchWorkLocWise");
            rbbuttonGroup4.add(rbWL);
            panSearch.add(rbWL);

            cmbDiv = new JComboBox();
            cmbDiv.addItem("Select");
            cmbDiv.addActionListener(this);
            cmbDiv.setActionCommand("selectDiv");
            panSearch.add(cmbDiv);

            cmbSec = new JComboBox();
            cmbSec.addItem("Select");
            cmbSec.addActionListener(this);
            cmbSec.setActionCommand("selectSection");
            panSearch.add(cmbSec);

            cmbWL = new JComboBox();
            cmbWL.addItem("Select");
            cmbWL.addActionListener(this);
            cmbWL.setActionCommand("SelectWorkLoc");
            FillCombo(cmbWL, "WorkLocation", "");
            panSearch.add(cmbWL);

            panCentr.add(panSearch, BorderLayout.NORTH);
            panMainCenter.add(panCentr, BorderLayout.CENTER);


            panEmpSearch = new MyPanel(new BorderLayout(), "Select Employee");
            tableModel = new EmpTableModel();
            table = new MyTable(tableModel);

            MyPanel panShowRpt = new MyPanel(new FlowLayout(), "");
            btnView = new MyButton("View", 2) {

                @Override
                public void onClick() {
                    View();
                }
            };
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

            checkHeadDivisionSection();//if user is Div head or section head

            panMainCenter.add(panCentr, BorderLayout.CENTER);
            panMainCenter.add(panShowRpt, BorderLayout.SOUTH);

            MainPanel.add(panMainCenter, BorderLayout.CENTER);
            this.setLayout(new BorderLayout());
            this.add(MainPanel, BorderLayout.CENTER);
            setVisible(true);
        } catch (Exception ex) {
            MyUtils.showException("MusterGUI", ex);
        }
    }

    private boolean FormFilled() {
//        if ((!bCheckDivHead == false && !bCheckSecHead == false)) {
        if (rbSel.isSelected() == true) {
            if ("".equals(getSelectedEmp())) {
                MyUtils.showMessage("No Employee Selected");
                return false;
            }
        }
//        }
        return true;
    }

    //for selected employee
    //collecting selected employee
    private String getSelectedEmp() {
        String Employee = "";
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.isRowSelected(i)) {
                Employee = Employee + " " + table.getValueAt(i, 0);
            }
        }
        return Employee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "FullReport":
                DisableControls();
                break;
            case "selectedRpt":
                EnableControls();
                break;
//            case "Self":
//
//                swapPanels(panMainNorth1, panMainNorth2);
//                MM.b.setVisible(false);
//                MM.c.setVisible(false);
//
//                rbSel.setEnabled(false);
//                rbFull.setEnabled(false);
//                DisableControls();
//                break;
//            case "Team":
//                swapPanels(panMainNorth2, panMainNorth1);
//
//                rbSel.setEnabled(true);
//                rbFull.setEnabled(true);
//                if (rbFull.isSelected() == false) {
//                    EnableControls();
//                }
//                break;
            case "selectDiv":
                String div1 = cmbDiv.getSelectedItem().toString();
                cmbSec.removeAllItems();
                cmbSec.addItem("Select");
                FillCombo(cmbSec, "Section", div1);
                getEmpList(SearchEmpBy());
                break;
            case "selectSection":
                getEmpList(SearchEmpBy());
                break;
            case "SelectWorkLoc":
                getEmpList(SearchEmpBy());
                break;
            case "SearchDivWise":
                cmbSec.setEnabled(false);
                cmbWL.setEnabled(false);
                cmbDiv.setEnabled(true);
                if (!"Select".equals(cmbDiv.getSelectedItem().toString())) {
                    getEmpList(SearchEmpBy());
                }
                break;
            case "SearchSectionWise":
                cmbSec.setEnabled(true);
                cmbWL.setEnabled(false);
                cmbDiv.setEnabled(false);
                if (!"Select".equals(cmbSec.getSelectedItem().toString())) {
                    getEmpList(SearchEmpBy());
                }
                break;
            case "SearchWorkLocWise":
                cmbSec.setEnabled(false);
                cmbDiv.setEnabled(false);
                cmbWL.setEnabled(true);
                if (!"Select".equals(cmbWL.getSelectedItem().toString())) {
                    getEmpList(SearchEmpBy());
                }
                break;
        }
    }

    public boolean checkHead(String empCode, String Type) throws Exception {
        MyHTTP myHTTP = MyUtils.createServletConnection("ReportFormServlet");
        myHTTP.openOS();
        myHTTP.println("checkHead");
        myHTTP.println(Type);
        myHTTP.println(empCode);
        myHTTP.closeOS();
        myHTTP.openIS();
        String result = myHTTP.readLine();
        myHTTP.closeIS();
        Depacketizer d = new Depacketizer(result);
        int rowCount = d.getInt();

        if (rowCount != 0) {
            if (result.startsWith("ERROR")) {
                MyUtils.showException("Database ERROR", new Exception(result));
            }
            String[] name = new String[rowCount];
//            String[] empState = new String[rowCount];
            String empState = "";
            DivNm = new String[rowCount];
            SectionNm = new String[rowCount];
            empState = d.getString();
            for (int i = 0; i < rowCount; i++) {
                name[i] = d.getString();
                if ("SectionHead".equals(empState)) {//if section head then getting section as well as division of that section
                    DivNm[i] = d.getString();
                    cmbDiv.addItem(DivNm[i]);
                    cmbSec.addItem(name[i]);
                    SectionNm[i] = name[i];
                }
                if ("DivHead".equals(empState)) {
                    cmbDiv.addItem(name[i]);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void View() {
        if (FormFilled()) {
            try {
//                String SelfTeam = "";
//
//                if (rbSelf.isSelected() == true) {
//                    ((AttendanceApplet) applet).showMonthlyMuster(MM.comboMonth.getSelectedIndex());
//                    return;
//                }

                if (rbPresent.isSelected() == true) {
                    ReportName = "Attendance Only Present";
                } else if (rbAbsent.isSelected() == true) {
                    ReportName = "Attendance Only Absent";
                    return;
                } else if (rbBoth.isSelected() == true) {
                    ReportName = "Attendance";
                }

                String ReportWise = "";
//                if ("Self".equals(SelfTeam)) {
//                    ReportWise = "Self";
//                } else 
                if (rbFull.isSelected() == true) {
                    ReportWise = "Full";
                } else if (rbDiv.isSelected() == true) {
                    ReportWise = "Division";
                } else if (rbSec.isSelected() == true) {
                    ReportWise = "Section";
                } else if (rbWL.isSelected() == true) {
                    ReportWise = "WorkLoc";
                } else {
                    ReportWise = "";
                }
                String ReportType = "";
                CreatePacketAttendance packet;
                CreatePacketAttendance divPacket;
                if (rbSel.isSelected() == true) {
                    ReportType = "Selected";
                    String selEmployee = getSelectedEmp();
                    String divSecWL = "";
                    String cmbdiv1 = "";
                    String cmbSec1 = "";
                    String cmbWL1 = "";
                    if (rbDiv.isSelected() == true) {
                        if (!"Select".equals(cmbDiv.getSelectedItem().toString())) {
                            cmbdiv1 = cmbDiv.getSelectedItem().toString();
                        }
                    }
                    if (rbSec.isSelected() == true) {
                        if (!"Select".equals(cmbDiv.getSelectedItem().toString())) {
                            cmbdiv1 = cmbDiv.getSelectedItem().toString();
                        }
                        if (!"Select".equals(cmbSec.getSelectedItem().toString())) {
                            cmbSec1 = cmbSec.getSelectedItem().toString();
                        }
                    }
                    if (rbWL.isSelected() == true) {
                        if (!"Select".equals(cmbDiv.getSelectedItem().toString())) {
                            cmbdiv1 = cmbDiv.getSelectedItem().toString();
                        }
                        if (!"Select".equals(cmbSec.getSelectedItem().toString())) {
                            cmbSec1 = cmbSec.getSelectedItem().toString();
                        }
                        if (!"Select".equals(cmbWL.getSelectedItem().toString())) {
                            cmbWL1 = SimpleUtilities.getSplittedString(cmbWL.getSelectedItem().toString(), 0);
                        }
                    }

                    packet = new CreatePacketAttendance(this, cmbdiv1, cmbSec1, cmbWL1);
                    callServlet(packet.getPacket(), selEmployee, ReportName, ReportType, ReportWise);
                } else if (rbFull.isSelected() == true) {
                    ReportType = "Full";
                    String strHead = "";
                    if (bCheckDivHead == true && bCheckSecHead == true) {
                        strHead = "Both";
                    } else if (bCheckSecHead == true) {
                        strHead = "SecHead";
                    } else if (bCheckDivHead == true) {
                        strHead = "DeptHead";
                    }

                    if (!"DivHead".equals(strHead) && !"Both".equals(strHead)) {
                        DivName = new String[cmbDiv.getItemCount()];
                        divPacket = new CreatePacketAttendance(getComboItems(cmbDiv, cmbDiv.getItemCount()));
                        packet = new CreatePacketAttendance(this, strHead);//sends section wise Division
                    } else {
                        DivName = new String[cmbDiv.getItemCount()];
                        divPacket = new CreatePacketAttendance(getComboItems(cmbDiv, cmbDiv.getItemCount()));
                        packet = new CreatePacketAttendance(this, strHead);//sends Division if emp is divhead
                    }
                    callServlet(packet.getPacket(), CreatePacket.Packet(getComboItems(cmbDiv, cmbDiv.getItemCount())), ReportName, ReportType, ReportWise);
                }
            } catch (Exception ex) {
                MyUtils.showException("Error loading " + ReportName + " report", ex);
            }
        }
    }

    private void callServlet(String Packet, String divPacket, String reqType, String ReportType, String rptWise) {
        String Packet1 = Packet;
        if (!Packet1.equals("PacketFail")) {
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("AttendanceFormServlet");
                myHTTP.openOS();
                myHTTP.println(reqType);
                myHTTP.println(ReportType);
                myHTTP.println(Packet1);
                myHTTP.println(rptWise);
                myHTTP.println(divPacket);
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                //MyUtils.showMessage("result :" + result);
                myHTTP.closeIS();
                if (result.startsWith("ERROR")) {
                    MyUtils.showException("Database ERROR", new Exception(result));
                } else {
                    ((AttendanceApplet) applet).showReportPanel(Packet, result, divPacket, rptWise);

                }
            } catch (Exception ex) {
                MyUtils.showException("CallServlet", ex);
            }
        }
    }

    private void getEmpList(String SearchBy) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ReportFormServlet");
            myHTTP.openOS();
            myHTTP.println("GetEmp");
            myHTTP.println(SearchBy);
            if ("Division".equals(SearchBy)) {
                myHTTP.println(cmbDiv.getSelectedItem().toString());
            } else if ("Section".equals(SearchBy)) {
                myHTTP.println(cmbSec.getSelectedItem().toString());
            } else if ("WorkLocation".equals(SearchBy)) {
                myHTTP.println(cmbDiv.getSelectedItem().toString());
                myHTTP.println(cmbSec.getSelectedItem().toString());
                myHTTP.println(SimpleUtilities.getSplittedString(cmbWL.getSelectedItem().toString(), 0));
            }
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

            EmpClass[] employee = new EmpClass[rowCount];
            for (int i = 0; i < rowCount; i++) {
                employee[i] = new EmpClass();
                employee[i].EmpNo = d.getString();
                employee[i].EmpName = d.getString();
            }
            tableModel.setData(employee);
        } catch (Exception e) {
            MyUtils.showException("GetEmployeeList", e);
            tableModel.setData(null);
        }
    }

    private void FillCombo(JComboBox myComboBox, String str, String divNm) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ReportFormServlet");
            myHTTP.openOS();
            myHTTP.println(str);
            myHTTP.println(divNm);
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

    private void DisableControls() {
        rbDiv.setEnabled(false);
        rbSec.setEnabled(false);
        rbWL.setEnabled(false);
        cmbDiv.setEnabled(false);
        cmbSec.setEnabled(false);
        cmbWL.setEnabled(false);
        table.setEnabled(false);
    }

    private void EnableControls() {

        rbSec.setEnabled(true);
        rbWL.setEnabled(true);
        if (rbSec.isSelected() == false) {
            cmbDiv.setEnabled(true);
            rbDiv.setEnabled(true);
        }
        cmbSec.setEnabled(true);
        if (rbWL.isSelected() == true) {
            cmbWL.setEnabled(true);
        }
        table.setEnabled(true);
        if (rbDiv.isSelected() == true) {
            cmbSec.setEnabled(false);
            cmbWL.setEnabled(false);

        }
        if (bCheckDivHead == false && bCheckSecHead == true) {
            rbDiv.removeActionListener(this);
            cmbDiv.removeActionListener(this);
            rbDiv.setEnabled(false);
            rbSec.setSelected(true);
            rbSec.setEnabled(true);
            cmbSec.setEnabled(true);
            rbWL.setEnabled(true);
            cmbWL.setEnabled(true);
        }
    }

    private String SearchEmpBy() {
        if (rbDiv.isSelected() != true) {
            if (rbSec.isSelected() == true) {
                return "Section";
            } else if (rbWL.isSelected() == true) {
                return "WorkLocation";
            }
        }
        return "Division";
    }

    private String[] getComboItems(JComboBox MyCombo, int items) {
        int j = 0;
        if (items > 0) {
            for (int i = 0; i < items; i++) {
                if (!"Select".equals(MyCombo.getItemAt(0).toString())) {
                    DivName[j] = MyCombo.getItemAt(i).toString();
                    j++;
                } else {
                    DivName[j] = MyCombo.getItemAt(i).toString();
                    j++;
                }
            }
        }
        return DivName;
    }
//    private void getYear() {
//        String result = null;
//        try {
//            MyHTTP myHTTP = MyUtils.createServletConnection("AttendanceFormServlet");
//            myHTTP.openOS();
//            myHTTP.println("getYear");
//            myHTTP.closeOS();
//            myHTTP.openIS();
//            result = myHTTP.readLine();
//            myHTTP.closeIS();
//            MyUtils.showMessage("result : " + result);
//
//            if (result.startsWith("ERROR")) {
//                MyUtils.showException("Database ERROR", new Exception(result));
//            } else {
//                Depacketizer d = new Depacketizer(result);
//                int rowCount = d.getInt();
//
//                for (int i = 0; i < rowCount; i++) {
//                    cmbYear.addItem(d.getInt());
//                }
//            }
//
//        } catch (Exception ex) {
//            MyUtils.showException("result Year : " + result, ex);
//        }
//    }

    public void swapPanels(MyPanel a, MyPanel b) {
        MainPanel.remove(a);
        MainPanel.add(b, BorderLayout.NORTH);
        MainPanel.validate();
        MainPanel.repaint();
    }

    private void checkHeadDivisionSection() {
        try {
            if (checkHead(empCode, "Section")) {
                rbDiv.setEnabled(false);
                cmbDiv.setEnabled(false);
                bCheckSecHead = true;
            } else {
                bCheckSecHead = false;
            }

            if (checkHead(empCode, "Division")) {
                rbDiv.setEnabled(true);
                cmbDiv.setEnabled(true);
                bCheckDivHead = true;
            } else {
                bCheckDivHead = false;
            }
            if (bCheckDivHead == true) {
                if ((cmbDiv.getItemCount()) > 0) {
                    cmbDiv.setSelectedIndex(1);
                }
            }

            if (bCheckDivHead == true && bCheckSecHead == true) {
                rbDiv.setEnabled(true);
                cmbDiv.setEnabled(true);
            }

            if (bCheckDivHead == false && bCheckSecHead == true) {
                rbDiv.removeActionListener(this);
                cmbDiv.removeActionListener(this);
                rbDiv.setEnabled(false);
                cmbDiv.setEnabled(true);

                int divcount = cmbDiv.getItemCount();
                if (divcount == 2) {
                    cmbDiv.setSelectedIndex(1);
                }
                int seccount = cmbSec.getItemCount();
                if (seccount == 2) {
                    cmbSec.setSelectedIndex(1);
                }
            }

            if (bCheckDivHead == false && bCheckSecHead == false) {
//                 rbTeam.setEnabled(false);
//                visibleFalseControls();               
            } else {
                panEmpSearch.add(table.getGUI(), BorderLayout.CENTER);
                panCentr.add(panEmpSearch, BorderLayout.CENTER);
                getEmpList(SearchEmpBy());
            }
            if (rbFull.isSelected() == true) {
                DisableControls();
            }
        } catch (Exception ex) {
            MyUtils.showMessage("checkHeadDivisionSection : " + ex);
        }
    }
}
