/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.EmpDataSync;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import lib.MathConverters.Converter;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.gui.table.MyTableModel;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author pradnya
 */
public class EmpDataSync extends MyPanel implements ActionListener {
    public JRadioButton rbAllEmp, rbSelEmp, rbSelReader, rbAllReader;
    public MyTextField txtSearchEmp;
    public MyTable tlbEmp;
    public EmpTableModel tablemodel;
    public readerTable tableModelReader;
    public JCheckBox chkrReader1, chkrReader2;
    public JComboBox cmbReader;
    public MyLabel lblResp, lblEmpSync, lblEmpSync1, lblEmpSync2, lblEmpSync3, lblEmpSync4;
    public MyButton btnSync, btnSearch, btnErasAll, btnExt;
    public MyPanel MainPanel;
    public MyPanel panTree;
    EmpDataSyncApplet empDataSyncApplet;
    String readerNo;
    String readerList[];
    String locationList[];
    int syncEmp;
    int syncEmpFailed;
    int syncAlready;
    int noAccess;
    int iempNotFound;
    int iempFound;
    boolean bEmpFound;
    boolean bEmpNotFound;
    String selReader[];
    String strNoReplyFrmReader;
    EmpReaderAccessClass EmpAccess[];
    JTree TreeEmp;
    DefaultTreeModel model1;
    DefaultMutableTreeNode Mainparent, subparent, child;
    DefaultTreeCellRenderer renderer;//for adding icons to the node of the tree

    public EmpDataSync(EmpDataSyncApplet empDataSyncApplet, String readerNo, String readerList[], String locationList[]) {
        this.readerNo = readerNo;
        this.empDataSyncApplet = empDataSyncApplet;
        this.locationList = locationList;
        this.readerList = readerList;
        getEmpDataSyncGUI(readerList);

    }

    public void getEmpDataSyncGUI(String readerList[]) {
        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) (screenSize.width / 1.5);
            int height = (int) (screenSize.height / 1.7);
            setSize(width, height);

            MainPanel = new MyPanel(new BorderLayout());
            MyPanel panCenter = new MyPanel(new BorderLayout());
            MyPanel panEmpRdr = new MyPanel(new GridLayout(1, 2));
            MyPanel panEmp = new MyPanel(new BorderLayout());
            MyPanel panEmpSearch = new MyPanel(new GridLayout(2, 2), "Select Employee");

            ButtonGroup rbGrpEmp = new ButtonGroup();
            rbAllEmp = new JRadioButton("Select All");
            rbAllEmp.addActionListener(this);
            rbAllEmp.setActionCommand("AllEmployee");
            rbGrpEmp.add(rbAllEmp);
            panEmpSearch.add(rbAllEmp);

            MyLabel lblFill = new MyLabel();
            panEmpSearch.add(lblFill);

            rbSelEmp = new JRadioButton("Selected (by empcode)", true);
            rbSelEmp.addActionListener(this);
            rbSelEmp.setActionCommand("SelectedEmployee");
            rbGrpEmp.add(rbSelEmp);
            panEmpSearch.add(rbSelEmp);

            txtSearchEmp = new MyTextField();
            panEmpSearch.add(txtSearchEmp);
            panEmp.add(panEmpSearch, BorderLayout.NORTH);

            //Employee teble
            tablemodel = new EmpTableModel();
            getEmpList();

            tlbEmp = new MyTable(tablemodel);
            tlbEmp.setEnabled(true);
            tlbEmp.setOpaque(true);

            panEmpRdr.setPreferredSize(new Dimension(width / 14, height / 14));
            tlbEmp.addSearchField(3, txtSearchEmp, MyTable.SEARCH_TYPE_CONTAINS);
            panEmp.add(tlbEmp.getGUI(), BorderLayout.CENTER);
            panEmpRdr.add(panEmp);

            MyPanel panReader = new MyPanel(new BorderLayout());
            MyPanel panRdrGrd = new MyPanel(new GridLayout(2, 1), "Reader");

            ButtonGroup rbGrpRdr = new ButtonGroup();
            rbAllReader = new JRadioButton("Select All");
            rbAllReader.addActionListener(this);
            rbAllReader.setActionCommand("AllReader");
            rbGrpRdr.add(rbAllReader);
            panRdrGrd.add(rbAllReader);

