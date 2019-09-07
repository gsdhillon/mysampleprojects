package MyOnlineExam_Package.TakeExam;
import MyOnlineExam_Package.MySettings;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * @created  : Aug 10, 2013 6:09:45 PM
 * @version 1.0.0
 * @author   : Gurmeet Singh, EMail-gsdhillon@gmail.com, Ph-9869117976
 */
public class TakeExamImage extends JPanel {
    private boolean submitted = false;
    private File questionFile;
    private int answeredOption = -1;
    private int correctOption = -1;
    private OptionArea[] optionAreas = new OptionArea[4];//0=A, 1=B, 2=C, 3=D
    private boolean correctOptionFount = false;
    private boolean optionAreasFount = false;
    private Image image = null;
    private int width = 200;
    private int height = 400;
    private File answerFile = null;
    private final JLabel statusLabel;
    /**
     *
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public TakeExamImage(File questionFile, JLabel statusLabel){
        this.questionFile = questionFile;
        this.statusLabel = statusLabel;
        fetchQuestionData();
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, height));
        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }
    public boolean found(){
        return (image != null && correctOptionFount && optionAreasFount);
    }
    public int getMarks(){
        if(answeredOption == -1){
            return 0;
        }else if(answeredOption == correctOption){
            return 3;
        }else{
            return -1;
        }
    }
    public int getTotalMarks(){
        return 3;
    }
    
    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }
    @Override
    public void paintComponent(Graphics g){
        //super.paintComponent(g);
        if(image != null){
            //check width height ratio if you want
            g.drawImage(image, 0, 0, width, height, this);
        }
        g.setColor(Color.BLACK);
        //g.drawRect(0, 0, width-1, height-1);
        for(int i=0;i<optionAreas.length;i++){
            if(i == answeredOption){
                optionAreas[i].draw(g);
            }
        }
    }
    /**
     * 
     * @param f
     * @return
     * @throws Exception
     *
    private Image makeImage(String file){
        try{
            FileInputStream in = new FileInputStream(new File(file));
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
            channel.read(buffer);
            SeekableStream stream = new ByteArraySeekableStream(buffer.array());
            String[] names = ImageCodec.getDecoderNames(stream);
            ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
            RenderedImage im = dec.decodeAsRenderedImage();
            return PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
        }catch(Exception e){
            return null;
        }
    }*/
    /**
     *
     * @param f
     * @return
     * @throws Exception
     */
    @SuppressWarnings("CallToThreadDumpStack")
    private void fetchQuestionData(){
        try{
            boolean imgFound = false;
            String[] questionImageFile = {".bmp",".BMP",".jpg",".JPG", ".png",".PNG",".GIF", ".gif"};
            for(String name :questionImageFile){
                if(questionFile.getName().endsWith(name)){
                    imgFound = true;
                    break;
                }
            }
            if(!imgFound){
                throw new Exception("QUESTION_IMAGE_NOT_FOUND");
            }     
            image = ImageIO.read(questionFile);
            width = image.getWidth(this);
            height = image.getHeight(this);
            //find answer and option area file
            File answerDir = new File(questionFile.getParentFile().getParentFile(), "Answers");
            answerFile = new File(answerDir, questionFile.getName().substring(0, questionFile.getName().indexOf("."))+".txt");
            if(!answerFile.exists()){
               throw new Exception("ANSWER_FILE_NOT_FOUND - "+answerFile.getName()); 
            }
            //get correct answer
            String option = MySettings.readParameter(answerFile.getAbsolutePath(), "ANSWER");
            setCorrectOption(option);
            //
            optionAreas[0] = getOptionAreaRect("A");
            optionAreas[1] = getOptionAreaRect("B");
            optionAreas[2] = getOptionAreaRect("C");
            optionAreas[3] = getOptionAreaRect("D");
            optionAreasFount = true;
            //System.out.println(answerFile.getAbsolutePath() + " - "+found());
        }catch(javax.imageio.IIOException e){
            //e.printStackTrace();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }
    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(submitted){
                return;
            }
            int x = e.getX();
            int y = e.getY();
            for(int i=0;i<optionAreas.length;i++){
                if(optionAreas[i].isPointInside(x,y)){
                    answeredOption = i;
                    statusLabel.setText(getStatusString());
                    repaint();
                    break;
                }
            }
        }
        @Override
        public void mouseMoved(MouseEvent e){
            if(submitted){
                return;
            }
            int x = e.getX();
            int y = e.getY();
            for(int i=0;i<optionAreas.length;i++){
                if(optionAreas[i].isPointInside(x,y)){
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    return;
                }
            }
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    /**
     * 
     */
    public void clearAnswer() {
        if(!submitted){
            answeredOption = -1;
        }
    }
    /**
     * 
     * @param answeredOption
     * @return 
     */
    private OptionArea getOptionAreaRect(String option) throws Exception{
        String[] s  = MySettings.readParameter(answerFile.getAbsolutePath(), option).split(",");
        int x = Integer.parseInt(s[0]);
        int y = Integer.parseInt(s[1]);
        int w = Integer.parseInt(s[2]);
        int h = Integer.parseInt(s[3]);
        return new OptionArea(x, y, w, h);
    }
    String[] optionNames = {"A", "B", "C", "D"};
    public String getStatusString() {
        if(submitted){
            if(answeredOption == -1){
                return "Not answered, Correct option = "+optionNames[correctOption];
            }else{
                return "Answered option = "+optionNames[answeredOption]+", Correct option = "+optionNames[correctOption];
            }
        }else{
            if(answeredOption == -1){
                return "Click on the option to choose";
            }else{
                return "Answered option = "+optionNames[answeredOption];
            }
        }
    }

    private void setCorrectOption(String option) {
        switch (option) {
            case "A":
                correctOption = 0;
                correctOptionFount = true;
                break;
            case "B":
                correctOption = 1;
                correctOptionFount = true;
                break;
            case "C":
                correctOption = 2;
                correctOptionFount = true;
                break;
            case "D":
                correctOption = 3;
                correctOptionFount = true;
                break;
        }
    }
    private class OptionArea{
        private int x;
        private int y;
        private int w;
        private int h;
        private Color lineColor;
        public OptionArea(int x, int y, int w, int h, Color color){
            setData(x,y,w,h, color);
        }
        public OptionArea(int x, int y, int w, int h){
            setData(x,y,w,h, Color.RED);
        }
        private void setData(int x, int y, int w, int h, Color color) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.lineColor = color;
            //JOptionPane.showMessageDialog(null, x+" "+y+" "+w+" "+h);
        }
        public void draw(Graphics g){
            g.setColor(lineColor);
//            draw rect
//            g.drawRect(x, y, w, h);
//            draw Tick
//            g.drawLine(x+2, y+h/2, x+5, y+h/2+10);
//            g.drawLine(x+5, y+h/2+10, x+15, y);
            
            //
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(4.5f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Point2D p1 = new Point2D.Float(x+4, y+h/2-2);
            Point2D p2 = new Point2D.Float(x+10, y+h/2+8);
            Point2D p3 = new Point2D.Float(x+26, y+h/2-8);
            g2.draw(new Line2D.Float(p1, p2));
            g2.setStroke(new BasicStroke(3f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(new Line2D.Float(p2, p3));
        }
        private boolean isPointInside(int x1, int y1) {
            if(x1 > x && x1 < (x+w) && y1 > y && y1 < (y+h)){
                return true;
            }else{
                return false;
            }
        }
    }
}