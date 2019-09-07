package MyOnlineExam_Package.UploadPaper;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * @created  : Aug 10, 2013 6:09:45 PM
 * @version 1.0.0
 * @author   : Gurmeet Singh, EMail-gsdhillon@gmail.com, Ph-9869117976
 */
public class UploadPaperImage extends JPanel implements MouseListener, MouseMotionListener{
    private Image image = null;
    private int width = 200;
    private int height = 400;
    private Color[] optionColor = {Color.GREEN.darker(), Color.BLUE, Color.MAGENTA, Color.CYAN.darker()};
    private int turn = 0;
    private boolean[] areaMarked = {false, false, false, false};
    private String correctOption = "Not Set";
    private String[] allOptions = {"A", "B", "C", "D"};
    private OptionArea[] optionAreas = new OptionArea[4];//0=A, 1=B, 2=C, 3=D
    private final File questionFile;
    private JLabel statusLabel;
    /**
     *
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public UploadPaperImage(File questionFile, JLabel statusLabel){
        this.questionFile = questionFile;
        this.statusLabel = statusLabel;
        setBackground(Color.black);
        loadQuestionImage();
        setPreferredSize(new Dimension(width, height+40));
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    public boolean found(){
        return image != null;
    }
    @Override
    public void paintComponent(Graphics g){
        //super.paintComponent(g);
        if(image != null){
            g.setColor(Color.BLUE);
            g.drawString("Mark all all option areas and set correct answer.", width/2-120, 15);
            g.drawImage(image, 0, 40, width, height, this);
        }
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, width-1, height+40-1);
        for(int i=0;i<optionAreas.length;i++){
            if(areaMarked[i]){
                optionAreas[i].draw(g);
            }
        }
        if(tempRect != null){
            tempRect.draw(g);
        }
    }
//    public void setCorrectOption(String option){
//        for(int i=0;i<allOptions.length;i++){
//            if(option.equals(allOptions[i])){
//                correctOption = option;
//            }
//        }
//    }
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
    private void loadQuestionImage(){
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
        }catch(javax.imageio.IIOException e){
            System.out.println(e.getMessage()+ "    -  " +questionFile.getAbsolutePath());
          //  e.printStackTrace();
        } catch (Exception ex) {
            System.out.println(ex.getMessage()+ " : " +questionFile.getAbsolutePath());
           // ex.printStackTrace();
        }
    }
//    /**
//     * 
//     * @param is
//     * @return 
//     */
//    private Image makeImage(InputStream is){
//        try{
//            return ImageIO.read(is);
//        }catch(Exception e){
//            return null;
//        }
//    }
    private int x = -1;
    private int y = -1;
    private OptionArea tempRect = null;
    @Override
    public void mousePressed(MouseEvent e) {
        if(allAreaMarked()){
            return;
        }
        x = e.getX();
        y = e.getY();
        //tempRect = null;
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(allAreaMarked()){
            return;
        }
        if(x==-1 || y==-1){
            x = -1;
            y = -1;
            return;
        }
        int x1 = e.getX();
        int y1 = e.getY();
        OptionArea rect = new OptionArea(x, y, x1-x, y1-y, optionColor[turn], allOptions[turn]);
        optionAreas[turn] = rect;
        x = -1;
        y = -1;
        tempRect = null;
        areaMarked[turn] = true;
        turn = (turn+1)%4;
        this.repaint();
    }
    
    public boolean saveData(){
        try{
            if(correctOption.length()!=1 || !allAreaMarked()){
                return false;
            }
            File answerDir = new File(questionFile.getParentFile().getParentFile(), "Answers");
            if(!answerDir.exists()){
                answerDir.mkdirs();
            }
            File answerFile = new File(answerDir, questionFile.getName().substring(0, questionFile.getName().indexOf("."))+".txt");
            PrintWriter pw = new PrintWriter(answerFile);
            pw.println("ANSWER="+correctOption);
            for(int i=0;i<optionAreas.length;i++){
                optionAreas[i].writeOptionArea(pw);
            }
            pw.close();
            return true;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Exception:"+e.getMessage());
            return false;
        }
    }
    
    private boolean allAreaMarked(){
        for(int i=0;i<areaMarked.length;i++){
            if(areaMarked[i] == false){
                return false;
            }
        }
        return true;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if(allAreaMarked()){
            return;
        }
        if(x==-1 || y==-1){
            tempRect = null;
            return;
        }
        int x1 = e.getX();
        int y1 = e.getY();
        tempRect = new OptionArea(x, y, x1-x, y1-y, Color.RED, "");
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(!allAreaMarked()){
            return;
        }
        int x1 = e.getX();
        int y1 = e.getY();
        for(int i=0;i<optionAreas.length;i++){
            if(optionAreas[i].isPointInside(x1,y1)){
                correctOption = allOptions[i];
                repaint();
                setStatusString();
                break;
            }
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void setStatusString() {
        String s = "";
        if(correctOption.length()!=1){
            s = "Correct Option Not Set.";
        }
        if(!allAreaMarked()){
            s = s + " All Option Areas Not set.";
        }
        if(s.equals("")){
            statusLabel.setText("Data OK. Correct Option = "+correctOption);
            statusLabel.setForeground(Color.green.darker());
            statusLabel.setFont(new Font("MONOSPACED", Font.BOLD, 20));
        }else{
            statusLabel.setText(s);
            statusLabel.setForeground(Color.red);
            statusLabel.setFont(new Font("MONOSPACED", Font.BOLD, 20));
        }
    }

    public void clearAnswerData() {
        for(int i=0;i<optionAreas.length;i++){
           areaMarked[i] = false;
        }
        correctOption = "Not Set";
        turn = 0;
    }

    private class OptionArea{
        private int x;
        private int y;
        private int w;
        private int h;
        private Color lineColor;
        private String option;
        public OptionArea(int x, int y, int w, int h, Color color, String option){
            setData(x,y,w,h, color, option);
        }
        public void draw(Graphics g){
            g.setColor(lineColor);
            g.drawRect(x, y, w, h);
            g.drawString(option, x+w+1, y+h/2);
            //draw Tick
            if(option.equals(correctOption)){
                g.drawLine(x+2, y-2, x+5, y+10);
                g.drawLine(x+5, y+10, x+15, y-4);
            }
        }
        private void setData(int x, int y, int w, int h, Color color, String option) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.lineColor = color;
            this.option = option;
            //JOptionPane.showMessageDialog(null, x+" "+y+" "+w+" "+h);
        }
        public void writeOptionArea(PrintWriter pw) throws Exception{
            pw.println(option+"="+x+","+(y-40)+","+w+","+h);
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