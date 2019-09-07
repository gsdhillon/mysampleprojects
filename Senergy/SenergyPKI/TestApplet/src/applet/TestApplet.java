package applet;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import pkilib.MSC;
import pkilib.RSATool;
import pkilib.SignResult;
import pkilib.VerifyResult;
/**
 *
 * @author gurmeet
 */
public class TestApplet extends JApplet implements ActionListener{
    private JTextField empText;
    private JTextArea textArea;
    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    @Override
    public void init() {
        setLayout(new BorderLayout());
        JPanel p = new JPanel(new GridLayout(1, 0));
       
        empText = new JTextField();
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        p.add(empText, BorderLayout.CENTER);
        
        JButton b = new JButton("SignVerifyTest");
        b.setActionCommand("test");
        b.addActionListener(this);
        p.add(b, BorderLayout.EAST);
        add(p, BorderLayout.NORTH);
        add(textArea, BorderLayout.CENTER);
        MSC.setServerURLBase("http://localhost:8084/RPGPKIServer");
    }
    /**
     * 
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("test")){
            testSignVerifyData();
        }
    }
    
    /**
     * 
     */
    private void testSignVerifyData(){
        byte[] data = {12,12,12,12,12,12,12,12};
        SignResult signResult = RSATool.sign(data, empText.getText());
        VerifyResult verifyResult = RSATool.verify(
              data, signResult.sign, signResult.certSNO, signResult.signDate);
        textArea.setText(textArea.getText()+
                         "\n---------sign result----------------\n" +
                         "StatusCode   ="+signResult.statusCode+"\n" +
                         "CertSNo      ="+signResult.certSNO+"\n" +
                         "Description  ="+signResult.description+"\n" +
                         "---------verify result----------------\n" +
                         "StatusCode   ="+verifyResult.statusCode+"\n" +
                         "description  ="+verifyResult.description);
    }
}
