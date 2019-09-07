package gui.image;


import gui.mycomponents.MyConstants;
import gui.mycomponents.MyPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.InputStream;
import javax.imageio.ImageIO;
/**
 */
public class MyImagePanel extends MyPanel{
    private Image image = null;
    /**
     *
     */
    public MyImagePanel(InputStream is, int w, int h){
        super(new FlowLayout(), null);
        setBackground(MyConstants.BG_COLOR);
        image = makeImage(is);
        setPreferredSize(new Dimension(w+2, h+2));
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image!=null){
            //check width height ratio if you want
            g.drawImage(image, 1, 1, getWidth()-2, getHeight()-2, this);
        }
    }
    
    public MyPanel getOuterPanel(int w, int h){
        MyPanel p = new MyPanel(new FlowLayout(FlowLayout.CENTER,0,(h-getPreferredSize().height)/2));
        p.setPreferredSize(new Dimension(w,h));
        p.add(this);
        return p;
    }
    /**
     * 
     * @param is
     * @return 
     */
    private Image makeImage(InputStream is){
        try{
            return ImageIO.read(is);
        }catch(Exception e){
            return null;
        }
    }
}