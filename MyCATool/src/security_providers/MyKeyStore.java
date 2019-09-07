package security_providers;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mykeystores.MyDialog;
import mykeystores.Result;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequestHolder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;
import sun.security.pkcs11.SunPKCS11;
import sun.security.pkcs11.wrapper.CK_TOKEN_INFO;
import sun.security.pkcs11.wrapper.PKCS11;
/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@SuppressWarnings({"CallToThreadDumpStack", "UseSpecificCatch", "SleepWhileInLoop"})
public class MyKeyStore{
    public Provider providerBC;
    public Provider providerKeyStore;//Sun or pkcs11
    public static final  String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    public static final  String END_CERTIFICATE = "-----END CERTIFICATE-----";
    private final String alias;
    private String password;
    private KeyStore keyStore = null;
    public X509Certificate certificateX509 = null;
    public PrivateKey privateKey = null;//kake it private
    private String ksName = "";//keystore name
    private File keyStoreFile = null;
    private String tokenSNo = null;
    /**
     * 
     * @param alias
     * @param pass
     * @param keyStoreFile 
     */
    public MyKeyStore(String alias)  {
        this.alias = alias;
    }
    public String getTokenSNo(){
        return tokenSNo;
    }
    /**
     * 
     * @return 
     */
    public Provider[] listProviders(){
        Provider[] providers = Security.getProviders();
        for(Provider p:providers){
            System.out.println("provider: " + p.getInfo());
        }
        return providers;
    }
    /**
     * 
     * @return - file exists or not
     * @throws Exception 
     */
    public void loadKeyStorePKCS11(String ksName,  String dllPath) throws Exception {
        //Check PKCS11 driver
        this.ksName = ksName;
        keyStoreFile = null;
        File pkcs11DLL = new File(dllPath);
        if (!pkcs11DLL.exists()) {
            throw new Exception("TokenNotInstalled");
        }
        //Find CryptoToken
        for (int slot = 0; slot < 16; slot++) {
            try {
                //
                String config =
                  "name = " + ksName + "\n"
                + "library = " + dllPath + "\n"
                + "slotListIndex = " + slot+"\n"
                + "attributes(*, *, *) = {\n" 
                + "     CKA_TOKEN = true\n"
                + "}\n"
                + "attributes(*, CKO_PUBLIC_KEY, * ) = {\n"
                + "     CKA_ENCRYPT = true\n"
                + "     CKA_VERIFY = true\n"
                + "     CKA_WRAP = true\n"
                + "}\n"
                + "attributes( *, CKO_PRIVATE_KEY, *) = {\n"
                + "     CKA_PRIVATE = false\n"
                + "     CKA_SENSITIVE = true\n"
                + "     CKA_SIGN = true\n"
                + "     CKA_DECRYPT = true\n"
                + "     CKA_EXTRACTABLE =  false\n"
                + "     CKA_UNWRAP = true\n"
                + "}\n";
                
//                SunPKCS11 provider = new SunPKCS11ProviderFactory()
//        .withDescription("PKCS11TestProvider - libeToken 8")
//        .withName("PKCS11TestProvider")
//        .withLibrary("/lib64/libeToken.so.8").build();
//    Security.addProvider(provider);
//    KeyStore.CallbackHandlerProtection pinHandler
//        = new KeyStore.CallbackHandlerProtection(new TextCallbackHandler());
//    return KeyStore.Builder.newInstance("PKCS11",provider,pinHandler).getKeyStore();
                ByteArrayInputStream configStream = new ByteArrayInputStream(config.getBytes());
                providerBC = new BouncyCastleProvider();
                Security.addProvider(providerBC);
                providerKeyStore = new SunPKCS11(configStream);
                Security.addProvider(providerKeyStore);
                keyStore = KeyStore.getInstance("pkcs11", providerKeyStore);
                //
                PKCS11 pkcs11 = PKCS11.getInstance(dllPath, null, null, false);
                CK_TOKEN_INFO _token_info = pkcs11.C_GetTokenInfo(slot);
                String modal = new String(_token_info.model).trim();
                String label = new String(_token_info.label).trim();
                tokenSNo = new String(_token_info.serialNumber).trim();
                System.out.println("CryptoToken: "+label+"-"+modal+"-"+tokenSNo);
                ksName += "[slot-"+slot+"]";
                System.out.println(ksName+" ("+keyStore.getType()+") loaded.");
                break;
            } catch (Exception e) {
                if(slot==15){
                    throw new Exception(ksName+" not found - "+e.getMessage());
                }
            }
        }
        //Load KeyStore
        try{
            password = "Password#1";// "" for safenet dialog
            keyStore.load(null, password.toCharArray());
        }catch(Exception e){
            System.out.println(ksName+" load failed - "+e.getMessage());
            throw e;
        }
    }
    /**
     * 
     * @return - file exists or not
     * @throws Exception 
     */
    public void loadKeyStoreJKS(File keyStoreFile) throws Exception {
        ksName = keyStoreFile.getName();
        this.keyStoreFile = keyStoreFile;
        //set BC provider for rest of the operations
        providerBC = new BouncyCastleProvider();
        Security.addProvider(providerBC);
        providerKeyStore = providerBC;
        keyStore = KeyStore.getInstance("jks", Security.getProviders()[0]);//for reading jks key store
        Security.addProvider(providerKeyStore);
        password = "password";
        if(keyStoreFile.exists()){
            keyStore.load(new FileInputStream(keyStoreFile), password.toCharArray());
        }else{
            keyStore.load(null, null);
        }
    }
    
