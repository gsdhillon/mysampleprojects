package SmartTimeApplet;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import lib.gui.MyButton;
import lib.gui.MyKeyListener;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.utils.Packetizer;

/**
 */
public class LoginApplet extends MyApplet {
    //make single combo box for login types

    private JComboBox loginTypesCombo = null;
    private JTextField username_text = null;
    private MyLabel usernameHint;
    private JPasswordField password_text = null;
    private MyLabel passHint;

    /**
     */
    @Override
    public void init() {
        try {
            super.init();
            setLayout(new BorderLayout());
            MyPanel formPanel = createLoginForm();
            add(formPanel, BorderLayout.CENTER);
        } catch (Exception e) {
            MyUtils.showException("applet init", e);
        }
    }

    private void doLogin() {
        if (authenticate()) {
            try {
                URL url = MyUtils.getRelativeURL("index.jsp");
                getAppletContext().showDocument(url, "_top");
            } catch (Exception e) {
                MyUtils.showException("closing applet", e);
            }
        }
    }

    /**
     */
    private boolean authenticate() {
        try {
            int userType = loginTypesCombo.getSelectedIndex();
            String loginID = username_text.getText();
            String password = new String(password_text.getPassword());
            Packetizer p = new Packetizer();
            p.addString(userType + "");
            p.addString(loginID);
            p.addString(password);
            //HTTP message exchange with servlet
            MyHTTP myHTTP = MyUtils.createServletConnection("LoginServlet");
            myHTTP.openOS();
            myHTTP.println("userlogin");
            myHTTP.println(p.getPacket());
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            
            if (response == null || !response.startsWith("OK")) {
                MyUtils.showMessage("Login Failed:\n" + response);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            MyUtils.showException("Exception: ", e);
            return false;
        }
    }

    /**
     */
    private MyPanel createLoginForm() {
        MyPanel formPanel = new MyPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));
        MyPanel loginPane = new MyPanel(new BorderLayout());
        loginPane.setBorder(BorderFactory.createTitledBorder("Login"));
        loginPane.setPreferredSize(new Dimension(320, 240));
        MyPanel g1 = new MyPanel(new GridLayout(6, 1, 0, 5));
        MyPanel g2 = new MyPanel(new GridLayout(6, 1, 0, 5));
        //add login types
        MyLabel loginType = new MyLabel(MyLabel.TYPE_LABEL, "Login as: ");
        g1.add(loginType);
        loginTypesCombo = new JComboBox(new String[]{"User",
                    "Office",
                    "Administrator"});
        loginTypesCombo.setActionCommand("loginType");
        g2.add(loginTypesCombo);
        //app username panel to login panel
        MyLabel username = new MyLabel(MyLabel.TYPE_LABEL, "Username: ");
        username_text = new JTextField("7810");
        g1.add(new Component() {
        });
        g1.add(username);
        usernameHint = new MyLabel(MyLabel.TYPE_LABEL, usernameHintString);
        g2.add(usernameHint);
        g2.add(username_text);

        //app password panel to login panel
        MyLabel password = new MyLabel(MyLabel.TYPE_LABEL, "Password: ");
        password_text = new JPasswordField(17);
        password_text.setText("7810");
        password_text.addKeyListener(new MyKeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    doLogin();
                }
            }
        });
        g1.add(new Component() {
        });
        g1.add(password);
        passHint = new MyLabel(MyLabel.TYPE_LABEL, passHintString);
        g2.add(passHint);
        g2.add(password_text);

        //add buttons panel to login panel
        MyPanel p = new MyPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        p.add(new MyButton("Login") {

            @Override
            public void onClick() {
                doLogin();
            }
        });
        g2.add(p);
        loginPane.add(g1, BorderLayout.WEST);
        loginPane.add(g2, BorderLayout.CENTER);
        formPanel.add(loginPane);
        return formPanel;
    }
    //data
    private String usernameHintString = "";// "[1001]";
    private String passHintString = "";// "[1001]";
}