            rbSelReader = new JRadioButton("Selected", true);
            rbSelReader.addActionListener(this);
            rbSelReader.setActionCommand("SelectedReader");
            rbGrpRdr.add(rbSelReader);
            panRdrGrd.add(rbSelReader);
            panReader.add(panRdrGrd, BorderLayout.NORTH);

            MyPanel panSelRdr = new MyPanel(new BorderLayout());

            //reader Table
            panSelRdr.add(setReaderData().getGUI(), BorderLayout.CENTER);
            panReader.add(panSelRdr, BorderLayout.CENTER);

            MyPanel panRespStat = new MyPanel(new BorderLayout(), "Response Status");
            lblResp = new MyLabel();
            lblResp.setPreferredSize(new Dimension(width / 18, height / 16));
            panRespStat.add(lblResp, BorderLayout.NORTH);

            lblEmpSync = new MyLabel();
            lblEmpSync.setPreferredSize(new Dimension(width / 18, height / 16));
            panRespStat.add(lblEmpSync, BorderLayout.CENTER);

            MyPanel panlblResp = new MyPanel(new GridLayout(2, 2));

            lblEmpSync1 = new MyLabel();
            lblEmpSync1.setPreferredSize(new Dimension(width / 18, height / 16));
            panlblResp.add(lblEmpSync1);

            lblEmpSync2 = new MyLabel();
            lblEmpSync2.setPreferredSize(new Dimension(width / 18, height / 16));
            panlblResp.add(lblEmpSync2);

            lblEmpSync3 = new MyLabel();
            lblEmpSync3.setPreferredSize(new Dimension(width / 18, height / 16));
            panlblResp.add(lblEmpSync3);

            lblEmpSync4 = new MyLabel();
            lblEmpSync4.setPreferredSize(new Dimension(width / 18, height / 16));
            panlblResp.add(lblEmpSync4);

            panRespStat.add(panlblResp, BorderLayout.SOUTH);

            panEmpRdr.add(panReader);
            panCenter.add(panEmpRdr, BorderLayout.CENTER);
            panCenter.add(panRespStat, BorderLayout.SOUTH);

            MyPanel panBtn = new MyPanel(new FlowLayout(FlowLayout.CENTER));
            btnSync = new MyButton("Sync", 2) {

                @Override
                public void onClick() {
                    clearLabels();
                    refreshTree();
                    callServlet(tablemodel, "add");
                }
            };

            panBtn.add(btnSync);

            btnSearch = new MyButton("Search", 2) {

                @Override
                public void onClick() {
                    clearLabels();
                    refreshTree();
                    callServlet(tablemodel, "search");
                }
            };

            panBtn.add(btnSearch);

            btnErasAll = new MyButton("Erase All", 2) {

                @Override
                public void onClick() {
                    clearLabels();
                    refreshTree();
                    EraseAllEmployee(getCheckedRows(tableModelReader));
                }
            };

            panBtn.add(btnErasAll);

//            btnExt = new MyButton("Exit", 2) {
//
//                @Override
//                public void onClick() {
//                    //checkReader = new LinkedList();
//                }
//            };
//
//            panBtn.add(btnExt);
            MainPanel.add(panCenter, BorderLayout.CENTER);

            panTree = new MyPanel(new BorderLayout());
            Mainparent = new DefaultMutableTreeNode("Employee Synchronization", true);
            TreeEmp = new JTree();
            TreeEmp.setModel(new DefaultTreeModel(Mainparent));
            TreeEmp.setPreferredSize(new Dimension((int) (width / 3), height));
            panTree.add(new JScrollPane(TreeEmp), BorderLayout.CENTER);

            MainPanel.add(panTree, BorderLayout.EAST);

            MainPanel.add(panBtn, BorderLayout.SOUTH);

