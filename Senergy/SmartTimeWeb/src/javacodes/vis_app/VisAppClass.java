package javacodes.vis_app;

import SmartTimeApplet.visitor.vis_app.Visitor;
import SmartTimeApplet.visitor.vis_app.VisitorForm;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import lib.digsign.DigSignAuth;
import lib.digsign.DigSignUser;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;
/**
 * VisAppClass.java
 */
public class VisAppClass {
    private static final String dateTimeFormat = "%d/%m/%Y %T";
    private static final String dateFormat = "%d/%m/%Y";
    /**
     * 
     * @param stmt
     * @param userID
     * @param p
     * @throws Exception 
     */
    public static void addFormsForSign(Statement stmt, String userID, Packetizer p) throws Exception{
        try {
            String query = 
            "Select "
                + "AppID,"
                + "applicantEmpNo,"
                + "date_format(applydate, '"+dateFormat+"') applydate,"
                + "remarks,"
                + "(select AUTH_ROLE from vis_app_work_flow where vis_app_work_flow.CURRENT_STATUS_CODE=vis_app_form.CURRENT_STATUS_CODE) as AuthRole "
            + "from "
                    + "vis_app_form "
            + "where "
                    + "CURRENT_STATUS_CODE = 10 and "
            + "recEmpNo = '" + userID + "' "
            + "UNION "
            + "Select "
                + "AppID,"
                + "applicantEmpNo,"
                + "date_format(applydate, '"+dateFormat+"') applydate,"
                + "remarks,"
                + "(select AUTH_ROLE from vis_app_work_flow where vis_app_work_flow.CURRENT_STATUS_CODE=vis_app_form.CURRENT_STATUS_CODE) as AuthRole "
            + "from "
                    + "vis_app_form "
            + "where "
                    + "CURRENT_STATUS_CODE= 20 and "
            + "appEmpNo = '" + userID + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                p.addString(VisitorForm.shortName);
                p.addString(rs.getString("AppID"));
                p.addString(rs.getString("applicantEmpNo"));
                p.addString(rs.getString("applydate"));
                p.addString(rs.getString("remarks"));
                p.addString(rs.getString("AuthRole"));
                p.incrCounter();
            }
        } catch (Exception ex) {
            AppContext.println("Exception on "+VisitorForm.fullName+" addFormsForSign:"+ex.getMessage());
            throw ex;
        }
    }
    /**
     * 
     * @param stmt
     * @param userID
     * @param year
     * @param p 
     */
    public static void addFormsApplied(Statement stmt, String userID, String year, Packetizer p) throws Exception{
        try {
            String query = 
            "Select "
                    + "AppID,"
                    + "date_format(applydate, '"+dateFormat+"') applydate,"
                    + "remarks,"
                    + "(select current_status from vis_app_status_list where vis_app_status_list.CURRENT_STATUS_CODE=vis_app_form.CURRENT_STATUS_CODE) as status "
                    + "from "
                    + "vis_app_form "
                    + "where "
            + "YEAR(applydate)='" + year + "' and "
                    + "applicantEmpNo='" + userID + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.last();
            rs.beforeFirst();
            while (rs.next()) {
                p.addString(VisitorForm.shortName);
                p.addString(rs.getString("AppID"));
                p.addString(rs.getString("applydate"));
                p.addString(rs.getString("remarks"));
                p.addString(rs.getString("status"));
                p.incrCounter();
            }
        }catch (Exception ex) {
            AppContext.println("Exception on "+VisitorForm.fullName+" addFormsApplied:"+ex.getMessage());
            throw ex;
        }
    }
    /**
     * 
     * @param stmt
     * @param userID
     * @param year
     * @param p
     * @throws Exception 
     */
    public static void addSignedForms(Statement stmt, String userID, String year, Packetizer p) throws Exception{
        try {
            String query =
            "Select "
                    + "AppID,"
                    + "date_format(applydate, '"+dateFormat+"') applydate,"
                    + "remarks,"
                    + "(select current_status from vis_app_status_list where vis_app_status_list.CURRENT_STATUS_CODE=vis_app_form.CURRENT_STATUS_CODE) status "
            + "from "
                    + "vis_app_form "
            + "where "
                    + "YEAR(applydate)='"+year+"' and "
                    + "(recEmpNo ='"+userID+"' or appEmpNo = '"+userID+"')";
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                p.addString("CEP");
                p.addString(rs.getString("AppID"));
                p.addString(rs.getString("applydate"));
                p.addString(rs.getString("remarks"));
                p.addString(rs.getString("status"));
                p.incrCounter();
            }
        }catch (Exception ex) {
            AppContext.println("Exception on "+VisitorForm.fullName+" addSignedForms:"+ex.getMessage());
            throw ex;
        }
    }
    /**
     * 
     * @param stmt
     * @param form 
     */
    public static void fillObjectData(Statement stmt, VisitorForm form) throws Exception{
        String query="";
        try{
            //vis app form data
            query = 
            "Select "
                    + "date_format(now(), '"+dateTimeFormat+"') todayDateTime, "
                    + "applicantEmpNo, "
                    + "date_format(applydate, '"+dateTimeFormat+"') applydate, "
                    + "date_format(visitdate_from, '"+dateFormat+"') visitdate_from, "
                    + "date_format(visitdate_to, '"+dateFormat+"') visitdate_to, "
                    + "purpose,"
                    + "companyID,"
                    + "visit_places,"
//                    + "recEmpNo,"
//                    + "appEmpNo,"
                    + "remarks, "
                    + "A.CURRENT_STATUS_CODE, "
                    + "B.current_status, "
                    + "B.state "
    //                + "(select count(*) from vis_app_auth_actions where AppID = A.AppID ) NUM_AUTHS "
            + "from "
                    + "vis_app_form A,"
                    + "vis_app_status_list B "
            + "where "
                    + "AppID='" + form.formID + "' and "
                    + "A.current_status_code = B.current_status_code";
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (!rs.next()) {
                    throw new Exception("Form not found - "+form.formID);
                }
                form.todayDateTime = rs.getString("todayDateTime");//send always
                form.applicant.userID = rs.getString("applicantEmpNo");
                form.applyDate = rs.getString("applydate");
                form.dateFrom = rs.getString("visitdate_from");
                form.dateTo = rs.getString("visitdate_to");
                form.purpose = rs.getString("purpose");
                form.company.companyID = rs.getString("companyID");
                form.places = rs.getString("visit_places");
//                form.approver1.userID = rs.getString("recEmpNo");
//                form.approver2.userID = rs.getString("appEmpNo");
                form.remarks = rs.getString("remarks");
                form.currentStatus = rs.getString("current_status");
                form.currentStatusCode = rs.getString("CURRENT_STATUS_CODE");
                form.state = rs.getString("state");
    //            form.numAuths = rs.getInt("NUM_AUTHS");
            }
        //fetch vislist
            query = 
            "select "
                    + "A.VIS_SNO, "
                    + "A.PVIS_ID, "
                    + "B.VisName, "
                    + "B.designation, "
                    + "B.PVC, "
                    + "B.Sex "
            + "from "
                    + "vis_app_visitor_list A, "
                    + "vis_app_visitors B "
            + "where "
                    + "A.PVIS_ID = B.PVIS_ID and "
                    + "A.AppID = '" + form.formID + "' "
            + "order by A.VIS_SNO";
            try (ResultSet rs = stmt.executeQuery(query)) {
                rs.last();
                int count = rs.getRow();
                rs.beforeFirst();
                form.visitorList = new Visitor[count];
                for (int i = 0; i < count; i++) {
                    rs.next();
                    form.visitorList[i] = new Visitor();
                    form.visitorList[i].visSNo = rs.getString("VIS_SNO");
                    form.visitorList[i].pvisID = rs.getString("PVIS_ID");
                    form.visitorList[i].name = rs.getString("VisName");
                    form.visitorList[i].desig = rs.getString("designation");
                    form.visitorList[i].pvcYesNo = rs.getInt("PVC")==1?"Yes":"No";
                    form.visitorList[i].sex = rs.getInt("Sex")==1?"FeMale":"Male";
                }
            }
            //fetch hostlist
            query = 
            "select "
                    + "A.hostSNo, "
                    + "A.host_EmpNo, "
                    + "B.EmpName, "
                    + "B.designation, "
                    + "B.Division, "
                    + "B.OfficeNo, "
                    + "B.Email "
            + "from "
                    + "vis_app_host_list A, "
                    + "employeemaster B "
            + "where "
                    + "A.AppID = '"+form.formID+"' and "
                    + "A.host_EmpNo = B.EmpCode";
            try (ResultSet rs = stmt.executeQuery(query)) {
                rs.last();
                int count = rs.getRow();
                rs.beforeFirst();
                form.hostList = new DigSignUser[count];
                for (int i = 0; i < count; i++) {
                    rs.next();
                    form.hostList[i] = new DigSignUser();
                    form.hostList[i].hostSNo = rs.getString("hostSNo");
                    form.hostList[i].userID = rs.getString("host_EmpNo");
                    form.hostList[i].designation = rs.getString("designation");
                    form.hostList[i].phoneNo = rs.getString("OfficeNo");
                    form.hostList[i].email = rs.getString("Email");
                }
            }
            //auths already signed
             query = 
                "SELECT "
                    + "auth_empno,"
                    + "auth_name,"
                    + "auth_desig,"
                    + "auth_role,"
                    + "auth_action,"
                    + "auth_remarks,"
                    + "cert_sno,"
                    + "sign,"
                    + "date_format(sign_date, '"+dateTimeFormat+"') sign_date "
            + "FROM "
                    + "vis_app_auth_actions  "
            + "WHERE "
                    + "AppID= '" + form.formID + "' "
            + " order by " //  it is must
                    + "auth_sno";
            try (ResultSet rs = stmt.executeQuery(query)) {
                while(rs.next()) {
                    DigSignAuth auth = new DigSignAuth();
                    auth.userID = rs.getString("auth_empno");
                    auth.name = rs.getString("auth_name");
                    auth.designation = rs.getString("auth_desig");
                    auth.role = rs.getString("auth_role");
                    auth.action = rs.getString("auth_action");
                    auth.remarks = rs.getString("auth_remarks");
                    auth.certSerNo = rs.getString("cert_sno");
                    auth.sign = rs.getString("sign");
                    auth.signDate = rs.getString("sign_date");
                    form.addAuth(auth);
                }
            }
            //work flow
            query="Select "
                    + "NEXT_STATUS_CODE_ON_YES,"
                    + "NEXT_STATUS_CODE_ON_NO,"
                    + "AUTH_ROLE,"
                    + "YES_ACTION,"
                    + "NO_ACTION "
                + "From "
                    + " vis_app_work_flow "
                + "Where "
                    + "CURRENT_STATUS_CODE = "+form.currentStatusCode;
            try (ResultSet rs = stmt.executeQuery(query)) {
                if(rs.next()){
                    form.nextStatusCodeOnYes=rs.getString("NEXT_STATUS_CODE_ON_YES");
                    form.nextStatusCodeOnNo=rs.getString("NEXT_STATUS_CODE_ON_NO");
                    form.currentAuthRole=rs.getString("AUTH_ROLE");
                    form.currentAuthYesAction=rs.getString("YES_ACTION");
                    form.currentAuthNoAction=rs.getString("NO_ACTION");
                }
            }
            form.servletResponse = "OK";
        }catch(Exception e){
            AppContext.println("Query:"+query);
            form.servletResponse = "ERROR:"+e.getMessage();
        }
    }

    /**
     * 
     * @param form
     * @param myHTTPConn 
     */
    public synchronized  static void submitNewForm(LoginSession session, VisitorForm form, MyHTTPConnection myHTTPConn) {
        //TODO insert form Object into db and send actual formID and apply datetime
        DBConnection connvis = null;
        PreparedStatement prstmnt = null;
        String query = "";
        // String ResultSet=null;
        try {
            connvis = AppContext.getDBConnection();
            connvis.setAutoCommit(false);
            prstmnt = (PreparedStatement) connvis.prepareStatement(query);
            if (connvis == null) {
                throw new Exception("Could not get database connection");
            }
            //get current date
            String ddmmyy;
            query = "select date_format(now(), '%d%m%y') ddmmyy, date_format(now(), '"+dateTimeFormat+"') fullDate from dual";
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                rs.next();
                ddmmyy = rs.getString(1);
                form.applyDate = rs.getString(2);
            }
            //make formID
            query = "SELECT AppID from vis_app_form where date_format(applydate, '%d%m%y')  = '" + ddmmyy + "'   ";
            
            ResultSet rs1 = prstmnt.executeQuery(query);
            ArrayList<Integer> arrayList = new ArrayList<>();
            if (rs1.next()) {
                rs1.beforeFirst();
                while (rs1.next()) {
                    String strconcat = rs1.getString(1);
                    String App1 = strconcat.substring((strconcat.length() - 3), strconcat.length());
                    arrayList.add(Integer.parseInt(App1));
                }
                int count = Collections.max(arrayList) + 1;
                String strCount = "000" + count;
                strCount = strCount.substring((strCount.length() - 3), strCount.length());
                form.formID = "P" + ddmmyy + strCount;
            } else {
                form.formID = "P" + ddmmyy + "001";
            }
            //   insert master data         
            query = 
            "INSERT INTO vis_app_form("
                    + "AppID,"
                    + "applicantEmpNo,"
                    + "applydate,"
                    + "visitdate_from,"
                    + "visitdate_to,"
                    + "purpose,"
                    + "companyID,"
                    + "visit_places,"
                    + "recEmpNo,"
                    + "appEmpNo,"
                    + "remarks,"
                    + "CURRENT_STATUS_CODE"
            + ") values ( "
                    + "'"+form.formID + "', "
                    + "'"+form.applicant.userID + "', "
                    + " STR_TO_DATE('"+form.applyDate+"', '"+dateTimeFormat+"'), "
                    + "'"+form.dateFrom + "',"
                    + "'"+form.dateTo + "',"
                    + "'"+form.purpose + "',"
                    + "'"+form.company.companyID + "',"
                    + "'"+form.places + "',"
                    + "'"+form.approver1.userID + "',"
                    + "'"+form.approver2.userID + "',"
                    + "'"+form.remarks + "',"
                    + "10"
            + ")";
            prstmnt.executeUpdate(query);
            //isert visitor list
//            System.out.println(" form.visitorList.length : " + form.visitorList.length + " form.hostList.length : " + form.hostList.length);
            for (int i = 0; i < form.visitorList.length; i++) {
                query = "Insert into vis_app_visitor_list (PVIS_ID,AppID,VIS_SNO) values ('" + form.visitorList[i].pvisID + "','" + form.formID + "'," + form.visitorList[i].visSNo + ")";
                prstmnt.executeUpdate(query);
            }
            //insert host list
            for (int j = 0; j < form.hostList.length; j++) {
                query = "Insert into vis_app_host_list (host_EmpNo,AppID,hostSNo) values ('" + form.hostList[j].userID + "','" +form.formID + "'," + form.hostList[j].hostSNo + ")";
                prstmnt.executeUpdate(query);
            }
            connvis.commit();
            myHTTPConn.println(form.formID);
            myHTTPConn.println(form.applyDate);
        } catch (Exception e) {
            AppContext.println("Query:"+query);
            AppContext.log(session, e);
            String err = "ERROR: Exception on Submit: " + e.getMessage();
            try {
                connvis.rollback();
            } catch (Exception ex) {
                err += "; OnRollBack: "+ex.getMessage();
            }
            myHTTPConn.println(err);
        } finally {
            AppContext.close(prstmnt, connvis);
        }
    }
    
    /**
     * 
     * @param bs
     * @param br
     * @return 
     */
    public static synchronized void submitSignedForm(LoginSession session, MyHTTPConnection httpConn){
        DBConnection conn = null;
        Statement stmt = null;
        String query;
        try{
            // get data for submit
            String msg = httpConn.readLine();
            Depacketizer d = new Depacketizer(msg);
            String formID = d.getString();
            int oldStatusCode = d.getInt();
            int newStatusCode = d.getInt();
            int authSNo = d.getInt();
            DigSignAuth auth= new DigSignAuth();
            auth.userID=d.getString();
            auth.name=d.getString();
            auth.designation=d.getString();
            auth.role=d.getString();
            auth.action=d.getString();
            auth.remarks=d.getString();
            auth.certSerNo = d.getString();
            auth.signDate=d.getString();
            auth.sign=d.getString();
          
            //get conn and save data
            conn = AppContext.getDBConnection();
            if(conn==null){
                throw new Exception("Could not get database connection");
            }
            conn.setAutoCommit(false);
            //check old status for synch pupose
            stmt = conn.createStatement();
            //MCPClass.checkCurrentStatus(stmt, formID, oldStatus);
            //insert auth record
            query =
            "insert into vis_app_auth_actions ( "
                    + "AppID,"
                    + "auth_sno,"
                    + "auth_empno,"
                    + "auth_name,"
                    + "auth_desig,"
                    + "auth_role,"
                    + "auth_action,"
                    + "sign_date,"
                    + "sign,"
                    + "cert_sno,"
                    + "auth_remarks "
            + ") values (" 
                    + "'"+formID+"', "
                    + authSNo+", "
                    + "'"+auth.userID+"', "
                    + "?, "//1 Name
                    + "?, "//2 Desig
                    + "'"+auth.role+"', "
                    + "'"+auth.action+"', "
                    + "str_to_date('"+auth.signDate+"','"+dateTimeFormat+"') ,"
                    + "'"+auth.sign+"', "
                    + "'"+auth.certSerNo+"', "
                    + " ? "//3 remarks
            + ")";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, auth.name);
                ps.setString(2, auth.designation);
                ps.setString(3, auth.remarks);
                if(ps.executeUpdate()!=1){
                    ps.close();
                    throw new Exception("Submit failed-2");
                }
            }
            //update status
            query =
            "update "
                    + "vis_app_form "+
            "set "
                    + " CURRENT_STATUS_CODE = "+newStatusCode+" "+
            "where"
                    + " AppID = '"+formID+"' ";
            if(stmt.executeUpdate(query) != 1){
                throw new Exception("Submit failed");
            }
            conn.commit();
            httpConn.println( "OK: form has been submitted successfully");
        }catch (Exception e){
            AppContext.log(session, e);
            String err = "ERROR: Exception on submit: " + e.getMessage();
            try {
                conn.rollback();
            } catch (Exception ex) {
                err += "; OnRollBack: "+ex.getMessage();
            }
            httpConn.println(err);
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
