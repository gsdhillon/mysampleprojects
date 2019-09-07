package MyOnlineExam_Package.TakeExam;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import javax.swing.JPanel;
/**
 * Class MyWhiteBoardPanel
 * Created on Aug 12, 2013
 * @version 1.0.0
 * @author  : Gurmeet Singh, EMail-gsdhillon@gmail.com, Ph-9869117976
 */
public class MyWhiteBoardPanel extends JPanel{
    private Point start = null;
    private Point stop = null;
    private Shape shape = null;
    private int maxShapes = 100;
    private Shape[] allShapes = new Shape[maxShapes];
    private int numShapes = 0;
    public MyWhiteBoardPanel() {
        setBackground(Color.white);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }
    /**
     * 
     */
    public void rub(){
        allShapes = new Shape[maxShapes];
        repaint();
    }
    @Override
    public void paintComponent(Graphics gc) {
        super.paintComponent(gc);
        Graphics2D g2 = (Graphics2D) gc;
        g2.setColor(Color.red);
        if(shape != null){
            BasicStroke stroke = new BasicStroke(1);
            Shape strokedShape = stroke.createStrokedShape(shape);
            g2.draw(strokedShape);
            g2.fill(strokedShape);
        }
        for(Shape s : allShapes){
            if(s != null){
                BasicStroke stroke = new BasicStroke(1);
                Shape strokedShape = stroke.createStrokedShape(s);
                g2.draw(strokedShape);
                g2.fill(strokedShape);
            }
        }
    }
    private class MyMouseListener  extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            start = event.getPoint();
            Path2D path = new Path2D.Double();
            shape = path;
        }
        @Override
        public void mouseDragged(MouseEvent event) {
            stop = event.getPoint();
            Path2D path = (Path2D) shape;
            path.moveTo(start.x, start.y);
            path.lineTo(stop.x, stop.y);
            shape = path;
            start = stop;
            repaint();
        }
        @Override
        public void mouseReleased(MouseEvent event) {
            Path2D path = (Path2D) shape;
            try {
                path.closePath();
            } catch(Exception ingore) {
            }
            shape = null;
            allShapes[numShapes] = path;
            numShapes = (numShapes+1)%maxShapes;
            repaint();
        }
    }
}