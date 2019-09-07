package SmartTimeApplet.visitor.vis_app;

import java.io.Serializable;
import lib.digsign.DigSignAuth;
import lib.digsign.DigSignForm;
import lib.digsign.DigSignUser;
import lib.digsign.html_form.HTMLTable;
import lib.session.MyHTTP;
import lib.session.MyUtils;
import lib.utils.Packetizer;

/**
 * VisitorForm.java
 */
public class VisitorForm extends DigSignForm implements Serializable {
//    public static final String servletName = "VisitorServlet";
    public static final String servletName = "VisAppServlet";
    public static final String shortName = "CEP";
    public static final String fullName = "Casual Entry Permit";
    
    public String dateFrom = "";
    public String dateTo = "";
    public String expectedGate = "";
    public String purpose = "";
    public String places = "";
    public Company company = new Company();
    public Visitor[] visitorList;
    public DigSignUser[] hostList;
    //approver1 and approver2 for submit form
    public DigSignUser approver1;
    public DigSignUser approver2;
    /**
     * 
     */
    public VisitorForm() {
        applicationFullName = fullName;
        appShortName = shortName;
    }

    /**
     *
     * @return
     */
    @Override
    protected String getFileName() {
        return "VisitorForm_" + formID + ".html";
    }

    /**
     *
     * @param formID
     * @return
     * @throws Exception
     */
    public static VisitorForm getForm(String formID) throws Exception {
        MyHTTP myHTTP = MyUtils.createServletConnection(servletName);
        myHTTP.openOS();
//        myHTTP.println("getVisitorForm");
        myHTTP.println("getVisAppform");
        myHTTP.println(formID);
        myHTTP.closeOS();
        //
        myHTTP.openIS();
        VisitorForm form = (VisitorForm) myHTTP.readObject();
        myHTTP.closeIS();
        //
        if (form.servletResponse.startsWith("ERROR")) {
            throw new Exception(form.servletResponse);
        }
        return form;
    }


