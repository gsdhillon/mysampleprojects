package lib.gui;



import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
/**
 */
public class MyLabel extends JLabel{
    public static final int ALIGN_LEFT = JLabel.LEFT;
    public static final int ALIGN_RIGHT = JLabel.RIGHT;
    public static final int ALIGN_CENTER = JLabel.CENTER;
    public static final int TYPE_LABEL = 1;
    public static final int TYPE_DATA = 2;
    public static final int TYPE_LABEL_HINDI = 3;
    public static final int TYPE_DATA_HINDI = 4;
    public static final int TYPE_HEADING = 5;
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
     * @param hAlign
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
        String hindiFont = "SHUSHA";
        Font font;
        Color fgColor;
        Color bgColor = MyConstants.BG_COLOR;
        if(type == TYPE_DATA){
            font = MyConstants.FONT_DATA;
            fgColor = MyConstants.FG_COLOR_DATA;
            bgColor = MyConstants.BG_COLOR_DATA;
        }else if(type == TYPE_LABEL_HINDI){
            font = new Font(hindiFont, Font.PLAIN, 20);
            fgColor = MyConstants.FG_COLOR_LABEL;
            bgColor = MyConstants.BG_COLOR;
        }else if(type == TYPE_DATA_HINDI){
            font = new Font(hindiFont, Font.BOLD, 22);
            fgColor = MyConstants.FG_COLOR_DATA;
            bgColor = MyConstants.BG_COLOR_DATA;
        }else if(type == TYPE_HEADING){
            font = MyConstants.FONT_HEADING;
            fgColor = MyConstants.FG_COLOR_LABEL;
            bgColor = MyConstants.BG_COLOR;
        }else{// if(type == TYPE_LABEL)
            font = MyConstants.FONT_LABEL;
            fgColor = MyConstants.FG_COLOR_LABEL;
            bgColor = MyConstants.BG_COLOR;
        }
        setFont(font);
        setForeground(fgColor);
        setBackground(bgColor);
        setOpaque(true);
    }
}