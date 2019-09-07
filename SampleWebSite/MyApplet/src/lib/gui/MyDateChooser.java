package lib.gui;



import lib.gui.MyPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 */
public class MyDateChooser extends JDialog implements ActionListener{
    private JTextField textField1 = null;
    private JTextField textField2 = null;
    private JButton selectedButton = null;
    private Calendar date = null;
    private int yyyy;
    private int month;
    private int day_month;
    private int num_days;
    private int start_day;
    private String dd = null;
    private String[] dayName = {"ERR", "SUN","MON","TUE","WED","THU","FRI","SAT"};
    private String[] monthName = {"ERR","January","February","March","April","May","June","July","August","September","October","November","December"};
    private Font font = new Font("SERIF", Font.BOLD, 15);
    private int width = 420;
    private int height  = 340;
    /**
     *
     * @param textField1
     * @param textField2
     */
    public MyDateChooser(JTextField textField1, JTextField textField2){
        super(new JFrame(), "DateChooser", true);
        setLayout(new BorderLayout());
        this.textField1 = textField1;
        this.textField2 = textField2;
        date = Calendar.getInstance();
        setLocation(400, 120);
        setSize(new Dimension(width, height));
        refresh();
        setVisible(true);
    }
    /**
     *
     * @param textField1
     * @param textField2
     */
    public MyDateChooser(JTextField textField1){
        super(new JFrame(), "DateChooser", true);
        setLayout(new BorderLayout());
        this.textField1 = textField1;
        this.textField2 = null;
        date = Calendar.getInstance();
        setLocation(400, 120);
        setSize(new Dimension(width, height));
        refresh();
        setVisible(true);
    }
    /**
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("yearNext")){
            date.roll(Calendar.YEAR, true);
            refresh();
        }else if(e.getActionCommand().equals("yearPrev")){
            date.roll(Calendar.YEAR, false);
            refresh();
        }else if(e.getActionCommand().equals("monthNext")){
            date.roll(Calendar.MONTH, true);
            refresh();
        }else if(e.getActionCommand().equals("monthPrev")){
            date.roll(Calendar.MONTH, false);
            refresh();
        }else{
            JButton b = (JButton)e.getSource();
            selectedButton = b;
            dd = e.getActionCommand();

            String mm = month<10?"0"+month:""+month;
            String ddmmyyyy = dd+"/"+mm+"/"+yyyy;
            if(textField1 != null){
                textField1.setText(ddmmyyyy);
            }
            if(textField2 != null){
                textField2.setText(ddmmyyyy);
            }
            setVisible(false);
        } 
    }
    /**
     */
    private void refresh(){
        getContentPane().removeAll();
        yyyy = date.get(Calendar.YEAR);
        month = date.get(Calendar.MONTH)+1;
        day_month = date.get(Calendar.DAY_OF_MONTH);
        dd = day_month<10?"0"+day_month:""+day_month;
        num_days = numDays(yyyy, month);
        /********* start_day of the month *********/
        date.set(Calendar.DAY_OF_MONTH, 1);
        start_day = date.get(Calendar.DAY_OF_WEEK);
        date.set(Calendar.DAY_OF_MONTH, day_month);
        /******************************************/
        MyPanel topPanel = new MyPanel(new GridLayout(1,2),null);
        topPanel.setPreferredSize(new Dimension(width, 30));
        Insets buttonInsets = new Insets(2,8,2,8);
        //add year
        MyPanel yearPanel = new MyPanel(new BorderLayout(),null);
        yearPanel.setBorder(BorderFactory.createEtchedBorder());
        JButton b = new JButton("<--");
        b.setMargin(buttonInsets);
        b.setActionCommand("yearPrev");
        b.addActionListener(this);
        yearPanel.add(b, BorderLayout.WEST);
        JLabel yearLabel = new JLabel(""+yyyy);
        yearLabel.setFont(font);
        yearPanel.add(yearLabel, BorderLayout.CENTER);
        b = new JButton("-->");
        b.setMargin(buttonInsets);
        b.setActionCommand("yearNext");
        b.addActionListener(this);
        yearPanel.add(b, BorderLayout.EAST);
        topPanel.add(yearPanel);
        //add month
        MyPanel monthPanel = new MyPanel(new BorderLayout(),null);
        monthPanel.setBorder(BorderFactory.createEtchedBorder());
        b = new JButton("<--");
        b.setMargin(buttonInsets);
        b.setActionCommand("monthPrev");
        b.addActionListener(this);
        monthPanel.add(b, BorderLayout.WEST);
        JLabel monthLabel = new JLabel(monthName[month]);
        monthLabel.setFont(font);
        monthPanel.add(monthLabel, BorderLayout.CENTER);
        b = new JButton("-->");
        b.setMargin(buttonInsets);
        b.setActionCommand("monthNext");
        b.addActionListener(this);
        monthPanel.add(b, BorderLayout.EAST);
        topPanel.add(monthPanel);
        add(topPanel, BorderLayout.NORTH);
        //add days
        MyPanel centerPane = new MyPanel(new BorderLayout(),null);
        MyPanel weekPanel = new MyPanel(new GridLayout(0,7,0,0),null);
        weekPanel.setPreferredSize(new Dimension(width, 30));
      //  weekPanel.setBackground(Color.YELLOW);
        //weekPanel.setBorder(BorderFactory.createEtchedBorder());
        for(int i=1;i<=7;i++){
            JLabel l = new JLabel(dayName[i]);
            l.setFont(font);
            if(i==1||i==7) l.setForeground(Color.RED);
            weekPanel.add(l);
        }
        centerPane.add(weekPanel, BorderLayout.NORTH);
        MyPanel dayPanel = new MyPanel(new GridLayout(0,7,0,0),null);
        dayPanel.setBorder(BorderFactory.createEtchedBorder());
        for(int i=1;i<start_day;i++){
            dayPanel.add(new Component(){});
        }
        for(int i=1;i<=num_days;i++){
            String s = i<10?"0"+i:""+i;
            b = new JButton(""+i);
            b.setFont(font);
            b.setMargin(buttonInsets);
            b.setActionCommand(s);
            b.addActionListener(this);
            dayPanel.add(b);
            b.setOpaque(true);
            if(i==day_month){
                selectedButton = b;
                selectedButton.setBackground(Color.BLUE);
            }
            /*******************************************/
            //get the week day number of month day i
            int wd = (start_day+i-2)%7 + 1;
            /******************************************/
            //set fore ground of sat and sun
            if(wd==1||wd==7){
                b.setForeground(Color.RED);
            }
        }
        centerPane.add(dayPanel, BorderLayout.CENTER);
        getContentPane().add(centerPane, BorderLayout.CENTER);
        validate();
        SwingUtilities.invokeLater(new Runnable(){
           public void run(){
            	selectedButton.requestFocus();
           }
        });
    }
    /**
     */
    public boolean leap(int y){
        if(y%400 == 0) return true;
        if(y%100 == 0) return false;
        if(y%4 == 0)   return true;
        return false;
    }
    /**
     */
    public int numDays(int year, int month){
        if(month == 2){
            if(leap(year)){
                return 29;
            }else{
                return 28;
            }
        }else if(month == 4 || month == 6 || month == 9 || month == 11  ){
            return 30;
        }else{
            return 31;
        }
    }
}