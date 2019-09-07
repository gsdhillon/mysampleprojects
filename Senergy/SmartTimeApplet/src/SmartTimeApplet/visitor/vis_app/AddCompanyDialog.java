/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.vis_app;

import lib.session.MyUtils;
import SmartTimeApplet.visitor.AddCompany.AddCompanyMaster;
import java.awt.BorderLayout;
import java.awt.Point;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author nbpatil
 */
public class AddCompanyDialog extends JDialog {
    private ApplyVisitorForm addNewCepMaster;
    private AddCompanyMaster addCompanyMaster;

    public AddCompanyDialog(ApplyVisitorForm addNewCepMaster) {
        super(new JFrame(), "Add QuickCompany", true);
        try {
            this.setLayout(new BorderLayout());
            this.addNewCepMaster = addNewCepMaster;
            AddCompanyMaster abc = new AddCompanyMaster(true);
            this.add(abc.addCompanyPanel());
//
//           MyPanel panbtn = new MyPanel(new FlowLayout(FlowLayout.CENTER));
//
//          JButton bubtCnc = new MyButton("Cancel", 2) 
//          {
//                @Override
//               public void onClick() {
//                   closeDialog();
//                }
//            };
//            panbtn.add(bubtCnc);
//            this.add(panbtn, BorderLayout.SOUTH);


            Point p = addNewCepMaster.getLocationOnScreen();
            setLocation(p.x + 80, p.y + 30);
            setSize(800, 250);
        } catch (Exception ex) {
            MyUtils.showMessage("SelectCompanyDialog : " + ex);
        }
    }

    public void closeDialog() {
        setVisible(true);
    }
}
