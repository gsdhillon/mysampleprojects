package lib.gui;

import lib.gui.table.MyTableModel;
import lib.gui.table.MyTableColumn;
import java.io.File;
import lib.utils.MyLog;
/**
 */
public class FileListTM extends MyTableModel{
    private final int MAX_FILES = 5000;
    private final long MAX_SIZE = 1000000000;//MB
    private File[] allFiles = new File[MAX_FILES];
    private File[] filteredFiles = new File[MAX_FILES];
    private File dir;
    private int numAllFiles = 0;
    private long totalSize = 0;
    public FileListTM() throws Exception{
        super();
        //SNO
        addColumn(new MyTableColumn("S.No", 100, MyTableColumn.TYPE_DO_NOT_SORT){
            @Override
            public String getValueAt(int row) {
                return Integer.toString(row + 1)+".";
            }

        });
        //FileName
        addColumn(new MyTableColumn("FileName", 700, MyTableColumn.TYPE_STRING){
            @Override
            public String getValueAt(int row) {
                File parent = dir.getParentFile();
                if (parent == null) {
                    return (filteredFiles[row].getAbsolutePath());
                } else {
                    return (filteredFiles[row].getAbsolutePath().substring(parent.getAbsolutePath().length()));
                }
            }

        });
        //Size
        addColumn(new MyTableColumn("Size (KB)", 200, MyTableColumn.TYPE_BIG_NUM){
            @Override
            public String getValueAt(int row) {
                return Long.toString(filteredFiles[row].length()/1024);
            }

        });
    }
    /**
     * @param row1
     * @param row2
     */
    @Override
    public void exchange(int row1, int row2) {
        File temp = filteredFiles[row1];
        filteredFiles[row1] = filteredFiles[row2];
        filteredFiles[row2] = temp;
    }
    /**
     *
     * @param dir
     * @throws Exception
     */
    public void setData(File dir) throws Exception{
        this.dir = dir;
        if(dir == null){
           throw new Exception("Directory is NULL");
        }
        numAllFiles = countFiles(dir, 0);
        if(numAllFiles > MAX_FILES){
            throw new Exception("More than "+MAX_FILES+" Files!");
        }
        totalSize = countTotalSize(dir, 0);
        if(totalSize > MAX_SIZE){
            throw new Exception("SIZE more than "+(MAX_SIZE/1000000)+" MB!");
        }
        rowCount = 0;
        addFile(dir);
        fireTableDataChanged();
    }
   /**
    *
    * @param dir
    * @throws Exception
    */
    private void addFile(File dir) throws Exception {
        //count++;
        if (dir.isFile()) {
            allFiles[rowCount] = dir;
            filteredFiles[rowCount] = dir;
            rowCount++;
        } else {
            File[] fileList = dir.listFiles();
            for (File f : fileList) {
                addFile(f);
            }
        }
    }
    /**
     *
     * @param name
     */
    public void filterFiles(String name){
        rowCount = 0;
        if(name==null || name.length()==0){
            for(int i=0;i<numAllFiles;i++){
                filteredFiles[rowCount++] = allFiles[i];
            }
        }else{
            for(int i=0;i<numAllFiles;i++){
                if(allFiles[i].getAbsolutePath().toLowerCase().indexOf(name.toLowerCase())!=-1){
                    filteredFiles[rowCount++] = allFiles[i];
                }
            }
        }
        fireTableDataChanged();
    }
    /**
     *
     * @param dir
     * @param count
     * @return
     * @throws Exception
     */
    private int countFiles(File dir, int count) throws Exception {
        if (dir.isFile()) {
            return count+1;
        } else {
            File[] fileList = dir.listFiles();
            int sum = 0;
            for (File f : fileList) {
                sum += countFiles(f, count);
            }
            return sum;
        }
    }
    /**
     *
     * @param dir
     * @param size
     * @return
     * @throws Exception
     */
    private long countTotalSize(File dir, long size) throws Exception {
        if (dir.isFile()) {
            return size+dir.length();
        } else {
            File[] fileList = dir.listFiles();
            long sum = 0;
            for (File f : fileList) {
                sum += countTotalSize(f, size);
            }
            return sum;
        }
    }
    /**
     * 
     * @return
     */
    public File getSelectedFile(int row) {
        return filteredFiles[row];
    }
    /**
     * 
     */
    public String getInfo() {
        if(dir == null){
            return "No file selected.";
        }else{
            return dir.getName()+", total files = "+numAllFiles+ " , total size = "+(totalSize/1000000)+" MB";
        }
    }
    /**
     *
     */
    public void deleteAllSelectedFile()  throws Exception{
        int total = 0;
        int deleted = 0;
        for(int i=0;i<rowCount;i++){
            if(isRowSelected(i)){
                total++;
                if(filteredFiles[i].delete()){
                    deleted++;
                }else{
                    MyLog.showErrorMsg("File "+filteredFiles[i].getName()+" could not be deleted!");
                }
            }
        }
        MyLog.showInfoMsg(deleted+" out of "+total+" files have been deleted.");
        setData(dir);
    }
}