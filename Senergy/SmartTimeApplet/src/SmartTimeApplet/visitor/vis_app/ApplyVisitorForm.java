package SmartTimeApplet.visitor.vis_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import lib.Utility.DateUtilities;
import lib.digsign.DigSignUser;
import lib.digsign.gui_form.MyFormPanel;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;
import lib.session.MyHTTP;
import lib.session.MyUtils;
import lib.utils.Depacketizer;
import lib.utils.MyDate;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 * ApplyVisitorForm.java
 */
public class ApplyVisitorForm extends MyPanel {
    private ShowCompanyListApplet applet;
    //GUI Fields
    private MyFormPanel form = null;
    private JDatePicker fromDatePicker;
    private JDatePicker toDatePicker;
    private JComboBox cmbExpectedGate;
    private MyTextField txtPurpse;
    private MyTextField txtLocation;
    private VisitorTM visitorTM;
    private MyTable visitorTable;
    private HostTM hostTM;
    private MyTable hostTable;
    MyPanel approver1Panel;
    MyPanel approver2Panel;
    //Data Objects
    private VisitorForm visitorForm = new VisitorForm();

    /**
     *
     * @param applet
     * @param companyID
     */
    public ApplyVisitorForm(ShowCompanyListApplet applet, String companyID) {
        try {
            this.applet = applet;
            //fetch required data data from server
            visitorForm.applicant.getInfo();
            visitorForm.company = getCompanyInfo(companyID);
            visitorTM = new VisitorTM();
            visitorTM.setData(getVisitorList(companyID));
            visitorTable = new MyTable(visitorTM);
            hostTM = new HostTM();
            hostTable = new MyTable(hostTM);
            //create GUI
            form = new MyFormPanel(applet, "CEP Form");
            form.addMyPanel(visitorForm.applicant.createGUIPanel("Applicant"));
            form.addMyPanel(visitorForm.company.getGUIPanel("Company"));
            form.addMyPanel(visitDetailsPanle());
            //
            MyPanel p = new MyPanel(new GridLayout(1, 1), "Visitor List");
            p.add(visitorTable.getGUI());
            p.setPreferredSize(new Dimension(400, 180));
            form.addMyPanel(p);
            //
            p = new MyPanel(new BorderLayout(), "Host List");
            p.add(hostTable.getGUI(), BorderLayout.CENTER);
            MyPanel visButtonPane = new MyPanel(new GridLayout(1, 3));
            visButtonPane.add(new MyButton("Add Host") {

                @Override
                public void onClick() {
                    addHost();
                }
            });
            visButtonPane.add(new MyButton("Remove Host") {

                @Override
                public void onClick() {
                    removeHost();
                }
            });
            p.add(visButtonPane, BorderLayout.SOUTH);
            p.setPreferredSize(new Dimension(400, 120));
            form.addMyPanel(p);
            form.addMyPanel(getApprover1Panel());
            form.addMyPanel(getApprover2Panel());
            //
            setLayout(new BorderLayout());
            add(form, BorderLayout.CENTER);
            MyPanel buttonPane = createButtonPane();
            add(buttonPane, BorderLayout.SOUTH);
        } catch (Exception e) {
            MyUtils.showException("ApplyVisitirForm", e);
            applet.showHomePanel();
        }
    }

