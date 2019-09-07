package gui.mytable;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import gui.mycomponents.MyConstants;
import gui.mycomponents.MyKeyListener;
import gui.myeventlisteners.MyMouseListener;
import gui.mycomponents.MyTextField;
/**
 */
public class MyTable extends JTable  implements Serializable{
    public static final int SEARCH_TYPE_EXACT_MATCH = 0;
    public static final int SEARCH_TYPE_CONTAINS = 1;
    public static final int SEARCH_TYPE_STARTS_WITH = 2;
    private MyTableModel tableModel;
    private MyTextField[] searchText;
    /**
     * Constructor
     */
    public MyTable(MyTableModel tableModel){
        super(tableModel);
        this.tableModel = tableModel;
        searchText = new MyTextField[tableModel.getColumnCount()];
        tableModel.setCellRenderersAndEditors(this);
        JTableHeader header = getTableHeader();
        header.addMouseListener(listener);
        header.setUpdateTableInRealTime(true);
        header.setReorderingAllowed(true);
        ListSelectionModel lsm = getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setRowHeight(22);
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
    public JScrollPane getGUI(){
        JScrollPane sp = new JScrollPane(this);
        sp.getViewport().setBackground(MyConstants.BG_COLOR);
        return sp;
    }
    /**
     *
     */
    //sort table data on the column ON MouseClick
    MyMouseListener listener = new MyMouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            TableColumnModel colModel = getColumnModel();
            int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
            tableModel.sortTableRows(columnModelIndex);
        }
    };
    /**
     * 
     * @param col
     * @param textField
     * @param searchType 
     */
    public void addSearchField(final int col, MyTextField textField, final int searchType){
        searchText[col] = textField;
        textField.addKeyListener(new MyKeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == '\n'){
                    searchNext(searchText[col].getText(), col, searchType);
                }
            }
        });
    }
    
    /**
     * @param table - JTable
     * @param searchString - not case sensitive
     * @param searchCol - col number of the table
     * @param searchType - 0 exact match, 1 containing, 2 startsWith
     */
    public void searchNext(String string, int col, int type) {
        if(getRowCount()==0){
            //System.out.println("row count 0");
            return;
        }
        int selectedRow = getSelectionModel().getMinSelectionIndex();
        if(selectedRow == -1){
            selectedRow = 0;
        }
        int numRows = getRowCount();
        string = string.trim().toLowerCase();
        boolean found = false;
        int count = 0;
        int i = (selectedRow+1)%numRows;
        while(count<numRows){
            String targateString = (String)getValueAt(i, col);
            targateString = targateString.toLowerCase();
            if(type == SEARCH_TYPE_EXACT_MATCH){//t = s
                if(targateString.equals(string)){
                    found = true;
                }
            }else if(type == SEARCH_TYPE_CONTAINS){//t contains s
                if(targateString.indexOf(string) != -1){
                    found = true;
                }
            }else if(type == SEARCH_TYPE_STARTS_WITH){//t starts with s
                if(targateString.startsWith(string)){
                    found = true;
                }
            }
            if(found){
                getSelectionModel().setSelectionInterval(i, i);
                Rectangle rect = getCellRect(i, 0, true);
                scrollRectToVisible(rect);
                break;//while loop
            }
            i = (i+1)%numRows;
            count++;
        }
    }
}