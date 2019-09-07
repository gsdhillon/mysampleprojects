package javacodes.vis_app;

import SmartTimeApplet.visitor.vis_app.VisitorForm;
import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 * VisAppServlet.java
 */
public class VisAppServlet extends HttpServlet {

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
            try (PrintWriter out = resp.getWriter()) {
                out.println("Error: this servlet does not support the GET method!");
            }
        } catch (Exception e) {
            //System.out.println("GET request from " + remoteAddr);
            AppContext.log(null, e);
        }
    }

    @Override
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
            String reqMsg = myHTTPConn.readLine();
            switch (reqMsg) {
                case "getUserInfo":
                    getUserInfo(myHTTPConn, loginSession);
                    break;
                case "getUserInfoWithUserID":
                    getUserInfoWithUserID(myHTTPConn, loginSession);
                    break;
                case "getCmpInfo":
                    getCmpInfo(myHTTPConn, loginSession);
                    break;
                 case "getCompanyInfo":
                    getCompanyInfo(myHTTPConn, loginSession);
                    break;
                case "getVisAppform":
                    getVisAppform(myHTTPConn, loginSession);
                    break;
                case "getVisitorInfo":
                    getVisitorInfo(myHTTPConn, loginSession);
                    break;
                case "submitFormSigned":
                    VisAppClass.submitSignedForm(loginSession, myHTTPConn);
                    break;
                //
                case "addNewCompany":
                    addNewCompany(myHTTPConn, loginSession);
                    break;
                case "AddCEP":
                    AddCEP(myHTTPConn, loginSession);
                    break;
                case "FillForm":
                    fillForm(myHTTPConn, loginSession);
                    break;
                case "getCmpId":
                    getCmpId(myHTTPConn, loginSession);
                    break;
                case "getApplicantInfo":
                    getApplicantInfo(myHTTPConn, loginSession);
                    break;
                default:
                    myHTTPConn.println("ERROR: unkwon request '" + reqMsg + "'");
            }
        } catch (Exception ex) {
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }
    }
    /**
     *
     * @param myHTTPConn
     * @param loginSession
     */
    private void getUserInfoWithUserID(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String userID = myHTTPConn.readLine();
            String query = "Select EmpName, designation, OfficeNo, Email from employeemaster where EmpCode='" + userID + "'";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                p.addString(rs.getString(4));
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     *
     * @param myHTTPConn
     * @param loginSession
     */
    private void getUserInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String userID = loginSession.getUserID();
            String query = "Select EmpName, designation, OfficeNo, Email from employeemaster where EmpCode='" + userID + "'";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            while (rs.next()) {
                p.addString(userID);
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                p.addString(rs.getString(4));
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     * Fill The Form for updatation
     *
     * @param myHTTPConn
     * @param loginSession
     */
    public void fillForm(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                p.setCounter();
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

    /**
     * Add New Company Detail
     *
     * @param myHTTPConn
     * @param loginSession
     */
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

    /**
     * Dispaly CompanyInformation
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            Packetizer p;
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                p = new Packetizer();
                p.setCounter();
                while (rs.next()) {
                    p.addString(rs.getString(1));
                    p.addString(rs.getString(2));
                    p.addString(rs.getString(3));
                    p.addString(rs.getString(4));
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

    /**
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            ResultSet rs = prstmnt.executeQuery(query);
            if (rs.next()) {
                strComId = rs.getString(1);
            }
            myHTTPConn.println(strComId);
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    /**
     * For Get the CompanyID
     *
     * @param myHTTPConn
     * @param loginSession
     */
    public void getCompanyInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection connvis = null;
        Statement stmt = null;
        String query = "";
        try {
            connvis = AppContext.getDBConnection();
            stmt = connvis.createStatement();
            if (connvis == null) {
                myHTTPConn.println("ERROR: Database could not connect");
                return;
            }
            String companyID = myHTTPConn.readLine();
            // MyUtils.showMessage(companyid);
            Packetizer p = new Packetizer();
            query = "SELECT ComName,ComAddr,ComType,City,State,Pin FROM vis_app_company WHERE PCOM_ID='" + companyID + "'";
            try (ResultSet rs = stmt.executeQuery(query)) {
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
            AppContext.close(stmt, connvis);
        }
    }

    /**
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                p = new Packetizer();
                p.setCounter();
                while (rs.next()) {
                    p.addString(rs.getString(1));
                    p.addString(rs.getString(2));
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

    /**
     * Get CompanyVisitorInfo
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            ResultSet rs = prstmnt.executeQuery(query);
            rs.next();
            for (int i = 1; i < 7; i++) {
                p.addString(rs.getString(i));
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    /**
     *
     * @param myHTTPConn
     * @param loginSession
     */
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

    /**
     * delete company from compnayinformation
     *
     * @param myHTTPConn
     * @param loginSession
     */
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

    /**
     * Add For New Visitor
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            String cmp = dp.getString();
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
                    + cmp + "','"
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

    /**
     * For Fill Form for update VisitorInformation
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            ResultSet rs = prstmnt.executeQuery(query);
            if (rs.next()) {
                for (int i = 1; i < 11; i++) {
                    p.addString(rs.getString(i));
                }
                p.incrCounter();
            }
            myHTTPConn.println(p.getPacket());

        } catch (Exception e) {

            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    /**
     *
     * @param myHTTPConn
     * @param loginSession
     */
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

    /**
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            Packetizer p;
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                p = new Packetizer();
                p.setCounter();
                while (rs.next()) {
                    p.addString(rs.getString(1));
                    p.addString(rs.getString(2));
                    p.addString(rs.getString(3));
                    p.addString(rs.getString(4));
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

    /**
     *
     * @param myHTTPConn
     * @param loginSession
     */
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

            String companyID = myHTTPConn.readLine();
            query = "SELECT "
                    + "PVIS_ID, "
                    + "VisName, "
                    + "Designation, "
                    + "Sex, "
                    + "PVC "
                    + "FROM "
                    + "vis_app_visitors WHERE PCOM_ID='" + companyID + "'";
            System.out.println("select query" + query);
            Packetizer p = new Packetizer();
            p.setCounter();
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                while (rs.next()) {
                    for (int i = 1; i <= 5; i++) {
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

    /**
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            ResultSet rs = prstmnt.executeQuery(query);
            if (rs.next()) {
                strVstId = rs.getString(1);
            }
            myHTTPConn.println(strVstId);
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    /**
     * Get Visitor According to Company
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            ResultSet rs = prstmnt.executeQuery(query);
            p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1) + " " + rs.getString(2));
                p.incrCounter();

            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    /**
     * get Host Information for Apply Cep
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            ResultSet rs = prstmnt.executeQuery(query);
            if (rs.next()) {
                for (int i = 1; i < 5; i++) {
                    p.addString(rs.getString(i));
                }
                p.incrCounter();
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, conn);
        }
    }

    /**
     * Get Applicant Information From Employeemaster for Applying Cep
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            ResultSet rs = prstmnt.executeQuery(query);


            if (rs.next()) {
                for (int i = 1; i < 5; i++) {
                    p.addString(rs.getString(i));

                }
                p.incrCounter();
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, conn);
        }
    }

    /**
     *
     * @param myHTTPConn
     */
    private void getVisAppform(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        VisitorForm form = new VisitorForm();
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            form.formID = myHTTPConn.readLine();
            VisAppClass.fillObjectData(stmt, form);
            myHTTPConn.writeObject(form);
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            query = "SELECT  EmpCode,Division,EmpName FROM employeemaster";

            System.out.println("getApproverList : " + query);

            Packetizer p = new Packetizer();
            p.setCounter();
            ResultSet rs = prstmnt.executeQuery(query);

            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                p.incrCounter();
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, conn);
        }
    }

    /**
     * Fill Combobox for Approver
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                p = new Packetizer();
                p.setCounter();
                while (rs.next()) {
                    p.addString(rs.getString(1));
                    p.addString(rs.getString(2));
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

    /**
     * AddHost for Applying Cep
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                rs.next();
                for (int i = 1; i < 5; i++) {
                    p.addString(rs.getString(i));
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

    /**
     * using for apply add CEP
     *
     * @param myHTTPConn
     * @param loginSession
     */
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

            DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
            Date date = new Date();
            String currdate = dateFormat.format(date);


            query = "SELECT VisAppID from visapplication where AppDate='" + AppDate + "'";
            System.out.println("query" + query);
            ResultSet rs = prstmnt.executeQuery(query);
            String visappid;
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

                visappid = "P" + currdate + strCount;

            } else {
                visappid = "P" + currdate + "001";
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
                    + Vis_id + "','"
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
//            System.out.println("4444 query : " + query);
            int executeUpdate = prstmnt.executeUpdate(query);
            myHTTPConn.println("apply For CEP");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }

    /**
     * Dispaly ApplyCep
     *
     * @param myHTTPConn
     * @param loginSession
     */
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
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                p.setCounter();
                if (rs.next()) {
                    for (int i = 1; i < 7; i++) {
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

    private void getApplicantInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String userID = loginSession.getUserID();
            String query = "Select EmpName, designation, OfficeNo, Email from employeemaster where EmpCode='" + userID + "'";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            while (rs.next()) {
                p.addString(userID);
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                p.addString(rs.getString(4));
                p.addString("");
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
