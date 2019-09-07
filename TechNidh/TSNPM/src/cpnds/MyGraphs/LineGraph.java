package cpnds.MyGraphs;

import cpnds.data.MyData;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.*;
/**
 * @type     : Java Class
 * @name     : LineGraph
 * @file     : LineGraph.java
 * @created  : Jan 14, 2011 11:51:12 AM
 * @version  : 1.2
 */
public class LineGraph extends JPanel implements MouseListener{
    public static final int TYPE_PICK_FIRST = 0;
    public static final int TYPE_PICK_MAX = 1;
    public Color lineColor = Color.YELLOW;
    private  Color BG_COLOR = Color.BLACK;
    private  Color FG_COLOR = Color.GREEN.brighter();
    private Color thresholdColor = Color.PINK;
    private Color avgTimePeriodColor = Color.BLUE;
    private Color pointColor = Color.GREEN.brighter();
    private Color gridColor = Color.GRAY;
    private int NUM_Y_MARKS = 17;
    private int NUM_X_MARKS = 40;
    private int PAD_H = 25;
    private int PAD_W = 100;
    private int GRAPH_HEIGHT;
    private int TOTAL_HEIGHT;
    private int MAX_POINTS = 2048;
    private int GRAPH_WIDTH = MAX_POINTS;
    private int TOTAL_WIDTH = GRAPH_WIDTH + 2*PAD_W;
    private int MIN_X = PAD_W;
    private int MAX_X = TOTAL_WIDTH-PAD_W;
    private int MIN_Y;
    private int MAX_Y = PAD_H;
    private int scrollPaneW = MyData.width;
    private int scrollPaneH = 500;
    private float yValMax;
    private float yValMin;
    private DataPoint[] graphPoints = null;
    private String xLabel;
    private String yLabel;
    private float gapX = 1;
    private float gapMarkX;
    private boolean showPoint = false;
    private boolean showGrid = false;
    private float fontHeight;
    private Font font;
    private FontRenderContext frc;
    private YLabelPopup yLabelPopup = null;
    private YMarkPopup yMarkPopup = null;
    private boolean showYLabelPopup = false;
    private float yThVal = 0.0f;
    private boolean thresholdSet = false;
    //for avgTimePeriod
    private float avgTimePeriod = 0.0f;
    private boolean avgTimePeriodSet = false;
    public boolean showUID = false;
    public boolean showSNo = false;
    //private String msg = "";
    /**
     *
     * @param points
     * @param xLabel
     * @param yLabel
     * @throws Exception
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public LineGraph(DataPoint[] points, String xLabel, String yLabel)
                                                            throws Exception{
        initGraph(points, TYPE_PICK_FIRST, xLabel, yLabel, 1);
    }
    /**
     *
     * @param points
     * @param type TYPE_PICK_REGULAR=0, TYPE_PICK_MAX=1
     * @param xLabel
     * @param yLabel
     * @throws Exception
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public LineGraph(DataPoint[] points, int type, String xLabel, String yLabel)
                                                                throws Exception{
        initGraph(points, type, xLabel, yLabel, 1);
    }
    /**
     *
     * @param points
     * @param type TYPE_PICK_REGULAR=0, TYPE_PICK_MAX=1
     * @param xLabel
     * @param yLabel
     * @param yZoom from 1-8
     * @throws Exception
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public LineGraph(DataPoint[] points, int type, String xLabel, String yLabel,
                                                    int yZoom) throws Exception{
        initGraph(points, type, xLabel, yLabel, yZoom);
    }
    /**
     * 
     * @param points
     * @param type TYPE_PICK_REGULAR=0, TYPE_PICK_MAX=1
     * @param xLabel
     * @param yLabel
     * @param yZoom
     * @throws Exception
     */
    private void initGraph(DataPoint[] points, int type, String xLabel, String yLabel, int yZoom) throws Exception{
        if(MyData.showPrintableGraphs.equals("TRUE")){
            lineColor = Color.BLUE;
            BG_COLOR = Color.WHITE;
            FG_COLOR = Color.BLACK;
        }
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        //set Graph Height
        if(yZoom<1 || yZoom>8){
            throw new Exception("InvalidYZoom 1-8");
        }
        if(yZoom==1){
            showYLabelPopup = true;
        }
        GRAPH_HEIGHT = 480*yZoom;
        TOTAL_HEIGHT = GRAPH_HEIGHT + 3*PAD_H;
        MIN_Y = TOTAL_HEIGHT - 2*PAD_H;
        //check data
        if(points == null || points.length == 0){
            this.yLabel = " [DATA LENGTH IS ZERO]";
            graphPoints = null;
            //
            MAX_POINTS = scrollPaneW-2*PAD_W-20;
            NUM_X_MARKS = 20;
            GRAPH_WIDTH = MAX_POINTS;
            TOTAL_WIDTH = GRAPH_WIDTH + 2*PAD_W;
            TOTAL_HEIGHT = GRAPH_HEIGHT + 3*PAD_H;
            MAX_X = TOTAL_WIDTH-PAD_W;
            return;
        }

        //
        if((points.length+2*PAD_W)<scrollPaneW){
            MAX_POINTS = scrollPaneW-2*PAD_W-20;
            NUM_X_MARKS = 20;
        }
        //
        GRAPH_WIDTH = MAX_POINTS;
        TOTAL_WIDTH = GRAPH_WIDTH + 2*PAD_W;
        TOTAL_HEIGHT = GRAPH_HEIGHT + 3*PAD_H;
        MAX_X = TOTAL_WIDTH-PAD_W;

        //pick only MAX_POINTS from allPoints if > MAX_POINTS
        if(points.length <= MAX_POINTS){
            graphPoints = points;
        }else{
            //msg = "Picking "+MAX_POINTS+" points from "+points.length+" points: -\n";
            graphPoints = new DataPoint[MAX_POINTS];
            double shrinkRation = ((double)points.length)/MAX_POINTS;
            if(type == TYPE_PICK_MAX){
                //pick MAX_POINT from each interval
                double start = 0.0;
                double end = shrinkRation;
                for(int i=0;i<MAX_POINTS;i++){
                    graphPoints[i] = pickMax(points, (int)start, (int)end);
                    start = end;
                    end += shrinkRation;
                }
            }else{
                //pick FIRST_POINT from each interval
                double start = 0.0;
                double end = shrinkRation;
                for(int i=0;i<MAX_POINTS;i++){
                    graphPoints[i] = pickFirst(points, (int)start, (int)end);
                    start = end;
                    end += shrinkRation;
                }
            }
            //MyData.showInfoMessageTA(msg);
        }
        //X distance between two points
        if(graphPoints.length==1){
            gapX = 10;//>5
        }else if(graphPoints.length < GRAPH_WIDTH){
            gapX = (GRAPH_WIDTH - graphPoints.length)/(graphPoints.length-1.0f) + 1;
        }
        //show graphPoints and Grid lines if points are
        //at sufficient distace
        if(gapX > 5){
            showPoint = true;
            //showGrid = true;
        }
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
        //***SMOOTH YMARKS
        yValMax +=.000000001;

        //System.out.println("MinY="+format.format(yValMin)+", MaxY="+format.format(yValMax));
        setPreferredSize(new Dimension(TOTAL_WIDTH, TOTAL_HEIGHT));
        setBackground(BG_COLOR);
        yLabelPopup = new YLabelPopup(PAD_W, GRAPH_HEIGHT);
        yMarkPopup = new YMarkPopup();
        addMouseListener(this);
    }
    /**
     *
     * @return
     */
    public JScrollPane getScrollPane(){
        JScrollPane sp = new JScrollPane(this);
        sp.setPreferredSize(new Dimension(scrollPaneW, scrollPaneH));
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //sp.setBorder(BorderFactory.createLineBorder(MyConstants.BG_COLOR_OUTER,2));
        return sp;
    }
    /**
     *
     */
    public void setThresholdVal(float thVal){
        if(thVal < yValMin || thVal > (yValMax-.0000001)){
            return;
        }
        yThVal = thVal;
        thresholdSet = true;
        repaint();
    }
    /**
     *
     */
    public void setAvgTimePeriod(float avgTP){
        if(avgTP < yValMin || avgTP > yValMax){
            return;
        }
        avgTimePeriod = avgTP;
        avgTimePeriodSet = true;
        repaint();
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
        g2D.setPaint(FG_COLOR);
        width = (float)font.getStringBounds(xLabel, frc).getWidth();
        //x = MIN_X + (GRAPH_WIDTH - width)/2.0f;
        //y = MIN_Y + PAD_H + fontHeight;
        x = 5;
        y = MIN_Y + fontHeight+ 5;
        g2D.drawString(xLabel+"->", (float)x, (float)y);

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
        
        //show UID below X marks
        if(showUID){
            // UID label
            g2D.setPaint(FG_COLOR);
            x = 5;
            y = MIN_Y + PAD_H + fontHeight;
            g2D.drawString("UID ->", (float)x, (float)y);
            for(float d=0.0f; d < graphPoints.length; d += gapMarkX) {
                markNum = (int)d;
                x = MIN_X + markNum*gapX;
                mark = getMark(graphPoints[markNum].uid);
                width = (float)font.getStringBounds(mark, frc).getWidth();
                g2D.drawString(mark, (float)(x - width/2.0), (float)y);
            }
        }else if(showSNo){
            // Sample no label
            g2D.setPaint(FG_COLOR);
            x = 5;
            y = MIN_Y + PAD_H + fontHeight;
            g2D.drawString("Serial No.->", (float)x, (float)y);
            for(float d=0.0f; d < graphPoints.length; d += gapMarkX) {
                markNum = (int)d;
                x = MIN_X + markNum*gapX;
                mark = getMark(graphPoints[markNum].sno);
                width = (float)font.getStringBounds(mark, frc).getWidth();
                g2D.drawString(mark, (float)(x - width/2.0), (float)y);
            }
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
        //System.out.println("------3");
        //set Threshold line
        float yTh = 0.0f;
        if(thresholdSet){
            g2D.setPaint(thresholdColor);
            yTh = MIN_Y - scale*yThVal + yShift;
            g2D.draw(new Line2D.Double(MIN_X, yTh, MAX_X, yTh));
        }

        //drawHorizontalLine
        if(avgTimePeriodSet){
            g2D.setPaint(avgTimePeriodColor);
            float yAvgTP = MIN_Y - scale*avgTimePeriod + yShift;
            g2D.draw(new Line2D.Double(MIN_X, yAvgTP, MAX_X, yAvgTP));
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
            if(!thresholdSet){
                g2D.setPaint(lineColor);
                g2D.draw(new Line2D.Double(x1, y1, x2, y2));
            }else{
                if(y1 > yTh){//y1 below Threshold
                    if(y2 > yTh){//both points below
                        g2D.setPaint(thresholdColor);
                        g2D.draw(new Line2D.Double(x1, y1, x2, y2));
                    }else{//y1 below y2 above
                        float xTh = (x2-x1)*(yTh-y1)/(y2-y1) + x1;
                        g2D.setPaint(thresholdColor);
                        g2D.draw(new Line2D.Double(x1, y1, xTh, yTh));
                        g2D.setPaint(lineColor);
                        g2D.draw(new Line2D.Double(xTh, yTh, x2, y2));
                    }
                }else{//y1 < thresholdValue means above Threshold
                    if(y2 < yTh){//Both apoints above
                        g2D.setPaint(lineColor);
                        g2D.draw(new Line2D.Double(x1, y1, x2, y2));
                    }else{//y1 above y2 below
                        float xTh = (x2-x1)*(yTh-y1)/(y2-y1) + x1;
                        g2D.setPaint(lineColor);
                        g2D.draw(new Line2D.Double(x1, y1, xTh, yTh));
                        g2D.setPaint(thresholdColor);
                        g2D.draw(new Line2D.Double(xTh, yTh, x2, y2));
                    }
                }
            }
            x1 = x2;
            y1 = y2;
        }
        //System.out.println("------5");
        //drawPoints
        if(showPoint){
            x = MIN_X;
            for(int k = 0; k < graphPoints.length; k++) {
                y = MIN_Y - scale*graphPoints[k].y + yShift;
                if(!thresholdSet){
                    g2D.setPaint(pointColor);
                    g2D.fillOval((int)(x-2), (int)(y-2), 4, 4);
                }else{
                    if(y<yTh){//
                        g2D.setPaint(pointColor);
                        g2D.fillOval((int)(x-2), (int)(y-2), 4, 4);
                    }else{
                        g2D.setPaint(thresholdColor);
                        g2D.fillOval((int)(x-2), (int)(y-2), 4, 4);
                    }
                }
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
    //rest all stuff is for showing info on graph
    //on moouse events
    public void mouseClicked(MouseEvent e) {
        if(e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2){
            int x = e.getX();
            int y = e.getY();
            //double clicked on yScale area
            if((x<MIN_X || x>MAX_X) && y > MAX_Y && y < MIN_Y){
                sortDataOnY();
            //double clicked on yScale area
            }else if(x>MIN_X && x<MAX_X && y > MAX_Y && y < MIN_Y){
                if(!showGrid){
                    showGrid = true;
                }else{
                    showGrid = false;
                }
            }
            repaint();
         }
    }
    public void mousePressed(MouseEvent e) {
        if(e.getButton()==MouseEvent.BUTTON1){
            int x = e.getX();
            int y = e.getY();
            //System.out.println("x="+x+", y="+y+", MIN_X"+MIN_X);
            if(x>MIN_X && x<MAX_X && y > MAX_Y && y < MIN_Y){
                int x1 = e.getXOnScreen();
                int y1 = e.getYOnScreen();
                yMarkPopup.showYMark(x, y, x1, y1);
           }
        }else if(e.getButton()==MouseEvent.BUTTON3 && showYLabelPopup){
            int x = e.getX();
            int y = e.getY();
            //System.out.println("x="+x+", y="+y+", MIN_X"+MIN_X);
            if(x>MIN_X && x<MAX_X && y > MAX_Y && y < MIN_Y){
                int x1 = e.getXOnScreen();
                yLabelPopup.showYLabel(x1-PAD_W, getLocationOnScreen().y+PAD_H);
           }
        }
    }
    public void mouseReleased(MouseEvent e) {
        if(e.getButton()==MouseEvent.BUTTON1){
            yMarkPopup.hideYMark();
        }else if(e.getButton()==MouseEvent.BUTTON3){
            yLabelPopup.hideYLabel();
        }
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    /**
     *
     */
    private void sortDataOnY() {
        DataPoint temp;
        for(int i=0;i<graphPoints.length-1;i++){
            for(int j=0;j<graphPoints.length-1;j++){
                if(graphPoints[j].y > graphPoints[j+1].y){
                    temp = graphPoints[j];
                    graphPoints[j] = graphPoints[j+1];
                    graphPoints[j+1] = temp;
                }
            }
        }
        repaint();
    }
    /**
     *
     * @return
     */
    public static String getHelp() {
        return "Help for graph:\n"
        + "1. To viw x,y val at any point in the plot area Left Click in the plot area.\n"
        + "2. To view y scale at any point in the plot area Right Click in the plot area.\n"
        + "3. To sort graph points on y values Double Click on YScale Area, left side OR right side.\n"
        + "4. To view grid lines in the plot area Double Click in the plot area.";
    }
    /**
     * 
     * @param points
     * @param start
     * @param end
     * @return
     */
    private DataPoint pickMax(DataPoint[] points, int start, int end) {
        int maxIndex = start;
        for(int i = start+1; i<end; i++){
            if(points[i].y > points[maxIndex].y){
                maxIndex = i;
            }
        }
        //msg += "Piking maximum point from "+start+" to "+(end-1)+" = "+maxIndex+"\n";
        return points[maxIndex];
    }
    /**
     *
     * @param points
     * @param start
     * @param end
     * @return
     */
    private DataPoint pickFirst(DataPoint[] points, int start, int end) {
        //msg += "Piking first point from "+start+" to "+(end-1)+" = "+start+"\n";
        return points[start];
    }
    /**
     *
     */
    private class YLabelPopup extends JPopupMenu{
        private int w;
        private int h;
        public YLabelPopup(int w, int h){
            this.w = w;
            this.h = h+12;
            setPreferredSize(new Dimension(w, h+12));
        }
        public void showYLabel(int x, int y){
            setLocation(x-5, y-7);
            setVisible(true);
        }
        public void hideYLabel(){
            setVisible(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2D = (Graphics2D)g;
            g2D.setFont(font);

            g2D.setPaint(BG_COLOR);
            g2D.fillRect(0, 0, w,h);

            g2D.setPaint(FG_COLOR);
            float yValInc = (float)(yValMax-yValMin)/(NUM_Y_MARKS-1);
            float yVal = yValMin;
            float yMarkGap = (float)GRAPH_HEIGHT/(NUM_Y_MARKS-1);
            float markAdjustment = (float)fontHeight/2-3;
            for(int i = 0; i < NUM_Y_MARKS; i ++) {
                String mark = getMark(yVal);//Float.toString(yVal);
                float width = (float)font.getStringBounds(mark, frc).getWidth();
                float y = h - i*yMarkGap - 5;
                g2D.drawString(mark, (float)(w-width-4), (float)(y+markAdjustment));
                g2D.draw(new Line2D.Double(w-3, y, w, y));
                yVal += yValInc;
            }
        }
    }
    private class YMarkPopup extends JPopupMenu{
        private JLabel yLabel = new JLabel("Y = ");
        private JLabel xLabel = new JLabel("X = ");
        public YMarkPopup(){
            setPreferredSize(new Dimension(120, 40));
            setLayout(new GridLayout(2,1));
            add(xLabel);
            add(yLabel);
        }
        public void showYMark(int x, int y, int xOnScreen, int yOnScreen){
            setLocation(xOnScreen-125, yOnScreen-45);
            float yVal = yValMin+(yValMax-yValMin)*(MIN_Y-y)/(MIN_Y-MAX_Y);
            int index = Math.round((x-MIN_X)/gapX);
            long xVal = graphPoints[index].x;
            xLabel.setText("X = "+getMark(xVal));
            yLabel.setText("Y = "+getMark(yVal));//Float.toString(yVal);
            setVisible(true);
        }
        public void hideYMark(){
            setVisible(false);
        }
    }
}