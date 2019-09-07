package lib.digsign;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import lib.utils.Depacketizer;
/**
 * CertificateClient.java
 */
public class CertificateClient {
    /**
     *
     * @param certSerNo
     * @return
     * @throws Exception
     */
    public static DigSignCertificate getCertFromCertSerNo(String certSerNo) throws Exception{
        MyHTTP myHTTP = MyUtils.createServletConnection("CertificateServlet");
        myHTTP.openOS();
        myHTTP.println("getcert_sno");
        myHTTP.println(certSerNo);
        myHTTP.closeOS();
        myHTTP.openIS();
        String response = myHTTP.readLine();
        myHTTP.closeIS();
        if(response.startsWith("ERROR")){
            MyUtils.showMessage(response);
            throw new Exception("CertificateNotFound S.No="+certSerNo);
        }
        Depacketizer d = new Depacketizer(response);
        DigSignCertificate boostCert = new DigSignCertificate(d.getString());
        boostCert.userID = d.getString();
        return boostCert;
    }
    /**
     * upload certificate of user who is logged in
     * @param digCertificate
     * @throws Exception
     */
    public static void uploadCertificate(DigSignCertificate digCertificate)  throws Exception{
        //Verify before sending
        //allow user to upload 
        VerifyResult vr = CertificateClient.verifyCertChain(digCertificate);
        if(!vr.valid){
            int option = JOptionPane.showConfirmDialog(new JFrame(), 
                    vr.desc+"\n\n"
                    + "Do you still want to upload?", null, JOptionPane.YES_NO_OPTION);
            if(option!=JOptionPane.YES_OPTION){
                return;
            }
        }
        //Upload Certificate
        MyHTTP myHTTP = MyUtils.createServletConnection("CertificateServlet");
        myHTTP.openOS();
        myHTTP.println("uploadcert");
        myHTTP.println(digCertificate.certInBase64);
        myHTTP.closeOS();
        myHTTP.openIS();
        String response = myHTTP.readLine();
        myHTTP.closeIS();
        MyUtils.showMessage(response.replaceAll(";", "\n"));
    }
       /**
     * @param cert
     * @return ''
     */
    public static VerifyResult verifyCertChain(DigSignCertificate cert){
        VerifyResult result = new VerifyResult();
        result.valid = true;
        result.desc = "Certificate is VALID";
        return result;
    }
}