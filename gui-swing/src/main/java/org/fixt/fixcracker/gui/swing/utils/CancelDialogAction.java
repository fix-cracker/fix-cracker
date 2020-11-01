/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fixt.fixcracker.gui.swing.utils;


import org.fixt.fixcracker.gui.swing.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


/**
 *
 * @author fixt
 */
public class CancelDialogAction extends AbstractAction{
    
    private Dialog dlg ;

    public CancelDialogAction(Dialog dlg) {
        super("Cancel");
        putValue(SMALL_ICON, Images.get(Images.CANCEL));
        this.dlg = dlg;        
    }    
    

    @Override
    public void actionPerformed(ActionEvent e) {
       if(dlg!=null){
           dlg.setVisible(false);
//           WindowListener[] wl=dlg.getWindowListeners();
//           for(WindowListener w: wl){
//               w.windowClosing(new WindowEvent(null, 0));
//           }
       }
    }

    public void setDlg(Dialog dlg) {
        this.dlg = dlg;
    }
    
    
    
    
    
    
}
