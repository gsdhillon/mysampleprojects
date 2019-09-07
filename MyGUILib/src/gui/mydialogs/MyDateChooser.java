package gui.mydialogs;

import gui.mycomponents.MyPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.*;
/**
 */
public class MyDateChooser extends JDialog implements ActionListener{
    private JTextField textField1 = null;
    private JTextField textField2 = null;
//    private JButton selectedButton = null;
    private Calendar toDayDate = null;
    private Calendar date = null;
    private int yyyy;
    private int month;
    private int dayOfMonth;
    private int num_days;
    private int firstDayOfWeek;
    private String[] dayName = {"ERR", "SUN","MON","TUE","WED","THU","FRI","SAT"};
    private String[] monthName = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    private Font font = new Font("SERIF", Font.BOLD, 15);
    private int width = 420;
    private int height  = 340;
    private boolean dateAndTimeBoth = false;
    public void setDateAndTimeBoth(boolean dateAndTimeBoth) {
        this.dateAndTimeBoth = dateAndTimeBoth;
    }
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
        toDayDate = Calendar.getInstance();
        date = Calendar.getInstance();
        setLocation(400, 120);
        setSize(new Dimension(width, height));
        refresh();
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
        toDayDate = Calendar.getInstance();
        date = Calendar.getInstance();
        setLocation(400, 120);
        setSize(new Dimension(width, height));
        refresh();
    }
    /**
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "yearNext":
                date.roll(Calendar.YEAR, true);
                refresh();
                break;
            case "yearPrev":
                date.roll(Calendar.YEAR, false);
                refresh();
                break;
            case "monthNext":
                date.roll(Calendar.MONTH, true);
                refresh();
                break;
            case "monthPrev":
                date.roll(Calendar.MONTH, false);
                refresh();
                break;
            default:
                String dd = e.getActionCommand();
                pickDate(dd);
                break;
        }
    }
    /**
     */
    private void refresh(){
        getContentPane().removeAll();
        yyyy = date.get(Calendar.YEAR);
        month = date.get(Calendar.MONTH);
        dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        //String dd = dayOfMonth<10?"0"+dayOfMonth:""+dayOfMonth;
        num_days = date.getActualMaximum(Calendar.DAY_OF_MONTH);
        //get DayOfWeek on DayOfMonth 1
        date.set(Calendar.DAY_OF_MONTH, 1);
        firstDayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        MyPanel topPanel = new MyPanel(new GridLayout(1,2),null);
        topPanel.setPreferredSize(new Dimension(width, 30));
        Insets buttonInsets = new Insets(2,8,2,8);
        //add year
        MyPanel yearPanel = new MyPanel(new BorderLayout(),null);
        yearPanel.setBorder(BorderFactory.createEtchedBorder());
        JButton b = new JButton("<-");
        b.setMargin(buttonInsets);
        b.setActionCommand("yearPrev");
        b.addActionListener(this);
        yearPanel.add(b, BorderLayout.WEST);
        JLabel yearLabel = new JLabel(""+yyyy,JLabel.CENTER);
        yearLabel.setFont(font);
        yearPanel.add(yearLabel, BorderLayout.CENTER);
        b = new JButton("->");
        b.setMargin(buttonInsets);
        b.setActionCommand("yearNext");
        b.addActionListener(this);
        yearPanel.add(b, BorderLayout.EAST);
        topPanel.add(yearPanel);
        //add month
        MyPanel monthPanel = new MyPanel(new BorderLayout(),null);
        monthPanel.setBorder(BorderFactory.createEtchedBorder());
        b = new JButton("<-");
        b.setMargin(buttonInsets);
        b.setActionCommand("monthPrev");
        b.addActionListener(this);
        monthPanel.add(b, BorderLayout.WEST);
        JLabel monthLabel = new JLabel(monthName[month], JLabel.CENTER);
        monthLabel.setFont(font);
        monthPanel.add(monthLabel, BorderLayout.CENTER);
        b = new JButton("->");
        b.setMargin(buttonInsets);
        b.setActionCommand("monthNext");
        b.addActionListener(this);
        monthPanel.add(b, BorderLayout.EAST);
        topPanel.add(monthPanel);
        add(topPanel, BorderLayout.NORTH);
        //add days
        MyPanel centerPane = new MyPanel(new BorderLayout(),null);
        MyPanel weekPanel = new MyPanel(new GridLayout(0,7,1,1),null);
        weekPanel.setPreferredSize(new Dimension(width, 30));
        //weekPanel.setBackground(Color.YELLOW);
        //weekPanel.setBorder(BorderFactory.createEtchedBorder());
        for(int i=1;i<=7;i++){
            JLabel l = new JLabel(dayName[i], JLabel.CENTER);
            l.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
            l.setFont(font);
            if(i==1||i==7) l.setForeground(Color.RED);
            weekPanel.add(l);
        }
        centerPane.add(weekPanel, BorderLayout.NORTH);
        MyPanel dayPanel = new MyPanel(new GridLayout(0,7,1,1),null);
       // dayPanel.setBorder(BorderFactory.createEtchedBorder());
        for(int i=1;i<firstDayOfWeek;i++){
            dayPanel.add(new Component(){});
        }
        for(int i=1;i<=num_days;i++){
            date.set(Calendar.DAY_OF_MONTH, i);
            String s = i<10?"0"+i:""+i;
            b = new JButton(""+i);
            b.setFont(font);
            b.setMargin(buttonInsets);
            b.setActionCommand(s);
            b.addActionListener(this);
            dayPanel.add(b);
            b.setOpaque(true);
            int weekDay = date.get(Calendar.DAY_OF_WEEK);
            if(date.equals(toDayDate)){
                b.setBackground(Color.YELLOW);
            }else{
                b.setBackground(Color.WHITE);
            }
            //get the week day number of month day i
            // int wd = (start_day+i-2)%7 + 1;
            if(weekDay==Calendar.SUNDAY||weekDay==Calendar.SATURDAY){
                b.setForeground(Color.RED);
            }else{
                b.setForeground(Color.BLACK);
            }
        }
        centerPane.add(dayPanel, BorderLayout.CENTER);
        getContentPane().add(centerPane, BorderLayout.CENTER);
        validate();
    }
//    /**
//     */
//    public boolean leap(int y){
//        if(y%400 == 0) return true;
//        if(y%100 == 0) return false;
//        if(y%4 == 0)   return true;
//        return false;
//    }
//    /**
//     */
//    public int numDays(int year, int month){
//        if(month == 2){
//            if(leap(year)){
//                return 29;
//            }else{
//                return 28;
//            }
//        }else if(month == 4 || month == 6 || month == 9 || month == 11  ){
//            return 30;
//        }else{
//            return 31;
//        }
//    }
    private void pickDate(String dd) {
        String mm = month<10?"0"+month:""+month;
        String ddmmyyyy = dd+"/"+mm+"/"+yyyy;
        if(textField1 != null){
            if(dateAndTimeBoth){
                textField1.setText(ddmmyyyy + " 00:00:00");
            }else{
                textField1.setText(ddmmyyyy);
            }
        }
        if(textField2 != null){
            if(dateAndTimeBoth){
                textField2.setText(ddmmyyyy + " 00:00:00");
            }else{
                textField2.setText(ddmmyyyy);
            }
        }
        setVisible(false);
    }
}