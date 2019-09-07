package mypa;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
/**
 *
 * @author Gurmeet Singh
 */
public class MySecretPacker extends JFrame {
    private boolean visible = true;
    private final JTextArea ta = new JTextArea();
    private final int w=850, h=400;
    public MySecretPacker() {
        super("My Password Packer");
        setLayout(new BorderLayout());
        //
        Color bg = new Color(253,245,230);
        getContentPane().setBackground(bg);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        //
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(30,h));
        p.setBackground(bg);
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ta.setVisible(visible);
                visible = !visible;
            }
        });
        add(p,BorderLayout.WEST);
        //
        p = new JPanel();
        p.setPreferredSize(new Dimension(30,h));
        p.setBackground(bg);
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        add(p,BorderLayout.EAST);
        //
        p = new JPanel();
        p.setPreferredSize(new Dimension(w,30));
        p.setBackground(bg);
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                copyText();
            }
        });
        add(p,BorderLayout.NORTH);
        //
        p = new JPanel();
        p.setPreferredSize(new Dimension(w,30));
        p.setBackground(bg);
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        add(p,BorderLayout.SOUTH);
        //
        ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        ta.setVisible(!visible);
        add(ta, BorderLayout.CENTER);
        setSize(new Dimension(w, h));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            ta.setText(new String(MyCipher.decrypt(encryptedText), "UTF-8"));
        } catch (Exception e) {
            ta.setText("Wrong Pass Phrase!!!");
        }
    }
    @SuppressWarnings("CallToThreadDumpStack")
    private void copyText(){
        try{
            @SuppressWarnings("MalformedRegexp")
            String s = "\""+ta.getText().replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n\"\n+\"")+"\"";
            StringSelection stringSelection = new StringSelection (s);
            Clipboard clipBoard = Toolkit.getDefaultToolkit ().getSystemClipboard ();
            clipBoard.setContents (stringSelection, null);
            System.out.println("plain text data is copied");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //replace encrypted text below with new one
    private static final String encryptedText = /*  put your encypted text here;                                                                                                                                                   */ "+5l+z7xOmxDNsYs/vUGeiOUEmUZrV3P6PT+AQ/pHYYBmFwYCpufowp9CqwvIgblbpJUzbsU1+RLcDxfAAD+uTYIjOQ4WZOio0YY7kXfLJB9qRaNHT6oyVKJGi+toOWGLkH56ZDbMgTD/5HdvTeT6wZE/lp0h9QjHpF1ruoG9S+QavkKRKNtK5/30xzND+ZjTrdiETdKcTIQoMpAWKHk9JOElgAoT5txXQeUVOopeIR9vwhVdlnFfAH5LIYCpzcDTPhZ34BeNaKZs/U/G7ZHgxb6sGtrsGMiMG9JGdHiVccHBHVu++pHPXY8odH1kIIW2RqhojEM7ljscasxIiaD6jqGzMom8lcii+RK0iaze5udP+mxjB7Ve8PmjTRKYEGStCG3DTJGwi1W1JK8Mny1Ee+kVWI82IbX/DYJrX7xmuLt5QjAozisKikEkn2CMElS3KT+q1waVSfgrqZRKwuoFg7Y7gzcLvR/JSI+nW5J37hpZd1w03ry0FKQ1Ge+LX0kgvJ3jtP6pAz03Y3MuZ4GX11iDCjEwKunJ2qUsC8a4QYoi9iVmB2PnebHbm0r+Ua5uscvPBp+pVcK3h42Q9NckOtNe3p93GyoT30OOEy0dB1A7MnUul8zYfGLufVHY0ybZ+hIV4vBE/JI/iLTnRyFVbqTqn/uTidczDK0Z8l/K45JiIqrBR5DVMhdlsbZnaOo9D6bKSMTqt90k7kLDt/6Avw2cPrEaP0aG42xxWnSH94zxSWO7kIUg6jH81iq2LXEQb3Es5PDwNnF26CHw5qd93CdjoIjkjjCwnkBFTC+Ni9GlrgdE82DdEyAQdy6/si9aAf9IBYTtyrd7sm2pPaoxgw9/xysbD5Zp/VnYQqnC9UmVCw/uxXiv8wNJTzd1mPG9Y50zvJGbpPsUErOle8Ky2HoGNmWK96fCeykN0XAHv8+cFFwXv2JmpVdtYZF4c9kOnA6Jr2nV3LCyVYbwcpQZWfLWmnY59Hgb5Uwt1VEgCn7C5oJKDNLBsDbce3HbUHm35mtZj8LO4OtpEeRdPZScDT4z7+3Hx17JWSgoVsiUpPpw5zGrcxgbmB3iTTpIBZ8iQZu/ac3IWp7QCzYSGxbWlqsTU3Tb6t7/OdoN1f+A+vyBhegBJIRD8sTIVA5ZFzM/cf+uxdIgSxYt0qEPsTUB/3ofgj0E8Vlp1+rNJj0Y/AsacDZibQ0ArOndxCJ9K8KX";
}