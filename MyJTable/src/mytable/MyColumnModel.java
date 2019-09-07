package mytable;

import java.io.Serializable;
import javax.swing.table.TableColumn;
import mytable.cellrenderers.MyDataCellRenderer;
import mytable.cellrenderers.MyHeaderRenderer;
/**
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public abstract class MyColumnModel  implements Serializable{
    public static final int TYPE_DO_NOT_SORT = 0;
    public static final int TYPE_LONG = 1;//long
    public static final int TYPE_BIG_NUMBER = 2;//java.math.BigInteger
    public static final int TYPE_DOUBLE = 3;//double
    public static final int TYPE_DATE = 4;//dd/MM/yyyy OR dd/MM/yyyy HH:mm:ss
    public static final int TYPE_STRING = 5;//String
    public static final int TYPE_CHECK_BOX = 6;//Boolean
    //
    public static final int SORTED_NOT = 11;
    public static final int SORTED_ASCENDING = 12;
    public static final int SORTED_DESCENDING = 13;
    //
    public static final String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    public static final String DATE_FORMAT_FULL = "dd/MM/yyyy HH:mm:ss";
    private String name;
    public int width;
    public int type;
    public MyHeaderRenderer headerRenderer = null;
    public int sortedType = SORTED_NOT;
    private MyTableModel tableModel;
    private int colIndex = -1;
    private boolean isEditable = false;
    /**
     * @param name
     * @param width
     * @param type
     */
    public MyColumnModel(String name, int width, int type){
        this.name = name;
        this.width = width;
        this.type = type;
        headerRenderer = new MyHeaderRenderer();
    }
    /**
     * @param name
     * @param width
     * @param type
     */
    public MyColumnModel(String name, int width, int type, boolean isEditable){
        this.name = name;
        this.width = width;
        this.type = type;
        this.isEditable = isEditable;
        headerRenderer = new MyHeaderRenderer();
    }
    /**
     * 
     * @return
     */
    public boolean isEditable() {
        return isEditable;
    }
    /**
     *
     * @return
     */
    public int getColIndex() {
        return colIndex;
    }
    /**
     * 
     * @param colIndex
     */
    public void setColIndex(int colIndex){
        this.colIndex = colIndex;
    }
    /**
     *
     * @param colIndex
     */
    public void setTableModel(MyTableModel tableModel){
        this.tableModel = tableModel;
    }
    /**
     *
     * @return
     */
    public int getType(){
        return type;
    }
    /**
     *
     * @return
     */
    public int getSortedType(){
        return sortedType;
    }
    /**
     * 
     * @param sortedType
     */
    public void setSortedType(int sortedType){
        this.sortedType = sortedType;
    }
    /**
     *
     * @param row
     * @return
     */
    public abstract Object getValueAt(int row);
    /**
     * Override this method for editable cell
     * @param row
     * @param value
     */
    public void setValueAt(int row, Object value){}
    /**
     * 
     * @return
     */
    public String getColumnName(){
        if(colIndex != tableModel.getSortedColIndex()){
            return name+"-";
        }else if(sortedType == SORTED_ASCENDING){
            return name + (char)9652;
        }else if(sortedType == SORTED_DESCENDING){
            return name + (char)9662;
        }else{
            return name+"-";
        } 
    }
    /**
     * 
     * @param column
     */
    public void initializeColumn(TableColumn column) {
        column.setHeaderRenderer(headerRenderer);
        column.setPreferredWidth(width);
        setCellRenderer(column);
        setCellEditor(column);
    }
    
    
    /**
     * Override this method for editable cell
     * @param column
     */
    public void setCellRenderer(TableColumn column){
        column.setCellRenderer(new MyDataCellRenderer());
    }
    /**
     * Override this method for editable cell
     * @param column
     */
    public void setCellEditor(TableColumn column){}
}