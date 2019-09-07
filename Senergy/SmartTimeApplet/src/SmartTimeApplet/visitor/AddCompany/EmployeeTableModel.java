/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.AddCompany;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author Roop
 */
public class EmployeeTableModel extends MyTableModel{
    private CompanyClass[] EmployeeInfo;
    
    public EmployeeTableModel() throws Exception{
        
    
     addColumn(new MyTableColumn("CompanyID", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return EmployeeInfo[row].CompanyID ;
            }
        });
        addColumn(new MyTableColumn("Name", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return EmployeeInfo[row].Name ;
            }
        });
        
        addColumn(new MyTableColumn("City", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return EmployeeInfo[row].City ;
            }
        });
        addColumn(new MyTableColumn("Address", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return EmployeeInfo[row].Address;
            }
        });
        
//        addColumn(new MyTableColumn("Added", 100, MyTableColumn.TYPE_STRING) {
//            @Override
//            public String getValueAt(int row) {
//                return EmployeeInfo[row].Added;
//            }
//        });
    /*    
        addColumn(new MyTableColumn("ComType", 100,MyTableColumn.TYPE_STRING) {

            @Override
            public Object getValueAt(int row) {
           
                //throw new UnsupportedOperationException("Not supported yet.");
            return EmployeeInfo[row].ComType;
            }
            
        });
        
         addColumn(new MyTableColumn("State", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public Object getValueAt(int row) {
                return EmployeeInfo[row].State;
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
         
          addColumn(new MyTableColumn("Pin", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public Object getValueAt(int row) {
                return EmployeeInfo[row].Pin;
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
          
           addColumn(new MyTableColumn("PhoneNum", rowCount, rowCount) {

            @Override
            public Object getValueAt(int row) {
                return EmployeeInfo[row].PhoneNo1;
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        * 
        */
    }
    

    
    
    @Override
    public void exchange(int row1, int row2) {
        
        CompanyClass temp=EmployeeInfo[row1];
        EmployeeInfo [row1]=EmployeeInfo[row2];
        EmployeeInfo[row2]=temp;
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
   
    public void setData(CompanyClass[] cmps) {
        rowCount =  0;
        this.EmployeeInfo = cmps;
        if(cmps != null){
            rowCount = cmps.length;
        }
        fireTableDataChanged();
    }
      
    
}
