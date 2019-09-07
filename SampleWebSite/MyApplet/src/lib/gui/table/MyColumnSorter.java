package lib.gui.table;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JCheckBox;

/**
 */
public class MyColumnSorter {
    public static void sortColumn(MyTableModel tableModel, MyTableColumn column) throws Exception{
        if(column.getType() == MyTableColumn.TYPE_DO_NOT_SORT ){
            return;
        }
        if(column.getSortedType() == MyTableColumn.SORTED_NOT ||
                column.getSortedType() == MyTableColumn.SORTED_DESCENDING){
            sortAscending(tableModel, column);
        }else{
            sortDescending(tableModel, column);
        }
    }
    /**
     *
     * @param column
     * @throws Exception
     */
    private static void sortAscending(MyTableModel tableModel, MyTableColumn column)  throws Exception{
        /**** Gurmeet null or invalid values comes last  12-08-11 **********/
        int numRows = tableModel.getRowCount();
        if(column.getType()==MyTableColumn.TYPE_INT){
            long[] longNum = new long[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    longNum[i] = Long.parseLong((String)column.getValueAt(i));
                }catch(Exception e){
                    longNum[i] = Long.MAX_VALUE;
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(longNum[j] > longNum[j+1]){
                        tableModel.exchange(j, j+1);
                        long temp = longNum[j];
                        longNum[j] = longNum[j+1];
                        longNum[j+1] = temp;
                    }
                }
            }
        }if(column.getType()==MyTableColumn.TYPE_FLOAT){
            float[] floatNum = new float[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    floatNum[i] = Float.parseFloat((String)column.getValueAt(i));
                }catch(Exception e){
                    floatNum[i] = Float.MAX_VALUE;
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(floatNum[j] > floatNum[j+1]){
                        tableModel.exchange(j, j+1);
                        float temp = floatNum[j];
                        floatNum[j] = floatNum[j+1];
                        floatNum[j+1] = temp;
                    }
                }
            }
        }else if(column.getType()==MyTableColumn.TYPE_STRING){
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    String s1 = (String)column.getValueAt(j);
                    String s2 = (String)column.getValueAt(j+1);
                    if(s1 == null) s1 = "zzzzzzzzzz";
                    if(s2 == null) s2 = "zzzzzzzzzz";
                    if(s1.compareToIgnoreCase(s2)>0){
                        tableModel.exchange(j, j+1);
                    }
                }
            }
        }else if(column.getType()==MyTableColumn.TYPE_DATE_SHORT){
            SimpleDateFormat df = new SimpleDateFormat(MyTableColumn.DATE_FORMAT_SHORT);
            Date[] d = new Date[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    d[i] = df.parse((String)column.getValueAt(i));
                }catch(Exception e){
                    d[i] = df.parse("31/12/2099");
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(d[j].after(d[j+1])){
                        tableModel.exchange(j, j+1);
                        Date temp = d[j];
                        d[j] = d[j+1];
                        d[j+1] = temp;
                    }
                }
            }
        }else if(column.getType()==MyTableColumn.TYPE_DATE_FULL){
            SimpleDateFormat df = new SimpleDateFormat(MyTableColumn.DATE_FORMAT_FULL);
            Date[] d = new Date[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    d[i] = df.parse((String)column.getValueAt(i));
                }catch(Exception e){
                    d[i] = df.parse("31/12/2099 23:59:59");
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(d[j].after(d[j+1])){
                        tableModel.exchange(j, j+1);
                        Date temp = d[j];
                        d[j] = d[j+1];
                        d[j+1] = temp;
                    }
                }
            }
        }else if(column.getType()==MyTableColumn.TYPE_BIG_NUM){
            BigInteger[] bigInt = new BigInteger[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    bigInt[i] = new BigInteger((String)column.getValueAt(i));
                }catch(Exception e){
                    bigInt[i] = BigInteger.valueOf(-Long.MAX_VALUE);
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(bigInt[j].compareTo(bigInt[j+1])>0){
                        tableModel.exchange(j, j+1);
                        BigInteger temp = bigInt[j];
                        bigInt[j] = bigInt[j+1];
                        bigInt[j+1] = temp;
                    }
                }
            }
        }else if(column.getType()==MyTableColumn.TYPE_CHECK_BOX){
            int[] intVal = new int[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    intVal[i] = ((JCheckBox)column.getValueAt(i)).isSelected()?1:0;
                }catch(Exception e){
                    intVal[i] = 0;
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(intVal[j] < intVal[j+1]){
                        tableModel.exchange(j, j+1);
                        int temp = intVal[j];
                        intVal[j] = intVal[j+1];
                        intVal[j+1] = temp;
                    }
                }
            }
        }
        column.setSortedType(MyTableColumn.SORTED_ASCENDING);
        tableModel.setSortedColIndex(column.getColIndex());

    }
    /**
     *
     * @param column
     * @throws Exception
     */
    private static void sortDescending(MyTableModel tableModel, MyTableColumn column) throws Exception{
        /**** Gurmeet null or invalid values comes last  12-08-11 **********/
        int numRows = tableModel.getRowCount();
        if(column.getType()==MyTableColumn.TYPE_INT){
            long[] longNum = new long[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    longNum[i] = Long.parseLong((String)column.getValueAt(i));
                }catch(Exception e){
                    longNum[i] = Long.MAX_VALUE;
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(longNum[j] < longNum[j+1]){
                        tableModel.exchange(j, j+1);
                        long temp = longNum[j];
                        longNum[j] = longNum[j+1];
                        longNum[j+1] = temp;
                    }
                }
            }
        }if(column.getType()==MyTableColumn.TYPE_FLOAT){
            float[] floatNum = new float[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    floatNum[i] = Float.parseFloat((String)column.getValueAt(i));
                }catch(Exception e){
                    floatNum[i] = Float.MAX_VALUE;
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(floatNum[j] < floatNum[j+1]){
                        tableModel.exchange(j, j+1);
                        float temp = floatNum[j];
                        floatNum[j] = floatNum[j+1];
                        floatNum[j+1] = temp;
                    }
                }
            }
        }else if(column.getType()==MyTableColumn.TYPE_STRING){
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    String s1 = (String)column.getValueAt(j);
                    String s2 = (String)column.getValueAt(j+1);
                    if(s1 == null) s1 = "zzzzzzzzzz";
                    if(s2 == null) s2 = "zzzzzzzzzz";
                    if(s1.compareToIgnoreCase(s2) < 0){
                        tableModel.exchange(j, j+1);
                    }
                }
            }
        }else if(column.getType()==MyTableColumn.TYPE_DATE_SHORT){
            SimpleDateFormat df = new SimpleDateFormat(MyTableColumn.DATE_FORMAT_SHORT);
            Date[] d = new Date[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    d[i] = df.parse((String)column.getValueAt(i));
                }catch(Exception e){
                    d[i] = df.parse("31/12/2099");
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(d[j].before(d[j+1])){
                        tableModel.exchange(j, j+1);
                        Date temp = d[j];
                        d[j] = d[j+1];
                        d[j+1] = temp;
                    }
                }
            }
        }else if(column.getType()==MyTableColumn.TYPE_DATE_FULL){
            SimpleDateFormat df = new SimpleDateFormat(MyTableColumn.DATE_FORMAT_FULL);
            Date[] d = new Date[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    d[i] = df.parse((String)column.getValueAt(i));
                }catch(Exception e){
                    d[i] = df.parse("31/12/2099 23:59:59");
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(d[j].before(d[j+1])){
                        tableModel.exchange(j, j+1);
                        Date temp = d[j];
                        d[j] = d[j+1];
                        d[j+1] = temp;
                    }
                }
            }
        }else if(column.getType()==MyTableColumn.TYPE_BIG_NUM){
            BigInteger[] bigInt = new BigInteger[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    bigInt[i] = new BigInteger((String)column.getValueAt(i));
                }catch(Exception e){
                    bigInt[i] = BigInteger.valueOf(-Long.MAX_VALUE);
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(bigInt[j].compareTo(bigInt[j+1]) < 0){
                        tableModel.exchange(j, j+1);
                        BigInteger temp = bigInt[j];
                        bigInt[j] = bigInt[j+1];
                        bigInt[j+1] = temp;
                    }
                }
            }
        }else if(column.getType()==MyTableColumn.TYPE_CHECK_BOX){
            int[] intVal = new int[numRows];
            for(int i=0;i<numRows;i++){
                try{
                    intVal[i] = ((JCheckBox)column.getValueAt(i)).isSelected()?1:0;
                }catch(Exception e){
                    intVal[i] = 0;
                }
            }
            for(int i=0;i<numRows-1;i++){
                for(int j=0;j<numRows-1-i;j++) {
                    if(intVal[j] > intVal[j+1]){
                        tableModel.exchange(j, j+1);
                        int temp = intVal[j];
                        intVal[j] = intVal[j+1];
                        intVal[j+1] = temp;
                    }
                }
            }
        }
        column.setSortedType(MyTableColumn.SORTED_DESCENDING);
        tableModel.setSortedColIndex(column.getColIndex());
    }
}
