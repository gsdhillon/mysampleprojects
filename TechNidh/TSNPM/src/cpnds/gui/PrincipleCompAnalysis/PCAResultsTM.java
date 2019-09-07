package cpnds.gui.PrincipleCompAnalysis;

import cpnds.gui.ADCAcquisition.*;
import gui.MyTableModel;
/**
 * @type     : Java Class
 * @name     : PCAResultsTM
 * @file     : PCAResultsTM.java
 * @created  : Jun 22, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class PCAResultsTM extends MyTableModel{
    private PCAParams[] pcaParams;
    private ADCAcqParams[] adcAcqParams;
    private int[] colWidth = {50, 100, 
                                100, 100};
    private String[] colNames = {"UID", "InputPower",
                                 "StartSampleNo", "PCA Value"};
    
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
        return pcaParams!=null?pcaParams.length:0;
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
            return pcaParams[rowIndex].uid+"";
        }else if(columnIndex==1){
            return adcAcqParams[rowIndex].inputPower+"";
        }else if(columnIndex==2){
            return pcaParams[rowIndex].startingSampleNumber+"";
        }else if(columnIndex==3){
            return pcaParams[rowIndex].pcaVal+"";
        }else{
            return "";
        }
    }

    public void setData(ADCAcqParams[] adcAcqParams, PCAParams[] pcaParams){
        this.adcAcqParams = adcAcqParams;
        this.pcaParams = pcaParams;
        fireTableDataChanged();
    }
}