    /**
     * 
     */
    public Result loadPrivateKeyEntry() throws Exception {
        Result result = new Result();
        if(keyStore == null){
            throw new Exception("KeyStore not loaded! alias="+alias);
        }
        privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        if(privateKey == null){
            result.msg = "private key entry not found! alias="+alias;
            return result;
        }
        result.msg += ksName+" loaded: private key entry found with alias="+alias+"\n";
        java.security.cert.Certificate[] certChain = keyStore.getCertificateChain(alias);
        if(certChain==null || certChain.length==0){
            throw new Exception("CertificateChain NULL! alias="+alias);
        }
//        for(java.security.cert.Certificate certI : certChain){
//            X509Certificate cert1 = (X509Certificate) certI;
//            System.out.println("Cert = "+ cert1.getSubjectDN() + " <== "+cert1.getIssuerDN());
//        }
        certificateX509 = (X509Certificate) certChain[0];
        String certInfo = MyCertUtil.getCertificateInfo(certificateX509);
        result.msg += "Cert: " + certInfo;
        result.done = true;
        return result;
    }
    /**
     * 
     * @param alias
     * @param certChain
     * @throws Exception 
     */
    
    private void setSelfsignedEntry(PrivateKey key, X509Certificate[] certChain) throws Exception{
        keyStore.setKeyEntry(alias, key, password.toCharArray(), certChain);//replace self signed
        if(keyStoreFile!=null){
            keyStore.store(new FileOutputStream(keyStoreFile), password.toCharArray());
        }else{
            keyStore.store(null, null);//password.toCharArray()
        }
    }
    /**
     * 
     * @param alias
     * @param certFromCA
     * @throws Exception 
     */
    
    public String importCertificate(X509Certificate certFromCA) throws Exception{
//        JPanel p = new JPanel(new GridLayout(1,0));
//        p.add(new JLabel("Enter new Alias: "));
//        JTextField tf = new JTextField(10);
//        tf.setText(alias);
//        p.add(tf);
//        JOptionPane.showMessageDialog(null, p);
//        String newAlias = tf.getText().trim();
//        if(newAlias.equals(alias)){
//            throw new Exception("New Alias can't be same as temp alias!");
//        }
        if(!certificateX509.getPublicKey().equals(certFromCA.getPublicKey())){
            throw new Exception("Public key not matching!");
        }
        X509Certificate[] certChain = new X509Certificate[2];
        certChain[0] = certFromCA;
        certChain[1] = certificateX509;
        keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), certChain);//replace self signed
//        keyStore.setCertificateEntry(alias, certFromCA); fails
//        keyStore.deleteEntry(alias);
        if(keyStoreFile!=null){
            keyStore.store(new FileOutputStream(keyStoreFile), password.toCharArray());
        }else{
            keyStore.store(null, null);//password.toCharArray()
        }
        return "Certificate Imported: "+MyCertUtil.getCertificateInfo(certFromCA);
    }
    /**
     * 
     * @param alias
     * @throws Exception 
     */
    public String deleteEntry() throws Exception {
        if(!MyDialog.confirm("It will permanently delete private key entry '"+alias+"' from keystore.\n"
                + "Are you sure?")){
            return "Delete key operation canceled.";
        }
        keyStore.deleteEntry(alias);
        if(keyStoreFile!=null){
            keyStore.store(new FileOutputStream(keyStoreFile), password.toCharArray());
        }else{
            keyStore.store(null,null);
        }
        return "Private Entry "+alias+" deleted successfully.";
    }
    
    /**
     * 
     * @throws Exception 
     */
    public String exportCertificate() throws Exception {
        File dir = MyCertUtil.chooseDir();
        if(dir == null){
            return "certificate not exported!";
        }
        File certFile = new File(dir, alias+".cer");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(certFile))) {
            pw.println(BEGIN_CERTIFICATE);
            pw.println(MyCertUtil.toBase64Str(certificateX509));
            pw.println(END_CERTIFICATE);
            pw.flush();
        }
        return "Certificate exported at "+certFile.getAbsolutePath();
