/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.AddCompany;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

    

    
    
/**
 *
 * @author nbpatil
 */
public class CommonTableModel extends MyTableModel{
private CommonClass[]CompanyDetails;

public CommonTableModel() throws Exception{
    
     addColumn(new MyTableColumn("CompanyID", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return CompanyDetails[row].CompanyID ;
            }
        });

     addColumn(new MyTableColumn("CompanyName", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return CompanyDetails[row].CompanyNm ;
            }
        });

     addColumn(new MyTableColumn("Address", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return CompanyDetails[row].CompanyAdd ;
            }
        });

//      addColumn(new MyTableColumn("CompanyType", 100, MyTableColumn.TYPE_STRING) {
//            @Override
//            public String getValueAt(int row) {
//                return CompanyDetails[row].CompanyType ;
//            }
//        });
//      
      addColumn(new MyTableColumn("City", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return CompanyDetails[row].CompanyCity ;
            }
        });
      addColumn(new MyTableColumn("VisitorId", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return CompanyDetails[row].VId ;
            }
        });
      
      addColumn(new MyTableColumn("Name", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return CompanyDetails[row].VName ;
            }
        });
      
      addColumn(new MyTableColumn("Designation", 100, MyTableColumn.TYPE_STRING) {
            @Override
            public String getValueAt(int row) {
                return CompanyDetails[row].Vdesig ;
            }
        });
}

    @Override
    public void exchange(int row1, int row2) {
        
        CommonClass temp=CompanyDetails[row1];
        CompanyDetails [row1]=CompanyDetails[row2];
        CompanyDetails[row2]=temp;
        
        
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    
  public void setData(CommonClass[] cmpVstinfo) {
        rowCount =  0;
        this.CompanyDetails = cmpVstinfo;
        if(cmpVstinfo != null){
            rowCount = cmpVstinfo.length;
        }
        fireTableDataChanged();
    } 
  
}
