/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.visitor.servlet;

import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javacodes.connection.*;
import javacodes.login.LoginSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author nbpatil
 */
public class VisitorServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String remoteAddr = req.getRemoteAddr();
        try {
            resp.setContentType("text/plain");
            PrintWriter out = resp.getWriter();
            out.println("Error: this servlet does not support the GET method!");
            out.close();
        } catch (Exception e) {
            System.out.println("GET request from " + remoteAddr);
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        MyHTTPConnection myHTTPConn = null;
        LoginSession loginSession = null;
        try {

            resp.setContentType("text/plain");
            myHTTPConn = new MyHTTPConnection(req, resp);

            HttpSession session = req.getSession(false);
            int[] reqTypes = {
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            loginSession = LoginSession.getLoginSession(session, reqTypes);

            String ReqType = myHTTPConn.readLine();
            switch (ReqType) {
                case "addNewCompany":
                    addNewCompany(myHTTPConn, loginSession);
                    break;
                case "getCmpInfo":
                    getCmpInfo(myHTTPConn, loginSession);
                    break;
                case "AddCEP":
                    AddCEP(myHTTPConn, loginSession);
                    break;
                case "FillForm":
                    FillForm(myHTTPConn, loginSession);
                    break;
                case "getCompanyInfo":
                    getCompanyInfo(myHTTPConn, loginSession);
                    break;
                case "getCmpId":
                    getCmpId(myHTTPConn, loginSession);
                    break;
                case "getComboList":
                    getComboList(myHTTPConn, loginSession);
                    break;
                case "deleteCompanyInfo":
                    deleteCompanyInfo(myHTTPConn, loginSession);
                    break;
                case "getComboVisitor":
                    getComboVisitor(myHTTPConn, loginSession);
                    break;
                case "updateCmpInfo":
                    updateCmpInfo(myHTTPConn, loginSession);
                    break;
                case "addNewVisitor":
                    addNewVisitor(myHTTPConn, loginSession);
                    break;
                case "VstFillForm":
                    VstFillForm(myHTTPConn, loginSession);
                    break;
                case "updateVisitorInfo":
                    updateVisitorInfo(myHTTPConn, loginSession);
                    break;
                case "showVisitorInfo":
                    showVisitorInfo(myHTTPConn, loginSession);
                    break;
                case "getVisitorInfo":
                    getVisitorInfo(myHTTPConn, loginSession);
                    break;
                case "getVisitorId":
                    getVisitorId(myHTTPConn, loginSession);
                    break;
                case "getCmpVisitorInfo":
                    getCmpVisitorInfo(myHTTPConn, loginSession);
                    break;
                case "getHost":
                    getHost(myHTTPConn, loginSession);
                    break;
                case "getApplicant":
                    getApplicant(myHTTPConn, loginSession);
                    break;
                case "getApprover":
                    getApprover(myHTTPConn, loginSession);
                    break;
                case "getComboboxList":
                    getComboboxList(myHTTPConn, loginSession);
                    break;
                case "AddHost":
                    AddHost(myHTTPConn, loginSession);
                    break;
            }
        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }
    }
//Fill The Form for updatation

    public void FillForm(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR: Could not get database connection");
                return;
            }
            String PCOM_ID = myHTTPConn.readLine();
            Packetizer p = new Packetizer();
            query = "SELECT "
                    + "PCOM_ID, "
                    + "ComName, "
                    + "ComAddr, "
                    + "ComType, "
                    + "City, "
                    + "State, "
                    + "Pin, "
                    + "PhoneNo1, "
                    + "PhoneNo2, "
                    + "PhoneNo3 "
                    + "FROM "
                    + "vis_app_company "
                    + "WHERE "
                    + "PCOM_ID='" + PCOM_ID + "'";
            ResultSet rs = prstmnt.executeQuery(query);
            p.setCounter();
            if (rs.next()) {

                for (int i = 1; i < 11; i++) {
                    p.addString(rs.getString(i));
                }
                p.incrCounter();
            }
            rs.close();
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }
    //Add New Company Detail 

    public void addNewCompany(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        //DBConnection conn = null;
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            String Packet = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(Packet);

//            String CmpId = dp.getString().
            String cmpid = dp.getString();
            String cmpname = dp.getString();
            String add = dp.getString();
            int cmptyp = dp.getInt();
            String city = dp.getString();
            String state = dp.getString();
            String pin = dp.getString();
            String phnum1 = dp.getString();
            String phnum2 = dp.getString();
            String phnum3 = dp.getString();
            String AddedBy = loginSession.getUserID();
            //String Mdate="";


            query = "INSERT INTO "
                    + "vis_app_company(PCOM_ID,ComName,ComAddr,ComType,City,State,Pin,PhoneNo1,PhoneNo2,PhoneNo3,AddedBy) Values('"
                    + cmpid + "','"
                    + cmpname + "','"
                    + add + "','"
                    + cmptyp + "','"
                    + city + "','"
                    + state + "','"
                    + pin + "','"
                    + phnum1 + "','"
                    + phnum2 + "','"
                    + phnum3 + "','"
                    + AddedBy + "')";


            //System.out.println("Insert query: " + query);
            int executeUpdate = prstmnt.executeUpdate(query);
            myHTTPConn.println("addnew Company");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }


    }

    //Dispaly CompanyInformation 
    public void getCmpInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            //String companyid=myHTTPConn.readLine();

            query =
                    "SELECT "
                    + "PCOM_ID, "
                    + "ComName, "
                    + "City, "
                    + "ComAddr "
                    + "FROM "
                    + "vis_app_company";
            //System.out.println("find query:" + query);
            ResultSet rs = prstmnt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                p.addString(rs.getString(4));
                p.incrCounter();

            }
            rs.close();
            myHTTPConn.println(p.getPacket());

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }
    //For Get the CompanyID 

    //getComanyId For ApplyCep
    public void getCmpId(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            query = "SELECT PCOM_ID from vis_app_company ORDER BY MDate DESC";
            System.out.println("query:" + query);
            String strComId = "";
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                if (rs.next()) {
                    strComId = rs.getString(1);
                }
            }
            myHTTPConn.println(strComId);
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }
    //For Get the CompanyID 

    public void getCompanyInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR: Database could not connect");
                return;
            }
            String PCOM_ID = myHTTPConn.readLine();
            // MyUtils.showMessage(companyid);
            Packetizer p = new Packetizer();
            query = "SELECT PCOM_ID,ComName,ComAddr,ComType,City,State,Pin FROM vis_app_company WHERE PCOM_ID='" + PCOM_ID + "'";


            ResultSet rs = prstmnt.executeQuery(query);
            rs.next();
            for (int i = 1; i < 8; i++) {
                p.addString(rs.getString(i));
            }
            rs.close();
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }


    }

    public void getComboList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            //get connection