//        System.out.println("Certificate exported at "+certFile.getAbsolutePath());
    }
    /**
     * 
     * @return 
     */
    public String getAlias() {
        return alias;
    }
    /**
     * 
     * @return 
     */
    
    public X509Certificate getCert() {
        return certificateX509;
    }
    /**
     * 
     * @param alias
     * @throws Exception 
     */
    
    public X509Certificate getCertificate() throws Exception{
        return (X509Certificate) keyStore.getCertificate(alias);
    }
    /**
     *
     * @param alias
     * @param certSNo
     * @param issuerX500Name
     * @param subjectX500Name
     * @param notBefore
     * @param notAfter
     * @param basicConstraints
     * @param keyUsage
     * @throws Exception
     */
    private void generateSelfSignedCertificate(
            Settings settings,
            BigInteger certSNo,
            X500Name issuerX500Name,
            X500Name subjectX500Name,
            Date notBefore,
            Date notAfter,
            BasicConstraints basicConstraints,
            KeyUsage keyUsage
    ) throws Exception {
        //
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance(settings.pubKeyType, providerKeyStore);
        kpGen.initialize(settings.keySize, new SecureRandom());
        KeyPair pair = kpGen.generateKeyPair();
        //
        X509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(
                issuerX500Name,
                certSNo,
                notBefore,
                notAfter,
                subjectX500Name,
                pair.getPublic());
        //add extensions
        MyCertUtil.addAuthorityKeyIdentifier(x509v3CertificateBuilder, false, new AuthorityKeyIdentifierStructure(pair.getPublic()));
        MyCertUtil.addSubjectKeyIdentifier(x509v3CertificateBuilder, false, new SubjectKeyIdentifierStructure(pair.getPublic()));
        MyCertUtil.addBasicConstraints(x509v3CertificateBuilder, true, basicConstraints);
        MyCertUtil.addKeyUses(x509v3CertificateBuilder, true, keyUsage);
       
        //contentSignerBuilder
        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder(settings.signAlg).setProvider(providerKeyStore);
        ContentSigner contentSigner = contentSignerBuilder.build(pair.getPrivate());
         X509CertificateHolder x509CertificateHolder = x509v3CertificateBuilder.build(contentSigner);
        //gen userCert
        JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter().setProvider(providerBC);
        X509Certificate userCert = certificateConverter.getCertificate(x509CertificateHolder);
        userCert.checkValidity(new Date());
        userCert.verify(pair.getPublic());
        //add key entry
        X509Certificate[] certChain = new X509Certificate[1];
        certChain[0] = userCert;
        setSelfsignedEntry(pair.getPrivate(), certChain);
    }
    /**
     * 
     * @throws Exception 
     */
    public PKCS10CertificationRequest generateCSR() throws Exception {
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
            certificateX509.getSubjectX500Principal(),
            certificateX509.getPublicKey()
        );
        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder("SHA256withRSA").setProvider(providerKeyStore);
        ContentSigner contentSigner = contentSignerBuilder.build(privateKey);
        PKCS10CertificationRequestHolder csrHolder = p10Builder.build(contentSigner);
        if(!csrHolder.isSignatureValid(new JcaContentVerifierProviderBuilder().setProvider(providerBC).build(certificateX509.getPublicKey()))){
            throw new Exception("PKCS#10 request verify Failed!");
        }
        return new PKCS10CertificationRequest(csrHolder.getEncoded());
    }
    /**
     *
     * @param alias
     * @throws Exception
     */
    public String generateSelfSignedCertificate_ROOT_CA(Settings settings, X500Name subjectX500Name, Date notAfter) throws Exception {
        //
        Date notBefore = new Date(System.currentTimeMillis());
        BigInteger certSNo = generateCertSNo(0);
        //
        BasicConstraints basicConstraints = new BasicConstraints(2);
        KeyUsage keyUsage = new KeyUsage(
                KeyUsage.digitalSignature | 
                KeyUsage.keyEncipherment  |
                KeyUsage.keyAgreement     |
                KeyUsage.keyCertSign      |
                KeyUsage.nonRepudiation   
        );
        //
        generateSelfSignedCertificate(
            settings,
            certSNo, 
            subjectX500Name, 
            subjectX500Name, 
            notBefore, 
            notAfter, 
            basicConstraints, 
            keyUsage
        );
        return "New self signed root CA certificate generated sucessfully. alias="+alias;
    }
    /**
     *
     * @param alias
     * @throws Exception
     */
    public String generateSelfSignedCertificate(Settings settings, X500Name subjectX500Name, Date notAfter) throws Exception {
        Date notBefore = new Date(System.currentTimeMillis());
//        Date notAfter = new Date(System.currentTimeMillis()+(long)30*24*60*60*1000);
        BigInteger certSNo = generateCertSNo(0);
        //
        BasicConstraints basicConstraints = new BasicConstraints(false);
        KeyUsage keyUsage = new KeyUsage(KeyUsage.digitalSignature);//do not sign any data untill get certificate from CA 
        //
        generateSelfSignedCertificate(
            settings,
            certSNo, 
            subjectX500Name, 
            subjectX500Name, 
            notBefore, 
            notAfter, 
            basicConstraints, 
            keyUsage
        );
        return "New self signed certificate generated sucessfully. alias="+alias;
    }
    /**
     *
     * @param applicationSNo
     * @param notAfter
     * @param selfSignedCert
     * @return
     * @throws Exception
     */
    private X509Certificate signCertificate(
            int applicationSNo, 
            Date notAfter,
            X509Certificate selfSignedCert,
            BasicConstraints basicConstraints,
            KeyUsage keyUsage) 
    throws Exception {
//        System.out.println(MyCertUtil.getInfo(certificateX509));
//        System.out.println(MyCertUtil.getInfo(selfSignedCert));
        //
        Date notBefore = new Date(System.currentTimeMillis());
        BigInteger certSNo =  generateCertSNo(applicationSNo);
        //
        X509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(
                certificateX509.getSubjectX500Principal(),
                certSNo,
                notBefore,
                notAfter,
                selfSignedCert.getSubjectX500Principal(),
                selfSignedCert.getPublicKey()
        );
        
        //add extensions
        MyCertUtil.addAuthorityKeyIdentifier(x509v3CertificateBuilder, false, new AuthorityKeyIdentifierStructure(certificateX509));
        MyCertUtil.addSubjectKeyIdentifier(x509v3CertificateBuilder, false, new SubjectKeyIdentifierStructure(selfSignedCert.getPublicKey()));
        MyCertUtil.addBasicConstraints(x509v3CertificateBuilder, true, basicConstraints);
        MyCertUtil.addKeyUses(x509v3CertificateBuilder, true, keyUsage);

        //contentSignerBuilder
        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder("SHA256withRSA").setProvider(providerKeyStore);
        ContentSigner contentSigner = contentSignerBuilder.build(privateKey);
        X509CertificateHolder certHolder = x509v3CertificateBuilder.build(contentSigner);
//        System.out.println(certHolder.getIssuer());
//        System.out.println(certHolder.getSubject());
        
        //
        X509Certificate userCert = new JcaX509CertificateConverter().setProvider(providerBC).getCertificate(certHolder);
        userCert.checkValidity(new Date());
        userCert.verify(certificateX509.getPublicKey());
//        System.out.println(MyCertUtil.getInfo(userCert));
        return userCert;
    }
    /**
     *
     * @param applicationSNo
     * @param notAfter
     * @param selfSignedCert
     * @return
     * @throws Exception
     */
    private X509Certificate signCertificate(
            int applicationSNo, 
            Date notAfter,
            PKCS10CertificationRequest csr,
            BasicConstraints basicConstraints,
            KeyUsage keyUsage) 
    throws Exception {
//        System.out.println(MyCertUtil.getInfo(certificateX509));
//        System.out.println(MyCertUtil.getInfo(selfSignedCert));
        //
        Date notBefore = new Date(System.currentTimeMillis());
        BigInteger certSNo =  generateCertSNo(applicationSNo);
        //
        X509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(
                new X500Name( certificateX509.getSubjectX500Principal().getName()),
                certSNo,
                notBefore,
                notAfter,
                new X500Name(csr.getCertificationRequestInfo().getSubject().toString()),
                csr.getPublicKey()
        );
        //add extensions
        MyCertUtil.addAuthorityKeyIdentifier(x509v3CertificateBuilder, false, new AuthorityKeyIdentifierStructure(certificateX509));
        MyCertUtil.addSubjectKeyIdentifier(x509v3CertificateBuilder, false, new SubjectKeyIdentifierStructure(csr.getPublicKey()));
        MyCertUtil.addBasicConstraints(x509v3CertificateBuilder, true, basicConstraints);
        MyCertUtil.addKeyUses(x509v3CertificateBuilder, true, keyUsage);

        //contentSignerBuilder
//        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption").setProvider(providerBC);
        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder("SHA256withRSA").setProvider(providerKeyStore);
        ContentSigner contentSigner = contentSignerBuilder.build(privateKey);
        X509CertificateHolder certHolder = x509v3CertificateBuilder.build(contentSigner);
//        System.out.println(certHolder.getIssuer());
//        System.out.println(certHolder.getSubject());
        
        //
        X509Certificate userCert = new JcaX509CertificateConverter().setProvider(providerBC).getCertificate(certHolder);
        userCert.checkValidity(new Date());
        userCert.verify(certificateX509.getPublicKey());
//        System.out.println(MyCertUtil.getInfo(userCert));
        return userCert;
    }
    /**
     *
     * @param applicationSNo
     * @param notAfter
     * @param selfSignedCert_IntermediateCA
     * @return
     * @throws Exception
     */
    public X509Certificate signCertificate_INTERMEDIATE_CA(
            int applicationSNo, 
            Date notAfter, 
            X509Certificate selfSignedCert_IntermediateCA
    ) throws Exception {
        BasicConstraints basicConstraints = new BasicConstraints(1);
        KeyUsage keyUsage = new KeyUsage(
                KeyUsage.digitalSignature | 
                KeyUsage.keyEncipherment  |
                KeyUsage.keyAgreement     |
                KeyUsage.keyCertSign      |     //CA authority
                KeyUsage.nonRepudiation   
        );
        return signCertificate(applicationSNo, notAfter, selfSignedCert_IntermediateCA, basicConstraints, keyUsage);
    }
    /**
     * 
     * @param applicationSNo
     * @param notAfter
     * @param csr_IntermediateCA
     * @return
     * @throws Exception 
     */
    public X509Certificate signCertificate_INTERMEDIATE_CA(
            int applicationSNo, 
            Date notAfter, 
            PKCS10CertificationRequest csr_IntermediateCA
    ) throws Exception {
        BasicConstraints basicConstraints = new BasicConstraints(1);
        KeyUsage keyUsage = new KeyUsage(
                KeyUsage.digitalSignature | 
                KeyUsage.keyEncipherment  |
                KeyUsage.keyAgreement     |
                KeyUsage.keyCertSign      |     //CA authority
                KeyUsage.nonRepudiation   
        );
        return signCertificate(applicationSNo, notAfter, csr_IntermediateCA, basicConstraints, keyUsage);
    }
    /**
     *
     * @param applicationSNo
     * @param notAfter
     * @param selfSignedCert_EndEntity 
     * @return
     * @throws Exception
     */
    public X509Certificate signCertificate_END_ENTITY(
            int applicationSNo, 
            Date notAfter, 
            X509Certificate selfSignedCert_EndEntity
    ) throws Exception {
        BasicConstraints basicConstraints = new BasicConstraints(false);
        KeyUsage keyUsage = new KeyUsage(
                KeyUsage.digitalSignature | 
                KeyUsage.keyEncipherment  |
                KeyUsage.keyAgreement     |
                KeyUsage.nonRepudiation   
        );
        return signCertificate(applicationSNo, notAfter, selfSignedCert_EndEntity, basicConstraints, keyUsage);
    }
    /**
     * 
     * @param applicationSNo
     * @param notAfter
     * @param csr_EndEntity
     * @return
     * @throws Exception 
     */
    public X509Certificate signCertificate_END_ENTITY(
            int applicationSNo, 
            Date notAfter, 
            PKCS10CertificationRequest csr_EndEntity
    ) throws Exception {
        BasicConstraints basicConstraints = new BasicConstraints(false);
        KeyUsage keyUsage = new KeyUsage(
                KeyUsage.digitalSignature | 
                KeyUsage.keyEncipherment  |
                KeyUsage.keyAgreement     |
                KeyUsage.nonRepudiation   
        );
        return signCertificate(applicationSNo, notAfter, csr_EndEntity, basicConstraints, keyUsage);
    }
    /**
     *
     * @param applicationSNo
     * @return - 14 bytes cert_sno 
     * @throws Exception
     */
    private BigInteger generateCertSNo(int applicationSNo) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            dos.write("B".getBytes("UTF-8"));                           //1-1
            dos.writeInt(applicationSNo);                               //2-5
            dos.writeInt((int)(System.currentTimeMillis() / 1000));    //6-9
            byte[] randomBytes = new byte[5];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(randomBytes);
            secureRandom.nextBytes(randomBytes);
            dos.write(randomBytes);                                     //10-14
        }
        return new BigInteger(baos.toByteArray());
    }
    /**
     * 
     * @param data
     * @return
     * @throws Exception 
     */
    public byte[] sign(byte[] data)  throws Exception {
        Signature signature = Signature.getInstance("SHA256WithRSA", providerKeyStore);
        signature.initSign(privateKey);
        signature.update(data);
        byte[] signBytes = signature.sign();
        return signBytes;
    }
    /**
     *
     * @param data
     * @return
     * @throws Exception
     */
    public String signAndPack(byte[] data) throws Exception {
        //Build CMS   pkcs7
        //1 contentSigner
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").setProvider(providerKeyStore).build(privateKey);
        //2 digestCalculatorProvider
        JcaDigestCalculatorProviderBuilder digestCalculatorProviderBuilder = new JcaDigestCalculatorProviderBuilder().setProvider(providerBC);
        DigestCalculatorProvider digestCalculatorProvider = digestCalculatorProviderBuilder.build();
        //3 signerInfoGenerator
        JcaSignerInfoGeneratorBuilder signerInfoGeneratorBuilder = new JcaSignerInfoGeneratorBuilder(digestCalculatorProvider);
        SignerInfoGenerator signerInfoGenerator = signerInfoGeneratorBuilder.build(contentSigner, certificateX509);
        //4 certStore
        List<X509Certificate> certList = new ArrayList<>();
        certList.add(certificateX509);
        Store certStore = new JcaCertStore(certList);
        //5 signedDataGenerator
        CMSSignedDataGenerator signedDataGenerator = new CMSSignedDataGenerator();
        signedDataGenerator.addSignerInfoGenerator(signerInfoGenerator);
        signedDataGenerator.addCertificates(certStore);
        //6 signedData
        CMSSignedData signedData = signedDataGenerator.generate(new CMSProcessableByteArray(data), true);//true include data
        return new String(Base64.encode(signedData.getEncoded()), "UTF-8");
    }
