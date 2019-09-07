/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.AddCompany;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author nbpatilp
 */
public class CmpVisitorTableModel extends MyTableModel{
 private CmpVisitorClass[] CmpVstInfo;
    
    public CmpVisitorTableModel() throws Exception{
    
    
        addColumn(new MyTableColumn("Name", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return CmpVstInfo[row].Name ;
            }
        });
        
        addColumn(new MyTableColumn("City", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return CmpVstInfo[row].City ;
            }
        });
        addColumn(new MyTableColumn("Address", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return CmpVstInfo[row].Address;
            }
        });
        
        
         addColumn(new MyTableColumn("VisitorId", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public Object getValueAt(int row) {
                return CmpVstInfo[row].VisitorId;
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
       addColumn(new MyTableColumn("VName", 100, MyTableColumn.TYPE_STRING) {
           @Override
           public String getValueAt(int row) {
               return CmpVstInfo[row].VName;
           }
        });
        
        addColumn(new MyTableColumn("VDesignation", 100,MyTableColumn.TYPE_STRING) {

            @Override
            public Object getValueAt(int row) {
           
                //throw new UnsupportedOperationException("Not supported yet.");
            return CmpVstInfo[row].VDesing;
            }
            
        });
        
        
        
    }
    
    
    
    @Override
   public void exchange(int row1, int row2) {
        
        CmpVisitorClass temp=CmpVstInfo[row1];
        CmpVstInfo [row1]=CmpVstInfo[row2];
        CmpVstInfo[row2]=temp;
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
   
    public void setData(CmpVisitorClass[] cmps) {
        rowCount =  0;
        this.CmpVstInfo = cmps;
        if(cmps != null){
            rowCount = cmps.length;
        }
        fireTableDataChanged();
    }
      
    
}
