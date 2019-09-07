package gui.myeditorpane;

import javax.swing.JEditorPane;
/**
 * Class MyEditorPane
 * Created on Aug 31, 2013
 * @version 1.0.0
 * @author Gurmeet Singh
 */
public class MyEditorPane extends JEditorPane {
    public MyEditorPane() {
        setEditable(false);
        this.setEditorKitForContentType("text/xml", new MyXmlEditorKit());
        this.setEditorKitForContentType("text/html", new MyHTMLEditorKit());
    }
}