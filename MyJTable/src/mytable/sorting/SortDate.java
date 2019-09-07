package mytable.sorting;
import java.text.SimpleDateFormat;
import mytable.MyColumnModel;
import mytable.MyTableModel;
/**
 * Created on Feb 23, 2014
 * @version 1.0.0
 */
class SortDate extends MergeSort{
    private long[] val;
    private long[] tempVal;
    private long temp;
    /**
     * 
     * @param tableModel
     * @param column 
     */
    public SortDate(MyTableModel tableModel, MyColumnModel column){
        super(tableModel, column);
        val = new long[numRows];
        tempVal = new long[numRows];
        SimpleDateFormat df1 = new SimpleDateFormat(MyColumnModel.DATE_FORMAT_SHORT);
        SimpleDateFormat df2 = new SimpleDateFormat(MyColumnModel.DATE_FORMAT_FULL);
        for(int i=0;i<numRows;i++){
            try{
                String s = (String)column.getValueAt(i);
                if(s.length() == 10){
                    val[i] = df1.parse(s).getTime();  
                }else{
                    val[i] = df2.parse(s).getTime();    
                }
            }catch(Exception e){
                val[i] = Long.MAX_VALUE;
            }
        }
    }
    /**
     * @param i
     * @param j
     * @return (val[i] > val[j])
     */
    @Override
    protected boolean isGreater(int i, int j) {
        return (val[i] > val[j]);
    }
    /**
     * @param i
     * @param j
     * @return (val[i] < val[j])
     */
    @Override
    protected boolean isLesser(int i, int j) {
        return (val[i] < val[j]);
    }
    /**
     * 
     * @param i
     * @param j 
     */
    @Override
    protected void exchangeValues(int i, int j) {
        //exchange values
        temp = val[i];
        val[i] = val[j];
        val[j] = temp;
    }
    /**
     * 
     * @param t
     * @param iOrJ 
     */
    @Override
    protected void copyInTemp(int t, int iOrJ) {
        tempVal[t] = val[iOrJ];
    }
    /**
     * 
     * @param startIndex
     * @param endIndex 
     */
    @Override
    protected void copyFromTemp(int startIndex, int endIndex) {
        System.arraycopy(tempVal, startIndex, val, startIndex, endIndex-startIndex);
    }
}