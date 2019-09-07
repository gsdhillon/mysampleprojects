/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.worklocation;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;

/**
 *
 * @author pradnya
 */
public class WorkLocMaster extends JApplet {

    private Container contentsPane;
    public MyButton btnAdd, btnCancel;
    public WLTableModel tableModel;
    public MyTable table;
    public MyTextField txtWorkLocation, txtWorkLocationcode;

    @Override
    public void init() {
        super.init();
        try {
            new WorkLocMaster();
        } catch (Exception ex) {
            Logger.getLogger(WorkLocMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public WorkLocMaster() throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width / 2.8);
        int height = screenSize.height / 2;
        setSize(width, height);

        MyPanel MainPanel = new MyPanel(new BorderLayout());
        MainPanel.setBackground(Color.GRAY);

        MyPanel panelWL = new MyPanel(new FlowLayout(FlowLayout.CENTER, 5, 5), "Work Location");
        panelWL.setPreferredSize(new Dimension(width / 2, height / 6));

        MyLabel lblWorkLoccode = new MyLabel(1, "Work Location code : ");
        panelWL.add(lblWorkLoccode);

        txtWorkLocationcode = new MyTextField();
        txtWorkLocationcode.setPreferredSize(new Dimension(width / 3, height / 15));
        panelWL.add(txtWorkLocationcode);

        MyLabel lblWorkLocation = new MyLabel(1, "Work Location : ");
        panelWL.add(lblWorkLocation);

        txtWorkLocation = new MyTextField();
        txtWorkLocation.setPreferredSize(new Dimension(width / 2, height / 15));
        panelWL.add(txtWorkLocation);

        MainPanel.add(panelWL, BorderLayout.NORTH);

        MyPanel panCenter = new MyPanel(new BorderLayout());

        MyPanel panWLTable = new MyPanel(new BorderLayout(), "Work Location Details");

        panWLTable.setBackground(Color.WHITE);
        tableModel = new WLTableModel();
        table = new MyTable(tableModel);

        panWLTable.add(table.getGUI(), BorderLayout.CENTER);

        panCenter.add(panWLTable, BorderLayout.CENTER);

        //adding Buttons
        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER), "Add Work Location");
        btnAdd = new MyButton("SAVE", 0, Color.WHITE) {

            @Override
            public void onClick() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        panButtons.add(btnAdd);

        btnCancel = new MyButton("Cancel", 2, Color.WHITE) {

            @Override
            public void onClick() {
                txtWorkLocationcode.setText("");
                txtWorkLocation.setText("");
                btnAdd.setEnabled(false);
            }
        };
        panButtons.add(btnCancel);
        panCenter.add(panButtons, BorderLayout.SOUTH);
        MainPanel.add(panCenter, BorderLayout.CENTER);

        contentsPane = getContentPane();
        contentsPane.setLayout(new BorderLayout());
        contentsPane.add(MainPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
