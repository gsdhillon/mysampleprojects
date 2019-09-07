package mykeystores;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import security_providers.Settings;
/**
 * 
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class MyX500Dialog extends JDialog {
      //<editor-fold defaultstate="collapsed" desc="X500Name Attributes">
        /**
         * C Country
         * CN Common name
         * DC Domain component
         * E E-mail address
         * EMAIL E-mail address (preferred)
         * EMAILADDRESS E-mail address
         * L Locality
         * O Organization name
         * OU Organizational unit name
         * PC Postal code
         * S State or province
         * SN Family name
         * SP State or province
         * ST State or province (preferred)
         * STREET Street
         * T Title
         **/
    //</editor-fold>
    private X500Name x500Name = null;
    private JTextField empnoText = new JTextField("24196");
    private JTextField titleText= new JTextField("");//Dr
    private JTextField nameText= new JTextField("Gurmeet Singh");
    private JTextField emailText= new JTextField("gsdhill@barc.gov.in");
    private JTextField phoneNoText= new JTextField("022-25592656");
    private JTextField orgUnitText= new JTextField("BARC");
    private JTextField orgText= new JTextField("DAE");
    private JTextField postalAddressText= new JTextField("604-Akashdeep, Anushakti Nagar");
    private JTextField cityText= new JTextField("Mumbai");
    private JTextField stateText= new JTextField("Maharashtra");
    private JTextField pinText= new JTextField("400094");
    private JTextField countaryInitText= new JTextField("IN");
    private JLabel tokenSNoLabel = new JLabel();
    private JLabel statusLabel;
    private final Settings settings;
    /**
     *
     * @param withEmpno
     */
    public MyX500Dialog(Settings settings, X500Name subjectX500Name) {
        super(new JFrame(), "Enter Information", true);
        this.settings = settings;
        createGUI();
        setData(subjectX500Name);
        setVisible(true);
    }
    /**
     *
     * @param withEmpno
     */
    private void createGUI() {
        setLayout(new BorderLayout());
        statusLabel = new JLabel("Enter X.500 subject name", JLabel.CENTER);
        statusLabel.setForeground(Color.blue);
        statusLabel.setFont(new Font("MONOSPACED", Font.BOLD, 14));
        add(statusLabel, BorderLayout.NORTH);
        JPanel dataEntryPanel = new JPanel(new BorderLayout());
        dataEntryPanel.setLayout(new GridLayout(0,2));
        //
        JLabel label = new JLabel("EmpNo[UID]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(empnoText);
        //
        label = new JLabel("Title(Dr/Smt)[T]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(titleText);
        //
        label = new JLabel("Name[CN]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(nameText);
        //
        label = new JLabel("EMail-ID[EmailAddress]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(emailText);
        //
        label = new JLabel("Phone No[TELEPHONE_NUMBER]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(phoneNoText);
        //
        label = new JLabel("Organization Unit[OU]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(orgUnitText);
        //
        label = new JLabel("Organization[O]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(orgText);
        //
        label = new JLabel("Address[POSTAL_ADDRESS]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(postalAddressText);
        //
        label = new JLabel("City[L]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(cityText);
        //
        label = new JLabel("State[ST]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(stateText);
        //
        label = new JLabel("PIN[POSTAL_CODE]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(pinText);
        //
        label = new JLabel("Countary Initials[C]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(countaryInitText);
        //
        tokenSNoLabel.setForeground(Color.red);
        tokenSNoLabel.setFont(new Font("MONOSPACED", Font.BOLD, 14));
        label = new JLabel("Crypto Token SNo[SERIALNUMBER]: ");
        dataEntryPanel.add(label);
        dataEntryPanel.add(tokenSNoLabel);
        //
        JButton b = new JButton("Submit");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setX500Name();
            }
        });
        dataEntryPanel.add(b);
        //
        b = new JButton("Cancel");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        dataEntryPanel.add(b);
        //
        JLabel certType = new JLabel("KeySize = "+settings.keySize+", PubKey = "+settings.pubKeyType+", SigAlg = "+settings.signAlg, JLabel.CENTER);
        certType.setForeground(Color.BLUE);
        certType.setFont(new Font("MONOSPACED", Font.BOLD, 14));
        add(certType, BorderLayout.SOUTH);
        //
        add(dataEntryPanel, BorderLayout.CENTER);
        setSize(700, 500);
        setLocationRelativeTo(null);
    }
    /**
     * 
     * @param subjectX500Name 
     */
    private void setData(X500Name subjectX500Name) {
        try{
            RDN[] rdns = subjectX500Name.getRDNs();
            for(RDN rdn : rdns){
                AttributeTypeAndValue atv = rdn.getFirst();
                if(atv.getType().equals(BCStyle.UID)){
                    empnoText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.T)){
                    titleText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.CN)){
                    nameText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.EmailAddress)){
                    emailText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.TELEPHONE_NUMBER)){
                    phoneNoText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.OU)){
                    orgUnitText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.O)){
                    orgText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.L)){
                    cityText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.ST)){
                    stateText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.POSTAL_CODE)){
                    pinText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.C)){
                    countaryInitText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.POSTAL_ADDRESS)){
                    postalAddressText.setText(atv.getValue().toString());
                }else if(atv.getType().equals(BCStyle.SERIALNUMBER)){
                    tokenSNoLabel.setText(atv.getValue().toString());
                }
            }
        }catch(Exception e){
            
        }
    }
    /**
     * 
     * @return
     * @throws Exception 
     */
    public void cancel(){
        x500Name = null;
        setVisible(false);
    }
    /**
     * 
     * @return
     * @throws Exception 
     */
    public X500Name getX500Name(){
        return x500Name;
    }
    /**
     * 
     */
    private void setX500Name(){
        try{
            String empno = empnoText.getText().trim();
            if(empno.length()==0){
                throw new Exception("emp_no can't be null");
            }
            String title = titleText.getText().trim();//Dr
            String name = nameText.getText().trim();
            if(name.length()==0){
                throw new Exception("name can't be null");
            }
            String emailID = emailText.getText().trim();
            if(emailID.length()==0){
                throw new Exception("emailID can't be null");
            }
            String phoneNo = phoneNoText.getText().trim();
            if(phoneNo.length()==0){
                throw new Exception("phoneNo can't be null");
            }
            String address = postalAddressText.getText().trim();
           
            //
            X500NameBuilder x500NameBuilder =  new X500NameBuilder(BCStyle.INSTANCE)
            .addRDN(BCStyle.UID, empno)
            .addRDN(BCStyle.CN, name)
            .addRDN(BCStyle.EmailAddress, emailID)
            .addRDN(BCStyle.TELEPHONE_NUMBER, phoneNo)
            .addRDN(BCStyle.OU, orgUnitText.getText().trim())
            .addRDN(BCStyle.O, orgText.getText().trim())
            .addRDN(BCStyle.L, cityText.getText().trim())
            .addRDN(BCStyle.ST, stateText.getText().trim())
            .addRDN(BCStyle.POSTAL_CODE, pinText.getText().trim())
            .addRDN(BCStyle.C, countaryInitText.getText().trim());
            if(title.length()>0){
                x500NameBuilder.addRDN(BCStyle.T, title);
            }
            if(address.length()>0){
                x500NameBuilder.addRDN(BCStyle.POSTAL_ADDRESS, address);
            }
            String tokenSNo = tokenSNoLabel.getText().trim();
            if(tokenSNo!=null && !tokenSNo.equals("")){
                x500NameBuilder.addRDN(BCStyle.SERIALNUMBER, tokenSNo);
            }
            x500Name = x500NameBuilder.build();
            setVisible(false);
        }catch(Exception ex){
            x500Name = null;
            statusLabel.setForeground(Color.red);
            statusLabel.setText("Ex: "+ex.getMessage());
        }
    }
}