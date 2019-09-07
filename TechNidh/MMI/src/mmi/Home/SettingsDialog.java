package mmi.Home;

import mmi.MyCrypto.PassProtect;
import mmi.data.LogReports;
import mmi.data.MyData;
import gui.MyButton;
import gui.MyConstants;
import gui.MyLabel;
import gui.MyPanel;
import gui.MyPasswordField;
import gui.MyTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * @type     : Java Class
 * @name     : SettingsDialog
 * @file     : SettingsDialog.java
 * @created  : Jun 21, 2011 4:46:29 PM
 * @version  : 1.0.0
 */
public class SettingsDialog extends JDialog implements ActionListener{
    private MyTextField userIDText;
    private MyPasswordField passField;
    private JCheckBox checkPassChange;
    private MyTextField userNameText;
    private MyTextField commPortText;
    public SettingsDialog(){
        super(MyData.mainFrame, "Settings", true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 900, 30));
        getContentPane().setBackground(MyConstants.BG_COLOR_OUTER);
        int rowHeight = 30;
        int verticalGap = 20;
        int numRows = 0;

        MyPanel settingsPanel = new MyPanel(new BorderLayout(0, verticalGap),"");
        MyPanel p = new MyPanel(new BorderLayout(2, 0), null);
        MyPanel left = new MyPanel(new GridLayout(0, 1, 0, verticalGap), null);
        MyPanel center = new MyPanel(new GridLayout(0, 1, 0,verticalGap), null);
        MyPanel right = new MyPanel(new GridLayout(0, 1, 0,verticalGap), null);

        //userID
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL, " UserID:");
        userIDText = new MyTextField(MyData.userID);
        left.add(l);
        center.add(userIDText);
        right.add(new JLabel(""));
        numRows++;

        //pass
        l = new MyLabel(MyLabel.TYPE_LABEL, " Password:");
        passField = new MyPasswordField("");
        left.add(l);
        center.add(passField);
        checkPassChange = new JCheckBox("ChangePass");
        right.add(checkPassChange);
        numRows++;

        //userName
        l = new MyLabel(MyLabel.TYPE_LABEL, " User Name:");
        userNameText = new MyTextField(MyData.userName);
        left.add(l);
        center.add(userNameText);
        right.add(new JLabel(""));
        numRows++;

        //comm Port
        l = new MyLabel(MyLabel.TYPE_LABEL, " Comm Port:");
        commPortText = new MyTextField(MyData.comPort);
        left.add(l);
        center.add(commPortText);
        right.add(new JLabel(""));
        numRows++;


        p.add(left,BorderLayout.WEST);
        p.add(center,BorderLayout.CENTER);
        p.add(right,BorderLayout.EAST);

        settingsPanel.add(p, BorderLayout.CENTER);
        MyPanel bp = new MyPanel(new FlowLayout(FlowLayout.CENTER, 20 , 2));
        bp.setPreferredSize(new Dimension(600, rowHeight));
        
        MyButton b = new MyButton(this, "Save", "Save", "Save");
        bp.add(b);

        b = new MyButton(this, "Cancel", "Cancel", "Cancel");
        bp.add(b);

        settingsPanel.add(bp, BorderLayout.SOUTH);
        numRows++;

        int height = settingsPanel.getPreferredSize().height;
        settingsPanel.setPreferredSize(new Dimension(600, height));
        getContentPane().add(settingsPanel);

        setSize(new Dimension(800, height+100));
        setLocationRelativeTo(MyData.mainFrame);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Save")){
            try{
                saveData();
                setVisible(false);
            }catch(Exception ex){
                LogReports.logError(ex);
                MyData.showErrorMessage(ex);
            }
        }else if(e.getActionCommand().equals("Cancel")){
            setVisible(false);
        }
    }
    /**
     * 
     * @throws Exception
     */
    private void saveData() throws Exception{
        //change the data in MyData
        MyData.userID = userIDText.getText().trim();
        if(checkPassChange.isSelected()){
            MyData.password = PassProtect.getDigest(passField.getPassword());
        }
        MyData.userName = userNameText.getText().trim();
        MyData.comPort = commPortText.getText().trim();
        //save the data to settings file
        MyData.writeSettingsFile();
    }

}
