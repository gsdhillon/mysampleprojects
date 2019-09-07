package signerapp;

import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class MySigner {
    static String getSign(String unitCodeEmpNo, String certSno, String data) throws Exception {
        JTextArea ta = new JTextArea(
                 "You are going to sign following data (enclosed in dash lines):"
                + "\n-----------------------"
                + "\n"+data
                + "\n-----------------------"
                +"\nUnitCode-Empno:" + unitCodeEmpNo + ", Certificate S.No:" + certSno  
                + "\nSign this data?");
        ta.setPreferredSize(new Dimension(800, 400));
        ta.setLineWrap(true);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SignerApp.mainFrame.toFront();
                SignerApp.mainFrame.repaint();
            }
        });
        
        int result = JOptionPane.showConfirmDialog(SignerApp.mainFrame, ta);
        if (result != JOptionPane.OK_OPTION) {
            throw new Exception("Rejected to sign!");
        }
        byte[] sign =
                ("12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "1234567890123456").getBytes("UTF-8");
        String signBase64 = Base64.base64Encode(sign);
        return signBase64;
    }
}