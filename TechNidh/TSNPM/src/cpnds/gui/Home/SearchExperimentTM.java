package cpnds.gui.Home;

import cpnds.gui.ADCAcquisition.*;
import gui.MyTableModel;
/**
 * @type     : Java Class
 * @name     : SearchExperimentTM
 * @file     : SearchExperimentTM.java
 * @created  : Jun 22, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class SearchExperimentTM extends MyTableModel{
    private ADCAcqParams[] adcAcqParams;
    private int[] colWidth = {50, 120, 120, 100, 100, 100, 100, 100, 110, 100, 100};
    private String[] colNames = {"UID", "Exp. Date", "Exp. Time", "FS [Hz]",
                                 "Pulse Height", "Sampling Avg", "Pulse Width",
                                 "Input Power", "InputCountRate", "Snapshot Time",
                                 "No. of Points"};
    //private final NumberFormat f = new DecimalFormat("#000000.000000000000");

    @Override
    public int getColumnWidth(int col) {
        return colWidth[col];
    }

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }
    /**
     * 
     * @return
     */
    public int getRowCount() {
        return adcAcqParams!=null?adcAcqParams.length:0;
    }
    /**
     *
     * @return
     */
    public int getColumnCount() {
        return colNames.length;
    }
    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex==0){
            return adcAcqParams[rowIndex].uid+"";
        }else if(columnIndex==1){
            return adcAcqParams[rowIndex].paramFile.getParentFile().getParentFile().getParentFile().getName();
        }else if(columnIndex==2){
            return adcAcqParams[rowIndex].paramFile.getParentFile().getParentFile().getName();
        }else if(columnIndex==3){
            try{
                float fs = adcAcqParams[rowIndex].getFS();
                return fs+" Hz";
            }catch(Exception e){
                return "null";
            }
        }else if(columnIndex==4){
            return adcAcqParams[rowIndex].pulseHeight+"";
        }else if(columnIndex==5){
            return adcAcqParams[rowIndex].samplingAvg+"";
        }else if(columnIndex==6){
            return adcAcqParams[rowIndex].pulseWidth+"";
        }else if(columnIndex==7){
            return adcAcqParams[rowIndex].inputPower+"";
        }else if(columnIndex==8){
            return adcAcqParams[rowIndex].inputCountRate+"";
        }else if(columnIndex==9){
            try{
                float snapShotTime = adcAcqParams[rowIndex].getSnapshotTime();
                return snapShotTime+"";
            }catch(Exception e){
                return "null";
            }
        }else if(columnIndex==10){
            return adcAcqParams[rowIndex].numOfPoints+"";
        }else{
            return "";
        }
    }

    public void setData(ADCAcqParams[] adcAcqParams){
        this.adcAcqParams = adcAcqParams;
        fireTableDataChanged();
    }
}
