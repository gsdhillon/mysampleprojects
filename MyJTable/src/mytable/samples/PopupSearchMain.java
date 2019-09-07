package mytable.samples;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import mytable.samples.SampleData;
import mytable.searchpopup.MySearchPopup;
/**
 */
public class PopupSearchMain extends JFrame{
    private SampleData[] data = SampleData.createDummyData();
    private JTextField nameTextField;
    private JTextField idTextField;
    private JTextField dateTextField;
    public PopupSearchMain(){
        try {
            //
            Container c = getContentPane();
            c.setLayout(new GridLayout(1,1));
            JPanel p = new JPanel(new GridLayout(0,2));
            //add search field
            p.add(new JLabel("Name:"));
            nameTextField = new JTextField(20);
            p.add(nameTextField);
            //add id field
            p.add(new JLabel("PRSN_ID:"));
            idTextField = new JTextField(20);
            p.add(idTextField);
            //add date field
            p.add(new JLabel("Date:"));
            dateTextField = new JTextField(20);
            p.add(dateTextField);
            c.add(p);
            //TODO SearchPopup Related Code
            PopupSearchTM tm = new PopupSearchTM();
            tm.setData(data);
            new MySearchPopup(nameTextField, tm){
                @Override
                public void useSelected(Object selectedObject) {
                    fillData((SampleData) selectedObject);
                }
            };
            int width = 400;
            int height = 160;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation(100, (screenSize.height-height)/2);
            setSize(new Dimension(width, height));
            addWindowListener(new WindowListener(){
                @Override
                public void windowClosing(WindowEvent arg0) {
                    System.exit(0);
                }
                @Override
                public void windowOpened(WindowEvent e) {
                }
                @Override
                public void windowClosed(WindowEvent e) {
                }
                @Override
                public void windowIconified(WindowEvent e) {
                }
                @Override
                public void windowDeiconified(WindowEvent e) {
                }
                @Override
                public void windowActivated(WindowEvent e) {
                }
                @Override
                public void windowDeactivated(WindowEvent e) {
                }

            });
        } catch (Exception exception) {
            exception.printStackTrace();
            JOptionPane.showConfirmDialog(null, exception.getMessage());
        }
    }
    /**
     *
     * @param selectedData
     */
    public void fillData(SampleData selectedData){
        nameTextField.setText(selectedData.name);
        idTextField.setText(selectedData.prsn_id);
        dateTextField.setText(selectedData.someDate);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        new PopupSearchMain().setVisible(true);
    }
}