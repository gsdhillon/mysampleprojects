package mmi.gui;

import mmi.MyCrypto.PassProtect;
import gui.MyButton;
import gui.MyKeyAdaptor;
import gui.MyLabel;
import gui.MyPanel;
import gui.MyPasswordField;
import gui.MyTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import mmi.data.MyData;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

/**
 * @type     : Java Class
 * @name     : LoginPanel
 * @file     : LoginPanel.java
 * @created  : Feb 3, 2011 5:05:42 PM
 * @version  : 1.0.0
 */
public class LoginPanel extends MyPanel implements ActionListener{
    private MyTextField userIDText;
    private MyPasswordField passField;
    /**
     * @param message
     */
    public LoginPanel(){
        super(new FlowLayout(FlowLayout.CENTER, 800, 70));
        setDefaultBG();

        int rowHeight = 30;
        int width = 700;

        MyLabel l = new MyLabel(
                MyLabel.TYPE_LABEL_HEADING,
                "Man Machine Interface – Traingle Waveform Generator ",
                JLabel.CENTER);
        l.setPreferredSize(new Dimension(width, rowHeight));
        add(l);

        int verticalGap = 20;
        int numRows = 0;

        MyPanel loginPanel = new MyPanel(new BorderLayout(0, verticalGap), "");
        MyPanel p = new MyPanel(new BorderLayout(2, 0), null);
        MyPanel left = new MyPanel(new GridLayout(0, 1, 0, verticalGap), null);
        MyPanel right = new MyPanel(new GridLayout(0, 1, 0,verticalGap), null);

        //userID
        l = new MyLabel(MyLabel.TYPE_LABEL, " UserID:");
        userIDText = new MyTextField(MyData.userID);
        left.add(l);
        right.add(userIDText);
        numRows++;

        //pass
        l = new MyLabel(MyLabel.TYPE_LABEL, " Password:");
        passField = new MyPasswordField("");
        passField.addKeyListener(keyListener);
        left.add(l);
        right.add(passField);
        numRows++;

        p.add(left,BorderLayout.WEST);
        p.add(right,BorderLayout.CENTER);
        loginPanel.add(p, BorderLayout.CENTER);

        MyPanel bp = new MyPanel(new FlowLayout(FlowLayout.CENTER, 20 , 2));
        bp.setPreferredSize(new Dimension(width, rowHeight));

        MyButton b = new MyButton(this, "Login", "Login");
        bp.add(b);

        b = new MyButton(this, "Reset", "Reset");
        bp.add(b);

        loginPanel.add(bp, BorderLayout.SOUTH);
        numRows++;
        
        int height = loginPanel.getPreferredSize().height;//inputPanel.getPreferredSize().height;
        loginPanel.setPreferredSize(new Dimension(400, height));
        add(loginPanel);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Login")){
            callLogin();
        }else if(e.getActionCommand().equals("Reset")){
            userIDText.setText(MyData.userID);
            passField.setText("");
        }
    }


    MyKeyAdaptor keyListener = new MyKeyAdaptor() {
        @Override
        public void keyTyped(KeyEvent e) {
            if(e.getKeyChar()=='\n'){
                callLogin();
           }
        }
    };
    /**
     *
     */
    private void callLogin() {
        String userID = userIDText.getText().trim();
        String password = PassProtect.getDigest(passField.getPassword());
        if(userID.equals(MyData.userID) && password.equals(MyData.password)){
            MyData.mainFrame.addMyPanel(new AllTabsPanel());
        }else{
            MyData.showErrorMessage("Invalid Login Pass");
        }
    }
}