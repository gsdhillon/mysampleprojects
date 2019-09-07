package lib.digsign;

import lib.session.MyUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import sun.security.pkcs11.SunPKCS11;

/**
 * DigSignToken.java
 */
public class DigSignToken {
    /**
     *
     * @throws Exception
     */
    protected static void loadKeyStore() throws Exception{
        //Check PKCS11 driver
        String lib = System.getenv("windir") + "\\system32\\dkck201.dll";
        File f = new File(lib);
        if (!f.exists()) {
            throw new Exception("TokenDriverNotInstalled");
        }
        //Find DigSignToken
        for (int slotListIndex = 0; slotListIndex < 16; slotListIndex++) {
            try {
                String config =
                      "name = " + name + "\n"
                    + "library = " + lib + "\n"
                    + "slotListIndex = " + slotListIndex;
                ByteArrayInputStream configStream = new ByteArrayInputStream(config.getBytes());
                provider = new SunPKCS11(configStream);
                keyStore = KeyStore.getInstance("pkcs11", provider);
                Security.addProvider(provider);
                System.out.println(provider.getInfo()+" slotListIndex="+slotListIndex);
                break;
            } catch (Exception e) {
                if(slotListIndex == 15){
                    throw new Exception("TokenNotFound - "+e.getMessage());
                }
            }
       }
       //Load KeyStore
       try{
            keyStore.load(null, new char[]{});
       }catch(Exception e){
            throw new Exception("TokenLoadFailed - "+e.getMessage());
       }
       //Read all aliases
       try {
            numAlias = 0;
            Enumeration list = keyStore.aliases();
            while (list.hasMoreElements()) {
                myAlias[numAlias++] = String.valueOf(list.nextElement());
            }
            //removeKeyStore();
        } catch (Exception e) {
            throw new Exception("ReadingTokenInformation - "+e.getMessage());
        }
    }
    /**
     *
     */
    protected static void removeKeyStore() {
        try {
            if (provider != null) {
                Security.removeProvider(provider.getName());
            }
            provider = null;
            keyStore = null;
            for (int i = 0; i < numAlias; i++) {
                myAlias[i] = null;
            }
            numAlias = 0;
        } catch (Exception e) {
            MyUtils.showException("unloading key store", e);
        }
    }
    /**
     *
     * @return
     */
    protected static String findLatestAlias(){
        try{
            X509Certificate[] x509Cert = new X509Certificate[numAlias];
            // fetch all certificates
            int count = 0;
            for(int i=0;i<numAlias;i++){
                Certificate[] certChain = keyStore.getCertificateChain(myAlias[i]);
                if(certChain == null || certChain.length==0){
                  continue;
                }
                byte[] certBytes = certChain[0].getEncoded();
                String cert =   "-----BEGIN CERTIFICATE-----\n"+
                                 Base64Tool.base64Encode(certBytes) +
                                "\n-----END CERTIFICATE-----";
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream bais = new ByteArrayInputStream(cert.getBytes());
                x509Cert[count++] = (X509Certificate)cf.generateCertificate(bais);
            }
            if(count==0){
                MyUtils.showMessage("No Certificate found in the Token");
                return null;
            }
            int latest = 0;
            Date latestDate = x509Cert[0].getNotAfter();
            for(int i=1;i<count;i++){
                Date validUpto = x509Cert[i].getNotAfter();
                if(latestDate.before(validUpto)){
                    latestDate = validUpto;
                    latest = i;
                }
            }
            return myAlias[latest];
        }catch(Exception e){
            MyUtils.showException("finding latest key from token", e);
            return null;
        }
    }
    /**
     *
     * @param doc
     * @param alias
     * @return
     */
    private static byte[] signDoc(byte[] doc, String alias){
        try{
            byte[] sign = null;
            //fetch private key
            PrivateKey privateKey = (PrivateKey)keyStore.getKey(alias, null);
            //sign doc "SHA1withRSA" or "MD5withRSA"
            Signature signature = Signature.getInstance("SHA1withRSA", provider);
            signature.initSign(privateKey);
            signature.update(doc);
            sign = signature.sign();
            return sign;
        }catch(Exception e){
            MyUtils.showException("signing document with key alias="+alias, e);
            return null;
        }
    }
    /**
     *
     * @param doc
     * @return
     */
    public static String signDocWithCurrentKey(String doc) throws Exception{
        MyUtils.showMessage("Generate Sign");
        byte[] data = doc.getBytes("UTF-8");
        return Base64Tool.base64Encode(HASH.getSHA1(data));
        //TODO ACTUAL_SIGN
      
//        try{
//            loadKeyStore();
//        }catch(Exception e){
//            MyUtils.showException("Loading token", e);
//            return null;
//        }
//        String alias = findLatestAlias();
//        if(alias!=null){
//            byte[] sign = signDoc(doc, alias);
//            removeKeyStore();
//            return sign;
//        }else{
//            removeKeyStore();
//            return null;
//        }
    }
    /**
     *
     * @param userID
     * @return
     */
    public static DigSignCertificate getCurrentCertificate(String userID){
        try{
            loadKeyStore();
        }catch(Exception e){
            MyUtils.showException("Loading token", e);
            return null;
        }
        String alias = findLatestAlias();
        if(alias != null){
            DigSignCertificate digCert = getCertificate(userID, alias);
            removeKeyStore();
            return digCert;
        }else{
            removeKeyStore();
            return null;
        }
    }

    /**
     *
     * @param userID
     * @return
     */
    public static DigSignCertificate[] getAllCertificates(String userID){
        try{
            loadKeyStore();
            DigSignCertificate[] certs = new DigSignCertificate[numAlias];
            // fetch certificates
            for(int i=0;i<DigSignToken.numAlias;i++){
                Certificate[] certChain = DigSignToken.keyStore.getCertificateChain(myAlias[i]);
                if(certChain!=null && certChain.length!=0){
                    byte[] certBytes = certChain[0].getEncoded();
                    String cert = Base64Tool.base64Encode(certBytes);
                    certs[i] = new DigSignCertificate(cert);
                    certs[i].alias = DigSignToken.myAlias[i];
                    certs[i].userID = userID;
                }else{
                    certs[i].serialNumber = "CertificateNotFound";
                }
            }
            removeKeyStore();
            return certs;
        }catch(Exception e){
            MyUtils.showException("getting all certificates from token", e);
            removeKeyStore();
            return null;
        }
    }
    /**
     * 
     * @param userID
     * @param alias
     * @return
     */
    private static DigSignCertificate getCertificate(String userID, String alias){
        try{
            Certificate[] certChain = keyStore.getCertificateChain(alias);
            if(certChain != null && certChain.length!=0){
                byte[] certBytes = certChain[0].getEncoded();
                String cert = Base64Tool.base64Encode(certBytes);
                DigSignCertificate digCert = new DigSignCertificate(cert);
                digCert.alias = alias;
                digCert.userID = userID;
                return digCert;
            }else{
                MyUtils.showMessage("No certificate found with alias="+alias);
                return null;
            }
        }catch(Exception e){
            MyUtils.showException("geting certificate with alias="+alias, e);
            return null;
        }
    }
    //data
    protected static Provider provider = null;
    protected static String[] myAlias = new String[50];
    protected static int numAlias = 0;
    protected static KeyStore keyStore = null;
    private static String name  = "CryptoToken";
}