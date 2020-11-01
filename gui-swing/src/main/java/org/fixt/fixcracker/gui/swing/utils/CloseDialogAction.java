
package org.fixt.fixcracker.gui.swing.utils;


import org.fixt.fixcracker.gui.swing.Images;

import java.awt.Dialog;


/**
 *
 * @author fixt
 */
public class CloseDialogAction extends CancelDialogAction {

    public CloseDialogAction(Dialog dlg) {
        super(dlg);
        putValue(NAME, "Close");
        putValue(SMALL_ICON, Images.get(Images.CLOSE));
    }
    
}
