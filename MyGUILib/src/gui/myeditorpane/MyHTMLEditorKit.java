package gui.myeditorpane;

import java.io.Serializable;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

/**
 * MyHTMLEditorKit.java
 */
public class MyHTMLEditorKit extends HTMLEditorKit  implements Serializable{
    /**
     *
     */
    public MyHTMLEditorKit() {
        super();

    }
    /**
     */
    @Override
    public ViewFactory getViewFactory() {
        return new HTMLFactoryX();
    }

    /**
     *
     */
    public static class HTMLFactoryX extends HTMLFactory implements ViewFactory {
        /**
         * 
         * @param elem
         * @return
         */
        @Override
        public View create(Element elem) {
            Object o = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
            if (o instanceof HTML.Tag) {
                HTML.Tag kind = (HTML.Tag) o;
                if (kind == HTML.Tag.IMG) {
                    return new MyHTMLImageView(elem);
                }
            }
            return super.create(elem);
        }
    }
}