//    /**
//     *
//     * @param signedDataBase64Str
//     * @return - data which was signed and verified
//     * @throws Exception
//     */
//    public byte[] verifyAndUnpack(String signedDataBase64Str) throws Exception {
//        CMSSignedData signedData = new CMSSignedData(Base64.decode(signedDataBase64Str.getBytes("UTF-8")));
//        Store store = signedData.getCertificates();
//        SignerInformationStore signerInformationStore = signedData.getSignerInfos();
//        Iterator iterator = signerInformationStore.getSigners().iterator();
//        StringBuilder verifyResult = new StringBuilder();
//        boolean overAllResult = true;
//        while (iterator.hasNext()) {
//            SignerInformation signerInformation = (SignerInformation) iterator.next();
//            Collection certCollection = store.getMatches(signerInformation.getSID());
//            Iterator certI = certCollection.iterator();
//            X509CertificateHolder certHolder = (X509CertificateHolder) certI.next();
//            X509Certificate cert = new JcaX509CertificateConverter().setProvider(providerBC).getCertificate(certHolder);
////            byte[] sign = signerInformation.getSignature();
////            System.out.println("SIGN  = "+new String(Base64.encode(sign), "UTF-8"));
//            boolean anyResult = signerInformation.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider(providerBC).build(cert));
//            verifyResult.append("Sign verify result ").append(anyResult).append(",  Certificate: - \n").append(MyCertUtil.getInfo(cert));
//            if(anyResult == false){
//                overAllResult = false;
//            }
//        }
//        JOptionPane.showMessageDialog(null, verifyResult.toString());
//        if(overAllResult == false){
//            throw new Exception("Sign verify failed!");
//        }
//        return (byte[])signedData.getSignedContent().getContent();
//    }
}