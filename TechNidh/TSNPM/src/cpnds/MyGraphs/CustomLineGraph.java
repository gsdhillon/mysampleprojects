package cpnds.MyGraphs;

import cpnds.data.MyData;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.*;
/**
 * @type     : Java Class
 * @name     : CustomLineGraph
 * @file     : CustomLineGraph.java
 * @created  : Aug 25, 2011 08:51:12 PM
 * @version  : 1.2
 */
public class CustomLineGraph extends JPanel{
    public Color lineColor = Color.YELLOW;
    private  Color BG_COLOR = Color.BLACK;
    private  Color FG_COLOR = Color.GREEN.brighter();
    private Color pointColor = Color.GREEN.brighter();
    private Color gridColor = Color.GRAY;
    private int NUM_Y_MARKS = 5;
    private int NUM_X_MARKS = 10;
    private int PAD_H = 25;
    private int PAD_W = 100;
    private int GRAPH_HEIGHT = 400;
    public int TOTAL_HEIGHT = GRAPH_HEIGHT + 3*PAD_H;
    private int GRAPH_WIDTH = 600;
    public int TOTAL_WIDTH = GRAPH_WIDTH + 2*PAD_W;
    private int MIN_X = PAD_W;
    private int MAX_X = TOTAL_WIDTH-PAD_W;
    private int MIN_Y = TOTAL_HEIGHT - 2*PAD_H;
    private int MAX_Y = PAD_H;
    private float yValMax;
    private float yValMin;
    private DataPoint[] graphPoints = null;
    private String xLabel;
    private String yLabel;
    private float gapX = 1;
    private float gapMarkX;
    public boolean showPoint = true;
    public boolean showGrid = false;
    private float fontHeight;
    private Font font;
    private FontRenderContext frc;
    //private String msg = "";
    /**
     *
     * @param points
     * @param xLabel
     * @param yLabel
     * @throws Exception
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public CustomLineGraph(DataPoint[] points, String xLabel, String yLabel) throws Exception{
        if(MyData.showPrintableGraphs.equals("TRUE")){
            lineColor = Color.BLUE;
            BG_COLOR = Color.WHITE;
            FG_COLOR = Color.BLACK;
        }
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        //check data
        if(points == null || points.length == 0){
            this.yLabel = " [DATA LENGTH IS ZERO]";
            graphPoints = null;
            return;
        }else{
            graphPoints = points;
        }
        //X distance between two points
        gapX = (GRAPH_WIDTH - graphPoints.length)/(graphPoints.length-1.0f) + 1;
        //x[1], x[5], x[7] are marked then gapMarkX is 4
        if(graphPoints.length <= NUM_X_MARKS){
            gapMarkX = 1.0f;
        }else{
            gapMarkX = (float)graphPoints.length/NUM_X_MARKS;
        }
        //MAX and MIN Y
        yValMax = - Float.MAX_VALUE;
        yValMin = Float.MAX_VALUE;
        for(int i=0;i<graphPoints.length;i++){
            if(yValMax<graphPoints[i].y) yValMax = graphPoints[i].y;
            if(yValMin>graphPoints[i].y) yValMin = graphPoints[i].y;
        }
        setPreferredSize(new Dimension(TOTAL_WIDTH, TOTAL_HEIGHT));
        setBackground(BG_COLOR);
    }
    /**
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Clear Plot area
        g2D.setPaint(BG_COLOR);
        g2D.fillRect(MIN_X-2, MAX_Y-2, GRAPH_WIDTH+4, GRAPH_HEIGHT+4);

        // Draw Boundaries
        g2D.setPaint(gridColor);
        g2D.drawRect(MIN_X, MAX_Y, GRAPH_WIDTH, GRAPH_HEIGHT);
        
        //Draw x and y labels
        font = g2D.getFont();
        frc = g2D.getFontRenderContext();
        LineMetrics lineMetrics = font.getLineMetrics("G", frc);
        fontHeight = lineMetrics.getAscent();
        float width;
        float x;
        float y;

        // Y labels at the TOP of the graph
        g2D.setPaint(lineColor);
        width = (float)font.getStringBounds(yLabel, frc).getWidth() + 20;
        x = MIN_X + (GRAPH_WIDTH-width)/2.0f;
        y = 10;
        g2D.fillRect((int)x, (int)y, 8, 8);
        g2D.drawString(yLabel, (float)(x+15), (float)(y+fontHeight-3));
        if(graphPoints == null){
            return;
        }

        // X label
        g2D.setPaint(lineColor);
        width = (float)font.getStringBounds(xLabel, frc).getWidth();
        x = MIN_X + (GRAPH_WIDTH - width)/2.0f;
        y = MIN_Y + PAD_H + fontHeight;
        g2D.drawString(xLabel, (float)x, (float)y);

        //X Marks
        g2D.setPaint(FG_COLOR);
        y = (int)(MIN_Y + fontHeight + 5);
        int markNum;
        String mark="-";
        for(float d=0.0f; d < graphPoints.length; d += gapMarkX) {
            markNum = (int)d;
            x = MIN_X + markNum*gapX;
            mark = getMark(graphPoints[markNum].x);
            width = (float)font.getStringBounds(mark, frc).getWidth();
            g2D.drawString(mark, (float)(x - width/2.0), (float)y);
            //mark vertical dash
            g2D.draw(new Line2D.Double(x, MIN_Y, x, MIN_Y+3));
            g2D.draw(new Line2D.Double(x, MAX_Y, x, MAX_Y-3));
        }
        
        //Y Marks
        g2D.setPaint(FG_COLOR);
        float scale;
        if((yValMax-yValMin)==0){
            scale = 0;
        }else{
            scale = (float)GRAPH_HEIGHT/(yValMax-yValMin);
        }
        float yShift = scale*yValMin;
        float yValInc = (float)(yValMax-yValMin)/(NUM_Y_MARKS-1);
        float yVal = yValMin;
        float yMarkGap = (float)GRAPH_HEIGHT/(NUM_Y_MARKS-1);
        float markAdjustment = (float)fontHeight/2-3;
        for(int i = 0; i < NUM_Y_MARKS; i ++) {
            mark = getMark(yVal);
            width = (float)font.getStringBounds(mark, frc).getWidth();
            y = MIN_Y - i*yMarkGap;
            g2D.drawString(mark, (float)(MIN_X-width-4), (float)(y+markAdjustment));
            g2D.drawString(mark, (float)(MAX_X+6), (float)(y+markAdjustment));
            g2D.draw(new Line2D.Double(MIN_X, y, MIN_X-3, y));
            g2D.draw(new Line2D.Double(MAX_X, y, MAX_X+3, y));
            yVal += yValInc;
        }
        //GridLines
        if(showGrid){
            g2D.setPaint(gridColor);
            //X GridLines
            float xGridGap = (float)(gapMarkX*gapX/2);
            x = MIN_X;
            while(x < MAX_X){
                g2D.draw(new Line2D.Double(x, MIN_Y, x, MAX_Y));
                x += xGridGap;
            }
            //Y GridLines
            float yGridGap = yMarkGap / 2;
            y = MIN_Y;
            while(y > MAX_Y) {//y is ulta**
                g2D.draw(new Line2D.Double(MIN_X, y, MAX_X, y));
                y -= yGridGap;
            }
        }
       
        //System.out.println("------4");
        //Plot Graph
        float x1 = MIN_X;
        float x2;
        float y1 = MIN_Y - scale*graphPoints[0].y + yShift;
        float y2;
        for(int j = 0; j < graphPoints.length-1; j++) {
            y2 = MIN_Y - scale*graphPoints[j+1].y + yShift;
            x2 = x1+gapX;
            g2D.setPaint(lineColor);
            g2D.draw(new Line2D.Double(x1, y1, x2, y2));
            x1 = x2;
            y1 = y2;
        }
        //System.out.println("------5");
        //drawPoints
        if(showPoint){
            x = MIN_X;
            for(int k = 0; k < graphPoints.length; k++) {
                y = MIN_Y - scale*graphPoints[k].y + yShift;
                g2D.setPaint(pointColor);
                g2D.fillOval((int)(x-2), (int)(y-2), 4, 4);
                x += gapX;
            }
        }
        //System.out.println("------6");
    }
    /**
     * 
     * @param val
     * @return
     */
    private String getMark(long val){
        if(val <= 99999){
            return String.valueOf(val);
        }else if(val <= 9999999){
            return String.valueOf(val/1000)+"K";
        }else if(val <= 99999999){
            NumberFormat f = new DecimalFormat("#0.0");
            return f.format(val/1000000.0)+"M";
        }else{
            return String.valueOf(val/1000000)+"M";
        }
    }
    /**
     *
     * @param val
     * @return
     */
    private String getMark(float val){
        float absVal = Math.abs(val);
        if(absVal < .00001){
            return Float.toString(val);
        }else if(absVal < 1){
            NumberFormat f = new DecimalFormat("#0.000000");
            return f.format(val);
        }else if(absVal < 10){
            NumberFormat f = new DecimalFormat("#0.00000");
            return f.format(val);
        }else if(absVal < 100){
            NumberFormat f = new DecimalFormat("#0.0000");
            return f.format(val);
        }else if(absVal < 1000){
            NumberFormat f = new DecimalFormat("#0.000");
            return f.format(val);
        }else if(absVal < 10000){
            NumberFormat f = new DecimalFormat("#0.00");
            return f.format(val);
        }else if(absVal < 100000){
            NumberFormat f = new DecimalFormat("#0.0");
            return f.format(val);
        }else{
            NumberFormat f = new DecimalFormat("#0");
            return f.format(val);
        }
    }
}