    /**
     * 
     * @param newStatusCode
     * @return 
     */
    public void saveAfterAuthSign(String newStatusCode) throws Exception{
            if (authList[currentAuthSNo].sign == null) {
                throw new Exception("Document is not signed!");
            }
            Packetizer p = new Packetizer();
            p.addString(formID);
             p.addString(currentStatusCode);//old current status for Synch purpose
            p.addString(newStatusCode);
            p.addInt(currentAuthSNo);
            p.addString(authList[currentAuthSNo].userID);
            p.addString(authList[currentAuthSNo].name);
            p.addString(authList[currentAuthSNo].designation);
            p.addString(authList[currentAuthSNo].role);
            p.addString(authList[currentAuthSNo].action);
            p.addString(authList[currentAuthSNo].remarks);
            p.addString(authList[currentAuthSNo].certSerNo);
            p.addString(authList[currentAuthSNo].signDate);
            p.addString(authList[currentAuthSNo].sign);
            MyHTTP myHTTP = MyUtils.createServletConnection(servletName);
            myHTTP.openOS();
            myHTTP.println("submitFormSigned");
            myHTTP.println(p.getPacket());
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                throw new Exception(response);
            } 
            MyUtils.showMessage(response);
    }

    /**
     *
     * @param form_id
     * @return
     */
    public static String getShortDetails(String form_id) {
        return "Visitor  Application - " + form_id;
    }

    /**
     * returns Content of HTML BODY
     */
    @Override
    protected String getBasicDataHTML() {
        int w1 = 130, w2 = 220, w3 = 130, w4 = 220;
        //visitDetailTable
        HTMLTable visitDetailTable = new HTMLTable(2);
        visitDetailTable.newRow();
        visitDetailTable.addCellNoBorder("<b>Visit Detail</b>", 0, 4, "left");
        visitDetailTable.newRow();
        visitDetailTable.addLabelCell("From: ", w1, 1, 1);
        visitDetailTable.addDataCell(dateFrom, w2, 1, 1);
        visitDetailTable.addLabelCell("To: ", w3, 1, 1);
        visitDetailTable.addDataCell(dateTo, w4, 1, 1);
        visitDetailTable.newRow();
        visitDetailTable.addLabelCell("Purpose: ", 1, 1);
        visitDetailTable.addDataCell(purpose, 1, 3);
        visitDetailTable.newRow();
        visitDetailTable.addLabelCell("Location: ", 1, 1);
        visitDetailTable.addDataCell(places, 1, 3);

        //companyTable
        HTMLTable companyTable = new HTMLTable(2);
        companyTable.newRow();
        companyTable.addCellNoBorder("<b>Company Detail</b>", 0, 4, "left");
        companyTable.newRow();
        companyTable.addLabelCell("Company ID: ", w1, 1, 1);
        companyTable.addDataCell(company.companyID, w2, 1, 1);
        companyTable.addLabelCell("Company Name: ", w3, 1, 1);
        companyTable.addDataCell(company.companyName, w4, 1, 1);
        companyTable.newRow();
        companyTable.addLabelCell("Address: ", 1, 1);
        companyTable.addDataCell(company.address, 1, 3);

        int v1 = 25, v2 = 75, v3 = 280, v4 = 220, v5 = 100;
        //visitors Table
        HTMLTable visitorsTab = new HTMLTable(2);
        visitorsTab.newRow();
        visitorsTab.addCellNoBorder("<b>Visitors:</b>", 0, 4, "left");
        visitorsTab.newRow();
        visitorsTab.addLabelCell("S.No: ", v1, 1, 1);
        visitorsTab.addLabelCell("PVIS_ID: ", v2, 1, 1);
        visitorsTab.addLabelCell("Name: ", v3, 1, 1);
        visitorsTab.addLabelCell("Occupation: ", v4, 1, 1);
        visitorsTab.addLabelCell("PVC: ", v5, 1, 1);
        for(int i=0;i<visitorList.length;i++){
            visitorsTab.newRow();
            visitorsTab.addDataCell(visitorList[i].visSNo, 1, 1);
            visitorsTab.addDataCell(visitorList[i].pvisID, 1, 1);
            visitorsTab.addDataCell(visitorList[i].name, 1, 1);
            visitorsTab.addDataCell(visitorList[i].desig, 1, 1);
            visitorsTab.addDataCell(visitorList[i].pvcYesNo, 1, 1);
        }
        
        //hosts Table
        HTMLTable hostsTab = new HTMLTable(2);
        hostsTab.newRow();
        hostsTab.addCellNoBorder("<b>Hosts:</b>", 0, 4, "left");
        hostsTab.newRow();
        hostsTab.addLabelCell("S.No: ", v1, 1, 1);
        hostsTab.addLabelCell("EmpNo: ", v2, 1, 1);
        hostsTab.addLabelCell("Name: ", v3, 1, 1);
        hostsTab.addLabelCell("Designation: ", v4, 1, 1);
        hostsTab.addLabelCell("Phone: ", v5, 1, 1);
        for(int i=0;i<hostList.length;i++){
            hostsTab.newRow();
            hostsTab.addDataCell(hostList[i].hostSNo, 1, 1);
            hostsTab.addDataCell(hostList[i].userID, 1, 1);
            hostsTab.addDataCell(hostList[i].name, 1, 1);
            hostsTab.addDataCell(hostList[i].designation, 1, 1);
            hostsTab.addDataCell(hostList[i].phoneNo, 1, 1);
        }
        return visitDetailTable.getTable()
               + companyTable.getTable()
                + visitorsTab.getTable()
                + hostsTab.getTable();
    }

    /**
     *
     * @param auth
     * @param authSNo
     * @return
     */
    @Override
    protected String getBasicDataXML(DigSignAuth auth, int authSNo) {
        String xml =
                "<FROM_DATE>" + dateFrom + "</FROM_DATE>\n"
                + "<TO_DATE>" + dateTo + "</TO_DATE>\n"
                + "<PURPOSE>" + purpose + "</PURPOSE>\n"
                + "<PLACES>" + places + "</PLACES>\n"
                + "<COMPANY_DETAILS>\n"
                + "<COMPANY ID>" + company.companyID + "</COMPANY ID>\n"
                + "</COMPANY_DETAILS>\n"
                + "<VISITORS_DETAILS>\n";
        for (int i = 0; i < visitorList.length; i++) {
            xml += "<VISITOR_SNO>" + visitorList[i].visSNo + "</VISITOR SNO>\n"
                    + "<VISITOR_ID>" + visitorList[i].pvisID + "</VISITOR_ID>\n";
        }
        xml += "</VISITORS_DETAILS>\n"
                + "<HOSTS_DETAILS>\n";
        for (int i = 0; i < hostList.length; i++) {
            xml += "<HOST_SNO>" + hostList[i].hostSNo + "</HOST_SNO>\n"
                    + "<HOST_EMPNO>" + hostList[i].userID + "</HOST_EMPNO>\n";
        }
        xml += "</HOSTS_DETAILS>\n";
        return xml;
    }

