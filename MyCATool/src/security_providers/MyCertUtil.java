package security_providers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pkcs.PKCS10CertificationRequestHolder;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;
/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class MyCertUtil {
    public static String cutPasteStr = "";
    /**
     * 
     * @param certificateX509
     * @return
     * @throws Exception 
     */
    public static String getCertificateInfo(X509Certificate certificateX509) throws Exception {
        String sub = certificateX509.getSubjectDN().getName();
        sub = sub.substring(sub.indexOf("CN="));
        if (sub.indexOf(",") > 0) {
            sub = sub.substring(0, sub.indexOf(","));
        }
        //
        String issuer = certificateX509.getIssuerDN().getName();
        issuer = issuer.substring(issuer.indexOf("CN="));
        if (issuer.indexOf(",") > 0) {
            issuer = issuer.substring(0, issuer.indexOf(","));
        }
        Date notBeforeDate = certificateX509.getNotBefore();
        Date notAfterDate = certificateX509.getNotAfter();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return "CertSNo="+certificateX509.getSerialNumber().toString(16) +", Subject" + sub + ", Issuer" + issuer + ", NotBefore=" + df.format(notBeforeDate) + ", NotAfter=" + df.format(notAfterDate);
    }
    /**
     * 
     * @param csr
     * @return
     * @throws Exception 
     */
    public static String toBase64Str(PKCS10CertificationRequest csr) throws Exception {
        return splitIntoMultLines(new String(Base64.encode(csr.getEncoded()), "UTF-8"));
    }
    /**
     * 
     * @param csrHolder
     * @return
     * @throws Exception 
     */
    public static String toBase64Str(PKCS10CertificationRequestHolder csrHolder) throws Exception {
        return splitIntoMultLines(new String(Base64.encode(csrHolder.getEncoded()), "UTF-8"));
    }
    /**
     *
     * @param csrBase64Str
     * @return
     * @throws Exception
     */
    public static PKCS10CertificationRequest makeCSR(String csrBase64Str) throws Exception {
        if(csrBase64Str==null || csrBase64Str.length() < 100){
            throw new Exception("INVALID CSR");
        }
        csrBase64Str.replaceAll("\n", "");
        return new PKCS10CertificationRequest(Base64.decode(csrBase64Str.getBytes("UTF-8")));
    }
    /**
     * 
     * @param caCertBase64Str
     * @return
     * @throws Exception 
     */
    public static X509Certificate makeX509Cert(String caCertBase64Str) throws Exception {
//        if(caCertBase64Str.indexOf("BEGIN CERTIFICATE") == -1){
//            caCertBase64Str = 
//                    MyKeyStoreJCA.BEGIN_CERTIFICATE+"\n"
//                    +caCertBase64Str+"\n"
//                    +MyKeyStoreJCA.END_CERTIFICATE;
//        }
        if(caCertBase64Str==null || caCertBase64Str.length() < 100){
            throw new Exception("INVALID BASE64 CERT");
        }
        caCertBase64Str.replaceAll("\n", "");
        X509Certificate x509Certificate;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(caCertBase64Str.getBytes("UTF-8")))) {
            x509Certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
        }
        return x509Certificate;
    }
    /**
     * 
     * @param x509Certificate
     * @return
     * @throws Exception 
     */
    public static String toBase64Str(X509Certificate x509Certificate) throws Exception {
        return splitIntoMultLines(new String(Base64.encode(x509Certificate.getEncoded()), "UTF-8"));
    }
    /**
     * 
     * @param input
     * @return 
     */
    public static String splitIntoMultLines(String input){
        if(input==null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int start = 0; int lineSize = 120;
        while(input.length() > start+lineSize){
            sb.append(input.substring(start, start+lineSize)).append("\n");
            start+=lineSize;
        }
        sb.append(input.substring(start));
        return sb.toString();
    }
    private static File rememberedDir = new File("D:/");
    /**
     * 
     * @return 
     */
    public static File chooseDir() {
        JFileChooser chooser = new JFileChooser(rememberedDir);
        chooser.setDialogTitle("Select keystores directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        int returnVal = chooser.showOpenDialog(new JFrame());
        if(returnVal != JFileChooser.APPROVE_OPTION){
            return null;
        }
        rememberedDir = chooser.getSelectedFile();
        return rememberedDir;
    }
    /**
     * 
     * @param x509v3CertificateBuilder
     * @param critical
     * @param authorityKeyIdentifierStructure
     * @throws Exception 
     */
    public static void addAuthorityKeyIdentifier(
            X509v3CertificateBuilder x509v3CertificateBuilder,
            boolean critical,
            AuthorityKeyIdentifierStructure authorityKeyIdentifierStructure
    ) throws Exception {
        x509v3CertificateBuilder.addExtension(
            new ASN1ObjectIdentifier("2.5.29.35"), 
            critical, 
            authorityKeyIdentifierStructure
        );
    }
    /**
     * 
     * @param x509v3CertificateBuilder
     * @param critical
     * @param subjectKeyIdentifierStructure
     * @throws Exception 
     */
    public static void addSubjectKeyIdentifier(
            X509v3CertificateBuilder x509v3CertificateBuilder, 
            boolean critical, 
            SubjectKeyIdentifierStructure subjectKeyIdentifierStructure
    ) throws Exception {
        x509v3CertificateBuilder.addExtension(
            new ASN1ObjectIdentifier("2.5.29.14"), 
            critical, 
            subjectKeyIdentifierStructure
        );
    }
    /**
     * 
     * @param x509v3CertificateBuilder
     * @param critical
     * @param basicConstraints
     * @throws Exception 
     */
    public static void addBasicConstraints(
            X509v3CertificateBuilder x509v3CertificateBuilder, 
            boolean critical, 
            BasicConstraints basicConstraints
    ) throws Exception {
        x509v3CertificateBuilder.addExtension(
            new ASN1ObjectIdentifier("2.5.29.19"), 
            critical, 
            basicConstraints
        );
    }
    /**
     * 
     * @param x509v3CertificateBuilder
     * @param critical
     * @param keyUsage
     * @throws Exception 
     */
    public static void addKeyUses(
            X509v3CertificateBuilder x509v3CertificateBuilder, 
            boolean critical, 
            KeyUsage keyUsage
    ) throws Exception {
        x509v3CertificateBuilder.addExtension(
            new ASN1ObjectIdentifier("2.5.29.15"), 
            critical, 
            keyUsage
        );
    }
    
    
    /**
     *
     * @param signedDataBase64Str
     * @return - data which was signed and verified
     * @throws Exception
     */
    public static byte[] verifyAndUnpack(String signedDataBase64Str) throws Exception {
        Provider providerBC = new BouncyCastleProvider();
        Security.addProvider(providerBC);
        CMSSignedData signedData = new CMSSignedData(Base64.decode(signedDataBase64Str.getBytes("UTF-8")));
        Store store = signedData.getCertificates();
        SignerInformationStore signerInformationStore = signedData.getSignerInfos();
        Iterator iterator = signerInformationStore.getSigners().iterator();
        StringBuilder verifyResult = new StringBuilder();
        boolean overAllResult = true;
        while (iterator.hasNext()) {
            SignerInformation signerInformation = (SignerInformation) iterator.next();
            Collection certCollection = store.getMatches(signerInformation.getSID());
            Iterator certI = certCollection.iterator();
            X509CertificateHolder certHolder = (X509CertificateHolder) certI.next();
            X509Certificate cert = new JcaX509CertificateConverter().setProvider(providerBC).getCertificate(certHolder);
//            byte[] sign = signerInformation.getSignature();
//            System.out.println("SIGN  = "+new String(Base64.encode(sign), "UTF-8"));
            boolean anyResult = signerInformation.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider(providerBC).build(cert));
            verifyResult.append("Sign verify result ").append(anyResult).append(",  Certificate: - \n").append(MyCertUtil.getCertificateInfo(cert));
            if(anyResult == false){
                overAllResult = false;
            }
        }
        JOptionPane.showMessageDialog(null, verifyResult.toString());
        if(overAllResult == false){
            throw new Exception("Sign verify failed!");
        }
        return (byte[])signedData.getSignedContent().getContent();
    }
}
