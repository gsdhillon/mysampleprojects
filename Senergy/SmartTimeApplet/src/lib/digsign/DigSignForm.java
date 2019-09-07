package lib.digsign;

import java.awt.Frame;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.StringReader;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import lib.digsign.html_form.HTMLTable;
import lib.digsign.html_form.MyHTMLEditorKit;
import lib.session.MyUtils;

/**
 * DigSignForm.java
 */
public abstract class DigSignForm implements Serializable {
    public static final String ORG_NAME = "Senergy Intellution Pvt Ltd";
    public String applicationFullName = "APP_FULL_NAME_NOT_SET";
    public String appShortName = "APP_NAME_NOT_SET";
    //
    public String servletRequest = "SRVLET_REQUEST_NOT_SET";
    public String servletResponse = "SERVLET_RESPONSE_NOT_SET";
    public String todayDateTime = "TODAY_DATE_TIME_NOT_SET";//send from server
    public int version = 0;
    public String formID = "";
    public String applyDate;
    public DigSignUser applicant = new DigSignUser();
    public String remarks = "";
    public String state = "";//P-Pending A-Approved C-Canceled
    //all auths
    public int numAuths = 0;
    public DigSignAuth[] authList = new DigSignAuth[50];
    protected int currentAuthSNo = -1;
    //following 6 fields are taken from workFlow
    public String currentStatus = "";
    public String currentStatusCode = "";
    public String nextStatusCodeOnYes = "";
    public String nextStatusCodeOnNo = "";
    public String currentAuthRole = "";
    public String currentAuthYesAction = "";
    public String currentAuthNoAction = "";
    //
    private JEditorPane editorPane = null;
    private boolean viewXMLFlag = false;
    private JScrollPane sp;
    public boolean htmlWithPhoto = false;

    /**
     *
     * @param authSNo
     */
    public void setCurrentAuthSNo(int authSNo) {
        currentAuthSNo = authSNo;
    }

    /**
     *
     * @return
     */
    public DigSignAuth getCurrentAuth() {
        DigSignAuth auth = null;
        if (currentAuthSNo > -1) {
            auth = authList[currentAuthSNo];
        }
        return auth;
    }

    /**
     *
     * @param auth
     * @return ''
     */
    public void addAuth(DigSignAuth auth) {
        currentAuthSNo = numAuths;
        authList[numAuths] = auth;
        numAuths++;
    }

    /**
     *
     * @param auth
     * @return ''
     */
    public int addNewAuth(DigSignAuth auth) {
        currentAuthSNo = numAuths;
        authList[numAuths] = auth;
        authList[numAuths].role = currentAuthRole;
        return numAuths++;
    }

    /**
     *
     */
    public String setYesAction() {
        authList[currentAuthSNo].remarks = "";
        authList[currentAuthSNo].sign = null;
        authList[currentAuthSNo].signDate = "";
        authList[currentAuthSNo].action = currentAuthYesAction;
        refreshForm();
        return nextStatusCodeOnYes;
    }

    /**
     *
     */
    public String setNoAction() {
        String rem = JOptionPane.showInputDialog("Please enter reason:");
        if (rem != null) {
            authList[currentAuthSNo].remarks = rem;
        } else {
            authList[currentAuthSNo].remarks = currentAuthRole + " Rejected";
        }
        authList[currentAuthSNo].sign = null;
        authList[currentAuthSNo].signDate = "";
        authList[currentAuthSNo].action = currentAuthNoAction;
        refreshForm();
        return nextStatusCodeOnNo;
    }

    /**
     * show JTable with AuthName, Role, Action, SignDate, SignStatus
     */
    public void verifyAllSigns() {
        //TODO complete it
    }

    /**
     *
     * @param withPhoto
     */
    @SuppressWarnings("UseSpecificCatch")
    public void refreshForm() {
        if (viewXMLFlag) {
            editorPane.setContentType("text/plain");
            editorPane.setText(getXMLFull(getCurrentAuth(), currentAuthSNo));
        } else {
            String html = getHTMLFull();
            editorPane.setContentType("text/html");
            if (!htmlWithPhoto) {
                editorPane.setText(html);
            } else {
                try {//initialize the editorkit if there is any change in HTMLTable
                    editorPane.setEditorKit(new MyHTMLEditorKit());
                    HTMLEditorKit kit = (HTMLEditorKit) editorPane.getEditorKit();
                    Document doc = editorPane.getDocument();
                    StringReader reader = new StringReader(html);
                    kit.read(reader, doc, 0);
                } catch (Exception ex) {
                    editorPane.setText(html);
                    MyUtils.showException("HTMLEditorKit", ex);
                }
            }
        }
        editorPane.validate();
    }

    /**
     *
     */
    public void viewInXML() {
        viewXMLFlag = true;
        refreshForm();
    }

    /**
     *
     */
    public void viewInHTML() {
        viewXMLFlag = false;
        refreshForm();
    }

