package applet;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import lib.gui.MyButton;
import lib.gui.MyKeyListener;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
/**
 */
public class LoginPanel extends MyPanel{
    private JComboBox<String> loginTypesCombo = new JComboBox<>(new String[]{
        "Individual",
        "Office",
        "Administrator"
    });
    private JTextField username_text = new JTextField("1001");
    private JPasswordField password_text = new JPasswordField();
    private LoginPage page;
    /**
     */
    public LoginPanel(LoginPage page){
        try{
            this.page = page;
            setBackground(new Color(104,55,157));
            MyPanel loginPanel = createLoginPanel();
            int topMargin = (int)((MainApplet.MAIN_PANEL_HEIGHT-loginPanel.getPreferredSize().getHeight())/2);
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, topMargin));
            add(loginPanel);
        }catch(Exception e){
        }
    }
    /**
     * 
     */
    private void doLogin(){
        int type = loginTypesCombo.getSelectedIndex();
        String loginID = username_text.getText();
        String password = new String(password_text.getPassword());
        page.doLogin(type, loginID, password);
    }
    /**
     * 
     * @return 
     */
    private MyPanel createLoginPanel() {
        MyPanel p = new MyPanel(new BorderLayout(), "Login");

        MyPanel left = new MyPanel(new GridLayout(0,1,0,5));
        MyPanel center = new MyPanel(new GridLayout(0,1,0,5));

        //add username panel to login panel
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL, "Login As: ");
        left.add(l);
        center.add(loginTypesCombo);

        //add username panel to login panel
        l = new MyLabel(MyLabel.TYPE_LABEL, "EmpNo: ");
        left.add(l);
        center.add(username_text);

        //add password panel to login panel
        l = new MyLabel(MyLabel.TYPE_LABEL, "Password: ");
        left.add(l);
        password_text.addKeyListener(new MyKeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar()=='\n'){
                    doLogin();
                }
            }
        });
        center.add(password_text);
        //add buttons panel to login panel
        MyPanel bottum = new MyPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottum.add(new MyButton("Login") {
            @Override
            public void onClick() {
                doLogin();
            }
        });
        p.add(left, BorderLayout.WEST);
        p.add(center, BorderLayout.CENTER);
        p.add(bottum, BorderLayout.SOUTH);
        p.setPreferredSize(new Dimension(320, 260));
        return p;
    }
}