package pkiadmin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Main.java
 * Created on Apr 2, 2009, 1:02:16 PM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 */
public class Main extends JFrame implements ActionListener{
    JTextArea textArea = new JTextArea();
    /**
     * 
     */
    public Main(){
        setLayout(new BorderLayout());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane sc = new JScrollPane(textArea);
        add(sc, BorderLayout.CENTER);
        JPanel bp = createButtonsPane();
        add(bp, BorderLayout.SOUTH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w=screenSize.width;
        int h=screenSize.height-30;
        setSize(new Dimension(w,h));
        setResizable(false);
        //add windows listner
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new MyWindowAdapter(this));
    }
    /**
     * 
     */
    private void getListOfEmployree() {
        String response = PKIAdminTasks.getListOfEmployee();
        showMessage(response.replaceAll(";", "\n"));
    }
    /**
     * 
     */
    private class MyWindowAdapter extends WindowAdapter{
     	Main frame;
     	public MyWindowAdapter(Main frame){
            this.frame = frame;
       	}
        @Override
     	public void windowClosing(WindowEvent e){
            frame.close();
        }
    }
    /**
     * 
     */
    private void close(){
        System.exit(0);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(!Config.setParameters()){
            return;
        }
        MSC.setServerURLBase(Config.pkiServerURLBase);
        Main main = new Main();
        main.setVisible(true);
    }
   /**
     * 
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("genkey")){
            generateKey();
        }else if(e.getActionCommand().equals("viewcert")){
            showCertificate();
        }else if(e.getActionCommand().equals("register")){
            //registerEmployee("18770", "Rohitashva Sharma");
            registerEmployee();
        }else if(e.getActionCommand().equals("updatecert")){
            updateCert();
        }else if(e.getActionCommand().equals("getListOfEmp")){
            getListOfEmployree();
        }else if(e.getActionCommand().equals("downloademp")){
            downloadCertByEmpno();
        }else if(e.getActionCommand().equals("downloadsno")){
            downloadCertBySno();
        }
    }
    /**
     * 
     * @return
     */
    private JPanel createButtonsPane() {
        JPanel p = new JPanel(new GridLayout(1, 0));
        //gen key pair
        JButton b = new JButton("Generate Keypair");
        b.setActionCommand("genkey");
        b.addActionListener(this);
        p.add(b);
        //view certificate
        b = new JButton("ViewTokenCert");
        b.setActionCommand("viewcert");
        b.addActionListener(this);
        p.add(b);
        //register user
        b = new JButton("Register Employee");
        b.setActionCommand("register");
        b.addActionListener(this);
        p.add(b);
        //update certificate
        b = new JButton("Update Certificate");
        b.setActionCommand("updatecert");
        b.addActionListener(this);
        p.add(b);
        //backup cert
        b = new JButton("Show Employee List");
        b.setActionCommand("getListOfEmp");
        b.addActionListener(this);
        p.add(b);
        //download cert by empno
        b = new JButton("Download Cert by empno");
        b.setActionCommand("downloademp");
        b.addActionListener(this);
        p.add(b);
        //download cert by sno
        b = new JButton("Download Cert by sno");
        b.setActionCommand("downloadsno");
        b.addActionListener(this);
        p.add(b);
        return p;
    }
    /**
     * 
     */
    private void showCertificate() {
        CertInfo certInfo = Token.getCertificate();
        if(certInfo!=null){
            showMessage(certInfo.getInfo());
        }else{
            showMessage("certificate not found!");
        }
    }
    /**
     * 
     * @param empno
     */
    private void downloadCertByEmpno() {
        String empno = JOptionPane.showInputDialog(this, "Enter empno");
        //download
        CertInfo certInfo = PKIAdminTasks.getCertificateFromEmpno(empno);
        certInfo.alias = empno;
        if(certInfo!=null){
            showMessage(certInfo.getInfo());
        }else{
            showMessage("download certificate failed!");
        }
    }
    /**
     * 
     * @param string
     */
    private void downloadCertBySno() {
        String sno = JOptionPane.showInputDialog(this, "Enter cert_SNO");
        //download by sno
        CertInfo certInfo = PKIAdminTasks.getCertificateFromSNO(sno);
        if(certInfo!=null){
            showMessage(certInfo.getInfo());
        }else{
            showMessage("download certificate failed!");
        }
    }
    /**
     * 
     * @param string
     * @param string0
     */
    private void registerEmployee() {
        String empno = JOptionPane.showInputDialog(this, "Enter empno");
        if(empno==null){
            return;
        }
        String name = JOptionPane.showInputDialog(this, "Enter name");
        if(name==null){
            return;
        }
        String result = PKIAdminTasks.registerEmployee(empno,name);
        showMessage(result);
    }
    /**
     * 
     */
    private void updateCert() {
        //extract
        CertInfo certInfo = Token.getCertificate();
        if(certInfo!=null){
            showMessage(certInfo.getInfo());
            //upload
            //String empno = JOptionPane.showInputDialog(this, "Enter empno");
            String result = PKIAdminTasks.updateCertificate(certInfo.alias, certInfo);
            showMessage(result);
        }else{
            showMessage("extracting certificate failed!"); 
        }
    }
    /**
     * 
     */
    private void generateKey(){
            String text =  Token.setKey(this);
            showMessage(text);
    }
    /**
     * 
     * @param msg
     */
    private void showMessage(String msg){
        textArea.setText(textArea.getText()+msg+"\n--------------------\n");
    }
    
}