    /**
     */
    private MyPanel createButtonPane() {
        MyPanel panel = new MyPanel(new GridLayout(1, 0, 20, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Actions"));

        panel.add(new MyButton("Approver-1") {

            @Override
            public void onClick() {
                setApprover1();
            }
        });
        panel.add(new MyButton("Approver-2") {

            @Override
            public void onClick() {
                setApprover2();
            }
        });

        panel.add(new MyButton("Submit") {

            @Override
            public void onClick() {
                submitForm();
            }
        });
        panel.add(new MyButton("Download") {

            @Override
            public void onClick() {
                downloadForm();
            }
        });
        panel.add(new MyButton("Cancel") {

            @Override
            public void onClick() {
                closeForm();
            }
        });
        return panel;
    }

    /**
     *
     */
    private void submitForm() {
        try {
            if (!formFilled()) {
                return;
            }
            //put data in Object
            visitorForm.dateFrom = MyDate.getDate(fromDatePicker);
            visitorForm.dateTo = MyDate.getDate(toDatePicker);
            visitorForm.purpose = txtPurpse.getText();
            visitorForm.places = txtLocation.getText();
            visitorForm.visitorList = getSelectedVisList();
            visitorForm.hostList = hostTM.getHostList();
            visitorForm.servletRequest = "submit_form";
            //send Object to the servlet
            MyHTTP myHTTP = MyUtils.createServletConnection("VisAppObjectServlet");
            myHTTP.openOS();
            myHTTP.writeObject(visitorForm);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            if (result.startsWith("ERROR")) {
                myHTTP.closeIS();
                throw new Exception(result);
            }
            visitorForm.formID = result;
            visitorForm.applyDate = myHTTP.readLine();
            myHTTP.closeIS();
            MyUtils.showMessage("Submited FormID=" + visitorForm.formID);
        } catch (Exception ex) {
            MyUtils.showException("SubmitForm", ex);
        }
    }

    /**
     *
     */
    private void downloadForm() {
        visitorForm.saveAtClientPC();
    }

    /**
     *
     */
    private void closeForm() {
        applet.closeApplet();
    }

    public void setApprover1() {
        try {
            String userID = JOptionPane.showInputDialog("Enter Approver-1 EmpNo:");
            DigSignUser app1 = new DigSignUser();
            app1.getUserInfo(userID);
            visitorForm.approver1 = app1;
            approver1Panel.removeAll();
            approver1Panel.add(app1.createGUIPanel(null));
            approver1Panel.validate();
            approver1Panel.repaint();
        } catch (Exception ex) {
            MyUtils.showException("GetInfo", ex);
        }
    }

    public void setApprover2() {
        try {
            String userID = JOptionPane.showInputDialog("Enter Approver-2 EmpNo:");
            DigSignUser app2 = new DigSignUser();
            app2.getUserInfo(userID);
            visitorForm.approver2 = app2;
            approver2Panel.removeAll();
            approver2Panel.add(app2.createGUIPanel(null));
            approver2Panel.validate();
            approver2Panel.repaint();
        } catch (Exception ex) {
            MyUtils.showException("GetInfo", ex);
        }
    }

    public void addHost() {
        try {
            String userID = JOptionPane.showInputDialog("Enter Host EmpNo:");
            DigSignUser host = new DigSignUser();
            host.getUserInfo(userID);
            hostTM.addHost(host);
        } catch (Exception ex) {
            MyUtils.showException("GetInfo", ex);
        }
    }

    public void removeHost() {
        try {
            int row = hostTable.getSelectedRow();
            if (row < 0) {
                MyUtils.showMessage("Please selected a host to remove!");
                return;
            }
            hostTM.removeHost(row);
        } catch (Exception ex) {
            MyUtils.showException("RemoveHost", ex);
        }
    }

    /**
     *
     * @return @throws Exception
     */
    private MyPanel getApprover1Panel() throws Exception {
        approver1Panel = new MyPanel(new GridLayout(1, 1), "Approver-1");
        approver1Panel.setPreferredSize(new Dimension(400, 70));
        return approver1Panel;
    }
    /**
     *
     * @return @throws Exception
     */
    private MyPanel getApprover2Panel() throws Exception {
        approver2Panel = new MyPanel(new GridLayout(1, 1), "Approver-2");
        approver2Panel.setPreferredSize(new Dimension(400, 70));
        return approver2Panel;
    }
    /**
     *
     * @return @throws Exception
     */
    private MyPanel visitDetailsPanle() throws Exception {
        MyPanel panel = new MyPanel(new GridLayout(3, 3), "Visit Details");

        MyPanel p1 = new MyPanel(new GridLayout(1, 3));

        MyPanel p11 = new MyPanel(new BorderLayout());
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL, "Date From: ");
        p11.add(l, BorderLayout.WEST);
        fromDatePicker = JDateComponentFactory.createJDatePicker();
        MyUtils.setDate((JComponent) fromDatePicker, DateUtilities.getCurrentDate());
        p11.add((JComponent) fromDatePicker, BorderLayout.CENTER);
        p1.add(p11);
        MyPanel p12 = new MyPanel(new BorderLayout());
        l = new MyLabel(MyLabel.TYPE_LABEL, "Date To: ");
        p12.add(l, BorderLayout.WEST);
        toDatePicker = JDateComponentFactory.createJDatePicker();
        MyUtils.setDate((JComponent) toDatePicker, DateUtilities.getCurrentDate());
        p12.add((JComponent) toDatePicker, BorderLayout.CENTER);
        p1.add(p12);
        MyPanel p13 = new MyPanel(new BorderLayout());
        l = new MyLabel(MyLabel.TYPE_LABEL, " Expected Gate:");
        p13.add(l, BorderLayout.WEST);
        String arr[] = {"North Gate", "East Gate", "West Gate", "South Gate"};
        cmbExpectedGate = new JComboBox(arr);
        p13.add(cmbExpectedGate, BorderLayout.CENTER);
        p1.add(p13);
        panel.add(p1);

        //Visit Purpose
        MyPanel p2 = new MyPanel(new BorderLayout());
        l = new MyLabel(MyLabel.TYPE_LABEL, "Visit Purpose: ");
        p2.add(l, BorderLayout.WEST);
        l.setForeground(new Color(89, 32, 222));
        txtPurpse = new MyTextField();
        p2.add(txtPurpse, BorderLayout.CENTER);
        final JComboBox comboPurposes = new JComboBox();
        comboPurposes.addItem("TechnicalDiscussion");
        comboPurposes.addItem("OfficialDiscussion");
        comboPurposes.addItem("Inspection");
        comboPurposes.addItem("Maintenance");
        comboPurposes.addItem("Repair OfEquipment");
        comboPurposes.addItem("ProjectTrainee");
        comboPurposes.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    txtPurpse.setText(comboPurposes.getSelectedItem().toString());
                }
            }
        });
        p2.add(comboPurposes, BorderLayout.EAST);
        panel.add(p2);

        //Place of Visit:
        MyPanel p3 = new MyPanel(new BorderLayout());
        l = new MyLabel(MyLabel.TYPE_LABEL, "Place of Visit:");
        p3.add(l, BorderLayout.WEST);
        txtLocation = new MyTextField();
        p3.add(txtLocation, BorderLayout.CENTER);
        final JComboBox comboPlaces = new JComboBox();
        comboPlaces.addItem("Select Visit Place");
        comboPlaces.addItem("CP");
        comboPlaces.addItem("FP");
        comboPlaces.addItem("SE");
        comboPlaces.addItem("SMG");
        comboPlaces.addItem("WS");
        comboPlaces.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    txtLocation.setText(comboPlaces.getSelectedItem().toString());
                }
            }
        });
        p3.add(comboPlaces, BorderLayout.EAST);
        panel.add(p3);
        return panel;
    }
    /**
     *
     * @return
     */
    private boolean formFilled() {
        if (cmbExpectedGate.getSelectedItem().equals("Select Type")) {
            MyUtils.showMessage("Expected Gate Not Selected");
            return false;
        }
        if (txtPurpse.getText().equals("")
                || txtLocation.getText().equals("")) {
            MyUtils.showMessage("Form not filled");
            return false;
        }
        return true;
    }
    /**
     * 
     * @param companyID
     * @return
     * @throws Exception 
     */
    private Company getCompanyInfo(String companyID) throws Exception {
        MyHTTP myHTTP = MyUtils.createServletConnection("VisAppServlet");
        myHTTP.openOS();
        myHTTP.println("getCompanyInfo");
        myHTTP.println(companyID);
        myHTTP.closeOS();
        myHTTP.openIS();
        String result = myHTTP.readLine();
        if (result.startsWith("ERROR")) {
            throw new Exception(result);
        }
        Depacketizer d = new Depacketizer(result);
        Company company = new Company();
        company.companyID = companyID;
        company.companyName = d.getString();
        company.address = d.getString();
        company.type = d.getString();
        company.city = d.getString();
        company.state = d.getString();
        company.pin = d.getString();
        return company;
    }
    /**
     * 
     * @param companyID
     * @return
     * @throws Exception 
     */
    private Visitor[] getVisitorList(String companyID) throws Exception {
        MyHTTP myHTTP = MyUtils.createServletConnection("VisAppServlet");
        myHTTP.openOS();
        myHTTP.println("getVisitorInfo");
        myHTTP.println(companyID);
        myHTTP.closeOS();
        myHTTP.openIS();
        String result = myHTTP.readLine();
        myHTTP.closeIS();
        if (result.startsWith("ERROR")) {
            throw new Exception(result);
        }
        // MyUtils.showMessage("Show Visitor response :" + result);
        Depacketizer d = new Depacketizer(result);
        int rowCount = d.getInt();
        Visitor[] visitorList = new Visitor[rowCount];
        for (int i = 0; i < rowCount; i++) {
            visitorList[i] = new Visitor();
            visitorList[i].pvisID = d.getString();
            visitorList[i].name = d.getString();
            visitorList[i].desig = d.getString();
            visitorList[i].pvcYesNo = getPVCYesNo(d.getString());
            visitorList[i].sex = getMaleFemale(d.getString());
        }
        return visitorList;
    }

    /**
     *
     * @param index
     * @return
     */
    public static String getPVCYesNo(String index) {
        String cmppvc = "";
        switch (index) {
            case "0":
                cmppvc = "YES";
                break;
            case "1":
                cmppvc = "NO";
                break;
        }
        return cmppvc;
    }

    /**
     *
     * @param index
     * @return
     */
    private static String getMaleFemale(String index) {
        String cmpsex = "";
        switch (index) {
            case "0":
                cmpsex = "Male";
                break;
            case "1":
                cmpsex = "FeMale";
                break;
        }
        return cmpsex;
    }
    /**
     *
     * @return
     */
    private Visitor[] getSelectedVisList() {
        Visitor[] visitors = new Visitor[visitorTM.rowCount];
        int count = 0;
        for (int i = 0; i < visitorTM.rowCount; i++) {
            if (visitorTM.isRowChecked(i)) {
                visitors[count] = visitorTM.getVisitor(i);
                visitors[count].visSNo = String.valueOf(count+1);
                count++;
            }
        }
        Visitor[] visList = new Visitor[count];
        System.arraycopy(visitors, 0, visList, 0, count);
        return visList;
    }

    /**
     *
     */
    public class HostTM extends MyTableModel {
        public DigSignUser[] hostList = new DigSignUser[20];
        
        public HostTM() throws Exception {
            //EmpNo
            addColumn(new MyTableColumn("EmpNo", 100, MyTableColumn.TYPE_STRING) {

                @Override
                public String getValueAt(int row) {
                    return hostList[row].userID;
                }
            });
            //Name
            addColumn(new MyTableColumn("Name", 100, MyTableColumn.TYPE_STRING) {

                @Override
                public String getValueAt(int row) {
                    return hostList[row].name;
                }
            });
            //Designation
            addColumn(new MyTableColumn("Designation", 100, MyTableColumn.TYPE_STRING) {

                @Override
                public String getValueAt(int row) {
                    return hostList[row].designation;
                }
            });
            //EMail
            addColumn(new MyTableColumn("EMail", 100, MyTableColumn.TYPE_STRING) {

                @Override
                public String getValueAt(int row) {
                    return hostList[row].email;
                }
            });
            //Phone
            addColumn(new MyTableColumn("Phone", 100, MyTableColumn.TYPE_STRING) {
                @Override
                public String getValueAt(int row) {
                    return hostList[row].phoneNo;
                }
            });
        }
        /**
         *
         * @param emp
         */
        public void addHost(DigSignUser emp) {
            hostList[rowCount] = emp;
            hostList[rowCount].hostSNo = String.valueOf(rowCount+1);
            rowCount++;
            fireTableDataChanged();
        }

        /**
         *
         * @param emp
         */
        public void removeHost(int row) {
            for (int i = row; i < rowCount - 1; i++) {
                hostList[i] = hostList[i + 1];
            }
            rowCount--;
            fireTableDataChanged();
        }

        /**
         *
         * @param row1
         * @param row2
         */
        @Override
        public void exchange(int row1, int row2) {
            DigSignUser temp = hostList[row1];
            hostList[row1] = hostList[row2];
            hostList[row2] = temp;
        }
        /**
         * 
         * @return 
         */
        private DigSignUser[] getHostList() {
            DigSignUser[] addedHosts = new DigSignUser[rowCount];
            System.arraycopy(hostList, 0, addedHosts, 0, rowCount);
            return addedHosts;
        }
    }
}
