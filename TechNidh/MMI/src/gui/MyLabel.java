package gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
/**
 * @type     : Java Class
 * @name     : MyLabel
 * @file     : MyLabel.java
 * @created  : Aug 13, 2010 11:36:25 AM
 * @version  : 1.0.0
 */
public class MyLabel extends JLabel{
    public static final int TYPE_LABEL = 1;
    public static final int TYPE_DATA = 2;
    public static final int TYPE_LABEL_HEADING = 3;
    /**
     * 
     */
    public MyLabel(){
        super();
        setFontAndColor(TYPE_LABEL);
    }
    /**
     *
     * @param type
     */
    public MyLabel(int type){
        super();
        setFontAndColor(type);
    }
    /**
     *
     * @param type
     * @param text
     */
    public MyLabel(int type, String text){
        super(text);
        setFontAndColor(type);
    }
    /**
     * 
     * @param type
     * @param text
     * @param hAlign
     */
    public MyLabel(int type, String text, int hAlign){
        super(text, hAlign);
        setFontAndColor(type);
    }
    /**
     * 
     * @param language
     * @param type
     */
    private void setFontAndColor(int type) {
        Font font;
        Color fgColor;
        if(type == TYPE_LABEL){
            font = MyConstants.FONT_LABEL;
            fgColor = MyConstants.FG_COLOR_LABEL;
        }else if(type == TYPE_DATA){
            font = MyConstants.FONT_DATA;
            fgColor = MyConstants.FG_COLOR_DATA;
        }else if(type == TYPE_LABEL_HEADING){
            font = MyConstants.FONT_LABEL_HEADING;
            fgColor = MyConstants.FG_HEADING;
        }else{
            font = MyConstants.FONT_LABEL;
            fgColor = MyConstants.FG_COLOR_LABEL;
        }
        setFont(font);
        setForeground(fgColor);
        //Color bgColor = MyConstants.BG_COLOR_INNER;
        //setBackground(bgColor);
        //setOpaque(true);
    }
}