            this.setLayout(new BorderLayout());
            this.add(MainPanel, BorderLayout.CENTER);

        } catch (Exception ex) {
            MyUtils.showMessage("Employee data sync GUI" + ex);
        }
    }

    private void EraseAllEmployee(LinkedList selReader) {


        Object selectedReader[] = selReader.toArray();//Storing selected readers from linkedlist to array to use in for loop
        for (int i = 0; i < selectedReader.length; i++) {
            subparent = new DefaultMutableTreeNode(selectedReader[i], true);//creating sub parent node
            Mainparent.add(subparent);//adding subparent to the main parent node on the Jtree
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("EmpDataSyncServlet");
                myHTTP.openOS();
                myHTTP.println("EraseAllEmp");
                myHTTP.println((String) selectedReader[i]);
                myHTTP.closeOS();
                myHTTP.openIS();
                String response = myHTTP.readLine();
                myHTTP.closeIS();
                if (response.startsWith("ERROR")) {
                    MyUtils.showMessage(response);
                    switch (response) {
                        case "ERROR: No reply from reader":
                        case "ERROR: null":
                            child = new DefaultMutableTreeNode("Reader Not Responding");
                            subparent.add(child);
                            break;
                        case "ERROR:Server response NULL":
                            child = new DefaultMutableTreeNode("Erase all employees done");
                            subparent.add(child);
                            lblEmpSync.setText("Erase all employees done in reader " + selectedReader[i]);
                            break;
                    }
                } else {
                    lblEmpSync.setText("Erase all employees done in reader " + selectedReader[i]);
                }
            } catch (Exception ex) {
                MyUtils.showException("sendUID", ex);
            }
        }
    }

    public void getEmpList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("EmpDataSyncServlet");
            myHTTP.openOS();
            myHTTP.println("getEmployeeList");
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            int rowCount = Integer.parseInt(myHTTP.readLine());
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            Depacketizer d = new Depacketizer(response);
            EmpClass[] emp = new EmpClass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {
                emp[i] = new EmpClass();
                emp[i].EmpUID = d.getString();
                emp[i].EmpName = d.getString();
                emp[i].EmpCode = d.getString();
            }
            tablemodel.setData(emp);
        } catch (Exception e) {
            MyUtils.showMessage("Error: " + e);
            MyUtils.showException("GetEmployeeList", e);
            tablemodel.setData(null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "AllEmployee":
                selectAllEmployees(tablemodel);
                this.repaint();
                break;
            case "SelectedEmployee":
                //EnableControls();
                break;
            case "AllReader":
                selectAllReader(tableModelReader);
                this.repaint();
                break;
            case "SelectedReader":
                break;
        }
    }

    private MyTable setReaderData() throws Exception {

        readerClass[] readers = new readerClass[readerList.length];
        MyTable tlbreader;
        //get all docs
        for (int i = 0; i < readerList.length; i++) {
            readers[i] = new readerClass();
            readers[i].readerNo = readerList[i];
            readers[i].readerLoc = locationList[i];
        }
        tableModelReader = new readerTable();
        tableModelReader.setData(readers);
        tlbreader = new MyTable(tableModelReader);
        tlbreader.setEnabled(true);
        return tlbreader;
    }

    public void selectAllEmployees(EmpTableModel tm) {
        for (int i = 0; i < tm.rowCount; i++) {
            tm.setSelectedRow(i);
        }
    }

    public void selectAllReader(readerTable tm) {
        for (int i = 0; i < tm.rowCount; i++) {
            tm.setSelectedRow(i);
        }
    }

    public LinkedList getCheckedRows(MyTableModel tm) {
        LinkedList lstChecked = new LinkedList();
        if (tm instanceof EmpTableModel) {
            EmpTableModel obj1 = (EmpTableModel) tm;
            for (int i = 0; i < obj1.rowCount; i++) {
                if (obj1.isRowChecked(i)) {
                    lstChecked.add(obj1.getValueAt(i, 1));
                }
            }
        } else if (tm instanceof readerTable) {
            readerTable obj1 = (readerTable) tm;
            for (int i = 0; i < obj1.rowCount; i++) {
                if (obj1.isRowChecked(i)) {
                    lstChecked.add(obj1.getValueAt(i, 1));
                }
            }
        }
        return lstChecked;
    }

    public void callServlet(EmpTableModel empTM, String action) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("EmpDataSyncServlet");
            myHTTP.openOS();
            myHTTP.println("GetAssignedReader");
            myHTTP.println(getCheckedRows(empTM).toString());
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
            EmpAccess = new EmpReaderAccessClass[rowCount];
            for (int i = 0; i < rowCount; i++) {
                Depacketizer dp = new Depacketizer(d.getString());
                EmpAccess[i] = new EmpReaderAccessClass();
                EmpAccess[i].Empreaders = dp.getString();
                EmpAccess[i].strEmpUID = dp.getString();

            }
            checkEmployeeOnReader(EmpAccess, action);
        } catch (Exception e) {
            MyUtils.showMessage("Error: " + e);
            MyUtils.showException("UploadEmployee", e);

        }
    }

    public void checkEmployeeOnReader(EmpReaderAccessClass EmpAccess[], String action) {

        boolean isAssignedReader;
        LinkedList lstReader = getCheckedRows(tableModelReader);//getting selected readers
        int lstsize = lstReader.size();
        selReader = new String[lstsize];
        strNoReplyFrmReader = "";

        for (int i = 0; i < lstsize; i++) {
            selReader[i] = lstReader.get(i).toString();//inserting reader list in sring array for simplification
        }

        for (int i = 0; i < selReader.length; i++) {//selected Readers
            syncEmpFailed = 0;
            syncAlready = 0;
            syncEmp = 0;
            noAccess = 0;
            iempFound = 0;
            iempNotFound = 0;
            clearLabels();
            subparent = new DefaultMutableTreeNode(selReader[i], true);
            Mainparent.add(subparent);
            //loop for employee
            for (int j = 0; j < EmpAccess.length; j++) {//length of the class EmpReaderAccessClass
                isAssignedReader = false;
                String assignReader[] = (EmpAccess[j].Empreaders).split(" ");
                if (!strNoReplyFrmReader.equals(selReader[i])) {
                    for (int k = 0; k < assignReader.length; k++) {//loop for assigned readers
                        if (selReader[i].equals(assignReader[k])) {//if selected reader equals assigned readers
                            isAssignedReader = true;
                            switch (action) {
                                case "add":
                                    if (!sendUID(EmpAccess[j].strEmpUID, Integer.parseInt(selReader[i]), 202))//202 for upload employee's UID to reader
                                    {
                                        strNoReplyFrmReader = selReader[i];
                                    }
                                    break;
                                case "search":
                                    if (!sendUID(EmpAccess[j].strEmpUID, Integer.parseInt(selReader[i]), 204)) {
                                        strNoReplyFrmReader = selReader[i];
                                    }
                                    break;
                            }
                        }
                    }
                }

                if (isAssignedReader == false) {
                    //delete uid from selected reader
                    if (!strNoReplyFrmReader.equals(selReader[i])) {
                        if (!sendUID(EmpAccess[j].strEmpUID, Integer.parseInt(selReader[i]), 203))//203 for deleting employee from reader if employee has not assigned reader
                        {
                            strNoReplyFrmReader = selReader[i];
                        }
                    }
                }
            }
            switch (action) {
                case "add":
                    if (!strNoReplyFrmReader.equals(selReader[i])) {
                        lblResp.setText("Synchronizing employee completed in reader " + selReader[i]);
                    }
                    lblEmpSync.setText("");
                    break;
                case "search":
                    if (!strNoReplyFrmReader.equals(selReader[i])) {
                        lblResp.setText("Search employee completed in reader " + selReader[i]);
                    }
                    lblEmpSync.setText("");
                    break;
            }
        }
    }

    //send UID to servlet to use in make write frame 
    public boolean sendUID(String empUID, int readerNo, int command) {
        try {
            byte arrUID[] = Converter.hexToByte(empUID);
            //creating packet
            Packetizer p = new Packetizer();
            p.addInt(readerNo);//sending reader no
            p.addInt(command);//sending command i.e.202=addUID,203=delete UID,204=search UID
            p.addInt(arrUID[0]);
            p.addInt(arrUID[1]);
            p.addInt(arrUID[2]);
            p.addInt(arrUID[3]);

            MyHTTP myHTTP = MyUtils.createServletConnection("EmpDataSyncServlet");
            myHTTP.openOS();
            myHTTP.println("sendUID");
            myHTTP.println(p.getPacket());
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                if ("ERROR: No reply from reader".equals(response) || "ERROR: null".equals(response)) {
                    child = new DefaultMutableTreeNode("Reader Not Responding");
                    subparent.add(child);
                }
                return false;
            }
            validateWriteFrame(response, command, empUID, readerNo);//checking whether UID has written successfully on reader or not

        } catch (Exception ex) {
            MyUtils.showException("sendUID", ex);
        }
        return true;
    }

    //checking whether UID has written successfully on reader or not
    private void validateWriteFrame(String packet, int command, String empUID, int readerNo) throws Exception {
        Depacketizer dp = new Depacketizer(packet);
        int data[] = new int[4];//storing UID from packet
        long bufferdata[] = new long[2];
        int j = 0;
        int frame[] = new int[11];
        for (int i = 0; i < 11; i++) {
            frame[i] = dp.getInt();
            if (i >= 2 & i <= 5) {//retrieving only UID from the packet i.e. UID starts from frame[2] to frame[5]
                data[j++] = frame[i];
            }
        }
        bufferdata[0] = Converter.byteToLong(data);
        int validatedata = frame[6] * 256;
        int validatedata1 = 0;//initializing variable for storing new value of (frame[6] * 256)
        if (validatedata != 32768) {
            validatedata1 = validatedata + 255;
            if (validatedata1 != 65535) {
                validatedata1 = validatedata;
            }
            bufferdata[1] = validatedata1;
        } else {
            bufferdata[1] = validatedata;
        }
        checkResponse(bufferdata, command, empUID, readerNo);
    }

    private void checkResponse(long bufferdata[], int command, String empUID, int readerNo) {//checks whether UID already uploaded or Upload Failed or upload successfully

        if (command == 202) {
            if (bufferdata[1] == 65535) {
                addChildNode(empUID, " Synchronization UID Fail");
                syncEmpFailed = syncEmpFailed + 1;
                lblEmpSync.setText("Emp(" + empUID + ") Synchronization fail in reader " + readerNo);
            }
            if ((bufferdata[1] & 32768) == 32768 && bufferdata[1] != 65535) {
                addChildNode(empUID, " Emp Already Synchronised");
                syncAlready = syncAlready + 1;
                lblEmpSync.setText("Emp (" + empUID + ") already Synchronized in reader " + readerNo);
            } else if ((bufferdata[1] & 32768) != 32768 && bufferdata[1] != 65535) {
                addChildNode(empUID, " Emp Synchronized");
                syncEmp = syncEmp + 1;
                lblEmpSync.setText("Emp(" + empUID + ") Synchronized in reader " + readerNo);
            }
            lblEmpSync2.setText("Emp Synchronization Fail : " + syncEmpFailed);
            lblEmpSync3.setText("Emp Already Synchronized : " + syncAlready);
            lblEmpSync1.setText("Emp Synchronized : " + syncEmp);

        } else if (command == 203) {
            addChildNode(empUID, " Emp Synchronized(No Access)");
            noAccess = noAccess + 1;
            lblEmpSync.setText("Emp(" + empUID + ") No Access to reader " + readerNo);

        } else if (command == 204) {
            if (bufferdata[1] != 0) {
                iempNotFound = iempNotFound + 1;
                addChildNode(empUID, " Emp not found");
                lblEmpSync.setText("Emp(" + empUID + ") not found in reader " + readerNo);

            } else {
                addChildNode(empUID, " Emp found");
                iempFound = iempFound + 1;
                lblEmpSync.setText("Emp(" + empUID + ") found in reader " + readerNo);
            }
            lblEmpSync2.setText("Emp not found : " + iempNotFound);
            lblEmpSync1.setText("Emp found : " + iempFound);
        }
        lblEmpSync4.setText("Emp No Access to Reader : " + noAccess);
    }

    private void setIconsToTree() {
        try {
            Icon leafIcon = new ImageIcon(MyUtils.getRelativeURL("AppletImages/MISC27.ico"));
            Icon openIcon = new ImageIcon(MyUtils.getRelativeURL("AppletImages/fw_add.ico"));
            Icon closedIcon = new ImageIcon(MyUtils.getRelativeURL("AppletImages/fw_del.ico"));
            renderer = (DefaultTreeCellRenderer) TreeEmp.getCellRenderer();
            renderer.setLeafIcon(leafIcon);
            renderer.setClosedIcon(closedIcon);
            renderer.setOpenIcon(openIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void refreshTree() {
        model1 = ((DefaultTreeModel) TreeEmp.getModel());
        Mainparent.removeAllChildren();
        model1.reload(Mainparent);
    }

    private void addChildNode(String empUID, String message) {
        child = new DefaultMutableTreeNode(empUID + " " + message);
        subparent.add(child);
    }

    private void clearLabels() {
        lblResp.setText("");
        lblEmpSync.setText("");
        lblEmpSync1.setText("");
        lblEmpSync2.setText("");
        lblEmpSync3.setText("");
        lblEmpSync4.setText("");
    }
}
