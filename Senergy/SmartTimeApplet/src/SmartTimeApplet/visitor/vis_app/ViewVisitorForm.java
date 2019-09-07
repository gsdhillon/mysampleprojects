package SmartTimeApplet.visitor.vis_app;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import lib.digsign.doc_list.DocListInterface;
import lib.gui.MyPanel;
import lib.session.MyUtils;
/**
 * ViewVisitorForm.java
 */
public class ViewVisitorForm extends MyPanel implements ActionListener{
    private DocListInterface docList;
    private VisitorForm form;
    private String userType;//APPLICANT / AUTH / OFFICE
    /**
     *
     * @param formID
     * @param userType
     * @param docList
     */
    public ViewVisitorForm(String formID, String userType, DocListInterface docList ) {
        try {
           this.docList = docList;
           this.userType = userType;
            //
            form =  VisitorForm.getForm(formID);
            //
            setLayout(new BorderLayout());
            MyPanel buttonPane = createButtonPane();
            buttonPane.setBorder(BorderFactory.createTitledBorder("Actions"));
            add(buttonPane, BorderLayout.SOUTH);
            add(form.getScrollPane("Visitor Form"), BorderLayout.CENTER);
            //
            form.viewInHTML();
            form.gotoTop();
         } catch (Exception exception) {
            MyUtils.showException("Opening Visitor Form", exception);
        }
    } 
    /**
     * @param ae 
     */
    @Override
    public void actionPerformed(ActionEvent ae){
        switch (ae.getActionCommand()) {
            case "close":
                docList.reLoad(false);
                return;
            case "xml":
                JButton b = (JButton) ae.getSource();
                b.setText("View Normal");
                b.setActionCommand("html");
                form.viewInXML();
                return;
            case "html":
                JButton rb = (JButton) ae.getSource();
                rb.setText("View XML");
                rb.setActionCommand("xml");
                form.viewInHTML();
                return;
        }
    }
    /**
     */
    private MyPanel createButtonPane(){
        MyPanel panel = new MyPanel(new GridLayout(1,0));
       
        //View XML/HTML
        JButton b = new JButton("View XML");
        b.setActionCommand("xml");
        b.addActionListener(this);
        panel.add(b);

        //TODO add more buttons here depending on authType

        //Close
        b = new JButton("Close");
        b.setActionCommand("close");
        b.addActionListener(this);
        panel.add(b);
        return panel;
    }
}