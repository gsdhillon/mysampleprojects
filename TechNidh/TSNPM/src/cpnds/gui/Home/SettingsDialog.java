package cpnds.gui.Home;

import cpnds.MyCrypto.PassProtect;
import cpnds.data.LogReports;
import cpnds.data.MyData;
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
import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

/**
 * @type     : Java Class
 * @name     : SettingsDialog
 * @file     : SettingsDialog.java
 * @created  : Jun 21, 2011 4:46:29 PM
 * @version  : 1.2
 */
public class SettingsDialog extends JDialog implements ActionListener{
    private MyTextField userIDText;
    private MyPasswordField passField;
    private JCheckBox checkPassChange;
    private MyTextField userNameText;
    private MyTextField dataPathText;
    private MyTextField commPortText;
    private JComboBox comboPrintableGraph;
    private JComboBox comboGenerateDummyData;
    public SettingsDialog(){
        super(MyData.mainFrame, "Settings", true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 900, 50));
        //getContentPane().setBackground(MyConstants.BG_COLOR_OUTER);
        int rowHeight = 30;
        int verticalGap = 30;
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

        //data path
        l = new MyLabel(MyLabel.TYPE_LABEL, " Data Path:");
        dataPathText = new MyTextField(MyData.dataPath);
        dataPathText.setFont(MyConstants.FONT_DATA_SMALL);
        left.add(l);
        center.add(dataPathText);
        MyButton b = new MyButton(this, "Browse", "", "Browse");
        right.add(b);
        numRows++;

        //comm Port
        l = new MyLabel(MyLabel.TYPE_LABEL, " Comm Port:");
        commPortText = new MyTextField(MyData.comPort);
        left.add(l);
        center.add(commPortText);
        right.add(new JLabel(""));
        numRows++;

        //comboPrintableGraph
        l = new MyLabel(MyLabel.TYPE_LABEL, " ShowPrintableGraphs:");
        comboPrintableGraph = new JComboBox(new String[]{"FALSE","TRUE"});
        comboPrintableGraph.setSelectedItem(MyData.showPrintableGraphs);
        left.add(l);
        center.add(comboPrintableGraph);
        right.add(new JLabel("Blue/White"));
        numRows++;

        //comboGenerateDummyData
        l = new MyLabel(MyLabel.TYPE_LABEL, " Dummy Data:");
        comboGenerateDummyData = new JComboBox(new String[]{"FALSE","TRUE"});
        comboGenerateDummyData.setSelectedItem(MyData.generateDummyData);
        comboGenerateDummyData.setEnabled(false);
        left.add(l);
        center.add(comboGenerateDummyData);
        right.add(new JLabel("set it FALSE"));
        numRows++;

        p.add(left,BorderLayout.WEST);
        p.add(center,BorderLayout.CENTER);
        p.add(right,BorderLayout.EAST);

        settingsPanel.add(p, BorderLayout.CENTER);
        MyPanel bp = MyButton.getButtonPanel();
        
        b = new MyButton(this, "Save", "Save", "Save");
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
        }else if(e.getActionCommand().equals("Browse")){
            chooseDir();
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
        MyData.dataPath = dataPathText.getText().trim();
        MyData.comPort = commPortText.getText().trim();
        MyData.showPrintableGraphs = (String)comboPrintableGraph.getSelectedItem();
        MyData.generateDummyData = (String)comboGenerateDummyData.getSelectedItem();
        MyData.GENERATE_DUMMY_DATA = MyData.generateDummyData.endsWith("TRUE");
        //save the data to settings file
        MyData.writeSettingsFile();
    }

        /**
     *
     * @return
     * @throws Exception
     */
    private void chooseDir(){
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select encrypted files folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal != JFileChooser.APPROVE_OPTION){
            return;
        }

        File file = chooser.getSelectedFile();
        dataPathText.setText(file.getAbsolutePath());
    }
}
