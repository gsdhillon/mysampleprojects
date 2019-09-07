package cpnds.gui.Home;

import cpnds.data.LogReports;
import cpnds.gui.ADCAcquisition.*;
import cpnds.data.MyData;
import gui.MyPanel;
import gui.MyButton;
import gui.MyLabel;
import gui.MyTable;
import gui.MyTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * @type     : Java Class
 * @name     : SearchExperiment
 * @file     : SearchExperiment.java
 * @created  : Jun 22, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class SearchExperiment extends MyPanel implements ActionListener{
    private String title = "All experiments performed so far";
    private MyLabel titleLabel;
    private ADCAcqParams[] adcAcqParams = null;
    private SearchExperimentTM tm = new SearchExperimentTM();
    private MyTable dataTable = new MyTable(tm);
    private JScrollPane sp = new JScrollPane(dataTable);
    private HomeTab parent;
    private File dataDir = null;
    public boolean dataFound = false;

    private String help =
    "Help: -\n"
    + "1. To search an experiment, type UID and press ENTER.\n"
    + "2. To delete data of an experiment, select from the list and press delete Button.\n"
    + "3. If you have performed a new experiment after opening this page\n"
    + "   then please press refresh button.";

    /**
     * Constructor
     */
    public SearchExperiment(HomeTab parent){
        super(new BorderLayout());
        this.parent = parent;
        int rowHeight = 30;
        int width = 1000;
        
        //Top panel
        MyPanel topPanel = new MyPanel(new GridLayout(2,1));
        topPanel.setPreferredSize(new Dimension(width, rowHeight));

        //add Title
        titleLabel = new MyLabel(MyLabel.TYPE_LABEL_HEADING, title, JLabel.CENTER);
        topPanel.add(titleLabel);

        add(topPanel, BorderLayout.NORTH);

        //add middle panel
        add(sp, BorderLayout.CENTER);

        //buttons panel
        MyPanel bp = MyButton.getButtonPanel();

        //add search UID field
        bp.add(new MyLabel(MyLabel.TYPE_LABEL, "SearchUID:"));
        MyTextField tf = new MyTextField(10);
        bp.add(tf);
        dataTable.addSearchField(0, tf, MyTable.EXACT_MATCH);

        MyButton b = new MyButton(this, "Delete", "Delete Selected", "Delete");
        bp.add(b);

        b = new MyButton(this, "Refresh", "", "Refresh");
        bp.add(b);

        bp.add(new MyButton(this, "Help", "", "Help"));

        b = new MyButton(this, "Back", "", "Back");
        bp.add(b);
        
        add(bp, BorderLayout.SOUTH);
        try{
            readAllPreviousResults();
            dataFound = true;
        }catch(Exception e){
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }
    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Delete")){
            if(dataDir == null || dataTable.getSelectedRowCount()<=0){
                MyData.showErrorMessage("No row selected!");
                return;
            }
            if(MyData.showConfirm("Delete data of selected experiments?")){
                deleteSelected();
            }
        }else if(e.getActionCommand().equals("Refresh")){
            refresh();
        }else if(e.getActionCommand().equals("Help")){
            MyData.showInfoMessage(help);
        }else if(e.getActionCommand().equals("Back")){
            parent.showOptionPane();
        }
    }
    /**
     *
     */
    private void readAllPreviousResults() throws Exception{
        dataDir = new File(MyData.dataPath);
        File[] dateDirs = dataDir.listFiles();
        if(dateDirs==null || dateDirs.length==0){
            throw new Exception("No Experiment Data found");
        }
        //count no. of exp dirs
        int count = 0;
        for(int k=0;k<dateDirs.length;k++){
            File[] timeDirs = dateDirs[k].listFiles();
            //MyData.showInfoMessage(dateDirs[k].getName());
            if(timeDirs==null||timeDirs.length==0){
                continue;
            }else{
                count += timeDirs.length;
            }
        }
        
        //read adcparams files from all exp dirs
        adcAcqParams = new ADCAcqParams[count];
        count = 0;
        for(int k=0;k<dateDirs.length;k++){
            File[] timeDirs = dateDirs[k].listFiles();
            if(timeDirs==null||timeDirs.length==0){
                continue;
            }
            for(int i=0;i<timeDirs.length;i++){
                adcAcqParams[count] = new ADCAcqParams();
                File file = ADCAcquisitionData.getParamFileFromExpDir(timeDirs[i]);
                adcAcqParams[count].read(file);
                count++;
            }
        }
        tm.setData(adcAcqParams);
    }
    /**
     *
     */
    private void deleteSelected() {
        for(int i=0;i<dataTable.getRowCount();i++){
            if(dataTable.isRowSelected(i)){
                deleteData(i);
            }
        }
        refresh();
    }
    /**
     *
     */
    private void refresh(){
        try{
            readAllPreviousResults();
        }catch(Exception e){
            tm.setData(null);
            LogReports.logError(e);
            MyData.showErrorMessage(e);
        }
    }
    /**
     *
     * @param i
     */
    private void deleteData(int i) {
        try{
            File expDir = new File(dataDir, "/"+tm.getValueAt(i,1)+"/"+tm.getValueAt(i,2));
            int uid = Integer.parseInt((String) tm.getValueAt(i,0));
            if(ADCAcquisitionData.newDataAvailable && uid == ADCAcquisitionData.uid){
                if(!MyData.showConfirm("Experiment no. "+uid+" is currently being done.\n"
                        + "Do you want to delete it?")){
                    return;
                }
            }
            ADCAcquisitionData.deleteData(expDir);
            if(ADCAcquisitionData.newDataAvailable && uid == ADCAcquisitionData.uid){
                ADCAcquisitionData.newDataAvailable = false;
                ADCAcquisitionData.numOfPoints = 0;
                ADCAcquisitionData.uid = 0;
            }
            MyData.showInfoMessage("Experiment no. "+uid+" deleted successfully.");
        }catch(Exception ex){
            LogReports.logError(ex);
            LogReports.logError(ex);
            MyData.showErrorMessage(ex);
        }
    }
}