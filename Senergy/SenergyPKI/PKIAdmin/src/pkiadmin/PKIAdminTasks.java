package pkiadmin;

import java.io.BufferedReader;
import java.io.PrintStream;

/**
 * PKITasks.java
 * Created on Apr 2, 2009, 1:02:16 PM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class PKIAdminTasks {
    /**
     * 
     * @param empno
     * @return
     */
    public static String getListOfEmployee(){
        try {
            MSC msc = new MSC("PKIServlet");
            PrintStream ps = msc.getPS();
            ps.println("list_of_employee");
            ps.close();
            BufferedReader br = msc.getBR();
            String response = br.readLine();
            br.close();
            return response;
        } catch (Exception ex) {
            MyUtils.showException("getting list of employee", ex);
            return null;
        }
    }
    /**
     * 
     * @param empno
     * @return
     */
    public static CertInfo getCertificateFromEmpno(String empno){
        try {
            MSC msc = new MSC("PKIServlet");
            PrintStream ps = msc.getPS();
            ps.println("getcertificate_empno");
            ps.println(empno);
            ps.close();
            BufferedReader br = msc.getBR();
            String response = br.readLine();
            br.close();
            if (response.startsWith("ERROR:")) {
                MyUtils.showError(response);
                return null;
            }else{
                CertInfo certInfo = new CertInfo(response);
                certInfo.alias = empno;
                return certInfo;
            }
        } catch (Exception ex) {
            MyUtils.showException("while getting cert from empno", ex);
            return null;
        }
    }
    /**
     * 
     * @param certSNO
     * @return
     */
    public static CertInfo getCertificateFromSNO(String certSNO) {
        try {
            MSC msc = new MSC("PKIServlet");
            PrintStream ps = msc.getPS();
            ps.println("getcertificate_sno");
            ps.println(certSNO);
            ps.close();
            BufferedReader br = msc.getBR();
            String response = br.readLine();
            if (response.startsWith("ERROR:")) {
                MyUtils.showError(response);
                br.close();
                return null;
            }else{
                CertInfo certInfo = new CertInfo(response);
                certInfo.alias = br.readLine();
                br.close();
                return certInfo;
            }
        } catch (Exception ex) {
            MyUtils.showException("while getting cert from SNO", ex);
            return null;
        }
    }
    /**
     * 
     * @param certSNO
     * @return
     */
    public static String updateCertificate(String empno, CertInfo certInfo) {
        try {
            MSC msc = new MSC("PKIServlet");
            PrintStream ps = msc.getPS();
            ps.println("update_certificate");
            ps.println(empno);
            ps.println(certInfo.serialNumber);
            String exp_date = MyUtils.dateToString(certInfo.expireDate);
            //System.out.println("*********exp_date = "+exp_date);
            ps.println(exp_date);
            ps.println(certInfo.certInBase64);
            ps.close();
            BufferedReader br = msc.getBR();
            String response = br.readLine();
            br.close();
            return response;
        } catch (Exception ex) {
            MyUtils.showException("while updating certificate of empno", ex);
            return "ERROR:"+ex.getMessage();
        }
    }
    /**
     * 
     * @param empno
     * @param name
     * @return
     */
    public static String registerEmployee(String empno, String name){
        try {
            MSC msc = new MSC("PKIServlet");
            PrintStream ps = msc.getPS();
            ps.println("register_emp");
            ps.println(empno);
            ps.println(name);
            ps.close();
            BufferedReader br = msc.getBR();
            String response = br.readLine();
            br.close();
            return response;
        } catch (Exception ex) {
            MyUtils.showException("while register employee", ex);
            return "ERROR:"+ex.getMessage();
        }
    }
}
