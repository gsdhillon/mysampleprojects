package MyOnlineExam_Package;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Class MyOnScreenPasswordDialog
 * Created on Aug 6, 2013
 * @version 1.0.0
 * @author
 */
public class MyOnScreenPasswordDialog extends JDialog implements ActionListener, KeyListener{
    private JLabel label = new JLabel("Login to Online Examnination");
    private JPanel keyPanel = new JPanel(new GridLayout(1,1));
    private JPasswordField passField;
    private boolean isCap = true;
    private boolean isSpecial = false;
    public int mode = -1;
    public MyOnScreenPasswordDialog(){
        super(new JFrame(), true);
        
        
        setUndecorated(true);
        setResizable(false);
        setLayout(new GridLayout(1,1));
        //
        JPanel outerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        outerPanel.setPreferredSize(new Dimension(780, 430));
        outerPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN.darker(), 8));
        outerPanel.setBackground(Color.WHITE);
        add(outerPanel);
        //
        outerPanel.add(labelPanel());
        passField = new JPasswordField(12);
        outerPanel.add(buttonPanel());
        keyPanel.setPreferredSize(new Dimension(730, 255));
        keyPanel.add(keyBoardPanel());
        outerPanel.add(keyPanel);
        //
        setSize(780, 430);
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(new WindowAdapter(){ 
            @Override
            public void windowOpened( WindowEvent e){ 
                passField.requestFocusInWindow();
            } 
        }); 
    }
    
    private JPanel labelPanel(){
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(720, 50));
        p.setBackground(Color.WHITE);
        label.setForeground(Color.BLUE);
        label.setFont(new Font("MONOSPACED", Font.BOLD, 24));
        p.add(label);
        return p;
    }
    
    
    private JPanel keyBoardPanel(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        p.setBackground(Color.WHITE);
        p.setPreferredSize(new Dimension(730, 255));
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        JButton[] b;
        if(isSpecial){
            b = getSpecialButtons();
        }else{
            b = getLettersButton();
        }
        int i=0;
        //1234567890   BACK
        JPanel p0 = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        p0.setBackground(Color.WHITE);
        p0.setPreferredSize(new Dimension(716, 50));
        for(;i<10;i++){
            p0.add(b[i]);
        }
        p0.add(getButton("BACK", 110, 50));
        p.add(p0);
        //QWERTYUIOP    SPCL
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        p1.setBackground(Color.WHITE);
        p1.setPreferredSize(new Dimension(716, 50));
        for(;i<20;i++){
            p1.add(b[i]);
        }
        p1.add(getButton("SPCL", 110, 50));
        p.add(p1);
        //ASDFGHJKL   CAP
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        p2.setBackground(Color.WHITE);
        p2.setPreferredSize(new Dimension(716, 50));
        p2.add(getEmptySpace(20, 50));
        for(;i<29;i++){
            p2.add(b[i]);
        }
        p2.add(getEmptySpace(22, 50));
        JButton cb = getButton("CAP", 110, 50);
        cb.setEnabled(!isSpecial);
        p2.add(cb);
        p.add(p2);
        //
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        p3.setBackground(Color.WHITE);
        p3.setPreferredSize(new Dimension(716, 50));
        p3.add(getEmptySpace(50, 50));
        for(;i<b.length;i++){
            p3.add(b[i]);
        }
        if(isSpecial){
            for(int j=0;j<4;j++){
                p3.add(getEmptySpace(50, 50));
            }
        }
        p3.add(getEmptySpace(96, 50));
        p3.add(getButton("ENTER", 125, 50));
        p.add(p3);
        return p;
    }
    
    private JLabel getEmptySpace(int w, int h){
        JLabel l = new JLabel();
        l.setPreferredSize(new Dimension(w, h));
        return l;
    }
    
    private JPanel buttonPanel(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 9, 5));
        p.setBackground(Color.WHITE);
        p.setPreferredSize(new Dimension(660, 50));
        JLabel l = new JLabel("Enter Password: ");
        l.setForeground(Color.BLACK);
        l.setFont(new Font("MONOSPACED", Font.BOLD, 18));
        p.add(l);
        JButton b1 = getButton("OK", 70, 40);
        JButton b2 = getButton("Cancel", 100, 40);
        passField.addKeyListener(this);
        passField.setFont(new Font("MONOSPACED", Font.BOLD, 18));
        passField.setPreferredSize(new Dimension(200, 40));
        p.add(passField);
        p.add(b1);
        p.add(b2);
        return p;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command.equals("Cancel")){
            setVisible(false);
            dispose();
            System.exit(0);
        }else if(command.equals("OK")){
            checkPassword();
        }else if(command.equals("BACK")){
            char[] pass = passField.getPassword();
            if(pass.length > 0){
                passField.setText(new String(pass, 0, pass.length-1));
            }
        }else if(command.equals("CAP")){
            isCap = !isCap;
            keyPanel.removeAll();
            keyPanel.add(keyBoardPanel());
            keyPanel.validate();
            keyPanel.repaint();
        }else if(command.equals("SPCL")){
            isSpecial = !isSpecial;
            keyPanel.removeAll();
            keyPanel.add(keyBoardPanel());
            keyPanel.validate();
            repaint();
        }else if(command.length() == 1){
            passField.setText(new String(passField.getPassword())+command);
            
        }
    }
    
    private JButton getButton(String command, int w, int h){
        JButton b = new JButton(command);
        b.setPreferredSize(new Dimension(w, h));
        b.setActionCommand(command);
        b.setFont(new Font("MONOSPACED", Font.BOLD, 18));
        b.addActionListener(this);
        return b;
    }
            
    private JButton[] getSpecialButtons() {
        JButton[] b = new JButton[32];
        
//        String dd = "1234567890";
//        for(int i=0;i<10;i++){
//            String d = dd.substring(i, i+1);
//            b[i] = getButton(d, 50, 50);
//        }
        
        String ss = "!@#$%^&*()`_+-={}[]|~:;\"'<>?\\,./";
        for(int i=0;i<32;i++){
            String s = ss.substring(i, i+1);
            b[i] = getButton(s, 50, 50);
        }
        
        return b;
    }

    private JButton[] getLettersButton() {
        JButton[] b = new JButton[36];
        
        String dd = "1234567890";
        for(int i=0;i<10;i++){
            String d = dd.substring(i, i+1);
            b[i] = getButton(d, 50, 50);
        }
        
        String ss;
        if(isCap){
            ss = "QWERTYUIOPASDFGHJKLZXCVBNM";
        }else{
            ss= "qwertyuiopasdfghjklzxcvbnm";
        }
        for(int i=0;i<26;i++){
            String s = ss.substring(i, i+1);
            b[10+i] = getButton(s, 50, 50);
        }
        return b;
    }
    /**
     * 
     */
    private void checkPassword() {
        String pass = new String(passField.getPassword());
        switch (pass) {
            case "Ajaybir":
            case "Ishjyot":
                mode = 1;
                setVisible(false);
                break;
            case "Gurmeet":
                mode = 0;
                setVisible(false);
                break;
            default:
                label.setText("Enter Ajaybir or Ishjyot");
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == '\n'){
            checkPassword();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}