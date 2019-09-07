package lib.digsign;

import lib.session.MyUtils;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
/*
 * DigSignCertificate.java
 */
public class DigSignCertificate implements  java.io.Serializable{
    public String userID;//attached externally
    public String alias;
    public int version;
    public String serialNumber;//is being used
    public String validFromString;//"dd/MM/yyyy HH:mm:ss"
    public String validUptoString;//"dd/MM/yyyy HH:mm:ss"
    public long validUptoInMills = 0;
    public String subjectName;
    public String subjectDN;
    public String issuerName;
    public String issuerDN;
    public PublicKey publicKey;
    public String pubKeyAlgo;
    public String pubKeyFormat;
    public String thumbPrint;
    public String sigAlgo;
    public X509Certificate x509Cert;
    public String certInBase64;
    public int len = 0;
    /**
     * @param cert - X.509 certificate in base64 format  
     * without -----------BEGIN ------   ------END-------- tags
     */
    public DigSignCertificate(String cert){
        try{
            certInBase64 = cert;
            cert =  "-----BEGIN CERTIFICATE-----\n"+
                            certInBase64 + 
                            "\n-----END CERTIFICATE-----";
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            byte[] certBytes = cert.getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(certBytes);
            x509Cert = (X509Certificate)cf.generateCertificate(bais);
            len = certInBase64.length();
            version = x509Cert.getVersion();
            serialNumber = x509Cert.getSerialNumber().toString(10);
            validFromString = dateToFullString( x509Cert.getNotBefore() );
            validUptoString = dateToFullString( x509Cert.getNotAfter() );
            validUptoInMills = x509Cert.getNotAfter().getTime();
            subjectName = extractName(x509Cert.getSubjectX500Principal());
            issuerName = extractName(x509Cert.getIssuerX500Principal());
            subjectDN = x509Cert.getSubjectX500Principal().toString();
            issuerDN = x509Cert.getIssuerX500Principal().toString();
            sigAlgo =   x509Cert.getSigAlgName();   
            publicKey = x509Cert.getPublicKey();
            pubKeyAlgo = getPubKeyAlgorith(publicKey);
            pubKeyFormat = publicKey.getFormat();
            thumbPrint = getThumbPrint(x509Cert);
        }catch(Exception e){
            MyUtils.showException("MakingCertObject", e);
            subjectName = "ExtractFailed";
        }
    }
    /**
     * 
     */
    public void showInfo(){
        MyUtils.showMessage(getInfo());
    }
    /**
     * 
     * @param doc
     * @param sign
     * @return ''
     */
    public VerifyResult verifySign(String doc, String sign){
        VerifyResult result = new VerifyResult();
        try{
            byte[] data = doc.getBytes("UTF-8");
            String hash = Base64Tool.base64Encode(HASH.getSHA1(data));
            if(hash.equals(sign)){
                result.valid = true;
                result.desc = "Signature is valid.";
            }else{
                result.valid = false;
                result.desc = "Signature is *** INVALID ***.";
            }
            return result;
            //  TODO ACTUAL_SIGN
//            // verify signature
//            Signature signature = Signature.getInstance("SHA1withRSA");
//            signature.initVerify(publicKey);
//            byte[] docBytes = doc.getBytes();            
//            signature.update(docBytes);
//            byte[] signBytes = Base64Tool.base64Decode(sign);
//            result.valid = signature.verify(signBytes);
//            if(result.valid){
//                result.desc = "Signature is valid\nCertificate:-\n"+getInfo();
//            }else{
//                result.desc = "Signature is *** INVALID ***\nCertificate:-\n"+
//                                                                    getInfo();
//            }
//            return result;
        }catch(Exception e){
            result.valid = false;
            result.desc = "Exception while verifying signature - "+e.getMessage();
            return result;
        }
    }
    /**
     * @return '' info
     */
    public String getInfo(){
        //set info
        return 
        " Certificate Format  : "+pubKeyFormat+", Version-"+version+
        "\n Certificate Sr. No. : "+serialNumber+
        "\n Subject Name        : "+subjectName+ 
        "\n Issuer              : "+issuerName+
        "\n Valid From          : "+validFromString+
        "\n Valid Upto          : "+validUptoString+
        "\n Public Key Algorithm: "+pubKeyAlgo+
        "\n Signature Algorithm : "+sigAlgo+
        "\n SHA1 Thumbprint     : " +thumbPrint+" \n";
    }
    /**
     * 
     * @param dn - X500Principal
     * @return '' string - CN part of the DomainName
     */
    private static String extractName(X500Principal dn){
        String cn = dn.toString();
        int begin = cn.indexOf("CN=");
        int end = cn.indexOf(",", begin);
        if(end == -1){
            end = cn.length();
        }
        return cn.substring(begin+3, end);
    }
    /**
     * 
     * @param d - java.util.Date
     * @return '' formated string
     */
    private static String dateToFullString(Date date){
        //in oracle to_char and to_date use "dd/MM/yyyy HH:mm:ss"
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }
    /**
     * @param x509Cert
     * @return '' sha-1 thumbprint
     */
    private String getThumbPrint(X509Certificate x509Cert){
        try{
            MessageDigest m = MessageDigest.getInstance("SHA-1");
            byte[] data = x509Cert.getEncoded();
            m.update(data,0,data.length);
            byte[] hash = m.digest();
            return hexify(hash);
        }catch(Exception e){
            return e.getMessage();
        }
    }
    /**
     * @param bytes
     * @return ''
     */
    private String hexify(byte[] bytes) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7',  
                            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'}; 
        StringBuilder buffer = new StringBuilder(); 
        buffer.append(hexDigits[(bytes[0] & 0xf0) >> 4]); 
        buffer.append(hexDigits[bytes[0] & 0x0f]); 
        for (int i = 1; i < bytes.length; ++i) { 
            buffer.append(' ');
            buffer.append(hexDigits[(bytes[i] & 0xf0) >> 4]); 
            buffer.append(hexDigits[bytes[i] & 0x0f]); 
        } 
        return buffer.toString();
    }
    /**
     * 
     * @param publicKey
     * @return ''
     * @throws Exception 
     */
    private String getPubKeyAlgorith(PublicKey publicKey) throws Exception{
        int length = publicKey.getEncoded().length;
        String algo = publicKey.getAlgorithm();
        String rsaLen = "";
        if(algo.equals("RSA")){
            if(length > 128 && length < 172){
                rsaLen = "1024";
            }else if(length > 256 && length < 300){
                rsaLen = "2048";
            }
        }
        return algo+" "+rsaLen;
    }
}