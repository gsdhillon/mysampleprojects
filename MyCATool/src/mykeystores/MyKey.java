package mykeystores;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import security_providers.MyCertUtil;
import security_providers.MyKeyStore;
import security_providers.Settings;
/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class MyKey {
    private static final Settings settings = new Settings();
    protected final MyKeyStore mks;
    /**
     * 
     * @param alias
     * @param keyStoreFile
     * @throws Exception 
     */
    public MyKey(String alias) throws Exception{
        mks = new MyKeyStore(alias);
    }
    /**
     * 
     * @return
     * @throws Exception 
     */
    public void loadJKS(File keyStoreFile) throws Exception{
        mks.loadKeyStoreJKS(keyStoreFile);
    }
    /**
     * 
     * @return
     * @throws Exception 
     */
    public void loadPKCS11(String name, String dllPath) throws Exception{
        mks.loadKeyStorePKCS11(name, dllPath);
    }
    /**
     * 
     * @return
     * @throws Exception 
     */
    public Result loadPrivateKeyEntry() throws Exception{
        return mks.loadPrivateKeyEntry();
    }
    /**
     * 
     * @return
     * @throws Exception 
     */
    public String exportCertficate() throws Exception {
        return mks.exportCertificate();
    }
    /**
     * 
     * @return 
     */
    public String getCertificate()  throws Exception {
        X509Certificate x509Certificate = mks.getCert();
        return MyCertUtil.toBase64Str(x509Certificate);
    }
    /**
     * 
     * @return
     * @throws Exception 
     */
    public String generateCSR()  throws Exception {
        PKCS10CertificationRequest csr = mks.generateCSR();
        return MyCertUtil.toBase64Str(csr);
    }
    /**
     * 
     * @param certFromCA
     * @return
     * @throws Exception 
     */
    public String importCACertificate(String caCertBase64Str) throws Exception {
        X509Certificate certFromCA = MyCertUtil.makeX509Cert(caCertBase64Str);
        return mks.importCertificate(certFromCA);
    }
    /**
     * 
     * @return
     * @throws Exception 
     */
    public String generateRootCACertificate() throws Exception{
        X500Name subjectX500Name =  //<editor-fold defaultstate="collapsed" desc="subjectDN;//distinguished name">
        new X500NameBuilder(BCStyle.INSTANCE)
                .addRDN(BCStyle.CN, "BARC_CA")
                .addRDN(BCStyle.OU, "BARC")
                .addRDN(BCStyle.O, "DAE")
                .addRDN(BCStyle.POSTAL_ADDRESS, "Trombay")
                .addRDN(BCStyle.L, "Mumbai")
                .addRDN(BCStyle.ST, "Maharashtra")
                .addRDN(BCStyle.POSTAL_CODE, "400085")
                .addRDN(BCStyle.C, "IN")
                .addRDN(BCStyle.E, "barc-ca@barc.gov.in")
                .addRDN(BCStyle.TELEPHONE_NUMBER, "91-22-25592656")
                .addRDN(BCStyle.SERIALNUMBER, String.valueOf(mks.getTokenSNo())
        ).build();
        //</editor-fold>
        MyX500Dialog x500Dialog = new MyX500Dialog(settings, subjectX500Name);
        subjectX500Name = x500Dialog.getX500Name();
        if(subjectX500Name == null){
            return "Information not enetered. Operation cancelled!";
        }
        int numYears = 5;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, numYears);
        Date notAfter = cal.getTime();
        return mks.generateSelfSignedCertificate_ROOT_CA(settings, subjectX500Name, notAfter);
    }
    /**
     * 
     * @return
     * @throws Exception 
     */
    public String generateIntermediateCACertificate() throws Exception{
        X500Name subjectX500Name = //<editor-fold defaultstate="collapsed" desc="subjectDN;//distinguished name">
        new X500NameBuilder(BCStyle.INSTANCE)
                .addRDN(BCStyle.CN, "BARC_SUB_CA_1")
                .addRDN(BCStyle.OU, "BARC")
                .addRDN(BCStyle.O, "DAE")
                .addRDN(BCStyle.POSTAL_ADDRESS, "Trombay")
                .addRDN(BCStyle.L, "Mumbai")
                .addRDN(BCStyle.ST, "Maharashtra")
                .addRDN(BCStyle.POSTAL_CODE, "400085")
                .addRDN(BCStyle.C, "IN")
                .addRDN(BCStyle.E, "barc-sub-ca-1@barc.gov.in")
                .addRDN(BCStyle.TELEPHONE_NUMBER, "91-22-25592656")
                .addRDN(BCStyle.SERIALNUMBER, String.valueOf(mks.getTokenSNo())
        ).build();
        //</editor-fold>
        MyX500Dialog x500Dialog = new MyX500Dialog(settings, subjectX500Name);
        subjectX500Name = x500Dialog.getX500Name();
        if(subjectX500Name == null){
            return "Information not enetered. Operation cancelled!";
        }
        //generate self signed certificate for 6 months
        int numYears = 0;
        int numMonths = 6;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, numYears);
        cal.add(Calendar.MONTH, numMonths);
        Date notAfter = cal.getTime();
        return mks.generateSelfSignedCertificate(settings, subjectX500Name, notAfter);
    }
    /**
     * 
     * @return
     * @throws Exception 
     */
    public String generateEndEntityCertificate() throws Exception{
        X500Name subjectX500Name =  //<editor-fold defaultstate="collapsed" desc="subjectDN;//distinguished name">
        new X500NameBuilder(BCStyle.INSTANCE)
                .addRDN(BCStyle.CN, "Gurmeet Singh")
                .addRDN(BCStyle.OU, "BARC")
                .addRDN(BCStyle.O, "DAE")
                .addRDN(BCStyle.POSTAL_ADDRESS, "604-Akashdeep, Anushakti Nagar")
                .addRDN(BCStyle.L, "Mumbai")
                .addRDN(BCStyle.ST, "Maharashtra")
                .addRDN(BCStyle.POSTAL_CODE, "400085")
                .addRDN(BCStyle.C, "IN")
                .addRDN(BCStyle.E, "gsdhillon@gmail.com")
                .addRDN(BCStyle.TELEPHONE_NUMBER, "91-9869117976")
                .addRDN(BCStyle.SERIALNUMBER, String.valueOf(mks.getTokenSNo())
        ).build();
        MyX500Dialog x500Dialog = new MyX500Dialog(settings, subjectX500Name);
        subjectX500Name = x500Dialog.getX500Name();
        if(subjectX500Name == null){
            return "Information not enetered. Operation cancelled!";
        }
        //generate self signed certificate for 6 months
        int numYears = 0;
        int numMonths = 6;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, numYears);
        cal.add(Calendar.MONTH, numMonths);
        Date notAfter = cal.getTime();
        //
        return mks.generateSelfSignedCertificate(settings, subjectX500Name, notAfter);
    }
    /**
     * 
     * @param applicationSNo
     * @param selfSignedCertBase64Str - without ----BEGIN ... ---- and -----END ... ------ marks
     * @return
     * @throws Exception 
     */
    public String signIntermediateCACertFromSelfSignedCert(int applicationSNo, String selfSignedCertBase64Str) throws Exception {
        X509Certificate selfSignedCert = MyCertUtil.makeX509Cert(selfSignedCertBase64Str);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 365*5);
        Date notAfter = cal.getTime();
        X509Certificate x509Certificate = mks.signCertificate_INTERMEDIATE_CA(applicationSNo, notAfter, selfSignedCert);
        return MyCertUtil.toBase64Str(x509Certificate);
    }
    /**
     * 
     * @param applicationSNo
     * @param csrBase64Str - without ----BEGIN ... ---- and -----END ... ------ marks
     * @return
     * @throws Exception 
     */
    public String signIntermediateCACertFromCSR(int applicationSNo, String csrBase64Str) throws Exception {
        PKCS10CertificationRequest csr = MyCertUtil.makeCSR(csrBase64Str);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 365*5);
        Date notAfter = cal.getTime();
        X509Certificate x509Certificate = mks.signCertificate_INTERMEDIATE_CA(applicationSNo, notAfter, csr);
        return MyCertUtil.toBase64Str(x509Certificate);
    }
    /**
     * 
     * @param applicationSNo
     * @param selfSignedCertBase64Str - without ----BEGIN ... ---- and -----END ... ------ marks
     * @return
     * @throws Exception 
     */
    public String signEndEntityCertFromSelfSignedCert(int applicationSNo, String selfSignedCertBase64Str) throws Exception {
        X509Certificate selfSignedCert = MyCertUtil.makeX509Cert(selfSignedCertBase64Str);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 365*2);
        Date notAfter = cal.getTime();
        X509Certificate x509Certificate = mks.signCertificate_INTERMEDIATE_CA(applicationSNo, notAfter, selfSignedCert);
        return MyCertUtil.toBase64Str(x509Certificate);
    }
    /**
     * 
     * @param applicationSNo
     * @param csrBase64Str - without ----BEGIN ... ---- and -----END ... ------ marks
     * @return
     * @throws Exception 
     */
    public String signEndEntityCertFromCSR(int applicationSNo, String csrBase64Str) throws Exception {
        PKCS10CertificationRequest csr = MyCertUtil.makeCSR(csrBase64Str);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 365*2);
        Date notAfter = cal.getTime();
        X509Certificate x509Certificate = mks.signCertificate_INTERMEDIATE_CA(applicationSNo, notAfter, csr);
        return MyCertUtil.toBase64Str(x509Certificate);
    }
    /**
     * 
     * @param data
     * @return
     * @throws Exception 
     */
    public String signAndPack(byte[] data) throws Exception{
        return mks.signAndPack(data);
    }
    /**
     * 
     * @param data
     * @return
     * @throws Exception 
     */
    public String deleteEntry() throws Exception{
        return mks.deleteEntry();
    }
//    /**
//     * 
//     * @param signedDataBase64Str
//     * @return
//     * @throws Exception 
//     */
//    public byte[] verifyAndUnpack(String signedDataBase64Str) throws Exception {
//        return mks.verifyAndUnpack(signedDataBase64Str);
//    }
}