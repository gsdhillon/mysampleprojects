package SmartTimeApplet.visitor.vis_app;

import lib.session.MyUtils;
import SmartTimeApplet.visitor.AddVisitor.AddVisitorMaster;
import java.awt.BorderLayout;
import java.awt.Point;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author nbpatil
 */
public class AddVisitorDialog extends JDialog{
    private ApplyVisitorForm addNewCepMaster;
    private AddVisitorMaster addVisitorMaster;
    
   public AddVisitorDialog(ApplyVisitorForm addNewCepMaster) {
        super(new JFrame(), "Add Company", true);
        try {
            this.setLayout(new BorderLayout());
            this.addNewCepMaster = addNewCepMaster;
            AddVisitorMaster addvst = new AddVisitorMaster(true, this);
            this.add(addvst.addvisitorPanel());
            Point p = addNewCepMaster.getLocationOnScreen();
            setLocation(p.x + 90, p.y + 30);
            setSize(900, 250);
        } catch (Exception ex) {
            MyUtils.showMessage("SelectCompanyDialog : " + ex);
        }
    }

    public void closeDialog() {
        setVisible(false);
    }
}
