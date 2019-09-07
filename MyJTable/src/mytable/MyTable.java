package mytable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
/**
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class MyTable extends JTable implements Serializable{
//    public static final Color HEADER_BG = new Color(219, 91, 130);
    public static final int SEARCH_TYPE_EXACT_MATCH = 0;
    public static final int SEARCH_TYPE_CONTAINS = 1;
    public static final int SEARCH_TYPE_STARTS_WITH = 2;
    //
    public static final int SEARCH_FIELD_NORTH = 21;
    public static final int SEARCH_FIELD_SOUT = 22;
    public int searchFieldsPosition = SEARCH_FIELD_NORTH;
    //
    private MyTableModel tableModel;
    /**
     * 
     * @param tableModel
     * @param addSearchFields 
     */
    public MyTable(MyTableModel tableModel, boolean addSearchFields){
        super(tableModel);
        this.tableModel = tableModel;
        final MyTable table = this;
        //
        if(addSearchFields){
            tableModel.addSearchFields();
        }
        //
        tableModel.setColumnModels(table);
        ListSelectionModel lsm = getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setRowHeight(22);
        setSelectionBackground(Color.BLUE);
        setSelectionForeground(Color.WHITE);
    }
    /**
     * 
     * @param g 
     */
    @Override
    public void paint(Graphics g){
        super.paint(g);
        tableModel.resetSearchTextFieldsWidth();
    }
    /**
     *
     * @param lsl
     */
    public void addListSelectionListener(ListSelectionListener lsl){
        ListSelectionModel lsm = getSelectionModel();
        lsm.addListSelectionListener(lsl);
    }
    /**
     * 
     * @param start
     * @param end
     */
    public void setSelectionInterval(int start, int end){
        getSelectionModel().setSelectionInterval(start, end);
        Rectangle rect = getCellRect(end, 0, true);
        scrollRectToVisible(rect);
    }
    /**
     *
     * @return
     */
    public JPanel getGUI(){
        JPanel p = new JPanel(new BorderLayout(0, 5));
        JScrollPane sp = new JScrollPane(this);
        sp.getViewport().setBackground(Color.WHITE);
        p.add(sp, BorderLayout.CENTER);
        JPanel searchPanel = tableModel.getSearchPanel();
        if(searchPanel != null){
            p.add(searchPanel, searchFieldsPosition==SEARCH_FIELD_NORTH?BorderLayout.NORTH:BorderLayout.SOUTH);
        }
        return p;
    }
}