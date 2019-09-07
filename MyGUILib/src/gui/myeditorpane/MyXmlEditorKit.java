package gui.myeditorpane;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;
/**
 * Class MyXmlEditorKit
 * Created on Aug 31, 2013
 * @version 1.0.0
 * @author Gurmeet Singh
 */
public class MyXmlEditorKit extends StyledEditorKit {
    private ViewFactory xmlViewFactory;
    /**
     * 
     */
    public MyXmlEditorKit() {
        xmlViewFactory = new MyXmlViewFactory();
    }
    /**
     * 
     * @return 
     */
    @Override
    public ViewFactory getViewFactory() {
        return xmlViewFactory;
    }
    /**
     * 
     * @return 
     */
    @Override
    public String getContentType() {
        return "text/xml";
    }
}