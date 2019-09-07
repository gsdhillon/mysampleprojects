package pkiadmin;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import sun.security.x509.X500Name;

/**
 * X500Dialog.java
 * Created on Jul 18, 2008, 10:48:33 AM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class X500Dialog extends JDialog implements ActionListener{
    public String alias = null;
    public X500Name x500Name = null;
    JTextField aliasText = new JTextField();
    JTextField nameText= new JTextField();
    JTextField orgUnitText= new JTextField("BARC");
    JTextField orgText= new JTextField("DAE");
    JTextField cityText= new JTextField("Mumbai");
    JTextField stateText= new JTextField("Maharashtra");
    JTextField countaryInitText= new JTextField("IN");
    JButton okButton = new JButton("OK");
    JButton cancelButton = new JButton("CANCEL");
    /**
     * @param parent 
     */
    public X500Dialog(JFrame parent) {
        super(parent, "Enter Information", true);
        getContentPane().setLayout(new GridLayout(8,2));
        JLabel label = new JLabel("Empno: ",JLabel.RIGHT);
        getContentPane().add(label);
        getContentPane().add(aliasText);
        
        label = new JLabel("Name: ",JLabel.RIGHT);
        getContentPane().add(label);
        getContentPane().add(nameText);
        
        label = new JLabel("Organization Unit: ",JLabel.RIGHT);
        getContentPane().add(label);
        getContentPane().add(orgUnitText);
        
        label = new JLabel("Organization: ",JLabel.RIGHT);
        getContentPane().add(label);
        getContentPane().add(orgText);
        
        label = new JLabel("City: ",JLabel.RIGHT);
        getContentPane().add(label);
        getContentPane().add(cityText);
        
        label = new JLabel("State: ",JLabel.RIGHT);
        getContentPane().add(label);
        getContentPane().add(stateText);
        
        label = new JLabel("Countary Initials: ",JLabel.RIGHT);
        getContentPane().add(label);
        getContentPane().add(countaryInitText);
        
        okButton.addActionListener(this);
        okButton.setActionCommand("OK");
        getContentPane().add(okButton);
        
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand("CANCEL");
        getContentPane().add(cancelButton);
        
        setSize(400, 400);
        setLocation(200, 200);
    }
    /**
     * Handle the action event from buttons.
     * @param e 
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if("OK".equals(e.getActionCommand())){
            alias = aliasText.getText();
            try{
                    x500Name = new X500Name(
                        nameText.getText(),
                        orgUnitText.getText(),
                        orgText.getText(),
                        cityText.getText(),
                        stateText.getText(),
                        countaryInitText.getText()
                    );
            }catch(Exception ex){
                ex.printStackTrace();
                alias = null;
            }
            setVisible(false);
        }else{
            setVisible(false);
        }
    }
}
