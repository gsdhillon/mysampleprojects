/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TableModels;

import javax.swing.JPopupMenu;
import lib.gui.table.MyTable;

/**
 *
 * @author GAURAV
 */
public class MyList extends JPopupMenu {
    public MyTable table;
    public DHeadTableModel tablemodel;
    
    public MyList() throws Exception{
        super();
        tablemodel=new DHeadTableModel();
        table=new MyTable(tablemodel);
        //JMenuItem list = new JMenuItem(table);
        this.add(table.getGUI());
        //this.setSize(200, 300);
        //this.setVisible(true);
    }
    
//    public static void main(String[] args) throws Exception{
//        new MyList();
//    }
}
