package mykeystores;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.*;
import org.bouncycastle.util.encoders.Base64;
import security_providers.MyCertUtil;
/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@SuppressWarnings("CallToThreadDumpStack")
public class MyKeyGUI extends JPanel{
    private MyKey key = null;
    private JTextField aliasTxt = new JTextField();
    private JTextArea inputOutputTxtArea = new JTextArea();
    private JTextArea statusTxtArea = new JTextArea();
    private JTextField jksOrDllFileNameTxt = new JTextField();
    private JComboBox<String> keyStoreTypeCombo = new JComboBox<>(new String[]{
        "JKS",
        "PKCS11"
    });
    private JComboBox<String> requestTypeCombo = new JComboBox<>(new String[]{
        "CSR",
        "SELF_SIGNED_CERT"
    });
    private JComboBox<String> certTypeCombo = new JComboBox<>(new String[]{
        "END_ENTITY",
        "INTERMEDIATE_CA"
    });
    private JTextField keyStorePathTxt;
    private final String storePath;
    private final int type; //0 root ca, 1 int. ca, 2 end entity
    private String defaultAlias; 
    private String defaultJKSFile;
    private String pkcs11Dll = "dkck201.dll";
    public static final int TYPE_ROOT_CA = 0;
    public static final int TYPE_INTERMEDIATE_CA = 1;
    public static final int TYPE_END_ENTITY = 2;
    /**
     * 
     */
    public MyKeyGUI(String storePath, int type){
        this.storePath = storePath;
        this.type = type;
        switch(type){
            case TYPE_ROOT_CA: 
                defaultAlias = "BARC_CA"; 
                defaultJKSFile="barc_ca.jks";
                break;
            case TYPE_INTERMEDIATE_CA: 
                defaultAlias = "BARC_SUB_CA_1"; 
                defaultJKSFile="barc_sub_ca_1.jks";
                break;
            case TYPE_END_ENTITY: 
                defaultAlias = "24196"; 
                defaultJKSFile="24196.jks";
                break;
        }
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(0,1));
        topPanel.add(keystoreFileDirPanel());
        topPanel.add(createButtonsPanel());
        add(topPanel, BorderLayout.NORTH);
        //////
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        //
        JPanel p1 = new JPanel(new BorderLayout());
        p1.setBorder(BorderFactory.createTitledBorder("CSR / CERTIFICATE"));
        //inputOutputTxtArea.setLineWrap(true);
        p1.add(new JScrollPane(inputOutputTxtArea), BorderLayout.CENTER);
        p1.add(dataSignCommands(), BorderLayout.SOUTH);
        splitPane.setTopComponent(p1);
        //
        JPanel p2 = new JPanel(new GridLayout(1,1));
        p2.setBorder(BorderFactory.createTitledBorder("STATUS"));
//        statusTxtArea.setLineWrap(true);
        p2.add(new JScrollPane(statusTxtArea));
        splitPane.setBottomComponent(p2);
        ///////
        add(splitPane, BorderLayout.CENTER);
        splitPane.setDividerLocation(500);
        splitPane.setDividerSize(15);
    }
    /**
     * 
     * @return 
     */
    private JPanel keystoreFileDirPanel(){
        JPanel p = new JPanel(new GridLayout(1,0));
        //
        JPanel p1 = new JPanel(new FlowLayout());
        keyStorePathTxt = new JTextField(storePath);
        keyStorePathTxt.setPreferredSize(new Dimension(250, 20));
        keyStoreTypeCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(keyStoreTypeCombo.getSelectedIndex()==0){
                    keyStorePathTxt.setText(storePath);
                    jksOrDllFileNameTxt.setText(defaultJKSFile);
                }else{
                    keyStorePathTxt.setText(System.getenv("windir") + "\\system32\\");
                    jksOrDllFileNameTxt.setText(pkcs11Dll);
                }
            }
        });
        p1.add(keyStoreTypeCombo);//, BorderLayout.WEST
        p1.add(keyStorePathTxt);//, BorderLayout.CENTER
        JButton b = new JButton("Browse");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseKSDir();
            }
        });
        p1.add(b);//, BorderLayout.EAST
        p.add(p1);
        //
        JPanel p2 = new JPanel(new FlowLayout());
        p2.add(new JLabel("Alias: "));
        aliasTxt.setPreferredSize(new Dimension(90, 20));
        aliasTxt.setText(defaultAlias);
        p2.add(aliasTxt);
        p2.add(new JLabel("KeyStore/pkcs11 dll file: "));
        jksOrDllFileNameTxt.setPreferredSize(new Dimension(110, 20));
        jksOrDllFileNameTxt.setText(defaultJKSFile);
        p2.add(jksOrDllFileNameTxt);
        p.add(p2);
        return p;
    }
    /**
     * 
     */
    private void chooseKSDir() {
        File dir = MyCertUtil.chooseDir();
        if(dir != null){
            keyStorePathTxt.setText(dir.getAbsolutePath());
        }
    }
    /**
     * 
     * @return 
     */
    private JPanel createButtonsPanel(){
        JPanel p = new JPanel(new GridLayout(1, 0));
        //
        JButton b = new JButton("Load KS");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadKeyStore();
            }
        });
        p.add(b);
