package mytable.celleditors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
/**
 * Class MyCheckBoxCellEditor
 * Created on Jun 30, 2012
 * @version 1.0.0
 * @author
 */
public class MyCheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
//    private JLabel editorLabel;
    private JCheckBox b;
    @Override
    public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, final int row, final int col) {
        if (value instanceof Boolean) {
//            editorLabel = new JLabel(((Boolean)value).booleanValue()?"Checked":"");
//            editorLabel.setBackground(Color.YELLOW);
//            editorLabel.setOpaque(true);
//            editorLabel.setToolTipText("To change value 'Dooble Click'");
//            editorLabel.addMouseListener(new MouseListener() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
////                    if(e.getClickCount() == 2){
////                        if(editorLabel.getText().equals("Checked")){
////                            editorLabel.setText("");
////                            table.setValueAt(false, row, col);
////                        }else{
////                            editorLabel.setText("Checked");
////                            table.setValueAt(true, row, col);
////                        }
////                    }
//                }
//                @Override
//                public void mousePressed(MouseEvent e) {
//                }
//                @Override
//                public void mouseReleased(MouseEvent e) {
//                    if(editorLabel.getText().equals("Checked")){
//                        editorLabel.setText("");
//                        table.setValueAt(false, row, col);
//                    }else{
//                        editorLabel.setText("Checked");
//                        table.setValueAt(true, row, col);
//                    }
//                }
//                @Override
//                public void mouseEntered(MouseEvent e) {
//                }
//                @Override
//                public void mouseExited(MouseEvent e) {
//                }
//            });
//            return editorLabel;
            b = new JCheckBox();
            b.setSelected(((Boolean)value).booleanValue());
            b.setBackground(Color.YELLOW);
            b.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    table.setValueAt(b.isSelected(), row, col);
                }
            });
            return b;
        }else{
            return new JLabel("");
        }
        
    }
    @Override
    @SuppressWarnings("BooleanConstructorCall")
    public Object getCellEditorValue() {
        return new Boolean(b.isSelected());
    }
}