package lib.sampleGUI;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFrame;
import lib.excel.MyExcel;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.MyWindowsAdaptor;
import lib.gui.table.MyTable;
import lib.utils.MyFile;
import lib.utils.MyLog;
/**
 */
public class SampleTMMain extends JFrame{
    private SampleData[] data = SampleData.createDummyData();
    private SampleTM tm;
    public SampleTMMain(){
        try {
            Container c = getContentPane();
            c.setLayout(new BorderLayout());
            //add text field
            MyTextField searchTextField = new MyTextField(20);
            MyPanel p = new MyPanel(new FlowLayout());
            p.add(new MyLabel(MyLabel.TYPE_LABEL, "Search in col2:"));
            p.add(searchTextField);
            p.add(new MyButton("ShowCheckedRows") {
                @Override
                public void onClick() {
                    showCheckedRows();
                }
            });
            p.add(new MyButton("ShowSelectedRows") {
                @Override
                public void onClick() {
                    showSelectedRows();
                }
            });

            p.add(new MyButton("ExportToExcel") {
                @Override
                public void onClick() {
                    exportToExcel();
                }
            });
            c.add(p, BorderLayout.NORTH);
            //add table
            tm = new SampleTM();
            MyTable table = new MyTable(tm);
            table.addSearchField(2, searchTextField, MyTable.SEARCH_TYPE_CONTAINS);
            c.add(table.getGUI(), BorderLayout.CENTER);
            tm.setData(data);
            //
            int width = 800;
            int height = 400;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((screenSize.width-width)/2, (screenSize.height-height)/2);
            setSize(new Dimension(width, height));
            addWindowListener(new MyWindowsAdaptor(){
                @Override
                public void windowClosing(WindowEvent arg0) {
                    System.exit(0);
                }

            });
        } catch (Exception exception) {
            MyLog.showException(exception);
        }
    }
    private void exportToExcel(){
        try {
            String time = MyLog.timeCompact();
            File file = MyFile.chooseFileForSave("Report_"+time+".xls");
            if(file != null){
                MyExcel.exportJTable(tm, file);
            }
        } catch (Exception exception) {
            MyLog.showException(exception);
        }
    }
    private void showCheckedRows(){
        StringBuilder list = new StringBuilder();
        for(int i=0;i<tm.getRowCount();i++){
            if(tm.isRowChecked(i)){
                list.append(tm.getValueAt(i, 1)).append("\n");
            }
        }
        MyLog.showInfoMsg(list.toString());
    }
    private void showSelectedRows(){
        StringBuilder list = new StringBuilder();
        for(int i=0;i<tm.getRowCount();i++){
            if(tm.isRowSelected(i)){
                list.append(tm.getValueAt(i, 1)).append("\n");
            }
        }
        MyLog.showInfoMsg(list.toString());
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        //MyLog.setHome(".");
        MyLog.setDebug(true);
        MyLog.showDebugMessage("Debug is on");
        new SampleTMMain().setVisible(true);
    }
}