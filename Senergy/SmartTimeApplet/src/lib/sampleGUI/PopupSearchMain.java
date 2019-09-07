package lib.sampleGUI;
import java.awt.event.WindowEvent;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import lib.gui.*;
import lib.gui.table.MySearchPopup;
import lib.session.MyUtils;
import lib.utils.MyLog;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePanel;
import net.sourceforge.jdatepicker.JDatePicker;
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
      
            
            //JDate Picker Test
            p.add(new MyLabel(MyLabel.TYPE_LABEL, "PickDate:"));
            JDatePicker datePicker = JDateComponentFactory.createJDatePicker();
//            JDatePanel datePanel = JDateComponentFactory.createJDatePanel();
//            datePicker.setShowYearButtons(true);
//            datePicker.setTextEditable(true);
//            datePicker.addActionListener(new MyActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent ae) {
//                    MyUtils.showMessage(ae.getActionCommand());
//                }
//            });
//            datePicker.setButtonFocusable(true);
            p.add((JComponent) datePicker);
            c.add(p);
            //TODO MANOJ SearchPopup Related Code
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