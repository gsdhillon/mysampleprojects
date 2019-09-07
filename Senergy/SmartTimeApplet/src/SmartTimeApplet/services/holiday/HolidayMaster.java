/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.holiday;

import lib.session.MyApplet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.gui.table.MyTableModel;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponent;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 *
 * @author nbpatil
 */
public class HolidayMaster extends MyApplet {

    private Container contentsPane;
    public MyButton btnAdd;
    public MyButton btnCancel;
    public MyTextField txtHolidayNm;
    public JDatePicker picker;
    public HolidayTableModel tableModel;
    public HTable table;

    @Override
    public void init() {
        super.init();
        try {
            HolidayMaster holidayMaster = new HolidayMaster();
        } catch (Exception ex) {
            Logger.getLogger(HolidayMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HolidayMaster() throws Exception {


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setSize(width / 2, height / 2);

        MyPanel MainPanel = new MyPanel(new BorderLayout());
        MainPanel.setBackground(Color.WHITE);

        MyPanel panHoliday = new MyPanel(new FlowLayout(FlowLayout.LEFT), "Holiday");
        panHoliday.setBackground(Color.WHITE);

        MyLabel lblHolidayNm = new MyLabel(1, "Holiday Name : ");
        lblHolidayNm.setBackground(Color.WHITE);
        panHoliday.add(lblHolidayNm);

        txtHolidayNm = new MyTextField(25);
        txtHolidayNm.setEditable(false);
        //txtCatNm.setPreferredSize(new Dimension(250, 30));
        panHoliday.add(txtHolidayNm);

        picker = JDateComponentFactory.createJDatePicker();
        JComponent guiElement = (JComponent) picker;
        ((JComponent) picker).setEnabled(false);
        guiElement.setEnabled(false);

        panHoliday.add(guiElement);

        MyPanel panCenter = new MyPanel(new BorderLayout());

        MyPanel panHolidayDetails = new MyPanel(new BorderLayout(), "Holiday Details");
        panHolidayDetails.setBackground(Color.WHITE);
        tableModel = new HolidayTableModel();

        table = new HTable(tableModel) {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                txtHolidayNm.setText((String) table.getValueAt(index, 1));
                String date = (String) table.getValueAt(index, 0);
                DateModel model = ((JDateComponent) picker).getModel();
                int month = Integer.parseInt(date.substring(3, 5));
                model.setDate(Integer.parseInt(date.substring(6, 10)), Integer.parseInt(date.substring(3, 5)) - 1, Integer.parseInt(date.substring(0, 2)));
                model.setSelected(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
 
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };

        panHolidayDetails.add(table.getGUI(), BorderLayout.CENTER);

        panCenter.add(panHolidayDetails, BorderLayout.CENTER);

        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Add Holiday Details");
        panButtons.setBackground(Color.WHITE);
        btnAdd = new MyButton("Submit", 2, Color.WHITE) {

            @Override
            public void onClick() {
            }
        };
        btnAdd.setEnabled(false);
        panButtons.add(btnAdd);

        btnCancel = new MyButton("Cancel", 2, Color.WHITE) {

            @Override
            public void onClick() {
            }
        };
        panButtons.add(btnCancel);


        panCenter.add(panButtons, BorderLayout.SOUTH);

        MainPanel.add(panHoliday, BorderLayout.NORTH);
        MainPanel.add(panCenter, BorderLayout.CENTER);

        MyPanel panStatus = new MyPanel();
        panStatus.setBackground(Color.WHITE);
        panStatus.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        MyLabel lblStatus = new MyLabel(1, "");
        lblStatus.setPreferredSize(new Dimension(width / 2 - 15, height / 35));

        lblStatus.setBackground(Color.WHITE);
        panStatus.add(lblStatus);

        MainPanel.add(panStatus, BorderLayout.SOUTH);

        contentsPane = getContentPane();
        contentsPane.setLayout(new BorderLayout());
        contentsPane.add(MainPanel, BorderLayout.CENTER);

        setVisible(true);

    }

    public abstract class HTable extends MyTable implements MouseListener {

        public HTable(MyTableModel tableModel) {
            super(tableModel);
            this.addMouseListener(this);
        }
    }
}
