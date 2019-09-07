package gui.myeditorpane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.*;
/**
 * Class MyXmlView
 * Created on Aug 31, 2013
 * @version 1.0.0
 * @author Gurmeet Singh
 */
public class MyXmlView extends PlainView {
    private static final HashMap<Pattern, Color> pcMap = new HashMap<>();
    private static String COMMENT_PATTERN = "(<!--[a-z|A-Z|0-9|.|\\s]*-->)";
    private static String TAG_PATTERN = "(</?[0-9|a-z|A-Z|_]*>)";
    public MyXmlView(Element element) {
        super(element);
        pcMap.put(Pattern.compile(COMMENT_PATTERN), Color.BLUE);
        pcMap.put(Pattern.compile(TAG_PATTERN), Color.RED.darker());
        getDocument().putProperty(PlainDocument.tabSizeAttribute, 4);
    }
    /**
     * 
     * @param g
     * @param x
     * @param y
     * @param p0
     * @param p1
     * @return
     * @throws BadLocationException 
     */
    @Override
    protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException {
        g.setFont(new Font("MONOSPACED",Font.BOLD, 14));
        Document doc = getDocument();
        String text = doc.getText(p0, p1 - p0);
        Segment segment = getLineBuffer();
        SortedMap<Integer, Integer> startMap = new TreeMap<>();
        SortedMap<Integer, Color> colorMap = new TreeMap<>();
        //
        for (Map.Entry<Pattern, Color> entry : pcMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(text);
             while (matcher.find()) {
                startMap.put(matcher.start(1), matcher.end());
                colorMap.put(matcher.start(1), entry.getValue());
            }
        }
        int i = 0;
        //
        for (Map.Entry<Integer, Integer> entry : startMap.entrySet()) {
            int start = entry.getKey();
            int end = entry.getValue();
 
            if (i < start) {
                g.setColor(Color.BLACK);
                doc.getText(p0 + i, start - i, segment);
                x = Utilities.drawTabbedText(segment, x, y, g, this, i);
            }
 
            g.setColor(colorMap.get(start));
            i = end;
            doc.getText(p0 + start, i - start, segment);
            x = Utilities.drawTabbedText(segment, x, y, g, this, start);
        }
        //
        if (i < text.length()) {
            g.setColor(Color.BLACK);
            doc.getText(p0 + i, text.length() - i, segment);
            x = Utilities.drawTabbedText(segment, x, y, g, this, i);
        }
        return x;
    }
    /**
     * 
     * @param g
     * @param x
     * @param y
     * @param p0
     * @param p1
     * @return
     * @throws BadLocationException 
     */
    @Override
    protected int drawSelectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException {
        g.setFont(new Font("MONOSPACED",Font.BOLD, 14));
        Document doc = getDocument();
        String text = doc.getText(p0, p1 - p0);
        Segment segment = getLineBuffer();
        SortedMap<Integer, Integer> startMap = new TreeMap<>();
        SortedMap<Integer, Color> colorMap = new TreeMap<>();
        //
        for (Map.Entry<Pattern, Color> entry : pcMap.entrySet()) {
 
            Matcher matcher = entry.getKey().matcher(text);
 
            while (matcher.find()) {
                startMap.put(matcher.start(1), matcher.end());
                colorMap.put(matcher.start(1), entry.getValue());
            }
        }
        int i = 0;
        //
        for (Map.Entry<Integer, Integer> entry : startMap.entrySet()) {
            int start = entry.getKey();
            int end = entry.getValue();
 
            if (i < start) {
                g.setColor(Color.BLACK);
                doc.getText(p0 + i, start - i, segment);
                x = Utilities.drawTabbedText(segment, x, y, g, this, i);
            }
 
            g.setColor(colorMap.get(start));
            i = end;
            doc.getText(p0 + start, i - start, segment);
            x = Utilities.drawTabbedText(segment, x, y, g, this, start);
        }
        //
        if (i < text.length()) {
            g.setColor(Color.BLACK);
            doc.getText(p0 + i, text.length() - i, segment);
            x = Utilities.drawTabbedText(segment, x, y, g, this, i);
        }
        return x;
    }
}