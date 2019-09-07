package mytable.sorting;

//import java.util.Date;
import mytable.MyColumnModel;
import mytable.MyTableModel;
/**
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class TableSorter {
    protected static final int SORT_ASC = 0;
    protected static final int SORT_DESC = 1;
    private final MyTableModel tableModel;
    public TableSorter(MyTableModel tableModel){
        this.tableModel = tableModel;
    }
    /**
     * 
     * @param tableModel
     * @param column
     * @throws Exception 
     */
    public synchronized void sort(MyColumnModel column) throws Exception{
        if(column.getType() == MyColumnModel.TYPE_DO_NOT_SORT ){
            return;
        }
        //Date t1 = new Date();
        int sortType;
        if(column.getSortedType() == MyColumnModel.SORTED_NOT || column.getSortedType() == MyColumnModel.SORTED_DESCENDING){
            sortType = SORT_ASC;
        }else{
            sortType = SORT_DESC;
        }
        if(column.getType() == MyColumnModel.TYPE_CHECK_BOX){
            new SortBoolean(tableModel, column).sort(sortType);
        }else if(column.getType() == MyColumnModel.TYPE_LONG){
            new SortLong(tableModel, column).sort(sortType);
        }else if(column.getType() == MyColumnModel.TYPE_DOUBLE){
            new SortDouble(tableModel, column).sort(sortType);
        }else if(column.getType() == MyColumnModel.TYPE_DATE){
            new SortDate(tableModel, column).sort(sortType);
        }else if(column.getType() == MyColumnModel.TYPE_BIG_NUMBER){
            new SortBigInteger(tableModel, column).sort(sortType);
        }else if(column.getType() == MyColumnModel.TYPE_STRING){
            new SortString(tableModel, column).sort(sortType);
        }
       // Date t2 = new Date();
        //System.out.println(((t2.getTime()-t1.getTime())/1000)+" seconds, "+((t2.getTime()-t1.getTime())%1000)+" miliseconds");
    }
}