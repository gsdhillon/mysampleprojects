package mytable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import mytable.cellrenderers.MyDataCellRenderer;
import mytable.cellrenderers.MyRowNumberRenderer;
import mytable.sorting.TableSorter;
/**
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public abstract class MyTableModel extends AbstractTableModel implements Serializable{
    private static final int MAX_COLUMNS = 100;
    public int rowCount = 0;
    public int numColumns = 0;
    private MyColumnModel[] myColumns = new MyColumnModel[MAX_COLUMNS];
    private MyTable table;
    private final TableSorter sorter;
    private int sortedColIndex = -1;
    private JTextField[] searchTextFields;
    private JPanel searchPanel = null;
    private final boolean showSNo;
    /**
     *
     */
    public MyTableModel(boolean showSNo) throws Exception{
        super();
        this.showSNo = showSNo;
        if(showSNo){
            // 
            addColumn(new MyColumnModel("SNo", 25, MyColumnModel.TYPE_DO_NOT_SORT){
                @Override
                public Object getValueAt(int row) {
                    return (row+1)+".";
                }
                @Override
                public void setCellRenderer(TableColumn column){
                    column.setCellRenderer(new MyRowNumberRenderer());
                }
            });
        }
        sorter = new TableSorter(this);
    }
    /**
     * 
     */
    public void addSearchFields(){
        searchTextFields = new JTextField[getColumnCount()];
        searchPanel =  new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
        for(int i=0;i<searchTextFields.length;i++){
            searchTextFields[i] = new JTextField();
            final int col = i;
            searchTextFields[i].addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if(e.getKeyChar() == '\n'){
                        searchNext(searchTextFields[col].getText(), col, MyTable.SEARCH_TYPE_CONTAINS);
                    }
                }
                @Override
                public void keyPressed(KeyEvent e) {
                }
                @Override
                public void keyReleased(KeyEvent e) {
                }
            });
            searchTextFields[i].setToolTipText("Type search text and press ENTER");
            searchPanel.add(searchTextFields[i]);
        }
        if(showSNo){
            searchTextFields[0].setEditable(false);
            searchTextFields[0].setText(" ☛");
            searchTextFields[0].setBackground(MyDataCellRenderer.bgEven);
            searchTextFields[0].setForeground(Color.WHITE);
        }
        setSearchTextFieldsWidth(searchPanel, searchTextFields);
    }
    /**
     * 
     * @return
     */
    public final void  addColumn(MyColumnModel tableColumn) throws Exception{
        if(numColumns >= MAX_COLUMNS){
            throw new Exception("MAX_COLUMNS_ALLOWED = "+MAX_COLUMNS);
        }
        tableColumn.setColIndex(numColumns);
        tableColumn.setTableModel(this);
        myColumns[numColumns++] = tableColumn;
    }
    /**
     *
     * @param table
     */
    public void setColumnModels(final MyTable table){
        this.table = table;
        final TableColumnModel tcm = table.getColumnModel();
        for (int i = 0; i < getColumnCount(); i++){
            myColumns[i].initializeColumn(tcm.getColumn(i));
        }
        JTableHeader header = table.getTableHeader();
        header.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tcm.getColumnIndexAtX(e.getX());
                if(col==0){
                    table.setSelectionInterval(0, rowCount);
                }else{
                    sort(col);
                }
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
        });
        header.setUpdateTableInRealTime(true);
        header.setReorderingAllowed(true);
    }
    /**
     * @param table - JTable
     * @param searchString - not case sensitive
     * @param searchCol - col number of the table
     * @param searchType - 0 exact match, 1 containing, 2 startsWith
     */
    private void searchNext(String searchText, int col, int type) {
        try{
            if(getRowCount()==0){
                //System.out.println("row count 0");
                return;
            }
            int selectedRow = table.getSelectionModel().getMinSelectionIndex();
            if(selectedRow == -1){
                selectedRow = 0;
            }
            int numRows = getRowCount();
            searchText = searchText.trim().toLowerCase();
            boolean found = false;
            int count = 0;
            int i = (selectedRow+1)%numRows;
            while(count<numRows){
                String targateString = (String)getValueAt(i, col);
                targateString = targateString.toLowerCase();
                if(type == MyTable.SEARCH_TYPE_EXACT_MATCH){//t = s
                    if(targateString.equals(searchText)){
                        found = true;
                    }
                }else if(type == MyTable.SEARCH_TYPE_CONTAINS){//t contains s
                    if(targateString.indexOf(searchText) != -1){
                        found = true;
                    }
                }else if(type == MyTable.SEARCH_TYPE_STARTS_WITH){//t starts with s
                    if(targateString.startsWith(searchText)){
                        found = true;
                    }
                }
                if(found){
                    table.getSelectionModel().setSelectionInterval(i, i);
                    Rectangle rect = table.getCellRect(i, 0, true);
                    table.scrollRectToVisible(rect);
                    break;//while loop
                }
                i = (i+1)%numRows;
                count++;
            }
        }catch(Exception e){
            
        }
    }
    /**
     * 
     */
    public void resort(){
        if(sortedColIndex>-1){
            sort(sortedColIndex);
        }
    }
    /**
     * 
     * @param col 
     */
    public void sort(int col){
        try{
            sorter.sort(myColumns[col]);
            refreshColumnHeaders();
            table.clearSelection();
            Rectangle rect = table.getCellRect(0, 0, true);
            table.scrollRectToVisible(rect);
            table.repaint();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Exception in sorting!\n"+ex.getMessage());
        }
    }
    /**
     * 
     * @param searchPanel
     * @param searchTextFields 
     */    
    public void setSearchTextFieldsWidth(JPanel searchPanel, JTextField[] searchTextFields) {
        for(int i=0;i< getColumnCount(); i++){
            searchTextFields[i].setPreferredSize(new Dimension(myColumns[i].width, 22));
        }
    }
    /**
     * 
     * @param searchPanel
     * @param searchTextFields 
     */    
    public void resetSearchTextFieldsWidth() {
        if(searchPanel == null){
            return;
        }
        if(getActualColumnWidths()){
            searchPanel.removeAll();
            for(int i=0;i< getColumnCount(); i++){
                searchTextFields[i].setPreferredSize(new Dimension(myColumns[i].width, 22));
                searchPanel.add(searchTextFields[i]);
            }
            searchPanel.validate();
            searchPanel.repaint();
           // System.out.println("SearchTextFields Resized");
        }
    }
    /**
     * 
     */
    public boolean getActualColumnWidths(){
        boolean cloWidthResized = false;
        for (int i = 0; i < getColumnCount(); i++){
            TableColumn column = table.getColumnModel().getColumn(i);
            if(myColumns[i].width != column.getWidth()){
                myColumns[i].width = column.getWidth();
                cloWidthResized = true;
            }
        }
        return cloWidthResized;
    }
    /**
     *
     * @param table
     */
    public void refreshColumnHeaders(){
        getActualColumnWidths();
        fireTableStructureChanged();
        for (int i = 0; i < getColumnCount(); i++){
            TableColumn column = table.getColumnModel().getColumn(i);
            myColumns[i].initializeColumn(column);
        }
       // System.out.println("TableColumns ReInitialized");
    }
    /**
     * 
     * @param col
     * @return 
     */
    @Override
    public String getColumnName(int col){
        return myColumns[col].getColumnName();
    }
    /**
     *
     * @param row
     * @return
     */
    public boolean isRowSelected(int row){
        return table.isRowSelected(row);
    }
    /**
     *
     * @return
     */
    public int getSortedColIndex() {
        return sortedColIndex;
    }
    /**
     *
     * @param sortedColIndex
     */
    public void setSortedColIndex(int sortedCol) {
        this.sortedColIndex = sortedCol;
    }
    /**
     * 
     * @param row
     * @param col
     * @return 
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return myColumns[col].isEditable();
    }
    /**
     * 
     * @return 
     */
    @Override
    public int getColumnCount() {
        return numColumns;
    }
    /**
     * 
     * @return 
     */
    @Override
    public int getRowCount() {
        return rowCount;
    }
    /**
     *
     * @param col
     * @return
     */
    public int getColType(int col) {
         return myColumns[col].type;
    }
    /**
     * 
     * @param row
     * @param col
     * @return 
     */
    @Override
    public Object getValueAt(int row, int col) {
       return myColumns[col].getValueAt(row);
    }
    /**
     * 
     * @param value
     * @param row
     * @param col 
     */
    @Override
    public void setValueAt(Object value, int row, int col){
        myColumns[col].setValueAt(row, value);
    }
    /**
     * 
     * @param index0
     * @param index1
     */
    public void setSelectionInterval(int index0, int index1){
        if(getRowCount()>index1){
            table.getSelectionModel().setSelectionInterval(0, 0);
        }
    }
//    /**
//     * Exchange data of row1 and row2
//     * @param row1
//     * @param row2
//     */
//    public abstract void exchange(int row1, int row2);
    /**
     * Override this method for sorting
     * @param index 
     */
    public void shuffle(int[] index){
    }
    /**
     * Override this method if you implements filter option
     * @param text
     */
    public void filter(String text){
    }
    /**
     * Override this method in your TM implementation
     * @param row
     * @return
     */
    public Object getDataObject(int row){
        return null;
    }
    /**
     * 
     * @return 
     */
    public JPanel getSearchPanel() {
        return searchPanel;
    }
}