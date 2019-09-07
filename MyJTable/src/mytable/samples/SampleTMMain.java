package mytable.samples;

import gui.mycomponents.MyButton;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import mytable.MyTableWithJXL;
/**
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class SampleTMMain extends JFrame{
    private SampleData[] data = SampleData.createDummyData();
    private MyTableWithJXL table;
    private SampleTM tm;
    @SuppressWarnings("CallToThreadDumpStack")
    public SampleTMMain(){
        try {
            Container c = getContentPane();
            c.setLayout(new BorderLayout());
            //add text field
          //  JTextField searchTextField = new JTextField(20);
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            p.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            MyButton b = new MyButton("Show Checked Rows") {
                @Override
                public void onClick() {
                    showCheckedRows();
                }
            };
            p.add(b);
            //
            b = new MyButton("Selected Rows") {
                @Override
                public void onClick() {
                    showSelectedRows();
                }
            };
            p.add(b);
            b = new MyButton("Export To Excel") {
                @Override
                public void onClick() {
                    table.exportToExcel(new File("D:/Gurmeet Singh/MyWork/BARC/MyTableNew/excel_export.xls"), "Header", MyTableWithJXL.PAGE_ORIENTATION_LANDSCAPE);
                }
            };
            p.add(b);
            c.add(p, BorderLayout.SOUTH);
            //add table
            tm = new SampleTM();
            table = new MyTableWithJXL(tm, true);
            //table.addSearchField(2, searchTextField, MyTable.SEARCH_TYPE_CONTAINS);
            c.add(table.getGUI(), BorderLayout.CENTER);
            tm.setData(data);
            //
            int width = 900;
            int height = 600;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((screenSize.width-width)/2, (screenSize.height-height)/2);
            setSize(new Dimension(width, height));
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     private void showCheckedRows(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<tm.getRowCount();i++){
            if(tm.isRowChecked(i)){
                sb.append(tm.getValueAt(i, 1)).append("\n");
            }
        }
        JOptionPane.showMessageDialog(rootPane, sb.toString());
    }
    private void showSelectedRows(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<table.getRowCount();i++){
            if(table.isRowSelected(i)){
                sb.append(table.getValueAt(i, 1)).append("\n");
            }
        }
        JOptionPane.showMessageDialog(rootPane, sb.toString());
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        new SampleTMMain().setVisible(true);
    }
}