//        //
//        b = new JButton("GetKeyPair");
//        b.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(alias == null){
//                    JOptionPane.showMessageDialog(null, "Pl Load Keystore first!");
//                }else{
//                    try {
//                        generateSelfSignedAndLoad("");
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        println("Exception: "+ex.getMessage());
//                    }
//                }
//            }
//        });
//        p.add(b);
        if(type != 0){
            //
            b = new JButton("GetCert");
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getCert();
                }
            });
            p.add(b);
            //
            b = new JButton("GenerateCSR");
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    generateCSR();
                }
            });
            p.add(b);
        }
        //
        if(type != 2){//for ROOT and IT. CAs
            p.add(requestTypeCombo);
          }
        //
        if(type == 0){//for ROOT
            p.add(certTypeCombo);
        }
        //
        //
        b = new JButton("Sign Cert");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signCertificate();
            }
        });
        p.add(b);
        //
        b = new JButton("ImportCert");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importCACert();
            }
        });
        p.add(b);
        //
        b = new JButton("ExportCert");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportCert();
            }
        });
        p.add(b);
        if(type==2){
            b = new JButton("DeleteKey");
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteKeyEntry();
                }
            });
            p.add(b);
        }
        
        return p;
    }
    private void loadKeyStore(){
        if(keyStoreTypeCombo.getSelectedIndex()==0){
            loadKeyStoreJKS();
        }else{
            loadKeyStorePKCS11();
        }
    }
    private String alias = null;
    /**
     * 
     */
    private void loadKeyStoreJKS() {
        try {
            alias = aliasTxt.getText().trim();
            File file = new File(keyStorePathTxt.getText() + "/" + jksOrDllFileNameTxt.getText().trim());
            key = new MyKey(alias);
            key.loadJKS(file);
            Result result = key.loadPrivateKeyEntry();
            if(!result.done){
                generateSelfSignedAndLoad("Private key entry with alias '" + alias + "' does not exists.\n");
            }else{
                println(result.msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            println("Ex on loadKeyStoreJKS: "+e.getMessage());
        }
    }
    
    /**
     * 
     */
    private void loadKeyStorePKCS11() {
        try {
            alias = aliasTxt.getText().trim();
            String dllPath = keyStorePathTxt.getText() + "\\" + jksOrDllFileNameTxt.getText().trim();
            File file = new File(dllPath);
            key = new MyKey(alias);
            if(!file.exists()){
                println(dllPath +" does not exists.");
            }else{
                key.loadPKCS11("CryptoToken", dllPath);
                Result result = key.loadPrivateKeyEntry();
                if(!result.done){
                    generateSelfSignedAndLoad("Private key entry with alias '" + alias + "' does not exists.\n");
                }else{
                    println(result.msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            println("Ex on loadKeyStorePKCS11: "+e.getMessage());
        }
    }

    /**
     * 
     */
    private void getCert() {
        try {
            String certBase64Str = key.getCertificate();
            inputOutputTxtArea.setText(certBase64Str);
            println("x509 certificate in base64 extracted");
        } catch (Exception e) {
            e.printStackTrace();
            println("Ex on getCert: "+e.getMessage());
        }
    }
    /**
     * 
     */
    private void generateCSR() {
        try {
            String csrBase64Str = key.generateCSR();
            inputOutputTxtArea.setText(csrBase64Str);
        } catch (Exception e) {
            e.printStackTrace();
            println("Ex on generateCSR: "+e.getMessage());
        }
    }
    /**
     * 
     */
    private void signCertificate() {
        switch((String)certTypeCombo.getSelectedItem()){
            case "INTERMEDIATE_CA":
                signIntermediateCACert((String)requestTypeCombo.getSelectedItem());
                break;
            case "END_ENTITY":
                signEndEntityCert((String)requestTypeCombo.getSelectedItem());
                break;
        }
    }
    
    /**
     * 
     */
    private void importCACert() {
        try {
            String certFromCABase64Str = inputOutputTxtArea.getText().trim();
            key.importCACertificate(certFromCABase64Str);
            inputOutputTxtArea.setText("");
            println("<<<<<<<<CA Certificate Imported Successfully>>>>>>>>>");
        } catch (Exception e) {
            e.printStackTrace();
            println("Ex on importCACert: "+e.getMessage());
        }
    }
    /**
     * 
     */
    private void signIntermediateCACert(String requestType) {
        try {
            String certBase64Str;
            switch(requestType){
                case "CSR":
                    String csrBase64Str = inputOutputTxtArea.getText().trim();
                    certBase64Str = key.signIntermediateCACertFromCSR(1111, csrBase64Str);
                    break;
                case "SELF_SIGNED_CERT":
                    String selfCertBase64Str = inputOutputTxtArea.getText().trim();
                    certBase64Str = key.signIntermediateCACertFromSelfSignedCert(1111, selfCertBase64Str);
                    break;
                default:
                    return;
            }
            inputOutputTxtArea.setText(certBase64Str);
            println("<<<<<<<<Intermidiate CA Certificate Signed Successfully>>>>>>>>>");
        } catch (Exception e) {
            e.printStackTrace();
            println("Ex on signIntermediateCACert: "+e.getMessage());
        }
    }
    
    
    /**
     * 
     */
    private void signEndEntityCert(String requestType) {
        try {
            String certBase64Str;
            switch(requestType){
                case "CSR":
                    String csrBase64Str = inputOutputTxtArea.getText().trim();
                    certBase64Str = key.signEndEntityCertFromCSR(2222, csrBase64Str);
                    break;
                case "SELF_SIGNED_CERT":
                    String selfCertBase64Str = inputOutputTxtArea.getText().trim();
                    certBase64Str = key.signEndEntityCertFromSelfSignedCert(1111, selfCertBase64Str);
                    break;
                default:
                    return;
            }
            inputOutputTxtArea.setText(certBase64Str);
            println("<<<<<<<<EndEntity Certificate Signed Successfully>>>>>>>>>");
        } catch (Exception e) {
            e.printStackTrace();
            println("Ex on signEndEntityCert: "+e.getMessage());
        }
    }
    /**
     * 
     */
    private void exportCert() {
        try {
            println(key.exportCertficate());
        } catch (Exception e) {
            e.printStackTrace();
            println("Ex on exportCert: "+e.getMessage());
        }
    }
    /**
     * 
     */
    private void deleteKeyEntry() {
        try {
            println(key.deleteEntry());
        } catch (Exception e) {
            e.printStackTrace();
            println("Ex on deleteKey: "+e.getMessage());
        }
    }
    /**
     * 
     * @param msg 
     */
    private void println(String msg) {
        statusTxtArea.setText(statusTxtArea.getText() + msg + "\n");
    }
    
    private void signAndPack(){
        try{
            String base64Data = inputOutputTxtArea.getText().trim().replaceAll("\n","");
            byte[] data = Base64.decode(base64Data.getBytes("UTF-8"));
            String base64SignedData = key.signAndPack(data);
            inputOutputTxtArea.setText(MyCertUtil.splitIntoMultLines(base64SignedData));
        }catch(Exception e){
            e.printStackTrace();
            println("Ex on signAndPack: "+e.getMessage());
        }
    }
    private void verifyAndUnpack() {
        try{
            String base64SignedData = inputOutputTxtArea.getText().trim().replaceAll("\n","");
            byte[] data = MyCertUtil.verifyAndUnpack(base64SignedData);
            String base64Data = new String(Base64.encode(data), "UTF-8");
            inputOutputTxtArea.setText(MyCertUtil.splitIntoMultLines(base64Data));
        }catch(Exception e){
            e.printStackTrace();
            println("Ex on verifyAndUnpack: "+e.getMessage());
        }
    }
    private void cut() {
        MyCertUtil.cutPasteStr = inputOutputTxtArea.getText();
        inputOutputTxtArea.setText("");
    }
    private void paste() {
        inputOutputTxtArea.setText(MyCertUtil.cutPasteStr);
        MyCertUtil.cutPasteStr = "";
    }
    private void clearLog() {
        statusTxtArea.setText("");
    }
    /**
     * 
     * @return 
     */
    private Component dataSignCommands() {
        JPanel p = new JPanel(new GridLayout(1,0));
        JPanel p1 = new JPanel(new GridLayout(1,0));
        JButton b = new JButton("SignAndPack");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signAndPack();
            }
        });
        p1.add(b);
        //
        b = new JButton("VerifyAndUnpack");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyAndUnpack();
            }
        });
        p1.add(b);
        p.add(p1);
        //
        JPanel p2 = new JPanel(new GridLayout(1,0));
        b = new JButton("Cut");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cut();
            }
        });
        p2.add(b);
        //
        b = new JButton("Paste");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paste();
            }
        });
        p2.add(b);
        //
        b = new JButton("Clear Log");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLog();
            }
        });
        p2.add(b);
        p.add(p2);
        return p;
    }
    /**
     * 
     * @param msg
     * @throws Exception 
     */
    private void generateSelfSignedAndLoad(String msg) throws Exception{
        if (MyDialog.confirm(msg + "Create new RSA keypair with self signed certificate?")) {
            switch(type){
                case 0: println(key.generateRootCACertificate()); break;
                case 1: println(key.generateIntermediateCACertificate()); break;
                case 2: println(key.generateEndEntityCertificate()); break;
            }
            Result result = key.loadPrivateKeyEntry();
            println(result.msg);
        }
    }
    
}