package gui.mydialogs;


import gui.mycomponents.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPasswordField;

/**
 */

public class MyPasswordDialog extends JDialog{
    private JPasswordField passField;
    private String password = null;
    private MyLabel msgLabel;
    /**
     * @param message 
     */
    public MyPasswordDialog(String msg){
        super(new JFrame(), "Password Dialog", true);
        msgLabel = new MyLabel(MyLabel.TYPE_LABEL, msg);
        int width = 400;
        int height = 200;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width-width)/2, (screenSize.height-height)/2);
        setSize(new Dimension(width, height));
        createGUI();
        setVisible(true);
    }
    /**
     * 
     * @return 
     */
    public String getPassword(){
        return password;
    }
    /**
     * 
     */
    private void setPassword() {
       char[] passChars  = passField.getPassword();
       if(passChars.length<4 || passChars.length>16){
           msgLabel.setText("PASSWORD Length between 4 to 16");
       }else{
           password = new String(passChars);
           setVisible(false);
       }
    }
    
    private void createGUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        getContentPane().setBackground(MyConstants.BG_COLOR);

        MyPanel loginPanel = new MyPanel(new BorderLayout(0, 10), null);
        loginPanel.setPreferredSize(new Dimension(350, 130));

        MyPanel p = new MyPanel(new BorderLayout(2, 0), null);
        MyPanel left = new MyPanel(new GridLayout(0, 1, 0, 10), null);
        MyPanel right = new MyPanel(new GridLayout(0, 1, 0, 10), null);
        //pass
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL, "  Enter Password:");
        passField = new JPasswordField();
        passField.addKeyListener(new MyKeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar()=='\n'){
                    setPassword();
               }
            }
        });
        left.add(l);
        right.add(passField);

        p.add(left,BorderLayout.WEST);
        p.add(right,BorderLayout.CENTER);
        loginPanel.add(p, BorderLayout.CENTER);

        MyPanel bp = new MyPanel(new GridLayout(1, 2, 50, 10), null);
        bp.setPreferredSize(new Dimension(100, 35));
        MyButton b = new MyButton("OK"){

            @Override
            public void onClick() {
                setPassword();
            }

        };
        bp.add(b);

        b = new MyButton("Cancel"){

            @Override
            public void onClick() {
                cancel();
            }
        };
        bp.add(b);

        loginPanel.add(bp, BorderLayout.SOUTH);

        msgLabel.setPreferredSize(new Dimension(100, 35));
        loginPanel.add(msgLabel, BorderLayout.NORTH);

        getContentPane().add(loginPanel);
    }
    /**
     * 
     */
    private void cancel() {
        setVisible(false);
    }
}
