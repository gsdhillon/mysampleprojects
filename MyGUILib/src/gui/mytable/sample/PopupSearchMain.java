package gui.mytable.sample;

import gui.MyLog;
import gui.mycomponents.MyButton;
import gui.mycomponents.MyLabel;
import gui.mycomponents.MyPanel;
import gui.mycomponents.MyTextField;
import gui.mydialogs.MyDateChooser;
import gui.mytable.MySearchPopup;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import gui.myeventlisteners.MyMouseListener;
import gui.myeventlisteners.MyWindowsAdaptor;
/**
 */
public class PopupSearchMain extends JFrame{
    private SampleData[] data = SampleData.createDummyData();
    private MyTextField nameTextField;
    private MyTextField idTextField;
    private MyTextField dateTextField;
    public PopupSearchMain(){
        try {
            //
            Container c = getContentPane();
            c.setLayout(new GridLayout(1,1));
            MyPanel p = new MyPanel(new GridLayout(0,2));
            //add search field
            p.add(new MyLabel(MyLabel.TYPE_LABEL, "Name:"));
            nameTextField = new MyTextField(20);
            p.add(nameTextField);
            //add id field
            p.add(new MyLabel(MyLabel.TYPE_LABEL, "PRSN_ID:"));
            idTextField = new MyTextField(20);
            p.add(idTextField);
            //add date field
            p.add(new MyLabel(MyLabel.TYPE_LABEL, "Date:"));
            dateTextField = new MyTextField(20);
            p.add(dateTextField);
            dateTextField.addMouseListener(new MyMouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    MyDateChooser dc = new MyDateChooser(dateTextField);
                    dc.setDateAndTimeBoth(true);
                    dc.setVisible(true);
                }
            });
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
        //MyLog.setHome(".");
        MyLog.setDebug(true);
        MyLog.showDebugMessage("Debug is on");
        new PopupSearchMain().setVisible(true);
    }
}