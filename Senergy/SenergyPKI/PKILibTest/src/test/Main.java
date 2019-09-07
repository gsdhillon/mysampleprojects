package test;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import pkilib.*;
/**
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        MSC.setServerURLBase("http://localhost:8084/RPGPKIServer");
        //testToken();
        //testSignVerifyData();
        //testSignVerifyFile();
        //testEncryptDecryptData();
        //testEncryptDecryptFile();
        testEncryptDecryptFile1();
        testDummySignVerifyFile();
    }
    /**
     * 
     */
    private static void testEncryptDecryptFile1() throws Exception{
        File f1 = new File("D:\\MySecApps_15032013\\PKI_SETUP_FOR_AAIS\\PKI api report.doc");
        File f2 = new File("D:\\MySecApps_15032013\\PKI_SETUP_FOR_AAIS\\encrypted.txt");
        File f3 = new File("D:\\MySecApps_15032013\\PKI_SETUP_FOR_AAIS\\decrypted.doc");
        //Use AESTool
        AESTool aesTool = new AESTool();
        GenKeyResult genKeyResult = aesTool.generateSeceretKey();
        if(genKeyResult.statusCode != 0){
            genKeyResult.show();
        }
        //encrypt file f1>f2
        InputStream in = new FileInputStream(f1);
        OutputStream out = new FileOutputStream(f2);
        EncryptStreamResult esr = aesTool.encryptStream(in, out);
        in.close();
        out.close();
        if(esr.statusCode !=0 ){
            esr.show();
        }
        //decrypt file f2>f3
        in = new FileInputStream(f2);
        out = new FileOutputStream(f3);
        DecryptStreamResult dsr = aesTool.decryptStream(in, out);
        in.close();
        out.close();
        if(dsr.statusCode!=0){
            dsr.show();
        }
        System.out.println("testEncryptDecryptFile1() Successful");
    }
    
    
    /**
     * 
     */
    private static void testEncryptFileForMultipleEmployees() throws Exception{
        //encrypt file for multiple employee
        ArrayList<String> empnoList = new ArrayList<>();
        empnoList.add("24196");
        empnoList.add("18770");
        ArrayList<GetKeyResult> gkr = encryptFileForMultipleEmp(empnoList);
        //decrypt file by any one employee
        String empNo = JOptionPane.showInputDialog("Enter EmpNo");
        int index = empnoList.indexOf(empNo);
        if(index==-1){
            JOptionPane.showMessageDialog(null, "File Not encrypted for "+empNo);
        }else{
            String encryptedKey = gkr.get(index).encryptedKey;
            decryptFileForEmp(encryptedKey, empNo);
        }
    }
    /**
     * 
     * @param empNoList
     * @return
     * @throws java.lang.Exception
     */
    public static ArrayList<GetKeyResult> encryptFileForMultipleEmp(
                                  ArrayList<String> empNoList) throws Exception{
        File f1 = new File("D:\\MySecApps_15032013\\PKI_SETUP_FOR_AAIS\\PKI api report.doc");
        File f2 = new File("D:\\MySecApps_15032013\\PKI_SETUP_FOR_AAIS\\PKI api report.doc");
        InputStream in = new FileInputStream(f1);
        OutputStream out = new FileOutputStream(f2);
        //Use AESTool
        AESTool aesTool = new AESTool();
        GenKeyResult genKeyResult = aesTool.generateSeceretKey();
        if(genKeyResult.statusCode != 0){
            genKeyResult.show();
            return null;
        }
        
        //encrypt file f1>f2
        EncryptStreamResult esr = aesTool.encryptStream(in, out);
        if(esr.statusCode !=0 ){
            esr.show();
            return null;        
        }
        //get encrypted secret key for each employee
        ArrayList<GetKeyResult> getKeyResultList = new ArrayList<>();
        for(int i = 0; i< empNoList.size(); i++){
            GetKeyResult getKeyResult = aesTool.getKey(empNoList.get(i));
           
            if(getKeyResult.statusCode!=0){
                getKeyResult.show();
                return null;
            }
            getKeyResultList.add(getKeyResult);
        }
        return getKeyResultList;
    }
    /**
     * 
     * @param encryptedKey
     * @param empNo
     * @throws java.lang.Exception
     */
    public static void decryptFileForEmp(String encryptedKey, String empNo)
                                                               throws Exception{
        File f1 = new File("D://RPG/RPGTest/encryptedFile.txt");
        File f2 = new File("D://RPG/RPGTest/decryptedFile["+empNo+"].txt");
        InputStream in = new FileInputStream(f1);
        OutputStream out = new FileOutputStream(f2);
        //Use AESTool
        AESTool aesTool = new AESTool();
        SetKeyResult setKeyResult = aesTool.SetSecretKey(encryptedKey, empNo);
        if(setKeyResult.statusCode!=0){
            setKeyResult.show();
            return;
        }
        //decrypt file f1>f2
        DecryptStreamResult dsr = aesTool.decryptStream(in, out);
        if(dsr.statusCode!=0){
            dsr.show();
        }
    }
    /**
     * 
     */
    private static void testSignVerifyData(){
        Date signDate = new Date();
        SignResult signResult = RSATool.sign(data, "24196");
        if(signResult.statusCode!=0){
            signResult.show();
            return;
        }
        //signDate = new Date();//incorrect date
        VerifyResult verifyResult = RSATool.verify(
                data, signResult.sign, signResult.certSNO, signResult.signDate);
        verifyResult.show();
        System.out.println("testSignVerifyData() Successful");
    }
    /**
     * 
     */
    private static void testSignVerifyFile(){
        InputStream is = null;
        File f = new File("D://RPG/RPGTest/sign-verify.txt");
        try{
            is = new FileInputStream(f);
        }catch(Exception e){
            System.out.println("Open for sign failed -"+f.getAbsolutePath());
            return;
        }
        Date signDate = MyUtils.stringToDate("07/08/2009 17:43:56");
        SignResult signResult = RSATool.sign(is, "24196");
        if(signResult.statusCode!=0){
            signResult.show();
            return;
        }
        try{
            is.close();
            is = new FileInputStream(f);
        }catch(Exception e){
            System.out.println("Open for verify failed  -"+f.getAbsolutePath());
            return;
        }
        VerifyResult verifyResult = RSATool.verify(
                is, signResult.sign, signResult.certSNO, signResult.signDate);
        verifyResult.show();
        System.out.println("testSignVerifyFile() Successful");
    }
    /**
     * 
     */
    private static void testDummySignVerifyFile(){
        InputStream is = null;
        File f = new File("D:\\MySecApps_15032013\\PKI_SETUP_FOR_AAIS\\PKI api report.doc");
        try{
            is = new FileInputStream(f);
        }catch(Exception e){
            System.out.println("Open for sign failed -"+f.getAbsolutePath());
            return;
        }
        SignResult signResult = RSATool.signDummy(is);
        if(signResult.statusCode!=0){
            signResult.show();
            return;
        }
        try{
            is.close();
            is = new FileInputStream(f);
        }catch(Exception e){
            System.out.println("Open for verify failed  -"+f.getAbsolutePath());
            return;
        }
        VerifyResult verifyResult = RSATool.verifyDummy(is, signResult.sign, signResult.signDate);
        verifyResult.show();
        System.out.println("testDummySignVerifyFile() Successful");
    }
    /**
     * 
     */
    private static void testEncryptDecryptData() throws Exception{
        //encrypt data
        AESTool desTool = new AESTool();
        desTool.generateSeceretKey();
        GetKeyResult getKeyResult = desTool.getKey("24196");
        if(getKeyResult.statusCode!=0){
            getKeyResult.show();
            return;
        }
        EncryptDataResult edr = desTool.encryptData(data);
        if(edr.statusCode!=0){
            edr.show();
            return;
        }
        //decrypt data
        desTool = new AESTool();
        desTool.SetSecretKey(getKeyResult.encryptedKey, "24196");
        DecryptDataResult ddr = desTool.decryptData(edr.encryptedData);
        if(ddr.statusCode!=0){
            ddr.show();
            return;
        }
        //show decrypted data back
        for(int i=0;i<ddr.data.length;i++){
            if(ddr.data[i]!=data[i]){
                System.out.println("testEncryptDecryptData() Failed");
                return;
            }
        }
        System.out.println("testEncryptDecryptData() Successful");
    }
    /**
     * 
     */
    private static void testEncryptDecryptFile() throws Exception{
        File f1 = new File("D://RPG/RPGTest/encrypt-decrypt.txt");
        File f2 = new File("D://RPG/RPGTest/f2.txt");
        File f3 = new File("D://RPG/RPGTest/f3.txt");
        //encrypt f1 -> f2
        InputStream in = new FileInputStream(f1);
        OutputStream out = new FileOutputStream(f2);
        AESTool desTasks = new AESTool();
        GenKeyResult gkr = desTasks.generateSeceretKey();
        if(gkr.statusCode!=0){
            gkr.show();
            return;
        }
        GetKeyResult getKeyResult = desTasks.getKey("24196");
        if(getKeyResult.statusCode!=0){
            getKeyResult.show();
            return;
        }
        EncryptStreamResult esr = desTasks.encryptStream(in, out);
        if(esr.statusCode!=0){
            esr.show();
            return;
        }
        //decrypt f2 -> f3
        in = new FileInputStream(f2);
        out = new FileOutputStream(f3);
        desTasks = new AESTool();
        SetKeyResult skr = desTasks.SetSecretKey(getKeyResult.encryptedKey, "24196");
        if(skr.statusCode!=0){
            skr.show();
            return;
        }
        DecryptStreamResult dsr = desTasks.decryptStream(in, out);
        if(dsr.statusCode!=0){
            dsr.show();
            return;
        }
        System.out.println("testEncryptDecryptFile() Successful");
    }
    /**
     * 
     */
    private static void testToken() {
        System.out.println(RSATool.getCertificate().getInfo());
    }
    
    //data
    private static  byte[] data = 
            ("aaaaaaaaaaaaaaaa bbbbbbbbbbbbbbbbbbbbbbb " +
             "cccccccccccccccc ddddddddddddddddddddddd " +
             "$$$$$$$$$$$$$$$$ &&&&&&&&&&&&&&&&&&&&&&& " +
             "~~~~~~~~~~~~~~~~ ``````````````````````` " +
             "vdsf65^%Z67zxcz87 cv897vx9cz8").getBytes();
}