//                    String divcode = myHTTPConn.readLine();
            query = "SELECT "
                    + "PCOM_ID, "
                    + "ComName "
                    + "FROM "
                    + "vis_app_company ";

            //System.out.println("getComboList : " + query);
            Packetizer p;
            ResultSet rs = prstmnt.executeQuery(query);
            p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.incrCounter();
            }
            rs.close();
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }
//Get CompanyVisitorInfo

    public void getCmpVisitorInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR: Database could not connect");
                return;
            }
            String PCOM_ID = myHTTPConn.readLine();
            // MyUtils.showMessage(companyid); 

            Packetizer p = new Packetizer();
            query = "Select vis_app_company.ComName,vis_app_company.ComAddr,vis_app_company.City,vis_app_visitors.VisName,vis_app_visitors.PVIS_ID,vis_app_visitors.Designation from vis_app_company left JOIN vis_app_visitors ON vis_app_company.PCOM_ID='" + PCOM_ID + "'";

            System.out.println("query : " + query);
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                rs.next();
                for (int i = 1; i < 7; i++) {
                    p.addString(rs.getString(i));
                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {

            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }


    }

    public void updateCmpInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {

        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            //int companyid = 0;

            String Packet = myHTTPConn.readLine();
            //String PCOM_ID = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(Packet);

            String cmpid = dp.getString();
            String cmpname = dp.getString();
            String address = dp.getString();
            int cmptyp = dp.getInt();
            String city = dp.getString();
            String state = dp.getString();
            String pin = dp.getString();
            String phnum = dp.getString();
            String phnum2 = dp.getString();
            String phnum3 = dp.getString();
            // String addedby = dp.getString();

            query = "UPDATE vis_app_company SET "
                    + " PCOM_ID='" + cmpid
                    + "',ComName='" + cmpname
                    + "',ComAddr='" + address
                    + "',ComType='" + cmptyp
                    + "',City='" + city
                    + "',State='" + state
                    + "',Pin='" + pin
                    + "',PhoneNo1='" + phnum
                    + "',PhoneNo2='" + phnum2
                    + "',PhoneNo3='" + phnum3 + "' WHERE PCOM_ID='" + cmpid + "'";
            System.out.println("update query: " + query);
            prstmnt.executeUpdate(query);

            //setEmpStatus(myHTTPConn, "" + cmpname, loginSession);
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }
    //delete company from compnayinformation

    public void deleteCompanyInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {

        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR: Database could not connect");
                return;
            }
            String PCOM_ID = myHTTPConn.readLine();
            query = "DELETE FROM vis_app_company WHERE PCOM_ID='" + PCOM_ID + "'";
            int executeUpdate = prstmnt.executeUpdate(query);
            myHTTPConn.println("Deleted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }



    }