//    @Override
//    protected String getVisitorDataHTML1() {
//        int w1 = 120, w2 = 220, w3 = 60, w4 = 220, w5 = 100;
//        HTMLTable visHeaderTable = new HTMLTable(8);
//        visHeaderTable.newRow();
//        visHeaderTable.addLabelCellNoBorder("<b> Visitor Details </b>", 1, 4);
//
//        HTMLTable visitorTable = new HTMLTable(0);
//        visitorTable.newRow();
//        visitorTable.addLabelCell("Visitor ID: ", w1, 1, 1);
//        visitorTable.addLabelCell("Name: ", w2, 1, 1);
//        visitorTable.addLabelCell("SEX: ", w3, 1, 1);
//        visitorTable.addLabelCell("Designation: ", w4, 1, 1);
//        visitorTable.addLabelCell("Having PVC: ", w5, 1, 1);
//        for (int i = 0; i < visitorList.length; i++) {
//            visitorTable.newRow();
//            visitorTable.addDataCell(visitorList[i].pvisID, w1, 1, 1);
//            visitorTable.addDataCell(visitorList[i].name, w2, 1, 1);
//            visitorTable.addDataCell(visitorList[i].sex, w3, 1, 1);
//            visitorTable.addDataCell(visitorList[i].desig, w4, 1, 1);
//            visitorTable.addDataCell(visitorList[i].pvcYesNo, w5, 1, 1);
//        }
//        return visHeaderTable.getTable() + visitorTable.getTable();
//    }
//
//    @Override
//    protected String getHostDataHTML1() {
//        int w1 = 120, w2 = 220, w3 = 60, w4 = 220;
//        HTMLTable hostHeaderTable = new HTMLTable(8);
//        hostHeaderTable.newRow();
//        hostHeaderTable.addLabelCellNoBorder("<b> Officers to be Visited </b>", 1, 4);
//
//        HTMLTable hostTable = new HTMLTable(0);
//        hostTable.newRow();
//        hostTable.addLabelCell("Emp No: ", w1, 1, 1);
//        hostTable.addLabelCell("Name: ", w2, 1, 1);
//        hostTable.addLabelCell("Designation: ", w3, 1, 1);
//        hostTable.addLabelCell("Phone/Email: ", w4, 1, 1);
//        for (int i = 0; i < hostList.length; i++) {
//            if (hostList[i] != null) {
//                hostTable.newRow();
//                hostTable.addDataCell(hostList[i].userID, w1, 1, 1);
//                hostTable.addDataCell(hostList[i].name, w2, 1, 1);
//                hostTable.addDataCell(hostList[i].designation, w3, 1, 1);
//                hostTable.addDataCell(hostList[i].phoneNo + "/" + hostList[i].email, w4, 1, 1);
//            } else {
//                break;
//            }
//        }
//        return hostHeaderTable.getTable() + hostTable.getTable();
//    }
}