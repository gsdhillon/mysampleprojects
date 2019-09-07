package gui.mydialogs;

import gui.mycomponents.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 */
public abstract class MyLoginDialog extends JDialog{
    private MyTextField userIDText;
    private JPasswordField passField;
    private String userID;
    private boolean loggedIN = false;
    private MyLabel msgLabel;
    private static int trials = 3;
    /**
     * @param message 
     */
    public MyLoginDialog(String msg, String userName){
        super(new JFrame(), "Application Login Dialog", true);
        createGUI(msg, userName);
    }
    /**
     * @param message
     */
    public MyLoginDialog(String msg){
        super(new JFrame(), "Application Login Dialog", true);
        createGUI(msg, "");
    }
    /**
     *
     * @param empno
     * @param pass
     * @return
     */
    public abstract boolean login(String empno, char[] pass);
    /**
     *
     */
    public abstract void callCancel();
    /**
     *
     */
    private void cancel(){
        callCancel();
        setVisible(false);
    }
    /**
     *
     * @return true false
     */
    public boolean isLoggedIn(){
        return loggedIN;
    }
    /**
     *
     * @return UserID who has logged in
     */
    public String getUserID(){
        return userID;
    }

    private void callLogin() {
        userID = userIDText.getText().trim();
        if(!login(userID, passField.getPassword())){
            loggedIN = false;
            trials--;
            if(trials>0){
                msgLabel.setForeground(Color.red);
                msgLabel.setText("Wrong UserID/Pass Trials Left = "+trials);
            }else{
                 setVisible(false);
            }
        }else{
            loggedIN = true;
            setVisible(false);
        }
    }
    /**
     * 
     * @param msg
     * @param userName
     */
    private void createGUI(String msg, String userName) {
        MyPanel loginPanel = new MyPanel(new BorderLayout(0, 10));
        msgLabel = new MyLabel(MyLabel.TYPE_LABEL, msg);
        loginPanel.add(msgLabel, BorderLayout.NORTH);
        //
        MyPanel p = new MyPanel(new BorderLayout(2, 0), null);
        MyPanel left = new MyPanel(new GridLayout(0, 1, 0, 10), null);
        MyPanel right = new MyPanel(new GridLayout(0, 1, 0, 10), null);
        //userID
        MyLabel  l = new MyLabel(MyLabel.TYPE_LABEL, "User Name:");
        userIDText = new MyTextField(userName);
        left.add(l);
        right.add(userIDText);
        //pass
        l = new MyLabel(MyLabel.TYPE_LABEL, "Password:");
        passField = new JPasswordField();
        passField.addKeyListener(new MyKeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar()=='\n'){
                    callLogin();
               }
            }
        });
        left.add(l);
        right.add(passField);
        //
        p.add(left,BorderLayout.WEST);
        p.add(right,BorderLayout.CENTER);
        loginPanel.add(p, BorderLayout.CENTER);

        MyPanel bp = new MyPanel(new GridLayout(1, 3), null);
        MyButton b = new MyButton("Login", MyButton.BUTTON_SMALL, Color.ORANGE){

            @Override
            public void onClick() {
                callLogin();
            }

        };
        bp.add(b);
        bp.add(new JComponent() {});
        b = new MyButton("Cancel", MyButton.BUTTON_SMALL, Color.ORANGE){

            @Override
            public void onClick() {
                cancel();
            }

        };
        bp.add(b);
        loginPanel.add(bp, BorderLayout.SOUTH);
        //
        loginPanel.setPreferredSize(new Dimension(350, loginPanel.getPreferredSize().height+10));
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        add(loginPanel);
        getContentPane().setBackground(MyConstants.BG_COLOR);
        Dimension d = loginPanel.getPreferredSize();
        d.width += 40;
        d.height += 70;
        setSize(d);
        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((s.width-d.width)/2, (s.height-d.height)/2);
        setResizable(false);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                passField.requestFocus();
            }
        });
        setVisible(true);
    }
}