//Add For New Visitor
    public void addNewVisitor(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        // String pvisid = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String Packet = myHTTPConn.readLine();
            String EmpName = loginSession.getUserID();
            Depacketizer dp = new Depacketizer(Packet);


            String pvisid = dp.getString();
            String cmpId = dp.getString();
            String visitornm = dp.getString();
            String design = dp.getString();
            int pvc = dp.getInt();
            System.out.println("pvc+pvc");
            String pvc_validitiy = dp.getString();
            System.out.println("pvc_validitiy+pvc_validitiy");
            String idProof = dp.getString();
            System.out.println("idProof+idProof");

            int sex = dp.getInt();
            String country = dp.getString();
            String VphNum = dp.getString();
            /*
             * DateFormat dateFormat = new SimpleDateFormat("ddMMyy"); Date date
             * = new Date(); String currdate = dateFormat.format(date);
             *
             * query = "SELECT COUNT(*) from vis_app_visitors order by MDate DESC";
             * System.out.println("query" + query); ResultSet rs =
             * prstmnt.executeQuery(query); if (rs.next()) { String strconcat
             * =rs.getString(1); System.out.println("Total:" + strconcat); int
             * count; count = Integer.parseInt(strconcat) + 1;
             *
             * String strCount = "000" + count;
             *
             *
             * String App1 = strCount.substring((strCount.length() -
             * 3),strCount.length()); pvisid = "V" + currdate + App1;
             * System.out.println("pvisid" + pvisid);
             *
             *
             *
             * }
             *
             */
            query = "INSERT INTO "
                    + "vis_app_visitors(PVIS_ID,PCOM_ID,VisName,Designation,PVC,PVC_Validity,IDproof,Sex,Nationality,AddedBy,VPhoneNo) Values('"
                    + pvisid + "','"
                    + cmpId + "','"
                    + visitornm + "','"
                    + design + "','"
                    + pvc + "','"
                    + pvc_validitiy + "','"
                    + idProof + "','"
                    + sex + "','"
                    + country + "','"
                    + EmpName + "','"
                    + VphNum + "')";

            System.out.println("Insert query: " + query);
            int executeUpdate = prstmnt.executeUpdate(query);
            myHTTPConn.println("addnew Visitor");


        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }


    }
