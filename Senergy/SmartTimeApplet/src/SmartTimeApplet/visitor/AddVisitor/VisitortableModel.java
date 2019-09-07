/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.AddVisitor;

import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 *
 * @author nbpatil
 */
class VisitortableModel extends MyTableModel {
    
    private VisitorInfoClass[] AddNewVisitor;
    
    public VisitortableModel()throws Exception{
    
   addColumn(new MyTableColumn("VisitorId", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return AddNewVisitor[row].VisitorId;
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
     addColumn(new MyTableColumn("PCompId", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return AddNewVisitor[row].PCOM_ID ; }
        });
        
        addColumn(new MyTableColumn("VisitorName", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return AddNewVisitor[row].VisitorName ; }
        });
    addColumn(new MyTableColumn("Occupation", 100, MyTableColumn.TYPE_STRING) {

            @Override
            public String getValueAt(int row) {
                return AddNewVisitor[row].Occupation;
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    
    
    }

     void setData(VisitorInfoClass[] visitors) {
        
        rowCount =  0;
        this.AddNewVisitor = visitors;
        if(visitors != null){
            rowCount = visitors.length;
        }
        fireTableDataChanged();
    
      }

    @Override
    public void exchange(int row1, int row2) {
        
        
        VisitorInfoClass temp=AddNewVisitor[row1];
        AddNewVisitor [row1]=AddNewVisitor[row2];
        AddNewVisitor[row2]=temp;
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
    
}