    /**
     *
     * @return ''
     */
    public JScrollPane getScrollPane(String heading) {
        editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        sp = new JScrollPane(editorPane);
        sp.setBorder(BorderFactory.createTitledBorder(heading));
        return sp;
    }

    /**
     *
     */
    public void gotoTop() {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e1) {
                }
                JScrollBar vsb = sp.getVerticalScrollBar();
                vsb.setValue(vsb.getMinimum());
                sp.validate();
                sp.repaint();
            }
        });
    }

    /**
     *
     * @throws Exception
     */
    public void doAuthSign() throws Exception {
        doAuthSign(currentAuthSNo);
    }

    /**
     *
     * @param authNo
     * @throws Exception
     */
    public void doAuthSign(int authNo) throws Exception {
        if (authList[authNo] == null) {
            throw new Exception("AuthObject NULL authNo=" + authNo);
        }
        
        //  TODO ACTUAL_SIGN
//        if (authList[authNo].certSerNo == null || authList[authNo].certSerNo.length() <= 4) {
//            throw new Exception("Certificate not uploaded at Server");
//        }
//        if (authList[authNo].cert == null) {
//            authList[authNo].cert = DigSignToken.getCurrentCertificate(authList[authNo].userID);
//        }
//        if (authList[authNo].cert == null) {
//            throw new Exception("No Certificate Found in Token");
//        }
//         if (!authList[authNo].certSerNo.equals(authList[authNo].cert.serialNumber)) {
//            throw new Exception("Certificate at Server does not match with Token");
//        }
        
        if (authList[authNo].action == null
                || authList[authNo].action.equals("")
                || authList[authNo].action.equals("PENDING")) {
            throw new Exception("AuthAction not set");
        }
        authList[authNo].signDate = todayDateTime;
        authList[authNo].sign = DigSignToken.signDocWithCurrentKey(getXMLForAuth(authNo));
    }

    /**
     *
     * @param authNo
     * @throws Exception
     */
    public VerifyResult verifyAuthSign(int authNo) throws Exception {
        //  TODO ACTUAL_SIGN
//        if (authList[authNo].cert == null) {
//            authList[authNo].cert = CertificateClient.getCertFromCertSerNo(authList[authNo].certSerNo);
//        }
        VerifyResult result = authList[authNo].cert.verifySign(
                getXMLForAuth(authNo),
                authList[authNo].sign);
        return result;
    }

    /**
     *
     * @param authNo
     * @return ''
     */
    private String getXMLForAuth(int authNo) {
        String xml =
                "<ORGANIZATION>" + ORG_NAME + "</ORGANIZATION>\n"
                + "<HEAD>" + applicationFullName + "</HEAD>\n"
                + "<REFERENCE_NO>" + appShortName + "/" + formID + "</REFERENCE_NO>\n"
                + "<APPLY_DATE>" + applyDate + "</APPLY_DATE>\n"
                + "<APPLICANT_EMP_NO>" + applicant.userID + "</APPLICANT_EMP_NO>\n"
                + getBasicDataXML(authList[authNo], authNo)
                + getAuthTagXML(authList[authNo]);
        return xml;
    }

    /**
     * if authSNO == -1 give basic xml irrespective of currentAuthRole
     *
     * @return ''
     */
    protected abstract String getBasicDataXML(DigSignAuth auth, int authSNo);

    /**
     *
     * @return ''
     */
    private String getXMLFull(DigSignAuth auth, int authSNo) {
        StringBuilder xml = new StringBuilder(getBasicDataXML(auth, authSNo));
        for (int i = 0; i < numAuths; i++) {
            xml.append("\n");
            xml.append(getAuthTagXML(authList[i]));
        }
        return xml.toString();
    }

    /**
     *
     * @param auth
     * @return ''
     */
    protected String getAuthTagXML(DigSignAuth auth) {
        return "<" + auth.role + ">\n"
                + "<EMPNO>" + auth.userID + "</EMPNO>\n"
                + "<ACTION>" + auth.action + "</ACTION>\n"
                + "<SIGN_DATE>" + auth.signDate + "</SIGN_DATE>\n"
                + "<CERT SNO>" + auth.certSerNo + "</CERT SNO>\n"
                + "</" + auth.role + ">\n";
    }
    
    protected abstract String getFileName();
    
    protected abstract String getBasicDataHTML();
    
    public String getHTMLFull() {
      //  MyUtils.showMessage("Hello world");
        StringBuilder html = new StringBuilder("<HTML>");
        html.append("<BODY><head></head>");
        //Header
        html.append(getHeaderTagHTML());

        //Applicant
        html.append(getApplicantTagHTML());

        //form's basic data
        html.append(getBasicDataHTML());

        //auths
        for (int i = 0; i < numAuths; i++) {
            html.append(getAuthTagHTML(i));
        }
        HTMLTable table = new HTMLTable(8);
        table.newRow();
        table.addLabelCell("Current Status:", 150, 1, 1);
        table.addDataCell(currentStatus, 550, 1, 1);
        table.newRow();
        table.addLabelCell("Remarks:", 1, 1);
        table.addDataCell(remarks, 1, 1);
        //status and remarks
        html.append(table.getTable());
        html.append("</BODY></HTML>");
        return html.toString();
    }

    /**
     *
     * @return
     */
    protected String getHeaderTagHTML() {
        HTMLTable headerTable = new HTMLTable(8);
        //ORG_NAME
        headerTable.newRow();
        headerTable.addCellNoBorder("<h1>" + ORG_NAME + "</h1>", HTMLTable.WIDTH, 2, "center");
        //APPLICATION_NAME
        headerTable.newRow();
        headerTable.addCellNoBorder("<h2>" + applicationFullName + "</h2>", HTMLTable.WIDTH, 2, "center");
        //RefNo and Date
        headerTable.newRow();
        headerTable.addCellNoBorder("<h3>RefNo:" + formID + "</h3>", HTMLTable.WIDTH / 2, 1, "left");
        headerTable.addCellNoBorder("<h3>Date:" + applyDate + "</h3>", HTMLTable.WIDTH / 2, 1, "right");
        return headerTable.getTable();
    }

    /**
     *
     * @param authNo
     * @return
     */
    protected String getAuthTagHTML(int authNo) {
        String actionTD = authList[authNo].action;
        String ramarkTD = authList[authNo].remarks;
        if (authNo == currentAuthSNo) {
            actionTD = "<font color='blue'>" + authList[authNo].action + "</font>";
            ramarkTD = "<font color='blue'>" + authList[authNo].remarks + "</font>";
        }
        int w1 = 130, w2 = 220, w3 = 130, w4 = 220;
        HTMLTable authTable = new HTMLTable(8);
        authTable.newRow();
        authTable.addCellNoBorder("<b>" + authList[authNo].role + ":</b>", 0, 4, "left");
        authTable.newRow();
        authTable.addLabelCell("UserID: ", w1, 1, 1);
        authTable.addDataCell(authList[authNo].userID, w2, 1, 1);
        authTable.addLabelCell("Name: ", w3, 1, 1);
        authTable.addDataCell(authList[authNo].name, w4, 1, 1);
        authTable.newRow();
        authTable.addLabelCell("Action: ", 1, 1);
        authTable.addDataCell(actionTD, 1, 1);
        authTable.addLabelCell("Date: ", 1, 1);
        authTable.addDataCell(authList[authNo].signDate, 1, 1);
        authTable.newRow();
        authTable.addLabelCell("Remarks: ", w1, 1, 1);
        authTable.addDataCell(ramarkTD, (w2 + w3 + w4), 1, 3);
        return authTable.getTable();
    }

    /**
     *
     * @return
     */
    protected String getApplicantTagHTML() {
        int w1 = 130, w2 = 220, w3 = 130, w4 = 220;
        
        HTMLTable applicantTable = new HTMLTable(0);
        applicantTable.newRow();
        applicantTable.addCellNoBorder("<b>Applicant</b>", 0, 1, "left");
        applicantTable.newRow();
        applicantTable.addLabelCell("Applicant EmpNo: ", w1, 1, 1);
        applicantTable.addDataCell(applicant.userID, w2, 1, 1);
        applicantTable.addLabelCell("Name: ", w3, 1, 1);
        applicantTable.addDataCell(applicant.name, w4, 1, 1);
        applicantTable.newRow();
        applicantTable.addLabelCell("Designation: ", 1, 1);
        applicantTable.addDataCell(applicant.designation, 1, 1);
        applicantTable.addLabelCell("Phone/Email: ", 1, 1);
        applicantTable.addDataCell(applicant.phoneNo + "/" + applicant.email, 1, 1);
        return applicantTable.getTable();
    }

    /**
     */
    public void saveAtClientPC() {
        try {
            String data = getHTMLFull();
            String fileName = getFileName();
            
            File f = chooseFileForSave(fileName);
            if (f == null) {
                MyUtils.showMessage("Not Saved.");
                return;
            }
            try (FileWriter fw = new FileWriter(f)) {
                fw.write(data);
            }
            MyUtils.showMessage("Form saved at: " + f.getAbsolutePath());
        } catch (Exception e) {
            MyUtils.showException("SaveForm", e);
        }
    }

    /**
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    private File chooseFileForSave(String fileName) throws Exception {
        while (true) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            File dir = chooser.getCurrentDirectory();
            chooser.setSelectedFile(new File(dir, fileName));
            int returnVal = chooser.showSaveDialog(new Frame());
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return null;
            }
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile.exists()) {
                int choice = JOptionPane.showConfirmDialog(
                        chooser,
                        "File Already Exists. Do you want to eplace it?");
                if (choice == JOptionPane.NO_OPTION) {
                    continue;
                } else if (choice == JOptionPane.CANCEL_OPTION) {
                    return null;
                }
            }
            return selectedFile;
        }
    }
}
