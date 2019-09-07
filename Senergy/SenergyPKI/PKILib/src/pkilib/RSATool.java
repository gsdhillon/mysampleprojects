package pkilib;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import javax.crypto.Cipher;
import javax.swing.JFrame;
import sun.security.pkcs11.SunPKCS11;
import sun.security.x509.CertAndKeyGen;
import sun.security.x509.X500Name;
/**
 * RSATool.java
 * Created on Oct 5, 2009, 10:58:03 AM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
@SuppressWarnings({"UseSpecificCatch", "CallToThreadDumpStack"})
public class RSATool {
    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static int buffSize = 8;
    private static void callUpdate(Signature signature, Date signDate) throws Exception{
        //call update for date bytes
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String sign_date_string = "Sign Date in [dd/mm/yyyy HH:MM:SS]:["+df.format(signDate)+"]";//56 chars
        byte[] data = sign_date_string.getBytes("UTF8");
        int start = 0;
        while(!((start+buffSize)>data.length)){
            signature.update(data,start,buffSize);
            start+=buffSize;
        }
        //call update for remaining bytes
        if(start < data.length){
            signature.update(data,start,data.length-start);
        }
    }
    /**
     * @param signature
     * @param data
     * @throws java.lang.Exception
     */
    private static void callUpdate(Signature signature, byte[] data) 
                                                               throws Exception{
        //call update for buffSize
        int start = 0;
        while(!((start+buffSize)>data.length)){
            signature.update(data,start,buffSize);
            start+=buffSize;
        }
        //call update for remaining bytes
        if(start < data.length){
            signature.update(data,start,data.length-start);
        }
        
    }
    /**
     * 
     * @param signature
     * @param is
     */
    private static void callUpdate(Signature signature, InputStream is) 
                                                              throws Exception{
        byte[] buff = new byte[buffSize];
        int len = 0;
        while((len=is.read(buff))>0){
            signature.update(buff, 0, len);
            //System.out.println("sign updated - "+len);
        }
    }
    /**
     * This class isn't meant to be instantiated.
     */
    private RSATool(){}
    /** 
     * 
     * @param data
     * @param empno
     * @return SignResult
     * statusCode 0 - Success
     * statusCode 1 - Certificate not found from Token
     * statusCode 2 - Certificate Expired on signdate
     * statusCode 3 - Token dos'nt belongs to empno
     * statusCode 4 - some other exception occurred
     * @throws java.lang.Exception
     */
    public static SignResult sign(byte[] data, String empno){
        SignResult signResult = new SignResult();
        try {
            CertInfo certInfo = getCertificate();
            if(certInfo==null){
                signResult.statusCode = 1;
                signResult.description = "No certificate found in the token";
                return signResult;
            }
            Date signDate = new Date();
            if(signDate.after(certInfo.expireDate)){
                signResult.statusCode = 2;
                signResult.description = "Certificate expired on -"+
                                  MyUtils.dateToString(certInfo.expireDate);
                return signResult;
            }
            if(!certInfo.alias.equals(empno)){
                signResult.statusCode = 3;
                signResult.description="Token dos'nt belongs to empno provided";
                return signResult;
            }
            loadKeyStore();
            //fetch private key
            PrivateKey privateKey = (PrivateKey) ks.getKey(certInfo.alias, null);
            //sign doc "SHA1withRSA" or "MD5withRSA"
            Signature signature = Signature.getInstance("SHA1withRSA", p);
            signature.initSign(privateKey);
            callUpdate(signature, data);
            callUpdate(signature, signDate);
            byte[] sign = signature.sign();
            signResult.statusCode = 0;
            signResult.sign = Base64.base64Encode(sign);
            signResult.certSNO = certInfo.serialNumber;
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
            signResult.signDate = df.format(signDate);
            signResult.description = "Document signed successfully";
            return signResult;
        } catch (Exception ex) {
            signResult.statusCode = 4;
            signResult.description = ex.getMessage();
            MyUtils.showException("while signing doc", ex);
            return signResult;
        } finally {
            removeKeyStore();
        }
    }
    /**
     * 
     * @param data
     * @param sign
     * @param certSNO
     * @param signDateString in  'dd/MM/yyyy HH:mm:ss'
     * @return SignVerifyResult
     * statusCode 0 - SignVerify true
     * statusCode 1 - SignVerify false
     * statusCode 2 - Certificate not found from the PKIServer
     * statusCode 3 - Certificate was expired on signdate
     * statusCode 4 - some other exception occurred
     */
    public static VerifyResult verify(byte[] data, String sign, String certSNO, String signDateString) {
        VerifyResult result = new VerifyResult();
        try{
            CertInfo certInfo = PKITasks.getCertificateFromSNO(certSNO);
            if(certInfo==null || !certInfo.serialNumber.equals(certSNO)){
                result.statusCode = 2;
                result.description = "Certificate not found.";
                return result;
            }
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
            Date signDate = df.parse(signDateString);
            if(signDate.after(certInfo.expireDate)){
                result.statusCode = 3;
                result.description = "Certificate expired on -"+
                                     MyUtils.dateToString(certInfo.expireDate);
                return result;
            }
            // verifyDummy signature
            byte[] signBytes = Base64.base64Decode(sign);
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(certInfo.publicKey);
            callUpdate(signature, data);
            callUpdate(signature, signDate);
            //set status
            if (signature.verify(signBytes)) {
                result.statusCode = 0;
                result.description = "Document verified TRUE";
            } else {
                result.statusCode = 1;
                result.description = "Document verified FALSE";
            }
            return result;
        }catch (Exception ex) {
            result.statusCode = 4;
            result.description = ex.getMessage();
            MyUtils.showException("while verify sign from cert", ex);
            return result;
        }
    }
    /** 
     * 
     * @param is
     * @param empno
     * @return SignResult
     * statusCode 0 - Success
     * statusCode 1 - Certificate not found from Token
     * statusCode 2 - Certificate Expired on signdate
     * statusCode 3 - Token dos'nt belongs to empno
     * statusCode 4 - some other exception occurred
     * @throws java.lang.Exception
     */
    public static SignResult sign(InputStream is, String empno){
        SignResult signResult = new SignResult();
        try {
            CertInfo certInfo = getCertificate();
            if(certInfo==null){
                signResult.statusCode = 1;
                signResult.description = "No certificate found in the token";
                return signResult;
            }
            Date signDate = new Date();
            if(signDate.after(certInfo.expireDate)){
                signResult.statusCode = 2;
                signResult.description = "Certificate expired on -"+
                                  MyUtils.dateToString(certInfo.expireDate);
                return signResult;
            }
            if(!certInfo.alias.equals(empno)){
                signResult.statusCode = 3;
                signResult.description = "Token dos'nt belongs to empno provided";
                return signResult;
            }
            loadKeyStore();
            //fetch private key
            PrivateKey privateKey = (PrivateKey) ks.getKey(certInfo.alias, null);
            //sign doc "SHA1withRSA" or "MD5withRSA"
            Signature signature = Signature.getInstance("SHA1withRSA", p);
            signature.initSign(privateKey);
            callUpdate(signature, is);
            callUpdate(signature, signDate);
            byte[] sign = signature.sign();
            signResult.statusCode = 0;
            signResult.sign = Base64.base64Encode(sign);
            signResult.certSNO = certInfo.serialNumber;
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
            signResult.signDate = df.format(signDate);
            signResult.description = "Document signed successfully";
            return signResult;
        } catch (Exception ex) {
            signResult.statusCode = 4;
            signResult.description = ex.getMessage();
            MyUtils.showException("while signing doc", ex);
            return signResult;
        } finally {
            removeKeyStore();
        }
    }
    /**
     * 
     * @param is
     * @param sign
     * @param certSNO
     * @param signDateString in 'dd/MM/yyyy HH:mm:ss'
     * @return SignVerifyResult
     * statusCode 0 - SignVerify true
     * statusCode 1 - SignVerify false
     * statusCode 2 - Certificate not found from the PKIServer
     * statusCode 3 - Certificate was expired on signdate
     * statusCode 4 - some other exception occurred
     */
    public static VerifyResult verify(InputStream is, String sign, 
                                                String certSNO, String signDateString) {
        VerifyResult result = new VerifyResult();
        try{
            CertInfo certInfo = PKITasks.getCertificateFromSNO(certSNO);
            if(certInfo==null || !certInfo.serialNumber.equals(certSNO)){
                result.statusCode = 2;
                result.description = "Certificate not found.";
                return result;
            }
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
            Date signDate = df.parse(signDateString);
            if(signDate.after(certInfo.expireDate)){
                result.statusCode = 3;
                result.description = "Certificate expired on -"+
                                     MyUtils.dateToString(certInfo.expireDate);
                return result;
            }
            // verifyDummy signature
            byte[] signBytes = Base64.base64Decode(sign);
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(certInfo.publicKey);
            callUpdate(signature, is);
            callUpdate(signature, signDate);
            //set status
            if (signature.verify(signBytes)) {
                result.statusCode = 0;
                result.description = "Document verified TRUE";
            } else {
                result.statusCode = 1;
                result.description = "Document verified FALSE";
            }
            return result;
        }catch (Exception ex) {
            result.statusCode = 4;
            result.description = ex.getMessage();
            MyUtils.showException("while verify sign from cert", ex);
            return result;
        }
    }
    
    /** 
     * 
     * @param data
     * @return SignResult
     * statusCode 0 - Success
     * statusCode 4 - some other exception occurred
     * @throws java.lang.Exception
     */
    public static SignResult signDummy(byte[] data){
        SignResult signResult = new SignResult();
        try {
            Date signDate = new Date();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
            md.update(data);
            signResult.signDate = df.format(signDate);
            md.update(signResult.signDate.getBytes("UTF-8"));
            signResult.sign = Base64.base64Encode(md.digest());
            signResult.statusCode = 0;
            signResult.certSNO = "DUMMY_SIGN";
            signResult.description = "Dummy signature created successfully";
            return signResult;
        } catch (Exception ex) {
            signResult.statusCode = 4;
            signResult.description = ex.getMessage();
            MyUtils.showException("while signing doc", ex);
            return signResult;
        } finally {
            removeKeyStore();
        }
    }
    /**
     * 
     * @param data
     * @param sign
     * @param signDateString in  'dd/MM/yyyy HH:mm:ss'
     * @return SignVerifyResult
     * statusCode 0 - SignVerify true
     * statusCode 4 - some other exception occurred
     */
    public static VerifyResult verifyDummy(byte[] data, String sign,  String signDateString) {
        VerifyResult result = new VerifyResult();
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);
            md.update(signDateString.getBytes("UTF-8"));
            String signCalculated = Base64.base64Encode(md.digest());
            //set status
            if (signCalculated.equals(sign)) {
                result.statusCode = 0;
                result.description = "Dummy sign verified - TRUE";
            } else {
                result.statusCode = 1;
                result.description = "Dummy sign verified - FALSE";
            }
            return result;
        }catch (Exception ex) {
            result.statusCode = 4;
            result.description = ex.getMessage();
            MyUtils.showException("while verify sign from cert", ex);
            return result;
        }
    }
    
    /** 
     * 
     * @param is
     * @return SignResult
     * statusCode 0 - Success
     * statusCode 4 - some other exception occurred
     * @throws java.lang.Exception
     */
    public static SignResult signDummy(InputStream is){
        SignResult signResult = new SignResult();
        try {
            Date signDate = new Date();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
            byte[] buff = new byte[buffSize];
            int len = 0;
            while((len=is.read(buff))>0){
                md.update(buff, 0, len);
            }
            signResult.signDate = df.format(signDate);
            md.update(signResult.signDate.getBytes("UTF-8"));
            signResult.sign = Base64.base64Encode(md.digest());
            signResult.statusCode = 0;
            signResult.certSNO = "DUMMY_SIGN";
            signResult.description = "Dummy signature created successfully";
            return signResult;
        } catch (Exception ex) {
            signResult.statusCode = 4;
            signResult.description = ex.getMessage();
            MyUtils.showException("while signing doc", ex);
            return signResult;
        } finally {
            removeKeyStore();
        }
    }
    /**
     * 
     * @param is - InputStream
     * @param sign
     * @param signDateString in  'dd/MM/yyyy HH:mm:ss'
     * @return SignVerifyResult
     * statusCode 0 - SignVerify true
     * statusCode 4 - some other exception occurred
     */
    public static VerifyResult verifyDummy(InputStream is, String sign,  String signDateString) {
        VerifyResult result = new VerifyResult();
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] buff = new byte[buffSize];
            int len = 0;
            while((len=is.read(buff))>0){
                md.update(buff, 0, len);
            }
            md.update(signDateString.getBytes("UTF-8"));
            String signCalculated = Base64.base64Encode(md.digest());
            //set status
            if (signCalculated.equals(sign)) {
                result.statusCode = 0;
                result.description = "Dummy sign verified - TRUE";
            } else {
                result.statusCode = 1;
                result.description = "Dummy sign verified - FALSE";
            }
            return result;
        }catch (Exception ex) {
            result.statusCode = 4;
            result.description = ex.getMessage();
            MyUtils.showException("while verify sign from cert", ex);
            return result;
        }
    }
    /**
     * decrypt encrypted key using private key
     * @param encryptedKey base64 format
     * @param empno 
     * @return DecryptRSAResult - status 0 OK
     *                            status 1 Token dos'nt belongs to empno provided
     *                            status 2 Invalid encrypted data
     *                            status 3 other exception
     */
    public static DecryptRSAResult decryptRSA(String encryptedKey, String empno){
        DecryptRSAResult decryptRSAResult = new DecryptRSAResult();
        try{
            if(encryptedKey==null){
                decryptRSAResult.statusCode = 2;
                decryptRSAResult.description = "Invalid encrypted data";
                return decryptRSAResult;
            }
            byte[] inBuf = Base64.base64Decode(encryptedKey);
            int inBlockSize = 128;
            if((inBuf.length%inBlockSize)!=0){
                decryptRSAResult.statusCode = 2;
                decryptRSAResult.description = "Invalid encrypted data";
                return decryptRSAResult;
            }
            int outBlockSize = 117;
            loadKeyStore();
            Enumeration enumeration =  ks.aliases();
            if(!enumeration.hasMoreElements()){
                decryptRSAResult.statusCode = 1;
                decryptRSAResult.description = 
                                       "Token dos'nt belongs to empno provided";
                return decryptRSAResult;
            }
            String alias = String.valueOf(enumeration.nextElement());
            if(!alias.equals(empno)){
                decryptRSAResult.statusCode = 1;
                decryptRSAResult.description = 
                                       "Token dos'nt belongs to empno provided";
                return decryptRSAResult;
            }
            //System.out.println(encryptedKey.length()+"-"+encryptedKey);
            //decrypt data
            PrivateKey privateKey = (PrivateKey)ks.getKey(alias, null);
            Cipher cp= Cipher.getInstance("RSA/ECB/PKCS1Padding", p);
            AlgorithmParameters algParams = cp.getParameters();
            cp.init(Cipher.DECRYPT_MODE, privateKey, algParams);
            int numBlocks = inBuf.length/inBlockSize;
            MyBuffer myBuffer = new MyBuffer(outBlockSize*numBlocks);
            int in = 0;
            int out = 0;
            for(int i=0;i<numBlocks-1;i++){
                cp.doFinal(inBuf, in, inBlockSize, myBuffer.buff, out);
                in+=inBlockSize;
                out+=outBlockSize;
            }
            int last = cp.doFinal(inBuf, in, inBlockSize, myBuffer.buff, out);
            myBuffer.len = out + last;
            byte[] keyBytes = myBuffer.getCopy();
            decryptRSAResult.statusCode = 0;
            decryptRSAResult.keyBytes = keyBytes;
            decryptRSAResult.description = "OK";
            return decryptRSAResult;
        }catch (Exception ex) {
            decryptRSAResult.statusCode = 3;
            decryptRSAResult.keyBytes = null;
            decryptRSAResult.description = ex.getMessage();
            MyUtils.showException("while decryptRSA", ex);
            return decryptRSAResult;
        } finally {
            removeKeyStore();
        }
    }
    /**
     * decrypt encrypted key using private key
     * @param keyBytes
     * @param empno 
     * @return EncryptRSAResult - status 0 OK
     *                            status 1 Certificate not found
     *                            status 2 other exception
     */
    public static EncryptRSAResult encryptRSA(byte[] keyBytes, String empno){
        EncryptRSAResult encryptRSAResult = new EncryptRSAResult();
        try{
            CertInfo certInfo = PKITasks.getCertificateFromEmpno(empno);
            if(certInfo==null){
                encryptRSAResult.statusCode = 1;
                encryptRSAResult.description = "Certificate not found.";
                return encryptRSAResult;
            }
            Cipher cp= Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cp.init(Cipher.ENCRYPT_MODE, certInfo.publicKey);
            int inBlockSize = 117;
            int outBlockSize = 128;
            byte[] outBuf;
            if((keyBytes.length%inBlockSize)==0){
                int numBlocks = keyBytes.length/inBlockSize;
                //make out buff
                outBuf = new byte[numBlocks*outBlockSize];
                int in = 0;int out = 0;
                for(int i=0;i<numBlocks;i++){
                     cp.doFinal(keyBytes, in, inBlockSize, outBuf, out);
                     in+=inBlockSize;
                     out+=outBlockSize;
                }
            }else{
                int numBlocks = (keyBytes.length/inBlockSize)+1;
                int lastInBlockSize = keyBytes.length%inBlockSize;
                //make out buff
                outBuf = new byte[numBlocks*outBlockSize];
                int in = 0;int out = 0;
                for(int i=0;i<numBlocks-1;i++){
                     cp.doFinal(keyBytes, in, inBlockSize, outBuf, out);
                     in+=inBlockSize;
                     out+=outBlockSize;
                }
                //last block
                cp.doFinal(keyBytes, in, lastInBlockSize, outBuf, out);
            }
            String encryptedKey = Base64.base64Encode(outBuf);
            encryptRSAResult.statusCode = 0;
            encryptRSAResult.encryptedKey = encryptedKey;
            encryptRSAResult.description = "OK";
            return encryptRSAResult;
        }catch(Exception e){
            encryptRSAResult.statusCode = 2;
            encryptRSAResult.description = e.getMessage();
            MyUtils.showException("while encryptRSA", e);
            return encryptRSAResult;
        }
    }
   /**
     * extract public key certificate from the Token
     * @return CertInfo
     */
    public static CertInfo getCertificate(){
        try{
            loadKeyStore();
            Enumeration enumeration =  ks.aliases();
            if(!enumeration.hasMoreElements()){
                MyUtils.showError("No certificate found in the token");
                return null;
            }
            String alias = String.valueOf(enumeration.nextElement());
            // fetch certificates
            Certificate[] certChain = ks.getCertificateChain(alias);
            if(certChain!=null){
                byte[] certBytes = certChain[0].getEncoded();
                CertInfo certInfo = new CertInfo(Base64.base64Encode(certBytes));
                certInfo.alias = alias;
                return certInfo;
            }else{
                MyUtils.showError("No certificate found in the token");
                return null;
            }
        }catch(Exception e){
            MyUtils.showException("While geting certificate from the token.",e);
            return null;
        } finally {
            removeKeyStore();
        }
    }
    /**
     * @param frame 
     * @return message
     * @throws java.lang.Exception 
     */
    public static String setKey(JFrame frame){
        try{
            loadKeyStore();
            X500Dialog dialog = new X500Dialog(frame);
            dialog.setVisible(true);
            String alias = dialog.alias;
            if(alias==null){
                return "Not done";
            }
            X500Name x500Name = dialog.x500Name;
            CertAndKeyGen keypair = new CertAndKeyGen("RSA", "SHA1WithRSA");
            keypair.generate(1024);
            PrivateKey privKey = keypair.getPrivateKey();
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = keypair.getSelfCertificate(x500Name, 2*365*24*60*60);
            ks.setKeyEntry(alias, privKey, null, chain);
            ks.store( null, null);
            removeKeyStore();
            return "Key pair has been generated successfully";
        }catch(Exception e){
            removeKeyStore();
            return "generating key pair failed -"+e.getMessage();
        }
    }
    /**
     * 
     * @throws Exception 
     */
    private static void loadKeyStore() throws Exception{
        //Check PKCS11 driver
        String lib = System.getenv("windir") + "\\system32\\aetpkss1.dll";
        File f = new File(lib);
        if (!f.exists()) {
            throw new Exception("TokenNotInstalled");
        }
        //Find CryptoToken
        for (int i = 0; i < 16; i++) {
            try {
                byte[] configBytes =
                      ("name = CryptoToken\n"
                    + "library = " + lib + "\n"
                    + "slotListIndex = " + i).getBytes("UTF-8");
                ByteArrayInputStream is = new ByteArrayInputStream(configBytes);
                p = new SunPKCS11(is);
                ks = KeyStore.getInstance("pkcs11", p);
                Security.addProvider(p);
                break;
            } catch (Exception e) {
                if(i==15){
                    throw new Exception("Crypto token not found - "+e.getMessage());
                }
            }
       }
       //Load KeyStore
       try{
           char[] pass = "nbpatil".toCharArray();
           ks.load(null, pass);
       }catch(Exception e){
           e.printStackTrace();
           throw new Exception("Loading crypto token failed - "+e.getMessage());
       }
    }
    /**
     * 
     * @throws java.lang.Exception
     */
    private static void removeKeyStore(){
        try{
            if(p != null){
                Security.removeProvider(p.getName());
            }
            p = null;
            ks = null;
            //MyUtils.showMessage("Token Logged Out");
        }catch(Exception e){
            
        }
    }
    //data
    private static Provider p = null;
    private static KeyStore ks = null;
}