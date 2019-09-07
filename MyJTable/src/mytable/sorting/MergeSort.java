package mytable.sorting;
import mytable.MyColumnModel;
import mytable.MyTableModel;
/**
 * Created on Feb 23, 2014
 * @version 1.0.0
 */
class MergeSort {
    private int[] index;
    private int[] tempIndex;
    private MyTableModel tableModel;
    private MyColumnModel column;
    protected int numRows = -1;
    /**
     * 
     * @param tableModel
     * @param column 
     */
    MergeSort(MyTableModel tableModel, MyColumnModel column){
        this.tableModel = tableModel;
        this.column = column;
        numRows = tableModel.getRowCount();
        tempIndex = new int[numRows];
        index = new int[numRows];
        for(int i=0;i<numRows;i++){
            index[i] = i;
        }
    }
    /**
     * 
     * @param sortType
     * @throws Exception 
     */
    void sort(int sortType) throws Exception{
        parseValues(column, numRows);
        if(sortType == TableSorter.SORT_ASC){
            sortAsc(0, index.length);
            column.setSortedType(MyColumnModel.SORTED_ASCENDING);
        }else{
            sortDesc(0, index.length);
            column.setSortedType(MyColumnModel.SORTED_DESCENDING);
        }
        if(index == null || index.length != numRows){
            return;
        }
        tableModel.shuffle(index);
        tableModel.setSortedColIndex(column.getColIndex());
        
    }
    /**
     * 
     * @param start
     * @param end
     * @throws Exception 
     */
    private void sortAsc(int start, int end)  throws Exception{
        if(start > (end - 2)){//only one or zero elements
            return;
        }
        if(start > (end - 3)){//two elements
            if(isGreater(start, start+1)){
                exchangeValues(start, start+1);
                //exchange indexs
                int tempIdx = index[start];
                index[start] = index[start+1];
                index[start+1] = tempIdx;
            }
            return;
        }
        //3 or more elements
        int mid = (start+end)/2;
        sortAsc(start, mid);
        sortAsc(mid, end);
        mergeAsc(start, mid, end);
    }
    /**
     * 
     * @param start
     * @param mid
     * @param end 
     */
    private void mergeAsc(int start, int mid, int end) throws Exception{
        int t = start;
        int i = start;
        int j = mid;
        while(i < mid && j < end){
            if(isLesser(i, j)){
                tempIndex[t] = index[i];
                copyInTemp(t++, i++);
            }else{
                tempIndex[t] = index[j];
                copyInTemp(t++, j++);
            }
        }
        while(i < mid){
            tempIndex[t] = index[i];
            copyInTemp(t++, i++);
        }
        while(j < end){
            tempIndex[t] = index[j];
            copyInTemp(t++, j++);
        }
        System.arraycopy(tempIndex, start, index, start, end-start);
        copyFromTemp(start, end);
    }
    /**
     * 
     * @param start
     * @param end
     * @throws Exception 
     */
    private void sortDesc(int start, int end)  throws Exception{
        if(start > (end - 2)){//only one or zero elements
            return;
        }
        if(start > (end - 3)){//two elements
            if(isLesser(start, start+1)){
                exchangeValues(start, start+1);
                //exchange indexs
                int tempIdx = index[start];
                index[start] = index[start+1];
                index[start+1] = tempIdx;
            }
            return;
        }
        //3 or more elements
        int mid = (start+end)/2;
        sortDesc(start, mid);
        sortDesc(mid, end);
        mergeDesc(start, mid, end);
    }
    /**
     * 
     * @param start
     * @param mid
     * @param end 
     */
    private void mergeDesc(int start, int mid, int end) {
        int t = start;
        int i = start;
        int j = mid;
        while(i < mid && j < end){
            if(isGreater(i, j)){
                tempIndex[t] = index[i];
                copyInTemp(t++, i++);
            }else{
                tempIndex[t] = index[j];
                copyInTemp(t++, j++);
            }
        }
        while(i < mid){
            tempIndex[t] = index[i];
            copyInTemp(t++, i++);
        }
        while(j < end){
            tempIndex[t] = index[j];
            copyInTemp(t++, j++);
        }
        System.arraycopy(tempIndex, start, index, start, end-start);
        copyFromTemp(start, end);
    }
    /**
     * 
     * @param column
     * @param numRows 
     */
    protected void parseValues(MyColumnModel column, int numRows){
        
    }
    /**
     * @param i
     * @param j
     * @return (val[i] > val[j])
     */
    protected boolean isGreater(int i, int j){
        return false;
    }
    /**
     * @param i
     * @param j
     * @return (val[i] < val[j])
     */
    protected boolean isLesser(int i, int j){
        return false;
    }
    /**
     * 
     * @param i
     * @param j 
     */
    protected void exchangeValues(int i, int j){
        //do nothing
    }
    /**
     * 
     * @param t
     * @param iOrJ 
     */
    protected void copyInTemp(int t, int iOrJ){
        //do nothing
    }
    /**
     * 
     * @param startIndex
     * @param endIndex 
     */
    protected void copyFromTemp(int startIndex, int endIndex){
        //do nothing
    }
}
