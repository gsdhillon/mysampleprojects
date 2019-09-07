package pkiadmin;

import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * CertInfo.java
 * Created on Nov 29, 2007, 2:14:37 AM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class CertInfo{
    public String alias;
    public String certInBase64;
    public String serialNumber;
    public Date expireDate;
    public String algorithm;
    public String format;
    public String signAlgo;
    public PublicKey publicKey;
    public String subjectDN;
    public String issuerDN;
    /**
     * 
     * @param cert
     */
    public CertInfo(String certInBase64) throws Exception{
        this.certInBase64 = certInBase64;
        byte[] certBytes =    ("-----BEGIN CERTIFICATE-----\n"+
                              certInBase64+
                              "\n-----END CERTIFICATE-----").getBytes();
      
        ByteArrayInputStream bais = new ByteArrayInputStream(certBytes);
        
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        
        if(bais.available() <= 0){
            throw new Exception("Invalid x.509 certificate excetion");
        }
        
        X509Certificate x509Certificate =  
                                (X509Certificate)cf.generateCertificate(bais);
        
        publicKey = x509Certificate.getPublicKey();
        signAlgo = x509Certificate.getSigAlgName();
        serialNumber = x509Certificate.getSerialNumber().toString();
        expireDate = x509Certificate.getNotAfter();
        subjectDN = x509Certificate.getSubjectDN().toString();
        issuerDN = x509Certificate.getIssuerDN().toString();
        algorithm = publicKey.getAlgorithm();
        format = publicKey.getFormat();
    }
    /**
     * 
     * @return
     */
    public String getInfo(){
        return 
        "----------------- certificate info --------------------\n" +
        "Empno: - "+alias+"\n"+        
        "Serial Number: - "+serialNumber+"\n"+
        "Expire Date: - "+MyUtils.dateToString(expireDate)+"\n"+
        //"Subject: - "+subjectDN+"\n"+
        //"Issuer: - "+issuerDN+"\n"+
        //"Algorithm: - "+algorithm+", Format = "+format+"\n"+
        //"Signature Algorithm: - "+signAlgo+"\n"+
        "CertBase64 len = "+certInBase64.length()+"\n" +
        "----------------- end certificate info ----------------";
    }
}
