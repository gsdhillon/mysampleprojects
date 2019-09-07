package SmartTimeApplet.visitor.vis_app;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import lib.digsign.DigSignAuth;
import lib.digsign.doc_list.DocListInterface;
import lib.gui.MyPanel;
import lib.session.MyUtils;
/**
 * SignVisitorForm.java
 */
public class SignVisitorForm extends MyPanel implements ActionListener{
    private DocListInterface docList;
    private VisitorForm form;
    private JRadioButton yesRadioButton;
    private JRadioButton noRadioButton;
    private String newStatusCode;
    private DigSignAuth auth = new DigSignAuth();
    /**
     * 
     * @param formID
     * @param docList 
     */
    public SignVisitorForm(String formID, DocListInterface docList ) {
        try {
           this.docList = docList;
            //
            form =  VisitorForm.getForm(formID);
            auth.getAuthInfo();//who is logged in
            form.addNewAuth(auth);
            //
            setLayout(new BorderLayout());
            MyPanel buttonPane = createButtonPane();
            buttonPane.setBorder(BorderFactory.createTitledBorder("Actions"));
            add(buttonPane, BorderLayout.SOUTH);
            add(form.getScrollPane("Visitor Form"), BorderLayout.CENTER);
            //
            newStatusCode = form.setYesAction();
            form.gotoTop();
         } catch (Exception e) {
            MyUtils.showException("Opening Visitor Form", e);
        }
    } 
    /**
     * @param ae 
     */
    @Override
    public void actionPerformed(ActionEvent ae){
        switch (ae.getActionCommand()) {
            case "sign":
                signAndSubmit();
                return;
            case "cancel":
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
        //Radio Button YES or NO
        if(ae.getActionCommand().equals(form.currentAuthYesAction)){
            newStatusCode = form.setYesAction();
        }else if(ae.getActionCommand().equals(form.currentAuthNoAction)){
            newStatusCode =form.setNoAction();
        }
    }
    /**
     */
    private MyPanel createButtonPane(){
        MyPanel panel = new MyPanel(new GridLayout(1,0));
        //Yes/No Action
        ButtonGroup bg = new ButtonGroup();
        yesRadioButton = new JRadioButton(form.currentAuthYesAction, true);
        yesRadioButton.setActionCommand(form.currentAuthYesAction);
        yesRadioButton.addActionListener(this);
        bg.add(yesRadioButton);
        panel.add(yesRadioButton);
        noRadioButton = new JRadioButton(form.currentAuthNoAction, false);
        noRadioButton.setActionCommand(form.currentAuthNoAction);
        noRadioButton.addActionListener(this);
        bg.add(noRadioButton);
        panel.add(noRadioButton);
        //Sign & Submit
        JButton b = new JButton("Sign & Submit");
        b.setActionCommand("sign");
        b.addActionListener(this);
        panel.add(b);
        //View XML/HTML
        b = new JButton("View XML");
        b.setActionCommand("xml");
        b.addActionListener(this);
        panel.add(b);
        //Cancel
        b = new JButton("Cancel");
        b.setActionCommand("cancel");
        b.addActionListener(this);
        panel.add(b);
        return panel;
    }
    /**
     * 
     */
    @SuppressWarnings("CallToThreadDumpStack")
    private void signAndSubmit() {
        try {
            form.doAuthSign();
            form.saveAfterAuthSign(newStatusCode);
            docList.reLoad(true);
        } catch (Exception e) {
            e.printStackTrace();
            MyUtils.showException("SigningForm: ", e);
            form.refreshForm();
        }
    }
}