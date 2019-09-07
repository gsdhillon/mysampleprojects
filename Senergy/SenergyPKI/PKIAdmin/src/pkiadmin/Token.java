package pkiadmin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.swing.JFrame;
import sun.security.pkcs11.SunPKCS11;
import sun.security.x509.CertAndKeyGen;
import sun.security.x509.X500Name;

/**
 * Token.java
 * Created on Oct 5, 2007, 10:58:03 AM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class Token {
    
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
            e.printStackTrace();
            MyUtils.showException("While geting certificate from the token.",e);
            return null;
        } finally {
            removeKeyStore();
        }
    }
    /**
     *setkey
     * @param name 
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
            keypair.generate(2048);//TODO GSD 1024
            PrivateKey privKey = keypair.getPrivateKey();
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = keypair.getSelfCertificate(x500Name, 10*365*24*60*60);
            ks.setKeyEntry(alias, privKey, null, chain);
            ks.store( null, null);
            removeKeyStore();
            return "Key pair has been generated successfully";
        }catch(Exception e){
            e.printStackTrace();
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
                      ("name = CryptoTken\n"
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
           System.out.println("ks.load() ***");
           throw e;
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