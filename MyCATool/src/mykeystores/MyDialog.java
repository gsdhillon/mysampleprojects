/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mykeystores;

import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class MyDialog {
    public static boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(null,msg,"pl confirm",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
