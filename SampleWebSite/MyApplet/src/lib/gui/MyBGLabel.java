package lib.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Class MyBGLabel
 * Created on Sep 7, 2013
 * @version 1.0.0
 * @author
 */
public class MyBGLabel extends JPanel{
    private Color COLOR1 = new Color(64,30,111);
    private Color COLOR2 = new Color(104,55,157);
    private int THICK1 = 3;
    private int THICK2 = 2;
    private Color[] colors = new Color[]{COLOR1, COLOR2};
    private int[] thicks = new int[]{THICK1, THICK2};
    private String title = null;
    private Font font = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    private int titlePosX = 4;
    private int titlePosY = 40;
    private boolean logo = false;
    public MyBGLabel(){
    }
    public MyBGLabel(String title, Font font, int titlePosX, int titlePosY, boolean logo){
        this.title= title;
        this.font = font;
        this.titlePosX = titlePosX;
        this.titlePosY = titlePosY;
        this.logo = true;
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        int i = 0;
        int y = 0;
        do{
           g.setColor(colors[i]);
           g.fillRect(0, y, w, thicks[i]);
           y += thicks[i];
           i = (i+1)%colors.length;
        }while(y < h);
        
        if(logo){
            g.setColor(Color.WHITE);//
            int x1 = 10;
            int y1 = 10;
            int x2 = titlePosX;//(x,y) = (b,b)
            int y2 = h;//(x,y) = (b,b)
            do{
                g.drawOval(x1, y1, x2-2*x1, y2-2*y1);
                x1 += 2;
                y1 += 2;
            }while(x2-2*x1>0 && y2-2*y1>0);
        }
        
        if(title!=null){
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString(title, titlePosX, titlePosY);
        }
    }
}