//For Fill Form for update VisitorInformation

    public void VstFillForm(MyHTTPConnection myHTTPConn, LoginSession loginSession) {

        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {

            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            String PVIS_ID = myHTTPConn.readLine();
            Packetizer p = new Packetizer();

            query = "SELECT "
                    + "PVIS_ID, "
                    + "PCOM_ID, "
                    + "VisName, "
                    + "Designation, "
                    + "PVC, "
                    + "PVC_Validity, "
                    + "IDproof, "
                    + "Sex, "
                    + "Nationality, "
                    + "VPhoneNo "
                    + "FROM "
                    + "vis_app_visitors WHERE PVIS_ID='" + PVIS_ID + "'";
            System.out.println("query" + query);

            p.setCounter();
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                if (rs.next()) {
                    for (int i = 1; i < 11; i++) {
                        p.addString(rs.getString(i));
                    }
                    p.incrCounter();
                }
            }
            myHTTPConn.println(p.getPacket());

        } catch (Exception e) {

            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    public void updateVisitorInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {

        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }


            String Packet = myHTTPConn.readLine();
            //String PCOM_ID = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(Packet);

            String PVsId = dp.getString();
            String CmpId = dp.getString();
            String VName = dp.getString();
            String VDesg = dp.getString();
            int Vpvc = dp.getInt();
            String VPvcValidity = dp.getString();
            String vIdProof = dp.getString();
            int Vsex = dp.getInt();
            String Vcntry = dp.getString();
            String Vph = dp.getString();
            // String addedby = dp.getString();

            query = "UPDATE vis_app_visitors SET "
                    + " PVIS_ID='" + PVsId
                    + "',PCOM_ID='" + CmpId
                    + "',VisName='" + VName
                    + "',Designation='" + VDesg
                    + "',PVC='" + Vpvc
                    + "',PVC_Validity='" + VPvcValidity
                    + "',IDproof='" + vIdProof
                    + "',Sex='" + Vsex
                    + "',Nationality='" + Vcntry
                    + "',VPhoneNo='" + Vph + "' WHERE PVIS_ID='" + PVsId + "'";
            System.out.println("query:  of Update VisitoriNfo" + query);
            prstmnt.executeUpdate(query);

            //setEmpStatus(myHTTPConn, "" + cmpname, loginSession);
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    public void showVisitorInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {

            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            //String PVIS_ID = myHTTPConn.readLine();
            //Packetizer p = new Packetizer();

            query = "SELECT "
                    + "PVIS_ID, "
                    + "PCOM_ID, "
                    + "VisName, "
                    + "Designation "
                    + "FROM "
                    + "vis_app_visitors";
            System.out.println("select query" + query);
            ResultSet rs = prstmnt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                p.addString(rs.getString(4));
                p.incrCounter();

            }
            rs.close();
            myHTTPConn.println(p.getPacket());

        } catch (Exception e) {

            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    public void getVisitorInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {

            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            String PCOM_ID = myHTTPConn.readLine();
            //Packetizer p = new Packetizer();

            query = "SELECT "
                    + "PVIS_ID, "
                    + "PCOM_ID, "
                    + "VisName, "
                    + "Designation, "
                    + "Sex, "
                    + "PVC "
                    + "FROM "
                    + "vis_app_visitors WHERE PCOM_ID='" + PCOM_ID + "'";
            System.out.println("select query" + query);
            ResultSet rs = prstmnt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                p.addString(rs.getString(4));
                p.addString(rs.getString(5));
                p.addString(rs.getString(6));
                p.incrCounter();

            }
            rs.close();
            myHTTPConn.println(p.getPacket());

        } catch (Exception e) {

            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    //getVisitorId From Visitor Table
    public void getVisitorId(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        String strVstId = "";
        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            query = "SELECT COUNT(*) from vis_app_visitors order by MDate DESC";
            System.out.println("query:" + query);
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                if (rs.next()) {
                    strVstId = rs.getString(1);
                }

            }
            myHTTPConn.println(strVstId);
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    //Get Visitor According to Company
    public void getComboVisitor(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {

            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            String PCOM_ID = myHTTPConn.readLine();
            //Packetizer p = new Packetizer();

            query = "SELECT PVIS_ID,VisName from vis_app_visitors WHERE PCOM_ID='" + PCOM_ID + "'";

            System.out.println("select query" + query);
            Packetizer p;
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                p = new Packetizer();
                p.setCounter();
                while (rs.next()) {
                    p.addString(rs.getString(1) + " " + rs.getString(2));
                    p.incrCounter();

                }
            }
            myHTTPConn.println(p.getPacket());

        } catch (Exception e) {

            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

//get Host Information for Apply Cep
    public void getHost(MyHTTPConnection myHTTPConn, LoginSession loginSession) {

        DBConnection conn = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            conn = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) conn.prepareStatement(query);
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;

            }
            String EmpCode = myHTTPConn.readLine();
            query = "SELECT  EmpCode,EmpName,designation,Division FROM employeemaster WHERE EmpCode='" + EmpCode + "'";
            Packetizer p = new Packetizer();
            p.setCounter();
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                if (rs.next()) {
                    for (int i = 1; i < 5; i++) {
                        p.addString(rs.getString(i));
                    }
                    p.incrCounter();
                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, conn);
        }
    }

    //Get Applicant Information From Employeemaster for Applying Cep
    public void getApplicant(MyHTTPConnection myHTTPConn, LoginSession loginSession) {

        DBConnection conn = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            conn = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) conn.prepareStatement(query);
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;

            }
            String EmpCode = myHTTPConn.readLine();

            query = "SELECT  EmpCode,EmpName,designation,Division FROM employeemaster WHERE EmpCode='" + EmpCode + "'";
            Packetizer p = new Packetizer();
            p.setCounter();
            try (ResultSet rs = prstmnt.executeQuery(query)) {


                if (rs.next()) {
                    for (int i = 1; i < 5; i++) {
                        p.addString(rs.getString(i));

                    }
                    p.incrCounter();
                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, conn);
        }
    }

    //Set Approver Information From Employeemaster for Applying Cep
    public void getApprover(MyHTTPConnection myHTTPConn, LoginSession loginSession) {

        DBConnection conn = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            conn = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) conn.prepareStatement(query);
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;

            }
            // String EmpCode = loginSession.getUserID();

            //String EmpCode = myHTTPConn.readLine();
            query = "SELECT  EmpCode,EmpName,Division FROM employeemaster";

            System.out.println("getApproverList : " + query);

            Packetizer p = new Packetizer();
            p.setCounter();
            try (ResultSet rs = prstmnt.executeQuery(query)) {

                while (rs.next()) {
                    p.addString(rs.getString(1));
                    p.addString(rs.getString(2));
                    p.addString(rs.getString(3));
                    p.incrCounter();
                }


            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, conn);
        }
    }

    //Fill Combobox for Approver
    public void getComboboxList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            conn = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) conn.prepareStatement(query);
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            //get connection

            //String EmpCode = loginSession.getUserID();
            query = "SELECT "
                    + "EmpCode, "
                    + "EmpName "
                    + "FROM "
                    + "employeemaster";

            System.out.println("getComboboxList : " + query);
            Packetizer p;
            ResultSet rs = prstmnt.executeQuery(query);
            p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.incrCounter();
            }
            rs.close();
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, conn);
        }
    }

    // AddHost for Applying Cep
    public void AddHost(MyHTTPConnection myHTTPConn, LoginSession loginSession) {

        DBConnection conn = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            conn = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) conn.prepareStatement(query);

            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String EmpCode = myHTTPConn.readLine();
            Packetizer p = new Packetizer();
            query = "SELECT  EmpCode,designation,Division,type FROM employeemaster WHERE EmpCode=" + EmpCode + "";
            ResultSet rs = prstmnt.executeQuery(query);
            rs.next();
            for (int i = 1; i < 5; i++) {
                p.addString(rs.getString(i));
            }
            rs.close();
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, conn);
        }

    }

