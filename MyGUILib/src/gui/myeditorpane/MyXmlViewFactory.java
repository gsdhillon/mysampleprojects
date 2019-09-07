package gui.myeditorpane;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
/**
 * Class MyXmlViewFactory
 * Created on Aug 31, 2013
 * @version 1.0.0
 * @author Gurmeet Singh
 */
public class MyXmlViewFactory extends Object implements ViewFactory {
    /**
     * 
     * @param element
     * @return 
     */
    @Override
    public View create(Element element) {
         return new MyXmlView(element);
    }
 
}