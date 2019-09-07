package gui;

import java.awt.Color;
import java.awt.Font;


/**
 * @type     : Java Class
 * @name     : MyConstants
 * @file     : MyConstants.java
 * @created  : Aug 18, 2010 9:34:12 AM
 * @version  : 1.2
 */
public class MyConstants {
    //colors
    public static Color FG_TABS = Color.BLACK;//new Color(180,0,0);
    public static Color FG_HEADING = new Color(0,0,128);
    public static Color BG_COLOR_INNER = new Color(214,217,223);
    public static Color BG_COLOR_OUTER = new Color(168,189,208);
    public static Color FG_COLOR_LABEL = Color.BLUE;
    public static Color FG_COLOR_DATA = Color.BLACK;
    //Fonts
    public static Font FONT_TABS = new Font("Arial", Font.PLAIN, 13);
    public static Font FONT_LABEL = new Font(Font.MONOSPACED, Font.PLAIN, 13);
    public static Font FONT_DATA = new Font(Font.MONOSPACED, Font.BOLD, 13);
    public static Font FONT_LABEL_HEADING = new Font("Arial", Font.BOLD, 16);
    public static Font FONT_DATA_SMALL = new Font(Font.SERIF, Font.PLAIN, 12);
    private MyConstants(){}
}