// using for  apply add CEP
    public void AddCEP(MyHTTPConnection myHTTPConn, LoginSession loginSession) {

        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        // String ResultSet=null;

        try {
            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            //String AppDate=myHTTPConn.readLine();

            String Packet = myHTTPConn.readLine();
            String EmpName = loginSession.getUserID();
            //MyUtils.showMessage("Packet"+Packet);
            Depacketizer dp = new Depacketizer(Packet);

            String Vis_id = myHTTPConn.readLine();
            Depacketizer visitor = new Depacketizer(Vis_id);
            int size = visitor.getInt();

            //For CompanyDetails   
            String Cmp_id = dp.getString();
            //String Vis_id = dp.getString();
            int Applicant_id = dp.getInt();
            int host_id = dp.getInt();
            String purof_Vst = dp.getString();
            String plcof_Vst = dp.getString();
            String Exgate = dp.getString();
            String AppDate = dp.getString();
            //String ToDate = dp.getString();
            //String Approver_Id=dp.getString();
            int Approver_Id = Integer.parseInt(dp.getString());
            // String VisID="";
            String visappid = "";



            DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
            Date date = new Date();
            String currdate = "P" + dateFormat.format(date);


            query = "SELECT VisAppID from visapplication where VisAppID LIKE '" + currdate + "%'";
            System.out.println("query" + query);
            ResultSet rs = prstmnt.executeQuery(query);
            ArrayList<Integer> arrayList = new ArrayList<>();
            if (rs.next()) {
                rs.beforeFirst();
                while (rs.next()) {
                    String strconcat = rs.getString(1);
                    System.out.println("Total:" + strconcat);
                    String App1 = strconcat.substring((strconcat.length() - 3), strconcat.length());
                    arrayList.add(Integer.parseInt(App1));
                }
                     int count = Collections.max(arrayList) + 1;
                    //int count = Integer.parseInt(App1) + 1;
                    String strCount = "00" + count;

                    visappid = currdate + strCount;
                
            } else {
                visappid = currdate + "001";
            }

            for (int i = 1; i <= size; i++) {
                int count;
                if (i != 1) {
                    String App1 = visappid.substring((visappid.length() - 3), visappid.length());
                    count = Integer.parseInt(App1) + 1;
                    String strCount = "00" + count;
                    visappid = currdate + strCount;
                }
   
             
            query = "INSERT INTO visapplication("
                    + "VisAppID,"
                    + "PVIS_ID,"
                    + "PCOM_ID,"
                    + "IDproof,"
                    + "AppDate,"
                    + "AppTime,"
                    + "ApplicantID,"
                    + "HostID,"
                    + "Escort_ID,"
                    + "ApproverID1,"
                    + "ApproverID2,"
                    + "EntryAt,"
                    + "IssueTime,"
                    + "OutTime,"
                    + "VInOut,"
                    + "SCard_No,"
                    + "Purpose,"
                    + "VistPlace,"
                    + "CurrStatus,"
                    + "Note,"
                    + "PrintTF,"
                    + "AddedBy)values('"
                    + visappid + "','"
                    + visitor.getString() + "','"
                    + Cmp_id + "',"
                    + "'12','"
                    + AppDate + "',"
                    + "'0000-00-00 16:55:18','"
                    + Applicant_id + "','"
                    + host_id + "',"
                    + "'0','"
                    + Approver_Id + "',"
                    + "'0','"
                    + Exgate + "',"
                    + "'2012-05-29',"
                    + "'0000-00-00 16:55:18',"
                    + "'0',"
                    + "'0','"
                    + purof_Vst + "','"
                    + plcof_Vst + "',"
                    + "'1',"
                    + "'0',"
                    + "'0','"
                    + EmpName + "')";
            int executeUpdate = prstmnt.executeUpdate(query);
            }
            myHTTPConn.println("apply For CEP");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    //Dispaly ApplyCep
    public void getApplyCep(MyHTTPConnection myHTTPConn, LoginSession loginSession) {

        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {

            connvis = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            String PVIS_ID = myHTTPConn.readLine();
            Packetizer p = new Packetizer();

            query = "SELECT "
                    + "PVIS_ID, "
                    + "VisName, "
                    + "Sex, "
                    + "Nationality, "
                    + "Designation, "
                    + "PVC "
                    + "FROM "
                    + "vis_app_visitors"
                    + " WHERE "
                    + "PVIS_ID=" + PVIS_ID + "";
            System.out.println("select query" + query);
            ResultSet rs = prstmnt.executeQuery(query);
            p.setCounter();
            if (rs.next()) {
                for (int i = 1; i < 7; i++) {
                    p.addString(rs.getString(i));
                }
                p.incrCounter();
            }
            rs.close();
            myHTTPConn.println(p.getPacket());

        } catch (Exception e) {

            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }
}
